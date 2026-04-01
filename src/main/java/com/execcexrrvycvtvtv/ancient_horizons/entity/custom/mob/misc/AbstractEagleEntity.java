package com.execcexrrvycvtvtv.ancient_horizons.entity.custom.mob.misc;

import com.execcexrrvycvtvtv.ancient_horizons.entity.ai.goal.*;
import com.execcexrrvycvtvtv.ancient_horizons.entity.ai.nav.BirdLookControl;
import com.execcexrrvycvtvtv.ancient_horizons.entity.ai.nav.BirdMoveControl;
import com.execcexrrvycvtvtv.ancient_horizons.entity.ai.nav.BirdPathNavigation;
import com.execcexrrvycvtvtv.ancient_horizons.entity.interfaces.SemiFlyer;
import com.execcexrrvycvtvtv.ancient_horizons.registry.ModSoundEvents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractEagleEntity extends TamableAnimal implements SemiFlyer {

    private static final EntityDataAccessor<Boolean> DATA_IS_DIVING =
            SynchedEntityData.defineId(AbstractEagleEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Optional<UUID>> DATA_CARRIED_ENTITY =
            SynchedEntityData.defineId(AbstractEagleEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> E_COLLAR_COLOR =
            SynchedEntityData.defineId(AbstractEagleEntity.class, EntityDataSerializers.INT);

    protected static final int MAX_CARRY_TICKS = 80;

    protected static final double DROP_HEIGHT = 8.0;

    protected int carryTimer = 0;

    protected final SimpleContainer inventory = new SimpleContainer(1);

    @Nullable
    protected LivingEntity carriedEntity = null;

    protected PathNavigation groundNavigation;
    protected PathNavigation flyingNavigation;

    protected AbstractEagleEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.flyingNavigation = this.navigation;
        this.groundNavigation = new GroundPathNavigation(this, level);
    }

    @Override
    public MoveControl getMoveControl() {
        return new BirdMoveControl(this);
    }

    @Override
    public LookControl getLookControl() {
        return new BirdLookControl(this);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new BirdPathNavigation(this, level);
    }

    public static AttributeSupplier.Builder createEagleAttributes() {
        return TamableAnimal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.FLYING_SPEED, 0.6)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.2);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IS_DIVING, false);
        builder.define(DATA_CARRIED_ENTITY, Optional.empty());
        builder.define(E_COLLAR_COLOR, DyeColor.RED.getId());
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    public ItemStack getItemInBeak() {
        return this.inventory.getItem(0);
    }

    public void dropInventoryContents() {
        if (!this.level().isClientSide) {
            ItemStack stack = inventory.getItem(0);
            if (!stack.isEmpty()) {
                this.spawnAtLocation(stack);
                inventory.setItem(0, ItemStack.EMPTY);
            }
        }
    }

    public boolean hasItems() {
        return !inventory.getItem(0).isEmpty();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("Inventory", this.inventory.createTag(this.registryAccess()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Inventory", 9)) {
            this.inventory.fromTag(compound.getList("Inventory", 10), this.registryAccess());
        }
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.setOnGround(true);
        this.setDeltaMovement(Vec3.ZERO);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new BirdTakeoffGoal(this, 1.0){
            @Override
            protected void onWindup(int ticksRemaining) {
                setPose(Pose.CROUCHING);
            }

            @Override
            protected void onLaunched() {
                playSound(SoundEvents.ENDER_DRAGON_FLAP, 1.0f, 1.2f);
            }
        });

        this.goalSelector.addGoal(2, new EagleDiveAttackGoal(this));
        this.goalSelector.addGoal(2, new EagleLiftAndDropGoal(this));

        this.goalSelector.addGoal(3, new BirdFlyGoal(this, 1.2, true));

        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(6, new BirdStrollGoal(this, 1.0f));
        this.goalSelector.addGoal(7, new BirdLandGoal(this, 0.8));
        this.goalSelector.addGoal(4, new ModFollowOwnerGoal(this, 1.0, 10.0f, 2.0f));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    public DyeColor getCollarColor() {
        return DyeColor.byId(this.entityData.get(E_COLLAR_COLOR));
    }

    public void setCollarColor(DyeColor color) {
        this.entityData.set(E_COLLAR_COLOR, color.getId());
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);

        if (!isTame()) {
            if (isTamingFood(item)) {
                if (!player.getAbilities().instabuild) item.shrink(1);
                tryToTame(player);
                return InteractionResult.sidedSuccess(level().isClientSide);
            }
            return super.mobInteract(player, hand);
        }

        if (item.getItem() instanceof DyeItem dyeItem && isTame() && isOwnedBy(player)) {
            DyeColor newColor = dyeItem.getDyeColor();
            if (getCollarColor() != newColor) {
                setCollarColor(newColor);
                if (!player.getAbilities().instabuild) item.shrink(1);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        InteractionResult interactionresult = super.mobInteract(player, hand);
        if (!interactionresult.consumesAction() && this.isOwnedBy(player)) {
            this.setOrderedToSit(!this.isOrderedToSit());
            this.jumping = false;
            this.navigation.stop();
            this.setTarget(null);
            return InteractionResult.SUCCESS_NO_ITEM_USED;
        }

        return interactionresult;
    }

    public abstract boolean isTamingFood(ItemStack stack);

    @Override
    public boolean isFlying() {
        return !this.onGround() && !this.isInWater();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return isTamingFood(stack);
    }

    private void tryToTame(Player player) {
        if (!this.level().isClientSide) {
            if (this.random.nextInt(3) == 0 && !EventHooks.onAnimalTame(this, player)) {
                this.tame(player);
                this.navigation.stop();
                this.setTarget(null);
                this.setOrderedToSit(true);
                this.level().broadcastEntityEvent(this, (byte)7);
            } else {
                this.level().broadcastEntityEvent(this, (byte)6);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide) {
            boolean isFlying = this.isFlying();
            if (isFlying && this.navigation != flyingNavigation) {
                this.navigation = flyingNavigation;
            } else if (!isFlying && this.navigation != groundNavigation) {
                this.navigation = groundNavigation;
            }

            tickCarryLogic();
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (this.isTame()){
                if (this.inventory.getItem(0).isEmpty()) {
                    List<ItemEntity> items = this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(2.0D));
                    for (ItemEntity itemEntity : items) {
                        if (itemEntity.isAlive()) {
                            this.inventory.setItem(0, itemEntity.getItem().copy());
                            itemEntity.discard();
                            this.playSound(SoundEvents.ITEM_PICKUP, 0.5F, 1.5F);
                            break;
                        }
                    }
                } else if (this.getOwner() != null && this.distanceTo(this.getOwner()) > 5.0F) {
                    this.getNavigation().moveTo(this.getOwner(), 1.2D);
                }
            }
        }
    }

    @Override
    public boolean isNoGravity() {
        return this.isFlying() || super.isNoGravity();
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, net.minecraft.core.BlockPos pos) {
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
            if (this.isFlying()) {
                this.moveRelative((float) this.getAttributeValue(Attributes.FLYING_SPEED), travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());

                double drag = 0.91F;
                this.setDeltaMovement(this.getDeltaMovement().scale(drag));

                if (this.getDeltaMovement().y < -0.05) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0, 0.05, 0));
                }
            } else {
                super.travel(travelVector);
            }
        }
        this.calculateEntityAnimation(false);
    }

    private void tickCarryLogic() {
        if (this.carriedEntity != null) {
            this.carryTimer++;

            Vec3 carryPos = this.position().add(this.getForward().scale(0.3)).subtract(0, 0.6, 0);

            this.carriedEntity.setPos(carryPos.x, carryPos.y, carryPos.z);

            this.carriedEntity.setDeltaMovement(Vec3.ZERO);
            this.carriedEntity.fallDistance = 0;
            this.carriedEntity.setOnGround(false);

            if (this.carryTimer >= MAX_CARRY_TICKS || !this.carriedEntity.isAlive() || this.onGround()) {
                dropCarriedEntity();
            }
        }
    }

    public void grabEntity(LivingEntity target) {
        if (!canCarry(target)) return;
        this.carriedEntity = target;
        this.entityData.set(DATA_CARRIED_ENTITY, Optional.of(target.getUUID()));
        this.carryTimer = 0;
        playGrabSound();
    }

    public void dropCarriedEntity() {
        if (carriedEntity != null) {
            this.entityData.set(DATA_CARRIED_ENTITY, Optional.empty());
            this.carriedEntity = null;
        }
    }

    public boolean canCarry(LivingEntity entity) {
        return entity != null
                && carriedEntity == null
                && !(entity instanceof Player)
                && entity.getBbWidth() < this.getBbWidth() * 1.5f
                && entity.getBbHeight() < 1.2f;
    }

    @Nullable
    public LivingEntity getCarriedEntity() {
        return carriedEntity;
    }

    public boolean isDiving() { return this.entityData.get(DATA_IS_DIVING); }
    public void setDiving(boolean diving) { this.entityData.set(DATA_IS_DIVING, diving); }

    protected void playGrabSound() {
        playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.8f, 1.3f);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ModSoundEvents.GOLDEN_EAGLE_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSoundEvents.GOLDEN_EAGLE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.GOLDEN_EAGLE_DEATH.get();
    }

    @Override
    public abstract AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mate);

    public boolean isCatching() {
        LivingEntity target = this.getTarget();
        if (target != null && !this.onGround()) {
            double dist = this.distanceToSqr(target);
            return dist < 16.0 || (isDiving() && dist < 25.0);
        }
        return false;
    }

    public boolean isCarrying() {
        return this.entityData.get(DATA_CARRIED_ENTITY).isPresent();
    }

    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        if (!(target instanceof Creeper) && !(target instanceof Ghast) && !(target instanceof ArmorStand)) {
            if (!(target instanceof AbstractEagleEntity eagle)) {
                if (target instanceof Player player) {
                    if (owner instanceof Player player1) {
                        if (!player1.canHarmPlayer(player)) {
                            return false;
                        }
                    }
                }

                if (target instanceof AbstractHorse abstracthorse) {
                    if (abstracthorse.isTamed()) {
                        return false;
                    }
                }

                if (target instanceof TamableAnimal tamableanimal) {
                    return !tamableanimal.isTame();
                }

                return true;
            } else {
                return !eagle.isTame() || eagle.getOwner() != owner;
            }
        } else {
            return false;
        }
    }

    protected static class EagleDiveAttackGoal extends Goal {
        private final AbstractEagleEntity eagle;
        private LivingEntity target;
        private int diveTimer;

        public EagleDiveAttackGoal(AbstractEagleEntity eagle) {
            this.eagle = eagle;
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            target = eagle.getTarget();
            if (target == null || !target.isAlive()) return false;
            return eagle.getY() > target.getY() + 4.0;
        }

        @Override
        public void start() {
            diveTimer = 0;
            eagle.setDiving(true);
        }

        @Override
        public void stop() {
            eagle.setDiving(false);
            diveTimer = 0;
        }

        @Override
        public void tick() {
            diveTimer++;
            Vec3 targetPos = target.position();
            Vec3 toTarget = targetPos.subtract(eagle.position()).normalize();

            double speed = eagle.getAttributeValue(Attributes.FLYING_SPEED) * 2.5;
            eagle.setDeltaMovement(toTarget.scale(speed));

            eagle.getLookControl().setLookAt(target, 30f, 30f);

            if (eagle.getBoundingBox().inflate(0.8).intersects(target.getBoundingBox())) {
                eagle.doHurtTarget(target);
                stop();
            }

            if (diveTimer > 100 || eagle.onGround()) {
                stop();
            }
        }

        @Override
        public boolean canContinueToUse() {
            return eagle.isDiving() && target != null && target.isAlive();
        }
    }

    protected static class EagleLiftAndDropGoal extends Goal {
        private final AbstractEagleEntity eagle;
        private LivingEntity prey;

        public EagleLiftAndDropGoal(AbstractEagleEntity eagle) {
            this.eagle = eagle;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            prey = eagle.getTarget();
            return prey != null && prey.isAlive()
                    && eagle.canCarry(prey)
                    && eagle.distanceToSqr(prey) < 4.0;
        }

        @Override
        public void start() {
            eagle.grabEntity(prey);
            eagle.setDeltaMovement(eagle.getDeltaMovement().add(0, 0.5, 0));
        }

        @Override
        public void tick() {
            Vec3 motion = eagle.getDeltaMovement();
            eagle.setDeltaMovement(motion.x, 0.3, motion.z);

            if (eagle.getY() > eagle.level().getHeight() + DROP_HEIGHT) {
                this.stop();
            }
        }

        @Override
        public void stop() {
            eagle.dropCarriedEntity();
            eagle.setTarget(null);
        }

        @Override
        public boolean canContinueToUse() {
            return eagle.getCarriedEntity() != null && !eagle.onGround();
        }
    }
}