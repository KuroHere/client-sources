/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.Size64;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObject2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectCollection;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectSet;
import com.viaversion.viaversion.libs.fastutil.objects.Object2ObjectMap;
import com.viaversion.viaversion.libs.fastutil.objects.Object2ObjectMaps;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSet;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterators;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;

public abstract class AbstractObject2ObjectMap<K, V>
extends AbstractObject2ObjectFunction<K, V>
implements Object2ObjectMap<K, V>,
Serializable {
    private static final long serialVersionUID = -4940583368468432370L;

    protected AbstractObject2ObjectMap() {
    }

    @Override
    public boolean containsKey(Object object) {
        Iterator iterator2 = this.object2ObjectEntrySet().iterator();
        while (iterator2.hasNext()) {
            if (((Object2ObjectMap.Entry)iterator2.next()).getKey() != object) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean containsValue(Object object) {
        Iterator iterator2 = this.object2ObjectEntrySet().iterator();
        while (iterator2.hasNext()) {
            if (((Object2ObjectMap.Entry)iterator2.next()).getValue() != object) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public ObjectSet<K> keySet() {
        return new AbstractObjectSet<K>(this){
            final AbstractObject2ObjectMap this$0;
            {
                this.this$0 = abstractObject2ObjectMap;
            }

            @Override
            public boolean contains(Object object) {
                return this.this$0.containsKey(object);
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
            public ObjectIterator<K> iterator() {
                return new ObjectIterator<K>(this){
                    private final ObjectIterator<Object2ObjectMap.Entry<K, V>> i;
                    final 1 this$1;
                    {
                        this.this$1 = var1_1;
                        this.i = Object2ObjectMaps.fastIterator(this.this$1.this$0);
                    }

                    @Override
                    public K next() {
                        return ((Object2ObjectMap.Entry)this.i.next()).getKey();
                    }

                    @Override
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }

                    @Override
                    public void remove() {
                        this.i.remove();
                    }

                    @Override
                    public void forEachRemaining(Consumer<? super K> consumer) {
                        this.i.forEachRemaining(arg_0 -> 1.lambda$forEachRemaining$0(consumer, arg_0));
                    }

                    private static void lambda$forEachRemaining$0(Consumer consumer, Object2ObjectMap.Entry entry) {
                        consumer.accept(entry.getKey());
                    }
                };
            }

            @Override
            public ObjectSpliterator<K> spliterator() {
                return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(this.this$0), 65);
            }

            @Override
            public Spliterator spliterator() {
                return this.spliterator();
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
            final AbstractObject2ObjectMap this$0;
            {
                this.this$0 = abstractObject2ObjectMap;
            }

            @Override
            public boolean contains(Object object) {
                return this.this$0.containsValue(object);
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
            public ObjectIterator<V> iterator() {
                return new ObjectIterator<V>(this){
                    private final ObjectIterator<Object2ObjectMap.Entry<K, V>> i;
                    final 2 this$1;
                    {
                        this.this$1 = var1_1;
                        this.i = Object2ObjectMaps.fastIterator(this.this$1.this$0);
                    }

                    @Override
                    public V next() {
                        return ((Object2ObjectMap.Entry)this.i.next()).getValue();
                    }

                    @Override
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }

                    @Override
                    public void remove() {
                        this.i.remove();
                    }

                    @Override
                    public void forEachRemaining(Consumer<? super V> consumer) {
                        this.i.forEachRemaining(arg_0 -> 1.lambda$forEachRemaining$0(consumer, arg_0));
                    }

                    private static void lambda$forEachRemaining$0(Consumer consumer, Object2ObjectMap.Entry entry) {
                        consumer.accept(entry.getValue());
                    }
                };
            }

            @Override
            public ObjectSpliterator<V> spliterator() {
                return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(this.this$0), 64);
            }

            @Override
            public Spliterator spliterator() {
                return this.spliterator();
            }

            @Override
            public Iterator iterator() {
                return this.iterator();
            }
        };
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        if (map instanceof Object2ObjectMap) {
            ObjectIterator objectIterator = Object2ObjectMaps.fastIterator((Object2ObjectMap)map);
            while (objectIterator.hasNext()) {
                Object2ObjectMap.Entry entry = (Object2ObjectMap.Entry)objectIterator.next();
                this.put(entry.getKey(), entry.getValue());
            }
        } else {
            int n = map.size();
            Iterator<Map.Entry<K, V>> iterator2 = map.entrySet().iterator();
            while (n-- != 0) {
                Map.Entry<K, V> entry = iterator2.next();
                this.put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public int hashCode() {
        int n = 0;
        int n2 = this.size();
        ObjectIterator objectIterator = Object2ObjectMaps.fastIterator(this);
        while (n2-- != 0) {
            n += ((Object2ObjectMap.Entry)objectIterator.next()).hashCode();
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
        return this.object2ObjectEntrySet().containsAll(map.entrySet());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        ObjectIterator objectIterator = Object2ObjectMaps.fastIterator(this);
        int n = this.size();
        boolean bl = true;
        stringBuilder.append("{");
        while (n-- != 0) {
            if (bl) {
                bl = false;
            } else {
                stringBuilder.append(", ");
            }
            Object2ObjectMap.Entry entry = (Object2ObjectMap.Entry)objectIterator.next();
            if (this == entry.getKey()) {
                stringBuilder.append("(this map)");
            } else {
                stringBuilder.append(String.valueOf(entry.getKey()));
            }
            stringBuilder.append("=>");
            if (this == entry.getValue()) {
                stringBuilder.append("(this map)");
                continue;
            }
            stringBuilder.append(String.valueOf(entry.getValue()));
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

    public static abstract class BasicEntrySet<K, V>
    extends AbstractObjectSet<Object2ObjectMap.Entry<K, V>> {
        protected final Object2ObjectMap<K, V> map;

        public BasicEntrySet(Object2ObjectMap<K, V> object2ObjectMap) {
            this.map = object2ObjectMap;
        }

        @Override
        public boolean contains(Object object) {
            if (!(object instanceof Map.Entry)) {
                return true;
            }
            if (object instanceof Object2ObjectMap.Entry) {
                Object2ObjectMap.Entry entry = (Object2ObjectMap.Entry)object;
                Object k = entry.getKey();
                return this.map.containsKey(k) && Objects.equals(this.map.get(k), entry.getValue());
            }
            Map.Entry entry = (Map.Entry)object;
            Object k = entry.getKey();
            Object v = entry.getValue();
            return this.map.containsKey(k) && Objects.equals(this.map.get(k), v);
        }

        @Override
        public boolean remove(Object object) {
            if (!(object instanceof Map.Entry)) {
                return true;
            }
            if (object instanceof Object2ObjectMap.Entry) {
                Object2ObjectMap.Entry entry = (Object2ObjectMap.Entry)object;
                return this.map.remove(entry.getKey(), entry.getValue());
            }
            Map.Entry entry = (Map.Entry)object;
            Object k = entry.getKey();
            Object v = entry.getValue();
            return this.map.remove(k, v);
        }

        @Override
        public int size() {
            return this.map.size();
        }

        @Override
        public ObjectSpliterator<Object2ObjectMap.Entry<K, V>> spliterator() {
            return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(this.map), 65);
        }

        @Override
        public Spliterator spliterator() {
            return this.spliterator();
        }
    }

    public static class BasicEntry<K, V>
    implements Object2ObjectMap.Entry<K, V> {
        protected K key;
        protected V value;

        public BasicEntry() {
        }

        public BasicEntry(K k, V v) {
            this.key = k;
            this.value = v;
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V v) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Map.Entry)) {
                return true;
            }
            if (object instanceof Object2ObjectMap.Entry) {
                Object2ObjectMap.Entry entry = (Object2ObjectMap.Entry)object;
                return Objects.equals(this.key, entry.getKey()) && Objects.equals(this.value, entry.getValue());
            }
            Map.Entry entry = (Map.Entry)object;
            Object k = entry.getKey();
            Object v = entry.getValue();
            return Objects.equals(this.key, k) && Objects.equals(this.value, v);
        }

        @Override
        public int hashCode() {
            return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
        }

        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}

