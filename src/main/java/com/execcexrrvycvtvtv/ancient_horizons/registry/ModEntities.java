package com.execcexrrvycvtvtv.ancient_horizons.registry;

import com.execcexrrvycvtvtv.ancient_horizons.AncientHorizons;
import com.execcexrrvycvtvtv.ancient_horizons.entity.custom.mob.DeerEntity;
import com.execcexrrvycvtvtv.ancient_horizons.entity.custom.mob.GoldenEagleEntity;
import com.execcexrrvycvtvtv.ancient_horizons.entity.custom.mob.TigerEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, AncientHorizons.MOD_ID);

    public static final Supplier<EntityType<TigerEntity>> TIGER =
            ENTITY_TYPES.register("tiger", () -> EntityType.Builder.of(TigerEntity::new, MobCategory.CREATURE)
                    .sized(0.685f,1.375f).build("tiger"));
    public static final Supplier<EntityType<GoldenEagleEntity>> GOLDEN_EAGLE =
            ENTITY_TYPES.register("golden_eagle", () -> EntityType.Builder.of(GoldenEagleEntity::new, MobCategory.CREATURE)
                    .sized(0.4375f,0.75f).build("golden_eagle"));
    public static final Supplier<EntityType<DeerEntity>> DEER =
            ENTITY_TYPES.register("deer", () -> EntityType.Builder.of(DeerEntity::new, MobCategory.CREATURE)
                    .sized(0.625f,1.375f).build("deer"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}