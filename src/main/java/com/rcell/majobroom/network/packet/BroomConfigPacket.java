package com.rcell.majobroom.network.packet;

import com.rcell.majobroom.entity.BroomEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * 扫帚配置同步数据包
 * 客户端 -> 服务端：同步扫帚的个性化配置
 */
public class BroomConfigPacket {
    private final int broomEntityId;
    private final int perspectiveModeOrdinal;  // PerspectiveMode的序号
    private final boolean sidewaysSitting;
    private final boolean autoHover;
    private final int speedPercent;

    public BroomConfigPacket(int broomEntityId, int perspectiveModeOrdinal, boolean sidewaysSitting, 
                            boolean autoHover, int speedPercent) {
        this.broomEntityId = broomEntityId;
        this.perspectiveModeOrdinal = perspectiveModeOrdinal;
        this.sidewaysSitting = sidewaysSitting;
        this.autoHover = autoHover;
        this.speedPercent = speedPercent;
    }

    /**
     * 从网络缓冲区解码
     */
    public BroomConfigPacket(FriendlyByteBuf buf) {
        this.broomEntityId = buf.readInt();
        this.perspectiveModeOrdinal = buf.readInt();
        this.sidewaysSitting = buf.readBoolean();
        this.autoHover = buf.readBoolean();
        this.speedPercent = buf.readInt();
    }

    /**
     * 编码到网络缓冲区
     */
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(broomEntityId);
        buf.writeInt(perspectiveModeOrdinal);
        buf.writeBoolean(sidewaysSitting);
        buf.writeBoolean(autoHover);
        buf.writeInt(speedPercent);
    }

    /**
     * 处理数据包（服务端）
     */
    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            // 查找扫帚实体
            Entity entity = player.level().getEntity(broomEntityId);
            if (!(entity instanceof BroomEntity broom)) {
                return;
            }

            // 权限检查：玩家必须正在骑乘这个扫帚
            if (player.getVehicle() != broom) {
                return;
            }

            // 应用配置
            broom.setPerspectiveMode(com.rcell.majobroom.common.PerspectiveMode.fromOrdinal(perspectiveModeOrdinal));
            broom.setSidewaysSitting(sidewaysSitting);
            broom.setAutoHover(autoHover);
            broom.setSpeedPercent(speedPercent);
        });
        return true;
    }
}

