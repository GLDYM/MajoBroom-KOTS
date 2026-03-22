package dev.polaris_light.majobroom.item.armor;

import dev.polaris_light.majobroom.MajoBroom;
import dev.polaris_light.majobroom.client.renderer.armor.MajoStockingRenderer;
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

public class MajoStockingItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final UUID ARMOR_UUID = UUID.fromString("5d901f48-23f2-4dea-b524-9be2f6e7cd11");
    private static final UUID TOUGHNESS_UUID = UUID.fromString("0f0cc1a5-778d-43e8-a2f4-58dfe53caa31");
    private static final UUID SPELL_RESIST_UUID = UUID.fromString("8e487a4b-3a7e-4e6a-a7bb-ec1450ab6d58");


    public MajoStockingItem(Properties properties) {
        super(MajoArmorMaterials.MAJO, ArmorItem.Type.LEGGINGS, properties);
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
                    this.renderer = new MajoStockingRenderer();

                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

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
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);
        if (entity instanceof LivingEntity livingEntity && Iterables.contains(livingEntity.getArmorSlots(), itemstack)) {
            if (ServerConfig.armorBless) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 240, 1, false, false));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 240, 2, false, false));
            }
        }
        if (itemstack.isDamaged() && ServerConfig.armorImmortal) {
		    itemstack.setDamageValue(0);
	    }
    }

    @Override
    public int getDefense() {
        return ServerConfig.armorOverpower ? 9 : this.material.getDefenseForType(this.type);
    }

    @Override
    public float getToughness() {
        return ServerConfig.armorOverpower ? 10.0F : this.material.getToughness();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (slot != EquipmentSlot.LEGS) {
            return super.getDefaultAttributeModifiers(slot);
        }

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

        builder.put(
            Attributes.ARMOR,
            new AttributeModifier(
                ARMOR_UUID,
                MajoBroom.MODID + ":majo_stocking_armor",
                ServerConfig.armorOverpower ? 9.0D : this.getDefense(),
                AttributeModifier.Operation.ADDITION
            )
        );

        builder.put(
            Attributes.ARMOR_TOUGHNESS,
            new AttributeModifier(
                TOUGHNESS_UUID,
                MajoBroom.MODID + ":majo_stocking_toughness",
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
                    MajoBroom.MODID + ":majo_stocking_spell_resist",
                    ServerConfig.armorOverpower ? 0.5D : 0.1D,
                    AttributeModifier.Operation.MULTIPLY_BASE
                )
            );
        }

        return builder.build();
    }
}