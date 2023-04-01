package ru.mrtenfan.metalfevermachinery;

import java.util.ArrayList;

import cofh.CoFHCore;
import cofh.thermalexpansion.ThermalExpansion;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
//import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import minetweaker.MineTweakerAPI;
import net.minecraft.creativetab.CreativeTabs;
import ru.mrtenfan.MTFCore.MTFCoreMain;
import ru.mrtenfan.metalfeverbasis.MetalFeverMain;
import ru.mrtenfan.metalfevermachinery.gui.GUIHandler;
import ru.mrtenfan.metalfevermachinery.init.MFMBlocks;
import ru.mrtenfan.metalfevermachinery.init.MFMItems;
import ru.mrtenfan.metalfevermachinery.init.MFMLibOreDict;
import ru.mrtenfan.metalfevermachinery.init.MFMRecipes;
import ru.mrtenfan.metalfevermachinery.init.WorldOreGenerator;
import ru.mrtenfan.metalfevermachinery.integration.minetweaker.MTAlloySmelter;
import ru.mrtenfan.metalfevermachinery.integration.minetweaker.MTAssembler;
import ru.mrtenfan.metalfevermachinery.integration.minetweaker.MTCentrifuge;
import ru.mrtenfan.metalfevermachinery.integration.minetweaker.MTElectricalFurnace;
import ru.mrtenfan.metalfevermachinery.integration.minetweaker.MTMetalProcessor;
import ru.mrtenfan.metalfevermachinery.integration.minetweaker.MTThermoMeterialFurnace;
import ru.mrtenfan.metalfevermachinery.packages.NetworkHandler;

@Mod(modid = MetalFeverMachinery.modID, name = MetalFeverMachinery.modName, version = MetalFeverMachinery.version, dependencies = MetalFeverMachinery.dependencies)
public class MetalFeverMachinery {

	public static final String modID = "MetalFeverMachinery";
	public static final String modName = "Metal Fever - Machinery";
	public static final String version = "1.2.0.9";
	public static final String dependencies = MTFCoreMain.version_group + MetalFeverMain.version_group + CoFHCore.version_group;
	public static final String version_max = "1.2.0.999";

	public static final String version_group = "required-after:" + modID + "@[" + version + "," + version_max + "];";

	@Instance("MetalFeverMachinery")
	public static MetalFeverMachinery instance;
	
	public static CreativeTabs TabMachineryMain = new TabMachineryMain("tab_machinery_main");
	public static CreativeTabs TabMachineryRes = new TabMachineryRes("tab_machinery_resource");
	
	public static WorldOreGenerator oregenerator = new WorldOreGenerator();
	
	private static ArrayList<String> registeredName = new ArrayList<String>();
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		new ConfigFile(e.getModConfigurationDirectory().getAbsolutePath());
		MFMBlocks.preInitialize();
		MFMItems.preInit();
		
		//ОБЯЗАТЕЛЬНО ДЛЯ GUI
		new GUIHandler();
		NetworkRegistry.INSTANCE.registerGuiHandler(MetalFeverMachinery.instance, new GUIHandler());
		
		new NetworkHandler();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		MFMBlocks.initialize();
		MFMItems.init();

		GameRegistry.registerWorldGenerator(oregenerator, 1);
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		MFMLibOreDict.initEntries();
		if(Loader.isModLoaded("MineTweaker3"))
			minetweakerInit();
		//Update 1.1.0
		MFMRecipes.recipeInit();
	}

    public static void minetweakerInit() {
    	MineTweakerAPI.registerClass(MTAssembler.class);
		if(!(Loader.isModLoaded(ThermalExpansion.modId) && ConfigFile.doesTEChangeLoading)) {
			MineTweakerAPI.registerClass(MTAlloySmelter.class);
			MineTweakerAPI.registerClass(MTElectricalFurnace.class);
		}
    	MineTweakerAPI.registerClass(MTCentrifuge.class);
    	MineTweakerAPI.registerClass(MTThermoMeterialFurnace.class);
    	MineTweakerAPI.registerClass(MTMetalProcessor.class);
    }
	
	public static void addRegisteredName(String name) {
		registeredName.add(name);
	}
	
	public static boolean isNameRegistered(String name) {
		for(String n : registeredName)
			if(n.equalsIgnoreCase(name))
				return true;
		return false;
	}
}
