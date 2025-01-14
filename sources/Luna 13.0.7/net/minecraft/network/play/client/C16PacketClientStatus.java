package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C16PacketClientStatus
  implements Packet<INetHandler>
{
  private EnumState status;
  private static final String __OBFID = "CL_00001348";
  
  public C16PacketClientStatus() {}
  
  public C16PacketClientStatus(EnumState statusIn)
  {
    this.status = statusIn;
  }
  
  public void readPacketData(PacketBuffer data)
    throws IOException
  {
    this.status = ((EnumState)data.readEnumValue(EnumState.class));
  }
  
  public void writePacketData(PacketBuffer data)
    throws IOException
  {
    data.writeEnumValue(this.status);
  }
  
  public void func_180758_a(INetHandlerPlayServer p_180758_1_)
  {
    p_180758_1_.processClientStatus(this);
  }
  
  public EnumState getStatus()
  {
    return this.status;
  }
  
  public void processPacket(INetHandler handler)
  {
    func_180758_a((INetHandlerPlayServer)handler);
  }
  
  public static enum EnumState
  {
    private static final EnumState[] $VALUES = { PERFORM_RESPAWN, REQUEST_STATS, OPEN_INVENTORY_ACHIEVEMENT };
    private static final String __OBFID = "CL_00001349";
  }
}
