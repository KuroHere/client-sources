package net.optifine.shaders;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.AnimationMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.client.resources.data.FontMetadataSectionSerializer;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.LanguageMetadataSectionSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.resources.data.PackMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSectionSerializer;
import org.apache.commons.io.IOUtils;

public class SimpleShaderTexture extends AbstractTexture {
   private String texturePath;
   private static final IMetadataSerializer METADATA_SERIALIZER = makeMetadataSerializer();

   public SimpleShaderTexture(String texturePath) {
      this.texturePath = texturePath;
   }

   public void loadTexture(IResourceManager resourceManager) throws IOException {
      this.deleteGlTexture();
      InputStream inputstream = Shaders.getShaderPackResourceStream(this.texturePath);
      if (inputstream == null) {
         throw new FileNotFoundException("Shader texture not found: " + this.texturePath);
      } else {
         try {
            BufferedImage bufferedimage = TextureUtil.readBufferedImage(inputstream);
            TextureMetadataSection texturemetadatasection = this.loadTextureMetadataSection();
            TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedimage, texturemetadatasection.getTextureBlur(), texturemetadatasection.getTextureClamp());
         } finally {
            IOUtils.closeQuietly(inputstream);
         }

      }
   }

   private TextureMetadataSection loadTextureMetadataSection() {
      String s = this.texturePath + ".mcmeta";
      String s1 = "texture";
      InputStream inputstream = Shaders.getShaderPackResourceStream(s);
      if (inputstream != null) {
         IMetadataSerializer imetadataserializer = METADATA_SERIALIZER;
         BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));

         TextureMetadataSection texturemetadatasection1;
         try {
            TextureMetadataSection texturemetadatasection;
            try {
               JsonObject jsonobject = (new JsonParser()).parse(bufferedreader).getAsJsonObject();
               texturemetadatasection = (TextureMetadataSection)imetadataserializer.parseMetadataSection(s1, jsonobject);
               if (texturemetadatasection == null) {
                  TextureMetadataSection var9 = new TextureMetadataSection(false, false, new ArrayList());
                  return var9;
               }

               texturemetadatasection1 = texturemetadatasection;
            } catch (RuntimeException var13) {
               SMCLog.warning("Error reading metadata: " + s);
               SMCLog.warning("" + var13.getClass().getName() + ": " + var13.getMessage());
               texturemetadatasection = new TextureMetadataSection(false, false, new ArrayList());
               return texturemetadatasection;
            }
         } finally {
            IOUtils.closeQuietly(bufferedreader);
            IOUtils.closeQuietly(inputstream);
         }

         return texturemetadatasection1;
      } else {
         return new TextureMetadataSection(false, false, new ArrayList());
      }
   }

   private static IMetadataSerializer makeMetadataSerializer() {
      IMetadataSerializer imetadataserializer = new IMetadataSerializer();
      imetadataserializer.registerMetadataSectionType(new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
      imetadataserializer.registerMetadataSectionType(new FontMetadataSectionSerializer(), FontMetadataSection.class);
      imetadataserializer.registerMetadataSectionType(new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
      imetadataserializer.registerMetadataSectionType(new PackMetadataSectionSerializer(), PackMetadataSection.class);
      imetadataserializer.registerMetadataSectionType(new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
      return imetadataserializer;
   }
}
