package dev.polaris_light.majobroom.event;

import dev.polaris_light.majobroom.MajoBroom;
import dev.polaris_light.majobroom.entity.BroomEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

/**
 * Handles broom dismount rules.
 */
@EventBusSubscriber(modid = MajoBroom.MODID)
public class BroomDismountHandler {

    @SubscribeEvent
    public static void onEntityDismount(EntityMountEvent event) {
        if (!event.isDismounting()) {
            return;
        }

        if (!(event.getEntityBeingMounted() instanceof BroomEntity broom)) {
            return;
        }

        if (!event.getEntityMounting().isAlive() || event.getEntityMounting().isRemoved()) {
            return;
        }

        if (!event.getLevel().isClientSide() && !broom.isAllowDismount()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity().getVehicle() instanceof BroomEntity broom)) {
            return;
        }

        broom.setAllowDismount(true);
        event.getEntity().stopRiding();
        broom.setAllowDismount(false);
    }
}
