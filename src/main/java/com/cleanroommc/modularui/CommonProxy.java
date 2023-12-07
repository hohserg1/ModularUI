package com.cleanroommc.modularui;

import com.cleanroommc.modularui.holoui.HoloScreenEntity;
import com.cleanroommc.modularui.manager.GuiInfos;
import com.cleanroommc.modularui.manager.GuiManager;
import com.cleanroommc.modularui.network.NetworkHandler;
import com.cleanroommc.modularui.screen.ModularContainer;
import com.cleanroommc.modularui.test.ItemEditorGui;
import com.cleanroommc.modularui.test.TestBlock;
import com.cleanroommc.modularui.value.sync.GuiSyncManager;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.Timer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;

public class CommonProxy {

    void preInit(FMLPreInitializationEvent event) {
        ModularUIConfig.init(event.getSuggestedConfigurationFile());
        NetworkRegistry.INSTANCE.registerGuiHandler(ModularUI.ID, GuiManager.INSTANCE);
        GuiInfos.init();

        if (ModularUIConfig.enableTestGuis) {
            MinecraftForge.EVENT_BUS.register(TestBlock.class);
            TestBlock.preInit();
        }

        EntityRegistry.registerModEntity(HoloScreenEntity.class, "modular_screen", 0, ModularUI.INSTANCE, 0, 0, false);

        NetworkHandler.init();

        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    void postInit(FMLPostInitializationEvent event) {}

    void onServerLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new ItemEditorGui.Command());
    }

    @SideOnly(Side.CLIENT)
    public Timer getTimer60Fps() {
        throw new UnsupportedOperationException();
    }

    @SubscribeEvent
    public final void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(ModularUI.ID)) {
            ModularUIConfig.syncConfig();
        }
    }

    @SubscribeEvent
    public final void onCloseContainer(PlayerOpenContainerEvent event) {
        if (event.entityPlayer.openContainer instanceof ModularContainer) {
            GuiSyncManager syncManager = ((ModularContainer) event.entityPlayer.openContainer).getSyncManager();
            if (syncManager != null) {
                syncManager.onOpen();
            }
        }
    }
}
