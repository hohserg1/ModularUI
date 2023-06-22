package com.cleanroommc.modularui.api.sync;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.PacketBuffer;

/**
 * A helper interface for syncing an object value.
 *
 * @param <T> object value type
 */
public interface IValueSyncHandler<T> {

    T getCachedValue();

    void setValue(T value);

    boolean needsSync(boolean isFirstSync);

    void updateAndWrite(PacketBuffer buffer);

    void read(PacketBuffer buffer);

    @SideOnly(Side.CLIENT)
    void updateFromClient(T value);
}
