package org.spongycastle.asn1.x500;

import org.spongycastle.asn1.ASN1Encodable;
import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1ObjectIdentifier;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.DERSequence;




public class AttributeTypeAndValue
  extends ASN1Object
{
  private ASN1ObjectIdentifier type;
  private ASN1Encodable value;
  
  private AttributeTypeAndValue(ASN1Sequence seq)
  {
    type = ((ASN1ObjectIdentifier)seq.getObjectAt(0));
    value = seq.getObjectAt(1);
  }
  
  public static AttributeTypeAndValue getInstance(Object o)
  {
    if ((o instanceof AttributeTypeAndValue))
    {
      return (AttributeTypeAndValue)o;
    }
    if (o != null)
    {
      return new AttributeTypeAndValue(ASN1Sequence.getInstance(o));
    }
    
    throw new IllegalArgumentException("null value in getInstance()");
  }
  


  public AttributeTypeAndValue(ASN1ObjectIdentifier type, ASN1Encodable value)
  {
    this.type = type;
    this.value = value;
  }
  
  public ASN1ObjectIdentifier getType()
  {
    return type;
  }
  
  public ASN1Encodable getValue()
  {
    return value;
  }
  








  public ASN1Primitive toASN1Primitive()
  {
    ASN1EncodableVector v = new ASN1EncodableVector();
    
    v.add(type);
    v.add(value);
    
    return new DERSequence(v);
  }
}
