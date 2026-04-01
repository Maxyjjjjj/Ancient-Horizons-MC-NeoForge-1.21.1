package com.execcexrrvycvtvtv.ancient_horizons.registry;

import com.execcexrrvycvtvtv.ancient_horizons.AncientHorizons;
import com.execcexrrvycvtvtv.ancient_horizons.item.FalconryGloveItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AncientHorizons.MOD_ID);

    // SPAWN EGS
    public static final DeferredItem<Item> TIGER_SPAWN_EGG = ITEMS.register("tiger_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.TIGER, 0xc07922,0x080b1a,
                    new Item.Properties()));
    public static final DeferredItem<Item> GOLDEN_EAGLE_SPAWN_EGG = ITEMS.register("golden_eagle_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.GOLDEN_EAGLE, 0x584836,0xd6c0a4,
                    new Item.Properties()));
    public static final DeferredItem<Item> DEER_SPAWN_EGG = ITEMS.register("deer_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.DEER, 0xa56a51,0x8d5b45,
                    new Item.Properties()));

    // MMMMM, DEER
    public static final DeferredItem<Item> VENISON = ITEMS.register("venison",
            () -> new Item(new Item.Properties().food(ModFoodProperties.VENISON)));
    public static final DeferredItem<Item> COOKED_VENISON = ITEMS.register("cooked_venison",
            () -> new Item(new Item.Properties().food(ModFoodProperties.COOKED_VENISON)));

    // FUNCTIONAL SHT
    public static final DeferredItem<Item> FALCONRY_GLOVE = ITEMS.register("falconry_glove",
            () -> new FalconryGloveItem(new Item.Properties()));

    // MISC SHT
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
