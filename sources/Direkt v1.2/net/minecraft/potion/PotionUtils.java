package net.minecraft.potion;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.src.Config;
import net.minecraft.src.CustomColors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public class PotionUtils {
	public static List<PotionEffect> getEffectsFromStack(ItemStack stack) {
		return getEffectsFromTag(stack.getTagCompound());
	}

	public static List<PotionEffect> mergeEffects(PotionType potionIn, Collection<PotionEffect> effects) {
		List<PotionEffect> list = Lists.<PotionEffect> newArrayList();
		list.addAll(potionIn.getEffects());
		list.addAll(effects);
		return list;
	}

	public static List<PotionEffect> getEffectsFromTag(@Nullable NBTTagCompound tag) {
		List<PotionEffect> list = Lists.<PotionEffect> newArrayList();
		list.addAll(getPotionTypeFromNBT(tag).getEffects());
		addCustomPotionEffectToList(tag, list);
		return list;
	}

	public static List<PotionEffect> getFullEffectsFromItem(ItemStack itemIn) {
		return getFullEffectsFromTag(itemIn.getTagCompound());
	}

	public static List<PotionEffect> getFullEffectsFromTag(@Nullable NBTTagCompound tag) {
		List<PotionEffect> list = Lists.<PotionEffect> newArrayList();
		addCustomPotionEffectToList(tag, list);
		return list;
	}

	public static void addCustomPotionEffectToList(@Nullable NBTTagCompound tag, List<PotionEffect> effectList) {
		if ((tag != null) && tag.hasKey("CustomPotionEffects", 9)) {
			NBTTagList nbttaglist = tag.getTagList("CustomPotionEffects", 10);

			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
				PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);

				if (potioneffect != null) {
					effectList.add(potioneffect);
				}
			}
		}
	}

	public static int getPotionColor(PotionType potionIn) {
		return getPotionColorFromEffectList(potionIn.getEffects());
	}

	public static int getPotionColorFromEffectList(Collection<PotionEffect> effects) {
		int i = 3694022;

		if (effects.isEmpty()) {
			return Config.isCustomColors() ? CustomColors.getPotionColor((Potion) null, i) : 3694022;
		} else {
			float f = 0.0F;
			float f1 = 0.0F;
			float f2 = 0.0F;
			int j = 0;

			for (PotionEffect potioneffect : effects) {
				if (potioneffect.doesShowParticles()) {
					int k = potioneffect.getPotion().getLiquidColor();

					if (Config.isCustomColors()) {
						k = CustomColors.getPotionColor(potioneffect.getPotion(), k);
					}

					int l = potioneffect.getAmplifier() + 1;
					f += l * ((k >> 16) & 255) / 255.0F;
					f1 += l * ((k >> 8) & 255) / 255.0F;
					f2 += l * ((k >> 0) & 255) / 255.0F;
					j += l;
				}
			}

			if (j == 0) {
				return 0;
			} else {
				f = (f / j) * 255.0F;
				f1 = (f1 / j) * 255.0F;
				f2 = (f2 / j) * 255.0F;
				return ((int) f << 16) | ((int) f1 << 8) | (int) f2;
			}
		}
	}

	public static PotionType getPotionFromItem(ItemStack itemIn) {
		return getPotionTypeFromNBT(itemIn.getTagCompound());
	}

	/**
	 * If no correct potion is found, returns the default one : PotionTypes.water
	 */
	public static PotionType getPotionTypeFromNBT(@Nullable NBTTagCompound tag) {
		return tag == null ? PotionTypes.WATER : PotionType.getPotionTypeForName(tag.getString("Potion"));
	}

	public static ItemStack addPotionToItemStack(ItemStack itemIn, PotionType potionIn) {
		ResourceLocation resourcelocation = PotionType.REGISTRY.getNameForObject(potionIn);

		if (resourcelocation != null) {
			NBTTagCompound nbttagcompound = itemIn.hasTagCompound() ? itemIn.getTagCompound() : new NBTTagCompound();
			nbttagcompound.setString("Potion", resourcelocation.toString());
			itemIn.setTagCompound(nbttagcompound);
		}

		return itemIn;
	}

	public static ItemStack appendEffects(ItemStack itemIn, Collection<PotionEffect> effects) {
		if (effects.isEmpty()) {
			return itemIn;
		} else {
			NBTTagCompound nbttagcompound = Objects.firstNonNull(itemIn.getTagCompound(), new NBTTagCompound());
			NBTTagList nbttaglist = nbttagcompound.getTagList("CustomPotionEffects", 9);

			for (PotionEffect potioneffect : effects) {
				nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
			}

			nbttagcompound.setTag("CustomPotionEffects", nbttaglist);
			itemIn.setTagCompound(nbttagcompound);
			return itemIn;
		}
	}

	public static void addPotionTooltip(ItemStack itemIn, List<String> lores, float durationFactor) {
		List<PotionEffect> list = getEffectsFromStack(itemIn);
		List<Tuple<String, AttributeModifier>> list1 = Lists.<Tuple<String, AttributeModifier>> newArrayList();

		if (list.isEmpty()) {
			String s = I18n.translateToLocal("effect.none").trim();
			lores.add(TextFormatting.GRAY + s);
		} else {
			for (PotionEffect potioneffect : list) {
				String s1 = I18n.translateToLocal(potioneffect.getEffectName()).trim();
				Potion potion = potioneffect.getPotion();
				Map<IAttribute, AttributeModifier> map = potion.getAttributeModifierMap();

				if (!map.isEmpty()) {
					for (Entry<IAttribute, AttributeModifier> entry : map.entrySet()) {
						AttributeModifier attributemodifier = entry.getValue();
						AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), potion.getAttributeModifierAmount(potioneffect.getAmplifier(), attributemodifier),
								attributemodifier.getOperation());
						list1.add(new Tuple(entry.getKey().getAttributeUnlocalizedName(), attributemodifier1));
					}
				}

				if (potioneffect.getAmplifier() > 0) {
					s1 = s1 + " " + I18n.translateToLocal("potion.potency." + potioneffect.getAmplifier()).trim();
				}

				if (potioneffect.getDuration() > 20) {
					s1 = s1 + " (" + Potion.getPotionDurationString(potioneffect, durationFactor) + ")";
				}

				if (potion.isBadEffect()) {
					lores.add(TextFormatting.RED + s1);
				} else {
					lores.add(TextFormatting.BLUE + s1);
				}
			}
		}

		if (!list1.isEmpty()) {
			lores.add("");
			lores.add(TextFormatting.DARK_PURPLE + I18n.translateToLocal("potion.whenDrank"));

			for (Tuple<String, AttributeModifier> tuple : list1) {
				AttributeModifier attributemodifier2 = tuple.getSecond();
				double d0 = attributemodifier2.getAmount();
				double d1;

				if ((attributemodifier2.getOperation() != 1) && (attributemodifier2.getOperation() != 2)) {
					d1 = attributemodifier2.getAmount();
				} else {
					d1 = attributemodifier2.getAmount() * 100.0D;
				}

				if (d0 > 0.0D) {
					lores.add(TextFormatting.BLUE + I18n.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier2.getOperation(),
							new Object[] { ItemStack.DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + tuple.getFirst()) }));
				} else if (d0 < 0.0D) {
					d1 = d1 * -1.0D;
					lores.add(TextFormatting.RED + I18n.translateToLocalFormatted("attribute.modifier.take." + attributemodifier2.getOperation(),
							new Object[] { ItemStack.DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + tuple.getFirst()) }));
				}
			}
		}
	}
}
