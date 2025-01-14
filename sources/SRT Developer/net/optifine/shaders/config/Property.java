package net.optifine.shaders.config;

import java.util.Properties;
import net.minecraft.src.Config;
import org.apache.commons.lang3.ArrayUtils;

public class Property {
   private final int[] values = null;
   private final int defaultValue;
   private final String propertyName;
   private final String[] propertyValues;
   private final String userName;
   private final String[] userValues;
   private int value;

   public Property(String propertyName, String[] propertyValues, String userName, String[] userValues, int defaultValue) {
      this.propertyName = propertyName;
      this.propertyValues = propertyValues;
      this.userName = userName;
      this.userValues = userValues;
      this.defaultValue = defaultValue;
      if (propertyValues.length != userValues.length) {
         throw new IllegalArgumentException("Property and user values have different lengths: " + propertyValues.length + " != " + userValues.length);
      } else if (defaultValue >= 0 && defaultValue < propertyValues.length) {
         this.value = defaultValue;
      } else {
         throw new IllegalArgumentException("Invalid default value: " + defaultValue);
      }
   }

   public boolean setPropertyValue(String propVal) {
      if (propVal == null) {
         this.value = this.defaultValue;
         return false;
      } else {
         this.value = ArrayUtils.indexOf(this.propertyValues, propVal);
         if (this.value >= 0 && this.value < this.propertyValues.length) {
            return true;
         } else {
            this.value = this.defaultValue;
            return false;
         }
      }
   }

   public void nextValue() {
      ++this.value;
      if (this.value < 0 || this.value >= this.propertyValues.length) {
         this.value = 0;
      }
   }

   public void setValue(int val) {
      this.value = val;
      if (this.value < 0 || this.value >= this.propertyValues.length) {
         this.value = this.defaultValue;
      }
   }

   public int getValue() {
      return this.value;
   }

   public String getUserValue() {
      return this.userValues[this.value];
   }

   public String getPropertyValue() {
      return this.propertyValues[this.value];
   }

   public String getUserName() {
      return this.userName;
   }

   public String getPropertyName() {
      return this.propertyName;
   }

   public void resetValue() {
      this.value = this.defaultValue;
   }

   public void loadFrom(Properties props) {
      this.resetValue();
      if (props != null) {
         String s = props.getProperty(this.propertyName);
         if (s != null) {
            this.setPropertyValue(s);
         }
      }
   }

   public void saveTo(Properties props) {
      if (props != null) {
         props.setProperty(this.getPropertyName(), this.getPropertyValue());
      }
   }

   @Override
   public String toString() {
      return "" + this.propertyName + "=" + this.getPropertyValue() + " [" + Config.arrayToString((Object[])this.propertyValues) + "], value: " + this.value;
   }
}
