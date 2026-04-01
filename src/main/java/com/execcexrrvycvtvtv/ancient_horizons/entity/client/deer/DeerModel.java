package com.execcexrrvycvtvtv.ancient_horizons.entity.client.deer;

import com.execcexrrvycvtvtv.ancient_horizons.AncientHorizons;
import com.execcexrrvycvtvtv.ancient_horizons.entity.custom.mob.DeerEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class DeerModel<T extends DeerEntity> extends AgeableListModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(AncientHorizons.MOD_ID, "deer"), "main");
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart antlers;
    private final ModelPart legleftfront;
    private final ModelPart legrightfront;
    private final ModelPart legrightback;
    private final ModelPart legleftback;

    public DeerModel(ModelPart root) {
        super(true, 16.0F, 3.35F, 2.0F, 2.0F, 24.0F);
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.antlers = this.head.getChild("antlers");
        this.legleftfront = root.getChild("legleftfront");
        this.legrightfront = root.getChild("legrightfront");
        this.legrightback = root.getChild("legrightback");
        this.legleftback = root.getChild("legleftback");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -5.0F, -9.0F, 10.0F, 10.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 40).addBox(-3.0F, -12.0F, -2.0F, 6.0F, 13.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(56, 15).addBox(-3.0F, 1.0F, -2.0F, 6.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(56, 9).addBox(3.0F, -11.0F, 1.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 12).addBox(-6.0F, -11.0F, 1.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 0).addBox(-2.0F, -10.0F, -6.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -9.0F));

        PartDefinition antlers = head.addOrReplaceChild("antlers", CubeListBuilder.create().texOffs(0, 28).addBox(0.5F, -6.0F, 0.0F, 9.0F, 5.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(56, 7).addBox(1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(56, 8).addBox(-3.0F, -1.0F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(32, 28).addBox(-9.5F, -6.0F, 0.0F, 9.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition legleftfront = partdefinition.addOrReplaceChild("legleftfront", CubeListBuilder.create().texOffs(50, 40).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 13.0F, -7.0F));

        PartDefinition legrightfront = partdefinition.addOrReplaceChild("legrightfront", CubeListBuilder.create().texOffs(50, 55).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 13.0F, -7.0F));

        PartDefinition legrightback = partdefinition.addOrReplaceChild("legrightback", CubeListBuilder.create().texOffs(22, 40).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 10.0F, 9.0F));

        PartDefinition legleftback = partdefinition.addOrReplaceChild("legleftback", CubeListBuilder.create().texOffs(36, 40).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 10.0F, 9.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        head.yRot   =  netHeadYaw  * Mth.DEG_TO_RAD;
        head.xRot   =  headPitch   * Mth.DEG_TO_RAD;

        antlers.visible = !entity.isBaby();

        legleftfront.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        legrightfront.xRot = -Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

        legleftback.xRot = -Mth.cos(limbSwing * 0.6662F + Mth.PI) * 1.4F * limbSwingAmount;
        legrightback.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 1.4F * limbSwingAmount;

    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(head);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(body, legleftback, legleftfront, legrightback, legrightfront);
    }
}
