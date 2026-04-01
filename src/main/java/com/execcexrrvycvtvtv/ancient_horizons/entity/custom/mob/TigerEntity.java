package com.execcexrrvycvtvtv.ancient_horizons.entity.custom.mob;

import com.execcexrrvycvtvtv.ancient_horizons.registry.ModEntities;
import com.execcexrrvycvtvtv.ancient_horizons.entity.variants.TigerVariant;
import com.execcexrrvycvtvtv.ancient_horizons.registry.ModItems;
import com.execcexrrvycvtvtv.ancient_horizons.registry.ModSoundEvents;
import com.execcexrrvycvtvtv.ancient_horizons.registry.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.UUID;

public class TigerEntity extends TamableAnimal implements NeutralMob, VariantHolder<TigerVariant> {

    private static final EntityDataAccessor<Integer> DATA_VARIANT =
            SynchedEntityData.defineId(TigerEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME =
            SynchedEntityData.defineId(TigerEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_SLEEPING =
            SynchedEntityData.defineId(TigerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> RELAX_STATE_ONE =
            SynchedEntityData.defineId(TigerEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> IS_ATTACKING =
            SynchedEntityData.defineId(TigerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_TEARING =
            SynchedEntityData.defineId(TigerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_POUNCING =
            SynchedEntityData.defineId(TigerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_DANCING =
            SynchedEntityData.defineId(TigerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_YAWNING =
            SynchedEntityData.defineId(TigerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_JUMPING =
            SynchedEntityData.defineId(TigerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> T_COLLAR_COLOR =
            SynchedEntityData.defineId(TigerEntity.class, EntityDataSerializers.INT);

    private int brushCooldown = 0;

    int pounceTicks = 0;
    private static final int POUNCE_DURATION = 12;

    private int yawnTicks = 0;
    private static final int YAWN_DURATION = 60;

    private BlockPos jukeboxPos;
    private float dancingAnimationTicks;

    @Nullable
    private UUID persistentAngerTarget;

    public TigerEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return TamableAnimal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.2);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, 0);
        builder.define(DATA_REMAINING_ANGER_TIME, 0);
        builder.define(IS_SLEEPING, false);
        builder.define(RELAX_STATE_ONE, false);
        builder.define(IS_ATTACKING, false);
        builder.define(IS_TEARING, false);
        builder.define(IS_POUNCING, false);
        builder.define(IS_DANCING, false);
        builder.define(IS_YAWNING, false);
        builder.define(IS_JUMPING, false);
        builder.define(T_COLLAR_COLOR, DyeColor.RED.getId());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, level()));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));

        this.goalSelector.addGoal(3, new TigerPounceGoal(this));
        this.goalSelector.addGoal(4, new TigerMeleeGoal(this, 1.4D, true));

        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.2D, 10.0F, 2.0F));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.2D));
        this.goalSelector.addGoal(7, new TigerRelaxOnOwnerGoal(this));

        this.goalSelector.addGoal(8,  new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(9,  new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>(
                this, LivingEntity.class, false,
                entity -> entity.getType().is(ModTags.EntityTypes.TIGER_PREY)){
            final TigerEntity tiger = TigerEntity.this;

            @Override
            public boolean canUse() {
                return !tiger.isBaby() && super.canUse();
            }
        });
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new TigerHurtByTargetGoal());
        this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide) {
            if (brushCooldown > 0) brushCooldown--;

            if (isPouncing()) {
                if (++pounceTicks >= POUNCE_DURATION) {
                    setIsPouncing(false);
                    pounceTicks = 0;
                }
            }

            if (isYawning()) {
                if (--yawnTicks <= 0) setIsYawning(false);
            }

            setIsJumping(this.jumping);

            if (isAttacking() && getTarget() == null) {
                setIsAttacking(false);
            }
        }

        if (this.level().isClientSide) {
            if (this.isDancing()) {
                this.dancingAnimationTicks++;
            } else {
                this.dancingAnimationTicks = 0.0F;
            }
        }
    }

    @Override
    public void setRecordPlayingNearby(BlockPos pos, boolean isPlaying) {
        this.setJukeboxPlaying(pos, isPlaying);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.isDancing() && this.shouldStopDancing() && this.tickCount % 20 == 0) {
            this.setDancing(false);
            this.jukeboxPos = null;
        }
    }

    public void setDancing(boolean dancing) {
        if (!this.level().isClientSide && this.isEffectiveAi() && (!dancing || !this.isPanicking())) {
            this.entityData.set(IS_DANCING, dancing);
        }
    }

    private boolean shouldStopDancing() {
        return this.jukeboxPos == null || !this.jukeboxPos.closerToCenterThan(this.position(), (double)((GameEvent)GameEvent.JUKEBOX_PLAY.value()).notificationRadius()) || !this.level().getBlockState(this.jukeboxPos).is(Blocks.JUKEBOX);
    }

    public void setJukeboxPlaying(BlockPos jukeboxPos, boolean jukeboxPlaying) {
        if (jukeboxPlaying) {
            if (!this.isDancing()) {
                this.jukeboxPos = jukeboxPos;
                this.setDancing(true);
            }
        } else if (jukeboxPos.equals(this.jukeboxPos) || this.jukeboxPos == null) {
            this.jukeboxPos = null;
            this.setDancing(false);
        }

    }

    @Override
    public void customServerAiStep() {
        if (this.getMoveControl().hasWanted()) {
            double speed = this.getMoveControl().getSpeedModifier();
            if (speed <= 0.6) {
                this.setPose(Pose.CROUCHING);
                this.setSprinting(false);
            } else if (speed >= 1.33) {
                this.setPose(Pose.STANDING);
                this.setSprinting(true);
            } else {
                this.setPose(Pose.STANDING);
                this.setSprinting(false);
            }
        } else {
            this.setPose(Pose.STANDING);
            this.setSprinting(false);
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ItemTags.MEAT);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);

        if (isFood(item) && !isTame()) {
            if (!player.getAbilities().instabuild) item.shrink(1);
            tryToTame(player);
            return InteractionResult.SUCCESS;
        }

        if (item.getItem() instanceof DyeItem dyeItem && isTame() && isOwnedBy(player)) {
            DyeColor newColor = dyeItem.getDyeColor();
            if (getCollarColor() != newColor) {
                setCollarColor(newColor);
                if (!player.getAbilities().instabuild) item.shrink(1);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        if (item.canPerformAction(ItemAbilities.BRUSH_BRUSH) && this.brushOffHair() && isTame() && isOwnedBy(player)) {
            item.hurtAndBreak(16, player, getSlotForHand(hand));
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (isTame() && isOwnedBy(player) && !isFood(item) && !(item.getItem() instanceof DyeItem) && !(item.canPerformAction(ItemAbilities.BRUSH_BRUSH) && this.brushOffHair())) {
            if (!level().isClientSide) {
                setOrderedToSit(!isOrderedToSit());
                this.jumping = false;
                this.navigation.stop();
                this.setTarget(null);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    public boolean brushOffHair() {
        if (this.isBaby()) {
            return false;
        } else {
            this.spawnAtLocation(new ItemStack(ModItems.TIGER_HAIR.get()));
            this.gameEvent(GameEvent.ENTITY_INTERACT);
            this.playSound(SoundEvents.ARMADILLO_BRUSH);
            return true;
        }
    }

    public DyeColor getCollarColor() {
        return DyeColor.byId(this.entityData.get(T_COLLAR_COLOR));
    }

    public void setCollarColor(DyeColor color) {
        this.entityData.set(T_COLLAR_COLOR, color.getId());
    }

    public boolean isAttacking() {
        return this.entityData.get(IS_ATTACKING);
    }

    public void setIsAttacking(boolean value) {
        this.entityData.set(IS_ATTACKING, value);
    }

    public boolean isTearing() {
        return this.entityData.get(IS_TEARING);
    }

    public void setIsTearing(boolean value) {
        this.entityData.set(IS_TEARING, value);
    }

    public boolean isPouncing() {
        return this.entityData.get(IS_POUNCING);
    }

    public void setIsPouncing(boolean value) {
        this.entityData.set(IS_POUNCING, value);
    }

    public boolean isDancing() {
        return this.entityData.get(IS_DANCING);
    }

    public boolean isYawning() {
        return this.entityData.get(IS_YAWNING);
    }

    public void setIsYawning(boolean value) {
        this.entityData.set(IS_YAWNING, value);
    }

    public boolean isJumping() {
        return this.entityData.get(IS_JUMPING);
    }

    public void setIsJumping(boolean value) {
        this.entityData.set(IS_JUMPING, value);
    }

    public void setTigerSleeping(boolean sleeping) {
        this.entityData.set(IS_SLEEPING, sleeping);
    }

    public boolean isTigerSleeping() {
        return this.entityData.get(IS_SLEEPING);
    }

    void setRelaxStateOne(boolean value) {
        this.entityData.set(RELAX_STATE_ONE, value);
    }

    boolean isRelaxStateOne() {
        return this.entityData.get(RELAX_STATE_ONE);
    }

    void triggerYawn() {
        yawnTicks = YAWN_DURATION;
        setIsYawning(true);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, time);
    }

    @Override
    public @Nullable UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uuid) {
        this.persistentAngerTarget = uuid;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(20 * (20 + this.random.nextInt(40)));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        this.setVariant(getRandomVariant(level.getRandom()));
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        if (name != null && name.getString().equalsIgnoreCase("Legends")) {
            this.setVariant(TigerVariant.LEGENDS);
        }
    }

    @Override
    public void setVariant(TigerVariant variant) {
        this.entityData.set(DATA_VARIANT, variant.ordinal());
    }

    @Override
    public TigerVariant getVariant() {
        return TigerVariant.values()[this.entityData.get(DATA_VARIANT)];
    }

    public TigerVariant getRandomVariant(RandomSource random) {
        int roll = random.nextInt(100);
        if      (roll < 1)  return TigerVariant.BLUE;
        else if (roll < 10) return TigerVariant.TABBY;
        else if (roll < 30) return TigerVariant.WHITE;
        else return TigerVariant.REGULAR;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        TigerEntity baby = ModEntities.TIGER.get().create(level);
        if (baby != null) {
            TigerEntity parent = (TigerEntity) partner;
            baby.setVariant(this.random.nextBoolean() ? this.getVariant() : parent.getVariant());
            if (this.isTame()) {
                baby.setOwnerUUID(this.getOwnerUUID());
                baby.setTame(true, true);
            }
        }
        return baby;
    }

    private void tryToTame(Player player) {
        if (!this.level().isClientSide) {
            if (this.random.nextInt(8) == 0 && !EventHooks.onAnimalTame(this, player)) {
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
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant",     this.getVariant().ordinal());
        tag.putInt("CollarColor", this.getCollarColor().getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setVariant(TigerVariant.values()[tag.getInt("Variant")]);
        if (tag.contains("CollarColor")) {
            this.setCollarColor(DyeColor.byId(tag.getInt("CollarColor")));
        }
    }

    static class TigerPounceGoal extends Goal {
        private final TigerEntity tiger;
        @Nullable private LivingEntity target;

        TigerPounceGoal(TigerEntity tiger) {
            this.tiger = tiger;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            this.target = tiger.getTarget();
            if (target == null) return false;
            double distSq = tiger.distanceToSqr(target);
            return distSq >= 16.0 && distSq <= 100.0 && tiger.onGround();
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            if (target == null) return;
            double dx = target.getX() - tiger.getX();
            double dz = target.getZ() - tiger.getZ();
            double len = Math.sqrt(dx * dx + dz * dz);
            if (len == 0) return;

            tiger.setDeltaMovement(
                    (dx / len) * 0.6,
                    0.5,
                    (dz / len) * 0.6
            );
            tiger.setIsPouncing(true);
            tiger.pounceTicks = 0;
            tiger.hasImpulse = true;
        }
    }

    static class TigerMeleeGoal extends MeleeAttackGoal {
        private final TigerEntity tiger;
        private int ticksOnTarget = 0;
        private static final int TEAR_THRESHOLD = 20;

        TigerMeleeGoal(TigerEntity tiger, double speed, boolean longMemory) {
            super(tiger, speed, longMemory);
            this.tiger = tiger;
        }

        @Override
        public void start() {
            super.start();
            tiger.setIsAttacking(true);
            tiger.setIsTearing(false);
            ticksOnTarget = 0;
        }

        @Override
        public void tick() {
            super.tick();
            LivingEntity target = tiger.getTarget();
            if (target != null && tiger.distanceToSqr(target) < 4.0) {
                if (++ticksOnTarget >= TEAR_THRESHOLD) {
                    tiger.setIsAttacking(false);
                    tiger.setIsTearing(true);
                }
            } else {
                ticksOnTarget = 0;
                tiger.setIsAttacking(true);
                tiger.setIsTearing(false);
            }
        }

        @Override
        public void stop() {
            super.stop();
            tiger.setIsAttacking(false);
            tiger.setIsTearing(false);
            ticksOnTarget = 0;
        }
    }

    static class TigerRelaxOnOwnerGoal extends Goal {
        private final TigerEntity tiger;
        @Nullable private Player ownerPlayer;
        @Nullable private BlockPos goalPos;
        private int onBedTicks;

        TigerRelaxOnOwnerGoal(TigerEntity tiger) {
            this.tiger = tiger;
        }

        @Override
        public boolean canUse() {
            if (!this.tiger.isTame())        return false;
            if (this.tiger.isOrderedToSit()) return false;

            LivingEntity owner = this.tiger.getOwner();
            if (!(owner instanceof Player player)) return false;
            this.ownerPlayer = player;
            if (!owner.isSleeping()) return false;
            if (this.tiger.distanceToSqr(ownerPlayer) > 100.0F) return false;

            BlockPos blockpos = ownerPlayer.blockPosition();
            BlockState blockstate = this.tiger.level().getBlockState(blockpos);
            if (!blockstate.is(BlockTags.BEDS)) return false;

            this.goalPos = blockstate.getOptionalValue(BedBlock.FACING)
                    .map(f -> blockpos.relative(f.getOpposite()))
                    .orElseGet(() -> new BlockPos(blockpos));
            return !this.spaceIsOccupied();
        }

        private boolean spaceIsOccupied() {
            for (TigerEntity other : tiger.level().getEntitiesOfClass(
                    TigerEntity.class, new AABB(goalPos).inflate(2.0F))) {
                if (other != tiger && (other.isTigerSleeping() || other.isRelaxStateOne())) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return tiger.isTame() && !tiger.isOrderedToSit()
                    && ownerPlayer != null && ownerPlayer.isSleeping()
                    && goalPos != null && !spaceIsOccupied();
        }

        @Override
        public void start() {
            if (goalPos != null) {
                tiger.setInSittingPose(false);
                tiger.getNavigation().moveTo(goalPos.getX(), goalPos.getY(), goalPos.getZ(), 1.1F);
            }
        }

        @Override
        public void stop() {
            tiger.setTigerSleeping(false);
            float f = tiger.level().getTimeOfDay(1.0F);
            if (ownerPlayer != null && ownerPlayer.getSleepTimer() >= 100
                    && f > 0.77F && f < 0.8F
                    && tiger.level().getRandom().nextFloat() < 0.7F) {
                giveMorningGift();
            }
            onBedTicks = 0;
            tiger.setRelaxStateOne(false);
            tiger.getNavigation().stop();
        }

        private void giveMorningGift() {
            RandomSource rng = tiger.getRandom();
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
            pos.set(tiger.isLeashed() ? tiger.getLeashHolder().blockPosition() : tiger.blockPosition());
            tiger.randomTeleport(
                    pos.getX() + rng.nextInt(11) - 5,
                    pos.getY() + rng.nextInt(5)  - 2,
                    pos.getZ() + rng.nextInt(11) - 5,
                    false);
            pos.set(tiger.blockPosition());

            LootTable table = tiger.level().getServer().reloadableRegistries()
                    .getLootTable(BuiltInLootTables.CAT_MORNING_GIFT);
            LootParams params = new LootParams.Builder((ServerLevel) tiger.level())
                    .withParameter(LootContextParams.ORIGIN, tiger.position())
                    .withParameter(LootContextParams.THIS_ENTITY, tiger)
                    .create(LootContextParamSets.GIFT);

            for (ItemStack stack : table.getRandomItems(params)) {
                tiger.level().addFreshEntity(new ItemEntity(
                        tiger.level(),
                        pos.getX() - Mth.sin(tiger.yBodyRot * (float) (Math.PI / 180.0)),
                        pos.getY(),
                        pos.getZ() + Mth.cos(tiger.yBodyRot * (float) (Math.PI / 180.0)),
                        stack));
            }
        }

        @Override
        public void tick() {
            if (ownerPlayer == null || goalPos == null) return;
            tiger.setInSittingPose(false);
            tiger.getNavigation().moveTo(goalPos.getX(), goalPos.getY(), goalPos.getZ(), 1.1F);
            if (tiger.distanceToSqr(ownerPlayer) < 2.5) {
                if (++onBedTicks > adjustedTickDelay(16)) {
                    tiger.setTigerSleeping(true);
                    tiger.setRelaxStateOne(false);
                } else {
                    tiger.lookAt(ownerPlayer, 45.0F, 45.0F);
                    tiger.setRelaxStateOne(true);
                }
            } else {
                tiger.setTigerSleeping(false);
            }
        }
    }
    class TigerHurtByTargetGoal extends HurtByTargetGoal {
        public TigerHurtByTargetGoal() {
            super(TigerEntity.this);
        }

        public void start() {
            super.start();
            if (TigerEntity.this.isBaby()) {
                this.stop();
            }
        }

        protected void alertOther(Mob mob, LivingEntity target) {
            if (mob instanceof TigerEntity && !mob.isBaby()) {
                super.alertOther(mob, target);
            }
        }
    }

    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        if (!(target instanceof Ghast) && !(target instanceof ArmorStand)) {
            if (!(target instanceof TigerEntity tiger)) {
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
                return !tiger.isTame() || tiger.getOwner() != owner;
            }
        } else {
            return false;
        }
    }

    @Override
    public int getAmbientSoundInterval() {
        return 240;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        if (isAggressive()) {
            return ModSoundEvents.TIGER_ANGRY.get();
        } else if (isSleeping()) {
            return ModSoundEvents.TIGER_SLEEP.get();
        } else if (isInLove()) {
            return ModSoundEvents.TIGER_CHUFFLE.get();
        } else return ModSoundEvents.TIGER_AMBIENT.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSoundEvents.TIGER_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return ModSoundEvents.TIGER_DEATH.get();
    }

    public boolean canMate(Animal otherAnimal) {
        if (otherAnimal == this) {
            return false;
        } else if (!this.isTame()) {
            return false;
        } else if (otherAnimal instanceof TigerEntity tiger) {
            if (!tiger.isTame()) {
                return false;
            } else {
                return !tiger.isInSittingPose() && !this.isInSittingPose() && this.isInLove() && tiger.isInLove();
            }
        } else {
            return false;
        }
    }
}