package com.cleanroommc.modularui.test;

import com.cleanroommc.modularui.factory.ClientGUI;

import net.minecraft.init.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {

    @SubscribeEvent
    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntityPlayer().getEntityWorld().isRemote && event.getItemStack().getItem() == Items.DIAMOND) {
            //GuiManager.openClientUI(Minecraft.getMinecraft().player, new TestGui());
            /*HoloUI.builder()
                    .inFrontOf(Minecraft.getMinecraft().player, 5, false)
                    .screenScale(0.5f)
                    .open(new TestGui());*/
            //ClientGUI.open(new ResizerTest());
            ClientGUI.open(new TestGui());
        }
    }
}
