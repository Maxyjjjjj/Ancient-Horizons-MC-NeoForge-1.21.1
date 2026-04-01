package com.execcexrrvycvtvtv.ancient_horizons.datagen;

import com.execcexrrvycvtvtv.ancient_horizons.AncientHorizons;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = AncientHorizons.MOD_ID)
public class ModDataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new ModAdvancementProvider(packOutput, lookupProvider, existingFileHelper));

        generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput, lookupProvider));

        generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));

        EntityTypeTagsProvider entityTagProvider = new ModEntityTagProvider(packOutput, lookupProvider, AncientHorizons.MOD_ID, existingFileHelper);
        generator.addProvider(event.includeServer(), entityTagProvider);
        BlockTagsProvider blockTagProvider = new ModBlockTagProvider(packOutput, lookupProvider, AncientHorizons.MOD_ID, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagProvider);
        ItemTagsProvider itemTagProvider = new ModItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), existingFileHelper);
        generator.addProvider(event.includeServer(), itemTagProvider);

        generator.addProvider(event.includeServer(), new ModDatapackProvider(packOutput, lookupProvider));

        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(ModEntityDropsProvider::new, LootContextParamSets.ENTITY)
        ), lookupProvider));
    }
}