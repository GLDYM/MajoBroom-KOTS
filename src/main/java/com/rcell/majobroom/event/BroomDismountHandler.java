package com.rcell.majobroom.event;

import com.rcell.majobroom.MajoBroom;
import com.rcell.majobroom.entity.BroomEntity;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 扫帚下马事件处理器
 * 阻止玩家通过shift键快速下马，必须长按1秒
 */
@Mod.EventBusSubscriber(modid = MajoBroom.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BroomDismountHandler {
    
    /**
     * 处理实体下马事件
     * Minecraft原版的shift下马机制会触发此事件
     * 我们需要取消它，只允许通过我们的网络包下马
     */
    @SubscribeEvent
    public static void onEntityDismount(EntityMountEvent event) {
        // 只处理下马事件
        if (!event.isDismounting()) {
            return;
        }
        
        // 只处理扫帚
        if (!(event.getEntityBeingMounted() instanceof BroomEntity broom)) {
            return;
        }
        
        // 在服务端检查是否允许下马
        if (!event.getLevel().isClientSide) {
            // 如果扫帚标记为允许下马，则放行；否则取消事件
            if (!broom.isAllowDismount()) {
                event.setCanceled(true);
            }
        }
    }
}

