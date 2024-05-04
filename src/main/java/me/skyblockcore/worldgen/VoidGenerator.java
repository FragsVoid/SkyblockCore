package me.skyblockcore.worldgen;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

public class VoidGenerator extends ChunkGenerator {

    private final Biome theVoid = Biome.THE_VOID;

    @Override
    @Nonnull
    public ChunkGenerator.ChunkData generateChunkData(World world, Random random, int chunkx, int chunkz, ChunkGenerator.BiomeGrid biome) {
        ChunkGenerator.ChunkData data = this.createChunkData(world);
        biome.setBiome(chunkx, chunkz, theVoid);
        return data;
    }

    public boolean canSpawn(World world, int x, int z) {
        return true;
    }
}
