package org.spongycastle.jcajce.provider.asymmetric.dstu;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import org.spongycastle.asn1.ASN1ObjectIdentifier;
import org.spongycastle.asn1.pkcs.PrivateKeyInfo;
import org.spongycastle.asn1.ua.UAObjectIdentifiers;
import org.spongycastle.asn1.x509.AlgorithmIdentifier;
import org.spongycastle.asn1.x509.SubjectPublicKeyInfo;
import org.spongycastle.jcajce.provider.asymmetric.util.BaseKeyFactorySpi;
import org.spongycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.spongycastle.jcajce.provider.config.ProviderConfiguration;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.jce.spec.ECParameterSpec;






public class KeyFactorySpi
  extends BaseKeyFactorySpi
{
  public KeyFactorySpi() {}
  
  protected KeySpec engineGetKeySpec(Key key, Class spec)
    throws InvalidKeySpecException
  {
    if ((spec.isAssignableFrom(java.security.spec.ECPublicKeySpec.class)) && ((key instanceof ECPublicKey)))
    {
      ECPublicKey k = (ECPublicKey)key;
      if (k.getParams() != null)
      {
        return new java.security.spec.ECPublicKeySpec(k.getW(), k.getParams());
      }
      

      ECParameterSpec implicitSpec = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa();
      
      return new java.security.spec.ECPublicKeySpec(k.getW(), EC5Util.convertSpec(EC5Util.convertCurve(implicitSpec.getCurve(), implicitSpec.getSeed()), implicitSpec));
    }
    
    if ((spec.isAssignableFrom(java.security.spec.ECPrivateKeySpec.class)) && ((key instanceof ECPrivateKey)))
    {
      ECPrivateKey k = (ECPrivateKey)key;
      
      if (k.getParams() != null)
      {
        return new java.security.spec.ECPrivateKeySpec(k.getS(), k.getParams());
      }
      

      ECParameterSpec implicitSpec = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa();
      
      return new java.security.spec.ECPrivateKeySpec(k.getS(), EC5Util.convertSpec(EC5Util.convertCurve(implicitSpec.getCurve(), implicitSpec.getSeed()), implicitSpec));
    }
    
    if ((spec.isAssignableFrom(org.spongycastle.jce.spec.ECPublicKeySpec.class)) && ((key instanceof ECPublicKey)))
    {
      ECPublicKey k = (ECPublicKey)key;
      if (k.getParams() != null)
      {
        return new org.spongycastle.jce.spec.ECPublicKeySpec(EC5Util.convertPoint(k.getParams(), k.getW(), false), EC5Util.convertSpec(k.getParams(), false));
      }
      

      ECParameterSpec implicitSpec = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa();
      
      return new org.spongycastle.jce.spec.ECPublicKeySpec(EC5Util.convertPoint(k.getParams(), k.getW(), false), implicitSpec);
    }
    
    if ((spec.isAssignableFrom(org.spongycastle.jce.spec.ECPrivateKeySpec.class)) && ((key instanceof ECPrivateKey)))
    {
      ECPrivateKey k = (ECPrivateKey)key;
      
      if (k.getParams() != null)
      {
        return new org.spongycastle.jce.spec.ECPrivateKeySpec(k.getS(), EC5Util.convertSpec(k.getParams(), false));
      }
      

      ECParameterSpec implicitSpec = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa();
      
      return new org.spongycastle.jce.spec.ECPrivateKeySpec(k.getS(), implicitSpec);
    }
    

    return super.engineGetKeySpec(key, spec);
  }
  

  protected Key engineTranslateKey(Key key)
    throws InvalidKeyException
  {
    throw new InvalidKeyException("key type unknown");
  }
  

  protected PrivateKey engineGeneratePrivate(KeySpec keySpec)
    throws InvalidKeySpecException
  {
    if ((keySpec instanceof org.spongycastle.jce.spec.ECPrivateKeySpec))
    {
      return new BCDSTU4145PrivateKey((org.spongycastle.jce.spec.ECPrivateKeySpec)keySpec);
    }
    if ((keySpec instanceof java.security.spec.ECPrivateKeySpec))
    {
      return new BCDSTU4145PrivateKey((java.security.spec.ECPrivateKeySpec)keySpec);
    }
    
    return super.engineGeneratePrivate(keySpec);
  }
  

  protected PublicKey engineGeneratePublic(KeySpec keySpec)
    throws InvalidKeySpecException
  {
    if ((keySpec instanceof org.spongycastle.jce.spec.ECPublicKeySpec))
    {
      return new BCDSTU4145PublicKey((org.spongycastle.jce.spec.ECPublicKeySpec)keySpec, BouncyCastleProvider.CONFIGURATION);
    }
    if ((keySpec instanceof java.security.spec.ECPublicKeySpec))
    {
      return new BCDSTU4145PublicKey((java.security.spec.ECPublicKeySpec)keySpec);
    }
    
    return super.engineGeneratePublic(keySpec);
  }
  
  public PrivateKey generatePrivate(PrivateKeyInfo keyInfo)
    throws IOException
  {
    ASN1ObjectIdentifier algOid = keyInfo.getPrivateKeyAlgorithm().getAlgorithm();
    
    if ((algOid.equals(UAObjectIdentifiers.dstu4145le)) || (algOid.equals(UAObjectIdentifiers.dstu4145be)))
    {
      return new BCDSTU4145PrivateKey(keyInfo);
    }
    

    throw new IOException("algorithm identifier " + algOid + " in key not recognised");
  }
  

  public PublicKey generatePublic(SubjectPublicKeyInfo keyInfo)
    throws IOException
  {
    ASN1ObjectIdentifier algOid = keyInfo.getAlgorithm().getAlgorithm();
    
    if ((algOid.equals(UAObjectIdentifiers.dstu4145le)) || (algOid.equals(UAObjectIdentifiers.dstu4145be)))
    {
      return new BCDSTU4145PublicKey(keyInfo);
    }
    

    throw new IOException("algorithm identifier " + algOid + " in key not recognised");
  }
}
