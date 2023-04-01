package ru.mrtenfan.metalfevermachinery.crafting;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import ru.mrtenfan.MTFCore.utils.ItemUtils;

public class CentrifugeRecipe {
	public static ArrayList<CentrifugetionRecipe> recipes = new ArrayList<CentrifugetionRecipe>();
	
	public static void addCentrifugetionRecipe(ItemStack input, ItemStack outputMain, int usedRF) {
		recipes.add(new CentrifugetionRecipe(input, outputMain, usedRF));
	}
	
	public static void addCentrifugetionRecipe(ItemStack input, ItemStack outputMain, ItemStack[] outputSecondary, int usedRF) {
		recipes.add(new CentrifugetionRecipe(input, outputMain, outputSecondary, usedRF));
	}
	
	public static boolean hasResult(ItemStack itemstack) {
		if(getMainResult(itemstack) != null || getSecondaryResult(itemstack) != null)
			return true;
		else
			return false;
	}
	
	private static ItemStack getMainResult(ItemStack itemstack) {
		for(CentrifugetionRecipe r : recipes) {
			if(getOutput(itemstack, r.input))
				return r.output1;
		}
		return null;
	}

	private static ItemStack[] getSecondaryResult(ItemStack itemstack) {
		CentrifugetionRecipe rM = null;
		int k = 0;
		for(int i = 0; i < 4; i++) {
			for(CentrifugetionRecipe r : recipes) {
				if(r.output2[i] != null) {
					if(getOutput(itemstack, r.input)) {
						k += 1;
					}
				} else {
					k += 1;
				}
				rM = r;
			}
		}
		if(k == 4)
			return rM.output2;
		else
			return null;
	}

	public static ItemStack getSecondaryResult(ItemStack itemstack, int i) {
		for(CentrifugetionRecipe r : recipes) {
			if(getOutput(itemstack, r.input))
				return r.output2[i];
		}
		return null;
	}
	
	private static boolean getOutput(ItemStack itemstack, ItemStack output1) {
		return ItemUtils.isItemEqual(itemstack, output1, true);
	}

	public static int[] getSecondaryResultStackSize(ItemStack itemstack) {
		int k = 0;
		int[] j = new int[4];
		for(int i = 0; i < 4; i++) {
			for(CentrifugetionRecipe r : recipes) {
				if(r.output2[i] != null) {
					if(getOutput(itemstack, r.input)) {
						j[i] = r.output2[i].stackSize;
						k += 1;
					}
				} else {
					j[i] = 0;
					k += 1;
				}
			}
		}
		if(k == 4)
			return j;
		else
			return null;
	}

	public static CentrifugetionRecipe getRecipe(ItemStack itemStack) {
		for(CentrifugetionRecipe r : recipes) {
			if(getOutput(itemStack, r.input))
				return r;
		}
		return null;
	}
	
	public static class CentrifugetionRecipe {
		private final ItemStack input;
		private final ItemStack output1;
		private final ItemStack[] output2 = new ItemStack[4];
		private final int usedRF;
		private int RFPerTick;
		
		public CentrifugetionRecipe(ItemStack input, ItemStack output, ItemStack[] outputArray, int RF) {
			this.input = input;
			this.output1 = output;
			this.output2[0] = outputArray[0];
			this.output2[1] = outputArray[1];
			this.output2[2] = outputArray[2];
			this.output2[3] = outputArray[3];
			this.usedRF = RF;
			try {
				this.RFPerTick = RF / ru.mrtenfan.metalfevermachinery.ConfigFile.centrifugingSpeed;
			} catch (ArithmeticException e) {
				this.RFPerTick = 0;
				e.printStackTrace();
			}
		}
		
		public CentrifugetionRecipe(ItemStack input, ItemStack output, int RF) {
			this.input = input;
			this.output1 = output;
			this.usedRF = RF;
			try {
				this.RFPerTick = RF / ru.mrtenfan.metalfevermachinery.ConfigFile.centrifugingSpeed;
			} catch (ArithmeticException e) {
				this.RFPerTick = 0;
				e.printStackTrace();
			}
			
		}

		public ItemStack getInput() {
			return input;
		}

		public ItemStack getMainResult() {
			return output1;
		}
		
		public ItemStack[] getSecondaryResult() {
			return output2;
		}
		
		public ItemStack getSecondaryResult(int i) {
			return output2[i];
		}
		
		public int getUsedRF() {
			return usedRF;
		}
		
		public int getRFPerTick() {
			return RFPerTick;
		}

		public int[] getSecondaryResultNumber() {
			int[] ret = new int[4];
			for(int i = 0; i < output2.length; i++)
				ret[i] = output2[i].stackSize;
			return ret;
		}
	}
}
