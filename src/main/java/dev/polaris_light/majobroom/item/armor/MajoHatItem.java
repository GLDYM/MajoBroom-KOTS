package dev.polaris_light.majobroom.item.armor;

import dev.polaris_light.majobroom.client.renderer.armor.MajoHatRenderer;
import dev.polaris_light.majobroom.config.ServerConfig;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import com.geckolib.animatable.GeoItem;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.renderer.GeoArmorRenderer;
import com.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

/**
 *  - GeckoLib?
 */
public class MajoHatItem extends Item implements GeoItem {
    private static final int EFFECT_DURATION = 340;
    private static final int EFFECT_CHECK_INTERVAL_TICKS = 80;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public MajoHatItem(Properties properties) {
        super(properties);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?, ?> renderer;

            @Override
            public GeoArmorRenderer<?, ?> getGeoArmorRenderer(ItemStack itemStack, EquipmentSlot equipmentSlot) {
                if (this.renderer == null)
                    this.renderer = new MajoHatRenderer(MajoHatItem.this);

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // 
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void inventoryTick(ItemStack itemstack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        super.inventoryTick(itemstack, level, entity, slot);
        if (entity instanceof LivingEntity livingEntity && slot == EquipmentSlot.HEAD && ServerConfig.armorBless) {
            if (level.getGameTime() % EFFECT_CHECK_INTERVAL_TICKS == 0) {
                refreshEffectIfNeeded(livingEntity, MobEffects.REGENERATION, 3);
                refreshEffectIfNeeded(livingEntity, MobEffects.WATER_BREATHING, 0);
            }
        }
        if (itemstack.isDamaged() && ServerConfig.armorImmortal) {
            itemstack.setDamageValue(0);
        }
    }

    private static void refreshEffectIfNeeded(LivingEntity entity, Holder<MobEffect> effect, int amplifier) {
        entity.addEffect(new MobEffectInstance(effect, EFFECT_DURATION, amplifier, false, false));
    }
}
