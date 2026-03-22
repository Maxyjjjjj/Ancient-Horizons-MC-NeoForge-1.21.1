package com.execcexrrvycvtvtv.ancient_horizons;

import com.execcexrrvycvtvtv.ancient_horizons.entity.client.tiger.TigerRenderer;
import com.execcexrrvycvtvtv.ancient_horizons.registry.ModCreativeModeTabs;
import com.execcexrrvycvtvtv.ancient_horizons.registry.ModEntities;
import com.execcexrrvycvtvtv.ancient_horizons.registry.ModItems;
import com.execcexrrvycvtvtv.ancient_horizons.registry.ModSoundEvents;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(AncientHorizons.MOD_ID)
public class AncientHorizons {

    public static final String MOD_ID = "ancient_horizons";

    public AncientHorizons(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSoundEvents.EVENTS.register(modEventBus);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {}

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    @EventBusSubscriber(modid = AncientHorizons.MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.TIGER.get(), TigerRenderer::new);
        }
    }
}