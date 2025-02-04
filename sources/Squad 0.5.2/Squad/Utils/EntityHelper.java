package Squad.Utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Multimap;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public final class EntityHelper
{
    private static Minecraft mc;

    static {
        EntityHelper.mc = Minecraft.getMinecraft();
    }

    public static int getBestWeapon(final Entity target) {
        final int originalSlot = EntityHelper.mc.thePlayer.inventory.currentItem;
        int weaponSlot = -1;
        float weaponDamage = 1.0f;
        for (byte slot = 0; slot < 9; ++slot) {
            EntityHelper.mc.thePlayer.inventory.currentItem = slot;
            final ItemStack itemStack = EntityHelper.mc.thePlayer.getHeldItem();
            if (itemStack != null) {
                float damage = getItemDamage(itemStack);
                damage += EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED);
                if (damage > weaponDamage) {
                    weaponDamage = damage;
                    weaponSlot = slot;
                }
            }
        }
        if (weaponSlot != -1) {
            return weaponSlot;
        }
        return originalSlot;
    }

    public static float[] getFacingRotations(final int x, final int y, final int z, final EnumFacing facing) {
        final Entity temp = new EntitySnowball(EntityHelper.mc.theWorld);
        temp.posX = x + 0.5;
        temp.posY = y + 0.5;
        temp.posZ = z + 0.5;
        final Entity entity = temp;
        entity.posX += facing.getDirectionVec().getX() * 0.25;
        final Entity entity2 = temp;
        entity2.posY += facing.getDirectionVec().getY() * 0.25;
        final Entity entity3 = temp;
        entity3.posZ += facing.getDirectionVec().getZ() * 0.25;
        return getAngles(temp);
    }



    public static float getYawChangeToEntity(final Entity entity) {
        final double deltaX = entity.posX - EntityHelper.mc.thePlayer.posX;
        final double deltaZ = entity.posZ - EntityHelper.mc.thePlayer.posZ;
        double yawToEntity;
        if (deltaZ < 0.0 && deltaX < 0.0) {
            yawToEntity = 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        }
        else if (deltaZ < 0.0 && deltaX > 0.0) {
            yawToEntity = -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        }
        else {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
        return MathHelper.wrapAngleTo180_float(-(EntityHelper.mc.thePlayer.rotationYaw - (float)yawToEntity));
    }

    public static float getPitchChangeToEntity(final Entity entity) {
        final double deltaX = entity.posX - EntityHelper.mc.thePlayer.posX;
        final double deltaZ = entity.posZ - EntityHelper.mc.thePlayer.posZ;
        final double deltaY = entity.posY - 1.6 + entity.getEyeHeight() - EntityHelper.mc.thePlayer.posY;
        final double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        final double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return -MathHelper.wrapAngleTo180_float(EntityHelper.mc.thePlayer.rotationPitch - (float)pitchToEntity);
    }

    public static float[] getAngles(final Entity e) {
        return new float[] { getYawChangeToEntity(e) + EntityHelper.mc.thePlayer.rotationYaw, getPitchChangeToEntity(e) + EntityHelper.mc.thePlayer.rotationPitch };
    }

    public static float[] getEntityRotations(final EntityPlayer player, final Entity target) {
        final double posX = target.posX - player.posX;
        final double posY = target.posY + target.getEyeHeight() - (player.posY + player.getEyeHeight());
        final double posZ = target.posZ - player.posZ;
        final double var14 = MathHelper.sqrt_double(posX * posX + posZ * posZ);
        final float yaw = (float)(Math.atan2(posZ, posX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(posY, var14) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }

    private float[] getBlockRotations(final int x, final int y, final int z) {
        final double var4 = x - EntityHelper.mc.thePlayer.posX + 0.5;
        final double var5 = z - EntityHelper.mc.thePlayer.posZ + 0.5;
        final double var6 = y - (EntityHelper.mc.thePlayer.posY + EntityHelper.mc.thePlayer.getEyeHeight() - 1.0);
        final double var7 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        final float var8 = (float)(Math.atan2(var5, var4) * 180.0 / 3.141592653589793) - 90.0f;
        return new float[] { var8, (float)(-(Math.atan2(var6, var7) * 180.0 / 3.141592653589793)) };
    }

    private static float getItemDamage(final ItemStack itemStack) {
        final Multimap multimap = itemStack.getAttributeModifiers();
        if (!multimap.isEmpty()) {
            final Iterator iterator = multimap.entries().iterator();
            if (iterator.hasNext()) {
                final Map.Entry entry = (Entry) iterator.next();
                final AttributeModifier attributeModifier = (AttributeModifier) entry.getValue();
                double damage;
                if (attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2) {
                    damage = attributeModifier.getAmount();
                }
                else {
                    damage = attributeModifier.getAmount() * 100.0;
                }
                if (attributeModifier.getAmount() > 1.0) {
                    return 1.0f + (float)damage;
                }
                return 1.0f;
            }
        }
        return 1.0f;
    }
}
