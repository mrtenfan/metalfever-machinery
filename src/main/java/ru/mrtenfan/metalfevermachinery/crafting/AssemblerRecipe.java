package ru.mrtenfan.metalfevermachinery.crafting;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import ru.mrtenfan.MTFCore.utils.ItemUtils;
import ru.mrtenfan.MTFCore.utils.OreStack;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityAssembler;

public class AssemblerRecipe {

	public static ArrayList<AssemblingRecipe> recipes = new ArrayList<AssemblingRecipe>();
	
	public static void addRecipe(AssemblingRecipe recipe) {
		recipes.add(recipe);
	}
	
	public static void addAssemblingRecipe(Object[] input, ItemStack output, int RFUsed) {
		if(input.length > 9)
			throw new RuntimeException("Recipe more than 9 ItemStacks!");

		for(int i = 0; i < input.length; i++) {
			if(!(input[i] instanceof ItemStack || input[i] instanceof OreStack))
				throw new RuntimeException("Alloying recipe input is invalid!");
		}
		
		if(RFUsed == 0)
			throw new RuntimeException("Using energy less than 0, this can't be!");

		recipes.add(new AssemblingRecipe(input, output, RFUsed));
	}

	public static int[] getAssemblingNumberIn(ItemStack[] itemStacks) {
		int[] returned = new int[9];
		for(AssemblingRecipe r : recipes)
			for(int i = 0; i < r.getInputsLength(); i++)
				for(int j = 0; j < itemStacks.length; j++)
					if(r.getInput(i) != null && itemStacks[j] != null)
						if(r.getInput(i) instanceof OreStack
								? ItemUtils.isItemEqual((ItemStack)r.getInput(i), itemStacks[j], false)
								: ItemUtils.isItemEqual(((OreStack)r.getInput(i)).getStack(), itemStacks[j], true))
							returned[j] = (r.getInput(i) instanceof OreStack ? ((OreStack)r.getInput(i)).stackSize : ((ItemStack)r.getInput(i)).stackSize);
		return returned;
	}

	public static AssemblingRecipe getRecipe(ItemStack[] itemStacks) {
		for(AssemblingRecipe recipe : recipes) {
			if(getOutput(itemStacks, recipe))
				return recipe;
		}
		return null;
	}

	public static boolean getOutput(ItemStack[] itemStacks, AssemblingRecipe recipe) {
		return getOutput(itemStacks, recipe.getInputs());
	}

	public static boolean getOutput(ItemStack[] itemStacksI, Object[] itemStacksR) {
		int k = 0;
		for(int i = 0; i < itemStacksR.length; i++) {
			if(itemStacksR[i] != null) {
				for(int j = 0; j < itemStacksI.length; j++)
					if(itemStacksI[j] != null) {
						if(itemStacksR[i] instanceof OreStack
								? ItemUtils.isItemEqual((ItemStack)itemStacksR[i], itemStacksI[j], false) && itemStacksI[j].stackSize >= ((ItemStack)itemStacksR[i]).stackSize
								: ItemUtils.isItemEqual(((OreStack)itemStacksR[i]).getStack(), itemStacksI[j], true) && itemStacksI[j].stackSize >= ((OreStack)itemStacksR[i]).stackSize)
							k += 1;
					}
						
			}else {
				k += 1;
			}
				
		}
		return k == 9;
	}
	
//	private static boolean getOutput(ItemStack itemStackI, AssemblingRecipe recipe) {
//		Object[] itemStacksR = recipe.getInputs();
//		if(itemStackI != null) {
//			for(int i = 0;i < itemStacksR.length; i++) {
//				if(itemStacksR[i] != null) {
//					if(itemStacksR[i] instanceof OreStack ? true : ItemUtils.isItemEqual((ItemStack)itemStacksR[i], itemStackI, false) && itemStackI.stackSize >= ((ItemStack)itemStacksR[i]).stackSize) {
//						return true;
//					}
//				}
//			}
//		}
//		return false;
//	}

	public static boolean isUsedInRecipe(ItemStack itemStack) {
		for(AssemblingRecipe recipe : recipes) {
			if(recipe.isUsedInRecipe(itemStack))
				return true;
		}
		return false;
	}

	public static class AssemblingRecipe {
		public final Object[] input = new Object[9];
		public final ItemStack result;
		public final int RFPerTick;

		public AssemblingRecipe(Object[] input, ItemStack output, int RFUsed) {
			for(int i = 0; i < input.length; i++) {
				this.input[i] = input[i];
			}
			this.result = output;
			this.RFPerTick =(int)RFUsed / TileEntityAssembler.getAssemblingSpeed();
		}

		public Object[] getInputs() {
			return input;
		}

		public Object getInput(int num) {
			return input[num];
		}

		public ItemStack getResult() {
			return result;
		}

		public int getRFPerTick() {
			return RFPerTick;
		}

		public int getInputsLength() {
			return input.length;
		}
		
		public int getRFUsed() {
			return RFPerTick * TileEntityAssembler.getAssemblingSpeed();
		}
		
		public boolean isUsedInRecipe(ItemStack is) {
			for(int i = 0; i < input.length; i++)
				if(input[i] instanceof OreStack
						? ItemUtils.isItemEqual((ItemStack)input[i], is, false) && is.stackSize >= ((ItemStack)input[i]).stackSize
						: ItemUtils.isItemEqual(((OreStack)input[i]).getStack(), is, true) && is.stackSize >= ((OreStack)input[i]).stackSize)
					return true;
			return false;
		}

//		public int getNumberOfItemStack(ItemStack itemStack) {
//			for(int i = 0; i < input.length; i++)
//				if(input[i].isItemEqual(itemStack))
//					return i;
//			return 0;
//		}
//		
//		public boolean isInputEqual(ItemStack itemStack) {
//			for(int i = 0; i < input.length; i++)
//				if(input[i] != null && itemStack != null)
//					return ItemUtils.isItemEqual(input[i], itemStack, isOreDict);
//				else if(input[i] == null && itemStack == null)
//					return true;
//				else if(input[i] == null && itemStack != null)
//					return true;
//			return false;
//		}
	}
}