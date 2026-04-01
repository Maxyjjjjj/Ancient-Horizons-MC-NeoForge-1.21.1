package com.execcexrrvycvtvtv.ancient_horizons.entity.ai.goal;

import com.execcexrrvycvtvtv.ancient_horizons.entity.interfaces.SemiFlyer;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import javax.annotation.Nullable;
import java.util.EnumSet;

public class BirdStrollGoal extends Goal {
    protected final PathfinderMob bird;
    protected double wantedX;
    protected double wantedY;
    protected double wantedZ;
    protected final double speedModifier;
    protected int interval;
    protected boolean forceTrigger;

    public BirdStrollGoal(PathfinderMob bird, double speedModifier) {
        this(bird, speedModifier, 120);
    }

    public BirdStrollGoal(PathfinderMob bird, double speedModifier, int interval) {
        this.bird = bird;
        this.speedModifier = speedModifier;
        this.interval = interval;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    private boolean isAirborne() {
        return bird.getDeltaMovement().y > 0.08 || !bird.onGround();
    }

    @Override
    public boolean canUse() {
        if (isAirborne() || ((SemiFlyer)bird).isDiving() || ((TamableAnimal)bird).isOrderedToSit()) {
            return false;
        }

        if (bird.hasControllingPassenger()) {
            return false;
        }

        if (!this.forceTrigger) {
            if (bird.getRandom().nextFloat() > 1.0f / this.interval) {
                return false;
            }
        }

        Vec3 vec3 = this.getPosition();
        if (vec3 == null) {
            return false;
        } else {
            this.wantedX = vec3.x;
            this.wantedY = vec3.y;
            this.wantedZ = vec3.z;
            this.forceTrigger = false;
            return true;
        }
    }

    @Nullable
    protected Vec3 getPosition() {
        Vec3 pos = DefaultRandomPos.getPos(this.bird, 10, 7);
        if (pos == null) return null;

        return new Vec3(pos.x, bird.getY(), pos.z);
    }

    @Override
    public boolean canContinueToUse() {
        return !bird.getNavigation().isDone()
                && !isAirborne()
                && !((TamableAnimal)bird).isOrderedToSit()
                && !bird.hasControllingPassenger();
    }

    @Override
    public void start() {
        this.bird.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
    }

    @Override
    public void stop() {
        this.bird.getNavigation().stop();
    }
}