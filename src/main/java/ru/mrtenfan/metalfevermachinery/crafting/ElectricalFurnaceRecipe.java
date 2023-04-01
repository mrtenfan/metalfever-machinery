package ru.mrtenfan.metalfevermachinery.crafting;

import java.util.ArrayList;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import ru.mrtenfan.MTFCore.utils.ItemUtils;
import ru.mrtenfan.MTFCore.utils.OreStack;
import ru.mrtenfan.metalfevermachinery.ConfigFile;

public class ElectricalFurnaceRecipe {

	public static ArrayList<ElectroSmeltingRecipe> recipes = new ArrayList<ElectroSmeltingRecipe>();
	
	public static void init() {
		//Adding custom smeltings
		@SuppressWarnings("unchecked")
		Map<ItemStack, ItemStack> smeltingList = FurnaceRecipes.smelting().getSmeltingList();
		
		ItemStack output;
        for (ItemStack key : smeltingList.keySet()) {
        	if(key != null) {
    			output = smeltingList.get(key);
        		addElectroSmeltingRecipe((ItemStack)key, (ItemStack)output);
        	}
        }
        
        //Adding vanila smeltings
        addElectroSmeltingRecipe(Blocks.iron_ore, new ItemStack(Items.iron_ingot));
        addElectroSmeltingRecipe(Blocks.gold_ore, new ItemStack(Items.gold_ingot));
        addElectroSmeltingRecipe(Blocks.diamond_ore, new ItemStack(Items.diamond));
        addElectroSmeltingRecipe(Blocks.sand, new ItemStack(Blocks.glass));
        addElectroSmeltingRecipe(Items.porkchop, new ItemStack(Items.cooked_porkchop));
        addElectroSmeltingRecipe(Items.beef, new ItemStack(Items.cooked_beef));
        addElectroSmeltingRecipe(Items.chicken, new ItemStack(Items.cooked_chicken));
        addElectroSmeltingRecipe(Blocks.cobblestone, new ItemStack(Blocks.stone));
        addElectroSmeltingRecipe(Items.clay_ball, new ItemStack(Items.brick));
        addElectroSmeltingRecipe(Blocks.clay, new ItemStack(Blocks.hardened_clay));
        addElectroSmeltingRecipe(Blocks.cactus, new ItemStack(Items.dye, 1, 2));
        addElectroSmeltingRecipe(Blocks.log, new ItemStack(Items.coal, 1, 1));
        addElectroSmeltingRecipe(Blocks.log2, new ItemStack(Items.coal, 1, 1));
        addElectroSmeltingRecipe(Blocks.emerald_ore, new ItemStack(Items.emerald));
        addElectroSmeltingRecipe(Items.potato, new ItemStack(Items.baked_potato));
        addElectroSmeltingRecipe(Blocks.netherrack, new ItemStack(Items.netherbrick));

        addElectroSmeltingRecipe(Blocks.coal_ore, new ItemStack(Items.coal));
        addElectroSmeltingRecipe(Blocks.redstone_ore, new ItemStack(Items.redstone));
        addElectroSmeltingRecipe(Blocks.lapis_ore, new ItemStack(Items.dye, 1, 4));
        addElectroSmeltingRecipe(Blocks.quartz_ore, new ItemStack(Items.quartz));
	}
	
	public static void addElectroSmeltingRecipe(Object input, ItemStack output) {
		addElectroSmeltingRecipe(input, output, 5500);
	}

	public static void addElectroSmeltingRecipe(Object input, ItemStack output, int usedRF) {
        if(!(input instanceof ItemStack || input instanceof OreStack))
        	throw new RuntimeException("Alloying recipe input is invalid!");
		
		recipes.add(new ElectroSmeltingRecipe(input, output, usedRF));
	}
	
	public static ItemStack getSmeltingResult(ItemStack input) {
		for(ElectroSmeltingRecipe r : recipes)
			if(getOutput(input, r))
				return r.getResult();
		return null;
	}
	
	private static boolean getOutput(ItemStack input, ElectroSmeltingRecipe r) {
		return getOutput(r.getInput(), input);
	}
	
	private static boolean getOutput(Object input, ItemStack input2) {
		if(input != null) {
			return (input instanceof OreStack ? ItemUtils.isItemEqual(((OreStack)input).getStack(), input2, true) && ((OreStack)input).stackSize >= input2.stackSize
					: ItemUtils.isItemEqual((ItemStack)input, input2, true) && ((ItemStack)input).stackSize >= input2.stackSize);
		}
		else
			return false;
	}

	public static int getRFPerTick(ItemStack input) {
		for(ElectroSmeltingRecipe r : recipes)
			if(getOutput(input, r))
				return r.getRFPerTick();
		return 0;
	}

	public static class ElectroSmeltingRecipe {
		private final Object input;
		private final ItemStack output;
		private final int usedRF;
		private int RFPerTick;
		
		public ElectroSmeltingRecipe(Object input, ItemStack output, int RF) {
			this.input = input;
			this.output = output;
			this.usedRF = RF;
			try {
				this.RFPerTick = RF / ConfigFile.smeltingSpeed;
			} catch (ArithmeticException e) {
				this.RFPerTick = 0;
				e.printStackTrace();
			}
		}
		
		public Object getInput() {
			return input;
		}
		
		public ItemStack getResult() {
			return output;
		}
		
		public int getUsedRF() {
			return usedRF;
		}
		
		public int getRFPerTick() {
			return RFPerTick;
		}
	}
}
