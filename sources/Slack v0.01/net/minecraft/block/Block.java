package net.minecraft.block;

import cc.slack.Slack;
import cc.slack.events.impl.player.CollideEvent;
import cc.slack.features.modules.impl.render.ChestESP;
import cc.slack.features.modules.impl.render.XRay;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.RegistryNamespacedDefaultedByKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Block {
   private static final ResourceLocation AIR_ID = new ResourceLocation("air");
   public static final RegistryNamespacedDefaultedByKey<ResourceLocation, Block> blockRegistry;
   public static final ObjectIntIdentityMap<IBlockState> BLOCK_STATE_IDS;
   private CreativeTabs displayOnCreativeTab;
   public static final Block.SoundType soundTypeStone;
   public static final Block.SoundType soundTypeWood;
   public static final Block.SoundType soundTypeGravel;
   public static final Block.SoundType soundTypeGrass;
   public static final Block.SoundType soundTypePiston;
   public static final Block.SoundType soundTypeMetal;
   public static final Block.SoundType soundTypeGlass;
   public static final Block.SoundType soundTypeCloth;
   public static final Block.SoundType soundTypeSand;
   public static final Block.SoundType soundTypeSnow;
   public static final Block.SoundType soundTypeLadder;
   public static final Block.SoundType soundTypeAnvil;
   public static final Block.SoundType SLIME_SOUND;
   protected boolean fullBlock;
   protected int lightOpacity;
   protected boolean translucent;
   protected int lightValue;
   protected boolean useNeighborBrightness;
   protected float blockHardness;
   protected float blockResistance;
   protected boolean enableStats;
   protected boolean needsRandomTick;
   protected boolean isBlockContainer;
   protected double minX;
   protected double minY;
   protected double minZ;
   protected double maxX;
   protected double maxY;
   protected double maxZ;
   public Block.SoundType stepSound;
   public float blockParticleGravity;
   protected final Material blockMaterial;
   protected final MapColor field_181083_K;
   public float slipperiness;
   protected final BlockState blockState;
   private IBlockState defaultBlockState;
   private String unlocalizedName;

   public static int getIdFromBlock(Block blockIn) {
      return blockRegistry.getIDForObject(blockIn);
   }

   public static int getStateId(IBlockState state) {
      Block block = state.getBlock();
      return getIdFromBlock(block) + (block.getMetaFromState(state) << 12);
   }

   public static Block getBlockById(int id) {
      return (Block)blockRegistry.getObjectById(id);
   }

   public static IBlockState getStateById(int id) {
      int i = id & 4095;
      int j = id >> 12 & 15;
      return getBlockById(i).getStateFromMeta(j);
   }

   public static Block getBlockFromItem(Item itemIn) {
      return itemIn instanceof ItemBlock ? ((ItemBlock)itemIn).getBlock() : null;
   }

   public static Block getBlockFromName(String name) {
      ResourceLocation resourcelocation = new ResourceLocation(name);
      if (blockRegistry.containsKey(resourcelocation)) {
         return (Block)blockRegistry.getObject(resourcelocation);
      } else {
         try {
            return (Block)blockRegistry.getObjectById(Integer.parseInt(name));
         } catch (NumberFormatException var3) {
            return null;
         }
      }
   }

   public boolean isFullBlock() {
      return this.fullBlock;
   }

   public int getLightOpacity() {
      return this.lightOpacity;
   }

   public boolean isTranslucent() {
      return this.translucent;
   }

   public int getLightValue() {
      return this.lightValue;
   }

   public boolean getUseNeighborBrightness() {
      return this.useNeighborBrightness;
   }

   public Material getMaterial() {
      return this.blockMaterial;
   }

   public MapColor getMapColor(IBlockState state) {
      return this.field_181083_K;
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState();
   }

   public int getMetaFromState(IBlockState state) {
      if (state != null && !state.getPropertyNames().isEmpty()) {
         throw new IllegalArgumentException("Don't know how to convert " + state + " back into data...");
      } else {
         return 0;
      }
   }

   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
      return state;
   }

   public Block(Material p_i46399_1_, MapColor p_i46399_2_) {
      this.enableStats = true;
      this.stepSound = soundTypeStone;
      this.blockParticleGravity = 1.0F;
      this.slipperiness = 0.6F;
      this.blockMaterial = p_i46399_1_;
      this.field_181083_K = p_i46399_2_;
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      this.fullBlock = this.isOpaqueCube();
      this.lightOpacity = this.isOpaqueCube() ? 255 : 0;
      this.translucent = !p_i46399_1_.blocksLight();
      this.blockState = this.createBlockState();
      this.setDefaultState(this.blockState.getBaseState());
   }

   protected Block(Material materialIn) {
      this(materialIn, materialIn.getMaterialMapColor());
   }

   protected Block setStepSound(Block.SoundType sound) {
      this.stepSound = sound;
      return this;
   }

   protected Block setLightOpacity(int opacity) {
      this.lightOpacity = opacity;
      return this;
   }

   protected Block setLightLevel(float value) {
      this.lightValue = (int)(15.0F * value);
      return this;
   }

   protected Block setResistance(float resistance) {
      this.blockResistance = resistance * 3.0F;
      return this;
   }

   public boolean isBlockNormalCube() {
      return this.blockMaterial.blocksMovement() && this.isFullCube();
   }

   public boolean isNormalCube() {
      return this.blockMaterial.isOpaque() && this.isFullCube() && !this.canProvidePower();
   }

   public boolean isVisuallyOpaque() {
      return this.blockMaterial.blocksMovement() && this.isFullCube();
   }

   public boolean isFullCube() {
      return true;
   }

   public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
      return !this.blockMaterial.blocksMovement();
   }

   public int getRenderType() {
      return 3;
   }

   public boolean isReplaceable(World worldIn, BlockPos pos) {
      return false;
   }

   protected Block setHardness(float hardness) {
      this.blockHardness = hardness;
      if (this.blockResistance < hardness * 5.0F) {
         this.blockResistance = hardness * 5.0F;
      }

      return this;
   }

   protected Block setBlockUnbreakable() {
      this.setHardness(-1.0F);
      return this;
   }

   public float getBlockHardness(World worldIn, BlockPos pos) {
      return this.blockHardness;
   }

   protected Block setTickRandomly(boolean shouldTick) {
      this.needsRandomTick = shouldTick;
      return this;
   }

   public boolean getTickRandomly() {
      return this.needsRandomTick;
   }

   public boolean hasTileEntity() {
      return this.isBlockContainer;
   }

   protected final void setBlockBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
      this.minX = (double)minX;
      this.minY = (double)minY;
      this.minZ = (double)minZ;
      this.maxX = (double)maxX;
      this.maxY = (double)maxY;
      this.maxZ = (double)maxZ;
   }

   public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos) {
      Block block = worldIn.getBlockState(pos).getBlock();
      int i = worldIn.getCombinedLight(pos, block.getLightValue());
      if (i == 0 && block instanceof BlockSlab) {
         pos = pos.down();
         block = worldIn.getBlockState(pos).getBlock();
         return worldIn.getCombinedLight(pos, block.getLightValue());
      } else {
         return i;
      }
   }

   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
      if (((XRay)Slack.getInstance().getModuleManager().getInstance(XRay.class)).isToggle()) {
         return ((XRay)Slack.getInstance().getModuleManager().getInstance(XRay.class)).shouldRenderBlock(this);
      } else {
         if (((ChestESP)Slack.getInstance().getModuleManager().getInstance(ChestESP.class)).isToggle() && ((ChestESP)Slack.getInstance().getModuleManager().getInstance(ChestESP.class)).isChest(this)) {
            ((ChestESP)Slack.getInstance().getModuleManager().getInstance(ChestESP.class)).chestBoundingBoxes.add(pos);
         }

         return side == EnumFacing.DOWN && this.minY > 0.0D ? true : (side == EnumFacing.UP && this.maxY < 1.0D ? true : (side == EnumFacing.NORTH && this.minZ > 0.0D ? true : (side == EnumFacing.SOUTH && this.maxZ < 1.0D ? true : (side == EnumFacing.WEST && this.minX > 0.0D ? true : (side == EnumFacing.EAST && this.maxX < 1.0D ? true : !worldIn.getBlockState(pos).getBlock().isOpaqueCube())))));
      }
   }

   public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
      return worldIn.getBlockState(pos).getBlock().getMaterial().isSolid();
   }

   public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
      return new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)pos.getY() + this.maxY, (double)pos.getZ() + this.maxZ);
   }

   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
      CollideEvent collideEvent = new CollideEvent(state.getBlock(), this.getCollisionBoundingBox(worldIn, pos, state), (double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
      collideEvent.call();
      if (!collideEvent.isCanceled()) {
         AxisAlignedBB axisalignedbb = collideEvent.getBoundingBox();
         if (axisalignedbb != null && mask.intersectsWith(axisalignedbb)) {
            list.add(axisalignedbb);
         }

      }
   }

   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
      return new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)pos.getY() + this.maxY, (double)pos.getZ() + this.maxZ);
   }

   public boolean isOpaqueCube() {
      return true;
   }

   public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
      return this.isCollidable();
   }

   public boolean isCollidable() {
      return true;
   }

   public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
      this.updateTick(worldIn, pos, state, random);
   }

   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
   }

   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
   }

   public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
   }

   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
   }

   public int tickRate(World worldIn) {
      return 10;
   }

   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
   }

   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
   }

   public int quantityDropped(Random random) {
      return 1;
   }

   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
      return Item.getItemFromBlock(this);
   }

   public float getPlayerRelativeBlockHardness(EntityPlayer playerIn, World worldIn, BlockPos pos) {
      float f = this.getBlockHardness(worldIn, pos);
      return f < 0.0F ? 0.0F : (!playerIn.canHarvestBlock(this) ? playerIn.getToolDigEfficiency(this) / f / 100.0F : playerIn.getToolDigEfficiency(this) / f / 30.0F);
   }

   public final void dropBlockAsItem(World worldIn, BlockPos pos, IBlockState state, int forture) {
      this.dropBlockAsItemWithChance(worldIn, pos, state, 1.0F, forture);
   }

   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
      if (!worldIn.isRemote) {
         int i = this.quantityDroppedWithBonus(fortune, worldIn.rand);

         for(int j = 0; j < i; ++j) {
            if (worldIn.rand.nextFloat() <= chance) {
               Item item = this.getItemDropped(state, worldIn.rand, fortune);
               if (item != null) {
                  spawnAsEntity(worldIn, pos, new ItemStack(item, 1, this.damageDropped(state)));
               }
            }
         }
      }

   }

   public static void spawnAsEntity(World worldIn, BlockPos pos, ItemStack stack) {
      if (!worldIn.isRemote && worldIn.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
         float f = 0.5F;
         double d0 = (double)(worldIn.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
         double d1 = (double)(worldIn.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
         double d2 = (double)(worldIn.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
         EntityItem entityitem = new EntityItem(worldIn, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, stack);
         entityitem.setDefaultPickupDelay();
         worldIn.spawnEntityInWorld(entityitem);
      }

   }

   protected void dropXpOnBlockBreak(World worldIn, BlockPos pos, int amount) {
      if (!worldIn.isRemote) {
         while(amount > 0) {
            int i = EntityXPOrb.getXPSplit(amount);
            amount -= i;
            worldIn.spawnEntityInWorld(new EntityXPOrb(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, i));
         }
      }

   }

   public int damageDropped(IBlockState state) {
      return 0;
   }

   public float getExplosionResistance(Entity exploder) {
      return this.blockResistance / 5.0F;
   }

   public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
      this.setBlockBoundsBasedOnState(worldIn, pos);
      start = start.addVector((double)(-pos.getX()), (double)(-pos.getY()), (double)(-pos.getZ()));
      end = end.addVector((double)(-pos.getX()), (double)(-pos.getY()), (double)(-pos.getZ()));
      Vec3 vec3 = start.getIntermediateWithXValue(end, this.minX);
      Vec3 vec31 = start.getIntermediateWithXValue(end, this.maxX);
      Vec3 vec32 = start.getIntermediateWithYValue(end, this.minY);
      Vec3 vec33 = start.getIntermediateWithYValue(end, this.maxY);
      Vec3 vec34 = start.getIntermediateWithZValue(end, this.minZ);
      Vec3 vec35 = start.getIntermediateWithZValue(end, this.maxZ);
      if (!this.isVecInsideYZBounds(vec3)) {
         vec3 = null;
      }

      if (!this.isVecInsideYZBounds(vec31)) {
         vec31 = null;
      }

      if (!this.isVecInsideXZBounds(vec32)) {
         vec32 = null;
      }

      if (!this.isVecInsideXZBounds(vec33)) {
         vec33 = null;
      }

      if (!this.isVecInsideXYBounds(vec34)) {
         vec34 = null;
      }

      if (!this.isVecInsideXYBounds(vec35)) {
         vec35 = null;
      }

      Vec3 vec36 = null;
      if (vec3 != null && (vec36 == null || start.squareDistanceTo(vec3) < start.squareDistanceTo(vec36))) {
         vec36 = vec3;
      }

      if (vec31 != null && (vec36 == null || start.squareDistanceTo(vec31) < start.squareDistanceTo(vec36))) {
         vec36 = vec31;
      }

      if (vec32 != null && (vec36 == null || start.squareDistanceTo(vec32) < start.squareDistanceTo(vec36))) {
         vec36 = vec32;
      }

      if (vec33 != null && (vec36 == null || start.squareDistanceTo(vec33) < start.squareDistanceTo(vec36))) {
         vec36 = vec33;
      }

      if (vec34 != null && (vec36 == null || start.squareDistanceTo(vec34) < start.squareDistanceTo(vec36))) {
         vec36 = vec34;
      }

      if (vec35 != null && (vec36 == null || start.squareDistanceTo(vec35) < start.squareDistanceTo(vec36))) {
         vec36 = vec35;
      }

      if (vec36 == null) {
         return null;
      } else {
         EnumFacing enumfacing = null;
         if (vec36 == vec3) {
            enumfacing = EnumFacing.WEST;
         }

         if (vec36 == vec31) {
            enumfacing = EnumFacing.EAST;
         }

         if (vec36 == vec32) {
            enumfacing = EnumFacing.DOWN;
         }

         if (vec36 == vec33) {
            enumfacing = EnumFacing.UP;
         }

         if (vec36 == vec34) {
            enumfacing = EnumFacing.NORTH;
         }

         if (vec36 == vec35) {
            enumfacing = EnumFacing.SOUTH;
         }

         return new MovingObjectPosition(vec36.addVector((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), enumfacing, pos);
      }
   }

   private boolean isVecInsideYZBounds(Vec3 point) {
      return point == null ? false : point.yCoord >= this.minY && point.yCoord <= this.maxY && point.zCoord >= this.minZ && point.zCoord <= this.maxZ;
   }

   private boolean isVecInsideXZBounds(Vec3 point) {
      return point == null ? false : point.xCoord >= this.minX && point.xCoord <= this.maxX && point.zCoord >= this.minZ && point.zCoord <= this.maxZ;
   }

   private boolean isVecInsideXYBounds(Vec3 point) {
      return point == null ? false : point.xCoord >= this.minX && point.xCoord <= this.maxX && point.yCoord >= this.minY && point.yCoord <= this.maxY;
   }

   public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.SOLID;
   }

   public boolean canReplace(World worldIn, BlockPos pos, EnumFacing side, ItemStack stack) {
      return this.canPlaceBlockOnSide(worldIn, pos, side);
   }

   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
      return this.canPlaceBlockAt(worldIn, pos);
   }

   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
      return worldIn.getBlockState(pos).getBlock().blockMaterial.isReplaceable();
   }

   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
      return false;
   }

   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn) {
   }

   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      return this.getStateFromMeta(meta);
   }

   public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
   }

   public Vec3 modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3 motion) {
      return motion;
   }

   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
   }

   public final double getBlockBoundsMinX() {
      return this.minX;
   }

   public final double getBlockBoundsMaxX() {
      return this.maxX;
   }

   public final double getBlockBoundsMinY() {
      return this.minY;
   }

   public final double getBlockBoundsMaxY() {
      return this.maxY;
   }

   public final double getBlockBoundsMinZ() {
      return this.minZ;
   }

   public final double getBlockBoundsMaxZ() {
      return this.maxZ;
   }

   public int getBlockColor() {
      return 16777215;
   }

   public int getRenderColor(IBlockState state) {
      return 16777215;
   }

   public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
      return 16777215;
   }

   public final int colorMultiplier(IBlockAccess worldIn, BlockPos pos) {
      return this.colorMultiplier(worldIn, pos, 0);
   }

   public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
      return 0;
   }

   public boolean canProvidePower() {
      return false;
   }

   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
   }

   public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
      return 0;
   }

   public void setBlockBoundsForItemRender() {
   }

   public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
      player.triggerAchievement(StatList.mineBlockStatArray[getIdFromBlock(this)]);
      player.addExhaustion(0.025F);
      if (this.canSilkHarvest() && EnchantmentHelper.getSilkTouchModifier(player)) {
         ItemStack itemstack = this.createStackedBlock(state);
         if (itemstack != null) {
            spawnAsEntity(worldIn, pos, itemstack);
         }
      } else {
         int i = EnchantmentHelper.getFortuneModifier(player);
         this.dropBlockAsItem(worldIn, pos, state, i);
      }

   }

   protected boolean canSilkHarvest() {
      return this.isFullCube() && !this.isBlockContainer;
   }

   protected ItemStack createStackedBlock(IBlockState state) {
      int i = 0;
      Item item = Item.getItemFromBlock(this);
      if (item != null && item.getHasSubtypes()) {
         i = this.getMetaFromState(state);
      }

      return new ItemStack(item, 1, i);
   }

   public int quantityDroppedWithBonus(int fortune, Random random) {
      return this.quantityDropped(random);
   }

   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
   }

   public boolean func_181623_g() {
      return !this.blockMaterial.isSolid() && !this.blockMaterial.isLiquid();
   }

   public Block setUnlocalizedName(String name) {
      this.unlocalizedName = name;
      return this;
   }

   public String getLocalizedName() {
      return StatCollector.translateToLocal(this.getUnlocalizedName() + ".name");
   }

   public String getUnlocalizedName() {
      return "tile." + this.unlocalizedName;
   }

   public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam) {
      return false;
   }

   public boolean getEnableStats() {
      return this.enableStats;
   }

   protected Block disableStats() {
      this.enableStats = false;
      return this;
   }

   public int getMobilityFlag() {
      return this.blockMaterial.getMaterialMobility();
   }

   public float getAmbientOcclusionLightValue() {
      return this.isBlockNormalCube() ? 0.2F : 1.0F;
   }

   public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
      entityIn.fall(fallDistance, 1.0F);
   }

   public void onLanded(World worldIn, Entity entityIn) {
      entityIn.motionY = 0.0D;
   }

   public Item getItem(World worldIn, BlockPos pos) {
      return Item.getItemFromBlock(this);
   }

   public int getDamageValue(World worldIn, BlockPos pos) {
      return this.damageDropped(worldIn.getBlockState(pos));
   }

   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
      list.add(new ItemStack(itemIn, 1, 0));
   }

   public CreativeTabs getCreativeTabToDisplayOn() {
      return this.displayOnCreativeTab;
   }

   public Block setCreativeTab(CreativeTabs tab) {
      this.displayOnCreativeTab = tab;
      return this;
   }

   public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
   }

   public void fillWithRain(World worldIn, BlockPos pos) {
   }

   public boolean isFlowerPot() {
      return false;
   }

   public boolean requiresUpdates() {
      return true;
   }

   public boolean canDropFromExplosion(Explosion explosionIn) {
      return true;
   }

   public boolean isAssociatedBlock(Block other) {
      return this == other;
   }

   public static boolean isEqualTo(Block blockIn, Block other) {
      return blockIn != null && other != null ? (blockIn == other ? true : blockIn.isAssociatedBlock(other)) : false;
   }

   public boolean hasComparatorInputOverride() {
      return false;
   }

   public int getComparatorInputOverride(World worldIn, BlockPos pos) {
      return 0;
   }

   public IBlockState getStateForEntityRender(IBlockState state) {
      return state;
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[0]);
   }

   public BlockState getBlockState() {
      return this.blockState;
   }

   protected final void setDefaultState(IBlockState state) {
      this.defaultBlockState = state;
   }

   public final IBlockState getDefaultState() {
      return this.defaultBlockState;
   }

   public Block.EnumOffsetType getOffsetType() {
      return Block.EnumOffsetType.NONE;
   }

   public String toString() {
      return "Block{" + blockRegistry.getNameForObject(this) + "}";
   }

   public static void registerBlocks() {
      registerBlock(0, (ResourceLocation)AIR_ID, (new BlockAir()).setUnlocalizedName("air"));
      registerBlock(1, (String)"stone", (new BlockStone()).setHardness(1.5F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stone"));
      registerBlock(2, (String)"grass", (new BlockGrass()).setHardness(0.6F).setStepSound(soundTypeGrass).setUnlocalizedName("grass"));
      registerBlock(3, (String)"dirt", (new BlockDirt()).setHardness(0.5F).setStepSound(soundTypeGravel).setUnlocalizedName("dirt"));
      Block block = (new Block(Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stonebrick").setCreativeTab(CreativeTabs.tabBlock);
      registerBlock(4, (String)"cobblestone", block);
      Block block1 = (new BlockPlanks()).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("wood");
      registerBlock(5, (String)"planks", block1);
      registerBlock(6, (String)"sapling", (new BlockSapling()).setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("sapling"));
      registerBlock(7, (String)"bedrock", (new Block(Material.rock)).setBlockUnbreakable().setResistance(6000000.0F).setStepSound(soundTypePiston).setUnlocalizedName("bedrock").disableStats().setCreativeTab(CreativeTabs.tabBlock));
      registerBlock(8, (String)"flowing_water", (new BlockDynamicLiquid(Material.water)).setHardness(100.0F).setLightOpacity(3).setUnlocalizedName("water").disableStats());
      registerBlock(9, (String)"water", (new BlockStaticLiquid(Material.water)).setHardness(100.0F).setLightOpacity(3).setUnlocalizedName("water").disableStats());
      registerBlock(10, (String)"flowing_lava", (new BlockDynamicLiquid(Material.lava)).setHardness(100.0F).setLightLevel(1.0F).setUnlocalizedName("lava").disableStats());
      registerBlock(11, (String)"lava", (new BlockStaticLiquid(Material.lava)).setHardness(100.0F).setLightLevel(1.0F).setUnlocalizedName("lava").disableStats());
      registerBlock(12, (String)"sand", (new BlockSand()).setHardness(0.5F).setStepSound(soundTypeSand).setUnlocalizedName("sand"));
      registerBlock(13, (String)"gravel", (new BlockGravel()).setHardness(0.6F).setStepSound(soundTypeGravel).setUnlocalizedName("gravel"));
      registerBlock(14, (String)"gold_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreGold"));
      registerBlock(15, (String)"iron_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreIron"));
      registerBlock(16, (String)"coal_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreCoal"));
      registerBlock(17, (String)"log", (new BlockOldLog()).setUnlocalizedName("log"));
      registerBlock(18, (String)"leaves", (new BlockOldLeaf()).setUnlocalizedName("leaves"));
      registerBlock(19, (String)"sponge", (new BlockSponge()).setHardness(0.6F).setStepSound(soundTypeGrass).setUnlocalizedName("sponge"));
      registerBlock(20, (String)"glass", (new BlockGlass(Material.glass, false)).setHardness(0.3F).setStepSound(soundTypeGlass).setUnlocalizedName("glass"));
      registerBlock(21, (String)"lapis_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreLapis"));
      registerBlock(22, (String)"lapis_block", (new Block(Material.iron, MapColor.lapisColor)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("blockLapis").setCreativeTab(CreativeTabs.tabBlock));
      registerBlock(23, (String)"dispenser", (new BlockDispenser()).setHardness(3.5F).setStepSound(soundTypePiston).setUnlocalizedName("dispenser"));
      Block block2 = (new BlockSandStone()).setStepSound(soundTypePiston).setHardness(0.8F).setUnlocalizedName("sandStone");
      registerBlock(24, (String)"sandstone", block2);
      registerBlock(25, (String)"noteblock", (new BlockNote()).setHardness(0.8F).setUnlocalizedName("musicBlock"));
      registerBlock(26, (String)"bed", (new BlockBed()).setStepSound(soundTypeWood).setHardness(0.2F).setUnlocalizedName("bed").disableStats());
      registerBlock(27, (String)"golden_rail", (new BlockRailPowered()).setHardness(0.7F).setStepSound(soundTypeMetal).setUnlocalizedName("goldenRail"));
      registerBlock(28, (String)"detector_rail", (new BlockRailDetector()).setHardness(0.7F).setStepSound(soundTypeMetal).setUnlocalizedName("detectorRail"));
      registerBlock(29, (String)"sticky_piston", (new BlockPistonBase(true)).setUnlocalizedName("pistonStickyBase"));
      registerBlock(30, (String)"web", (new BlockWeb()).setLightOpacity(1).setHardness(4.0F).setUnlocalizedName("web"));
      registerBlock(31, (String)"tallgrass", (new BlockTallGrass()).setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("tallgrass"));
      registerBlock(32, (String)"deadbush", (new BlockDeadBush()).setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("deadbush"));
      registerBlock(33, (String)"piston", (new BlockPistonBase(false)).setUnlocalizedName("pistonBase"));
      registerBlock(34, (String)"piston_head", (new BlockPistonExtension()).setUnlocalizedName("pistonBase"));
      registerBlock(35, (String)"wool", (new BlockColored(Material.cloth)).setHardness(0.8F).setStepSound(soundTypeCloth).setUnlocalizedName("cloth"));
      registerBlock(36, (String)"piston_extension", new BlockPistonMoving());
      registerBlock(37, (String)"yellow_flower", (new BlockYellowFlower()).setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("flower1"));
      registerBlock(38, (String)"red_flower", (new BlockRedFlower()).setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("flower2"));
      Block block3 = (new BlockMushroom()).setHardness(0.0F).setStepSound(soundTypeGrass).setLightLevel(0.125F).setUnlocalizedName("mushroom");
      registerBlock(39, (String)"brown_mushroom", block3);
      Block block4 = (new BlockMushroom()).setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("mushroom");
      registerBlock(40, (String)"red_mushroom", block4);
      registerBlock(41, (String)"gold_block", (new Block(Material.iron, MapColor.goldColor)).setHardness(3.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setUnlocalizedName("blockGold").setCreativeTab(CreativeTabs.tabBlock));
      registerBlock(42, (String)"iron_block", (new Block(Material.iron, MapColor.ironColor)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setUnlocalizedName("blockIron").setCreativeTab(CreativeTabs.tabBlock));
      registerBlock(43, (String)"double_stone_slab", (new BlockDoubleStoneSlab()).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stoneSlab"));
      registerBlock(44, (String)"stone_slab", (new BlockHalfStoneSlab()).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stoneSlab"));
      Block block5 = (new Block(Material.rock, MapColor.redColor)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("brick").setCreativeTab(CreativeTabs.tabBlock);
      registerBlock(45, (String)"brick_block", block5);
      registerBlock(46, (String)"tnt", (new BlockTNT()).setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("tnt"));
      registerBlock(47, (String)"bookshelf", (new BlockBookshelf()).setHardness(1.5F).setStepSound(soundTypeWood).setUnlocalizedName("bookshelf"));
      registerBlock(48, (String)"mossy_cobblestone", (new Block(Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stoneMoss").setCreativeTab(CreativeTabs.tabBlock));
      registerBlock(49, (String)"obsidian", (new BlockObsidian()).setHardness(50.0F).setResistance(2000.0F).setStepSound(soundTypePiston).setUnlocalizedName("obsidian"));
      registerBlock(50, (String)"torch", (new BlockTorch()).setHardness(0.0F).setLightLevel(0.9375F).setStepSound(soundTypeWood).setUnlocalizedName("torch"));
      registerBlock(51, (String)"fire", (new BlockFire()).setHardness(0.0F).setLightLevel(1.0F).setStepSound(soundTypeCloth).setUnlocalizedName("fire").disableStats());
      registerBlock(52, (String)"mob_spawner", (new BlockMobSpawner()).setHardness(5.0F).setStepSound(soundTypeMetal).setUnlocalizedName("mobSpawner").disableStats());
      registerBlock(53, (String)"oak_stairs", (new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK))).setUnlocalizedName("stairsWood"));
      registerBlock(54, (String)"chest", (new BlockChest(0)).setHardness(2.5F).setStepSound(soundTypeWood).setUnlocalizedName("chest"));
      registerBlock(55, (String)"redstone_wire", (new BlockRedstoneWire()).setHardness(0.0F).setStepSound(soundTypeStone).setUnlocalizedName("redstoneDust").disableStats());
      registerBlock(56, (String)"diamond_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreDiamond"));
      registerBlock(57, (String)"diamond_block", (new Block(Material.iron, MapColor.diamondColor)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setUnlocalizedName("blockDiamond").setCreativeTab(CreativeTabs.tabBlock));
      registerBlock(58, (String)"crafting_table", (new BlockWorkbench()).setHardness(2.5F).setStepSound(soundTypeWood).setUnlocalizedName("workbench"));
      registerBlock(59, (String)"wheat", (new BlockCrops()).setUnlocalizedName("crops"));
      Block block6 = (new BlockFarmland()).setHardness(0.6F).setStepSound(soundTypeGravel).setUnlocalizedName("farmland");
      registerBlock(60, (String)"farmland", block6);
      registerBlock(61, (String)"furnace", (new BlockFurnace(false)).setHardness(3.5F).setStepSound(soundTypePiston).setUnlocalizedName("furnace").setCreativeTab(CreativeTabs.tabDecorations));
      registerBlock(62, (String)"lit_furnace", (new BlockFurnace(true)).setHardness(3.5F).setStepSound(soundTypePiston).setLightLevel(0.875F).setUnlocalizedName("furnace"));
      registerBlock(63, (String)"standing_sign", (new BlockStandingSign()).setHardness(1.0F).setStepSound(soundTypeWood).setUnlocalizedName("sign").disableStats());
      registerBlock(64, (String)"wooden_door", (new BlockDoor(Material.wood)).setHardness(3.0F).setStepSound(soundTypeWood).setUnlocalizedName("doorOak").disableStats());
      registerBlock(65, (String)"ladder", (new BlockLadder()).setHardness(0.4F).setStepSound(soundTypeLadder).setUnlocalizedName("ladder"));
      registerBlock(66, (String)"rail", (new BlockRail()).setHardness(0.7F).setStepSound(soundTypeMetal).setUnlocalizedName("rail"));
      registerBlock(67, (String)"stone_stairs", (new BlockStairs(block.getDefaultState())).setUnlocalizedName("stairsStone"));
      registerBlock(68, (String)"wall_sign", (new BlockWallSign()).setHardness(1.0F).setStepSound(soundTypeWood).setUnlocalizedName("sign").disableStats());
      registerBlock(69, (String)"lever", (new BlockLever()).setHardness(0.5F).setStepSound(soundTypeWood).setUnlocalizedName("lever"));
      registerBlock(70, (String)"stone_pressure_plate", (new BlockPressurePlate(Material.rock, BlockPressurePlate.Sensitivity.MOBS)).setHardness(0.5F).setStepSound(soundTypePiston).setUnlocalizedName("pressurePlateStone"));
      registerBlock(71, (String)"iron_door", (new BlockDoor(Material.iron)).setHardness(5.0F).setStepSound(soundTypeMetal).setUnlocalizedName("doorIron").disableStats());
      registerBlock(72, (String)"wooden_pressure_plate", (new BlockPressurePlate(Material.wood, BlockPressurePlate.Sensitivity.EVERYTHING)).setHardness(0.5F).setStepSound(soundTypeWood).setUnlocalizedName("pressurePlateWood"));
      registerBlock(73, (String)"redstone_ore", (new BlockRedstoneOre(false)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreRedstone").setCreativeTab(CreativeTabs.tabBlock));
      registerBlock(74, (String)"lit_redstone_ore", (new BlockRedstoneOre(true)).setLightLevel(0.625F).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreRedstone"));
      registerBlock(75, (String)"unlit_redstone_torch", (new BlockRedstoneTorch(false)).setHardness(0.0F).setStepSound(soundTypeWood).setUnlocalizedName("notGate"));
      registerBlock(76, (String)"redstone_torch", (new BlockRedstoneTorch(true)).setHardness(0.0F).setLightLevel(0.5F).setStepSound(soundTypeWood).setUnlocalizedName("notGate").setCreativeTab(CreativeTabs.tabRedstone));
      registerBlock(77, (String)"stone_button", (new BlockButtonStone()).setHardness(0.5F).setStepSound(soundTypePiston).setUnlocalizedName("button"));
      registerBlock(78, (String)"snow_layer", (new BlockSnow()).setHardness(0.1F).setStepSound(soundTypeSnow).setUnlocalizedName("snow").setLightOpacity(0));
      registerBlock(79, (String)"ice", (new BlockIce()).setHardness(0.5F).setLightOpacity(3).setStepSound(soundTypeGlass).setUnlocalizedName("ice"));
      registerBlock(80, (String)"snow", (new BlockSnowBlock()).setHardness(0.2F).setStepSound(soundTypeSnow).setUnlocalizedName("snow"));
      registerBlock(81, (String)"cactus", (new BlockCactus()).setHardness(0.4F).setStepSound(soundTypeCloth).setUnlocalizedName("cactus"));
      registerBlock(82, (String)"clay", (new BlockClay()).setHardness(0.6F).setStepSound(soundTypeGravel).setUnlocalizedName("clay"));
      registerBlock(83, (String)"reeds", (new BlockReed()).setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("reeds").disableStats());
      registerBlock(84, (String)"jukebox", (new BlockJukebox()).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("jukebox"));
      registerBlock(85, (String)"fence", (new BlockFence(Material.wood, BlockPlanks.EnumType.OAK.func_181070_c())).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("fence"));
      Block block7 = (new BlockPumpkin()).setHardness(1.0F).setStepSound(soundTypeWood).setUnlocalizedName("pumpkin");
      registerBlock(86, (String)"pumpkin", block7);
      registerBlock(87, (String)"netherrack", (new BlockNetherrack()).setHardness(0.4F).setStepSound(soundTypePiston).setUnlocalizedName("hellrock"));
      registerBlock(88, (String)"soul_sand", (new BlockSoulSand()).setHardness(0.5F).setStepSound(soundTypeSand).setUnlocalizedName("hellsand"));
      registerBlock(89, (String)"glowstone", (new BlockGlowstone(Material.glass)).setHardness(0.3F).setStepSound(soundTypeGlass).setLightLevel(1.0F).setUnlocalizedName("lightgem"));
      registerBlock(90, (String)"portal", (new BlockPortal()).setHardness(-1.0F).setStepSound(soundTypeGlass).setLightLevel(0.75F).setUnlocalizedName("portal"));
      registerBlock(91, (String)"lit_pumpkin", (new BlockPumpkin()).setHardness(1.0F).setStepSound(soundTypeWood).setLightLevel(1.0F).setUnlocalizedName("litpumpkin"));
      registerBlock(92, (String)"cake", (new BlockCake()).setHardness(0.5F).setStepSound(soundTypeCloth).setUnlocalizedName("cake").disableStats());
      registerBlock(93, (String)"unpowered_repeater", (new BlockRedstoneRepeater(false)).setHardness(0.0F).setStepSound(soundTypeWood).setUnlocalizedName("diode").disableStats());
      registerBlock(94, (String)"powered_repeater", (new BlockRedstoneRepeater(true)).setHardness(0.0F).setStepSound(soundTypeWood).setUnlocalizedName("diode").disableStats());
      registerBlock(95, (String)"stained_glass", (new BlockStainedGlass(Material.glass)).setHardness(0.3F).setStepSound(soundTypeGlass).setUnlocalizedName("stainedGlass"));
      registerBlock(96, (String)"trapdoor", (new BlockTrapDoor(Material.wood)).setHardness(3.0F).setStepSound(soundTypeWood).setUnlocalizedName("trapdoor").disableStats());
      registerBlock(97, (String)"monster_egg", (new BlockSilverfish()).setHardness(0.75F).setUnlocalizedName("monsterStoneEgg"));
      Block block8 = (new BlockStoneBrick()).setHardness(1.5F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stonebricksmooth");
      registerBlock(98, (String)"stonebrick", block8);
      registerBlock(99, (String)"brown_mushroom_block", (new BlockHugeMushroom(Material.wood, MapColor.dirtColor, block3)).setHardness(0.2F).setStepSound(soundTypeWood).setUnlocalizedName("mushroom"));
      registerBlock(100, (String)"red_mushroom_block", (new BlockHugeMushroom(Material.wood, MapColor.redColor, block4)).setHardness(0.2F).setStepSound(soundTypeWood).setUnlocalizedName("mushroom"));
      registerBlock(101, (String)"iron_bars", (new BlockPane(Material.iron, true)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setUnlocalizedName("fenceIron"));
      registerBlock(102, (String)"glass_pane", (new BlockPane(Material.glass, false)).setHardness(0.3F).setStepSound(soundTypeGlass).setUnlocalizedName("thinGlass"));
      Block block9 = (new BlockMelon()).setHardness(1.0F).setStepSound(soundTypeWood).setUnlocalizedName("melon");
      registerBlock(103, (String)"melon_block", block9);
      registerBlock(104, (String)"pumpkin_stem", (new BlockStem(block7)).setHardness(0.0F).setStepSound(soundTypeWood).setUnlocalizedName("pumpkinStem"));
      registerBlock(105, (String)"melon_stem", (new BlockStem(block9)).setHardness(0.0F).setStepSound(soundTypeWood).setUnlocalizedName("pumpkinStem"));
      registerBlock(106, (String)"vine", (new BlockVine()).setHardness(0.2F).setStepSound(soundTypeGrass).setUnlocalizedName("vine"));
      registerBlock(107, (String)"fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.OAK)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("fenceGate"));
      registerBlock(108, (String)"brick_stairs", (new BlockStairs(block5.getDefaultState())).setUnlocalizedName("stairsBrick"));
      registerBlock(109, (String)"stone_brick_stairs", (new BlockStairs(block8.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT))).setUnlocalizedName("stairsStoneBrickSmooth"));
      registerBlock(110, (String)"mycelium", (new BlockMycelium()).setHardness(0.6F).setStepSound(soundTypeGrass).setUnlocalizedName("mycel"));
      registerBlock(111, (String)"waterlily", (new BlockLilyPad()).setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("waterlily"));
      Block block10 = (new BlockNetherBrick()).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("netherBrick").setCreativeTab(CreativeTabs.tabBlock);
      registerBlock(112, (String)"nether_brick", block10);
      registerBlock(113, (String)"nether_brick_fence", (new BlockFence(Material.rock, MapColor.netherrackColor)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("netherFence"));
      registerBlock(114, (String)"nether_brick_stairs", (new BlockStairs(block10.getDefaultState())).setUnlocalizedName("stairsNetherBrick"));
      registerBlock(115, (String)"nether_wart", (new BlockNetherWart()).setUnlocalizedName("netherStalk"));
      registerBlock(116, (String)"enchanting_table", (new BlockEnchantmentTable()).setHardness(5.0F).setResistance(2000.0F).setUnlocalizedName("enchantmentTable"));
      registerBlock(117, (String)"brewing_stand", (new BlockBrewingStand()).setHardness(0.5F).setLightLevel(0.125F).setUnlocalizedName("brewingStand"));
      registerBlock(118, (String)"cauldron", (new BlockCauldron()).setHardness(2.0F).setUnlocalizedName("cauldron"));
      registerBlock(119, (String)"end_portal", (new BlockEndPortal(Material.portal)).setHardness(-1.0F).setResistance(6000000.0F));
      registerBlock(120, (String)"end_portal_frame", (new BlockEndPortalFrame()).setStepSound(soundTypeGlass).setLightLevel(0.125F).setHardness(-1.0F).setUnlocalizedName("endPortalFrame").setResistance(6000000.0F).setCreativeTab(CreativeTabs.tabDecorations));
      registerBlock(121, (String)"end_stone", (new Block(Material.rock, MapColor.sandColor)).setHardness(3.0F).setResistance(15.0F).setStepSound(soundTypePiston).setUnlocalizedName("whiteStone").setCreativeTab(CreativeTabs.tabBlock));
      registerBlock(122, (String)"dragon_egg", (new BlockDragonEgg()).setHardness(3.0F).setResistance(15.0F).setStepSound(soundTypePiston).setLightLevel(0.125F).setUnlocalizedName("dragonEgg"));
      registerBlock(123, (String)"redstone_lamp", (new BlockRedstoneLight(false)).setHardness(0.3F).setStepSound(soundTypeGlass).setUnlocalizedName("redstoneLight").setCreativeTab(CreativeTabs.tabRedstone));
      registerBlock(124, (String)"lit_redstone_lamp", (new BlockRedstoneLight(true)).setHardness(0.3F).setStepSound(soundTypeGlass).setUnlocalizedName("redstoneLight"));
      registerBlock(125, (String)"double_wooden_slab", (new BlockDoubleWoodSlab()).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("woodSlab"));
      registerBlock(126, (String)"wooden_slab", (new BlockHalfWoodSlab()).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("woodSlab"));
      registerBlock(127, (String)"cocoa", (new BlockCocoa()).setHardness(0.2F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("cocoa"));
      registerBlock(128, (String)"sandstone_stairs", (new BlockStairs(block2.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH))).setUnlocalizedName("stairsSandStone"));
      registerBlock(129, (String)"emerald_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreEmerald"));
      registerBlock(130, (String)"ender_chest", (new BlockEnderChest()).setHardness(22.5F).setResistance(1000.0F).setStepSound(soundTypePiston).setUnlocalizedName("enderChest").setLightLevel(0.5F));
      registerBlock(131, (String)"tripwire_hook", (new BlockTripWireHook()).setUnlocalizedName("tripWireSource"));
      registerBlock(132, (String)"tripwire", (new BlockTripWire()).setUnlocalizedName("tripWire"));
      registerBlock(133, (String)"emerald_block", (new Block(Material.iron, MapColor.emeraldColor)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setUnlocalizedName("blockEmerald").setCreativeTab(CreativeTabs.tabBlock));
      registerBlock(134, (String)"spruce_stairs", (new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE))).setUnlocalizedName("stairsWoodSpruce"));
      registerBlock(135, (String)"birch_stairs", (new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH))).setUnlocalizedName("stairsWoodBirch"));
      registerBlock(136, (String)"jungle_stairs", (new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE))).setUnlocalizedName("stairsWoodJungle"));
      registerBlock(137, (String)"command_block", (new BlockCommandBlock()).setBlockUnbreakable().setResistance(6000000.0F).setUnlocalizedName("commandBlock"));
      registerBlock(138, (String)"beacon", (new BlockBeacon()).setUnlocalizedName("beacon").setLightLevel(1.0F));
      registerBlock(139, (String)"cobblestone_wall", (new BlockWall(block)).setUnlocalizedName("cobbleWall"));
      registerBlock(140, (String)"flower_pot", (new BlockFlowerPot()).setHardness(0.0F).setStepSound(soundTypeStone).setUnlocalizedName("flowerPot"));
      registerBlock(141, (String)"carrots", (new BlockCarrot()).setUnlocalizedName("carrots"));
      registerBlock(142, (String)"potatoes", (new BlockPotato()).setUnlocalizedName("potatoes"));
      registerBlock(143, (String)"wooden_button", (new BlockButtonWood()).setHardness(0.5F).setStepSound(soundTypeWood).setUnlocalizedName("button"));
      registerBlock(144, (String)"skull", (new BlockSkull()).setHardness(1.0F).setStepSound(soundTypePiston).setUnlocalizedName("skull"));
      registerBlock(145, (String)"anvil", (new BlockAnvil()).setHardness(5.0F).setStepSound(soundTypeAnvil).setResistance(2000.0F).setUnlocalizedName("anvil"));
      registerBlock(146, (String)"trapped_chest", (new BlockChest(1)).setHardness(2.5F).setStepSound(soundTypeWood).setUnlocalizedName("chestTrap"));
      registerBlock(147, (String)"light_weighted_pressure_plate", (new BlockPressurePlateWeighted(Material.iron, 15, MapColor.goldColor)).setHardness(0.5F).setStepSound(soundTypeWood).setUnlocalizedName("weightedPlate_light"));
      registerBlock(148, (String)"heavy_weighted_pressure_plate", (new BlockPressurePlateWeighted(Material.iron, 150)).setHardness(0.5F).setStepSound(soundTypeWood).setUnlocalizedName("weightedPlate_heavy"));
      registerBlock(149, (String)"unpowered_comparator", (new BlockRedstoneComparator(false)).setHardness(0.0F).setStepSound(soundTypeWood).setUnlocalizedName("comparator").disableStats());
      registerBlock(150, (String)"powered_comparator", (new BlockRedstoneComparator(true)).setHardness(0.0F).setLightLevel(0.625F).setStepSound(soundTypeWood).setUnlocalizedName("comparator").disableStats());
      registerBlock(151, (String)"daylight_detector", new BlockDaylightDetector(false));
      registerBlock(152, (String)"redstone_block", (new BlockCompressedPowered(Material.iron, MapColor.tntColor)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setUnlocalizedName("blockRedstone").setCreativeTab(CreativeTabs.tabRedstone));
      registerBlock(153, (String)"quartz_ore", (new BlockOre(MapColor.netherrackColor)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("netherquartz"));
      registerBlock(154, (String)"hopper", (new BlockHopper()).setHardness(3.0F).setResistance(8.0F).setStepSound(soundTypeMetal).setUnlocalizedName("hopper"));
      Block block11 = (new BlockQuartz()).setStepSound(soundTypePiston).setHardness(0.8F).setUnlocalizedName("quartzBlock");
      registerBlock(155, (String)"quartz_block", block11);
      registerBlock(156, (String)"quartz_stairs", (new BlockStairs(block11.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.DEFAULT))).setUnlocalizedName("stairsQuartz"));
      registerBlock(157, (String)"activator_rail", (new BlockRailPowered()).setHardness(0.7F).setStepSound(soundTypeMetal).setUnlocalizedName("activatorRail"));
      registerBlock(158, (String)"dropper", (new BlockDropper()).setHardness(3.5F).setStepSound(soundTypePiston).setUnlocalizedName("dropper"));
      registerBlock(159, (String)"stained_hardened_clay", (new BlockColored(Material.rock)).setHardness(1.25F).setResistance(7.0F).setStepSound(soundTypePiston).setUnlocalizedName("clayHardenedStained"));
      registerBlock(160, (String)"stained_glass_pane", (new BlockStainedGlassPane()).setHardness(0.3F).setStepSound(soundTypeGlass).setUnlocalizedName("thinStainedGlass"));
      registerBlock(161, (String)"leaves2", (new BlockNewLeaf()).setUnlocalizedName("leaves"));
      registerBlock(162, (String)"log2", (new BlockNewLog()).setUnlocalizedName("log"));
      registerBlock(163, (String)"acacia_stairs", (new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA))).setUnlocalizedName("stairsWoodAcacia"));
      registerBlock(164, (String)"dark_oak_stairs", (new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK))).setUnlocalizedName("stairsWoodDarkOak"));
      registerBlock(165, (String)"slime", (new BlockSlime()).setUnlocalizedName("slime").setStepSound(SLIME_SOUND));
      registerBlock(166, (String)"barrier", (new BlockBarrier()).setUnlocalizedName("barrier"));
      registerBlock(167, (String)"iron_trapdoor", (new BlockTrapDoor(Material.iron)).setHardness(5.0F).setStepSound(soundTypeMetal).setUnlocalizedName("ironTrapdoor").disableStats());
      registerBlock(168, (String)"prismarine", (new BlockPrismarine()).setHardness(1.5F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("prismarine"));
      registerBlock(169, (String)"sea_lantern", (new BlockSeaLantern(Material.glass)).setHardness(0.3F).setStepSound(soundTypeGlass).setLightLevel(1.0F).setUnlocalizedName("seaLantern"));
      registerBlock(170, (String)"hay_block", (new BlockHay()).setHardness(0.5F).setStepSound(soundTypeGrass).setUnlocalizedName("hayBlock").setCreativeTab(CreativeTabs.tabBlock));
      registerBlock(171, (String)"carpet", (new BlockCarpet()).setHardness(0.1F).setStepSound(soundTypeCloth).setUnlocalizedName("woolCarpet").setLightOpacity(0));
      registerBlock(172, (String)"hardened_clay", (new BlockHardenedClay()).setHardness(1.25F).setResistance(7.0F).setStepSound(soundTypePiston).setUnlocalizedName("clayHardened"));
      registerBlock(173, (String)"coal_block", (new Block(Material.rock, MapColor.blackColor)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("blockCoal").setCreativeTab(CreativeTabs.tabBlock));
      registerBlock(174, (String)"packed_ice", (new BlockPackedIce()).setHardness(0.5F).setStepSound(soundTypeGlass).setUnlocalizedName("icePacked"));
      registerBlock(175, (String)"double_plant", new BlockDoublePlant());
      registerBlock(176, (String)"standing_banner", (new BlockBanner.BlockBannerStanding()).setHardness(1.0F).setStepSound(soundTypeWood).setUnlocalizedName("banner").disableStats());
      registerBlock(177, (String)"wall_banner", (new BlockBanner.BlockBannerHanging()).setHardness(1.0F).setStepSound(soundTypeWood).setUnlocalizedName("banner").disableStats());
      registerBlock(178, (String)"daylight_detector_inverted", new BlockDaylightDetector(true));
      Block block12 = (new BlockRedSandstone()).setStepSound(soundTypePiston).setHardness(0.8F).setUnlocalizedName("redSandStone");
      registerBlock(179, (String)"red_sandstone", block12);
      registerBlock(180, (String)"red_sandstone_stairs", (new BlockStairs(block12.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH))).setUnlocalizedName("stairsRedSandStone"));
      registerBlock(181, (String)"double_stone_slab2", (new BlockDoubleStoneSlabNew()).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stoneSlab2"));
      registerBlock(182, (String)"stone_slab2", (new BlockHalfStoneSlabNew()).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stoneSlab2"));
      registerBlock(183, (String)"spruce_fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.SPRUCE)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("spruceFenceGate"));
      registerBlock(184, (String)"birch_fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.BIRCH)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("birchFenceGate"));
      registerBlock(185, (String)"jungle_fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.JUNGLE)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("jungleFenceGate"));
      registerBlock(186, (String)"dark_oak_fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.DARK_OAK)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("darkOakFenceGate"));
      registerBlock(187, (String)"acacia_fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.ACACIA)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("acaciaFenceGate"));
      registerBlock(188, (String)"spruce_fence", (new BlockFence(Material.wood, BlockPlanks.EnumType.SPRUCE.func_181070_c())).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("spruceFence"));
      registerBlock(189, (String)"birch_fence", (new BlockFence(Material.wood, BlockPlanks.EnumType.BIRCH.func_181070_c())).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("birchFence"));
      registerBlock(190, (String)"jungle_fence", (new BlockFence(Material.wood, BlockPlanks.EnumType.JUNGLE.func_181070_c())).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("jungleFence"));
      registerBlock(191, (String)"dark_oak_fence", (new BlockFence(Material.wood, BlockPlanks.EnumType.DARK_OAK.func_181070_c())).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("darkOakFence"));
      registerBlock(192, (String)"acacia_fence", (new BlockFence(Material.wood, BlockPlanks.EnumType.ACACIA.func_181070_c())).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("acaciaFence"));
      registerBlock(193, (String)"spruce_door", (new BlockDoor(Material.wood)).setHardness(3.0F).setStepSound(soundTypeWood).setUnlocalizedName("doorSpruce").disableStats());
      registerBlock(194, (String)"birch_door", (new BlockDoor(Material.wood)).setHardness(3.0F).setStepSound(soundTypeWood).setUnlocalizedName("doorBirch").disableStats());
      registerBlock(195, (String)"jungle_door", (new BlockDoor(Material.wood)).setHardness(3.0F).setStepSound(soundTypeWood).setUnlocalizedName("doorJungle").disableStats());
      registerBlock(196, (String)"acacia_door", (new BlockDoor(Material.wood)).setHardness(3.0F).setStepSound(soundTypeWood).setUnlocalizedName("doorAcacia").disableStats());
      registerBlock(197, (String)"dark_oak_door", (new BlockDoor(Material.wood)).setHardness(3.0F).setStepSound(soundTypeWood).setUnlocalizedName("doorDarkOak").disableStats());
      blockRegistry.validateKey();
      Iterator var13 = blockRegistry.iterator();

      while(true) {
         Block block14;
         while(var13.hasNext()) {
            block14 = (Block)var13.next();
            if (block14.blockMaterial == Material.air) {
               block14.useNeighborBrightness = false;
            } else {
               boolean flag = false;
               boolean flag1 = block14 instanceof BlockStairs;
               boolean flag2 = block14 instanceof BlockSlab;
               boolean flag3 = block14 == block6;
               boolean flag4 = block14.translucent;
               boolean flag5 = block14.lightOpacity == 0;
               if (flag1 || flag2 || flag3 || flag4 || flag5) {
                  flag = true;
               }

               block14.useNeighborBrightness = flag;
            }
         }

         var13 = blockRegistry.iterator();

         while(var13.hasNext()) {
            block14 = (Block)var13.next();
            UnmodifiableIterator var21 = block14.getBlockState().getValidStates().iterator();

            while(var21.hasNext()) {
               IBlockState iblockstate = (IBlockState)var21.next();
               int i = blockRegistry.getIDForObject(block14) << 4 | block14.getMetaFromState(iblockstate);
               BLOCK_STATE_IDS.put(iblockstate, i);
            }
         }

         return;
      }
   }

   private static void registerBlock(int id, ResourceLocation textualID, Block block_) {
      blockRegistry.register(id, textualID, block_);
   }

   private static void registerBlock(int id, String textualID, Block block_) {
      registerBlock(id, new ResourceLocation(textualID), block_);
   }

   static {
      blockRegistry = new RegistryNamespacedDefaultedByKey(AIR_ID);
      BLOCK_STATE_IDS = new ObjectIntIdentityMap();
      soundTypeStone = new Block.SoundType("stone", 1.0F, 1.0F);
      soundTypeWood = new Block.SoundType("wood", 1.0F, 1.0F);
      soundTypeGravel = new Block.SoundType("gravel", 1.0F, 1.0F);
      soundTypeGrass = new Block.SoundType("grass", 1.0F, 1.0F);
      soundTypePiston = new Block.SoundType("stone", 1.0F, 1.0F);
      soundTypeMetal = new Block.SoundType("stone", 1.0F, 1.5F);
      soundTypeGlass = new Block.SoundType("stone", 1.0F, 1.0F) {
         public String getBreakSound() {
            return "dig.glass";
         }

         public String getPlaceSound() {
            return "step.stone";
         }
      };
      soundTypeCloth = new Block.SoundType("cloth", 1.0F, 1.0F);
      soundTypeSand = new Block.SoundType("sand", 1.0F, 1.0F);
      soundTypeSnow = new Block.SoundType("snow", 1.0F, 1.0F);
      soundTypeLadder = new Block.SoundType("ladder", 1.0F, 1.0F) {
         public String getBreakSound() {
            return "dig.wood";
         }
      };
      soundTypeAnvil = new Block.SoundType("anvil", 0.3F, 1.0F) {
         public String getBreakSound() {
            return "dig.stone";
         }

         public String getPlaceSound() {
            return "random.anvil_land";
         }
      };
      SLIME_SOUND = new Block.SoundType("slime", 1.0F, 1.0F) {
         public String getBreakSound() {
            return "mob.slime.big";
         }

         public String getPlaceSound() {
            return "mob.slime.big";
         }

         public String getStepSound() {
            return "mob.slime.small";
         }
      };
   }

   public static class SoundType {
      public final String soundName;
      public final float volume;
      public final float frequency;

      public SoundType(String name, float volume, float frequency) {
         this.soundName = name;
         this.volume = volume;
         this.frequency = frequency;
      }

      public float getVolume() {
         return this.volume;
      }

      public float getFrequency() {
         return this.frequency;
      }

      public String getBreakSound() {
         return "dig." + this.soundName;
      }

      public String getStepSound() {
         return "step." + this.soundName;
      }

      public String getPlaceSound() {
         return this.getBreakSound();
      }
   }

   public static enum EnumOffsetType {
      NONE,
      XZ,
      XYZ;
   }
}
