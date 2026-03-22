package dev.polaris_light.majobroom.item.armor;

import dev.polaris_light.majobroom.MajoBroom;
import dev.polaris_light.majobroom.client.renderer.armor.MajoHatRenderer;
import dev.polaris_light.majobroom.config.ServerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import com.google.common.collect.Iterables;

/**
 * 魔女帽子 - 使用GeckoLib动画的装备
 */
public class MajoHatItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public MajoHatItem(Properties properties) {
        super(
            MajoArmorMaterials.MAJO_CLOTH,
            ArmorItem.Type.HELMET,
            properties
        );
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public <T extends LivingEntity> HumanoidModel<T> getGeoArmorRenderer(T livingEntity, 
                                                                   ItemStack itemStack, 
                                                                   EquipmentSlot equipmentSlot, 
                                                                   HumanoidModel<T> original) {
                if (this.renderer == null)
                    this.renderer = new MajoHatRenderer();

                this.renderer.prepForRender(
                    livingEntity, 
                    itemStack, 
                    equipmentSlot, 
                    original,
                    Minecraft.getInstance().renderBuffers().bufferSource(),
                    0.0F,
                    0.0F,
                    0.0F,
                    0.0F,
                    0.0F
                );

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // 帽子暂时不需要动画控制器
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);
        if (entity instanceof LivingEntity livingEntity && Iterables.contains(livingEntity.getArmorSlots(), itemstack)) {
            if (ServerConfig.armorBless) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 240, 3, false, false));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 240, 3, false, false));
            }
        }
	    if (itemstack.isDamaged() && ServerConfig.armorImmortal) {
		    itemstack.setDamageValue(0);
	    }
    }

    @Override
    public int getDefense() {
        return ServerConfig.armorOverpower ? 6 : this.material.value().getDefense(this.type);
    }

    @Override
    public float getToughness() {
        return ServerConfig.armorOverpower ? 10.0F : this.material.value().toughness();
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();

        // 护甲值
        builder.add(
            Attributes.ARMOR,
            new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(MajoBroom.MODID, "majo_hat_armor"),
                ServerConfig.armorOverpower ? 6.0 : this.getDefense(),
                AttributeModifier.Operation.ADD_VALUE
            ),
            EquipmentSlotGroup.HEAD
        );

        // 护甲韧性
        builder.add(
            Attributes.ARMOR_TOUGHNESS,
            new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(MajoBroom.MODID, "majo_hat_toughness"),
                ServerConfig.armorOverpower ? 10.0F : this.getToughness(),
                AttributeModifier.Operation.ADD_VALUE
            ),
            EquipmentSlotGroup.HEAD
        );

        BuiltInRegistries.ATTRIBUTE
            .getHolder(ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "max_mana"))
            .ifPresent(attr -> builder.add(
                    attr,
                    new AttributeModifier(
                        ResourceLocation.fromNamespaceAndPath(MajoBroom.MODID, "majo_hat_max_mana"),
                        ServerConfig.armorOverpower ? 15000 : 200,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    EquipmentSlotGroup.HEAD
                )
            );

        BuiltInRegistries.ATTRIBUTE
            .getHolder(ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "spell_resist"))
            .ifPresent(attr -> builder.add(
                    attr,
                    new AttributeModifier(
                        ResourceLocation.fromNamespaceAndPath(MajoBroom.MODID, "majo_hat_spell_resist"),
                        ServerConfig.armorOverpower ? 0.5 : 0.1,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    EquipmentSlotGroup.HEAD
                )
            );

        BuiltInRegistries.ATTRIBUTE
            .getHolder(ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "spell_power"))
            .ifPresent(attr -> builder.add(
                    attr,
                    new AttributeModifier(
                        ResourceLocation.fromNamespaceAndPath(MajoBroom.MODID, "majo_hat_spell_power"),
                        ServerConfig.armorOverpower ? 2.0 : 0.1,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    EquipmentSlotGroup.HEAD
                )
            );
            
        BuiltInRegistries.ATTRIBUTE
            .getHolder(ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "cooldown_reduction"))
            .ifPresent(attr -> builder.add(
                    attr,
                    new AttributeModifier(
                        ResourceLocation.fromNamespaceAndPath(MajoBroom.MODID, "majo_hat_cooldown_reduction"),
                        ServerConfig.armorOverpower ? 0.95 : 0.1,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    EquipmentSlotGroup.HEAD
                )
            );
            
        BuiltInRegistries.ATTRIBUTE
            .getHolder(ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "mana_regen"))
            .ifPresent(attr -> builder.add(
                    attr,
                    new AttributeModifier(
                        ResourceLocation.fromNamespaceAndPath(MajoBroom.MODID, "majo_hat_mana_regen"),
                        ServerConfig.armorOverpower ? 0.5 : 0.05,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    EquipmentSlotGroup.HEAD
                )
            );
        
        return builder.build();
    }
}
