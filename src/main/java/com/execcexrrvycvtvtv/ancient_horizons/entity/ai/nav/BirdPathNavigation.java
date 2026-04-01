package com.execcexrrvycvtvtv.ancient_horizons.entity.ai.nav;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class BirdPathNavigation extends PathNavigation {

    private static final int PATH_REFRESH_INTERVAL = 20;
    private static final double NODE_REACH_DIST_SQ  = 1.5 * 1.5;
    private static final double REPLAN_DIST_SQ       = 4.0 * 4.0;

    private boolean airborne = false;
    boolean isAirborne = mob.getDeltaMovement().y > 0.08 || !mob.onGround();
    private int refreshTimer = 0;
    @Nullable private Vec3 lastTargetPos = null;

    public BirdPathNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    protected PathFinder createPathFinder(int maxExpansions) {
        this.nodeEvaluator = new BirdNodeEvaluator();

        BirdNodeEvaluator evaluator = (BirdNodeEvaluator) this.nodeEvaluator;
        evaluator.setCanPassDoors(false);
        evaluator.setCanOpenDoors(false);

        return new PathFinder(evaluator, maxExpansions);
    }

    @Override
    public NodeEvaluator getNodeEvaluator() {
        return this.nodeEvaluator;
    }

    @Override
    protected boolean canUpdatePath() {
        return true;
    }

    @Override
    protected Vec3 getTempMobPos() {
        return mob.position();
    }

    @Override
    public void tick() {
        if (!hasPath()) {
            return;
        }

        airborne = isAirborne;

        if (++refreshTimer >= PATH_REFRESH_INTERVAL) {
            refreshTimer = 0;
            maybeReplan();
        }

        followPath();
    }

    private boolean hasPath() {
        return this.path != null && !this.path.isDone();
    }

    protected void followPath() {
        Path currentPath = this.path;
        if (currentPath == null || currentPath.isDone()) return;

        Vec3 mobPos = this.getTempMobPos();
        int currentIndex = currentPath.getNextNodeIndex();

        if (currentIndex >= currentPath.getNodeCount()) {
            this.stop();
            return;
        }

        Vec3 target = currentPath.getEntityPosAtNode(this.mob, currentIndex);

        double reachDist = isAirborne ? 2.0 : NODE_REACH_DIST_SQ;
        if (mobPos.distanceToSqr(target) < reachDist) {
            currentPath.advance();
            if (currentPath.isDone()) {
                this.stop();
                return;
            }
        }

        this.mob.getMoveControl().setWantedPosition(target.x, target.y, target.z, this.speedModifier);
    }

    private void maybeReplan() {
        Path path = getPath();
        if (path == null || path.isDone()) return;

        Vec3 finalNode = path.getEntityPosAtNode(mob, path.getNodeCount() - 1);
        if (lastTargetPos != null
                && finalNode.distanceToSqr(lastTargetPos) > REPLAN_DIST_SQ) {
            moveTo(finalNode.x, finalNode.y, finalNode.z, speedModifier);
        }
        lastTargetPos = finalNode;
    }

    @Override
    public boolean moveTo(double x, double y, double z, double speed) {
        lastTargetPos = new Vec3(x, y, z);
        return super.moveTo(x, y, z, speed);
    }

    @Override
    public boolean moveTo(Entity target, double speed) {
        return moveTo(target.getX(), target.getY() + 1.5, target.getZ(), speed);
    }

    public boolean isAirborne() {
        return airborne;
    }

    public boolean moveToAbove(BlockPos pos, double flightHeight, double speed) {
        return moveTo(pos.getX() + 0.5,
                pos.getY() + flightHeight,
                pos.getZ() + 0.5,
                speed);
    }

    public boolean moveToLand(BlockPos landingSpot, double speed) {
        double approachX = mob.getX() * 0.5 + landingSpot.getX() * 0.5;
        double approachY = mob.getY() * 0.7 + landingSpot.getY() * 0.3 + 2.0;
        double approachZ = mob.getZ() * 0.5 + landingSpot.getZ() * 0.5;

        return moveTo(approachX, approachY, approachZ, speed);
    }
}