package org.spongycastle.asn1.cms;

import java.io.IOException;
import org.spongycastle.asn1.ASN1Encodable;
import org.spongycastle.asn1.ASN1Integer;
import org.spongycastle.asn1.ASN1SequenceParser;
import org.spongycastle.asn1.ASN1SetParser;
import org.spongycastle.asn1.ASN1TaggedObjectParser;

















public class EnvelopedDataParser
{
  private ASN1SequenceParser _seq;
  private ASN1Integer _version;
  private ASN1Encodable _nextObject;
  private boolean _originatorInfoCalled;
  
  public EnvelopedDataParser(ASN1SequenceParser seq)
    throws IOException
  {
    _seq = seq;
    _version = ASN1Integer.getInstance(seq.readObject());
  }
  
  public ASN1Integer getVersion()
  {
    return _version;
  }
  
  public OriginatorInfo getOriginatorInfo()
    throws IOException
  {
    _originatorInfoCalled = true;
    
    if (_nextObject == null)
    {
      _nextObject = _seq.readObject();
    }
    
    if (((_nextObject instanceof ASN1TaggedObjectParser)) && (((ASN1TaggedObjectParser)_nextObject).getTagNo() == 0))
    {
      ASN1SequenceParser originatorInfo = (ASN1SequenceParser)((ASN1TaggedObjectParser)_nextObject).getObjectParser(16, false);
      _nextObject = null;
      return OriginatorInfo.getInstance(originatorInfo.toASN1Primitive());
    }
    
    return null;
  }
  
  public ASN1SetParser getRecipientInfos()
    throws IOException
  {
    if (!_originatorInfoCalled)
    {
      getOriginatorInfo();
    }
    
    if (_nextObject == null)
    {
      _nextObject = _seq.readObject();
    }
    
    ASN1SetParser recipientInfos = (ASN1SetParser)_nextObject;
    _nextObject = null;
    return recipientInfos;
  }
  
  public EncryptedContentInfoParser getEncryptedContentInfo()
    throws IOException
  {
    if (_nextObject == null)
    {
      _nextObject = _seq.readObject();
    }
    

    if (_nextObject != null)
    {
      ASN1SequenceParser o = (ASN1SequenceParser)_nextObject;
      _nextObject = null;
      return new EncryptedContentInfoParser(o);
    }
    
    return null;
  }
  
  public ASN1SetParser getUnprotectedAttrs()
    throws IOException
  {
    if (_nextObject == null)
    {
      _nextObject = _seq.readObject();
    }
    

    if (_nextObject != null)
    {
      ASN1Encodable o = _nextObject;
      _nextObject = null;
      return (ASN1SetParser)((ASN1TaggedObjectParser)o).getObjectParser(17, false);
    }
    
    return null;
  }
}
