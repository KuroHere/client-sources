package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockNewLog
  extends BlockLog
{
  public static final PropertyEnum field_176300_b = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate()
  {
    private static final String __OBFID = "CL_00002089";
    
    public boolean func_180194_a(BlockPlanks.EnumType p_180194_1_)
    {
      return p_180194_1_.func_176839_a() >= 4;
    }
    
    public boolean apply(Object p_apply_1_)
    {
      return func_180194_a((BlockPlanks.EnumType)p_apply_1_);
    }
  });
  private static final String __OBFID = "CL_00000277";
  
  public BlockNewLog()
  {
    setDefaultState(this.blockState.getBaseState().withProperty(field_176300_b, BlockPlanks.EnumType.ACACIA).withProperty(AXIS_PROP, BlockLog.EnumAxis.Y));
  }
  
  public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
  {
    list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.ACACIA.func_176839_a() - 4));
    list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4));
  }
  
  public IBlockState getStateFromMeta(int meta)
  {
    IBlockState var2 = getDefaultState().withProperty(field_176300_b, BlockPlanks.EnumType.func_176837_a((meta & 0x3) + 4));
    switch (meta & 0xC)
    {
    case 0: 
      var2 = var2.withProperty(AXIS_PROP, BlockLog.EnumAxis.Y);
      break;
    case 4: 
      var2 = var2.withProperty(AXIS_PROP, BlockLog.EnumAxis.X);
      break;
    case 8: 
      var2 = var2.withProperty(AXIS_PROP, BlockLog.EnumAxis.Z);
      break;
    default: 
      var2 = var2.withProperty(AXIS_PROP, BlockLog.EnumAxis.NONE);
    }
    return var2;
  }
  
  public int getMetaFromState(IBlockState state)
  {
    byte var2 = 0;
    int var3 = var2 | ((BlockPlanks.EnumType)state.getValue(field_176300_b)).func_176839_a() - 4;
    switch (SwitchEnumAxis.field_180191_a[((BlockLog.EnumAxis)state.getValue(AXIS_PROP)).ordinal()])
    {
    case 1: 
      var3 |= 0x4;
      break;
    case 2: 
      var3 |= 0x8;
      break;
    case 3: 
      var3 |= 0xC;
    }
    return var3;
  }
  
  protected BlockState createBlockState()
  {
    return new BlockState(this, new IProperty[] { field_176300_b, AXIS_PROP });
  }
  
  protected ItemStack createStackedBlock(IBlockState state)
  {
    return new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType)state.getValue(field_176300_b)).func_176839_a() - 4);
  }
  
  public int damageDropped(IBlockState state)
  {
    return ((BlockPlanks.EnumType)state.getValue(field_176300_b)).func_176839_a() - 4;
  }
  
  static final class SwitchEnumAxis
  {
    static final int[] field_180191_a = new int[BlockLog.EnumAxis.values().length];
    private static final String __OBFID = "CL_00002088";
    
    SwitchEnumAxis() {}
    
    static
    {
      try
      {
        field_180191_a[BlockLog.EnumAxis.X.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        field_180191_a[BlockLog.EnumAxis.Z.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      try
      {
        field_180191_a[BlockLog.EnumAxis.NONE.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
    }
  }
}
