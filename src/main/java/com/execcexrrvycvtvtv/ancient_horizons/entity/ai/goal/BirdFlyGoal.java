package com.execcexrrvycvtvtv.ancient_horizons.entity.ai.goal;

import com.execcexrrvycvtvtv.ancient_horizons.entity.ai.nav.BirdPathNavigation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class BirdFlyGoal extends Goal {

    private static final int   IDLE_TICKS_MIN    = 40;
    private static final int   IDLE_TICKS_MAX    = 100;
    private static final int   WANDER_RADIUS_H   = 24;
    private static final int   WANDER_RADIUS_V   = 8;
    private static final int   MIN_ALTITUDE       = 5;
    private static final int   MAX_ALTITUDE       = 200;

    protected final Mob mob;
    protected final double speedModifier;
    private final boolean requiresAirborne;

    private int idleTimer = 0;
    @Nullable private Vec3 currentTarget = null;

    private int stuckTimer = 0;
    private Vec3 lastPos = Vec3.ZERO;

    public BirdFlyGoal(Mob mob, double speedModifier, boolean requiresAirborne) {
        this.mob             = mob;
        this.speedModifier   = speedModifier;
        this.requiresAirborne = requiresAirborne;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        boolean isAirborne = mob.getDeltaMovement().y > 0.08 || !mob.onGround();
        if (requiresAirborne && !isAirborne) return false;
        return currentTarget == null && idleTimer <= 0;
    }

    @Override
    public boolean canContinueToUse() {
        BirdPathNavigation nav = getNavigation();
        if (nav == null) return false;
        return currentTarget != null && !nav.isDone() && idleTimer <= 0;
    }

    @Override
    public void start() {
        currentTarget = pickTarget();
        if (currentTarget != null) {
            navigate(currentTarget);
        }
    }

    @Override
    public void tick() {
        if (idleTimer > 0) {
            --idleTimer;
            return;
        }

        BirdPathNavigation nav = getNavigation();
        if (nav == null) return;

        if (nav.isDone() || currentTarget == null) {
            idleTimer = IDLE_TICKS_MIN
                    + mob.getRandom().nextInt(IDLE_TICKS_MAX - IDLE_TICKS_MIN);
            currentTarget = null;
        }

        if (currentTarget != null && nav.isDone()) {
            navigate(currentTarget);
        }

        if (mob.tickCount % 20 == 0) {
            if (mob.position().distanceToSqr(lastPos) < 0.05) {
                if (stuckTimer > 5) {
                    stuckTimer++;
                }
            } else {
                stuckTimer = 0;
            }
            lastPos = mob.position();
        }

        if (stuckTimer > 3) {
            this.stop();
            this.idleTimer = 40;
            stuckTimer = 0;
        }
    }

    @Override
    public void stop() {
        currentTarget = null;
    }

    @Nullable
    protected Vec3 pickTarget() {
        RandomSource rng = mob.getRandom();

        for (int attempt = 0; attempt < 10; attempt++) {
            double dx = (rng.nextDouble() * 2 - 1) * WANDER_RADIUS_H;
            double dz = (rng.nextDouble() * 2 - 1) * WANDER_RADIUS_H;
            double dy = (rng.nextDouble() * 2 - 1) * WANDER_RADIUS_V;

            double tx = mob.getX() + dx;
            double ty = Mth.clamp(mob.getY() + dy, minFlightY(), MAX_ALTITUDE);
            double tz = mob.getZ() + dz;

            if (isOpenAir(tx, ty, tz)) {
                return new Vec3(tx, ty, tz);
            }
        }
        return null;
    }

    protected double minFlightY() {
        return mob.level().getMinBuildHeight() + MIN_ALTITUDE;
    }

    protected boolean isOpenAir(double x, double y, double z) {
        BlockPos pos = BlockPos.containing(x, y, z);
        BlockState at    = mob.level().getBlockState(pos);
        BlockState above = mob.level().getBlockState(pos.above());
        return !at.isSolid() && !above.isSolid();
    }

    protected void navigate(Vec3 target) {
        BirdPathNavigation nav = getNavigation();
        if (nav != null) {
            nav.moveTo(target.x, target.y, target.z, speedModifier);
        }
    }

    @Nullable
    protected BirdPathNavigation getNavigation() {
        if (mob.getNavigation() instanceof BirdPathNavigation bn) return bn;
        return null;
    }
}
