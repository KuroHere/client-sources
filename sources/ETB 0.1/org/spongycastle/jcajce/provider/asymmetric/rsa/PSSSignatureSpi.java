package org.spongycastle.jcajce.provider.asymmetric.rsa;

import java.io.ByteArrayOutputStream;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import org.spongycastle.asn1.ASN1ObjectIdentifier;
import org.spongycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.spongycastle.crypto.AsymmetricBlockCipher;
import org.spongycastle.crypto.CryptoException;
import org.spongycastle.crypto.Digest;
import org.spongycastle.crypto.engines.RSABlindedEngine;
import org.spongycastle.crypto.params.ParametersWithRandom;
import org.spongycastle.crypto.signers.PSSSigner;
import org.spongycastle.jcajce.provider.util.DigestFactory;
import org.spongycastle.jcajce.util.BCJcaJceHelper;
import org.spongycastle.jcajce.util.JcaJceHelper;

public class PSSSignatureSpi extends SignatureSpi
{
  private final JcaJceHelper helper = new BCJcaJceHelper();
  
  private AlgorithmParameters engineParams;
  
  private PSSParameterSpec paramSpec;
  
  private PSSParameterSpec originalSpec;
  private AsymmetricBlockCipher signer;
  private Digest contentDigest;
  private Digest mgfDigest;
  private int saltLength;
  private byte trailer;
  private boolean isRaw;
  private PSSSigner pss;
  
  private byte getTrailer(int trailerField)
  {
    if (trailerField == 1)
    {
      return -68;
    }
    
    throw new IllegalArgumentException("unknown trailer field");
  }
  
  private void setupContentDigest()
  {
    if (isRaw)
    {
      contentDigest = new NullPssDigest(mgfDigest);
    }
    else
    {
      contentDigest = mgfDigest;
    }
  }
  



  protected PSSSignatureSpi(AsymmetricBlockCipher signer, PSSParameterSpec paramSpecArg)
  {
    this(signer, paramSpecArg, false);
  }
  




  protected PSSSignatureSpi(AsymmetricBlockCipher signer, PSSParameterSpec baseParamSpec, boolean isRaw)
  {
    this.signer = signer;
    originalSpec = baseParamSpec;
    
    if (baseParamSpec == null)
    {
      paramSpec = PSSParameterSpec.DEFAULT;
    }
    else
    {
      paramSpec = baseParamSpec;
    }
    
    mgfDigest = DigestFactory.getDigest(paramSpec.getDigestAlgorithm());
    saltLength = paramSpec.getSaltLength();
    trailer = getTrailer(paramSpec.getTrailerField());
    this.isRaw = isRaw;
    
    setupContentDigest();
  }
  

  protected void engineInitVerify(PublicKey publicKey)
    throws InvalidKeyException
  {
    if (!(publicKey instanceof RSAPublicKey))
    {
      throw new InvalidKeyException("Supplied key is not a RSAPublicKey instance");
    }
    
    pss = new PSSSigner(signer, contentDigest, mgfDigest, saltLength, trailer);
    pss.init(false, 
      RSAUtil.generatePublicKeyParameter((RSAPublicKey)publicKey));
  }
  


  protected void engineInitSign(PrivateKey privateKey, SecureRandom random)
    throws InvalidKeyException
  {
    if (!(privateKey instanceof RSAPrivateKey))
    {
      throw new InvalidKeyException("Supplied key is not a RSAPrivateKey instance");
    }
    
    pss = new PSSSigner(signer, contentDigest, mgfDigest, saltLength, trailer);
    pss.init(true, new ParametersWithRandom(RSAUtil.generatePrivateKeyParameter((RSAPrivateKey)privateKey), random));
  }
  

  protected void engineInitSign(PrivateKey privateKey)
    throws InvalidKeyException
  {
    if (!(privateKey instanceof RSAPrivateKey))
    {
      throw new InvalidKeyException("Supplied key is not a RSAPrivateKey instance");
    }
    
    pss = new PSSSigner(signer, contentDigest, mgfDigest, saltLength, trailer);
    pss.init(true, RSAUtil.generatePrivateKeyParameter((RSAPrivateKey)privateKey));
  }
  

  protected void engineUpdate(byte b)
    throws SignatureException
  {
    pss.update(b);
  }
  



  protected void engineUpdate(byte[] b, int off, int len)
    throws SignatureException
  {
    pss.update(b, off, len);
  }
  
  protected byte[] engineSign()
    throws SignatureException
  {
    try
    {
      return pss.generateSignature();
    }
    catch (CryptoException e)
    {
      throw new SignatureException(e.getMessage());
    }
  }
  

  protected boolean engineVerify(byte[] sigBytes)
    throws SignatureException
  {
    return pss.verifySignature(sigBytes);
  }
  

  protected void engineSetParameter(AlgorithmParameterSpec params)
    throws InvalidAlgorithmParameterException
  {
    if ((params instanceof PSSParameterSpec))
    {
      PSSParameterSpec newParamSpec = (PSSParameterSpec)params;
      
      if (originalSpec != null)
      {
        if (!DigestFactory.isSameDigest(originalSpec.getDigestAlgorithm(), newParamSpec.getDigestAlgorithm()))
        {
          throw new InvalidAlgorithmParameterException("parameter must be using " + originalSpec.getDigestAlgorithm());
        }
      }
      if ((!newParamSpec.getMGFAlgorithm().equalsIgnoreCase("MGF1")) && (!newParamSpec.getMGFAlgorithm().equals(PKCSObjectIdentifiers.id_mgf1.getId())))
      {
        throw new InvalidAlgorithmParameterException("unknown mask generation function specified");
      }
      
      if (!(newParamSpec.getMGFParameters() instanceof MGF1ParameterSpec))
      {
        throw new InvalidAlgorithmParameterException("unknown MGF parameters");
      }
      
      MGF1ParameterSpec mgfParams = (MGF1ParameterSpec)newParamSpec.getMGFParameters();
      
      if (!DigestFactory.isSameDigest(mgfParams.getDigestAlgorithm(), newParamSpec.getDigestAlgorithm()))
      {
        throw new InvalidAlgorithmParameterException("digest algorithm for MGF should be the same as for PSS parameters.");
      }
      
      Digest newDigest = DigestFactory.getDigest(mgfParams.getDigestAlgorithm());
      
      if (newDigest == null)
      {
        throw new InvalidAlgorithmParameterException("no match on MGF digest algorithm: " + mgfParams.getDigestAlgorithm());
      }
      
      engineParams = null;
      paramSpec = newParamSpec;
      mgfDigest = newDigest;
      saltLength = paramSpec.getSaltLength();
      trailer = getTrailer(paramSpec.getTrailerField());
      
      setupContentDigest();
    }
    else
    {
      throw new InvalidAlgorithmParameterException("Only PSSParameterSpec supported");
    }
  }
  
  protected AlgorithmParameters engineGetParameters()
  {
    if (engineParams == null)
    {
      if (paramSpec != null)
      {
        try
        {
          engineParams = helper.createAlgorithmParameters("PSS");
          engineParams.init(paramSpec);
        }
        catch (Exception e)
        {
          throw new RuntimeException(e.toString());
        }
      }
    }
    
    return engineParams;
  }
  


  /**
   * @deprecated
   */
  protected void engineSetParameter(String param, Object value)
  {
    throw new UnsupportedOperationException("engineSetParameter unsupported");
  }
  

  protected Object engineGetParameter(String param)
  {
    throw new UnsupportedOperationException("engineGetParameter unsupported");
  }
  
  public static class nonePSS
    extends PSSSignatureSpi
  {
    public nonePSS()
    {
      super(null, true);
    }
  }
  
  public static class PSSwithRSA
    extends PSSSignatureSpi
  {
    public PSSwithRSA()
    {
      super(null);
    }
  }
  
  public static class SHA1withRSA
    extends PSSSignatureSpi
  {
    public SHA1withRSA()
    {
      super(PSSParameterSpec.DEFAULT);
    }
  }
  
  public static class SHA224withRSA
    extends PSSSignatureSpi
  {
    public SHA224withRSA()
    {
      super(new PSSParameterSpec("SHA-224", "MGF1", new MGF1ParameterSpec("SHA-224"), 28, 1));
    }
  }
  
  public static class SHA256withRSA
    extends PSSSignatureSpi
  {
    public SHA256withRSA()
    {
      super(new PSSParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), 32, 1));
    }
  }
  
  public static class SHA384withRSA
    extends PSSSignatureSpi
  {
    public SHA384withRSA()
    {
      super(new PSSParameterSpec("SHA-384", "MGF1", new MGF1ParameterSpec("SHA-384"), 48, 1));
    }
  }
  
  public static class SHA512withRSA
    extends PSSSignatureSpi
  {
    public SHA512withRSA()
    {
      super(new PSSParameterSpec("SHA-512", "MGF1", new MGF1ParameterSpec("SHA-512"), 64, 1));
    }
  }
  
  public static class SHA512_224withRSA
    extends PSSSignatureSpi
  {
    public SHA512_224withRSA()
    {
      super(new PSSParameterSpec("SHA-512(224)", "MGF1", new MGF1ParameterSpec("SHA-512(224)"), 28, 1));
    }
  }
  
  public static class SHA512_256withRSA
    extends PSSSignatureSpi
  {
    public SHA512_256withRSA()
    {
      super(new PSSParameterSpec("SHA-512(256)", "MGF1", new MGF1ParameterSpec("SHA-512(256)"), 32, 1));
    }
  }
  
  public static class SHA3_224withRSA
    extends PSSSignatureSpi
  {
    public SHA3_224withRSA()
    {
      super(new PSSParameterSpec("SHA3-224", "MGF1", new MGF1ParameterSpec("SHA3-224"), 28, 1));
    }
  }
  
  public static class SHA3_256withRSA
    extends PSSSignatureSpi
  {
    public SHA3_256withRSA()
    {
      super(new PSSParameterSpec("SHA3-256", "MGF1", new MGF1ParameterSpec("SHA3-256"), 32, 1));
    }
  }
  
  public static class SHA3_384withRSA
    extends PSSSignatureSpi
  {
    public SHA3_384withRSA()
    {
      super(new PSSParameterSpec("SHA3-384", "MGF1", new MGF1ParameterSpec("SHA3-384"), 48, 1));
    }
  }
  
  public static class SHA3_512withRSA
    extends PSSSignatureSpi
  {
    public SHA3_512withRSA()
    {
      super(new PSSParameterSpec("SHA3-512", "MGF1", new MGF1ParameterSpec("SHA3-512"), 64, 1));
    }
  }
  
  private class NullPssDigest
    implements Digest
  {
    private ByteArrayOutputStream bOut = new ByteArrayOutputStream();
    private Digest baseDigest;
    private boolean oddTime = true;
    
    public NullPssDigest(Digest mgfDigest)
    {
      baseDigest = mgfDigest;
    }
    
    public String getAlgorithmName()
    {
      return "NULL";
    }
    
    public int getDigestSize()
    {
      return baseDigest.getDigestSize();
    }
    
    public void update(byte in)
    {
      bOut.write(in);
    }
    
    public void update(byte[] in, int inOff, int len)
    {
      bOut.write(in, inOff, len);
    }
    
    public int doFinal(byte[] out, int outOff)
    {
      byte[] res = bOut.toByteArray();
      
      if (oddTime)
      {
        System.arraycopy(res, 0, out, outOff, res.length);
      }
      else
      {
        baseDigest.update(res, 0, res.length);
        
        baseDigest.doFinal(out, outOff);
      }
      
      reset();
      
      oddTime = (!oddTime);
      
      return res.length;
    }
    
    public void reset()
    {
      bOut.reset();
      baseDigest.reset();
    }
    
    public int getByteLength()
    {
      return 0;
    }
  }
}
