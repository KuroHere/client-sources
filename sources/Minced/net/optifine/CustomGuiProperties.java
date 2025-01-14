// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.block.BlockChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.optifine.reflect.ReflectorField;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.optifine.reflect.Reflector;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.world.IWorldNameable;
import net.minecraft.client.gui.GuiScreen;
import net.optifine.config.Matches;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.math.BlockPos;
import java.util.Iterator;
import net.optifine.util.StrUtils;
import java.util.HashMap;
import net.optifine.util.TextureUtils;
import net.minecraft.src.Config;
import net.optifine.config.ConnectedParser;
import java.util.Properties;
import net.minecraft.item.EnumDyeColor;
import net.optifine.config.VillagerProfession;
import net.optifine.config.RangeListInt;
import net.minecraft.world.biome.Biome;
import net.optifine.config.NbtTagValue;
import net.minecraft.util.ResourceLocation;
import java.util.Map;

public class CustomGuiProperties
{
    private String fileName;
    private String basePath;
    private EnumContainer container;
    private Map<ResourceLocation, ResourceLocation> textureLocations;
    private NbtTagValue nbtName;
    private Biome[] biomes;
    private RangeListInt heights;
    private Boolean large;
    private Boolean trapped;
    private Boolean christmas;
    private Boolean ender;
    private RangeListInt levels;
    private VillagerProfession[] professions;
    private EnumVariant[] variants;
    private EnumDyeColor[] colors;
    private static final EnumVariant[] VARIANTS_HORSE;
    private static final EnumVariant[] VARIANTS_DISPENSER;
    private static final EnumVariant[] VARIANTS_INVALID;
    private static final EnumDyeColor[] COLORS_INVALID;
    private static final ResourceLocation ANVIL_GUI_TEXTURE;
    private static final ResourceLocation BEACON_GUI_TEXTURE;
    private static final ResourceLocation BREWING_STAND_GUI_TEXTURE;
    private static final ResourceLocation CHEST_GUI_TEXTURE;
    private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURE;
    private static final ResourceLocation HORSE_GUI_TEXTURE;
    private static final ResourceLocation DISPENSER_GUI_TEXTURE;
    private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE;
    private static final ResourceLocation FURNACE_GUI_TEXTURE;
    private static final ResourceLocation HOPPER_GUI_TEXTURE;
    private static final ResourceLocation INVENTORY_GUI_TEXTURE;
    private static final ResourceLocation SHULKER_BOX_GUI_TEXTURE;
    private static final ResourceLocation VILLAGER_GUI_TEXTURE;
    
    public CustomGuiProperties(final Properties props, final String path) {
        this.fileName = null;
        this.basePath = null;
        this.container = null;
        this.textureLocations = null;
        this.nbtName = null;
        this.biomes = null;
        this.heights = null;
        this.large = null;
        this.trapped = null;
        this.christmas = null;
        this.ender = null;
        this.levels = null;
        this.professions = null;
        this.variants = null;
        this.colors = null;
        final ConnectedParser connectedparser = new ConnectedParser("CustomGuis");
        this.fileName = connectedparser.parseName(path);
        this.basePath = connectedparser.parseBasePath(path);
        this.container = (EnumContainer)connectedparser.parseEnum(props.getProperty("container"), EnumContainer.values(), "container");
        this.textureLocations = parseTextureLocations(props, "texture", this.container, "textures/gui/", this.basePath);
        this.nbtName = connectedparser.parseNbtTagValue("name", props.getProperty("name"));
        this.biomes = connectedparser.parseBiomes(props.getProperty("biomes"));
        this.heights = connectedparser.parseRangeListInt(props.getProperty("heights"));
        this.large = connectedparser.parseBooleanObject(props.getProperty("large"));
        this.trapped = connectedparser.parseBooleanObject(props.getProperty("trapped"));
        this.christmas = connectedparser.parseBooleanObject(props.getProperty("christmas"));
        this.ender = connectedparser.parseBooleanObject(props.getProperty("ender"));
        this.levels = connectedparser.parseRangeListInt(props.getProperty("levels"));
        this.professions = connectedparser.parseProfessions(props.getProperty("professions"));
        final EnumVariant[] acustomguiproperties$enumvariant = getContainerVariants(this.container);
        this.variants = (EnumVariant[])connectedparser.parseEnums(props.getProperty("variants"), acustomguiproperties$enumvariant, "variants", CustomGuiProperties.VARIANTS_INVALID);
        this.colors = parseEnumDyeColors(props.getProperty("colors"));
    }
    
    private static EnumVariant[] getContainerVariants(final EnumContainer cont) {
        if (cont == EnumContainer.HORSE) {
            return CustomGuiProperties.VARIANTS_HORSE;
        }
        return (cont == EnumContainer.DISPENSER) ? CustomGuiProperties.VARIANTS_DISPENSER : new EnumVariant[0];
    }
    
    private static EnumDyeColor[] parseEnumDyeColors(String str) {
        if (str == null) {
            return null;
        }
        str = str.toLowerCase();
        final String[] astring = Config.tokenize(str, " ");
        final EnumDyeColor[] aenumdyecolor = new EnumDyeColor[astring.length];
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final EnumDyeColor enumdyecolor = parseEnumDyeColor(s);
            if (enumdyecolor == null) {
                warn("Invalid color: " + s);
                return CustomGuiProperties.COLORS_INVALID;
            }
            aenumdyecolor[i] = enumdyecolor;
        }
        return aenumdyecolor;
    }
    
    private static EnumDyeColor parseEnumDyeColor(final String str) {
        if (str == null) {
            return null;
        }
        final EnumDyeColor[] aenumdyecolor = EnumDyeColor.values();
        for (int i = 0; i < aenumdyecolor.length; ++i) {
            final EnumDyeColor enumdyecolor = aenumdyecolor[i];
            if (enumdyecolor.getName().equals(str)) {
                return enumdyecolor;
            }
            if (enumdyecolor.getTranslationKey().equals(str)) {
                return enumdyecolor;
            }
        }
        return null;
    }
    
    private static ResourceLocation parseTextureLocation(String str, final String basePath) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        String s = TextureUtils.fixResourcePath(str, basePath);
        if (!s.endsWith(".png")) {
            s += ".png";
        }
        return new ResourceLocation(basePath + "/" + s);
    }
    
    private static Map<ResourceLocation, ResourceLocation> parseTextureLocations(final Properties props, final String property, final EnumContainer container, final String pathPrefix, final String basePath) {
        final Map<ResourceLocation, ResourceLocation> map = new HashMap<ResourceLocation, ResourceLocation>();
        final String s = props.getProperty(property);
        if (s != null) {
            final ResourceLocation resourcelocation = getGuiTextureLocation(container);
            final ResourceLocation resourcelocation2 = parseTextureLocation(s, basePath);
            if (resourcelocation != null && resourcelocation2 != null) {
                map.put(resourcelocation, resourcelocation2);
            }
        }
        final String s2 = property + ".";
        for (final Object s3 : props.keySet()) {
            final String s4 = (String)s3;
            if (s4.startsWith(s2)) {
                String s5 = s4.substring(s2.length());
                s5 = s5.replace('\\', '/');
                s5 = StrUtils.removePrefixSuffix(s5, "/", ".png");
                final String s6 = pathPrefix + s5 + ".png";
                final String s7 = props.getProperty(s4);
                final ResourceLocation resourcelocation3 = new ResourceLocation(s6);
                final ResourceLocation resourcelocation4 = parseTextureLocation(s7, basePath);
                map.put(resourcelocation3, resourcelocation4);
            }
        }
        return map;
    }
    
    private static ResourceLocation getGuiTextureLocation(final EnumContainer container) {
        if (container == null) {
            return null;
        }
        switch (container) {
            case ANVIL: {
                return CustomGuiProperties.ANVIL_GUI_TEXTURE;
            }
            case BEACON: {
                return CustomGuiProperties.BEACON_GUI_TEXTURE;
            }
            case BREWING_STAND: {
                return CustomGuiProperties.BREWING_STAND_GUI_TEXTURE;
            }
            case CHEST: {
                return CustomGuiProperties.CHEST_GUI_TEXTURE;
            }
            case CRAFTING: {
                return CustomGuiProperties.CRAFTING_TABLE_GUI_TEXTURE;
            }
            case CREATIVE: {
                return null;
            }
            case DISPENSER: {
                return CustomGuiProperties.DISPENSER_GUI_TEXTURE;
            }
            case ENCHANTMENT: {
                return CustomGuiProperties.ENCHANTMENT_TABLE_GUI_TEXTURE;
            }
            case FURNACE: {
                return CustomGuiProperties.FURNACE_GUI_TEXTURE;
            }
            case HOPPER: {
                return CustomGuiProperties.HOPPER_GUI_TEXTURE;
            }
            case HORSE: {
                return CustomGuiProperties.HORSE_GUI_TEXTURE;
            }
            case INVENTORY: {
                return CustomGuiProperties.INVENTORY_GUI_TEXTURE;
            }
            case SHULKER_BOX: {
                return CustomGuiProperties.SHULKER_BOX_GUI_TEXTURE;
            }
            case VILLAGER: {
                return CustomGuiProperties.VILLAGER_GUI_TEXTURE;
            }
            default: {
                return null;
            }
        }
    }
    
    public boolean isValid(final String path) {
        if (this.fileName == null || this.fileName.length() <= 0) {
            warn("No name found: " + path);
            return false;
        }
        if (this.basePath == null) {
            warn("No base path found: " + path);
            return false;
        }
        if (this.container == null) {
            warn("No container found: " + path);
            return false;
        }
        if (this.textureLocations.isEmpty()) {
            warn("No texture found: " + path);
            return false;
        }
        if (this.professions == ConnectedParser.PROFESSIONS_INVALID) {
            warn("Invalid professions or careers: " + path);
            return false;
        }
        if (this.variants == CustomGuiProperties.VARIANTS_INVALID) {
            warn("Invalid variants: " + path);
            return false;
        }
        if (this.colors == CustomGuiProperties.COLORS_INVALID) {
            warn("Invalid colors: " + path);
            return false;
        }
        return true;
    }
    
    private static void warn(final String str) {
        Config.warn("[CustomGuis] " + str);
    }
    
    private boolean matchesGeneral(final EnumContainer ec, final BlockPos pos, final IBlockAccess blockAccess) {
        if (this.container != ec) {
            return false;
        }
        if (this.biomes != null) {
            final Biome biome = blockAccess.getBiome(pos);
            if (!Matches.biome(biome, this.biomes)) {
                return false;
            }
        }
        return this.heights == null || this.heights.isInRange(pos.getY());
    }
    
    public boolean matchesPos(final EnumContainer ec, final BlockPos pos, final IBlockAccess blockAccess, final GuiScreen screen) {
        if (!this.matchesGeneral(ec, pos, blockAccess)) {
            return false;
        }
        if (this.nbtName != null) {
            final String s = getName(screen);
            if (!this.nbtName.matchesValue(s)) {
                return false;
            }
        }
        switch (ec) {
            case BEACON: {
                return this.matchesBeacon(pos, blockAccess);
            }
            case CHEST: {
                return this.matchesChest(pos, blockAccess);
            }
            case DISPENSER: {
                return this.matchesDispenser(pos, blockAccess);
            }
            case SHULKER_BOX: {
                return this.matchesShulker(pos, blockAccess);
            }
            default: {
                return true;
            }
        }
    }
    
    public static String getName(final GuiScreen screen) {
        final IWorldNameable iworldnameable = getWorldNameable(screen);
        return (iworldnameable == null) ? null : iworldnameable.getDisplayName().getUnformattedText();
    }
    
    private static IWorldNameable getWorldNameable(final GuiScreen screen) {
        if (screen instanceof GuiBeacon) {
            return getWorldNameable(screen, Reflector.GuiBeacon_tileBeacon);
        }
        if (screen instanceof GuiBrewingStand) {
            return getWorldNameable(screen, Reflector.GuiBrewingStand_tileBrewingStand);
        }
        if (screen instanceof GuiChest) {
            return getWorldNameable(screen, Reflector.GuiChest_lowerChestInventory);
        }
        if (screen instanceof GuiDispenser) {
            return ((GuiDispenser)screen).dispenserInventory;
        }
        if (screen instanceof GuiEnchantment) {
            return getWorldNameable(screen, Reflector.GuiEnchantment_nameable);
        }
        if (screen instanceof GuiFurnace) {
            return getWorldNameable(screen, Reflector.GuiFurnace_tileFurnace);
        }
        if (screen instanceof GuiHopper) {
            return getWorldNameable(screen, Reflector.GuiHopper_hopperInventory);
        }
        return (screen instanceof GuiShulkerBox) ? getWorldNameable(screen, Reflector.GuiShulkerBox_inventory) : null;
    }
    
    private static IWorldNameable getWorldNameable(final GuiScreen screen, final ReflectorField fieldInventory) {
        final Object object = Reflector.getFieldValue(screen, fieldInventory);
        return (object instanceof IWorldNameable) ? ((IWorldNameable)object) : null;
    }
    
    private boolean matchesBeacon(final BlockPos pos, final IBlockAccess blockAccess) {
        final TileEntity tileentity = blockAccess.getTileEntity(pos);
        if (!(tileentity instanceof TileEntityBeacon)) {
            return false;
        }
        final TileEntityBeacon tileentitybeacon = (TileEntityBeacon)tileentity;
        if (this.levels != null) {
            final int i = tileentitybeacon.getLevels();
            if (!this.levels.isInRange(i)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean matchesChest(final BlockPos pos, final IBlockAccess blockAccess) {
        final TileEntity tileentity = blockAccess.getTileEntity(pos);
        if (tileentity instanceof TileEntityChest) {
            final TileEntityChest tileentitychest = (TileEntityChest)tileentity;
            return this.matchesChest(tileentitychest, pos, blockAccess);
        }
        if (tileentity instanceof TileEntityEnderChest) {
            final TileEntityEnderChest tileentityenderchest = (TileEntityEnderChest)tileentity;
            return this.matchesEnderChest(tileentityenderchest, pos, blockAccess);
        }
        return false;
    }
    
    private boolean matchesChest(final TileEntityChest tec, final BlockPos pos, final IBlockAccess blockAccess) {
        final boolean flag = tec.adjacentChestXNeg != null || tec.adjacentChestXPos != null || tec.adjacentChestZNeg != null || tec.adjacentChestZPos != null;
        final boolean flag2 = tec.getChestType() == BlockChest.Type.TRAP;
        final boolean flag3 = CustomGuis.isChristmas;
        final boolean flag4 = false;
        return this.matchesChest(flag, flag2, flag3, flag4);
    }
    
    private boolean matchesEnderChest(final TileEntityEnderChest teec, final BlockPos pos, final IBlockAccess blockAccess) {
        return this.matchesChest(false, false, false, true);
    }
    
    private boolean matchesChest(final boolean isLarge, final boolean isTrapped, final boolean isChristmas, final boolean isEnder) {
        return (this.large == null || this.large == isLarge) && (this.trapped == null || this.trapped == isTrapped) && (this.christmas == null || this.christmas == isChristmas) && (this.ender == null || this.ender == isEnder);
    }
    
    private boolean matchesDispenser(final BlockPos pos, final IBlockAccess blockAccess) {
        final TileEntity tileentity = blockAccess.getTileEntity(pos);
        if (!(tileentity instanceof TileEntityDispenser)) {
            return false;
        }
        final TileEntityDispenser tileentitydispenser = (TileEntityDispenser)tileentity;
        if (this.variants != null) {
            final EnumVariant customguiproperties$enumvariant = this.getDispenserVariant(tileentitydispenser);
            if (!Config.equalsOne(customguiproperties$enumvariant, this.variants)) {
                return false;
            }
        }
        return true;
    }
    
    private EnumVariant getDispenserVariant(final TileEntityDispenser ted) {
        return (ted instanceof TileEntityDropper) ? EnumVariant.DROPPER : EnumVariant.DISPENSER;
    }
    
    private boolean matchesShulker(final BlockPos pos, final IBlockAccess blockAccess) {
        final TileEntity tileentity = blockAccess.getTileEntity(pos);
        if (!(tileentity instanceof TileEntityShulkerBox)) {
            return false;
        }
        final TileEntityShulkerBox tileentityshulkerbox = (TileEntityShulkerBox)tileentity;
        if (this.colors != null) {
            final EnumDyeColor enumdyecolor = tileentityshulkerbox.getColor();
            if (!Config.equalsOne(enumdyecolor, this.colors)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean matchesEntity(final EnumContainer ec, final Entity entity, final IBlockAccess blockAccess) {
        if (!this.matchesGeneral(ec, entity.getPosition(), blockAccess)) {
            return false;
        }
        if (this.nbtName != null) {
            final String s = entity.getName();
            if (!this.nbtName.matchesValue(s)) {
                return false;
            }
        }
        switch (ec) {
            case HORSE: {
                return this.matchesHorse(entity, blockAccess);
            }
            case VILLAGER: {
                return this.matchesVillager(entity, blockAccess);
            }
            default: {
                return true;
            }
        }
    }
    
    private boolean matchesVillager(final Entity entity, final IBlockAccess blockAccess) {
        if (!(entity instanceof EntityVillager)) {
            return false;
        }
        final EntityVillager entityvillager = (EntityVillager)entity;
        if (this.professions != null) {
            final int i = entityvillager.getProfession();
            final int j = Reflector.getFieldValueInt(entityvillager, Reflector.EntityVillager_careerId, -1);
            if (j < 0) {
                return false;
            }
            boolean flag = false;
            for (int k = 0; k < this.professions.length; ++k) {
                final VillagerProfession villagerprofession = this.professions[k];
                if (villagerprofession.matches(i, j)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return false;
            }
        }
        return true;
    }
    
    private boolean matchesHorse(final Entity entity, final IBlockAccess blockAccess) {
        if (!(entity instanceof AbstractHorse)) {
            return false;
        }
        final AbstractHorse abstracthorse = (AbstractHorse)entity;
        if (this.variants != null) {
            final EnumVariant customguiproperties$enumvariant = this.getHorseVariant(abstracthorse);
            if (!Config.equalsOne(customguiproperties$enumvariant, this.variants)) {
                return false;
            }
        }
        if (this.colors != null && abstracthorse instanceof EntityLlama) {
            final EntityLlama entityllama = (EntityLlama)abstracthorse;
            final EnumDyeColor enumdyecolor = entityllama.getColor();
            if (!Config.equalsOne(enumdyecolor, this.colors)) {
                return false;
            }
        }
        return true;
    }
    
    private EnumVariant getHorseVariant(final AbstractHorse entity) {
        if (entity instanceof EntityHorse) {
            return EnumVariant.HORSE;
        }
        if (entity instanceof EntityDonkey) {
            return EnumVariant.DONKEY;
        }
        if (entity instanceof EntityMule) {
            return EnumVariant.MULE;
        }
        return (entity instanceof EntityLlama) ? EnumVariant.LLAMA : null;
    }
    
    public EnumContainer getContainer() {
        return this.container;
    }
    
    public ResourceLocation getTextureLocation(final ResourceLocation loc) {
        final ResourceLocation resourcelocation = this.textureLocations.get(loc);
        return (resourcelocation == null) ? loc : resourcelocation;
    }
    
    @Override
    public String toString() {
        return "name: " + this.fileName + ", container: " + this.container + ", textures: " + this.textureLocations;
    }
    
    static {
        VARIANTS_HORSE = new EnumVariant[] { EnumVariant.HORSE, EnumVariant.DONKEY, EnumVariant.MULE, EnumVariant.LLAMA };
        VARIANTS_DISPENSER = new EnumVariant[] { EnumVariant.DISPENSER, EnumVariant.DROPPER };
        VARIANTS_INVALID = new EnumVariant[0];
        COLORS_INVALID = new EnumDyeColor[0];
        ANVIL_GUI_TEXTURE = new ResourceLocation("textures/gui/container/anvil.png");
        BEACON_GUI_TEXTURE = new ResourceLocation("textures/gui/container/beacon.png");
        BREWING_STAND_GUI_TEXTURE = new ResourceLocation("textures/gui/container/brewing_stand.png");
        CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
        CRAFTING_TABLE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/crafting_table.png");
        HORSE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/horse.png");
        DISPENSER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/dispenser.png");
        ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/enchanting_table.png");
        FURNACE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/furnace.png");
        HOPPER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/hopper.png");
        INVENTORY_GUI_TEXTURE = new ResourceLocation("textures/gui/container/inventory.png");
        SHULKER_BOX_GUI_TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");
        VILLAGER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/villager.png");
    }
    
    public enum EnumContainer
    {
        ANVIL, 
        BEACON, 
        BREWING_STAND, 
        CHEST, 
        CRAFTING, 
        DISPENSER, 
        ENCHANTMENT, 
        FURNACE, 
        HOPPER, 
        HORSE, 
        VILLAGER, 
        SHULKER_BOX, 
        CREATIVE, 
        INVENTORY;
    }
    
    private enum EnumVariant
    {
        HORSE, 
        DONKEY, 
        MULE, 
        LLAMA, 
        DISPENSER, 
        DROPPER;
    }
}
