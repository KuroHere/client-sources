package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1OctetString;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1TaggedObject;
import org.spongycastle.asn1.DEROctetString;
import org.spongycastle.util.Arrays;









public class SubjectKeyIdentifier
  extends ASN1Object
{
  private byte[] keyidentifier;
  
  public static SubjectKeyIdentifier getInstance(ASN1TaggedObject obj, boolean explicit)
  {
    return getInstance(ASN1OctetString.getInstance(obj, explicit));
  }
  

  public static SubjectKeyIdentifier getInstance(Object obj)
  {
    if ((obj instanceof SubjectKeyIdentifier))
    {
      return (SubjectKeyIdentifier)obj;
    }
    if (obj != null)
    {
      return new SubjectKeyIdentifier(ASN1OctetString.getInstance(obj));
    }
    
    return null;
  }
  
  public static SubjectKeyIdentifier fromExtensions(Extensions extensions)
  {
    return getInstance(extensions.getExtensionParsedValue(Extension.subjectKeyIdentifier));
  }
  

  public SubjectKeyIdentifier(byte[] keyid)
  {
    keyidentifier = Arrays.clone(keyid);
  }
  

  protected SubjectKeyIdentifier(ASN1OctetString keyid)
  {
    this(keyid.getOctets());
  }
  
  public byte[] getKeyIdentifier()
  {
    return Arrays.clone(keyidentifier);
  }
  
  public ASN1Primitive toASN1Primitive()
  {
    return new DEROctetString(getKeyIdentifier());
  }
}
