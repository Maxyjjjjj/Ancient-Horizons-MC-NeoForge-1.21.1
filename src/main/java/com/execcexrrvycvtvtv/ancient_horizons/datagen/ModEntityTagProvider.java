package com.execcexrrvycvtvtv.ancient_horizons.datagen;

import com.execcexrrvycvtvtv.ancient_horizons.registry.ModEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.execcexrrvycvtvtv.ancient_horizons.registry.ModTags.EntityTypes.*;

public class ModEntityTagProvider extends EntityTypeTagsProvider {
    public ModEntityTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(TIGER_PREY).add(
                EntityType.ARMADILLO,
                EntityType.CHICKEN,
                EntityType.HOGLIN,
                EntityType.RABBIT,
                EntityType.COW,
                EntityType.SHEEP,
                EntityType.PIG,
                EntityType.CREEPER,
                EntityType.LLAMA,
                EntityType.GOAT
        );
        tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(
                ModEntities.TIGER.get()
        );
        tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES).add(
                ModEntities.TIGER.get()
        );
        tag(FELIDAE).add(
                ModEntities.TIGER.get()
        );
    }
}
