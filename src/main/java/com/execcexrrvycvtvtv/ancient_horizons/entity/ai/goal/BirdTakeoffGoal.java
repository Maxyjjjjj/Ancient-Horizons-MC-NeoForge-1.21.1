package com.execcexrrvycvtvtv.ancient_horizons.entity.ai.goal;

import com.execcexrrvycvtvtv.ancient_horizons.entity.ai.nav.BirdPathNavigation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class BirdTakeoffGoal extends Goal {

    private static final int    WINDUP_TICKS  = 12;
    private static final double LEAP_STRENGTH = 0.6;
    private static final int    CLIMB_HEIGHT  = 10;
    private static final int    CLEARANCE_NEEDED = 3;
    private static final float  TAKEOFF_CHANCE   = 0.008F;

    protected final Mob mob;
    protected final double speedModifier;

    private int windupTimer = 0;
    private boolean launched = false;
    private double launchY   = 0;

    public BirdTakeoffGoal(Mob mob, double speedModifier) {
        this.mob           = mob;
        this.speedModifier = speedModifier;
        setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
    }

    private boolean isAirborne() {
        return mob.getDeltaMovement().y > 0.08 || !mob.onGround();
    }

    @Override
    public boolean canUse() {
        if (isAirborne() || (mob instanceof TamableAnimal tamableAnimal && tamableAnimal.isOrderedToSit())) return false;

        LivingEntity target = mob.getTarget();
        if (target != null && target.isAlive()) {
            return true;
        }

        if (mob.getRandom().nextFloat() < TAKEOFF_CHANCE) return true;

        if (mob instanceof TamableAnimal tamable && tamable.isTame()) {
            LivingEntity owner = tamable.getOwner();
            if (owner != null && mob.distanceToSqr(owner) > 144.0) {
                return true;
            }
        }

        if (!hasClearance()) return false;

        return mob.getNavigation().isStuck()
                || (mob.getNavigation().isDone() && mob.tickCount % 40 == 0);
    }

    @Override
    public boolean canContinueToUse() {
        return !isAirborne() || mob.getY() < launchY + CLIMB_HEIGHT;
    }

    @Override
    public void start() {
        windupTimer = WINDUP_TICKS;
        launched    = false;
        launchY     = mob.getY();
        mob.setNoGravity(false);
        mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (windupTimer > 0) {
            --windupTimer;
            onWindup(windupTimer);
            return;
        }

        if (!launched) {
            launch();
        }
    }

    @Override
    public void stop() {
        windupTimer = 0;
        launched    = false;
    }

    private void launch() {
        launched = true;

        Vec3 current = mob.getDeltaMovement();
        mob.setDeltaMovement(current.x, LEAP_STRENGTH, current.z);
        mob.hasImpulse = true;


        if (mob.onGround()) {
            mob.getJumpControl().jump();
        }

        BlockPos climbTarget = BlockPos.containing(
                mob.getX(),
                launchY + CLIMB_HEIGHT,
                mob.getZ()
        );

        if (mob.getNavigation() instanceof BirdPathNavigation nav) {
            nav.moveTo(climbTarget.getX(), climbTarget.getY(), climbTarget.getZ(), speedModifier);
        }

        onLaunched();
    }

    private boolean hasClearance() {
        BlockPos base = mob.blockPosition().above();
        for (BlockPos pos : BlockPos.betweenClosed(base.offset(-1, 0, -1), base.offset(1, CLEARANCE_NEEDED, 1))) {
            if (!mob.level().isEmptyBlock(pos)) {
                return false;
            }
        }
        return true;
    }

    protected void onWindup(int ticksRemaining) {
    }

    protected void onLaunched() {
    }
}