package org.spongycastle.crypto.params;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.spongycastle.crypto.KeyGenerationParameters;



public class ElGamalKeyGenerationParameters
  extends KeyGenerationParameters
{
  private ElGamalParameters params;
  
  public ElGamalKeyGenerationParameters(SecureRandom random, ElGamalParameters params)
  {
    super(random, getStrength(params));
    
    this.params = params;
  }
  
  public ElGamalParameters getParameters()
  {
    return params;
  }
  
  static int getStrength(ElGamalParameters params)
  {
    return params.getL() != 0 ? params.getL() : params.getP().bitLength();
  }
}
