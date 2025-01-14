// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import com.google.common.collect.Lists;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.Packet;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.nbt.NBTTagList;

public class TileEntityBanner extends TileEntity
{
    private int baseColor;
    private NBTTagList field_175118_f;
    private boolean field_175119_g;
    private List field_175122_h;
    private List field_175123_i;
    private String field_175121_j;
    private static final String __OBFID = "CL_00002044";
    
    public void setItemValues(final ItemStack p_175112_1_) {
        this.field_175118_f = null;
        if (p_175112_1_.hasTagCompound() && p_175112_1_.getTagCompound().hasKey("BlockEntityTag", 10)) {
            final NBTTagCompound var2 = p_175112_1_.getTagCompound().getCompoundTag("BlockEntityTag");
            if (var2.hasKey("Patterns")) {
                this.field_175118_f = (NBTTagList)var2.getTagList("Patterns", 10).copy();
            }
            if (var2.hasKey("Base", 99)) {
                this.baseColor = var2.getInteger("Base");
            }
            else {
                this.baseColor = (p_175112_1_.getMetadata() & 0xF);
            }
        }
        else {
            this.baseColor = (p_175112_1_.getMetadata() & 0xF);
        }
        this.field_175122_h = null;
        this.field_175123_i = null;
        this.field_175121_j = "";
        this.field_175119_g = true;
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("Base", this.baseColor);
        if (this.field_175118_f != null) {
            compound.setTag("Patterns", this.field_175118_f);
        }
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.baseColor = compound.getInteger("Base");
        this.field_175118_f = compound.getTagList("Patterns", 10);
        this.field_175122_h = null;
        this.field_175123_i = null;
        this.field_175121_j = null;
        this.field_175119_g = true;
    }
    
    @Override
    public Packet getDescriptionPacket() {
        final NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new S35PacketUpdateTileEntity(this.pos, 6, var1);
    }
    
    public int getBaseColor() {
        return this.baseColor;
    }
    
    public static int getBaseColor(final ItemStack stack) {
        final NBTTagCompound var1 = stack.getSubCompound("BlockEntityTag", false);
        return (var1 != null && var1.hasKey("Base")) ? var1.getInteger("Base") : stack.getMetadata();
    }
    
    public static int func_175113_c(final ItemStack p_175113_0_) {
        final NBTTagCompound var1 = p_175113_0_.getSubCompound("BlockEntityTag", false);
        return (var1 != null && var1.hasKey("Patterns")) ? var1.getTagList("Patterns", 10).tagCount() : 0;
    }
    
    public List func_175114_c() {
        this.func_175109_g();
        return this.field_175122_h;
    }
    
    public List func_175110_d() {
        this.func_175109_g();
        return this.field_175123_i;
    }
    
    public String func_175116_e() {
        this.func_175109_g();
        return this.field_175121_j;
    }
    
    private void func_175109_g() {
        if (this.field_175122_h == null || this.field_175123_i == null || this.field_175121_j == null) {
            if (!this.field_175119_g) {
                this.field_175121_j = "";
            }
            else {
                this.field_175122_h = Lists.newArrayList();
                this.field_175123_i = Lists.newArrayList();
                this.field_175122_h.add(EnumBannerPattern.BASE);
                this.field_175123_i.add(EnumDyeColor.func_176766_a(this.baseColor));
                this.field_175121_j = "b" + this.baseColor;
                if (this.field_175118_f != null) {
                    for (int var1 = 0; var1 < this.field_175118_f.tagCount(); ++var1) {
                        final NBTTagCompound var2 = this.field_175118_f.getCompoundTagAt(var1);
                        final EnumBannerPattern var3 = EnumBannerPattern.func_177268_a(var2.getString("Pattern"));
                        if (var3 != null) {
                            this.field_175122_h.add(var3);
                            final int var4 = var2.getInteger("Color");
                            this.field_175123_i.add(EnumDyeColor.func_176766_a(var4));
                            this.field_175121_j = String.valueOf(this.field_175121_j) + var3.func_177273_b() + var4;
                        }
                    }
                }
            }
        }
    }
    
    public static void func_175117_e(final ItemStack p_175117_0_) {
        final NBTTagCompound var1 = p_175117_0_.getSubCompound("BlockEntityTag", false);
        if (var1 != null && var1.hasKey("Patterns", 9)) {
            final NBTTagList var2 = var1.getTagList("Patterns", 10);
            if (var2.tagCount() > 0) {
                var2.removeTag(var2.tagCount() - 1);
                if (var2.hasNoTags()) {
                    p_175117_0_.getTagCompound().removeTag("BlockEntityTag");
                    if (p_175117_0_.getTagCompound().hasNoTags()) {
                        p_175117_0_.setTagCompound(null);
                    }
                }
            }
        }
    }
    
    public enum EnumBannerPattern
    {
        BASE("BASE", 0, "BASE", 0, "base", "b"), 
        SQUARE_BOTTOM_LEFT("SQUARE_BOTTOM_LEFT", 1, "SQUARE_BOTTOM_LEFT", 1, "square_bottom_left", "bl", "   ", "   ", "#  "), 
        SQUARE_BOTTOM_RIGHT("SQUARE_BOTTOM_RIGHT", 2, "SQUARE_BOTTOM_RIGHT", 2, "square_bottom_right", "br", "   ", "   ", "  #"), 
        SQUARE_TOP_LEFT("SQUARE_TOP_LEFT", 3, "SQUARE_TOP_LEFT", 3, "square_top_left", "tl", "#  ", "   ", "   "), 
        SQUARE_TOP_RIGHT("SQUARE_TOP_RIGHT", 4, "SQUARE_TOP_RIGHT", 4, "square_top_right", "tr", "  #", "   ", "   "), 
        STRIPE_BOTTOM("STRIPE_BOTTOM", 5, "STRIPE_BOTTOM", 5, "stripe_bottom", "bs", "   ", "   ", "###"), 
        STRIPE_TOP("STRIPE_TOP", 6, "STRIPE_TOP", 6, "stripe_top", "ts", "###", "   ", "   "), 
        STRIPE_LEFT("STRIPE_LEFT", 7, "STRIPE_LEFT", 7, "stripe_left", "ls", "#  ", "#  ", "#  "), 
        STRIPE_RIGHT("STRIPE_RIGHT", 8, "STRIPE_RIGHT", 8, "stripe_right", "rs", "  #", "  #", "  #"), 
        STRIPE_CENTER("STRIPE_CENTER", 9, "STRIPE_CENTER", 9, "stripe_center", "cs", " # ", " # ", " # "), 
        STRIPE_MIDDLE("STRIPE_MIDDLE", 10, "STRIPE_MIDDLE", 10, "stripe_middle", "ms", "   ", "###", "   "), 
        STRIPE_DOWNRIGHT("STRIPE_DOWNRIGHT", 11, "STRIPE_DOWNRIGHT", 11, "stripe_downright", "drs", "#  ", " # ", "  #"), 
        STRIPE_DOWNLEFT("STRIPE_DOWNLEFT", 12, "STRIPE_DOWNLEFT", 12, "stripe_downleft", "dls", "  #", " # ", "#  "), 
        STRIPE_SMALL("STRIPE_SMALL", 13, "STRIPE_SMALL", 13, "small_stripes", "ss", "# #", "# #", "   "), 
        CROSS("CROSS", 14, "CROSS", 14, "cross", "cr", "# #", " # ", "# #"), 
        STRAIGHT_CROSS("STRAIGHT_CROSS", 15, "STRAIGHT_CROSS", 15, "straight_cross", "sc", " # ", "###", " # "), 
        TRIANGLE_BOTTOM("TRIANGLE_BOTTOM", 16, "TRIANGLE_BOTTOM", 16, "triangle_bottom", "bt", "   ", " # ", "# #"), 
        TRIANGLE_TOP("TRIANGLE_TOP", 17, "TRIANGLE_TOP", 17, "triangle_top", "tt", "# #", " # ", "   "), 
        TRIANGLES_BOTTOM("TRIANGLES_BOTTOM", 18, "TRIANGLES_BOTTOM", 18, "triangles_bottom", "bts", "   ", "# #", " # "), 
        TRIANGLES_TOP("TRIANGLES_TOP", 19, "TRIANGLES_TOP", 19, "triangles_top", "tts", " # ", "# #", "   "), 
        DIAGONAL_LEFT("DIAGONAL_LEFT", 20, "DIAGONAL_LEFT", 20, "diagonal_left", "ld", "## ", "#  ", "   "), 
        DIAGONAL_RIGHT("DIAGONAL_RIGHT", 21, "DIAGONAL_RIGHT", 21, "diagonal_up_right", "rd", "   ", "  #", " ##"), 
        DIAGONAL_LEFT_MIRROR("DIAGONAL_LEFT_MIRROR", 22, "DIAGONAL_LEFT_MIRROR", 22, "diagonal_up_left", "lud", "   ", "#  ", "## "), 
        DIAGONAL_RIGHT_MIRROR("DIAGONAL_RIGHT_MIRROR", 23, "DIAGONAL_RIGHT_MIRROR", 23, "diagonal_right", "rud", " ##", "  #", "   "), 
        CIRCLE_MIDDLE("CIRCLE_MIDDLE", 24, "CIRCLE_MIDDLE", 24, "circle", "mc", "   ", " # ", "   "), 
        RHOMBUS_MIDDLE("RHOMBUS_MIDDLE", 25, "RHOMBUS_MIDDLE", 25, "rhombus", "mr", " # ", "# #", " # "), 
        HALF_VERTICAL("HALF_VERTICAL", 26, "HALF_VERTICAL", 26, "half_vertical", "vh", "## ", "## ", "## "), 
        HALF_HORIZONTAL("HALF_HORIZONTAL", 27, "HALF_HORIZONTAL", 27, "half_horizontal", "hh", "###", "###", "   "), 
        HALF_VERTICAL_MIRROR("HALF_VERTICAL_MIRROR", 28, "HALF_VERTICAL_MIRROR", 28, "half_vertical_right", "vhr", " ##", " ##", " ##"), 
        HALF_HORIZONTAL_MIRROR("HALF_HORIZONTAL_MIRROR", 29, "HALF_HORIZONTAL_MIRROR", 29, "half_horizontal_bottom", "hhb", "   ", "###", "###"), 
        BORDER("BORDER", 30, "BORDER", 30, "border", "bo", "###", "# #", "###"), 
        CURLY_BORDER("CURLY_BORDER", 31, "CURLY_BORDER", 31, "curly_border", "cbo", new ItemStack(Blocks.vine)), 
        CREEPER("CREEPER", 32, "CREEPER", 32, "creeper", "cre", new ItemStack(Items.skull, 1, 4)), 
        GRADIENT("GRADIENT", 33, "GRADIENT", 33, "gradient", "gra", "# #", " # ", " # "), 
        GRADIENT_UP("GRADIENT_UP", 34, "GRADIENT_UP", 34, "gradient_up", "gru", " # ", " # ", "# #"), 
        BRICKS("BRICKS", 35, "BRICKS", 35, "bricks", "bri", new ItemStack(Blocks.brick_block)), 
        SKULL("SKULL", 36, "SKULL", 36, "skull", "sku", new ItemStack(Items.skull, 1, 1)), 
        FLOWER("FLOWER", 37, "FLOWER", 37, "flower", "flo", new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.OXEYE_DAISY.func_176968_b())), 
        MOJANG("MOJANG", 38, "MOJANG", 38, "mojang", "moj", new ItemStack(Items.golden_apple, 1, 1));
        
        private String field_177284_N;
        private String field_177285_O;
        private String[] field_177291_P;
        private ItemStack field_177290_Q;
        private static final EnumBannerPattern[] $VALUES;
        private static final String __OBFID = "CL_00002043";
        
        static {
            $VALUES = new EnumBannerPattern[] { EnumBannerPattern.BASE, EnumBannerPattern.SQUARE_BOTTOM_LEFT, EnumBannerPattern.SQUARE_BOTTOM_RIGHT, EnumBannerPattern.SQUARE_TOP_LEFT, EnumBannerPattern.SQUARE_TOP_RIGHT, EnumBannerPattern.STRIPE_BOTTOM, EnumBannerPattern.STRIPE_TOP, EnumBannerPattern.STRIPE_LEFT, EnumBannerPattern.STRIPE_RIGHT, EnumBannerPattern.STRIPE_CENTER, EnumBannerPattern.STRIPE_MIDDLE, EnumBannerPattern.STRIPE_DOWNRIGHT, EnumBannerPattern.STRIPE_DOWNLEFT, EnumBannerPattern.STRIPE_SMALL, EnumBannerPattern.CROSS, EnumBannerPattern.STRAIGHT_CROSS, EnumBannerPattern.TRIANGLE_BOTTOM, EnumBannerPattern.TRIANGLE_TOP, EnumBannerPattern.TRIANGLES_BOTTOM, EnumBannerPattern.TRIANGLES_TOP, EnumBannerPattern.DIAGONAL_LEFT, EnumBannerPattern.DIAGONAL_RIGHT, EnumBannerPattern.DIAGONAL_LEFT_MIRROR, EnumBannerPattern.DIAGONAL_RIGHT_MIRROR, EnumBannerPattern.CIRCLE_MIDDLE, EnumBannerPattern.RHOMBUS_MIDDLE, EnumBannerPattern.HALF_VERTICAL, EnumBannerPattern.HALF_HORIZONTAL, EnumBannerPattern.HALF_VERTICAL_MIRROR, EnumBannerPattern.HALF_HORIZONTAL_MIRROR, EnumBannerPattern.BORDER, EnumBannerPattern.CURLY_BORDER, EnumBannerPattern.CREEPER, EnumBannerPattern.GRADIENT, EnumBannerPattern.GRADIENT_UP, EnumBannerPattern.BRICKS, EnumBannerPattern.SKULL, EnumBannerPattern.FLOWER, EnumBannerPattern.MOJANG };
        }
        
        private EnumBannerPattern(final String s, final int n, final String p_i45670_1_, final int p_i45670_2_, final String p_i45670_3_, final String p_i45670_4_) {
            this.field_177291_P = new String[3];
            this.field_177284_N = p_i45670_3_;
            this.field_177285_O = p_i45670_4_;
        }
        
        private EnumBannerPattern(final String s, final int n, final String p_i45671_1_, final int p_i45671_2_, final String p_i45671_3_, final String p_i45671_4_, final ItemStack p_i45671_5_) {
            this(s, n, p_i45671_1_, p_i45671_2_, p_i45671_3_, p_i45671_4_);
            this.field_177290_Q = p_i45671_5_;
        }
        
        private EnumBannerPattern(final String s, final int n, final String p_i45672_1_, final int p_i45672_2_, final String p_i45672_3_, final String p_i45672_4_, final String p_i45672_5_, final String p_i45672_6_, final String p_i45672_7_) {
            this(s, n, p_i45672_1_, p_i45672_2_, p_i45672_3_, p_i45672_4_);
            this.field_177291_P[0] = p_i45672_5_;
            this.field_177291_P[1] = p_i45672_6_;
            this.field_177291_P[2] = p_i45672_7_;
        }
        
        public String func_177271_a() {
            return this.field_177284_N;
        }
        
        public String func_177273_b() {
            return this.field_177285_O;
        }
        
        public String[] func_177267_c() {
            return this.field_177291_P;
        }
        
        public boolean func_177270_d() {
            return this.field_177290_Q != null || this.field_177291_P[0] != null;
        }
        
        public boolean func_177269_e() {
            return this.field_177290_Q != null;
        }
        
        public ItemStack func_177272_f() {
            return this.field_177290_Q;
        }
        
        public static EnumBannerPattern func_177268_a(final String p_177268_0_) {
            for (final EnumBannerPattern var4 : values()) {
                if (var4.field_177285_O.equals(p_177268_0_)) {
                    return var4;
                }
            }
            return null;
        }
    }
}
