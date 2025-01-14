package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.ASN1Integer;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1OctetString;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.ASN1TaggedObject;
import org.spongycastle.asn1.DERSequence;
import org.spongycastle.asn1.x509.AlgorithmIdentifier;
















public class KeyTransRecipientInfo
  extends ASN1Object
{
  private ASN1Integer version;
  private RecipientIdentifier rid;
  private AlgorithmIdentifier keyEncryptionAlgorithm;
  private ASN1OctetString encryptedKey;
  
  public KeyTransRecipientInfo(RecipientIdentifier rid, AlgorithmIdentifier keyEncryptionAlgorithm, ASN1OctetString encryptedKey)
  {
    if ((rid.toASN1Primitive() instanceof ASN1TaggedObject))
    {
      version = new ASN1Integer(2L);
    }
    else
    {
      version = new ASN1Integer(0L);
    }
    
    this.rid = rid;
    this.keyEncryptionAlgorithm = keyEncryptionAlgorithm;
    this.encryptedKey = encryptedKey;
  }
  

  /**
   * @deprecated
   */
  public KeyTransRecipientInfo(ASN1Sequence seq)
  {
    version = ((ASN1Integer)seq.getObjectAt(0));
    rid = RecipientIdentifier.getInstance(seq.getObjectAt(1));
    keyEncryptionAlgorithm = AlgorithmIdentifier.getInstance(seq.getObjectAt(2));
    encryptedKey = ((ASN1OctetString)seq.getObjectAt(3));
  }
  














  public static KeyTransRecipientInfo getInstance(Object obj)
  {
    if ((obj instanceof KeyTransRecipientInfo))
    {
      return (KeyTransRecipientInfo)obj;
    }
    
    if (obj != null)
    {
      return new KeyTransRecipientInfo(ASN1Sequence.getInstance(obj));
    }
    
    return null;
  }
  
  public ASN1Integer getVersion()
  {
    return version;
  }
  
  public RecipientIdentifier getRecipientIdentifier()
  {
    return rid;
  }
  
  public AlgorithmIdentifier getKeyEncryptionAlgorithm()
  {
    return keyEncryptionAlgorithm;
  }
  
  public ASN1OctetString getEncryptedKey()
  {
    return encryptedKey;
  }
  



  public ASN1Primitive toASN1Primitive()
  {
    ASN1EncodableVector v = new ASN1EncodableVector();
    
    v.add(version);
    v.add(rid);
    v.add(keyEncryptionAlgorithm);
    v.add(encryptedKey);
    
    return new DERSequence(v);
  }
}
