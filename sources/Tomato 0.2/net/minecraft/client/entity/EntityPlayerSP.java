package net.minecraft.client.entity;


import java.util.Iterator;

import com.darkmagician6.eventapi.EventManager;

import cow.milkgod.cheese.Cheese;
import cow.milkgod.cheese.commands.Command;
import cow.milkgod.cheese.commands.CommandManager;
import cow.milkgod.cheese.commands.commands.Prefix;
import cow.milkgod.cheese.events.EventChatSend;
import cow.milkgod.cheese.events.EventPostMotionUpdates;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.events.EventPushOutOfBlock;
import cow.milkgod.cheese.events.EventRotation;
import cow.milkgod.cheese.events.MoveEvent;
import cow.milkgod.cheese.events.MovementInputEvent;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.module.ModuleManager;
import cow.milkgod.cheese.module.modules.Regen;
import cow.milkgod.cheese.people.FriendManager;
import cow.milkgod.cheese.people.FriendManager.Friend;
import cow.milkgod.cheese.utils.Logger;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

public class EntityPlayerSP extends AbstractClientPlayer
{
    public final NetHandlerPlayClient sendQueue;
    private final StatFileWriter field_146108_bO;
    private double field_175172_bI;
    private double field_175166_bJ;
    private double field_175167_bK;
    private float field_175164_bL;
    private float field_175165_bM;
    private boolean field_175170_bN;
    private boolean field_175171_bO;
    private int field_175168_bP;
    private boolean field_175169_bQ;
    private String clientBrand;
    public MovementInput movementInput;
    protected Minecraft mc;
    public static boolean executor = true;

    /**
     * Used to tell if the player pressed forward twice. If this is at 0 and it's pressed (And they are allowed to
     * sprint, aka enough food on the ground etc) it sets this to 7. If it's pressed and it's greater than 0 enable
     * sprinting.
     */
    protected int sprintToggleTimer;

    /** Ticks left before sprinting is disabled. */
    public int sprintingTicksLeft;
    public float renderArmYaw;
    public float renderArmPitch;
    public float prevRenderArmYaw;
    public float prevRenderArmPitch;
    private int horseJumpPowerCounter;
    private float horseJumpPower;

    /** The amount of time an entity has been in a Portal */
    public float timeInPortal;

    /** The amount of time an entity has been in a Portal the previous tick */
    public float prevTimeInPortal;
    private static final String __OBFID = "CL_00000938";

    public EntityPlayerSP(Minecraft mcIn, World worldIn, NetHandlerPlayClient p_i46278_3_, StatFileWriter p_i46278_4_)
    {
        super(worldIn, p_i46278_3_.func_175105_e());
        this.sendQueue = p_i46278_3_;
        this.field_146108_bO = p_i46278_4_;
        this.mc = mcIn;
        this.dimension = 0;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return false;
    }
    
    /**
     * Heal living entity (param: amount of half-hearts)
     */
    public void heal(float p_70691_1_) {}

    /**
     * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
     */
    public void mountEntity(Entity entityIn)
    {
        super.mountEntity(entityIn);

        if (entityIn instanceof EntityMinecart)
        {
            this.mc.getSoundHandler().playSound(new MovingSoundMinecartRiding(this, (EntityMinecart)entityIn));
        }
    }
    
    public void moveEntity(double x, double y, double z)
    {
      MoveEvent event = new MoveEvent(x, y, z);
      EventManager.call(event);
      super.moveEntity(event.x, event.y, event.z);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
    	if (this.worldObj.isBlockLoaded(new BlockPos(this.posX, 0.0D, this.posZ)))
        {
    	    Cheese.getInstance();
    	    for (Module m : ModuleManager.activeModules) {
    	      m.onUpdate();
    	    }
            super.onUpdate();

            if (this.isRiding())
            {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
                this.sendQueue.addToSendQueue(new C0CPacketInput(this.moveStrafing, this.moveForward, this.movementInput.jump, this.movementInput.sneak));
            }
            else
            {
                this.func_175161_p();
                EventPostMotionUpdates postMotion = new EventPostMotionUpdates();
                EventManager.call(postMotion);
            }
        }
    }
    public void func_175161_p222() {
        final EventPreMotionUpdates event = new EventPreMotionUpdates(this.posX, this.getEntityBoundingBox().minY, this.posZ, this.field_175172_bI, this.field_175166_bJ, this.field_175167_bK, this.rotationYaw, this.rotationPitch, this.field_175164_bL, this.field_175165_bM, this.isSprinting(), this.field_175171_bO, this.isSneaking(), this.field_175170_bN, this.onGround);
        EventManager.call(event);
        if (event.isCancelled()) {
            return;
        }
        final double originalX = this.posX;
        final double originalZ = this.posZ;
        final float originalYaw = this.rotationYaw;
        final float originalPitch = this.rotationPitch;
        final boolean originalOnGround = this.onGround;
        this.posX = event.getX();
        final double y = event.getY();
        this.posZ = event.getZ();
        this.rotationYaw = event.getYaw();
        this.rotationPitch = event.getPitch();
        this.onGround = event.isOnGround();
        final boolean var1 = this.isSprinting();
        if (var1 != this.field_175171_bO) {
            if (var1) {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SPRINTING));
            }
            else {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }
            this.field_175171_bO = var1;
        }
        final boolean var2 = event.isSneaking();
        if (var2 != this.field_175170_bN) {
            if (var2) {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SNEAKING));
            }
            else {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
            this.field_175170_bN = var2;
        }
        if (this.func_175160_A()) {
            final boolean pls = true;
            final double var3 = this.posX - this.field_175172_bI;
            final double var4 = this.getEntityBoundingBox().minY - this.field_175166_bJ;
            final double var5 = this.posZ - this.field_175167_bK;
            final double var6 = this.rotationYaw - this.field_175164_bL;
            final double var7 = this.rotationPitch - this.field_175165_bM;
            boolean var8 = var3 * var3 + var4 * var4 + var5 * var5 > 9.0E-4 || this.field_175168_bP >= 20;
            final boolean var9 = var6 != 0.0 || var7 != 0.0;
            boolean b = false;
            Label_0482: {
                if (Regen.packets.value <= 10) {
                    Cheese.getInstance();
                    if (Cheese.moduleManager.getModState("Regen") && this.mc.thePlayer.getHealth() < this.mc.thePlayer.getMaxHealth()) {
                        b = true;
                        break Label_0482;
                    }
                }
                b = false;
            }
            final boolean regen = b;
            final boolean onGround = this.onGround;
            final EventRotation rotate = new EventRotation();
            EventManager.call(rotate);
            if (this.ridingEntity == null) {
                if (Wrapper.isOnLiquid()) {
                    this.mc.thePlayer.isImmuneToFire = true;
                }
                if (var8 && var9) {
                    if (rotate.isCancelled()) {
                        return;
                    }
                    if (rotate.getYaw() != 0.0f || rotate.getPitch() != 0.0f) {
                        this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.posX, this.getEntityBoundingBox().minY, this.posZ, rotate.getYaw(), rotate.getPitch(), onGround));
                        if (this.mc.thePlayer.ticksExisted % Regen.packets.value == 0 && regen) {
                            this.sendQueue.addToSendQueue(new C03PacketPlayer());
                        }
                    }
                    else {
                        this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch, onGround));
                        if (this.mc.thePlayer.ticksExisted % Regen.packets.value == 0 && regen) {
                            this.sendQueue.addToSendQueue(new C03PacketPlayer());
                        }
                    }
                }
                else if (var8) {
                    if (this.mc.thePlayer.ticksExisted % Regen.packets.value == 0 && regen && !var9) {
                        this.sendQueue.addToSendQueue(new C03PacketPlayer());
                    }
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.posX, this.getEntityBoundingBox().minY, this.posZ, onGround));
                }
                else if (var9) {
                    if (rotate.isCancelled()) {
                        return;
                    }
                    if (rotate.getYaw() != 0.0f || rotate.getPitch() != 0.0f) {
                        this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(rotate.getYaw(), rotate.getYaw(), onGround));
                        if (this.mc.thePlayer.ticksExisted % Regen.packets.value == 0 && regen && !var8) {
                            this.sendQueue.addToSendQueue(new C03PacketPlayer());
                        }
                    }
                    else {
                        this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, onGround));
                        if (this.mc.thePlayer.ticksExisted % Regen.packets.value == 0 && regen && !var8) {
                            this.sendQueue.addToSendQueue(new C03PacketPlayer());
                        }
                    }
                }
                else {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer(onGround));
                    if (this.mc.thePlayer.ticksExisted % Regen.packets.value == 0 && regen) {
                        this.sendQueue.addToSendQueue(new C03PacketPlayer(onGround));
                    }
                    if (this.mc.thePlayer.ticksExisted % (Regen.packets.value * 4) == 0 && regen) {
                        this.sendQueue.addToSendQueue(new C03PacketPlayer(onGround));
                    }
                    if (this.mc.thePlayer.ticksExisted % (Regen.packets.value * 8) == 0 && regen) {
                        this.sendQueue.addToSendQueue(new C03PacketPlayer(onGround));
                    }
                    if (this.mc.thePlayer.ticksExisted % (Regen.packets.value * 12) == 0 && regen) {
                        this.sendQueue.addToSendQueue(new C03PacketPlayer(onGround));
                    }
                }
            }
            else {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0, this.motionZ, this.rotationYaw, this.rotationPitch, onGround));
                var8 = false;
            }
            ++this.field_175168_bP;
            if (var8) {
                this.field_175172_bI = this.posX;
                this.field_175166_bJ = this.getEntityBoundingBox().minY;
                this.field_175167_bK = this.posZ;
                this.field_175168_bP = 0;
            }
            if (var9) {
                this.field_175164_bL = this.rotationYaw;
                this.field_175165_bM = this.rotationPitch;
            }
        }
    }

    public void func_175161_p()
    {
      EventPreMotionUpdates event = new EventPreMotionUpdates(this.posX, getEntityBoundingBox().minY, this.posZ, this.field_175172_bI, this.field_175166_bJ, this.field_175167_bK, this.rotationYaw, this.rotationPitch, this.field_175164_bL, this.field_175165_bM, isSprinting(), this.field_175171_bO, isSneaking(), this.field_175170_bN, this.onGround);
      
      EventManager.call(event);
      if (event.isCancelled()) {
        return;
      } 
      double originalX = this.posX;
      double originalZ = this.posZ;
      float originalYaw = this.rotationYaw;
      float originalPitch = this.rotationPitch;
      boolean originalOnGround = this.onGround;
      this.posX = event.getX();
      double y = event.getY();
      this.posZ = event.getZ();
      this.rotationYaw = event.getYaw();
      this.rotationPitch = event.getPitch();
      this.onGround = event.isOnGround();
      boolean var1 = isSprinting();
      if (var1 != this.field_175171_bO)
      {
        if (var1) {
          this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SPRINTING));
        } else {
          this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SPRINTING));
        }
        this.field_175171_bO = var1;
      }
      boolean var2 = event.isSneaking();
      if (var2 != this.field_175170_bN)
      {
        if (var2) {
          this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SNEAKING));
        } else {
          this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
        this.field_175170_bN = var2;
      }
      if (func_175160_A())
      {
        boolean pls = true;
        double var3 = this.posX - this.field_175172_bI;
        double var4 = getEntityBoundingBox().minY - this.field_175166_bJ;
        double var5 = this.posZ - this.field_175167_bK;
        double var6 = this.rotationYaw - this.field_175164_bL;
        double var7 = this.rotationPitch - this.field_175165_bM;
        boolean var8 = (var3 * var3 + var4 * var4 + var5 * var5 > 9.0E-4D) || (this.field_175168_bP >= 20);
        boolean var9 = (var6 != 0.0D) || (var7 != 0.0D);
        if (((Integer)Regen.packets.value).intValue() <= 10) {
          Cheese.getInstance();
        }
        boolean regen = (Cheese.moduleManager.getModState("Regen")) && (this.mc.thePlayer.getHealth() < this.mc.thePlayer.getMaxHealth());
        boolean onGround = this.onGround;
        EventRotation rotate = new EventRotation();
        EventManager.call(rotate);
        if (this.ridingEntity == null)
        {
          if (Wrapper.isOnLiquid()) {
            this.mc.thePlayer.isImmuneToFire = true;
          }
          if ((var8) && (var9))
          {
            if (rotate.isCancelled()) {
              return;
            }
            if ((rotate.getYaw() != 0.0F) || (rotate.getPitch() != 0.0F))
            {
              this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.posX, getEntityBoundingBox().minY, this.posZ, rotate.getYaw(), rotate.getPitch(), onGround));
              if ((this.mc.thePlayer.ticksExisted % ((Integer)Regen.packets.value).intValue() == 0) && (regen)) {
                this.sendQueue.addToSendQueue(new C03PacketPlayer());
              }
            }
            else
            {
              this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch, onGround));
              if ((this.mc.thePlayer.ticksExisted % ((Integer)Regen.packets.value).intValue() == 0) && (regen)) {
                this.sendQueue.addToSendQueue(new C03PacketPlayer());
              }
            }
          }
          else if (var8)
          {
            if ((this.mc.thePlayer.ticksExisted % ((Integer)Regen.packets.value).intValue() == 0) && (regen) && (!var9)) {
              this.sendQueue.addToSendQueue(new C03PacketPlayer());
            }
            this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.posX, getEntityBoundingBox().minY, this.posZ, onGround));
          }
          else if (var9)
          {
            if (rotate.isCancelled()) {
              return;
            }
            if ((rotate.getYaw() != 0.0F) || (rotate.getPitch() != 0.0F))
            {
              this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(rotate.getYaw(), rotate.getYaw(), onGround));
              if ((this.mc.thePlayer.ticksExisted % ((Integer)Regen.packets.value).intValue() == 0) && (regen) && (!var8)) {
                this.sendQueue.addToSendQueue(new C03PacketPlayer());
              }
            }
            else
            {
              this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, onGround));
              if ((this.mc.thePlayer.ticksExisted % ((Integer)Regen.packets.value).intValue() == 0) && (regen) && (!var8)) {
                this.sendQueue.addToSendQueue(new C03PacketPlayer());
              }
            }
          }
          else
          {
            this.sendQueue.addToSendQueue(new C03PacketPlayer(onGround));
            if ((this.mc.thePlayer.ticksExisted % ((Integer)Regen.packets.value).intValue() == 0) && (regen)) {
              this.sendQueue.addToSendQueue(new C03PacketPlayer(onGround));
            }
            if ((this.mc.thePlayer.ticksExisted % (((Integer)Regen.packets.value).intValue() * 4) == 0) && (regen)) {
              this.sendQueue.addToSendQueue(new C03PacketPlayer(onGround));
            }
            if ((this.mc.thePlayer.ticksExisted % (((Integer)Regen.packets.value).intValue() * 8) == 0) && (regen)) {
              this.sendQueue.addToSendQueue(new C03PacketPlayer(onGround));
            }
            if ((this.mc.thePlayer.ticksExisted % (((Integer)Regen.packets.value).intValue() * 12) == 0) && (regen)) {
              this.sendQueue.addToSendQueue(new C03PacketPlayer(onGround));
            }
          }
        }
        else
        {
          this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0D, this.motionZ, this.rotationYaw, this.rotationPitch, onGround));
          var8 = false;
        }
        this.field_175168_bP += 1;
        if (var8)
        {
          this.field_175172_bI = this.posX;
          this.field_175166_bJ = getEntityBoundingBox().minY;
          this.field_175167_bK = this.posZ;
          this.field_175168_bP = 0;
        }
        if (var9)
        {
          this.field_175164_bL = this.rotationYaw;
          this.field_175165_bM = this.rotationPitch;
        }
      }
    }

    /**
     * Called when player presses the drop item key
     */
    public EntityItem dropOneItem(boolean p_71040_1_)
    {
        C07PacketPlayerDigging.Action var2 = p_71040_1_ ? C07PacketPlayerDigging.Action.DROP_ALL_ITEMS : C07PacketPlayerDigging.Action.DROP_ITEM;
        this.sendQueue.addToSendQueue(new C07PacketPlayerDigging(var2, BlockPos.ORIGIN, EnumFacing.DOWN));
        return null;
    }

    /**
     * Joins the passed in entity item with the world. Args: entityItem
     */
    protected void joinEntityItemWithWorld(EntityItem p_71012_1_) {}

    /**
     * Sends a chat message from the player. Args: chatMessage
     */
    public void sendChatMessage(String p_71165_1_) {
        EventChatSend var11 = new EventChatSend();
        EventManager.call(var11);
        var11.setMessage(p_71165_1_);
        String[] var12 = EventChatSend.getMessage().split(" ");
        if(p_71165_1_.startsWith((String)Prefix.prefix.value)) {
           executor = true;
           Iterator var5 = CommandManager.activeCommands.iterator();

           while(true) {
              while(var5.hasNext()) {
                 Command cmd = (Command)var5.next();
                 if(var12[0].equalsIgnoreCase(String.valueOf(Prefix.prefix.value) + cmd.getName())) {
                    cmd.Toggle();
                    Cheese.getInstance();
                    Cheese.fileManager.saveFiles();
                    executor = false;
                 } else {
                    try {
                       String[] alias2;
                       int length = (alias2 = cmd.getAlias()).length;

                       for(int i = 0; i < length; ++i) {
                          String alias = alias2[i];
                          if(var12[0].equalsIgnoreCase(String.valueOf(Prefix.prefix.value) + alias)) {
                             cmd.Toggle();
                             Cheese.getInstance();
                             Cheese.fileManager.saveFiles();
                             executor = false;
                          }
                       }
                    } catch (Exception var10) {
                       ;
                    }
                 }
              }

              if(executor) {
                 Logger.logChat("Invalid command!");
              }
              break;
           }
        } else {
           this.sendQueue.addToSendQueue(new C01PacketChatMessage(p_71165_1_));
        }

     }



    /**
     * Swings the item the player is holding.
     */
    public void swingItem()
    {
        super.swingItem();
        this.sendQueue.addToSendQueue(new C0APacketAnimation());
    }

    public void respawnPlayer()
    {
        this.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
    }

    /**
     * Deals damage to the entity. If its a EntityPlayer then will take damage from the armor first and then health
     * second with the reduced value. Args: damageAmount
     */
    protected void damageEntity(DamageSource p_70665_1_, float p_70665_2_)
    {
        if (!this.func_180431_b(p_70665_1_))
        {
            this.setHealth(this.getHealth() - p_70665_2_);
        }
    }

    /**
     * set current crafting inventory back to the 2x2 square
     */
    public void closeScreen()
    {
        this.sendQueue.addToSendQueue(new C0DPacketCloseWindow(this.openContainer.windowId));
        this.func_175159_q();
    }

    public void func_175159_q()
    {
        this.inventory.setItemStack((ItemStack)null);
        super.closeScreen();
        this.mc.displayGuiScreen((GuiScreen)null);
    }

    /**
     * Updates health locally.
     */
    public void setPlayerSPHealth(float p_71150_1_)
    {
        if (this.field_175169_bQ)
        {
            float var2 = this.getHealth() - p_71150_1_;

            if (var2 <= 0.0F)
            {
                this.setHealth(p_71150_1_);

                if (var2 < 0.0F)
                {
                    this.hurtResistantTime = this.maxHurtResistantTime / 2;
                }
            }
            else
            {
                this.lastDamage = var2;
                this.setHealth(this.getHealth());
                this.hurtResistantTime = this.maxHurtResistantTime;
                this.damageEntity(DamageSource.generic, var2);
                this.hurtTime = this.maxHurtTime = 10;
            }
        }
        else
        {
            this.setHealth(p_71150_1_);
            this.field_175169_bQ = true;
        }
    }

    /**
     * Adds a value to a statistic field.
     */
    public void addStat(StatBase p_71064_1_, int p_71064_2_)
    {
        if (p_71064_1_ != null)
        {
            if (p_71064_1_.isIndependent)
            {
                super.addStat(p_71064_1_, p_71064_2_);
            }
        }
    }

    /**
     * Sends the player's abilities to the server (if there is one).
     */
    public void sendPlayerAbilities()
    {
        this.sendQueue.addToSendQueue(new C13PacketPlayerAbilities(this.capabilities));
    }

    public boolean func_175144_cb()
    {
        return true;
    }

    protected void sendHorseJump()
    {
        this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.RIDING_JUMP, (int)(this.getHorseJumpPower() * 100.0F)));
    }

    public void func_175163_u()
    {
        this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.OPEN_INVENTORY));
    }

    public void func_175158_f(String p_175158_1_)
    {
        this.clientBrand = p_175158_1_;
    }

    public String getClientBrand()
    {
        return this.clientBrand;
    }

    public StatFileWriter getStatFileWriter()
    {
        return this.field_146108_bO;
    }

    public void addChatComponentMessage(IChatComponent p_146105_1_)
    {
        this.mc.ingameGUI.getChatGUI().printChatMessage(p_146105_1_);
    }

    protected boolean pushOutOfBlocks(double x, double y, double z)
    {
        EventPushOutOfBlock blockPush = new EventPushOutOfBlock();
        EventManager.call(blockPush);
        if (!this.noClip)
        {
          Cheese.getInstance();
          if (!Cheese.moduleManager.getModState("CheeseClip")) {}
        }
        else
        {
          return false;
        }
            BlockPos var7 = new BlockPos(x, y, z);
            double var8 = x - (double)var7.getX();
            double var10 = z - (double)var7.getZ();

            if (!this.func_175162_d(var7))
            {
                byte var12 = -1;
                double var13 = 9999.0D;

                if (this.func_175162_d(var7.offsetWest()) && var8 < var13)
                {
                    var13 = var8;
                    var12 = 0;
                }

                if (this.func_175162_d(var7.offsetEast()) && 1.0D - var8 < var13)
                {
                    var13 = 1.0D - var8;
                    var12 = 1;
                }

                if (this.func_175162_d(var7.offsetNorth()) && var10 < var13)
                {
                    var13 = var10;
                    var12 = 4;
                }

                if (this.func_175162_d(var7.offsetSouth()) && 1.0D - var10 < var13)
                {
                    var13 = 1.0D - var10;
                    var12 = 5;
                }

                float var15 = 0.1F;

                if (var12 == 0)
                {
                    this.motionX = (double)(-var15);
                }

                if (var12 == 1)
                {
                    this.motionX = (double)var15;
                }

                if (var12 == 4)
                {
                    this.motionZ = (double)(-var15);
                }

                if (var12 == 5)
                {
                    this.motionZ = (double)var15;
                }
            }

            return false;
        }

    private boolean func_175162_d(BlockPos p_175162_1_)
    {
        return !this.worldObj.getBlockState(p_175162_1_).getBlock().isNormalCube() && !this.worldObj.getBlockState(p_175162_1_.offsetUp()).getBlock().isNormalCube();
    }

    /**
     * Set sprinting switch for Entity.
     */
    public void setSprinting(boolean sprinting)
    {
        super.setSprinting(sprinting);
        this.sprintingTicksLeft = sprinting ? 600 : 0;
    }

    /**
     * Sets the current XP, total XP, and level number.
     */
    public void setXPStats(float p_71152_1_, int p_71152_2_, int p_71152_3_)
    {
        this.experience = p_71152_1_;
        this.experienceTotal = p_71152_2_;
        this.experienceLevel = p_71152_3_;
    }

    /**
     * Notifies this sender of some sort of information.  This is for messages intended to display to the user.  Used
     * for typical output (like "you asked for whether or not this game rule is set, so here's your answer"), warnings
     * (like "I fetched this block for you by ID, but I'd like you to know that every time you do this, I die a little
     * inside"), and errors (like "it's not called iron_pixacke, silly").
     */
    public void addChatMessage(IChatComponent message)
    {
        this.mc.ingameGUI.getChatGUI().printChatMessage(message);
    }

    /**
     * Returns true if the command sender is allowed to use the given command.
     */
    public boolean canCommandSenderUseCommand(int permissionLevel, String command)
    {
        return permissionLevel <= 0;
    }

    public BlockPos getPosition()
    {
        return new BlockPos(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D);
    }

    public void playSound(String name, float volume, float pitch)
    {
        this.worldObj.playSound(this.posX, this.posY, this.posZ, name, volume, pitch, false);
    }

    /**
     * Returns whether the entity is in a server world
     */
    public boolean isServerWorld()
    {
        return true;
    }

    public boolean isRidingHorse()
    {
        return this.ridingEntity != null && this.ridingEntity instanceof EntityHorse && ((EntityHorse)this.ridingEntity).isHorseSaddled();
    }

    public float getHorseJumpPower()
    {
        return this.horseJumpPower;
    }

    public void func_175141_a(TileEntitySign p_175141_1_)
    {
        this.mc.displayGuiScreen(new GuiEditSign(p_175141_1_));
    }

    public void func_146095_a(CommandBlockLogic p_146095_1_)
    {
        this.mc.displayGuiScreen(new GuiCommandBlock(p_146095_1_));
    }

    /**
     * Displays the GUI for interacting with a book.
     */
    public void displayGUIBook(ItemStack bookStack)
    {
        Item var2 = bookStack.getItem();

        if (var2 == Items.writable_book)
        {
            this.mc.displayGuiScreen(new GuiScreenBook(this, bookStack, true));
        }
    }

    /**
     * Displays the GUI for interacting with a chest inventory. Args: chestInventory
     */
    public void displayGUIChest(IInventory chestInventory)
    {
        String var2 = chestInventory instanceof IInteractionObject ? ((IInteractionObject)chestInventory).getGuiID() : "minecraft:container";

        if ("minecraft:chest".equals(var2))
        {
            this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
        }
        else if ("minecraft:hopper".equals(var2))
        {
            this.mc.displayGuiScreen(new GuiHopper(this.inventory, chestInventory));
        }
        else if ("minecraft:furnace".equals(var2))
        {
            this.mc.displayGuiScreen(new GuiFurnace(this.inventory, chestInventory));
        }
        else if ("minecraft:brewing_stand".equals(var2))
        {
            this.mc.displayGuiScreen(new GuiBrewingStand(this.inventory, chestInventory));
        }
        else if ("minecraft:beacon".equals(var2))
        {
            this.mc.displayGuiScreen(new GuiBeacon(this.inventory, chestInventory));
        }
        else if (!"minecraft:dispenser".equals(var2) && !"minecraft:dropper".equals(var2))
        {
            this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
        }
        else
        {
            this.mc.displayGuiScreen(new GuiDispenser(this.inventory, chestInventory));
        }
    }

    public void displayGUIHorse(EntityHorse p_110298_1_, IInventory p_110298_2_)
    {
        this.mc.displayGuiScreen(new GuiScreenHorseInventory(this.inventory, p_110298_2_, p_110298_1_));
    }

    public void displayGui(IInteractionObject guiOwner)
    {
        String var2 = guiOwner.getGuiID();

        if ("minecraft:crafting_table".equals(var2))
        {
            this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.worldObj));
        }
        else if ("minecraft:enchanting_table".equals(var2))
        {
            this.mc.displayGuiScreen(new GuiEnchantment(this.inventory, this.worldObj, guiOwner));
        }
        else if ("minecraft:anvil".equals(var2))
        {
            this.mc.displayGuiScreen(new GuiRepair(this.inventory, this.worldObj));
        }
    }

    public void displayVillagerTradeGui(IMerchant villager)
    {
        this.mc.displayGuiScreen(new GuiMerchant(this.inventory, villager, this.worldObj));
    }

    /**
     * Called when the player performs a critical hit on the Entity. Args: entity that was hit critically
     */
    public void onCriticalHit(Entity p_71009_1_)
    {
        this.mc.effectRenderer.func_178926_a(p_71009_1_, EnumParticleTypes.CRIT);
    }

    public void onEnchantmentCritical(Entity p_71047_1_)
    {
        this.mc.effectRenderer.func_178926_a(p_71047_1_, EnumParticleTypes.CRIT_MAGIC);
    }

    /**
     * Returns if this entity is sneaking.
     */
    public boolean isSneaking()
    {
        boolean var1 = this.movementInput != null ? this.movementInput.sneak : false;
        return var1 && !this.sleeping;
    }

    public void updateEntityActionState()
    {
        super.updateEntityActionState();

        if (this.func_175160_A())
        {
            this.moveStrafing = this.movementInput.moveStrafe;
            this.moveForward = this.movementInput.moveForward;
            this.isJumping = this.movementInput.jump;
            this.prevRenderArmYaw = this.renderArmYaw;
            this.prevRenderArmPitch = this.renderArmPitch;
            this.renderArmPitch = (float)((double)this.renderArmPitch + (double)(this.rotationPitch - this.renderArmPitch) * 0.5D);
            this.renderArmYaw = (float)((double)this.renderArmYaw + (double)(this.rotationYaw - this.renderArmYaw) * 0.5D);
        }
    }

    protected boolean func_175160_A()
    {
        return this.mc.func_175606_aa() == this;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        if (this.sprintingTicksLeft > 0) {
            --this.sprintingTicksLeft;
            if (this.sprintingTicksLeft == 0) {
                this.setSprinting(false);
            }
        }
        if (this.sprintToggleTimer > 0) {
            --this.sprintToggleTimer;
        }
        this.prevTimeInPortal = this.timeInPortal;
        if (this.inPortal) {
            if (this.mc.currentScreen != null && !this.mc.currentScreen.doesGuiPauseGame()) {
                this.mc.displayGuiScreen(null);
            }
            if (this.timeInPortal == 0.0f) {
                this.mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("portal.trigger"), this.rand.nextFloat() * 0.4f + 0.8f));
            }
            this.timeInPortal += 0.0125f;
            if (this.timeInPortal >= 1.0f) {
                this.timeInPortal = 1.0f;
            }
            this.inPortal = false;
        }
        else if (this.isPotionActive(Potion.confusion) && this.getActivePotionEffect(Potion.confusion).getDuration() > 60) {
            this.timeInPortal += 0.006666667f;
            if (this.timeInPortal > 1.0f) {
                this.timeInPortal = 1.0f;
            }
        }
        else {
            if (this.timeInPortal > 0.0f) {
                this.timeInPortal -= 0.05f;
            }
            if (this.timeInPortal < 0.0f) {
                this.timeInPortal = 0.0f;
            }
        }
        if (this.timeUntilPortal > 0) {
            --this.timeUntilPortal;
        }
        final boolean var1 = this.movementInput.jump;
        final boolean var2 = this.movementInput.sneak;
        final float var3 = 0.8f;
        final boolean var4 = this.movementInput.moveForward >= var3;
        this.movementInput.updatePlayerMoveState();
        final MovementInputEvent eventinput = new MovementInputEvent(this.movementInput);
        EventManager.call(eventinput);
        if (this.isUsingItem() && !this.isRiding()) {
            Cheese.getInstance();
            if (!Cheese.moduleManager.getModState("NoSlowDown")) {
                final MovementInput movementInput3;
                final MovementInput movementInput = movementInput3 = this.movementInput;
                movementInput3.moveStrafe *= 0.2f;
                final MovementInput movementInput4;
                final MovementInput movementInput2 = movementInput4 = this.movementInput;
                movementInput4.moveForward *= 0.2f;
                this.sprintToggleTimer = 0;
            }
        }
        this.pushOutOfBlocks(this.posX - this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ + this.width * 0.35);
        this.pushOutOfBlocks(this.posX - this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ - this.width * 0.35);
        this.pushOutOfBlocks(this.posX + this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ - this.width * 0.35);
        this.pushOutOfBlocks(this.posX + this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ + this.width * 0.35);
        final boolean var5 = this.getFoodStats().getFoodLevel() > 6.0f || this.capabilities.allowFlying;
        if (this.onGround && !var2 && !var4 && this.movementInput.moveForward >= var3 && !this.isSprinting() && var5 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness)) {
            if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.getIsKeyPressed()) {
                this.sprintToggleTimer = 7;
            }
            else {
                this.setSprinting(true);
            }
        }
        if (!this.isSprinting() && this.movementInput.moveForward >= var3 && var5 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && this.mc.gameSettings.keyBindSprint.getIsKeyPressed()) {
            this.setSprinting(true);
        }
        if (this.isSprinting() && (this.movementInput.moveForward < var3 || this.isCollidedHorizontally || !var5)) {
            this.setSprinting(false);
        }
        if (this.capabilities.allowFlying) {
            if (this.mc.playerController.isSpectatorMode()) {
                if (!this.capabilities.isFlying) {
                    this.capabilities.isFlying = true;
                    this.sendPlayerAbilities();
                }
            }
            else if (!var1 && this.movementInput.jump) {
                if (this.flyToggleTimer == 0) {
                    this.flyToggleTimer = 7;
                }
                else {
                    this.capabilities.isFlying = !this.capabilities.isFlying;
                    this.sendPlayerAbilities();
                    this.flyToggleTimer = 0;
                }
            }
        }
        if (this.capabilities.isFlying && this.func_175160_A()) {
            if (this.movementInput.sneak) {
                this.motionY -= this.capabilities.getFlySpeed() * 3.0f;
            }
            if (this.movementInput.jump) {
                this.motionY += this.capabilities.getFlySpeed() * 3.0f;
            }
        }
        if (this.isRidingHorse()) {
            if (this.horseJumpPowerCounter < 0) {
                ++this.horseJumpPowerCounter;
                if (this.horseJumpPowerCounter == 0) {
                    this.horseJumpPower = 0.0f;
                }
            }
            if (var1 && !this.movementInput.jump) {
                this.horseJumpPowerCounter = -10;
                this.sendHorseJump();
            }
            else if (!var1 && this.movementInput.jump) {
                this.horseJumpPowerCounter = 0;
                this.horseJumpPower = 0.0f;
            }
            else if (var1) {
                ++this.horseJumpPowerCounter;
                if (this.horseJumpPowerCounter < 10) {
                    this.horseJumpPower = this.horseJumpPowerCounter * 0.1f;
                }
                else {
                    this.horseJumpPower = 0.8f + 2.0f / (this.horseJumpPowerCounter - 9) * 0.1f;
                }
            }
        }
        else {
            this.horseJumpPower = 0.0f;
        }
        super.onLivingUpdate();
        if (this.onGround && this.capabilities.isFlying && !this.mc.playerController.isSpectatorMode()) {
            this.capabilities.isFlying = false;
            this.sendPlayerAbilities();
        }
    }
    
    public void fakeSwingItem()
    {
      super.swingItem();
    }
}
