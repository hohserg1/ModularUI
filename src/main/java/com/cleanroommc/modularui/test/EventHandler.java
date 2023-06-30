package com.cleanroommc.modularui.test;

import com.cleanroommc.modularui.holoui.HoloUI;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class EventHandler {

    @SubscribeEvent
    public void onItemUse(PlayerInteractEvent event) {
        ItemStack heldItem = event.entityPlayer.getHeldItem();
        if (event.entityPlayer.getEntityWorld().isRemote && heldItem != null && heldItem.getItem() == Items.diamond) {
            //GuiManager.openClientUI(Minecraft.getMinecraft().player, new TestGui());
            HoloUI.builder()
                    .inFrontOf(Minecraft.getMinecraft().thePlayer, 5, false)
                    .open(new TestGui());
        }
    }
}
