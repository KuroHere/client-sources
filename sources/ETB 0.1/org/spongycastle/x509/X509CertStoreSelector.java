package org.spongycastle.x509;

import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import org.spongycastle.util.Selector;





/**
 * @deprecated
 */
public class X509CertStoreSelector
  extends X509CertSelector
  implements Selector
{
  public X509CertStoreSelector() {}
  
  public boolean match(Object obj)
  {
    if (!(obj instanceof X509Certificate))
    {
      return false;
    }
    
    X509Certificate other = (X509Certificate)obj;
    
    return super.match(other);
  }
  
  public boolean match(Certificate cert)
  {
    return match(cert);
  }
  
  public Object clone()
  {
    X509CertStoreSelector selector = (X509CertStoreSelector)super.clone();
    
    return selector;
  }
  







  public static X509CertStoreSelector getInstance(X509CertSelector selector)
  {
    if (selector == null)
    {
      throw new IllegalArgumentException("cannot create from null selector");
    }
    X509CertStoreSelector cs = new X509CertStoreSelector();
    cs.setAuthorityKeyIdentifier(selector.getAuthorityKeyIdentifier());
    cs.setBasicConstraints(selector.getBasicConstraints());
    cs.setCertificate(selector.getCertificate());
    cs.setCertificateValid(selector.getCertificateValid());
    cs.setMatchAllSubjectAltNames(selector.getMatchAllSubjectAltNames());
    try
    {
      cs.setPathToNames(selector.getPathToNames());
      cs.setExtendedKeyUsage(selector.getExtendedKeyUsage());
      cs.setNameConstraints(selector.getNameConstraints());
      cs.setPolicy(selector.getPolicy());
      cs.setSubjectPublicKeyAlgID(selector.getSubjectPublicKeyAlgID());
      cs.setSubjectAlternativeNames(selector.getSubjectAlternativeNames());
    }
    catch (IOException e)
    {
      throw new IllegalArgumentException("error in passed in selector: " + e);
    }
    cs.setIssuer(selector.getIssuer());
    cs.setKeyUsage(selector.getKeyUsage());
    cs.setPrivateKeyValid(selector.getPrivateKeyValid());
    cs.setSerialNumber(selector.getSerialNumber());
    cs.setSubject(selector.getSubject());
    cs.setSubjectKeyIdentifier(selector.getSubjectKeyIdentifier());
    cs.setSubjectPublicKey(selector.getSubjectPublicKey());
    return cs;
  }
}
