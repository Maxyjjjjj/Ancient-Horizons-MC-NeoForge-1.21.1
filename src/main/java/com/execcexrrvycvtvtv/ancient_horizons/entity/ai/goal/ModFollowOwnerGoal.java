package com.execcexrrvycvtvtv.ancient_horizons.entity.ai.goal;

import com.execcexrrvycvtvtv.ancient_horizons.entity.ai.nav.BirdLookControl;
import com.execcexrrvycvtvtv.ancient_horizons.entity.ai.nav.BirdPathNavigation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class ModFollowOwnerGoal extends Goal {

    private static final int RECALC_INTERVAL = 10;

    private static final double HOVER_HEIGHT_ABOVE_OWNER = 2.5;

    private static final double SIGNIFICANT_Y_DIFF = 4.0;

    private static final double LANDING_APPROACH_DIST_SQ = 9.0;

    private final TamableAnimal tamable;
    @Nullable private LivingEntity owner;
    private final double speedModifier;
    private final float startDistance;
    private final float stopDistance;
    private int timeToRecalcPath;

    public ModFollowOwnerGoal(
            TamableAnimal tamable,
            double speedModifier,
            float startDistance,
            float stopDistance
    ) {
        this.tamable = tamable;
        this.speedModifier = speedModifier;
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity candidate = tamable.getOwner();
        if (candidate == null) return false;
        if (tamable.isOrderedToSit()) return false;
        if (tamable.unableToMoveToOwner()) return false;
        if (distSq(candidate) < sq(startDistance)) return false;
        owner = candidate;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (owner == null) return false;
        if (tamable.isOrderedToSit()) return false;
        if (tamable.unableToMoveToOwner()) return false;
        if (distSq(owner) <= sq(stopDistance)) return false;
        PathNavigation nav = tamable.getNavigation();
        return !nav.isDone() || distSq(owner) > sq(stopDistance);
    }

    @Override
    public void start() {
        timeToRecalcPath = 0;
    }

    @Override
    public void stop() {
        owner = null;
        tamable.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (owner == null) return;

        updateLookControl();

        if (tamable.shouldTryTeleportToOwner()) {
            tamable.tryToTeleportToOwner();
            timeToRecalcPath = RECALC_INTERVAL;
            return;
        }

        if (--timeToRecalcPath > 0) return;
        timeToRecalcPath = adjustedTickDelay(RECALC_INTERVAL);

        navigateToOwner();
    }

    private void navigateToOwner() {
        PathNavigation nav = tamable.getNavigation();

        if (nav instanceof BirdPathNavigation birdNav) {
            navigateWithBirdNav(birdNav);
        } else {
            nav.moveTo(owner, speedModifier);
        }
    }

    private void navigateWithBirdNav(BirdPathNavigation birdNav) {
        double distSqToOwner = distSq(owner);
        double yDiff         = tamable.getY() - owner.getY();
        boolean ownerOnGround = owner.onGround();
        boolean birdAirborne = birdNav.isAirborne();

        if (distSqToOwner < LANDING_APPROACH_DIST_SQ) {
            BlockPos landTarget = owner.blockPosition().offset(
                    tamable.getRandom().nextInt(3) - 1,
                    0,
                    tamable.getRandom().nextInt(3) - 1
            );
            birdNav.moveToLand(landTarget, speedModifier);
            return;
        }

        if (birdAirborne && ownerOnGround && yDiff < -SIGNIFICANT_Y_DIFF) {
            birdNav.moveToAbove(owner.blockPosition(), HOVER_HEIGHT_ABOVE_OWNER, speedModifier);
            return;
        }

        if (birdAirborne) {
            double targetY = owner.getY() + (ownerOnGround ? HOVER_HEIGHT_ABOVE_OWNER : 1.0);
            birdNav.moveTo(owner.getX(), targetY, owner.getZ(), speedModifier);
            return;
        }

        birdNav.moveTo(owner, speedModifier);
    }

    private void updateLookControl() {
        if (tamable.getLookControl() instanceof BirdLookControl birdLook) {
            birdLook.setLookAt(owner);
        } else {
            tamable.getLookControl().setLookAt(
                    owner,
                    (float) tamable.getMaxHeadYRot(),
                    (float) tamable.getMaxHeadXRot()
            );
        }
    }

    private double distSq(LivingEntity entity) {
        return tamable.distanceToSqr(entity);
    }

    private static double sq(float v) {
        return (double) v * v;
    }
}