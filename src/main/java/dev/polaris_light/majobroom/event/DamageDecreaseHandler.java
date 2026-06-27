package dev.polaris_light.majobroom.event;

import dev.polaris_light.majobroom.MajoBroom;
import dev.polaris_light.majobroom.init.ModEffects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = MajoBroom.MODID)
public class DamageDecreaseHandler {
    @SubscribeEvent
    public static void onLivingEntityDamagedPre(LivingDamageEvent.Pre event){
        var effectInstance = event.getEntity().getEffect(ModEffects.DECREASE_DAMAGE_EFFECT);
        if( effectInstance != null){
            event.setNewDamage(Math.max(event.getNewDamage()-effectInstance.getAmplifier()*2,0));
        }
    }
}
