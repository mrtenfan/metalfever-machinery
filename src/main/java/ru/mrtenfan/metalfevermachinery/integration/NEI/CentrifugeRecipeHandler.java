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
import ru.mrtenfan.MTFCore.utils.ItemUtils;
import ru.mrtenfan.MTFCore.utils.OreDictUtils;
import ru.mrtenfan.metalfevermachinery.crafting.CentrifugeRecipe;
import ru.mrtenfan.metalfevermachinery.crafting.CentrifugeRecipe.CentrifugetionRecipe;
import ru.mrtenfan.metalfevermachinery.gui.GUICentrifuge;

public class CentrifugeRecipeHandler extends TemplateRecipeHandler {

	public Class <?extends GuiContainer> getGuiClass() {
		return GUICentrifuge.class;
	}
	
	public FontRenderer getFontRenderer() {
		return Minecraft.getMinecraft().fontRenderer;
	}

	public class CachedCentrifugetionRecipe extends CachedRecipe {

		private final List<PositionedStack> input = new ArrayList<PositionedStack>();
		private final PositionedStack output1;
		private final List<PositionedStack> output2 = new ArrayList<PositionedStack>();
		private final int UsedRF;

		@Override
		public List<PositionedStack> getIngredients() {
			return this.getCycledIngredients(CentrifugeRecipeHandler.this.cycleticks / 20, this.input);
		}

		@Override
		public PositionedStack getResult() {
			return this.output1;
		}
		
		@Override
		public List<PositionedStack> getOtherStacks() {
			return this.output2;
		}
		
		public int getUsedRF() {
			return UsedRF;
		}

		public CachedCentrifugetionRecipe(CentrifugetionRecipe recipe) {
			if (recipe == null){
				throw new NullPointerException("Recipe must not be null.");
			} else {
				this.input.add(new PositionedStack(OreDictUtils.getStackWithAllOre(recipe.getInput()), 27, 28));
				this.output1 = new  PositionedStack(recipe.getMainResult(), 106, 28);
				if(recipe.getSecondaryResult()[0] != null)
					this.output2.add(new PositionedStack((ItemStack)recipe.getSecondaryResult()[0], 106, 2));
				if(recipe.getSecondaryResult()[1] != null)
					this.output2.add(new PositionedStack((ItemStack)recipe.getSecondaryResult()[1], 79, 28));
				if(recipe.getSecondaryResult()[2] != null)
					this.output2.add(new PositionedStack((ItemStack)recipe.getSecondaryResult()[2], 106, 56));
				if(recipe.getSecondaryResult()[3] != null)
					this.output2.add(new PositionedStack((ItemStack)recipe.getSecondaryResult()[3], 133, 28));
				this.UsedRF = recipe.getUsedRF();
			}
		}
	}

	@Override
	public int recipiesPerPage() {
		return 1;
	}

	public String getRecipeID() {
		return "mtf.centrifuge";
	}

	@Override
	public String getRecipeName() {
		return I18n.format("container.centrifuge", new Object[0]);
	}

	@Override
	public String getGuiTexture() {
		return "metalfevermachinery:textures/GUI/NEI/centrifuge_gui_nei.png";
	}

	public String getOverlayIdentifier() {
		return "centrifuge";
	}

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(48, 17, 28, 28), this.getRecipeID(), new Object[0]));
    }
    
    public void drawBackground(int recipeNumber) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(this.getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 3, 0, 150, 80);
    }
    
    public void drawExtras(int recipeNumber) {
    	float f = this.cycleticks >= 20 ? (float)((this.cycleticks - 20) % 60) / 60.0F : 0.0F;
    	this.drawProgressBar(49, 24, 160, 0, 24, 24, f, 0);
    	float h = this.cycleticks >= 20 ? (float)((this.cycleticks - 20) % 2580) / 2580.0F : 0.0F;
    	this.drawProgressBar(7, 26, 160, 32, 14, 43, h, 7);
    	String usedRF = ((CachedCentrifugetionRecipe) this.arecipes.get(recipeNumber)).getUsedRF() + "RF";
        GuiDraw.drawStringR(usedRF, 66 + 42 - getFontRenderer().getStringWidth(usedRF), 54, 0x808080, false);
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if(outputId.equals(this.getRecipeID())) {
            for(CentrifugetionRecipe processing : CentrifugeRecipe.recipes) {
                this.arecipes.add(new CachedCentrifugetionRecipe(processing));
            }
        }
        else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    
    public void loadCraftingRecipes(ItemStack result) {
        for(CentrifugetionRecipe processing : CentrifugeRecipe.recipes) {
            if(NEIServerUtils.areStacksSameTypeCrafting(processing.getMainResult(), result) && ItemUtils.isItemEqual(processing.getMainResult(), result, true)) {
                this.arecipes.add(new CachedCentrifugetionRecipe(processing));
            } else {
            	for(ItemStack itemstack : processing.getSecondaryResult()) {
            		if(itemstack != null) {
            			if(NEIServerUtils.areStacksSameTypeCrafting(itemstack, result) && ItemUtils.isItemEqual(itemstack, result, true)) {
            				this.arecipes.add(new CachedCentrifugetionRecipe(processing));
            			}
            		}
            	}
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingred) {
        for(CentrifugetionRecipe processing : CentrifugeRecipe.recipes) {
            if(NEIServerUtils.areStacksSameTypeCrafting(processing.getInput(), ingred) && ItemUtils.isItemEqual(processing.getInput(), ingred, true)) {
                this.arecipes.add(new CachedCentrifugetionRecipe(processing));
            }
        }
    }
}
