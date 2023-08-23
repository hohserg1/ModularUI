package com.cleanroommc.modularui.test;

import com.cleanroommc.modularui.future.IItemHandlerModifiable;
import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.manager.GuiCreationContext;
import com.cleanroommc.modularui.manager.GuiInfos;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.ItemStackItemHandler;
import com.cleanroommc.modularui.value.sync.GuiSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class TestItem extends Item implements IGuiHolder {

    public static final TestItem testItem = new TestItem();

    @Override
    public ModularPanel buildUI(GuiCreationContext guiCreationContext, GuiSyncManager guiSyncManager, boolean isClient) {
        IItemHandlerModifiable itemHandler = new ItemStackItemHandler(guiCreationContext.getMainHandItem(), 4);
        guiSyncManager.registerSlotGroup("mixer_items", 2);

        ModularPanel panel = ModularPanel.defaultPanel("knapping_gui");
        panel.child(new Column()
                //.coverChildren()
                .padding(7)
                .child(SlotGroupWidget.playerInventory())
                .child(SlotGroupWidget.builder()
                        .row("II")
                        .row("II")
                        .key('I', index -> new ItemSlot().slot(SyncHandlers.phantomItemSlot(itemHandler, index)
                                .ignoreMaxStackSize(true)
                                .slotGroup("mixer_items")))
                        .build()));

        return panel;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player) {
        if (!worldIn.isRemote) {
            GuiInfos.PLAYER_ITEM_MAIN_HAND.open(player);
        }
        return super.onItemRightClick(itemStackIn, worldIn, player);
    }
}
