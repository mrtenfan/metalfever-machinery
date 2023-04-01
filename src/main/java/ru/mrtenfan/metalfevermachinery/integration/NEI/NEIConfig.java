package ru.mrtenfan.metalfevermachinery.integration.NEI;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import cofh.thermalexpansion.ThermalExpansion;
import cpw.mods.fml.common.Loader;
import ru.mrtenfan.metalfevermachinery.ConfigFile;

public class NEIConfig implements IConfigureNEI {

	@Override
	public String getName() {
		return "MetalFeverMachinery";
	}

	@Override
	public String getVersion() {
		return "${version}";
	}

	@Override
	public void loadConfig() {
		API.registerRecipeHandler(new AssemblerRecipeHandler());
		API.registerUsageHandler(new AssemblerRecipeHandler());
		if(!(Loader.isModLoaded(ThermalExpansion.modId) && ConfigFile.doesTEChangeLoading)) {
			API.registerRecipeHandler(new ElectroFurnaceRecipeHandler());
			API.registerUsageHandler(new ElectroFurnaceRecipeHandler());
			API.registerRecipeHandler(new AlloySmelterRecipeHandler());
			API.registerUsageHandler(new AlloySmelterRecipeHandler());
		}
		API.registerRecipeHandler(new CentrifugeRecipeHandler());
		API.registerUsageHandler(new CentrifugeRecipeHandler());
		API.registerRecipeHandler(new ThermoMaterialFurnaceRecipeHandler());
		API.registerUsageHandler(new ThermoMaterialFurnaceRecipeHandler());
		API.registerRecipeHandler(new MetalProcessorHandler());
		API.registerUsageHandler(new MetalProcessorHandler());
	}
}