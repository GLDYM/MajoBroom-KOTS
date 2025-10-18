package com.rcell.majobroom.network.packet;

import com.rcell.majobroom.compat.CompatManager;
import com.rcell.majobroom.entity.BroomEntity;
import com.rcell.majobroom.init.ModEntities;
import com.rcell.majobroom.init.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public record BroomSummonPacket() {
    public static void encode(@Nonnull BroomSummonPacket msg, @Nonnull FriendlyByteBuf buf) {}
    public static @Nonnull BroomSummonPacket decode(@Nonnull FriendlyByteBuf buf) { return new BroomSummonPacket(); }

    public static void handle(@Nonnull BroomSummonPacket msg, @Nonnull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            if (player.isPassenger() && player.getVehicle() instanceof BroomEntity broom) {
                // 设置允许下马标记（R键快速回收也需要授权）
                broom.setAllowDismount(true);
                player.stopRiding();
                broom.setAllowDismount(false);
                
                // 生成回收粒子效果
                BroomEntity.spawnBroomParticles(player.level(), broom.getX(), broom.getY(), broom.getZ());
                
                // 返还扫帚物品并保存配置
                ItemStack stack = new ItemStack(ModItems.BROOM.get());
                broom.saveConfigToItemStack(stack);  // 保存配置到物品NBT
                
                // 尝试放回来源背包或原版物品栏
                java.util.UUID sourceBackpackUUID = broom.getSourceBackpackUUID();
                boolean stored = false;
                
                if (sourceBackpackUUID != null) {
                    // 如果有来源背包，优先放回原始背包
                    stored = CompatManager.storeItemToBackpack(player, sourceBackpackUUID, stack);
                }
                
                if (!stored) {
                    // 如果没有来源背包或放回失败，直接放回原版物品栏
                    // 注意：这里不使用 CompatManager.storeItem()，因为它会优先尝试背包
                    // 我们希望保持"从哪里召唤就回到哪里"的原则
                    if (!player.getInventory().add(stack)) {
                        // 如果物品栏也满了，就掉落在地上
                        player.drop(stack, false);
                    }
                }
                broom.discard();
                player.level().playSound(null, player.blockPosition(), net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_ELYTRA, SoundSource.PLAYERS, 1.0F, 1.0F);
                return;
            }

            // 先检查玩家是否有扫帚
            CompatManager.FindItemResult findResult = CompatManager.findItemInAllInventories(player, ModItems.BROOM.get());
            if (findResult.stack.isEmpty()) {
                return;  // 没有扫帚，直接返回
            }

            Level level = player.level();
            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();
            
            BroomEntity broom = new BroomEntity(ModEntities.BROOM.get(), level);
            broom.setPos(x, y, z);
            broom.setYRot(player.getYRot());
            
            // 使用兼容管理器移除扫帚，会正确处理背包同步
            CompatManager.FindItemResult removeResult = CompatManager.removeItemFromAllInventories(player, ModItems.BROOM.get());
            
            // 从物品NBT加载配置
            if (!removeResult.stack.isEmpty()) {
                broom.loadConfigFromItemStack(removeResult.stack);
            }
            
            // 记录来源背包 UUID（如果是从背包召唤的）
            broom.setSourceBackpackUUID(removeResult.sourceBackpackUUID);
            
            level.addFreshEntity(broom);
            player.startRiding(broom);
            
            // 生成召唤粒子效果
            BroomEntity.spawnBroomParticles(level, x, y, z);
            
            level.playSound(null, player.blockPosition(), net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_ELYTRA, SoundSource.PLAYERS, 1.0F, 1.0F);
        });
        ctx.get().setPacketHandled(true);
    }
}


