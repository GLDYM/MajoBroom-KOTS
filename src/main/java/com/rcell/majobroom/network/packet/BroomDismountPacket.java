package com.rcell.majobroom.network.packet;

import com.rcell.majobroom.entity.BroomEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * 扫帚下马数据包
 * 客户端 -> 服务端
 * 
 * 当玩家长按shift键超过1秒时，客户端发送此包请求下马
 */
public record BroomDismountPacket(int entityId) {
    
    public void encode(@Nonnull FriendlyByteBuf buf) {
        buf.writeVarInt(entityId);
    }
    
    public static BroomDismountPacket decode(@Nonnull FriendlyByteBuf buf) {
        return new BroomDismountPacket(buf.readVarInt());
    }
    
    public static void handle(@Nonnull BroomDismountPacket msg, @Nonnull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) {
                return;
            }
            
            // 验证玩家确实在骑乘该扫帚
            Entity vehicle = player.getVehicle();
            if (vehicle == null) {
                return;
            }
            
            // 验证实体ID匹配
            Entity target = player.level().getEntity(msg.entityId);
            if (!(target instanceof BroomEntity broom) || !target.equals(vehicle)) {
                return;
            }
            
            // 设置允许下马标记，然后执行下马
            broom.setAllowDismount(true);
            player.stopRiding();
            // 下马后立即重置标记（以防下次意外）
            broom.setAllowDismount(false);
        });
        ctx.get().setPacketHandled(true);
    }
}

