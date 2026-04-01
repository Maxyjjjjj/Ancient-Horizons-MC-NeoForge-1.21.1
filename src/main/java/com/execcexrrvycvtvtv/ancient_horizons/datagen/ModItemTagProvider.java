package com.execcexrrvycvtvtv.ancient_horizons.datagen;

import com.execcexrrvycvtvtv.ancient_horizons.AncientHorizons;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

import static com.execcexrrvycvtvtv.ancient_horizons.registry.ModItems.*;
import static com.execcexrrvycvtvtv.ancient_horizons.registry.ModTags.Items.*;
import static net.minecraft.tags.ItemTags.*;
import static net.minecraft.world.item.Items.*;

public class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, AncientHorizons.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(DEER_FOOD).add(
                WHEAT
        ).addTag(
                SMALL_FLOWERS
        );
        tag(ATTRACTS_DEER).add(
                APPLE
        ).addTag(
                DEER_FOOD
        );
        tag(MEAT).add(
                VENISON.get(),
                COOKED_VENISON.get()
        );
    }
}
