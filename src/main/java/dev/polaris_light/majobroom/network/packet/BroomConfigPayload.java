package dev.polaris_light.majobroom.network.packet;

import dev.polaris_light.majobroom.MajoBroom;
import dev.polaris_light.majobroom.entity.BroomEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Objects;

/**
 * 扫帚配置同步数据包
 * 客户端 -> 服务端：同步扫帚的个性化配置
 */
public record BroomConfigPayload(
    int broomEntityId,
    int perspectiveModeOrdinal,
    boolean sidewaysSitting,
    boolean autoHover,
    int speedPercent
) implements CustomPacketPayload {
    
    public static final CustomPacketPayload.Type<BroomConfigPayload> TYPE = 
        new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MajoBroom.MODID, "broom_config"));
    
    public static final StreamCodec<ByteBuf, BroomConfigPayload> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT,
        BroomConfigPayload::broomEntityId,
        ByteBufCodecs.INT,
        BroomConfigPayload::perspectiveModeOrdinal,
        ByteBufCodecs.BOOL,
        BroomConfigPayload::sidewaysSitting,
        ByteBufCodecs.BOOL,
        BroomConfigPayload::autoHover,
        ByteBufCodecs.INT,
        BroomConfigPayload::speedPercent,
        BroomConfigPayload::fromCodec
    );

    private static BroomConfigPayload fromCodec(Integer broomEntityId,
                                                Integer perspectiveModeOrdinal,
                                                Boolean sidewaysSitting,
                                                Boolean autoHover,
                                                Integer speedPercent) {
        return new BroomConfigPayload(
            Objects.requireNonNull(broomEntityId, "broomEntityId"),
            Objects.requireNonNull(perspectiveModeOrdinal, "perspectiveModeOrdinal"),
            Objects.requireNonNull(sidewaysSitting, "sidewaysSitting"),
            Objects.requireNonNull(autoHover, "autoHover"),
            Objects.requireNonNull(speedPercent, "speedPercent")
        );
    }
    
    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    
    public static void handle(BroomConfigPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) {
                return;
            }

            // 查找扫帚实体
            Entity entity = player.level().getEntity(payload.broomEntityId);
            if (!(entity instanceof BroomEntity broom)) {
                return;
            }

            // 权限检查：玩家必须正在骑乘这个扫帚
            if (player.getVehicle() != broom) {
                return;
            }

            // 应用配置
            broom.setPerspectiveMode(dev.polaris_light.majobroom.common.PerspectiveMode.fromOrdinal(payload.perspectiveModeOrdinal));
            broom.setSidewaysSitting(payload.sidewaysSitting);
            broom.setAutoHover(payload.autoHover);
            broom.setSpeedPercent(payload.speedPercent);
        });
    }
}

