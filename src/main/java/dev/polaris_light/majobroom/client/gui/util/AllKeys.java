package dev.polaris_light.majobroom.client.gui.util;

import net.minecraft.client.Minecraft;

/**
 * 键盘和鼠标输入工具类
 * 参考 Create 的 AllKeys 实现
 */
public class AllKeys {
    
    /**
     * 检查鼠标按钮是否按下
     * @param button 鼠标按钮 (0=左键, 1=右键, 2=中键)
     */
    public static boolean isMouseButtonDown(int button) {
        var mouseHandler = Minecraft.getInstance().mouseHandler;
        return switch (button) {
            case 0 -> mouseHandler.isLeftPressed();
            case 1 -> mouseHandler.isRightPressed();
            case 2 -> mouseHandler.isMiddlePressed();
            default -> false;
        };
    }
}

