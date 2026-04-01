package com.execcexrrvycvtvtv.ancient_horizons.entity.client.golden_eagle;

import com.execcexrrvycvtvtv.ancient_horizons.entity.custom.mob.GoldenEagleEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class GoldenEagleItemLayer extends RenderLayer<GoldenEagleEntity, GoldenEagleModel<GoldenEagleEntity>> {
    private final ItemInHandRenderer itemInHandRenderer;

    public GoldenEagleItemLayer(RenderLayerParent<GoldenEagleEntity, GoldenEagleModel<GoldenEagleEntity>> renderer, ItemInHandRenderer itemInHandRenderer) {
        super(renderer);
        this.itemInHandRenderer = itemInHandRenderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, GoldenEagleEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemstack = entity.getItemInBeak();
        if (!itemstack.isEmpty()) {
            poseStack.pushPose();

            this.getParentModel().headParts().iterator().next().translateAndRotate(poseStack);

            poseStack.translate(0.0F, 0.2F, -0.5F);

            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

            this.itemInHandRenderer.renderItem(
                    entity,
                    itemstack,
                    ItemDisplayContext.GROUND,
                    false,
                    poseStack,
                    buffer,
                    packedLight
            );

            poseStack.popPose();
        }
    }
}