package com.cleanroommc.modularui;

import com.cleanroommc.modularui.holoui.HoloScreenEntity;
import com.cleanroommc.modularui.manager.GuiInfos;
import com.cleanroommc.modularui.manager.GuiManager;
import com.cleanroommc.modularui.network.NetworkHandler;
import com.cleanroommc.modularui.test.TestBlock;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        ModularUIConfig.init(event.getSuggestedConfigurationFile());
        NetworkRegistry.INSTANCE.registerGuiHandler(Tags.MODID, GuiManager.INSTANCE);
        GuiInfos.init();

        if (ModularUI.isDevEnv) {
            MinecraftForge.EVENT_BUS.register(TestBlock.class);
            TestBlock.preInit();
        }

        EntityRegistry.registerModEntity(HoloScreenEntity.class, "modular_screen", 0, ModularUI.INSTANCE, 0, 0, false);

        NetworkHandler.init();

        FMLCommonHandler.instance().bus().register(this);
    }

    public void postInit(FMLPostInitializationEvent event) {}

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(Tags.MODID)) {
            ModularUIConfig.syncConfig();
        }
    }
}
