package net.minecraft.world.chunk.storage;

import java.io.IOException;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public interface IChunkLoader {
   void saveExtraData();

   void chunkTick();

   void saveChunk(World var1, Chunk var2) throws MinecraftException, IOException;

   Chunk loadChunk(World var1, int var2, int var3) throws IOException;

   void saveExtraChunkData(World var1, Chunk var2) throws IOException;
}
