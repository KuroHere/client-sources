package org.spongycastle.jcajce.provider.asymmetric.rsa;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.spec.RSAPublicKeySpec;
import org.spongycastle.asn1.DERNull;
import org.spongycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.spongycastle.asn1.x509.AlgorithmIdentifier;
import org.spongycastle.asn1.x509.SubjectPublicKeyInfo;
import org.spongycastle.crypto.params.RSAKeyParameters;
import org.spongycastle.jcajce.provider.asymmetric.util.KeyUtil;
import org.spongycastle.util.Strings;



public class BCRSAPublicKey
  implements java.security.interfaces.RSAPublicKey
{
  private static final AlgorithmIdentifier DEFAULT_ALGORITHM_IDENTIFIER = new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE);
  
  static final long serialVersionUID = 2675817738516720772L;
  
  private BigInteger modulus;
  
  private BigInteger publicExponent;
  private transient AlgorithmIdentifier algorithmIdentifier;
  
  BCRSAPublicKey(RSAKeyParameters key)
  {
    algorithmIdentifier = DEFAULT_ALGORITHM_IDENTIFIER;
    modulus = key.getModulus();
    publicExponent = key.getExponent();
  }
  

  BCRSAPublicKey(RSAPublicKeySpec spec)
  {
    algorithmIdentifier = DEFAULT_ALGORITHM_IDENTIFIER;
    modulus = spec.getModulus();
    publicExponent = spec.getPublicExponent();
  }
  

  BCRSAPublicKey(java.security.interfaces.RSAPublicKey key)
  {
    algorithmIdentifier = DEFAULT_ALGORITHM_IDENTIFIER;
    modulus = key.getModulus();
    publicExponent = key.getPublicExponent();
  }
  

  BCRSAPublicKey(SubjectPublicKeyInfo info)
  {
    populateFromPublicKeyInfo(info);
  }
  
  private void populateFromPublicKeyInfo(SubjectPublicKeyInfo info)
  {
    try
    {
      org.spongycastle.asn1.pkcs.RSAPublicKey pubKey = org.spongycastle.asn1.pkcs.RSAPublicKey.getInstance(info.parsePublicKey());
      
      algorithmIdentifier = info.getAlgorithm();
      modulus = pubKey.getModulus();
      publicExponent = pubKey.getPublicExponent();
    }
    catch (IOException e)
    {
      throw new IllegalArgumentException("invalid info structure in RSA public key");
    }
  }
  





  public BigInteger getModulus()
  {
    return modulus;
  }
  





  public BigInteger getPublicExponent()
  {
    return publicExponent;
  }
  
  public String getAlgorithm()
  {
    return "RSA";
  }
  
  public String getFormat()
  {
    return "X.509";
  }
  
  public byte[] getEncoded()
  {
    return KeyUtil.getEncodedSubjectPublicKeyInfo(algorithmIdentifier, new org.spongycastle.asn1.pkcs.RSAPublicKey(getModulus(), getPublicExponent()));
  }
  
  public int hashCode()
  {
    return getModulus().hashCode() ^ getPublicExponent().hashCode();
  }
  
  public boolean equals(Object o)
  {
    if (o == this)
    {
      return true;
    }
    
    if (!(o instanceof java.security.interfaces.RSAPublicKey))
    {
      return false;
    }
    
    java.security.interfaces.RSAPublicKey key = (java.security.interfaces.RSAPublicKey)o;
    
    return (getModulus().equals(key.getModulus())) && 
      (getPublicExponent().equals(key.getPublicExponent()));
  }
  
  public String toString()
  {
    StringBuffer buf = new StringBuffer();
    String nl = Strings.lineSeparator();
    
    buf.append("RSA Public Key [").append(RSAUtil.generateKeyFingerprint(getModulus(), getPublicExponent())).append("]").append(nl);
    buf.append("            modulus: ").append(getModulus().toString(16)).append(nl);
    buf.append("    public exponent: ").append(getPublicExponent().toString(16)).append(nl);
    
    return buf.toString();
  }
  

  private void readObject(ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    
    try
    {
      algorithmIdentifier = AlgorithmIdentifier.getInstance(in.readObject());
    }
    catch (Exception e)
    {
      algorithmIdentifier = DEFAULT_ALGORITHM_IDENTIFIER;
    }
  }
  

  private void writeObject(ObjectOutputStream out)
    throws IOException
  {
    out.defaultWriteObject();
    
    if (!algorithmIdentifier.equals(DEFAULT_ALGORITHM_IDENTIFIER))
    {
      out.writeObject(algorithmIdentifier.getEncoded());
    }
  }
}
