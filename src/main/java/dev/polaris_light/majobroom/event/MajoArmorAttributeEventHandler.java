package dev.polaris_light.majobroom.event;

import dev.polaris_light.majobroom.MajoBroom;
import dev.polaris_light.majobroom.config.ServerConfig;
import dev.polaris_light.majobroom.init.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

@EventBusSubscriber(modid = MajoBroom.MODID)
public final class MajoArmorAttributeEventHandler {
    private MajoArmorAttributeEventHandler() {}

    @SubscribeEvent
    public static void onItemAttributeModifier(ItemAttributeModifierEvent event) {
        if (!ServerConfig.armorOverpower) {
            return;
        }

        Item item = event.getItemStack().getItem();

        if (item == ModItems.MAJO_BOOTS.get()) {
            event.replaceModifier(
                Attributes.ARMOR,
                new AttributeModifier(
                    Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_boots_armor"),
                    5.0,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.FEET
            );
            event.replaceModifier(
                Attributes.ARMOR_TOUGHNESS,
                new AttributeModifier(
                    Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_boots_toughness"),
                    10.0,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.FEET
            );
            BuiltInRegistries.ATTRIBUTE
                .get(Identifier.fromNamespaceAndPath("irons_spellbooks", "spell_resist"))
                .ifPresent(attr -> event.replaceModifier(
                    attr,
                    new AttributeModifier(
                        Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_boots_spell_resist"),
                        0.5,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    EquipmentSlotGroup.FEET
                ));
            return;
        }

        if (item == ModItems.MAJO_STOCKING.get()) {
            event.replaceModifier(
                Attributes.ARMOR,
                new AttributeModifier(
                    Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_stocking_armor"),
                    9.0,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.LEGS
            );
            event.replaceModifier(
                Attributes.ARMOR_TOUGHNESS,
                new AttributeModifier(
                    Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_stocking_toughness"),
                    10.0,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.LEGS
            );
            BuiltInRegistries.ATTRIBUTE
                .get(Identifier.fromNamespaceAndPath("irons_spellbooks", "spell_resist"))
                .ifPresent(attr -> event.replaceModifier(
                    attr,
                    new AttributeModifier(
                        Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_stocking_spell_resist"),
                        0.5,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    EquipmentSlotGroup.LEGS
                ));
            return;
        }

        if (item == ModItems.MAJO_CLOTH.get()) {
            event.replaceModifier(
                Attributes.ARMOR,
                new AttributeModifier(
                    Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_cloth_armor"),
                    10.0,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.CHEST
            );
            event.replaceModifier(
                Attributes.ARMOR_TOUGHNESS,
                new AttributeModifier(
                    Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_cloth_toughness"),
                    10.0,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.CHEST
            );
            BuiltInRegistries.ATTRIBUTE
                .get(Identifier.fromNamespaceAndPath("irons_spellbooks", "spell_resist"))
                .ifPresent(attr -> event.replaceModifier(
                    attr,
                    new AttributeModifier(
                        Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_cloth_spell_resist"),
                        0.5,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    EquipmentSlotGroup.CHEST
                ));
            return;
        }

        if (item == ModItems.MAJO_HAT.get()) {
            event.replaceModifier(
                Attributes.ARMOR,
                new AttributeModifier(
                    Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_hat_armor"),
                    6.0,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.HEAD
            );
            event.replaceModifier(
                Attributes.ARMOR_TOUGHNESS,
                new AttributeModifier(
                    Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_hat_toughness"),
                    10.0,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.HEAD
            );
            BuiltInRegistries.ATTRIBUTE
                .get(Identifier.fromNamespaceAndPath("irons_spellbooks", "max_mana"))
                .ifPresent(attr -> event.replaceModifier(
                    attr,
                    new AttributeModifier(
                        Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_hat_max_mana"),
                        15000.0,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    EquipmentSlotGroup.HEAD
                ));
            BuiltInRegistries.ATTRIBUTE
                .get(Identifier.fromNamespaceAndPath("irons_spellbooks", "spell_resist"))
                .ifPresent(attr -> event.replaceModifier(
                    attr,
                    new AttributeModifier(
                        Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_hat_spell_resist"),
                        0.5,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    EquipmentSlotGroup.HEAD
                ));
            BuiltInRegistries.ATTRIBUTE
                .get(Identifier.fromNamespaceAndPath("irons_spellbooks", "spell_power"))
                .ifPresent(attr -> event.replaceModifier(
                    attr,
                    new AttributeModifier(
                        Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_hat_spell_power"),
                        2.0,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    EquipmentSlotGroup.HEAD
                ));
            BuiltInRegistries.ATTRIBUTE
                .get(Identifier.fromNamespaceAndPath("irons_spellbooks", "cooldown_reduction"))
                .ifPresent(attr -> event.replaceModifier(
                    attr,
                    new AttributeModifier(
                        Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_hat_cooldown_reduction"),
                        0.95,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    EquipmentSlotGroup.HEAD
                ));
            BuiltInRegistries.ATTRIBUTE
                .get(Identifier.fromNamespaceAndPath("irons_spellbooks", "mana_regen"))
                .ifPresent(attr -> event.replaceModifier(
                    attr,
                    new AttributeModifier(
                        Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_hat_mana_regen"),
                        0.5,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    EquipmentSlotGroup.HEAD
                ));
        }
    }
}
