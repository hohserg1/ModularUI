package com.cleanroommc.modularui.manager;

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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class GuiManager implements IGuiHandler {

    public static final GuiManager INSTANCE = new GuiManager();

    final TIntObjectMap<GuiInfo> guiInfos = new TIntObjectHashMap<>();
    static ModularScreen queuedClientScreen;
    static NEISettings queuedNEISettings;

    private GuiManager() {
    }

    void register(GuiInfo info) {
        this.guiInfos.put(info.getId(), info);
    }

    @SideOnly(Side.CLIENT)
    public static void checkQueuedScreen() {
        if (queuedClientScreen != null) {
            queuedClientScreen.getContext().setNEISettings(queuedNEISettings);
            GuiScreenWrapper screenWrapper = new GuiScreenWrapper(new ModularContainer(), queuedClientScreen);
            FMLCommonHandler.instance().showGuiScreen(screenWrapper);
            queuedClientScreen = null;
            queuedNEISettings = null;
        }
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
