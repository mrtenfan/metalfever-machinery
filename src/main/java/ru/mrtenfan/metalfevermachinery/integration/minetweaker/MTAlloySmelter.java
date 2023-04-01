package ru.mrtenfan.metalfevermachinery.integration.minetweaker;

import java.util.ArrayList;
import java.util.List;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import ru.mrtenfan.MTFCore.Debuging;
import ru.mrtenfan.MTFCore.utils.ItemUtils;
import ru.mrtenfan.MTFCore.utils.MinetweakerUtils;
import ru.mrtenfan.metalfeverbasis.MetalFeverMain;
import ru.mrtenfan.metalfeverbasis.integration.minetweaker.MinetweakerCompat;
import ru.mrtenfan.metalfevermachinery.crafting.AlloySmelterRecipe;
import ru.mrtenfan.metalfevermachinery.crafting.AlloySmelterRecipe.AlloySmelteringRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.metalfever.AlloySmelter")
public class MTAlloySmelter {
	
	public static final String name = "Metal Fever Machinery Alloy Smelter";

	@ZenMethod
	public static void addRecipe(IIngredient  input1, IIngredient  input2, IItemStack output, int usedRF) {
		Object oInput1 = MinetweakerUtils.toIoOStack(input1);
		Object oInput2 = MinetweakerUtils.toIoOStack(input2);
		if(oInput1 == null || oInput2 == null) {
			Debuging.errorOutput("Some input is null!", MetalFeverMain.modName);
			return;
		}
		ItemStack oResult = MinetweakerUtils.toStack(output);
		
		AlloySmelteringRecipe r = new AlloySmelteringRecipe(new Object[] {oInput1, oInput2}, oResult, usedRF);
		
		addRecipe(r);
	}
	
	@ZenMethod
	public static void removeRecipe(IItemStack output) {
		MineTweakerAPI.apply(new Remove(MinetweakerCompat.toStack(output)));
	}

	private static void addRecipe(AlloySmelteringRecipe r) {
		MineTweakerAPI.apply(new Add(r));
	}

	private static class Add implements IUndoableAction {
		private final AlloySmelteringRecipe recipe;

		public Add(AlloySmelteringRecipe r) {
			this.recipe = r;
		}

		@Override
		public void apply() {
			AlloySmelterRecipe.recipes.add(recipe);
		}

		@Override
		public boolean canUndo() {
			return true;
		}

		@Override
		public void undo() {
			for (AlloySmelteringRecipe recipeType : AlloySmelterRecipe.recipes) {
				if(recipeType == recipe) {
					AlloySmelterRecipe.recipes.remove(recipeType);
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
		List<AlloySmelteringRecipe> removedRecipes = new ArrayList<AlloySmelteringRecipe>();

		public Remove(ItemStack output) {
			this.output = output;
		}

		@Override
		public void apply() {
			for (AlloySmelteringRecipe recipeType : AlloySmelterRecipe.recipes) {
				if (ItemUtils.isItemEqual(recipeType.getResult(), output, false)) {
					removedRecipes.add((AlloySmelteringRecipe) recipeType);
					AlloySmelterRecipe.recipes.remove(recipeType);
					break;
				}
			}
		}

		@Override
		public void undo() {
			if (removedRecipes != null) {
				for (AlloySmelteringRecipe recipe : removedRecipes) {
					if (recipe != null) {
						AlloySmelterRecipe.recipes.add(recipe);
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
