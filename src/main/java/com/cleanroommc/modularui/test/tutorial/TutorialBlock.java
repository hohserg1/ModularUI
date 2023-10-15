package com.cleanroommc.modularui.test.tutorial;

import com.cleanroommc.modularui.manager.GuiInfos;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TutorialBlock extends Block implements ITileEntityProvider {

    public static final Block testBlock = new TutorialBlock();
    public static final ItemBlock testItemBlock = new ItemBlock(testBlock);

    public static void preInit() {
        testBlock.setBlockName("tutorial_block").setBlockTextureName("stone");
        GameRegistry.registerBlock(testBlock, "tutorial_block");
        GameRegistry.registerTileEntity(TutorialTile.class, "tutorial_block");
    }

    public TutorialBlock() {
        super(Material.rock);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@NotNull World worldIn, int meta) {
        return new TutorialTile();
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer playerIn, int side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            GuiInfos.TILE_ENTITY.open(playerIn, worldIn, x, y, z);
        }
        return true;
    }
}
