package org.spongycastle.jcajce.provider.symmetric;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.spec.IvParameterSpec;
import org.spongycastle.asn1.ASN1InputStream;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.misc.IDEACBCPar;
import org.spongycastle.asn1.misc.MiscObjectIdentifiers;
import org.spongycastle.crypto.CipherKeyGenerator;
import org.spongycastle.crypto.engines.IDEAEngine;
import org.spongycastle.crypto.macs.CBCBlockCipherMac;
import org.spongycastle.crypto.macs.CFBBlockCipherMac;
import org.spongycastle.crypto.modes.CBCBlockCipher;
import org.spongycastle.jcajce.provider.config.ConfigurableProvider;
import org.spongycastle.jcajce.provider.symmetric.util.BaseAlgorithmParameterGenerator;
import org.spongycastle.jcajce.provider.symmetric.util.BaseAlgorithmParameters;
import org.spongycastle.jcajce.provider.symmetric.util.BaseBlockCipher;
import org.spongycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.spongycastle.jcajce.provider.symmetric.util.BaseMac;
import org.spongycastle.jcajce.provider.symmetric.util.PBESecretKeyFactory;
import org.spongycastle.jcajce.provider.util.AlgorithmProvider;





public final class IDEA
{
  private IDEA() {}
  
  public static class ECB
    extends BaseBlockCipher
  {
    public ECB()
    {
      super();
    }
  }
  
  public static class CBC
    extends BaseBlockCipher
  {
    public CBC()
    {
      super(64);
    }
  }
  
  public static class KeyGen
    extends BaseKeyGenerator
  {
    public KeyGen()
    {
      super(128, new CipherKeyGenerator());
    }
  }
  
  public static class PBEWithSHAAndIDEAKeyGen
    extends PBESecretKeyFactory
  {
    public PBEWithSHAAndIDEAKeyGen()
    {
      super(null, true, 2, 1, 128, 64);
    }
  }
  
  public static class PBEWithSHAAndIDEA
    extends BaseBlockCipher
  {
    public PBEWithSHAAndIDEA()
    {
      super();
    }
  }
  
  public static class AlgParamGen
    extends BaseAlgorithmParameterGenerator
  {
    public AlgParamGen() {}
    
    protected void engineInit(AlgorithmParameterSpec genParamSpec, SecureRandom random)
      throws InvalidAlgorithmParameterException
    {
      throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for IDEA parameter generation.");
    }
    
    protected AlgorithmParameters engineGenerateParameters()
    {
      byte[] iv = new byte[8];
      
      if (random == null)
      {
        random = new SecureRandom();
      }
      
      random.nextBytes(iv);
      


      try
      {
        AlgorithmParameters params = createParametersInstance("IDEA");
        params.init(new IvParameterSpec(iv));
      }
      catch (Exception e)
      {
        throw new RuntimeException(e.getMessage());
      }
      AlgorithmParameters params;
      return params;
    }
  }
  
  public static class AlgParams extends BaseAlgorithmParameters
  {
    private byte[] iv;
    
    public AlgParams() {}
    
    protected byte[] engineGetEncoded() throws IOException
    {
      return engineGetEncoded("ASN.1");
    }
    

    protected byte[] engineGetEncoded(String format)
      throws IOException
    {
      if (isASN1FormatString(format))
      {
        return new IDEACBCPar(engineGetEncoded("RAW")).getEncoded();
      }
      
      if (format.equals("RAW"))
      {
        byte[] tmp = new byte[iv.length];
        
        System.arraycopy(iv, 0, tmp, 0, iv.length);
        return tmp;
      }
      
      return null;
    }
    

    protected AlgorithmParameterSpec localEngineGetParameterSpec(Class paramSpec)
      throws InvalidParameterSpecException
    {
      if (paramSpec == IvParameterSpec.class)
      {
        return new IvParameterSpec(iv);
      }
      
      throw new InvalidParameterSpecException("unknown parameter spec passed to IV parameters object.");
    }
    

    protected void engineInit(AlgorithmParameterSpec paramSpec)
      throws InvalidParameterSpecException
    {
      if (!(paramSpec instanceof IvParameterSpec))
      {
        throw new InvalidParameterSpecException("IvParameterSpec required to initialise a IV parameters algorithm parameters object");
      }
      
      iv = ((IvParameterSpec)paramSpec).getIV();
    }
    

    protected void engineInit(byte[] params)
      throws IOException
    {
      iv = new byte[params.length];
      
      System.arraycopy(params, 0, iv, 0, iv.length);
    }
    


    protected void engineInit(byte[] params, String format)
      throws IOException
    {
      if (format.equals("RAW"))
      {
        engineInit(params);
        return;
      }
      if (format.equals("ASN.1"))
      {
        ASN1InputStream aIn = new ASN1InputStream(params);
        IDEACBCPar oct = new IDEACBCPar((ASN1Sequence)aIn.readObject());
        
        engineInit(oct.getIV());
        return;
      }
      
      throw new IOException("Unknown parameters format in IV parameters object");
    }
    
    protected String engineToString()
    {
      return "IDEA Parameters";
    }
  }
  
  public static class Mac
    extends BaseMac
  {
    public Mac()
    {
      super();
    }
  }
  
  public static class CFB8Mac
    extends BaseMac
  {
    public CFB8Mac()
    {
      super();
    }
  }
  
  public static class Mappings
    extends AlgorithmProvider
  {
    private static final String PREFIX = IDEA.class.getName();
    

    public Mappings() {}
    

    public void configure(ConfigurableProvider provider)
    {
      provider.addAlgorithm("AlgorithmParameterGenerator.IDEA", PREFIX + "$AlgParamGen");
      provider.addAlgorithm("AlgorithmParameterGenerator.1.3.6.1.4.1.188.7.1.1.2", PREFIX + "$AlgParamGen");
      provider.addAlgorithm("AlgorithmParameters.IDEA", PREFIX + "$AlgParams");
      provider.addAlgorithm("AlgorithmParameters.1.3.6.1.4.1.188.7.1.1.2", PREFIX + "$AlgParams");
      provider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHAANDIDEA", "PKCS12PBE");
      provider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHAANDIDEA-CBC", "PKCS12PBE");
      provider.addAlgorithm("Cipher.IDEA", PREFIX + "$ECB");
      provider.addAlgorithm("Cipher", MiscObjectIdentifiers.as_sys_sec_alg_ideaCBC, PREFIX + "$CBC");
      provider.addAlgorithm("Cipher.PBEWITHSHAANDIDEA-CBC", PREFIX + "$PBEWithSHAAndIDEA");
      provider.addAlgorithm("KeyGenerator.IDEA", PREFIX + "$KeyGen");
      provider.addAlgorithm("KeyGenerator", MiscObjectIdentifiers.as_sys_sec_alg_ideaCBC, PREFIX + "$KeyGen");
      provider.addAlgorithm("SecretKeyFactory.PBEWITHSHAANDIDEA-CBC", PREFIX + "$PBEWithSHAAndIDEAKeyGen");
      provider.addAlgorithm("Mac.IDEAMAC", PREFIX + "$Mac");
      provider.addAlgorithm("Alg.Alias.Mac.IDEA", "IDEAMAC");
      provider.addAlgorithm("Mac.IDEAMAC/CFB8", PREFIX + "$CFB8Mac");
      provider.addAlgorithm("Alg.Alias.Mac.IDEA/CFB8", "IDEAMAC/CFB8");
    }
  }
}
