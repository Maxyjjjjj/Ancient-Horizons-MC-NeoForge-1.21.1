package com.execcexrrvycvtvtv.ancient_horizons.entity.client.tiger;

import com.execcexrrvycvtvtv.ancient_horizons.AncientHorizons;
import com.execcexrrvycvtvtv.ancient_horizons.entity.custom.mob.TigerEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TigerModel<T extends TigerEntity> extends AgeableListModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(AncientHorizons.MOD_ID, "tiger"), "main");
    private final ModelPart body;
    private final ModelPart belly;
    private final ModelPart angerfur;
    private final ModelPart head;
    private final ModelPart muzzle;
    private final ModelPart jaw;
    private final ModelPart tongue;
    private final ModelPart eyesangry;
    private final ModelPart eyesclosed;
    private final ModelPart earleft;
    private final ModelPart earleft2;
    private final ModelPart pawleftfront;
    private final ModelPart clawsleftfront;
    private final ModelPart pawrightfront;
    private final ModelPart clawsrightfront;
    private final ModelPart pawrightback;
    private final ModelPart clawsrightback;
    private final ModelPart pawleftback;
    private final ModelPart clawsleftback;
    private final ModelPart tail;

    private static final float DEFAULT_TAIL_X = -1.0036F;

    public TigerModel(ModelPart root) {
        super(true, 3.0F, 3.0F, 1.4F, 0.5F, 0.0F);
        this.body = root.getChild("body");
        this.belly = this.body.getChild("belly");
        this.angerfur = this.belly.getChild("angerfur");
        this.head = this.body.getChild("head");
        this.muzzle = this.head.getChild("muzzle");
        this.jaw = this.head.getChild("jaw");
        this.tongue = this.jaw.getChild("tongue");
        this.eyesangry = this.head.getChild("eyesangry");
        this.eyesclosed = this.head.getChild("eyesclosed");
        this.earleft = this.head.getChild("earleft");
        this.earleft2 = this.head.getChild("earleft2");
        this.pawleftfront = this.body.getChild("pawleftfront");
        this.clawsleftfront = this.pawleftfront.getChild("clawsleftfront");
        this.pawrightfront = this.body.getChild("pawrightfront");
        this.clawsrightfront = this.pawrightfront.getChild("clawsrightfront");
        this.pawrightback = this.body.getChild("pawrightback");
        this.clawsrightback = this.pawrightback.getChild("clawsrightback");
        this.pawleftback = this.body.getChild("pawleftback");
        this.clawsleftback = this.pawleftback.getChild("clawsleftback");
        this.tail = this.body.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, 0.0F));

        PartDefinition belly = body.addOrReplaceChild("belly", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -6.0F, -18.0F, 11.0F, 11.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 11.0F));

        PartDefinition angerfur = belly.addOrReplaceChild("angerfur", CubeListBuilder.create().texOffs(52, 33).addBox(-0.5F, -1.0F, -20.0F, 0.0F, 1.0F, 19.0F, new CubeDeformation(0.0F))
                .texOffs(36, 58).addBox(-4.5F, -1.0F, -19.0F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 82).addBox(-5.5F, -2.0F, -17.0F, 10.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(20, 82).addBox(-5.5F, -2.0F, -15.0F, 10.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(36, 59).addBox(-4.5F, -1.0F, -13.0F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 3.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(52, 53).addBox(-6.0F, -5.0F, -7.0F, 12.0F, 9.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 58).addBox(-9.0F, -2.0F, -3.0F, 18.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -7.0F));

        PartDefinition muzzle = head.addOrReplaceChild("muzzle", CubeListBuilder.create().texOffs(40, 69).addBox(-3.5F, 0.0F, -5.0F, 6.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(60, 77).addBox(-3.0F, 3.0F, -4.75F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.05F)), PartPose.offset(0.5F, -2.0F, -7.0F));

        PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(62, 69).addBox(-3.0F, 0.0F, -5.0F, 6.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(40, 77).addBox(-2.5F, -2.0F, -4.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.025F)), PartPose.offset(0.0F, 1.0F, -7.0F));

        PartDefinition tongue = jaw.addOrReplaceChild("tongue", CubeListBuilder.create().texOffs(80, 77).addBox(-2.5F, -0.5F, -4.0F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition eyesangry = head.addOrReplaceChild("eyesangry", CubeListBuilder.create().texOffs(84, 3).addBox(1.5F, -1.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(84, 6).addBox(-4.5F, -1.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -6.05F));

        PartDefinition eyesclosed = head.addOrReplaceChild("eyesclosed", CubeListBuilder.create().texOffs(84, 9).addBox(2.0F, -0.5F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(84, 9).addBox(-4.0F, -0.5F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, -6.05F));

        PartDefinition earleft = head.addOrReplaceChild("earleft", CubeListBuilder.create().texOffs(40, 66).addBox(-1.5F, -2.0F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, -5.0F, -1.5F));

        PartDefinition earleft2 = head.addOrReplaceChild("earleft2", CubeListBuilder.create().texOffs(84, 0).addBox(-1.5F, -2.0F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.5F, -5.0F, -1.5F));

        PartDefinition pawleftfront = body.addOrReplaceChild("pawleftfront", CubeListBuilder.create().texOffs(64, 0).addBox(-2.5F, 0.0F, -3.0F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.025F)), PartPose.offset(3.0F, 5.0F, -4.0F));

        PartDefinition clawsleftfront = pawleftfront.addOrReplaceChild("clawsleftfront", CubeListBuilder.create().texOffs(36, 60).addBox(-2.5F, 0.0F, -0.025F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, -3.025F));

        PartDefinition pawrightfront = body.addOrReplaceChild("pawrightfront", CubeListBuilder.create().texOffs(64, 16).addBox(-2.5F, 0.0F, -3.0F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.025F)), PartPose.offset(-3.0F, 5.0F, -4.0F));

        PartDefinition clawsrightfront = pawrightfront.addOrReplaceChild("clawsrightfront", CubeListBuilder.create().texOffs(36, 63).addBox(-2.5F, 0.0F, -0.025F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, -3.025F));

        PartDefinition pawrightback = body.addOrReplaceChild("pawrightback", CubeListBuilder.create().texOffs(0, 66).addBox(-2.5F, 0.0F, -3.0F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.025F)), PartPose.offset(-3.0F, 5.0F, 12.0F));

        PartDefinition clawsrightback = pawrightback.addOrReplaceChild("clawsrightback", CubeListBuilder.create().texOffs(80, 82).addBox(-2.5F, 0.0F, -0.025F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, -3.025F));

        PartDefinition pawleftback = body.addOrReplaceChild("pawleftback", CubeListBuilder.create().texOffs(20, 66).addBox(-2.5F, 0.0F, -3.0F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.025F)), PartPose.offset(3.0F, 5.0F, 12.0F));

        PartDefinition clawsleftback = pawleftback.addOrReplaceChild("clawsleftback", CubeListBuilder.create().texOffs(0, 84).addBox(-2.5F, 0.0F, -0.025F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, -3.025F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 14.0F, -1.0036F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {

        resetPose();

        boolean isAngry   = entity.isAngry();
        boolean isSleeping = entity.isTigerSleeping();
        boolean isSneak    = entity.isCrouching();

        eyesangry.visible  = isAngry && !isSleeping;
        eyesclosed.visible = isSleeping;

        if (isSleeping) {
            animSleep(ageInTicks);
        } else if (entity.isAttacking()) {
            animAttack(ageInTicks);
        } else if (entity.isTearing()) {
            animTear(ageInTicks);
        } else if (entity.isPouncing()) {
            animPounce(ageInTicks);
        } else if (isAngry) {
            animAngry(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        } else if (entity.isDancing()) {
            animDance(ageInTicks);
        } else if (entity.isYawning()) {
            animYawn(ageInTicks);
        } else if (isSneak) {
            animSneak(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        } else if (entity.isJumping()) {
            animJump(ageInTicks);
        } else if (entity.isSprinting()) {
            animGallop(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        } else {
            animWalk(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }

        if (!isSleeping) {
            head.yRot = netHeadYaw  * (Mth.PI / 180F);
            head.xRot = headPitch   * (Mth.PI / 180F);
        }
    }

    private void resetPose() {
        body.xRot = 0F; body.yRot = 0F; body.zRot = 0F;
        body.x = 0F;    body.y = 8F;    body.z = 0F;

        belly.xRot = 0F; belly.yRot = 0F; belly.zRot = 0F;

        angerfur.xRot = 0F; angerfur.yRot = 0F; angerfur.zRot = 0F;
        angerfur.visible = false;

        head.xRot = 0F; head.yRot = 0F; head.zRot = 0F;
        head.y = -3F; head.z = -7F;

        jaw.xRot  = 0F;
        tongue.visible = false;

        earleft.zRot  = 0F;
        earleft2.zRot = 0F;

        eyesangry.visible  = false;
        eyesclosed.visible = false;

        clawsleftfront.xRot  = 0F;
        clawsrightfront.xRot = 0F;
        clawsleftback.xRot   = 0F;
        clawsrightback.xRot  = 0F;

        pawleftfront.xRot  = 0F; pawleftfront.yRot  = 0F;
        pawrightfront.xRot = 0F; pawrightfront.yRot = 0F;
        pawleftback.xRot   = 0F; pawleftback.yRot   = 0F;
        pawrightback.xRot  = 0F; pawrightback.yRot  = 0F;

        tail.xRot = DEFAULT_TAIL_X;
        tail.yRot = 0F; tail.zRot = 0F;
    }

    private void animWalk(float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {

        float swing = Mth.cos(limbSwing * 0.6662F);

        pawleftfront.xRot  =  swing * 0.8F * limbSwingAmount;
        pawrightfront.xRot = -swing * 0.8F * limbSwingAmount;
        pawleftback.xRot   = -swing * 0.8F * limbSwingAmount;
        pawrightback.xRot  =  swing * 0.8F * limbSwingAmount;

        body.zRot = Mth.cos(limbSwing * 0.6662F) * 0.05F * limbSwingAmount;

        tail.yRot = Mth.cos(ageInTicks * 0.15F) * 0.3F;
        tail.xRot = DEFAULT_TAIL_X + Mth.cos(limbSwing * 0.6662F) * 0.2F * limbSwingAmount;
    }

    private void animGallop(float limbSwing, float limbSwingAmount,
                            float ageInTicks, float netHeadYaw, float headPitch) {

        float bound = Mth.cos(limbSwing * 0.4F);

        float frontSwing = bound * 1.1F * limbSwingAmount;
        pawleftfront.xRot  = frontSwing;
        pawrightfront.xRot = frontSwing;

        float backSwing = Mth.cos(limbSwing * 0.4F + Mth.PI) * 1.1F * limbSwingAmount;
        pawleftback.xRot  = backSwing;
        pawrightback.xRot = backSwing;

        body.xRot = bound * 0.08F * limbSwingAmount;

        head.xRot += bound * 0.06F * limbSwingAmount;

        tail.xRot = DEFAULT_TAIL_X - 0.4F + Mth.cos(ageInTicks * 0.2F) * 0.15F;
        tail.yRot = Mth.cos(ageInTicks * 0.2F) * 0.15F;
    }

    private void animAngry(float limbSwing, float limbSwingAmount,
                           float ageInTicks, float netHeadYaw, float headPitch) {

        animWalk(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        head.xRot += 0.35F;
        head.y    += 1.5F;

        angerfur.visible = true;
        angerfur.y    = -2F;

        earleft.zRot  =  0.35F;
        earleft2.zRot = -0.35F;

        extendClaws(0.25F);

        jaw.xRot = 0.15F + Mth.cos(ageInTicks * 0.1F) * 0.05F;

        tail.yRot = Mth.cos(ageInTicks * 0.35F) * 0.6F;
        tail.xRot = DEFAULT_TAIL_X + 0.15F;
    }

    private void animSleep(float ageInTicks) {
        float breathe = Mth.cos(ageInTicks * 0.04F) * 0.03F;

        body.zRot = Mth.HALF_PI - 0.3F;
        body.y    = 11F;
        body.xRot = breathe;

        head.xRot = Mth.HALF_PI * 0.5F;
        head.yRot = 0.3F;
        head.y    = -3F + 2F;

        jaw.xRot = 0.1F;

        pawleftfront.xRot   = -Mth.HALF_PI * 0.6F;
        pawrightfront.xRot  = -Mth.HALF_PI * 0.6F;
        pawleftback.xRot    =  Mth.HALF_PI * 0.5F;
        pawrightback.xRot   =  Mth.HALF_PI * 0.5F;

        tail.xRot = DEFAULT_TAIL_X + 1.2F;
        tail.yRot = 0.6F;
    }

    private void animAttack(float ageInTicks) {
        float swipeProgress = Mth.sin(ageInTicks * 0.5F);

        if (swipeProgress >= 0F) {
            pawleftfront.xRot = -1.2F + swipeProgress * 1.8F;
            pawleftfront.yRot = -0.2F;
            pawrightfront.xRot = 0.2F;
        } else {
            pawrightfront.xRot = -1.2F + (-swipeProgress) * 1.8F;
            pawrightfront.yRot = 0.2F;
            pawleftfront.xRot = 0.2F;
        }

        head.xRot = 0.3F + Mth.abs(swipeProgress) * 0.2F;

        extendClaws(0.6F);
        angerfur.visible = true;
        angerfur.y = -2F;

        body.xRot = 0.1F;
        body.y    = 8F + 0.8F;

        eyesangry.visible = true;
    }

    private void animSneak(float limbSwing, float limbSwingAmount,
                           float ageInTicks, float netHeadYaw, float headPitch) {
        body.xRot = 0.35F;
        body.y    = 8F + 3.5F;

        float swing = Mth.cos(limbSwing * 0.6662F) * 0.6F * limbSwingAmount;
        pawleftfront.xRot = swing;
        pawrightfront.xRot = -swing;
        pawleftback.xRot  = -swing;
        pawrightback.xRot = swing;

        head.xRot = -0.3F;
        head.y    = -3F + 1F;

        tail.xRot = DEFAULT_TAIL_X - 0.5F;
        tail.yRot = Mth.cos(ageInTicks * 0.25F) * 0.2F;
    }

    private void animTear(float ageInTicks) {
        float tear = Mth.sin(ageInTicks * 0.7F);

        head.xRot = 0.6F + tear * 0.5F;
        head.y    = -3F + 1.5F + tear;

        jaw.xRot = 0.5F + Mth.abs(tear) * 0.4F;
        tongue.visible = (tear > 0.3F);

        pawleftfront.xRot   =  0.3F;
        pawleftfront.yRot   =  0.25F;
        pawrightfront.xRot  =  0.3F;
        pawrightfront.yRot  = -0.25F;

        pawleftback.xRot  = 0.2F;
        pawrightback.xRot = 0.2F;

        extendClaws(0.6F);
        angerfur.visible = true;
        angerfur.xRot    = -0.5F;
        eyesangry.visible = true;

        tail.yRot = Mth.cos(ageInTicks * 0.4F) * 0.8F;
        tail.xRot = DEFAULT_TAIL_X + 0.3F;

        body.xRot = 0.25F;
        body.y    = 8F + 2F;
    }

    private void animJump(float ageInTicks) {
        pawleftfront.xRot  = -0.9F;
        pawrightfront.xRot = -0.9F;

        pawleftback.xRot  = 0.7F;
        pawrightback.xRot = 0.7F;

        body.xRot = -0.15F;

        tail.xRot = DEFAULT_TAIL_X - 0.5F;

        head.xRot = -0.2F;
    }

    private void animPounce(float ageInTicks) {
        pawleftfront.xRot  = -1.2F;
        pawleftfront.yRot  =  0.3F;
        pawrightfront.xRot = -1.2F;
        pawrightfront.yRot = -0.3F;

        float kick = Mth.sin(ageInTicks * 0.5F) * 0.4F;
        pawleftback.xRot  = 0.9F + kick;
        pawrightback.xRot = 0.9F + kick;

        body.xRot = -0.3F;

        extendClaws(0.7F);
        angerfur.visible  = true;
        angerfur.xRot     = -0.5F;
        eyesangry.visible = true;

        jaw.xRot = 0.2F;

        tail.xRot = DEFAULT_TAIL_X - 0.6F;
        tail.yRot = 0F;
    }

    private void animYawn(float ageInTicks) {
        float t = (ageInTicks % 60F) / 60F;
        float envelope = Mth.sin(t * Mth.PI);

        jaw.xRot       = envelope * 0.85F;
        tongue.visible = (envelope > 0.4F);

        head.xRot = -envelope * 0.3F;

        eyesclosed.visible = (envelope > 0.5F);

        earleft.zRot  =  envelope * 0.2F;
        earleft2.zRot = -envelope * 0.2F;

        tail.xRot = DEFAULT_TAIL_X - envelope * 0.3F;
    }

    private void animDance(float ageInTicks) {
        float bounce = Mth.sin(ageInTicks * 0.3F);
        float rock   = Mth.cos(ageInTicks * 0.3F);

        body.zRot = rock * 0.25F;
        body.y    = 8F + Mth.abs(bounce) * 1.5F;

        pawleftfront.xRot  = -bounce * 0.9F;
        pawrightfront.xRot =  bounce * 0.9F;

        pawleftback.xRot  =  bounce * 0.2F;
        pawrightback.xRot = -bounce * 0.2F;

        head.zRot = rock * 0.2F;
        head.xRot = Mth.abs(bounce) * 0.1F - 0.1F;

        earleft.zRot  =  bounce * 0.3F;
        earleft2.zRot = -bounce * 0.3F;

        tail.yRot = Mth.cos(ageInTicks * 0.5F) * 0.9F;
        tail.xRot = DEFAULT_TAIL_X - 0.3F;
    }

    private void extendClaws(float amount) {
        clawsleftfront.xRot  = amount;
        clawsrightfront.xRot = amount;
        clawsleftback.xRot   = amount;
        clawsrightback.xRot  = amount;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(head, eyesangry, earleft, earleft2, eyesclosed, muzzle, jaw, tongue);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(belly, angerfur, pawleftback, pawleftfront, pawrightback, pawrightfront, clawsleftback, clawsleftfront, clawsrightback, clawsrightfront, tail);
    }
}