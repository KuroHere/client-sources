// 
// Decompiled by Procyon v0.5.30
// 

package jaco.mp3.resources;

import java.io.IOException;

public interface Source
{
    public static final long LENGTH_UNKNOWN = -1L;
    
    int read(final byte[] p0, final int p1, final int p2) throws IOException;
    
    boolean willReadBlock();
    
    boolean isSeekable();
    
    long length();
    
    long tell();
    
    long seek(final long p0);
}
