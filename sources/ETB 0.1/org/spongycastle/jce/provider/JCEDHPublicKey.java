package org.spongycastle.jce.provider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;
import org.spongycastle.asn1.ASN1Integer;
import org.spongycastle.asn1.ASN1ObjectIdentifier;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.pkcs.DHParameter;
import org.spongycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.spongycastle.asn1.x509.AlgorithmIdentifier;
import org.spongycastle.asn1.x509.SubjectPublicKeyInfo;
import org.spongycastle.asn1.x9.DHDomainParameters;
import org.spongycastle.asn1.x9.X9ObjectIdentifiers;
import org.spongycastle.crypto.params.DHParameters;
import org.spongycastle.crypto.params.DHPublicKeyParameters;
import org.spongycastle.jcajce.provider.asymmetric.util.KeyUtil;




public class JCEDHPublicKey
  implements DHPublicKey
{
  static final long serialVersionUID = -216691575254424324L;
  private BigInteger y;
  private DHParameterSpec dhSpec;
  private SubjectPublicKeyInfo info;
  
  JCEDHPublicKey(DHPublicKeySpec spec)
  {
    y = spec.getY();
    dhSpec = new DHParameterSpec(spec.getP(), spec.getG());
  }
  

  JCEDHPublicKey(DHPublicKey key)
  {
    y = key.getY();
    dhSpec = key.getParams();
  }
  

  JCEDHPublicKey(DHPublicKeyParameters params)
  {
    y = params.getY();
    dhSpec = new DHParameterSpec(params.getParameters().getP(), params.getParameters().getG(), params.getParameters().getL());
  }
  


  JCEDHPublicKey(BigInteger y, DHParameterSpec dhSpec)
  {
    this.y = y;
    this.dhSpec = dhSpec;
  }
  

  JCEDHPublicKey(SubjectPublicKeyInfo info)
  {
    this.info = info;
    

    try
    {
      derY = (ASN1Integer)info.parsePublicKey();
    }
    catch (IOException e) {
      ASN1Integer derY;
      throw new IllegalArgumentException("invalid info structure in DH public key");
    }
    ASN1Integer derY;
    y = derY.getValue();
    
    ASN1Sequence seq = ASN1Sequence.getInstance(info.getAlgorithmId().getParameters());
    ASN1ObjectIdentifier id = info.getAlgorithmId().getAlgorithm();
    

    if ((id.equals(PKCSObjectIdentifiers.dhKeyAgreement)) || (isPKCSParam(seq)))
    {
      DHParameter params = DHParameter.getInstance(seq);
      
      if (params.getL() != null)
      {
        dhSpec = new DHParameterSpec(params.getP(), params.getG(), params.getL().intValue());
      }
      else
      {
        dhSpec = new DHParameterSpec(params.getP(), params.getG());
      }
    }
    else if (id.equals(X9ObjectIdentifiers.dhpublicnumber))
    {
      DHDomainParameters params = DHDomainParameters.getInstance(seq);
      
      dhSpec = new DHParameterSpec(params.getP().getValue(), params.getG().getValue());
    }
    else
    {
      throw new IllegalArgumentException("unknown algorithm type: " + id);
    }
  }
  
  public String getAlgorithm()
  {
    return "DH";
  }
  
  public String getFormat()
  {
    return "X.509";
  }
  
  public byte[] getEncoded()
  {
    if (info != null)
    {
      return KeyUtil.getEncodedSubjectPublicKeyInfo(info);
    }
    
    return KeyUtil.getEncodedSubjectPublicKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.dhKeyAgreement, new DHParameter(dhSpec.getP(), dhSpec.getG(), dhSpec.getL())), new ASN1Integer(y));
  }
  
  public DHParameterSpec getParams()
  {
    return dhSpec;
  }
  
  public BigInteger getY()
  {
    return y;
  }
  
  private boolean isPKCSParam(ASN1Sequence seq)
  {
    if (seq.size() == 2)
    {
      return true;
    }
    
    if (seq.size() > 3)
    {
      return false;
    }
    
    ASN1Integer l = ASN1Integer.getInstance(seq.getObjectAt(2));
    ASN1Integer p = ASN1Integer.getInstance(seq.getObjectAt(0));
    
    if (l.getValue().compareTo(BigInteger.valueOf(p.getValue().bitLength())) > 0)
    {
      return false;
    }
    
    return true;
  }
  

  private void readObject(ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    y = ((BigInteger)in.readObject());
    dhSpec = new DHParameterSpec((BigInteger)in.readObject(), (BigInteger)in.readObject(), in.readInt());
  }
  

  private void writeObject(ObjectOutputStream out)
    throws IOException
  {
    out.writeObject(getY());
    out.writeObject(dhSpec.getP());
    out.writeObject(dhSpec.getG());
    out.writeInt(dhSpec.getL());
  }
}
