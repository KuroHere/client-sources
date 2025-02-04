package net.minecraft.scoreboard;

import java.util.*;
import net.minecraft.entity.player.*;

public class ScoreDummyCriteria implements IScoreObjectiveCriteria
{
    private final String dummyName;
    
    public ScoreDummyCriteria(final String dummyName) {
        this.dummyName = dummyName;
        IScoreObjectiveCriteria.INSTANCES.put(dummyName, this);
    }
    
    @Override
    public String getName() {
        return this.dummyName;
    }
    
    @Override
    public int func_96635_a(final List<EntityPlayer> list) {
        return "".length();
    }
    
    @Override
    public boolean isReadOnly() {
        return "".length() != 0;
    }
    
    private static String I(final String s, final String s2) {
        final StringBuilder sb = new StringBuilder();
        final char[] charArray = s2.toCharArray();
        int length = "".length();
        final char[] charArray2 = s.toCharArray();
        final int length2 = charArray2.length;
        int i = "".length();
        while (i < length2) {
            sb.append((char)(charArray2[i] ^ charArray[length % charArray.length]));
            ++length;
            ++i;
            "".length();
            if (2 < 0) {
                throw null;
            }
        }
        return sb.toString();
    }
    
    @Override
    public EnumRenderType getRenderType() {
        return EnumRenderType.INTEGER;
    }
}
