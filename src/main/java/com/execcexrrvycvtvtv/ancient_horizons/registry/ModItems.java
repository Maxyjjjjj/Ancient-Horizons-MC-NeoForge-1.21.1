package com.execcexrrvycvtvtv.ancient_horizons.registry;

import com.execcexrrvycvtvtv.ancient_horizons.AncientHorizons;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AncientHorizons.MOD_ID);

    public static final DeferredItem<Item> TIGER_SPAWN_EGG = ITEMS.register("tiger_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.TIGER, 0xc07922,0x080b1a,
                    new Item.Properties()));

    public static final DeferredItem<Item> TIGER_HAIR = ITEMS.register("tiger_hair",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> TIGER_FUR = ITEMS.register("tiger_fur",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> TIGER_PELT = ITEMS.register("tiger_pelt",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
