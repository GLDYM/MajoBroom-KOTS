package dev.polaris_light.majobroom.client;

import dev.polaris_light.majobroom.MajoBroom;
import dev.polaris_light.majobroom.client.renderer.entity.BroomGeoRenderer;
import dev.polaris_light.majobroom.init.ModEntities;
import dev.polaris_light.majobroom.client.tooltip.TooltipModifier;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

/**
 * 客户端事件处理
 * 注册实体渲染器、按键绑定和 Tooltip
 */
@EventBusSubscriber(modid = MajoBroom.MODID, value = Dist.CLIENT)
public final class ClientEvents {
    private ClientEvents() {}

    /**
     * 注册实体渲染器
     */
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.BROOM.get(), BroomGeoRenderer::new);
    }

    /**
     * 注册按键映射
     */
    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        dev.polaris_light.majobroom.client.input.KeyBindings.register(event);
    }

    /**
     * Tooltip 事件监听器
     * 在游戏事件总线上监听
     */
    @EventBusSubscriber(modid = MajoBroom.MODID, value = Dist.CLIENT)
    public static class GameEventHandler {
        
        /**
         * 添加物品 Tooltip
         */
        @SubscribeEvent
        public static void onItemTooltip(ItemTooltipEvent event) {
            if (event.getEntity() == null) {
                return;
            }

            Item item = event.getItemStack().getItem();
            TooltipModifier modifier = TooltipModifier.get(item);
            if (modifier != null && modifier != TooltipModifier.EMPTY) {
                modifier.modify(event);
            }
        }
    }
}