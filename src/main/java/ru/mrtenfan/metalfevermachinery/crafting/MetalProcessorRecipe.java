package ru.mrtenfan.metalfevermachinery.crafting;

import java.util.ArrayList;

import cofh.thermalfoundation.item.TFItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import ru.mrtenfan.MTFCore.utils.ItemUtils;
import ru.mrtenfan.MTFCore.utils.OreDictUtils;
import ru.mrtenfan.MTFCore.utils.OreStack;
import ru.mrtenfan.metalfeverbasis.ConfigFile;
import ru.mrtenfan.metalfeverbasis.MetalFeverMain;
import ru.mrtenfan.metalfeverbasis.init.MFItems;
import ru.mrtenfan.metalfevermachinery.init.MFMItems;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityMetalProcessor;

public class MetalProcessorRecipe {
	private static final int standartRFPerTick = 12000;
	public static ArrayList<MetalProcessing> recipes = new ArrayList<MetalProcessing>();

	public static void init() {
		if(ConfigFile.EnabPlates) {
			for(int i = 0; i < 11; i++)
				if(i < 8)
					addRollingRecipe(new ItemStack(MFItems.metals_ingot, 1, i), new ItemStack(MFItems.metals_plate, 1, i), standartRFPerTick);
				else if(i > 7)
					addRollingRecipe(new ItemStack(MFItems.metals_ingot, 1, i), new ItemStack(MFItems.metals_plate, 1, i + 2), standartRFPerTick);
			for(int i = 0; i < 7; i++)
				addRollingRecipe(new ItemStack(MFItems.alloys_ingot, 1, i), new ItemStack(MFItems.alloys_plate, 1, i), standartRFPerTick);
			for(int i = 0; i < 5; i++)
				addRollingRecipe(new ItemStack(MFMItems.ingots, 1, i), new ItemStack(MFMItems.plates, 1, i), standartRFPerTick);
			if(MetalFeverMain.THERMAL) {
				final String[] oreDict = new String[] {"ingotMithril", "ingotInvar", "ingotElectrum", "ingotSignalum", "ingotLumium", "ingotEnderium"};
				for(int i = 0; i < oreDict.length; i++)
					if(OreDictUtils.doesOreExist(oreDict[i]))
						addRollingRecipe(OreDictUtils.getItemStackFromOreDict(oreDict[i]), new ItemStack(MFItems.therm_plate, 1, i), standartRFPerTick);
			}
			if(MetalFeverMain.ENDER) {
				final String[] oreDict = new String[] {"ingotElectricalSteel", "ingotEnergeticAlloy", "ingotSoularium", "ingotVibrantAlloy", "ingotDarkSteel"};
				for(int i = 0; i < oreDict.length; i++)
					if(OreDictUtils.doesOreExist(oreDict[i]))
						addRollingRecipe(OreDictUtils.getItemStackFromOreDict(oreDict[i]), new ItemStack(MFItems.ender_plate, 1, i), standartRFPerTick);
			}
			if(MetalFeverMain.REACTORS) {
				final String[] oreDict = new String[] {"ingotYellorium", "ingotCyanite", "ingotBlutonium", "ingotLudicrite"};
				for(int i = 0; i < oreDict.length; i++)
					if(OreDictUtils.doesOreExist(oreDict[i]))
						addRollingRecipe(OreDictUtils.getItemStackFromOreDict(oreDict[i]), new ItemStack(MFItems.breactor_plate, 1, i), standartRFPerTick);
			}
			if(MetalFeverMain.TINKERS) {
				final String[] oreDict = new String[] {"ingotAlumite", "ingotArdite", "ingotManyullyn"};
				for(int i = 0; i < oreDict.length; i++)
					if(OreDictUtils.doesOreExist(oreDict[i]))
						addRollingRecipe(OreDictUtils.getItemStackFromOreDict(oreDict[i]), new ItemStack(MFItems.tinkers_plate, 1, i), standartRFPerTick);
			}
			if(MetalFeverMain.DRACONIC) {
				final String[] oreDict = new String[] {"ingotDraconium", "ingotDraconiumAwakened"};
				for(int i = 0; i < oreDict.length; i++)
					if(OreDictUtils.doesOreExist(oreDict[i]))
						addRollingRecipe(OreDictUtils.getItemStackFromOreDict(oreDict[i]), new ItemStack(MFItems.draconic_plate, 1, i), standartRFPerTick);
			}
			
			for(int i = 0; i < 8; i++) {
				final ItemStack[] items = new ItemStack[] { new ItemStack(MFItems.metals_plate, 4, 1), new ItemStack(MFItems.metals_plate, 4, 2), new ItemStack(MFItems.metals_plate, 4, 3),
						new ItemStack(MFItems.metals_plate, 4, 4), new ItemStack(MFItems.metals_plate, 4, 5), new ItemStack(MFItems.metals_plate, 4, 6), new ItemStack(MFItems.metals_plate, 4, 8),
						new ItemStack(MFItems.metals_plate, 4, 9) };
				addShapingRecipe(items[i], new ItemStack(MFItems.metals_gear_b, 1, i), standartRFPerTick);
			}
			for(int i = 0; i < 5; i++) {
				final ItemStack[] items = new ItemStack[] { new ItemStack(MFItems.metals_plate, 4, 0), new ItemStack(MFItems.metals_plate, 4, 7), new ItemStack(MFItems.metals_plate, 4, 10),
						new ItemStack(MFItems.metals_plate, 4, 11), new ItemStack(MFItems.metals_plate, 4, 12) };
				addShapingRecipe(items[i], new ItemStack(MFItems.metals_gear_c, 1, i), standartRFPerTick);
			}
			for(int i = 0; i < 6; i++)
				addShapingRecipe(new ItemStack(MFItems.alloys_plate, 4, i), new ItemStack(MFItems.alloys_gear, 1, i), standartRFPerTick);
			if(MetalFeverMain.THERMAL) {
				final String[] oreDict = new String[] {"plateMithril", "plateInvar", "plateElectrum", "plateSignalum", "plateLumium", "plateEnderium"};
				final ItemStack[] items = new ItemStack[] { TFItems.gearMithril, TFItems.gearInvar, TFItems.gearElectrum, TFItems.gearSignalum, TFItems.gearLumium, TFItems.gearEnderium};
				for(int i = 0; i < oreDict.length; i++) {
					ItemStack input = OreDictUtils.getItemStackFromOreDict(oreDict[i]);
					input.stackSize = 4;
					addShapingRecipe(input, items[i], standartRFPerTick);
				}
			}
			if(MetalFeverMain.ENDER) {
				final String[] oreDict = new String[] {"plateElectricalSteel", "plateEnergeticAlloy", "plateSoularium", "plateVibrantAlloy", "plateDarkSteel"};
				for(int i = 0; i < oreDict.length; i++) {
					ItemStack input = OreDictUtils.getItemStackFromOreDict(oreDict[i]);
					input.stackSize = 4;
					addShapingRecipe(input, new ItemStack(MFItems.ender_gear, 1, i), standartRFPerTick);
				}
			}
			if(MetalFeverMain.REACTORS) {
				final String[] oreDict = new String[] {"plateYellorium", "plateCyanite", "plateBlutonium", "plateLudicrite"};
				for(int i = 0; i < oreDict.length; i++) {
					ItemStack input = OreDictUtils.getItemStackFromOreDict(oreDict[i]);
					input.stackSize = 4;
					addShapingRecipe(input, new ItemStack(MFItems.breactor_gear, 1, i), standartRFPerTick);
				}
			}
			if(MetalFeverMain.TINKERS) {
				final String[] oreDict = new String[] {"plateAlumite", "plateArdite", "plateManyullyn"};
				for(int i = 0; i < oreDict.length; i++) {
					ItemStack input = OreDictUtils.getItemStackFromOreDict(oreDict[i]);
					input.stackSize = 4;
					addShapingRecipe(input, new ItemStack(MFItems.tinkers_gear, 1, i), standartRFPerTick);
				}
			}
			if(MetalFeverMain.DRACONIC) {
				final String[] oreDict = new String[] {"plateDraconium", "plateDraconiumAwakened"};
				for(int i = 0; i < oreDict.length; i++)
					if(OreDictUtils.doesOreExist(oreDict[i])) {
						ItemStack input = OreDictUtils.getItemStackFromOreDict(oreDict[i]);
						input.stackSize = 4;
						addShapingRecipe(input, new ItemStack(MFItems.draconic_gear, 1, i), standartRFPerTick);
					}
			}
		}

		if(ConfigFile.EnabGearParts) {
			if(ConfigFile.EnabPlates) {
				for(int i = 0; i < 13; i++)
					addCuttingRecipe(new ItemStack(MFItems.metals_plate, 1, i), new ItemStack(MFItems.metals_gear_part, 2, i), standartRFPerTick);
				for(int i = 0; i < 7; i++)
					addCuttingRecipe(new ItemStack(MFItems.alloys_plate, 1, i), new ItemStack(MFItems.alloys_gear_part, 2, i), standartRFPerTick);
				if(MetalFeverMain.THERMAL) {
					final String[] oreDict = new String[] {"plateMithril", "plateInvar", "plateElectrum", "plateSignalum", "plateLumium", "plateEnderium"};
					for(int i = 0; i < oreDict.length; i++)
						if(OreDictUtils.doesOreExist(oreDict[i]))
							addCuttingRecipe(OreDictUtils.getItemStackFromOreDict(oreDict[i]), new ItemStack(MFItems.thermal_gear_part, 2, i), standartRFPerTick);
				}
				if(MetalFeverMain.ENDER) {
					final String[] oreDict = new String[] {"plateElectricalSteel", "plateEnergeticAlloy", "plateSoularium", "plateVibrantAlloy", "plateDarkSteel"};
					for(int i = 0; i < oreDict.length; i++)
						if(OreDictUtils.doesOreExist(oreDict[i]))
							addCuttingRecipe(OreDictUtils.getItemStackFromOreDict(oreDict[i]), new ItemStack(MFItems.ender_gear_part, 2, i), standartRFPerTick);
				}
				if(MetalFeverMain.REACTORS) {
					final String[] oreDict = new String[] {"plateYellorium", "plateCyanite", "plateBlutonium", "plateLudicrite"};
					for(int i = 0; i < oreDict.length; i++)
						if(OreDictUtils.doesOreExist(oreDict[i]))
							addCuttingRecipe(OreDictUtils.getItemStackFromOreDict(oreDict[i]), new ItemStack(MFItems.breactor_gear_part, 2, i), standartRFPerTick);
				}
				if(MetalFeverMain.TINKERS) {
					final String[] oreDict = new String[] {"plateAlumite", "plateArdite", "plateManyullyn"};
					for(int i = 0; i < oreDict.length; i++)
						if(OreDictUtils.doesOreExist(oreDict[i]))
							addCuttingRecipe(OreDictUtils.getItemStackFromOreDict(oreDict[i]), new ItemStack(MFItems.tinkers_gear_part, 2, i), standartRFPerTick);
				}
				if(MetalFeverMain.DRACONIC) {
					final String[] oreDict = new String[] {"plateDraconium", "plateDraconiumAwakened"};
					for(int i = 0; i < oreDict.length; i++)
						if(OreDictUtils.doesOreExist(oreDict[i]))
							addCuttingRecipe(OreDictUtils.getItemStackFromOreDict(oreDict[i]), new ItemStack(MFItems.draconic_gear_part, 2, i), standartRFPerTick);
				}
			}
		} else {
			for(int i = 0; i < 13; i++)
				if(i < 8)
					addCuttingRecipe(new ItemStack(MFItems.metals_ingot, 1, i), new ItemStack(MFItems.metals_gear_part, 1, i), standartRFPerTick);
				else if(i > 7)
					addCuttingRecipe(new ItemStack(MFItems.metals_ingot, 1, i), new ItemStack(MFItems.metals_gear_part, 1, i + 2), standartRFPerTick);
			addCuttingRecipe(new ItemStack(Items.iron_ingot), new ItemStack(MFItems.metals_gear_part, 1, 8), standartRFPerTick);
			addCuttingRecipe(new ItemStack(Items.gold_ingot), new ItemStack(MFItems.metals_gear_part, 1, 9), standartRFPerTick);
			for(int i = 0; i < 7; i++)
				addCuttingRecipe(new ItemStack(MFItems.alloys_ingot, 1, i), new ItemStack(MFItems.alloys_gear_part, 2, i), standartRFPerTick);
			if(MetalFeverMain.THERMAL) {
				final String[] oreDict = new String[] {"ingotMithril", "ingotInvar", "ingotElectrum", "ingotSignalum", "ingotLumium", "ingotEnderium"};
				for(int i = 0; i < oreDict.length; i++)
					if(OreDictUtils.doesOreExist(oreDict[i]))
						addCuttingRecipe(OreDictUtils.getItemStackFromOreDict(oreDict[i]), new ItemStack(MFItems.thermal_gear_part, 2, i), standartRFPerTick);
			}
			if(MetalFeverMain.ENDER) {
				final String[] oreDict = new String[] {"ingotElectricalSteel", "ingotEnergeticAlloy", "ingotSoularium", "ingotVibrantAlloy", "ingotDarkSteel"};
				for(int i = 0; i < oreDict.length; i++)
					if(OreDictUtils.doesOreExist(oreDict[i]))
						addCuttingRecipe(OreDictUtils.getItemStackFromOreDict(oreDict[i]), new ItemStack(MFItems.ender_gear_part, 2, i), standartRFPerTick);
			}
			if(MetalFeverMain.REACTORS) {
				final String[] oreDict = new String[] {"ingotYellorium", "ingotCyanite", "ingotBlutonium", "ingotLudicrite"};
				for(int i = 0; i < oreDict.length; i++)
					if(OreDictUtils.doesOreExist(oreDict[i]))
						addCuttingRecipe(OreDictUtils.getItemStackFromOreDict(oreDict[i]), new ItemStack(MFItems.breactor_gear_part, 2, i), standartRFPerTick);
			}
			if(MetalFeverMain.TINKERS) {
				final String[] oreDict = new String[] {"ingotAlumite", "ingotArdite", "ingotManyullyn"};
				for(int i = 0; i < oreDict.length; i++)
					if(OreDictUtils.doesOreExist(oreDict[i]))
						addCuttingRecipe(OreDictUtils.getItemStackFromOreDict(oreDict[i]), new ItemStack(MFItems.tinkers_gear_part, 2, i), standartRFPerTick);
			}
			if(MetalFeverMain.DRACONIC) {
				final String[] oreDict = new String[] {"ingotDraconium", "ingotDraconiumAwakened"};
				for(int i = 0; i < oreDict.length; i++)
					if(OreDictUtils.doesOreExist(oreDict[i]))
						addCuttingRecipe(OreDictUtils.getItemStackFromOreDict(oreDict[i]), new ItemStack(MFItems.draconic_gear_part, 2, i), standartRFPerTick);
			}
		}
	}
	
	public static void addRollingRecipe(Object input, ItemStack output, int RFForRecipe) {
		addMetalProcessing(MetalProcessorModes.ROLLING, input, output, RFForRecipe);
	}
	
	public static void addCuttingRecipe(Object input, ItemStack output, int RFForRecipe) {
		addMetalProcessing(MetalProcessorModes.CUTTING, input, output, RFForRecipe);
	}
	
	public static void addShapingRecipe(Object input, ItemStack output, int RFForRecipe) {
		addMetalProcessing(MetalProcessorModes.SHAPING, input, output, RFForRecipe);
	}
	
	public static void addMetalProcessing(MetalProcessorModes mode, Object input, ItemStack output, int RFForRecipe) {
		recipes.add(new MetalProcessing(mode, input, output, RFForRecipe));
	}

	public static void addRecipe(MetalProcessing recipe) {
		recipes.add(recipe);
	}

	public static MetalProcessing getRecipe(ItemStack input, MetalProcessorModes i) {
		for(MetalProcessing r : recipes) {
			if(getOutput(input, i, r))
				return r;
		}
		return null;
	}
	
	private static boolean getOutput(ItemStack input, MetalProcessorModes mMode, MetalProcessing r) {
		MetalProcessorModes rMode = r.getMode();
		Object rInput = r.getInput();
		return mMode.ordinal() == rMode.ordinal() && (rInput instanceof OreStack ? ItemUtils.isItemEqual(((OreStack)rInput), input, true) : ItemUtils.isItemEqual((ItemStack)rInput, input, false));
	}

	public static boolean usedForCrafting(ItemStack is) {
		for(MetalProcessing r : recipes)
			if((r.getInput() instanceof OreStack ? ItemUtils.isItemEqual(((OreStack)r.getInput()).getStack(), is, true) : ItemUtils.isItemEqual((ItemStack)r.getInput(), is, false))) return true;
		return false;
	}
	
	public static class MetalProcessing {
		private final MetalProcessorModes mode;
		private final Object input;
		private final ItemStack result;
		private final int RFPerTick;
		private final int RFUsed;
		
		public MetalProcessing(MetalProcessorModes mode, Object input, ItemStack output, int RFForRecipe) {
			this.mode = mode;
			this.input = input;
			this.result = output;
			this.RFPerTick = (int) RFForRecipe / TileEntityMetalProcessor.rollingSpeed;
			this.RFUsed = RFForRecipe;
		}
		
		public MetalProcessorModes getMode() {
			return mode;
		}

		public Object getInput() {
			return input;
		}

		public ItemStack getResult() {
			return result;
		}
		
		public int getRFPerTick() {
			return RFPerTick;
		}
		
		public int getRFUsed() {
			return RFUsed;
		}
	}
	
	public static enum MetalProcessorModes {
		ROLLING, CUTTING, SHAPING
	}
}
