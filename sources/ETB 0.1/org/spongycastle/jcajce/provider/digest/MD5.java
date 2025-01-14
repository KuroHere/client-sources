package org.spongycastle.jcajce.provider.digest;

import org.spongycastle.asn1.iana.IANAObjectIdentifiers;
import org.spongycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.spongycastle.crypto.CipherKeyGenerator;
import org.spongycastle.crypto.digests.MD5Digest;
import org.spongycastle.crypto.macs.HMac;
import org.spongycastle.jcajce.provider.config.ConfigurableProvider;
import org.spongycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.spongycastle.jcajce.provider.symmetric.util.BaseMac;







public class MD5
{
  private MD5() {}
  
  public static class HashMac
    extends BaseMac
  {
    public HashMac()
    {
      super();
    }
  }
  
  public static class KeyGenerator
    extends BaseKeyGenerator
  {
    public KeyGenerator()
    {
      super(128, new CipherKeyGenerator());
    }
  }
  
  public static class Digest
    extends BCMessageDigest
    implements Cloneable
  {
    public Digest()
    {
      super();
    }
    
    public Object clone()
      throws CloneNotSupportedException
    {
      Digest d = (Digest)super.clone();
      digest = new MD5Digest((MD5Digest)digest);
      
      return d;
    }
  }
  
  public static class Mappings
    extends DigestAlgorithmProvider
  {
    private static final String PREFIX = MD5.class.getName();
    

    public Mappings() {}
    

    public void configure(ConfigurableProvider provider)
    {
      provider.addAlgorithm("MessageDigest.MD5", PREFIX + "$Digest");
      provider.addAlgorithm("Alg.Alias.MessageDigest." + PKCSObjectIdentifiers.md5, "MD5");
      
      addHMACAlgorithm(provider, "MD5", PREFIX + "$HashMac", PREFIX + "$KeyGenerator");
      addHMACAlias(provider, "MD5", IANAObjectIdentifiers.hmacMD5);
    }
  }
}
