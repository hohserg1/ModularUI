package com.cleanroommc.modularui;

import codechicken.nei.guihook.GuiContainerManager;
import com.cleanroommc.modularui.drawable.DrawableSerialization;
import com.cleanroommc.modularui.integration.nei.ModularUIContainerObjectHandler;
import com.cleanroommc.modularui.integration.nei.ModularUIInputHandler;
import com.cleanroommc.modularui.test.EventHandler;
import com.cleanroommc.modularui.theme.ThemeManager;
import com.cleanroommc.modularui.theme.ThemeReloadCommand;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.Timer;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        GuiContainerManager.addInputHandler(new ModularUIInputHandler());
        GuiContainerManager.addObjectHandler(new ModularUIContainerObjectHandler());

        ModularUI.timer60Fps = new Timer(60f);

        if (ModularUI.isDevEnv) {
            MinecraftForge.EVENT_BUS.register(EventHandler.class);
        }

        DrawableSerialization.init();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new ThemeReloadCommand());
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(new ThemeManager());
    }
}
