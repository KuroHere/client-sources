package org.spongycastle.x509;

import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.PKIXParameters;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.spongycastle.jce.provider.AnnotatedException;
import org.spongycastle.util.StoreException;

class PKIXCRLUtil
{
  PKIXCRLUtil() {}
  
  public Set findCRLs(X509CRLStoreSelector crlselect, ExtendedPKIXParameters paramsPKIX, Date currentDate) throws AnnotatedException
  {
    Set initialSet = new HashSet();
    

    try
    {
      initialSet.addAll(findCRLs(crlselect, paramsPKIX.getAdditionalStores()));
      initialSet.addAll(findCRLs(crlselect, paramsPKIX.getStores()));
      initialSet.addAll(findCRLs(crlselect, paramsPKIX.getCertStores()));
    }
    catch (AnnotatedException e)
    {
      throw new AnnotatedException("Exception obtaining complete CRLs.", e);
    }
    
    Set finalSet = new HashSet();
    Date validityDate = currentDate;
    
    if (paramsPKIX.getDate() != null)
    {
      validityDate = paramsPKIX.getDate();
    }
    

    for (Iterator it = initialSet.iterator(); it.hasNext();)
    {
      X509CRL crl = (X509CRL)it.next();
      
      if (crl.getNextUpdate().after(validityDate))
      {
        X509Certificate cert = crlselect.getCertificateChecking();
        
        if (cert != null)
        {
          if (crl.getThisUpdate().before(cert.getNotAfter()))
          {
            finalSet.add(crl);
          }
          
        }
        else {
          finalSet.add(crl);
        }
      }
    }
    
    return finalSet;
  }
  
  public Set findCRLs(X509CRLStoreSelector crlselect, PKIXParameters paramsPKIX)
    throws AnnotatedException
  {
    Set completeSet = new HashSet();
    

    try
    {
      completeSet.addAll(findCRLs(crlselect, paramsPKIX.getCertStores()));
    }
    catch (AnnotatedException e)
    {
      throw new AnnotatedException("Exception obtaining complete CRLs.", e);
    }
    
    return completeSet;
  }
  













  private final Collection findCRLs(X509CRLStoreSelector crlSelect, List crlStores)
    throws AnnotatedException
  {
    Set crls = new HashSet();
    Iterator iter = crlStores.iterator();
    
    AnnotatedException lastException = null;
    boolean foundValidStore = false;
    
    while (iter.hasNext())
    {
      Object obj = iter.next();
      
      if ((obj instanceof X509Store))
      {
        X509Store store = (X509Store)obj;
        
        try
        {
          crls.addAll(store.getMatches(crlSelect));
          foundValidStore = true;
        }
        catch (StoreException e)
        {
          lastException = new AnnotatedException("Exception searching in X.509 CRL store.", e);
        }
        
      }
      else
      {
        CertStore store = (CertStore)obj;
        
        try
        {
          crls.addAll(store.getCRLs(crlSelect));
          foundValidStore = true;
        }
        catch (CertStoreException e)
        {
          lastException = new AnnotatedException("Exception searching in X.509 CRL store.", e);
        }
      }
    }
    
    if ((!foundValidStore) && (lastException != null))
    {
      throw lastException;
    }
    return crls;
  }
}
