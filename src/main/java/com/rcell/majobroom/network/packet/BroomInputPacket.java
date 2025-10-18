package com.rcell.majobroom.network.packet;

import com.rcell.majobroom.entity.BroomEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * 扫帚输入控制网络包（完整的6方向输入）
 * 使用位标志压缩6个布尔值到1个字节
 */
public record BroomInputPacket(int entityId, byte flags) {
    
    // flags 位标志（7位）:
    // bit 0 (0x01): 左转 (A)
    // bit 1 (0x02): 右转 (D)
    // bit 2 (0x04): 前进 (W)
    // bit 3 (0x08): 后退 (S)
    // bit 4 (0x10): 上升 (Space/Jump)
    // bit 5 (0x20): 下降 (Ctrl)
    // bit 6 (0x40): 刹车 (Shift)
    
    public static void encode(@Nonnull BroomInputPacket msg, @Nonnull FriendlyByteBuf buf) {
        buf.writeVarInt(msg.entityId);
        buf.writeByte(msg.flags);
    }
    
    public static @Nonnull BroomInputPacket decode(@Nonnull FriendlyByteBuf buf) {
        return new BroomInputPacket(buf.readVarInt(), buf.readByte());
    }
    
    public static void handle(@Nonnull BroomInputPacket msg, @Nonnull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) {
                System.out.println("[输入包处理] 错误: 发送者为null");
                return;
            }
            
            // 调试日志减少频率（原版每tick发包会很多）
            // System.out.println(String.format(
            //     "[输入包接收-服务端] 玩家:%s 实体ID:%d flags:0x%02X",
            //     player.getName().getString(), msg.entityId, msg.flags
            // ));
            
            // 获取目标实体
            Entity target = player.level().getEntity(msg.entityId);
            if (!(target instanceof BroomEntity broom)) {
                System.out.println(String.format(
                    "[输入包处理] 错误: 实体ID:%d 不是扫帚实体 (实际类型:%s)",
                    msg.entityId, target == null ? "null" : target.getClass().getSimpleName()
                ));
                return;
            }
            
            // 验证：玩家必须正在骑乘该扫帚
            // 注意：当玩家按shift下扫帚时，可能会收到最后几个已发送的包
            // 这是正常的网络时序问题，静默忽略即可
            Entity vehicle = player.getVehicle();
            if (vehicle == null || !vehicle.equals(broom)) {
                // 静默返回，这是正常的dismount时序
                return;
            }
            
            // 解析所有输入标志
            boolean left = (msg.flags & 0x01) != 0;
            boolean right = (msg.flags & 0x02) != 0;
            boolean forward = (msg.flags & 0x04) != 0;
            boolean backward = (msg.flags & 0x08) != 0;
            boolean up = (msg.flags & 0x10) != 0;
            boolean down = (msg.flags & 0x20) != 0;
            boolean brake = (msg.flags & 0x40) != 0;  // bit 6: 刹车
            
            
            // 应用所有输入到扫帚
            broom.setInput(left, right, forward, backward, up, down, brake);
        });
        ctx.get().setPacketHandled(true);
    }
}

