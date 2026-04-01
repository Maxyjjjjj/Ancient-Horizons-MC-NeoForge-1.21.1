package com.execcexrrvycvtvtv.ancient_horizons.registry;

import com.execcexrrvycvtvtv.ancient_horizons.AncientHorizons;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

public class ModTags {
    public static class EntityTypes {
        public static final TagKey<EntityType<?>> TIGER_PREY = createTag("tiger_prey");
        public static final TagKey<EntityType<?>> GOLDEN_EAGLE_PREY = createTag("golden_eagle_prey");

        public static final TagKey<EntityType<?>> FELIDAE = createTag("felidae");

        private static TagKey<EntityType<?>> createTag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(AncientHorizons.MOD_ID, name));
        }
    }

    public static class Blocks {
        public static final TagKey<Block> GOLDEN_EAGLES_SPAWN_ON_ADDITIONAL = createTag("golden_eagles_spawn_on_additional");

        private static TagKey<Block> createTag(String name) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(AncientHorizons.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> DEER_FOOD = createTag("deer_food");
        public static final TagKey<Item> ATTRACTS_DEER = createTag("attracts_deer");

        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(AncientHorizons.MOD_ID, name));
        }
    }
}
