package net.minecraft.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemLingeringPotion extends ItemPotion {
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return I18n.translateToLocal(PotionUtils.getPotionFromItem(stack).getNamePrefixed("lingering_potion.effect."));
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		PotionUtils.addPotionTooltip(stack, tooltip, 0.25F);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (!playerIn.capabilities.isCreativeMode) {
			--itemStackIn.stackSize;
		}

		worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_LINGERINGPOTION_THROW, SoundCategory.NEUTRAL, 0.5F,
				0.4F / ((itemRand.nextFloat() * 0.4F) + 0.8F));

		if (!worldIn.isRemote) {
			EntityPotion entitypotion = new EntityPotion(worldIn, playerIn, itemStackIn);
			entitypotion.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, -20.0F, 0.5F, 1.0F);
			worldIn.spawnEntityInWorld(entitypotion);
		}

		playerIn.addStat(StatList.getObjectUseStats(this));
		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}
}
