/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.queues.MpscArrayQueueProducerLimitField;

abstract class MpscArrayQueueL2Pad<E>
extends MpscArrayQueueProducerLimitField<E> {
    long p00;
    long p01;
    long p02;
    long p03;
    long p04;
    long p05;
    long p06;
    long p07;
    long p10;
    long p11;
    long p12;
    long p13;
    long p14;
    long p15;
    long p16;

    public MpscArrayQueueL2Pad(int n) {
        super(n);
    }
}

