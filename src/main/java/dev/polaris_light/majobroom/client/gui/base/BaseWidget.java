package dev.polaris_light.majobroom.client.gui.base;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * 基础控件类
 * 替代 catnip 的 AbstractSimiWidget
 * 提供工具提示、回调机制等基础功能
 */
public abstract class BaseWidget extends AbstractWidget {
    
    protected List<Component> toolTip = new ArrayList<>();
    protected BiConsumer<Integer, Integer> onClick = (_$, _$$) -> {};
    protected boolean wasHovered = false;

    protected BaseWidget(int x, int y) {
        this(x, y, 16, 16);
    }

    protected BaseWidget(int x, int y, int width, int height) {
        this(x, y, width, height, CommonComponents.EMPTY);
    }

    protected BaseWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    /**
     * 设置点击回调
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseWidget> T withCallback(BiConsumer<Integer, Integer> cb) {
        this.onClick = cb;
        return (T) this;
    }

    /**
     * 设置点击回调（无参数版本）
     */
    public <T extends BaseWidget> T withCallback(Runnable cb) {
        return withCallback((_$, _$$) -> cb.run());
    }

    /**
     * 设置激活状态
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseWidget> T setActive(boolean active) {
        this.active = active;
        return (T) this;
    }

    /**
     * 获取工具提示
     */
    public List<Component> getToolTip() {
        return toolTip;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        beforeRender(graphics, mouseX, mouseY, partialTicks);
        doRender(graphics, mouseX, mouseY, partialTicks);
        afterRender(graphics, mouseX, mouseY, partialTicks);
        wasHovered = isHoveredOrFocused();
    }

    /**
     * 渲染前准备（子类可重写）
     */
    protected void beforeRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.pose().pushMatrix();
    }

    /**
     * 执行渲染（子类必须实现）
     */
    protected abstract void doRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks);

    /**
     * 渲染后清理（子类可重写）
     */
    protected void afterRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.pose().popMatrix();
    }

    /**
     * 执行回调
     */
    public void runCallback(double mouseX, double mouseY) {
        onClick.accept((int) mouseX, (int) mouseY);
    }

    // @Override
    // protected boolean clicked(double mouseX, double mouseY) {
    //     return this.isMouseOver(mouseX, mouseY);
    // }

    @Override
    public void onClick(MouseButtonEvent event, boolean isDoubleClick) {
        // 从事件对象里取坐标
        double mouseX = event.x();
        double mouseY = event.y();

        runCallback(mouseX, mouseY);
    }


    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        defaultButtonNarrationText(output);
    }
}

