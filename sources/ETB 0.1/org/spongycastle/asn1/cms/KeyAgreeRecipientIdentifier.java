package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.ASN1Choice;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.ASN1TaggedObject;
import org.spongycastle.asn1.DERTaggedObject;






















public class KeyAgreeRecipientIdentifier
  extends ASN1Object
  implements ASN1Choice
{
  private IssuerAndSerialNumber issuerSerial;
  private RecipientKeyIdentifier rKeyID;
  
  public static KeyAgreeRecipientIdentifier getInstance(ASN1TaggedObject obj, boolean explicit)
  {
    return getInstance(ASN1Sequence.getInstance(obj, explicit));
  }
  
















  public static KeyAgreeRecipientIdentifier getInstance(Object obj)
  {
    if ((obj == null) || ((obj instanceof KeyAgreeRecipientIdentifier)))
    {
      return (KeyAgreeRecipientIdentifier)obj;
    }
    
    if ((obj instanceof ASN1Sequence))
    {
      return new KeyAgreeRecipientIdentifier(IssuerAndSerialNumber.getInstance(obj));
    }
    
    if (((obj instanceof ASN1TaggedObject)) && (((ASN1TaggedObject)obj).getTagNo() == 0))
    {
      return new KeyAgreeRecipientIdentifier(RecipientKeyIdentifier.getInstance((ASN1TaggedObject)obj, false));
    }
    

    throw new IllegalArgumentException("Invalid KeyAgreeRecipientIdentifier: " + obj.getClass().getName());
  }
  

  public KeyAgreeRecipientIdentifier(IssuerAndSerialNumber issuerSerial)
  {
    this.issuerSerial = issuerSerial;
    rKeyID = null;
  }
  

  public KeyAgreeRecipientIdentifier(RecipientKeyIdentifier rKeyID)
  {
    issuerSerial = null;
    this.rKeyID = rKeyID;
  }
  
  public IssuerAndSerialNumber getIssuerAndSerialNumber()
  {
    return issuerSerial;
  }
  
  public RecipientKeyIdentifier getRKeyID()
  {
    return rKeyID;
  }
  



  public ASN1Primitive toASN1Primitive()
  {
    if (issuerSerial != null)
    {
      return issuerSerial.toASN1Primitive();
    }
    
    return new DERTaggedObject(false, 0, rKeyID);
  }
}
