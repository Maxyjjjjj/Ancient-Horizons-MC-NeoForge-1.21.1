package com.execcexrrvycvtvtv.ancient_horizons.registry;

import com.execcexrrvycvtvtv.ancient_horizons.AncientHorizons;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class ModTags {
    public static class EntityTypes {
        public static final TagKey<EntityType<?>> TIGER_PREY = createTag("tiger_prey");
        public static final TagKey<EntityType<?>> FELIDAE = createTag("felidae");

        private static TagKey<EntityType<?>> createTag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(AncientHorizons.MOD_ID, name));
        }
    }
}
