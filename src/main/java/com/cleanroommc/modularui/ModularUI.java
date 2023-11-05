package com.cleanroommc.modularui;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.launchwrapper.Launch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = ModularUI.ID, name = Tags.MODNAME, version = Tags.VERSION, dependencies = ModularUI.DEPENDENCIES, guiFactory = ModularUI.GUI_FACTORY)
public class ModularUI {

    static final String DEPENDENCIES = "required-after:gtnhmixins@[2.0.1,); "
        + "required-after:NotEnoughItems@[2.3.27-GTNH,);"
        + "after:hodgepodge@[2.0.0,);"
        + "before:gregtech";
    static final String GUI_FACTORY = Tags.GROUPNAME + ".config.GuiFactory";

    public static final String ID = Tags.MODID;

    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static final String MODID_GT5U = "gregtech";
    public static final String MODID_GT6 = "gregapi_post";
    public static final boolean isGT5ULoaded = Loader.isModLoaded(MODID_GT5U) && !Loader.isModLoaded(MODID_GT6);
    public static final boolean isHodgepodgeLoaded = Loader.isModLoaded("hodgepodge");

    @SidedProxy(
        modId = ModularUI.ID,
        clientSide = Tags.GROUPNAME + ".ClientProxy",
        serverSide = Tags.GROUPNAME + ".CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static ModularUI INSTANCE;

    public static final boolean isDevEnv = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void onServerLoad(FMLServerStartingEvent event) {
        proxy.onServerLoad(event);
    }
}
