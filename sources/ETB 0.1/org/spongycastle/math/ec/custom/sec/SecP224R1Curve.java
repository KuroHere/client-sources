package org.spongycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECCurve.AbstractFp;
import org.spongycastle.math.ec.ECFieldElement;
import org.spongycastle.math.ec.ECPoint;
import org.spongycastle.util.encoders.Hex;

public class SecP224R1Curve extends ECCurve.AbstractFp
{
  public static final BigInteger q = new BigInteger(1, 
    Hex.decode("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF000000000000000000000001"));
  
  private static final int SecP224R1_DEFAULT_COORDS = 2;
  
  protected SecP224R1Point infinity;
  
  public SecP224R1Curve()
  {
    super(q);
    
    infinity = new SecP224R1Point(this, null, null);
    
    a = fromBigInteger(new BigInteger(1, 
      Hex.decode("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFE")));
    b = fromBigInteger(new BigInteger(1, 
      Hex.decode("B4050A850C04B3ABF54132565044B0B7D7BFD8BA270B39432355FFB4")));
    order = new BigInteger(1, Hex.decode("FFFFFFFFFFFFFFFFFFFFFFFFFFFF16A2E0B8F03E13DD29455C5C2A3D"));
    cofactor = BigInteger.valueOf(1L);
    
    coord = 2;
  }
  
  protected ECCurve cloneCurve()
  {
    return new SecP224R1Curve();
  }
  
  public boolean supportsCoordinateSystem(int coord)
  {
    switch (coord)
    {
    case 2: 
      return true;
    }
    return false;
  }
  

  public BigInteger getQ()
  {
    return q;
  }
  
  public int getFieldSize()
  {
    return q.bitLength();
  }
  
  public ECFieldElement fromBigInteger(BigInteger x)
  {
    return new SecP224R1FieldElement(x);
  }
  
  protected ECPoint createRawPoint(ECFieldElement x, ECFieldElement y, boolean withCompression)
  {
    return new SecP224R1Point(this, x, y, withCompression);
  }
  
  protected ECPoint createRawPoint(ECFieldElement x, ECFieldElement y, ECFieldElement[] zs, boolean withCompression)
  {
    return new SecP224R1Point(this, x, y, zs, withCompression);
  }
  
  public ECPoint getInfinity()
  {
    return infinity;
  }
}
