package com.execcexrrvycvtvtv.ancient_horizons.events;

import com.execcexrrvycvtvtv.ancient_horizons.AncientHorizons;
import com.execcexrrvycvtvtv.ancient_horizons.entity.client.deer.DeerModel;
import com.execcexrrvycvtvtv.ancient_horizons.entity.client.golden_eagle.GoldenEagleModel;
import com.execcexrrvycvtvtv.ancient_horizons.entity.client.tiger.TigerModel;
import com.execcexrrvycvtvtv.ancient_horizons.entity.custom.mob.DeerEntity;
import com.execcexrrvycvtvtv.ancient_horizons.entity.custom.mob.GoldenEagleEntity;
import com.execcexrrvycvtvtv.ancient_horizons.entity.custom.mob.TigerEntity;
import com.execcexrrvycvtvtv.ancient_horizons.registry.ModEntities;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber(modid = AncientHorizons.MOD_ID)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TigerModel.LAYER_LOCATION, TigerModel::createBodyLayer);
        event.registerLayerDefinition(GoldenEagleModel.LAYER_LOCATION, GoldenEagleModel::createBodyLayer);
        event.registerLayerDefinition(DeerModel.LAYER_LOCATION, DeerModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.TIGER.get(), TigerEntity.createAttributes().build());
        event.put(ModEntities.GOLDEN_EAGLE.get(), GoldenEagleEntity.createGEAttributes().build());
        event.put(ModEntities.DEER.get(), DeerEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacement(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.TIGER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.GOLDEN_EAGLE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GoldenEagleEntity::checkGoldenEagleSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}