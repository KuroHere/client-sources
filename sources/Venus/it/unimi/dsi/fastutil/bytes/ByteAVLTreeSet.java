/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.bytes.AbstractByteSortedSet;
import it.unimi.dsi.fastutil.bytes.ByteArrays;
import it.unimi.dsi.fastutil.bytes.ByteBidirectionalIterator;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.bytes.ByteComparator;
import it.unimi.dsi.fastutil.bytes.ByteComparators;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.ByteIterators;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;
import it.unimi.dsi.fastutil.bytes.ByteSortedSet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class ByteAVLTreeSet
extends AbstractByteSortedSet
implements Serializable,
Cloneable,
ByteSortedSet {
    protected transient Entry tree;
    protected int count;
    protected transient Entry firstEntry;
    protected transient Entry lastEntry;
    protected Comparator<? super Byte> storedComparator;
    protected transient ByteComparator actualComparator;
    private static final long serialVersionUID = -7046029254386353130L;
    private transient boolean[] dirPath;
    static final boolean $assertionsDisabled = !ByteAVLTreeSet.class.desiredAssertionStatus();

    public ByteAVLTreeSet() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }

    private void setActualComparator() {
        this.actualComparator = ByteComparators.asByteComparator(this.storedComparator);
    }

    public ByteAVLTreeSet(Comparator<? super Byte> comparator) {
        this();
        this.storedComparator = comparator;
        this.setActualComparator();
    }

    public ByteAVLTreeSet(Collection<? extends Byte> collection) {
        this();
        this.addAll(collection);
    }

    public ByteAVLTreeSet(SortedSet<Byte> sortedSet) {
        this(sortedSet.comparator());
        this.addAll(sortedSet);
    }

    public ByteAVLTreeSet(ByteCollection byteCollection) {
        this();
        this.addAll(byteCollection);
    }

    public ByteAVLTreeSet(ByteSortedSet byteSortedSet) {
        this(byteSortedSet.comparator());
        this.addAll(byteSortedSet);
    }

    public ByteAVLTreeSet(ByteIterator byteIterator) {
        this.allocatePaths();
        while (byteIterator.hasNext()) {
            this.add(byteIterator.nextByte());
        }
    }

    public ByteAVLTreeSet(Iterator<?> iterator2) {
        this(ByteIterators.asByteIterator(iterator2));
    }

    public ByteAVLTreeSet(byte[] byArray, int n, int n2, Comparator<? super Byte> comparator) {
        this(comparator);
        ByteArrays.ensureOffsetLength(byArray, n, n2);
        for (int i = 0; i < n2; ++i) {
            this.add(byArray[n + i]);
        }
    }

    public ByteAVLTreeSet(byte[] byArray, int n, int n2) {
        this(byArray, n, n2, null);
    }

    public ByteAVLTreeSet(byte[] byArray) {
        this();
        int n = byArray.length;
        while (n-- != 0) {
            this.add(byArray[n]);
        }
    }

    public ByteAVLTreeSet(byte[] byArray, Comparator<? super Byte> comparator) {
        this(comparator);
        int n = byArray.length;
        while (n-- != 0) {
            this.add(byArray[n]);
        }
    }

    final int compare(byte by, byte by2) {
        return this.actualComparator == null ? Byte.compare(by, by2) : this.actualComparator.compare(by, by2);
    }

    private Entry findKey(byte by) {
        int n;
        Entry entry = this.tree;
        while (entry != null && (n = this.compare(by, entry.key)) != 0) {
            entry = n < 0 ? entry.left() : entry.right();
        }
        return entry;
    }

    final Entry locateKey(byte by) {
        Entry entry = this.tree;
        Entry entry2 = this.tree;
        int n = 0;
        while (entry != null && (n = this.compare(by, entry.key)) != 0) {
            entry2 = entry;
            entry = n < 0 ? entry.left() : entry.right();
        }
        return n == 0 ? entry : entry2;
    }

    private void allocatePaths() {
        this.dirPath = new boolean[48];
    }

    @Override
    public boolean add(byte by) {
        if (this.tree == null) {
            ++this.count;
            this.lastEntry = this.firstEntry = new Entry(by);
            this.tree = this.firstEntry;
        } else {
            Entry entry = this.tree;
            Entry entry2 = null;
            Entry entry3 = this.tree;
            Entry entry4 = null;
            Entry entry5 = null;
            Entry entry6 = null;
            int n = 0;
            while (true) {
                int n2;
                if ((n2 = this.compare(by, entry.key)) == 0) {
                    return true;
                }
                if (entry.balance() != 0) {
                    n = 0;
                    entry4 = entry2;
                    entry3 = entry;
                }
                if (this.dirPath[n++] = n2 > 0) {
                    if (entry.succ()) {
                        ++this.count;
                        entry5 = new Entry(by);
                        if (entry.right == null) {
                            this.lastEntry = entry5;
                        }
                        entry5.left = entry;
                        entry5.right = entry.right;
                        entry.right(entry5);
                        break;
                    }
                    entry2 = entry;
                    entry = entry.right;
                    continue;
                }
                if (entry.pred()) {
                    ++this.count;
                    entry5 = new Entry(by);
                    if (entry.left == null) {
                        this.firstEntry = entry5;
                    }
                    entry5.right = entry;
                    entry5.left = entry.left;
                    entry.left(entry5);
                    break;
                }
                entry2 = entry;
                entry = entry.left;
            }
            entry = entry3;
            n = 0;
            while (entry != entry5) {
                if (this.dirPath[n]) {
                    entry.incBalance();
                } else {
                    entry.decBalance();
                }
                entry = this.dirPath[n++] ? entry.right : entry.left;
            }
            if (entry3.balance() == -2) {
                Entry entry7 = entry3.left;
                if (entry7.balance() == -1) {
                    entry6 = entry7;
                    if (entry7.succ()) {
                        entry7.succ(true);
                        entry3.pred(entry7);
                    } else {
                        entry3.left = entry7.right;
                    }
                    entry7.right = entry3;
                    entry7.balance(0);
                    entry3.balance(0);
                } else {
                    if (!$assertionsDisabled && entry7.balance() != 1) {
                        throw new AssertionError();
                    }
                    entry6 = entry7.right;
                    entry7.right = entry6.left;
                    entry6.left = entry7;
                    entry3.left = entry6.right;
                    entry6.right = entry3;
                    if (entry6.balance() == -1) {
                        entry7.balance(0);
                        entry3.balance(1);
                    } else if (entry6.balance() == 0) {
                        entry7.balance(0);
                        entry3.balance(0);
                    } else {
                        entry7.balance(-1);
                        entry3.balance(0);
                    }
                    entry6.balance(0);
                    if (entry6.pred()) {
                        entry7.succ(entry6);
                        entry6.pred(true);
                    }
                    if (entry6.succ()) {
                        entry3.pred(entry6);
                        entry6.succ(true);
                    }
                }
            } else if (entry3.balance() == 2) {
                Entry entry8 = entry3.right;
                if (entry8.balance() == 1) {
                    entry6 = entry8;
                    if (entry8.pred()) {
                        entry8.pred(true);
                        entry3.succ(entry8);
                    } else {
                        entry3.right = entry8.left;
                    }
                    entry8.left = entry3;
                    entry8.balance(0);
                    entry3.balance(0);
                } else {
                    if (!$assertionsDisabled && entry8.balance() != -1) {
                        throw new AssertionError();
                    }
                    entry6 = entry8.left;
                    entry8.left = entry6.right;
                    entry6.right = entry8;
                    entry3.right = entry6.left;
                    entry6.left = entry3;
                    if (entry6.balance() == 1) {
                        entry8.balance(0);
                        entry3.balance(-1);
                    } else if (entry6.balance() == 0) {
                        entry8.balance(0);
                        entry3.balance(0);
                    } else {
                        entry8.balance(1);
                        entry3.balance(0);
                    }
                    entry6.balance(0);
                    if (entry6.pred()) {
                        entry3.succ(entry6);
                        entry6.pred(true);
                    }
                    if (entry6.succ()) {
                        entry8.pred(entry6);
                        entry6.succ(true);
                    }
                }
            } else {
                return false;
            }
            if (entry4 == null) {
                this.tree = entry6;
            } else if (entry4.left == entry3) {
                entry4.left = entry6;
            } else {
                entry4.right = entry6;
            }
        }
        return false;
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
    public boolean remove(byte by) {
        Entry entry;
        Entry entry2;
        int n;
        if (this.tree == null) {
            return true;
        }
        Entry entry3 = this.tree;
        Entry entry4 = null;
        boolean bl = false;
        byte by2 = by;
        while ((n = this.compare(by2, entry3.key)) != 0) {
            bl = n > 0;
            if (bl) {
                entry4 = entry3;
                if ((entry3 = entry3.right()) != null) continue;
                return true;
            }
            entry4 = entry3;
            if ((entry3 = entry3.left()) != null) continue;
            return true;
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
        --this.count;
        return false;
    }

    @Override
    public boolean contains(byte by) {
        return this.findKey(by) != null;
    }

    @Override
    public void clear() {
        this.count = 0;
        this.tree = null;
        this.lastEntry = null;
        this.firstEntry = null;
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
    public byte firstByte() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }

    @Override
    public byte lastByte() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }

    @Override
    public ByteBidirectionalIterator iterator() {
        return new SetIterator(this);
    }

    @Override
    public ByteBidirectionalIterator iterator(byte by) {
        return new SetIterator(this, by);
    }

    @Override
    public ByteComparator comparator() {
        return this.actualComparator;
    }

    @Override
    public ByteSortedSet headSet(byte by) {
        return new Subset(this, 0, true, by, false);
    }

    @Override
    public ByteSortedSet tailSet(byte by) {
        return new Subset(this, by, false, 0, true);
    }

    @Override
    public ByteSortedSet subSet(byte by, byte by2) {
        return new Subset(this, by, false, by2, false);
    }

    public Object clone() {
        ByteAVLTreeSet byteAVLTreeSet;
        try {
            byteAVLTreeSet = (ByteAVLTreeSet)super.clone();
        } catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new InternalError();
        }
        byteAVLTreeSet.allocatePaths();
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
                            byteAVLTreeSet.firstEntry = byteAVLTreeSet.tree = entry2.left;
                            while (byteAVLTreeSet.firstEntry.left != null) {
                                byteAVLTreeSet.firstEntry = byteAVLTreeSet.firstEntry.left;
                            }
                            byteAVLTreeSet.lastEntry = byteAVLTreeSet.tree;
                            while (byteAVLTreeSet.lastEntry.right != null) {
                                byteAVLTreeSet.lastEntry = byteAVLTreeSet.lastEntry.right;
                            }
                            return byteAVLTreeSet;
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
        return byteAVLTreeSet;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        int n = this.count;
        SetIterator setIterator = new SetIterator(this);
        objectOutputStream.defaultWriteObject();
        while (n-- != 0) {
            objectOutputStream.writeByte(setIterator.nextByte());
        }
    }

    private Entry readTree(ObjectInputStream objectInputStream, int n, Entry entry, Entry entry2) throws IOException, ClassNotFoundException {
        if (n == 1) {
            Entry entry3 = new Entry(objectInputStream.readByte());
            entry3.pred(entry);
            entry3.succ(entry2);
            return entry3;
        }
        if (n == 2) {
            Entry entry4 = new Entry(objectInputStream.readByte());
            entry4.right(new Entry(objectInputStream.readByte()));
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
        entry5.key = objectInputStream.readByte();
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
    public ByteIterator iterator() {
        return this.iterator();
    }

    @Override
    public Iterator iterator() {
        return this.iterator();
    }

    @Override
    public Comparator comparator() {
        return this.comparator();
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    private final class Subset
    extends AbstractByteSortedSet
    implements Serializable,
    ByteSortedSet {
        private static final long serialVersionUID = -7046029254386353129L;
        byte from;
        byte to;
        boolean bottom;
        boolean top;
        final ByteAVLTreeSet this$0;

        public Subset(ByteAVLTreeSet byteAVLTreeSet, byte by, boolean bl, byte by2, boolean bl2) {
            this.this$0 = byteAVLTreeSet;
            if (!bl && !bl2 && byteAVLTreeSet.compare(by, by2) > 0) {
                throw new IllegalArgumentException("Start element (" + by + ") is larger than end element (" + by2 + ")");
            }
            this.from = by;
            this.bottom = bl;
            this.to = by2;
            this.top = bl2;
        }

        @Override
        public void clear() {
            SubsetIterator subsetIterator = new SubsetIterator(this);
            while (subsetIterator.hasNext()) {
                subsetIterator.nextByte();
                subsetIterator.remove();
            }
        }

        final boolean in(byte by) {
            return !(!this.bottom && this.this$0.compare(by, this.from) < 0 || !this.top && this.this$0.compare(by, this.to) >= 0);
        }

        @Override
        public boolean contains(byte by) {
            return this.in(by) && this.this$0.contains(by);
        }

        @Override
        public boolean add(byte by) {
            if (!this.in(by)) {
                throw new IllegalArgumentException("Element (" + by + ") out of range [" + (this.bottom ? "-" : String.valueOf(this.from)) + ", " + (this.top ? "-" : String.valueOf(this.to)) + ")");
            }
            return this.this$0.add(by);
        }

        @Override
        public boolean remove(byte by) {
            if (!this.in(by)) {
                return true;
            }
            return this.this$0.remove(by);
        }

        @Override
        public int size() {
            SubsetIterator subsetIterator = new SubsetIterator(this);
            int n = 0;
            while (subsetIterator.hasNext()) {
                ++n;
                subsetIterator.nextByte();
            }
            return n;
        }

        @Override
        public boolean isEmpty() {
            return !new SubsetIterator(this).hasNext();
        }

        @Override
        public ByteComparator comparator() {
            return this.this$0.actualComparator;
        }

        @Override
        public ByteBidirectionalIterator iterator() {
            return new SubsetIterator(this);
        }

        @Override
        public ByteBidirectionalIterator iterator(byte by) {
            return new SubsetIterator(this, by);
        }

        @Override
        public ByteSortedSet headSet(byte by) {
            if (this.top) {
                return new Subset(this.this$0, this.from, this.bottom, by, false);
            }
            return this.this$0.compare(by, this.to) < 0 ? new Subset(this.this$0, this.from, this.bottom, by, false) : this;
        }

        @Override
        public ByteSortedSet tailSet(byte by) {
            if (this.bottom) {
                return new Subset(this.this$0, by, false, this.to, this.top);
            }
            return this.this$0.compare(by, this.from) > 0 ? new Subset(this.this$0, by, false, this.to, this.top) : this;
        }

        @Override
        public ByteSortedSet subSet(byte by, byte by2) {
            if (this.top && this.bottom) {
                return new Subset(this.this$0, by, false, by2, false);
            }
            if (!this.top) {
                byte by3 = by2 = this.this$0.compare(by2, this.to) < 0 ? by2 : this.to;
            }
            if (!this.bottom) {
                byte by4 = by = this.this$0.compare(by, this.from) > 0 ? by : this.from;
            }
            if (!this.top && !this.bottom && by == this.from && by2 == this.to) {
                return this;
            }
            return new Subset(this.this$0, by, false, by2, false);
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
        public byte firstByte() {
            Entry entry = this.firstEntry();
            if (entry == null) {
                throw new NoSuchElementException();
            }
            return entry.key;
        }

        @Override
        public byte lastByte() {
            Entry entry = this.lastEntry();
            if (entry == null) {
                throw new NoSuchElementException();
            }
            return entry.key;
        }

        @Override
        public ByteIterator iterator() {
            return this.iterator();
        }

        @Override
        public Iterator iterator() {
            return this.iterator();
        }

        @Override
        public Comparator comparator() {
            return this.comparator();
        }

        private final class SubsetIterator
        extends SetIterator {
            final Subset this$1;

            SubsetIterator(Subset subset) {
                this.this$1 = subset;
                super(subset.this$0);
                this.next = subset.firstEntry();
            }

            /*
             * Enabled aggressive block sorting
             */
            SubsetIterator(Subset subset, byte by) {
                this(subset);
                if (this.next == null) return;
                if (!subset.bottom && subset.this$0.compare(by, this.next.key) < 0) {
                    this.prev = null;
                    return;
                }
                if (!subset.top) {
                    this.prev = subset.lastEntry();
                    if (subset.this$0.compare(by, this.prev.key) >= 0) {
                        this.next = null;
                        return;
                    }
                }
                this.next = subset.this$0.locateKey(by);
                if (subset.this$0.compare(this.next.key, by) <= 0) {
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
    }

    private class SetIterator
    implements ByteListIterator {
        Entry prev;
        Entry next;
        Entry curr;
        int index;
        final ByteAVLTreeSet this$0;

        SetIterator(ByteAVLTreeSet byteAVLTreeSet) {
            this.this$0 = byteAVLTreeSet;
            this.index = 0;
            this.next = byteAVLTreeSet.firstEntry;
        }

        SetIterator(ByteAVLTreeSet byteAVLTreeSet, byte by) {
            this.this$0 = byteAVLTreeSet;
            this.index = 0;
            this.next = byteAVLTreeSet.locateKey(by);
            if (this.next != null) {
                if (byteAVLTreeSet.compare(this.next.key, by) <= 0) {
                    this.prev = this.next;
                    this.next = this.next.next();
                } else {
                    this.prev = this.next.prev();
                }
            }
        }

        @Override
        public boolean hasNext() {
            return this.next != null;
        }

        @Override
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

        @Override
        public byte nextByte() {
            return this.nextEntry().key;
        }

        @Override
        public byte previousByte() {
            return this.previousEntry().key;
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

        @Override
        public int nextIndex() {
            return this.index;
        }

        @Override
        public int previousIndex() {
            return this.index - 1;
        }

        @Override
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
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    private static final class Entry
    implements Cloneable {
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        private static final int PRED_MASK = 0x40000000;
        private static final int BALANCE_MASK = 255;
        byte key;
        Entry left;
        Entry right;
        int info;

        Entry() {
        }

        Entry(byte by) {
            this.key = by;
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

        public Entry clone() {
            Entry entry;
            try {
                entry = (Entry)super.clone();
            } catch (CloneNotSupportedException cloneNotSupportedException) {
                throw new InternalError();
            }
            entry.key = this.key;
            entry.info = this.info;
            return entry;
        }

        public boolean equals(Object object) {
            if (!(object instanceof Entry)) {
                return true;
            }
            Entry entry = (Entry)object;
            return this.key == entry.key;
        }

        public int hashCode() {
            return this.key;
        }

        public String toString() {
            return String.valueOf(this.key);
        }

        public Object clone() throws CloneNotSupportedException {
            return this.clone();
        }
    }
}

