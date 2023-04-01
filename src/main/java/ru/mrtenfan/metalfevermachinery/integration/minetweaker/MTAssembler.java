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
import ru.mrtenfan.metalfevermachinery.crafting.AssemblerRecipe;
import ru.mrtenfan.metalfevermachinery.crafting.AssemblerRecipe.AssemblingRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.metalfever.Assembler")
public class MTAssembler {

	public static final String name = "Metal Fever Machinery Assembler";
	
	@ZenMethod
	public static void addRecipe(IIngredient[] input, IItemStack output, int RFUsed) {
		Object[] oInput = new Object[input.length];
		
		for(int i = 0; i  < input.length; i++) {
			oInput[i] = MinetweakerUtils.toIoOStack(input[i]);
		}
		
		AssemblingRecipe r = new AssemblingRecipe(oInput, MinetweakerUtils.toStack(output), RFUsed);
		
		addRecipe(r);
	}
	
	@ZenMethod
	public static void removeRecipe(IItemStack output, boolean useOreDict) {
		MineTweakerAPI.apply(new Remove(MinetweakerUtils.toStack(output), useOreDict));
	}

	private static void addRecipe(AssemblingRecipe r) {
		MineTweakerAPI.apply(new Add(r));
	}

	private static class Add implements IUndoableAction {
		private final AssemblingRecipe recipe;

		public Add(AssemblingRecipe r) {
			this.recipe = r;
		}

		@Override
		public void apply() {
			AssemblerRecipe.recipes.add(recipe);
		}

		@Override
		public boolean canUndo() {
			return true;
		}

		@Override
		public void undo() {
			for (AssemblingRecipe recipeType : AssemblerRecipe.recipes) {
				if(recipeType == recipe) {
					AssemblerRecipe.recipes.remove(recipeType);
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
		List<AssemblingRecipe> removedRecipes = new ArrayList<AssemblingRecipe>();

		public Remove(ItemStack output, boolean useOreDict) {
			this.output = output;
			this.isOreDict = useOreDict;
		}

		@Override
		public void apply() {
			for (AssemblingRecipe recipeType : AssemblerRecipe.recipes) {
				if (ItemUtils.isItemEqual(recipeType.getResult(), output, isOreDict)) {
					removedRecipes.add((AssemblingRecipe) recipeType);
					AssemblerRecipe.recipes.remove(recipeType);
					break;
				}
			}
		}

		@Override
		public void undo() {
			if (removedRecipes != null) {
				for (AssemblingRecipe recipe : removedRecipes) {
					if (recipe != null) {
						AssemblerRecipe.addRecipe(recipe);
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
