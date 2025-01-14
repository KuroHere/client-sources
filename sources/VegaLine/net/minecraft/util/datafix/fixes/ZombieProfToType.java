/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.util.datafix.fixes;

import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ZombieProfToType
implements IFixableData {
    private static final Random RANDOM = new Random();

    @Override
    public int getFixVersion() {
        return 502;
    }

    @Override
    public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
        if ("Zombie".equals(compound.getString("id")) && compound.getBoolean("IsVillager")) {
            if (!compound.hasKey("ZombieType", 99)) {
                int i = -1;
                if (compound.hasKey("VillagerProfession", 99)) {
                    try {
                        i = this.func_191277_a(compound.getInteger("VillagerProfession"));
                    } catch (RuntimeException runtimeException) {
                        // empty catch block
                    }
                }
                if (i == -1) {
                    i = this.func_191277_a(RANDOM.nextInt(6));
                }
                compound.setInteger("ZombieType", i);
            }
            compound.removeTag("IsVillager");
        }
        return compound;
    }

    private int func_191277_a(int p_191277_1_) {
        return p_191277_1_ >= 0 && p_191277_1_ < 6 ? p_191277_1_ : -1;
    }
}

