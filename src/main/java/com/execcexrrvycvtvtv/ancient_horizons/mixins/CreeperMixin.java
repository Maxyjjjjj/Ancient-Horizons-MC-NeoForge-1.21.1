package com.execcexrrvycvtvtv.ancient_horizons.mixins;

import com.execcexrrvycvtvtv.ancient_horizons.registry.ModTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creeper.class)
public abstract class CreeperMixin extends Monster {

    protected CreeperMixin(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void ignoreFelidTargets(LivingEntity target, CallbackInfo ci) {
        if (target != null && target.getType().is(ModTags.EntityTypes.FELIDAE)) {
            ci.cancel();
        }
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void addFelidFleeGoal(CallbackInfo ci) {
        Creeper self = (Creeper) (Object) this;

        self.goalSelector.addGoal(3, new AvoidEntityGoal<>(
                self,
                LivingEntity.class,
                6.0F,
                1.0D,
                1.2D,
                entity -> entity.getType().is(ModTags.EntityTypes.FELIDAE) && !(entity instanceof Cat || entity instanceof Ocelot)
        ));
    }
}