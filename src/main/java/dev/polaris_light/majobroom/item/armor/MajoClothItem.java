package dev.polaris_light.majobroom.item.armor;

import dev.polaris_light.majobroom.MajoBroom;
import dev.polaris_light.majobroom.client.renderer.armor.MajoClothRenderer;
import dev.polaris_light.majobroom.config.ServerConfig;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;

import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;
import com.google.common.collect.Iterables;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.util.UUID;

import java.util.function.Consumer;

/**
 * 魔女长袍物品 - 使用 GeckoLib 渲染
 */
public class MajoClothItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final UUID ARMOR_UUID = UUID.fromString("f7ef2f8d-63dc-4e7f-8be7-9dfe2be77c8d");
    private static final UUID TOUGHNESS_UUID = UUID.fromString("61b71a8d-fe09-4daa-95aa-a6d6f262fb07");
    private static final UUID SPELL_RESIST_UUID = UUID.fromString("89fe7a0f-68de-4b91-b8c6-22fe61fcb70f");


    public MajoClothItem(Properties properties) {
        super(MajoArmorMaterials.MAJO, ArmorItem.Type.CHESTPLATE, properties);
    }

    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(@NotNull LivingEntity livingEntity, 
                                                                   @NotNull ItemStack itemStack, 
                                                                   @NotNull EquipmentSlot equipmentSlot, 
                                                                   @NotNull HumanoidModel<?> original) {
                if (this.renderer == null)
                    this.renderer = new MajoClothRenderer();

                // 准备当前渲染帧
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // 长袍不需要动画，保持空实现
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
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 240, 3, false, false));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 240, 0, false, false));
            }
        }
	    if (itemstack.isDamaged() && ServerConfig.armorImmortal) {
		    itemstack.setDamageValue(0);
	    }
    }

    @Override
    public int getDefense() {
        return ServerConfig.armorOverpower ? 10 : this.material.getDefenseForType(this.type);
    }

    @Override
    public float getToughness() {
        return ServerConfig.armorOverpower ? 10.0F : this.material.getToughness();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (slot != EquipmentSlot.CHEST) {
            return super.getDefaultAttributeModifiers(slot);
        }

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

        builder.put(
            Attributes.ARMOR,
            new AttributeModifier(
                ARMOR_UUID,
                MajoBroom.MODID + ":majo_cloth_armor",
                ServerConfig.armorOverpower ? 10.0D : this.getDefense(),
                AttributeModifier.Operation.ADDITION
            )
        );

        builder.put(
            Attributes.ARMOR_TOUGHNESS,
            new AttributeModifier(
                TOUGHNESS_UUID,
                MajoBroom.MODID + ":majo_cloth_toughness",
                ServerConfig.armorOverpower ? 10.0D : this.getToughness(),
                AttributeModifier.Operation.ADDITION
            )
        );

        Attribute spellResist = ForgeRegistries.ATTRIBUTES.getValue(
            ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "spell_resist")
        );
        if (spellResist != null) {
            builder.put(
                spellResist,
                new AttributeModifier(
                    SPELL_RESIST_UUID,
                    MajoBroom.MODID + ":majo_cloth_spell_resist",
                    ServerConfig.armorOverpower ? 0.5D : 0.1D,
                    AttributeModifier.Operation.MULTIPLY_BASE
                )
            );
        }
        
        return builder.build();
    }
}