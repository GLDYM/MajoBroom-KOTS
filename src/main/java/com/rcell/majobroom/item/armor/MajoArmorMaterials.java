package com.rcell.majobroom.item.armor;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public enum MajoArmorMaterials implements ArmorMaterial {
    MAJO;

    @Override
    public int getDurabilityForType(@NotNull ArmorItem.Type type) {
        return ArmorMaterials.LEATHER.getDurabilityForType(type);
    }

    @Override
    public int getDefenseForType(@NotNull ArmorItem.Type type) {
        return ArmorMaterials.LEATHER.getDefenseForType(type);
    }

    @Override
    public int getEnchantmentValue() {
        return ArmorMaterials.LEATHER.getEnchantmentValue();
    }

    @Override
    public @NotNull SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_LEATHER;
    }

    @Override
    public @NotNull Ingredient getRepairIngredient() {
        return Ingredient.EMPTY;
    }

    @Override
    public @NotNull String getName() {
        return "majobroom:majo";
    }

    @Override
    public float getToughness() {
        return 0.0F;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.0F;
    }
}


