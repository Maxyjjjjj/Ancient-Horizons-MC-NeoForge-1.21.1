package com.execcexrrvycvtvtv.ancient_horizons.entity.client.golden_eagle;

import com.execcexrrvycvtvtv.ancient_horizons.AncientHorizons;
import com.execcexrrvycvtvtv.ancient_horizons.entity.custom.mob.GoldenEagleEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class GoldenEagleModel<T extends GoldenEagleEntity> extends AgeableListModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(AncientHorizons.MOD_ID, "golden_eagle"), "main");
    private final ModelPart base;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart head_base;
    private final ModelPart head;
    private final ModelPart wingleft;
    private final ModelPart wingright;
    private final ModelPart legleft;
    private final ModelPart legright;

    public GoldenEagleModel(ModelPart root) {
        super(true, 16.0F, 4.0F, 2.0F, 2.0F, 24.0F);
        this.base = root.getChild("base");
        this.body = this.base.getChild("body");
        this.tail = this.body.getChild("tail");
        this.head_base = this.base.getChild("head_base");
        this.head = this.head_base.getChild("head");
        this.wingleft = this.body.getChild("wingleft");
        this.wingright = this.body.getChild("wingright");
        this.legleft = this.base.getChild("legleft");
        this.legright = this.base.getChild("legright");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = base.addOrReplaceChild("body", CubeListBuilder.create().texOffs(38, 0).addBox(-3.5F, -3.0F, -6.0F, 7.0F, 7.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(16, 50).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(38, 19).addBox(-5.0F, 0.0F, 1.0F, 10.0F, 0.0F, 9.0F, new CubeDeformation(0.025F)), PartPose.offsetAndRotation(0.0F, -1.0F, 6.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition head_base = base.addOrReplaceChild("head_base", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, -5.0F));

        head_base.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 50).addBox(-2.0F, -7.0F, -2.0F, 4.0F, 8.0F, 4.0F)
                        .texOffs(48, 62).addBox(-0.5F, -6.0F, -5.0F, 1.0F, 2.0F, 3.0F)
                        .texOffs(32, 50).addBox(0.0F, -4.0F, -5.0F, 0.0F, 1.0F, 1.0F),
                PartPose.ZERO);

        PartDefinition wingleft = body.addOrReplaceChild("wingleft", CubeListBuilder.create().texOffs(38, 45).addBox(0.0F, -1.0F, -1.0F, 1.0F, 5.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(0.5F, -2.0F, -1.0F, 0.0F, 6.0F, 19.0F, new CubeDeformation(0.025F)), PartPose.offsetAndRotation(3.5F, -1.0F, -4.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition wingright = body.addOrReplaceChild("wingright", CubeListBuilder.create().texOffs(38, 28).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 5.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 25).addBox(-0.5F, -2.0F, -1.0F, 0.0F, 6.0F, 19.0F, new CubeDeformation(0.025F)), PartPose.offsetAndRotation(-3.5F, -1.0F, -4.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition legleft = base.addOrReplaceChild("legleft", CubeListBuilder.create().texOffs(32, 62).addBox(-1.0F, 0.0F, -0.5F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.025F))
                .texOffs(16, 57).addBox(-1.5F, 5.0F, -2.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -6.0F, 0.0F));

        PartDefinition legright = base.addOrReplaceChild("legright", CubeListBuilder.create().texOffs(40, 62).addBox(-1.0F, 0.0F, -0.5F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.025F))
                .texOffs(0, 62).addBox(-1.5F, 5.0F, -2.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -6.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(head_base);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(body, legleft, legright);
    }

    @Override
    public void setupAnim(GoldenEagleEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        resetToDefaultPose();

        float rad = (float)Math.PI / 180F;

        this.head.xRot = headPitch * rad;
        this.head.yRot = netHeadYaw * rad;

        if (entity.isFlying()) {
            applyFlightPose(entity, ageInTicks, rad);
            this.head_base.z = -7.0F;
            this.head_base.y = -11.0F;

            if (entity.isDiving()) {
                applyDivePose(rad);
            }

            if (entity.isCatching()) {
                applyCatchPose(rad);
            }

            if (entity.isCarrying()) {
                this.legleft.xRot = 15.0F * rad;
                this.legright.xRot = 15.0F * rad;
                this.legleft.y -= 1.0F;
                this.legright.y -= 1.0F;
            }

            if (entity.hasItems()) {
                this.head.xRot += 0.1F;
            }
        } else {

        }

        if (entity.isOrderedToSit()) {
            this.base.y += 3.0F;
            this.legleft.y -= 3.0F;
            this.legright.y -= 3.0F;
        }
    }

    private void applyFlightPose(GoldenEagleEntity entity, float ageInTicks, float rad) {
        this.body.xRot += 10.0F * rad;
        this.head.xRot += -10.0F * rad;
        float flap = (float) (Math.sin(ageInTicks * 0.6F) * 0.5F);

        this.wingleft.xRot = -97.5F * rad;
        this.wingleft.yRot = -7.5F * rad;
        this.wingleft.zRot = -90.0F * rad + flap;
        this.wingleft.z += 2.0F;

        this.wingright.xRot = -97.5F * rad;
        this.wingright.yRot = 7.5F * rad;
        this.wingright.zRot = 90.0F * rad - flap;
        this.wingright.z += 2.0F;
    }

    private void applyDivePose(float rad) {
        this.wingleft.xRot = -17.5F * rad;
        this.wingleft.zRot = -90.0F * rad;
        this.wingleft.y += 1.0F;
        this.wingright.xRot = -17.5F * rad;
        this.wingright.zRot = 90.0F * rad;
        this.wingright.y += 1.0F;
    }

    private void applyCatchPose(float rad) {
        this.body.xRot = -20.0F * rad;
        this.head.xRot = 70.0F * rad;
        this.wingleft.xRot = -115.0F * rad;
        this.wingright.xRot = -115.0F * rad;
        this.legleft.xRot = -60.0F * rad;
        this.legright.xRot = -60.0F * rad;
        this.legleft.y = -6.0F;
        this.legright.y = -6.0F;
    }

    private void resetToDefaultPose() {
        this.body.xRot = -0.1745F;
        this.head.xRot = 0.1745F;
        this.wingleft.xRot = 0.1309F;
        this.wingleft.yRot = 0.0F;
        this.wingleft.zRot = 0.0F;
        this.wingleft.z = -4.0F;
        this.wingleft.y = -1.0F;
        this.wingright.xRot = 0.1309F;
        this.wingright.yRot = 0.0F;
        this.wingright.zRot = 0.0F;
        this.wingright.z = -4.0F;
        this.wingright.y = -1.0F;
        this.legleft.xRot = 0.0F;
        this.legleft.y = -6.0F;
        this.legright.xRot = 0.0F;
        this.legright.y = -6.0F;
    }
}
