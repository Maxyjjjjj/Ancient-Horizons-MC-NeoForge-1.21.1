package com.execcexrrvycvtvtv.ancient_horizons.entity.ai.nav;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.phys.Vec3;

public class BirdLookControl extends LookControl {

    private static final float AIR_YAW_SPEED   = 10.0F;
    private static final float AIR_PITCH_SPEED  =  8.0F;
    private static final float LAND_YAW_SPEED   =  7.5F;
    private static final float LAND_PITCH_SPEED  =  5.0F;
    private static final float IDLE_LEVEL_SPEED  =  1.5F;

    private final Mob bird;

    public BirdLookControl(Mob bird) {
        super(bird);
        this.bird = bird;
    }

    @Override
    public void tick() {
        if (lookAtCooldown > 0) {
            --lookAtCooldown;
            applyRotations();
        } else {
            idleTick();
        }
    }

    private void applyRotations() {
        double dx = wantedX - bird.getX();
        double dy = wantedY - (bird.getY() + bird.getEyeHeight());
        double dz = wantedZ - bird.getZ();

        double horizDist = Math.sqrt(dx * dx + dz * dz);

        float targetYaw   = (float)(Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
        float targetPitch = (float)(-Mth.atan2(dy, horizDist) * (180.0 / Math.PI));

        float yawSpeed, pitchSpeed, pitchMin, pitchMax;
        boolean isAirborne = bird.getDeltaMovement().y > 0.08 || !bird.onGround();

        if (!isAirborne) {
            yawSpeed  = LAND_YAW_SPEED;
            pitchSpeed = LAND_PITCH_SPEED;
            pitchMin  = -45.0F;
            pitchMax  =  45.0F;
        } else {
            yawSpeed  = AIR_YAW_SPEED;
            pitchSpeed = AIR_PITCH_SPEED;
            pitchMin  = -90.0F;
            pitchMax  =  90.0F;
        }

        bird.yHeadRot = rotateToward(bird.yHeadRot, targetYaw, yawSpeed);
        bird.setXRot(
                Mth.clamp(
                        rotateToward(bird.getXRot(), targetPitch, pitchSpeed),
                        pitchMin, pitchMax
                )
        );

        clampHeadRotationToBody();
    }

    private void idleTick() {
        bird.setXRot(
                rotateToward(bird.getXRot(), 0.0F, IDLE_LEVEL_SPEED)
        );
    }

    protected void clampHeadRotationToBody() {
        float bodyYaw = bird.getYRot();
        float headYaw = bird.yHeadRot;

        float delta = Mth.wrapDegrees(headYaw - bodyYaw);
        if (Math.abs(delta) > 30.0F) {
            bird.yHeadRot = bodyYaw + Math.signum(delta) * 30.0F;
        }
    }

    private static float rotateToward(float current, float target, float maxDelta) {
        float delta   = Mth.wrapDegrees(target - current);
        float clamped = Mth.clamp(delta, -maxDelta, maxDelta);
        return current + clamped;
    }

    public void setLookAt(Vec3 pos) {
        setLookAt(pos.x, pos.y, pos.z);
    }

    public void setLookAt(net.minecraft.world.entity.Entity target) {
        setLookAt(target.getX(),
                target.getY() + target.getEyeHeight(),
                target.getZ());
    }
}