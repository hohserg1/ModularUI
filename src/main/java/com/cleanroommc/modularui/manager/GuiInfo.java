package com.cleanroommc.modularui.manager;

import com.cleanroommc.modularui.Tags;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.value.sync.GuiSyncManager;
import com.cleanroommc.modularui.widget.WidgetTree;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.function.BiFunction;

public class GuiInfo {

    public static Builder builder() {
        return new Builder();
    }

    private static int nextId = 0;
    private final BiFunction<GuiCreationContext, GuiSyncManager, ModularPanel> mainPanelCreator;
    private final BiFunction<GuiCreationContext, ModularPanel, Object> clientGuiCreator;
    private final int id;

    public GuiInfo(BiFunction<GuiCreationContext, GuiSyncManager, ModularPanel> mainPanelCreator, BiFunction<GuiCreationContext, ModularPanel, Object> clientGuiCreator) {
        this.mainPanelCreator = mainPanelCreator;
        this.clientGuiCreator = clientGuiCreator;
        this.id = nextId++;
        GuiManager.INSTANCE.register(this);
    }

    public int getId() {
        return this.id;
    }

    public void open(EntityPlayer player) {
        open(player, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
    }

    public void open(EntityPlayer player, World world, int x, int y, int z) {
        FMLNetworkHandler.openGui(player, Tags.MODID, this.id, world, x, y, z);
    }

    public ModularPanel createCommonGui(GuiCreationContext context, GuiSyncManager guiSyncManager) {
        ModularPanel panel = this.mainPanelCreator.apply(context, guiSyncManager);
        WidgetTree.collectSyncValues(guiSyncManager, panel);
        return panel;
    }

    @SideOnly(Side.CLIENT)
    public ModularScreen createClientGui(GuiCreationContext context, ModularPanel panel) {
        Object screen = this.clientGuiCreator.apply(context, panel);
        if (!(screen instanceof ModularScreen)) {
            throw new IllegalStateException("Client screen must be an instance of ModularScreen");
        }
        return (ModularScreen) screen;
    }

    public static class Builder {

        private BiFunction<GuiCreationContext, GuiSyncManager, ModularPanel> mainPanelCreator;
        private BiFunction<GuiCreationContext, ModularPanel, Object> clientGuiCreator;

        public Builder commonGui(BiFunction<GuiCreationContext, GuiSyncManager, ModularPanel> mainPanelCreator) {
            this.mainPanelCreator = mainPanelCreator;
            return this;
        }

        public Builder clientGui(BiFunction<GuiCreationContext, ModularPanel, Object> clientGuiCreator) {
            this.clientGuiCreator = clientGuiCreator;
            return this;
        }

        public GuiInfo build() {
            return new GuiInfo(this.mainPanelCreator, this.clientGuiCreator);
        }
    }
}
