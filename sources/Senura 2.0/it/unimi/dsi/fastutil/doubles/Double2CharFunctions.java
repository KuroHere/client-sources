/*     */ package it.unimi.dsi.fastutil.doubles;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.Function;
/*     */ import it.unimi.dsi.fastutil.SafeMath;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Objects;
/*     */ import java.util.function.DoubleToIntFunction;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Double2CharFunctions
/*     */ {
/*     */   public static class EmptyFunction
/*     */     extends AbstractDouble2CharFunction
/*     */     implements Serializable, Cloneable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     
/*     */     public char get(double k) {
/*  41 */       return Character.MIN_VALUE;
/*     */     }
/*     */     
/*     */     public boolean containsKey(double k) {
/*  45 */       return false;
/*     */     }
/*     */     
/*     */     public char defaultReturnValue() {
/*  49 */       return Character.MIN_VALUE;
/*     */     }
/*     */     
/*     */     public void defaultReturnValue(char defRetValue) {
/*  53 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int size() {
/*  57 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {}
/*     */     
/*     */     public Object clone() {
/*  64 */       return Double2CharFunctions.EMPTY_FUNCTION;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/*  68 */       return 0;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/*  72 */       if (!(o instanceof Function))
/*  73 */         return false; 
/*  74 */       return (((Function)o).size() == 0);
/*     */     }
/*     */     
/*     */     public String toString() {
/*  78 */       return "{}";
/*     */     }
/*     */     private Object readResolve() {
/*  81 */       return Double2CharFunctions.EMPTY_FUNCTION;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   public static final EmptyFunction EMPTY_FUNCTION = new EmptyFunction();
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Singleton
/*     */     extends AbstractDouble2CharFunction
/*     */     implements Serializable, Cloneable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */ 
/*     */     
/*     */     protected final double key;
/*     */ 
/*     */     
/*     */     protected final char value;
/*     */ 
/*     */     
/*     */     protected Singleton(double key, char value) {
/* 107 */       this.key = key;
/* 108 */       this.value = value;
/*     */     }
/*     */     
/*     */     public boolean containsKey(double k) {
/* 112 */       return (Double.doubleToLongBits(this.key) == Double.doubleToLongBits(k));
/*     */     }
/*     */     
/*     */     public char get(double k) {
/* 116 */       return (Double.doubleToLongBits(this.key) == Double.doubleToLongBits(k)) ? this.value : this.defRetValue;
/*     */     }
/*     */     
/*     */     public int size() {
/* 120 */       return 1;
/*     */     }
/*     */     
/*     */     public Object clone() {
/* 124 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Double2CharFunction singleton(double key, char value) {
/* 143 */     return new Singleton(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Double2CharFunction singleton(Double key, Character value) {
/* 161 */     return new Singleton(key.doubleValue(), value.charValue());
/*     */   }
/*     */   
/*     */   public static class SynchronizedFunction implements Double2CharFunction, Serializable { private static final long serialVersionUID = -7046029254386353129L;
/*     */     protected final Double2CharFunction function;
/*     */     protected final Object sync;
/*     */     
/*     */     protected SynchronizedFunction(Double2CharFunction f, Object sync) {
/* 169 */       if (f == null)
/* 170 */         throw new NullPointerException(); 
/* 171 */       this.function = f;
/* 172 */       this.sync = sync;
/*     */     }
/*     */     protected SynchronizedFunction(Double2CharFunction f) {
/* 175 */       if (f == null)
/* 176 */         throw new NullPointerException(); 
/* 177 */       this.function = f;
/* 178 */       this.sync = this;
/*     */     }
/*     */     
/*     */     public int applyAsInt(double operand) {
/* 182 */       synchronized (this.sync) {
/* 183 */         return this.function.applyAsInt(operand);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Character apply(Double key) {
/* 194 */       synchronized (this.sync) {
/* 195 */         return (Character)this.function.apply(key);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int size() {
/* 200 */       synchronized (this.sync) {
/* 201 */         return this.function.size();
/*     */       } 
/*     */     }
/*     */     
/*     */     public char defaultReturnValue() {
/* 206 */       synchronized (this.sync) {
/* 207 */         return this.function.defaultReturnValue();
/*     */       } 
/*     */     }
/*     */     
/*     */     public void defaultReturnValue(char defRetValue) {
/* 212 */       synchronized (this.sync) {
/* 213 */         this.function.defaultReturnValue(defRetValue);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean containsKey(double k) {
/* 218 */       synchronized (this.sync) {
/* 219 */         return this.function.containsKey(k);
/*     */       } 
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public boolean containsKey(Object k) {
/* 225 */       synchronized (this.sync) {
/* 226 */         return this.function.containsKey(k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public char put(double k, char v) {
/* 231 */       synchronized (this.sync) {
/* 232 */         return this.function.put(k, v);
/*     */       } 
/*     */     }
/*     */     
/*     */     public char get(double k) {
/* 237 */       synchronized (this.sync) {
/* 238 */         return this.function.get(k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public char remove(double k) {
/* 243 */       synchronized (this.sync) {
/* 244 */         return this.function.remove(k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void clear() {
/* 249 */       synchronized (this.sync) {
/* 250 */         this.function.clear();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Character put(Double k, Character v) {
/* 261 */       synchronized (this.sync) {
/* 262 */         return this.function.put(k, v);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Character get(Object k) {
/* 273 */       synchronized (this.sync) {
/* 274 */         return this.function.get(k);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Character remove(Object k) {
/* 285 */       synchronized (this.sync) {
/* 286 */         return this.function.remove(k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 291 */       synchronized (this.sync) {
/* 292 */         return this.function.hashCode();
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 297 */       if (o == this)
/* 298 */         return true; 
/* 299 */       synchronized (this.sync) {
/* 300 */         return this.function.equals(o);
/*     */       } 
/*     */     }
/*     */     
/*     */     public String toString() {
/* 305 */       synchronized (this.sync) {
/* 306 */         return this.function.toString();
/*     */       } 
/*     */     }
/*     */     private void writeObject(ObjectOutputStream s) throws IOException {
/* 310 */       synchronized (this.sync) {
/* 311 */         s.defaultWriteObject();
/*     */       } 
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Double2CharFunction synchronize(Double2CharFunction f) {
/* 325 */     return new SynchronizedFunction(f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Double2CharFunction synchronize(Double2CharFunction f, Object sync) {
/* 340 */     return new SynchronizedFunction(f, sync);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableFunction extends AbstractDouble2CharFunction implements Serializable { private static final long serialVersionUID = -7046029254386353129L;
/*     */     protected final Double2CharFunction function;
/*     */     
/*     */     protected UnmodifiableFunction(Double2CharFunction f) {
/* 347 */       if (f == null)
/* 348 */         throw new NullPointerException(); 
/* 349 */       this.function = f;
/*     */     }
/*     */     
/*     */     public int size() {
/* 353 */       return this.function.size();
/*     */     }
/*     */     
/*     */     public char defaultReturnValue() {
/* 357 */       return this.function.defaultReturnValue();
/*     */     }
/*     */     
/*     */     public void defaultReturnValue(char defRetValue) {
/* 361 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean containsKey(double k) {
/* 365 */       return this.function.containsKey(k);
/*     */     }
/*     */     
/*     */     public char put(double k, char v) {
/* 369 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public char get(double k) {
/* 373 */       return this.function.get(k);
/*     */     }
/*     */     
/*     */     public char remove(double k) {
/* 377 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void clear() {
/* 381 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Character put(Double k, Character v) {
/* 391 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Character get(Object k) {
/* 401 */       return this.function.get(k);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Character remove(Object k) {
/* 411 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 415 */       return this.function.hashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 419 */       return (o == this || this.function.equals(o));
/*     */     }
/*     */     
/*     */     public String toString() {
/* 423 */       return this.function.toString();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Double2CharFunction unmodifiable(Double2CharFunction f) {
/* 436 */     return new UnmodifiableFunction(f);
/*     */   }
/*     */   
/*     */   public static class PrimitiveFunction
/*     */     implements Double2CharFunction
/*     */   {
/*     */     protected final Function<? super Double, ? extends Character> function;
/*     */     
/*     */     protected PrimitiveFunction(Function<? super Double, ? extends Character> function) {
/* 445 */       this.function = function;
/*     */     }
/*     */     
/*     */     public boolean containsKey(double key) {
/* 449 */       return (this.function.apply(Double.valueOf(key)) != null);
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public boolean containsKey(Object key) {
/* 455 */       if (key == null)
/* 456 */         return false; 
/* 457 */       return (this.function.apply((Double)key) != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public char get(double key) {
/* 462 */       Character v = this.function.apply(Double.valueOf(key));
/* 463 */       if (v == null)
/* 464 */         return defaultReturnValue(); 
/* 465 */       return v.charValue();
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Character get(Object key) {
/* 471 */       if (key == null)
/* 472 */         return null; 
/* 473 */       return this.function.apply((Double)key);
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public Character put(Double key, Character value) {
/* 478 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Double2CharFunction primitive(Function<? super Double, ? extends Character> f) {
/* 505 */     Objects.requireNonNull(f);
/* 506 */     if (f instanceof Double2CharFunction)
/* 507 */       return (Double2CharFunction)f; 
/* 508 */     if (f instanceof DoubleToIntFunction) {
/* 509 */       return key -> SafeMath.safeIntToChar(((DoubleToIntFunction)f).applyAsInt(key));
/*     */     }
/* 511 */     return new PrimitiveFunction(f);
/*     */   }
/*     */ }


/* Location:              C:\Users\Syz\Downloads\Senura (1).jar!\i\\unimi\dsi\fastutil\doubles\Double2CharFunctions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */