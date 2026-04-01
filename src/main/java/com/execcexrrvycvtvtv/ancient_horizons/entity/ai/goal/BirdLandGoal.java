package com.execcexrrvycvtvtv.ancient_horizons.entity.ai.goal;

import com.execcexrrvycvtvtv.ancient_horizons.entity.ai.nav.BirdPathNavigation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class BirdLandGoal extends Goal {

    private static final int MIN_FLIGHT_TICKS   = 100;
    private static final int SEARCH_RADIUS      = 16;
    private static final int SEARCH_DEPTH       = 20;
    private static final float LAND_CHANCE       = 0.004F;
    private static final double APPROACH_HEIGHT  = 6.0;
    private static final double DESCENT_TRIGGER_DIST_SQ = 4.0 * 4.0;
    private static final int SEARCH_ATTEMPTS     = 20;

    private enum Phase { APPROACH, DESCENT, DONE }

    protected final Mob mob;
    protected final double speedModifier;

    private int airborneTimer = 0;
    private Phase phase = Phase.DONE;
    @Nullable private BlockPos perch = null;

    public BirdLandGoal(Mob mob, double speedModifier) {
        this.mob           = mob;
        this.speedModifier = speedModifier;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    private boolean isAirborne() {
        return mob.getDeltaMovement().y > 0.08 || !mob.onGround();
    }

    @Override
    public boolean canUse() {
        if (!isAirborne()) {
            airborneTimer = 0;
            return false;
        }
        airborneTimer++;
        if (airborneTimer < MIN_FLIGHT_TICKS) return false;
        if (mob.getRandom().nextFloat() > LAND_CHANCE * airborneTimer / 200f) return false;

        perch = findPerch();
        return perch != null;
    }

    @Override
    public boolean canContinueToUse() {
        return perch != null && phase != Phase.DONE && isAirborne();
    }

    @Override
    public void start() {
        phase = Phase.APPROACH;
        navigateToApproach();
    }

    @Override
    public void tick() {
        if (perch == null) { stop(); return; }

        switch (phase) {
            case APPROACH -> tickApproach();
            case DESCENT  -> tickDescent();
            case DONE     -> {}
        }

        if (perch != null && !isValidPerch(mob.level(), perch)) {
            stop();
        }
    }

    @Override
    public void stop() {
        perch = null;
        phase = Phase.DONE;
        airborneTimer = 0;
    }

    private void tickApproach() {
        BirdPathNavigation nav = getNavigation();
        if (nav == null) return;
        if (perch != null) {
            double dx = mob.getX() - (perch.getX() + 0.5);
            double dz = mob.getZ() - (perch.getZ() + 0.5);
            boolean overPerch = (dx * dx + dz * dz) < DESCENT_TRIGGER_DIST_SQ;
            boolean lowEnough = mob.getY() <= perch.getY() + APPROACH_HEIGHT + 2.0;

            if (overPerch && lowEnough) {
                phase = Phase.DESCENT;
                navigateToDescent();
            } else if (nav.isDone()) {
                navigateToApproach();
            }
        }
    }

    private void tickDescent() {
        BirdPathNavigation nav = getNavigation();
        if (nav == null) return;

        double distSq = mob.distanceToSqr(
                perch.getX() + 0.5,
                perch.getY(),
                perch.getZ() + 0.5
        );

        if (distSq < 6.0 && nav.isDone()) {
            Vec3 delta = mob.getDeltaMovement();
            mob.setDeltaMovement(
                    delta.x * 0.8,
                    Math.max(delta.y - 0.05, -0.35),
                    delta.z * 0.8
            );
        }

        if (!isAirborne()) {
            phase = Phase.DONE;
            airborneTimer = 0;
            onTouchdown(perch);
        }
    }

    private void navigateToApproach() {
        if (perch == null) return;
        BirdPathNavigation nav = getNavigation();
        if (nav == null) return;
        nav.moveToAbove(perch, APPROACH_HEIGHT, speedModifier);
    }

    private void navigateToDescent() {
        if (perch == null) return;
        BirdPathNavigation nav = getNavigation();
        if (nav == null) return;
        nav.moveToLand(perch, speedModifier);
    }

    @Nullable
    private BlockPos findPerch() {
        Level level       = mob.level();
        RandomSource rng  = mob.getRandom();
        int birdX         = (int) mob.getX();
        int birdZ         = (int) mob.getZ();
        int birdY         = (int) mob.getY();

        for (int attempt = 0; attempt < SEARCH_ATTEMPTS; attempt++) {
            int tx = birdX + rng.nextInt(SEARCH_RADIUS * 2) - SEARCH_RADIUS;
            int tz = birdZ + rng.nextInt(SEARCH_RADIUS * 2) - SEARCH_RADIUS;
            int startY = Math.min(birdY + 6, level.getMaxBuildHeight() - 1);

            for (int ty = startY; ty >= startY - SEARCH_DEPTH; ty--) {
                BlockPos candidate = new BlockPos(tx, ty, tz);
                if (isValidPerch(level, candidate)) {
                    return candidate;
                }
            }
        }
        return null;
    }

    protected boolean isValidPerch(Level level, BlockPos pos) {
        BlockState surface = level.getBlockState(pos);
        if (!surface.isFaceSturdy(level, pos, Direction.UP)) return false;

        BlockPos above1 = pos.above();
        BlockPos above2 = pos.above(2);
        if (!level.getBlockState(above1).isAir()) return false;
        if (!level.getBlockState(above2).isAir()) return false;

        for (Direction dir : Direction.values()) {
            BlockState neighbour = level.getBlockState(pos.relative(dir));
            if (neighbour.getBlock() == net.minecraft.world.level.block.Blocks.LAVA
                    || neighbour.getBlock() == net.minecraft.world.level.block.Blocks.FIRE
                    || neighbour.getBlock() == net.minecraft.world.level.block.Blocks.SOUL_FIRE) {
                return false;
            }
        }
        return true;
    }

    protected void onTouchdown(BlockPos landedOn) {
    }

    @Nullable
    private BirdPathNavigation getNavigation() {
        if (mob.getNavigation() instanceof BirdPathNavigation bn) return bn;
        return null;
    }
}