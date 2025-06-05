package bruce.projectreflection.capability.energy;

import net.minecraftforge.energy.IEnergyStorage;

public interface IExtendedEnergyStorage extends IEnergyStorage {
    long receiveEnergyInternal(long maxReceive, boolean simulate);

    /**
     * Removes energy from the storage. Returns quantity of energy that was removed.
     *
     * @param maxExtract Maximum amount of energy to be extracted.
     * @param simulate   If TRUE, the extraction will only be simulated.
     * @return Amount of energy that was (or would have been, if simulated) extracted from the storage.
     */
    long extractEnergyInternal(long maxExtract, boolean simulate);

    /**
     * Returns the amount of energy currently stored.
     */
    long getEnergyStoredLong();

    /**
     * Returns the maximum amount of energy that can be stored.
     */
    long getMaxEnergyStoredLong();
}
