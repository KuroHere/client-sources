package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.raw.Interleave;
import org.spongycastle.math.raw.Nat256;

public class SecT239Field
{
  private static final long M47 = 140737488355327L;
  private static final long M60 = 1152921504606846975L;
  
  public SecT239Field() {}
  
  public static void add(long[] x, long[] y, long[] z)
  {
    x[0] ^= y[0];
    x[1] ^= y[1];
    x[2] ^= y[2];
    x[3] ^= y[3];
  }
  
  public static void addExt(long[] xx, long[] yy, long[] zz)
  {
    xx[0] ^= yy[0];
    xx[1] ^= yy[1];
    xx[2] ^= yy[2];
    xx[3] ^= yy[3];
    xx[4] ^= yy[4];
    xx[5] ^= yy[5];
    xx[6] ^= yy[6];
    xx[7] ^= yy[7];
  }
  
  public static void addOne(long[] x, long[] z)
  {
    x[0] ^= 1L;
    z[1] = x[1];
    z[2] = x[2];
    z[3] = x[3];
  }
  
  public static long[] fromBigInteger(java.math.BigInteger x)
  {
    long[] z = Nat256.fromBigInteger64(x);
    reduce17(z, 0);
    return z;
  }
  
  public static void invert(long[] x, long[] z)
  {
    if (Nat256.isZero64(x))
    {
      throw new IllegalStateException();
    }
    


    long[] t0 = Nat256.create64();
    long[] t1 = Nat256.create64();
    
    square(x, t0);
    multiply(t0, x, t0);
    square(t0, t0);
    multiply(t0, x, t0);
    squareN(t0, 3, t1);
    multiply(t1, t0, t1);
    square(t1, t1);
    multiply(t1, x, t1);
    squareN(t1, 7, t0);
    multiply(t0, t1, t0);
    squareN(t0, 14, t1);
    multiply(t1, t0, t1);
    square(t1, t1);
    multiply(t1, x, t1);
    squareN(t1, 29, t0);
    multiply(t0, t1, t0);
    square(t0, t0);
    multiply(t0, x, t0);
    squareN(t0, 59, t1);
    multiply(t1, t0, t1);
    square(t1, t1);
    multiply(t1, x, t1);
    squareN(t1, 119, t0);
    multiply(t0, t1, t0);
    square(t0, z);
  }
  
  public static void multiply(long[] x, long[] y, long[] z)
  {
    long[] tt = Nat256.createExt64();
    implMultiply(x, y, tt);
    reduce(tt, z);
  }
  
  public static void multiplyAddToExt(long[] x, long[] y, long[] zz)
  {
    long[] tt = Nat256.createExt64();
    implMultiply(x, y, tt);
    addExt(zz, tt, zz);
  }
  
  public static void reduce(long[] xx, long[] z)
  {
    long x0 = xx[0];long x1 = xx[1];long x2 = xx[2];long x3 = xx[3];
    long x4 = xx[4];long x5 = xx[5];long x6 = xx[6];long x7 = xx[7];
    
    x3 ^= x7 << 17;
    x4 ^= x7 >>> 47;
    x5 ^= x7 << 47;
    x6 ^= x7 >>> 17;
    
    x2 ^= x6 << 17;
    x3 ^= x6 >>> 47;
    x4 ^= x6 << 47;
    x5 ^= x6 >>> 17;
    
    x1 ^= x5 << 17;
    x2 ^= x5 >>> 47;
    x3 ^= x5 << 47;
    x4 ^= x5 >>> 17;
    
    x0 ^= x4 << 17;
    x1 ^= x4 >>> 47;
    x2 ^= x4 << 47;
    x3 ^= x4 >>> 17;
    
    long t = x3 >>> 47;
    z[0] = (x0 ^ t);
    z[1] = x1;
    z[2] = (x2 ^ t << 30);
    z[3] = (x3 & 0x7FFFFFFFFFFF);
  }
  
  public static void reduce17(long[] z, int zOff)
  {
    long z3 = z[(zOff + 3)];long t = z3 >>> 47;
    z[zOff] ^= t;
    z[(zOff + 2)] ^= t << 30;
    z[(zOff + 3)] = (z3 & 0x7FFFFFFFFFFF);
  }
  

  public static void sqrt(long[] x, long[] z)
  {
    long u0 = Interleave.unshuffle(x[0]);long u1 = Interleave.unshuffle(x[1]);
    long e0 = u0 & 0xFFFFFFFF | u1 << 32;
    long c0 = u0 >>> 32 | u1 & 0xFFFFFFFF00000000;
    
    u0 = Interleave.unshuffle(x[2]);u1 = Interleave.unshuffle(x[3]);
    long e1 = u0 & 0xFFFFFFFF | u1 << 32;
    long c1 = u0 >>> 32 | u1 & 0xFFFFFFFF00000000;
    

    long c3 = c1 >>> 49;
    long c2 = c0 >>> 49 | c1 << 15;
    c1 ^= c0 << 15;
    
    long[] tt = Nat256.createExt64();
    
    int[] shifts = { 39, 120 };
    for (int i = 0; i < shifts.length; i++)
    {
      int w = shifts[i] >>> 6;int s = shifts[i] & 0x3F;
      
      tt[w] ^= c0 << s;
      tt[(w + 1)] ^= (c1 << s | c0 >>> -s);
      tt[(w + 2)] ^= (c2 << s | c1 >>> -s);
      tt[(w + 3)] ^= (c3 << s | c2 >>> -s);
      tt[(w + 4)] ^= c3 >>> -s;
    }
    
    reduce(tt, z);
    
    z[0] ^= e0;
    z[1] ^= e1;
  }
  
  public static void square(long[] x, long[] z)
  {
    long[] tt = Nat256.createExt64();
    implSquare(x, tt);
    reduce(tt, z);
  }
  
  public static void squareAddToExt(long[] x, long[] zz)
  {
    long[] tt = Nat256.createExt64();
    implSquare(x, tt);
    addExt(zz, tt, zz);
  }
  


  public static void squareN(long[] x, int n, long[] z)
  {
    long[] tt = Nat256.createExt64();
    implSquare(x, tt);
    reduce(tt, z);
    for (;;) {
      n--; if (n <= 0)
        break;
      implSquare(z, tt);
      reduce(tt, z);
    }
  }
  

  public static int trace(long[] x)
  {
    return (int)(x[0] ^ x[1] >>> 17 ^ x[2] >>> 34) & 0x1;
  }
  
  protected static void implCompactExt(long[] zz)
  {
    long z0 = zz[0];long z1 = zz[1];long z2 = zz[2];long z3 = zz[3];long z4 = zz[4];long z5 = zz[5];long z6 = zz[6];long z7 = zz[7];
    zz[0] = (z0 ^ z1 << 60);
    zz[1] = (z1 >>> 4 ^ z2 << 56);
    zz[2] = (z2 >>> 8 ^ z3 << 52);
    zz[3] = (z3 >>> 12 ^ z4 << 48);
    zz[4] = (z4 >>> 16 ^ z5 << 44);
    zz[5] = (z5 >>> 20 ^ z6 << 40);
    zz[6] = (z6 >>> 24 ^ z7 << 36);
    zz[7] = (z7 >>> 28);
  }
  
  protected static void implExpand(long[] x, long[] z)
  {
    long x0 = x[0];long x1 = x[1];long x2 = x[2];long x3 = x[3];
    z[0] = (x0 & 0xFFFFFFFFFFFFFFF);
    z[1] = ((x0 >>> 60 ^ x1 << 4) & 0xFFFFFFFFFFFFFFF);
    z[2] = ((x1 >>> 56 ^ x2 << 8) & 0xFFFFFFFFFFFFFFF);
    z[3] = (x2 >>> 52 ^ x3 << 12);
  }
  




  protected static void implMultiply(long[] x, long[] y, long[] zz)
  {
    long[] f = new long[4];long[] g = new long[4];
    implExpand(x, f);
    implExpand(y, g);
    
    implMulwAcc(f[0], g[0], zz, 0);
    implMulwAcc(f[1], g[1], zz, 1);
    implMulwAcc(f[2], g[2], zz, 2);
    implMulwAcc(f[3], g[3], zz, 3);
    

    for (int i = 5; i > 0; i--)
    {
      zz[i] ^= zz[(i - 1)];
    }
    
    implMulwAcc(f[0] ^ f[1], g[0] ^ g[1], zz, 1);
    implMulwAcc(f[2] ^ f[3], g[2] ^ g[3], zz, 3);
    

    for (int i = 7; i > 1; i--)
    {
      zz[i] ^= zz[(i - 2)];
    }
    


    long c0 = f[0] ^ f[2];long c1 = f[1] ^ f[3];
    long d0 = g[0] ^ g[2];long d1 = g[1] ^ g[3];
    implMulwAcc(c0 ^ c1, d0 ^ d1, zz, 3);
    long[] t = new long[3];
    implMulwAcc(c0, d0, t, 0);
    implMulwAcc(c1, d1, t, 1);
    long t0 = t[0];long t1 = t[1];long t2 = t[2];
    zz[2] ^= t0;
    zz[3] ^= t0 ^ t1;
    zz[4] ^= t2 ^ t1;
    zz[5] ^= t2;
    

    implCompactExt(zz);
  }
  



  protected static void implMulwAcc(long x, long y, long[] z, int zOff)
  {
    long[] u = new long[8];
    
    u[1] = y;
    u[2] = (u[1] << 1);
    u[3] = (u[2] ^ y);
    u[4] = (u[2] << 1);
    u[5] = (u[4] ^ y);
    u[6] = (u[3] << 1);
    u[7] = (u[6] ^ y);
    
    int j = (int)x;
    long h = 0L;long l = u[(j & 0x7)] ^ u[(j >>> 3 & 0x7)] << 3;
    
    int k = 54;
    do
    {
      j = (int)(x >>> k);
      long g = u[(j & 0x7)] ^ u[(j >>> 3 & 0x7)] << 3;
      
      l ^= g << k;
      h ^= g >>> -k;
      
      k -= 6; } while (k > 0);
    
    h ^= (x & 0x820820820820820 & y << 4 >> 63) >>> 5;
    


    z[zOff] ^= l & 0xFFFFFFFFFFFFFFF;
    z[(zOff + 1)] ^= l >>> 60 ^ h << 4;
  }
  
  protected static void implSquare(long[] x, long[] zz)
  {
    Interleave.expand64To128(x[0], zz, 0);
    Interleave.expand64To128(x[1], zz, 2);
    Interleave.expand64To128(x[2], zz, 4);
    
    long x3 = x[3];
    zz[6] = Interleave.expand32to64((int)x3);
    zz[7] = (Interleave.expand16to32((int)(x3 >>> 32)) & 0xFFFFFFFF);
  }
}
