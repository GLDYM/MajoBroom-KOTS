package com.rcell.majobroom.network;

import com.rcell.majobroom.MajoBroom;
import com.rcell.majobroom.network.packet.BroomInputPacket;
import com.rcell.majobroom.network.packet.BroomSummonPacket;
import com.rcell.majobroom.network.packet.BroomConfigPacket;
import com.rcell.majobroom.network.packet.BroomDismountPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkDirection;

/**
 * 网络通信管理
 * 注册所有自定义网络包
 */
public final class ModNetwork {
    private ModNetwork() {}

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(MajoBroom.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;
    private static int id() { return packetId++; }

    public static void register() {
        // 输入控制包（客户端→服务端，包含完整的6方向输入）
        CHANNEL.messageBuilder(BroomInputPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(BroomInputPacket::encode)
                .decoder(BroomInputPacket::decode)
                .consumerMainThread(BroomInputPacket::handle)
                .add();

        // 召唤/收回包（客户端→服务端）
        CHANNEL.messageBuilder(BroomSummonPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(BroomSummonPacket::encode)
                .decoder(BroomSummonPacket::decode)
                .consumerMainThread(BroomSummonPacket::handle)
                .add();

        // 配置同步包（客户端→服务端）
        CHANNEL.messageBuilder(BroomConfigPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(BroomConfigPacket::toBytes)
                .decoder(BroomConfigPacket::new)
                .consumerMainThread(BroomConfigPacket::handle)
                .add();

        // 下马包（客户端→服务端）
        CHANNEL.messageBuilder(BroomDismountPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(BroomDismountPacket::encode)
                .decoder(BroomDismountPacket::decode)
                .consumerMainThread(BroomDismountPacket::handle)
                .add();
    }

    public static void sendToServer(Object packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendToPlayer(Object packet, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    public static void sendToAllTracking(Object packet, Entity entity) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), packet);
    }
}



