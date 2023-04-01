package ru.mrtenfan.metalfevermachinery.integration.minetweaker;

import java.util.ArrayList;
import java.util.List;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import ru.mrtenfan.MTFCore.utils.ItemUtils;
import ru.mrtenfan.MTFCore.utils.MinetweakerUtils;
import ru.mrtenfan.metalfevermachinery.crafting.ThermoMaterialFurnaceRecipe;
import ru.mrtenfan.metalfevermachinery.crafting.ThermoMaterialFurnaceRecipe.TMFurnaceRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.metalfever.TMFurnace")
public class MTThermoMeterialFurnace {
	
	public static final String name = "Metal Fever Machinery Thermo-Material Furnace";

	@ZenMethod
	public static void addRecipe(int usedRF, IItemStack[] input, IItemStack output, int temperature, boolean isOD) {
		Object[] oInput = new Object[input.length];
		
		for(int i = 0; i  < input.length; i++) {
			oInput[i] = MinetweakerUtils.toIoOStack(input[i]);
		}
		
		TMFurnaceRecipe r = new TMFurnaceRecipe(usedRF, oInput, MinetweakerUtils.toStack(output), temperature, isOD);
		
		addRecipe(r);
	}
	
	@ZenMethod
	public static void removeRecipe(IItemStack output, boolean useOreDict) {
		MineTweakerAPI.apply(new Remove(MinetweakerUtils.toStack(output), useOreDict));
	}

	private static void addRecipe(TMFurnaceRecipe r) {
		MineTweakerAPI.apply(new Add(r));
	}

	private static class Add implements IUndoableAction {
		private final TMFurnaceRecipe recipe;

		public Add(TMFurnaceRecipe r) {
			this.recipe = r;
		}

		@Override
		public void apply() {
			ThermoMaterialFurnaceRecipe.recipes.add(recipe);
		}

		@Override
		public boolean canUndo() {
			return true;
		}

		@Override
		public void undo() {
			for (TMFurnaceRecipe recipeType : ThermoMaterialFurnaceRecipe.recipes) {
				if(recipeType == recipe) {
					ThermoMaterialFurnaceRecipe.recipes.remove(recipeType);
					break;
				}
			}
		}

		@Override
		public String describe() {
			return "Adding " + name + " recipe for " + recipe.getResult().getDisplayName();
		}

		@Override
		public String describeUndo() {
			return "Removing " + name + " recipe for " + recipe.getResult().getDisplayName();
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}
	}

	private static class Remove implements IUndoableAction {
		private final ItemStack output;
		private final boolean isOreDict;
		List<TMFurnaceRecipe> removedRecipes = new ArrayList<TMFurnaceRecipe>();

		public Remove(ItemStack output, boolean useOreDict) {
			this.output = output;
			this.isOreDict = useOreDict;
		}

		@Override
		public void apply() {
			for (TMFurnaceRecipe recipeType : ThermoMaterialFurnaceRecipe.recipes) {
				if (ItemUtils.isItemEqual(recipeType.getResult(), output, isOreDict)) {
					removedRecipes.add((TMFurnaceRecipe) recipeType);
					ThermoMaterialFurnaceRecipe.recipes.remove(recipeType);
					break;
				}
			}
		}

		@Override
		public void undo() {
			if (removedRecipes != null) {
				for (TMFurnaceRecipe recipe : removedRecipes) {
					if (recipe != null) {
						ThermoMaterialFurnaceRecipe.recipes.add(recipe);
					}
				}
			}
		}

		@Override
		public String describe() {
			return "Removing " + name + " recipe for " + output.getDisplayName();
		}

		@Override
		public String describeUndo() {
			return "Re-Adding " + name + " recipe for " + output.getDisplayName();
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}

		@Override
		public boolean canUndo() {
			return true;
		}
	}

}
