package dev.polaris_light.majobroom.init;

import dev.polaris_light.majobroom.MajoBroom;
import dev.polaris_light.majobroom.effect.DecreaseDamageEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.awt.*;
import java.util.function.Supplier;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOD_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, MajoBroom.MODID);
    // 第二个参数是颜色。自己选一个颜色。
    // 应该是用十进制表示的16进制吧，大家自己试试
    public static final Holder<MobEffect> DECREASE_DAMAGE_EFFECT = register("decrease_damage", ()->new DecreaseDamageEffect(MobEffectCategory.BENEFICIAL, 0xff0000));

    public static <T extends MobEffect> DeferredHolder<MobEffect, T> register(String name, Supplier<T> effect){
        return MOD_EFFECTS.register(name, effect);
    }
    public static void register(IEventBus eventBus){
        MOD_EFFECTS.register(eventBus);
    }
}
