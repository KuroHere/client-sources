package org.spongycastle.pqc.asn1;

import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.ASN1Integer;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.DERSequence;
import org.spongycastle.asn1.x509.AlgorithmIdentifier;

public class SPHINCS256KeyParams
  extends ASN1Object
{
  private final ASN1Integer version;
  private final AlgorithmIdentifier treeDigest;
  
  public SPHINCS256KeyParams(AlgorithmIdentifier treeDigest)
  {
    version = new ASN1Integer(0L);
    this.treeDigest = treeDigest;
  }
  
  private SPHINCS256KeyParams(ASN1Sequence sequence)
  {
    version = ASN1Integer.getInstance(sequence.getObjectAt(0));
    treeDigest = AlgorithmIdentifier.getInstance(sequence.getObjectAt(1));
  }
  
  public static final SPHINCS256KeyParams getInstance(Object o)
  {
    if ((o instanceof SPHINCS256KeyParams))
    {
      return (SPHINCS256KeyParams)o;
    }
    if (o != null)
    {
      return new SPHINCS256KeyParams(ASN1Sequence.getInstance(o));
    }
    
    return null;
  }
  
  public AlgorithmIdentifier getTreeDigest()
  {
    return treeDigest;
  }
  
  public ASN1Primitive toASN1Primitive()
  {
    ASN1EncodableVector v = new ASN1EncodableVector();
    
    v.add(version);
    v.add(treeDigest);
    
    return new DERSequence(v);
  }
}
