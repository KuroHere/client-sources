package net.minecraft.src;

import java.io.*;

public class NBTTagShort extends NBTBase
{
    public short data;
    
    public NBTTagShort(final String par1Str) {
        super(par1Str);
    }
    
    public NBTTagShort(final String par1Str, final short par2) {
        super(par1Str);
        this.data = par2;
    }
    
    @Override
    void write(final DataOutput par1DataOutput) throws IOException {
        par1DataOutput.writeShort(this.data);
    }
    
    @Override
    void load(final DataInput par1DataInput) throws IOException {
        this.data = par1DataInput.readShort();
    }
    
    @Override
    public byte getId() {
        return 2;
    }
    
    @Override
    public String toString() {
        return new StringBuilder().append(this.data).toString();
    }
    
    @Override
    public NBTBase copy() {
        return new NBTTagShort(this.getName(), this.data);
    }
    
    @Override
    public boolean equals(final Object par1Obj) {
        if (super.equals(par1Obj)) {
            final NBTTagShort var2 = (NBTTagShort)par1Obj;
            return this.data == var2.data;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return super.hashCode() ^ this.data;
    }
}
