package com.cleanroommc.modularui.manager;

import com.cleanroommc.modularui.ModularUI;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.GuiScreenWrapper;
import com.cleanroommc.modularui.screen.ModularContainer;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.NEISettings;
import com.cleanroommc.modularui.value.sync.GuiSyncManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public final class GuiManager implements IGuiHandler {

    public static final GuiManager INSTANCE = new GuiManager();

    private final TIntObjectMap<GuiInfo> guiInfos = new TIntObjectHashMap<>();

    private GuiManager() {
    }

    void register(GuiInfo info) {
        this.guiInfos.put(info.getId(), info);
    }

    @SideOnly(Side.CLIENT)
    public static void openClientUI(EntityPlayer player, ModularScreen screen) {
        if (!NetworkUtils.isClient(player)) {
            ModularUI.LOGGER.info("Tried opening client ui on server!");
            return;
        }
        GuiScreenWrapper screenWrapper = new GuiScreenWrapper(new ModularContainer(), screen);
        FMLCommonHandler.instance().showGuiScreen(screenWrapper);
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        GuiInfo info = this.guiInfos.get(ID);
        if (info == null) return null;
        GuiSyncManager guiSyncManager = new GuiSyncManager(player);
        info.createCommonGui(new GuiCreationContext(player, world, x, y, z, new NEISettings()), guiSyncManager);
        return new ModularContainer(guiSyncManager);
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        GuiInfo info = this.guiInfos.get(ID);
        if (info == null) return null;
        GuiSyncManager guiSyncManager = new GuiSyncManager(player);
        GuiCreationContext context = new GuiCreationContext(player, world, x, y, z, new NEISettings());
        ModularPanel panel = info.createCommonGui(context, guiSyncManager);
        ModularScreen modularScreen = info.createClientGui(context, panel);
        modularScreen.getContext().setNEISettings(context.getNEISettings());
        return new GuiScreenWrapper(new ModularContainer(guiSyncManager), modularScreen);
    }
}
