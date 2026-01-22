package com.rcell.majobroom;

import com.mojang.logging.LogUtils;
import com.rcell.majobroom.compat.CompatManager;
import com.rcell.majobroom.config.ServerConfig;
import com.rcell.majobroom.init.ModEntities;
import com.rcell.majobroom.init.ModItems;
import com.rcell.majobroom.network.ModNetwork;
import com.rcell.majobroom.init.ModCreativeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.ModLoadingContext;
// removed unused imports
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MajoBroom.MODID)
public class MajoBroom
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "majobroom";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // 移除模板示例，使用自有注册类

    public MajoBroom(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        ModEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // 可在此注册语言/标签/数据生成事件监听

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        // 使用SERVER类型：配置存储在服务端并同步到客户端
        context.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);

        // Initialize GeckoLib (client+server safe)
        GeckoLib.initialize();
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(ModNetwork::register);
        
        // 初始化模组兼容
        event.enqueueWork(CompatManager::init);
    }
    
    private void clientSetup(final FMLClientSetupEvent event)
    {
        // 注册物品 Tooltip（在物品注册完成后）
        event.enqueueWork(() -> {
            ModItems.registerTooltips();
            LOGGER.info("Item tooltips registered!");
        });
    }

    // 可选：添加你的内容到创造模式物品栏

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // 客户端初始化（渲染注册请使用 EntityRenderersEvent.RegisterRenderers）
        }
    }
}

