package com.cleanroommc.modularui.screen;

import com.cleanroommc.modularui.ModularUI;
import com.cleanroommc.modularui.future.IItemHandler;
import com.cleanroommc.modularui.future.ItemHandlerHelper;
import com.cleanroommc.modularui.future.PlayerMainInvWrapper;
import com.cleanroommc.modularui.future.SlotItemHandler;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.value.sync.GuiSyncManager;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.SlotGroup;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ModularContainer extends Container {

    public static ModularContainer getCurrent(EntityPlayer player) {
        Container container = player.openContainer;
        if (container instanceof ModularContainer) {
            return (ModularContainer) container;
        }
        return null;
    }

    private final GuiSyncManager guiSyncManager;
    private boolean init = true;
    private final List<ModularSlot> slots = new ArrayList<>();
    private final List<ModularSlot> shiftClickSlots = new ArrayList<>();

    public ModularContainer(GuiSyncManager guiSyncManager) {
        this.guiSyncManager = Objects.requireNonNull(guiSyncManager);
        this.guiSyncManager.construct(this);
        sortShiftClickSlots();
    }

    @SideOnly(Side.CLIENT)
    public ModularContainer() {
        this.guiSyncManager = null;
    }

    @Override
    public void onContainerClosed(@NotNull EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        if (this.guiSyncManager != null) {
            this.guiSyncManager.onClose();
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        this.guiSyncManager.detectAndSendChanges(this.init);
        this.init = false;
    }

    private void sortShiftClickSlots() {
        this.shiftClickSlots.sort(Comparator.comparingInt(slot -> Objects.requireNonNull(slot.getSlotGroup()).getShiftClickPriority()));
    }

    @Override
    public void putStacksInSlots(ItemStack[] items) {
        if (this.inventorySlots.size() != items.length) {
            ModularUI.LOGGER.error("Here are {} slots, but expected {}", this.inventorySlots.size(), items.length);
        }
        for (int i = 0; i < Math.min(this.inventorySlots.size(), items.length); ++i) {
            this.getSlot(i).putStack(items[i]);
        }
    }

    @ApiStatus.Internal
    public void registerSlot(ModularSlot slot) {
        if (this.inventorySlots.contains(slot)) {
            throw new IllegalArgumentException();
        }
        addSlotToContainer(slot);
        this.slots.add(slot);
        if (slot.getSlotGroupName() != null) {
            SlotGroup slotGroup = getSyncManager().getSlotGroup(slot.getSlotGroupName());
            if (slotGroup == null) {
                ModularUI.LOGGER.throwing(new IllegalArgumentException("SlotGroup '" + slot.getSlotGroupName() + "' is not registered!"));
                return;
            }
            slot.slotGroup(slotGroup);
        }
        if (slot.getSlotGroup() != null) {
            SlotGroup slotGroup = slot.getSlotGroup();
            if (slotGroup.allowShiftTransfer()) {
                this.shiftClickSlots.add(slot);
                if (!this.init) {
                    sortShiftClickSlots();
                }
            }
        }
    }

    public GuiSyncManager getSyncManager() {
        if (this.guiSyncManager == null) {
            throw new IllegalStateException("GuiSyncManager is not available for client only GUI's.");
        }
        return this.guiSyncManager;
    }

    public boolean isClient() {
        return this.guiSyncManager == null || NetworkUtils.isClient(this.guiSyncManager.getPlayer());
    }

    public boolean isClientOnly() {
        return this.guiSyncManager == null;
    }

    @Override
    public boolean canInteractWith(@NotNull EntityPlayer playerIn) {
        return true;
    }

    @Override
    @Nullable
    public ItemStack transferStackInSlot(@NotNull EntityPlayer playerIn, int index) {
        ModularSlot slot = this.slots.get(index);
        if (!slot.isPhantom()) {
            ItemStack stack = slot.getStack();
            if (stack != null) {
                ItemStack remainder = transferItem(slot, stack.copy());
                stack.stackSize = remainder.stackSize;
                if (stack.stackSize < 1) {
                    slot.putStack(null);
                }
                return null;
            }
        }
        return null;
    }

    protected ItemStack transferItem(ModularSlot fromSlot, ItemStack stack) {
        @Nullable SlotGroup fromSlotGroup = fromSlot.getSlotGroup();
        for (ModularSlot slot : this.shiftClickSlots) {
            SlotGroup slotGroup = Objects.requireNonNull(slot.getSlotGroup());
            // func_111238_b: isEnabled
            if (slotGroup != fromSlotGroup && slot.func_111238_b() && slot.isItemValid(stack)) {
                ItemStack itemstack = slot.getStack();
                if (slot.isPhantom()) {
                    if (itemstack == null || (ItemHandlerHelper.canItemStacksStack(stack, itemstack) && itemstack.stackSize < slot.getSlotStackLimit())) {
                        slot.putStack(stack.copy());
                        return stack;
                    }
                } else if (ItemHandlerHelper.canItemStacksStack(stack, itemstack)) {
                    int j = itemstack.stackSize + stack.stackSize;
                    int maxSize = Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize());

                    if (j <= maxSize) {
                        stack.stackSize = 0;
                        itemstack.stackSize = j;
                        slot.onSlotChanged();
                    } else if (itemstack.stackSize < maxSize) {
                        stack.stackSize -= maxSize - itemstack.stackSize;
                        itemstack.stackSize = maxSize;
                        slot.onSlotChanged();
                    }

                    if (stack.stackSize < 1) {
                        return stack;
                    }
                }
            }
        }
        for (ModularSlot slot : this.shiftClickSlots) {
            ItemStack itemstack = slot.getStack();
            SlotGroup slotGroup = Objects.requireNonNull(slot.getSlotGroup());
            // func_111238_b: isEnabled
            if (slotGroup != fromSlotGroup && slot.func_111238_b() && itemstack == null && slot.isItemValid(stack)) {
                if (stack.stackSize > slot.getSlotStackLimit()) {
                    slot.putStack(stack.splitStack(slot.getSlotStackLimit()));
                } else {
                    slot.putStack(stack.splitStack(stack.stackSize));
                }
                if (stack.stackSize < 1) {
                    break;
                }
            }
        }
        return stack;
    }

    private static boolean isPlayerSlot(Slot slot) {
        if (slot == null) return false;
        if (slot.inventory instanceof InventoryPlayer) {
            return slot.getSlotIndex() >= 0 && slot.getSlotIndex() < 36;
        }
        if (slot instanceof SlotItemHandler) {
            IItemHandler iItemHandler = ((SlotItemHandler) slot).getItemHandler();
            if (iItemHandler instanceof PlayerMainInvWrapper) {
                return slot.getSlotIndex() >= 0 && slot.getSlotIndex() < 36;
            }
        }
        return false;
    }
}
