package ru.mrtenfan.metalfevermachinery;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigFile {
	
	private final String twk = "Tweaks";
	
	private Configuration config;
	//Tweaks
	public static int alloyingSpeed;
	public static int assemblingSpeed;
	public static int centrifugingSpeed;
	public static int smeltingSpeed;
	public static int rollingSpeed;
	public static int alloyingSpeedTMF;
	public static int RFPerTempTMF;
	public static boolean enableDamageFromTemp;
	public static int[] BMSettings;
	public static int[] AMSettings;
	public static boolean enabPodstavia;
	public static int[] podstaviaSpawn;
	public static boolean doesTEChangeLoading;

	public ConfigFile(String directory) {
		config = new Configuration(new File(directory + "/MetalFever/Machinery.cfg"));
		config.load();
		
		config.addCustomCategoryComment(twk, "Here you can configure Metal Fever the way you need it\nAnd how it possible :)");
		
		alloyingSpeed=config.get(twk, "Alloying speed in ticks; 200 = furnace speed", 150).getInt(150);
		assemblingSpeed=config.get(twk, "Assembling speed in ticks; 200 = furnace speed", 500).getInt(500);
		centrifugingSpeed=config.get(twk, "Centrifuging speed in ticks; 200 = furnace speed", 900).getInt(900);
		smeltingSpeed=config.get(twk, "Smelting speed in ticks; 200 = furnace speed", 150).getInt(150);
		rollingSpeed=config.get(twk, "Rolling speed in ticks; 200 = furnace speed", 400).getInt(400);
		alloyingSpeedTMF=config.get(twk, "Alloying speed in ticks(Thermo-material furnace)", 600).getInt(600);
		RFPerTempTMF=config.get(twk, "RF per 1 degree in thermo-material furnace", 5).getInt(5);
		
		enableDamageFromTemp=config.get(twk, "While this is \"true\" thermo-material furnace will damage nearby players", false).getBoolean(false);
		
		BMSettings=config.get(twk, "Basic machine settings: capacity, in-out of energy", new int[] {30000, 500}).getIntList();
		AMSettings=config.get(twk, "Advanced machine settings: capacity, in-out of energy", new int[] {50000, 750}).getIntList();
		
		enabPodstavia=config.get(twk, "Is podstavia ore generated", true).getBoolean(true);
		podstaviaSpawn=config.get(twk, "Settings for generation podstavia ore: minVeinSize, maxVeinSize, minVeinsPerChunk, maxVeinsPerChunk, chanceToSpawn, minY, maxY", new int[] {2, 6, 10, 20, 110, 15, 75}).setDefaultValues(new int[] {2, 6, 10, 20, 110, 15, 75}).getIntList();
    	
		doesTEChangeLoading=config.get(twk, "Does the \"Thermal Expansion\" presence remove repetitive machines in \"Metal Fever - Machinery\"", true).getBoolean(true);
		
		if(config.hasChanged()) {
			if(BMSettings.length < 2)
				throw new ArrayIndexOutOfBoundsException("The length of the BMSettings array is less than 2");
			if(AMSettings.length < 2)
				throw new ArrayIndexOutOfBoundsException("The length of the AMSettings array is less than 2");
		}
		
		config.save();
	}
}
