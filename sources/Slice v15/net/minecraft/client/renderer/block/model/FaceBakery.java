package net.minecraft.client.renderer.block.model;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import net.minecraft.client.renderer.EnumFaceing;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;

public class FaceBakery
{
  private static final double field_178418_a = 1.0D / Math.cos(0.39269908169872414D) - 1.0D;
  private static final double field_178417_b = 1.0D / Math.cos(0.7853981633974483D) - 1.0D;
  private static final String __OBFID = "CL_00002490";
  
  public FaceBakery() {}
  
  public BakedQuad func_178414_a(Vector3f posFrom, Vector3f posTo, BlockPartFace face, TextureAtlasSprite sprite, EnumFacing facing, ModelRotation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade) { int[] var10 = func_178405_a(face, sprite, facing, func_178403_a(posFrom, posTo), modelRotationIn, partRotation, uvLocked, shade);
    EnumFacing var11 = func_178410_a(var10);
    
    if (uvLocked)
    {
      func_178409_a(var10, var11, field_178243_e, sprite);
    }
    
    if (partRotation == null)
    {
      func_178408_a(var10, var11);
    }
    
    return new BakedQuad(var10, field_178245_c, var11, sprite);
  }
  
  private int[] func_178405_a(BlockPartFace p_178405_1_, TextureAtlasSprite p_178405_2_, EnumFacing p_178405_3_, float[] p_178405_4_, ModelRotation p_178405_5_, BlockPartRotation p_178405_6_, boolean p_178405_7_, boolean shade)
  {
    int[] var9 = new int[28];
    
    for (int var10 = 0; var10 < 4; var10++)
    {
      func_178402_a(var9, var10, p_178405_3_, p_178405_1_, p_178405_4_, p_178405_2_, p_178405_5_, p_178405_6_, p_178405_7_, shade);
    }
    
    return var9;
  }
  
  private int func_178413_a(EnumFacing p_178413_1_)
  {
    float var2 = func_178412_b(p_178413_1_);
    int var3 = MathHelper.clamp_int((int)(var2 * 255.0F), 0, 255);
    return 0xFF000000 | var3 << 16 | var3 << 8 | var3;
  }
  
  private float func_178412_b(EnumFacing p_178412_1_)
  {
    switch (SwitchEnumFacing.field_178400_a[p_178412_1_.ordinal()])
    {
    case 1: 
      return 0.5F;
    
    case 2: 
      return 1.0F;
    
    case 3: 
    case 4: 
      return 0.8F;
    
    case 5: 
    case 6: 
      return 0.6F;
    }
    
    return 1.0F;
  }
  

  private float[] func_178403_a(Vector3f pos1, Vector3f pos2)
  {
    float[] var3 = new float[EnumFacing.values().length];
    var3[net.minecraft.client.renderer.EnumFaceing.Constants.field_179176_f] = (x / 16.0F);
    var3[net.minecraft.client.renderer.EnumFaceing.Constants.field_179178_e] = (y / 16.0F);
    var3[net.minecraft.client.renderer.EnumFaceing.Constants.field_179177_d] = (z / 16.0F);
    var3[net.minecraft.client.renderer.EnumFaceing.Constants.field_179180_c] = (x / 16.0F);
    var3[net.minecraft.client.renderer.EnumFaceing.Constants.field_179179_b] = (y / 16.0F);
    var3[net.minecraft.client.renderer.EnumFaceing.Constants.field_179181_a] = (z / 16.0F);
    return var3;
  }
  
  private void func_178402_a(int[] faceData, int vertexIndex, EnumFacing facing, BlockPartFace partFace, float[] p_178402_5_, TextureAtlasSprite sprite, ModelRotation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade)
  {
    EnumFacing var11 = modelRotationIn.func_177523_a(facing);
    int var12 = shade ? func_178413_a(var11) : -1;
    net.minecraft.client.renderer.EnumFaceing.VertexInformation var13 = EnumFaceing.func_179027_a(facing).func_179025_a(vertexIndex);
    Vector3d var14 = new Vector3d(p_178402_5_[field_179184_a], p_178402_5_[field_179182_b], p_178402_5_[field_179183_c]);
    func_178407_a(var14, partRotation);
    int var15 = func_178415_a(var14, facing, vertexIndex, modelRotationIn, uvLocked);
    func_178404_a(faceData, var15, vertexIndex, var14, var12, sprite, field_178243_e);
  }
  
  private void func_178404_a(int[] faceData, int storeIndex, int vertexIndex, Vector3d position, int shadeColor, TextureAtlasSprite sprite, BlockFaceUV faceUV)
  {
    int var8 = storeIndex * 7;
    faceData[var8] = Float.floatToRawIntBits((float)x);
    faceData[(var8 + 1)] = Float.floatToRawIntBits((float)y);
    faceData[(var8 + 2)] = Float.floatToRawIntBits((float)z);
    faceData[(var8 + 3)] = shadeColor;
    faceData[(var8 + 4)] = Float.floatToRawIntBits(sprite.getInterpolatedU(faceUV.func_178348_a(vertexIndex)));
    faceData[(var8 + 4 + 1)] = Float.floatToRawIntBits(sprite.getInterpolatedV(faceUV.func_178346_b(vertexIndex)));
  }
  
  private void func_178407_a(Vector3d p_178407_1_, BlockPartRotation p_178407_2_)
  {
    if (p_178407_2_ != null)
    {
      Matrix4d var3 = func_178411_a();
      Vector3d var4 = new Vector3d(0.0D, 0.0D, 0.0D);
      
      switch (SwitchEnumFacing.field_178399_b[field_178342_b.ordinal()])
      {
      case 1: 
        var3.mul(func_178416_a(new AxisAngle4d(1.0D, 0.0D, 0.0D, field_178343_c * 0.017453292519943295D)));
        var4.set(0.0D, 1.0D, 1.0D);
        break;
      
      case 2: 
        var3.mul(func_178416_a(new AxisAngle4d(0.0D, 1.0D, 0.0D, field_178343_c * 0.017453292519943295D)));
        var4.set(1.0D, 0.0D, 1.0D);
        break;
      
      case 3: 
        var3.mul(func_178416_a(new AxisAngle4d(0.0D, 0.0D, 1.0D, field_178343_c * 0.017453292519943295D)));
        var4.set(1.0D, 1.0D, 0.0D);
      }
      
      if (field_178341_d)
      {
        if (Math.abs(field_178343_c) == 22.5F)
        {
          var4.scale(field_178418_a);
        }
        else
        {
          var4.scale(field_178417_b);
        }
        
        var4.add(new Vector3d(1.0D, 1.0D, 1.0D));
      }
      else
      {
        var4.set(new Vector3d(1.0D, 1.0D, 1.0D));
      }
      
      func_178406_a(p_178407_1_, new Vector3d(field_178344_a), var3, var4);
    }
  }
  
  public int func_178415_a(Vector3d position, EnumFacing facing, int vertexIndex, ModelRotation modelRotationIn, boolean uvLocked)
  {
    if (modelRotationIn == ModelRotation.X0_Y0)
    {
      return vertexIndex;
    }
    

    func_178406_a(position, new Vector3d(0.5D, 0.5D, 0.5D), modelRotationIn.func_177525_a(), new Vector3d(1.0D, 1.0D, 1.0D));
    return modelRotationIn.func_177520_a(facing, vertexIndex);
  }
  

  private void func_178406_a(Vector3d position, Vector3d rotationOrigin, Matrix4d rotationMatrix, Vector3d scale)
  {
    position.sub(rotationOrigin);
    rotationMatrix.transform(position);
    x *= x;
    y *= y;
    z *= z;
    position.add(rotationOrigin);
  }
  
  private Matrix4d func_178416_a(AxisAngle4d p_178416_1_)
  {
    Matrix4d var2 = func_178411_a();
    var2.setRotation(p_178416_1_);
    return var2;
  }
  
  private Matrix4d func_178411_a()
  {
    Matrix4d var1 = new Matrix4d();
    var1.setIdentity();
    return var1;
  }
  
  public static EnumFacing func_178410_a(int[] p_178410_0_)
  {
    Vector3f var1 = new Vector3f(Float.intBitsToFloat(p_178410_0_[0]), Float.intBitsToFloat(p_178410_0_[1]), Float.intBitsToFloat(p_178410_0_[2]));
    Vector3f var2 = new Vector3f(Float.intBitsToFloat(p_178410_0_[7]), Float.intBitsToFloat(p_178410_0_[8]), Float.intBitsToFloat(p_178410_0_[9]));
    Vector3f var3 = new Vector3f(Float.intBitsToFloat(p_178410_0_[14]), Float.intBitsToFloat(p_178410_0_[15]), Float.intBitsToFloat(p_178410_0_[16]));
    Vector3f var4 = new Vector3f();
    Vector3f var5 = new Vector3f();
    Vector3f var6 = new Vector3f();
    var4.sub(var1, var2);
    var5.sub(var3, var2);
    var6.cross(var5, var4);
    var6.normalize();
    EnumFacing var7 = null;
    float var8 = 0.719F;
    EnumFacing[] var9 = EnumFacing.values();
    int var10 = var9.length;
    
    for (int var11 = 0; var11 < var10; var11++)
    {
      EnumFacing var12 = var9[var11];
      Vec3i var13 = var12.getDirectionVec();
      Vector3f var14 = new Vector3f(var13.getX(), var13.getY(), var13.getZ());
      float var15 = var6.dot(var14);
      
      if ((var15 >= 0.0F) && (var15 > var8))
      {
        var8 = var15;
        var7 = var12;
      }
    }
    
    if (var7 == null)
    {
      return EnumFacing.UP;
    }
    

    return var7;
  }
  

  public void func_178409_a(int[] p_178409_1_, EnumFacing p_178409_2_, BlockFaceUV p_178409_3_, TextureAtlasSprite p_178409_4_)
  {
    for (int var5 = 0; var5 < 4; var5++)
    {
      func_178401_a(var5, p_178409_1_, p_178409_2_, p_178409_3_, p_178409_4_);
    }
  }
  
  private void func_178408_a(int[] p_178408_1_, EnumFacing p_178408_2_)
  {
    int[] var3 = new int[p_178408_1_.length];
    System.arraycopy(p_178408_1_, 0, var3, 0, p_178408_1_.length);
    float[] var4 = new float[EnumFacing.values().length];
    var4[net.minecraft.client.renderer.EnumFaceing.Constants.field_179176_f] = 999.0F;
    var4[net.minecraft.client.renderer.EnumFaceing.Constants.field_179178_e] = 999.0F;
    var4[net.minecraft.client.renderer.EnumFaceing.Constants.field_179177_d] = 999.0F;
    var4[net.minecraft.client.renderer.EnumFaceing.Constants.field_179180_c] = -999.0F;
    var4[net.minecraft.client.renderer.EnumFaceing.Constants.field_179179_b] = -999.0F;
    var4[net.minecraft.client.renderer.EnumFaceing.Constants.field_179181_a] = -999.0F;
    


    for (int var17 = 0; var17 < 4; var17++)
    {
      int var6 = 7 * var17;
      float var18 = Float.intBitsToFloat(var3[var6]);
      float var19 = Float.intBitsToFloat(var3[(var6 + 1)]);
      float var9 = Float.intBitsToFloat(var3[(var6 + 2)]);
      
      if (var18 < var4[net.minecraft.client.renderer.EnumFaceing.Constants.field_179176_f])
      {
        var4[net.minecraft.client.renderer.EnumFaceing.Constants.field_179176_f] = var18;
      }
      
      if (var19 < var4[net.minecraft.client.renderer.EnumFaceing.Constants.field_179178_e])
      {
        var4[net.minecraft.client.renderer.EnumFaceing.Constants.field_179178_e] = var19;
      }
      
      if (var9 < var4[net.minecraft.client.renderer.EnumFaceing.Constants.field_179177_d])
      {
        var4[net.minecraft.client.renderer.EnumFaceing.Constants.field_179177_d] = var9;
      }
      
      if (var18 > var4[net.minecraft.client.renderer.EnumFaceing.Constants.field_179180_c])
      {
        var4[net.minecraft.client.renderer.EnumFaceing.Constants.field_179180_c] = var18;
      }
      
      if (var19 > var4[net.minecraft.client.renderer.EnumFaceing.Constants.field_179179_b])
      {
        var4[net.minecraft.client.renderer.EnumFaceing.Constants.field_179179_b] = var19;
      }
      
      if (var9 > var4[net.minecraft.client.renderer.EnumFaceing.Constants.field_179181_a])
      {
        var4[net.minecraft.client.renderer.EnumFaceing.Constants.field_179181_a] = var9;
      }
    }
    
    EnumFaceing var171 = EnumFaceing.func_179027_a(p_178408_2_);
    
    for (int var6 = 0; var6 < 4; var6++)
    {
      int var181 = 7 * var6;
      net.minecraft.client.renderer.EnumFaceing.VertexInformation var191 = var171.func_179025_a(var6);
      float var9 = var4[field_179184_a];
      float var10 = var4[field_179182_b];
      float var11 = var4[field_179183_c];
      p_178408_1_[var181] = Float.floatToRawIntBits(var9);
      p_178408_1_[(var181 + 1)] = Float.floatToRawIntBits(var10);
      p_178408_1_[(var181 + 2)] = Float.floatToRawIntBits(var11);
      
      for (int var12 = 0; var12 < 4; var12++)
      {
        int var13 = 7 * var12;
        float var14 = Float.intBitsToFloat(var3[var13]);
        float var15 = Float.intBitsToFloat(var3[(var13 + 1)]);
        float var16 = Float.intBitsToFloat(var3[(var13 + 2)]);
        
        if ((MathHelper.func_180185_a(var9, var14)) && (MathHelper.func_180185_a(var10, var15)) && (MathHelper.func_180185_a(var11, var16)))
        {
          p_178408_1_[(var181 + 4)] = var3[(var13 + 4)];
          p_178408_1_[(var181 + 4 + 1)] = var3[(var13 + 4 + 1)];
        }
      }
    }
  }
  
  private void func_178401_a(int p_178401_1_, int[] p_178401_2_, EnumFacing p_178401_3_, BlockFaceUV p_178401_4_, TextureAtlasSprite p_178401_5_)
  {
    int var6 = 7 * p_178401_1_;
    float var7 = Float.intBitsToFloat(p_178401_2_[var6]);
    float var8 = Float.intBitsToFloat(p_178401_2_[(var6 + 1)]);
    float var9 = Float.intBitsToFloat(p_178401_2_[(var6 + 2)]);
    
    if ((var7 < -0.1F) || (var7 >= 1.1F))
    {
      var7 -= MathHelper.floor_float(var7);
    }
    
    if ((var8 < -0.1F) || (var8 >= 1.1F))
    {
      var8 -= MathHelper.floor_float(var8);
    }
    
    if ((var9 < -0.1F) || (var9 >= 1.1F))
    {
      var9 -= MathHelper.floor_float(var9);
    }
    
    float var10 = 0.0F;
    float var11 = 0.0F;
    
    switch (SwitchEnumFacing.field_178400_a[p_178401_3_.ordinal()])
    {
    case 1: 
      var10 = var7 * 16.0F;
      var11 = (1.0F - var9) * 16.0F;
      break;
    
    case 2: 
      var10 = var7 * 16.0F;
      var11 = var9 * 16.0F;
      break;
    
    case 3: 
      var10 = (1.0F - var7) * 16.0F;
      var11 = (1.0F - var8) * 16.0F;
      break;
    
    case 4: 
      var10 = var7 * 16.0F;
      var11 = (1.0F - var8) * 16.0F;
      break;
    
    case 5: 
      var10 = var9 * 16.0F;
      var11 = (1.0F - var8) * 16.0F;
      break;
    
    case 6: 
      var10 = (1.0F - var9) * 16.0F;
      var11 = (1.0F - var8) * 16.0F;
    }
    
    int var12 = p_178401_4_.func_178345_c(p_178401_1_) * 7;
    p_178401_2_[(var12 + 4)] = Float.floatToRawIntBits(p_178401_5_.getInterpolatedU(var10));
    p_178401_2_[(var12 + 4 + 1)] = Float.floatToRawIntBits(p_178401_5_.getInterpolatedV(var11));
  }
  
  static final class SwitchEnumFacing
  {
    static final int[] field_178400_a;
    static final int[] field_178399_b = new int[net.minecraft.util.EnumFacing.Axis.values().length];
    private static final String __OBFID = "CL_00002489";
    
    static
    {
      try
      {
        field_178399_b[net.minecraft.util.EnumFacing.Axis.X.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      



      try
      {
        field_178399_b[net.minecraft.util.EnumFacing.Axis.Y.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      



      try
      {
        field_178399_b[net.minecraft.util.EnumFacing.Axis.Z.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      



      field_178400_a = new int[EnumFacing.values().length];
      
      try
      {
        field_178400_a[EnumFacing.DOWN.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
      



      try
      {
        field_178400_a[EnumFacing.UP.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError5) {}
      



      try
      {
        field_178400_a[EnumFacing.NORTH.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError6) {}
      



      try
      {
        field_178400_a[EnumFacing.SOUTH.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError7) {}
      



      try
      {
        field_178400_a[EnumFacing.WEST.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError8) {}
      



      try
      {
        field_178400_a[EnumFacing.EAST.ordinal()] = 6;
      }
      catch (NoSuchFieldError localNoSuchFieldError9) {}
    }
    
    SwitchEnumFacing() {}
  }
}
