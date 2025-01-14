package net.minecraft.network.play.server;

import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.Difficulty;

import java.io.IOException;

public class SServerDifficultyPacket implements IPacket<IClientPlayNetHandler>
{
    private Difficulty difficulty;
    private boolean difficultyLocked;

    public SServerDifficultyPacket()
    {
    }

    public SServerDifficultyPacket(Difficulty difficultyIn, boolean difficultyLockedIn)
    {
        this.difficulty = difficultyIn;
        this.difficultyLocked = difficultyLockedIn;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(IClientPlayNetHandler handler)
    {
        handler.handleServerDifficulty(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.difficulty = Difficulty.byId(buf.readUnsignedByte());
        this.difficultyLocked = buf.readBoolean();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(this.difficulty.getId());
        buf.writeBoolean(this.difficultyLocked);
    }

    public boolean isDifficultyLocked()
    {
        return this.difficultyLocked;
    }

    public Difficulty getDifficulty()
    {
        return this.difficulty;
    }
}
