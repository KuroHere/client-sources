// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.scoreboard;

import java.util.Iterator;
import java.util.Collection;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.nbt.NBTTagList;
import org.apache.logging.log4j.LogManager;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.WorldSavedData;

public class ScoreboardSaveData extends WorldSavedData
{
    private static final Logger logger;
    private Scoreboard theScoreboard;
    private NBTTagCompound field_96506_b;
    private static final String __OBFID = "CL_00000620";
    
    static {
        logger = LogManager.getLogger();
    }
    
    public ScoreboardSaveData() {
        this("scoreboard");
    }
    
    public ScoreboardSaveData(final String p_i2310_1_) {
        super(p_i2310_1_);
    }
    
    public void func_96499_a(final Scoreboard p_96499_1_) {
        this.theScoreboard = p_96499_1_;
        if (this.field_96506_b != null) {
            this.readFromNBT(this.field_96506_b);
        }
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        if (this.theScoreboard == null) {
            this.field_96506_b = nbt;
        }
        else {
            this.func_96501_b(nbt.getTagList("Objectives", 10));
            this.func_96500_c(nbt.getTagList("PlayerScores", 10));
            if (nbt.hasKey("DisplaySlots", 10)) {
                this.func_96504_c(nbt.getCompoundTag("DisplaySlots"));
            }
            if (nbt.hasKey("Teams", 9)) {
                this.func_96498_a(nbt.getTagList("Teams", 10));
            }
        }
    }
    
    protected void func_96498_a(final NBTTagList p_96498_1_) {
        for (int var2 = 0; var2 < p_96498_1_.tagCount(); ++var2) {
            final NBTTagCompound var3 = p_96498_1_.getCompoundTagAt(var2);
            final ScorePlayerTeam var4 = this.theScoreboard.createTeam(var3.getString("Name"));
            var4.setTeamName(var3.getString("DisplayName"));
            if (var3.hasKey("TeamColor", 8)) {
                var4.func_178774_a(EnumChatFormatting.getValueByName(var3.getString("TeamColor")));
            }
            var4.setNamePrefix(var3.getString("Prefix"));
            var4.setNameSuffix(var3.getString("Suffix"));
            if (var3.hasKey("AllowFriendlyFire", 99)) {
                var4.setAllowFriendlyFire(var3.getBoolean("AllowFriendlyFire"));
            }
            if (var3.hasKey("SeeFriendlyInvisibles", 99)) {
                var4.setSeeFriendlyInvisiblesEnabled(var3.getBoolean("SeeFriendlyInvisibles"));
            }
            if (var3.hasKey("NameTagVisibility", 8)) {
                final Team.EnumVisible var5 = Team.EnumVisible.func_178824_a(var3.getString("NameTagVisibility"));
                if (var5 != null) {
                    var4.func_178772_a(var5);
                }
            }
            if (var3.hasKey("DeathMessageVisibility", 8)) {
                final Team.EnumVisible var5 = Team.EnumVisible.func_178824_a(var3.getString("DeathMessageVisibility"));
                if (var5 != null) {
                    var4.func_178773_b(var5);
                }
            }
            this.func_96502_a(var4, var3.getTagList("Players", 8));
        }
    }
    
    protected void func_96502_a(final ScorePlayerTeam p_96502_1_, final NBTTagList p_96502_2_) {
        for (int var3 = 0; var3 < p_96502_2_.tagCount(); ++var3) {
            this.theScoreboard.func_151392_a(p_96502_2_.getStringTagAt(var3), p_96502_1_.getRegisteredName());
        }
    }
    
    protected void func_96504_c(final NBTTagCompound p_96504_1_) {
        for (int var2 = 0; var2 < 19; ++var2) {
            if (p_96504_1_.hasKey("slot_" + var2, 8)) {
                final String var3 = p_96504_1_.getString("slot_" + var2);
                final ScoreObjective var4 = this.theScoreboard.getObjective(var3);
                this.theScoreboard.setObjectiveInDisplaySlot(var2, var4);
            }
        }
    }
    
    protected void func_96501_b(final NBTTagList p_96501_1_) {
        for (int var2 = 0; var2 < p_96501_1_.tagCount(); ++var2) {
            final NBTTagCompound var3 = p_96501_1_.getCompoundTagAt(var2);
            final IScoreObjectiveCriteria var4 = IScoreObjectiveCriteria.INSTANCES.get(var3.getString("CriteriaName"));
            if (var4 != null) {
                final ScoreObjective var5 = this.theScoreboard.addScoreObjective(var3.getString("Name"), var4);
                var5.setDisplayName(var3.getString("DisplayName"));
                var5.func_178767_a(IScoreObjectiveCriteria.EnumRenderType.func_178795_a(var3.getString("RenderType")));
            }
        }
    }
    
    protected void func_96500_c(final NBTTagList p_96500_1_) {
        for (int var2 = 0; var2 < p_96500_1_.tagCount(); ++var2) {
            final NBTTagCompound var3 = p_96500_1_.getCompoundTagAt(var2);
            final ScoreObjective var4 = this.theScoreboard.getObjective(var3.getString("Objective"));
            final Score var5 = this.theScoreboard.getValueFromObjective(var3.getString("Name"), var4);
            var5.setScorePoints(var3.getInteger("Score"));
            if (var3.hasKey("Locked")) {
                var5.func_178815_a(var3.getBoolean("Locked"));
            }
        }
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound nbt) {
        if (this.theScoreboard == null) {
            ScoreboardSaveData.logger.warn("Tried to save scoreboard without having a scoreboard...");
        }
        else {
            nbt.setTag("Objectives", this.func_96505_b());
            nbt.setTag("PlayerScores", this.func_96503_e());
            nbt.setTag("Teams", this.func_96496_a());
            this.func_96497_d(nbt);
        }
    }
    
    protected NBTTagList func_96496_a() {
        final NBTTagList var1 = new NBTTagList();
        final Collection var2 = this.theScoreboard.getTeams();
        for (final ScorePlayerTeam var4 : var2) {
            final NBTTagCompound var5 = new NBTTagCompound();
            var5.setString("Name", var4.getRegisteredName());
            var5.setString("DisplayName", var4.func_96669_c());
            if (var4.func_178775_l().func_175746_b() >= 0) {
                var5.setString("TeamColor", var4.func_178775_l().getFriendlyName());
            }
            var5.setString("Prefix", var4.getColorPrefix());
            var5.setString("Suffix", var4.getColorSuffix());
            var5.setBoolean("AllowFriendlyFire", var4.getAllowFriendlyFire());
            var5.setBoolean("SeeFriendlyInvisibles", var4.func_98297_h());
            var5.setString("NameTagVisibility", var4.func_178770_i().field_178830_e);
            var5.setString("DeathMessageVisibility", var4.func_178771_j().field_178830_e);
            final NBTTagList var6 = new NBTTagList();
            for (final String var8 : var4.getMembershipCollection()) {
                var6.appendTag(new NBTTagString(var8));
            }
            var5.setTag("Players", var6);
            var1.appendTag(var5);
        }
        return var1;
    }
    
    protected void func_96497_d(final NBTTagCompound p_96497_1_) {
        final NBTTagCompound var2 = new NBTTagCompound();
        boolean var3 = false;
        for (int var4 = 0; var4 < 19; ++var4) {
            final ScoreObjective var5 = this.theScoreboard.getObjectiveInDisplaySlot(var4);
            if (var5 != null) {
                var2.setString("slot_" + var4, var5.getName());
                var3 = true;
            }
        }
        if (var3) {
            p_96497_1_.setTag("DisplaySlots", var2);
        }
    }
    
    protected NBTTagList func_96505_b() {
        final NBTTagList var1 = new NBTTagList();
        final Collection var2 = this.theScoreboard.getScoreObjectives();
        for (final ScoreObjective var4 : var2) {
            if (var4.getCriteria() != null) {
                final NBTTagCompound var5 = new NBTTagCompound();
                var5.setString("Name", var4.getName());
                var5.setString("CriteriaName", var4.getCriteria().getName());
                var5.setString("DisplayName", var4.getDisplayName());
                var5.setString("RenderType", var4.func_178766_e().func_178796_a());
                var1.appendTag(var5);
            }
        }
        return var1;
    }
    
    protected NBTTagList func_96503_e() {
        final NBTTagList var1 = new NBTTagList();
        final Collection var2 = this.theScoreboard.func_96528_e();
        for (final Score var4 : var2) {
            if (var4.getObjective() != null) {
                final NBTTagCompound var5 = new NBTTagCompound();
                var5.setString("Name", var4.getPlayerName());
                var5.setString("Objective", var4.getObjective().getName());
                var5.setInteger("Score", var4.getScorePoints());
                var5.setBoolean("Locked", var4.func_178816_g());
                var1.appendTag(var5);
            }
        }
        return var1;
    }
}
