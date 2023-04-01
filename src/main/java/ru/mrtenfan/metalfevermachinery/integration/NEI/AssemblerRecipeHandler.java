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
import ru.mrtenfan.MTFCore.utils.OreStack;
import ru.mrtenfan.metalfevermachinery.crafting.AssemblerRecipe;
import ru.mrtenfan.metalfevermachinery.crafting.AssemblerRecipe.AssemblingRecipe;
import ru.mrtenfan.metalfevermachinery.gui.GUIAssembler;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityAssembler;

public class AssemblerRecipeHandler extends TemplateRecipeHandler {

	public Class <?extends GuiContainer> getGuiClass() {
		return GUIAssembler.class;
	}
	
	public FontRenderer getFontRenderer() {
		return Minecraft.getMinecraft().fontRenderer;
	}

	public class CachedAssemblingRecipe extends CachedRecipe {

		public final List<PositionedStack> input = new ArrayList<PositionedStack>();
		public final PositionedStack output;
		public int usedRF;
		public int RFPerTick;

		@Override
		public List<PositionedStack> getIngredients() {
			return this.getCycledIngredients(AssemblerRecipeHandler.this.cycleticks / 20, this.input);
		}

		@Override
		public PositionedStack getResult() {
			return this.output;
		}

		public int getRFUsed() {
			return this.usedRF;
		}

		public CachedAssemblingRecipe(AssemblingRecipe recipe) {
			if (recipe == null){
				throw new NullPointerException("Recipe must not be null.");
			} else {
				for(int i = 0; i < 3; i++)
					for(int j = 0; j < 3; j++)
						if(recipe.getInput(i + j * 3) != null)
							if(recipe.getInput(i + j * 3) instanceof OreStack)
								this.input.add(new PositionedStack(((OreStack)recipe.getInput(i + j * 3)).getStacks(), 35 + i * 18, 6 + j * 18));
							else
								this.input.add(new PositionedStack(recipe.getInput(i + j * 3), 35 + i * 18, 6 + j * 18));
				this.output = new PositionedStack(recipe.result, 129, 23);
				this.usedRF = recipe.getRFPerTick() * TileEntityAssembler.assemblingSpeed;
				this.RFPerTick = recipe.getRFPerTick();
			}
		}
	}

	@Override
	public int recipiesPerPage() {
		return 2;
	}

	public String getRecipeID() {
		return "mtf.assembler";
	}

	@Override
	public String getRecipeName() {
		return I18n.format("container.assembler", new Object[0]);
	}

	@Override
	public String getGuiTexture() {
		return "metalfevermachinery:textures/GUI/NEI/assembly_table_gui_nei.png";
	}

	public String getOverlayIdentifier() {
		return "assembler";
	}

    @Override
    public void loadTransferRects () {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(90, 20, 28, 17), this.getRecipeID(), new Object[0]));
    }

	public void drawBackground(int recipeNumber) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(this.getGuiTexture());
		GuiDraw.drawTexturedModalRect(0, 0, 8, 10, 150, 80);
	}

	public void drawExtras(int recipeNumber) {
		float f = this.cycleticks >= 20 ? (float)((this.cycleticks - 20) % 60) / 60.0F : 0.0F;
		this.drawProgressBar(94, 23, 172, 0, 24, 17, f, 0);
		float h = this.cycleticks >= 20 ? (float)((this.cycleticks - 20) % 2580) / 2580.0F : 0.0F;
        this.drawProgressBar(10, 1, 172, 17, 14, 43, h, 7);
        int usedRF = ((CachedAssemblingRecipe) this.arecipes.get(recipeNumber)).usedRF;
        GuiDraw.drawStringR(String.valueOf(usedRF), 30, 46, 0x808080, false);
        GuiDraw.drawStringR("RF", 24, 54, 0x808080, false);
	}
	
	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID())) {
            for (AssemblingRecipe assembling : AssemblerRecipe.recipes) {
                this.arecipes.add(new CachedAssemblingRecipe(assembling));
            }
        }
        else {
            super.loadCraftingRecipes(outputId, results);
        }
	}

    @Override
    public void loadCraftingRecipes (ItemStack result) {
        for (AssemblingRecipe assembling : AssemblerRecipe.recipes) {
            if (NEIServerUtils.areStacksSameTypeCrafting(assembling.result, result) && ItemUtils.isItemEqual(assembling.result, result, true)) {
                this.arecipes.add(new CachedAssemblingRecipe(assembling));
            }
        }
    }

    @Override
    public void loadUsageRecipes (ItemStack ingred) {
        for (AssemblingRecipe assembling : AssemblerRecipe.recipes) {
        	for(Object input : assembling.input)
        		if((input instanceof OreStack)
    					? NEIServerUtils.areStacksSameTypeCrafting(((OreStack)input).getStack(), ingred) && ItemUtils.isItemEqual(((OreStack)input).getStack(), ingred, true)
    							: NEIServerUtils.areStacksSameTypeCrafting(((ItemStack)input), ingred) && ItemUtils.isItemEqual(((ItemStack)input), ingred, false)) {
        			this.arecipes.add(new CachedAssemblingRecipe(assembling));
            }
        }
    }
}
