package dev.polaris_light.majobroom.client.input;

import org.lwjgl.glfw.GLFW;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import net.neoforged.neoforge.common.util.Lazy;

public final class KeyBindings {
    private KeyBindings() {}

    private static final KeyMapping.Category MAJOBROOM_CATEGORY =
            new KeyMapping.Category(Identifier.fromNamespaceAndPath("majobroom", "category"));
    
    public static final Lazy<KeyMapping> SUMMON_BROOM = Lazy.of(() -> new KeyMapping(
            "key.majobroom.summon",
            GLFW.GLFW_KEY_R,
            MAJOBROOM_CATEGORY
    ));

    public static final Lazy<KeyMapping> FLY_UP = Lazy.of(() -> new KeyMapping(
            "key.majobroom.fly_up",
            GLFW.GLFW_KEY_SPACE,
            MAJOBROOM_CATEGORY
    ));

    public static final Lazy<KeyMapping> FLY_DOWN = Lazy.of(() -> new KeyMapping(
            "key.majobroom.fly_down",
            GLFW.GLFW_KEY_LEFT_CONTROL,
            MAJOBROOM_CATEGORY
    ));

    public static final Lazy<KeyMapping> OPEN_CONFIG = Lazy.of(() -> new KeyMapping(
            "key.majobroom.open_config",
            GLFW.GLFW_KEY_Y,
            MAJOBROOM_CATEGORY
    ));

    public static void register(RegisterKeyMappingsEvent event) {
        event.registerCategory(MAJOBROOM_CATEGORY);
        event.register(SUMMON_BROOM.get());
        event.register(FLY_UP.get());
        event.register(FLY_DOWN.get());
        event.register(OPEN_CONFIG.get());
    }
}



