package net.minecraft.network.play.server;

import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.io.IOException;

public class STitlePacket implements IPacket<IClientPlayNetHandler>
{
    private STitlePacket.Type type;
    private ITextComponent message;
    private int fadeInTime;
    private int displayTime;
    private int fadeOutTime;

    public STitlePacket()
    {
    }

    public STitlePacket(STitlePacket.Type typeIn, ITextComponent messageIn)
    {
        this(typeIn, messageIn, -1, -1, -1);
    }

    public STitlePacket(int fadeInTimeIn, int displayTimeIn, int fadeOutTimeIn)
    {
        this(STitlePacket.Type.TIMES, (ITextComponent)null, fadeInTimeIn, displayTimeIn, fadeOutTimeIn);
    }

    public STitlePacket(STitlePacket.Type typeIn, @Nullable ITextComponent messageIn, int fadeInTimeIn, int displayTimeIn, int fadeOutTimeIn)
    {
        this.type = typeIn;
        this.message = messageIn;
        this.fadeInTime = fadeInTimeIn;
        this.displayTime = displayTimeIn;
        this.fadeOutTime = fadeOutTimeIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.type = buf.readEnumValue(STitlePacket.Type.class);

        if (this.type == STitlePacket.Type.TITLE || this.type == STitlePacket.Type.SUBTITLE || this.type == STitlePacket.Type.ACTIONBAR)
        {
            this.message = buf.readTextComponent();
        }

        if (this.type == STitlePacket.Type.TIMES)
        {
            this.fadeInTime = buf.readInt();
            this.displayTime = buf.readInt();
            this.fadeOutTime = buf.readInt();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeEnumValue(this.type);

        if (this.type == STitlePacket.Type.TITLE || this.type == STitlePacket.Type.SUBTITLE || this.type == STitlePacket.Type.ACTIONBAR)
        {
            buf.writeTextComponent(this.message);
        }

        if (this.type == STitlePacket.Type.TIMES)
        {
            buf.writeInt(this.fadeInTime);
            buf.writeInt(this.displayTime);
            buf.writeInt(this.fadeOutTime);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(IClientPlayNetHandler handler)
    {
        handler.handleTitle(this);
    }

    public STitlePacket.Type getType()
    {
        return this.type;
    }

    public ITextComponent getMessage()
    {
        return this.message;
    }

    public int getFadeInTime()
    {
        return this.fadeInTime;
    }

    public int getDisplayTime()
    {
        return this.displayTime;
    }

    public int getFadeOutTime()
    {
        return this.fadeOutTime;
    }

    public static enum Type
    {
        TITLE,
        SUBTITLE,
        ACTIONBAR,
        TIMES,
        CLEAR,
        RESET;
    }
}
