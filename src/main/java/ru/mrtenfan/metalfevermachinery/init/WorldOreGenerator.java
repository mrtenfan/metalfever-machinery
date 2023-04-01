package ru.mrtenfan.metalfevermachinery.init;

import java.util.Random;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import ru.mrtenfan.metalfevermachinery.ConfigFile;

public class WorldOreGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
	    generateOverworld(random, chunkX, chunkZ, world);
	}

	private void generateOverworld(Random random, int chunkX, int chunkZ, World world) {
		generateOverworld(world, random, chunkX * 16, chunkZ * 16);
	}

	private void generateOverworld(World world, Random random, int chunkX, int chunkZ) {
		if(ConfigFile.enabPodstavia)
			addOreSpawn(MFMBlocks.podstavia_ore, Blocks.stone, world, random, chunkX, chunkZ, 16, 16, ConfigFile.podstaviaSpawn[0], ConfigFile.podstaviaSpawn[1], ConfigFile.podstaviaSpawn[2], 
					ConfigFile.podstaviaSpawn[3], ConfigFile.podstaviaSpawn[4], ConfigFile.podstaviaSpawn[5], ConfigFile.podstaviaSpawn[6]);
	}
	
	public static void addOreSpawn(Block ore, Block replace, World world, Random rand, int blockXPos, int blockZPos, int maxX, int maxZ, int minVeinSize, int maxVeinSize, int minVeinsPerChunk, int maxVeinsPerChunk,
			int chanceToSpawn, int minY, int maxY) {
		if (rand.nextInt(101) < (100 - chanceToSpawn)) return;
		int veins = rand.nextInt(maxVeinsPerChunk - minVeinsPerChunk + 1) + minVeinsPerChunk;
		for (int i = 0; i < veins; i++) {
			int posX = blockXPos + rand.nextInt(maxX);
			int posY = minY + rand.nextInt(maxY - minY);
			int posZ = blockZPos + rand.nextInt(maxZ);
			int VeinSize = minVeinSize + rand.nextInt(maxVeinSize - minVeinSize + 1);
			(new WorldGenMinable(ore, VeinSize, replace)).generate(world, rand, posX, posY, posZ);
		}
	}
}
