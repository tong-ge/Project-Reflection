package bruce.projectreflection.api;

import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import mysticalmechanics.api.IMechCapability;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MechanicalTurbineRecipeLogic extends MultiblockRecipeLogic {
    public boolean isAllowOverclocking() {
        return false;
    }

    public int getParallelLimit() {
        return (int) (getMaxVoltage() / Math.max(getRecipeEUt(), 32));
    }

    public MechanicalTurbineRecipeLogic(RecipeMapMultiblockController tileEntity) {
        super(tileEntity, true);
    }

    protected boolean hasEnoughPower(@NotNull int[] resultOverclock) {
        return true;
    }

    public void setWorkingEnabled(boolean workingEnabled) {
        super.setWorkingEnabled(workingEnabled);
        if (!workingEnabled) {
            List<IGTMechCapability> mechCapability = ((MechanicalTurbineMultiblockController) this.metaTileEntity).mechCapability;
            for (IMechCapability capability : mechCapability) {
                capability.setPower(0, null);
            }
        }
    }

    @Override
    protected boolean drawEnergy(int recipeEUt, boolean simulate) {
        if (!simulate) {
            List<IGTMechCapability> mechCapability = ((MechanicalTurbineMultiblockController) this.metaTileEntity).mechCapability;
            for (IGTMechCapability capability : mechCapability) {
                capability.setEU((double) recipeEUt / mechCapability.size());
            }
        }
        return true;
    }

    @Override
    protected void completeRecipe() {
        super.completeRecipe();
        List<IGTMechCapability> mechCapability = ((MechanicalTurbineMultiblockController) this.metaTileEntity).mechCapability;
        for (IMechCapability capability : mechCapability) {
            capability.setPower(0, null);
        }
    }

    public long getMaxVoltage() {
        long maxVoltage = 0;
        List<IGTMechCapability> mechCapability = ((MechanicalTurbineMultiblockController) this.metaTileEntity).mechCapability;
        for (IGTMechCapability capability : mechCapability) {
            maxVoltage += capability.getMaxEU();
        }
        return maxVoltage;
    }
}
