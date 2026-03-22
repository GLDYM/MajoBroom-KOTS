package dev.polaris_light.majobroom.client.renderer.armor;

import dev.polaris_light.majobroom.MajoBroom;
import dev.polaris_light.majobroom.item.armor.MajoStockingItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class MajoStockingRenderer extends GeoArmorRenderer<MajoStockingItem> {
    public MajoStockingRenderer() {
        super(new DefaultedItemGeoModel<>(
            ResourceLocation.fromNamespaceAndPath(MajoBroom.MODID, "armor/majo_stocking")
        ));
    }
}
