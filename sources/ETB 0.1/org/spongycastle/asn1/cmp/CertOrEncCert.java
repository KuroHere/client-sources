package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.ASN1Choice;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1TaggedObject;
import org.spongycastle.asn1.DERTaggedObject;
import org.spongycastle.asn1.crmf.EncryptedValue;

public class CertOrEncCert
  extends ASN1Object
  implements ASN1Choice
{
  private CMPCertificate certificate;
  private EncryptedValue encryptedCert;
  
  private CertOrEncCert(ASN1TaggedObject tagged)
  {
    if (tagged.getTagNo() == 0)
    {
      certificate = CMPCertificate.getInstance(tagged.getObject());
    }
    else if (tagged.getTagNo() == 1)
    {
      encryptedCert = EncryptedValue.getInstance(tagged.getObject());
    }
    else
    {
      throw new IllegalArgumentException("unknown tag: " + tagged.getTagNo());
    }
  }
  
  public static CertOrEncCert getInstance(Object o)
  {
    if ((o instanceof CertOrEncCert))
    {
      return (CertOrEncCert)o;
    }
    
    if ((o instanceof ASN1TaggedObject))
    {
      return new CertOrEncCert((ASN1TaggedObject)o);
    }
    
    return null;
  }
  
  public CertOrEncCert(CMPCertificate certificate)
  {
    if (certificate == null)
    {
      throw new IllegalArgumentException("'certificate' cannot be null");
    }
    
    this.certificate = certificate;
  }
  
  public CertOrEncCert(EncryptedValue encryptedCert)
  {
    if (encryptedCert == null)
    {
      throw new IllegalArgumentException("'encryptedCert' cannot be null");
    }
    
    this.encryptedCert = encryptedCert;
  }
  
  public CMPCertificate getCertificate()
  {
    return certificate;
  }
  
  public EncryptedValue getEncryptedCert()
  {
    return encryptedCert;
  }
  









  public ASN1Primitive toASN1Primitive()
  {
    if (certificate != null)
    {
      return new DERTaggedObject(true, 0, certificate);
    }
    
    return new DERTaggedObject(true, 1, encryptedCert);
  }
}
