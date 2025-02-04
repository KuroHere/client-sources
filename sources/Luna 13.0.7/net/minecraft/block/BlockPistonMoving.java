package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonMoving
  extends BlockContainer
{
  public static final PropertyDirection field_176426_a = BlockPistonExtension.field_176326_a;
  public static final PropertyEnum field_176425_b = BlockPistonExtension.field_176325_b;
  private static final String __OBFID = "CL_00000368";
  
  public BlockPistonMoving()
  {
    super(Material.piston);
    setDefaultState(this.blockState.getBaseState().withProperty(field_176426_a, EnumFacing.NORTH).withProperty(field_176425_b, BlockPistonExtension.EnumPistonType.DEFAULT));
    setHardness(-1.0F);
  }
  
  public TileEntity createNewTileEntity(World worldIn, int meta)
  {
    return null;
  }
  
  public static TileEntity func_176423_a(IBlockState p_176423_0_, EnumFacing p_176423_1_, boolean p_176423_2_, boolean p_176423_3_)
  {
    return new TileEntityPiston(p_176423_0_, p_176423_1_, p_176423_2_, p_176423_3_);
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
  {
    TileEntity var4 = worldIn.getTileEntity(pos);
    if ((var4 instanceof TileEntityPiston)) {
      ((TileEntityPiston)var4).clearPistonTileEntity();
    } else {
      super.breakBlock(worldIn, pos, state);
    }
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
  {
    return false;
  }
  
  public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
  {
    return false;
  }
  
  public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
  {
    BlockPos var4 = pos.offset(((EnumFacing)state.getValue(field_176426_a)).getOpposite());
    IBlockState var5 = worldIn.getBlockState(var4);
    if (((var5.getBlock() instanceof BlockPistonBase)) && (((Boolean)var5.getValue(BlockPistonBase.EXTENDED)).booleanValue())) {
      worldIn.setBlockToAir(var4);
    }
  }
  
  public boolean isOpaqueCube()
  {
    return false;
  }
  
  public boolean isFullCube()
  {
    return false;
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
  {
    if ((!worldIn.isRemote) && (worldIn.getTileEntity(pos) == null))
    {
      worldIn.setBlockToAir(pos);
      return true;
    }
    return false;
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune)
  {
    return null;
  }
  
  public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
  {
    if (!worldIn.isRemote)
    {
      TileEntityPiston var6 = func_176422_e(worldIn, pos);
      if (var6 != null)
      {
        IBlockState var7 = var6.func_174927_b();
        var7.getBlock().dropBlockAsItem(worldIn, pos, var7, 0);
      }
    }
  }
  
  public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
  {
    return null;
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
  {
    if (!worldIn.isRemote) {
      worldIn.getTileEntity(pos);
    }
  }
  
  public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
  {
    TileEntityPiston var4 = func_176422_e(worldIn, pos);
    if (var4 == null) {
      return null;
    }
    float var5 = var4.func_145860_a(0.0F);
    if (var4.isExtending()) {
      var5 = 1.0F - var5;
    }
    return func_176424_a(worldIn, pos, var4.func_174927_b(), var5, var4.func_174930_e());
  }
  
  public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos)
  {
    TileEntityPiston var3 = func_176422_e(access, pos);
    if (var3 != null)
    {
      IBlockState var4 = var3.func_174927_b();
      Block var5 = var4.getBlock();
      if ((var5 == this) || (var5.getMaterial() == Material.air)) {
        return;
      }
      float var6 = var3.func_145860_a(0.0F);
      if (var3.isExtending()) {
        var6 = 1.0F - var6;
      }
      var5.setBlockBoundsBasedOnState(access, pos);
      if ((var5 == Blocks.piston) || (var5 == Blocks.sticky_piston)) {
        var6 = 0.0F;
      }
      EnumFacing var7 = var3.func_174930_e();
      this.minX = (var5.getBlockBoundsMinX() - var7.getFrontOffsetX() * var6);
      this.minY = (var5.getBlockBoundsMinY() - var7.getFrontOffsetY() * var6);
      this.minZ = (var5.getBlockBoundsMinZ() - var7.getFrontOffsetZ() * var6);
      this.maxX = (var5.getBlockBoundsMaxX() - var7.getFrontOffsetX() * var6);
      this.maxY = (var5.getBlockBoundsMaxY() - var7.getFrontOffsetY() * var6);
      this.maxZ = (var5.getBlockBoundsMaxZ() - var7.getFrontOffsetZ() * var6);
    }
  }
  
  public AxisAlignedBB func_176424_a(World worldIn, BlockPos p_176424_2_, IBlockState p_176424_3_, float p_176424_4_, EnumFacing p_176424_5_)
  {
    if ((p_176424_3_.getBlock() != this) && (p_176424_3_.getBlock().getMaterial() != Material.air))
    {
      AxisAlignedBB var6 = p_176424_3_.getBlock().getCollisionBoundingBox(worldIn, p_176424_2_, p_176424_3_);
      if (var6 == null) {
        return null;
      }
      double var7 = var6.minX;
      double var9 = var6.minY;
      double var11 = var6.minZ;
      double var13 = var6.maxX;
      double var15 = var6.maxY;
      double var17 = var6.maxZ;
      if (p_176424_5_.getFrontOffsetX() < 0) {
        var7 -= p_176424_5_.getFrontOffsetX() * p_176424_4_;
      } else {
        var13 -= p_176424_5_.getFrontOffsetX() * p_176424_4_;
      }
      if (p_176424_5_.getFrontOffsetY() < 0) {
        var9 -= p_176424_5_.getFrontOffsetY() * p_176424_4_;
      } else {
        var15 -= p_176424_5_.getFrontOffsetY() * p_176424_4_;
      }
      if (p_176424_5_.getFrontOffsetZ() < 0) {
        var11 -= p_176424_5_.getFrontOffsetZ() * p_176424_4_;
      } else {
        var17 -= p_176424_5_.getFrontOffsetZ() * p_176424_4_;
      }
      return new AxisAlignedBB(var7, var9, var11, var13, var15, var17);
    }
    return null;
  }
  
  private TileEntityPiston func_176422_e(IBlockAccess p_176422_1_, BlockPos p_176422_2_)
  {
    TileEntity var3 = p_176422_1_.getTileEntity(p_176422_2_);
    return (var3 instanceof TileEntityPiston) ? (TileEntityPiston)var3 : null;
  }
  
  public Item getItem(World worldIn, BlockPos pos)
  {
    return null;
  }
  
  public IBlockState getStateFromMeta(int meta)
  {
    return getDefaultState().withProperty(field_176426_a, BlockPistonExtension.func_176322_b(meta)).withProperty(field_176425_b, (meta & 0x8) > 0 ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
  }
  
  public int getMetaFromState(IBlockState state)
  {
    byte var2 = 0;
    int var3 = var2 | ((EnumFacing)state.getValue(field_176426_a)).getIndex();
    if (state.getValue(field_176425_b) == BlockPistonExtension.EnumPistonType.STICKY) {
      var3 |= 0x8;
    }
    return var3;
  }
  
  protected BlockState createBlockState()
  {
    return new BlockState(this, new IProperty[] { field_176426_a, field_176425_b });
  }
}
