package com.cleanroommc.modularui.manager;

import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.NEISettings;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

/**
 * Helper class to open client only GUIs.
 */
@SideOnly(Side.CLIENT)
public class ClientGUI {

    private ClientGUI() {
    }

    /**
     * Opens a modular screen on the next client tick with default NEI settings.
     *
     * @param screen new modular screen
     */
    public static void open(@NotNull ModularScreen screen) {
        open(screen, new NEISettings());
    }

    /**
     * Opens a modular screen on the next client tick with custom NEI settings.
     * It needs to be opened in next tick, because we might break the current GUI if we open it now.
     *
     * @param screen      new modular screen
     * @param neiSettings custom NEI settings
     */
    public static void open(@NotNull ModularScreen screen, @NotNull NEISettings neiSettings) {
        GuiManager.queuedClientScreen = screen;
        GuiManager.queuedNEISettings = neiSettings;
    }
}
