package com.execcexrrvycvtvtv.ancient_horizons.entity.client.deer;

import com.execcexrrvycvtvtv.ancient_horizons.AncientHorizons;
import com.execcexrrvycvtvtv.ancient_horizons.entity.custom.mob.DeerEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class DeerCollarLayer extends RenderLayer<DeerEntity, DeerModel<DeerEntity>> {

    private static final ResourceLocation DEER_COLLAR_LOCATION = ResourceLocation.fromNamespaceAndPath(AncientHorizons.MOD_ID, "textures/entity/deer/deer_borys_collar.png");

    public DeerCollarLayer(MobRenderer<DeerEntity, DeerModel<DeerEntity>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, DeerEntity entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isBorys()) {
            VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(DEER_COLLAR_LOCATION));
            this.getParentModel().renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
        }
    }
}