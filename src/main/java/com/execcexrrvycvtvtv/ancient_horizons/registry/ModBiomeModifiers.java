package com.execcexrrvycvtvtv.ancient_horizons.registry;

import com.execcexrrvycvtvtv.ancient_horizons.AncientHorizons;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;

public class ModBiomeModifiers {

    public static final ResourceKey<BiomeModifier> SPAWN_TIGER = registerKey("spawn_tiger");
    public static final ResourceKey<BiomeModifier> SPAWN_GOLDEN_EAGLE = registerKey("spawn_golden_eagle");
    public static final ResourceKey<BiomeModifier> SPAWN_DEER = registerKey("spawn_deer");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var biomes = context.lookup(Registries.BIOME);

        context.register(SPAWN_TIGER, new BiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_TAIGA),
                List.of(new MobSpawnSettings.SpawnerData
                        (ModEntities.TIGER.get(), 2, 1, 3)
                )
        ));

        context.register(SPAWN_GOLDEN_EAGLE, new BiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_MOUNTAIN),
                List.of(new MobSpawnSettings.SpawnerData
                        (ModEntities.GOLDEN_EAGLE.get(), 4, 1, 3)
                )
        ));

        context.register(SPAWN_DEER, new BiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_FOREST),
                List.of(new MobSpawnSettings.SpawnerData
                        (ModEntities.DEER.get(), 6, 2, 4)
                )
        ));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                ResourceLocation.fromNamespaceAndPath(AncientHorizons.MOD_ID, name));
    }
}