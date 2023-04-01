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
import ru.mrtenfan.metalfevermachinery.crafting.ElectricalFurnaceRecipe;
import ru.mrtenfan.metalfevermachinery.crafting.ElectricalFurnaceRecipe.ElectroSmeltingRecipe;
import ru.mrtenfan.metalfevermachinery.gui.GUIElectricalFurnace;

public class ElectroFurnaceRecipeHandler extends TemplateRecipeHandler {

	public Class <?extends GuiContainer> getGuiClass() {
		return GUIElectricalFurnace.class;
	}
	
	public FontRenderer getFontRenderer() {
		return Minecraft.getMinecraft().fontRenderer;
	}
	
	public class CachedElectroFurnaceRecipe extends CachedRecipe {

		private final List<PositionedStack> input = new ArrayList<PositionedStack>();
		private final PositionedStack output;
		private final int UsedRF;

		@Override
		public List<PositionedStack> getIngredients() {
			return this.getCycledIngredients(ElectroFurnaceRecipeHandler.this.cycleticks / 20, this.input);
		}
		
		@Override
		public PositionedStack getResult() {
			return output;
		}
		
		public int getUsedRF() {
			return UsedRF;
		}
		
		public CachedElectroFurnaceRecipe(ElectroSmeltingRecipe recipe) {
			if(recipe == null)
				throw new NullPointerException("Recipe must not be null.");
			else {
				if(recipe.getInput() instanceof OreStack)
					this.input.add(new PositionedStack(((OreStack)recipe.getInput()).getStacks(), 50, 9));
				else if(recipe.getInput() instanceof ItemStack)
					this.input.add(new PositionedStack(((ItemStack)recipe.getInput()), 50, 9));
				else
					Debuging.warnOutput("Not possible to init a recipe of " + recipe.getResult(), MetalFeverMachinery.modName);
				this.output = new PositionedStack(recipe.getResult(), 110, 17);
				this.UsedRF = recipe.getUsedRF();
			}
		}
	}

	@Override
	public int recipiesPerPage() {
		return 2;
	}

	public String getRecipeID() {
		return "mtf.electricFurnace";
	}

	@Override
	public String getRecipeName() {
		return I18n.format("container.electricalFurnace", new Object[0]);
	}

	@Override
	public String getGuiTexture() {
		return "metalfevermachinery:textures/GUI/NEI/electrical_furnace_gui_nei.png";
	}

	public String getOverlayIdentifier() {
		return "electricFurnace";
	}

    @Override
    public void loadTransferRects () {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(73, 25, 28, 17), this.getRecipeID(), new Object[0]));
    }

	public void drawBackground(int recipeNumber) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(this.getGuiTexture());
		GuiDraw.drawTexturedModalRect(0, 0, -5, 0, 150, 80);
	}

	public void drawExtras(int recipeNumber) {
		float f = this.cycleticks >= 20 ? (float)((this.cycleticks - 20) % 60) / 60.0F : 0.0F;
		this.drawProgressBar(50, 28, 160, 0, 14, 14, f, 7);
		this.drawProgressBar(73, 16, 160, 14, 24, 17, f, 0);
		float h = this.cycleticks >= 20 ? (float)((this.cycleticks - 20) % 2580) / 2580.0F : 0.0F;
        this.drawProgressBar(13, 4, 160, 31, 14, 43, h, 7);
        String usedRF = ((CachedElectroFurnaceRecipe) this.arecipes.get(recipeNumber)).getUsedRF() + "RF";
        GuiDraw.drawStringR(usedRF, 74 - getFontRenderer().getStringWidth(usedRF), 48, 0x808080, false);
	}
	
	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID())) {
            for (ElectroSmeltingRecipe smelting : ElectricalFurnaceRecipe.recipes) {
                this.arecipes.add(new CachedElectroFurnaceRecipe(smelting));
            }
        }
        else {
            super.loadCraftingRecipes(outputId, results);
        }
	}

    @Override
    public void loadCraftingRecipes (ItemStack result) {
        for (ElectroSmeltingRecipe smelting : ElectricalFurnaceRecipe.recipes) {
            if (NEIServerUtils.areStacksSameTypeCrafting(smelting.getResult(), result) && ItemUtils.isItemEqual(smelting.getResult(), result, true)) {
                this.arecipes.add(new CachedElectroFurnaceRecipe(smelting));
            }
        }
    }

    @Override
    public void loadUsageRecipes (ItemStack ingred) {
        for (ElectroSmeltingRecipe smelting : ElectricalFurnaceRecipe.recipes) {
			Object input1 = smelting.getInput();
        	if((input1 instanceof OreStack)
					? NEIServerUtils.areStacksSameTypeCrafting(((OreStack)input1).getStack(), ingred) && ItemUtils.isItemEqual(((OreStack)input1).getStack(), ingred, true)
							: NEIServerUtils.areStacksSameTypeCrafting(((ItemStack)input1), ingred) && ItemUtils.isItemEqual(((ItemStack)input1), ingred, false)) {
        		this.arecipes.add(new CachedElectroFurnaceRecipe(smelting));
            }
        }
    }
}
