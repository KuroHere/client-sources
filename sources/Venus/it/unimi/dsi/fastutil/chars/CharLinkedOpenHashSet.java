/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.chars.AbstractCharSortedSet;
import it.unimi.dsi.fastutil.chars.CharArrays;
import it.unimi.dsi.fastutil.chars.CharBidirectionalIterator;
import it.unimi.dsi.fastutil.chars.CharCollection;
import it.unimi.dsi.fastutil.chars.CharComparator;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.CharIterators;
import it.unimi.dsi.fastutil.chars.CharListIterator;
import it.unimi.dsi.fastutil.chars.CharSortedSet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class CharLinkedOpenHashSet
extends AbstractCharSortedSet
implements Serializable,
Cloneable,
Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient char[] key;
    protected transient int mask;
    protected transient boolean containsNull;
    protected transient int first = -1;
    protected transient int last = -1;
    protected transient long[] link;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;

    public CharLinkedOpenHashSet(int n, float f) {
        if (f <= 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1");
        }
        if (n < 0) {
            throw new IllegalArgumentException("The expected number of elements must be nonnegative");
        }
        this.f = f;
        this.minN = this.n = HashCommon.arraySize(n, f);
        this.mask = this.n - 1;
        this.maxFill = HashCommon.maxFill(this.n, f);
        this.key = new char[this.n + 1];
        this.link = new long[this.n + 1];
    }

    public CharLinkedOpenHashSet(int n) {
        this(n, 0.75f);
    }

    public CharLinkedOpenHashSet() {
        this(16, 0.75f);
    }

    public CharLinkedOpenHashSet(Collection<? extends Character> collection, float f) {
        this(collection.size(), f);
        this.addAll(collection);
    }

    public CharLinkedOpenHashSet(Collection<? extends Character> collection) {
        this(collection, 0.75f);
    }

    public CharLinkedOpenHashSet(CharCollection charCollection, float f) {
        this(charCollection.size(), f);
        this.addAll(charCollection);
    }

    public CharLinkedOpenHashSet(CharCollection charCollection) {
        this(charCollection, 0.75f);
    }

    public CharLinkedOpenHashSet(CharIterator charIterator, float f) {
        this(16, f);
        while (charIterator.hasNext()) {
            this.add(charIterator.nextChar());
        }
    }

    public CharLinkedOpenHashSet(CharIterator charIterator) {
        this(charIterator, 0.75f);
    }

    public CharLinkedOpenHashSet(Iterator<?> iterator2, float f) {
        this(CharIterators.asCharIterator(iterator2), f);
    }

    public CharLinkedOpenHashSet(Iterator<?> iterator2) {
        this(CharIterators.asCharIterator(iterator2));
    }

    public CharLinkedOpenHashSet(char[] cArray, int n, int n2, float f) {
        this(n2 < 0 ? 0 : n2, f);
        CharArrays.ensureOffsetLength(cArray, n, n2);
        for (int i = 0; i < n2; ++i) {
            this.add(cArray[n + i]);
        }
    }

    public CharLinkedOpenHashSet(char[] cArray, int n, int n2) {
        this(cArray, n, n2, 0.75f);
    }

    public CharLinkedOpenHashSet(char[] cArray, float f) {
        this(cArray, 0, cArray.length, f);
    }

    public CharLinkedOpenHashSet(char[] cArray) {
        this(cArray, 0.75f);
    }

    private int realSize() {
        return this.containsNull ? this.size - 1 : this.size;
    }

    private void ensureCapacity(int n) {
        int n2 = HashCommon.arraySize(n, this.f);
        if (n2 > this.n) {
            this.rehash(n2);
        }
    }

    private void tryCapacity(long l) {
        int n = (int)Math.min(0x40000000L, Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil((float)l / this.f))));
        if (n > this.n) {
            this.rehash(n);
        }
    }

    @Override
    public boolean addAll(CharCollection charCollection) {
        if ((double)this.f <= 0.5) {
            this.ensureCapacity(charCollection.size());
        } else {
            this.tryCapacity(this.size() + charCollection.size());
        }
        return super.addAll(charCollection);
    }

    @Override
    public boolean addAll(Collection<? extends Character> collection) {
        if ((double)this.f <= 0.5) {
            this.ensureCapacity(collection.size());
        } else {
            this.tryCapacity(this.size() + collection.size());
        }
        return super.addAll(collection);
    }

    @Override
    public boolean add(char c) {
        int n;
        if (c == '\u0000') {
            if (this.containsNull) {
                return true;
            }
            n = this.n;
            this.containsNull = true;
        } else {
            char[] cArray = this.key;
            n = HashCommon.mix(c) & this.mask;
            char c2 = cArray[n];
            if (c2 != '\u0000') {
                if (c2 == c) {
                    return true;
                }
                while ((c2 = cArray[n = n + 1 & this.mask]) != '\u0000') {
                    if (c2 != c) continue;
                    return true;
                }
            }
            cArray[n] = c;
        }
        if (this.size == 0) {
            this.first = this.last = n;
            this.link[n] = -1L;
        } else {
            int n2 = this.last;
            this.link[n2] = this.link[n2] ^ (this.link[this.last] ^ (long)n & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            this.link[n] = ((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL;
            this.last = n;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return false;
    }

    protected final void shiftKeys(int n) {
        char[] cArray = this.key;
        while (true) {
            char c;
            int n2 = n;
            n = n2 + 1 & this.mask;
            while (true) {
                if ((c = cArray[n]) == '\u0000') {
                    cArray[n2] = '\u0000';
                    return;
                }
                int n3 = HashCommon.mix(c) & this.mask;
                if (n2 <= n ? n2 >= n3 || n3 > n : n2 >= n3 && n3 > n) break;
                n = n + 1 & this.mask;
            }
            cArray[n2] = c;
            this.fixPointers(n, n2);
        }
    }

    private boolean removeEntry(int n) {
        --this.size;
        this.fixPointers(n);
        this.shiftKeys(n);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return false;
    }

    private boolean removeNullEntry() {
        this.containsNull = false;
        this.key[this.n] = '\u0000';
        --this.size;
        this.fixPointers(this.n);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return false;
    }

    @Override
    public boolean remove(char c) {
        if (c == '\u0000') {
            if (this.containsNull) {
                return this.removeNullEntry();
            }
            return true;
        }
        char[] cArray = this.key;
        int n = HashCommon.mix(c) & this.mask;
        char c2 = cArray[n];
        if (c2 == '\u0000') {
            return true;
        }
        if (c == c2) {
            return this.removeEntry(n);
        }
        do {
            if ((c2 = cArray[n = n + 1 & this.mask]) != '\u0000') continue;
            return true;
        } while (c != c2);
        return this.removeEntry(n);
    }

    @Override
    public boolean contains(char c) {
        if (c == '\u0000') {
            return this.containsNull;
        }
        char[] cArray = this.key;
        int n = HashCommon.mix(c) & this.mask;
        char c2 = cArray[n];
        if (c2 == '\u0000') {
            return true;
        }
        if (c == c2) {
            return false;
        }
        do {
            if ((c2 = cArray[n = n + 1 & this.mask]) != '\u0000') continue;
            return true;
        } while (c != c2);
        return false;
    }

    public char removeFirstChar() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        int n = this.first;
        this.first = (int)this.link[n];
        if (0 <= this.first) {
            int n2 = this.first;
            this.link[n2] = this.link[n2] | 0xFFFFFFFF00000000L;
        }
        char c = this.key[n];
        --this.size;
        if (c == '\u0000') {
            this.containsNull = false;
            this.key[this.n] = '\u0000';
        } else {
            this.shiftKeys(n);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return c;
    }

    public char removeLastChar() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        int n = this.last;
        this.last = (int)(this.link[n] >>> 32);
        if (0 <= this.last) {
            int n2 = this.last;
            this.link[n2] = this.link[n2] | 0xFFFFFFFFL;
        }
        char c = this.key[n];
        --this.size;
        if (c == '\u0000') {
            this.containsNull = false;
            this.key[this.n] = '\u0000';
        } else {
            this.shiftKeys(n);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return c;
    }

    private void moveIndexToFirst(int n) {
        if (this.size == 1 || this.first == n) {
            return;
        }
        if (this.last == n) {
            int n2 = this.last = (int)(this.link[n] >>> 32);
            this.link[n2] = this.link[n2] | 0xFFFFFFFFL;
        } else {
            long l = this.link[n];
            int n3 = (int)(l >>> 32);
            int n4 = (int)l;
            int n5 = n3;
            this.link[n5] = this.link[n5] ^ (this.link[n3] ^ l & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            int n6 = n4;
            this.link[n6] = this.link[n6] ^ (this.link[n4] ^ l & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
        }
        int n7 = this.first;
        this.link[n7] = this.link[n7] ^ (this.link[this.first] ^ ((long)n & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
        this.link[n] = 0xFFFFFFFF00000000L | (long)this.first & 0xFFFFFFFFL;
        this.first = n;
    }

    private void moveIndexToLast(int n) {
        if (this.size == 1 || this.last == n) {
            return;
        }
        if (this.first == n) {
            int n2 = this.first = (int)this.link[n];
            this.link[n2] = this.link[n2] | 0xFFFFFFFF00000000L;
        } else {
            long l = this.link[n];
            int n3 = (int)(l >>> 32);
            int n4 = (int)l;
            int n5 = n3;
            this.link[n5] = this.link[n5] ^ (this.link[n3] ^ l & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            int n6 = n4;
            this.link[n6] = this.link[n6] ^ (this.link[n4] ^ l & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
        }
        int n7 = this.last;
        this.link[n7] = this.link[n7] ^ (this.link[this.last] ^ (long)n & 0xFFFFFFFFL) & 0xFFFFFFFFL;
        this.link[n] = ((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL;
        this.last = n;
    }

    public boolean addAndMoveToFirst(char c) {
        int n;
        if (c == '\u0000') {
            if (this.containsNull) {
                this.moveIndexToFirst(this.n);
                return true;
            }
            this.containsNull = true;
            n = this.n;
        } else {
            char[] cArray = this.key;
            n = HashCommon.mix(c) & this.mask;
            while (cArray[n] != '\u0000') {
                if (c == cArray[n]) {
                    this.moveIndexToFirst(n);
                    return true;
                }
                n = n + 1 & this.mask;
            }
        }
        this.key[n] = c;
        if (this.size == 0) {
            this.first = this.last = n;
            this.link[n] = -1L;
        } else {
            int n2 = this.first;
            this.link[n2] = this.link[n2] ^ (this.link[this.first] ^ ((long)n & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
            this.link[n] = 0xFFFFFFFF00000000L | (long)this.first & 0xFFFFFFFFL;
            this.first = n;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size, this.f));
        }
        return false;
    }

    public boolean addAndMoveToLast(char c) {
        int n;
        if (c == '\u0000') {
            if (this.containsNull) {
                this.moveIndexToLast(this.n);
                return true;
            }
            this.containsNull = true;
            n = this.n;
        } else {
            char[] cArray = this.key;
            n = HashCommon.mix(c) & this.mask;
            while (cArray[n] != '\u0000') {
                if (c == cArray[n]) {
                    this.moveIndexToLast(n);
                    return true;
                }
                n = n + 1 & this.mask;
            }
        }
        this.key[n] = c;
        if (this.size == 0) {
            this.first = this.last = n;
            this.link[n] = -1L;
        } else {
            int n2 = this.last;
            this.link[n2] = this.link[n2] ^ (this.link[this.last] ^ (long)n & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            this.link[n] = ((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL;
            this.last = n;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size, this.f));
        }
        return false;
    }

    @Override
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNull = false;
        Arrays.fill(this.key, '\u0000');
        this.last = -1;
        this.first = -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    protected void fixPointers(int n) {
        if (this.size == 0) {
            this.last = -1;
            this.first = -1;
            return;
        }
        if (this.first == n) {
            this.first = (int)this.link[n];
            if (0 <= this.first) {
                int n2 = this.first;
                this.link[n2] = this.link[n2] | 0xFFFFFFFF00000000L;
            }
            return;
        }
        if (this.last == n) {
            this.last = (int)(this.link[n] >>> 32);
            if (0 <= this.last) {
                int n3 = this.last;
                this.link[n3] = this.link[n3] | 0xFFFFFFFFL;
            }
            return;
        }
        long l = this.link[n];
        int n4 = (int)(l >>> 32);
        int n5 = (int)l;
        int n6 = n4;
        this.link[n6] = this.link[n6] ^ (this.link[n4] ^ l & 0xFFFFFFFFL) & 0xFFFFFFFFL;
        int n7 = n5;
        this.link[n7] = this.link[n7] ^ (this.link[n5] ^ l & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
    }

    protected void fixPointers(int n, int n2) {
        if (this.size == 1) {
            this.first = this.last = n2;
            this.link[n2] = -1L;
            return;
        }
        if (this.first == n) {
            this.first = n2;
            int n3 = (int)this.link[n];
            this.link[n3] = this.link[n3] ^ (this.link[(int)this.link[n]] ^ ((long)n2 & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
            this.link[n2] = this.link[n];
            return;
        }
        if (this.last == n) {
            this.last = n2;
            int n4 = (int)(this.link[n] >>> 32);
            this.link[n4] = this.link[n4] ^ (this.link[(int)(this.link[n] >>> 32)] ^ (long)n2 & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            this.link[n2] = this.link[n];
            return;
        }
        long l = this.link[n];
        int n5 = (int)(l >>> 32);
        int n6 = (int)l;
        int n7 = n5;
        this.link[n7] = this.link[n7] ^ (this.link[n5] ^ (long)n2 & 0xFFFFFFFFL) & 0xFFFFFFFFL;
        int n8 = n6;
        this.link[n8] = this.link[n8] ^ (this.link[n6] ^ ((long)n2 & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
        this.link[n2] = l;
    }

    @Override
    public char firstChar() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.first];
    }

    @Override
    public char lastChar() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.last];
    }

    @Override
    public CharSortedSet tailSet(char c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CharSortedSet headSet(char c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CharSortedSet subSet(char c, char c2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CharComparator comparator() {
        return null;
    }

    @Override
    public CharListIterator iterator(char c) {
        return new SetIterator(this, c);
    }

    @Override
    public CharListIterator iterator() {
        return new SetIterator(this);
    }

    public boolean trim() {
        int n = HashCommon.arraySize(this.size, this.f);
        if (n >= this.n || this.size > HashCommon.maxFill(n, this.f)) {
            return false;
        }
        try {
            this.rehash(n);
        } catch (OutOfMemoryError outOfMemoryError) {
            return true;
        }
        return false;
    }

    public boolean trim(int n) {
        int n2 = HashCommon.nextPowerOfTwo((int)Math.ceil((float)n / this.f));
        if (n2 >= n || this.size > HashCommon.maxFill(n2, this.f)) {
            return false;
        }
        try {
            this.rehash(n2);
        } catch (OutOfMemoryError outOfMemoryError) {
            return true;
        }
        return false;
    }

    protected void rehash(int n) {
        char[] cArray = this.key;
        int n2 = n - 1;
        char[] cArray2 = new char[n + 1];
        int n3 = this.first;
        int n4 = -1;
        int n5 = -1;
        long[] lArray = this.link;
        long[] lArray2 = new long[n + 1];
        this.first = -1;
        int n6 = this.size;
        while (n6-- != 0) {
            int n7;
            if (cArray[n3] == '\u0000') {
                n7 = n;
            } else {
                n7 = HashCommon.mix(cArray[n3]) & n2;
                while (cArray2[n7] != '\u0000') {
                    n7 = n7 + 1 & n2;
                }
            }
            cArray2[n7] = cArray[n3];
            if (n4 != -1) {
                int n8 = n5;
                lArray2[n8] = lArray2[n8] ^ (lArray2[n5] ^ (long)n7 & 0xFFFFFFFFL) & 0xFFFFFFFFL;
                int n9 = n7;
                lArray2[n9] = lArray2[n9] ^ (lArray2[n7] ^ ((long)n5 & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
                n5 = n7;
            } else {
                n5 = this.first = n7;
                lArray2[n7] = -1L;
            }
            int n10 = n3;
            n3 = (int)lArray[n3];
            n4 = n10;
        }
        this.link = lArray2;
        this.last = n5;
        if (n5 != -1) {
            int n11 = n5;
            lArray2[n11] = lArray2[n11] | 0xFFFFFFFFL;
        }
        this.n = n;
        this.mask = n2;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = cArray2;
    }

    public CharLinkedOpenHashSet clone() {
        CharLinkedOpenHashSet charLinkedOpenHashSet;
        try {
            charLinkedOpenHashSet = (CharLinkedOpenHashSet)super.clone();
        } catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new InternalError();
        }
        charLinkedOpenHashSet.key = (char[])this.key.clone();
        charLinkedOpenHashSet.containsNull = this.containsNull;
        charLinkedOpenHashSet.link = (long[])this.link.clone();
        return charLinkedOpenHashSet;
    }

    @Override
    public int hashCode() {
        int n = 0;
        int n2 = this.realSize();
        int n3 = 0;
        while (n2-- != 0) {
            while (this.key[n3] == '\u0000') {
                ++n3;
            }
            n += this.key[n3];
            ++n3;
        }
        return n;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        CharListIterator charListIterator = this.iterator();
        objectOutputStream.defaultWriteObject();
        int n = this.size;
        while (n-- != 0) {
            objectOutputStream.writeChar(charListIterator.nextChar());
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        this.key = new char[this.n + 1];
        char[] cArray = this.key;
        this.link = new long[this.n + 1];
        long[] lArray = this.link;
        int n = -1;
        this.last = -1;
        this.first = -1;
        int n2 = this.size;
        while (n2-- != 0) {
            int n3;
            char c = objectInputStream.readChar();
            if (c == '\u0000') {
                n3 = this.n;
                this.containsNull = true;
            } else {
                n3 = HashCommon.mix(c) & this.mask;
                if (cArray[n3] != '\u0000') {
                    while (cArray[n3 = n3 + 1 & this.mask] != '\u0000') {
                    }
                }
            }
            cArray[n3] = c;
            if (this.first != -1) {
                int n4 = n;
                lArray[n4] = lArray[n4] ^ (lArray[n] ^ (long)n3 & 0xFFFFFFFFL) & 0xFFFFFFFFL;
                int n5 = n3;
                lArray[n5] = lArray[n5] ^ (lArray[n3] ^ ((long)n & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
                n = n3;
                continue;
            }
            n = this.first = n3;
            int n6 = n3;
            lArray[n6] = lArray[n6] | 0xFFFFFFFF00000000L;
        }
        this.last = n;
        if (n != -1) {
            int n7 = n;
            lArray[n7] = lArray[n7] | 0xFFFFFFFFL;
        }
    }

    private void checkTable() {
    }

    @Override
    public CharBidirectionalIterator iterator() {
        return this.iterator();
    }

    @Override
    public CharBidirectionalIterator iterator(char c) {
        return this.iterator(c);
    }

    @Override
    public CharIterator iterator() {
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

    public Object clone() throws CloneNotSupportedException {
        return this.clone();
    }

    private class SetIterator
    implements CharListIterator {
        int prev;
        int next;
        int curr;
        int index;
        final CharLinkedOpenHashSet this$0;

        SetIterator(CharLinkedOpenHashSet charLinkedOpenHashSet) {
            this.this$0 = charLinkedOpenHashSet;
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            this.next = charLinkedOpenHashSet.first;
            this.index = 0;
        }

        SetIterator(CharLinkedOpenHashSet charLinkedOpenHashSet, char c) {
            this.this$0 = charLinkedOpenHashSet;
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            if (c == '\u0000') {
                if (charLinkedOpenHashSet.containsNull) {
                    this.next = (int)charLinkedOpenHashSet.link[charLinkedOpenHashSet.n];
                    this.prev = charLinkedOpenHashSet.n;
                    return;
                }
                throw new NoSuchElementException("The key " + c + " does not belong to this set.");
            }
            if (charLinkedOpenHashSet.key[charLinkedOpenHashSet.last] == c) {
                this.prev = charLinkedOpenHashSet.last;
                this.index = charLinkedOpenHashSet.size;
                return;
            }
            char[] cArray = charLinkedOpenHashSet.key;
            int n = HashCommon.mix(c) & charLinkedOpenHashSet.mask;
            while (cArray[n] != '\u0000') {
                if (cArray[n] == c) {
                    this.next = (int)charLinkedOpenHashSet.link[n];
                    this.prev = n;
                    return;
                }
                n = n + 1 & charLinkedOpenHashSet.mask;
            }
            throw new NoSuchElementException("The key " + c + " does not belong to this set.");
        }

        @Override
        public boolean hasNext() {
            return this.next != -1;
        }

        @Override
        public boolean hasPrevious() {
            return this.prev != -1;
        }

        @Override
        public char nextChar() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = this.next;
            this.next = (int)this.this$0.link[this.curr];
            this.prev = this.curr;
            if (this.index >= 0) {
                ++this.index;
            }
            return this.this$0.key[this.curr];
        }

        @Override
        public char previousChar() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = this.prev;
            this.prev = (int)(this.this$0.link[this.curr] >>> 32);
            this.next = this.curr;
            if (this.index >= 0) {
                --this.index;
            }
            return this.this$0.key[this.curr];
        }

        private final void ensureIndexKnown() {
            if (this.index >= 0) {
                return;
            }
            if (this.prev == -1) {
                this.index = 0;
                return;
            }
            if (this.next == -1) {
                this.index = this.this$0.size;
                return;
            }
            int n = this.this$0.first;
            this.index = 1;
            while (n != this.prev) {
                n = (int)this.this$0.link[n];
                ++this.index;
            }
        }

        @Override
        public int nextIndex() {
            this.ensureIndexKnown();
            return this.index;
        }

        @Override
        public int previousIndex() {
            this.ensureIndexKnown();
            return this.index - 1;
        }

        @Override
        public void remove() {
            this.ensureIndexKnown();
            if (this.curr == -1) {
                throw new IllegalStateException();
            }
            if (this.curr == this.prev) {
                --this.index;
                this.prev = (int)(this.this$0.link[this.curr] >>> 32);
            } else {
                this.next = (int)this.this$0.link[this.curr];
            }
            --this.this$0.size;
            if (this.prev == -1) {
                this.this$0.first = this.next;
            } else {
                int n = this.prev;
                this.this$0.link[n] = this.this$0.link[n] ^ (this.this$0.link[this.prev] ^ (long)this.next & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            }
            if (this.next == -1) {
                this.this$0.last = this.prev;
            } else {
                int n = this.next;
                this.this$0.link[n] = this.this$0.link[n] ^ (this.this$0.link[this.next] ^ ((long)this.prev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
            }
            int n = this.curr;
            this.curr = -1;
            if (n != this.this$0.n) {
                char[] cArray = this.this$0.key;
                while (true) {
                    char c;
                    int n2 = n;
                    n = n2 + 1 & this.this$0.mask;
                    while (true) {
                        if ((c = cArray[n]) == '\u0000') {
                            cArray[n2] = '\u0000';
                            return;
                        }
                        int n3 = HashCommon.mix(c) & this.this$0.mask;
                        if (n2 <= n ? n2 >= n3 || n3 > n : n2 >= n3 && n3 > n) break;
                        n = n + 1 & this.this$0.mask;
                    }
                    cArray[n2] = c;
                    if (this.next == n) {
                        this.next = n2;
                    }
                    if (this.prev == n) {
                        this.prev = n2;
                    }
                    this.this$0.fixPointers(n, n2);
                }
            }
            this.this$0.containsNull = false;
            this.this$0.key[this.this$0.n] = '\u0000';
        }
    }
}

