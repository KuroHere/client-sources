package org.spongycastle.asn1.crmf;

import java.math.BigInteger;
import java.util.Enumeration;
import org.spongycastle.asn1.ASN1Integer;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.ASN1TaggedObject;
import org.spongycastle.asn1.DERBitString;
import org.spongycastle.asn1.x500.X500Name;
import org.spongycastle.asn1.x509.AlgorithmIdentifier;
import org.spongycastle.asn1.x509.Extensions;
import org.spongycastle.asn1.x509.SubjectPublicKeyInfo;


public class CertTemplate
  extends ASN1Object
{
  private ASN1Sequence seq;
  private ASN1Integer version;
  private ASN1Integer serialNumber;
  private AlgorithmIdentifier signingAlg;
  private X500Name issuer;
  private OptionalValidity validity;
  private X500Name subject;
  private SubjectPublicKeyInfo publicKey;
  private DERBitString issuerUID;
  private DERBitString subjectUID;
  private Extensions extensions;
  
  private CertTemplate(ASN1Sequence seq)
  {
    this.seq = seq;
    
    Enumeration en = seq.getObjects();
    while (en.hasMoreElements())
    {
      ASN1TaggedObject tObj = (ASN1TaggedObject)en.nextElement();
      
      switch (tObj.getTagNo())
      {
      case 0: 
        version = ASN1Integer.getInstance(tObj, false);
        break;
      case 1: 
        serialNumber = ASN1Integer.getInstance(tObj, false);
        break;
      case 2: 
        signingAlg = AlgorithmIdentifier.getInstance(tObj, false);
        break;
      case 3: 
        issuer = X500Name.getInstance(tObj, true);
        break;
      case 4: 
        validity = OptionalValidity.getInstance(ASN1Sequence.getInstance(tObj, false));
        break;
      case 5: 
        subject = X500Name.getInstance(tObj, true);
        break;
      case 6: 
        publicKey = SubjectPublicKeyInfo.getInstance(tObj, false);
        break;
      case 7: 
        issuerUID = DERBitString.getInstance(tObj, false);
        break;
      case 8: 
        subjectUID = DERBitString.getInstance(tObj, false);
        break;
      case 9: 
        extensions = Extensions.getInstance(tObj, false);
        break;
      default: 
        throw new IllegalArgumentException("unknown tag: " + tObj.getTagNo());
      }
    }
  }
  
  public static CertTemplate getInstance(Object o)
  {
    if ((o instanceof CertTemplate))
    {
      return (CertTemplate)o;
    }
    if (o != null)
    {
      return new CertTemplate(ASN1Sequence.getInstance(o));
    }
    
    return null;
  }
  
  public int getVersion()
  {
    return version.getValue().intValue();
  }
  
  public ASN1Integer getSerialNumber()
  {
    return serialNumber;
  }
  
  public AlgorithmIdentifier getSigningAlg()
  {
    return signingAlg;
  }
  
  public X500Name getIssuer()
  {
    return issuer;
  }
  
  public OptionalValidity getValidity()
  {
    return validity;
  }
  
  public X500Name getSubject()
  {
    return subject;
  }
  
  public SubjectPublicKeyInfo getPublicKey()
  {
    return publicKey;
  }
  
  public DERBitString getIssuerUID()
  {
    return issuerUID;
  }
  
  public DERBitString getSubjectUID()
  {
    return subjectUID;
  }
  
  public Extensions getExtensions()
  {
    return extensions;
  }
  
















  public ASN1Primitive toASN1Primitive()
  {
    return seq;
  }
}
