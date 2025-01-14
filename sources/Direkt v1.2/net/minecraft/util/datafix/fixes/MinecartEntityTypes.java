package net.minecraft.util.datafix.fixes;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class MinecartEntityTypes implements IFixableData {
	private static final List<String> MINECART_TYPE_LIST = Lists
			.newArrayList(new String[] { "MinecartRideable", "MinecartChest", "MinecartFurnace", "MinecartTNT", "MinecartSpawner", "MinecartHopper", "MinecartCommandBlock" });

	@Override
	public int getFixVersion() {
		return 106;
	}

	@Override
	public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
		if ("Minecart".equals(compound.getString("id"))) {
			String s = "MinecartRideable";
			int i = compound.getInteger("Type");

			if ((i > 0) && (i < MINECART_TYPE_LIST.size())) {
				s = MINECART_TYPE_LIST.get(i);
			}

			compound.setString("id", s);
			compound.removeTag("Type");
		}

		return compound;
	}
}
