// 
// Decompiled by Procyon v0.5.30
// 

package me.chrest.utils;

public class TimeUtils
{
    private long prevMS;
    
    public TimeUtils() {
        this.prevMS = 0L;
    }
    
    public boolean delay(final float milliSec) {
        return this.getTime() - this.prevMS >= milliSec;
    }
    
    public void reset() {
        this.prevMS = this.getTime();
    }
    
    public long getTime() {
        return System.nanoTime() / 1000000L;
    }
    
    public long getDifference() {
        return this.getTime() - this.prevMS;
    }
}
