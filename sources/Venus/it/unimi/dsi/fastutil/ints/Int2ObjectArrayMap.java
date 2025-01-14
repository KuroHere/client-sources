/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap;
import it.unimi.dsi.fastutil.ints.AbstractIntSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class Int2ObjectArrayMap<V>
extends AbstractInt2ObjectMap<V>
implements Serializable,
Cloneable {
    private static final long serialVersionUID = 1L;
    private transient int[] key;
    private transient Object[] value;
    private int size;

    public Int2ObjectArrayMap(int[] nArray, Object[] objectArray) {
        this.key = nArray;
        this.value = objectArray;
        this.size = nArray.length;
        if (nArray.length != objectArray.length) {
            throw new IllegalArgumentException("Keys and values have different lengths (" + nArray.length + ", " + objectArray.length + ")");
        }
    }

    public Int2ObjectArrayMap() {
        this.key = IntArrays.EMPTY_ARRAY;
        this.value = ObjectArrays.EMPTY_ARRAY;
    }

    public Int2ObjectArrayMap(int n) {
        this.key = new int[n];
        this.value = new Object[n];
    }

    public Int2ObjectArrayMap(Int2ObjectMap<V> int2ObjectMap) {
        this(int2ObjectMap.size());
        this.putAll(int2ObjectMap);
    }

    public Int2ObjectArrayMap(Map<? extends Integer, ? extends V> map) {
        this(map.size());
        this.putAll(map);
    }

    public Int2ObjectArrayMap(int[] nArray, Object[] objectArray, int n) {
        this.key = nArray;
        this.value = objectArray;
        this.size = n;
        if (nArray.length != objectArray.length) {
            throw new IllegalArgumentException("Keys and values have different lengths (" + nArray.length + ", " + objectArray.length + ")");
        }
        if (n > nArray.length) {
            throw new IllegalArgumentException("The provided size (" + n + ") is larger than or equal to the backing-arrays size (" + nArray.length + ")");
        }
    }

    public Int2ObjectMap.FastEntrySet<V> int2ObjectEntrySet() {
        return new EntrySet(this, null);
    }

    private int findKey(int n) {
        int[] nArray = this.key;
        int n2 = this.size;
        while (n2-- != 0) {
            if (nArray[n2] != n) continue;
            return n2;
        }
        return 1;
    }

    @Override
    public V get(int n) {
        int[] nArray = this.key;
        int n2 = this.size;
        while (n2-- != 0) {
            if (nArray[n2] != n) continue;
            return (V)this.value[n2];
        }
        return (V)this.defRetValue;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
        int n = this.size;
        while (n-- != 0) {
            this.value[n] = null;
        }
        this.size = 0;
    }

    @Override
    public boolean containsKey(int n) {
        return this.findKey(n) != -1;
    }

    @Override
    public boolean containsValue(Object object) {
        int n = this.size;
        while (n-- != 0) {
            if (!Objects.equals(this.value[n], object)) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public V put(int n, V v) {
        int n2 = this.findKey(n);
        if (n2 != -1) {
            Object object = this.value[n2];
            this.value[n2] = v;
            return (V)object;
        }
        if (this.size == this.key.length) {
            int[] nArray = new int[this.size == 0 ? 2 : this.size * 2];
            Object[] objectArray = new Object[this.size == 0 ? 2 : this.size * 2];
            int n3 = this.size;
            while (n3-- != 0) {
                nArray[n3] = this.key[n3];
                objectArray[n3] = this.value[n3];
            }
            this.key = nArray;
            this.value = objectArray;
        }
        this.key[this.size] = n;
        this.value[this.size] = v;
        ++this.size;
        return (V)this.defRetValue;
    }

    @Override
    public V remove(int n) {
        int n2 = this.findKey(n);
        if (n2 == -1) {
            return (V)this.defRetValue;
        }
        Object object = this.value[n2];
        int n3 = this.size - n2 - 1;
        System.arraycopy(this.key, n2 + 1, this.key, n2, n3);
        System.arraycopy(this.value, n2 + 1, this.value, n2, n3);
        --this.size;
        this.value[this.size] = null;
        return (V)object;
    }

    @Override
    public IntSet keySet() {
        return new AbstractIntSet(this){
            final Int2ObjectArrayMap this$0;
            {
                this.this$0 = int2ObjectArrayMap;
            }

            @Override
            public boolean contains(int n) {
                return Int2ObjectArrayMap.access$300(this.this$0, n) != -1;
            }

            @Override
            public boolean remove(int n) {
                int n2 = Int2ObjectArrayMap.access$300(this.this$0, n);
                if (n2 == -1) {
                    return true;
                }
                int n3 = Int2ObjectArrayMap.access$000(this.this$0) - n2 - 1;
                System.arraycopy(Int2ObjectArrayMap.access$100(this.this$0), n2 + 1, Int2ObjectArrayMap.access$100(this.this$0), n2, n3);
                System.arraycopy(Int2ObjectArrayMap.access$200(this.this$0), n2 + 1, Int2ObjectArrayMap.access$200(this.this$0), n2, n3);
                Int2ObjectArrayMap.access$010(this.this$0);
                return false;
            }

            @Override
            public IntIterator iterator() {
                return new IntIterator(this){
                    int pos;
                    final 1 this$1;
                    {
                        this.this$1 = var1_1;
                        this.pos = 0;
                    }

                    @Override
                    public boolean hasNext() {
                        return this.pos < Int2ObjectArrayMap.access$000(this.this$1.this$0);
                    }

                    @Override
                    public int nextInt() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Int2ObjectArrayMap.access$100(this.this$1.this$0)[this.pos++];
                    }

                    @Override
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        int n = Int2ObjectArrayMap.access$000(this.this$1.this$0) - this.pos;
                        System.arraycopy(Int2ObjectArrayMap.access$100(this.this$1.this$0), this.pos, Int2ObjectArrayMap.access$100(this.this$1.this$0), this.pos - 1, n);
                        System.arraycopy(Int2ObjectArrayMap.access$200(this.this$1.this$0), this.pos, Int2ObjectArrayMap.access$200(this.this$1.this$0), this.pos - 1, n);
                        Int2ObjectArrayMap.access$010(this.this$1.this$0);
                    }
                };
            }

            @Override
            public int size() {
                return Int2ObjectArrayMap.access$000(this.this$0);
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

    @Override
    public ObjectCollection<V> values() {
        return new AbstractObjectCollection<V>(this){
            final Int2ObjectArrayMap this$0;
            {
                this.this$0 = int2ObjectArrayMap;
            }

            @Override
            public boolean contains(Object object) {
                return this.this$0.containsValue(object);
            }

            @Override
            public ObjectIterator<V> iterator() {
                return new ObjectIterator<V>(this){
                    int pos;
                    final 2 this$1;
                    {
                        this.this$1 = var1_1;
                        this.pos = 0;
                    }

                    @Override
                    public boolean hasNext() {
                        return this.pos < Int2ObjectArrayMap.access$000(this.this$1.this$0);
                    }

                    @Override
                    public V next() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Int2ObjectArrayMap.access$200(this.this$1.this$0)[this.pos++];
                    }

                    @Override
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        int n = Int2ObjectArrayMap.access$000(this.this$1.this$0) - this.pos;
                        System.arraycopy(Int2ObjectArrayMap.access$100(this.this$1.this$0), this.pos, Int2ObjectArrayMap.access$100(this.this$1.this$0), this.pos - 1, n);
                        System.arraycopy(Int2ObjectArrayMap.access$200(this.this$1.this$0), this.pos, Int2ObjectArrayMap.access$200(this.this$1.this$0), this.pos - 1, n);
                        Int2ObjectArrayMap.access$010(this.this$1.this$0);
                    }
                };
            }

            @Override
            public int size() {
                return Int2ObjectArrayMap.access$000(this.this$0);
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

    public Int2ObjectArrayMap<V> clone() {
        Int2ObjectArrayMap int2ObjectArrayMap;
        try {
            int2ObjectArrayMap = (Int2ObjectArrayMap)super.clone();
        } catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new InternalError();
        }
        int2ObjectArrayMap.key = (int[])this.key.clone();
        int2ObjectArrayMap.value = (Object[])this.value.clone();
        return int2ObjectArrayMap;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            objectOutputStream.writeInt(this.key[i]);
            objectOutputStream.writeObject(this.value[i]);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.key = new int[this.size];
        this.value = new Object[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = objectInputStream.readInt();
            this.value[i] = objectInputStream.readObject();
        }
    }

    @Override
    public ObjectSet int2ObjectEntrySet() {
        return this.int2ObjectEntrySet();
    }

    @Override
    public Collection values() {
        return this.values();
    }

    @Override
    public Set keySet() {
        return this.keySet();
    }

    public Object clone() throws CloneNotSupportedException {
        return this.clone();
    }

    static int access$000(Int2ObjectArrayMap int2ObjectArrayMap) {
        return int2ObjectArrayMap.size;
    }

    static int[] access$100(Int2ObjectArrayMap int2ObjectArrayMap) {
        return int2ObjectArrayMap.key;
    }

    static Object[] access$200(Int2ObjectArrayMap int2ObjectArrayMap) {
        return int2ObjectArrayMap.value;
    }

    static int access$010(Int2ObjectArrayMap int2ObjectArrayMap) {
        return int2ObjectArrayMap.size--;
    }

    static int access$300(Int2ObjectArrayMap int2ObjectArrayMap, int n) {
        return int2ObjectArrayMap.findKey(n);
    }

    private final class EntrySet
    extends AbstractObjectSet<Int2ObjectMap.Entry<V>>
    implements Int2ObjectMap.FastEntrySet<V> {
        final Int2ObjectArrayMap this$0;

        private EntrySet(Int2ObjectArrayMap int2ObjectArrayMap) {
            this.this$0 = int2ObjectArrayMap;
        }

        @Override
        public ObjectIterator<Int2ObjectMap.Entry<V>> iterator() {
            return new ObjectIterator<Int2ObjectMap.Entry<V>>(this){
                int curr;
                int next;
                final EntrySet this$1;
                {
                    this.this$1 = entrySet;
                    this.curr = -1;
                    this.next = 0;
                }

                @Override
                public boolean hasNext() {
                    return this.next < Int2ObjectArrayMap.access$000(this.this$1.this$0);
                }

                @Override
                public Int2ObjectMap.Entry<V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.curr = this.next;
                    return new AbstractInt2ObjectMap.BasicEntry<Object>(Int2ObjectArrayMap.access$100(this.this$1.this$0)[this.curr], Int2ObjectArrayMap.access$200(this.this$1.this$0)[this.next++]);
                }

                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    int n = Int2ObjectArrayMap.access$010(this.this$1.this$0) - this.next--;
                    System.arraycopy(Int2ObjectArrayMap.access$100(this.this$1.this$0), this.next + 1, Int2ObjectArrayMap.access$100(this.this$1.this$0), this.next, n);
                    System.arraycopy(Int2ObjectArrayMap.access$200(this.this$1.this$0), this.next + 1, Int2ObjectArrayMap.access$200(this.this$1.this$0), this.next, n);
                    Int2ObjectArrayMap.access$200((Int2ObjectArrayMap)this.this$1.this$0)[Int2ObjectArrayMap.access$000((Int2ObjectArrayMap)this.this$1.this$0)] = null;
                }

                @Override
                public Object next() {
                    return this.next();
                }
            };
        }

        @Override
        public ObjectIterator<Int2ObjectMap.Entry<V>> fastIterator() {
            return new ObjectIterator<Int2ObjectMap.Entry<V>>(this){
                int next;
                int curr;
                final AbstractInt2ObjectMap.BasicEntry<V> entry;
                final EntrySet this$1;
                {
                    this.this$1 = entrySet;
                    this.next = 0;
                    this.curr = -1;
                    this.entry = new AbstractInt2ObjectMap.BasicEntry();
                }

                @Override
                public boolean hasNext() {
                    return this.next < Int2ObjectArrayMap.access$000(this.this$1.this$0);
                }

                @Override
                public Int2ObjectMap.Entry<V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.curr = this.next;
                    this.entry.key = Int2ObjectArrayMap.access$100(this.this$1.this$0)[this.curr];
                    this.entry.value = Int2ObjectArrayMap.access$200(this.this$1.this$0)[this.next++];
                    return this.entry;
                }

                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    int n = Int2ObjectArrayMap.access$010(this.this$1.this$0) - this.next--;
                    System.arraycopy(Int2ObjectArrayMap.access$100(this.this$1.this$0), this.next + 1, Int2ObjectArrayMap.access$100(this.this$1.this$0), this.next, n);
                    System.arraycopy(Int2ObjectArrayMap.access$200(this.this$1.this$0), this.next + 1, Int2ObjectArrayMap.access$200(this.this$1.this$0), this.next, n);
                    Int2ObjectArrayMap.access$200((Int2ObjectArrayMap)this.this$1.this$0)[Int2ObjectArrayMap.access$000((Int2ObjectArrayMap)this.this$1.this$0)] = null;
                }

                @Override
                public Object next() {
                    return this.next();
                }
            };
        }

        @Override
        public int size() {
            return Int2ObjectArrayMap.access$000(this.this$0);
        }

        @Override
        public boolean contains(Object object) {
            if (!(object instanceof Map.Entry)) {
                return true;
            }
            Map.Entry entry = (Map.Entry)object;
            if (entry.getKey() == null || !(entry.getKey() instanceof Integer)) {
                return true;
            }
            int n = (Integer)entry.getKey();
            return this.this$0.containsKey(n) && Objects.equals(this.this$0.get(n), entry.getValue());
        }

        @Override
        public boolean remove(Object object) {
            if (!(object instanceof Map.Entry)) {
                return true;
            }
            Map.Entry entry = (Map.Entry)object;
            if (entry.getKey() == null || !(entry.getKey() instanceof Integer)) {
                return true;
            }
            int n = (Integer)entry.getKey();
            Object v = entry.getValue();
            int n2 = Int2ObjectArrayMap.access$300(this.this$0, n);
            if (n2 == -1 || !Objects.equals(v, Int2ObjectArrayMap.access$200(this.this$0)[n2])) {
                return true;
            }
            int n3 = Int2ObjectArrayMap.access$000(this.this$0) - n2 - 1;
            System.arraycopy(Int2ObjectArrayMap.access$100(this.this$0), n2 + 1, Int2ObjectArrayMap.access$100(this.this$0), n2, n3);
            System.arraycopy(Int2ObjectArrayMap.access$200(this.this$0), n2 + 1, Int2ObjectArrayMap.access$200(this.this$0), n2, n3);
            Int2ObjectArrayMap.access$010(this.this$0);
            Int2ObjectArrayMap.access$200((Int2ObjectArrayMap)this.this$0)[Int2ObjectArrayMap.access$000((Int2ObjectArrayMap)this.this$0)] = null;
            return false;
        }

        @Override
        public Iterator iterator() {
            return this.iterator();
        }

        EntrySet(Int2ObjectArrayMap int2ObjectArrayMap, 1 var2_2) {
            this(int2ObjectArrayMap);
        }
    }
}

