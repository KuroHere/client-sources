package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.DERSequence;
import org.spongycastle.asn1.DERUTF8String;


public class CMCStatusInfoBuilder
{
  private final CMCStatus cMCStatus;
  private final ASN1Sequence bodyList;
  private DERUTF8String statusString;
  private CMCStatusInfo.OtherInfo otherInfo;
  
  public CMCStatusInfoBuilder(CMCStatus cMCStatus, BodyPartID bodyPartID)
  {
    this.cMCStatus = cMCStatus;
    bodyList = new DERSequence(bodyPartID);
  }
  
  public CMCStatusInfoBuilder(CMCStatus cMCStatus, BodyPartID[] bodyList)
  {
    this.cMCStatus = cMCStatus;
    this.bodyList = new DERSequence(bodyList);
  }
  
  public CMCStatusInfoBuilder setStatusString(String statusString)
  {
    this.statusString = new DERUTF8String(statusString);
    
    return this;
  }
  
  public CMCStatusInfoBuilder setOtherInfo(CMCFailInfo failInfo)
  {
    otherInfo = new CMCStatusInfo.OtherInfo(failInfo);
    
    return this;
  }
  
  public CMCStatusInfoBuilder setOtherInfo(PendInfo pendInfo)
  {
    otherInfo = new CMCStatusInfo.OtherInfo(pendInfo);
    
    return this;
  }
  
  public CMCStatusInfo build()
  {
    return new CMCStatusInfo(cMCStatus, bodyList, statusString, otherInfo);
  }
}
