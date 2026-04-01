package com.execcexrrvycvtvtv.ancient_horizons.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.execcexrrvycvtvtv.ancient_horizons.registry.ModEntities.*;
import static com.execcexrrvycvtvtv.ancient_horizons.registry.ModTags.EntityTypes.*;
import static net.minecraft.tags.EntityTypeTags.*;
import static net.minecraft.world.entity.EntityType.*;

public class ModEntityTagProvider extends EntityTypeTagsProvider {
    public ModEntityTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(TIGER_PREY).add(
                ARMADILLO,
                CHICKEN,
                HOGLIN,
                RABBIT,
                COW,
                SHEEP,
                PIG,
                CREEPER,
                LLAMA,
                GOAT,
                DEER.get()
        );
        tag(POWDER_SNOW_WALKABLE_MOBS).add(
                TIGER.get()
        );
        tag(FREEZE_IMMUNE_ENTITY_TYPES).add(
                TIGER.get()
        );
        tag(FELIDAE).add(
                TIGER.get(),
                CAT,
                OCELOT
        );
        tag(GOLDEN_EAGLE_PREY).add(
                ARMADILLO,
                SHEEP,
                GOAT,
                RABBIT
        );
    }
}
