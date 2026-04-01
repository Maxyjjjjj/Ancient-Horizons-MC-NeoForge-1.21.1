package com.execcexrrvycvtvtv.ancient_horizons.entity.client.golden_eagle;

import com.execcexrrvycvtvtv.ancient_horizons.AncientHorizons;
import com.execcexrrvycvtvtv.ancient_horizons.entity.custom.mob.GoldenEagleEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class GoldenEagleCollarLayer extends RenderLayer<GoldenEagleEntity, GoldenEagleModel<GoldenEagleEntity>> {
    private static final ResourceLocation GOLDEN_EAGLE_COLLAR_LOCATION = ResourceLocation.fromNamespaceAndPath(AncientHorizons.MOD_ID, "textures/entity/golden_eagle/golden_eagle_collar.png");

    public GoldenEagleCollarLayer(RenderLayerParent<GoldenEagleEntity, GoldenEagleModel<GoldenEagleEntity>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, GoldenEagleEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (livingEntity.isTame() && !livingEntity.isInvisible()) {
            int i = livingEntity.getCollarColor().getTextureDiffuseColor();
            VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(GOLDEN_EAGLE_COLLAR_LOCATION));
            this.getParentModel().renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, i);
        }
    }
}
