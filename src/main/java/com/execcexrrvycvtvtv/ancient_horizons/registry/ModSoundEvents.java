package com.execcexrrvycvtvtv.ancient_horizons.registry;

import com.execcexrrvycvtvtv.ancient_horizons.AncientHorizons;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSoundEvents {
    public static final DeferredRegister<SoundEvent> EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, AncientHorizons.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> TIGER_AMBIENT = register("entity.tiger.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> TIGER_HURT = register("entity.tiger.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> TIGER_DEATH = register("entity.tiger.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> TIGER_ANGRY = register("entity.tiger.angry");
    public static final DeferredHolder<SoundEvent, SoundEvent> TIGER_SLEEP = register("entity.tiger.sleep");
    public static final DeferredHolder<SoundEvent, SoundEvent> TIGER_CHUFFLE = register("entity.tiger.chuffle");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(
                ResourceLocation.fromNamespaceAndPath(AncientHorizons.MOD_ID, name)));
    }
}