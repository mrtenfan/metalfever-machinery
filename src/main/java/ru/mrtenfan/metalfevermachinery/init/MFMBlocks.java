package ru.mrtenfan.metalfevermachinery.init;

import cofh.thermalexpansion.ThermalExpansion;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import ru.mrtenfan.MTFCore.Debuging;
import ru.mrtenfan.metalfevermachinery.ConfigFile;
import ru.mrtenfan.metalfevermachinery.MetalFeverMachinery;
import ru.mrtenfan.metalfevermachinery.blocks.AlloySmelter;
import ru.mrtenfan.metalfevermachinery.blocks.Assembler;
import ru.mrtenfan.metalfevermachinery.blocks.Centrifuge;
import ru.mrtenfan.metalfevermachinery.blocks.ElectricFurnace;
import ru.mrtenfan.metalfevermachinery.blocks.MetalProcessor;
import ru.mrtenfan.metalfevermachinery.blocks.PodstaviaOre;
import ru.mrtenfan.metalfevermachinery.blocks.ThermoMaterialFurnace;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityAlloySmelter;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityAssembler;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityCentrifuge;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityElectricalFurnace;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityMetalProcessor;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityThermoMaterialFurnace;

public class MFMBlocks {
	
	public static Block assembler;
	public static Block assembler_active;
	public static final int GUI_ID_assembler = 1;
	//Update 1.1.0
	public static Block electric_furnace;
	public static Block electric_furnace_active;
	public static final int GUI_ID_electrical_furnace = 2;
	public static Block alloy_smelter;
	public static Block alloy_smelter_active;
	public static final int GUI_ID_alloy_smelter = 3;
	public static Block centrifuge;
	public static Block centrifuge_active;
	public static final int GUI_ID_centrifuge = 4;
	//Update 1.1.1
	public static Block thermomaterial_furnace;
	public static Block thermomaterial_furnace_active;
	public static final int GUI_ID_thermomaterial_furnace = 5;
	public static Block thermomaterial_furnace_temp;
	//Update 1.2.0
	public static Block metal_processor;
	public static Block metal_processor_active;
	public static final int GUI_ID_metal_processor = 6;
	public static Block podstavia_ore;

	public static void preInitialize() {
		assembler = new Assembler(false);
		assembler_active = new Assembler(true);
		//Update 1.1.0
		electric_furnace = new ElectricFurnace(false);
		electric_furnace_active = new ElectricFurnace(true);
		alloy_smelter = new AlloySmelter(false);
		alloy_smelter_active = new AlloySmelter(true);
		centrifuge = new Centrifuge(false);
		centrifuge_active = new Centrifuge(true);
		//Update 1.1.1
		thermomaterial_furnace = new ThermoMaterialFurnace(false, false);
		thermomaterial_furnace_active = new ThermoMaterialFurnace(true, true);
		thermomaterial_furnace_temp = new ThermoMaterialFurnace(false, true);
		//Update 1.2.0
		metal_processor = new MetalProcessor(false);
		metal_processor_active = new MetalProcessor(true);
		podstavia_ore = new PodstaviaOre(Material.rock, 3F, 5F, "pickaxe", 1);
	}

	public static void initialize() {
		gameRegister(assembler, "tile.assembler");
		gameRegister(assembler_active, "tile.assemblerActive");
    	gameRegister(TileEntityAssembler.class, "tileEntity.assembler");
    	//Update 1.1.0
    	if(!(Loader.isModLoaded(ThermalExpansion.modId) && ConfigFile.doesTEChangeLoading)) {
    		gameRegister(electric_furnace, "tile.electricalFurnace");
    		gameRegister(electric_furnace_active, "tile.electricalFurnaceActive");
    		gameRegister(TileEntityElectricalFurnace.class, "tileEntity.electricalFurnace");
    		gameRegister(alloy_smelter, "tile.alloySmelter");
    		gameRegister(alloy_smelter_active, "tile.alloySmelterActive");
    		gameRegister(TileEntityAlloySmelter.class, "tileEntity.alloySmelter"); 
    		Debuging.infoOutput("Not noticed \"Thermal Expansion\" or in config file disable disabling repetitive machines!", MetalFeverMachinery.modID);
    	} else {
    		Debuging.infoOutput("Noticed \"Thermal Expansion\" and in config file enabled disabling repetitive machines!", MetalFeverMachinery.modID);
    	}
    	gameRegister(centrifuge, "tile.centrifuge");
    	gameRegister(centrifuge_active, "tile.centrifugeActive");
    	gameRegister(TileEntityCentrifuge.class, "tileEntity.centrifuge");
    	//Update 1.1.1
    	gameRegister(thermomaterial_furnace, "tile.thermoMaterialFurnace");
    	gameRegister(thermomaterial_furnace_active, "tile.thermoMaterialFurnaceActive");
    	gameRegister(TileEntityThermoMaterialFurnace.class, "tileEntity.thermoMaterialFurnace");
    	gameRegister(thermomaterial_furnace_temp, "tile.thermoMaterialFurnaceTemp");
    	//Update 1.2.0
    	gameRegister(metal_processor, "tile.MetalProcessor");
    	gameRegister(metal_processor_active, "tile.MetalProcessorActive");
    	gameRegister(TileEntityMetalProcessor.class, "tileEntity.MetalProcessor");
    	gameRegister(podstavia_ore, "tile.podstaviaOre");
	}

	@SuppressWarnings("unchecked")
	private static void gameRegister(Object object, String name) {
		if(object instanceof Block) {
			GameRegistry.registerBlock((Block)object, name);
			MetalFeverMachinery.addRegisteredName(name);
		} else if(object instanceof Class) {
			GameRegistry.registerTileEntity((Class<? extends TileEntity>)object, name);
			MetalFeverMachinery.addRegisteredName(name);
		} else
			Debuging.errorOutput("Something goes wrong with game registering " + name, MetalFeverMachinery.modName);
	}
}
