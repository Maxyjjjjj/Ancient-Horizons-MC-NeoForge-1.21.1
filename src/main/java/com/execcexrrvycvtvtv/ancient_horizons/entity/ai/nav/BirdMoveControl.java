package com.execcexrrvycvtvtv.ancient_horizons.entity.ai.nav;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class BirdMoveControl extends MoveControl {

    public BirdMoveControl(Mob entity) {
        super(entity);
    }

    @Override
    public void tick() {
        if (this.operation == Operation.MOVE_TO) {
            this.operation = Operation.WAIT;

            double dx = this.wantedX - this.mob.getX();
            double dy = this.wantedY - this.mob.getY();
            double dz = this.wantedZ - this.mob.getZ();
            double distSq = dx * dx + dy * dy + dz * dz;

            if (distSq < 1.0E-4D) {
                this.mob.setZza(0.0F);
                return;
            }

            boolean isAirborne = !this.mob.onGround();

            if (isAirborne) {
                handleAirborne(dx, dy, dz, distSq);
            } else {
                handleGround(dx, dz);
            }
        } else {
            this.mob.setSpeed(0.0F);
            this.mob.setYya(0.0F);
            this.mob.setZza(0.0F);
        }
    }

    private void handleGround(double dx, double dz) {
        float targetYaw = (float) (Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
        this.mob.setYRot(this.rotlerp(this.mob.getYRot(), targetYaw, 10.0F));
        this.mob.setYBodyRot(this.mob.getYRot());

        float speed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
        this.mob.setSpeed(speed);
    }

    private void handleAirborne(double dx, double dy, double dz, double distSq) {
        double horizontalDist = Math.sqrt(dx * dx + dz * dz);
        float targetYaw = (float) (Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;

        this.mob.setYRot(this.rotlerp(this.mob.getYRot(), targetYaw, 10.0F));
        this.mob.setYBodyRot(this.mob.getYRot());

        float targetPitch = (float) (-(Mth.atan2(dy, horizontalDist) * (180.0 / Math.PI)));
        this.mob.setXRot(this.rotlerp(this.mob.getXRot(), targetPitch, 10.0F));

        double baseSpeed = this.mob.getAttributeValue(Attributes.FLYING_SPEED);
        float speed = (float) (this.speedModifier * baseSpeed);

        double distance = Math.sqrt(distSq);
        double verticalInertia = (dy / distance) * speed;

        Vec3 delta = this.mob.getDeltaMovement();
        double finalY = Mth.lerp(0.2, delta.y, verticalInertia);

        this.mob.setDeltaMovement(delta.x, finalY, delta.z);

        this.mob.setZza(speed);
    }
}