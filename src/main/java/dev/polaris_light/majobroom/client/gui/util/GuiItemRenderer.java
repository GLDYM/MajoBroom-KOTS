package dev.polaris_light.majobroom.client.gui.util;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

/**
 * GUI 物品渲染工具 (1.21.11+)
 * 用于在 GUI 中渲染可缩放的物品图标
 */
public class GuiItemRenderer {
    private GuiItemRenderer() {}
    
    /**
     * 在 GUI 中渲染物品，支持位置和缩放
     * 深度挂了。
     * 
     * @param graphics 渲染上下文
     * @param stack 物品栈
     * @param x X 坐标
     * @param y Y 坐标
     * @param scale 缩放比例
     */
    public static void renderItemAt(GuiGraphicsExtractor graphics, ItemStack stack, float x, float y, float scale) {
        graphics.pose().pushMatrix();
        graphics.pose().translate(x, y);
        graphics.pose().scale(scale, scale);
        graphics.item(stack, 0, 0);
        graphics.pose().popMatrix();
    }
}

