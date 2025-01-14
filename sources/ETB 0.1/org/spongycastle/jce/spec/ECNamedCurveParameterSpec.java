package org.spongycastle.jce.spec;

import java.math.BigInteger;
import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECPoint;












public class ECNamedCurveParameterSpec
  extends ECParameterSpec
{
  private String name;
  
  public ECNamedCurveParameterSpec(String name, ECCurve curve, ECPoint G, BigInteger n)
  {
    super(curve, G, n);
    
    this.name = name;
  }
  





  public ECNamedCurveParameterSpec(String name, ECCurve curve, ECPoint G, BigInteger n, BigInteger h)
  {
    super(curve, G, n, h);
    
    this.name = name;
  }
  






  public ECNamedCurveParameterSpec(String name, ECCurve curve, ECPoint G, BigInteger n, BigInteger h, byte[] seed)
  {
    super(curve, G, n, h, seed);
    
    this.name = name;
  }
  



  public String getName()
  {
    return name;
  }
}
