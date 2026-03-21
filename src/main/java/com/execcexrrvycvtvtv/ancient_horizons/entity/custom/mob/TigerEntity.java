package com.execcexrrvycvtvtv.ancient_horizons.entity.custom.mob;

import com.execcexrrvycvtvtv.ancient_horizons.entity.ModEntities;
import com.execcexrrvycvtvtv.ancient_horizons.entity.variants.TigerVariant;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class TigerEntity extends TamableAnimal implements NeutralMob, VariantHolder<TigerVariant> {

    private static final EntityDataAccessor<Integer> DATA_VARIANT =
            SynchedEntityData.defineId(TigerEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME =
            SynchedEntityData.defineId(TigerEntity.class, EntityDataSerializers.INT);

    private int brushCooldown = 0;

    @Override
    public void tick() {
        super.tick();
        if (brushCooldown > 0) brushCooldown--;
    }

    public TigerEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F));
        this.goalSelector.addGoal(5, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ItemTags.MEAT);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);

        if (isFood(item) && !isTame()) {
            if (!player.getAbilities().instabuild) {
                item.shrink(1);
            }

            if (this.random.nextInt(3) == 0) {
                this.tame(player);
                this.navigation.stop();
                this.setTarget(null);
                this.level().broadcastEntityEvent(this, (byte) 7);
            } else {
                this.level().broadcastEntityEvent(this, (byte) 6);
            }

            return InteractionResult.SUCCESS;
        }

        if (item.is(Items.BRUSH) && isTame() && brushCooldown == 0) {
            if (!level().isClientSide) {
                brushCooldown = 20 * 60; // 1 min
            }
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Nullable
    private UUID persistentAngerTarget;

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

    public void giveMorningGift(Player player) {
        if (this.random.nextFloat() < 0.3F) {
            this.spawnAtLocation(getGiftItem());
        }
    }

    private ItemStack getGiftItem() {
        return LootTableHelper.getRandomItem("yourmod:gameplay/tiger_gifts", (ServerLevel) level());
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

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getVariant().ordinal());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setVariant(TigerVariant.values()[tag.getInt("Variant")]);
    }

    public TigerVariant getRandomVariant(RandomSource random) {
        int roll = random.nextInt(100);

        if (roll < 1) return TigerVariant.BLUE;       // 1%
        else if (roll < 10) return TigerVariant.TABBY; // 9%
        else if (roll < 30) return TigerVariant.WHITE; // 20%
        else return TigerVariant.REGULAR;              // 70%
    }
}
