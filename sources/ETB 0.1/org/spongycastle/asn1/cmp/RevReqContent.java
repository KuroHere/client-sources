package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.DERSequence;

public class RevReqContent
  extends ASN1Object
{
  private ASN1Sequence content;
  
  private RevReqContent(ASN1Sequence seq)
  {
    content = seq;
  }
  
  public static RevReqContent getInstance(Object o)
  {
    if ((o instanceof RevReqContent))
    {
      return (RevReqContent)o;
    }
    
    if (o != null)
    {
      return new RevReqContent(ASN1Sequence.getInstance(o));
    }
    
    return null;
  }
  
  public RevReqContent(RevDetails revDetails)
  {
    content = new DERSequence(revDetails);
  }
  
  public RevReqContent(RevDetails[] revDetailsArray)
  {
    ASN1EncodableVector v = new ASN1EncodableVector();
    
    for (int i = 0; i != revDetailsArray.length; i++)
    {
      v.add(revDetailsArray[i]);
    }
    
    content = new DERSequence(v);
  }
  
  public RevDetails[] toRevDetailsArray()
  {
    RevDetails[] result = new RevDetails[content.size()];
    
    for (int i = 0; i != result.length; i++)
    {
      result[i] = RevDetails.getInstance(content.getObjectAt(i));
    }
    
    return result;
  }
  






  public ASN1Primitive toASN1Primitive()
  {
    return content;
  }
}
