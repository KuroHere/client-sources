package net.minecraft.util;

public class ChatAllowedCharacters
{
  public static final char[] allowedCharactersArray = { '/', '\n', '\r', '\t', '\000', '\f', '`', '?', '*', '\\', '<', '>', '|', '"', ':' };
  private static final String __OBFID = "CL_00001606";
  
  public ChatAllowedCharacters() {}
  
  public static boolean isAllowedCharacter(char character)
  {
    return (character != '§') && (character >= ' ') && (character != '');
  }
  
  public static String filterAllowedCharacters(String input)
  {
    StringBuilder var1 = new StringBuilder();
    char[] var2 = input.toCharArray();
    int var3 = var2.length;
    for (int var4 = 0; var4 < var3; var4++)
    {
      char var5 = var2[var4];
      if (isAllowedCharacter(var5)) {
        var1.append(var5);
      }
    }
    return var1.toString();
  }
}
