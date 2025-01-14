package org.spongycastle.asn1.x500.style;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import org.spongycastle.asn1.ASN1Encodable;
import org.spongycastle.asn1.ASN1ObjectIdentifier;
import org.spongycastle.asn1.ASN1ParsingException;
import org.spongycastle.asn1.DERUTF8String;
import org.spongycastle.asn1.x500.AttributeTypeAndValue;
import org.spongycastle.asn1.x500.RDN;
import org.spongycastle.asn1.x500.X500Name;
import org.spongycastle.asn1.x500.X500NameStyle;












public abstract class AbstractX500NameStyle
  implements X500NameStyle
{
  public AbstractX500NameStyle() {}
  
  public static Hashtable copyHashTable(Hashtable paramsMap)
  {
    Hashtable newTable = new Hashtable();
    
    Enumeration keys = paramsMap.keys();
    while (keys.hasMoreElements())
    {
      Object key = keys.nextElement();
      newTable.put(key, paramsMap.get(key));
    }
    
    return newTable;
  }
  
  private int calcHashCode(ASN1Encodable enc)
  {
    String value = IETFUtils.valueToString(enc);
    value = IETFUtils.canonicalize(value);
    return value.hashCode();
  }
  
  public int calculateHashCode(X500Name name)
  {
    int hashCodeValue = 0;
    RDN[] rdns = name.getRDNs();
    

    for (int i = 0; i != rdns.length; i++)
    {
      if (rdns[i].isMultiValued())
      {
        AttributeTypeAndValue[] atv = rdns[i].getTypesAndValues();
        
        for (int j = 0; j != atv.length; j++)
        {
          hashCodeValue ^= atv[j].getType().hashCode();
          hashCodeValue ^= calcHashCode(atv[j].getValue());
        }
      }
      else
      {
        hashCodeValue ^= rdns[i].getFirst().getType().hashCode();
        hashCodeValue ^= calcHashCode(rdns[i].getFirst().getValue());
      }
    }
    
    return hashCodeValue;
  }
  















  public ASN1Encodable stringToValue(ASN1ObjectIdentifier oid, String value)
  {
    if ((value.length() != 0) && (value.charAt(0) == '#'))
    {
      try
      {
        return IETFUtils.valueFromHexString(value, 1);
      }
      catch (IOException e)
      {
        throw new ASN1ParsingException("can't recode value for oid " + oid.getId());
      }
    }
    
    if ((value.length() != 0) && (value.charAt(0) == '\\'))
    {
      value = value.substring(1);
    }
    
    return encodeStringValue(oid, value);
  }
  











  protected ASN1Encodable encodeStringValue(ASN1ObjectIdentifier oid, String value)
  {
    return new DERUTF8String(value);
  }
  
  public boolean areEqual(X500Name name1, X500Name name2)
  {
    RDN[] rdns1 = name1.getRDNs();
    RDN[] rdns2 = name2.getRDNs();
    
    if (rdns1.length != rdns2.length)
    {
      return false;
    }
    
    boolean reverse = false;
    
    if ((rdns1[0].getFirst() != null) && (rdns2[0].getFirst() != null))
    {
      reverse = !rdns1[0].getFirst().getType().equals(rdns2[0].getFirst().getType());
    }
    
    for (int i = 0; i != rdns1.length; i++)
    {
      if (!foundMatch(reverse, rdns1[i], rdns2))
      {
        return false;
      }
    }
    
    return true;
  }
  
  private boolean foundMatch(boolean reverse, RDN rdn, RDN[] possRDNs)
  {
    if (reverse)
    {
      for (int i = possRDNs.length - 1; i >= 0; i--)
      {
        if ((possRDNs[i] != null) && (rdnAreEqual(rdn, possRDNs[i])))
        {
          possRDNs[i] = null;
          return true;
        }
        
      }
      
    } else {
      for (int i = 0; i != possRDNs.length; i++)
      {
        if ((possRDNs[i] != null) && (rdnAreEqual(rdn, possRDNs[i])))
        {
          possRDNs[i] = null;
          return true;
        }
      }
    }
    
    return false;
  }
  
  protected boolean rdnAreEqual(RDN rdn1, RDN rdn2)
  {
    return IETFUtils.rDNAreEqual(rdn1, rdn2);
  }
}
