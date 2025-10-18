package com.rcell.majobroom.init;

import com.rcell.majobroom.MajoBroom;
import com.rcell.majobroom.entity.BroomEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    private ModEntities() {}

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MajoBroom.MODID);

    public static final RegistryObject<EntityType<BroomEntity>> BROOM = ENTITIES.register(
            "broom",
            () -> EntityType.Builder
                    .of(BroomEntity::new, MobCategory.MISC)
                    .sized(1.0F, 0.5F)
                    .clientTrackingRange(10)
                    .build("broom")
    );

    public static void register(IEventBus modEventBus) {
        ENTITIES.register(modEventBus);
    }
}



