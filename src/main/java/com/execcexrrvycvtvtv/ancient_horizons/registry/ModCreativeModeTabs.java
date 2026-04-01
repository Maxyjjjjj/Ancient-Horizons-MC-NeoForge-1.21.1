package com.execcexrrvycvtvtv.ancient_horizons.registry;

import com.execcexrrvycvtvtv.ancient_horizons.AncientHorizons;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AncientHorizons.MOD_ID);
    public static final Supplier<CreativeModeTab> AH_SPAWN_EGGS = CREATIVE_MODE_TAB.register("ah_spawn_eggs",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.TIGER_SPAWN_EGG.get()))
                    .title(Component.translatable("creativetab.ancient_horizons.ancient_horizons_spawn_eggs"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.GOLDEN_EAGLE_SPAWN_EGG);
                        output.accept(ModItems.DEER_SPAWN_EGG);
                        output.accept(ModItems.TIGER_SPAWN_EGG);
                    }).build());
    public static final Supplier<CreativeModeTab> AH_BLOCKS_AND_ITEMS = CREATIVE_MODE_TAB.register("ah_blocks_and_items",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.FALCONRY_GLOVE.get()))
                    .title(Component.translatable("creativetab.ancient_horizons.ancient_horizons_blocks_and_items"))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(AncientHorizons.MOD_ID, "ah_spawn_eggs"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.FALCONRY_GLOVE);
                    }).build());
    public static final Supplier<CreativeModeTab> AH_FOOD = CREATIVE_MODE_TAB.register("ah_foodstuffs",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.FALCONRY_GLOVE.get()))
                    .title(Component.translatable("creativetab.ancient_horizons.ancient_horizons_foodstuffs"))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(AncientHorizons.MOD_ID, "ah_spawn_eggs"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.VENISON);
                        output.accept(ModItems.COOKED_VENISON);
                    }).build());
    public static final Supplier<CreativeModeTab> AH_MISC = CREATIVE_MODE_TAB.register("ah_misc",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.TIGER_PELT.get()))
                    .title(Component.translatable("creativetab.ancient_horizons.ancient_horizons_misc"))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(AncientHorizons.MOD_ID, "ah_foodstuffs"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.TIGER_HAIR);
                        output.accept(ModItems.TIGER_FUR);
                        output.accept(ModItems.TIGER_PELT);
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
