package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class BlockWoodSlab
  extends BlockSlab
{
  public static final PropertyEnum field_176557_b = PropertyEnum.create("variant", BlockPlanks.EnumType.class);
  private static final String __OBFID = "CL_00000337";
  
  public BlockWoodSlab()
  {
    super(Material.wood);
    IBlockState var1 = this.blockState.getBaseState();
    if (!isDouble()) {
      var1 = var1.withProperty(HALF_PROP, BlockSlab.EnumBlockHalf.BOTTOM);
    }
    setDefaultState(var1.withProperty(field_176557_b, BlockPlanks.EnumType.OAK));
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune)
  {
    return Item.getItemFromBlock(Blocks.wooden_slab);
  }
  
  public Item getItem(World worldIn, BlockPos pos)
  {
    return Item.getItemFromBlock(Blocks.wooden_slab);
  }
  
  public String getFullSlabName(int p_150002_1_)
  {
    return super.getUnlocalizedName() + "." + BlockPlanks.EnumType.func_176837_a(p_150002_1_).func_176840_c();
  }
  
  public IProperty func_176551_l()
  {
    return field_176557_b;
  }
  
  public Object func_176553_a(ItemStack p_176553_1_)
  {
    return BlockPlanks.EnumType.func_176837_a(p_176553_1_.getMetadata() & 0x7);
  }
  
  public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
  {
    if (itemIn != Item.getItemFromBlock(Blocks.double_wooden_slab))
    {
      BlockPlanks.EnumType[] var4 = BlockPlanks.EnumType.values();
      int var5 = var4.length;
      for (int var6 = 0; var6 < var5; var6++)
      {
        BlockPlanks.EnumType var7 = var4[var6];
        list.add(new ItemStack(itemIn, 1, var7.func_176839_a()));
      }
    }
  }
  
  public IBlockState getStateFromMeta(int meta)
  {
    IBlockState var2 = getDefaultState().withProperty(field_176557_b, BlockPlanks.EnumType.func_176837_a(meta & 0x7));
    if (!isDouble()) {
      var2 = var2.withProperty(HALF_PROP, (meta & 0x8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
    }
    return var2;
  }
  
  public int getMetaFromState(IBlockState state)
  {
    byte var2 = 0;
    int var3 = var2 | ((BlockPlanks.EnumType)state.getValue(field_176557_b)).func_176839_a();
    if ((!isDouble()) && (state.getValue(HALF_PROP) == BlockSlab.EnumBlockHalf.TOP)) {
      var3 |= 0x8;
    }
    return var3;
  }
  
  protected BlockState createBlockState()
  {
    return isDouble() ? new BlockState(this, new IProperty[] { field_176557_b }) : new BlockState(this, new IProperty[] { HALF_PROP, field_176557_b });
  }
  
  public int damageDropped(IBlockState state)
  {
    return ((BlockPlanks.EnumType)state.getValue(field_176557_b)).func_176839_a();
  }
}
