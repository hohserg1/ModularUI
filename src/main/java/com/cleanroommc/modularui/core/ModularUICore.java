package com.cleanroommc.modularui.core;

import com.cleanroommc.modularui.mixinplugin.Mixins;
import com.gtnewhorizon.gtnhmixins.IEarlyMixinLoader;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import net.minecraft.launchwrapper.Launch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.TransformerExclusions("com.cleanroommc.modularui.core")
public class ModularUICore implements IFMLLoadingPlugin, IEarlyMixinLoader {

    public static final Logger LOGGER = LogManager.getLogger("modularui");
    public static final boolean isDevEnv;

    static {
        boolean dev;
        try {
            dev = Launch.classLoader.getClassBytes("net.minecraft.world.World") != null;
        } catch (IOException e) {
            dev = false;
        }
        isDevEnv = dev;
    }

    @Override
    public String getMixinConfig() {
        return "mixins.modularui.early.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedCoreMods) {
        final List<String> mixins = new ArrayList<>();
        for (Mixins mixin : Mixins.values()) {
            if (mixin.phase == Mixins.Phase.EARLY && mixin.shouldLoad(loadedCoreMods, Collections.emptySet())) {
                mixins.add(mixin.mixinClass);
            }
        }
        return mixins;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"com.cleanroommc.modularui.core.ModularUITransformer"};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
