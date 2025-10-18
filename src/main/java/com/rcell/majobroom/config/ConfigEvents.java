package com.rcell.majobroom.config;
import com.rcell.majobroom.MajoBroom;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = MajoBroom.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ConfigEvents {
    private ConfigEvents() {}

    @SubscribeEvent
    public static void onConfigLoading(ModConfigEvent.Loading event) {
        if (event.getConfig().getModId().equals(MajoBroom.MODID)) {
            apply();
        }
    }

    @SubscribeEvent
    public static void onConfigReloading(ModConfigEvent.Reloading event) {
        if (event.getConfig().getModId().equals(MajoBroom.MODID)) {
            apply();
        }
    }

    private static void apply() {
        // 将配置值烘焙到缓存变量中
        // 这是 Forge 推荐的做法：避免每次使用都调用 .get()
        ServerConfig.bake();
    }
}


