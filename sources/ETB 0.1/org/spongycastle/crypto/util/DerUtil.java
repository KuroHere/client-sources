package org.spongycastle.crypto.util;

import java.io.IOException;
import org.spongycastle.asn1.ASN1OctetString;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.DEROctetString;

class DerUtil
{
  DerUtil() {}
  
  static ASN1OctetString getOctetString(byte[] data)
  {
    if (data == null)
    {
      return new DEROctetString(new byte[0]);
    }
    
    return new DEROctetString(org.spongycastle.util.Arrays.clone(data));
  }
  
  static byte[] toByteArray(ASN1Primitive primitive)
  {
    try
    {
      return primitive.getEncoded();
    }
    catch (IOException e)
    {
      throw new IllegalStateException("Cannot get encoding: " + e.getMessage())
      {
        public Throwable getCause()
        {
          return e;
        }
      };
    }
  }
}
