package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.Digest;




class CombinedHash
  implements TlsHandshakeHash
{
  protected TlsContext context;
  protected Digest md5;
  protected Digest sha1;
  
  CombinedHash()
  {
    md5 = TlsUtils.createHash((short)1);
    sha1 = TlsUtils.createHash((short)2);
  }
  
  CombinedHash(CombinedHash t)
  {
    context = context;
    md5 = TlsUtils.cloneHash((short)1, md5);
    sha1 = TlsUtils.cloneHash((short)2, sha1);
  }
  
  public void init(TlsContext context)
  {
    this.context = context;
  }
  
  public TlsHandshakeHash notifyPRFDetermined()
  {
    return this;
  }
  
  public void trackHashAlgorithm(short hashAlgorithm)
  {
    throw new IllegalStateException("CombinedHash only supports calculating the legacy PRF for handshake hash");
  }
  

  public void sealHashAlgorithms() {}
  

  public TlsHandshakeHash stopTracking()
  {
    return new CombinedHash(this);
  }
  
  public Digest forkPRFHash()
  {
    return new CombinedHash(this);
  }
  
  public byte[] getFinalHash(short hashAlgorithm)
  {
    throw new IllegalStateException("CombinedHash doesn't support multiple hashes");
  }
  



  public String getAlgorithmName()
  {
    return md5.getAlgorithmName() + " and " + sha1.getAlgorithmName();
  }
  



  public int getDigestSize()
  {
    return md5.getDigestSize() + sha1.getDigestSize();
  }
  



  public void update(byte input)
  {
    md5.update(input);
    sha1.update(input);
  }
  



  public void update(byte[] input, int inOff, int len)
  {
    md5.update(input, inOff, len);
    sha1.update(input, inOff, len);
  }
  



  public int doFinal(byte[] output, int outOff)
  {
    if ((context != null) && (TlsUtils.isSSL(context)))
    {
      ssl3Complete(md5, SSL3Mac.IPAD, SSL3Mac.OPAD, 48);
      ssl3Complete(sha1, SSL3Mac.IPAD, SSL3Mac.OPAD, 40);
    }
    
    int i1 = md5.doFinal(output, outOff);
    int i2 = sha1.doFinal(output, outOff + i1);
    return i1 + i2;
  }
  



  public void reset()
  {
    md5.reset();
    sha1.reset();
  }
  
  protected void ssl3Complete(Digest d, byte[] ipad, byte[] opad, int padLength)
  {
    byte[] master_secret = context.getSecurityParameters().masterSecret;
    
    d.update(master_secret, 0, master_secret.length);
    d.update(ipad, 0, padLength);
    
    byte[] tmp = new byte[d.getDigestSize()];
    d.doFinal(tmp, 0);
    
    d.update(master_secret, 0, master_secret.length);
    d.update(opad, 0, padLength);
    d.update(tmp, 0, tmp.length);
  }
}
