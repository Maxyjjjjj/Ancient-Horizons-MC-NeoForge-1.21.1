package com.execcexrrvycvtvtv.ancient_horizons.advancement;

import com.execcexrrvycvtvtv.ancient_horizons.registry.ModEntities;
import com.execcexrrvycvtvtv.ancient_horizons.entity.variants.TigerVariant;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class ModHusbandryAdvancements implements AdvancementProvider.AdvancementGenerator {

    @Override
    public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
        ResourceLocation vanillaTameId = ResourceLocation.withDefaultNamespace( "husbandry/tame_an_animal");

        AdvancementHolder vanillaTameAnimal = new AdvancementHolder(ResourceLocation.withDefaultNamespace("husbandry/tame_an_animal"), Advancement.Builder.advancement().build(vanillaTameId).value());

        AdvancementHolder eyeOfTheTaiga = Advancement.Builder.advancement()
                .parent(vanillaTameAnimal)
                .display(
                        Items.BEEF,
                        Component.translatable("ancient_horizons.advancements.eye_of_the_taiga.title"),
                        Component.translatable("ancient_horizons.advancements.eye_of_the_taiga.description"),
                        null,
                        AdvancementType.CHALLENGE,
                        true, true, false
                )
                .addCriterion("tame_regular", TameAnimalTrigger.TriggerInstance.tamedAnimal(tigerVariantPredicate(TigerVariant.REGULAR)))
                .addCriterion("tame_white", TameAnimalTrigger.TriggerInstance.tamedAnimal(tigerVariantPredicate(TigerVariant.WHITE)))
                .addCriterion("tame_tabby", TameAnimalTrigger.TriggerInstance.tamedAnimal(tigerVariantPredicate(TigerVariant.TABBY)))
                .addCriterion("tame_blue", TameAnimalTrigger.TriggerInstance.tamedAnimal(tigerVariantPredicate(TigerVariant.BLUE)))
                .rewards(AdvancementRewards.Builder.experience(75))
                .save(consumer, "ancient_horizons/eye_of_the_taiga");
    }


    private EntityPredicate.Builder tigerVariantPredicate(TigerVariant variant) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Variant", variant.ordinal());
        return EntityPredicate.Builder.entity()
                .of(ModEntities.TIGER.get())
                .nbt(new NbtPredicate(tag));
    }
}