/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import it.unimi.dsi.fastutil.doubles.DoubleComparator;
import java.util.Arrays;

public final class DoubleIndirectHeaps {
    static final boolean $assertionsDisabled = !DoubleIndirectHeaps.class.desiredAssertionStatus();

    private DoubleIndirectHeaps() {
    }

    public static int downHeap(double[] dArray, int[] nArray, int[] nArray2, int n, int n2, DoubleComparator doubleComparator) {
        if (!$assertionsDisabled && n2 >= n) {
            throw new AssertionError();
        }
        int n3 = nArray[n2];
        double d = dArray[n3];
        if (doubleComparator == null) {
            int n4;
            while ((n4 = (n2 << 1) + 1) < n) {
                int n5 = nArray[n4];
                int n6 = n4 + 1;
                if (n6 < n && Double.compare(dArray[nArray[n6]], dArray[n5]) < 0) {
                    n4 = n6;
                    n5 = nArray[n4];
                }
                if (Double.compare(d, dArray[n5]) > 0) {
                    nArray[n2] = n5;
                    nArray2[nArray[n2]] = n2;
                    n2 = n4;
                    continue;
                }
                break;
            }
        } else {
            int n7;
            while ((n7 = (n2 << 1) + 1) < n) {
                int n8 = nArray[n7];
                int n9 = n7 + 1;
                if (n9 < n && doubleComparator.compare(dArray[nArray[n9]], dArray[n8]) < 0) {
                    n7 = n9;
                    n8 = nArray[n7];
                }
                if (doubleComparator.compare(d, dArray[n8]) > 0) {
                    nArray[n2] = n8;
                    nArray2[nArray[n2]] = n2;
                    n2 = n7;
                    continue;
                }
                break;
            }
        }
        nArray[n2] = n3;
        nArray2[n3] = n2;
        return n2;
    }

    public static int upHeap(double[] dArray, int[] nArray, int[] nArray2, int n, int n2, DoubleComparator doubleComparator) {
        if (!$assertionsDisabled && n2 >= n) {
            throw new AssertionError();
        }
        int n3 = nArray[n2];
        double d = dArray[n3];
        if (doubleComparator == null) {
            int n4;
            int n5;
            while (n2 != 0 && Double.compare(dArray[n5 = nArray[n4 = n2 - 1 >>> 1]], d) > 0) {
                nArray[n2] = n5;
                nArray2[nArray[n2]] = n2;
                n2 = n4;
            }
        } else {
            int n6;
            int n7;
            while (n2 != 0 && doubleComparator.compare(dArray[n7 = nArray[n6 = n2 - 1 >>> 1]], d) > 0) {
                nArray[n2] = n7;
                nArray2[nArray[n2]] = n2;
                n2 = n6;
            }
        }
        nArray[n2] = n3;
        nArray2[n3] = n2;
        return n2;
    }

    public static void makeHeap(double[] dArray, int n, int n2, int[] nArray, int[] nArray2, DoubleComparator doubleComparator) {
        DoubleArrays.ensureOffsetLength(dArray, n, n2);
        if (nArray.length < n2) {
            throw new IllegalArgumentException("The heap length (" + nArray.length + ") is smaller than the number of elements (" + n2 + ")");
        }
        if (nArray2.length < dArray.length) {
            throw new IllegalArgumentException("The inversion array length (" + nArray.length + ") is smaller than the length of the reference array (" + dArray.length + ")");
        }
        Arrays.fill(nArray2, 0, dArray.length, -1);
        int n3 = n2;
        while (n3-- != 0) {
            nArray[n3] = n + n3;
            nArray2[nArray[n3]] = n3;
        }
        n3 = n2 >>> 1;
        while (n3-- != 0) {
            DoubleIndirectHeaps.downHeap(dArray, nArray, nArray2, n2, n3, doubleComparator);
        }
    }

    public static void makeHeap(double[] dArray, int[] nArray, int[] nArray2, int n, DoubleComparator doubleComparator) {
        int n2 = n >>> 1;
        while (n2-- != 0) {
            DoubleIndirectHeaps.downHeap(dArray, nArray, nArray2, n, n2, doubleComparator);
        }
    }
}

