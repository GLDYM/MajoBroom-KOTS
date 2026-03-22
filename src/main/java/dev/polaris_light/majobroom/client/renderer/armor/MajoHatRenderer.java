package dev.polaris_light.majobroom.client.renderer.armor;

import dev.polaris_light.majobroom.MajoBroom;
import dev.polaris_light.majobroom.item.armor.MajoHatItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

/**
 * 魔女帽子的 GeckoLib 渲染器
 */
public class MajoHatRenderer extends GeoArmorRenderer<MajoHatItem> {
    public MajoHatRenderer() {
        super(new DefaultedItemGeoModel<>(
            ResourceLocation.fromNamespaceAndPath(MajoBroom.MODID, "armor/majo_hat")
        ));
    }
}
