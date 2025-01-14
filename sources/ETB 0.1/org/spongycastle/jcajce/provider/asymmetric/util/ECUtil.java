package org.spongycastle.jcajce.provider.asymmetric.util;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Enumeration;
import java.util.Map;
import org.spongycastle.asn1.ASN1ObjectIdentifier;
import org.spongycastle.asn1.anssi.ANSSINamedCurves;
import org.spongycastle.asn1.cryptopro.ECGOST3410NamedCurves;
import org.spongycastle.asn1.gm.GMNamedCurves;
import org.spongycastle.asn1.nist.NISTNamedCurves;
import org.spongycastle.asn1.pkcs.PrivateKeyInfo;
import org.spongycastle.asn1.sec.SECNamedCurves;
import org.spongycastle.asn1.teletrust.TeleTrusTNamedCurves;
import org.spongycastle.asn1.x509.SubjectPublicKeyInfo;
import org.spongycastle.asn1.x9.ECNamedCurveTable;
import org.spongycastle.asn1.x9.X962NamedCurves;
import org.spongycastle.asn1.x9.X962Parameters;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.ec.CustomNamedCurves;
import org.spongycastle.crypto.params.AsymmetricKeyParameter;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.crypto.params.ECNamedDomainParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.jcajce.provider.config.ProviderConfiguration;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.jce.spec.ECNamedCurveParameterSpec;
import org.spongycastle.jce.spec.ECParameterSpec;
import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECFieldElement;
import org.spongycastle.math.ec.ECPoint;
import org.spongycastle.util.Arrays;
import org.spongycastle.util.Fingerprint;
import org.spongycastle.util.Strings;













public class ECUtil
{
  public ECUtil() {}
  
  static int[] convertMidTerms(int[] k)
  {
    int[] res = new int[3];
    
    if (k.length == 1)
    {
      res[0] = k[0];
    }
    else
    {
      if (k.length != 3)
      {
        throw new IllegalArgumentException("Only Trinomials and pentanomials supported");
      }
      
      if ((k[0] < k[1]) && (k[0] < k[2]))
      {
        res[0] = k[0];
        if (k[1] < k[2])
        {
          res[1] = k[1];
          res[2] = k[2];
        }
        else
        {
          res[1] = k[2];
          res[2] = k[1];
        }
      }
      else if (k[1] < k[2])
      {
        res[0] = k[1];
        if (k[0] < k[2])
        {
          res[1] = k[0];
          res[2] = k[2];
        }
        else
        {
          res[1] = k[2];
          res[2] = k[0];
        }
      }
      else
      {
        res[0] = k[2];
        if (k[0] < k[1])
        {
          res[1] = k[0];
          res[2] = k[1];
        }
        else
        {
          res[1] = k[1];
          res[2] = k[0];
        }
      }
    }
    
    return res;
  }
  

  public static ECDomainParameters getDomainParameters(ProviderConfiguration configuration, ECParameterSpec params)
  {
    ECDomainParameters domainParameters;
    
    ECDomainParameters domainParameters;
    if ((params instanceof ECNamedCurveParameterSpec))
    {
      ECNamedCurveParameterSpec nParams = (ECNamedCurveParameterSpec)params;
      ASN1ObjectIdentifier nameOid = getNamedCurveOid(nParams.getName());
      
      domainParameters = new ECNamedDomainParameters(nameOid, nParams.getCurve(), nParams.getG(), nParams.getN(), nParams.getH(), nParams.getSeed());
    } else { ECDomainParameters domainParameters;
      if (params == null)
      {
        ECParameterSpec iSpec = configuration.getEcImplicitlyCa();
        
        domainParameters = new ECDomainParameters(iSpec.getCurve(), iSpec.getG(), iSpec.getN(), iSpec.getH(), iSpec.getSeed());
      }
      else
      {
        domainParameters = new ECDomainParameters(params.getCurve(), params.getG(), params.getN(), params.getH(), params.getSeed());
      }
    }
    return domainParameters;
  }
  

  public static ECDomainParameters getDomainParameters(ProviderConfiguration configuration, X962Parameters params)
  {
    ECDomainParameters domainParameters;
    
    ECDomainParameters domainParameters;
    if (params.isNamedCurve())
    {
      ASN1ObjectIdentifier oid = ASN1ObjectIdentifier.getInstance(params.getParameters());
      X9ECParameters ecP = getNamedCurveByOid(oid);
      if (ecP == null)
      {
        Map extraCurves = configuration.getAdditionalECParameters();
        
        ecP = (X9ECParameters)extraCurves.get(oid);
      }
      domainParameters = new ECNamedDomainParameters(oid, ecP.getCurve(), ecP.getG(), ecP.getN(), ecP.getH(), ecP.getSeed());
    } else { ECDomainParameters domainParameters;
      if (params.isImplicitlyCA())
      {
        ECParameterSpec iSpec = configuration.getEcImplicitlyCa();
        
        domainParameters = new ECDomainParameters(iSpec.getCurve(), iSpec.getG(), iSpec.getN(), iSpec.getH(), iSpec.getSeed());
      }
      else
      {
        X9ECParameters ecP = X9ECParameters.getInstance(params.getParameters());
        
        domainParameters = new ECDomainParameters(ecP.getCurve(), ecP.getG(), ecP.getN(), ecP.getH(), ecP.getSeed());
      }
    }
    return domainParameters;
  }
  

  public static AsymmetricKeyParameter generatePublicKeyParameter(PublicKey key)
    throws InvalidKeyException
  {
    if ((key instanceof org.spongycastle.jce.interfaces.ECPublicKey))
    {
      org.spongycastle.jce.interfaces.ECPublicKey k = (org.spongycastle.jce.interfaces.ECPublicKey)key;
      ECParameterSpec s = k.getParameters();
      
      return new ECPublicKeyParameters(k
        .getQ(), new ECDomainParameters(s
        .getCurve(), s.getG(), s.getN(), s.getH(), s.getSeed()));
    }
    if ((key instanceof java.security.interfaces.ECPublicKey))
    {
      java.security.interfaces.ECPublicKey pubKey = (java.security.interfaces.ECPublicKey)key;
      ECParameterSpec s = EC5Util.convertSpec(pubKey.getParams(), false);
      return new ECPublicKeyParameters(
        EC5Util.convertPoint(pubKey.getParams(), pubKey.getW(), false), new ECDomainParameters(s
        .getCurve(), s.getG(), s.getN(), s.getH(), s.getSeed()));
    }
    


    try
    {
      byte[] bytes = key.getEncoded();
      
      if (bytes == null)
      {
        throw new InvalidKeyException("no encoding for EC public key");
      }
      
      PublicKey publicKey = BouncyCastleProvider.getPublicKey(SubjectPublicKeyInfo.getInstance(bytes));
      
      if ((publicKey instanceof java.security.interfaces.ECPublicKey))
      {
        return generatePublicKeyParameter(publicKey);
      }
    }
    catch (Exception e)
    {
      throw new InvalidKeyException("cannot identify EC public key: " + e.toString());
    }
    

    throw new InvalidKeyException("cannot identify EC public key.");
  }
  

  public static AsymmetricKeyParameter generatePrivateKeyParameter(PrivateKey key)
    throws InvalidKeyException
  {
    if ((key instanceof org.spongycastle.jce.interfaces.ECPrivateKey))
    {
      org.spongycastle.jce.interfaces.ECPrivateKey k = (org.spongycastle.jce.interfaces.ECPrivateKey)key;
      ECParameterSpec s = k.getParameters();
      
      if (s == null)
      {
        s = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa();
      }
      
      return new ECPrivateKeyParameters(k
        .getD(), new ECDomainParameters(s
        .getCurve(), s.getG(), s.getN(), s.getH(), s.getSeed()));
    }
    if ((key instanceof java.security.interfaces.ECPrivateKey))
    {
      java.security.interfaces.ECPrivateKey privKey = (java.security.interfaces.ECPrivateKey)key;
      ECParameterSpec s = EC5Util.convertSpec(privKey.getParams(), false);
      return new ECPrivateKeyParameters(privKey
        .getS(), new ECDomainParameters(s
        .getCurve(), s.getG(), s.getN(), s.getH(), s.getSeed()));
    }
    


    try
    {
      byte[] bytes = key.getEncoded();
      
      if (bytes == null)
      {
        throw new InvalidKeyException("no encoding for EC private key");
      }
      
      PrivateKey privateKey = BouncyCastleProvider.getPrivateKey(PrivateKeyInfo.getInstance(bytes));
      
      if ((privateKey instanceof java.security.interfaces.ECPrivateKey))
      {
        return generatePrivateKeyParameter(privateKey);
      }
    }
    catch (Exception e)
    {
      throw new InvalidKeyException("cannot identify EC private key: " + e.toString());
    }
    

    throw new InvalidKeyException("can't identify EC private key.");
  }
  
  public static int getOrderBitLength(ProviderConfiguration configuration, BigInteger order, BigInteger privateValue)
  {
    if (order == null)
    {
      ECParameterSpec implicitCA = configuration.getEcImplicitlyCa();
      
      if (implicitCA == null)
      {
        return privateValue.bitLength();
      }
      
      return implicitCA.getN().bitLength();
    }
    

    return order.bitLength();
  }
  

  public static ASN1ObjectIdentifier getNamedCurveOid(String curveName)
  {
    String name;
    
    String name;
    if (curveName.indexOf(' ') > 0)
    {
      name = curveName.substring(curveName.indexOf(' ') + 1);
    }
    else
    {
      name = curveName;
    }
    
    try
    {
      if ((name.charAt(0) >= '0') && (name.charAt(0) <= '2'))
      {
        return new ASN1ObjectIdentifier(name);
      }
      

      return lookupOidByName(name);
    }
    catch (IllegalArgumentException ex) {}
    

    return lookupOidByName(name);
  }
  

  private static ASN1ObjectIdentifier lookupOidByName(String name)
  {
    ASN1ObjectIdentifier oid = X962NamedCurves.getOID(name);
    
    if (oid == null)
    {
      oid = SECNamedCurves.getOID(name);
      if (oid == null)
      {
        oid = NISTNamedCurves.getOID(name);
      }
      if (oid == null)
      {
        oid = TeleTrusTNamedCurves.getOID(name);
      }
      if (oid == null)
      {
        oid = ECGOST3410NamedCurves.getOID(name);
      }
      if (oid == null)
      {
        oid = ANSSINamedCurves.getOID(name);
      }
      if (oid == null)
      {
        oid = GMNamedCurves.getOID(name);
      }
    }
    
    return oid;
  }
  

  public static ASN1ObjectIdentifier getNamedCurveOid(ECParameterSpec ecParameterSpec)
  {
    for (Enumeration names = ECNamedCurveTable.getNames(); names.hasMoreElements();)
    {
      String name = (String)names.nextElement();
      
      X9ECParameters params = ECNamedCurveTable.getByName(name);
      
      if ((params.getN().equals(ecParameterSpec.getN())) && 
        (params.getH().equals(ecParameterSpec.getH())) && 
        (params.getCurve().equals(ecParameterSpec.getCurve())) && 
        (params.getG().equals(ecParameterSpec.getG())))
      {
        return ECNamedCurveTable.getOID(name);
      }
    }
    
    return null;
  }
  

  public static X9ECParameters getNamedCurveByOid(ASN1ObjectIdentifier oid)
  {
    X9ECParameters params = CustomNamedCurves.getByOID(oid);
    
    if (params == null)
    {
      params = X962NamedCurves.getByOID(oid);
      if (params == null)
      {
        params = SECNamedCurves.getByOID(oid);
      }
      if (params == null)
      {
        params = NISTNamedCurves.getByOID(oid);
      }
      if (params == null)
      {
        params = TeleTrusTNamedCurves.getByOID(oid);
      }
      if (params == null)
      {
        params = ANSSINamedCurves.getByOID(oid);
      }
      if (params == null)
      {
        params = GMNamedCurves.getByOID(oid);
      }
    }
    
    return params;
  }
  

  public static X9ECParameters getNamedCurveByName(String curveName)
  {
    X9ECParameters params = CustomNamedCurves.getByName(curveName);
    
    if (params == null)
    {
      params = X962NamedCurves.getByName(curveName);
      if (params == null)
      {
        params = SECNamedCurves.getByName(curveName);
      }
      if (params == null)
      {
        params = NISTNamedCurves.getByName(curveName);
      }
      if (params == null)
      {
        params = TeleTrusTNamedCurves.getByName(curveName);
      }
      if (params == null)
      {
        params = ANSSINamedCurves.getByName(curveName);
      }
      if (params == null)
      {
        params = GMNamedCurves.getByName(curveName);
      }
    }
    
    return params;
  }
  

  public static String getCurveName(ASN1ObjectIdentifier oid)
  {
    String name = X962NamedCurves.getName(oid);
    
    if (name == null)
    {
      name = SECNamedCurves.getName(oid);
      if (name == null)
      {
        name = NISTNamedCurves.getName(oid);
      }
      if (name == null)
      {
        name = TeleTrusTNamedCurves.getName(oid);
      }
      if (name == null)
      {
        name = ECGOST3410NamedCurves.getName(oid);
      }
      if (name == null)
      {
        name = ANSSINamedCurves.getName(oid);
      }
      if (name == null)
      {
        name = GMNamedCurves.getName(oid);
      }
    }
    
    return name;
  }
  
  public static String privateKeyToString(String algorithm, BigInteger d, ECParameterSpec spec)
  {
    StringBuffer buf = new StringBuffer();
    String nl = Strings.lineSeparator();
    
    ECPoint q = calculateQ(d, spec);
    
    buf.append(algorithm);
    buf.append(" Private Key [").append(generateKeyFingerprint(q, spec)).append("]").append(nl);
    buf.append("            X: ").append(q.getAffineXCoord().toBigInteger().toString(16)).append(nl);
    buf.append("            Y: ").append(q.getAffineYCoord().toBigInteger().toString(16)).append(nl);
    
    return buf.toString();
  }
  
  private static ECPoint calculateQ(BigInteger d, ECParameterSpec spec)
  {
    return spec.getG().multiply(d).normalize();
  }
  
  public static String publicKeyToString(String algorithm, ECPoint q, ECParameterSpec spec)
  {
    StringBuffer buf = new StringBuffer();
    String nl = Strings.lineSeparator();
    
    buf.append(algorithm);
    buf.append(" Public Key [").append(generateKeyFingerprint(q, spec)).append("]").append(nl);
    buf.append("            X: ").append(q.getAffineXCoord().toBigInteger().toString(16)).append(nl);
    buf.append("            Y: ").append(q.getAffineYCoord().toBigInteger().toString(16)).append(nl);
    
    return buf.toString();
  }
  
  public static String generateKeyFingerprint(ECPoint publicPoint, ECParameterSpec spec)
  {
    ECCurve curve = spec.getCurve();
    ECPoint g = spec.getG();
    
    if (curve != null)
    {
      return new Fingerprint(Arrays.concatenate(publicPoint.getEncoded(false), curve.getA().getEncoded(), curve.getB().getEncoded(), g.getEncoded(false))).toString();
    }
    
    return new Fingerprint(publicPoint.getEncoded(false)).toString();
  }
}
