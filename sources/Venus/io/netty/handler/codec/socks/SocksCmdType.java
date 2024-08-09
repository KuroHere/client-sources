/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package io.netty.handler.codec.socks;

public enum SocksCmdType {
    CONNECT(1),
    BIND(2),
    UDP(3),
    UNKNOWN(-1);

    private final byte b;

    private SocksCmdType(byte by) {
        this.b = by;
    }

    @Deprecated
    public static SocksCmdType fromByte(byte by) {
        return SocksCmdType.valueOf(by);
    }

    public static SocksCmdType valueOf(byte by) {
        for (SocksCmdType socksCmdType : SocksCmdType.values()) {
            if (socksCmdType.b != by) continue;
            return socksCmdType;
        }
        return UNKNOWN;
    }

    public byte byteValue() {
        return this.b;
    }
}

