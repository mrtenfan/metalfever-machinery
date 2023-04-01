package ru.mrtenfan.metalfevermachinery.tileentity;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import ru.mrtenfan.MTFCore.utils.OreStack;
import ru.mrtenfan.metalfevermachinery.ConfigFile;
import ru.mrtenfan.metalfevermachinery.blocks.MetalProcessor;
import ru.mrtenfan.metalfevermachinery.crafting.MetalProcessorRecipe;
import ru.mrtenfan.metalfevermachinery.crafting.MetalProcessorRecipe.MetalProcessing;
import ru.mrtenfan.metalfevermachinery.crafting.MetalProcessorRecipe.MetalProcessorModes;

public class TileEntityMetalProcessor extends TileEntityBasicMachine implements ISidedInventory {
	
	private String localyzedName;
	private ItemStack[] slots;
	
	public static int rollingSpeed = ConfigFile.rollingSpeed;
	public int cookTime;
	private MetalProcessorModes mode;
	
	private static final int[] slots_in = new int[] {0};
	private static final int[] slots_out = new int[] {2};
	
	public TileEntityMetalProcessor() {
		slots = new ItemStack[3];
		mode = MetalProcessorModes.ROLLING;
	}

	@Override
	public int getSizeInventory() {
		return slots.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return slots[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (slots[i] != null) {
			if (slots[i].stackSize <= j ) {
				ItemStack itemStack = slots[i];
				slots[i] = null;
				return itemStack;
			}

			ItemStack itemStack1 = slots[i].splitStack(j);

			if (slots[i].stackSize == 0)
				slots[i] = null;

			return itemStack1;
		}else
			return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemStack) {
		slots[i] = itemStack;
		if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
			itemStack.stackSize = getInventoryStackLimit();
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.localyzedName : "container.MetalProcessor";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return this.localyzedName != null && this.localyzedName.length() > 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : player.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return slot == 2 ? false : true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return side == 0 ? slots_out : slots_in;
	}

	@Override
	public boolean canInsertItem(int var1, ItemStack itemStack, int var3) {
		return this.isItemValidForSlot(var1, itemStack);
	}

	@Override
	public boolean canExtractItem(int var1, ItemStack itemStack, int var3) {
		return var3 != 0 || var1 != 1;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NBTTagList list = nbt.getTagList("Items", 10);
		slots = new ItemStack[getSizeInventory()];

		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound nbt1 = (NBTTagCompound)list.getCompoundTagAt(i);
			byte b0 = nbt1.getByte("Slot");

			if (b0 >= 0 && b0 < slots.length) {
				slots[b0] = ItemStack.loadItemStackFromNBT(nbt1);
			}
		}

		cookTime = nbt.getShort("CookTime");
		mode = MetalProcessorModes.values()[nbt.getShort("IntMode")];
	}

	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setShort("CookTime", (short)cookTime);
		nbt.setShort("IntMode", (short) mode.ordinal());
		NBTTagList list = new NBTTagList();

		for (int i = 0; i < slots.length; i++ ) {
			if (slots[i] != null) {
				NBTTagCompound nbt1 = new NBTTagCompound();
				nbt1.setByte("Slot", (byte)i);
				slots[i].writeToNBT(nbt1);
				list.appendTag(nbt1);
			}
		}

		nbt.setTag("Items", list);
	}

	private boolean canRoll(MetalProcessing recipe) {
		if(slots[0] == null) return false;
		
		if(recipe == null || recipe.getResult() == null) return false;
		int i = recipe.getResult().stackSize;
		
		if(recipe.getMode() != this.mode) return false;
		if(slots[2] == null) return true;
		if(!slots[2].isItemEqual(recipe.getResult())) return false;
		else return slots[2].stackSize + i <= 64;
	}

	private void rollItem(MetalProcessing recipe) {
		if(canRoll(recipe)) {
			ItemStack itemStack = recipe.getResult();
			int number = (recipe.getInput() instanceof OreStack ? ((OreStack)recipe.getInput()).stackSize : ((ItemStack)recipe.getInput()).stackSize);
			
			if(slots[2] == null)
				slots[2] = itemStack.copy();
			else if(slots[2].isItemEqual(itemStack))
				slots[2].stackSize += itemStack.stackSize;
			
			if (slots[0].stackSize <= 0)
				slots[0] = new ItemStack(slots[0].getItem().setFull3D());
			else
				slots[0].stackSize -= number;
			
			
			if (slots[0].stackSize <= 0)
				slots[0] = null; 
		}
	}
	
	public void updateEntity() {
			boolean flag = this.cookTime > 0;
			boolean flag1 = false;
			MetalProcessing recipe = MetalProcessorRecipe.getRecipe(slots[0], getMode());
			int RFPerTick = (recipe != null ? recipe.getRFPerTick() : 0);

			if(this.hasEnergy() && this.canRoll(recipe)) {
				this.cookTime++;
				energyStorage.modifyEnergyStored(-RFPerTick);

				if(cookTime == rollingSpeed) {
					cookTime = 0;
					this.rollItem(recipe);
					flag1 = true;
				}
			}else {
				cookTime = 0;
			}

			if (flag != this.hasEnergy()) {
				flag1 = true;
				MetalProcessor.updateBlockState(this.cookTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}

			if (flag1) {
				this.markDirty(); 
			}
	}

	public int getEnergyStoredScaled(int i) {
		return getInfoEnergyStored() * i / getInfoMaxEnergyStored();
	}

	public boolean isRolling() {
		return cookTime > 0;
	}

	public int getRollingProgressScaled(int i) {
		return cookTime * i / rollingSpeed;
	}
	
	public void setMode(MetalProcessorModes i) {
		this.mode = i;
	}
	
	public MetalProcessorModes getMode() {
		return mode;
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

	public MetalProcessorModes getNextMode(MetalProcessorModes mode) {
		MetalProcessorModes ret = null;
		switch(mode) {
		case ROLLING:
			ret = MetalProcessorModes.CUTTING;
			break;
		case CUTTING:
			ret = MetalProcessorModes.SHAPING;
			break;
		case SHAPING:
			ret = MetalProcessorModes.ROLLING;
			break;
		}
		return ret;
	}
}
