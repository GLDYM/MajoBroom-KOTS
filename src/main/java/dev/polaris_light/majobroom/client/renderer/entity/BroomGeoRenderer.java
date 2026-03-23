package dev.polaris_light.majobroom.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.polaris_light.majobroom.client.renderer.model.BroomGeoModel;
import dev.polaris_light.majobroom.entity.BroomEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.Mth;
import org.jspecify.annotations.Nullable;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.RenderPassInfo;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.Map;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;

/**
 * 扫帚实体渲染器
 * 使用 GeckoLib 渲染模型
 * 重写 applyRotations 来处理非 LivingEntity 的旋转
 */
public class BroomGeoRenderer extends GeoEntityRenderer<BroomEntity, BroomGeoRenderer.BroomRenderState> {
    private static final DataTicket<Float> BROOM_FLOAT_OFFSET = DataTicket.create("majobroom:broom_float_offset", Float.class);
    private static final DataTicket<Float> BROOM_ENTITY_YAW = DataTicket.create("majobroom:broom_entity_yaw", Float.class);

    public BroomGeoRenderer(EntityRendererProvider.Context context) {
        super(context, new BroomGeoModel());
        this.shadowRadius = 0.3F;
    }

    @Override
    public BroomRenderState createRenderState(BroomEntity animatable, @Nullable Void relatedObject) {
        return new BroomRenderState();
    }

    /**
     * 将与实体相关的渲染数据写入 RenderState。
     */
    @Override
    public void addRenderData(BroomEntity animatable, @Nullable Void relatedObject, BroomRenderState renderState, float partialTick) {
        super.addRenderData(animatable, relatedObject, renderState, partialTick);

        // Use the renderer's instance id to stay aligned with GeckoLib's internal lookups.
        long instanceId = getInstanceId(animatable, relatedObject);
        AnimatableManager<?> manager = animatable.getAnimatableInstanceCache().getManagerForId(instanceId);
        renderState.addGeckolibData(DataTickets.ANIMATABLE_INSTANCE_ID, instanceId);
        renderState.addGeckolibData(DataTickets.ANIMATABLE_MANAGER, manager);

        renderState.addGeckolibData(BROOM_FLOAT_OFFSET, animatable.getInterpolatedFloatOffset(partialTick));
        renderState.addGeckolibData(BROOM_ENTITY_YAW, Mth.rotLerp(partialTick, animatable.yRotO, animatable.getYRot()));
    }

    @Override
    public void adjustRenderPose(RenderPassInfo<BroomRenderState> renderPassInfo) {
        super.adjustRenderPose(renderPassInfo);

        // 在默认姿态调整后叠加扫帚浮动位移。
        float floatOffset = renderPassInfo.getOrDefaultGeckolibData(BROOM_FLOAT_OFFSET, 0.0F);
        renderPassInfo.poseStack().translate(0.0D, floatOffset, 0.0D);
    }

    /**
     * 重写旋转应用逻辑以支持非 LivingEntity
     * 因为 BroomEntity 不是 LivingEntity，默认的 GeckoLib 逻辑会导致 lerpBodyRot = 0
     * 我们需要手动使用实体的 yRot 来应用旋转
     */
    @Override
    protected void applyRotations(RenderPassInfo<BroomRenderState> renderPassInfo, PoseStack poseStack, float nativeScale) {
        float entityYaw = renderPassInfo.getOrDefaultGeckolibData(BROOM_ENTITY_YAW, 0.0F);

        // 应用 Y 轴旋转（180度是因为模型默认朝向）
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));
    }

    public static class BroomRenderState extends EntityRenderState implements GeoRenderState {
        private final Map<DataTicket<?>, Object> dataMap = new Reference2ObjectOpenHashMap<>();

        @Override
        public <D> void addGeckolibData(DataTicket<D> dataTicket, D data) {
            this.dataMap.put(dataTicket, data);
        }

        @Override
        public boolean hasGeckolibData(DataTicket<?> dataTicket) {
            return this.dataMap.containsKey(dataTicket);
        }

        @Override
        public Map<DataTicket<?>, Object> getDataMap() {
            return this.dataMap;
        }
    }
}



