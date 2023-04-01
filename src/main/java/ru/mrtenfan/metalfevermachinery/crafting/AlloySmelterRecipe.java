package ru.mrtenfan.metalfevermachinery.crafting;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import ru.mrtenfan.MTFCore.utils.ItemUtils;
import ru.mrtenfan.MTFCore.utils.OreStack;
import ru.mrtenfan.metalfeverbasis.crafting.AlloyFurnaceRecipe;
import ru.mrtenfan.metalfeverbasis.crafting.AlloyFurnaceRecipe.AlloyingRecipe;
import ru.mrtenfan.metalfevermachinery.ConfigFile;

public class AlloySmelterRecipe {
	public static ArrayList<AlloySmelteringRecipe> recipes = new ArrayList<AlloySmelteringRecipe>();

	public static void init() {
		for(AlloyingRecipe r : AlloyFurnaceRecipe.recipes) {
			recipes.add(new AlloySmelteringRecipe(r.getInput(), r.getResult(), 6000));
		}
	}

	/**adding new recipes for Alloy Smelter.Map: [input1, input2], output */
    public static void addAlloyingRecipe(Object[] input, ItemStack output, int usedRF) {
        if(!(input[0] instanceof ItemStack || input[0] instanceof OreStack))
        	throw new RuntimeException("Alloying recipe input is invalid!");

        if(!(input[1] instanceof ItemStack || input[1] instanceof OreStack))
        	throw new RuntimeException("Alloying recipe input is invalid!");

        recipes.add(new AlloySmelteringRecipe(input, output, usedRF));
    }

	public static AlloySmelteringRecipe getRecipe(ItemStack is, ItemStack is2) {
		for(AlloySmelteringRecipe r : recipes) {
			if(getOutput(is, is2, r.getInput()))
				return r;
		}
		return null;
	}
	
	public static boolean getOutput(ItemStack item, ItemStack item2, Object[] inputs) {
		if(item == null || item2 == null)
			return false;
		for(Object obj : inputs) {
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
		for(AlloySmelteringRecipe recipe : recipes)
			if(recipe.isUsedInRecipe(itemStack))
				return true;
		return false;
	}

	public static class AlloySmelteringRecipe {
		private final Object[] input = new Object[2];
		private final ItemStack result;
		private final int usedRF;
		private int RFPerTick;
		
		public AlloySmelteringRecipe(Object[] input2, ItemStack output, int RF) {
        	if(!(input2[0] instanceof ItemStack || input2[0] instanceof OreStack || input2[1] instanceof ItemStack || input2[1] instanceof OreStack)) 
        		throw new RuntimeException("Input must be ItemStack or OreStack(OreDictionary)");
			this.input[0] = input2[0];
			this.input[1] = input2[1];
			this.result = output;
			this.usedRF = RF;
			try {
				this.RFPerTick = RF / ConfigFile.alloyingSpeed;
			} catch (ArithmeticException e) {
				this.RFPerTick = 0;
				e.printStackTrace();
			}
		}

		public Object[] getInput() {
			return input;
		}

		public ItemStack getResult() {
			return result;
		}

		public Object getInput(int i) {
			return input[i];
		}
		
		public int getUsedRF() {
			return usedRF;
		}
		
		public int getRFPerTick() {
			return RFPerTick;
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
		
		@Override
		public String toString() {
			return "(" + result + ")=A(" + input[0] + ")&(" + input[1] + ")";
		}
	}
}
