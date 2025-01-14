package org.spongycastle.pqc.jcajce.provider.newhope;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactorySpi;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.pkcs.PrivateKeyInfo;
import org.spongycastle.asn1.x509.SubjectPublicKeyInfo;
import org.spongycastle.jcajce.provider.util.AsymmetricKeyInfoConverter;

public class NHKeyFactorySpi
  extends KeyFactorySpi
  implements AsymmetricKeyInfoConverter
{
  public NHKeyFactorySpi() {}
  
  public PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException
  {
    if ((keySpec instanceof PKCS8EncodedKeySpec))
    {

      byte[] encKey = ((PKCS8EncodedKeySpec)keySpec).getEncoded();
      
      try
      {
        return generatePrivate(PrivateKeyInfo.getInstance(ASN1Primitive.fromByteArray(encKey)));
      }
      catch (Exception e)
      {
        throw new InvalidKeySpecException(e.toString());
      }
    }
    

    throw new InvalidKeySpecException("Unsupported key specification: " + keySpec.getClass() + ".");
  }
  
  public PublicKey engineGeneratePublic(KeySpec keySpec)
    throws InvalidKeySpecException
  {
    if ((keySpec instanceof X509EncodedKeySpec))
    {

      byte[] encKey = ((X509EncodedKeySpec)keySpec).getEncoded();
      

      try
      {
        return generatePublic(SubjectPublicKeyInfo.getInstance(encKey));
      }
      catch (Exception e)
      {
        throw new InvalidKeySpecException(e.toString());
      }
    }
    
    throw new InvalidKeySpecException("Unknown key specification: " + keySpec + ".");
  }
  
  public final KeySpec engineGetKeySpec(Key key, Class keySpec)
    throws InvalidKeySpecException
  {
    if ((key instanceof BCNHPrivateKey))
    {
      if (PKCS8EncodedKeySpec.class.isAssignableFrom(keySpec))
      {
        return new PKCS8EncodedKeySpec(key.getEncoded());
      }
    }
    else if ((key instanceof BCNHPublicKey))
    {
      if (X509EncodedKeySpec.class.isAssignableFrom(keySpec))
      {
        return new X509EncodedKeySpec(key.getEncoded());
      }
      

    }
    else {
      throw new InvalidKeySpecException("Unsupported key type: " + key.getClass() + ".");
    }
    
    throw new InvalidKeySpecException("Unknown key specification: " + keySpec + ".");
  }
  

  public final Key engineTranslateKey(Key key)
    throws InvalidKeyException
  {
    if (((key instanceof BCNHPrivateKey)) || ((key instanceof BCNHPublicKey)))
    {
      return key;
    }
    
    throw new InvalidKeyException("Unsupported key type");
  }
  
  public PrivateKey generatePrivate(PrivateKeyInfo keyInfo)
    throws IOException
  {
    return new BCNHPrivateKey(keyInfo);
  }
  
  public PublicKey generatePublic(SubjectPublicKeyInfo keyInfo)
    throws IOException
  {
    return new BCNHPublicKey(keyInfo);
  }
}
