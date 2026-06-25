package dev.polaris_light.majobroom.client.renderer.armor;

import com.geckolib.constant.DataTickets;
import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.renderer.GeoArmorRenderer;
import com.geckolib.renderer.base.BoneSnapshots;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.RenderPassInfo;
import dev.polaris_light.majobroom.item.armor.MajoClothItem;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.Pose;

/**
 * 魔女长袍的 GeckoLib 渲染器
 * 处理 dress 节点的特殊运动逻辑：
 * - dress 的旋转由左右腿的平均旋转决定
 * - 在骑乘状态下隐藏 sithide1 和 sithide2 节点
 */
public class MajoClothRenderer extends GeoArmorRenderer<MajoClothItem, MajoClothRenderer.ClothRenderState> {
    public MajoClothRenderer(MajoClothItem item) {
        super(item);
    }

    /**
     * 在渲染前处理 dress 节点的特殊运动逻辑。
     * 按用户要求保留 preRender 入口，由 GeckoLib 5 的 adjustModelBonesForRender 调用。
     */
    @Override
    public void adjustModelBonesForRender(RenderPassInfo<ClothRenderState> renderPassInfo, BoneSnapshots snapshots) {
        super.adjustModelBonesForRender(renderPassInfo, snapshots);
        preRender(renderPassInfo, snapshots);
    }

    /**
     * preRender 逻辑入口（GeckoLib 5 版本）
     */
    public void preRender(RenderPassInfo<ClothRenderState> renderPassInfo, BoneSnapshots snapshots) {
        HumanoidModel<?> baseModel = Objects.requireNonNull(renderPassInfo.getGeckolibData(GeoArmorRenderer.BASE_MODEL));
        Pose pose = renderPassInfo.getGeckolibData(DataTickets.ENTITY_POSE);
        boolean isRiding = pose == Pose.SITTING;

        // 处理 dress 节点的特殊运动
        ModelPart leftLeg = baseModel.leftLeg;
        ModelPart rightLeg = baseModel.rightLeg;

        snapshots.ifPresent("dress", dress -> {
            // dress 的 X 轴旋转是左右腿旋转的平均值
            dress.setRotX(-(leftLeg.xRot + rightLeg.xRot) / 2.0f);

            // dress 的 Z 轴位置跟随左腿
            dress.setTranslateZ(leftLeg.z);

            // 骑乘状态：固定旋转角度
            if (isRiding) {
                dress.setRotX(1.04f);
            }
        });

        snapshots.ifPresent("sithide1", bone -> bone.skipRender(isRiding));
        snapshots.ifPresent("sithide2", bone -> bone.skipRender(isRiding));
    }

    public static class ClothRenderState extends HumanoidRenderState implements GeoRenderState {
        private final Map<DataTicket<?>, Object> dataMap = new Reference2ObjectOpenHashMap<>();

        @Override
        public Map<DataTicket<?>, Object> getDataMap() {
            return this.dataMap;
        }
    }
}
