/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.floats.AbstractFloat2FloatMap;
import it.unimi.dsi.fastutil.floats.AbstractFloat2FloatSortedMap;
import it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
import it.unimi.dsi.fastutil.floats.Float2FloatMap;
import it.unimi.dsi.fastutil.floats.Float2FloatSortedMap;
import it.unimi.dsi.fastutil.floats.FloatBidirectionalIterator;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import it.unimi.dsi.fastutil.floats.FloatComparator;
import it.unimi.dsi.fastutil.floats.FloatComparators;
import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.FloatListIterator;
import it.unimi.dsi.fastutil.floats.FloatSet;
import it.unimi.dsi.fastutil.floats.FloatSortedSet;
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
public class Float2FloatRBTreeMap
extends AbstractFloat2FloatSortedMap
implements Serializable,
Cloneable {
    protected transient Entry tree;
    protected int count;
    protected transient Entry firstEntry;
    protected transient Entry lastEntry;
    protected transient ObjectSortedSet<Float2FloatMap.Entry> entries;
    protected transient FloatSortedSet keys;
    protected transient FloatCollection values;
    protected transient boolean modified;
    protected Comparator<? super Float> storedComparator;
    protected transient FloatComparator actualComparator;
    private static final long serialVersionUID = -7046029254386353129L;
    private transient boolean[] dirPath;
    private transient Entry[] nodePath;

    public Float2FloatRBTreeMap() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }

    private void setActualComparator() {
        this.actualComparator = FloatComparators.asFloatComparator(this.storedComparator);
    }

    public Float2FloatRBTreeMap(Comparator<? super Float> comparator) {
        this();
        this.storedComparator = comparator;
        this.setActualComparator();
    }

    public Float2FloatRBTreeMap(Map<? extends Float, ? extends Float> map) {
        this();
        this.putAll(map);
    }

    public Float2FloatRBTreeMap(SortedMap<Float, Float> sortedMap) {
        this(sortedMap.comparator());
        this.putAll((Map<? extends Float, ? extends Float>)sortedMap);
    }

    public Float2FloatRBTreeMap(Float2FloatMap float2FloatMap) {
        this();
        this.putAll(float2FloatMap);
    }

    public Float2FloatRBTreeMap(Float2FloatSortedMap float2FloatSortedMap) {
        this(float2FloatSortedMap.comparator());
        this.putAll(float2FloatSortedMap);
    }

    public Float2FloatRBTreeMap(float[] fArray, float[] fArray2, Comparator<? super Float> comparator) {
        this(comparator);
        if (fArray.length != fArray2.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + fArray.length + " and " + fArray2.length + ")");
        }
        for (int i = 0; i < fArray.length; ++i) {
            this.put(fArray[i], fArray2[i]);
        }
    }

    public Float2FloatRBTreeMap(float[] fArray, float[] fArray2) {
        this(fArray, fArray2, null);
    }

    final int compare(float f, float f2) {
        return this.actualComparator == null ? Float.compare(f, f2) : this.actualComparator.compare(f, f2);
    }

    final Entry findKey(float f) {
        int n;
        Entry entry = this.tree;
        while (entry != null && (n = this.compare(f, entry.key)) != 0) {
            entry = n < 0 ? entry.left() : entry.right();
        }
        return entry;
    }

    final Entry locateKey(float f) {
        Entry entry = this.tree;
        Entry entry2 = this.tree;
        int n = 0;
        while (entry != null && (n = this.compare(f, entry.key)) != 0) {
            entry2 = entry;
            entry = n < 0 ? entry.left() : entry.right();
        }
        return n == 0 ? entry : entry2;
    }

    private void allocatePaths() {
        this.dirPath = new boolean[64];
        this.nodePath = new Entry[64];
    }

    public float addTo(float f, float f2) {
        Entry entry = this.add(f);
        float f3 = entry.value;
        entry.value += f2;
        return f3;
    }

    @Override
    public float put(float f, float f2) {
        Entry entry = this.add(f);
        float f3 = entry.value;
        entry.value = f2;
        return f3;
    }

    private Entry add(float f) {
        Entry entry;
        this.modified = false;
        int n = 0;
        if (this.tree == null) {
            ++this.count;
            this.lastEntry = this.firstEntry = new Entry(f, this.defRetValue);
            this.tree = this.firstEntry;
            entry = this.firstEntry;
        } else {
            Entry entry2 = this.tree;
            int n2 = 0;
            while (true) {
                int n3;
                if ((n3 = this.compare(f, entry2.key)) == 0) {
                    while (n2-- != 0) {
                        this.nodePath[n2] = null;
                    }
                    return entry2;
                }
                this.nodePath[n2] = entry2;
                this.dirPath[n2++] = n3 > 0;
                if (this.dirPath[n2++]) {
                    if (entry2.succ()) {
                        ++this.count;
                        entry = new Entry(f, this.defRetValue);
                        if (entry2.right == null) {
                            this.lastEntry = entry;
                        }
                        entry.left = entry2;
                        entry.right = entry2.right;
                        entry2.right(entry);
                        break;
                    }
                    entry2 = entry2.right;
                    continue;
                }
                if (entry2.pred()) {
                    ++this.count;
                    entry = new Entry(f, this.defRetValue);
                    if (entry2.left == null) {
                        this.firstEntry = entry;
                    }
                    entry.right = entry2;
                    entry.left = entry2.left;
                    entry2.left(entry);
                    break;
                }
                entry2 = entry2.left;
            }
            this.modified = true;
            n = n2--;
            while (n2 > 0 && !this.nodePath[n2].black()) {
                Entry entry3;
                Entry entry4;
                if (!this.dirPath[n2 - 1]) {
                    entry4 = this.nodePath[n2 - 1].right;
                    if (!this.nodePath[n2 - 1].succ() && !entry4.black()) {
                        this.nodePath[n2].black(false);
                        entry4.black(false);
                        this.nodePath[n2 - 1].black(true);
                        n2 -= 2;
                        continue;
                    }
                    if (!this.dirPath[n2]) {
                        entry4 = this.nodePath[n2];
                    } else {
                        entry3 = this.nodePath[n2];
                        entry4 = entry3.right;
                        entry3.right = entry4.left;
                        entry4.left = entry3;
                        this.nodePath[n2 - 1].left = entry4;
                        if (entry4.pred()) {
                            entry4.pred(true);
                            entry3.succ(entry4);
                        }
                    }
                    entry3 = this.nodePath[n2 - 1];
                    entry3.black(true);
                    entry4.black(false);
                    entry3.left = entry4.right;
                    entry4.right = entry3;
                    if (n2 < 2) {
                        this.tree = entry4;
                    } else if (this.dirPath[n2 - 2]) {
                        this.nodePath[n2 - 2].right = entry4;
                    } else {
                        this.nodePath[n2 - 2].left = entry4;
                    }
                    if (!entry4.succ()) break;
                    entry4.succ(true);
                    entry3.pred(entry4);
                    break;
                }
                entry4 = this.nodePath[n2 - 1].left;
                if (!this.nodePath[n2 - 1].pred() && !entry4.black()) {
                    this.nodePath[n2].black(false);
                    entry4.black(false);
                    this.nodePath[n2 - 1].black(true);
                    n2 -= 2;
                    continue;
                }
                if (this.dirPath[n2]) {
                    entry4 = this.nodePath[n2];
                } else {
                    entry3 = this.nodePath[n2];
                    entry4 = entry3.left;
                    entry3.left = entry4.right;
                    entry4.right = entry3;
                    this.nodePath[n2 - 1].right = entry4;
                    if (entry4.succ()) {
                        entry4.succ(true);
                        entry3.pred(entry4);
                    }
                }
                entry3 = this.nodePath[n2 - 1];
                entry3.black(true);
                entry4.black(false);
                entry3.right = entry4.left;
                entry4.left = entry3;
                if (n2 < 2) {
                    this.tree = entry4;
                } else if (this.dirPath[n2 - 2]) {
                    this.nodePath[n2 - 2].right = entry4;
                } else {
                    this.nodePath[n2 - 2].left = entry4;
                }
                if (!entry4.pred()) break;
                entry4.pred(true);
                entry3.succ(entry4);
                break;
            }
        }
        this.tree.black(false);
        while (n-- != 0) {
            this.nodePath[n] = null;
        }
        return entry;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public float remove(float f) {
        Entry entry;
        int n;
        Entry entry2;
        int n2;
        Entry entry3;
        block68: {
            int n3;
            block74: {
                block69: {
                    block73: {
                        block71: {
                            block72: {
                                block66: {
                                    block67: {
                                        this.modified = false;
                                        if (this.tree == null) {
                                            return this.defRetValue;
                                        }
                                        entry3 = this.tree;
                                        n2 = 0;
                                        float f2 = f;
                                        while (true) {
                                            int n4;
                                            if ((n4 = this.compare(f2, entry3.key)) == 0) {
                                                if (entry3.left != null) break block66;
                                                break block67;
                                            }
                                            this.dirPath[n2] = n4 > 0;
                                            this.nodePath[n2] = entry3;
                                            if (this.dirPath[n2++]) {
                                                if ((entry3 = entry3.right()) != null) continue;
                                                while (true) {
                                                    if (n2-- == 0) {
                                                        return this.defRetValue;
                                                    }
                                                    this.nodePath[n2] = null;
                                                }
                                            }
                                            if ((entry3 = entry3.left()) == null) break;
                                        }
                                        while (true) {
                                            if (n2-- == 0) {
                                                return this.defRetValue;
                                            }
                                            this.nodePath[n2] = null;
                                        }
                                    }
                                    this.firstEntry = entry3.next();
                                }
                                if (entry3.right == null) {
                                    this.lastEntry = entry3.prev();
                                }
                                if (!entry3.succ()) break block71;
                                if (!entry3.pred()) break block72;
                                if (n2 == 0) {
                                    this.tree = entry3.left;
                                    break block68;
                                } else if (this.dirPath[n2 - 1]) {
                                    this.nodePath[n2 - 1].succ(entry3.right);
                                    break block68;
                                } else {
                                    this.nodePath[n2 - 1].pred(entry3.left);
                                }
                                break block68;
                            }
                            entry3.prev().right = entry3.right;
                            if (n2 == 0) {
                                this.tree = entry3.left;
                                break block68;
                            } else if (this.dirPath[n2 - 1]) {
                                this.nodePath[n2 - 1].right = entry3.left;
                                break block68;
                            } else {
                                this.nodePath[n2 - 1].left = entry3.left;
                            }
                            break block68;
                        }
                        entry2 = entry3.right;
                        if (!entry2.pred()) break block73;
                        entry2.left = entry3.left;
                        entry2.pred(entry3.pred());
                        if (!entry2.pred()) {
                            entry2.prev().right = entry2;
                        }
                        if (n2 == 0) {
                            this.tree = entry2;
                        } else if (this.dirPath[n2 - 1]) {
                            this.nodePath[n2 - 1].right = entry2;
                        } else {
                            this.nodePath[n2 - 1].left = entry2;
                        }
                        n = entry2.black() ? 1 : 0;
                        entry2.black(entry3.black());
                        entry3.black(n != 0);
                        this.dirPath[n2] = true;
                        this.nodePath[n2++] = entry2;
                        break block68;
                    }
                    n3 = n2++;
                    while (true) {
                        this.dirPath[n2] = false;
                        this.nodePath[n2++] = entry2;
                        entry = entry2.left;
                        if (entry.pred()) {
                            this.dirPath[n3] = true;
                            this.nodePath[n3] = entry;
                            if (entry.succ()) {
                                break;
                            }
                            break block69;
                        }
                        entry2 = entry;
                    }
                    entry2.pred(entry);
                    break block74;
                }
                entry2.left = entry.right;
            }
            entry.left = entry3.left;
            if (!entry3.pred()) {
                entry3.prev().right = entry;
                entry.pred(true);
            }
            entry.right(entry3.right);
            n = entry.black() ? 1 : 0;
            entry.black(entry3.black());
            entry3.black(n != 0);
            if (n3 == 0) {
                this.tree = entry;
            } else if (this.dirPath[n3 - 1]) {
                this.nodePath[n3 - 1].right = entry;
            } else {
                this.nodePath[n3 - 1].left = entry;
            }
        }
        n = n2;
        if (entry3.black()) {
            while (n2 > 0) {
                block70: {
                    if (this.dirPath[n2 - 1] && !this.nodePath[n2 - 1].succ() || !this.dirPath[n2 - 1] && !this.nodePath[n2 - 1].pred()) {
                        Entry entry4 = entry2 = this.dirPath[n2 - 1] ? this.nodePath[n2 - 1].right : this.nodePath[n2 - 1].left;
                        if (!entry2.black()) {
                            entry2.black(false);
                            break;
                        }
                    }
                    if (!this.dirPath[n2 - 1]) {
                        entry2 = this.nodePath[n2 - 1].right;
                        if (!entry2.black()) {
                            entry2.black(false);
                            this.nodePath[n2 - 1].black(true);
                            this.nodePath[n2 - 1].right = entry2.left;
                            entry2.left = this.nodePath[n2 - 1];
                            if (n2 < 2) {
                                this.tree = entry2;
                            } else if (this.dirPath[n2 - 2]) {
                                this.nodePath[n2 - 2].right = entry2;
                            } else {
                                this.nodePath[n2 - 2].left = entry2;
                            }
                            this.nodePath[n2] = this.nodePath[n2 - 1];
                            this.dirPath[n2] = false;
                            this.nodePath[n2 - 1] = entry2;
                            if (n == n2++) {
                                ++n;
                            }
                            entry2 = this.nodePath[n2 - 1].right;
                        }
                        if ((entry2.pred() || entry2.left.black()) && (entry2.succ() || entry2.right.black())) {
                            entry2.black(true);
                            break block70;
                        } else {
                            if (entry2.succ() || entry2.right.black()) {
                                entry = entry2.left;
                                entry.black(false);
                                entry2.black(true);
                                entry2.left = entry.right;
                                entry.right = entry2;
                                entry2 = this.nodePath[n2 - 1].right = entry;
                                if (entry2.succ()) {
                                    entry2.succ(true);
                                    entry2.right.pred(entry2);
                                }
                            }
                            entry2.black(this.nodePath[n2 - 1].black());
                            this.nodePath[n2 - 1].black(false);
                            entry2.right.black(false);
                            this.nodePath[n2 - 1].right = entry2.left;
                            entry2.left = this.nodePath[n2 - 1];
                            if (n2 < 2) {
                                this.tree = entry2;
                            } else if (this.dirPath[n2 - 2]) {
                                this.nodePath[n2 - 2].right = entry2;
                            } else {
                                this.nodePath[n2 - 2].left = entry2;
                            }
                            if (!entry2.pred()) break;
                            entry2.pred(true);
                            this.nodePath[n2 - 1].succ(entry2);
                            break;
                        }
                    }
                    entry2 = this.nodePath[n2 - 1].left;
                    if (!entry2.black()) {
                        entry2.black(false);
                        this.nodePath[n2 - 1].black(true);
                        this.nodePath[n2 - 1].left = entry2.right;
                        entry2.right = this.nodePath[n2 - 1];
                        if (n2 < 2) {
                            this.tree = entry2;
                        } else if (this.dirPath[n2 - 2]) {
                            this.nodePath[n2 - 2].right = entry2;
                        } else {
                            this.nodePath[n2 - 2].left = entry2;
                        }
                        this.nodePath[n2] = this.nodePath[n2 - 1];
                        this.dirPath[n2] = true;
                        this.nodePath[n2 - 1] = entry2;
                        if (n == n2++) {
                            ++n;
                        }
                        entry2 = this.nodePath[n2 - 1].left;
                    }
                    if ((entry2.pred() || entry2.left.black()) && (entry2.succ() || entry2.right.black())) {
                        entry2.black(true);
                    } else {
                        if (entry2.pred() || entry2.left.black()) {
                            entry = entry2.right;
                            entry.black(false);
                            entry2.black(true);
                            entry2.right = entry.left;
                            entry.left = entry2;
                            entry2 = this.nodePath[n2 - 1].left = entry;
                            if (entry2.pred()) {
                                entry2.pred(true);
                                entry2.left.succ(entry2);
                            }
                        }
                        entry2.black(this.nodePath[n2 - 1].black());
                        this.nodePath[n2 - 1].black(false);
                        entry2.left.black(false);
                        this.nodePath[n2 - 1].left = entry2.right;
                        entry2.right = this.nodePath[n2 - 1];
                        if (n2 < 2) {
                            this.tree = entry2;
                        } else if (this.dirPath[n2 - 2]) {
                            this.nodePath[n2 - 2].right = entry2;
                        } else {
                            this.nodePath[n2 - 2].left = entry2;
                        }
                        if (!entry2.succ()) break;
                        entry2.succ(true);
                        this.nodePath[n2 - 1].pred(entry2);
                        break;
                    }
                }
                --n2;
            }
            if (this.tree != null) {
                this.tree.black(false);
            }
        }
        this.modified = true;
        --this.count;
        while (n-- != 0) {
            this.nodePath[n] = null;
        }
        return entry3.value;
    }

    @Override
    public boolean containsValue(float f) {
        ValueIterator valueIterator = new ValueIterator(this, null);
        int n = this.count;
        while (n-- != 0) {
            float f2 = valueIterator.nextFloat();
            if (Float.floatToIntBits(f2) != Float.floatToIntBits(f)) continue;
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
    public boolean containsKey(float f) {
        return this.findKey(f) != null;
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
    public float get(float f) {
        Entry entry = this.findKey(f);
        return entry == null ? this.defRetValue : entry.value;
    }

    @Override
    public float firstFloatKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }

    @Override
    public float lastFloatKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }

    @Override
    public ObjectSortedSet<Float2FloatMap.Entry> float2FloatEntrySet() {
        if (this.entries == null) {
            this.entries = new AbstractObjectSortedSet<Float2FloatMap.Entry>(this){
                final Comparator<? super Float2FloatMap.Entry> comparator;
                final Float2FloatRBTreeMap this$0;
                {
                    this.this$0 = float2FloatRBTreeMap;
                    this.comparator = this::lambda$$0;
                }

                @Override
                public Comparator<? super Float2FloatMap.Entry> comparator() {
                    return this.comparator;
                }

                @Override
                public ObjectBidirectionalIterator<Float2FloatMap.Entry> iterator() {
                    return new EntryIterator(this.this$0);
                }

                @Override
                public ObjectBidirectionalIterator<Float2FloatMap.Entry> iterator(Float2FloatMap.Entry entry) {
                    return new EntryIterator(this.this$0, entry.getFloatKey());
                }

                @Override
                public boolean contains(Object object) {
                    if (!(object instanceof Map.Entry)) {
                        return true;
                    }
                    Map.Entry entry = (Map.Entry)object;
                    if (entry.getKey() == null || !(entry.getKey() instanceof Float)) {
                        return true;
                    }
                    if (entry.getValue() == null || !(entry.getValue() instanceof Float)) {
                        return true;
                    }
                    Entry entry2 = this.this$0.findKey(((Float)entry.getKey()).floatValue());
                    return entry.equals(entry2);
                }

                @Override
                public boolean remove(Object object) {
                    if (!(object instanceof Map.Entry)) {
                        return true;
                    }
                    Map.Entry entry = (Map.Entry)object;
                    if (entry.getKey() == null || !(entry.getKey() instanceof Float)) {
                        return true;
                    }
                    if (entry.getValue() == null || !(entry.getValue() instanceof Float)) {
                        return true;
                    }
                    Entry entry2 = this.this$0.findKey(((Float)entry.getKey()).floatValue());
                    if (entry2 == null || Float.floatToIntBits(entry2.getFloatValue()) != Float.floatToIntBits(((Float)entry.getValue()).floatValue())) {
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
                public Float2FloatMap.Entry first() {
                    return this.this$0.firstEntry;
                }

                @Override
                public Float2FloatMap.Entry last() {
                    return this.this$0.lastEntry;
                }

                @Override
                public ObjectSortedSet<Float2FloatMap.Entry> subSet(Float2FloatMap.Entry entry, Float2FloatMap.Entry entry2) {
                    return this.this$0.subMap(entry.getFloatKey(), entry2.getFloatKey()).float2FloatEntrySet();
                }

                @Override
                public ObjectSortedSet<Float2FloatMap.Entry> headSet(Float2FloatMap.Entry entry) {
                    return this.this$0.headMap(entry.getFloatKey()).float2FloatEntrySet();
                }

                @Override
                public ObjectSortedSet<Float2FloatMap.Entry> tailSet(Float2FloatMap.Entry entry) {
                    return this.this$0.tailMap(entry.getFloatKey()).float2FloatEntrySet();
                }

                @Override
                public ObjectSortedSet tailSet(Object object) {
                    return this.tailSet((Float2FloatMap.Entry)object);
                }

                @Override
                public ObjectSortedSet headSet(Object object) {
                    return this.headSet((Float2FloatMap.Entry)object);
                }

                @Override
                public ObjectSortedSet subSet(Object object, Object object2) {
                    return this.subSet((Float2FloatMap.Entry)object, (Float2FloatMap.Entry)object2);
                }

                @Override
                public ObjectBidirectionalIterator iterator(Object object) {
                    return this.iterator((Float2FloatMap.Entry)object);
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
                    return this.tailSet((Float2FloatMap.Entry)object);
                }

                @Override
                public SortedSet headSet(Object object) {
                    return this.headSet((Float2FloatMap.Entry)object);
                }

                @Override
                public SortedSet subSet(Object object, Object object2) {
                    return this.subSet((Float2FloatMap.Entry)object, (Float2FloatMap.Entry)object2);
                }

                private int lambda$$0(Float2FloatMap.Entry entry, Float2FloatMap.Entry entry2) {
                    return this.this$0.actualComparator.compare(entry.getFloatKey(), entry2.getFloatKey());
                }
            };
        }
        return this.entries;
    }

    @Override
    public FloatSortedSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet(this, null);
        }
        return this.keys;
    }

    @Override
    public FloatCollection values() {
        if (this.values == null) {
            this.values = new AbstractFloatCollection(this){
                final Float2FloatRBTreeMap this$0;
                {
                    this.this$0 = float2FloatRBTreeMap;
                }

                @Override
                public FloatIterator iterator() {
                    return new ValueIterator(this.this$0, null);
                }

                @Override
                public boolean contains(float f) {
                    return this.this$0.containsValue(f);
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
    public FloatComparator comparator() {
        return this.actualComparator;
    }

    @Override
    public Float2FloatSortedMap headMap(float f) {
        return new Submap(this, 0.0f, true, f, false);
    }

    @Override
    public Float2FloatSortedMap tailMap(float f) {
        return new Submap(this, f, false, 0.0f, true);
    }

    @Override
    public Float2FloatSortedMap subMap(float f, float f2) {
        return new Submap(this, f, false, f2, false);
    }

    public Float2FloatRBTreeMap clone() {
        Float2FloatRBTreeMap float2FloatRBTreeMap;
        try {
            float2FloatRBTreeMap = (Float2FloatRBTreeMap)super.clone();
        } catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new InternalError();
        }
        float2FloatRBTreeMap.keys = null;
        float2FloatRBTreeMap.values = null;
        float2FloatRBTreeMap.entries = null;
        float2FloatRBTreeMap.allocatePaths();
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
                            float2FloatRBTreeMap.firstEntry = float2FloatRBTreeMap.tree = entry2.left;
                            while (float2FloatRBTreeMap.firstEntry.left != null) {
                                float2FloatRBTreeMap.firstEntry = float2FloatRBTreeMap.firstEntry.left;
                            }
                            float2FloatRBTreeMap.lastEntry = float2FloatRBTreeMap.tree;
                            while (float2FloatRBTreeMap.lastEntry.right != null) {
                                float2FloatRBTreeMap.lastEntry = float2FloatRBTreeMap.lastEntry.right;
                            }
                            return float2FloatRBTreeMap;
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
        return float2FloatRBTreeMap;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        int n = this.count;
        EntryIterator entryIterator = new EntryIterator(this);
        objectOutputStream.defaultWriteObject();
        while (n-- != 0) {
            Entry entry = entryIterator.nextEntry();
            objectOutputStream.writeFloat(entry.key);
            objectOutputStream.writeFloat(entry.value);
        }
    }

    private Entry readTree(ObjectInputStream objectInputStream, int n, Entry entry, Entry entry2) throws IOException, ClassNotFoundException {
        if (n == 1) {
            Entry entry3 = new Entry(objectInputStream.readFloat(), objectInputStream.readFloat());
            entry3.pred(entry);
            entry3.succ(entry2);
            entry3.black(false);
            return entry3;
        }
        if (n == 2) {
            Entry entry4 = new Entry(objectInputStream.readFloat(), objectInputStream.readFloat());
            entry4.black(false);
            entry4.right(new Entry(objectInputStream.readFloat(), objectInputStream.readFloat()));
            entry4.right.pred(entry4);
            entry4.pred(entry);
            entry4.right.succ(entry2);
            return entry4;
        }
        int n2 = n / 2;
        int n3 = n - n2 - 1;
        Entry entry5 = new Entry();
        entry5.left(this.readTree(objectInputStream, n3, entry, entry5));
        entry5.key = objectInputStream.readFloat();
        entry5.value = objectInputStream.readFloat();
        entry5.black(false);
        entry5.right(this.readTree(objectInputStream, n2, entry5, entry2));
        if (n + 2 == (n + 2 & -(n + 2))) {
            entry5.right.black(true);
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
    public FloatSet keySet() {
        return this.keySet();
    }

    @Override
    public ObjectSet float2FloatEntrySet() {
        return this.float2FloatEntrySet();
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
    extends AbstractFloat2FloatSortedMap
    implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        float from;
        float to;
        boolean bottom;
        boolean top;
        protected transient ObjectSortedSet<Float2FloatMap.Entry> entries;
        protected transient FloatSortedSet keys;
        protected transient FloatCollection values;
        final Float2FloatRBTreeMap this$0;

        public Submap(Float2FloatRBTreeMap float2FloatRBTreeMap, float f, boolean bl, float f2, boolean bl2) {
            this.this$0 = float2FloatRBTreeMap;
            if (!bl && !bl2 && float2FloatRBTreeMap.compare(f, f2) > 0) {
                throw new IllegalArgumentException("Start key (" + f + ") is larger than end key (" + f2 + ")");
            }
            this.from = f;
            this.bottom = bl;
            this.to = f2;
            this.top = bl2;
            this.defRetValue = float2FloatRBTreeMap.defRetValue;
        }

        @Override
        public void clear() {
            SubmapIterator submapIterator = new SubmapIterator(this);
            while (submapIterator.hasNext()) {
                submapIterator.nextEntry();
                submapIterator.remove();
            }
        }

        final boolean in(float f) {
            return !(!this.bottom && this.this$0.compare(f, this.from) < 0 || !this.top && this.this$0.compare(f, this.to) >= 0);
        }

        @Override
        public ObjectSortedSet<Float2FloatMap.Entry> float2FloatEntrySet() {
            if (this.entries == null) {
                this.entries = new AbstractObjectSortedSet<Float2FloatMap.Entry>(this){
                    final Submap this$1;
                    {
                        this.this$1 = submap;
                    }

                    @Override
                    public ObjectBidirectionalIterator<Float2FloatMap.Entry> iterator() {
                        return new SubmapEntryIterator(this.this$1);
                    }

                    @Override
                    public ObjectBidirectionalIterator<Float2FloatMap.Entry> iterator(Float2FloatMap.Entry entry) {
                        return new SubmapEntryIterator(this.this$1, entry.getFloatKey());
                    }

                    @Override
                    public Comparator<? super Float2FloatMap.Entry> comparator() {
                        return this.this$1.this$0.float2FloatEntrySet().comparator();
                    }

                    @Override
                    public boolean contains(Object object) {
                        if (!(object instanceof Map.Entry)) {
                            return true;
                        }
                        Map.Entry entry = (Map.Entry)object;
                        if (entry.getKey() == null || !(entry.getKey() instanceof Float)) {
                            return true;
                        }
                        if (entry.getValue() == null || !(entry.getValue() instanceof Float)) {
                            return true;
                        }
                        Entry entry2 = this.this$1.this$0.findKey(((Float)entry.getKey()).floatValue());
                        return entry2 != null && this.this$1.in(entry2.key) && entry.equals(entry2);
                    }

                    @Override
                    public boolean remove(Object object) {
                        if (!(object instanceof Map.Entry)) {
                            return true;
                        }
                        Map.Entry entry = (Map.Entry)object;
                        if (entry.getKey() == null || !(entry.getKey() instanceof Float)) {
                            return true;
                        }
                        if (entry.getValue() == null || !(entry.getValue() instanceof Float)) {
                            return true;
                        }
                        Entry entry2 = this.this$1.this$0.findKey(((Float)entry.getKey()).floatValue());
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
                    public Float2FloatMap.Entry first() {
                        return this.this$1.firstEntry();
                    }

                    @Override
                    public Float2FloatMap.Entry last() {
                        return this.this$1.lastEntry();
                    }

                    @Override
                    public ObjectSortedSet<Float2FloatMap.Entry> subSet(Float2FloatMap.Entry entry, Float2FloatMap.Entry entry2) {
                        return this.this$1.subMap(entry.getFloatKey(), entry2.getFloatKey()).float2FloatEntrySet();
                    }

                    @Override
                    public ObjectSortedSet<Float2FloatMap.Entry> headSet(Float2FloatMap.Entry entry) {
                        return this.this$1.headMap(entry.getFloatKey()).float2FloatEntrySet();
                    }

                    @Override
                    public ObjectSortedSet<Float2FloatMap.Entry> tailSet(Float2FloatMap.Entry entry) {
                        return this.this$1.tailMap(entry.getFloatKey()).float2FloatEntrySet();
                    }

                    @Override
                    public ObjectSortedSet tailSet(Object object) {
                        return this.tailSet((Float2FloatMap.Entry)object);
                    }

                    @Override
                    public ObjectSortedSet headSet(Object object) {
                        return this.headSet((Float2FloatMap.Entry)object);
                    }

                    @Override
                    public ObjectSortedSet subSet(Object object, Object object2) {
                        return this.subSet((Float2FloatMap.Entry)object, (Float2FloatMap.Entry)object2);
                    }

                    @Override
                    public ObjectBidirectionalIterator iterator(Object object) {
                        return this.iterator((Float2FloatMap.Entry)object);
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
                        return this.tailSet((Float2FloatMap.Entry)object);
                    }

                    @Override
                    public SortedSet headSet(Object object) {
                        return this.headSet((Float2FloatMap.Entry)object);
                    }

                    @Override
                    public SortedSet subSet(Object object, Object object2) {
                        return this.subSet((Float2FloatMap.Entry)object, (Float2FloatMap.Entry)object2);
                    }
                };
            }
            return this.entries;
        }

        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = new KeySet(this, null);
            }
            return this.keys;
        }

        @Override
        public FloatCollection values() {
            if (this.values == null) {
                this.values = new AbstractFloatCollection(this){
                    final Submap this$1;
                    {
                        this.this$1 = submap;
                    }

                    @Override
                    public FloatIterator iterator() {
                        return new SubmapValueIterator(this.this$1, null);
                    }

                    @Override
                    public boolean contains(float f) {
                        return this.this$1.containsValue(f);
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
        public boolean containsKey(float f) {
            return this.in(f) && this.this$0.containsKey(f);
        }

        @Override
        public boolean containsValue(float f) {
            SubmapIterator submapIterator = new SubmapIterator(this);
            while (submapIterator.hasNext()) {
                float f2 = submapIterator.nextEntry().value;
                if (Float.floatToIntBits(f2) != Float.floatToIntBits(f)) continue;
                return false;
            }
            return true;
        }

        @Override
        public float get(float f) {
            Entry entry;
            float f2 = f;
            return this.in(f2) && (entry = this.this$0.findKey(f2)) != null ? entry.value : this.defRetValue;
        }

        @Override
        public float put(float f, float f2) {
            this.this$0.modified = false;
            if (!this.in(f)) {
                throw new IllegalArgumentException("Key (" + f + ") out of range [" + (this.bottom ? "-" : String.valueOf(this.from)) + ", " + (this.top ? "-" : String.valueOf(this.to)) + ")");
            }
            float f3 = this.this$0.put(f, f2);
            return this.this$0.modified ? this.defRetValue : f3;
        }

        @Override
        public float remove(float f) {
            this.this$0.modified = false;
            if (!this.in(f)) {
                return this.defRetValue;
            }
            float f2 = this.this$0.remove(f);
            return this.this$0.modified ? f2 : this.defRetValue;
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
        public FloatComparator comparator() {
            return this.this$0.actualComparator;
        }

        @Override
        public Float2FloatSortedMap headMap(float f) {
            if (this.top) {
                return new Submap(this.this$0, this.from, this.bottom, f, false);
            }
            return this.this$0.compare(f, this.to) < 0 ? new Submap(this.this$0, this.from, this.bottom, f, false) : this;
        }

        @Override
        public Float2FloatSortedMap tailMap(float f) {
            if (this.bottom) {
                return new Submap(this.this$0, f, false, this.to, this.top);
            }
            return this.this$0.compare(f, this.from) > 0 ? new Submap(this.this$0, f, false, this.to, this.top) : this;
        }

        @Override
        public Float2FloatSortedMap subMap(float f, float f2) {
            if (this.top && this.bottom) {
                return new Submap(this.this$0, f, false, f2, false);
            }
            if (!this.top) {
                float f3 = f2 = this.this$0.compare(f2, this.to) < 0 ? f2 : this.to;
            }
            if (!this.bottom) {
                float f4 = f = this.this$0.compare(f, this.from) > 0 ? f : this.from;
            }
            if (!this.top && !this.bottom && f == this.from && f2 == this.to) {
                return this;
            }
            return new Submap(this.this$0, f, false, f2, false);
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
        public float firstFloatKey() {
            Entry entry = this.firstEntry();
            if (entry == null) {
                throw new NoSuchElementException();
            }
            return entry.key;
        }

        @Override
        public float lastFloatKey() {
            Entry entry = this.lastEntry();
            if (entry == null) {
                throw new NoSuchElementException();
            }
            return entry.key;
        }

        @Override
        public FloatSet keySet() {
            return this.keySet();
        }

        @Override
        public ObjectSet float2FloatEntrySet() {
            return this.float2FloatEntrySet();
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
        implements FloatListIterator {
            final Submap this$1;

            private SubmapValueIterator(Submap submap) {
                this.this$1 = submap;
                super(submap);
            }

            @Override
            public float nextFloat() {
                return this.nextEntry().value;
            }

            @Override
            public float previousFloat() {
                return this.previousEntry().value;
            }

            SubmapValueIterator(Submap submap, 1 var2_2) {
                this(submap);
            }
        }

        private final class SubmapKeyIterator
        extends SubmapIterator
        implements FloatListIterator {
            final Submap this$1;

            public SubmapKeyIterator(Submap submap) {
                this.this$1 = submap;
                super(submap);
            }

            public SubmapKeyIterator(Submap submap, float f) {
                this.this$1 = submap;
                super(submap, f);
            }

            @Override
            public float nextFloat() {
                return this.nextEntry().key;
            }

            @Override
            public float previousFloat() {
                return this.previousEntry().key;
            }
        }

        /*
         * Duplicate member names - consider using --renamedupmembers true
         */
        private class SubmapEntryIterator
        extends SubmapIterator
        implements ObjectListIterator<Float2FloatMap.Entry> {
            final Submap this$1;

            SubmapEntryIterator(Submap submap) {
                this.this$1 = submap;
                super(submap);
            }

            SubmapEntryIterator(Submap submap, float f) {
                this.this$1 = submap;
                super(submap, f);
            }

            @Override
            public Float2FloatMap.Entry next() {
                return this.nextEntry();
            }

            @Override
            public Float2FloatMap.Entry previous() {
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
            SubmapIterator(Submap submap, float f) {
                this(submap);
                if (this.next == null) return;
                if (!submap.bottom && submap.this$0.compare(f, this.next.key) < 0) {
                    this.prev = null;
                    return;
                }
                if (!submap.top) {
                    this.prev = submap.lastEntry();
                    if (submap.this$0.compare(f, this.prev.key) >= 0) {
                        this.next = null;
                        return;
                    }
                }
                this.next = submap.this$0.locateKey(f);
                if (submap.this$0.compare(this.next.key, f) <= 0) {
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
        extends AbstractFloat2FloatSortedMap.KeySet {
            final Submap this$1;

            private KeySet(Submap submap) {
                this.this$1 = submap;
                super(submap);
            }

            @Override
            public FloatBidirectionalIterator iterator() {
                return new SubmapKeyIterator(this.this$1);
            }

            @Override
            public FloatBidirectionalIterator iterator(float f) {
                return new SubmapKeyIterator(this.this$1, f);
            }

            @Override
            public FloatIterator iterator() {
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
    implements FloatListIterator {
        final Float2FloatRBTreeMap this$0;

        private ValueIterator(Float2FloatRBTreeMap float2FloatRBTreeMap) {
            this.this$0 = float2FloatRBTreeMap;
            super(float2FloatRBTreeMap);
        }

        @Override
        public float nextFloat() {
            return this.nextEntry().value;
        }

        @Override
        public float previousFloat() {
            return this.previousEntry().value;
        }

        ValueIterator(Float2FloatRBTreeMap float2FloatRBTreeMap, 1 var2_2) {
            this(float2FloatRBTreeMap);
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    private class KeySet
    extends AbstractFloat2FloatSortedMap.KeySet {
        final Float2FloatRBTreeMap this$0;

        private KeySet(Float2FloatRBTreeMap float2FloatRBTreeMap) {
            this.this$0 = float2FloatRBTreeMap;
            super(float2FloatRBTreeMap);
        }

        @Override
        public FloatBidirectionalIterator iterator() {
            return new KeyIterator(this.this$0);
        }

        @Override
        public FloatBidirectionalIterator iterator(float f) {
            return new KeyIterator(this.this$0, f);
        }

        @Override
        public FloatIterator iterator() {
            return this.iterator();
        }

        @Override
        public Iterator iterator() {
            return this.iterator();
        }

        KeySet(Float2FloatRBTreeMap float2FloatRBTreeMap, 1 var2_2) {
            this(float2FloatRBTreeMap);
        }
    }

    private final class KeyIterator
    extends TreeIterator
    implements FloatListIterator {
        final Float2FloatRBTreeMap this$0;

        public KeyIterator(Float2FloatRBTreeMap float2FloatRBTreeMap) {
            this.this$0 = float2FloatRBTreeMap;
            super(float2FloatRBTreeMap);
        }

        public KeyIterator(Float2FloatRBTreeMap float2FloatRBTreeMap, float f) {
            this.this$0 = float2FloatRBTreeMap;
            super(float2FloatRBTreeMap, f);
        }

        @Override
        public float nextFloat() {
            return this.nextEntry().key;
        }

        @Override
        public float previousFloat() {
            return this.previousEntry().key;
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    private class EntryIterator
    extends TreeIterator
    implements ObjectListIterator<Float2FloatMap.Entry> {
        final Float2FloatRBTreeMap this$0;

        EntryIterator(Float2FloatRBTreeMap float2FloatRBTreeMap) {
            this.this$0 = float2FloatRBTreeMap;
            super(float2FloatRBTreeMap);
        }

        EntryIterator(Float2FloatRBTreeMap float2FloatRBTreeMap, float f) {
            this.this$0 = float2FloatRBTreeMap;
            super(float2FloatRBTreeMap, f);
        }

        @Override
        public Float2FloatMap.Entry next() {
            return this.nextEntry();
        }

        @Override
        public Float2FloatMap.Entry previous() {
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

    private class TreeIterator {
        Entry prev;
        Entry next;
        Entry curr;
        int index;
        final Float2FloatRBTreeMap this$0;

        TreeIterator(Float2FloatRBTreeMap float2FloatRBTreeMap) {
            this.this$0 = float2FloatRBTreeMap;
            this.index = 0;
            this.next = float2FloatRBTreeMap.firstEntry;
        }

        TreeIterator(Float2FloatRBTreeMap float2FloatRBTreeMap, float f) {
            this.this$0 = float2FloatRBTreeMap;
            this.index = 0;
            this.next = float2FloatRBTreeMap.locateKey(f);
            if (this.next != null) {
                if (float2FloatRBTreeMap.compare(this.next.key, f) <= 0) {
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
    extends AbstractFloat2FloatMap.BasicEntry
    implements Cloneable {
        private static final int BLACK_MASK = 1;
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        private static final int PRED_MASK = 0x40000000;
        Entry left;
        Entry right;
        int info;

        Entry() {
            super(0.0f, 0.0f);
        }

        Entry(float f, float f2) {
            super(f, f2);
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

        boolean black() {
            return (this.info & 1) != 0;
        }

        void black(boolean bl) {
            this.info = bl ? (this.info |= 1) : (this.info &= 0xFFFFFFFE);
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
        public float setValue(float f) {
            float f2 = this.value;
            this.value = f;
            return f2;
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
            return Float.floatToIntBits(this.key) == Float.floatToIntBits(((Float)entry.getKey()).floatValue()) && Float.floatToIntBits(this.value) == Float.floatToIntBits(((Float)entry.getValue()).floatValue());
        }

        @Override
        public int hashCode() {
            return HashCommon.float2int(this.key) ^ HashCommon.float2int(this.value);
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

