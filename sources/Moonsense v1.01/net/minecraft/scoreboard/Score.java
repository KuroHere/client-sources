// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.scoreboard;

import java.util.List;
import java.util.Comparator;

public class Score
{
    public static final Comparator scoreComparator;
    private final Scoreboard theScoreboard;
    private final ScoreObjective theScoreObjective;
    private final String scorePlayerName;
    private int scorePoints;
    private boolean field_178817_f;
    private boolean field_178818_g;
    private static final String __OBFID = "CL_00000617";
    
    static {
        scoreComparator = new Comparator() {
            private static final String __OBFID = "CL_00000618";
            
            public int compare(final Score p_compare_1_, final Score p_compare_2_) {
                return (p_compare_1_.getScorePoints() > p_compare_2_.getScorePoints()) ? 1 : ((p_compare_1_.getScorePoints() < p_compare_2_.getScorePoints()) ? -1 : p_compare_2_.getPlayerName().compareToIgnoreCase(p_compare_1_.getPlayerName()));
            }
            
            @Override
            public int compare(final Object p_compare_1_, final Object p_compare_2_) {
                return this.compare((Score)p_compare_1_, (Score)p_compare_2_);
            }
        };
    }
    
    public Score(final Scoreboard p_i2309_1_, final ScoreObjective p_i2309_2_, final String p_i2309_3_) {
        this.theScoreboard = p_i2309_1_;
        this.theScoreObjective = p_i2309_2_;
        this.scorePlayerName = p_i2309_3_;
        this.field_178818_g = true;
    }
    
    public void increseScore(final int p_96649_1_) {
        if (this.theScoreObjective.getCriteria().isReadOnly()) {
            throw new IllegalStateException("Cannot modify read-only score");
        }
        this.setScorePoints(this.getScorePoints() + p_96649_1_);
    }
    
    public void decreaseScore(final int p_96646_1_) {
        if (this.theScoreObjective.getCriteria().isReadOnly()) {
            throw new IllegalStateException("Cannot modify read-only score");
        }
        this.setScorePoints(this.getScorePoints() - p_96646_1_);
    }
    
    public void func_96648_a() {
        if (this.theScoreObjective.getCriteria().isReadOnly()) {
            throw new IllegalStateException("Cannot modify read-only score");
        }
        this.increseScore(1);
    }
    
    public int getScorePoints() {
        return this.scorePoints;
    }
    
    public void setScorePoints(final int p_96647_1_) {
        final int var2 = this.scorePoints;
        this.scorePoints = p_96647_1_;
        if (var2 != p_96647_1_ || this.field_178818_g) {
            this.field_178818_g = false;
            this.getScoreScoreboard().func_96536_a(this);
        }
    }
    
    public ScoreObjective getObjective() {
        return this.theScoreObjective;
    }
    
    public String getPlayerName() {
        return this.scorePlayerName;
    }
    
    public Scoreboard getScoreScoreboard() {
        return this.theScoreboard;
    }
    
    public boolean func_178816_g() {
        return this.field_178817_f;
    }
    
    public void func_178815_a(final boolean p_178815_1_) {
        this.field_178817_f = p_178815_1_;
    }
    
    public void func_96651_a(final List p_96651_1_) {
        this.setScorePoints(this.theScoreObjective.getCriteria().func_96635_a(p_96651_1_));
    }
}
