package dev.polaris_light.majobroom.client.renderer.model;

import dev.polaris_light.majobroom.MajoBroom;
import dev.polaris_light.majobroom.entity.BroomEntity;
import net.minecraft.resources.Identifier;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

/**
 * 扫帚的 GeckoLib 模型
 * 定义模型和纹理资源的位置（不使用动画系统）
 */
public class BroomGeoModel extends DefaultedEntityGeoModel<BroomEntity> {
    public BroomGeoModel() {
        super(Identifier.fromNamespaceAndPath(MajoBroom.MODID, "broom"));
    }
}


