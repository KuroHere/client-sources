/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;
import it.unimi.dsi.fastutil.chars.AbstractChar2ByteMap;
import it.unimi.dsi.fastutil.chars.AbstractChar2ByteSortedMap;
import it.unimi.dsi.fastutil.chars.Char2ByteMap;
import it.unimi.dsi.fastutil.chars.Char2ByteSortedMap;
import it.unimi.dsi.fastutil.chars.CharBidirectionalIterator;
import it.unimi.dsi.fastutil.chars.CharComparator;
import it.unimi.dsi.fastutil.chars.CharComparators;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.CharListIterator;
import it.unimi.dsi.fastutil.chars.CharSet;
import it.unimi.dsi.fastutil.chars.CharSortedSet;
import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class Char2ByteAVLTreeMap
extends AbstractChar2ByteSortedMap
implements Serializable,
Cloneable {
    protected transient Entry tree;
    protected int count;
    protected transient Entry firstEntry;
    protected transient Entry lastEntry;
    protected transient ObjectSortedSet<Char2ByteMap.Entry> entries;
    protected transient CharSortedSet keys;
    protected transient ByteCollection values;
    protected transient boolean modified;
    protected Comparator<? super Character> storedComparator;
    protected transient CharComparator actualComparator;
    private static final long serialVersionUID = -7046029254386353129L;
    private transient boolean[] dirPath;
    static final boolean $assertionsDisabled = !Char2ByteAVLTreeMap.class.desiredAssertionStatus();

    public Char2ByteAVLTreeMap() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }

    private void setActualComparator() {
        this.actualComparator = CharComparators.asCharComparator(this.storedComparator);
    }

    public Char2ByteAVLTreeMap(Comparator<? super Character> comparator) {
        this();
        this.storedComparator = comparator;
        this.setActualComparator();
    }

    public Char2ByteAVLTreeMap(Map<? extends Character, ? extends Byte> map) {
        this();
        this.putAll(map);
    }

    public Char2ByteAVLTreeMap(SortedMap<Character, Byte> sortedMap) {
        this(sortedMap.comparator());
        this.putAll((Map<? extends Character, ? extends Byte>)sortedMap);
    }

    public Char2ByteAVLTreeMap(Char2ByteMap char2ByteMap) {
        this();
        this.putAll(char2ByteMap);
    }

    public Char2ByteAVLTreeMap(Char2ByteSortedMap char2ByteSortedMap) {
        this(char2ByteSortedMap.comparator());
        this.putAll(char2ByteSortedMap);
    }

    public Char2ByteAVLTreeMap(char[] cArray, byte[] byArray, Comparator<? super Character> comparator) {
        this(comparator);
        if (cArray.length != byArray.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + cArray.length + " and " + byArray.length + ")");
        }
        for (int i = 0; i < cArray.length; ++i) {
            this.put(cArray[i], byArray[i]);
        }
    }

    public Char2ByteAVLTreeMap(char[] cArray, byte[] byArray) {
        this(cArray, byArray, null);
    }

    final int compare(char c, char c2) {
        return this.actualComparator == null ? Character.compare(c, c2) : this.actualComparator.compare(c, c2);
    }

    final Entry findKey(char c) {
        int n;
        Entry entry = this.tree;
        while (entry != null && (n = this.compare(c, entry.key)) != 0) {
            entry = n < 0 ? entry.left() : entry.right();
        }
        return entry;
    }

    final Entry locateKey(char c) {
        Entry entry = this.tree;
        Entry entry2 = this.tree;
        int n = 0;
        while (entry != null && (n = this.compare(c, entry.key)) != 0) {
            entry2 = entry;
            entry = n < 0 ? entry.left() : entry.right();
        }
        return n == 0 ? entry : entry2;
    }

    private void allocatePaths() {
        this.dirPath = new boolean[48];
    }

    public byte addTo(char c, byte by) {
        Entry entry = this.add(c);
        byte by2 = entry.value;
        entry.value = (byte)(entry.value + by);
        return by2;
    }

    @Override
    public byte put(char c, byte by) {
        Entry entry = this.add(c);
        byte by2 = entry.value;
        entry.value = by;
        return by2;
    }

    private Entry add(char c) {
        this.modified = false;
        Entry entry = null;
        if (this.tree == null) {
            ++this.count;
            this.lastEntry = this.firstEntry = new Entry(c, this.defRetValue);
            this.tree = this.firstEntry;
            entry = this.firstEntry;
            this.modified = true;
        } else {
            Entry entry2 = this.tree;
            Entry entry3 = null;
            Entry entry4 = this.tree;
            Entry entry5 = null;
            Entry entry6 = null;
            int n = 0;
            while (true) {
                int n2;
                if ((n2 = this.compare(c, entry2.key)) == 0) {
                    return entry2;
                }
                if (entry2.balance() != 0) {
                    n = 0;
                    entry5 = entry3;
                    entry4 = entry2;
                }
                if (this.dirPath[n++] = n2 > 0) {
                    if (entry2.succ()) {
                        ++this.count;
                        entry = new Entry(c, this.defRetValue);
                        this.modified = true;
                        if (entry2.right == null) {
                            this.lastEntry = entry;
                        }
                        entry.left = entry2;
                        entry.right = entry2.right;
                        entry2.right(entry);
                        break;
                    }
                    entry3 = entry2;
                    entry2 = entry2.right;
                    continue;
                }
                if (entry2.pred()) {
                    ++this.count;
                    entry = new Entry(c, this.defRetValue);
                    this.modified = true;
                    if (entry2.left == null) {
                        this.firstEntry = entry;
                    }
                    entry.right = entry2;
                    entry.left = entry2.left;
                    entry2.left(entry);
                    break;
                }
                entry3 = entry2;
                entry2 = entry2.left;
            }
            entry2 = entry4;
            n = 0;
            while (entry2 != entry) {
                if (this.dirPath[n]) {
                    entry2.incBalance();
                } else {
                    entry2.decBalance();
                }
                entry2 = this.dirPath[n++] ? entry2.right : entry2.left;
            }
            if (entry4.balance() == -2) {
                Entry entry7 = entry4.left;
                if (entry7.balance() == -1) {
                    entry6 = entry7;
                    if (entry7.succ()) {
                        entry7.succ(true);
                        entry4.pred(entry7);
                    } else {
                        entry4.left = entry7.right;
                    }
                    entry7.right = entry4;
                    entry7.balance(0);
                    entry4.balance(0);
                } else {
                    if (!$assertionsDisabled && entry7.balance() != 1) {
                        throw new AssertionError();
                    }
                    entry6 = entry7.right;
                    entry7.right = entry6.left;
                    entry6.left = entry7;
                    entry4.left = entry6.right;
                    entry6.right = entry4;
                    if (entry6.balance() == -1) {
                        entry7.balance(0);
                        entry4.balance(1);
                    } else if (entry6.balance() == 0) {
                        entry7.balance(0);
                        entry4.balance(0);
                    } else {
                        entry7.balance(-1);
                        entry4.balance(0);
                    }
                    entry6.balance(0);
                    if (entry6.pred()) {
                        entry7.succ(entry6);
                        entry6.pred(true);
                    }
                    if (entry6.succ()) {
                        entry4.pred(entry6);
                        entry6.succ(true);
                    }
                }
            } else if (entry4.balance() == 2) {
                Entry entry8 = entry4.right;
                if (entry8.balance() == 1) {
                    entry6 = entry8;
                    if (entry8.pred()) {
                        entry8.pred(true);
                        entry4.succ(entry8);
                    } else {
                        entry4.right = entry8.left;
                    }
                    entry8.left = entry4;
                    entry8.balance(0);
                    entry4.balance(0);
                } else {
                    if (!$assertionsDisabled && entry8.balance() != -1) {
                        throw new AssertionError();
                    }
                    entry6 = entry8.left;
                    entry8.left = entry6.right;
                    entry6.right = entry8;
                    entry4.right = entry6.left;
                    entry6.left = entry4;
                    if (entry6.balance() == 1) {
                        entry8.balance(0);
                        entry4.balance(-1);
                    } else if (entry6.balance() == 0) {
                        entry8.balance(0);
                        entry4.balance(0);
                    } else {
                        entry8.balance(1);
                        entry4.balance(0);
                    }
                    entry6.balance(0);
                    if (entry6.pred()) {
                        entry4.succ(entry6);
                        entry6.pred(true);
                    }
                    if (entry6.succ()) {
                        entry8.pred(entry6);
                        entry6.succ(true);
                    }
                }
            } else {
                return entry;
            }
            if (entry5 == null) {
                this.tree = entry6;
            } else if (entry5.left == entry4) {
                entry5.left = entry6;
            } else {
                entry5.right = entry6;
            }
        }
        return entry;
    }

    private Entry parent(Entry entry) {
        Entry entry2;
        if (entry == this.tree) {
            return null;
        }
        Entry entry3 = entry2 = entry;
        while (true) {
            if (entry2.succ()) {
                Entry entry4 = entry2.right;
                if (entry4 == null || entry4.left != entry) {
                    while (!entry3.pred()) {
                        entry3 = entry3.left;
                    }
                    entry4 = entry3.left;
                }
                return entry4;
            }
            if (entry3.pred()) {
                Entry entry5 = entry3.left;
                if (entry5 == null || entry5.right != entry) {
                    while (!entry2.succ()) {
                        entry2 = entry2.right;
                    }
                    entry5 = entry2.right;
                }
                return entry5;
            }
            entry3 = entry3.left;
            entry2 = entry2.right;
        }
    }

    @Override
    public byte remove(char c) {
        Entry entry;
        Entry entry2;
        int n;
        this.modified = false;
        if (this.tree == null) {
            return this.defRetValue;
        }
        Entry entry3 = this.tree;
        Entry entry4 = null;
        boolean bl = false;
        char c2 = c;
        while ((n = this.compare(c2, entry3.key)) != 0) {
            bl = n > 0;
            if (bl) {
                entry4 = entry3;
                if ((entry3 = entry3.right()) != null) continue;
                return this.defRetValue;
            }
            entry4 = entry3;
            if ((entry3 = entry3.left()) != null) continue;
            return this.defRetValue;
        }
        if (entry3.left == null) {
            this.firstEntry = entry3.next();
        }
        if (entry3.right == null) {
            this.lastEntry = entry3.prev();
        }
        if (entry3.succ()) {
            if (entry3.pred()) {
                if (entry4 != null) {
                    if (bl) {
                        entry4.succ(entry3.right);
                    } else {
                        entry4.pred(entry3.left);
                    }
                } else {
                    this.tree = bl ? entry3.right : entry3.left;
                }
            } else {
                entry3.prev().right = entry3.right;
                if (entry4 != null) {
                    if (bl) {
                        entry4.right = entry3.left;
                    } else {
                        entry4.left = entry3.left;
                    }
                } else {
                    this.tree = entry3.left;
                }
            }
        } else {
            entry2 = entry3.right;
            if (entry2.pred()) {
                entry2.left = entry3.left;
                entry2.pred(entry3.pred());
                if (!entry2.pred()) {
                    entry2.prev().right = entry2;
                }
                if (entry4 != null) {
                    if (bl) {
                        entry4.right = entry2;
                    } else {
                        entry4.left = entry2;
                    }
                } else {
                    this.tree = entry2;
                }
                entry2.balance(entry3.balance());
                entry4 = entry2;
                bl = true;
            } else {
                while (!(entry = entry2.left).pred()) {
                    entry2 = entry;
                }
                if (entry.succ()) {
                    entry2.pred(entry);
                } else {
                    entry2.left = entry.right;
                }
                entry.left = entry3.left;
                if (!entry3.pred()) {
                    entry3.prev().right = entry;
                    entry.pred(true);
                }
                entry.right = entry3.right;
                entry.succ(true);
                if (entry4 != null) {
                    if (bl) {
                        entry4.right = entry;
                    } else {
                        entry4.left = entry;
                    }
                } else {
                    this.tree = entry;
                }
                entry.balance(entry3.balance());
                entry4 = entry2;
                bl = false;
            }
        }
        while (entry4 != null) {
            Entry entry5;
            entry2 = entry4;
            entry4 = this.parent(entry2);
            if (!bl) {
                bl = entry4 != null && entry4.left != entry2;
                entry2.incBalance();
                if (entry2.balance() == 1) break;
                if (entry2.balance() != 2) continue;
                entry = entry2.right;
                if (!$assertionsDisabled && entry == null) {
                    throw new AssertionError();
                }
                if (entry.balance() == -1) {
                    if (!$assertionsDisabled && entry.balance() != -1) {
                        throw new AssertionError();
                    }
                    entry5 = entry.left;
                    entry.left = entry5.right;
                    entry5.right = entry;
                    entry2.right = entry5.left;
                    entry5.left = entry2;
                    if (entry5.balance() == 1) {
                        entry.balance(0);
                        entry2.balance(-1);
                    } else if (entry5.balance() == 0) {
                        entry.balance(0);
                        entry2.balance(0);
                    } else {
                        if (!$assertionsDisabled && entry5.balance() != -1) {
                            throw new AssertionError();
                        }
                        entry.balance(1);
                        entry2.balance(0);
                    }
                    entry5.balance(0);
                    if (entry5.pred()) {
                        entry2.succ(entry5);
                        entry5.pred(true);
                    }
                    if (entry5.succ()) {
                        entry.pred(entry5);
                        entry5.succ(true);
                    }
                    if (entry4 != null) {
                        if (bl) {
                            entry4.right = entry5;
                            continue;
                        }
                        entry4.left = entry5;
                        continue;
                    }
                    this.tree = entry5;
                    continue;
                }
                if (entry4 != null) {
                    if (bl) {
                        entry4.right = entry;
                    } else {
                        entry4.left = entry;
                    }
                } else {
                    this.tree = entry;
                }
                if (entry.balance() == 0) {
                    entry2.right = entry.left;
                    entry.left = entry2;
                    entry.balance(-1);
                    entry2.balance(1);
                    break;
                }
                if (!$assertionsDisabled && entry.balance() != 1) {
                    throw new AssertionError();
                }
                if (entry.pred()) {
                    entry2.succ(false);
                    entry.pred(true);
                } else {
                    entry2.right = entry.left;
                }
                entry.left = entry2;
                entry2.balance(0);
                entry.balance(0);
                continue;
            }
            bl = entry4 != null && entry4.left != entry2;
            entry2.decBalance();
            if (entry2.balance() == -1) break;
            if (entry2.balance() != -2) continue;
            entry = entry2.left;
            if (!$assertionsDisabled && entry == null) {
                throw new AssertionError();
            }
            if (entry.balance() == 1) {
                if (!$assertionsDisabled && entry.balance() != 1) {
                    throw new AssertionError();
                }
                entry5 = entry.right;
                entry.right = entry5.left;
                entry5.left = entry;
                entry2.left = entry5.right;
                entry5.right = entry2;
                if (entry5.balance() == -1) {
                    entry.balance(0);
                    entry2.balance(1);
                } else if (entry5.balance() == 0) {
                    entry.balance(0);
                    entry2.balance(0);
                } else {
                    if (!$assertionsDisabled && entry5.balance() != 1) {
                        throw new AssertionError();
                    }
                    entry.balance(-1);
                    entry2.balance(0);
                }
                entry5.balance(0);
                if (entry5.pred()) {
                    entry.succ(entry5);
                    entry5.pred(true);
                }
                if (entry5.succ()) {
                    entry2.pred(entry5);
                    entry5.succ(true);
                }
                if (entry4 != null) {
                    if (bl) {
                        entry4.right = entry5;
                        continue;
                    }
                    entry4.left = entry5;
                    continue;
                }
                this.tree = entry5;
                continue;
            }
            if (entry4 != null) {
                if (bl) {
                    entry4.right = entry;
                } else {
                    entry4.left = entry;
                }
            } else {
                this.tree = entry;
            }
            if (entry.balance() == 0) {
                entry2.left = entry.right;
                entry.right = entry2;
                entry.balance(1);
                entry2.balance(-1);
                break;
            }
            if (!$assertionsDisabled && entry.balance() != -1) {
                throw new AssertionError();
            }
            if (entry.succ()) {
                entry2.pred(false);
                entry.succ(true);
            } else {
                entry2.left = entry.right;
            }
            entry.right = entry2;
            entry2.balance(0);
            entry.balance(0);
        }
        this.modified = true;
        --this.count;
        return entry3.value;
    }

    @Override
    public boolean containsValue(byte by) {
        ValueIterator valueIterator = new ValueIterator(this, null);
        int n = this.count;
        while (n-- != 0) {
            byte by2 = valueIterator.nextByte();
            if (by2 != by) continue;
            return false;
        }
        return true;
    }

    @Override
    public void clear() {
        this.count = 0;
        this.tree = null;
        this.entries = null;
        this.values = null;
        this.keys = null;
        this.lastEntry = null;
        this.firstEntry = null;
    }

    @Override
    public boolean containsKey(char c) {
        return this.findKey(c) != null;
    }

    @Override
    public int size() {
        return this.count;
    }

    @Override
    public boolean isEmpty() {
        return this.count == 0;
    }

    @Override
    public byte get(char c) {
        Entry entry = this.findKey(c);
        return entry == null ? this.defRetValue : entry.value;
    }

    @Override
    public char firstCharKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }

    @Override
    public char lastCharKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }

    @Override
    public ObjectSortedSet<Char2ByteMap.Entry> char2ByteEntrySet() {
        if (this.entries == null) {
            this.entries = new AbstractObjectSortedSet<Char2ByteMap.Entry>(this){
                final Comparator<? super Char2ByteMap.Entry> comparator;
                final Char2ByteAVLTreeMap this$0;
                {
                    this.this$0 = char2ByteAVLTreeMap;
                    this.comparator = this::lambda$$0;
                }

                @Override
                public Comparator<? super Char2ByteMap.Entry> comparator() {
                    return this.comparator;
                }

                @Override
                public ObjectBidirectionalIterator<Char2ByteMap.Entry> iterator() {
                    return new EntryIterator(this.this$0);
                }

                @Override
                public ObjectBidirectionalIterator<Char2ByteMap.Entry> iterator(Char2ByteMap.Entry entry) {
                    return new EntryIterator(this.this$0, entry.getCharKey());
                }

                @Override
                public boolean contains(Object object) {
                    if (!(object instanceof Map.Entry)) {
                        return true;
                    }
                    Map.Entry entry = (Map.Entry)object;
                    if (entry.getKey() == null || !(entry.getKey() instanceof Character)) {
                        return true;
                    }
                    if (entry.getValue() == null || !(entry.getValue() instanceof Byte)) {
                        return true;
                    }
                    Entry entry2 = this.this$0.findKey(((Character)entry.getKey()).charValue());
                    return entry.equals(entry2);
                }

                @Override
                public boolean remove(Object object) {
                    if (!(object instanceof Map.Entry)) {
                        return true;
                    }
                    Map.Entry entry = (Map.Entry)object;
                    if (entry.getKey() == null || !(entry.getKey() instanceof Character)) {
                        return true;
                    }
                    if (entry.getValue() == null || !(entry.getValue() instanceof Byte)) {
                        return true;
                    }
                    Entry entry2 = this.this$0.findKey(((Character)entry.getKey()).charValue());
                    if (entry2 == null || entry2.getByteValue() != ((Byte)entry.getValue()).byteValue()) {
                        return true;
                    }
                    this.this$0.remove(entry2.key);
                    return false;
                }

                @Override
                public int size() {
                    return this.this$0.count;
                }

                @Override
                public void clear() {
                    this.this$0.clear();
                }

                @Override
                public Char2ByteMap.Entry first() {
                    return this.this$0.firstEntry;
                }

                @Override
                public Char2ByteMap.Entry last() {
                    return this.this$0.lastEntry;
                }

                @Override
                public ObjectSortedSet<Char2ByteMap.Entry> subSet(Char2ByteMap.Entry entry, Char2ByteMap.Entry entry2) {
                    return this.this$0.subMap(entry.getCharKey(), entry2.getCharKey()).char2ByteEntrySet();
                }

                @Override
                public ObjectSortedSet<Char2ByteMap.Entry> headSet(Char2ByteMap.Entry entry) {
                    return this.this$0.headMap(entry.getCharKey()).char2ByteEntrySet();
                }

                @Override
                public ObjectSortedSet<Char2ByteMap.Entry> tailSet(Char2ByteMap.Entry entry) {
                    return this.this$0.tailMap(entry.getCharKey()).char2ByteEntrySet();
                }

                @Override
                public ObjectSortedSet tailSet(Object object) {
                    return this.tailSet((Char2ByteMap.Entry)object);
                }

                @Override
                public ObjectSortedSet headSet(Object object) {
                    return this.headSet((Char2ByteMap.Entry)object);
                }

                @Override
                public ObjectSortedSet subSet(Object object, Object object2) {
                    return this.subSet((Char2ByteMap.Entry)object, (Char2ByteMap.Entry)object2);
                }

                @Override
                public ObjectBidirectionalIterator iterator(Object object) {
                    return this.iterator((Char2ByteMap.Entry)object);
                }

                @Override
                public ObjectIterator iterator() {
                    return this.iterator();
                }

                @Override
                public Iterator iterator() {
                    return this.iterator();
                }

                @Override
                public Object last() {
                    return this.last();
                }

                @Override
                public Object first() {
                    return this.first();
                }

                @Override
                public SortedSet tailSet(Object object) {
                    return this.tailSet((Char2ByteMap.Entry)object);
                }

                @Override
                public SortedSet headSet(Object object) {
                    return this.headSet((Char2ByteMap.Entry)object);
                }

                @Override
                public SortedSet subSet(Object object, Object object2) {
                    return this.subSet((Char2ByteMap.Entry)object, (Char2ByteMap.Entry)object2);
                }

                private int lambda$$0(Char2ByteMap.Entry entry, Char2ByteMap.Entry entry2) {
                    return this.this$0.actualComparator.compare(entry.getCharKey(), entry2.getCharKey());
                }
            };
        }
        return this.entries;
    }

    @Override
    public CharSortedSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet(this, null);
        }
        return this.keys;
    }

    @Override
    public ByteCollection values() {
        if (this.values == null) {
            this.values = new AbstractByteCollection(this){
                final Char2ByteAVLTreeMap this$0;
                {
                    this.this$0 = char2ByteAVLTreeMap;
                }

                @Override
                public ByteIterator iterator() {
                    return new ValueIterator(this.this$0, null);
                }

                @Override
                public boolean contains(byte by) {
                    return this.this$0.containsValue(by);
                }

                @Override
                public int size() {
                    return this.this$0.count;
                }

                @Override
                public void clear() {
                    this.this$0.clear();
                }

                @Override
                public Iterator iterator() {
                    return this.iterator();
                }
            };
        }
        return this.values;
    }

    @Override
    public CharComparator comparator() {
        return this.actualComparator;
    }

    @Override
    public Char2ByteSortedMap headMap(char c) {
        return new Submap(this, '\u0000', true, c, false);
    }

    @Override
    public Char2ByteSortedMap tailMap(char c) {
        return new Submap(this, c, false, '\u0000', true);
    }

    @Override
    public Char2ByteSortedMap subMap(char c, char c2) {
        return new Submap(this, c, false, c2, false);
    }

    public Char2ByteAVLTreeMap clone() {
        Char2ByteAVLTreeMap char2ByteAVLTreeMap;
        try {
            char2ByteAVLTreeMap = (Char2ByteAVLTreeMap)super.clone();
        } catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new InternalError();
        }
        char2ByteAVLTreeMap.keys = null;
        char2ByteAVLTreeMap.values = null;
        char2ByteAVLTreeMap.entries = null;
        char2ByteAVLTreeMap.allocatePaths();
        if (this.count != 0) {
            Entry entry = new Entry();
            Entry entry2 = new Entry();
            Entry entry3 = entry;
            entry.left(this.tree);
            Entry entry4 = entry2;
            entry2.pred(null);
            while (true) {
                Entry entry5;
                if (!entry3.pred()) {
                    entry5 = entry3.left.clone();
                    entry5.pred(entry4.left);
                    entry5.succ(entry4);
                    entry4.left(entry5);
                    entry3 = entry3.left;
                    entry4 = entry4.left;
                } else {
                    while (entry3.succ()) {
                        entry3 = entry3.right;
                        if (entry3 == null) {
                            entry4.right = null;
                            char2ByteAVLTreeMap.firstEntry = char2ByteAVLTreeMap.tree = entry2.left;
                            while (char2ByteAVLTreeMap.firstEntry.left != null) {
                                char2ByteAVLTreeMap.firstEntry = char2ByteAVLTreeMap.firstEntry.left;
                            }
                            char2ByteAVLTreeMap.lastEntry = char2ByteAVLTreeMap.tree;
                            while (char2ByteAVLTreeMap.lastEntry.right != null) {
                                char2ByteAVLTreeMap.lastEntry = char2ByteAVLTreeMap.lastEntry.right;
                            }
                            return char2ByteAVLTreeMap;
                        }
                        entry4 = entry4.right;
                    }
                    entry3 = entry3.right;
                    entry4 = entry4.right;
                }
                if (entry3.succ()) continue;
                entry5 = entry3.right.clone();
                entry5.succ(entry4.right);
                entry5.pred(entry4);
                entry4.right(entry5);
            }
        }
        return char2ByteAVLTreeMap;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        int n = this.count;
        EntryIterator entryIterator = new EntryIterator(this);
        objectOutputStream.defaultWriteObject();
        while (n-- != 0) {
            Entry entry = entryIterator.nextEntry();
            objectOutputStream.writeChar(entry.key);
            objectOutputStream.writeByte(entry.value);
        }
    }

    private Entry readTree(ObjectInputStream objectInputStream, int n, Entry entry, Entry entry2) throws IOException, ClassNotFoundException {
        if (n == 1) {
            Entry entry3 = new Entry(objectInputStream.readChar(), objectInputStream.readByte());
            entry3.pred(entry);
            entry3.succ(entry2);
            return entry3;
        }
        if (n == 2) {
            Entry entry4 = new Entry(objectInputStream.readChar(), objectInputStream.readByte());
            entry4.right(new Entry(objectInputStream.readChar(), objectInputStream.readByte()));
            entry4.right.pred(entry4);
            entry4.balance(1);
            entry4.pred(entry);
            entry4.right.succ(entry2);
            return entry4;
        }
        int n2 = n / 2;
        int n3 = n - n2 - 1;
        Entry entry5 = new Entry();
        entry5.left(this.readTree(objectInputStream, n3, entry, entry5));
        entry5.key = objectInputStream.readChar();
        entry5.value = objectInputStream.readByte();
        entry5.right(this.readTree(objectInputStream, n2, entry5, entry2));
        if (n == (n & -n)) {
            entry5.balance(1);
        }
        return entry5;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.setActualComparator();
        this.allocatePaths();
        if (this.count != 0) {
            Entry entry = this.tree = this.readTree(objectInputStream, this.count, null, null);
            while (entry.left() != null) {
                entry = entry.left();
            }
            this.firstEntry = entry;
            entry = this.tree;
            while (entry.right() != null) {
                entry = entry.right();
            }
            this.lastEntry = entry;
        }
    }

    @Override
    public CharSet keySet() {
        return this.keySet();
    }

    @Override
    public ObjectSet char2ByteEntrySet() {
        return this.char2ByteEntrySet();
    }

    @Override
    public Collection values() {
        return this.values();
    }

    @Override
    public Set keySet() {
        return this.keySet();
    }

    @Override
    public Comparator comparator() {
        return this.comparator();
    }

    public Object clone() throws CloneNotSupportedException {
        return this.clone();
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    private final class Submap
    extends AbstractChar2ByteSortedMap
    implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        char from;
        char to;
        boolean bottom;
        boolean top;
        protected transient ObjectSortedSet<Char2ByteMap.Entry> entries;
        protected transient CharSortedSet keys;
        protected transient ByteCollection values;
        final Char2ByteAVLTreeMap this$0;

        public Submap(Char2ByteAVLTreeMap char2ByteAVLTreeMap, char c, boolean bl, char c2, boolean bl2) {
            this.this$0 = char2ByteAVLTreeMap;
            if (!bl && !bl2 && char2ByteAVLTreeMap.compare(c, c2) > 0) {
                throw new IllegalArgumentException("Start key (" + c + ") is larger than end key (" + c2 + ")");
            }
            this.from = c;
            this.bottom = bl;
            this.to = c2;
            this.top = bl2;
            this.defRetValue = char2ByteAVLTreeMap.defRetValue;
        }

        @Override
        public void clear() {
            SubmapIterator submapIterator = new SubmapIterator(this);
            while (submapIterator.hasNext()) {
                submapIterator.nextEntry();
                submapIterator.remove();
            }
        }

        final boolean in(char c) {
            return !(!this.bottom && this.this$0.compare(c, this.from) < 0 || !this.top && this.this$0.compare(c, this.to) >= 0);
        }

        @Override
        public ObjectSortedSet<Char2ByteMap.Entry> char2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = new AbstractObjectSortedSet<Char2ByteMap.Entry>(this){
                    final Submap this$1;
                    {
                        this.this$1 = submap;
                    }

                    @Override
                    public ObjectBidirectionalIterator<Char2ByteMap.Entry> iterator() {
                        return new SubmapEntryIterator(this.this$1);
                    }

                    @Override
                    public ObjectBidirectionalIterator<Char2ByteMap.Entry> iterator(Char2ByteMap.Entry entry) {
                        return new SubmapEntryIterator(this.this$1, entry.getCharKey());
                    }

                    @Override
                    public Comparator<? super Char2ByteMap.Entry> comparator() {
                        return this.this$1.this$0.char2ByteEntrySet().comparator();
                    }

                    @Override
                    public boolean contains(Object object) {
                        if (!(object instanceof Map.Entry)) {
                            return true;
                        }
                        Map.Entry entry = (Map.Entry)object;
                        if (entry.getKey() == null || !(entry.getKey() instanceof Character)) {
                            return true;
                        }
                        if (entry.getValue() == null || !(entry.getValue() instanceof Byte)) {
                            return true;
                        }
                        Entry entry2 = this.this$1.this$0.findKey(((Character)entry.getKey()).charValue());
                        return entry2 != null && this.this$1.in(entry2.key) && entry.equals(entry2);
                    }

                    @Override
                    public boolean remove(Object object) {
                        if (!(object instanceof Map.Entry)) {
                            return true;
                        }
                        Map.Entry entry = (Map.Entry)object;
                        if (entry.getKey() == null || !(entry.getKey() instanceof Character)) {
                            return true;
                        }
                        if (entry.getValue() == null || !(entry.getValue() instanceof Byte)) {
                            return true;
                        }
                        Entry entry2 = this.this$1.this$0.findKey(((Character)entry.getKey()).charValue());
                        if (entry2 != null && this.this$1.in(entry2.key)) {
                            this.this$1.remove(entry2.key);
                        }
                        return entry2 != null;
                    }

                    @Override
                    public int size() {
                        int n = 0;
                        ObjectIterator objectIterator = this.iterator();
                        while (objectIterator.hasNext()) {
                            ++n;
                            objectIterator.next();
                        }
                        return n;
                    }

                    @Override
                    public boolean isEmpty() {
                        return !new SubmapIterator(this.this$1).hasNext();
                    }

                    @Override
                    public void clear() {
                        this.this$1.clear();
                    }

                    @Override
                    public Char2ByteMap.Entry first() {
                        return this.this$1.firstEntry();
                    }

                    @Override
                    public Char2ByteMap.Entry last() {
                        return this.this$1.lastEntry();
                    }

                    @Override
                    public ObjectSortedSet<Char2ByteMap.Entry> subSet(Char2ByteMap.Entry entry, Char2ByteMap.Entry entry2) {
                        return this.this$1.subMap(entry.getCharKey(), entry2.getCharKey()).char2ByteEntrySet();
                    }

                    @Override
                    public ObjectSortedSet<Char2ByteMap.Entry> headSet(Char2ByteMap.Entry entry) {
                        return this.this$1.headMap(entry.getCharKey()).char2ByteEntrySet();
                    }

                    @Override
                    public ObjectSortedSet<Char2ByteMap.Entry> tailSet(Char2ByteMap.Entry entry) {
                        return this.this$1.tailMap(entry.getCharKey()).char2ByteEntrySet();
                    }

                    @Override
                    public ObjectSortedSet tailSet(Object object) {
                        return this.tailSet((Char2ByteMap.Entry)object);
                    }

                    @Override
                    public ObjectSortedSet headSet(Object object) {
                        return this.headSet((Char2ByteMap.Entry)object);
                    }

                    @Override
                    public ObjectSortedSet subSet(Object object, Object object2) {
                        return this.subSet((Char2ByteMap.Entry)object, (Char2ByteMap.Entry)object2);
                    }

                    @Override
                    public ObjectBidirectionalIterator iterator(Object object) {
                        return this.iterator((Char2ByteMap.Entry)object);
                    }

                    @Override
                    public ObjectIterator iterator() {
                        return this.iterator();
                    }

                    @Override
                    public Iterator iterator() {
                        return this.iterator();
                    }

                    @Override
                    public Object last() {
                        return this.last();
                    }

                    @Override
                    public Object first() {
                        return this.first();
                    }

                    @Override
                    public SortedSet tailSet(Object object) {
                        return this.tailSet((Char2ByteMap.Entry)object);
                    }

                    @Override
                    public SortedSet headSet(Object object) {
                        return this.headSet((Char2ByteMap.Entry)object);
                    }

                    @Override
                    public SortedSet subSet(Object object, Object object2) {
                        return this.subSet((Char2ByteMap.Entry)object, (Char2ByteMap.Entry)object2);
                    }
                };
            }
            return this.entries;
        }

        @Override
        public CharSortedSet keySet() {
            if (this.keys == null) {
                this.keys = new KeySet(this, null);
            }
            return this.keys;
        }

        @Override
        public ByteCollection values() {
            if (this.values == null) {
                this.values = new AbstractByteCollection(this){
                    final Submap this$1;
                    {
                        this.this$1 = submap;
                    }

                    @Override
                    public ByteIterator iterator() {
                        return new SubmapValueIterator(this.this$1, null);
                    }

                    @Override
                    public boolean contains(byte by) {
                        return this.this$1.containsValue(by);
                    }

                    @Override
                    public int size() {
                        return this.this$1.size();
                    }

                    @Override
                    public void clear() {
                        this.this$1.clear();
                    }

                    @Override
                    public Iterator iterator() {
                        return this.iterator();
                    }
                };
            }
            return this.values;
        }

        @Override
        public boolean containsKey(char c) {
            return this.in(c) && this.this$0.containsKey(c);
        }

        @Override
        public boolean containsValue(byte by) {
            SubmapIterator submapIterator = new SubmapIterator(this);
            while (submapIterator.hasNext()) {
                byte by2 = submapIterator.nextEntry().value;
                if (by2 != by) continue;
                return false;
            }
            return true;
        }

        @Override
        public byte get(char c) {
            Entry entry;
            char c2 = c;
            return this.in(c2) && (entry = this.this$0.findKey(c2)) != null ? entry.value : this.defRetValue;
        }

        @Override
        public byte put(char c, byte by) {
            this.this$0.modified = false;
            if (!this.in(c)) {
                throw new IllegalArgumentException("Key (" + c + ") out of range [" + (this.bottom ? "-" : String.valueOf(this.from)) + ", " + (this.top ? "-" : String.valueOf(this.to)) + ")");
            }
            byte by2 = this.this$0.put(c, by);
            return this.this$0.modified ? this.defRetValue : by2;
        }

        @Override
        public byte remove(char c) {
            this.this$0.modified = false;
            if (!this.in(c)) {
                return this.defRetValue;
            }
            byte by = this.this$0.remove(c);
            return this.this$0.modified ? by : this.defRetValue;
        }

        @Override
        public int size() {
            SubmapIterator submapIterator = new SubmapIterator(this);
            int n = 0;
            while (submapIterator.hasNext()) {
                ++n;
                submapIterator.nextEntry();
            }
            return n;
        }

        @Override
        public boolean isEmpty() {
            return !new SubmapIterator(this).hasNext();
        }

        @Override
        public CharComparator comparator() {
            return this.this$0.actualComparator;
        }

        @Override
        public Char2ByteSortedMap headMap(char c) {
            if (this.top) {
                return new Submap(this.this$0, this.from, this.bottom, c, false);
            }
            return this.this$0.compare(c, this.to) < 0 ? new Submap(this.this$0, this.from, this.bottom, c, false) : this;
        }

        @Override
        public Char2ByteSortedMap tailMap(char c) {
            if (this.bottom) {
                return new Submap(this.this$0, c, false, this.to, this.top);
            }
            return this.this$0.compare(c, this.from) > 0 ? new Submap(this.this$0, c, false, this.to, this.top) : this;
        }

        @Override
        public Char2ByteSortedMap subMap(char c, char c2) {
            if (this.top && this.bottom) {
                return new Submap(this.this$0, c, false, c2, false);
            }
            if (!this.top) {
                char c3 = c2 = this.this$0.compare(c2, this.to) < 0 ? c2 : this.to;
            }
            if (!this.bottom) {
                char c4 = c = this.this$0.compare(c, this.from) > 0 ? c : this.from;
            }
            if (!this.top && !this.bottom && c == this.from && c2 == this.to) {
                return this;
            }
            return new Submap(this.this$0, c, false, c2, false);
        }

        public Entry firstEntry() {
            Entry entry;
            if (this.this$0.tree == null) {
                return null;
            }
            if (this.bottom) {
                entry = this.this$0.firstEntry;
            } else {
                entry = this.this$0.locateKey(this.from);
                if (this.this$0.compare(entry.key, this.from) < 0) {
                    entry = entry.next();
                }
            }
            if (entry == null || !this.top && this.this$0.compare(entry.key, this.to) >= 0) {
                return null;
            }
            return entry;
        }

        public Entry lastEntry() {
            Entry entry;
            if (this.this$0.tree == null) {
                return null;
            }
            if (this.top) {
                entry = this.this$0.lastEntry;
            } else {
                entry = this.this$0.locateKey(this.to);
                if (this.this$0.compare(entry.key, this.to) >= 0) {
                    entry = entry.prev();
                }
            }
            if (entry == null || !this.bottom && this.this$0.compare(entry.key, this.from) < 0) {
                return null;
            }
            return entry;
        }

        @Override
        public char firstCharKey() {
            Entry entry = this.firstEntry();
            if (entry == null) {
                throw new NoSuchElementException();
            }
            return entry.key;
        }

        @Override
        public char lastCharKey() {
            Entry entry = this.lastEntry();
            if (entry == null) {
                throw new NoSuchElementException();
            }
            return entry.key;
        }

        @Override
        public CharSet keySet() {
            return this.keySet();
        }

        @Override
        public ObjectSet char2ByteEntrySet() {
            return this.char2ByteEntrySet();
        }

        @Override
        public Collection values() {
            return this.values();
        }

        @Override
        public Set keySet() {
            return this.keySet();
        }

        @Override
        public Comparator comparator() {
            return this.comparator();
        }

        private final class SubmapValueIterator
        extends SubmapIterator
        implements ByteListIterator {
            final Submap this$1;

            private SubmapValueIterator(Submap submap) {
                this.this$1 = submap;
                super(submap);
            }

            @Override
            public byte nextByte() {
                return this.nextEntry().value;
            }

            @Override
            public byte previousByte() {
                return this.previousEntry().value;
            }

            SubmapValueIterator(Submap submap, 1 var2_2) {
                this(submap);
            }
        }

        private final class SubmapKeyIterator
        extends SubmapIterator
        implements CharListIterator {
            final Submap this$1;

            public SubmapKeyIterator(Submap submap) {
                this.this$1 = submap;
                super(submap);
            }

            public SubmapKeyIterator(Submap submap, char c) {
                this.this$1 = submap;
                super(submap, c);
            }

            @Override
            public char nextChar() {
                return this.nextEntry().key;
            }

            @Override
            public char previousChar() {
                return this.previousEntry().key;
            }
        }

        /*
         * Duplicate member names - consider using --renamedupmembers true
         */
        private class SubmapEntryIterator
        extends SubmapIterator
        implements ObjectListIterator<Char2ByteMap.Entry> {
            final Submap this$1;

            SubmapEntryIterator(Submap submap) {
                this.this$1 = submap;
                super(submap);
            }

            SubmapEntryIterator(Submap submap, char c) {
                this.this$1 = submap;
                super(submap, c);
            }

            @Override
            public Char2ByteMap.Entry next() {
                return this.nextEntry();
            }

            @Override
            public Char2ByteMap.Entry previous() {
                return this.previousEntry();
            }

            @Override
            public Object next() {
                return this.next();
            }

            @Override
            public Object previous() {
                return this.previous();
            }
        }

        private class SubmapIterator
        extends TreeIterator {
            final Submap this$1;

            SubmapIterator(Submap submap) {
                this.this$1 = submap;
                super(submap.this$0);
                this.next = submap.firstEntry();
            }

            /*
             * Enabled aggressive block sorting
             */
            SubmapIterator(Submap submap, char c) {
                this(submap);
                if (this.next == null) return;
                if (!submap.bottom && submap.this$0.compare(c, this.next.key) < 0) {
                    this.prev = null;
                    return;
                }
                if (!submap.top) {
                    this.prev = submap.lastEntry();
                    if (submap.this$0.compare(c, this.prev.key) >= 0) {
                        this.next = null;
                        return;
                    }
                }
                this.next = submap.this$0.locateKey(c);
                if (submap.this$0.compare(this.next.key, c) <= 0) {
                    this.prev = this.next;
                    this.next = this.next.next();
                    return;
                }
                this.prev = this.next.prev();
            }

            @Override
            void updatePrevious() {
                this.prev = this.prev.prev();
                if (!this.this$1.bottom && this.prev != null && this.this$1.this$0.compare(this.prev.key, this.this$1.from) < 0) {
                    this.prev = null;
                }
            }

            @Override
            void updateNext() {
                this.next = this.next.next();
                if (!this.this$1.top && this.next != null && this.this$1.this$0.compare(this.next.key, this.this$1.to) >= 0) {
                    this.next = null;
                }
            }
        }

        /*
         * Duplicate member names - consider using --renamedupmembers true
         */
        private class KeySet
        extends AbstractChar2ByteSortedMap.KeySet {
            final Submap this$1;

            private KeySet(Submap submap) {
                this.this$1 = submap;
                super(submap);
            }

            @Override
            public CharBidirectionalIterator iterator() {
                return new SubmapKeyIterator(this.this$1);
            }

            @Override
            public CharBidirectionalIterator iterator(char c) {
                return new SubmapKeyIterator(this.this$1, c);
            }

            @Override
            public CharIterator iterator() {
                return this.iterator();
            }

            @Override
            public Iterator iterator() {
                return this.iterator();
            }

            KeySet(Submap submap, 1 var2_2) {
                this(submap);
            }
        }
    }

    private final class ValueIterator
    extends TreeIterator
    implements ByteListIterator {
        final Char2ByteAVLTreeMap this$0;

        private ValueIterator(Char2ByteAVLTreeMap char2ByteAVLTreeMap) {
            this.this$0 = char2ByteAVLTreeMap;
            super(char2ByteAVLTreeMap);
        }

        @Override
        public byte nextByte() {
            return this.nextEntry().value;
        }

        @Override
        public byte previousByte() {
            return this.previousEntry().value;
        }

        ValueIterator(Char2ByteAVLTreeMap char2ByteAVLTreeMap, 1 var2_2) {
            this(char2ByteAVLTreeMap);
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    private class KeySet
    extends AbstractChar2ByteSortedMap.KeySet {
        final Char2ByteAVLTreeMap this$0;

        private KeySet(Char2ByteAVLTreeMap char2ByteAVLTreeMap) {
            this.this$0 = char2ByteAVLTreeMap;
            super(char2ByteAVLTreeMap);
        }

        @Override
        public CharBidirectionalIterator iterator() {
            return new KeyIterator(this.this$0);
        }

        @Override
        public CharBidirectionalIterator iterator(char c) {
            return new KeyIterator(this.this$0, c);
        }

        @Override
        public CharIterator iterator() {
            return this.iterator();
        }

        @Override
        public Iterator iterator() {
            return this.iterator();
        }

        KeySet(Char2ByteAVLTreeMap char2ByteAVLTreeMap, 1 var2_2) {
            this(char2ByteAVLTreeMap);
        }
    }

    private final class KeyIterator
    extends TreeIterator
    implements CharListIterator {
        final Char2ByteAVLTreeMap this$0;

        public KeyIterator(Char2ByteAVLTreeMap char2ByteAVLTreeMap) {
            this.this$0 = char2ByteAVLTreeMap;
            super(char2ByteAVLTreeMap);
        }

        public KeyIterator(Char2ByteAVLTreeMap char2ByteAVLTreeMap, char c) {
            this.this$0 = char2ByteAVLTreeMap;
            super(char2ByteAVLTreeMap, c);
        }

        @Override
        public char nextChar() {
            return this.nextEntry().key;
        }

        @Override
        public char previousChar() {
            return this.previousEntry().key;
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    private class EntryIterator
    extends TreeIterator
    implements ObjectListIterator<Char2ByteMap.Entry> {
        final Char2ByteAVLTreeMap this$0;

        EntryIterator(Char2ByteAVLTreeMap char2ByteAVLTreeMap) {
            this.this$0 = char2ByteAVLTreeMap;
            super(char2ByteAVLTreeMap);
        }

        EntryIterator(Char2ByteAVLTreeMap char2ByteAVLTreeMap, char c) {
            this.this$0 = char2ByteAVLTreeMap;
            super(char2ByteAVLTreeMap, c);
        }

        @Override
        public Char2ByteMap.Entry next() {
            return this.nextEntry();
        }

        @Override
        public Char2ByteMap.Entry previous() {
            return this.previousEntry();
        }

        @Override
        public void set(Char2ByteMap.Entry entry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(Char2ByteMap.Entry entry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(Object object) {
            this.add((Char2ByteMap.Entry)object);
        }

        @Override
        public void set(Object object) {
            this.set((Char2ByteMap.Entry)object);
        }

        @Override
        public Object next() {
            return this.next();
        }

        @Override
        public Object previous() {
            return this.previous();
        }
    }

    private class TreeIterator {
        Entry prev;
        Entry next;
        Entry curr;
        int index;
        final Char2ByteAVLTreeMap this$0;

        TreeIterator(Char2ByteAVLTreeMap char2ByteAVLTreeMap) {
            this.this$0 = char2ByteAVLTreeMap;
            this.index = 0;
            this.next = char2ByteAVLTreeMap.firstEntry;
        }

        TreeIterator(Char2ByteAVLTreeMap char2ByteAVLTreeMap, char c) {
            this.this$0 = char2ByteAVLTreeMap;
            this.index = 0;
            this.next = char2ByteAVLTreeMap.locateKey(c);
            if (this.next != null) {
                if (char2ByteAVLTreeMap.compare(this.next.key, c) <= 0) {
                    this.prev = this.next;
                    this.next = this.next.next();
                } else {
                    this.prev = this.next.prev();
                }
            }
        }

        public boolean hasNext() {
            return this.next != null;
        }

        public boolean hasPrevious() {
            return this.prev != null;
        }

        void updateNext() {
            this.next = this.next.next();
        }

        Entry nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = this.prev = this.next;
            ++this.index;
            this.updateNext();
            return this.curr;
        }

        void updatePrevious() {
            this.prev = this.prev.prev();
        }

        Entry previousEntry() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = this.next = this.prev;
            --this.index;
            this.updatePrevious();
            return this.curr;
        }

        public int nextIndex() {
            return this.index;
        }

        public int previousIndex() {
            return this.index - 1;
        }

        public void remove() {
            if (this.curr == null) {
                throw new IllegalStateException();
            }
            if (this.curr == this.prev) {
                --this.index;
            }
            this.next = this.prev = this.curr;
            this.updatePrevious();
            this.updateNext();
            this.this$0.remove(this.curr.key);
            this.curr = null;
        }

        public int skip(int n) {
            int n2 = n;
            while (n2-- != 0 && this.hasNext()) {
                this.nextEntry();
            }
            return n - n2 - 1;
        }

        public int back(int n) {
            int n2 = n;
            while (n2-- != 0 && this.hasPrevious()) {
                this.previousEntry();
            }
            return n - n2 - 1;
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    private static final class Entry
    extends AbstractChar2ByteMap.BasicEntry
    implements Cloneable {
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        private static final int PRED_MASK = 0x40000000;
        private static final int BALANCE_MASK = 255;
        Entry left;
        Entry right;
        int info;

        Entry() {
            super('\u0000', (byte)0);
        }

        Entry(char c, byte by) {
            super(c, by);
            this.info = -1073741824;
        }

        Entry left() {
            return (this.info & 0x40000000) != 0 ? null : this.left;
        }

        Entry right() {
            return (this.info & Integer.MIN_VALUE) != 0 ? null : this.right;
        }

        boolean pred() {
            return (this.info & 0x40000000) != 0;
        }

        boolean succ() {
            return (this.info & Integer.MIN_VALUE) != 0;
        }

        void pred(boolean bl) {
            this.info = bl ? (this.info |= 0x40000000) : (this.info &= 0xBFFFFFFF);
        }

        void succ(boolean bl) {
            this.info = bl ? (this.info |= Integer.MIN_VALUE) : (this.info &= Integer.MAX_VALUE);
        }

        void pred(Entry entry) {
            this.info |= 0x40000000;
            this.left = entry;
        }

        void succ(Entry entry) {
            this.info |= Integer.MIN_VALUE;
            this.right = entry;
        }

        void left(Entry entry) {
            this.info &= 0xBFFFFFFF;
            this.left = entry;
        }

        void right(Entry entry) {
            this.info &= Integer.MAX_VALUE;
            this.right = entry;
        }

        int balance() {
            return (byte)this.info;
        }

        void balance(int n) {
            this.info &= 0xFFFFFF00;
            this.info |= n & 0xFF;
        }

        void incBalance() {
            this.info = this.info & 0xFFFFFF00 | (byte)this.info + 1 & 0xFF;
        }

        protected void decBalance() {
            this.info = this.info & 0xFFFFFF00 | (byte)this.info - 1 & 0xFF;
        }

        Entry next() {
            Entry entry = this.right;
            if ((this.info & Integer.MIN_VALUE) == 0) {
                while ((entry.info & 0x40000000) == 0) {
                    entry = entry.left;
                }
            }
            return entry;
        }

        Entry prev() {
            Entry entry = this.left;
            if ((this.info & 0x40000000) == 0) {
                while ((entry.info & Integer.MIN_VALUE) == 0) {
                    entry = entry.right;
                }
            }
            return entry;
        }

        @Override
        public byte setValue(byte by) {
            byte by2 = this.value;
            this.value = by;
            return by2;
        }

        public Entry clone() {
            Entry entry;
            try {
                entry = (Entry)super.clone();
            } catch (CloneNotSupportedException cloneNotSupportedException) {
                throw new InternalError();
            }
            entry.key = this.key;
            entry.value = this.value;
            entry.info = this.info;
            return entry;
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Map.Entry)) {
                return true;
            }
            Map.Entry entry = (Map.Entry)object;
            return this.key == ((Character)entry.getKey()).charValue() && this.value == (Byte)entry.getValue();
        }

        @Override
        public int hashCode() {
            return this.key ^ this.value;
        }

        @Override
        public String toString() {
            return this.key + "=>" + this.value;
        }

        public Object clone() throws CloneNotSupportedException {
            return this.clone();
        }
    }
}

