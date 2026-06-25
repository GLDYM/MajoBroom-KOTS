package dev.polaris_light.majobroom.item;

import dev.polaris_light.majobroom.init.ModEntities;
import dev.polaris_light.majobroom.entity.BroomEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class BroomItem extends Item {
    public BroomItem(Properties properties) { super(properties); }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;

        if (!level.isClientSide()) {
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 1.0;
            double z = pos.getZ() + 0.5;
            
            BroomEntity broom = new BroomEntity(ModEntities.BROOM.get(), level);
            broom.setPos(x, y, z);
            broom.setYRot(player.getYRot());
            
            // 从物品NBT加载配置
            broom.loadConfigFromItemStack(context.getItemInHand());
            
            level.addFreshEntity(broom);
            
            // 播放音效和粒子效果
            level.playSound(null, pos, net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_ELYTRA.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
            BroomEntity.spawnBroomParticles(level, x, y, z);
            
            if (!player.getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
        }
        return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
    }
}



