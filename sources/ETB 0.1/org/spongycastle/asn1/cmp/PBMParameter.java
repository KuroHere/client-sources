package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.ASN1Integer;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1OctetString;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.DEROctetString;
import org.spongycastle.asn1.DERSequence;
import org.spongycastle.asn1.x509.AlgorithmIdentifier;

public class PBMParameter
  extends ASN1Object
{
  private ASN1OctetString salt;
  private AlgorithmIdentifier owf;
  private ASN1Integer iterationCount;
  private AlgorithmIdentifier mac;
  
  private PBMParameter(ASN1Sequence seq)
  {
    salt = ASN1OctetString.getInstance(seq.getObjectAt(0));
    owf = AlgorithmIdentifier.getInstance(seq.getObjectAt(1));
    iterationCount = ASN1Integer.getInstance(seq.getObjectAt(2));
    mac = AlgorithmIdentifier.getInstance(seq.getObjectAt(3));
  }
  
  public static PBMParameter getInstance(Object o)
  {
    if ((o instanceof PBMParameter))
    {
      return (PBMParameter)o;
    }
    
    if (o != null)
    {
      return new PBMParameter(ASN1Sequence.getInstance(o));
    }
    
    return null;
  }
  




  public PBMParameter(byte[] salt, AlgorithmIdentifier owf, int iterationCount, AlgorithmIdentifier mac)
  {
    this(new DEROctetString(salt), owf, new ASN1Integer(iterationCount), mac);
  }
  





  public PBMParameter(ASN1OctetString salt, AlgorithmIdentifier owf, ASN1Integer iterationCount, AlgorithmIdentifier mac)
  {
    this.salt = salt;
    this.owf = owf;
    this.iterationCount = iterationCount;
    this.mac = mac;
  }
  
  public ASN1OctetString getSalt()
  {
    return salt;
  }
  
  public AlgorithmIdentifier getOwf()
  {
    return owf;
  }
  
  public ASN1Integer getIterationCount()
  {
    return iterationCount;
  }
  
  public AlgorithmIdentifier getMac()
  {
    return mac;
  }
  




















  public ASN1Primitive toASN1Primitive()
  {
    ASN1EncodableVector v = new ASN1EncodableVector();
    
    v.add(salt);
    v.add(owf);
    v.add(iterationCount);
    v.add(mac);
    
    return new DERSequence(v);
  }
}
