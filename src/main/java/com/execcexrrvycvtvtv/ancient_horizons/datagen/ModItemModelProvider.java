package com.execcexrrvycvtvtv.ancient_horizons.datagen;

import com.execcexrrvycvtvtv.ancient_horizons.AncientHorizons;
import com.execcexrrvycvtvtv.ancient_horizons.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AncientHorizons.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        spawnEggItem(ModItems.TIGER_SPAWN_EGG.get());
        spawnEggItem(ModItems.GOLDEN_EAGLE_SPAWN_EGG.get());
        spawnEggItem(ModItems.DEER_SPAWN_EGG.get());

        basicItem(ModItems.TIGER_HAIR.get());
        basicItem(ModItems.TIGER_FUR.get());
        basicItem(ModItems.TIGER_PELT.get());
        basicItem(ModItems.FALCONRY_GLOVE.get());
        basicItem(ModItems.VENISON.get());
        basicItem(ModItems.COOKED_VENISON.get());
    }
}