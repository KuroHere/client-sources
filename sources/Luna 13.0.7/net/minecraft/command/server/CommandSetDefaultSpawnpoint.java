package net.minecraft.command.server;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandSetDefaultSpawnpoint
  extends CommandBase
{
  private static final String __OBFID = "CL_00000973";
  
  public CommandSetDefaultSpawnpoint() {}
  
  public String getCommandName()
  {
    return "setworldspawn";
  }
  
  public int getRequiredPermissionLevel()
  {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender sender)
  {
    return "orders.setworldspawn.usage";
  }
  
  public void processCommand(ICommandSender sender, String[] args)
    throws CommandException
  {
    BlockPos var3;
    BlockPos var3;
    if (args.length == 0)
    {
      var3 = getCommandSenderAsPlayer(sender).getPosition();
    }
    else
    {
      if ((args.length != 3) || (sender.getEntityWorld() == null)) {
        throw new WrongUsageException("orders.setworldspawn.usage", new Object[0]);
      }
      var3 = func_175757_a(sender, args, 0, true);
    }
    sender.getEntityWorld().setSpawnLocation(var3);
    MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(new S05PacketSpawnPosition(var3));
    notifyOperators(sender, this, "orders.setworldspawn.success", new Object[] { Integer.valueOf(var3.getX()), Integer.valueOf(var3.getY()), Integer.valueOf(var3.getZ()) });
  }
  
  public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
  {
    return (args.length > 0) && (args.length <= 3) ? func_175771_a(args, 0, pos) : null;
  }
}
