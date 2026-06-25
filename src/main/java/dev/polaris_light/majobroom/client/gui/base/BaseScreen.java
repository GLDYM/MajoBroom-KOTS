package dev.polaris_light.majobroom.client.gui.base;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;


import java.util.Collection;
import java.util.List;

/**
 * 基础屏幕类
 * 替代 catnip 的 AbstractSimiScreen
 * 提供窗口布局、控件管理、渲染流程等基础功能
 */
public abstract class BaseScreen extends Screen {
    
    protected int windowWidth, windowHeight;
    protected int windowXOffset, windowYOffset;
    protected int guiLeft, guiTop;

    protected BaseScreen(Component title) {
        super(title);
    }

    protected BaseScreen() {
        this(CommonComponents.EMPTY);
    }

    /**
     * 设置窗口大小（必须在 init() 之前调用）
     */
    protected void setWindowSize(int width, int height) {
        windowWidth = width;
        windowHeight = height;
    }

    /**
     * 设置窗口偏移（必须在 init() 之前调用）
     */
    protected void setWindowOffset(int xOffset, int yOffset) {
        windowXOffset = xOffset;
        windowYOffset = yOffset;
    }

    @Override
    protected void init() {
        // 计算窗口居中位置
        guiLeft = (width - windowWidth) / 2;
        guiTop = (height - windowHeight) / 2;
        // 应用偏移
        guiLeft += windowXOffset;
        guiTop += windowYOffset;
    }

    /**
     * 批量添加可渲染控件
     */
    @SuppressWarnings("unchecked")
    protected <W extends GuiEventListener & Renderable & NarratableEntry> void addRenderableWidgets(W... widgets) {
        for (W widget : widgets) {
            addRenderableWidget(widget);
        }
    }

    /**
     * 批量添加可渲染控件
     */
    protected <W extends GuiEventListener & Renderable & NarratableEntry> void addRenderableWidgets(Collection<W> widgets) {
        for (W widget : widgets) {
            addRenderableWidget(widget);
        }
    }

    /**
     * 批量移除控件
     */
    protected void removeWidgets(GuiEventListener... widgets) {
        for (GuiEventListener widget : widgets) {
            removeWidget(widget);
        }
    }

    /**
     * 批量移除控件
     */
    protected void removeWidgets(Collection<? extends GuiEventListener> widgets) {
        for (GuiEventListener widget : widgets) {
            removeWidget(widget);
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        // 渲染菜单背景（背景纹理，继承自 Screen）
        extractMenuBackground(graphics);
        
        // 渲染窗口背景（半透明遮罩）
        renderWindowBackground(graphics, mouseX, mouseY, partialTicks);
        
        // 渲染窗口内容（由子类实现）
        renderWindow(graphics, mouseX, mouseY, partialTicks);
        
        // 直接渲染控件，不调用 super.render()，因为那会再次调用 renderBackground()
        for (Renderable renderable : renderables) {
            renderable.extractRenderState(graphics, mouseX, mouseY, partialTicks);
        }
        
        // 渲染工具提示
        renderWindowForeground(graphics, mouseX, mouseY, partialTicks);
    }

    /**
     * 渲染窗口背景（半透明遮罩）
     * 默认调用 renderBackground，子类可以重写以自定义背景
     */
    protected void renderWindowBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        extractBackground(graphics, mouseX, mouseY, partialTicks);
    }

    /**
     * 重写 renderBackground 以避免默认的高斯模糊效果
     * 使用 fillGradient 渲染半透明遮罩
     */
    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        // 使用半透明的深灰色背景，不使用模糊效果
        // 0x50 是透明度（约31%），0x101010 是深灰色
        graphics.fillGradient(0, 0, this.width, this.height, 0x50_101010, 0x50_101010);
    }

    /**
     * 渲染窗口内容（由子类实现）
     */
    protected abstract void renderWindow(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks);

    /**
     * 渲染前景层（工具提示等）
     */
    protected void renderWindowForeground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        for (Renderable widget : renderables) {
            if (widget instanceof BaseWidget baseWidget && baseWidget.isHovered() && baseWidget.visible) {
                var tooltip = baseWidget.getToolTip();
                if (!tooltip.isEmpty()) {
                    // 将 Component 转换为 ClientTooltipComponent
                    List<ClientTooltipComponent> clientComponents =
                            tooltip.stream()
                                .map(Component::getVisualOrderText)
                                .map(ClientTooltipComponent::create)
                                .toList();

                    // 使用新版 API
                        graphics.tooltip(
                            font,
                            clientComponents,
                            mouseX,
                            mouseY,
                            DefaultTooltipPositioner.INSTANCE, // 默认位置器
                            null                               // 可选背景 Identifier
                    );
                }
            }
        }
    }


    @Override
    public boolean keyPressed(KeyEvent event) {
        // 先交给父类处理
        if (super.keyPressed(event)) {
            return true;
        }

        // ESC 关闭界面
        if (event.isEscape() && this.shouldCloseOnEsc()) {
            this.onClose();
            return true;
        }

        // 允许用 E 键关闭界面（背包键）
        if (this.minecraft != null && this.minecraft.options.keyInventory.matches(event)) {
            this.onClose();
            return true;
        }

        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

