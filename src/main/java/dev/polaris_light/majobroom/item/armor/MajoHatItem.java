package dev.polaris_light.majobroom.item.armor;

import dev.polaris_light.majobroom.MajoBroom;
import dev.polaris_light.majobroom.client.renderer.armor.MajoHatRenderer;
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
 * 魔女帽子物品 - 使用 GeckoLib 渲染
 */
public class MajoHatItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final UUID ARMOR_UUID = UUID.fromString("16e67a1b-4293-439b-915d-8d2325ef95b6");
    private static final UUID TOUGHNESS_UUID = UUID.fromString("f1de4963-958f-4c63-8518-d40f921fd211");
    private static final UUID MAX_MANA_UUID = UUID.fromString("939f6a8b-48d4-47a3-85a8-a4f64d1ff11d");
    private static final UUID SPELL_RESIST_UUID = UUID.fromString("38950910-d4e9-46d8-b749-5cc03da6f4a0");
    private static final UUID SPELL_POWER_UUID = UUID.fromString("f8ca3ecf-facc-45d4-a8ef-4ec720ec3ad5");
    private static final UUID COOLDOWN_REDUCTION_UUID = UUID.fromString("36458f38-1454-47fd-bf4a-5ac0705240f3");
    private static final UUID MANA_REGEN_UUID = UUID.fromString("5b4f5f37-7c6b-4365-8f2e-84bd16f689b8");

    public MajoHatItem(Properties properties) {
        super(MajoArmorMaterials.MAJO, ArmorItem.Type.HELMET, properties);
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
                    this.renderer = new MajoHatRenderer();

                // 准备当前渲染帧
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // 帽子不需要动画，保持空实现
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
        return ServerConfig.armorOverpower ? 6 : this.material.getDefenseForType(this.type);
    }

    @Override
    public float getToughness() {
        return ServerConfig.armorOverpower ? 10.0F : this.material.getToughness();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (slot != EquipmentSlot.HEAD) {
            return super.getDefaultAttributeModifiers(slot);
        }

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

        builder.put(
            Attributes.ARMOR,
            new AttributeModifier(
                ARMOR_UUID,
                MajoBroom.MODID + ":majo_hat_armor",
                ServerConfig.armorOverpower ? 6.0D : this.getDefense(),
                AttributeModifier.Operation.ADDITION
            )
        );

        builder.put(
            Attributes.ARMOR_TOUGHNESS,
            new AttributeModifier(
                TOUGHNESS_UUID,
                MajoBroom.MODID + ":majo_hat_toughness",
                ServerConfig.armorOverpower ? 10.0D : this.getToughness(),
                AttributeModifier.Operation.ADDITION
            )
        );

        putOptional(builder, "max_mana", MAX_MANA_UUID, "majo_hat_max_mana",
            ServerConfig.armorOverpower ? 15000D : 200D, AttributeModifier.Operation.MULTIPLY_BASE);

        putOptional(builder, "spell_resist", SPELL_RESIST_UUID, "majo_hat_spell_resist",
            ServerConfig.armorOverpower ? 0.5D : 0.1D, AttributeModifier.Operation.MULTIPLY_BASE);

        putOptional(builder, "spell_power", SPELL_POWER_UUID, "majo_hat_spell_power",
            ServerConfig.armorOverpower ? 2.0D : 0.1D, AttributeModifier.Operation.MULTIPLY_BASE);

        putOptional(builder, "cooldown_reduction", COOLDOWN_REDUCTION_UUID, "majo_hat_cooldown_reduction",
            ServerConfig.armorOverpower ? 0.95D : 0.1D, AttributeModifier.Operation.MULTIPLY_BASE);

        putOptional(builder, "mana_regen", MANA_REGEN_UUID, "majo_hat_mana_regen",
            ServerConfig.armorOverpower ? 0.5D : 0.05D, AttributeModifier.Operation.MULTIPLY_BASE);

        return builder.build();
    }

    private static void putOptional(
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder,
        String attributePath,
        UUID uuid,
        String modifierName,
        double amount,
        AttributeModifier.Operation operation
    ) {
        Attribute attr = ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.fromNamespaceAndPath("irons_spellbooks", attributePath));
        if (attr != null) {
            builder.put(attr, new AttributeModifier(uuid, MajoBroom.MODID + ":" + modifierName, amount, operation));
        }
    }
}