package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class C07PacketPlayerDigging
  implements Packet<INetHandler>
{
  private BlockPos field_179717_a;
  private EnumFacing field_179716_b;
  private Action status;
  private static final String __OBFID = "CL_00001365";
  
  public C07PacketPlayerDigging() {}
  
  public C07PacketPlayerDigging(Action p_i45940_1_, BlockPos p_i45940_2_, EnumFacing p_i45940_3_)
  {
    this.status = p_i45940_1_;
    this.field_179717_a = p_i45940_2_;
    this.field_179716_b = p_i45940_3_;
  }
  
  public void readPacketData(PacketBuffer data)
    throws IOException
  {
    this.status = ((Action)data.readEnumValue(Action.class));
    this.field_179717_a = data.readBlockPos();
    this.field_179716_b = EnumFacing.getFront(data.readUnsignedByte());
  }
  
  public void writePacketData(PacketBuffer data)
    throws IOException
  {
    data.writeEnumValue(this.status);
    data.writeBlockPos(this.field_179717_a);
    data.writeByte(this.field_179716_b.getIndex());
  }
  
  public void func_180763_a(INetHandlerPlayServer p_180763_1_)
  {
    p_180763_1_.processPlayerDigging(this);
  }
  
  public BlockPos func_179715_a()
  {
    return this.field_179717_a;
  }
  
  public EnumFacing func_179714_b()
  {
    return this.field_179716_b;
  }
  
  public Action func_180762_c()
  {
    return this.status;
  }
  
  public void processPacket(INetHandler handler)
  {
    func_180763_a((INetHandlerPlayServer)handler);
  }
  
  public static enum Action
  {
    private static final Action[] $VALUES = { START_DESTROY_BLOCK, ABORT_DESTROY_BLOCK, STOP_DESTROY_BLOCK, DROP_ALL_ITEMS, DROP_ITEM, RELEASE_USE_ITEM };
    private static final String __OBFID = "CL_00002284";
  }
}
