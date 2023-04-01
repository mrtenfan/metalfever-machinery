package ru.mrtenfan.metalfevermachinery.init;

import cofh.thermalexpansion.ThermalExpansion;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import ru.mrtenfan.MTFCore.utils.OreStack;
import ru.mrtenfan.metalfeverbasis.crafting.AlloyFurnaceRecipe;
import ru.mrtenfan.metalfeverbasis.crafting.AlloyFurnaceRecipe.AlloyingRecipe;
import ru.mrtenfan.metalfeverbasis.integration.ThermalExpansion.ThermalExpansionCompat;
import ru.mrtenfan.metalfevermachinery.ConfigFile;
import ru.mrtenfan.metalfevermachinery.crafting.AlloySmelterRecipe;
import ru.mrtenfan.metalfevermachinery.crafting.ElectricalFurnaceRecipe;
import ru.mrtenfan.metalfevermachinery.crafting.MetalProcessorRecipe;
import ru.mrtenfan.metalfevermachinery.crafting.ThermoMaterialFurnaceRecipe;

public class MFMRecipes {

	public static void recipeInit() {
		WorkbenchRecipeInit();
		if(ru.mrtenfan.metalfeverbasis.ConfigFile.EnabPlates && ru.mrtenfan.metalfeverbasis.ConfigFile.EnabHammerCrafts)
			HammerRecipe();
		FurnaceRecipeInit();
		if(!(Loader.isModLoaded(ThermalExpansion.modId) && ConfigFile.doesTEChangeLoading)) {
			ElectricalFurnaceRecipe.init();
			AlloySmelterRecipe.init();
		} else {
			IntegrationInit();
			ThermalExpansionReplace();
		}
		ThermoMaterialFurnaceRecipe.init();
		MetalProcessorRecipe.init();
	}

	private static void WorkbenchRecipeInit() {
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(MFMItems.nuggets, 9, 0), "ingotDiamingot"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(MFMItems.nuggets, 9, 1), "ingotEmeringot"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(MFMItems.nuggets, 9, 2), "ingotGlowingot"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(MFMItems.nuggets, 9, 3), "ingotLapingot"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(MFMItems.nuggets, 9, 4), "ingotRedingot"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(MFMItems.ingots, 1, 0), "nuggetDiamingot", "nuggetDiamingot", "nuggetDiamingot", "nuggetDiamingot", "nuggetDiamingot", "nuggetDiamingot", "nuggetDiamingot", "nuggetDiamingot", "nuggetDiamingot"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(MFMItems.ingots, 1, 1), "nuggetEmeringot", "nuggetEmeringot", "nuggetEmeringot", "nuggetEmeringot", "nuggetEmeringot", "nuggetEmeringot", "nuggetEmeringot", "nuggetEmeringot", "nuggetEmeringot"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(MFMItems.ingots, 1, 2), "nuggetGlowingot", "nuggetGlowingot", "nuggetGlowingot", "nuggetGlowingot", "nuggetGlowingot", "nuggetGlowingot", "nuggetGlowingot", "nuggetGlowingot", "nuggetGlowingot"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(MFMItems.ingots, 1, 3), "nuggetLapingot", "nuggetLapingot", "nuggetLapingot", "nuggetLapingot", "nuggetLapingot", "nuggetLapingot", "nuggetLapingot", "nuggetLapingot", "nuggetLapingot"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(MFMItems.ingots, 1, 4), "nuggetRedingot", "nuggetRedingot", "nuggetRedingot", "nuggetRedingot", "nuggetRedingot", "nuggetRedingot", "nuggetRedingot", "nuggetRedingot", "nuggetRedingot"));
	}

	private static void FurnaceRecipeInit() {
		GameRegistry.addSmelting(MFMBlocks.podstavia_ore, new ItemStack(MFMItems.ingots, 1, 5), 0.5F);
		ItemStack[] is = new ItemStack[] {new ItemStack(Items.diamond), new ItemStack(Items.emerald), new ItemStack(Items.dye, 1, 4)};
		for(int i = 0; i < 2; i++)
			GameRegistry.addSmelting(new ItemStack(MFMItems.dusts, 1, i), is[i], 0.5F);
	}

	private static void HammerRecipe() {
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(MFMItems.plates, 1, 0), "ingotDiamingot", "ingotDiamingot", "anyHammers"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(MFMItems.plates, 1, 1), "ingotEmeringot", "ingotEmeringot", "anyHammers"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(MFMItems.plates, 1, 2), "ingotGlowingot", "ingotGlowingot", "anyHammers"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(MFMItems.plates, 1, 3), "ingotLapingot", "ingotLapingot", "anyHammers"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(MFMItems.plates, 1, 4), "ingotRedingot", "ingotRedingot", "anyHammers"));
	}

	private static void IntegrationInit() {
		ItemStack[] is = new ItemStack[] {new ItemStack(MFMItems.dusts, 1, 0), new ItemStack(MFMItems.dusts, 1, 1), new ItemStack(Items.glowstone_dust), new ItemStack(MFMItems.dusts, 1, 2), new ItemStack(Items.redstone)};
		for(int i = 0; i < 5; i++)
			ThermalExpansionCompat.registerPulverizerRecipe(2400, new ItemStack(MFMItems.ingots, 1, i), is[i]); // тут
		ThermalExpansionCompat.registerPulverizerRecipe(2400, new ItemStack(Items.diamond), is[0]);
	}

	private static void ThermalExpansionReplace() {
		for(AlloyingRecipe r : AlloyFurnaceRecipe.recipes) { // тут
			ThermalExpansionCompat.registerInductionSmelterRecipe(2400, (r.getInput(0) instanceof OreStack ? ((OreStack)r.getInput(0)).getStack() : (ItemStack)r.getInput(0)), 
					(r.getInput(1) instanceof OreStack ? ((OreStack)r.getInput(1)).getStack() : (ItemStack)r.getInput(1)), r.getResult()); // тут
		}
	}
}