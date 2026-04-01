package com.execcexrrvycvtvtv.ancient_horizons.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.execcexrrvycvtvtv.ancient_horizons.registry.ModTags.Blocks.*;
import static net.minecraft.world.level.block.Blocks.*;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(GOLDEN_EAGLES_SPAWN_ON_ADDITIONAL)
                .add(STONE)
                .add(SNOW_BLOCK)
                .add(PACKED_ICE);
    }
}
