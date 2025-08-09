package com.example.getexpfrommining_tantn;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = GetExpFromMiningMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = GetExpFromMiningMod.MODID, value = Dist.CLIENT)
public final class GetExpFromMiningClient {

    public GetExpFromMiningClient(final ModContainer container) {
        // Đăng ký config screen (nhẹ, chỉ khi mở màn hình Mods)
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(final FMLClientSetupEvent event) {
    
    }
}
