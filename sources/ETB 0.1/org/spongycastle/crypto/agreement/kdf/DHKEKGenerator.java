package org.spongycastle.crypto.agreement.kdf;

import java.io.IOException;
import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.ASN1ObjectIdentifier;
import org.spongycastle.asn1.DEROctetString;
import org.spongycastle.asn1.DERSequence;
import org.spongycastle.asn1.DERTaggedObject;
import org.spongycastle.crypto.DataLengthException;
import org.spongycastle.crypto.DerivationFunction;
import org.spongycastle.crypto.DerivationParameters;
import org.spongycastle.crypto.Digest;
import org.spongycastle.crypto.OutputLengthException;
import org.spongycastle.util.Pack;








public class DHKEKGenerator
  implements DerivationFunction
{
  private final Digest digest;
  private ASN1ObjectIdentifier algorithm;
  private int keySize;
  private byte[] z;
  private byte[] partyAInfo;
  
  public DHKEKGenerator(Digest digest)
  {
    this.digest = digest;
  }
  
  public void init(DerivationParameters param)
  {
    DHKDFParameters params = (DHKDFParameters)param;
    
    algorithm = params.getAlgorithm();
    keySize = params.getKeySize();
    z = params.getZ();
    partyAInfo = params.getExtraInfo();
  }
  
  public Digest getDigest()
  {
    return digest;
  }
  
  public int generateBytes(byte[] out, int outOff, int len)
    throws DataLengthException, IllegalArgumentException
  {
    if (out.length - len < outOff)
    {
      throw new OutputLengthException("output buffer too small");
    }
    
    long oBytes = len;
    int outLen = digest.getDigestSize();
    






    if (oBytes > 8589934591L)
    {
      throw new IllegalArgumentException("Output length too large");
    }
    
    int cThreshold = (int)((oBytes + outLen - 1L) / outLen);
    
    byte[] dig = new byte[digest.getDigestSize()];
    
    int counter = 1;
    
    for (int i = 0; i < cThreshold; i++)
    {
      digest.update(z, 0, z.length);
      

      ASN1EncodableVector v1 = new ASN1EncodableVector();
      
      ASN1EncodableVector v2 = new ASN1EncodableVector();
      
      v2.add(algorithm);
      v2.add(new DEROctetString(Pack.intToBigEndian(counter)));
      
      v1.add(new DERSequence(v2));
      
      if (partyAInfo != null)
      {
        v1.add(new DERTaggedObject(true, 0, new DEROctetString(partyAInfo)));
      }
      
      v1.add(new DERTaggedObject(true, 2, new DEROctetString(Pack.intToBigEndian(keySize))));
      
      try
      {
        byte[] other = new DERSequence(v1).getEncoded("DER");
        
        digest.update(other, 0, other.length);
      }
      catch (IOException e)
      {
        throw new IllegalArgumentException("unable to encode parameter info: " + e.getMessage());
      }
      
      digest.doFinal(dig, 0);
      
      if (len > outLen)
      {
        System.arraycopy(dig, 0, out, outOff, outLen);
        outOff += outLen;
        len -= outLen;
      }
      else
      {
        System.arraycopy(dig, 0, out, outOff, len);
      }
      
      counter++;
    }
    
    digest.reset();
    
    return (int)oBytes;
  }
}
