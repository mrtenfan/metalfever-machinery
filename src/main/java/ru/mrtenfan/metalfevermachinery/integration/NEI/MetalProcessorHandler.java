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
import ru.mrtenfan.metalfevermachinery.crafting.MetalProcessorRecipe;
import ru.mrtenfan.metalfevermachinery.crafting.MetalProcessorRecipe.MetalProcessing;
import ru.mrtenfan.metalfevermachinery.crafting.MetalProcessorRecipe.MetalProcessorModes;
import ru.mrtenfan.metalfevermachinery.gui.GUIMetalProcessor;

public class MetalProcessorHandler extends TemplateRecipeHandler {

	public Class <?extends GuiContainer> getGuiClass() {
		return GUIMetalProcessor.class;
	}
	
	public FontRenderer getFontRenderer() {
		return Minecraft.getMinecraft().fontRenderer;
	}

	public class CachedMetalProcessing extends CachedRecipe {

		private final List<PositionedStack> input = new ArrayList<PositionedStack>();
		private final PositionedStack output;
		private int usedRF;
		private int RFPerTick;
		private final MetalProcessorModes mode; 

		@Override
		public List<PositionedStack> getIngredients() {
			return this.getCycledIngredients(MetalProcessorHandler.this.cycleticks / 20, this.input);
		}

		@Override
		public PositionedStack getResult() {
			return this.output;
		}
		
		public String getModeName() {
			String name = null;
			switch(mode) {
			case ROLLING:
				name = I18n.format("mode.rolling", new Object[0]);
				break;
			case CUTTING:
				name = I18n.format("mode.cutting", new Object[0]);
				break;
			case SHAPING:
				name = I18n.format("mode.shaping", new Object[0]);
				break;
			}
			return name;
		}
		
		public int getRFPerTick() {
			return RFPerTick;
		}

		public CachedMetalProcessing(MetalProcessing recipe) {
			if (recipe == null){
				throw new NullPointerException("Recipe must not be null.");
			} else {
				if(recipe.getInput() instanceof OreStack)
					this.input.add(new PositionedStack(((OreStack)recipe.getInput()).getStacks(), 46, 23));
				else if(recipe.getInput() instanceof ItemStack)
					this.input.add(new PositionedStack(((ItemStack)recipe.getInput()), 46, 23));
				else
					Debuging.warnOutput("Not possible to init a recipe of " + recipe.getResult(), MetalFeverMachinery.modName);
				this.output = new PositionedStack(recipe.getResult(), 102, 23);
				this.RFPerTick = recipe.getRFPerTick();
				this.usedRF = recipe.getRFUsed();
				this.mode = recipe.getMode();
			}
		}
	}

	@Override
	public int recipiesPerPage() {
		return 2;
	}

	public String getRecipeID() {
		return "mtf.MetalProcessor";
	}

	@Override
	public String getRecipeName() {
		return I18n.format("container.MetalProcessor", new Object[0]);
	}

	@Override
	public String getGuiTexture() {
		return "metalfevermachinery:textures/GUI/NEI/metal_processor_gui_nei.png";
	}

	public String getOverlayIdentifier() {
		return "MetalProcessor";
	}

    @Override
    public void loadTransferRects () {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(71, 28, 22, 7), this.getRecipeID(), new Object[0]));
    }

	public void drawBackground(int recipeNumber) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(this.getGuiTexture());
		GuiDraw.drawTexturedModalRect(0, 0, -15, 0, 150, 80);
	}

	public void drawExtras(int recipeNumber) {
		float f = this.cycleticks >= 20 ? (float)((this.cycleticks - 20) % 60) / 60.0F : 0.0F;
		if(f > 0.95F) f = 1;
		this.drawProgressBar(71, 28, 160, 0+(5*(int)(f*10)), 20, 5, 0.99F, 0);
		float h = this.cycleticks >= 20 ? (float)((this.cycleticks - 20) % 2580) / 2580.0F : 0.0F;
        this.drawProgressBar(23, 10, 160, 55, 14, 43, h, 7);
        String usedRF = String.valueOf(((CachedMetalProcessing) this.arecipes.get(recipeNumber)).usedRF);
        GuiDraw.drawStringR(usedRF, 30 + getFontRenderer().getStringWidth(usedRF) - getFontRenderer().getStringWidth(usedRF) / 2, 54, 0x808080, false);
        String str = ((CachedMetalProcessing) this.arecipes.get(recipeNumber)).getModeName();
        GuiDraw.drawStringR(str, 80 + getFontRenderer().getStringWidth(str) - getFontRenderer().getStringWidth(str) / 2, 10, 0x808080, false);
	}
	
	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID())) {
            for (MetalProcessing rolling : MetalProcessorRecipe.recipes) {
                this.arecipes.add(new CachedMetalProcessing(rolling));
            }
        }
        else {
            super.loadCraftingRecipes(outputId, results);
        }
	}

    @Override
    public void loadCraftingRecipes (ItemStack result) {
        for (MetalProcessing rolling : MetalProcessorRecipe.recipes) {
            if (NEIServerUtils.areStacksSameTypeCrafting(rolling.getResult(), result) && ItemUtils.isItemEqual(rolling.getResult(), result, true)) {
                this.arecipes.add(new CachedMetalProcessing(rolling));
            }
        }
    }

    @Override
    public void loadUsageRecipes (ItemStack ingred) {
        for (MetalProcessing rolling : MetalProcessorRecipe.recipes) {
			Object input1 = rolling.getInput();
        	if((input1 instanceof OreStack)
					? NEIServerUtils.areStacksSameTypeCrafting(((OreStack)input1).getStack(), ingred) && ItemUtils.isItemEqual(((OreStack)input1).getStack(), ingred, true)
							: NEIServerUtils.areStacksSameTypeCrafting(((ItemStack)input1), ingred) && ItemUtils.isItemEqual(((ItemStack)input1), ingred, false)) {
        		this.arecipes.add(new CachedMetalProcessing(rolling));
            }
        }
    }
}
