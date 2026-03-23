package dev.polaris_light.majobroom.client.renderer.armor;

import dev.polaris_light.majobroom.item.armor.MajoHatItem;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.Map;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;

/**
 * 魔女帽子的 GeckoLib 渲染器
 */
public class MajoHatRenderer extends GeoArmorRenderer<MajoHatItem, MajoHatRenderer.HatRenderState> {
    public MajoHatRenderer(MajoHatItem item) {
        super(item);
    }

    public static class HatRenderState extends HumanoidRenderState implements GeoRenderState {
        private final Map<DataTicket<?>, Object> dataMap = new Reference2ObjectOpenHashMap<>();

        @Override
        public Map<DataTicket<?>, Object> getDataMap() {
            return this.dataMap;
        }
    }
}
