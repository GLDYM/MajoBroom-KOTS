package dev.polaris_light.majobroom.client.gui.screen;

import dev.polaris_light.majobroom.client.gui.base.BaseScreen;
import dev.polaris_light.majobroom.client.gui.texture.GuiTextures;
import dev.polaris_light.majobroom.client.gui.widget.IconButton;
import net.minecraft.network.chat.Component;

/**
 * 扫帚模组的抽象基础屏幕类
 * 继承自自定义的 BaseScreen
 * 提供通用功能：窗口管理、通用按钮、工具提示等
 */
public abstract class AbstractBroomScreen extends BaseScreen {
    protected GuiTextures background;
    protected int windowXOffset, windowYOffset;

    protected IconButton confirmButton;

    protected AbstractBroomScreen(Component title) {
        super(title);
        this.windowXOffset = 0;
        this.windowYOffset = 0;
    }
}

