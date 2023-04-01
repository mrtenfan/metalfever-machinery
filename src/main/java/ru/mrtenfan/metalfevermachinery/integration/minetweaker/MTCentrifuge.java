package ru.mrtenfan.metalfevermachinery.integration.minetweaker;

import java.util.ArrayList;
import java.util.List;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import ru.mrtenfan.MTFCore.utils.ItemUtils;
import ru.mrtenfan.MTFCore.utils.MinetweakerUtils;
import ru.mrtenfan.metalfevermachinery.crafting.CentrifugeRecipe;
import ru.mrtenfan.metalfevermachinery.crafting.CentrifugeRecipe.CentrifugetionRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.metalfever.Centrifuge")
public class MTCentrifuge {

	public static final String name = "Metal Fever Machinery Centrifuge";
	
	@ZenMethod
	public static void addRecipe(IItemStack input, IItemStack output, IItemStack[] output2, int usedRF) {
		ItemStack[] iss = new ItemStack[output2.length];
		for(int i = 0; i < output2.length; i++) {
			iss[i] = MinetweakerUtils.toStack(output2[i]);
		}
		
		CentrifugetionRecipe r = new CentrifugetionRecipe(MinetweakerUtils.toStack(input), MinetweakerUtils.toStack(output), iss, usedRF);
		
		addRecipe(r);
	}
	
	@ZenMethod
	public static void addRecipe(IItemStack input, IItemStack output, int usedRF) {
		
		CentrifugetionRecipe r = new CentrifugetionRecipe(MinetweakerUtils.toStack(output), MinetweakerUtils.toStack(output), usedRF);
		
		addRecipe(r);
	}
	
	@ZenMethod
	public static void removeRecipe(IItemStack output, boolean useOreDict) {
		MineTweakerAPI.apply(new Remove(MinetweakerUtils.toStack(output), useOreDict));
	}

	private static void addRecipe(CentrifugetionRecipe r) {
		MineTweakerAPI.apply(new Add(r));
	}

	private static class Add implements IUndoableAction {
		private final CentrifugetionRecipe recipe;

		public Add(CentrifugetionRecipe r) {
			this.recipe = r;
		}

		@Override
		public void apply() {
			CentrifugeRecipe.recipes.add(recipe);
		}

		@Override
		public boolean canUndo() {
			return true;
		}

		@Override
		public void undo() {
			for (CentrifugetionRecipe recipeType : CentrifugeRecipe.recipes) {
				if(recipeType == recipe) {
					CentrifugeRecipe.recipes.remove(recipeType);
					break;
				}
			}
		}

		@Override
		public String describe() {
			return "Adding " + name + " recipe for " + recipe.getMainResult().getDisplayName();
		}

		@Override
		public String describeUndo() {
			return "Removing " + name + " recipe for " + recipe.getMainResult().getDisplayName();
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}
	}

	private static class Remove implements IUndoableAction {
		private final ItemStack output;
		private final boolean isOreDict;
		List<CentrifugetionRecipe> removedRecipes = new ArrayList<CentrifugetionRecipe>();

		public Remove(ItemStack output, boolean useOreDict) {
			this.output = output;
			this.isOreDict = useOreDict;
		}

		@Override
		public void apply() {
			for (CentrifugetionRecipe recipeType : CentrifugeRecipe.recipes) {
				if (ItemUtils.isItemEqual(recipeType.getMainResult(), output, isOreDict)) {
					removedRecipes.add((CentrifugetionRecipe) recipeType);
					CentrifugeRecipe.recipes.remove(recipeType);
					break;
				}
			}
		}

		@Override
		public void undo() {
			if (removedRecipes != null) {
				for (CentrifugetionRecipe recipe : removedRecipes) {
					if (recipe != null) {
						CentrifugeRecipe.recipes.add(recipe);
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
