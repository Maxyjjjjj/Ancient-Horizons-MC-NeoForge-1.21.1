package com.execcexrrvycvtvtv.ancient_horizons.entity.client.tiger;

import com.execcexrrvycvtvtv.ancient_horizons.AncientHorizons;
import com.execcexrrvycvtvtv.ancient_horizons.entity.custom.mob.TigerEntity;
import com.execcexrrvycvtvtv.ancient_horizons.entity.variants.TigerVariant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class TigerRenderer extends MobRenderer<TigerEntity, TigerModel<TigerEntity>> {

    private static final Map<TigerVariant, ResourceLocation> VARIANT_TEXTURES = Map.of(
            TigerVariant.LEGENDS, tex("tiger/tiger_legends.png"),
            TigerVariant.BLUE,    tex("tiger/tiger_blue.png"),
            TigerVariant.TABBY,   tex("tiger/tiger_tabby.png"),
            TigerVariant.WHITE,   tex("tiger/tiger_white.png"),
            TigerVariant.REGULAR, tex("tiger/tiger.png")
    );

    public TigerRenderer(EntityRendererProvider.Context context) {
        super(context, new TigerModel<>(context.bakeLayer(TigerModel.LAYER_LOCATION)), 0.685f);
        this.addLayer(new TigerCollarLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(TigerEntity tiger) {
        return VARIANT_TEXTURES.getOrDefault(tiger.getVariant(), VARIANT_TEXTURES.get(TigerVariant.REGULAR));
    }

    private static ResourceLocation tex(String path) {
        return ResourceLocation.fromNamespaceAndPath(AncientHorizons.MOD_ID, "textures/entity/" + path);
    }
}