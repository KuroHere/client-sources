/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.longs.AbstractLong2ByteFunction;
import it.unimi.dsi.fastutil.longs.AbstractLongSet;
import it.unimi.dsi.fastutil.longs.Long2ByteMap;
import it.unimi.dsi.fastutil.longs.Long2ByteMaps;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public abstract class AbstractLong2ByteMap
extends AbstractLong2ByteFunction
implements Long2ByteMap,
Serializable {
    private static final long serialVersionUID = -4940583368468432370L;

    protected AbstractLong2ByteMap() {
    }

    @Override
    public boolean containsValue(byte by) {
        return this.values().contains(by);
    }

    @Override
    public boolean containsKey(long l) {
        Iterator iterator2 = this.long2ByteEntrySet().iterator();
        while (iterator2.hasNext()) {
            if (((Long2ByteMap.Entry)iterator2.next()).getLongKey() != l) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public LongSet keySet() {
        return new AbstractLongSet(this){
            final AbstractLong2ByteMap this$0;
            {
                this.this$0 = abstractLong2ByteMap;
            }

            @Override
            public boolean contains(long l) {
                return this.this$0.containsKey(l);
            }

            @Override
            public int size() {
                return this.this$0.size();
            }

            @Override
            public void clear() {
                this.this$0.clear();
            }

            @Override
            public LongIterator iterator() {
                return new LongIterator(this){
                    private final ObjectIterator<Long2ByteMap.Entry> i;
                    final 1 this$1;
                    {
                        this.this$1 = var1_1;
                        this.i = Long2ByteMaps.fastIterator(this.this$1.this$0);
                    }

                    @Override
                    public long nextLong() {
                        return ((Long2ByteMap.Entry)this.i.next()).getLongKey();
                    }

                    @Override
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }

                    @Override
                    public void remove() {
                        this.i.remove();
                    }
                };
            }

            @Override
            public Iterator iterator() {
                return this.iterator();
            }
        };
    }

    @Override
    public ByteCollection values() {
        return new AbstractByteCollection(this){
            final AbstractLong2ByteMap this$0;
            {
                this.this$0 = abstractLong2ByteMap;
            }

            @Override
            public boolean contains(byte by) {
                return this.this$0.containsValue(by);
            }

            @Override
            public int size() {
                return this.this$0.size();
            }

            @Override
            public void clear() {
                this.this$0.clear();
            }

            @Override
            public ByteIterator iterator() {
                return new ByteIterator(this){
                    private final ObjectIterator<Long2ByteMap.Entry> i;
                    final 2 this$1;
                    {
                        this.this$1 = var1_1;
                        this.i = Long2ByteMaps.fastIterator(this.this$1.this$0);
                    }

                    @Override
                    public byte nextByte() {
                        return ((Long2ByteMap.Entry)this.i.next()).getByteValue();
                    }

                    @Override
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }
                };
            }

            @Override
            public Iterator iterator() {
                return this.iterator();
            }
        };
    }

    @Override
    public void putAll(Map<? extends Long, ? extends Byte> map) {
        if (map instanceof Long2ByteMap) {
            ObjectIterator<Long2ByteMap.Entry> objectIterator = Long2ByteMaps.fastIterator((Long2ByteMap)map);
            while (objectIterator.hasNext()) {
                Long2ByteMap.Entry entry = (Long2ByteMap.Entry)objectIterator.next();
                this.put(entry.getLongKey(), entry.getByteValue());
            }
        } else {
            int n = map.size();
            Iterator<Map.Entry<? extends Long, ? extends Byte>> iterator2 = map.entrySet().iterator();
            while (n-- != 0) {
                Map.Entry<? extends Long, ? extends Byte> entry = iterator2.next();
                this.put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public int hashCode() {
        int n = 0;
        int n2 = this.size();
        ObjectIterator<Long2ByteMap.Entry> objectIterator = Long2ByteMaps.fastIterator(this);
        while (n2-- != 0) {
            n += ((Long2ByteMap.Entry)objectIterator.next()).hashCode();
        }
        return n;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return false;
        }
        if (!(object instanceof Map)) {
            return true;
        }
        Map map = (Map)object;
        if (map.size() != this.size()) {
            return true;
        }
        return this.long2ByteEntrySet().containsAll(map.entrySet());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        ObjectIterator<Long2ByteMap.Entry> objectIterator = Long2ByteMaps.fastIterator(this);
        int n = this.size();
        boolean bl = true;
        stringBuilder.append("{");
        while (n-- != 0) {
            if (bl) {
                bl = false;
            } else {
                stringBuilder.append(", ");
            }
            Long2ByteMap.Entry entry = (Long2ByteMap.Entry)objectIterator.next();
            stringBuilder.append(String.valueOf(entry.getLongKey()));
            stringBuilder.append("=>");
            stringBuilder.append(String.valueOf(entry.getByteValue()));
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Override
    public Collection values() {
        return this.values();
    }

    @Override
    public Set keySet() {
        return this.keySet();
    }

    public static abstract class BasicEntrySet
    extends AbstractObjectSet<Long2ByteMap.Entry> {
        protected final Long2ByteMap map;

        public BasicEntrySet(Long2ByteMap long2ByteMap) {
            this.map = long2ByteMap;
        }

        @Override
        public boolean contains(Object object) {
            if (!(object instanceof Map.Entry)) {
                return true;
            }
            if (object instanceof Long2ByteMap.Entry) {
                Long2ByteMap.Entry entry = (Long2ByteMap.Entry)object;
                long l = entry.getLongKey();
                return this.map.containsKey(l) && this.map.get(l) == entry.getByteValue();
            }
            Map.Entry entry = (Map.Entry)object;
            Object k = entry.getKey();
            if (k == null || !(k instanceof Long)) {
                return true;
            }
            long l = (Long)k;
            Object v = entry.getValue();
            if (v == null || !(v instanceof Byte)) {
                return true;
            }
            return this.map.containsKey(l) && this.map.get(l) == ((Byte)v).byteValue();
        }

        @Override
        public boolean remove(Object object) {
            if (!(object instanceof Map.Entry)) {
                return true;
            }
            if (object instanceof Long2ByteMap.Entry) {
                Long2ByteMap.Entry entry = (Long2ByteMap.Entry)object;
                return this.map.remove(entry.getLongKey(), entry.getByteValue());
            }
            Map.Entry entry = (Map.Entry)object;
            Object k = entry.getKey();
            if (k == null || !(k instanceof Long)) {
                return true;
            }
            long l = (Long)k;
            Object v = entry.getValue();
            if (v == null || !(v instanceof Byte)) {
                return true;
            }
            byte by = (Byte)v;
            return this.map.remove(l, by);
        }

        @Override
        public int size() {
            return this.map.size();
        }
    }

    public static class BasicEntry
    implements Long2ByteMap.Entry {
        protected long key;
        protected byte value;

        public BasicEntry() {
        }

        public BasicEntry(Long l, Byte by) {
            this.key = l;
            this.value = by;
        }

        public BasicEntry(long l, byte by) {
            this.key = l;
            this.value = by;
        }

        @Override
        public long getLongKey() {
            return this.key;
        }

        @Override
        public byte getByteValue() {
            return this.value;
        }

        @Override
        public byte setValue(byte by) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Map.Entry)) {
                return true;
            }
            if (object instanceof Long2ByteMap.Entry) {
                Long2ByteMap.Entry entry = (Long2ByteMap.Entry)object;
                return this.key == entry.getLongKey() && this.value == entry.getByteValue();
            }
            Map.Entry entry = (Map.Entry)object;
            Object k = entry.getKey();
            if (k == null || !(k instanceof Long)) {
                return true;
            }
            Object v = entry.getValue();
            if (v == null || !(v instanceof Byte)) {
                return true;
            }
            return this.key == (Long)k && this.value == (Byte)v;
        }

        @Override
        public int hashCode() {
            return HashCommon.long2int(this.key) ^ this.value;
        }

        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}

