package org.spongycastle.crypto.digests;







public class SHA3Digest
  extends KeccakDigest
{
  private static int checkBitLength(int bitLength)
  {
    switch (bitLength)
    {
    case 224: 
    case 256: 
    case 384: 
    case 512: 
      return bitLength;
    }
    throw new IllegalArgumentException("'bitLength' " + bitLength + " not supported for SHA-3");
  }
  

  public SHA3Digest()
  {
    this(256);
  }
  
  public SHA3Digest(int bitLength)
  {
    super(checkBitLength(bitLength));
  }
  
  public SHA3Digest(SHA3Digest source) {
    super(source);
  }
  
  public String getAlgorithmName()
  {
    return "SHA3-" + fixedOutputLength;
  }
  
  public int doFinal(byte[] out, int outOff)
  {
    absorbBits(2, 2);
    
    return super.doFinal(out, outOff);
  }
  



  protected int doFinal(byte[] out, int outOff, byte partialByte, int partialBits)
  {
    if ((partialBits < 0) || (partialBits > 7))
    {
      throw new IllegalArgumentException("'partialBits' must be in the range [0,7]");
    }
    
    int finalInput = partialByte & (1 << partialBits) - 1 | 2 << partialBits;
    int finalBits = partialBits + 2;
    
    if (finalBits >= 8)
    {
      absorb(new byte[] { (byte)finalInput }, 0, 1);
      finalBits -= 8;
      finalInput >>>= 8;
    }
    
    return super.doFinal(out, outOff, (byte)finalInput, finalBits);
  }
}
