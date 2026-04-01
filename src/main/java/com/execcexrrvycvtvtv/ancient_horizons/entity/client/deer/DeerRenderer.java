package com.execcexrrvycvtvtv.ancient_horizons.entity.client.deer;

import com.execcexrrvycvtvtv.ancient_horizons.AncientHorizons;
import com.execcexrrvycvtvtv.ancient_horizons.entity.custom.mob.DeerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DeerRenderer extends MobRenderer<DeerEntity, DeerModel<DeerEntity>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(AncientHorizons.MOD_ID, "textures/entity/deer/deer.png");

    public DeerRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new DeerModel<>(ctx.bakeLayer(DeerModel.LAYER_LOCATION)), 0.625F);
        this.addLayer(new DeerCollarLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(DeerEntity entity) {
        return TEXTURE;
    }
}