package org.spongycastle.jcajce.provider.asymmetric.x509;

import java.io.IOException;
import java.io.InputStream;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.util.encoders.Base64;



class PEMUtil
{
  private final String _header1;
  private final String _header2;
  private final String _header3;
  private final String _footer1;
  private final String _footer2;
  private final String _footer3;
  
  PEMUtil(String type)
  {
    _header1 = ("-----BEGIN " + type + "-----");
    _header2 = ("-----BEGIN X509 " + type + "-----");
    _header3 = "-----BEGIN PKCS7-----";
    _footer1 = ("-----END " + type + "-----");
    _footer2 = ("-----END X509 " + type + "-----");
    _footer3 = "-----END PKCS7-----";
  }
  


  private String readLine(InputStream in)
    throws IOException
  {
    StringBuffer l = new StringBuffer();
    int c;
    do
    {
      while (((c = in.read()) != 13) && (c != 10) && (c >= 0))
      {
        l.append((char)c);
      }
      
    } while ((c >= 0) && (l.length() == 0));
    
    if (c < 0)
    {
      return null;
    }
    

    if (c == 13)
    {

      in.mark(1);
      if ((c = in.read()) == 10)
      {
        in.mark(1);
      }
      
      if (c > 0)
      {
        in.reset();
      }
    }
    
    return l.toString();
  }
  


  ASN1Sequence readPEMObject(InputStream in)
    throws IOException
  {
    StringBuffer pemBuf = new StringBuffer();
    String line;
    while ((line = readLine(in)) != null)
    {
      if ((!line.startsWith(_header1)) && (!line.startsWith(_header2))) { if (line.startsWith(_header3)) {
          break;
        }
      }
    }
    
    while ((line = readLine(in)) != null)
    {
      if ((line.startsWith(_footer1)) || (line.startsWith(_footer2)) || (line.startsWith(_footer3))) {
        break;
      }
      

      pemBuf.append(line);
    }
    
    if (pemBuf.length() != 0)
    {
      try
      {
        return ASN1Sequence.getInstance(Base64.decode(pemBuf.toString()));
      }
      catch (Exception e)
      {
        throw new IOException("malformed PEM data encountered");
      }
    }
    
    return null;
  }
}
