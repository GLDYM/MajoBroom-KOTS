package com.rcell.majobroom.init;

import com.rcell.majobroom.MajoBroom;
import com.rcell.majobroom.item.BroomItem;
import com.rcell.majobroom.item.armor.MajoHatItem;
import com.rcell.majobroom.item.armor.MajoRobeItem;
import com.rcell.majobroom.client.tooltip.ItemDescription;
import com.rcell.majobroom.client.tooltip.TooltipModifier;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    private ModItems() {}

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MajoBroom.MODID);

    public static final RegistryObject<Item> BROOM = ITEMS.register(
            "broom", () -> new BroomItem(new Item.Properties().stacksTo(1))
    );

    public static final RegistryObject<Item> MAJO_HAT = ITEMS.register(
            "majo_hat", () -> new MajoHatItem(new Item.Properties())
    );

    public static final RegistryObject<Item> MAJO_CLOTH = ITEMS.register(
            "majo_cloth", () -> new MajoRobeItem(new Item.Properties())
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
}



