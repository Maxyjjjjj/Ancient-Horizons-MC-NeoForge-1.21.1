package com.execcexrrvycvtvtv.ancient_horizons.registry;

import com.execcexrrvycvtvtv.ancient_horizons.AncientHorizons;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AncientHorizons.MOD_ID);
    public static final Supplier<CreativeModeTab> ANCIENT_HORIZONS_TAB_SPAWN_EGGS = CREATIVE_MODE_TAB.register("ancient_horizons_tab_spawn_eggs",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.TIGER_SPAWN_EGG.get()))
                    .title(Component.translatable("creativetab.ancient_horizons.ancient_horizons_spawn_eggs"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.TIGER_SPAWN_EGG);
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
