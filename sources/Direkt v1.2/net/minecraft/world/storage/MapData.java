package net.minecraft.world.storage;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec4b;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class MapData extends WorldSavedData {
	public int xCenter;
	public int zCenter;
	public byte dimension;
	public boolean trackingPosition;
	public byte scale;

	/** colours */
	public byte[] colors = new byte[16384];
	public List<MapData.MapInfo> playersArrayList = Lists.<MapData.MapInfo> newArrayList();
	private final Map<EntityPlayer, MapData.MapInfo> playersHashMap = Maps.<EntityPlayer, MapData.MapInfo> newHashMap();
	public Map<String, Vec4b> mapDecorations = Maps.<String, Vec4b> newLinkedHashMap();

	public MapData(String mapname) {
		super(mapname);
	}

	public void calculateMapCenter(double x, double z, int mapScale) {
		int i = 128 * (1 << mapScale);
		int j = MathHelper.floor_double((x + 64.0D) / i);
		int k = MathHelper.floor_double((z + 64.0D) / i);
		this.xCenter = ((j * i) + (i / 2)) - 64;
		this.zCenter = ((k * i) + (i / 2)) - 64;
	}

	/**
	 * reads in data from the NBTTagCompound into this MapDataBase
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.dimension = nbt.getByte("dimension");
		this.xCenter = nbt.getInteger("xCenter");
		this.zCenter = nbt.getInteger("zCenter");
		this.scale = nbt.getByte("scale");
		this.scale = (byte) MathHelper.clamp_int(this.scale, 0, 4);

		if (nbt.hasKey("trackingPosition", 1)) {
			this.trackingPosition = nbt.getBoolean("trackingPosition");
		} else {
			this.trackingPosition = true;
		}

		int i = nbt.getShort("width");
		int j = nbt.getShort("height");

		if ((i == 128) && (j == 128)) {
			this.colors = nbt.getByteArray("colors");
		} else {
			byte[] abyte = nbt.getByteArray("colors");
			this.colors = new byte[16384];
			int k = (128 - i) / 2;
			int l = (128 - j) / 2;

			for (int i1 = 0; i1 < j; ++i1) {
				int j1 = i1 + l;

				if ((j1 >= 0) || (j1 < 128)) {
					for (int k1 = 0; k1 < i; ++k1) {
						int l1 = k1 + k;

						if ((l1 >= 0) || (l1 < 128)) {
							this.colors[l1 + (j1 * 128)] = abyte[k1 + (i1 * i)];
						}
					}
				}
			}
		}
	}

	@Override
	public NBTTagCompound func_189551_b(NBTTagCompound p_189551_1_) {
		p_189551_1_.setByte("dimension", this.dimension);
		p_189551_1_.setInteger("xCenter", this.xCenter);
		p_189551_1_.setInteger("zCenter", this.zCenter);
		p_189551_1_.setByte("scale", this.scale);
		p_189551_1_.setShort("width", (short) 128);
		p_189551_1_.setShort("height", (short) 128);
		p_189551_1_.setByteArray("colors", this.colors);
		p_189551_1_.setBoolean("trackingPosition", this.trackingPosition);
		return p_189551_1_;
	}

	/**
	 * Adds the player passed to the list of visible players and checks to see which players are visible
	 */
	public void updateVisiblePlayers(EntityPlayer player, ItemStack mapStack) {
		if (!this.playersHashMap.containsKey(player)) {
			MapData.MapInfo mapdata$mapinfo = new MapData.MapInfo(player);
			this.playersHashMap.put(player, mapdata$mapinfo);
			this.playersArrayList.add(mapdata$mapinfo);
		}

		if (!player.inventory.hasItemStack(mapStack)) {
			this.mapDecorations.remove(player.getName());
		}

		for (int i = 0; i < this.playersArrayList.size(); ++i) {
			MapData.MapInfo mapdata$mapinfo1 = this.playersArrayList.get(i);

			if (!mapdata$mapinfo1.entityplayerObj.isDead && (mapdata$mapinfo1.entityplayerObj.inventory.hasItemStack(mapStack) || mapStack.isOnItemFrame())) {
				if (!mapStack.isOnItemFrame() && (mapdata$mapinfo1.entityplayerObj.dimension == this.dimension) && this.trackingPosition) {
					this.updateDecorations(0, mapdata$mapinfo1.entityplayerObj.worldObj, mapdata$mapinfo1.entityplayerObj.getName(), mapdata$mapinfo1.entityplayerObj.posX,
							mapdata$mapinfo1.entityplayerObj.posZ, mapdata$mapinfo1.entityplayerObj.rotationYaw);
				}
			} else {
				this.playersHashMap.remove(mapdata$mapinfo1.entityplayerObj);
				this.playersArrayList.remove(mapdata$mapinfo1);
			}
		}

		if (mapStack.isOnItemFrame() && this.trackingPosition) {
			EntityItemFrame entityitemframe = mapStack.getItemFrame();
			BlockPos blockpos = entityitemframe.getHangingPosition();
			this.updateDecorations(1, player.worldObj, "frame-" + entityitemframe.getEntityId(), blockpos.getX(), blockpos.getZ(), entityitemframe.facingDirection.getHorizontalIndex() * 90);
		}

		if (mapStack.hasTagCompound() && mapStack.getTagCompound().hasKey("Decorations", 9)) {
			NBTTagList nbttaglist = mapStack.getTagCompound().getTagList("Decorations", 10);

			for (int j = 0; j < nbttaglist.tagCount(); ++j) {
				NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(j);

				if (!this.mapDecorations.containsKey(nbttagcompound.getString("id"))) {
					this.updateDecorations(nbttagcompound.getByte("type"), player.worldObj, nbttagcompound.getString("id"), nbttagcompound.getDouble("x"), nbttagcompound.getDouble("z"),
							nbttagcompound.getDouble("rot"));
				}
			}
		}
	}

	private void updateDecorations(int type, World worldIn, String entityIdentifier, double worldX, double worldZ, double rotation) {
		int i = 1 << this.scale;
		float f = (float) (worldX - this.xCenter) / i;
		float f1 = (float) (worldZ - this.zCenter) / i;
		byte b0 = (byte) ((int) (f * 2.0F + 0.5D));
		byte b1 = (byte) ((int) (f1 * 2.0F + 0.5D));
		int j = 63;
		byte b2;

		if ((f >= -63.0F) && (f1 >= -63.0F) && (f <= 63.0F) && (f1 <= 63.0F)) {
			rotation = rotation + (rotation < 0.0D ? -8.0D : 8.0D);
			b2 = (byte) ((int) ((rotation * 16.0D) / 360.0D));

			if (this.dimension < 0) {
				int k = (int) (worldIn.getWorldInfo().getWorldTime() / 10L);
				b2 = (byte) ((((k * k * 34187121) + (k * 121)) >> 15) & 15);
			}
		} else {
			if ((Math.abs(f) >= 320.0F) || (Math.abs(f1) >= 320.0F)) {
				this.mapDecorations.remove(entityIdentifier);
				return;
			}

			type = 6;
			b2 = 0;

			if (f <= -63.0F) {
				b0 = -128;
			}

			if (f1 <= -63.0F) {
				b1 = -128;
			}

			if (f >= 63.0F) {
				b0 = 127;
			}

			if (f1 >= 63.0F) {
				b1 = 127;
			}
		}

		this.mapDecorations.put(entityIdentifier, new Vec4b((byte) type, b0, b1, b2));
	}

	@Nullable
	public Packet<?> getMapPacket(ItemStack mapStack, World worldIn, EntityPlayer player) {
		MapData.MapInfo mapdata$mapinfo = this.playersHashMap.get(player);
		return mapdata$mapinfo == null ? null : mapdata$mapinfo.getPacket(mapStack);
	}

	public void updateMapData(int x, int y) {
		super.markDirty();

		for (MapData.MapInfo mapdata$mapinfo : this.playersArrayList) {
			mapdata$mapinfo.update(x, y);
		}
	}

	public MapData.MapInfo getMapInfo(EntityPlayer player) {
		MapData.MapInfo mapdata$mapinfo = this.playersHashMap.get(player);

		if (mapdata$mapinfo == null) {
			mapdata$mapinfo = new MapData.MapInfo(player);
			this.playersHashMap.put(player, mapdata$mapinfo);
			this.playersArrayList.add(mapdata$mapinfo);
		}

		return mapdata$mapinfo;
	}

	public class MapInfo {
		public final EntityPlayer entityplayerObj;
		private boolean isDirty = true;
		private int minX;
		private int minY;
		private int maxX = 127;
		private int maxY = 127;
		private int tick;
		public int step;

		public MapInfo(EntityPlayer player) {
			this.entityplayerObj = player;
		}

		public Packet<?> getPacket(ItemStack stack) {
			if (this.isDirty) {
				this.isDirty = false;
				return new SPacketMaps(stack.getMetadata(), MapData.this.scale, MapData.this.trackingPosition, MapData.this.mapDecorations.values(), MapData.this.colors, this.minX, this.minY,
						(this.maxX + 1) - this.minX, (this.maxY + 1) - this.minY);
			} else {
				return (this.tick++ % 5) == 0
						? new SPacketMaps(stack.getMetadata(), MapData.this.scale, MapData.this.trackingPosition, MapData.this.mapDecorations.values(), MapData.this.colors, 0, 0, 0, 0) : null;
			}
		}

		public void update(int x, int y) {
			if (this.isDirty) {
				this.minX = Math.min(this.minX, x);
				this.minY = Math.min(this.minY, y);
				this.maxX = Math.max(this.maxX, x);
				this.maxY = Math.max(this.maxY, y);
			} else {
				this.isDirty = true;
				this.minX = x;
				this.minY = y;
				this.maxX = x;
				this.maxY = y;
			}
		}
	}
}
