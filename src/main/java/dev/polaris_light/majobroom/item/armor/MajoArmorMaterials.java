package dev.polaris_light.majobroom.item.armor;

import dev.polaris_light.majobroom.MajoBroom;
import net.minecraft.util.Util;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.EnumMap;

/**
 * 魔女装备材质
 * NeoForge 1.21.1: ArmorMaterial从接口改为类，需要直接创建实例
 */
public class MajoArmorMaterials {
    public static final ResourceKey<EquipmentAsset> MAJO_ASSET = ResourceKey.create(
        EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(MajoBroom.MODID, "majo_cloth")
    );    
    /**
     * 魔女布料材质 - 轻便但防御较低的魔法布料
     */
    public static final ArmorMaterial MAJO_CLOTH = new ArmorMaterial(
        400, 
        // 防御值映射
        Util.make(new EnumMap<>(ArmorType.class), map -> {
            map.put(ArmorType.BOOTS, 1);
            map.put(ArmorType.LEGGINGS, 2);
            map.put(ArmorType.CHESTPLATE, 3);
            map.put(ArmorType.HELMET, 1);
            map.put(ArmorType.BODY, 3);
        }),
        // 魔咒加成 - 较高的魔咒加成，适合魔法装备
        30,
        // 装备音效
        SoundEvents.ARMOR_EQUIP_LEATHER,
        // 韧性值 - 布料无韧性
        0,
        // 击退抗性 - 布料无击退抗性
        0,
        // 修复材料 - 使用羊毛修复
        ItemTags.WOOL,
        // 材质层
        MAJO_ASSET
    );
}
