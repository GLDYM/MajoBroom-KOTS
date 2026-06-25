package dev.polaris_light.majobroom.client.renderer.armor;

import dev.polaris_light.majobroom.item.armor.MajoStockingItem;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.GeoArmorRenderer;

import java.util.Map;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;

public class MajoStockingRenderer extends GeoArmorRenderer<MajoStockingItem, MajoStockingRenderer.StockingRenderState> {
    public MajoStockingRenderer(MajoStockingItem item) {
        super(item);
    }

    public static class StockingRenderState extends HumanoidRenderState implements GeoRenderState {
        private final Map<DataTicket<?>, Object> dataMap = new Reference2ObjectOpenHashMap<>();

        @Override
        public Map<DataTicket<?>, Object> getDataMap() {
            return this.dataMap;
        }
    }
}
