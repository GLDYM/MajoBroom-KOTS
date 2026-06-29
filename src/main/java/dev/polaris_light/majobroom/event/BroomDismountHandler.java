package dev.polaris_light.majobroom.event;

import dev.polaris_light.majobroom.MajoBroom;
import dev.polaris_light.majobroom.entity.BroomEntity;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 扫帚下马事件处理器
 * 阻止玩家通过shift键快速下马，必须长按1秒
 */
@Mod.EventBusSubscriber(modid = MajoBroom.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BroomDismountHandler {

    @SubscribeEvent
    public static void onEntityDismount(EntityMountEvent event) {
        if (!event.isDismounting()) {
            return;
        }

        if (!(event.getEntityBeingMounted() instanceof BroomEntity broom)) {
            return;
        }

        // Allow vanilla-forced dismounts during death/removal so rider state can be cleaned up.
        if (!event.getEntityMounting().isAlive() || event.getEntityMounting().isRemoved()) {
            return;
        }

        if (!event.getLevel().isClientSide && !broom.isAllowDismount()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity().getVehicle() instanceof BroomEntity broom)) {
            return;
        }

        // Death should always break the vehicle link, otherwise respawn can inherit a stale mount.
        broom.setAllowDismount(true);
        event.getEntity().stopRiding();
        broom.setAllowDismount(false);
    }
}
