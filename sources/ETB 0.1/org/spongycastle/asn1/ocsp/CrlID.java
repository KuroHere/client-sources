package org.spongycastle.asn1.ocsp;

import java.util.Enumeration;
import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.ASN1GeneralizedTime;
import org.spongycastle.asn1.ASN1Integer;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.ASN1TaggedObject;
import org.spongycastle.asn1.DERIA5String;
import org.spongycastle.asn1.DERSequence;
import org.spongycastle.asn1.DERTaggedObject;



public class CrlID
  extends ASN1Object
{
  private DERIA5String crlUrl;
  private ASN1Integer crlNum;
  private ASN1GeneralizedTime crlTime;
  
  private CrlID(ASN1Sequence seq)
  {
    Enumeration e = seq.getObjects();
    
    while (e.hasMoreElements())
    {
      ASN1TaggedObject o = (ASN1TaggedObject)e.nextElement();
      
      switch (o.getTagNo())
      {
      case 0: 
        crlUrl = DERIA5String.getInstance(o, true);
        break;
      case 1: 
        crlNum = ASN1Integer.getInstance(o, true);
        break;
      case 2: 
        crlTime = ASN1GeneralizedTime.getInstance(o, true);
        break;
      
      default: 
        throw new IllegalArgumentException("unknown tag number: " + o.getTagNo());
      }
      
    }
  }
  
  public static CrlID getInstance(Object obj)
  {
    if ((obj instanceof CrlID))
    {
      return (CrlID)obj;
    }
    if (obj != null)
    {
      return new CrlID(ASN1Sequence.getInstance(obj));
    }
    
    return null;
  }
  
  public DERIA5String getCrlUrl()
  {
    return crlUrl;
  }
  
  public ASN1Integer getCrlNum()
  {
    return crlNum;
  }
  
  public ASN1GeneralizedTime getCrlTime()
  {
    return crlTime;
  }
  









  public ASN1Primitive toASN1Primitive()
  {
    ASN1EncodableVector v = new ASN1EncodableVector();
    
    if (crlUrl != null)
    {
      v.add(new DERTaggedObject(true, 0, crlUrl));
    }
    
    if (crlNum != null)
    {
      v.add(new DERTaggedObject(true, 1, crlNum));
    }
    
    if (crlTime != null)
    {
      v.add(new DERTaggedObject(true, 2, crlTime));
    }
    
    return new DERSequence(v);
  }
}
