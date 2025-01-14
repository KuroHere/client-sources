/*      */ package it.unimi.dsi.fastutil.chars;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
/*      */ import it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectSet;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
/*      */ import it.unimi.dsi.fastutil.objects.ReferenceCollection;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Char2ReferenceRBTreeMap<V>
/*      */   extends AbstractChar2ReferenceSortedMap<V>
/*      */   implements Serializable, Cloneable
/*      */ {
/*      */   protected transient Entry<V> tree;
/*      */   protected int count;
/*      */   protected transient Entry<V> firstEntry;
/*      */   protected transient Entry<V> lastEntry;
/*      */   protected transient ObjectSortedSet<Char2ReferenceMap.Entry<V>> entries;
/*      */   protected transient CharSortedSet keys;
/*      */   protected transient ReferenceCollection<V> values;
/*      */   protected transient boolean modified;
/*      */   protected Comparator<? super Character> storedComparator;
/*      */   protected transient CharComparator actualComparator;
/*      */   private static final long serialVersionUID = -7046029254386353129L;
/*      */   private transient boolean[] dirPath;
/*      */   private transient Entry<V>[] nodePath;
/*      */   
/*      */   public Char2ReferenceRBTreeMap() {
/*   74 */     allocatePaths();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   80 */     this.tree = null;
/*   81 */     this.count = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setActualComparator() {
/*   93 */     this.actualComparator = CharComparators.asCharComparator(this.storedComparator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Char2ReferenceRBTreeMap(Comparator<? super Character> c) {
/*  102 */     this();
/*  103 */     this.storedComparator = c;
/*  104 */     setActualComparator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Char2ReferenceRBTreeMap(Map<? extends Character, ? extends V> m) {
/*  113 */     this();
/*  114 */     putAll(m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Char2ReferenceRBTreeMap(SortedMap<Character, V> m) {
/*  124 */     this(m.comparator());
/*  125 */     putAll(m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Char2ReferenceRBTreeMap(Char2ReferenceMap<? extends V> m) {
/*  134 */     this();
/*  135 */     putAll(m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Char2ReferenceRBTreeMap(Char2ReferenceSortedMap<V> m) {
/*  145 */     this(m.comparator());
/*  146 */     putAll(m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Char2ReferenceRBTreeMap(char[] k, V[] v, Comparator<? super Character> c) {
/*  162 */     this(c);
/*  163 */     if (k.length != v.length) {
/*  164 */       throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
/*      */     }
/*  166 */     for (int i = 0; i < k.length; i++) {
/*  167 */       put(k[i], v[i]);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Char2ReferenceRBTreeMap(char[] k, V[] v) {
/*  180 */     this(k, v, (Comparator<? super Character>)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final int compare(char k1, char k2) {
/*  208 */     return (this.actualComparator == null) ? Character.compare(k1, k2) : this.actualComparator.compare(k1, k2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final Entry<V> findKey(char k) {
/*  220 */     Entry<V> e = this.tree;
/*      */     int cmp;
/*  222 */     while (e != null && (cmp = compare(k, e.key)) != 0)
/*  223 */       e = (cmp < 0) ? e.left() : e.right(); 
/*  224 */     return e;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final Entry<V> locateKey(char k) {
/*  236 */     Entry<V> e = this.tree, last = this.tree;
/*  237 */     int cmp = 0;
/*  238 */     while (e != null && (cmp = compare(k, e.key)) != 0) {
/*  239 */       last = e;
/*  240 */       e = (cmp < 0) ? e.left() : e.right();
/*      */     } 
/*  242 */     return (cmp == 0) ? e : last;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void allocatePaths() {
/*  252 */     this.dirPath = new boolean[64];
/*  253 */     this.nodePath = (Entry<V>[])new Entry[64];
/*      */   }
/*      */   
/*      */   public V put(char k, V v) {
/*  257 */     Entry<V> e = add(k);
/*  258 */     V oldValue = e.value;
/*  259 */     e.value = v;
/*  260 */     return oldValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Entry<V> add(char k) {
/*      */     Entry<V> e;
/*  277 */     this.modified = false;
/*  278 */     int maxDepth = 0;
/*      */     
/*  280 */     if (this.tree == null) {
/*  281 */       this.count++;
/*  282 */       e = this.tree = this.lastEntry = this.firstEntry = new Entry<>(k, this.defRetValue);
/*      */     } else {
/*  284 */       Entry<V> p = this.tree;
/*  285 */       int i = 0; while (true) {
/*      */         int cmp;
/*  287 */         if ((cmp = compare(k, p.key)) == 0) {
/*      */           
/*  289 */           while (i-- != 0)
/*  290 */             this.nodePath[i] = null; 
/*  291 */           return p;
/*      */         } 
/*  293 */         this.nodePath[i] = p;
/*  294 */         this.dirPath[i++] = (cmp > 0); if ((cmp > 0)) {
/*  295 */           if (p.succ()) {
/*  296 */             this.count++;
/*  297 */             e = new Entry<>(k, this.defRetValue);
/*  298 */             if (p.right == null)
/*  299 */               this.lastEntry = e; 
/*  300 */             e.left = p;
/*  301 */             e.right = p.right;
/*  302 */             p.right(e);
/*      */             break;
/*      */           } 
/*  305 */           p = p.right; continue;
/*      */         } 
/*  307 */         if (p.pred()) {
/*  308 */           this.count++;
/*  309 */           e = new Entry<>(k, this.defRetValue);
/*  310 */           if (p.left == null)
/*  311 */             this.firstEntry = e; 
/*  312 */           e.right = p;
/*  313 */           e.left = p.left;
/*  314 */           p.left(e);
/*      */           break;
/*      */         } 
/*  317 */         p = p.left;
/*      */       } 
/*      */       
/*  320 */       this.modified = true;
/*  321 */       maxDepth = i--;
/*  322 */       while (i > 0 && !this.nodePath[i].black()) {
/*  323 */         if (!this.dirPath[i - 1]) {
/*  324 */           Entry<V> entry1 = (this.nodePath[i - 1]).right;
/*  325 */           if (!this.nodePath[i - 1].succ() && !entry1.black()) {
/*  326 */             this.nodePath[i].black(true);
/*  327 */             entry1.black(true);
/*  328 */             this.nodePath[i - 1].black(false);
/*  329 */             i -= 2;
/*      */             continue;
/*      */           } 
/*  332 */           if (!this.dirPath[i]) {
/*  333 */             entry1 = this.nodePath[i];
/*      */           } else {
/*  335 */             Entry<V> entry = this.nodePath[i];
/*  336 */             entry1 = entry.right;
/*  337 */             entry.right = entry1.left;
/*  338 */             entry1.left = entry;
/*  339 */             (this.nodePath[i - 1]).left = entry1;
/*  340 */             if (entry1.pred()) {
/*  341 */               entry1.pred(false);
/*  342 */               entry.succ(entry1);
/*      */             } 
/*      */           } 
/*  345 */           Entry<V> entry2 = this.nodePath[i - 1];
/*  346 */           entry2.black(false);
/*  347 */           entry1.black(true);
/*  348 */           entry2.left = entry1.right;
/*  349 */           entry1.right = entry2;
/*  350 */           if (i < 2) {
/*  351 */             this.tree = entry1;
/*      */           }
/*  353 */           else if (this.dirPath[i - 2]) {
/*  354 */             (this.nodePath[i - 2]).right = entry1;
/*      */           } else {
/*  356 */             (this.nodePath[i - 2]).left = entry1;
/*      */           } 
/*  358 */           if (entry1.succ()) {
/*  359 */             entry1.succ(false);
/*  360 */             entry2.pred(entry1);
/*      */           } 
/*      */           
/*      */           break;
/*      */         } 
/*  365 */         Entry<V> y = (this.nodePath[i - 1]).left;
/*  366 */         if (!this.nodePath[i - 1].pred() && !y.black()) {
/*  367 */           this.nodePath[i].black(true);
/*  368 */           y.black(true);
/*  369 */           this.nodePath[i - 1].black(false);
/*  370 */           i -= 2;
/*      */           continue;
/*      */         } 
/*  373 */         if (this.dirPath[i]) {
/*  374 */           y = this.nodePath[i];
/*      */         } else {
/*  376 */           Entry<V> entry = this.nodePath[i];
/*  377 */           y = entry.left;
/*  378 */           entry.left = y.right;
/*  379 */           y.right = entry;
/*  380 */           (this.nodePath[i - 1]).right = y;
/*  381 */           if (y.succ()) {
/*  382 */             y.succ(false);
/*  383 */             entry.pred(y);
/*      */           } 
/*      */         } 
/*  386 */         Entry<V> x = this.nodePath[i - 1];
/*  387 */         x.black(false);
/*  388 */         y.black(true);
/*  389 */         x.right = y.left;
/*  390 */         y.left = x;
/*  391 */         if (i < 2) {
/*  392 */           this.tree = y;
/*      */         }
/*  394 */         else if (this.dirPath[i - 2]) {
/*  395 */           (this.nodePath[i - 2]).right = y;
/*      */         } else {
/*  397 */           (this.nodePath[i - 2]).left = y;
/*      */         } 
/*  399 */         if (y.pred()) {
/*  400 */           y.pred(false);
/*  401 */           x.succ(y);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  408 */     this.tree.black(true);
/*      */     
/*  410 */     while (maxDepth-- != 0)
/*  411 */       this.nodePath[maxDepth] = null; 
/*  412 */     return e;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public V remove(char k) {
/*  421 */     this.modified = false;
/*  422 */     if (this.tree == null)
/*  423 */       return this.defRetValue; 
/*  424 */     Entry<V> p = this.tree;
/*      */     
/*  426 */     int i = 0;
/*  427 */     char kk = k;
/*      */     int cmp;
/*  429 */     while ((cmp = compare(kk, p.key)) != 0) {
/*      */       
/*  431 */       this.dirPath[i] = (cmp > 0);
/*  432 */       this.nodePath[i] = p;
/*  433 */       if (this.dirPath[i++]) {
/*  434 */         if ((p = p.right()) == null) {
/*      */           
/*  436 */           while (i-- != 0)
/*  437 */             this.nodePath[i] = null; 
/*  438 */           return this.defRetValue;
/*      */         }  continue;
/*      */       } 
/*  441 */       if ((p = p.left()) == null) {
/*      */         
/*  443 */         while (i-- != 0)
/*  444 */           this.nodePath[i] = null; 
/*  445 */         return this.defRetValue;
/*      */       } 
/*      */     } 
/*      */     
/*  449 */     if (p.left == null)
/*  450 */       this.firstEntry = p.next(); 
/*  451 */     if (p.right == null)
/*  452 */       this.lastEntry = p.prev(); 
/*  453 */     if (p.succ()) {
/*  454 */       if (p.pred()) {
/*  455 */         if (i == 0) {
/*  456 */           this.tree = p.left;
/*      */         }
/*  458 */         else if (this.dirPath[i - 1]) {
/*  459 */           this.nodePath[i - 1].succ(p.right);
/*      */         } else {
/*  461 */           this.nodePath[i - 1].pred(p.left);
/*      */         } 
/*      */       } else {
/*  464 */         (p.prev()).right = p.right;
/*  465 */         if (i == 0) {
/*  466 */           this.tree = p.left;
/*      */         }
/*  468 */         else if (this.dirPath[i - 1]) {
/*  469 */           (this.nodePath[i - 1]).right = p.left;
/*      */         } else {
/*  471 */           (this.nodePath[i - 1]).left = p.left;
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/*  476 */       Entry<V> r = p.right;
/*  477 */       if (r.pred()) {
/*  478 */         r.left = p.left;
/*  479 */         r.pred(p.pred());
/*  480 */         if (!r.pred())
/*  481 */           (r.prev()).right = r; 
/*  482 */         if (i == 0) {
/*  483 */           this.tree = r;
/*      */         }
/*  485 */         else if (this.dirPath[i - 1]) {
/*  486 */           (this.nodePath[i - 1]).right = r;
/*      */         } else {
/*  488 */           (this.nodePath[i - 1]).left = r;
/*      */         } 
/*  490 */         boolean color = r.black();
/*  491 */         r.black(p.black());
/*  492 */         p.black(color);
/*  493 */         this.dirPath[i] = true;
/*  494 */         this.nodePath[i++] = r;
/*      */       } else {
/*      */         Entry<V> s;
/*  497 */         int j = i++;
/*      */         while (true) {
/*  499 */           this.dirPath[i] = false;
/*  500 */           this.nodePath[i++] = r;
/*  501 */           s = r.left;
/*  502 */           if (s.pred())
/*      */             break; 
/*  504 */           r = s;
/*      */         } 
/*  506 */         this.dirPath[j] = true;
/*  507 */         this.nodePath[j] = s;
/*  508 */         if (s.succ()) {
/*  509 */           r.pred(s);
/*      */         } else {
/*  511 */           r.left = s.right;
/*  512 */         }  s.left = p.left;
/*  513 */         if (!p.pred()) {
/*  514 */           (p.prev()).right = s;
/*  515 */           s.pred(false);
/*      */         } 
/*  517 */         s.right(p.right);
/*  518 */         boolean color = s.black();
/*  519 */         s.black(p.black());
/*  520 */         p.black(color);
/*  521 */         if (j == 0) {
/*  522 */           this.tree = s;
/*      */         }
/*  524 */         else if (this.dirPath[j - 1]) {
/*  525 */           (this.nodePath[j - 1]).right = s;
/*      */         } else {
/*  527 */           (this.nodePath[j - 1]).left = s;
/*      */         } 
/*      */       } 
/*      */     } 
/*  531 */     int maxDepth = i;
/*  532 */     if (p.black()) {
/*  533 */       for (; i > 0; i--) {
/*  534 */         if ((this.dirPath[i - 1] && !this.nodePath[i - 1].succ()) || (!this.dirPath[i - 1] && !this.nodePath[i - 1].pred())) {
/*  535 */           Entry<V> x = this.dirPath[i - 1] ? (this.nodePath[i - 1]).right : (this.nodePath[i - 1]).left;
/*  536 */           if (!x.black()) {
/*  537 */             x.black(true);
/*      */             break;
/*      */           } 
/*      */         } 
/*  541 */         if (!this.dirPath[i - 1]) {
/*  542 */           Entry<V> w = (this.nodePath[i - 1]).right;
/*  543 */           if (!w.black()) {
/*  544 */             w.black(true);
/*  545 */             this.nodePath[i - 1].black(false);
/*  546 */             (this.nodePath[i - 1]).right = w.left;
/*  547 */             w.left = this.nodePath[i - 1];
/*  548 */             if (i < 2) {
/*  549 */               this.tree = w;
/*      */             }
/*  551 */             else if (this.dirPath[i - 2]) {
/*  552 */               (this.nodePath[i - 2]).right = w;
/*      */             } else {
/*  554 */               (this.nodePath[i - 2]).left = w;
/*      */             } 
/*  556 */             this.nodePath[i] = this.nodePath[i - 1];
/*  557 */             this.dirPath[i] = false;
/*  558 */             this.nodePath[i - 1] = w;
/*  559 */             if (maxDepth == i++)
/*  560 */               maxDepth++; 
/*  561 */             w = (this.nodePath[i - 1]).right;
/*      */           } 
/*  563 */           if ((w.pred() || w.left.black()) && (w.succ() || w.right.black())) {
/*  564 */             w.black(false);
/*      */           } else {
/*  566 */             if (w.succ() || w.right.black()) {
/*  567 */               Entry<V> y = w.left;
/*  568 */               y.black(true);
/*  569 */               w.black(false);
/*  570 */               w.left = y.right;
/*  571 */               y.right = w;
/*  572 */               w = (this.nodePath[i - 1]).right = y;
/*  573 */               if (w.succ()) {
/*  574 */                 w.succ(false);
/*  575 */                 w.right.pred(w);
/*      */               } 
/*      */             } 
/*  578 */             w.black(this.nodePath[i - 1].black());
/*  579 */             this.nodePath[i - 1].black(true);
/*  580 */             w.right.black(true);
/*  581 */             (this.nodePath[i - 1]).right = w.left;
/*  582 */             w.left = this.nodePath[i - 1];
/*  583 */             if (i < 2) {
/*  584 */               this.tree = w;
/*      */             }
/*  586 */             else if (this.dirPath[i - 2]) {
/*  587 */               (this.nodePath[i - 2]).right = w;
/*      */             } else {
/*  589 */               (this.nodePath[i - 2]).left = w;
/*      */             } 
/*  591 */             if (w.pred()) {
/*  592 */               w.pred(false);
/*  593 */               this.nodePath[i - 1].succ(w);
/*      */             } 
/*      */             break;
/*      */           } 
/*      */         } else {
/*  598 */           Entry<V> w = (this.nodePath[i - 1]).left;
/*  599 */           if (!w.black()) {
/*  600 */             w.black(true);
/*  601 */             this.nodePath[i - 1].black(false);
/*  602 */             (this.nodePath[i - 1]).left = w.right;
/*  603 */             w.right = this.nodePath[i - 1];
/*  604 */             if (i < 2) {
/*  605 */               this.tree = w;
/*      */             }
/*  607 */             else if (this.dirPath[i - 2]) {
/*  608 */               (this.nodePath[i - 2]).right = w;
/*      */             } else {
/*  610 */               (this.nodePath[i - 2]).left = w;
/*      */             } 
/*  612 */             this.nodePath[i] = this.nodePath[i - 1];
/*  613 */             this.dirPath[i] = true;
/*  614 */             this.nodePath[i - 1] = w;
/*  615 */             if (maxDepth == i++)
/*  616 */               maxDepth++; 
/*  617 */             w = (this.nodePath[i - 1]).left;
/*      */           } 
/*  619 */           if ((w.pred() || w.left.black()) && (w.succ() || w.right.black())) {
/*  620 */             w.black(false);
/*      */           } else {
/*  622 */             if (w.pred() || w.left.black()) {
/*  623 */               Entry<V> y = w.right;
/*  624 */               y.black(true);
/*  625 */               w.black(false);
/*  626 */               w.right = y.left;
/*  627 */               y.left = w;
/*  628 */               w = (this.nodePath[i - 1]).left = y;
/*  629 */               if (w.pred()) {
/*  630 */                 w.pred(false);
/*  631 */                 w.left.succ(w);
/*      */               } 
/*      */             } 
/*  634 */             w.black(this.nodePath[i - 1].black());
/*  635 */             this.nodePath[i - 1].black(true);
/*  636 */             w.left.black(true);
/*  637 */             (this.nodePath[i - 1]).left = w.right;
/*  638 */             w.right = this.nodePath[i - 1];
/*  639 */             if (i < 2) {
/*  640 */               this.tree = w;
/*      */             }
/*  642 */             else if (this.dirPath[i - 2]) {
/*  643 */               (this.nodePath[i - 2]).right = w;
/*      */             } else {
/*  645 */               (this.nodePath[i - 2]).left = w;
/*      */             } 
/*  647 */             if (w.succ()) {
/*  648 */               w.succ(false);
/*  649 */               this.nodePath[i - 1].pred(w);
/*      */             } 
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       } 
/*  655 */       if (this.tree != null)
/*  656 */         this.tree.black(true); 
/*      */     } 
/*  658 */     this.modified = true;
/*  659 */     this.count--;
/*      */     
/*  661 */     while (maxDepth-- != 0)
/*  662 */       this.nodePath[maxDepth] = null; 
/*  663 */     return p.value;
/*      */   }
/*      */   
/*      */   public boolean containsValue(Object v) {
/*  667 */     ValueIterator i = new ValueIterator();
/*      */     
/*  669 */     int j = this.count;
/*  670 */     while (j-- != 0) {
/*  671 */       Object ev = i.next();
/*  672 */       if (ev == v)
/*  673 */         return true; 
/*      */     } 
/*  675 */     return false;
/*      */   }
/*      */   
/*      */   public void clear() {
/*  679 */     this.count = 0;
/*  680 */     this.tree = null;
/*  681 */     this.entries = null;
/*  682 */     this.values = null;
/*  683 */     this.keys = null;
/*  684 */     this.firstEntry = this.lastEntry = null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class Entry<V>
/*      */     extends AbstractChar2ReferenceMap.BasicEntry<V>
/*      */     implements Cloneable
/*      */   {
/*      */     private static final int BLACK_MASK = 1;
/*      */ 
/*      */     
/*      */     private static final int SUCC_MASK = -2147483648;
/*      */ 
/*      */     
/*      */     private static final int PRED_MASK = 1073741824;
/*      */ 
/*      */     
/*      */     Entry<V> left;
/*      */ 
/*      */     
/*      */     Entry<V> right;
/*      */ 
/*      */     
/*      */     int info;
/*      */ 
/*      */     
/*      */     Entry() {
/*  712 */       super(false, (V)null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Entry(char k, V v) {
/*  723 */       super(k, v);
/*  724 */       this.info = -1073741824;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Entry<V> left() {
/*  732 */       return ((this.info & 0x40000000) != 0) ? null : this.left;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Entry<V> right() {
/*  740 */       return ((this.info & Integer.MIN_VALUE) != 0) ? null : this.right;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean pred() {
/*  748 */       return ((this.info & 0x40000000) != 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean succ() {
/*  756 */       return ((this.info & Integer.MIN_VALUE) != 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void pred(boolean pred) {
/*  765 */       if (pred) {
/*  766 */         this.info |= 0x40000000;
/*      */       } else {
/*  768 */         this.info &= 0xBFFFFFFF;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void succ(boolean succ) {
/*  777 */       if (succ) {
/*  778 */         this.info |= Integer.MIN_VALUE;
/*      */       } else {
/*  780 */         this.info &= Integer.MAX_VALUE;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void pred(Entry<V> pred) {
/*  789 */       this.info |= 0x40000000;
/*  790 */       this.left = pred;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void succ(Entry<V> succ) {
/*  799 */       this.info |= Integer.MIN_VALUE;
/*  800 */       this.right = succ;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void left(Entry<V> left) {
/*  809 */       this.info &= 0xBFFFFFFF;
/*  810 */       this.left = left;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void right(Entry<V> right) {
/*  819 */       this.info &= Integer.MAX_VALUE;
/*  820 */       this.right = right;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean black() {
/*  828 */       return ((this.info & 0x1) != 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void black(boolean black) {
/*  837 */       if (black) {
/*  838 */         this.info |= 0x1;
/*      */       } else {
/*  840 */         this.info &= 0xFFFFFFFE;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Entry<V> next() {
/*  848 */       Entry<V> next = this.right;
/*  849 */       if ((this.info & Integer.MIN_VALUE) == 0)
/*  850 */         while ((next.info & 0x40000000) == 0)
/*  851 */           next = next.left;  
/*  852 */       return next;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Entry<V> prev() {
/*  860 */       Entry<V> prev = this.left;
/*  861 */       if ((this.info & 0x40000000) == 0)
/*  862 */         while ((prev.info & Integer.MIN_VALUE) == 0)
/*  863 */           prev = prev.right;  
/*  864 */       return prev;
/*      */     }
/*      */     
/*      */     public V setValue(V value) {
/*  868 */       V oldValue = this.value;
/*  869 */       this.value = value;
/*  870 */       return oldValue;
/*      */     }
/*      */ 
/*      */     
/*      */     public Entry<V> clone() {
/*      */       Entry<V> c;
/*      */       try {
/*  877 */         c = (Entry<V>)super.clone();
/*  878 */       } catch (CloneNotSupportedException cantHappen) {
/*  879 */         throw new InternalError();
/*      */       } 
/*  881 */       c.key = this.key;
/*  882 */       c.value = this.value;
/*  883 */       c.info = this.info;
/*  884 */       return c;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  889 */       if (!(o instanceof Map.Entry))
/*  890 */         return false; 
/*  891 */       Map.Entry<Character, V> e = (Map.Entry<Character, V>)o;
/*  892 */       return (this.key == ((Character)e.getKey()).charValue() && this.value == e.getValue());
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  896 */       return this.key ^ ((this.value == null) ? 0 : System.identityHashCode(this.value));
/*      */     }
/*      */     
/*      */     public String toString() {
/*  900 */       return this.key + "=>" + this.value;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean containsKey(char k) {
/*  921 */     return (findKey(k) != null);
/*      */   }
/*      */   
/*      */   public int size() {
/*  925 */     return this.count;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  929 */     return (this.count == 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public V get(char k) {
/*  934 */     Entry<V> e = findKey(k);
/*  935 */     return (e == null) ? this.defRetValue : e.value;
/*      */   }
/*      */   
/*      */   public char firstCharKey() {
/*  939 */     if (this.tree == null)
/*  940 */       throw new NoSuchElementException(); 
/*  941 */     return this.firstEntry.key;
/*      */   }
/*      */   
/*      */   public char lastCharKey() {
/*  945 */     if (this.tree == null)
/*  946 */       throw new NoSuchElementException(); 
/*  947 */     return this.lastEntry.key;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class TreeIterator
/*      */   {
/*      */     Char2ReferenceRBTreeMap.Entry<V> prev;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Char2ReferenceRBTreeMap.Entry<V> next;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Char2ReferenceRBTreeMap.Entry<V> curr;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  978 */     int index = 0;
/*      */     TreeIterator() {
/*  980 */       this.next = Char2ReferenceRBTreeMap.this.firstEntry;
/*      */     }
/*      */     TreeIterator(char k) {
/*  983 */       if ((this.next = Char2ReferenceRBTreeMap.this.locateKey(k)) != null)
/*  984 */         if (Char2ReferenceRBTreeMap.this.compare(this.next.key, k) <= 0) {
/*  985 */           this.prev = this.next;
/*  986 */           this.next = this.next.next();
/*      */         } else {
/*  988 */           this.prev = this.next.prev();
/*      */         }  
/*      */     }
/*      */     public boolean hasNext() {
/*  992 */       return (this.next != null);
/*      */     }
/*      */     public boolean hasPrevious() {
/*  995 */       return (this.prev != null);
/*      */     }
/*      */     void updateNext() {
/*  998 */       this.next = this.next.next();
/*      */     }
/*      */     Char2ReferenceRBTreeMap.Entry<V> nextEntry() {
/* 1001 */       if (!hasNext())
/* 1002 */         throw new NoSuchElementException(); 
/* 1003 */       this.curr = this.prev = this.next;
/* 1004 */       this.index++;
/* 1005 */       updateNext();
/* 1006 */       return this.curr;
/*      */     }
/*      */     void updatePrevious() {
/* 1009 */       this.prev = this.prev.prev();
/*      */     }
/*      */     Char2ReferenceRBTreeMap.Entry<V> previousEntry() {
/* 1012 */       if (!hasPrevious())
/* 1013 */         throw new NoSuchElementException(); 
/* 1014 */       this.curr = this.next = this.prev;
/* 1015 */       this.index--;
/* 1016 */       updatePrevious();
/* 1017 */       return this.curr;
/*      */     }
/*      */     public int nextIndex() {
/* 1020 */       return this.index;
/*      */     }
/*      */     public int previousIndex() {
/* 1023 */       return this.index - 1;
/*      */     }
/*      */     public void remove() {
/* 1026 */       if (this.curr == null) {
/* 1027 */         throw new IllegalStateException();
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1032 */       if (this.curr == this.prev)
/* 1033 */         this.index--; 
/* 1034 */       this.next = this.prev = this.curr;
/* 1035 */       updatePrevious();
/* 1036 */       updateNext();
/* 1037 */       Char2ReferenceRBTreeMap.this.remove(this.curr.key);
/* 1038 */       this.curr = null;
/*      */     }
/*      */     public int skip(int n) {
/* 1041 */       int i = n;
/* 1042 */       while (i-- != 0 && hasNext())
/* 1043 */         nextEntry(); 
/* 1044 */       return n - i - 1;
/*      */     }
/*      */     public int back(int n) {
/* 1047 */       int i = n;
/* 1048 */       while (i-- != 0 && hasPrevious())
/* 1049 */         previousEntry(); 
/* 1050 */       return n - i - 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private class EntryIterator
/*      */     extends TreeIterator
/*      */     implements ObjectListIterator<Char2ReferenceMap.Entry<V>>
/*      */   {
/*      */     EntryIterator() {}
/*      */ 
/*      */     
/*      */     EntryIterator(char k) {
/* 1063 */       super(k);
/*      */     }
/*      */     
/*      */     public Char2ReferenceMap.Entry<V> next() {
/* 1067 */       return nextEntry();
/*      */     }
/*      */     
/*      */     public Char2ReferenceMap.Entry<V> previous() {
/* 1071 */       return previousEntry();
/*      */     }
/*      */   }
/*      */   
/*      */   public ObjectSortedSet<Char2ReferenceMap.Entry<V>> char2ReferenceEntrySet() {
/* 1076 */     if (this.entries == null)
/* 1077 */       this.entries = (ObjectSortedSet<Char2ReferenceMap.Entry<V>>)new AbstractObjectSortedSet<Char2ReferenceMap.Entry<V>>()
/*      */         {
/*      */           final Comparator<? super Char2ReferenceMap.Entry<V>> comparator;
/*      */           
/*      */           public Comparator<? super Char2ReferenceMap.Entry<V>> comparator() {
/* 1082 */             return this.comparator;
/*      */           }
/*      */           
/*      */           public ObjectBidirectionalIterator<Char2ReferenceMap.Entry<V>> iterator() {
/* 1086 */             return (ObjectBidirectionalIterator<Char2ReferenceMap.Entry<V>>)new Char2ReferenceRBTreeMap.EntryIterator();
/*      */           }
/*      */ 
/*      */           
/*      */           public ObjectBidirectionalIterator<Char2ReferenceMap.Entry<V>> iterator(Char2ReferenceMap.Entry<V> from) {
/* 1091 */             return (ObjectBidirectionalIterator<Char2ReferenceMap.Entry<V>>)new Char2ReferenceRBTreeMap.EntryIterator(from.getCharKey());
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean contains(Object o) {
/* 1096 */             if (!(o instanceof Map.Entry))
/* 1097 */               return false; 
/* 1098 */             Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1099 */             if (e.getKey() == null || !(e.getKey() instanceof Character))
/* 1100 */               return false; 
/* 1101 */             Char2ReferenceRBTreeMap.Entry<V> f = Char2ReferenceRBTreeMap.this.findKey(((Character)e.getKey()).charValue());
/* 1102 */             return e.equals(f);
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean remove(Object o) {
/* 1107 */             if (!(o instanceof Map.Entry))
/* 1108 */               return false; 
/* 1109 */             Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1110 */             if (e.getKey() == null || !(e.getKey() instanceof Character))
/* 1111 */               return false; 
/* 1112 */             Char2ReferenceRBTreeMap.Entry<V> f = Char2ReferenceRBTreeMap.this.findKey(((Character)e.getKey()).charValue());
/* 1113 */             if (f == null || f.getValue() != e.getValue())
/* 1114 */               return false; 
/* 1115 */             Char2ReferenceRBTreeMap.this.remove(f.key);
/* 1116 */             return true;
/*      */           }
/*      */           
/*      */           public int size() {
/* 1120 */             return Char2ReferenceRBTreeMap.this.count;
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1124 */             Char2ReferenceRBTreeMap.this.clear();
/*      */           }
/*      */           
/*      */           public Char2ReferenceMap.Entry<V> first() {
/* 1128 */             return Char2ReferenceRBTreeMap.this.firstEntry;
/*      */           }
/*      */           
/*      */           public Char2ReferenceMap.Entry<V> last() {
/* 1132 */             return Char2ReferenceRBTreeMap.this.lastEntry;
/*      */           }
/*      */ 
/*      */           
/*      */           public ObjectSortedSet<Char2ReferenceMap.Entry<V>> subSet(Char2ReferenceMap.Entry<V> from, Char2ReferenceMap.Entry<V> to) {
/* 1137 */             return Char2ReferenceRBTreeMap.this.subMap(from.getCharKey(), to.getCharKey()).char2ReferenceEntrySet();
/*      */           }
/*      */           
/*      */           public ObjectSortedSet<Char2ReferenceMap.Entry<V>> headSet(Char2ReferenceMap.Entry<V> to) {
/* 1141 */             return Char2ReferenceRBTreeMap.this.headMap(to.getCharKey()).char2ReferenceEntrySet();
/*      */           }
/*      */           
/*      */           public ObjectSortedSet<Char2ReferenceMap.Entry<V>> tailSet(Char2ReferenceMap.Entry<V> from) {
/* 1145 */             return Char2ReferenceRBTreeMap.this.tailMap(from.getCharKey()).char2ReferenceEntrySet();
/*      */           }
/*      */         }; 
/* 1148 */     return this.entries;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final class KeyIterator
/*      */     extends TreeIterator
/*      */     implements CharListIterator
/*      */   {
/*      */     public KeyIterator() {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public KeyIterator(char k) {
/* 1164 */       super(k);
/*      */     }
/*      */     
/*      */     public char nextChar() {
/* 1168 */       return (nextEntry()).key;
/*      */     }
/*      */     
/*      */     public char previousChar() {
/* 1172 */       return (previousEntry()).key;
/*      */     } }
/*      */   
/*      */   private class KeySet extends AbstractChar2ReferenceSortedMap<V>.KeySet {
/*      */     private KeySet() {}
/*      */     
/*      */     public CharBidirectionalIterator iterator() {
/* 1179 */       return new Char2ReferenceRBTreeMap.KeyIterator();
/*      */     }
/*      */     
/*      */     public CharBidirectionalIterator iterator(char from) {
/* 1183 */       return new Char2ReferenceRBTreeMap.KeyIterator(from);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CharSortedSet keySet() {
/* 1198 */     if (this.keys == null)
/* 1199 */       this.keys = new KeySet(); 
/* 1200 */     return this.keys;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final class ValueIterator
/*      */     extends TreeIterator
/*      */     implements ObjectListIterator<V>
/*      */   {
/*      */     private ValueIterator() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public V next() {
/* 1215 */       return (nextEntry()).value;
/*      */     }
/*      */     
/*      */     public V previous() {
/* 1219 */       return (previousEntry()).value;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ReferenceCollection<V> values() {
/* 1234 */     if (this.values == null)
/* 1235 */       this.values = (ReferenceCollection<V>)new AbstractReferenceCollection<V>()
/*      */         {
/*      */           public ObjectIterator<V> iterator() {
/* 1238 */             return (ObjectIterator<V>)new Char2ReferenceRBTreeMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public boolean contains(Object k) {
/* 1242 */             return Char2ReferenceRBTreeMap.this.containsValue(k);
/*      */           }
/*      */           
/*      */           public int size() {
/* 1246 */             return Char2ReferenceRBTreeMap.this.count;
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1250 */             Char2ReferenceRBTreeMap.this.clear();
/*      */           }
/*      */         }; 
/* 1253 */     return this.values;
/*      */   }
/*      */   
/*      */   public CharComparator comparator() {
/* 1257 */     return this.actualComparator;
/*      */   }
/*      */   
/*      */   public Char2ReferenceSortedMap<V> headMap(char to) {
/* 1261 */     return new Submap(false, true, to, false);
/*      */   }
/*      */   
/*      */   public Char2ReferenceSortedMap<V> tailMap(char from) {
/* 1265 */     return new Submap(from, false, false, true);
/*      */   }
/*      */   
/*      */   public Char2ReferenceSortedMap<V> subMap(char from, char to) {
/* 1269 */     return new Submap(from, false, to, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final class Submap
/*      */     extends AbstractChar2ReferenceSortedMap<V>
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -7046029254386353129L;
/*      */ 
/*      */ 
/*      */     
/*      */     char from;
/*      */ 
/*      */ 
/*      */     
/*      */     char to;
/*      */ 
/*      */ 
/*      */     
/*      */     boolean bottom;
/*      */ 
/*      */ 
/*      */     
/*      */     boolean top;
/*      */ 
/*      */ 
/*      */     
/*      */     protected transient ObjectSortedSet<Char2ReferenceMap.Entry<V>> entries;
/*      */ 
/*      */ 
/*      */     
/*      */     protected transient CharSortedSet keys;
/*      */ 
/*      */ 
/*      */     
/*      */     protected transient ReferenceCollection<V> values;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Submap(char from, boolean bottom, char to, boolean top) {
/* 1313 */       if (!bottom && !top && Char2ReferenceRBTreeMap.this.compare(from, to) > 0)
/* 1314 */         throw new IllegalArgumentException("Start key (" + from + ") is larger than end key (" + to + ")"); 
/* 1315 */       this.from = from;
/* 1316 */       this.bottom = bottom;
/* 1317 */       this.to = to;
/* 1318 */       this.top = top;
/* 1319 */       this.defRetValue = Char2ReferenceRBTreeMap.this.defRetValue;
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1323 */       SubmapIterator i = new SubmapIterator();
/* 1324 */       while (i.hasNext()) {
/* 1325 */         i.nextEntry();
/* 1326 */         i.remove();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final boolean in(char k) {
/* 1337 */       return ((this.bottom || Char2ReferenceRBTreeMap.this.compare(k, this.from) >= 0) && (this.top || Char2ReferenceRBTreeMap.this
/* 1338 */         .compare(k, this.to) < 0));
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Char2ReferenceMap.Entry<V>> char2ReferenceEntrySet() {
/* 1342 */       if (this.entries == null)
/* 1343 */         this.entries = (ObjectSortedSet<Char2ReferenceMap.Entry<V>>)new AbstractObjectSortedSet<Char2ReferenceMap.Entry<V>>()
/*      */           {
/*      */             public ObjectBidirectionalIterator<Char2ReferenceMap.Entry<V>> iterator() {
/* 1346 */               return (ObjectBidirectionalIterator<Char2ReferenceMap.Entry<V>>)new Char2ReferenceRBTreeMap.Submap.SubmapEntryIterator();
/*      */             }
/*      */ 
/*      */             
/*      */             public ObjectBidirectionalIterator<Char2ReferenceMap.Entry<V>> iterator(Char2ReferenceMap.Entry<V> from) {
/* 1351 */               return (ObjectBidirectionalIterator<Char2ReferenceMap.Entry<V>>)new Char2ReferenceRBTreeMap.Submap.SubmapEntryIterator(from.getCharKey());
/*      */             }
/*      */             
/*      */             public Comparator<? super Char2ReferenceMap.Entry<V>> comparator() {
/* 1355 */               return Char2ReferenceRBTreeMap.this.char2ReferenceEntrySet().comparator();
/*      */             }
/*      */ 
/*      */             
/*      */             public boolean contains(Object o) {
/* 1360 */               if (!(o instanceof Map.Entry))
/* 1361 */                 return false; 
/* 1362 */               Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1363 */               if (e.getKey() == null || !(e.getKey() instanceof Character))
/* 1364 */                 return false; 
/* 1365 */               Char2ReferenceRBTreeMap.Entry<V> f = Char2ReferenceRBTreeMap.this.findKey(((Character)e.getKey()).charValue());
/* 1366 */               return (f != null && Char2ReferenceRBTreeMap.Submap.this.in(f.key) && e.equals(f));
/*      */             }
/*      */ 
/*      */             
/*      */             public boolean remove(Object o) {
/* 1371 */               if (!(o instanceof Map.Entry))
/* 1372 */                 return false; 
/* 1373 */               Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1374 */               if (e.getKey() == null || !(e.getKey() instanceof Character))
/* 1375 */                 return false; 
/* 1376 */               Char2ReferenceRBTreeMap.Entry<V> f = Char2ReferenceRBTreeMap.this.findKey(((Character)e.getKey()).charValue());
/* 1377 */               if (f != null && Char2ReferenceRBTreeMap.Submap.this.in(f.key))
/* 1378 */                 Char2ReferenceRBTreeMap.Submap.this.remove(f.key); 
/* 1379 */               return (f != null);
/*      */             }
/*      */             
/*      */             public int size() {
/* 1383 */               int c = 0;
/* 1384 */               for (ObjectBidirectionalIterator<Char2ReferenceMap.Entry<V>> objectBidirectionalIterator = iterator(); objectBidirectionalIterator.hasNext(); objectBidirectionalIterator.next())
/* 1385 */                 c++; 
/* 1386 */               return c;
/*      */             }
/*      */             
/*      */             public boolean isEmpty() {
/* 1390 */               return !(new Char2ReferenceRBTreeMap.Submap.SubmapIterator()).hasNext();
/*      */             }
/*      */             
/*      */             public void clear() {
/* 1394 */               Char2ReferenceRBTreeMap.Submap.this.clear();
/*      */             }
/*      */             
/*      */             public Char2ReferenceMap.Entry<V> first() {
/* 1398 */               return Char2ReferenceRBTreeMap.Submap.this.firstEntry();
/*      */             }
/*      */             
/*      */             public Char2ReferenceMap.Entry<V> last() {
/* 1402 */               return Char2ReferenceRBTreeMap.Submap.this.lastEntry();
/*      */             }
/*      */ 
/*      */             
/*      */             public ObjectSortedSet<Char2ReferenceMap.Entry<V>> subSet(Char2ReferenceMap.Entry<V> from, Char2ReferenceMap.Entry<V> to) {
/* 1407 */               return Char2ReferenceRBTreeMap.Submap.this.subMap(from.getCharKey(), to.getCharKey()).char2ReferenceEntrySet();
/*      */             }
/*      */             
/*      */             public ObjectSortedSet<Char2ReferenceMap.Entry<V>> headSet(Char2ReferenceMap.Entry<V> to) {
/* 1411 */               return Char2ReferenceRBTreeMap.Submap.this.headMap(to.getCharKey()).char2ReferenceEntrySet();
/*      */             }
/*      */             
/*      */             public ObjectSortedSet<Char2ReferenceMap.Entry<V>> tailSet(Char2ReferenceMap.Entry<V> from) {
/* 1415 */               return Char2ReferenceRBTreeMap.Submap.this.tailMap(from.getCharKey()).char2ReferenceEntrySet();
/*      */             }
/*      */           }; 
/* 1418 */       return this.entries;
/*      */     }
/*      */     
/*      */     private class KeySet extends AbstractChar2ReferenceSortedMap<V>.KeySet {
/*      */       public CharBidirectionalIterator iterator() {
/* 1423 */         return new Char2ReferenceRBTreeMap.Submap.SubmapKeyIterator();
/*      */       }
/*      */       private KeySet() {}
/*      */       public CharBidirectionalIterator iterator(char from) {
/* 1427 */         return new Char2ReferenceRBTreeMap.Submap.SubmapKeyIterator(from);
/*      */       }
/*      */     }
/*      */     
/*      */     public CharSortedSet keySet() {
/* 1432 */       if (this.keys == null)
/* 1433 */         this.keys = new KeySet(); 
/* 1434 */       return this.keys;
/*      */     }
/*      */     
/*      */     public ReferenceCollection<V> values() {
/* 1438 */       if (this.values == null)
/* 1439 */         this.values = (ReferenceCollection<V>)new AbstractReferenceCollection<V>()
/*      */           {
/*      */             public ObjectIterator<V> iterator() {
/* 1442 */               return (ObjectIterator<V>)new Char2ReferenceRBTreeMap.Submap.SubmapValueIterator();
/*      */             }
/*      */             
/*      */             public boolean contains(Object k) {
/* 1446 */               return Char2ReferenceRBTreeMap.Submap.this.containsValue(k);
/*      */             }
/*      */             
/*      */             public int size() {
/* 1450 */               return Char2ReferenceRBTreeMap.Submap.this.size();
/*      */             }
/*      */             
/*      */             public void clear() {
/* 1454 */               Char2ReferenceRBTreeMap.Submap.this.clear();
/*      */             }
/*      */           }; 
/* 1457 */       return this.values;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(char k) {
/* 1462 */       return (in(k) && Char2ReferenceRBTreeMap.this.containsKey(k));
/*      */     }
/*      */     
/*      */     public boolean containsValue(Object v) {
/* 1466 */       SubmapIterator i = new SubmapIterator();
/*      */       
/* 1468 */       while (i.hasNext()) {
/* 1469 */         Object ev = (i.nextEntry()).value;
/* 1470 */         if (ev == v)
/* 1471 */           return true; 
/*      */       } 
/* 1473 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public V get(char k) {
/* 1479 */       char kk = k; Char2ReferenceRBTreeMap.Entry<V> e;
/* 1480 */       return (in(kk) && (e = Char2ReferenceRBTreeMap.this.findKey(kk)) != null) ? e.value : this.defRetValue;
/*      */     }
/*      */     
/*      */     public V put(char k, V v) {
/* 1484 */       Char2ReferenceRBTreeMap.this.modified = false;
/* 1485 */       if (!in(k))
/* 1486 */         throw new IllegalArgumentException("Key (" + k + ") out of range [" + (
/* 1487 */             this.bottom ? "-" : String.valueOf(this.from)) + ", " + (this.top ? "-" : String.valueOf(this.to)) + ")"); 
/* 1488 */       V oldValue = Char2ReferenceRBTreeMap.this.put(k, v);
/* 1489 */       return Char2ReferenceRBTreeMap.this.modified ? this.defRetValue : oldValue;
/*      */     }
/*      */ 
/*      */     
/*      */     public V remove(char k) {
/* 1494 */       Char2ReferenceRBTreeMap.this.modified = false;
/* 1495 */       if (!in(k))
/* 1496 */         return this.defRetValue; 
/* 1497 */       V oldValue = Char2ReferenceRBTreeMap.this.remove(k);
/* 1498 */       return Char2ReferenceRBTreeMap.this.modified ? oldValue : this.defRetValue;
/*      */     }
/*      */     
/*      */     public int size() {
/* 1502 */       SubmapIterator i = new SubmapIterator();
/* 1503 */       int n = 0;
/* 1504 */       while (i.hasNext()) {
/* 1505 */         n++;
/* 1506 */         i.nextEntry();
/*      */       } 
/* 1508 */       return n;
/*      */     }
/*      */     
/*      */     public boolean isEmpty() {
/* 1512 */       return !(new SubmapIterator()).hasNext();
/*      */     }
/*      */     
/*      */     public CharComparator comparator() {
/* 1516 */       return Char2ReferenceRBTreeMap.this.actualComparator;
/*      */     }
/*      */     
/*      */     public Char2ReferenceSortedMap<V> headMap(char to) {
/* 1520 */       if (this.top)
/* 1521 */         return new Submap(this.from, this.bottom, to, false); 
/* 1522 */       return (Char2ReferenceRBTreeMap.this.compare(to, this.to) < 0) ? new Submap(this.from, this.bottom, to, false) : this;
/*      */     }
/*      */     
/*      */     public Char2ReferenceSortedMap<V> tailMap(char from) {
/* 1526 */       if (this.bottom)
/* 1527 */         return new Submap(from, false, this.to, this.top); 
/* 1528 */       return (Char2ReferenceRBTreeMap.this.compare(from, this.from) > 0) ? new Submap(from, false, this.to, this.top) : this;
/*      */     }
/*      */     
/*      */     public Char2ReferenceSortedMap<V> subMap(char from, char to) {
/* 1532 */       if (this.top && this.bottom)
/* 1533 */         return new Submap(from, false, to, false); 
/* 1534 */       if (!this.top)
/* 1535 */         to = (Char2ReferenceRBTreeMap.this.compare(to, this.to) < 0) ? to : this.to; 
/* 1536 */       if (!this.bottom)
/* 1537 */         from = (Char2ReferenceRBTreeMap.this.compare(from, this.from) > 0) ? from : this.from; 
/* 1538 */       if (!this.top && !this.bottom && from == this.from && to == this.to)
/* 1539 */         return this; 
/* 1540 */       return new Submap(from, false, to, false);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Char2ReferenceRBTreeMap.Entry<V> firstEntry() {
/*      */       Char2ReferenceRBTreeMap.Entry<V> e;
/* 1549 */       if (Char2ReferenceRBTreeMap.this.tree == null) {
/* 1550 */         return null;
/*      */       }
/*      */ 
/*      */       
/* 1554 */       if (this.bottom) {
/* 1555 */         e = Char2ReferenceRBTreeMap.this.firstEntry;
/*      */       } else {
/* 1557 */         e = Char2ReferenceRBTreeMap.this.locateKey(this.from);
/*      */         
/* 1559 */         if (Char2ReferenceRBTreeMap.this.compare(e.key, this.from) < 0) {
/* 1560 */           e = e.next();
/*      */         }
/*      */       } 
/*      */       
/* 1564 */       if (e == null || (!this.top && Char2ReferenceRBTreeMap.this.compare(e.key, this.to) >= 0))
/* 1565 */         return null; 
/* 1566 */       return e;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Char2ReferenceRBTreeMap.Entry<V> lastEntry() {
/*      */       Char2ReferenceRBTreeMap.Entry<V> e;
/* 1575 */       if (Char2ReferenceRBTreeMap.this.tree == null) {
/* 1576 */         return null;
/*      */       }
/*      */ 
/*      */       
/* 1580 */       if (this.top) {
/* 1581 */         e = Char2ReferenceRBTreeMap.this.lastEntry;
/*      */       } else {
/* 1583 */         e = Char2ReferenceRBTreeMap.this.locateKey(this.to);
/*      */         
/* 1585 */         if (Char2ReferenceRBTreeMap.this.compare(e.key, this.to) >= 0) {
/* 1586 */           e = e.prev();
/*      */         }
/*      */       } 
/*      */       
/* 1590 */       if (e == null || (!this.bottom && Char2ReferenceRBTreeMap.this.compare(e.key, this.from) < 0))
/* 1591 */         return null; 
/* 1592 */       return e;
/*      */     }
/*      */     
/*      */     public char firstCharKey() {
/* 1596 */       Char2ReferenceRBTreeMap.Entry<V> e = firstEntry();
/* 1597 */       if (e == null)
/* 1598 */         throw new NoSuchElementException(); 
/* 1599 */       return e.key;
/*      */     }
/*      */     
/*      */     public char lastCharKey() {
/* 1603 */       Char2ReferenceRBTreeMap.Entry<V> e = lastEntry();
/* 1604 */       if (e == null)
/* 1605 */         throw new NoSuchElementException(); 
/* 1606 */       return e.key;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private class SubmapIterator
/*      */       extends Char2ReferenceRBTreeMap<V>.TreeIterator
/*      */     {
/*      */       SubmapIterator() {
/* 1619 */         this.next = Char2ReferenceRBTreeMap.Submap.this.firstEntry();
/*      */       }
/*      */       SubmapIterator(char k) {
/* 1622 */         this();
/* 1623 */         if (this.next != null)
/* 1624 */           if (!Char2ReferenceRBTreeMap.Submap.this.bottom && Char2ReferenceRBTreeMap.this.compare(k, this.next.key) < 0) {
/* 1625 */             this.prev = null;
/* 1626 */           } else if (!Char2ReferenceRBTreeMap.Submap.this.top && Char2ReferenceRBTreeMap.this.compare(k, (this.prev = Char2ReferenceRBTreeMap.Submap.this.lastEntry()).key) >= 0) {
/* 1627 */             this.next = null;
/*      */           } else {
/* 1629 */             this.next = Char2ReferenceRBTreeMap.this.locateKey(k);
/* 1630 */             if (Char2ReferenceRBTreeMap.this.compare(this.next.key, k) <= 0) {
/* 1631 */               this.prev = this.next;
/* 1632 */               this.next = this.next.next();
/*      */             } else {
/* 1634 */               this.prev = this.next.prev();
/*      */             } 
/*      */           }  
/*      */       }
/*      */       
/*      */       void updatePrevious() {
/* 1640 */         this.prev = this.prev.prev();
/* 1641 */         if (!Char2ReferenceRBTreeMap.Submap.this.bottom && this.prev != null && Char2ReferenceRBTreeMap.this.compare(this.prev.key, Char2ReferenceRBTreeMap.Submap.this.from) < 0)
/* 1642 */           this.prev = null; 
/*      */       }
/*      */       
/*      */       void updateNext() {
/* 1646 */         this.next = this.next.next();
/* 1647 */         if (!Char2ReferenceRBTreeMap.Submap.this.top && this.next != null && Char2ReferenceRBTreeMap.this.compare(this.next.key, Char2ReferenceRBTreeMap.Submap.this.to) >= 0)
/* 1648 */           this.next = null; 
/*      */       }
/*      */     }
/*      */     
/*      */     private class SubmapEntryIterator
/*      */       extends SubmapIterator implements ObjectListIterator<Char2ReferenceMap.Entry<V>> {
/*      */       SubmapEntryIterator() {}
/*      */       
/*      */       SubmapEntryIterator(char k) {
/* 1657 */         super(k);
/*      */       }
/*      */       
/*      */       public Char2ReferenceMap.Entry<V> next() {
/* 1661 */         return nextEntry();
/*      */       }
/*      */       
/*      */       public Char2ReferenceMap.Entry<V> previous() {
/* 1665 */         return previousEntry();
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final class SubmapKeyIterator
/*      */       extends SubmapIterator
/*      */       implements CharListIterator
/*      */     {
/*      */       public SubmapKeyIterator() {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public SubmapKeyIterator(char from) {
/* 1683 */         super(from);
/*      */       }
/*      */       
/*      */       public char nextChar() {
/* 1687 */         return (nextEntry()).key;
/*      */       }
/*      */       
/*      */       public char previousChar() {
/* 1691 */         return (previousEntry()).key;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final class SubmapValueIterator
/*      */       extends SubmapIterator
/*      */       implements ObjectListIterator<V>
/*      */     {
/*      */       private SubmapValueIterator() {}
/*      */ 
/*      */ 
/*      */       
/*      */       public V next() {
/* 1707 */         return (nextEntry()).value;
/*      */       }
/*      */       
/*      */       public V previous() {
/* 1711 */         return (previousEntry()).value;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Char2ReferenceRBTreeMap<V> clone() {
/*      */     Char2ReferenceRBTreeMap<V> c;
/*      */     try {
/* 1730 */       c = (Char2ReferenceRBTreeMap<V>)super.clone();
/* 1731 */     } catch (CloneNotSupportedException cantHappen) {
/* 1732 */       throw new InternalError();
/*      */     } 
/* 1734 */     c.keys = null;
/* 1735 */     c.values = null;
/* 1736 */     c.entries = null;
/* 1737 */     c.allocatePaths();
/* 1738 */     if (this.count != 0) {
/*      */       
/* 1740 */       Entry<V> rp = new Entry<>(), rq = new Entry<>();
/* 1741 */       Entry<V> p = rp;
/* 1742 */       rp.left(this.tree);
/* 1743 */       Entry<V> q = rq;
/* 1744 */       rq.pred((Entry<V>)null);
/*      */       while (true) {
/* 1746 */         if (!p.pred()) {
/* 1747 */           Entry<V> e = p.left.clone();
/* 1748 */           e.pred(q.left);
/* 1749 */           e.succ(q);
/* 1750 */           q.left(e);
/* 1751 */           p = p.left;
/* 1752 */           q = q.left;
/*      */         } else {
/* 1754 */           while (p.succ()) {
/* 1755 */             p = p.right;
/* 1756 */             if (p == null) {
/* 1757 */               q.right = null;
/* 1758 */               c.tree = rq.left;
/* 1759 */               c.firstEntry = c.tree;
/* 1760 */               while (c.firstEntry.left != null)
/* 1761 */                 c.firstEntry = c.firstEntry.left; 
/* 1762 */               c.lastEntry = c.tree;
/* 1763 */               while (c.lastEntry.right != null)
/* 1764 */                 c.lastEntry = c.lastEntry.right; 
/* 1765 */               return c;
/*      */             } 
/* 1767 */             q = q.right;
/*      */           } 
/* 1769 */           p = p.right;
/* 1770 */           q = q.right;
/*      */         } 
/* 1772 */         if (!p.succ()) {
/* 1773 */           Entry<V> e = p.right.clone();
/* 1774 */           e.succ(q.right);
/* 1775 */           e.pred(q);
/* 1776 */           q.right(e);
/*      */         } 
/*      */       } 
/*      */     } 
/* 1780 */     return c;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1783 */     int n = this.count;
/* 1784 */     EntryIterator i = new EntryIterator();
/*      */     
/* 1786 */     s.defaultWriteObject();
/* 1787 */     while (n-- != 0) {
/* 1788 */       Entry<V> e = i.nextEntry();
/* 1789 */       s.writeChar(e.key);
/* 1790 */       s.writeObject(e.value);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Entry<V> readTree(ObjectInputStream s, int n, Entry<V> pred, Entry<V> succ) throws IOException, ClassNotFoundException {
/* 1811 */     if (n == 1) {
/* 1812 */       Entry<V> entry = new Entry<>(s.readChar(), (V)s.readObject());
/* 1813 */       entry.pred(pred);
/* 1814 */       entry.succ(succ);
/* 1815 */       entry.black(true);
/* 1816 */       return entry;
/*      */     } 
/* 1818 */     if (n == 2) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1823 */       Entry<V> entry = new Entry<>(s.readChar(), (V)s.readObject());
/* 1824 */       entry.black(true);
/* 1825 */       entry.right(new Entry<>(s.readChar(), (V)s.readObject()));
/* 1826 */       entry.right.pred(entry);
/* 1827 */       entry.pred(pred);
/* 1828 */       entry.right.succ(succ);
/* 1829 */       return entry;
/*      */     } 
/*      */     
/* 1832 */     int rightN = n / 2, leftN = n - rightN - 1;
/* 1833 */     Entry<V> top = new Entry<>();
/* 1834 */     top.left(readTree(s, leftN, pred, top));
/* 1835 */     top.key = s.readChar();
/* 1836 */     top.value = (V)s.readObject();
/* 1837 */     top.black(true);
/* 1838 */     top.right(readTree(s, rightN, top, succ));
/* 1839 */     if (n + 2 == (n + 2 & -(n + 2)))
/* 1840 */       top.right.black(false); 
/* 1841 */     return top;
/*      */   }
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1844 */     s.defaultReadObject();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1849 */     setActualComparator();
/* 1850 */     allocatePaths();
/* 1851 */     if (this.count != 0) {
/* 1852 */       this.tree = readTree(s, this.count, (Entry<V>)null, (Entry<V>)null);
/*      */       
/* 1854 */       Entry<V> e = this.tree;
/* 1855 */       while (e.left() != null)
/* 1856 */         e = e.left(); 
/* 1857 */       this.firstEntry = e;
/* 1858 */       e = this.tree;
/* 1859 */       while (e.right() != null)
/* 1860 */         e = e.right(); 
/* 1861 */       this.lastEntry = e;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\Syz\Downloads\Senura (1).jar!\i\\unimi\dsi\fastutil\chars\Char2ReferenceRBTreeMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */