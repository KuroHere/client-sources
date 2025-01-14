package net.minecraft.network;

import com.google.common.collect.Lists;
import io.netty.buffer.Unpooled;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook.EnumFlags;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.server.management.UserListBans;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IntHashMap;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.storage.WorldInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetHandlerPlayServer implements INetHandlerPlayServer, net.minecraft.server.gui.IUpdatePlayerListBox
{
  private static final Logger logger = ;
  
  public final NetworkManager netManager;
  
  private final MinecraftServer serverController;
  
  public EntityPlayerMP playerEntity;
  
  private int networkTickCount;
  
  private int field_175090_f;
  
  private int floatingTickCount;
  
  private boolean field_147366_g;
  
  private int field_147378_h;
  
  private long lastPingTime;
  
  private long lastSentPingPacket;
  private int chatSpamThresholdCount;
  private int itemDropThreshold;
  private IntHashMap field_147372_n = new IntHashMap();
  private double lastPosX;
  private double lastPosY;
  private double lastPosZ;
  private boolean hasMoved = true;
  private static final String __OBFID = "CL_00001452";
  
  public NetHandlerPlayServer(MinecraftServer server, NetworkManager networkManagerIn, EntityPlayerMP playerIn)
  {
    serverController = server;
    netManager = networkManagerIn;
    networkManagerIn.setNetHandler(this);
    playerEntity = playerIn;
    playerNetServerHandler = this;
  }
  



  public void update()
  {
    field_147366_g = false;
    networkTickCount += 1;
    serverController.theProfiler.startSection("keepAlive");
    
    if (networkTickCount - lastSentPingPacket > 40L)
    {
      lastSentPingPacket = networkTickCount;
      lastPingTime = currentTimeMillis();
      field_147378_h = ((int)lastPingTime);
      sendPacket(new net.minecraft.network.play.server.S00PacketKeepAlive(field_147378_h));
    }
    
    serverController.theProfiler.endSection();
    
    if (chatSpamThresholdCount > 0)
    {
      chatSpamThresholdCount -= 1;
    }
    
    if (itemDropThreshold > 0)
    {
      itemDropThreshold -= 1;
    }
    
    if ((playerEntity.getLastActiveTime() > 0L) && (serverController.getMaxPlayerIdleMinutes() > 0) && (MinecraftServer.getCurrentTimeMillis() - playerEntity.getLastActiveTime() > serverController.getMaxPlayerIdleMinutes() * 1000 * 60))
    {
      kickPlayerFromServer("You have been idle for too long!");
    }
  }
  
  public NetworkManager getNetworkManager()
  {
    return netManager;
  }
  



  public void kickPlayerFromServer(String reason)
  {
    final ChatComponentText var2 = new ChatComponentText(reason);
    netManager.sendPacket(new S40PacketDisconnect(var2), new GenericFutureListener()
    {
      private static final String __OBFID = "CL_00001453";
      
      public void operationComplete(Future p_operationComplete_1_) {
        netManager.closeChannel(var2);
      }
    }, new GenericFutureListener[0]);
    netManager.disableAutoRead();
    com.google.common.util.concurrent.Futures.getUnchecked(serverController.addScheduledTask(new Runnable()
    {
      private static final String __OBFID = "CL_00001454";
      
      public void run() {
        netManager.checkDisconnected();
      }
    }));
  }
  




  public void processInput(C0CPacketInput packetIn)
  {
    PacketThreadUtil.func_180031_a(packetIn, this, playerEntity.getServerForPlayer());
    playerEntity.setEntityActionState(packetIn.getStrafeSpeed(), packetIn.getForwardSpeed(), packetIn.isJumping(), packetIn.isSneaking());
  }
  



  public void processPlayer(C03PacketPlayer packetIn)
  {
    PacketThreadUtil.func_180031_a(packetIn, this, playerEntity.getServerForPlayer());
    WorldServer var2 = serverController.worldServerForDimension(playerEntity.dimension);
    field_147366_g = true;
    
    if (!playerEntity.playerConqueredTheEnd)
    {
      double var3 = playerEntity.posX;
      double var5 = playerEntity.posY;
      double var7 = playerEntity.posZ;
      double var9 = 0.0D;
      double var11 = packetIn.getPositionX() - lastPosX;
      double var13 = packetIn.getPositionY() - lastPosY;
      double var15 = packetIn.getPositionZ() - lastPosZ;
      
      if (packetIn.func_149466_j())
      {
        var9 = var11 * var11 + var13 * var13 + var15 * var15;
        
        if ((!hasMoved) && (var9 < 0.25D))
        {
          hasMoved = true;
        }
      }
      
      if (hasMoved)
      {
        field_175090_f = networkTickCount;
        



        if (playerEntity.ridingEntity != null)
        {
          float var47 = playerEntity.rotationYaw;
          float var18 = playerEntity.rotationPitch;
          playerEntity.ridingEntity.updateRiderPosition();
          double var19 = playerEntity.posX;
          double var21 = playerEntity.posY;
          double var23 = playerEntity.posZ;
          
          if (packetIn.getRotating())
          {
            var47 = packetIn.getYaw();
            var18 = packetIn.getPitch();
          }
          
          playerEntity.onGround = packetIn.func_149465_i();
          playerEntity.onUpdateEntity();
          playerEntity.setPositionAndRotation(var19, var21, var23, var47, var18);
          
          if (playerEntity.ridingEntity != null)
          {
            playerEntity.ridingEntity.updateRiderPosition();
          }
          
          serverController.getConfigurationManager().serverUpdateMountedMovingPlayer(playerEntity);
          
          if (playerEntity.ridingEntity != null)
          {
            if (var9 > 4.0D)
            {
              Entity var48 = playerEntity.ridingEntity;
              playerEntity.playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S18PacketEntityTeleport(var48));
              setPlayerLocation(playerEntity.posX, playerEntity.posY, playerEntity.posZ, playerEntity.rotationYaw, playerEntity.rotationPitch);
            }
            
            playerEntity.ridingEntity.isAirBorne = true;
          }
          
          if (hasMoved)
          {
            lastPosX = playerEntity.posX;
            lastPosY = playerEntity.posY;
            lastPosZ = playerEntity.posZ;
          }
          
          var2.updateEntity(playerEntity);
          return;
        }
        
        if (playerEntity.isPlayerSleeping())
        {
          playerEntity.onUpdateEntity();
          playerEntity.setPositionAndRotation(lastPosX, lastPosY, lastPosZ, playerEntity.rotationYaw, playerEntity.rotationPitch);
          var2.updateEntity(playerEntity);
          return;
        }
        
        double var17 = playerEntity.posY;
        lastPosX = playerEntity.posX;
        lastPosY = playerEntity.posY;
        lastPosZ = playerEntity.posZ;
        double var19 = playerEntity.posX;
        double var21 = playerEntity.posY;
        double var23 = playerEntity.posZ;
        float var25 = playerEntity.rotationYaw;
        float var26 = playerEntity.rotationPitch;
        
        if ((packetIn.func_149466_j()) && (packetIn.getPositionY() == -999.0D))
        {
          packetIn.func_149469_a(false);
        }
        
        if (packetIn.func_149466_j())
        {
          var19 = packetIn.getPositionX();
          var21 = packetIn.getPositionY();
          var23 = packetIn.getPositionZ();
          
          if ((Math.abs(packetIn.getPositionX()) > 3.0E7D) || (Math.abs(packetIn.getPositionZ()) > 3.0E7D))
          {
            kickPlayerFromServer("Illegal position");
            return;
          }
        }
        
        if (packetIn.getRotating())
        {
          var25 = packetIn.getYaw();
          var26 = packetIn.getPitch();
        }
        
        playerEntity.onUpdateEntity();
        playerEntity.setPositionAndRotation(lastPosX, lastPosY, lastPosZ, var25, var26);
        
        if (!hasMoved)
        {
          return;
        }
        
        double var27 = var19 - playerEntity.posX;
        double var29 = var21 - playerEntity.posY;
        double var31 = var23 - playerEntity.posZ;
        double var33 = Math.min(Math.abs(var27), Math.abs(playerEntity.motionX));
        double var35 = Math.min(Math.abs(var29), Math.abs(playerEntity.motionY));
        double var37 = Math.min(Math.abs(var31), Math.abs(playerEntity.motionZ));
        double var39 = var33 * var33 + var35 * var35 + var37 * var37;
        
        if ((var39 > 100.0D) && ((!serverController.isSinglePlayer()) || (!serverController.getServerOwner().equals(playerEntity.getName()))))
        {
          logger.warn(playerEntity.getName() + " moved too quickly! " + var27 + "," + var29 + "," + var31 + " (" + var33 + ", " + var35 + ", " + var37 + ")");
          setPlayerLocation(lastPosX, lastPosY, lastPosZ, playerEntity.rotationYaw, playerEntity.rotationPitch);
          return;
        }
        
        float var41 = 0.0625F;
        boolean var42 = var2.getCollidingBoundingBoxes(playerEntity, playerEntity.getEntityBoundingBox().contract(var41, var41, var41)).isEmpty();
        
        if ((playerEntity.onGround) && (!packetIn.func_149465_i()) && (var29 > 0.0D))
        {
          playerEntity.jump();
        }
        
        playerEntity.moveEntity(var27, var29, var31);
        playerEntity.onGround = packetIn.func_149465_i();
        double var43 = var29;
        var27 = var19 - playerEntity.posX;
        var29 = var21 - playerEntity.posY;
        
        if ((var29 > -0.5D) || (var29 < 0.5D))
        {
          var29 = 0.0D;
        }
        
        var31 = var23 - playerEntity.posZ;
        var39 = var27 * var27 + var29 * var29 + var31 * var31;
        boolean var45 = false;
        
        if ((var39 > 0.0625D) && (!playerEntity.isPlayerSleeping()) && (!playerEntity.theItemInWorldManager.isCreative()))
        {
          var45 = true;
          logger.warn(playerEntity.getName() + " moved wrongly!");
        }
        
        playerEntity.setPositionAndRotation(var19, var21, var23, var25, var26);
        playerEntity.addMovementStat(playerEntity.posX - var3, playerEntity.posY - var5, playerEntity.posZ - var7);
        
        if (!playerEntity.noClip)
        {
          boolean var46 = var2.getCollidingBoundingBoxes(playerEntity, playerEntity.getEntityBoundingBox().contract(var41, var41, var41)).isEmpty();
          
          if ((var42) && ((var45) || (!var46)) && (!playerEntity.isPlayerSleeping()))
          {
            setPlayerLocation(lastPosX, lastPosY, lastPosZ, var25, var26);
            return;
          }
        }
        
        AxisAlignedBB var49 = playerEntity.getEntityBoundingBox().expand(var41, var41, var41).addCoord(0.0D, -0.55D, 0.0D);
        
        if ((!serverController.isFlightAllowed()) && (!playerEntity.capabilities.allowFlying) && (!var2.checkBlockCollision(var49)))
        {
          if (var43 >= -0.03125D)
          {
            floatingTickCount += 1;
            
            if (floatingTickCount > 80)
            {
              logger.warn(playerEntity.getName() + " was kicked for floating too long!");
              kickPlayerFromServer("Flying is not enabled on this server");
            }
            
          }
          
        }
        else {
          floatingTickCount = 0;
        }
        
        playerEntity.onGround = packetIn.func_149465_i();
        serverController.getConfigurationManager().serverUpdateMountedMovingPlayer(playerEntity);
        playerEntity.handleFalling(playerEntity.posY - var17, packetIn.func_149465_i());
      }
      else if (networkTickCount - field_175090_f > 20)
      {
        setPlayerLocation(lastPosX, lastPosY, lastPosZ, playerEntity.rotationYaw, playerEntity.rotationPitch);
      }
    }
  }
  
  public void setPlayerLocation(double x, double y, double z, float yaw, float pitch)
  {
    func_175089_a(x, y, z, yaw, pitch, java.util.Collections.emptySet());
  }
  
  public void func_175089_a(double p_175089_1_, double p_175089_3_, double p_175089_5_, float p_175089_7_, float p_175089_8_, Set p_175089_9_)
  {
    hasMoved = false;
    lastPosX = p_175089_1_;
    lastPosY = p_175089_3_;
    lastPosZ = p_175089_5_;
    
    if (p_175089_9_.contains(S08PacketPlayerPosLook.EnumFlags.X))
    {
      lastPosX += playerEntity.posX;
    }
    
    if (p_175089_9_.contains(S08PacketPlayerPosLook.EnumFlags.Y))
    {
      lastPosY += playerEntity.posY;
    }
    
    if (p_175089_9_.contains(S08PacketPlayerPosLook.EnumFlags.Z))
    {
      lastPosZ += playerEntity.posZ;
    }
    
    float var10 = p_175089_7_;
    float var11 = p_175089_8_;
    
    if (p_175089_9_.contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT))
    {
      var10 = p_175089_7_ + playerEntity.rotationYaw;
    }
    
    if (p_175089_9_.contains(S08PacketPlayerPosLook.EnumFlags.X_ROT))
    {
      var11 = p_175089_8_ + playerEntity.rotationPitch;
    }
    
    playerEntity.setPositionAndRotation(lastPosX, lastPosY, lastPosZ, var10, var11);
    playerEntity.playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S08PacketPlayerPosLook(p_175089_1_, p_175089_3_, p_175089_5_, p_175089_7_, p_175089_8_, p_175089_9_));
  }
  





  public void processPlayerDigging(C07PacketPlayerDigging packetIn)
  {
    PacketThreadUtil.func_180031_a(packetIn, this, playerEntity.getServerForPlayer());
    WorldServer var2 = serverController.worldServerForDimension(playerEntity.dimension);
    BlockPos var3 = packetIn.func_179715_a();
    playerEntity.markPlayerActive();
    
    switch (SwitchAction.field_180224_a[packetIn.func_180762_c().ordinal()])
    {
    case 1: 
      if (!playerEntity.func_175149_v())
      {
        playerEntity.dropOneItem(false);
      }
      
      return;
    
    case 2: 
      if (!playerEntity.func_175149_v())
      {
        playerEntity.dropOneItem(true);
      }
      
      return;
    
    case 3: 
      playerEntity.stopUsingItem();
      return;
    
    case 4: 
    case 5: 
    case 6: 
      double var4 = playerEntity.posX - (var3.getX() + 0.5D);
      double var6 = playerEntity.posY - (var3.getY() + 0.5D) + 1.5D;
      double var8 = playerEntity.posZ - (var3.getZ() + 0.5D);
      double var10 = var4 * var4 + var6 * var6 + var8 * var8;
      
      if (var10 > 36.0D)
      {
        return;
      }
      if (var3.getY() >= serverController.getBuildLimit())
      {
        return;
      }
      

      if (packetIn.func_180762_c() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK)
      {
        if ((!serverController.isBlockProtected(var2, var3, playerEntity)) && (var2.getWorldBorder().contains(var3)))
        {
          playerEntity.theItemInWorldManager.func_180784_a(var3, packetIn.func_179714_b());
        }
        else
        {
          playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(var2, var3));
        }
      }
      else
      {
        if (packetIn.func_180762_c() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK)
        {
          playerEntity.theItemInWorldManager.func_180785_a(var3);
        }
        else if (packetIn.func_180762_c() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK)
        {
          playerEntity.theItemInWorldManager.func_180238_e();
        }
        
        if (var2.getBlockState(var3).getBlock().getMaterial() != net.minecraft.block.material.Material.air)
        {
          playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(var2, var3));
        }
      }
      
      return;
    }
    
    
    throw new IllegalArgumentException("Invalid player action");
  }
  




  public void processPlayerBlockPlacement(C08PacketPlayerBlockPlacement packetIn)
  {
    PacketThreadUtil.func_180031_a(packetIn, this, playerEntity.getServerForPlayer());
    WorldServer var2 = serverController.worldServerForDimension(playerEntity.dimension);
    ItemStack var3 = playerEntity.inventory.getCurrentItem();
    boolean var4 = false;
    BlockPos var5 = packetIn.func_179724_a();
    EnumFacing var6 = EnumFacing.getFront(packetIn.getPlacedBlockDirection());
    playerEntity.markPlayerActive();
    
    if (packetIn.getPlacedBlockDirection() == 255)
    {
      if (var3 == null)
      {
        return;
      }
      
      playerEntity.theItemInWorldManager.tryUseItem(playerEntity, var2, var3);
    }
    else if ((var5.getY() >= serverController.getBuildLimit() - 1) && ((var6 == EnumFacing.UP) || (var5.getY() >= serverController.getBuildLimit())))
    {
      ChatComponentTranslation var7 = new ChatComponentTranslation("build.tooHigh", new Object[] { Integer.valueOf(serverController.getBuildLimit()) });
      var7.getChatStyle().setColor(EnumChatFormatting.RED);
      playerEntity.playerNetServerHandler.sendPacket(new S02PacketChat(var7));
      var4 = true;
    }
    else
    {
      if ((hasMoved) && (playerEntity.getDistanceSq(var5.getX() + 0.5D, var5.getY() + 0.5D, var5.getZ() + 0.5D) < 64.0D) && (!serverController.isBlockProtected(var2, var5, playerEntity)) && (var2.getWorldBorder().contains(var5)))
      {
        playerEntity.theItemInWorldManager.func_180236_a(playerEntity, var2, var3, var5, var6, packetIn.getPlacedBlockOffsetX(), packetIn.getPlacedBlockOffsetY(), packetIn.getPlacedBlockOffsetZ());
      }
      
      var4 = true;
    }
    
    if (var4)
    {
      playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(var2, var5));
      playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(var2, var5.offset(var6)));
    }
    
    var3 = playerEntity.inventory.getCurrentItem();
    
    if ((var3 != null) && (stackSize == 0))
    {
      playerEntity.inventory.mainInventory[playerEntity.inventory.currentItem] = null;
      var3 = null;
    }
    
    if ((var3 == null) || (var3.getMaxItemUseDuration() == 0))
    {
      playerEntity.isChangingQuantityOnly = true;
      playerEntity.inventory.mainInventory[playerEntity.inventory.currentItem] = ItemStack.copyItemStack(playerEntity.inventory.mainInventory[playerEntity.inventory.currentItem]);
      Slot var8 = playerEntity.openContainer.getSlotFromInventory(playerEntity.inventory, playerEntity.inventory.currentItem);
      playerEntity.openContainer.detectAndSendChanges();
      playerEntity.isChangingQuantityOnly = false;
      
      if (!ItemStack.areItemStacksEqual(playerEntity.inventory.getCurrentItem(), packetIn.getStack()))
      {
        sendPacket(new net.minecraft.network.play.server.S2FPacketSetSlot(playerEntity.openContainer.windowId, slotNumber, playerEntity.inventory.getCurrentItem()));
      }
    }
  }
  
  public void func_175088_a(C18PacketSpectate p_175088_1_)
  {
    PacketThreadUtil.func_180031_a(p_175088_1_, this, playerEntity.getServerForPlayer());
    
    if (playerEntity.func_175149_v())
    {
      Entity var2 = null;
      WorldServer[] var3 = serverController.worldServers;
      int var4 = var3.length;
      
      for (int var5 = 0; var5 < var4; var5++)
      {
        WorldServer var6 = var3[var5];
        
        if (var6 != null)
        {
          var2 = p_175088_1_.func_179727_a(var6);
          
          if (var2 != null) {
            break;
          }
        }
      }
      

      if (var2 != null)
      {
        playerEntity.func_175399_e(playerEntity);
        playerEntity.mountEntity(null);
        
        if (worldObj != playerEntity.worldObj)
        {
          WorldServer var7 = playerEntity.getServerForPlayer();
          WorldServer var8 = (WorldServer)worldObj;
          playerEntity.dimension = dimension;
          sendPacket(new net.minecraft.network.play.server.S07PacketRespawn(playerEntity.dimension, var7.getDifficulty(), var7.getWorldInfo().getTerrainType(), playerEntity.theItemInWorldManager.getGameType()));
          var7.removePlayerEntityDangerously(playerEntity);
          playerEntity.isDead = false;
          playerEntity.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
          
          if (playerEntity.isEntityAlive())
          {
            var7.updateEntityWithOptionalForce(playerEntity, false);
            var8.spawnEntityInWorld(playerEntity);
            var8.updateEntityWithOptionalForce(playerEntity, false);
          }
          
          playerEntity.setWorld(var8);
          serverController.getConfigurationManager().func_72375_a(playerEntity, var7);
          playerEntity.setPositionAndUpdate(posX, posY, posZ);
          playerEntity.theItemInWorldManager.setWorld(var8);
          serverController.getConfigurationManager().updateTimeAndWeatherForPlayer(playerEntity, var8);
          serverController.getConfigurationManager().syncPlayerInventory(playerEntity);
        }
        else
        {
          playerEntity.setPositionAndUpdate(posX, posY, posZ);
        }
      }
    }
  }
  


  public void func_175086_a(C19PacketResourcePackStatus p_175086_1_) {}
  

  public void onDisconnect(IChatComponent reason)
  {
    logger.info(playerEntity.getName() + " lost connection: " + reason);
    serverController.refreshStatusNextTick();
    ChatComponentTranslation var2 = new ChatComponentTranslation("multiplayer.player.left", new Object[] { playerEntity.getDisplayName() });
    var2.getChatStyle().setColor(EnumChatFormatting.YELLOW);
    serverController.getConfigurationManager().sendChatMsg(var2);
    playerEntity.mountEntityAndWakeUp();
    serverController.getConfigurationManager().playerLoggedOut(playerEntity);
    
    if ((serverController.isSinglePlayer()) && (playerEntity.getName().equals(serverController.getServerOwner())))
    {
      logger.info("Stopping singleplayer server as player logged out");
      serverController.initiateShutdown();
    }
  }
  
  public void sendPacket(final Packet packetIn)
  {
    if ((packetIn instanceof S02PacketChat))
    {
      S02PacketChat var2 = (S02PacketChat)packetIn;
      EntityPlayer.EnumChatVisibility var3 = playerEntity.getChatVisibility();
      
      if (var3 == EntityPlayer.EnumChatVisibility.HIDDEN)
      {
        return;
      }
      
      if ((var3 == EntityPlayer.EnumChatVisibility.SYSTEM) && (!var2.isChat()))
      {
        return;
      }
    }
    
    try
    {
      netManager.sendPacket(packetIn);
    }
    catch (Throwable var5)
    {
      CrashReport var6 = CrashReport.makeCrashReport(var5, "Sending packet");
      CrashReportCategory var4 = var6.makeCategory("Packet being sent");
      var4.addCrashSectionCallable("Packet class", new Callable()
      {
        private static final String __OBFID = "CL_00002270";
        
        public String func_180225_a() {
          return packetIn.getClass().getCanonicalName();
        }
        
        public Object call() {
          return func_180225_a();
        }
      });
      throw new net.minecraft.util.ReportedException(var6);
    }
  }
  



  public void processHeldItemChange(C09PacketHeldItemChange packetIn)
  {
    PacketThreadUtil.func_180031_a(packetIn, this, playerEntity.getServerForPlayer());
    
    if ((packetIn.getSlotId() >= 0) && (packetIn.getSlotId() < InventoryPlayer.getHotbarSize()))
    {
      playerEntity.inventory.currentItem = packetIn.getSlotId();
      playerEntity.markPlayerActive();
    }
    else
    {
      logger.warn(playerEntity.getName() + " tried to set an invalid carried item");
    }
  }
  



  public void processChatMessage(C01PacketChatMessage packetIn)
  {
    PacketThreadUtil.func_180031_a(packetIn, this, playerEntity.getServerForPlayer());
    
    if (playerEntity.getChatVisibility() == EntityPlayer.EnumChatVisibility.HIDDEN)
    {
      ChatComponentTranslation var4 = new ChatComponentTranslation("chat.cannotSend", new Object[0]);
      var4.getChatStyle().setColor(EnumChatFormatting.RED);
      sendPacket(new S02PacketChat(var4));
    }
    else
    {
      playerEntity.markPlayerActive();
      String var2 = packetIn.getMessage();
      var2 = org.apache.commons.lang3.StringUtils.normalizeSpace(var2);
      
      for (int var3 = 0; var3 < var2.length(); var3++)
      {
        if (!ChatAllowedCharacters.isAllowedCharacter(var2.charAt(var3)))
        {
          kickPlayerFromServer("Illegal characters in chat");
          return;
        }
      }
      
      if (var2.startsWith("/"))
      {
        handleSlashCommand(var2);
      }
      else
      {
        ChatComponentTranslation var5 = new ChatComponentTranslation("chat.type.text", new Object[] { playerEntity.getDisplayName(), var2 });
        serverController.getConfigurationManager().sendChatMsgImpl(var5, false);
      }
      
      chatSpamThresholdCount += 20;
      
      if ((chatSpamThresholdCount > 200) && (!serverController.getConfigurationManager().canSendCommands(playerEntity.getGameProfile())))
      {
        kickPlayerFromServer("disconnect.spam");
      }
    }
  }
  



  private void handleSlashCommand(String command)
  {
    serverController.getCommandManager().executeCommand(playerEntity, command);
  }
  
  public void func_175087_a(C0APacketAnimation p_175087_1_)
  {
    PacketThreadUtil.func_180031_a(p_175087_1_, this, playerEntity.getServerForPlayer());
    playerEntity.markPlayerActive();
    playerEntity.swingItem();
  }
  




  public void processEntityAction(C0BPacketEntityAction packetIn)
  {
    PacketThreadUtil.func_180031_a(packetIn, this, playerEntity.getServerForPlayer());
    playerEntity.markPlayerActive();
    
    switch (SwitchAction.field_180222_b[packetIn.func_180764_b().ordinal()])
    {
    case 1: 
      playerEntity.setSneaking(true);
      break;
    
    case 2: 
      playerEntity.setSneaking(false);
      break;
    
    case 3: 
      playerEntity.setSprinting(true);
      break;
    
    case 4: 
      playerEntity.setSprinting(false);
      break;
    
    case 5: 
      playerEntity.wakeUpPlayer(false, true, true);
      hasMoved = false;
      break;
    
    case 6: 
      if ((playerEntity.ridingEntity instanceof EntityHorse))
      {
        ((EntityHorse)playerEntity.ridingEntity).setJumpPower(packetIn.func_149512_e());
      }
      
      break;
    
    case 7: 
      if ((playerEntity.ridingEntity instanceof EntityHorse))
      {
        ((EntityHorse)playerEntity.ridingEntity).openGUI(playerEntity);
      }
      
      break;
    
    default: 
      throw new IllegalArgumentException("Invalid client command!");
    }
    
  }
  



  public void processUseEntity(C02PacketUseEntity packetIn)
  {
    PacketThreadUtil.func_180031_a(packetIn, this, playerEntity.getServerForPlayer());
    WorldServer var2 = serverController.worldServerForDimension(playerEntity.dimension);
    Entity var3 = packetIn.getEntityFromWorld(var2);
    playerEntity.markPlayerActive();
    
    if (var3 != null)
    {
      boolean var4 = playerEntity.canEntityBeSeen(var3);
      double var5 = 36.0D;
      
      if (!var4)
      {
        var5 = 9.0D;
      }
      
      if (playerEntity.getDistanceSqToEntity(var3) < var5)
      {
        if (packetIn.getAction() == C02PacketUseEntity.Action.INTERACT)
        {
          playerEntity.interactWith(var3);
        }
        else if (packetIn.getAction() == C02PacketUseEntity.Action.INTERACT_AT)
        {
          var3.func_174825_a(playerEntity, packetIn.func_179712_b());
        }
        else if (packetIn.getAction() == C02PacketUseEntity.Action.ATTACK)
        {
          if (((var3 instanceof EntityItem)) || ((var3 instanceof net.minecraft.entity.item.EntityXPOrb)) || ((var3 instanceof net.minecraft.entity.projectile.EntityArrow)) || (var3 == playerEntity))
          {
            kickPlayerFromServer("Attempting to attack an invalid entity");
            serverController.logWarning("Player " + playerEntity.getName() + " tried to attack an invalid entity");
            return;
          }
          
          playerEntity.attackTargetEntityWithCurrentItem(var3);
        }
      }
    }
  }
  




  public void processClientStatus(C16PacketClientStatus packetIn)
  {
    PacketThreadUtil.func_180031_a(packetIn, this, playerEntity.getServerForPlayer());
    playerEntity.markPlayerActive();
    C16PacketClientStatus.EnumState var2 = packetIn.getStatus();
    
    switch (SwitchAction.field_180223_c[var2.ordinal()])
    {
    case 1: 
      if (playerEntity.playerConqueredTheEnd)
      {
        playerEntity = serverController.getConfigurationManager().recreatePlayerEntity(playerEntity, 0, true);
      }
      else if (playerEntity.getServerForPlayer().getWorldInfo().isHardcoreModeEnabled())
      {
        if ((serverController.isSinglePlayer()) && (playerEntity.getName().equals(serverController.getServerOwner())))
        {
          playerEntity.playerNetServerHandler.kickPlayerFromServer("You have died. Game over, man, it's game over!");
          serverController.deleteWorldAndStopServer();
        }
        else
        {
          UserListBansEntry var3 = new UserListBansEntry(playerEntity.getGameProfile(), null, "(You just lost the game)", null, "Death in Hardcore");
          serverController.getConfigurationManager().getBannedPlayers().addEntry(var3);
          playerEntity.playerNetServerHandler.kickPlayerFromServer("You have died. Game over, man, it's game over!");
        }
      }
      else
      {
        if (playerEntity.getHealth() > 0.0F)
        {
          return;
        }
        
        playerEntity = serverController.getConfigurationManager().recreatePlayerEntity(playerEntity, 0, false);
      }
      
      break;
    
    case 2: 
      playerEntity.getStatFile().func_150876_a(playerEntity);
      break;
    
    case 3: 
      playerEntity.triggerAchievement(net.minecraft.stats.AchievementList.openInventory);
    }
    
  }
  


  public void processCloseWindow(C0DPacketCloseWindow packetIn)
  {
    PacketThreadUtil.func_180031_a(packetIn, this, playerEntity.getServerForPlayer());
    playerEntity.closeContainer();
  }
  





  public void processClickWindow(C0EPacketClickWindow packetIn)
  {
    PacketThreadUtil.func_180031_a(packetIn, this, playerEntity.getServerForPlayer());
    playerEntity.markPlayerActive();
    
    if ((playerEntity.openContainer.windowId == packetIn.getWindowId()) && (playerEntity.openContainer.getCanCraft(playerEntity)))
    {
      if (playerEntity.func_175149_v())
      {
        ArrayList var2 = Lists.newArrayList();
        
        for (int var3 = 0; var3 < playerEntity.openContainer.inventorySlots.size(); var3++)
        {
          var2.add(((Slot)playerEntity.openContainer.inventorySlots.get(var3)).getStack());
        }
        
        playerEntity.updateCraftingInventory(playerEntity.openContainer, var2);
      }
      else
      {
        ItemStack var5 = playerEntity.openContainer.slotClick(packetIn.getSlotId(), packetIn.getUsedButton(), packetIn.getMode(), playerEntity);
        
        if (ItemStack.areItemStacksEqual(packetIn.getClickedItem(), var5))
        {
          playerEntity.playerNetServerHandler.sendPacket(new S32PacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
          playerEntity.isChangingQuantityOnly = true;
          playerEntity.openContainer.detectAndSendChanges();
          playerEntity.updateHeldItem();
          playerEntity.isChangingQuantityOnly = false;
        }
        else
        {
          field_147372_n.addKey(playerEntity.openContainer.windowId, Short.valueOf(packetIn.getActionNumber()));
          playerEntity.playerNetServerHandler.sendPacket(new S32PacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), false));
          playerEntity.openContainer.setCanCraft(playerEntity, false);
          ArrayList var6 = Lists.newArrayList();
          
          for (int var4 = 0; var4 < playerEntity.openContainer.inventorySlots.size(); var4++)
          {
            var6.add(((Slot)playerEntity.openContainer.inventorySlots.get(var4)).getStack());
          }
          
          playerEntity.updateCraftingInventory(playerEntity.openContainer, var6);
        }
      }
    }
  }
  




  public void processEnchantItem(C11PacketEnchantItem packetIn)
  {
    PacketThreadUtil.func_180031_a(packetIn, this, playerEntity.getServerForPlayer());
    playerEntity.markPlayerActive();
    
    if ((playerEntity.openContainer.windowId == packetIn.getId()) && (playerEntity.openContainer.getCanCraft(playerEntity)) && (!playerEntity.func_175149_v()))
    {
      playerEntity.openContainer.enchantItem(playerEntity, packetIn.getButton());
      playerEntity.openContainer.detectAndSendChanges();
    }
  }
  



  public void processCreativeInventoryAction(C10PacketCreativeInventoryAction packetIn)
  {
    PacketThreadUtil.func_180031_a(packetIn, this, playerEntity.getServerForPlayer());
    
    if (playerEntity.theItemInWorldManager.isCreative())
    {
      boolean var2 = packetIn.getSlotId() < 0;
      ItemStack var3 = packetIn.getStack();
      
      if ((var3 != null) && (var3.hasTagCompound()) && (var3.getTagCompound().hasKey("BlockEntityTag", 10)))
      {
        NBTTagCompound var4 = var3.getTagCompound().getCompoundTag("BlockEntityTag");
        
        if ((var4.hasKey("x")) && (var4.hasKey("y")) && (var4.hasKey("z")))
        {
          BlockPos var5 = new BlockPos(var4.getInteger("x"), var4.getInteger("y"), var4.getInteger("z"));
          TileEntity var6 = playerEntity.worldObj.getTileEntity(var5);
          
          if (var6 != null)
          {
            NBTTagCompound var7 = new NBTTagCompound();
            var6.writeToNBT(var7);
            var7.removeTag("x");
            var7.removeTag("y");
            var7.removeTag("z");
            var3.setTagInfo("BlockEntityTag", var7);
          }
        }
      }
      
      boolean var8 = (packetIn.getSlotId() >= 1) && (packetIn.getSlotId() < 36 + InventoryPlayer.getHotbarSize());
      boolean var9 = (var3 == null) || (var3.getItem() != null);
      boolean var10 = (var3 == null) || ((var3.getMetadata() >= 0) && (stackSize <= 64) && (stackSize > 0));
      
      if ((var8) && (var9) && (var10))
      {
        if (var3 == null)
        {
          playerEntity.inventoryContainer.putStackInSlot(packetIn.getSlotId(), null);
        }
        else
        {
          playerEntity.inventoryContainer.putStackInSlot(packetIn.getSlotId(), var3);
        }
        
        playerEntity.inventoryContainer.setCanCraft(playerEntity, true);
      }
      else if ((var2) && (var9) && (var10) && (itemDropThreshold < 200))
      {
        itemDropThreshold += 20;
        EntityItem var11 = playerEntity.dropPlayerItemWithRandomChoice(var3, true);
        
        if (var11 != null)
        {
          var11.setAgeToCreativeDespawnTime();
        }
      }
    }
  }
  





  public void processConfirmTransaction(C0FPacketConfirmTransaction packetIn)
  {
    PacketThreadUtil.func_180031_a(packetIn, this, playerEntity.getServerForPlayer());
    Short var2 = (Short)field_147372_n.lookup(playerEntity.openContainer.windowId);
    
    if ((var2 != null) && (packetIn.getUid() == var2.shortValue()) && (playerEntity.openContainer.windowId == packetIn.getId()) && (!playerEntity.openContainer.getCanCraft(playerEntity)) && (!playerEntity.func_175149_v()))
    {
      playerEntity.openContainer.setCanCraft(playerEntity, true);
    }
  }
  
  public void processUpdateSign(C12PacketUpdateSign packetIn)
  {
    PacketThreadUtil.func_180031_a(packetIn, this, playerEntity.getServerForPlayer());
    playerEntity.markPlayerActive();
    WorldServer var2 = serverController.worldServerForDimension(playerEntity.dimension);
    BlockPos var3 = packetIn.func_179722_a();
    
    if (var2.isBlockLoaded(var3))
    {
      TileEntity var4 = var2.getTileEntity(var3);
      
      if (!(var4 instanceof TileEntitySign))
      {
        return;
      }
      
      TileEntitySign var5 = (TileEntitySign)var4;
      
      if ((!var5.getIsEditable()) || (var5.func_145911_b() != playerEntity))
      {
        serverController.logWarning("Player " + playerEntity.getName() + " just tried to change non-editable sign");
        return;
      }
      
      System.arraycopy(packetIn.func_180768_b(), 0, signText, 0, 4);
      var5.markDirty();
      var2.markBlockForUpdate(var3);
    }
  }
  



  public void processKeepAlive(C00PacketKeepAlive packetIn)
  {
    if (packetIn.getKey() == field_147378_h)
    {
      int var2 = (int)(currentTimeMillis() - lastPingTime);
      playerEntity.ping = ((playerEntity.ping * 3 + var2) / 4);
    }
  }
  
  private long currentTimeMillis()
  {
    return System.nanoTime() / 1000000L;
  }
  



  public void processPlayerAbilities(C13PacketPlayerAbilities packetIn)
  {
    PacketThreadUtil.func_180031_a(packetIn, this, playerEntity.getServerForPlayer());
    playerEntity.capabilities.isFlying = ((packetIn.isFlying()) && (playerEntity.capabilities.allowFlying));
  }
  



  public void processTabComplete(C14PacketTabComplete packetIn)
  {
    PacketThreadUtil.func_180031_a(packetIn, this, playerEntity.getServerForPlayer());
    ArrayList var2 = Lists.newArrayList();
    Iterator var3 = serverController.func_180506_a(playerEntity, packetIn.getMessage(), packetIn.func_179709_b()).iterator();
    
    while (var3.hasNext())
    {
      String var4 = (String)var3.next();
      var2.add(var4);
    }
    
    playerEntity.playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S3APacketTabComplete((String[])var2.toArray(new String[var2.size()])));
  }
  




  public void processClientSettings(C15PacketClientSettings packetIn)
  {
    PacketThreadUtil.func_180031_a(packetIn, this, playerEntity.getServerForPlayer());
    playerEntity.handleClientSettings(packetIn);
  }
  



  public void processVanilla250Packet(C17PacketCustomPayload packetIn)
  {
    PacketThreadUtil.func_180031_a(packetIn, this, playerEntity.getServerForPlayer());
    



    if ("MC|BEdit".equals(packetIn.getChannelName()))
    {
      PacketBuffer var2 = new PacketBuffer(Unpooled.wrappedBuffer(packetIn.getBufferData()));
      
      try
      {
        ItemStack var3 = var2.readItemStackFromBuffer();
        
        if (var3 == null)
        {
          return;
        }
        
        if (!ItemWritableBook.validBookPageTagContents(var3.getTagCompound()))
        {
          throw new IOException("Invalid book tag!");
        }
        
        ItemStack var4 = playerEntity.inventory.getCurrentItem();
        
        if (var4 != null)
        {
          if ((var3.getItem() == Items.writable_book) && (var3.getItem() == var4.getItem()))
          {
            var4.setTagInfo("pages", var3.getTagCompound().getTagList("pages", 8));
          }
          
          return;
        }
      }
      catch (Exception var38)
      {
        logger.error("Couldn't handle book info", var38);
        return;
      }
      finally
      {
        var2.release(); } ItemStack var4; ItemStack var3; var2.release();
      

      return;
    }
    if ("MC|BSign".equals(packetIn.getChannelName()))
    {
      PacketBuffer var2 = new PacketBuffer(Unpooled.wrappedBuffer(packetIn.getBufferData()));
      
      try
      {
        ItemStack var3 = var2.readItemStackFromBuffer();
        
        if (var3 != null)
        {
          if (!net.minecraft.item.ItemEditableBook.validBookTagContents(var3.getTagCompound()))
          {
            throw new IOException("Invalid book tag!");
          }
          
          ItemStack var4 = playerEntity.inventory.getCurrentItem();
          
          if (var4 == null)
          {
            return;
          }
          
          if ((var3.getItem() == Items.written_book) && (var4.getItem() == Items.writable_book))
          {
            var4.setTagInfo("author", new NBTTagString(playerEntity.getName()));
            var4.setTagInfo("title", new NBTTagString(var3.getTagCompound().getString("title")));
            var4.setTagInfo("pages", var3.getTagCompound().getTagList("pages", 8));
            var4.setItem(Items.written_book);
          }
          
          return;
        }
      }
      catch (Exception var36)
      {
        logger.error("Couldn't sign book", var36);
        return;
      }
      finally
      {
        var2.release(); } ItemStack var3; var2.release();
      

      return;
    }
    if ("MC|TrSel".equals(packetIn.getChannelName()))
    {
      try
      {
        int var40 = packetIn.getBufferData().readInt();
        Container var42 = playerEntity.openContainer;
        
        if (!(var42 instanceof ContainerMerchant))
          return;
        ((ContainerMerchant)var42).setCurrentRecipeIndex(var40);

      }
      catch (Exception var35)
      {
        logger.error("Couldn't select trade", var35);
      }
    }
    else if ("MC|AdvCdm".equals(packetIn.getChannelName()))
    {
      if (!serverController.isCommandBlockEnabled())
      {
        playerEntity.addChatMessage(new ChatComponentTranslation("advMode.notEnabled", new Object[0]));
      }
      else if ((playerEntity.canCommandSenderUseCommand(2, "")) && (playerEntity.capabilities.isCreativeMode))
      {
        PacketBuffer var2 = packetIn.getBufferData();
        
        try
        {
          byte var43 = var2.readByte();
          CommandBlockLogic var46 = null;
          
          if (var43 == 0)
          {
            TileEntity var5 = playerEntity.worldObj.getTileEntity(new BlockPos(var2.readInt(), var2.readInt(), var2.readInt()));
            
            if ((var5 instanceof TileEntityCommandBlock))
            {
              var46 = ((TileEntityCommandBlock)var5).getCommandBlockLogic();
            }
          }
          else if (var43 == 1)
          {
            Entity var48 = playerEntity.worldObj.getEntityByID(var2.readInt());
            
            if ((var48 instanceof EntityMinecartCommandBlock))
            {
              var46 = ((EntityMinecartCommandBlock)var48).func_145822_e();
            }
          }
          
          String var49 = var2.readStringFromBuffer(var2.readableBytes());
          boolean var6 = var2.readBoolean();
          
          if (var46 != null)
          {
            var46.setCommand(var49);
            var46.func_175573_a(var6);
            
            if (!var6)
            {
              var46.func_145750_b(null);
            }
            
            var46.func_145756_e();
            playerEntity.addChatMessage(new ChatComponentTranslation("advMode.setCommand.success", new Object[] { var49 }));
          }
        }
        catch (Exception var33)
        {
          logger.error("Couldn't set command block", var33);
        }
        finally
        {
          var2.release();
        }
      }
      else
      {
        playerEntity.addChatMessage(new ChatComponentTranslation("advMode.notAllowed", new Object[0]));
      }
    }
    else if ("MC|Beacon".equals(packetIn.getChannelName()))
    {
      if ((playerEntity.openContainer instanceof ContainerBeacon))
      {
        try
        {
          PacketBuffer var2 = packetIn.getBufferData();
          int var44 = var2.readInt();
          int var47 = var2.readInt();
          ContainerBeacon var50 = (ContainerBeacon)playerEntity.openContainer;
          Slot var51 = var50.getSlot(0);
          
          if (!var51.getHasStack())
            return;
          var51.decrStackSize(1);
          IInventory var7 = var50.func_180611_e();
          var7.setField(1, var44);
          var7.setField(2, var47);
          var7.markDirty();

        }
        catch (Exception var32)
        {
          logger.error("Couldn't set beacon", var32);
        }
      }
    }
    else if (("MC|ItemName".equals(packetIn.getChannelName())) && ((playerEntity.openContainer instanceof ContainerRepair)))
    {
      ContainerRepair var41 = (ContainerRepair)playerEntity.openContainer;
      
      if ((packetIn.getBufferData() != null) && (packetIn.getBufferData().readableBytes() >= 1))
      {
        String var45 = ChatAllowedCharacters.filterAllowedCharacters(packetIn.getBufferData().readStringFromBuffer(32767));
        
        if (var45.length() <= 30)
        {
          var41.updateItemName(var45);
        }
      }
      else
      {
        var41.updateItemName("");
      }
    }
  }
  

  static final class SwitchAction
  {
    static final int[] field_180224_a;
    
    static final int[] field_180222_b;
    static final int[] field_180223_c = new int[C16PacketClientStatus.EnumState.values().length];
    private static final String __OBFID = "CL_00002269";
    
    static
    {
      try
      {
        field_180223_c[C16PacketClientStatus.EnumState.PERFORM_RESPAWN.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      



      try
      {
        field_180223_c[C16PacketClientStatus.EnumState.REQUEST_STATS.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      



      try
      {
        field_180223_c[C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      



      field_180222_b = new int[net.minecraft.network.play.client.C0BPacketEntityAction.Action.values().length];
      
      try
      {
        field_180222_b[net.minecraft.network.play.client.C0BPacketEntityAction.Action.START_SNEAKING.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
      



      try
      {
        field_180222_b[net.minecraft.network.play.client.C0BPacketEntityAction.Action.STOP_SNEAKING.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError5) {}
      



      try
      {
        field_180222_b[net.minecraft.network.play.client.C0BPacketEntityAction.Action.START_SPRINTING.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError6) {}
      



      try
      {
        field_180222_b[net.minecraft.network.play.client.C0BPacketEntityAction.Action.STOP_SPRINTING.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError7) {}
      



      try
      {
        field_180222_b[net.minecraft.network.play.client.C0BPacketEntityAction.Action.STOP_SLEEPING.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError8) {}
      



      try
      {
        field_180222_b[net.minecraft.network.play.client.C0BPacketEntityAction.Action.RIDING_JUMP.ordinal()] = 6;
      }
      catch (NoSuchFieldError localNoSuchFieldError9) {}
      



      try
      {
        field_180222_b[net.minecraft.network.play.client.C0BPacketEntityAction.Action.OPEN_INVENTORY.ordinal()] = 7;
      }
      catch (NoSuchFieldError localNoSuchFieldError10) {}
      



      field_180224_a = new int[C07PacketPlayerDigging.Action.values().length];
      
      try
      {
        field_180224_a[C07PacketPlayerDigging.Action.DROP_ITEM.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError11) {}
      



      try
      {
        field_180224_a[C07PacketPlayerDigging.Action.DROP_ALL_ITEMS.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError12) {}
      



      try
      {
        field_180224_a[C07PacketPlayerDigging.Action.RELEASE_USE_ITEM.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError13) {}
      



      try
      {
        field_180224_a[C07PacketPlayerDigging.Action.START_DESTROY_BLOCK.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError14) {}
      



      try
      {
        field_180224_a[C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError15) {}
      



      try
      {
        field_180224_a[C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK.ordinal()] = 6;
      }
      catch (NoSuchFieldError localNoSuchFieldError16) {}
    }
    
    SwitchAction() {}
  }
}
