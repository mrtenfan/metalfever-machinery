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
import ru.mrtenfan.metalfevermachinery.crafting.MetalProcessorRecipe;
import ru.mrtenfan.metalfevermachinery.crafting.MetalProcessorRecipe.MetalProcessing;
import ru.mrtenfan.metalfevermachinery.crafting.MetalProcessorRecipe.MetalProcessorModes;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.metalfever.MetalHandler")
public class MTMetalProcessor {

	public static final String name = "Metal Fever Machinery Metal Handler";
	
	@ZenMethod
	public static void addRollingRecipe(IIngredient input, IItemStack output, int RFUsed) {
		
		MetalProcessing r = new MetalProcessing(MetalProcessorModes.ROLLING, MinetweakerUtils.toIoOStack(input), MinetweakerUtils.toStack(output), RFUsed);
		
		addRecipe(r);
	}
	
	@ZenMethod
	public static void addCuttingRecipe(IIngredient input, IItemStack output, int RFUsed) {
		
		MetalProcessing r = new MetalProcessing(MetalProcessorModes.CUTTING, MinetweakerUtils.toIoOStack(input), MinetweakerUtils.toStack(output), RFUsed);
		
		addRecipe(r);
	}
	
	@ZenMethod
	public static void addShapingRecipe(IIngredient input, IItemStack output, int RFUsed) {
		
		MetalProcessing r = new MetalProcessing(MetalProcessorModes.SHAPING, MinetweakerUtils.toIoOStack(input), MinetweakerUtils.toStack(output), RFUsed);
		
		addRecipe(r);
	}
	
	@ZenMethod
	public static void removeRecipe(IItemStack output) {
		MineTweakerAPI.apply(new Remove(MinetweakerUtils.toStack(output)));
	}

	private static void addRecipe(MetalProcessing r) {
		MineTweakerAPI.apply(new Add(r));
	}

	private static class Add implements IUndoableAction {
		private final MetalProcessing recipe;

		public Add(MetalProcessing r) {
			this.recipe = r;
		}

		@Override
		public void apply() {
			MetalProcessorRecipe.recipes.add(recipe);
		}

		@Override
		public boolean canUndo() {
			return true;
		}

		@Override
		public void undo() {
			for (MetalProcessing recipeType : MetalProcessorRecipe.recipes) {
				if(recipeType == recipe) {
					MetalProcessorRecipe.recipes.remove(recipeType);
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
		List<MetalProcessing> removedRecipes = new ArrayList<MetalProcessing>();

		public Remove(ItemStack output) {
			this.output = output;
		}

		@Override
		public void apply() {
			for (MetalProcessing recipeType : MetalProcessorRecipe.recipes) {
				if (ItemUtils.isItemEqual(recipeType.getResult(), output, true)) {
					removedRecipes.add((MetalProcessing) recipeType);
					MetalProcessorRecipe.recipes.remove(recipeType);
					break;
				}
			}
		}

		@Override
		public void undo() {
			if (removedRecipes != null) {
				for (MetalProcessing recipe : removedRecipes) {
					if (recipe != null) {
						MetalProcessorRecipe.addRecipe(recipe);
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
