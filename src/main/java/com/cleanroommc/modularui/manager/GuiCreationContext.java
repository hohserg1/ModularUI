package com.cleanroommc.modularui.manager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;

public class GuiCreationContext {

    private final EntityPlayer player;
    private final World world;
    private final int x, y, z;

    @ApiStatus.Internal
    public GuiCreationContext(EntityPlayer player, World world, int x, int y, int z) {
        this.player = player;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }

    public World getWorld() {
        return this.world;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public ItemStack getMainHandItem() {
        return this.player.getHeldItem();
    }

    public void setItemInMainHand(ItemStack item) {
        this.player.setCurrentItemOrArmor(0, item);
    }

    public TileEntity getTileEntity() {
        return this.world.getTileEntity(x, y, z);
    }
}
