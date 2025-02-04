package org.spongycastle.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Vector;




public final class Strings
{
  private static String LINE_SEPARATOR;
  
  static
  {
    try
    {
      LINE_SEPARATOR = (String)AccessController.doPrivileged(new PrivilegedAction()
      {

        public String run()
        {
          return System.getProperty("line.separator");
        }
        

      });
    }
    catch (Exception e)
    {
      try
      {
        LINE_SEPARATOR = String.format("%n", new Object[0]);
      }
      catch (Exception ef)
      {
        LINE_SEPARATOR = "\n";
      }
    }
  }
  
  public static String fromUTF8ByteArray(byte[] bytes)
  {
    int i = 0;
    int length = 0;
    
    while (i < bytes.length)
    {
      length++;
      if ((bytes[i] & 0xF0) == 240)
      {

        length++;
        i += 4;
      }
      else if ((bytes[i] & 0xE0) == 224)
      {
        i += 3;
      }
      else if ((bytes[i] & 0xC0) == 192)
      {
        i += 2;
      }
      else
      {
        i++;
      }
    }
    
    char[] cs = new char[length];
    
    i = 0;
    length = 0;
    
    while (i < bytes.length)
    {
      char ch;
      
      if ((bytes[i] & 0xF0) == 240)
      {
        int codePoint = (bytes[i] & 0x3) << 18 | (bytes[(i + 1)] & 0x3F) << 12 | (bytes[(i + 2)] & 0x3F) << 6 | bytes[(i + 3)] & 0x3F;
        int U = codePoint - 65536;
        char W1 = (char)(0xD800 | U >> 10);
        char W2 = (char)(0xDC00 | U & 0x3FF);
        cs[(length++)] = W1;
        char ch = W2;
        i += 4;
      }
      else if ((bytes[i] & 0xE0) == 224)
      {
        char ch = (char)((bytes[i] & 0xF) << 12 | (bytes[(i + 1)] & 0x3F) << 6 | bytes[(i + 2)] & 0x3F);
        
        i += 3;
      }
      else if ((bytes[i] & 0xD0) == 208)
      {
        char ch = (char)((bytes[i] & 0x1F) << 6 | bytes[(i + 1)] & 0x3F);
        i += 2;
      }
      else if ((bytes[i] & 0xC0) == 192)
      {
        char ch = (char)((bytes[i] & 0x1F) << 6 | bytes[(i + 1)] & 0x3F);
        i += 2;
      }
      else
      {
        ch = (char)(bytes[i] & 0xFF);
        i++;
      }
      
      cs[(length++)] = ch;
    }
    
    return new String(cs);
  }
  
  public static byte[] toUTF8ByteArray(String string)
  {
    return toUTF8ByteArray(string.toCharArray());
  }
  
  public static byte[] toUTF8ByteArray(char[] string)
  {
    ByteArrayOutputStream bOut = new ByteArrayOutputStream();
    
    try
    {
      toUTF8ByteArray(string, bOut);
    }
    catch (IOException e)
    {
      throw new IllegalStateException("cannot encode string to byte array!");
    }
    
    return bOut.toByteArray();
  }
  
  public static void toUTF8ByteArray(char[] string, OutputStream sOut)
    throws IOException
  {
    char[] c = string;
    int i = 0;
    
    while (i < c.length)
    {
      char ch = c[i];
      
      if (ch < '')
      {
        sOut.write(ch);
      }
      else if (ch < 'ࠀ')
      {
        sOut.write(0xC0 | ch >> '\006');
        sOut.write(0x80 | ch & 0x3F);

      }
      else if ((ch >= 55296) && (ch <= 57343))
      {


        if (i + 1 >= c.length)
        {
          throw new IllegalStateException("invalid UTF-16 codepoint");
        }
        char W1 = ch;
        ch = c[(++i)];
        char W2 = ch;
        

        if (W1 > 56319)
        {
          throw new IllegalStateException("invalid UTF-16 codepoint");
        }
        int codePoint = ((W1 & 0x3FF) << '\n' | W2 & 0x3FF) + 65536;
        sOut.write(0xF0 | codePoint >> 18);
        sOut.write(0x80 | codePoint >> 12 & 0x3F);
        sOut.write(0x80 | codePoint >> 6 & 0x3F);
        sOut.write(0x80 | codePoint & 0x3F);
      }
      else
      {
        sOut.write(0xE0 | ch >> '\f');
        sOut.write(0x80 | ch >> '\006' & 0x3F);
        sOut.write(0x80 | ch & 0x3F);
      }
      
      i++;
    }
  }
  






  public static String toUpperCase(String string)
  {
    boolean changed = false;
    char[] chars = string.toCharArray();
    
    for (int i = 0; i != chars.length; i++)
    {
      char ch = chars[i];
      if (('a' <= ch) && ('z' >= ch))
      {
        changed = true;
        chars[i] = ((char)(ch - 'a' + 65));
      }
    }
    
    if (changed)
    {
      return new String(chars);
    }
    
    return string;
  }
  






  public static String toLowerCase(String string)
  {
    boolean changed = false;
    char[] chars = string.toCharArray();
    
    for (int i = 0; i != chars.length; i++)
    {
      char ch = chars[i];
      if (('A' <= ch) && ('Z' >= ch))
      {
        changed = true;
        chars[i] = ((char)(ch - 'A' + 97));
      }
    }
    
    if (changed)
    {
      return new String(chars);
    }
    
    return string;
  }
  
  public static byte[] toByteArray(char[] chars)
  {
    byte[] bytes = new byte[chars.length];
    
    for (int i = 0; i != bytes.length; i++)
    {
      bytes[i] = ((byte)chars[i]);
    }
    
    return bytes;
  }
  
  public static byte[] toByteArray(String string)
  {
    byte[] bytes = new byte[string.length()];
    
    for (int i = 0; i != bytes.length; i++)
    {
      char ch = string.charAt(i);
      
      bytes[i] = ((byte)ch);
    }
    
    return bytes;
  }
  
  public static int toByteArray(String s, byte[] buf, int off)
  {
    int count = s.length();
    for (int i = 0; i < count; i++)
    {
      char c = s.charAt(i);
      buf[(off + i)] = ((byte)c);
    }
    return count;
  }
  






  public static String fromByteArray(byte[] bytes)
  {
    return new String(asCharArray(bytes));
  }
  






  public static char[] asCharArray(byte[] bytes)
  {
    char[] chars = new char[bytes.length];
    
    for (int i = 0; i != chars.length; i++)
    {
      chars[i] = ((char)(bytes[i] & 0xFF));
    }
    
    return chars;
  }
  
  public static String[] split(String input, char delimiter)
  {
    Vector v = new Vector();
    boolean moreTokens = true;
    

    while (moreTokens)
    {
      int tokenLocation = input.indexOf(delimiter);
      if (tokenLocation > 0)
      {
        String subString = input.substring(0, tokenLocation);
        v.addElement(subString);
        input = input.substring(tokenLocation + 1);
      }
      else
      {
        moreTokens = false;
        v.addElement(input);
      }
    }
    
    String[] res = new String[v.size()];
    
    for (int i = 0; i != res.length; i++)
    {
      res[i] = ((String)v.elementAt(i));
    }
    return res;
  }
  
  public static StringList newList()
  {
    return new StringListImpl(null);
  }
  
  public static String lineSeparator()
  {
    return LINE_SEPARATOR;
  }
  
  public Strings() {}
  
  private static class StringListImpl extends ArrayList<String> implements StringList {
    private StringListImpl() {}
    
    public boolean add(String s) {
      return super.add(s);
    }
    
    public String set(int index, String element)
    {
      return (String)super.set(index, element);
    }
    
    public void add(int index, String element)
    {
      super.add(index, element);
    }
    
    public String[] toStringArray()
    {
      String[] strs = new String[size()];
      
      for (int i = 0; i != strs.length; i++)
      {
        strs[i] = ((String)get(i));
      }
      
      return strs;
    }
    
    public String[] toStringArray(int from, int to)
    {
      String[] strs = new String[to - from];
      
      for (int i = from; (i != size()) && (i != to); i++)
      {
        strs[(i - from)] = ((String)get(i));
      }
      
      return strs;
    }
  }
}
