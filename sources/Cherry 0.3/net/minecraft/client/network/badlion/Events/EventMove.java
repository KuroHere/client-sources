// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.network.badlion.Events;

import net.minecraft.client.network.badlion.memes.events.Event;

public class EventMove implements Event
{
    public double x;
    public double y;
    public double z;
    
    public EventMove(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
}
