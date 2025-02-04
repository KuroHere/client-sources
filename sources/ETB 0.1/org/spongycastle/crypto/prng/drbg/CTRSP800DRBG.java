package org.spongycastle.crypto.prng.drbg;

import org.spongycastle.crypto.BlockCipher;
import org.spongycastle.crypto.params.KeyParameter;
import org.spongycastle.crypto.prng.EntropySource;
import org.spongycastle.util.Arrays;
import org.spongycastle.util.encoders.Hex;







public class CTRSP800DRBG
  implements SP80090DRBG
{
  private static final long TDEA_RESEED_MAX = 2147483648L;
  private static final long AES_RESEED_MAX = 140737488355328L;
  private static final int TDEA_MAX_BITS_REQUEST = 4096;
  private static final int AES_MAX_BITS_REQUEST = 262144;
  private EntropySource _entropySource;
  private BlockCipher _engine;
  private int _keySizeInBits;
  private int _seedLength;
  private int _securityStrength;
  private byte[] _Key;
  private byte[] _V;
  private long _reseedCounter = 0L;
  private boolean _isTDEA = false;
  












  public CTRSP800DRBG(BlockCipher engine, int keySizeInBits, int securityStrength, EntropySource entropySource, byte[] personalizationString, byte[] nonce)
  {
    _entropySource = entropySource;
    _engine = engine;
    
    _keySizeInBits = keySizeInBits;
    _securityStrength = securityStrength;
    _seedLength = (keySizeInBits + engine.getBlockSize() * 8);
    _isTDEA = isTDEA(engine);
    
    if (securityStrength > 256)
    {
      throw new IllegalArgumentException("Requested security strength is not supported by the derivation function");
    }
    
    if (getMaxSecurityStrength(engine, keySizeInBits) < securityStrength)
    {
      throw new IllegalArgumentException("Requested security strength is not supported by block cipher and key size");
    }
    
    if (entropySource.entropySize() < securityStrength)
    {
      throw new IllegalArgumentException("Not enough entropy for security strength required");
    }
    
    byte[] entropy = getEntropy();
    
    CTR_DRBG_Instantiate_algorithm(entropy, nonce, personalizationString);
  }
  

  private void CTR_DRBG_Instantiate_algorithm(byte[] entropy, byte[] nonce, byte[] personalisationString)
  {
    byte[] seedMaterial = Arrays.concatenate(entropy, nonce, personalisationString);
    byte[] seed = Block_Cipher_df(seedMaterial, _seedLength);
    
    int outlen = _engine.getBlockSize();
    
    _Key = new byte[(_keySizeInBits + 7) / 8];
    _V = new byte[outlen];
    

    CTR_DRBG_Update(seed, _Key, _V);
    
    _reseedCounter = 1L;
  }
  
  private void CTR_DRBG_Update(byte[] seed, byte[] key, byte[] v)
  {
    byte[] temp = new byte[seed.length];
    byte[] outputBlock = new byte[_engine.getBlockSize()];
    
    int i = 0;
    int outLen = _engine.getBlockSize();
    
    _engine.init(true, new KeyParameter(expandKey(key)));
    while (i * outLen < seed.length)
    {
      addOneTo(v);
      _engine.processBlock(v, 0, outputBlock, 0);
      
      int bytesToCopy = temp.length - i * outLen > outLen ? outLen : temp.length - i * outLen;
      

      System.arraycopy(outputBlock, 0, temp, i * outLen, bytesToCopy);
      i++;
    }
    
    XOR(temp, seed, temp, 0);
    
    System.arraycopy(temp, 0, key, 0, key.length);
    System.arraycopy(temp, key.length, v, 0, v.length);
  }
  
  private void CTR_DRBG_Reseed_algorithm(byte[] additionalInput)
  {
    byte[] seedMaterial = Arrays.concatenate(getEntropy(), additionalInput);
    
    seedMaterial = Block_Cipher_df(seedMaterial, _seedLength);
    
    CTR_DRBG_Update(seedMaterial, _Key, _V);
    
    _reseedCounter = 1L;
  }
  
  private void XOR(byte[] out, byte[] a, byte[] b, int bOff)
  {
    for (int i = 0; i < out.length; i++)
    {
      out[i] = ((byte)(a[i] ^ b[(i + bOff)]));
    }
  }
  
  private void addOneTo(byte[] longer)
  {
    int carry = 1;
    for (int i = 1; i <= longer.length; i++)
    {
      int res = (longer[(longer.length - i)] & 0xFF) + carry;
      carry = res > 255 ? 1 : 0;
      longer[(longer.length - i)] = ((byte)res);
    }
  }
  
  private byte[] getEntropy()
  {
    byte[] entropy = _entropySource.getEntropy();
    if (entropy.length < (_securityStrength + 7) / 8)
    {
      throw new IllegalStateException("Insufficient entropy provided by entropy source");
    }
    return entropy;
  }
  


  private static final byte[] K_BITS = Hex.decode("000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F");
  




























































  private byte[] Block_Cipher_df(byte[] inputString, int bitLength)
  {
    int outLen = _engine.getBlockSize();
    int L = inputString.length;
    int N = bitLength / 8;
    
    int sLen = 8 + L + 1;
    int blockLen = (sLen + outLen - 1) / outLen * outLen;
    byte[] S = new byte[blockLen];
    copyIntToByteArray(S, L, 0);
    copyIntToByteArray(S, N, 4);
    System.arraycopy(inputString, 0, S, 8, L);
    S[(8 + L)] = Byte.MIN_VALUE;
    

    byte[] temp = new byte[_keySizeInBits / 8 + outLen];
    byte[] bccOut = new byte[outLen];
    
    byte[] IV = new byte[outLen];
    
    int i = 0;
    byte[] K = new byte[_keySizeInBits / 8];
    System.arraycopy(K_BITS, 0, K, 0, K.length);
    
    while (i * outLen * 8 < _keySizeInBits + outLen * 8)
    {
      copyIntToByteArray(IV, i, 0);
      BCC(bccOut, K, IV, S);
      
      int bytesToCopy = temp.length - i * outLen > outLen ? outLen : temp.length - i * outLen;
      


      System.arraycopy(bccOut, 0, temp, i * outLen, bytesToCopy);
      i++;
    }
    
    byte[] X = new byte[outLen];
    System.arraycopy(temp, 0, K, 0, K.length);
    System.arraycopy(temp, K.length, X, 0, X.length);
    
    temp = new byte[bitLength / 2];
    
    i = 0;
    _engine.init(true, new KeyParameter(expandKey(K)));
    
    while (i * outLen < temp.length)
    {
      _engine.processBlock(X, 0, X, 0);
      
      int bytesToCopy = temp.length - i * outLen > outLen ? outLen : temp.length - i * outLen;
      


      System.arraycopy(X, 0, temp, i * outLen, bytesToCopy);
      i++;
    }
    
    return temp;
  }
  












  private void BCC(byte[] bccOut, byte[] k, byte[] iV, byte[] data)
  {
    int outlen = _engine.getBlockSize();
    byte[] chainingValue = new byte[outlen];
    int n = data.length / outlen;
    
    byte[] inputBlock = new byte[outlen];
    
    _engine.init(true, new KeyParameter(expandKey(k)));
    
    _engine.processBlock(iV, 0, chainingValue, 0);
    
    for (int i = 0; i < n; i++)
    {
      XOR(inputBlock, chainingValue, data, i * outlen);
      _engine.processBlock(inputBlock, 0, chainingValue, 0);
    }
    
    System.arraycopy(chainingValue, 0, bccOut, 0, bccOut.length);
  }
  
  private void copyIntToByteArray(byte[] buf, int value, int offSet)
  {
    buf[(offSet + 0)] = ((byte)(value >> 24));
    buf[(offSet + 1)] = ((byte)(value >> 16));
    buf[(offSet + 2)] = ((byte)(value >> 8));
    buf[(offSet + 3)] = ((byte)value);
  }
  





  public int getBlockSize()
  {
    return _V.length * 8;
  }
  









  public int generate(byte[] output, byte[] additionalInput, boolean predictionResistant)
  {
    if (_isTDEA)
    {
      if (_reseedCounter > 2147483648L)
      {
        return -1;
      }
      
      if (Utils.isTooLarge(output, 512))
      {
        throw new IllegalArgumentException("Number of bits per request limited to 4096");
      }
    }
    else
    {
      if (_reseedCounter > 140737488355328L)
      {
        return -1;
      }
      
      if (Utils.isTooLarge(output, 32768))
      {
        throw new IllegalArgumentException("Number of bits per request limited to 262144");
      }
    }
    
    if (predictionResistant)
    {
      CTR_DRBG_Reseed_algorithm(additionalInput);
      additionalInput = null;
    }
    
    if (additionalInput != null)
    {
      additionalInput = Block_Cipher_df(additionalInput, _seedLength);
      CTR_DRBG_Update(additionalInput, _Key, _V);
    }
    else
    {
      additionalInput = new byte[_seedLength];
    }
    
    byte[] out = new byte[_V.length];
    
    _engine.init(true, new KeyParameter(expandKey(_Key)));
    
    for (int i = 0; i <= output.length / out.length; i++)
    {
      int bytesToCopy = output.length - i * out.length > out.length ? out.length : output.length - i * _V.length;
      


      if (bytesToCopy != 0)
      {
        addOneTo(_V);
        
        _engine.processBlock(_V, 0, out, 0);
        
        System.arraycopy(out, 0, output, i * out.length, bytesToCopy);
      }
    }
    
    CTR_DRBG_Update(additionalInput, _Key, _V);
    
    _reseedCounter += 1L;
    
    return output.length * 8;
  }
  





  public void reseed(byte[] additionalInput)
  {
    CTR_DRBG_Reseed_algorithm(additionalInput);
  }
  
  private boolean isTDEA(BlockCipher cipher)
  {
    return (cipher.getAlgorithmName().equals("DESede")) || (cipher.getAlgorithmName().equals("TDEA"));
  }
  
  private int getMaxSecurityStrength(BlockCipher cipher, int keySizeInBits)
  {
    if ((isTDEA(cipher)) && (keySizeInBits == 168))
    {
      return 112;
    }
    if (cipher.getAlgorithmName().equals("AES"))
    {
      return keySizeInBits;
    }
    
    return -1;
  }
  
  byte[] expandKey(byte[] key)
  {
    if (_isTDEA)
    {

      byte[] tmp = new byte[24];
      
      padKey(key, 0, tmp, 0);
      padKey(key, 7, tmp, 8);
      padKey(key, 14, tmp, 16);
      
      return tmp;
    }
    

    return key;
  }
  









  private void padKey(byte[] keyMaster, int keyOff, byte[] tmp, int tmpOff)
  {
    tmp[(tmpOff + 0)] = ((byte)(keyMaster[(keyOff + 0)] & 0xFE));
    tmp[(tmpOff + 1)] = ((byte)(keyMaster[(keyOff + 0)] << 7 | (keyMaster[(keyOff + 1)] & 0xFC) >>> 1));
    tmp[(tmpOff + 2)] = ((byte)(keyMaster[(keyOff + 1)] << 6 | (keyMaster[(keyOff + 2)] & 0xF8) >>> 2));
    tmp[(tmpOff + 3)] = ((byte)(keyMaster[(keyOff + 2)] << 5 | (keyMaster[(keyOff + 3)] & 0xF0) >>> 3));
    tmp[(tmpOff + 4)] = ((byte)(keyMaster[(keyOff + 3)] << 4 | (keyMaster[(keyOff + 4)] & 0xE0) >>> 4));
    tmp[(tmpOff + 5)] = ((byte)(keyMaster[(keyOff + 4)] << 3 | (keyMaster[(keyOff + 5)] & 0xC0) >>> 5));
    tmp[(tmpOff + 6)] = ((byte)(keyMaster[(keyOff + 5)] << 2 | (keyMaster[(keyOff + 6)] & 0x80) >>> 6));
    tmp[(tmpOff + 7)] = ((byte)(keyMaster[(keyOff + 6)] << 1));
    
    for (int i = tmpOff; i <= tmpOff + 7; i++)
    {
      int b = tmp[i];
      tmp[i] = ((byte)(b & 0xFE | (b >> 1 ^ b >> 2 ^ b >> 3 ^ b >> 4 ^ b >> 5 ^ b >> 6 ^ b >> 7 ^ 0x1) & 0x1));
    }
  }
}
