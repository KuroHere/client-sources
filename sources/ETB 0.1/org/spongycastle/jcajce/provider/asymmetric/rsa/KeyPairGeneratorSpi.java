package org.spongycastle.jcajce.provider.asymmetric.rsa;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.generators.RSAKeyPairGenerator;
import org.spongycastle.crypto.params.RSAKeyGenerationParameters;
import org.spongycastle.crypto.params.RSAKeyParameters;
import org.spongycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.spongycastle.jcajce.provider.asymmetric.util.PrimeCertaintyCalculator;


public class KeyPairGeneratorSpi
  extends KeyPairGenerator
{
  public KeyPairGeneratorSpi(String algorithmName)
  {
    super(algorithmName);
  }
  
  static final BigInteger defaultPublicExponent = BigInteger.valueOf(65537L);
  
  RSAKeyGenerationParameters param;
  RSAKeyPairGenerator engine;
  
  public KeyPairGeneratorSpi()
  {
    super("RSA");
    
    engine = new RSAKeyPairGenerator();
    
    param = new RSAKeyGenerationParameters(defaultPublicExponent, new SecureRandom(), 2048, PrimeCertaintyCalculator.getDefaultCertainty(2048));
    engine.init(param);
  }
  



  public void initialize(int strength, SecureRandom random)
  {
    param = new RSAKeyGenerationParameters(defaultPublicExponent, random, strength, PrimeCertaintyCalculator.getDefaultCertainty(strength));
    
    engine.init(param);
  }
  


  public void initialize(AlgorithmParameterSpec params, SecureRandom random)
    throws InvalidAlgorithmParameterException
  {
    if (!(params instanceof RSAKeyGenParameterSpec))
    {
      throw new InvalidAlgorithmParameterException("parameter object not a RSAKeyGenParameterSpec");
    }
    RSAKeyGenParameterSpec rsaParams = (RSAKeyGenParameterSpec)params;
    


    param = new RSAKeyGenerationParameters(rsaParams.getPublicExponent(), random, rsaParams.getKeysize(), PrimeCertaintyCalculator.getDefaultCertainty(2048));
    
    engine.init(param);
  }
  
  public KeyPair generateKeyPair()
  {
    AsymmetricCipherKeyPair pair = engine.generateKeyPair();
    RSAKeyParameters pub = (RSAKeyParameters)pair.getPublic();
    RSAPrivateCrtKeyParameters priv = (RSAPrivateCrtKeyParameters)pair.getPrivate();
    
    return new KeyPair(new BCRSAPublicKey(pub), new BCRSAPrivateCrtKey(priv));
  }
}
