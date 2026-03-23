package dev.polaris_light.majobroom.item.armor;

import dev.polaris_light.majobroom.client.renderer.armor.MajoStockingRenderer;
import dev.polaris_light.majobroom.config.ServerConfig;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import org.jspecify.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class MajoStockingItem extends Item implements GeoItem {
    private static final int EFFECT_DURATION = 340;
    private static final int EFFECT_REFRESH_THRESHOLD = 40;
    private static final int EFFECT_CHECK_INTERVAL_TICKS = 80;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public MajoStockingItem(Properties properties) {
        super(properties);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?, ?> renderer;

            @Override
            public GeoArmorRenderer<?, ?> getGeoArmorRenderer(ItemStack itemStack, EquipmentSlot equipmentSlot) {
                if (this.renderer == null)
                    this.renderer = new MajoStockingRenderer(MajoStockingItem.this);

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void inventoryTick(ItemStack itemstack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(itemstack, level, entity, slot);
        if (entity instanceof LivingEntity livingEntity && slot == EquipmentSlot.LEGS && ServerConfig.armorBless) {
            if (level.getGameTime() % EFFECT_CHECK_INTERVAL_TICKS == 0) {
                refreshEffectIfNeeded(livingEntity, MobEffects.HASTE, 1);
                refreshEffectIfNeeded(livingEntity, MobEffects.STRENGTH, 2);
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