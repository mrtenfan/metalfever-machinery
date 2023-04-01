package ru.mrtenfan.metalfevermachinery.integration.NEI;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import ru.mrtenfan.MTFCore.Debuging;
import ru.mrtenfan.MTFCore.utils.ItemUtils;
import ru.mrtenfan.MTFCore.utils.OreStack;
import ru.mrtenfan.metalfevermachinery.MetalFeverMachinery;
import ru.mrtenfan.metalfevermachinery.crafting.AlloySmelterRecipe;
import ru.mrtenfan.metalfevermachinery.crafting.AlloySmelterRecipe.AlloySmelteringRecipe;
import ru.mrtenfan.metalfevermachinery.gui.GUIAlloySmelter;

public class AlloySmelterRecipeHandler extends TemplateRecipeHandler {

	public Class <?extends GuiContainer> getGuiClass() {
		return GUIAlloySmelter.class;
	}
	
	public FontRenderer getFontRenderer() {
		return Minecraft.getMinecraft().fontRenderer;
	}

	public class CachedAlloyingRecipe extends CachedRecipe {

		private final List<PositionedStack> input = new ArrayList<PositionedStack>();
		private final PositionedStack output;
		private final int UsedRF;

		@Override
		public List<PositionedStack> getIngredients() {
			return this.getCycledIngredients(AlloySmelterRecipeHandler.this.cycleticks / 20, this.input);
		}

		@Override
		public PositionedStack getResult() {
			return this.output;
		}
		
		public int getUsedRF() {
			return UsedRF;
		}

		public CachedAlloyingRecipe(AlloySmelteringRecipe recipe) {
			if (recipe == null){
				throw new NullPointerException("Recipe must not be null.");
			} else {
				if(recipe.getInput(0) instanceof OreStack)
					this.input.add(new PositionedStack(((OreStack)recipe.getInput(0)).getStacks(), 31, 9));
				else if(recipe.getInput(0) instanceof ItemStack)
					this.input.add(new PositionedStack(((ItemStack)recipe.getInput(0)), 31, 9));
				else
					Debuging.warnOutput("Not possible to init a recipe of " + recipe.getResult(), MetalFeverMachinery.modName);
				if(recipe.getInput(1) instanceof OreStack)
					this.input.add(new PositionedStack(((OreStack)recipe.getInput(1)).getStacks(), 53, 9));
				else if(recipe.getInput(1) instanceof ItemStack)
					this.input.add(new PositionedStack(((ItemStack)recipe.getInput(1)), 53, 9));
				else
					Debuging.warnOutput("Not possible to init a recipe of " + recipe.getResult(), MetalFeverMachinery.modName);
				this.output = new PositionedStack(recipe.getResult(), 107, 17);
				this.UsedRF = recipe.getUsedRF();
			}
		}
	}

	@Override
	public int recipiesPerPage() {
		return 2;
	}

	public String getRecipeID() {
		return "mtf.alloySmelter";
	}

	@Override
	public String getRecipeName() {
		return I18n.format("container.alloySmelter", new Object[0]);
	}

	@Override
	public String getGuiTexture() {
		return "metalfevermachinery:textures/GUI/NEI/alloy_smelter_gui_nei.png";
	}

	public String getOverlayIdentifier() {
		return "alloySmelter";
	}

    @Override
    public void loadTransferRects () {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(73, 20, 28, 17), this.getRecipeID(), new Object[0]));
    }
    
    public void drawBackground(int recipeNumber) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(this.getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, -4, 0, 150, 80);
    }
    
    public void drawExtras(int recipeNumber) {
    	float f = this.cycleticks >= 20 ? (float)((this.cycleticks - 20) % 60) / 60.0F : 0.0F;
    	this.drawProgressBar(74, 16, 160, 0, 24, 17, f, 0);
    	this.drawProgressBar(42, 28, 160, 17, 14, 14, f, 7);
    	float h = this.cycleticks >= 20 ? (float)((this.cycleticks - 20) % 2580) / 2580.0F : 0.0F;
    	this.drawProgressBar(8, 5, 160, 32, 14, 43, h, 7);
    	String usedRF = ((CachedAlloyingRecipe)this.arecipes.get(recipeNumber)).getUsedRF() + "RF";
        GuiDraw.drawStringR(usedRF, 81 - getFontRenderer().getStringWidth(usedRF), 48, 0x808080, false);
    }

    @Override
    public void loadCraftingRecipes (String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID())) {
            for (AlloySmelteringRecipe alloying : AlloySmelterRecipe.recipes) {
                this.arecipes.add(new CachedAlloyingRecipe(alloying));
            }
        }
        else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes (ItemStack result) {
        for (AlloySmelteringRecipe alloying : AlloySmelterRecipe.recipes) {
            if (NEIServerUtils.areStacksSameTypeCrafting(alloying.getResult(), result) && ItemUtils.isItemEqual(alloying.getResult(), result, true)) {
                this.arecipes.add(new CachedAlloyingRecipe(alloying));
            }
        }
    }

    @Override
    public void loadUsageRecipes (ItemStack ingred) {
        for (AlloySmelteringRecipe alloying : AlloySmelterRecipe.recipes) {
			Object input1 = alloying.getInput(0), input2 = alloying.getInput(1);
			if((input1 instanceof OreStack)
					? NEIServerUtils.areStacksSameTypeCrafting(((OreStack)input1).getStack(), ingred) && ItemUtils.isItemEqual(((OreStack)input1).getStack(), ingred, true)
							: NEIServerUtils.areStacksSameTypeCrafting(((ItemStack)input1), ingred) && ItemUtils.isItemEqual(((ItemStack)input1), ingred, false)
							|| (input2 instanceof OreStack) ? NEIServerUtils.areStacksSameTypeCrafting(((OreStack)input2).getStack(), ingred) && ItemUtils.isItemEqual(((OreStack)input2).getStack(), ingred, true)
									: NEIServerUtils.areStacksSameTypeCrafting(((ItemStack)input2), ingred) && ItemUtils.isItemEqual(((ItemStack)input2), ingred, false))
				this.arecipes.add(new CachedAlloyingRecipe(alloying));
        }
    }
}