package ru.mrtenfan.metalfevermachinery.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import ru.mrtenfan.MTFCore.utils.ItemUtils;
import ru.mrtenfan.metalfevermachinery.ConfigFile;
import ru.mrtenfan.metalfevermachinery.blocks.Centrifuge;
import ru.mrtenfan.metalfevermachinery.crafting.CentrifugeRecipe;
import ru.mrtenfan.metalfevermachinery.crafting.CentrifugeRecipe.CentrifugetionRecipe;

public class TileEntityCentrifuge extends TileEntityAdvMachine implements ISidedInventory {

	private String localyzedName;
	private ItemStack slots[];

	public static int centrifugetionSpeed = ConfigFile.centrifugingSpeed; //in ticks
	public int cookTime;

	private static final int[] slots_in = new int[] {0};
	private static final int[] slots_out = new int[] {1, 2, 3, 4, 5};

	public TileEntityCentrifuge() {
		slots = new ItemStack[6];
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
		return this.hasCustomInventoryName() ? this.localyzedName : "container.centrifuge";
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
		return slot == 0 ? true : false;
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
	}

	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setShort("CookTime", (short)cookTime);
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
	
	

	public boolean isCentrifugetion() {
		return cookTime > 0;
	}

	public boolean hasEnergy() {
		return energyStorage.getEnergyStored() > 0;
	}
	
	private boolean canСentrifugation(CentrifugetionRecipe recipe) {
		if(slots[0] == null)
			return false;
		
		ItemStack itemStack = recipe.getMainResult();
		int[] i = recipe.getSecondaryResultNumber();

		if(itemStack == null) return false;
		if(slots[1] == null && slots[2] == null && slots[3] == null && slots[4] == null && slots[5] == null) return true;
		if(!ItemUtils.isItemEqual(slots[1], itemStack, true) && !ItemUtils.isItemEqual(slots[2], itemStack, true) && !ItemUtils.isItemEqual(slots[3], itemStack, true) && 
				!ItemUtils.isItemEqual(slots[4], itemStack, true) && !ItemUtils.isItemEqual(slots[5], itemStack, true)) return false;
		else return slots[1].stackSize + itemStack.stackSize <= 64 && slots[2].stackSize + i[0] <= 64 && slots[3].stackSize + i[2] <= 64 && slots[4].stackSize + i[3] <= 64 &&
				slots[5].stackSize + i[4] <= 64;
	}
	
	private void centrifugationItem(CentrifugetionRecipe recipe) {
		if(canСentrifugation(recipe)) {
			ItemStack itemStack = recipe.getMainResult();
			ItemStack[] itemStacks = recipe.getSecondaryResult();
			int number = itemStack.stackSize;

			if (slots[1] == null)
				slots[1] = itemStack.copy();
			else if (slots[1].isItemEqual(itemStack)) 
				slots[1].stackSize += itemStack.stackSize;
			
			for(int i = 0; i < 4; i++) {
				if (slots[i+2] == null)
					slots[i+2] = itemStacks[i].copy();
				else if (slots[i+2].isItemEqual(itemStacks[i])) 
					slots[i+2].stackSize += itemStacks[i].stackSize;
			}
			
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
		CentrifugetionRecipe recipe = CentrifugeRecipe.getRecipe(slots[0]);
		int RFPerTick = (recipe != null ? recipe.getRFPerTick() : 0);

		if(this.hasEnergy() && this.canСentrifugation(recipe)) {
			this.cookTime++;
			energyStorage.modifyEnergyStored(-RFPerTick * 1);	
			                                            //energyMod
			if(this.cookTime == centrifugetionSpeed) {
				this.cookTime = 0;
				this.centrifugationItem(recipe);
				flag1 = true;
			}
		}else {
			cookTime = 0;
		}

		if (flag != this.hasEnergy()) {
			flag1 = true;
			Centrifuge.updateBlockState(this.cookTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}

		if (flag1) {
			this.markDirty(); 
		}
	}

	@Override
	public int getInfoMaxEnergyPerTick() {
		return 500 * 1;
	}                                  //energyMod

	public int getEnergyStoredScaled(int i) {
		return getInfoEnergyStored() * i / getInfoMaxEnergyStored();
	}

	public int getCentrifugetionProgressScaled(int i) {
		return this.cookTime * i / centrifugetionSpeed;
	}
}
