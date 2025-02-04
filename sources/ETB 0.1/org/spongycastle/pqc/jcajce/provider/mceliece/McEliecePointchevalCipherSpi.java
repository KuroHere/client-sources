package org.spongycastle.pqc.jcajce.provider.mceliece;

import java.io.ByteArrayOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.BadPaddingException;
import org.spongycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.spongycastle.asn1.x509.X509ObjectIdentifiers;
import org.spongycastle.crypto.CipherParameters;
import org.spongycastle.crypto.Digest;
import org.spongycastle.crypto.InvalidCipherTextException;
import org.spongycastle.crypto.params.ParametersWithRandom;
import org.spongycastle.crypto.util.DigestFactory;
import org.spongycastle.pqc.crypto.mceliece.McElieceCCA2KeyParameters;
import org.spongycastle.pqc.crypto.mceliece.McEliecePointchevalCipher;
import org.spongycastle.pqc.jcajce.provider.util.AsymmetricHybridCipher;








public class McEliecePointchevalCipherSpi
  extends AsymmetricHybridCipher
  implements PKCSObjectIdentifiers, X509ObjectIdentifiers
{
  private Digest digest;
  private McEliecePointchevalCipher cipher;
  private ByteArrayOutputStream buf = new ByteArrayOutputStream();
  

  protected McEliecePointchevalCipherSpi(Digest digest, McEliecePointchevalCipher cipher)
  {
    this.digest = digest;
    this.cipher = cipher;
    buf = new ByteArrayOutputStream();
  }
  








  public byte[] update(byte[] input, int inOff, int inLen)
  {
    buf.write(input, inOff, inLen);
    return new byte[0];
  }
  












  public byte[] doFinal(byte[] input, int inOff, int inLen)
    throws BadPaddingException
  {
    update(input, inOff, inLen);
    byte[] data = buf.toByteArray();
    buf.reset();
    if (opMode == 1)
    {
      return cipher.messageEncrypt(data);
    }
    if (opMode == 2)
    {
      try
      {
        return cipher.messageDecrypt(data);
      }
      catch (InvalidCipherTextException e)
      {
        throw new BadPaddingException(e.getMessage());
      }
    }
    return null;
  }
  
  protected int encryptOutputSize(int inLen)
  {
    return 0;
  }
  
  protected int decryptOutputSize(int inLen)
  {
    return 0;
  }
  



  protected void initCipherEncrypt(Key key, AlgorithmParameterSpec params, SecureRandom sr)
    throws InvalidKeyException, InvalidAlgorithmParameterException
  {
    CipherParameters param = McElieceCCA2KeysToParams.generatePublicKeyParameter((PublicKey)key);
    
    param = new ParametersWithRandom(param, sr);
    digest.reset();
    cipher.init(true, param);
  }
  

  protected void initCipherDecrypt(Key key, AlgorithmParameterSpec params)
    throws InvalidKeyException, InvalidAlgorithmParameterException
  {
    CipherParameters param = McElieceCCA2KeysToParams.generatePrivateKeyParameter((PrivateKey)key);
    
    digest.reset();
    cipher.init(false, param);
  }
  
  public String getName()
  {
    return "McEliecePointchevalCipher";
  }
  
  public int getKeySize(Key key)
    throws InvalidKeyException
  {
    McElieceCCA2KeyParameters mcElieceCCA2KeyParameters;
    McElieceCCA2KeyParameters mcElieceCCA2KeyParameters;
    if ((key instanceof PublicKey))
    {
      mcElieceCCA2KeyParameters = (McElieceCCA2KeyParameters)McElieceCCA2KeysToParams.generatePublicKeyParameter((PublicKey)key);
    }
    else
    {
      mcElieceCCA2KeyParameters = (McElieceCCA2KeyParameters)McElieceCCA2KeysToParams.generatePrivateKeyParameter((PrivateKey)key);
    }
    
    return cipher.getKeySize(mcElieceCCA2KeyParameters);
  }
  


  public static class McEliecePointcheval
    extends McEliecePointchevalCipherSpi
  {
    public McEliecePointcheval()
    {
      super(new McEliecePointchevalCipher());
    }
  }
  
  public static class McEliecePointcheval224
    extends McEliecePointchevalCipherSpi
  {
    public McEliecePointcheval224()
    {
      super(new McEliecePointchevalCipher());
    }
  }
  
  public static class McEliecePointcheval256
    extends McEliecePointchevalCipherSpi
  {
    public McEliecePointcheval256()
    {
      super(new McEliecePointchevalCipher());
    }
  }
  
  public static class McEliecePointcheval384
    extends McEliecePointchevalCipherSpi
  {
    public McEliecePointcheval384()
    {
      super(new McEliecePointchevalCipher());
    }
  }
  
  public static class McEliecePointcheval512
    extends McEliecePointchevalCipherSpi
  {
    public McEliecePointcheval512()
    {
      super(new McEliecePointchevalCipher());
    }
  }
}
