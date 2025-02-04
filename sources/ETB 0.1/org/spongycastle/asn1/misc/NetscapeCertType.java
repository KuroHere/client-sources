package org.spongycastle.asn1.misc;

import org.spongycastle.asn1.DERBitString;























public class NetscapeCertType
  extends DERBitString
{
  public static final int sslClient = 128;
  public static final int sslServer = 64;
  public static final int smime = 32;
  public static final int objectSigning = 16;
  public static final int reserved = 8;
  public static final int sslCA = 4;
  public static final int smimeCA = 2;
  public static final int objectSigningCA = 1;
  
  public NetscapeCertType(int usage)
  {
    super(getBytes(usage), getPadBits(usage));
  }
  

  public NetscapeCertType(DERBitString usage)
  {
    super(usage.getBytes(), usage.getPadBits());
  }
  
  public String toString()
  {
    return "NetscapeCertType: 0x" + Integer.toHexString(data[0] & 0xFF);
  }
}
