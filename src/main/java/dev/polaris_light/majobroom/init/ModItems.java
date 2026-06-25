package dev.polaris_light.majobroom.init;

import dev.polaris_light.majobroom.MajoBroom;
import dev.polaris_light.majobroom.item.BroomItem;
import dev.polaris_light.majobroom.item.armor.MajoHatItem;
import dev.polaris_light.majobroom.item.armor.MajoClothItem;
import dev.polaris_light.majobroom.item.armor.MajoStockingItem;
import dev.polaris_light.majobroom.item.armor.MajoArmorMaterials;
import dev.polaris_light.majobroom.item.armor.MajoBootsItem;
import dev.polaris_light.majobroom.client.tooltip.ItemDescription;
import dev.polaris_light.majobroom.client.tooltip.TooltipModifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;


public final class ModItems {
    private ModItems() {}

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(MajoBroom.MODID);

    public static final DeferredItem<Item> BROOM = ITEMS.registerItem(
        "broom", 
        props -> new BroomItem(
            props.stacksTo(1)
                .fireResistant()
        )
    );

    public static final DeferredItem<Item> MAJO_HAT = ITEMS.registerItem(
        "majo_hat", 
        props -> new MajoHatItem(
            props.stacksTo(1)
                .fireResistant()
                .humanoidArmor(MajoArmorMaterials.MAJO_CLOTH, ArmorType.HELMET)
                .attributes(createHatAttributes())
        )
    );

    public static final DeferredItem<Item> MAJO_CLOTH = ITEMS.registerItem(
        "majo_cloth", 
        props -> new MajoClothItem(
            props.stacksTo(1)
                .fireResistant()
                .humanoidArmor(MajoArmorMaterials.MAJO_CLOTH, ArmorType.CHESTPLATE)
                .attributes(createClothAttributes())
        )
    );

    public static final DeferredItem<Item> MAJO_STOCKING = ITEMS.registerItem(
        "majo_stocking", 
        props -> new MajoStockingItem(
            props.stacksTo(1)
                .fireResistant()
                .humanoidArmor(MajoArmorMaterials.MAJO_CLOTH, ArmorType.LEGGINGS)
                .attributes(createStockingAttributes())
        )
    );

    public static final DeferredItem<Item> MAJO_BOOTS = ITEMS.registerItem(
        "majo_boots", 
        props -> new MajoBootsItem(
            props.stacksTo(1)
                .fireResistant()
                .humanoidArmor(MajoArmorMaterials.MAJO_CLOTH, ArmorType.BOOTS)
                .attributes(createBootsAttributes())
        )
    );


    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }

    /**
     * 注册物品的 Tooltip 修饰器
     * 必须在物品注册完成后调用（在客户端设置阶段）
     */
    public static void registerTooltips() {
        // 为扫帚注册 Tooltip
        TooltipModifier.register(BROOM.get(), new ItemDescription.Modifier(BROOM.get()));
    }

    private static ItemAttributeModifiers createBootsAttributes() {
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        builder.add(
            Attributes.ARMOR,
            new AttributeModifier(
                Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_boots_armor"),
                1.0,
                AttributeModifier.Operation.ADD_VALUE
            ),
            EquipmentSlotGroup.FEET
        );
        builder.add(
            Attributes.ARMOR_TOUGHNESS,
            new AttributeModifier(
                Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_boots_toughness"),
                0.0,
                AttributeModifier.Operation.ADD_VALUE
            ),
            EquipmentSlotGroup.FEET
        );
        BuiltInRegistries.ATTRIBUTE
            .get(Identifier.fromNamespaceAndPath("irons_spellbooks", "spell_resist"))
            .ifPresent(attr -> builder.add(
                attr,
                new AttributeModifier(
                    Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_boots_spell_resist"),
                    0.1,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ),
                EquipmentSlotGroup.FEET
            ));
        return builder.build();
    }

    private static ItemAttributeModifiers createStockingAttributes() {
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        builder.add(
            Attributes.ARMOR,
            new AttributeModifier(
                Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_stocking_armor"),
                2.0,
                AttributeModifier.Operation.ADD_VALUE
            ),
            EquipmentSlotGroup.LEGS
        );
        builder.add(
            Attributes.ARMOR_TOUGHNESS,
            new AttributeModifier(
                Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_stocking_toughness"),
                0.0,
                AttributeModifier.Operation.ADD_VALUE
            ),
            EquipmentSlotGroup.LEGS
        );
        BuiltInRegistries.ATTRIBUTE
            .get(Identifier.fromNamespaceAndPath("irons_spellbooks", "spell_resist"))
            .ifPresent(attr -> builder.add(
                attr,
                new AttributeModifier(
                    Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_stocking_spell_resist"),
                    0.1,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ),
                EquipmentSlotGroup.LEGS
            ));
        return builder.build();
    }

    private static ItemAttributeModifiers createClothAttributes() {
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        builder.add(
            Attributes.ARMOR,
            new AttributeModifier(
                Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_cloth_armor"),
                3.0,
                AttributeModifier.Operation.ADD_VALUE
            ),
            EquipmentSlotGroup.CHEST
        );
        builder.add(
            Attributes.ARMOR_TOUGHNESS,
            new AttributeModifier(
                Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_cloth_toughness"),
                0.0,
                AttributeModifier.Operation.ADD_VALUE
            ),
            EquipmentSlotGroup.CHEST
        );
        BuiltInRegistries.ATTRIBUTE
            .get(Identifier.fromNamespaceAndPath("irons_spellbooks", "spell_resist"))
            .ifPresent(attr -> builder.add(
                attr,
                new AttributeModifier(
                    Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_cloth_spell_resist"),
                    0.1,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ),
                EquipmentSlotGroup.CHEST
            ));
        return builder.build();
    }

    private static ItemAttributeModifiers createHatAttributes() {
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        builder.add(
            Attributes.ARMOR,
            new AttributeModifier(
                Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_hat_armor"),
                1.0,
                AttributeModifier.Operation.ADD_VALUE
            ),
            EquipmentSlotGroup.HEAD
        );
        builder.add(
            Attributes.ARMOR_TOUGHNESS,
            new AttributeModifier(
                Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_hat_toughness"),
                0.0,
                AttributeModifier.Operation.ADD_VALUE
            ),
            EquipmentSlotGroup.HEAD
        );
        BuiltInRegistries.ATTRIBUTE
            .get(Identifier.fromNamespaceAndPath("irons_spellbooks", "max_mana"))
            .ifPresent(attr -> builder.add(
                attr,
                new AttributeModifier(
                    Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_hat_max_mana"),
                    200.0,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ),
                EquipmentSlotGroup.HEAD
            ));
        BuiltInRegistries.ATTRIBUTE
            .get(Identifier.fromNamespaceAndPath("irons_spellbooks", "spell_resist"))
            .ifPresent(attr -> builder.add(
                attr,
                new AttributeModifier(
                    Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_hat_spell_resist"),
                    0.1,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ),
                EquipmentSlotGroup.HEAD
            ));
        BuiltInRegistries.ATTRIBUTE
            .get(Identifier.fromNamespaceAndPath("irons_spellbooks", "spell_power"))
            .ifPresent(attr -> builder.add(
                attr,
                new AttributeModifier(
                    Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_hat_spell_power"),
                    0.1,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ),
                EquipmentSlotGroup.HEAD
            ));
        BuiltInRegistries.ATTRIBUTE
            .get(Identifier.fromNamespaceAndPath("irons_spellbooks", "cooldown_reduction"))
            .ifPresent(attr -> builder.add(
                attr,
                new AttributeModifier(
                    Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_hat_cooldown_reduction"),
                    0.1,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ),
                EquipmentSlotGroup.HEAD
            ));
        BuiltInRegistries.ATTRIBUTE
            .get(Identifier.fromNamespaceAndPath("irons_spellbooks", "mana_regen"))
            .ifPresent(attr -> builder.add(
                attr,
                new AttributeModifier(
                    Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_hat_mana_regen"),
                    0.05,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ),
                EquipmentSlotGroup.HEAD
            ));
        return builder.build();
    }
}
