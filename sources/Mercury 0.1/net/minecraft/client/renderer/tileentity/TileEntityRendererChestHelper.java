/*
 * Decompiled with CFR 0.145.
 */
package net.minecraft.client.renderer.tileentity;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;

public class TileEntityRendererChestHelper {
    public static TileEntityRendererChestHelper instance = new TileEntityRendererChestHelper();
    private TileEntityChest field_147717_b = new TileEntityChest(0);
    private TileEntityChest field_147718_c = new TileEntityChest(1);
    private TileEntityEnderChest field_147716_d = new TileEntityEnderChest();
    private TileEntityBanner banner = new TileEntityBanner();
    private TileEntitySkull field_179023_f = new TileEntitySkull();
    private static final String __OBFID = "CL_00000946";

    public void renderByItem(ItemStack p_179022_1_) {
        if (p_179022_1_.getItem() == Items.banner) {
            this.banner.setItemValues(p_179022_1_);
            TileEntityRendererDispatcher.instance.renderTileEntityAt(this.banner, 0.0, 0.0, 0.0, 0.0f);
        } else if (p_179022_1_.getItem() == Items.skull) {
            GameProfile var2 = null;
            if (p_179022_1_.hasTagCompound()) {
                NBTTagCompound var3 = p_179022_1_.getTagCompound();
                if (var3.hasKey("SkullOwner", 10)) {
                    var2 = NBTUtil.readGameProfileFromNBT(var3.getCompoundTag("SkullOwner"));
                } else if (var3.hasKey("SkullOwner", 8) && var3.getString("SkullOwner").length() > 0) {
                    var2 = new GameProfile(null, var3.getString("SkullOwner"));
                    var2 = TileEntitySkull.updateGameprofile(var2);
                    var3.removeTag("SkullOwner");
                    var3.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), var2));
                }
            }
            if (TileEntitySkullRenderer.instance != null) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(-0.5f, 0.0f, -0.5f);
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                GlStateManager.disableCull();
                TileEntitySkullRenderer.instance.renderSkull(0.0f, 0.0f, 0.0f, EnumFacing.UP, 0.0f, p_179022_1_.getMetadata(), var2, -1);
                GlStateManager.enableCull();
                GlStateManager.popMatrix();
            }
        } else {
            Block var4 = Block.getBlockFromItem(p_179022_1_.getItem());
            if (var4 == Blocks.ender_chest) {
                TileEntityRendererDispatcher.instance.renderTileEntityAt(this.field_147716_d, 0.0, 0.0, 0.0, 0.0f);
            } else if (var4 == Blocks.trapped_chest) {
                TileEntityRendererDispatcher.instance.renderTileEntityAt(this.field_147718_c, 0.0, 0.0, 0.0, 0.0f);
            } else {
                TileEntityRendererDispatcher.instance.renderTileEntityAt(this.field_147717_b, 0.0, 0.0, 0.0, 0.0f);
            }
        }
    }
}

