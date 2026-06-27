package dev.polaris_light.majobroom;

import com.mojang.logging.LogUtils;
import dev.polaris_light.majobroom.compat.CompatManager;
import dev.polaris_light.majobroom.config.ServerConfig;
import dev.polaris_light.majobroom.init.ModEffects;
import dev.polaris_light.majobroom.init.ModEntities;
import dev.polaris_light.majobroom.init.ModItems;
import dev.polaris_light.majobroom.init.ModCreativeTabs;
import dev.polaris_light.majobroom.item.armor.MajoArmorMaterials;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.ModContainer;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(MajoBroom.MODID)
public class MajoBroom
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "majobroom";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public MajoBroom(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        MajoArmorMaterials.ARMOR_MATERIALS.register(modEventBus);
        ModEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModEffects.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        // NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        // 使用SERVER类型：配置存储在服务端并同步到客户端
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // 网络注册已移至ModNetwork的@EventBusSubscriber
        // event.enqueueWork(ModNetwork::register);
        
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

//    // You can use SubscribeEvent and let the Event Bus discover methods to call
//    @SubscribeEvent
//    public void onServerStarting(ServerStartingEvent event)
//    {
//        // Do something when the server starts
//        LOGGER.info("HELLO from server starting");
//    }

//    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
//    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
//    public static class ClientModEvents
//    {
//        @SubscribeEvent
//        public static void onClientSetup(FMLClientSetupEvent event)
//        {
//            // 客户端初始化（渲染注册请使用 EntityRenderersEvent.RegisterRenderers）
//        }
//    }
}
