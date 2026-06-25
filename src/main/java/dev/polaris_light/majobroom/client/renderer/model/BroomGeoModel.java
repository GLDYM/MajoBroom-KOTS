package dev.polaris_light.majobroom.client.renderer.model;

import dev.polaris_light.majobroom.MajoBroom;
import dev.polaris_light.majobroom.entity.BroomEntity;
import net.minecraft.resources.Identifier;
import com.geckolib.model.DefaultedEntityGeoModel;

/**
 * ?GeckoLib 
 * ?
 */
public class BroomGeoModel extends DefaultedEntityGeoModel<BroomEntity> {
    public BroomGeoModel() {
        super(Identifier.fromNamespaceAndPath(MajoBroom.MODID, "broom"));
    }
}


