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
        withExistingParent(ModItems.TIGER_SPAWN_EGG.getId().getPath(),mcLoc("item/template_spawn_egg"));
        basicItem(ModItems.TIGER_HAIR.get());
        basicItem(ModItems.TIGER_FUR.get());
        basicItem(ModItems.TIGER_PELT.get());
    }
}