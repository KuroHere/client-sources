package org.reflections.vfs;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

public class JarInputFile implements Vfs.File {
   private final ZipEntry entry;
   private final JarInputDir jarInputDir;
   private final long fromIndex;
   private final long endIndex;

   public JarInputFile(ZipEntry var1, JarInputDir var2, long var3, long var5) {
      this.entry = var1;
      this.jarInputDir = var2;
      this.fromIndex = var3;
      this.endIndex = var5;
   }

   public String getName() {
      String var1 = this.entry.getName();
      return var1.substring(var1.lastIndexOf("/") + 1);
   }

   public String getRelativePath() {
      return this.entry.getName();
   }

   public InputStream openInputStream() throws IOException {
      return new InputStream(this) {
         final JarInputFile this$0;

         {
            this.this$0 = var1;
         }

         public int read() throws IOException {
            if (this.this$0.jarInputDir.cursor >= this.this$0.fromIndex && this.this$0.jarInputDir.cursor <= this.this$0.endIndex) {
               int var1 = this.this$0.jarInputDir.jarInputStream.read();
               ++this.this$0.jarInputDir.cursor;
               return var1;
            } else {
               return -1;
            }
         }
      };
   }

   static JarInputDir access$000(JarInputFile var0) {
      return var0.jarInputDir;
   }

   static long access$100(JarInputFile var0) {
      return var0.fromIndex;
   }

   static long access$200(JarInputFile var0) {
      return var0.endIndex;
   }
}
