package ru.mrtenfan.metalfevermachinery.crafting;

import java.util.ArrayList;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import ru.mrtenfan.MTFCore.utils.ItemUtils;
import ru.mrtenfan.MTFCore.utils.OreStack;
import ru.mrtenfan.metalfevermachinery.init.MFMItems;

public class ThermoMaterialFurnaceRecipe {
	public static ArrayList<TMFurnaceRecipe> recipes = new ArrayList<TMFurnaceRecipe>();
	private static final ItemStack pod = new ItemStack(MFMItems.ingots, 1, 5);
	private static final int sene = 20000;
	
	public static void init() {
		addTMAlloyingRecipe(sene, new ItemStack[] {pod, new ItemStack(Items.diamond)}, new ItemStack(MFMItems.ingots, 1, 0), 1650, true);
		addTMAlloyingRecipe(sene, new ItemStack[] {pod, new ItemStack(Items.emerald)}, new ItemStack(MFMItems.ingots, 1, 1), 1750, true);
		addTMAlloyingRecipe(sene, new ItemStack[] {pod, new ItemStack(Items.glowstone_dust, 2)}, new ItemStack(MFMItems.ingots, 1, 2), 1950, true);
		addTMAlloyingRecipe(sene, new ItemStack[] {pod, new ItemStack(Items.dye, 2, 4)}, new ItemStack(MFMItems.ingots, 1, 3), 1550, false);
		addTMAlloyingRecipe(sene, new ItemStack[] {pod, new ItemStack(Items.redstone, 2)}, new ItemStack(MFMItems.ingots, 1, 4), 1850, true);
	}

	/**adding new recipes for Thermo-Material Furnace.Map: [input1, input2], output, temperature, isOreDictionary */
    public static void addTMAlloyingRecipe(int usedRF, Object[] input, ItemStack output, int temp, boolean isod) {
    	Object inputItems[] = new Object[2];
        ItemStack outputItem = null;
        
        if(input[0] instanceof ItemStack && input[1] instanceof ItemStack)
        	inputItems = (ItemStack[])input;
        else
        	throw new RuntimeException("Alloying recipe input is invalid!");

        if(output instanceof ItemStack)
            outputItem = (ItemStack)output;
        else
            throw new RuntimeException("Alloying recipe output is invalid!");
        
        if(temp > 2000 || temp < 0)
        	throw new RuntimeException("Alloying recipe temperature is invalid!");

        recipes.add(new TMFurnaceRecipe(usedRF, inputItems, outputItem, temp, isod));
    }

	public static TMFurnaceRecipe getAlloyingRecipe(ItemStack itemStack, ItemStack itemStack2) {
		return getAlloyingRecipe(new ItemStack[] {itemStack, itemStack2});
	}
	
	public static TMFurnaceRecipe getAlloyingRecipe(ItemStack[] slots) {
		for(TMFurnaceRecipe r : recipes) {
			if(getOutput(slots, r))
				return r;
		}
		
		return null;
	}

	private static boolean getOutput(ItemStack[] slots, TMFurnaceRecipe recipe) {
		return getOutput(slots[0], slots[1], recipe.getInput());
	}
	
	public static boolean getOutput(ItemStack item, ItemStack item2, Object[] itemStacks) {
		if(item == null || item2 == null)
			return false;
		for(Object obj : itemStacks) {
			if(obj instanceof ItemStack) {
				ItemStack is = (ItemStack)obj;
				if(!((ItemUtils.isItemEqual(item, is, false) || ItemUtils.isItemEqual(item2, is, false)) && (item.stackSize >= is.stackSize || item2.stackSize >= is.stackSize)))
					return false;
			} else if(obj instanceof OreStack) {
				OreStack os = (OreStack)obj;
				if(!((ItemUtils.isItemEqual(item, os.getStack(), true) || ItemUtils.isItemEqual(item2, os.getStack(), true)) && (item.stackSize >= os.stackSize || item2.stackSize >= os.stackSize)))
					return false;
			} else
				return false;
		}
		return true;
	}

	public static boolean isUsedForRecipe(ItemStack itemStack) {
		for(TMFurnaceRecipe recipe : recipes)
			if(recipe.isUsedInRecipe(itemStack))
				return true;
		return false;
	}

	public static class TMFurnaceRecipe {
		private final Object[] input = new Object[2];
		private final ItemStack result;
		private final int temperature;
		private final int usedRF;
		private int RFPerTick;
		
		public TMFurnaceRecipe(int RF, Object[] input, ItemStack output, int temp, boolean oreDict) {
			this.input[0] = input[0];
			this.input[1] = input[1];
			this.result = output;
			this.temperature = temp;
			this.usedRF = RF;
			try {
				this.RFPerTick = RF / ru.mrtenfan.metalfevermachinery.ConfigFile.alloyingSpeedTMF;
			} catch (ArithmeticException e) {
				this.RFPerTick = 0;
				e.printStackTrace();
			}
		}

		public Object[] getInput() {
			return input;
		}

		public Object getInput(int i) {
			return input[i];
		}

		public ItemStack getResult() {
			return result;
		}
		
		public int getTemp() {
			return temperature;
		}
		
		public int getRFPerTick() {
			return RFPerTick;
		}
		
		public int getRFUsed() {
			return usedRF;
		}
		
		public int[] getInputNumbers(ItemStack slot1, ItemStack slot2) {
			int[] ret = new int[2];
			if(input[0] instanceof OreStack) {
				if(ItemUtils.isItemEqual(((OreStack)input[0]).getStack(), slot1, true))
					ret[0] = ((OreStack)input[0]).stackSize;
				else
					ret[1] = ((OreStack)input[0]).stackSize;
			} else if(input[0] instanceof ItemStack) {
				if(ItemUtils.isItemEqual(((ItemStack)input[0]), slot1, false))
					ret[0] = ((ItemStack)input[0]).stackSize;
				else
					ret[1] = ((ItemStack)input[0]).stackSize;
			}
			if(input[1] instanceof OreStack) {
				if(ItemUtils.isItemEqual(((OreStack)input[1]).getStack(), slot2, true))
					ret[1] = ((OreStack)input[1]).stackSize;
				else
					ret[0] = ((OreStack)input[1]).stackSize;
			} else if(input[1] instanceof ItemStack) {
				if(ItemUtils.isItemEqual(((ItemStack)input[1]), slot2, false))
					ret[1] = ((ItemStack)input[1]).stackSize;
				else
					ret[0] = ((ItemStack)input[1]).stackSize;
			}
			return ret;
		}
		
		public boolean isUsedInRecipe(ItemStack itemStack) {
			for(int i = 0; i < 2; i++) {
				if(input[i] instanceof OreStack) {
					if(ItemUtils.isItemEqual(((OreStack)input[i]).getStack(), itemStack, true))
						return true;
				} else if(input[i] instanceof ItemStack) {
					if(ItemUtils.isItemEqual(input[i], itemStack, false))
						return true;
				}
			}
			return false;
		}
	}
}
