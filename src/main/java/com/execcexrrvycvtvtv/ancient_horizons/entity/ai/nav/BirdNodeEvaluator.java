package com.execcexrrvycvtvtv.ancient_horizons.entity.ai.nav;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.*;

import javax.annotation.Nullable;
import java.util.Objects;

public class BirdNodeEvaluator extends NodeEvaluator {

    private static final int GROUND_CLEARANCE_MIN = 2;
    private static final float NEAR_GROUND_PENALTY = 1.5F;
    private static final float FOLIAGE_PENALTY     = 1.3F;

    @Override
    public void prepare(PathNavigationRegion region, Mob mob) {
        super.prepare(region, mob);
    }

    @Override
    public Node getStart() {
        BlockPos startPos = mob.blockPosition();

        if (this.getBlockPathType(startPos.getX(), startPos.getY(), startPos.getZ()) == PathType.BLOCKED) {
            for (int i = 1; i < 3; i++) {
                if (this.getBlockPathType(startPos.getX(), startPos.getY() + i, startPos.getZ()) != PathType.BLOCKED) {
                    startPos = startPos.above(i);
                    break;
                }
            }
        }

        return Objects.requireNonNull(this.getNode(startPos.getX(), startPos.getY(), startPos.getZ()));
    }

    @Override
    public Target getTarget(double x, double y, double z) {
        return new Target(Objects.requireNonNull(this.getNode(Mth.floor(x), Mth.floor(y), Mth.floor(z))));
    }

    @Override
    public int getNeighbors(Node[] output, Node node) {
        int count = 0;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;

                    Node neighbor = this.getNode(node.x + dx, node.y + dy, node.z + dz);

                    if (neighbor != null && !neighbor.closed && count < output.length) {
                        PathType type = this.getBlockPathType(neighbor.x, neighbor.y, neighbor.z);

                        if (type != PathType.BLOCKED && type != PathType.DAMAGE_FIRE && type != PathType.LAVA) {
                            int distSq = (dx != 0 ? 1 : 0) + (dy != 0 ? 1 : 0) + (dz != 0 ? 1 : 0);
                            float diagonalCost = distSq == 1 ? 1.0F : (distSq == 2 ? 1.414F : 1.732F);

                            neighbor.costMalus = computeCostMalus(neighbor.x, neighbor.y, neighbor.z, type) * diagonalCost;
                            output[count++] = neighbor;
                        }
                    }
                }
            }
        }
        return count;
    }

    @Override
    public PathType getPathTypeOfMob(PathfindingContext context, int x, int y, int z, Mob mob) {
        return this.getBlockPathType(x, y, z);
    }

    @Override
    public PathType getPathType(PathfindingContext context, int x, int y, int z) {
        return this.getBlockPathType(x, y, z);
    }

    private PathType getBlockPathType(int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        BlockState state = this.currentContext.getBlockState(pos);

        if (state.isAir()) return PathType.OPEN;
        if (state.getBlock() instanceof LeavesBlock || state.getBlock() instanceof VineBlock) {
            return PathType.LEAVES;
        }
        if (state.isSolid()) return PathType.BLOCKED;

        return PathType.OPEN;
    }

    private float computeCostMalus(int x, int y, int z, PathType type) {
        float malus = 0.0F;

        if (type == PathType.LEAVES) {
            malus += FOLIAGE_PENALTY;
        }

        int clearance = groundClearanceBelow(x, y, z);
        if (clearance < GROUND_CLEARANCE_MIN) {
            float t = 1.0F - (float) clearance / GROUND_CLEARANCE_MIN;
            malus += (NEAR_GROUND_PENALTY - 1.0F) * t;
        }

        return malus;
    }

    private int groundClearanceBelow(int x, int y, int z) {
        for (int i = 1; i <= GROUND_CLEARANCE_MIN; i++) {
            BlockState below = this.currentContext.getBlockState(new BlockPos(x, y - i, z));
            if (!below.isAir()) {
                return i - 1;
            }
        }
        return GROUND_CLEARANCE_MIN;
    }

    @Nullable
    protected Node getNode(int x, int y, int z) {
        if (y < this.currentContext.level().getMinBuildHeight() || y >= this.currentContext.level().getMaxBuildHeight()) {
            return null;
        }

        return this.nodes.computeIfAbsent(Node.createHash(x, y, z), hash -> new Node(x, y, z));
    }
}