package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.ASN1Integer;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.DERSequence;

public class PollReqContent
  extends ASN1Object
{
  private ASN1Sequence content;
  
  private PollReqContent(ASN1Sequence seq)
  {
    content = seq;
  }
  
  public static PollReqContent getInstance(Object o)
  {
    if ((o instanceof PollReqContent))
    {
      return (PollReqContent)o;
    }
    
    if (o != null)
    {
      return new PollReqContent(ASN1Sequence.getInstance(o));
    }
    
    return null;
  }
  





  public PollReqContent(ASN1Integer certReqId)
  {
    this(new DERSequence(new DERSequence(certReqId)));
  }
  
  public ASN1Integer[][] getCertReqIds()
  {
    ASN1Integer[][] result = new ASN1Integer[content.size()][];
    
    for (int i = 0; i != result.length; i++)
    {
      result[i] = sequenceToASN1IntegerArray((ASN1Sequence)content.getObjectAt(i));
    }
    
    return result;
  }
  
  private static ASN1Integer[] sequenceToASN1IntegerArray(ASN1Sequence seq)
  {
    ASN1Integer[] result = new ASN1Integer[seq.size()];
    
    for (int i = 0; i != result.length; i++)
    {
      result[i] = ASN1Integer.getInstance(seq.getObjectAt(i));
    }
    
    return result;
  }
  








  public ASN1Primitive toASN1Primitive()
  {
    return content;
  }
}
