/*     */ package it.unimi.dsi.fastutil.doubles;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.NoSuchElementException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DoubleBigListIterators
/*     */ {
/*     */   public static class EmptyBigListIterator
/*     */     implements DoubleBigListIterator, Serializable, Cloneable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     
/*     */     public boolean hasNext() {
/*  42 */       return false;
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/*  46 */       return false;
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/*  50 */       throw new NoSuchElementException();
/*     */     }
/*     */     
/*     */     public double previousDouble() {
/*  54 */       throw new NoSuchElementException();
/*     */     }
/*     */     
/*     */     public long nextIndex() {
/*  58 */       return 0L;
/*     */     }
/*     */     
/*     */     public long previousIndex() {
/*  62 */       return -1L;
/*     */     }
/*     */     
/*     */     public long skip(long n) {
/*  66 */       return 0L;
/*     */     }
/*     */     
/*     */     public long back(long n) {
/*  70 */       return 0L;
/*     */     }
/*     */     
/*     */     public Object clone() {
/*  74 */       return DoubleBigListIterators.EMPTY_BIG_LIST_ITERATOR;
/*     */     }
/*     */     private Object readResolve() {
/*  77 */       return DoubleBigListIterators.EMPTY_BIG_LIST_ITERATOR;
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
/*  88 */   public static final EmptyBigListIterator EMPTY_BIG_LIST_ITERATOR = new EmptyBigListIterator();
/*     */   
/*     */   private static class SingletonBigListIterator implements DoubleBigListIterator { private final double element;
/*     */     private int curr;
/*     */     
/*     */     public SingletonBigListIterator(double element) {
/*  94 */       this.element = element;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/*  98 */       return (this.curr == 0);
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 102 */       return (this.curr == 1);
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 106 */       if (!hasNext())
/* 107 */         throw new NoSuchElementException(); 
/* 108 */       this.curr = 1;
/* 109 */       return this.element;
/*     */     }
/*     */     
/*     */     public double previousDouble() {
/* 113 */       if (!hasPrevious())
/* 114 */         throw new NoSuchElementException(); 
/* 115 */       this.curr = 0;
/* 116 */       return this.element;
/*     */     }
/*     */     
/*     */     public long nextIndex() {
/* 120 */       return this.curr;
/*     */     }
/*     */     
/*     */     public long previousIndex() {
/* 124 */       return (this.curr - 1);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DoubleBigListIterator singleton(double element) {
/* 135 */     return new SingletonBigListIterator(element);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableBigListIterator implements DoubleBigListIterator { protected final DoubleBigListIterator i;
/*     */     
/*     */     public UnmodifiableBigListIterator(DoubleBigListIterator i) {
/* 141 */       this.i = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 145 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 149 */       return this.i.hasPrevious();
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 153 */       return this.i.nextDouble();
/*     */     }
/*     */     
/*     */     public double previousDouble() {
/* 157 */       return this.i.previousDouble();
/*     */     }
/*     */     
/*     */     public long nextIndex() {
/* 161 */       return this.i.nextIndex();
/*     */     }
/*     */     
/*     */     public long previousIndex() {
/* 165 */       return this.i.previousIndex();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DoubleBigListIterator unmodifiable(DoubleBigListIterator i) {
/* 176 */     return new UnmodifiableBigListIterator(i);
/*     */   }
/*     */   
/*     */   public static class BigListIteratorListIterator implements DoubleBigListIterator { protected final DoubleListIterator i;
/*     */     
/*     */     protected BigListIteratorListIterator(DoubleListIterator i) {
/* 182 */       this.i = i;
/*     */     }
/*     */     private int intDisplacement(long n) {
/* 185 */       if (n < -2147483648L || n > 2147483647L)
/* 186 */         throw new IndexOutOfBoundsException("This big iterator is restricted to 32-bit displacements"); 
/* 187 */       return (int)n;
/*     */     }
/*     */     
/*     */     public void set(double ok) {
/* 191 */       this.i.set(ok);
/*     */     }
/*     */     
/*     */     public void add(double ok) {
/* 195 */       this.i.add(ok);
/*     */     }
/*     */     
/*     */     public int back(int n) {
/* 199 */       return this.i.back(n);
/*     */     }
/*     */     
/*     */     public long back(long n) {
/* 203 */       return this.i.back(intDisplacement(n));
/*     */     }
/*     */     
/*     */     public void remove() {
/* 207 */       this.i.remove();
/*     */     }
/*     */     
/*     */     public int skip(int n) {
/* 211 */       return this.i.skip(n);
/*     */     }
/*     */     
/*     */     public long skip(long n) {
/* 215 */       return this.i.skip(intDisplacement(n));
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 219 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 223 */       return this.i.hasPrevious();
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 227 */       return this.i.nextDouble();
/*     */     }
/*     */     
/*     */     public double previousDouble() {
/* 231 */       return this.i.previousDouble();
/*     */     }
/*     */     
/*     */     public long nextIndex() {
/* 235 */       return this.i.nextIndex();
/*     */     }
/*     */     
/*     */     public long previousIndex() {
/* 239 */       return this.i.previousIndex();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DoubleBigListIterator asBigListIterator(DoubleListIterator i) {
/* 250 */     return new BigListIteratorListIterator(i);
/*     */   }
/*     */ }


/* Location:              C:\Users\Syz\Downloads\Senura (1).jar!\i\\unimi\dsi\fastutil\doubles\DoubleBigListIterators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */