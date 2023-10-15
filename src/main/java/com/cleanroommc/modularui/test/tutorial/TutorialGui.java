package com.cleanroommc.modularui.test.tutorial;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.manager.ClientGUI;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class TutorialGui {

    @SubscribeEvent
    public static void onItemUse(PlayerInteractEvent event) {
        ItemStack heldItem = event.entityPlayer.getHeldItem();
        if (event.entityPlayer.getEntityWorld().isRemote && heldItem != null && heldItem.getItem() == Items.diamond) {
            ClientGUI.open(createGui());
        }
    }

    public static ModularScreen createGui() {
        ModularPanel panel = ModularPanel.defaultPanel("tutorial_panel");
        panel.child(IKey.str("My first screen").asWidget()
                        .top(7).left(7))
                .child(new ButtonWidget<>()
                        .align(Alignment.Center)
                        .size(60, 16)
                        .overlay(IKey.str("Say Hello"))
                        .onMousePressed(button -> {
                            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                            player.addChatMessage(new ChatComponentText("Hello " + player.getDisplayName()));
                            return true;
                        }));
        return new ModularScreen(panel);
    }
}
