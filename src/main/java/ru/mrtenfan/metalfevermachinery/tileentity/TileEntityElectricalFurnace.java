package ru.mrtenfan.metalfevermachinery.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import ru.mrtenfan.metalfevermachinery.ConfigFile;
import ru.mrtenfan.metalfevermachinery.blocks.ElectricFurnace;
import ru.mrtenfan.metalfevermachinery.crafting.ElectricalFurnaceRecipe;

public class TileEntityElectricalFurnace extends TileEntityBasicMachine implements ISidedInventory {

	private String localyzedName;
	private ItemStack slots[];

	public static int smeltingSpeed = ConfigFile.smeltingSpeed; //in ticks
	public int cookTime;

	private static final int[] slots_in = new int[] {0};
	private static final int[] slots_out = new int[] {1};

	public TileEntityElectricalFurnace() {
		slots = new ItemStack[2];
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
		return this.hasCustomInventoryName() ? this.localyzedName : "container.electricalFurnace";
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

	//Slot, ItemStack, Side
	@Override
	public boolean canInsertItem(int var1, ItemStack itemStack, int var3) {
		return this.isItemValidForSlot(var1, itemStack);
	}

	//Slot, ItemStack, Side
	@Override
	public boolean canExtractItem(int var1, ItemStack itemStack, int var3) {
		return var3 == 0 || var1 != 1;
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
	
	public boolean canSmelt() {
        if (this.slots[0] == null)
            return false;
        else{
            ItemStack itemstack = ElectricalFurnaceRecipe.getSmeltingResult(this.slots[0]);
            if (itemstack == null) return false;
            if (this.slots[1] == null) return true;
            if (!this.slots[1].isItemEqual(itemstack)) return false;
            int result = slots[1].stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= this.slots[1].getMaxStackSize();
        }
	}
	
    public void smeltItem() {
        if (this.canSmelt()) {
            ItemStack itemstack = ElectricalFurnaceRecipe.getSmeltingResult(this.slots[0]);

            if (this.slots[1] == null)
                this.slots[1] = itemstack.copy();
            
            else if (this.slots[1].getItem() == itemstack.getItem())
                this.slots[1].stackSize += itemstack.stackSize;

            --this.slots[0].stackSize;

            if (this.slots[0].stackSize <= 0)
                this.slots[0] = null;
        }
    }

	public boolean isSmelting() {
		return this.cookTime > 0;
	}

	public boolean hasEnergy() {
		return energyStorage.getEnergyStored() > 0;
	}

	public void updateEntity() {
		boolean flag = this.cookTime > 0;
		boolean flag1 = false;
		int RFPerTick = ElectricalFurnaceRecipe.getRFPerTick(slots[0]);

		if(this.hasEnergy() && this.canSmelt()) {
			this.cookTime++;
			energyStorage.modifyEnergyStored(-RFPerTick * 1);	
			                                            //energyMod
			if(this.cookTime == smeltingSpeed) {
				this.cookTime = 0;
				this.smeltItem();
				flag1 = true;
			}
		}else {
			cookTime = 0;
		}

		if (flag != this.cookTime > 0) {
			flag1 = true;
			ElectricFurnace.updateBlockState(this.cookTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
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

	public int getAlloyProgressScaled(int i) {
		return this.cookTime * i / smeltingSpeed;
	}
}