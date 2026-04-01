package com.execcexrrvycvtvtv.ancient_horizons.entity.client.golden_eagle;

import com.execcexrrvycvtvtv.ancient_horizons.AncientHorizons;
import com.execcexrrvycvtvtv.ancient_horizons.entity.custom.mob.GoldenEagleEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GoldenEagleRenderer extends MobRenderer<GoldenEagleEntity, GoldenEagleModel<GoldenEagleEntity>> {

    private static final ResourceLocation ANGRY_GE = ResourceLocation.fromNamespaceAndPath(AncientHorizons.MOD_ID, "textures/entity/golden_eagle/golden_eagle_angry.png");
    private static final ResourceLocation NORMAL_GE = ResourceLocation.fromNamespaceAndPath(AncientHorizons.MOD_ID, "textures/entity/golden_eagle/golden_eagle.png");

    public GoldenEagleRenderer(EntityRendererProvider.Context context) {
        super(context, new GoldenEagleModel<>(context.bakeLayer(GoldenEagleModel.LAYER_LOCATION)), 0.685f);
        this.addLayer(new GoldenEagleCollarLayer(this));
        this.addLayer(new GoldenEagleItemLayer(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(GoldenEagleEntity goldenEagle) {
        return goldenEagle.isAggressive() ? ANGRY_GE : NORMAL_GE;
    }
}