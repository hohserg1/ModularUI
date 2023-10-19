package com.cleanroommc.modularui.drawable;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.screen.GuiScreenWrapper;
import com.cleanroommc.modularui.screen.viewport.GuiContext;
import com.cleanroommc.modularui.utils.GameObjectHelper;
import com.cleanroommc.modularui.utils.JsonHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.NoSuchElementException;

public class ItemDrawable implements IDrawable {

    private ItemStack item = null;

    public ItemDrawable() {
    }

    public ItemDrawable(@Nullable ItemStack item) {
        this.item = item;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void draw(GuiContext context, int x, int y, int width, int height) {
        if (this.item == null) return;
        GL11.glPushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glScalef(width / 16f, height / 16f, 1);
        RenderItem renderItem = GuiScreenWrapper.getItemRenderer();
        renderItem.zLevel = 200;
        renderItem.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), this.item, x, y);
        renderItem.zLevel = 0;
        GuiDraw.afterRenderItemAndEffectIntoGUI(item);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    @Override
    public Icon asIcon() {
        return IDrawable.super.asIcon().size(16);
    }

    public ItemDrawable setItem(@Nullable ItemStack item) {
        this.item = item;
        return this;
    }

    public static ItemDrawable ofJson(JsonObject json) {
        String itemS = JsonHelper.getString(json, null, "item");
        if (itemS == null) throw new JsonParseException("Item property not found!");
        String[] parts = itemS.split(":");
        if (parts.length < 2)
            throw new JsonParseException("Item property must have be in the format 'mod:item_name:meta'");
        int meta = 0;
        if (parts.length > 2) {
            try {
                meta = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                throw new JsonParseException(e);
            }
        }
        ItemStack item;
        try {
            item = GameObjectHelper.getItemStack(parts[0], parts[1], meta);
        } catch (NoSuchElementException e) {
            throw new JsonParseException(e);
        }
        if (json.has("nbt")) {
            try {
                NBTTagCompound nbt = (NBTTagCompound) JsonToNBT.func_150315_a(JsonHelper.getObject(json, new JsonObject(), o -> o, "nbt").toString());
                item.setTagCompound(nbt);
            } catch (NBTException e) {
                throw new JsonParseException(e);
            }
        }
        return new ItemDrawable(item);
    }
}
