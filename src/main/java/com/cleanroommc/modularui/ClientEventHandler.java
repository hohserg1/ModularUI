package com.cleanroommc.modularui;

import com.cleanroommc.modularui.drawable.Stencil;
import com.cleanroommc.modularui.manager.GuiManager;
import com.cleanroommc.modularui.screen.GuiScreenWrapper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiOpenEvent;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {

    private static long ticks = 0L;

    public static long getTicks() {
        return ticks;
    }

    @SubscribeEvent
    public static void preDraw(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            GL11.glEnable(GL11.GL_STENCIL_TEST);
        }
        Stencil.reset();
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            ticks++;
            GuiManager.checkQueuedScreen();
        }
    }

    @SubscribeEvent
    public static void onOpenScreen(GuiOpenEvent event) {
        if (event.gui instanceof GuiScreenWrapper && Minecraft.getMinecraft().currentScreen != null) {
            // another screen is already open, don't fade in the dark background as it's already there
            ((GuiScreenWrapper) event.gui).setAlphaFade(false);
        }
    }
}
