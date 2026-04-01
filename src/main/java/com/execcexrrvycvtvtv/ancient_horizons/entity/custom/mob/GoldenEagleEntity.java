package com.execcexrrvycvtvtv.ancient_horizons.entity.custom.mob;

import com.execcexrrvycvtvtv.ancient_horizons.entity.custom.mob.misc.AbstractEagleEntity;
import com.execcexrrvycvtvtv.ancient_horizons.registry.ModEntities;
import com.execcexrrvycvtvtv.ancient_horizons.registry.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

public class GoldenEagleEntity extends AbstractEagleEntity {
    public GoldenEagleEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean isTamingFood(ItemStack stack) {
        return stack.is(ItemTags.MEAT);
    }

    public static AttributeSupplier.Builder createGEAttributes() {
        return AbstractEagleEntity.createEagleAttributes()
                .add(Attributes.MAX_HEALTH, 24.0)
                .add(Attributes.FLYING_SPEED, 0.65)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.FOLLOW_RANGE, 38.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.25);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(0, new NonTameRandomTargetGoal<>(this, LivingEntity.class, true, prey -> prey.getType().is(ModTags.EntityTypes.GOLDEN_EAGLE_PREY)));
    }

    public static boolean checkGoldenEagleSpawnRules(EntityType<? extends GoldenEagleEntity> eagle, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkAnimalSpawnRules(eagle, level, spawnType, pos, random) && level.getBlockState(pos.below()).is(ModTags.Blocks.GOLDEN_EAGLES_SPAWN_ON_ADDITIONAL);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        GoldenEagleEntity baby = ModEntities.GOLDEN_EAGLE.get().create(serverLevel);
        if (baby != null && ageableMob instanceof TamableAnimal tamable) {
            if (tamable.isTame()) {
                baby.setOwnerUUID(tamable.getOwnerUUID());
                baby.setTame(true, true);
            }
        }
        return baby;
    }
}