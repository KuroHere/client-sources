package org.newdawn.slick.opengl.pbuffer;

import java.util.HashMap;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.Pbuffer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;







public class GraphicsFactory
{
  private static HashMap graphics = new HashMap();
  
  private static boolean pbuffer = true;
  
  private static boolean pbufferRT = true;
  
  private static boolean fbo = true;
  
  private static boolean init = false;
  

  public GraphicsFactory() {}
  

  private static void init()
    throws SlickException
  {
    init = true;
    
    if (fbo) {
      fbo = getCapabilitiesGL_EXT_framebuffer_object;
    }
    pbuffer = (Pbuffer.getCapabilities() & 0x1) != 0;
    pbufferRT = (Pbuffer.getCapabilities() & 0x2) != 0;
    
    if ((!fbo) && (!pbuffer) && (!pbufferRT)) {
      throw new SlickException("Your OpenGL card does not support offscreen buffers and hence can't handle the dynamic images required for this application.");
    }
    
    Log.info("Offscreen Buffers FBO=" + fbo + " PBUFFER=" + pbuffer + " PBUFFERRT=" + pbufferRT);
  }
  




  public static void setUseFBO(boolean useFBO)
  {
    fbo = useFBO;
  }
  




  public static boolean usingFBO()
  {
    return fbo;
  }
  




  public static boolean usingPBuffer()
  {
    return (!fbo) && (pbuffer);
  }
  






  public static Graphics getGraphicsForImage(Image image)
    throws SlickException
  {
    Graphics g = (Graphics)graphics.get(image.getTexture());
    
    if (g == null) {
      g = createGraphics(image);
      graphics.put(image.getTexture(), g);
    }
    
    return g;
  }
  




  public static void releaseGraphicsForImage(Image image)
    throws SlickException
  {
    Graphics g = (Graphics)graphics.remove(image.getTexture());
    
    if (g != null) {
      g.destroy();
    }
  }
  



  private static Graphics createGraphics(Image image)
    throws SlickException
  {
    
    


    if (fbo) {
      try {
        return new FBOGraphics(image);
      } catch (Exception e) {
        fbo = false;
        Log.warn("FBO failed in use, falling back to PBuffer");
      }
    }
    
    if (pbuffer) {
      if (pbufferRT) {
        return new PBufferGraphics(image);
      }
      return new PBufferUniqueGraphics(image);
    }
    

    throw new SlickException("Failed to create offscreen buffer even though the card reports it's possible");
  }
}
