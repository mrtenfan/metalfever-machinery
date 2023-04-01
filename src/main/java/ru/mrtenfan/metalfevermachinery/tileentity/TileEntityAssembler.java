package ru.mrtenfan.metalfevermachinery.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import ru.mrtenfan.metalfevermachinery.ConfigFile;
import ru.mrtenfan.metalfevermachinery.blocks.Assembler;
import ru.mrtenfan.metalfevermachinery.crafting.AssemblerRecipe;
import ru.mrtenfan.metalfevermachinery.crafting.AssemblerRecipe.AssemblingRecipe;

public class TileEntityAssembler extends TileEntityBasicMachine implements ISidedInventory {

	private String localyzedName;
	public ItemStack slots[];

	public static int assemblingSpeed = ConfigFile.assemblingSpeed; //in ticks
	public int cookTime;

	private static final int[] slots_in = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
	private static final int[] slots_out = new int[] {9};

	public TileEntityAssembler() {
		slots = new ItemStack[10];
	}

	public static int getAssemblingSpeed() {
		return assemblingSpeed;
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
		return this.hasCustomInventoryName() ? this.localyzedName : "container.assembler";
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
		return slot == 9 ? false : true;
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

	public int getAlloyProgressScaled(int i) {
		return this.cookTime * i / assemblingSpeed;
	}

	public boolean canAssemble(AssemblingRecipe recipe) {
		ItemStack itemStacks[] = new ItemStack[9];
		for(int i = 0; i < 9; i++)
			itemStacks[i] = slots[i];
		ItemStack itemStack = recipe.getResult();
		if(itemStack == null) return false;
		int i = itemStack.stackSize;

		if(slots[9] == null) return true;
		if(!slots[9].isItemEqual(itemStack)) return false;
		else return slots[9].stackSize + i <= 64;
	}

	public void assemblyItem(AssemblingRecipe recipe) {
		if(canAssemble(recipe)) {
			ItemStack itemStacks[] = getInputItemStacks();
			ItemStack itemStack = recipe.getResult();
			int[] number = AssemblerRecipe.getAssemblingNumberIn(itemStacks);

			if (slots[9] == null)
				slots[9] = itemStack.copy();
			else if (slots[9].isItemEqual(itemStack)) 
				slots[9].stackSize += itemStack.stackSize;

			for (int k = 0; k < 9; k++) {
				if(number[k] != 0 && slots[k] != null) {
					if (slots[k].stackSize <= 0)
						slots[k] = new ItemStack(slots[k].getItem().setFull3D());
					else
						slots[k].stackSize -= number[k];


					if (slots[k].stackSize <= 0)
						slots[k] = null;
				}
			}
		}
	}

	public ItemStack[] getInputItemStacks() {
		ItemStack itemStacksTimed[] = new ItemStack[9];
		int i = 0;
		for(i = 0; i < 9; i++) {
			itemStacksTimed[i] = slots[i];
		}
		ItemStack itemStacks[] = new ItemStack[i];
		for(int j = 0; j < itemStacks.length; j++)
			itemStacks[j] = itemStacksTimed[j];
		return itemStacks;
	}

	public boolean isAssembly() {
		return this.cookTime > 0;
	}

	public boolean hasEnergy() {
		return energyStorage.getEnergyStored() > 0;
	}

	public void updateEntity() {
		boolean flag = this.cookTime > 0;
		boolean flag1 = false;
		ItemStack[] itemStacks = new ItemStack[9];
		for(int i = 0; i < itemStacks.length; i++)
			itemStacks[i] = slots[i];
		AssemblingRecipe recipe = AssemblerRecipe.getRecipe(itemStacks);
		int RFPerTick = (recipe != null ? recipe.getRFPerTick() : 0);

		if(this.hasEnergy() && this.canAssemble(recipe)) {
			this.cookTime++;
			energyStorage.modifyEnergyStored(-RFPerTick * 1);	
			                                            //energyMod
			if(this.cookTime == assemblingSpeed) {
				this.cookTime = 0;
				this.assemblyItem(recipe);
				flag1 = true;
			}
		}else {
			cookTime = 0;
		}

		if (flag != this.hasEnergy()) {
			flag1 = true;
			Assembler.updateBlockState(this.cookTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}

		if (flag1) {
			this.markDirty(); 
		}
	}


	@Override
	public int getInfoEnergyPerTick() {
		ItemStack[] itemStacks = new ItemStack[9];
		for(int i = 0; i < itemStacks.length; i++)
			itemStacks[i] = slots[i];
		AssemblingRecipe recipe = AssemblerRecipe.getRecipe(itemStacks);
		int RFPerTick = recipe.getRFPerTick();
		return RFPerTick * 1;
	}                       //energyMod

	@Override
	public int getInfoMaxEnergyPerTick() {
		return 500 * 1;
	}                                  //energyMod

	public int getEnergyStoredScaled(int i) {
		return getInfoEnergyStored() * i / getInfoMaxEnergyStored();
	}
}
