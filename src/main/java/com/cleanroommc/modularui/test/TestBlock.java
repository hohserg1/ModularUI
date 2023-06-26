package com.cleanroommc.modularui.test;

import com.cleanroommc.modularui.manager.GuiInfos;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TestBlock extends Block implements ITileEntityProvider {

    public final static Block testBlock = new TestBlock();
    public final static ItemBlock testItemBlock = new ItemBlock(testBlock);

    public static void preInit() {
        testBlock.setBlockName("test_block").setBlockTextureName("stone");
        GameRegistry.registerBlock(testBlock, "test_block");
        GameRegistry.registerTileEntity(TestTile.class, "test_block");
        TestItem.testItem.setUnlocalizedName("test_item").setTextureName("diamond");
        GameRegistry.registerItem(TestItem.testItem, "test_item");
    }

    public TestBlock() {
        super(Material.rock);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TestTile();
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer playerIn, int side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            GuiInfos.TILE_ENTITY.open(playerIn, worldIn, x, y, z);
        }
        return true;
    }
}
