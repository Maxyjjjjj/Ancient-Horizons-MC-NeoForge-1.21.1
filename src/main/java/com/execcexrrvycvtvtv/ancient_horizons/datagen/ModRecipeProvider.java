package com.execcexrrvycvtvtv.ancient_horizons.datagen;

import com.execcexrrvycvtvtv.ancient_horizons.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TIGER_FUR.get())
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', ModItems.TIGER_HAIR.get())
                .unlockedBy("has_tiger_hair", has(ModItems.TIGER_HAIR)).save(recipeOutput,"tiger_fur");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TIGER_PELT.get())
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', ModItems.TIGER_FUR.get())
                .unlockedBy("has_tiger_fur", has(ModItems.TIGER_FUR))
                .save(recipeOutput,"tiger_pelt");

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.FALCONRY_GLOVE.get())
                .pattern(" L ")
                .pattern("LHL")
                .pattern("SL ")
                .define('H', Items.RABBIT_HIDE)
                .define('L', Items.LEATHER)
                .define('S', Items.STRING)
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(recipeOutput, "falconry_glove");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.LEATHER)
                .requires(ModItems.TIGER_PELT.get(), 2)
                .unlockedBy("has_tiger_pelt", has(ModItems.TIGER_PELT))
                .save(recipeOutput, "leather_from_tiger_pelt");

        SimpleCookingRecipeBuilder
                .smelting(Ingredient.of(ModItems.VENISON), RecipeCategory.FOOD, ModItems.COOKED_VENISON, 0.35F, 200)
                .unlockedBy("has_venison", has(ModItems.VENISON))
                .save(recipeOutput);
    }
}
