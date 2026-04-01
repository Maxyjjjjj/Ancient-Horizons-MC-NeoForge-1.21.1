package com.execcexrrvycvtvtv.ancient_horizons.entity.custom.mob;

import com.execcexrrvycvtvtv.ancient_horizons.registry.ModEntities;
import com.execcexrrvycvtvtv.ancient_horizons.registry.ModSoundEvents;
import com.execcexrrvycvtvtv.ancient_horizons.registry.ModTags;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

public class DeerEntity extends Animal {
    public DeerEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new DeerTemptGoal(this, 1.25D, Ingredient.of(ModTags.Items.ATTRACTS_DEER)));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (isBorys() && stack.is(Items.APPLE) && !this.isBaby()) {
            if (!this.level().isClientSide) {
                this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                if (!player.getAbilities().instabuild) stack.shrink(1);
                this.spawnAtLocation(new ItemStack(Blocks.DANDELION.asItem()));
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ModTags.Items.DEER_FOOD);
    }

    public boolean isBorys() {
        Component name = this.getCustomName();
        return name != null && name.getString().equalsIgnoreCase("Borys");
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return ModEntities.DEER.get().create(serverLevel);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ModSoundEvents.DEER_AMBIENT.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSoundEvents.DEER_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return ModSoundEvents.DEER_DEATH.get();
    }

    private class DeerTemptGoal extends TemptGoal {
        public DeerTemptGoal(DeerEntity deer, double speedMod, Ingredient ingredient) {
            super(deer, speedMod, ingredient, !DeerEntity.this.isBorys());
        }

        @Override
        public boolean canUse() {
            return super.canUse();
        }
    }
}