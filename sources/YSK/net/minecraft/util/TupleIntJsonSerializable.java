package net.minecraft.util;

public class TupleIntJsonSerializable
{
    private IJsonSerializable jsonSerializableValue;
    private int integerValue;
    
    public int getIntegerValue() {
        return this.integerValue;
    }
    
    public void setJsonSerializableValue(final IJsonSerializable jsonSerializableValue) {
        this.jsonSerializableValue = jsonSerializableValue;
    }
    
    public void setIntegerValue(final int integerValue) {
        this.integerValue = integerValue;
    }
    
    public <T extends IJsonSerializable> T getJsonSerializableValue() {
        return (T)this.jsonSerializableValue;
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
            if (false) {
                throw null;
            }
        }
        return sb.toString();
    }
}
