package ooo.cpacket.ruby.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SoptWatchXDD
{
    private long lastTime;
    
    public SoptWatchXDD() {
        this.reset();
    }
    
    public long getCurrentTime() {
        return System.nanoTime() / 1000000L;
    }
    
    public long getLastTime() {
        return this.lastTime;
    }
    
    public long getDifference() {
        return this.getCurrentTime() - this.lastTime;
    }
    
    public void reset() {
        this.lastTime = this.getCurrentTime();
    }
    
    public boolean hasReached(final long milliseconds) {
        return this.getDifference() >= milliseconds;
    }
    
    public SoptWatchXDD hasTimeElapsed(final long milliseconds) {
        if (this.getDifference() >= milliseconds) {
            return this;
        }
        return null;
    }
    
}
