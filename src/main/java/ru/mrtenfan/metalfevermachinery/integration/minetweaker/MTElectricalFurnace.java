package ru.mrtenfan.metalfevermachinery.integration.minetweaker;

import java.util.ArrayList;
import java.util.List;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import ru.mrtenfan.MTFCore.utils.ItemUtils;
import ru.mrtenfan.MTFCore.utils.MinetweakerUtils;
import ru.mrtenfan.metalfevermachinery.crafting.ElectricalFurnaceRecipe;
import ru.mrtenfan.metalfevermachinery.crafting.ElectricalFurnaceRecipe.ElectroSmeltingRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.metalfever.Furnace")
public class MTElectricalFurnace {
	
	public static final String name = "Metal Fever Machinery Electrical Furnace";
	
	@ZenMethod
	public static void addRecipe(IIngredient input, IItemStack output, int usedRF) {
		ElectroSmeltingRecipe r = new ElectroSmeltingRecipe(MinetweakerUtils.toIoOStack(input), MinetweakerUtils.toStack(output), usedRF);
		addRecipe(r);
	}
	
	@ZenMethod
	public static void removeRecipe(IItemStack output, boolean useOreDict) {
		MineTweakerAPI.apply(new Remove(MinetweakerUtils.toStack(output), useOreDict));
	}

	private static void addRecipe(ElectroSmeltingRecipe r) {
		MineTweakerAPI.apply(new Add(r));
	}

	private static class Add implements IUndoableAction {
		private final ElectroSmeltingRecipe recipe;

		public Add(ElectroSmeltingRecipe r) {
			this.recipe = r;
		}

		@Override
		public void apply() {
			ElectricalFurnaceRecipe.recipes.add(recipe);
		}

		@Override
		public boolean canUndo() {
			return true;
		}

		@Override
		public void undo() {
			for (ElectroSmeltingRecipe recipeType : ElectricalFurnaceRecipe.recipes) {
				if(recipeType == recipe) {
					ElectricalFurnaceRecipe.recipes.remove(recipeType);
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
		List<ElectroSmeltingRecipe> removedRecipes = new ArrayList<ElectroSmeltingRecipe>();

		public Remove(ItemStack output, boolean useOreDict) {
			this.output = output;
			this.isOreDict = useOreDict;
		}

		@Override
		public void apply() {
			for (ElectroSmeltingRecipe recipeType : ElectricalFurnaceRecipe.recipes) {
				if (ItemUtils.isItemEqual(recipeType.getResult(), output, isOreDict)) {
					removedRecipes.add((ElectroSmeltingRecipe) recipeType);
					ElectricalFurnaceRecipe.recipes.remove(recipeType);
					break;
				}
			}
		}

		@Override
		public void undo() {
			if (removedRecipes != null) {
				for (ElectroSmeltingRecipe recipe : removedRecipes) {
					if (recipe != null) {
						ElectricalFurnaceRecipe.recipes.add(recipe);
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
