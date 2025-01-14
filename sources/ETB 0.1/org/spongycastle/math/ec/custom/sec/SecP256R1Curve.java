package org.spongycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECCurve.AbstractFp;
import org.spongycastle.math.ec.ECFieldElement;
import org.spongycastle.math.ec.ECPoint;
import org.spongycastle.util.encoders.Hex;

public class SecP256R1Curve extends ECCurve.AbstractFp
{
  public static final BigInteger q = new BigInteger(1, 
    Hex.decode("FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF"));
  
  private static final int SecP256R1_DEFAULT_COORDS = 2;
  
  protected SecP256R1Point infinity;
  
  public SecP256R1Curve()
  {
    super(q);
    
    infinity = new SecP256R1Point(this, null, null);
    
    a = fromBigInteger(new BigInteger(1, 
      Hex.decode("FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC")));
    b = fromBigInteger(new BigInteger(1, 
      Hex.decode("5AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B")));
    order = new BigInteger(1, Hex.decode("FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551"));
    cofactor = BigInteger.valueOf(1L);
    
    coord = 2;
  }
  
  protected ECCurve cloneCurve()
  {
    return new SecP256R1Curve();
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
    return new SecP256R1FieldElement(x);
  }
  
  protected ECPoint createRawPoint(ECFieldElement x, ECFieldElement y, boolean withCompression)
  {
    return new SecP256R1Point(this, x, y, withCompression);
  }
  
  protected ECPoint createRawPoint(ECFieldElement x, ECFieldElement y, ECFieldElement[] zs, boolean withCompression)
  {
    return new SecP256R1Point(this, x, y, zs, withCompression);
  }
  
  public ECPoint getInfinity()
  {
    return infinity;
  }
}
