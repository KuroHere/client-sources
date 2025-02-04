// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import java.util.concurrent.Callable;
import net.minecraft.world.Explosion;
import net.minecraft.util.StatCollector;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.EnumFacing;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.block.Block;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldServer;
import net.minecraft.util.MathHelper;
import net.minecraft.command.CommandResultStats;
import java.util.UUID;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.command.ICommandSender;

public abstract class Entity implements ICommandSender
{
    private static final AxisAlignedBB field_174836_a;
    private static int nextEntityID;
    private int entityId;
    public double renderDistanceWeight;
    public boolean preventEntitySpawning;
    public Entity riddenByEntity;
    public Entity ridingEntity;
    public boolean forceSpawn;
    public World worldObj;
    public double prevPosX;
    public double prevPosY;
    public double prevPosZ;
    public double posX;
    public double posY;
    public double posZ;
    public double motionX;
    public double motionY;
    public double motionZ;
    public float rotationYaw;
    public float rotationPitch;
    public float prevRotationYaw;
    public float prevRotationPitch;
    public AxisAlignedBB boundingBox;
    public boolean onGround;
    public boolean isCollidedHorizontally;
    public boolean isCollidedVertically;
    public boolean isCollided;
    public boolean velocityChanged;
    public boolean isInWeb;
    private boolean isOutsideBorder;
    public boolean isDead;
    public float width;
    public float height;
    public float prevDistanceWalkedModified;
    public float distanceWalkedModified;
    public float distanceWalkedOnStepModified;
    public float fallDistance;
    private int nextStepDistance;
    public double lastTickPosX;
    public double lastTickPosY;
    public double lastTickPosZ;
    public float stepHeight;
    public boolean noClip;
    public float entityCollisionReduction;
    protected Random rand;
    public int ticksExisted;
    public int fireResistance;
    private int fire;
    protected boolean inWater;
    public int hurtResistantTime;
    protected boolean firstUpdate;
    protected boolean isImmuneToFire;
    protected DataWatcher dataWatcher;
    private double entityRiderPitchDelta;
    private double entityRiderYawDelta;
    public boolean addedToChunk;
    public int chunkCoordX;
    public int chunkCoordY;
    public int chunkCoordZ;
    public int serverPosX;
    public int serverPosY;
    public int serverPosZ;
    public boolean ignoreFrustumCheck;
    public boolean isAirBorne;
    public int timeUntilPortal;
    protected boolean inPortal;
    protected int portalCounter;
    public int dimension;
    protected int teleportDirection;
    private boolean invulnerable;
    protected UUID entityUniqueID;
    private final CommandResultStats field_174837_as;
    private static final String __OBFID = "CL_00001533";
    
    static {
        field_174836_a = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public void setEntityId(final int id) {
        this.entityId = id;
    }
    
    public void func_174812_G() {
        this.setDead();
    }
    
    public Entity(final World worldIn) {
        this.entityId = Entity.nextEntityID++;
        this.renderDistanceWeight = 1.0;
        this.boundingBox = Entity.field_174836_a;
        this.width = 0.6f;
        this.height = 1.8f;
        this.nextStepDistance = 1;
        this.rand = new Random();
        this.fireResistance = 1;
        this.firstUpdate = true;
        this.entityUniqueID = MathHelper.func_180182_a(this.rand);
        this.field_174837_as = new CommandResultStats();
        this.worldObj = worldIn;
        this.setPosition(0.0, 0.0, 0.0);
        if (worldIn != null) {
            this.dimension = worldIn.provider.getDimensionId();
        }
        (this.dataWatcher = new DataWatcher(this)).addObject(0, 0);
        this.dataWatcher.addObject(1, 300);
        this.dataWatcher.addObject(3, 0);
        this.dataWatcher.addObject(2, "");
        this.dataWatcher.addObject(4, 0);
        this.entityInit();
    }
    
    protected abstract void entityInit();
    
    public DataWatcher getDataWatcher() {
        return this.dataWatcher;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        return p_equals_1_ instanceof Entity && ((Entity)p_equals_1_).entityId == this.entityId;
    }
    
    @Override
    public int hashCode() {
        return this.entityId;
    }
    
    protected void preparePlayerToSpawn() {
        if (this.worldObj != null) {
            while (this.posY > 0.0 && this.posY < 256.0) {
                this.setPosition(this.posX, this.posY, this.posZ);
                if (this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty()) {
                    break;
                }
                ++this.posY;
            }
            final double motionX = 0.0;
            this.motionZ = motionX;
            this.motionY = motionX;
            this.motionX = motionX;
            this.rotationPitch = 0.0f;
        }
    }
    
    public void setDead() {
        this.isDead = true;
    }
    
    protected void setSize(final float width, final float height) {
        if (width != this.width || height != this.height) {
            final float var3 = this.width;
            this.width = width;
            this.height = height;
            this.func_174826_a(new AxisAlignedBB(this.getEntityBoundingBox().minX, this.getEntityBoundingBox().minY, this.getEntityBoundingBox().minZ, this.getEntityBoundingBox().minX + this.width, this.getEntityBoundingBox().minY + this.height, this.getEntityBoundingBox().minZ + this.width));
            if (this.width > var3 && !this.firstUpdate && !this.worldObj.isRemote) {
                this.moveEntity(var3 - this.width, 0.0, var3 - this.width);
            }
        }
    }
    
    protected void setRotation(final float yaw, final float pitch) {
        this.rotationYaw = yaw % 360.0f;
        this.rotationPitch = pitch % 360.0f;
    }
    
    public void setPosition(final double x, final double y, final double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        final float var7 = this.width / 2.0f;
        final float var8 = this.height;
        this.func_174826_a(new AxisAlignedBB(x - var7, y, z - var7, x + var7, y + var8, z + var7));
    }
    
    public void setAngles(final float yaw, final float pitch) {
        final float var3 = this.rotationPitch;
        final float var4 = this.rotationYaw;
        this.rotationYaw += (float)(yaw * 0.15);
        this.rotationPitch -= (float)(pitch * 0.15);
        this.rotationPitch = MathHelper.clamp_float(this.rotationPitch, -90.0f, 90.0f);
        this.prevRotationPitch += this.rotationPitch - var3;
        this.prevRotationYaw += this.rotationYaw - var4;
    }
    
    public void onUpdate() {
        this.onEntityUpdate();
    }
    
    public void onEntityUpdate() {
        this.worldObj.theProfiler.startSection("entityBaseTick");
        if (this.ridingEntity != null && this.ridingEntity.isDead) {
            this.ridingEntity = null;
        }
        this.prevDistanceWalkedModified = this.distanceWalkedModified;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;
        if (!this.worldObj.isRemote && this.worldObj instanceof WorldServer) {
            this.worldObj.theProfiler.startSection("portal");
            final MinecraftServer var1 = ((WorldServer)this.worldObj).func_73046_m();
            final int var2 = this.getMaxInPortalTime();
            if (this.inPortal) {
                if (var1.getAllowNether()) {
                    if (this.ridingEntity == null && this.portalCounter++ >= var2) {
                        this.portalCounter = var2;
                        this.timeUntilPortal = this.getPortalCooldown();
                        byte var3;
                        if (this.worldObj.provider.getDimensionId() == -1) {
                            var3 = 0;
                        }
                        else {
                            var3 = -1;
                        }
                        this.travelToDimension(var3);
                    }
                    this.inPortal = false;
                }
            }
            else {
                if (this.portalCounter > 0) {
                    this.portalCounter -= 4;
                }
                if (this.portalCounter < 0) {
                    this.portalCounter = 0;
                }
            }
            if (this.timeUntilPortal > 0) {
                --this.timeUntilPortal;
            }
            this.worldObj.theProfiler.endSection();
        }
        this.func_174830_Y();
        this.handleWaterMovement();
        if (this.worldObj.isRemote) {
            this.fire = 0;
        }
        else if (this.fire > 0) {
            if (this.isImmuneToFire) {
                this.fire -= 4;
                if (this.fire < 0) {
                    this.fire = 0;
                }
            }
            else {
                if (this.fire % 20 == 0) {
                    this.attackEntityFrom(DamageSource.onFire, 1.0f);
                }
                --this.fire;
            }
        }
        if (this.func_180799_ab()) {
            this.setOnFireFromLava();
            this.fallDistance *= 0.5f;
        }
        if (this.posY < -64.0) {
            this.kill();
        }
        if (!this.worldObj.isRemote) {
            this.setFlag(0, this.fire > 0);
        }
        this.firstUpdate = false;
        this.worldObj.theProfiler.endSection();
    }
    
    public int getMaxInPortalTime() {
        return 0;
    }
    
    protected void setOnFireFromLava() {
        if (!this.isImmuneToFire) {
            this.attackEntityFrom(DamageSource.lava, 4.0f);
            this.setFire(15);
        }
    }
    
    public void setFire(final int seconds) {
        int var2 = seconds * 20;
        var2 = EnchantmentProtection.getFireTimeForEntity(this, var2);
        if (this.fire < var2) {
            this.fire = var2;
        }
    }
    
    public void extinguish() {
        this.fire = 0;
    }
    
    protected void kill() {
        this.setDead();
    }
    
    public boolean isOffsetPositionInLiquid(final double x, final double y, final double z) {
        final AxisAlignedBB var7 = this.getEntityBoundingBox().offset(x, y, z);
        return this.func_174809_b(var7);
    }
    
    private boolean func_174809_b(final AxisAlignedBB p_174809_1_) {
        return this.worldObj.getCollidingBoundingBoxes(this, p_174809_1_).isEmpty() && !this.worldObj.isAnyLiquid(p_174809_1_);
    }
    
    public void moveEntity(double x, double y, double z) {
        if (this.noClip) {
            this.func_174826_a(this.getEntityBoundingBox().offset(x, y, z));
            this.func_174829_m();
        }
        else {
            this.worldObj.theProfiler.startSection("move");
            final double var7 = this.posX;
            final double var8 = this.posY;
            final double var9 = this.posZ;
            if (this.isInWeb) {
                this.isInWeb = false;
                x *= 0.25;
                y *= 0.05000000074505806;
                z *= 0.25;
                this.motionX = 0.0;
                this.motionY = 0.0;
                this.motionZ = 0.0;
            }
            double var10 = x;
            final double var11 = y;
            double var12 = z;
            final boolean var13 = this.onGround && this.isSneaking() && this instanceof EntityPlayer;
            if (var13) {
                final double var14 = 0.05;
                while (x != 0.0) {
                    if (!this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(x, -1.0, 0.0)).isEmpty()) {
                        break;
                    }
                    if (x < var14 && x >= -var14) {
                        x = 0.0;
                    }
                    else if (x > 0.0) {
                        x -= var14;
                    }
                    else {
                        x += var14;
                    }
                    var10 = x;
                }
                while (z != 0.0) {
                    if (!this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(0.0, -1.0, z)).isEmpty()) {
                        break;
                    }
                    if (z < var14 && z >= -var14) {
                        z = 0.0;
                    }
                    else if (z > 0.0) {
                        z -= var14;
                    }
                    else {
                        z += var14;
                    }
                    var12 = z;
                }
                while (x != 0.0 && z != 0.0 && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(x, -1.0, z)).isEmpty()) {
                    if (x < var14 && x >= -var14) {
                        x = 0.0;
                    }
                    else if (x > 0.0) {
                        x -= var14;
                    }
                    else {
                        x += var14;
                    }
                    var10 = x;
                    if (z < var14 && z >= -var14) {
                        z = 0.0;
                    }
                    else if (z > 0.0) {
                        z -= var14;
                    }
                    else {
                        z += var14;
                    }
                    var12 = z;
                }
            }
            final List var15 = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(x, y, z));
            final AxisAlignedBB var16 = this.getEntityBoundingBox();
            for (final AxisAlignedBB var18 : var15) {
                y = var18.calculateYOffset(this.getEntityBoundingBox(), y);
            }
            this.func_174826_a(this.getEntityBoundingBox().offset(0.0, y, 0.0));
            final boolean var19 = this.onGround || (var11 != y && var11 < 0.0);
            for (final AxisAlignedBB var21 : var15) {
                x = var21.calculateXOffset(this.getEntityBoundingBox(), x);
            }
            this.func_174826_a(this.getEntityBoundingBox().offset(x, 0.0, 0.0));
            for (final AxisAlignedBB var21 : var15) {
                z = var21.calculateZOffset(this.getEntityBoundingBox(), z);
            }
            this.func_174826_a(this.getEntityBoundingBox().offset(0.0, 0.0, z));
            if (this.stepHeight > 0.0f && var19 && (var10 != x || var12 != z)) {
                final double var22 = x;
                final double var23 = y;
                final double var24 = z;
                final AxisAlignedBB var25 = this.getEntityBoundingBox();
                this.func_174826_a(var16);
                y = this.stepHeight;
                final List var26 = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(var10, y, var12));
                AxisAlignedBB var27 = this.getEntityBoundingBox();
                final AxisAlignedBB var28 = var27.addCoord(var10, 0.0, var12);
                double var29 = y;
                for (final AxisAlignedBB var31 : var26) {
                    var29 = var31.calculateYOffset(var28, var29);
                }
                var27 = var27.offset(0.0, var29, 0.0);
                double var32 = var10;
                for (final AxisAlignedBB var34 : var26) {
                    var32 = var34.calculateXOffset(var27, var32);
                }
                var27 = var27.offset(var32, 0.0, 0.0);
                double var35 = var12;
                for (final AxisAlignedBB var37 : var26) {
                    var35 = var37.calculateZOffset(var27, var35);
                }
                var27 = var27.offset(0.0, 0.0, var35);
                AxisAlignedBB var38 = this.getEntityBoundingBox();
                double var39 = y;
                for (final AxisAlignedBB var41 : var26) {
                    var39 = var41.calculateYOffset(var38, var39);
                }
                var38 = var38.offset(0.0, var39, 0.0);
                double var42 = var10;
                for (final AxisAlignedBB var44 : var26) {
                    var42 = var44.calculateXOffset(var38, var42);
                }
                var38 = var38.offset(var42, 0.0, 0.0);
                double var45 = var12;
                for (final AxisAlignedBB var47 : var26) {
                    var45 = var47.calculateZOffset(var38, var45);
                }
                var38 = var38.offset(0.0, 0.0, var45);
                final double var48 = var32 * var32 + var35 * var35;
                final double var49 = var42 * var42 + var45 * var45;
                if (var48 > var49) {
                    x = var32;
                    z = var35;
                    this.func_174826_a(var27);
                }
                else {
                    x = var42;
                    z = var45;
                    this.func_174826_a(var38);
                }
                y = -this.stepHeight;
                for (final AxisAlignedBB var51 : var26) {
                    y = var51.calculateYOffset(this.getEntityBoundingBox(), y);
                }
                this.func_174826_a(this.getEntityBoundingBox().offset(0.0, y, 0.0));
                if (var22 * var22 + var24 * var24 >= x * x + z * z) {
                    x = var22;
                    y = var23;
                    z = var24;
                    this.func_174826_a(var25);
                }
            }
            this.worldObj.theProfiler.endSection();
            this.worldObj.theProfiler.startSection("rest");
            this.func_174829_m();
            this.isCollidedHorizontally = (var10 != x || var12 != z);
            this.isCollidedVertically = (var11 != y);
            this.onGround = (this.isCollidedVertically && var11 < 0.0);
            this.isCollided = (this.isCollidedHorizontally || this.isCollidedVertically);
            final int var52 = MathHelper.floor_double(this.posX);
            final int var53 = MathHelper.floor_double(this.posY - 0.20000000298023224);
            final int var54 = MathHelper.floor_double(this.posZ);
            BlockPos var55 = new BlockPos(var52, var53, var54);
            Block var56 = this.worldObj.getBlockState(var55).getBlock();
            if (var56.getMaterial() == Material.air) {
                final Block var57 = this.worldObj.getBlockState(var55.offsetDown()).getBlock();
                if (var57 instanceof BlockFence || var57 instanceof BlockWall || var57 instanceof BlockFenceGate) {
                    var56 = var57;
                    var55 = var55.offsetDown();
                }
            }
            this.func_180433_a(y, this.onGround, var56, var55);
            if (var10 != x) {
                this.motionX = 0.0;
            }
            if (var12 != z) {
                this.motionZ = 0.0;
            }
            if (var11 != y) {
                var56.onLanded(this.worldObj, this);
            }
            if (this.canTriggerWalking() && !var13 && this.ridingEntity == null) {
                final double var58 = this.posX - var7;
                double var59 = this.posY - var8;
                final double var60 = this.posZ - var9;
                if (var56 != Blocks.ladder) {
                    var59 = 0.0;
                }
                if (var56 != null && this.onGround) {
                    var56.onEntityCollidedWithBlock(this.worldObj, var55, this);
                }
                this.distanceWalkedModified += (float)(MathHelper.sqrt_double(var58 * var58 + var60 * var60) * 0.6);
                this.distanceWalkedOnStepModified += (float)(MathHelper.sqrt_double(var58 * var58 + var59 * var59 + var60 * var60) * 0.6);
                if (this.distanceWalkedOnStepModified > this.nextStepDistance && var56.getMaterial() != Material.air) {
                    this.nextStepDistance = (int)this.distanceWalkedOnStepModified + 1;
                    if (this.isInWater()) {
                        float var61 = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224 + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224) * 0.35f;
                        if (var61 > 1.0f) {
                            var61 = 1.0f;
                        }
                        this.playSound(this.getSwimSound(), var61, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
                    }
                    this.func_180429_a(var55, var56);
                }
            }
            try {
                this.doBlockCollisions();
            }
            catch (Throwable var63) {
                final CrashReport var62 = CrashReport.makeCrashReport(var63, "Checking entity block collision");
                final CrashReportCategory var64 = var62.makeCategory("Entity being checked for collision");
                this.addEntityCrashInfo(var64);
                throw new ReportedException(var62);
            }
            final boolean var65 = this.isWet();
            if (this.worldObj.func_147470_e(this.getEntityBoundingBox().contract(0.001, 0.001, 0.001))) {
                this.dealFireDamage(1);
                if (!var65) {
                    ++this.fire;
                    if (this.fire == 0) {
                        this.setFire(8);
                    }
                }
            }
            else if (this.fire <= 0) {
                this.fire = -this.fireResistance;
            }
            if (var65 && this.fire > 0) {
                this.playSound("random.fizz", 0.7f, 1.6f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
                this.fire = -this.fireResistance;
            }
            this.worldObj.theProfiler.endSection();
        }
    }
    
    private void func_174829_m() {
        this.posX = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0;
        this.posY = this.getEntityBoundingBox().minY;
        this.posZ = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0;
    }
    
    protected String getSwimSound() {
        return "game.neutral.swim";
    }
    
    protected void doBlockCollisions() {
        final BlockPos var1 = new BlockPos(this.getEntityBoundingBox().minX + 0.001, this.getEntityBoundingBox().minY + 0.001, this.getEntityBoundingBox().minZ + 0.001);
        final BlockPos var2 = new BlockPos(this.getEntityBoundingBox().maxX - 0.001, this.getEntityBoundingBox().maxY - 0.001, this.getEntityBoundingBox().maxZ - 0.001);
        if (this.worldObj.isAreaLoaded(var1, var2)) {
            for (int var3 = var1.getX(); var3 <= var2.getX(); ++var3) {
                for (int var4 = var1.getY(); var4 <= var2.getY(); ++var4) {
                    for (int var5 = var1.getZ(); var5 <= var2.getZ(); ++var5) {
                        final BlockPos var6 = new BlockPos(var3, var4, var5);
                        final IBlockState var7 = this.worldObj.getBlockState(var6);
                        try {
                            var7.getBlock().onEntityCollidedWithBlock(this.worldObj, var6, var7, this);
                        }
                        catch (Throwable var9) {
                            final CrashReport var8 = CrashReport.makeCrashReport(var9, "Colliding entity with block");
                            final CrashReportCategory var10 = var8.makeCategory("Block being collided with");
                            CrashReportCategory.addBlockInfo(var10, var6, var7);
                            throw new ReportedException(var8);
                        }
                    }
                }
            }
        }
    }
    
    protected void func_180429_a(final BlockPos p_180429_1_, final Block p_180429_2_) {
        Block.SoundType var3 = p_180429_2_.stepSound;
        if (this.worldObj.getBlockState(p_180429_1_.offsetUp()).getBlock() == Blocks.snow_layer) {
            var3 = Blocks.snow_layer.stepSound;
            this.playSound(var3.getStepSound(), var3.getVolume() * 0.15f, var3.getFrequency());
        }
        else if (!p_180429_2_.getMaterial().isLiquid()) {
            this.playSound(var3.getStepSound(), var3.getVolume() * 0.15f, var3.getFrequency());
        }
    }
    
    public void playSound(final String name, final float volume, final float pitch) {
        if (!this.isSlient()) {
            this.worldObj.playSoundAtEntity(this, name, volume, pitch);
        }
    }
    
    public boolean isSlient() {
        return this.dataWatcher.getWatchableObjectByte(4) == 1;
    }
    
    public void func_174810_b(final boolean p_174810_1_) {
        this.dataWatcher.updateObject(4, (byte)(byte)(p_174810_1_ ? 1 : 0));
    }
    
    protected boolean canTriggerWalking() {
        return true;
    }
    
    protected void func_180433_a(final double p_180433_1_, final boolean p_180433_3_, final Block p_180433_4_, final BlockPos p_180433_5_) {
        if (p_180433_3_) {
            if (this.fallDistance > 0.0f) {
                if (p_180433_4_ != null) {
                    p_180433_4_.onFallenUpon(this.worldObj, p_180433_5_, this, this.fallDistance);
                }
                else {
                    this.fall(this.fallDistance, 1.0f);
                }
                this.fallDistance = 0.0f;
            }
        }
        else if (p_180433_1_ < 0.0) {
            this.fallDistance -= (float)p_180433_1_;
        }
    }
    
    public AxisAlignedBB getBoundingBox() {
        return null;
    }
    
    protected void dealFireDamage(final int amount) {
        if (!this.isImmuneToFire) {
            this.attackEntityFrom(DamageSource.inFire, (float)amount);
        }
    }
    
    public final boolean isImmuneToFire() {
        return this.isImmuneToFire;
    }
    
    public void fall(final float distance, final float damageMultiplier) {
        if (this.riddenByEntity != null) {
            this.riddenByEntity.fall(distance, damageMultiplier);
        }
    }
    
    public boolean isWet() {
        return this.inWater || this.worldObj.func_175727_C(new BlockPos(this.posX, this.posY, this.posZ)) || this.worldObj.func_175727_C(new BlockPos(this.posX, this.posY + this.height, this.posZ));
    }
    
    public boolean isInWater() {
        return this.inWater;
    }
    
    public boolean handleWaterMovement() {
        if (this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox().expand(0.0, -0.4000000059604645, 0.0).contract(0.001, 0.001, 0.001), Material.water, this)) {
            if (!this.inWater && !this.firstUpdate) {
                this.resetHeight();
            }
            this.fallDistance = 0.0f;
            this.inWater = true;
            this.fire = 0;
        }
        else {
            this.inWater = false;
        }
        return this.inWater;
    }
    
    protected void resetHeight() {
        float var1 = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224 + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224) * 0.2f;
        if (var1 > 1.0f) {
            var1 = 1.0f;
        }
        this.playSound(this.getSplashSound(), var1, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
        final float var2 = (float)MathHelper.floor_double(this.getEntityBoundingBox().minY);
        for (int var3 = 0; var3 < 1.0f + this.width * 20.0f; ++var3) {
            final float var4 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width;
            final float var5 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width;
            this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + var4, var2 + 1.0f, this.posZ + var5, this.motionX, this.motionY - this.rand.nextFloat() * 0.2f, this.motionZ, new int[0]);
        }
        for (int var3 = 0; var3 < 1.0f + this.width * 20.0f; ++var3) {
            final float var4 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width;
            final float var5 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width;
            this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + var4, var2 + 1.0f, this.posZ + var5, this.motionX, this.motionY, this.motionZ, new int[0]);
        }
    }
    
    public void func_174830_Y() {
        if (this.isSprinting() && !this.isInWater()) {
            this.func_174808_Z();
        }
    }
    
    protected void func_174808_Z() {
        final int var1 = MathHelper.floor_double(this.posX);
        final int var2 = MathHelper.floor_double(this.posY - 0.20000000298023224);
        final int var3 = MathHelper.floor_double(this.posZ);
        final BlockPos var4 = new BlockPos(var1, var2, var3);
        final IBlockState var5 = this.worldObj.getBlockState(var4);
        final Block var6 = var5.getBlock();
        if (var6.getRenderType() != -1) {
            this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (this.rand.nextFloat() - 0.5) * this.width, this.getEntityBoundingBox().minY + 0.1, this.posZ + (this.rand.nextFloat() - 0.5) * this.width, -this.motionX * 4.0, 1.5, -this.motionZ * 4.0, Block.getStateId(var5));
        }
    }
    
    protected String getSplashSound() {
        return "game.neutral.swim.splash";
    }
    
    public boolean isInsideOfMaterial(final Material materialIn) {
        final double var2 = this.posY + this.getEyeHeight();
        final BlockPos var3 = new BlockPos(this.posX, var2, this.posZ);
        final IBlockState var4 = this.worldObj.getBlockState(var3);
        final Block var5 = var4.getBlock();
        if (var5.getMaterial() == materialIn) {
            final float var6 = BlockLiquid.getLiquidHeightPercent(var4.getBlock().getMetaFromState(var4)) - 0.11111111f;
            final float var7 = var3.getY() + 1 - var6;
            final boolean var8 = var2 < var7;
            return (var8 || !(this instanceof EntityPlayer)) && var8;
        }
        return false;
    }
    
    public boolean func_180799_ab() {
        return this.worldObj.isMaterialInBB(this.getEntityBoundingBox().expand(-0.10000000149011612, -0.4000000059604645, -0.10000000149011612), Material.lava);
    }
    
    public void moveFlying(float strafe, float forward, final float friction) {
        float var4 = strafe * strafe + forward * forward;
        if (var4 >= 1.0E-4f) {
            var4 = MathHelper.sqrt_float(var4);
            if (var4 < 1.0f) {
                var4 = 1.0f;
            }
            var4 = friction / var4;
            strafe *= var4;
            forward *= var4;
            final float var5 = MathHelper.sin(this.rotationYaw * 3.1415927f / 180.0f);
            final float var6 = MathHelper.cos(this.rotationYaw * 3.1415927f / 180.0f);
            this.motionX += strafe * var6 - forward * var5;
            this.motionZ += forward * var6 + strafe * var5;
        }
    }
    
    public int getBrightnessForRender(final float p_70070_1_) {
        final BlockPos var2 = new BlockPos(this.posX, 0.0, this.posZ);
        if (this.worldObj.isBlockLoaded(var2)) {
            final double var3 = (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * 0.66;
            final int var4 = MathHelper.floor_double(this.posY + var3);
            return this.worldObj.getCombinedLight(var2.offsetUp(var4), 0);
        }
        return 0;
    }
    
    public float getBrightness(final float p_70013_1_) {
        final BlockPos var2 = new BlockPos(this.posX, 0.0, this.posZ);
        if (this.worldObj.isBlockLoaded(var2)) {
            final double var3 = (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * 0.66;
            final int var4 = MathHelper.floor_double(this.posY + var3);
            return this.worldObj.getLightBrightness(var2.offsetUp(var4));
        }
        return 0.0f;
    }
    
    public void setWorld(final World worldIn) {
        this.worldObj = worldIn;
    }
    
    public void setPositionAndRotation(final double x, final double y, final double z, final float yaw, final float pitch) {
        this.posX = x;
        this.prevPosX = x;
        this.posY = y;
        this.prevPosY = y;
        this.posZ = z;
        this.prevPosZ = z;
        this.rotationYaw = yaw;
        this.prevRotationYaw = yaw;
        this.rotationPitch = pitch;
        this.prevRotationPitch = pitch;
        final double var9 = this.prevRotationYaw - yaw;
        if (var9 < -180.0) {
            this.prevRotationYaw += 360.0f;
        }
        if (var9 >= 180.0) {
            this.prevRotationYaw -= 360.0f;
        }
        this.setPosition(this.posX, this.posY, this.posZ);
        this.setRotation(yaw, pitch);
    }
    
    public void func_174828_a(final BlockPos p_174828_1_, final float p_174828_2_, final float p_174828_3_) {
        this.setLocationAndAngles(p_174828_1_.getX() + 0.5, p_174828_1_.getY(), p_174828_1_.getZ() + 0.5, p_174828_2_, p_174828_3_);
    }
    
    public void setLocationAndAngles(final double x, final double y, final double z, final float yaw, final float pitch) {
        this.posX = x;
        this.prevPosX = x;
        this.lastTickPosX = x;
        this.posY = y;
        this.prevPosY = y;
        this.lastTickPosY = y;
        this.posZ = z;
        this.prevPosZ = z;
        this.lastTickPosZ = z;
        this.rotationYaw = yaw;
        this.rotationPitch = pitch;
        this.setPosition(this.posX, this.posY, this.posZ);
    }
    
    public float getDistanceToEntity(final Entity entityIn) {
        final float var2 = (float)(this.posX - entityIn.posX);
        final float var3 = (float)(this.posY - entityIn.posY);
        final float var4 = (float)(this.posZ - entityIn.posZ);
        return MathHelper.sqrt_float(var2 * var2 + var3 * var3 + var4 * var4);
    }
    
    public double getDistanceSq(final double x, final double y, final double z) {
        final double var7 = this.posX - x;
        final double var8 = this.posY - y;
        final double var9 = this.posZ - z;
        return var7 * var7 + var8 * var8 + var9 * var9;
    }
    
    public double getDistanceSq(final BlockPos p_174818_1_) {
        return p_174818_1_.distanceSq(this.posX, this.posY, this.posZ);
    }
    
    public double func_174831_c(final BlockPos p_174831_1_) {
        return p_174831_1_.distanceSqToCenter(this.posX, this.posY, this.posZ);
    }
    
    public double getDistance(final double x, final double y, final double z) {
        final double var7 = this.posX - x;
        final double var8 = this.posY - y;
        final double var9 = this.posZ - z;
        return MathHelper.sqrt_double(var7 * var7 + var8 * var8 + var9 * var9);
    }
    
    public double getDistanceSqToEntity(final Entity entityIn) {
        final double var2 = this.posX - entityIn.posX;
        final double var3 = this.posY - entityIn.posY;
        final double var4 = this.posZ - entityIn.posZ;
        return var2 * var2 + var3 * var3 + var4 * var4;
    }
    
    public void onCollideWithPlayer(final EntityPlayer entityIn) {
    }
    
    public void applyEntityCollision(final Entity entityIn) {
        if (entityIn.riddenByEntity != this && entityIn.ridingEntity != this && !entityIn.noClip && !this.noClip) {
            double var2 = entityIn.posX - this.posX;
            double var3 = entityIn.posZ - this.posZ;
            double var4 = MathHelper.abs_max(var2, var3);
            if (var4 >= 0.009999999776482582) {
                var4 = MathHelper.sqrt_double(var4);
                var2 /= var4;
                var3 /= var4;
                double var5 = 1.0 / var4;
                if (var5 > 1.0) {
                    var5 = 1.0;
                }
                var2 *= var5;
                var3 *= var5;
                var2 *= 0.05000000074505806;
                var3 *= 0.05000000074505806;
                var2 *= 1.0f - this.entityCollisionReduction;
                var3 *= 1.0f - this.entityCollisionReduction;
                if (this.riddenByEntity == null) {
                    this.addVelocity(-var2, 0.0, -var3);
                }
                if (entityIn.riddenByEntity == null) {
                    entityIn.addVelocity(var2, 0.0, var3);
                }
            }
        }
    }
    
    public void addVelocity(final double x, final double y, final double z) {
        this.motionX += x;
        this.motionY += y;
        this.motionZ += z;
        this.isAirBorne = true;
    }
    
    protected void setBeenAttacked() {
        this.velocityChanged = true;
    }
    
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.func_180431_b(source)) {
            return false;
        }
        this.setBeenAttacked();
        return false;
    }
    
    public Vec3 getLook(final float p_70676_1_) {
        if (p_70676_1_ == 1.0f) {
            return this.func_174806_f(this.rotationPitch, this.rotationYaw);
        }
        final float var2 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * p_70676_1_;
        final float var3 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * p_70676_1_;
        return this.func_174806_f(var2, var3);
    }
    
    protected final Vec3 func_174806_f(final float p_174806_1_, final float p_174806_2_) {
        final float var3 = MathHelper.cos(-p_174806_2_ * 0.017453292f - 3.1415927f);
        final float var4 = MathHelper.sin(-p_174806_2_ * 0.017453292f - 3.1415927f);
        final float var5 = -MathHelper.cos(-p_174806_1_ * 0.017453292f);
        final float var6 = MathHelper.sin(-p_174806_1_ * 0.017453292f);
        return new Vec3(var4 * var5, var6, var3 * var5);
    }
    
    public Vec3 func_174824_e(final float p_174824_1_) {
        if (p_174824_1_ == 1.0f) {
            return new Vec3(this.posX, this.posY + this.getEyeHeight(), this.posZ);
        }
        final double var2 = this.prevPosX + (this.posX - this.prevPosX) * p_174824_1_;
        final double var3 = this.prevPosY + (this.posY - this.prevPosY) * p_174824_1_ + this.getEyeHeight();
        final double var4 = this.prevPosZ + (this.posZ - this.prevPosZ) * p_174824_1_;
        return new Vec3(var2, var3, var4);
    }
    
    public MovingObjectPosition func_174822_a(final double p_174822_1_, final float p_174822_3_) {
        final Vec3 var4 = this.func_174824_e(p_174822_3_);
        final Vec3 var5 = this.getLook(p_174822_3_);
        final Vec3 var6 = var4.addVector(var5.xCoord * p_174822_1_, var5.yCoord * p_174822_1_, var5.zCoord * p_174822_1_);
        return this.worldObj.rayTraceBlocks(var4, var6, false, false, true);
    }
    
    public boolean canBeCollidedWith() {
        return false;
    }
    
    public boolean canBePushed() {
        return false;
    }
    
    public void addToPlayerScore(final Entity entityIn, final int amount) {
    }
    
    public boolean isInRangeToRender3d(final double x, final double y, final double z) {
        final double var7 = this.posX - x;
        final double var8 = this.posY - y;
        final double var9 = this.posZ - z;
        final double var10 = var7 * var7 + var8 * var8 + var9 * var9;
        return this.isInRangeToRenderDist(var10);
    }
    
    public boolean isInRangeToRenderDist(final double distance) {
        double var3 = this.getEntityBoundingBox().getAverageEdgeLength();
        var3 *= 64.0 * this.renderDistanceWeight;
        return distance < var3 * var3;
    }
    
    public boolean writeMountToNBT(final NBTTagCompound tagCompund) {
        final String var2 = this.getEntityString();
        if (!this.isDead && var2 != null) {
            tagCompund.setString("id", var2);
            this.writeToNBT(tagCompund);
            return true;
        }
        return false;
    }
    
    public boolean writeToNBTOptional(final NBTTagCompound tagCompund) {
        final String var2 = this.getEntityString();
        if (!this.isDead && var2 != null && this.riddenByEntity == null) {
            tagCompund.setString("id", var2);
            this.writeToNBT(tagCompund);
            return true;
        }
        return false;
    }
    
    public void writeToNBT(final NBTTagCompound tagCompund) {
        try {
            tagCompund.setTag("Pos", this.newDoubleNBTList(this.posX, this.posY, this.posZ));
            tagCompund.setTag("Motion", this.newDoubleNBTList(this.motionX, this.motionY, this.motionZ));
            tagCompund.setTag("Rotation", this.newFloatNBTList(this.rotationYaw, this.rotationPitch));
            tagCompund.setFloat("FallDistance", this.fallDistance);
            tagCompund.setShort("Fire", (short)this.fire);
            tagCompund.setShort("Air", (short)this.getAir());
            tagCompund.setBoolean("OnGround", this.onGround);
            tagCompund.setInteger("Dimension", this.dimension);
            tagCompund.setBoolean("Invulnerable", this.invulnerable);
            tagCompund.setInteger("PortalCooldown", this.timeUntilPortal);
            tagCompund.setLong("UUIDMost", this.getUniqueID().getMostSignificantBits());
            tagCompund.setLong("UUIDLeast", this.getUniqueID().getLeastSignificantBits());
            if (this.getCustomNameTag() != null && this.getCustomNameTag().length() > 0) {
                tagCompund.setString("CustomName", this.getCustomNameTag());
                tagCompund.setBoolean("CustomNameVisible", this.getAlwaysRenderNameTag());
            }
            this.field_174837_as.func_179670_b(tagCompund);
            if (this.isSlient()) {
                tagCompund.setBoolean("Silent", this.isSlient());
            }
            this.writeEntityToNBT(tagCompund);
            if (this.ridingEntity != null) {
                final NBTTagCompound var2 = new NBTTagCompound();
                if (this.ridingEntity.writeMountToNBT(var2)) {
                    tagCompund.setTag("Riding", var2);
                }
            }
        }
        catch (Throwable var4) {
            final CrashReport var3 = CrashReport.makeCrashReport(var4, "Saving entity NBT");
            final CrashReportCategory var5 = var3.makeCategory("Entity being saved");
            this.addEntityCrashInfo(var5);
            throw new ReportedException(var3);
        }
    }
    
    public void readFromNBT(final NBTTagCompound tagCompund) {
        try {
            final NBTTagList var2 = tagCompund.getTagList("Pos", 6);
            final NBTTagList var3 = tagCompund.getTagList("Motion", 6);
            final NBTTagList var4 = tagCompund.getTagList("Rotation", 5);
            this.motionX = var3.getDouble(0);
            this.motionY = var3.getDouble(1);
            this.motionZ = var3.getDouble(2);
            if (Math.abs(this.motionX) > 10.0) {
                this.motionX = 0.0;
            }
            if (Math.abs(this.motionY) > 10.0) {
                this.motionY = 0.0;
            }
            if (Math.abs(this.motionZ) > 10.0) {
                this.motionZ = 0.0;
            }
            final double double1 = var2.getDouble(0);
            this.posX = double1;
            this.lastTickPosX = double1;
            this.prevPosX = double1;
            final double double2 = var2.getDouble(1);
            this.posY = double2;
            this.lastTickPosY = double2;
            this.prevPosY = double2;
            final double double3 = var2.getDouble(2);
            this.posZ = double3;
            this.lastTickPosZ = double3;
            this.prevPosZ = double3;
            final float float1 = var4.getFloat(0);
            this.rotationYaw = float1;
            this.prevRotationYaw = float1;
            final float float2 = var4.getFloat(1);
            this.rotationPitch = float2;
            this.prevRotationPitch = float2;
            this.fallDistance = tagCompund.getFloat("FallDistance");
            this.fire = tagCompund.getShort("Fire");
            this.setAir(tagCompund.getShort("Air"));
            this.onGround = tagCompund.getBoolean("OnGround");
            this.dimension = tagCompund.getInteger("Dimension");
            this.invulnerable = tagCompund.getBoolean("Invulnerable");
            this.timeUntilPortal = tagCompund.getInteger("PortalCooldown");
            if (tagCompund.hasKey("UUIDMost", 4) && tagCompund.hasKey("UUIDLeast", 4)) {
                this.entityUniqueID = new UUID(tagCompund.getLong("UUIDMost"), tagCompund.getLong("UUIDLeast"));
            }
            else if (tagCompund.hasKey("UUID", 8)) {
                this.entityUniqueID = UUID.fromString(tagCompund.getString("UUID"));
            }
            this.setPosition(this.posX, this.posY, this.posZ);
            this.setRotation(this.rotationYaw, this.rotationPitch);
            if (tagCompund.hasKey("CustomName", 8) && tagCompund.getString("CustomName").length() > 0) {
                this.setCustomNameTag(tagCompund.getString("CustomName"));
            }
            this.setAlwaysRenderNameTag(tagCompund.getBoolean("CustomNameVisible"));
            this.field_174837_as.func_179668_a(tagCompund);
            this.func_174810_b(tagCompund.getBoolean("Silent"));
            this.readEntityFromNBT(tagCompund);
            if (this.shouldSetPosAfterLoading()) {
                this.setPosition(this.posX, this.posY, this.posZ);
            }
        }
        catch (Throwable var6) {
            final CrashReport var5 = CrashReport.makeCrashReport(var6, "Loading entity NBT");
            final CrashReportCategory var7 = var5.makeCategory("Entity being loaded");
            this.addEntityCrashInfo(var7);
            throw new ReportedException(var5);
        }
    }
    
    protected boolean shouldSetPosAfterLoading() {
        return true;
    }
    
    protected final String getEntityString() {
        return EntityList.getEntityString(this);
    }
    
    protected abstract void readEntityFromNBT(final NBTTagCompound p0);
    
    protected abstract void writeEntityToNBT(final NBTTagCompound p0);
    
    public void onChunkLoad() {
    }
    
    protected NBTTagList newDoubleNBTList(final double... numbers) {
        final NBTTagList var2 = new NBTTagList();
        final double[] var3 = numbers;
        for (int var4 = numbers.length, var5 = 0; var5 < var4; ++var5) {
            final double var6 = var3[var5];
            var2.appendTag(new NBTTagDouble(var6));
        }
        return var2;
    }
    
    protected NBTTagList newFloatNBTList(final float... numbers) {
        final NBTTagList var2 = new NBTTagList();
        final float[] var3 = numbers;
        for (int var4 = numbers.length, var5 = 0; var5 < var4; ++var5) {
            final float var6 = var3[var5];
            var2.appendTag(new NBTTagFloat(var6));
        }
        return var2;
    }
    
    public EntityItem dropItem(final Item itemIn, final int size) {
        return this.dropItemWithOffset(itemIn, size, 0.0f);
    }
    
    public EntityItem dropItemWithOffset(final Item itemIn, final int size, final float p_145778_3_) {
        return this.entityDropItem(new ItemStack(itemIn, size, 0), p_145778_3_);
    }
    
    public EntityItem entityDropItem(final ItemStack itemStackIn, final float offsetY) {
        if (itemStackIn.stackSize != 0 && itemStackIn.getItem() != null) {
            final EntityItem var3 = new EntityItem(this.worldObj, this.posX, this.posY + offsetY, this.posZ, itemStackIn);
            var3.setDefaultPickupDelay();
            this.worldObj.spawnEntityInWorld(var3);
            return var3;
        }
        return null;
    }
    
    public boolean isEntityAlive() {
        return !this.isDead;
    }
    
    public boolean isEntityInsideOpaqueBlock() {
        if (this.noClip) {
            return false;
        }
        for (int var1 = 0; var1 < 8; ++var1) {
            final double var2 = this.posX + ((var1 >> 0) % 2 - 0.5f) * this.width * 0.8f;
            final double var3 = this.posY + ((var1 >> 1) % 2 - 0.5f) * 0.1f;
            final double var4 = this.posZ + ((var1 >> 2) % 2 - 0.5f) * this.width * 0.8f;
            if (this.worldObj.getBlockState(new BlockPos(var2, var3 + this.getEyeHeight(), var4)).getBlock().isVisuallyOpaque()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean interactFirst(final EntityPlayer playerIn) {
        return false;
    }
    
    public AxisAlignedBB getCollisionBox(final Entity entityIn) {
        return null;
    }
    
    public void updateRidden() {
        if (this.ridingEntity.isDead) {
            this.ridingEntity = null;
        }
        else {
            this.motionX = 0.0;
            this.motionY = 0.0;
            this.motionZ = 0.0;
            this.onUpdate();
            if (this.ridingEntity != null) {
                this.ridingEntity.updateRiderPosition();
                this.entityRiderYawDelta += this.ridingEntity.rotationYaw - this.ridingEntity.prevRotationYaw;
                this.entityRiderPitchDelta += this.ridingEntity.rotationPitch - this.ridingEntity.prevRotationPitch;
                while (this.entityRiderYawDelta >= 180.0) {
                    this.entityRiderYawDelta -= 360.0;
                }
                while (this.entityRiderYawDelta < -180.0) {
                    this.entityRiderYawDelta += 360.0;
                }
                while (this.entityRiderPitchDelta >= 180.0) {
                    this.entityRiderPitchDelta -= 360.0;
                }
                while (this.entityRiderPitchDelta < -180.0) {
                    this.entityRiderPitchDelta += 360.0;
                }
                double var1 = this.entityRiderYawDelta * 0.5;
                double var2 = this.entityRiderPitchDelta * 0.5;
                final float var3 = 10.0f;
                if (var1 > var3) {
                    var1 = var3;
                }
                if (var1 < -var3) {
                    var1 = -var3;
                }
                if (var2 > var3) {
                    var2 = var3;
                }
                if (var2 < -var3) {
                    var2 = -var3;
                }
                this.entityRiderYawDelta -= var1;
                this.entityRiderPitchDelta -= var2;
            }
        }
    }
    
    public void updateRiderPosition() {
        if (this.riddenByEntity != null) {
            this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
        }
    }
    
    public double getYOffset() {
        return 0.0;
    }
    
    public double getMountedYOffset() {
        return this.height * 0.75;
    }
    
    public void mountEntity(final Entity entityIn) {
        this.entityRiderPitchDelta = 0.0;
        this.entityRiderYawDelta = 0.0;
        if (entityIn == null) {
            if (this.ridingEntity != null) {
                this.setLocationAndAngles(this.ridingEntity.posX, this.ridingEntity.getEntityBoundingBox().minY + this.ridingEntity.height, this.ridingEntity.posZ, this.rotationYaw, this.rotationPitch);
                this.ridingEntity.riddenByEntity = null;
            }
            this.ridingEntity = null;
        }
        else {
            if (this.ridingEntity != null) {
                this.ridingEntity.riddenByEntity = null;
            }
            if (entityIn != null) {
                for (Entity var2 = entityIn.ridingEntity; var2 != null; var2 = var2.ridingEntity) {
                    if (var2 == this) {
                        return;
                    }
                }
            }
            this.ridingEntity = entityIn;
            entityIn.riddenByEntity = this;
        }
    }
    
    public void func_180426_a(final double p_180426_1_, double p_180426_3_, final double p_180426_5_, final float p_180426_7_, final float p_180426_8_, final int p_180426_9_, final boolean p_180426_10_) {
        this.setPosition(p_180426_1_, p_180426_3_, p_180426_5_);
        this.setRotation(p_180426_7_, p_180426_8_);
        final List var11 = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().contract(0.03125, 0.0, 0.03125));
        if (!var11.isEmpty()) {
            double var12 = 0.0;
            for (final AxisAlignedBB var14 : var11) {
                if (var14.maxY > var12) {
                    var12 = var14.maxY;
                }
            }
            p_180426_3_ += var12 - this.getEntityBoundingBox().minY;
            this.setPosition(p_180426_1_, p_180426_3_, p_180426_5_);
        }
    }
    
    public float getCollisionBorderSize() {
        return 0.1f;
    }
    
    public Vec3 getLookVec() {
        return null;
    }
    
    public void setInPortal() {
        if (this.timeUntilPortal > 0) {
            this.timeUntilPortal = this.getPortalCooldown();
        }
        else {
            final double var1 = this.prevPosX - this.posX;
            final double var2 = this.prevPosZ - this.posZ;
            if (!this.worldObj.isRemote && !this.inPortal) {
                int var3;
                if (MathHelper.abs((float)var1) > MathHelper.abs((float)var2)) {
                    var3 = ((var1 > 0.0) ? EnumFacing.WEST.getHorizontalIndex() : EnumFacing.EAST.getHorizontalIndex());
                }
                else {
                    var3 = ((var2 > 0.0) ? EnumFacing.NORTH.getHorizontalIndex() : EnumFacing.SOUTH.getHorizontalIndex());
                }
                this.teleportDirection = var3;
            }
            this.inPortal = true;
        }
    }
    
    public int getPortalCooldown() {
        return 300;
    }
    
    public void setVelocity(final double x, final double y, final double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
    }
    
    public void handleHealthUpdate(final byte p_70103_1_) {
    }
    
    public void performHurtAnimation() {
    }
    
    public ItemStack[] getInventory() {
        return null;
    }
    
    public void setCurrentItemOrArmor(final int slotIn, final ItemStack itemStackIn) {
    }
    
    public boolean isBurning() {
        final boolean var1 = this.worldObj != null && this.worldObj.isRemote;
        return !this.isImmuneToFire && (this.fire > 0 || (var1 && this.getFlag(0)));
    }
    
    public boolean isRiding() {
        return this.ridingEntity != null;
    }
    
    public boolean isSneaking() {
        return this.getFlag(1);
    }
    
    public void setSneaking(final boolean sneaking) {
        this.setFlag(1, sneaking);
    }
    
    public boolean isSprinting() {
        return this.getFlag(3);
    }
    
    public void setSprinting(final boolean sprinting) {
        this.setFlag(3, sprinting);
    }
    
    public boolean isInvisible() {
        return this.getFlag(5);
    }
    
    public boolean isInvisibleToPlayer(final EntityPlayer playerIn) {
        return !playerIn.func_175149_v() && this.isInvisible();
    }
    
    public void setInvisible(final boolean invisible) {
        this.setFlag(5, invisible);
    }
    
    public boolean isEating() {
        return this.getFlag(4);
    }
    
    public void setEating(final boolean eating) {
        this.setFlag(4, eating);
    }
    
    protected boolean getFlag(final int flag) {
        return (this.dataWatcher.getWatchableObjectByte(0) & 1 << flag) != 0x0;
    }
    
    protected void setFlag(final int flag, final boolean set) {
        final byte var3 = this.dataWatcher.getWatchableObjectByte(0);
        if (set) {
            this.dataWatcher.updateObject(0, (byte)(var3 | 1 << flag));
        }
        else {
            this.dataWatcher.updateObject(0, (byte)(var3 & ~(1 << flag)));
        }
    }
    
    public int getAir() {
        return this.dataWatcher.getWatchableObjectShort(1);
    }
    
    public void setAir(final int air) {
        this.dataWatcher.updateObject(1, (short)air);
    }
    
    public void onStruckByLightning(final EntityLightningBolt lightningBolt) {
        this.attackEntityFrom(DamageSource.field_180137_b, 5.0f);
        ++this.fire;
        if (this.fire == 0) {
            this.setFire(8);
        }
    }
    
    public void onKillEntity(final EntityLivingBase entityLivingIn) {
    }
    
    protected boolean pushOutOfBlocks(final double x, final double y, final double z) {
        final BlockPos var7 = new BlockPos(x, y, z);
        final double var8 = x - var7.getX();
        final double var9 = y - var7.getY();
        final double var10 = z - var7.getZ();
        final List var11 = this.worldObj.func_147461_a(this.getEntityBoundingBox());
        if (var11.isEmpty() && !this.worldObj.func_175665_u(var7)) {
            return false;
        }
        byte var12 = 3;
        double var13 = 9999.0;
        if (!this.worldObj.func_175665_u(var7.offsetWest()) && var8 < var13) {
            var13 = var8;
            var12 = 0;
        }
        if (!this.worldObj.func_175665_u(var7.offsetEast()) && 1.0 - var8 < var13) {
            var13 = 1.0 - var8;
            var12 = 1;
        }
        if (!this.worldObj.func_175665_u(var7.offsetUp()) && 1.0 - var9 < var13) {
            var13 = 1.0 - var9;
            var12 = 3;
        }
        if (!this.worldObj.func_175665_u(var7.offsetNorth()) && var10 < var13) {
            var13 = var10;
            var12 = 4;
        }
        if (!this.worldObj.func_175665_u(var7.offsetSouth()) && 1.0 - var10 < var13) {
            var13 = 1.0 - var10;
            var12 = 5;
        }
        final float var14 = this.rand.nextFloat() * 0.2f + 0.1f;
        if (var12 == 0) {
            this.motionX = -var14;
        }
        if (var12 == 1) {
            this.motionX = var14;
        }
        if (var12 == 3) {
            this.motionY = var14;
        }
        if (var12 == 4) {
            this.motionZ = -var14;
        }
        if (var12 == 5) {
            this.motionZ = var14;
        }
        return true;
    }
    
    public void setInWeb() {
        this.isInWeb = true;
        this.fallDistance = 0.0f;
    }
    
    @Override
    public String getName() {
        if (this.hasCustomName()) {
            return this.getCustomNameTag();
        }
        String var1 = EntityList.getEntityString(this);
        if (var1 == null) {
            var1 = "generic";
        }
        return StatCollector.translateToLocal("entity." + var1 + ".name");
    }
    
    public Entity[] getParts() {
        return null;
    }
    
    public boolean isEntityEqual(final Entity entityIn) {
        return this == entityIn;
    }
    
    public float getRotationYawHead() {
        return 0.0f;
    }
    
    public void setRotationYawHead(final float rotation) {
    }
    
    public boolean canAttackWithItem() {
        return true;
    }
    
    public boolean hitByEntity(final Entity entityIn) {
        return false;
    }
    
    @Override
    public String toString() {
        return String.format("%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]", this.getClass().getSimpleName(), this.getName(), this.entityId, (this.worldObj == null) ? "~NULL~" : this.worldObj.getWorldInfo().getWorldName(), this.posX, this.posY, this.posZ);
    }
    
    public boolean func_180431_b(final DamageSource p_180431_1_) {
        return this.invulnerable && p_180431_1_ != DamageSource.outOfWorld && !p_180431_1_.func_180136_u();
    }
    
    public void copyLocationAndAnglesFrom(final Entity entityIn) {
        this.setLocationAndAngles(entityIn.posX, entityIn.posY, entityIn.posZ, entityIn.rotationYaw, entityIn.rotationPitch);
    }
    
    public void func_180432_n(final Entity p_180432_1_) {
        final NBTTagCompound var2 = new NBTTagCompound();
        p_180432_1_.writeToNBT(var2);
        this.readFromNBT(var2);
        this.timeUntilPortal = p_180432_1_.timeUntilPortal;
        this.teleportDirection = p_180432_1_.teleportDirection;
    }
    
    public void travelToDimension(final int dimensionId) {
        if (!this.worldObj.isRemote && !this.isDead) {
            this.worldObj.theProfiler.startSection("changeDimension");
            final MinecraftServer var2 = MinecraftServer.getServer();
            final int var3 = this.dimension;
            final WorldServer var4 = var2.worldServerForDimension(var3);
            WorldServer var5 = var2.worldServerForDimension(dimensionId);
            this.dimension = dimensionId;
            if (var3 == 1 && dimensionId == 1) {
                var5 = var2.worldServerForDimension(0);
                this.dimension = 0;
            }
            this.worldObj.removeEntity(this);
            this.isDead = false;
            this.worldObj.theProfiler.startSection("reposition");
            var2.getConfigurationManager().transferEntityToWorld(this, var3, var4, var5);
            this.worldObj.theProfiler.endStartSection("reloading");
            final Entity var6 = EntityList.createEntityByName(EntityList.getEntityString(this), var5);
            if (var6 != null) {
                var6.func_180432_n(this);
                if (var3 == 1 && dimensionId == 1) {
                    final BlockPos var7 = this.worldObj.func_175672_r(var5.getSpawnPoint());
                    var6.func_174828_a(var7, var6.rotationYaw, var6.rotationPitch);
                }
                var5.spawnEntityInWorld(var6);
            }
            this.isDead = true;
            this.worldObj.theProfiler.endSection();
            var4.resetUpdateEntityTick();
            var5.resetUpdateEntityTick();
            this.worldObj.theProfiler.endSection();
        }
    }
    
    public float getExplosionResistance(final Explosion p_180428_1_, final World worldIn, final BlockPos p_180428_3_, final IBlockState p_180428_4_) {
        return p_180428_4_.getBlock().getExplosionResistance(this);
    }
    
    public boolean func_174816_a(final Explosion p_174816_1_, final World worldIn, final BlockPos p_174816_3_, final IBlockState p_174816_4_, final float p_174816_5_) {
        return true;
    }
    
    public int getMaxFallHeight() {
        return 3;
    }
    
    public int getTeleportDirection() {
        return this.teleportDirection;
    }
    
    public boolean doesEntityNotTriggerPressurePlate() {
        return false;
    }
    
    public void addEntityCrashInfo(final CrashReportCategory category) {
        category.addCrashSectionCallable("Entity Type", new Callable() {
            private static final String __OBFID = "CL_00001534";
            
            @Override
            public String call() {
                return String.valueOf(EntityList.getEntityString(Entity.this)) + " (" + Entity.this.getClass().getCanonicalName() + ")";
            }
        });
        category.addCrashSection("Entity ID", this.entityId);
        category.addCrashSectionCallable("Entity Name", new Callable() {
            private static final String __OBFID = "CL_00001535";
            
            @Override
            public String call() {
                return Entity.this.getName();
            }
        });
        category.addCrashSection("Entity's Exact location", String.format("%.2f, %.2f, %.2f", this.posX, this.posY, this.posZ));
        category.addCrashSection("Entity's Block location", CrashReportCategory.getCoordinateInfo(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)));
        category.addCrashSection("Entity's Momentum", String.format("%.2f, %.2f, %.2f", this.motionX, this.motionY, this.motionZ));
        category.addCrashSectionCallable("Entity's Rider", new Callable() {
            private static final String __OBFID = "CL_00002259";
            
            public String func_180118_a() {
                return Entity.this.riddenByEntity.toString();
            }
            
            @Override
            public Object call() {
                return this.func_180118_a();
            }
        });
        category.addCrashSectionCallable("Entity's Vehicle", new Callable() {
            private static final String __OBFID = "CL_00002258";
            
            public String func_180116_a() {
                return Entity.this.ridingEntity.toString();
            }
            
            @Override
            public Object call() {
                return this.func_180116_a();
            }
        });
    }
    
    public boolean canRenderOnFire() {
        return this.isBurning();
    }
    
    public UUID getUniqueID() {
        return this.entityUniqueID;
    }
    
    public boolean isPushedByWater() {
        return true;
    }
    
    @Override
    public IChatComponent getDisplayName() {
        final ChatComponentText var1 = new ChatComponentText(this.getName());
        var1.getChatStyle().setChatHoverEvent(this.func_174823_aP());
        var1.getChatStyle().setInsertion(this.getUniqueID().toString());
        return var1;
    }
    
    public void setCustomNameTag(final String p_96094_1_) {
        this.dataWatcher.updateObject(2, p_96094_1_);
    }
    
    public String getCustomNameTag() {
        return this.dataWatcher.getWatchableObjectString(2);
    }
    
    public boolean hasCustomName() {
        return this.dataWatcher.getWatchableObjectString(2).length() > 0;
    }
    
    public void setAlwaysRenderNameTag(final boolean p_174805_1_) {
        this.dataWatcher.updateObject(3, (byte)(byte)(p_174805_1_ ? 1 : 0));
    }
    
    public boolean getAlwaysRenderNameTag() {
        return this.dataWatcher.getWatchableObjectByte(3) == 1;
    }
    
    public void setPositionAndUpdate(final double p_70634_1_, final double p_70634_3_, final double p_70634_5_) {
        this.setLocationAndAngles(p_70634_1_, p_70634_3_, p_70634_5_, this.rotationYaw, this.rotationPitch);
    }
    
    public boolean getAlwaysRenderNameTagForRender() {
        return this.getAlwaysRenderNameTag();
    }
    
    public void func_145781_i(final int p_145781_1_) {
    }
    
    public EnumFacing func_174811_aO() {
        return EnumFacing.getHorizontal(MathHelper.floor_double(this.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3);
    }
    
    protected HoverEvent func_174823_aP() {
        final NBTTagCompound var1 = new NBTTagCompound();
        final String var2 = EntityList.getEntityString(this);
        var1.setString("id", this.getUniqueID().toString());
        if (var2 != null) {
            var1.setString("type", var2);
        }
        var1.setString("name", this.getName());
        return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new ChatComponentText(var1.toString()));
    }
    
    public boolean func_174827_a(final EntityPlayerMP p_174827_1_) {
        return true;
    }
    
    public AxisAlignedBB getEntityBoundingBox() {
        return this.boundingBox;
    }
    
    public void func_174826_a(final AxisAlignedBB p_174826_1_) {
        this.boundingBox = p_174826_1_;
    }
    
    public float getEyeHeight() {
        return this.height * 0.85f;
    }
    
    public boolean isOutsideBorder() {
        return this.isOutsideBorder;
    }
    
    public void setOutsideBorder(final boolean p_174821_1_) {
        this.isOutsideBorder = p_174821_1_;
    }
    
    public boolean func_174820_d(final int p_174820_1_, final ItemStack p_174820_2_) {
        return false;
    }
    
    @Override
    public void addChatMessage(final IChatComponent message) {
    }
    
    @Override
    public boolean canCommandSenderUseCommand(final int permissionLevel, final String command) {
        return true;
    }
    
    @Override
    public BlockPos getPosition() {
        return new BlockPos(this.posX, this.posY + 0.5, this.posZ);
    }
    
    @Override
    public Vec3 getPositionVector() {
        return new Vec3(this.posX, this.posY, this.posZ);
    }
    
    @Override
    public World getEntityWorld() {
        return this.worldObj;
    }
    
    @Override
    public Entity getCommandSenderEntity() {
        return this;
    }
    
    @Override
    public boolean sendCommandFeedback() {
        return false;
    }
    
    @Override
    public void func_174794_a(final CommandResultStats.Type p_174794_1_, final int p_174794_2_) {
        this.field_174837_as.func_179672_a(this, p_174794_1_, p_174794_2_);
    }
    
    public CommandResultStats func_174807_aT() {
        return this.field_174837_as;
    }
    
    public void func_174817_o(final Entity p_174817_1_) {
        this.field_174837_as.func_179671_a(p_174817_1_.func_174807_aT());
    }
    
    public NBTTagCompound func_174819_aU() {
        return null;
    }
    
    public void func_174834_g(final NBTTagCompound p_174834_1_) {
    }
    
    public boolean func_174825_a(final EntityPlayer p_174825_1_, final Vec3 p_174825_2_) {
        return false;
    }
    
    public boolean func_180427_aV() {
        return false;
    }
    
    protected void func_174815_a(final EntityLivingBase p_174815_1_, final Entity p_174815_2_) {
        if (p_174815_2_ instanceof EntityLivingBase) {
            EnchantmentHelper.func_151384_a((EntityLivingBase)p_174815_2_, p_174815_1_);
        }
        EnchantmentHelper.func_151385_b(p_174815_1_, p_174815_2_);
    }
}
