package net.minecraft.network.status.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusServer;

public class C00PacketServerQuery
  implements Packet<INetHandler>
{
  private static final String __OBFID = "CL_00001393";
  
  public C00PacketServerQuery() {}
  
  public void readPacketData(PacketBuffer data)
    throws IOException
  {}
  
  public void writePacketData(PacketBuffer data)
    throws IOException
  {}
  
  public void func_180775_a(INetHandlerStatusServer p_180775_1_)
  {
    p_180775_1_.processServerQuery(this);
  }
  
  public void processPacket(INetHandler handler)
  {
    func_180775_a((INetHandlerStatusServer)handler);
  }
}
