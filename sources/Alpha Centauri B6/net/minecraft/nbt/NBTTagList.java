package net.minecraft.nbt;

import com.google.common.collect.Lists;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagEnd;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagIntArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NBTTagList extends NBTBase {
   private static final Logger LOGGER = LogManager.getLogger();
   private List tagList = Lists.newArrayList();
   private byte tagType = 0;

   public NBTBase get(int idx) {
      return (NBTBase)(idx >= 0 && idx < this.tagList.size()?(NBTBase)this.tagList.get(idx):new NBTTagEnd());
   }

   public boolean equals(Object p_equals_1_) {
      if(super.equals(p_equals_1_)) {
         NBTTagList nbttaglist = (NBTTagList)p_equals_1_;
         if(this.tagType == nbttaglist.tagType) {
            return this.tagList.equals(nbttaglist.tagList);
         }
      }

      return false;
   }

   public String toString() {
      StringBuilder stringbuilder = new StringBuilder("[");

      for(int i = 0; i < this.tagList.size(); ++i) {
         if(i != 0) {
            stringbuilder.append(',');
         }

         stringbuilder.append(i).append(':').append(this.tagList.get(i));
      }

      return stringbuilder.append(']').toString();
   }

   public int hashCode() {
      return super.hashCode() ^ this.tagList.hashCode();
   }

   void write(DataOutput output) throws IOException {
      if(!this.tagList.isEmpty()) {
         this.tagType = ((NBTBase)this.tagList.get(0)).getId();
      } else {
         this.tagType = 0;
      }

      output.writeByte(this.tagType);
      output.writeInt(this.tagList.size());

      for(int i = 0; i < this.tagList.size(); ++i) {
         ((NBTBase)this.tagList.get(i)).write(output);
      }

   }

   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
      sizeTracker.read(296L);
      if(depth > 512) {
         throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
      } else {
         this.tagType = input.readByte();
         int i = input.readInt();
         if(this.tagType == 0 && i > 0) {
            throw new RuntimeException("Missing type on ListTag");
         } else {
            sizeTracker.read(32L * (long)i);
            this.tagList = Lists.newArrayListWithCapacity(i);

            for(int j = 0; j < i; ++j) {
               NBTBase nbtbase = NBTBase.createNewByType(this.tagType);
               nbtbase.read(input, depth + 1, sizeTracker);
               this.tagList.add(nbtbase);
            }

         }
      }
   }

   public byte getId() {
      return (byte)9;
   }

   public NBTBase copy() {
      NBTTagList nbttaglist = new NBTTagList();
      nbttaglist.tagType = this.tagType;

      for(NBTBase nbtbase : this.tagList) {
         NBTBase nbtbase1 = nbtbase.copy();
         nbttaglist.tagList.add(nbtbase1);
      }

      return nbttaglist;
   }

   public void set(int idx, NBTBase nbt) {
      if(nbt.getId() == 0) {
         LOGGER.warn("Invalid TagEnd added to ListTag");
      } else if(idx >= 0 && idx < this.tagList.size()) {
         if(this.tagType == 0) {
            this.tagType = nbt.getId();
         } else if(this.tagType != nbt.getId()) {
            LOGGER.warn("Adding mismatching tag types to tag list");
            return;
         }

         this.tagList.set(idx, nbt);
      } else {
         LOGGER.warn("index out of bounds to set tag in tag list");
      }

   }

   public double getDoubleAt(int i) {
      if(i >= 0 && i < this.tagList.size()) {
         NBTBase nbtbase = (NBTBase)this.tagList.get(i);
         return nbtbase.getId() == 6?((NBTTagDouble)nbtbase).getDouble():0.0D;
      } else {
         return 0.0D;
      }
   }

   public float getFloatAt(int i) {
      if(i >= 0 && i < this.tagList.size()) {
         NBTBase nbtbase = (NBTBase)this.tagList.get(i);
         return nbtbase.getId() == 5?((NBTTagFloat)nbtbase).getFloat():0.0F;
      } else {
         return 0.0F;
      }
   }

   public NBTBase removeTag(int i) {
      return (NBTBase)this.tagList.remove(i);
   }

   public boolean hasNoTags() {
      return this.tagList.isEmpty();
   }

   public int getTagType() {
      return this.tagType;
   }

   public String getStringTagAt(int i) {
      if(i >= 0 && i < this.tagList.size()) {
         NBTBase nbtbase = (NBTBase)this.tagList.get(i);
         return nbtbase.getId() == 8?nbtbase.getString():nbtbase.toString();
      } else {
         return "";
      }
   }

   public int[] getIntArrayAt(int i) {
      if(i >= 0 && i < this.tagList.size()) {
         NBTBase nbtbase = (NBTBase)this.tagList.get(i);
         return nbtbase.getId() == 11?((NBTTagIntArray)nbtbase).getIntArray():new int[0];
      } else {
         return new int[0];
      }
   }

   public NBTTagCompound getCompoundTagAt(int i) {
      if(i >= 0 && i < this.tagList.size()) {
         NBTBase nbtbase = (NBTBase)this.tagList.get(i);
         return nbtbase.getId() == 10?(NBTTagCompound)nbtbase:new NBTTagCompound();
      } else {
         return new NBTTagCompound();
      }
   }

   public int tagCount() {
      return this.tagList.size();
   }

   public void appendTag(NBTBase nbt) {
      if(nbt.getId() == 0) {
         LOGGER.warn("Invalid TagEnd added to ListTag");
      } else {
         if(this.tagType == 0) {
            this.tagType = nbt.getId();
         } else if(this.tagType != nbt.getId()) {
            LOGGER.warn("Adding mismatching tag types to tag list");
            return;
         }

         this.tagList.add(nbt);
      }

   }
}
