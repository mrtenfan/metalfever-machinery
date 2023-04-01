package ru.mrtenfan.metalfevermachinery.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import cofh.api.tileentity.IEnergyInfo;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import ru.mrtenfan.metalfevermachinery.ConfigFile;

public class TileEntityBasicMachine extends TileEntity implements IEnergyInfo, IEnergyReceiver {

	protected EnergyStorage energyStorage = new EnergyStorage(ConfigFile.BMSettings[0], ConfigFile.BMSettings[1]);

	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		super.readFromNBT(nbt);

		energyStorage.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {

		super.writeToNBT(nbt);

		energyStorage.writeToNBT(nbt);
	}
	
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return energyStorage.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return energyStorage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return energyStorage.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return energyStorage.getMaxEnergyStored() > 0;
	}
	
	@Override
	public int getInfoEnergyPerTick() {
		return ConfigFile.BMSettings[1];
	}

	@Override
	public int getInfoMaxEnergyPerTick() {
		return ConfigFile.BMSettings[1];
	}

	@Override
	public int getInfoEnergyStored() {
		return energyStorage.getEnergyStored();
	}

	@Override
	public int getInfoMaxEnergyStored() {
		return energyStorage.getMaxEnergyStored();
	}
	

	/* Energy Config Class */
//	public static class EnergyConfig {
//
//		public int minPower = 1;
//		public int maxPower = 500;
//		public int maxEnergy = 30000;
//		public int minPowerLevel = 1 * maxEnergy / 10;
//		public int maxPowerLevel = 9 * maxEnergy / 10;
//		public int energyRamp = maxPowerLevel / maxPower;
//
//		public EnergyConfig() {
//
//		}
//
//		public EnergyConfig(EnergyConfig config) {
//
//			this.minPower = config.minPower;
//			this.maxPower = config.maxPower;
//			this.maxEnergy = config.maxEnergy;
//			this.minPowerLevel = config.minPowerLevel;
//			this.maxPowerLevel = config.maxPowerLevel;
//			this.energyRamp = config.energyRamp;
//		}
//
//		public EnergyConfig copy() {
//
//			return new EnergyConfig(this);
//		}
//
//		public boolean setParams(int minPower, int maxPower, int maxEnergy) {
//
//			this.minPower = minPower;
//			this.maxPower = maxPower;
//			this.maxEnergy = maxEnergy;
//			this.maxPowerLevel = maxEnergy * 8 / 10;
//			this.energyRamp = maxPower > 0 ? maxPowerLevel / maxPower : 0;
//			this.minPowerLevel = minPower * energyRamp;
//
//			return true;
//		}
//
//		public boolean setParamsPower(int maxPower) {
//
//			return setParams(maxPower / 4, maxPower, maxPower * 1200);
//		}
//
//		public boolean setParamsPower(int maxPower, int scale) {
//
//			return setParams(maxPower / 4, maxPower, maxPower * 1200 * scale);
//		}
//
//		public boolean setParamsEnergy(int maxEnergy) {
//
//			return setParams(maxEnergy / 4800, maxEnergy / 1200, maxEnergy);
//		}
//
//		public boolean setParamsEnergy(int maxEnergy, int scale) {
//
//			maxEnergy *= scale;
//			return setParams(maxEnergy / 4800, maxEnergy / 1200, maxEnergy);
//		}
//
//		public boolean setParamsDefault(int maxPower) {
//
//			this.maxPower = maxPower;
//			minPower = maxPower / 10;
//			maxEnergy = maxPower * 500;
//			minPowerLevel = 1 * maxEnergy / 10;
//			maxPowerLevel = 9 * maxEnergy / 10;
//			energyRamp = maxPowerLevel / maxPower;
//
//			return true;
//		}
//
//	}
	
	public EnergyStorage getEnergyStorage() {
		return energyStorage;
	}

	public boolean hasEnergy() {
		return energyStorage.getEnergyStored() > 0;
	}
}
