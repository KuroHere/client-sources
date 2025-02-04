package org.spongycastle.asn1.x9;

import java.math.BigInteger;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1OctetString;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.DEROctetString;
import org.spongycastle.math.ec.ECFieldElement;
import org.spongycastle.math.ec.ECFieldElement.F2m;
import org.spongycastle.math.ec.ECFieldElement.Fp;




public class X9FieldElement
  extends ASN1Object
{
  protected ECFieldElement f;
  private static X9IntegerConverter converter = new X9IntegerConverter();
  
  public X9FieldElement(ECFieldElement f)
  {
    this.f = f;
  }
  
  public X9FieldElement(BigInteger p, ASN1OctetString s)
  {
    this(new ECFieldElement.Fp(p, new BigInteger(1, s.getOctets())));
  }
  
  public X9FieldElement(int m, int k1, int k2, int k3, ASN1OctetString s)
  {
    this(new ECFieldElement.F2m(m, k1, k2, k3, new BigInteger(1, s.getOctets())));
  }
  
  public ECFieldElement getValue()
  {
    return f;
  }
  
















  public ASN1Primitive toASN1Primitive()
  {
    int byteCount = converter.getByteLength(f);
    byte[] paddedBigInteger = converter.integerToBytes(f.toBigInteger(), byteCount);
    
    return new DEROctetString(paddedBigInteger);
  }
}
