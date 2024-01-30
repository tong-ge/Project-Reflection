package bruce.projectreflection.api;

import bruce.projectreflection.PRAbility;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.recipes.RecipeMap;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public abstract class RecipeMapMechanicalMultiblockController extends RecipeMapMultiblockController {
    protected RecipeMapMechanicalMultiblockController(ResourceLocation metaTileEntityId, RecipeMap<?> recipeMap) {
        super(metaTileEntityId, recipeMap);
        this.recipeMapWorkable = new MechanicalMultiblockRecipeLogic(this);
    }

    public List<IGTMechCapability> mechCapability = new ArrayList<>();

    @Override
    public TraceabilityPredicate autoAbilities() {
        return autoAbilities(false, true, true, true, true, true, true).or(abilities(PRAbility.INPUT_MECH));
    }

    @Override
    protected void initializeAbilities() {
        super.initializeAbilities();
        mechCapability = this.getAbilities(PRAbility.INPUT_MECH);
    }

    public static class MechanicalMultiblockRecipeLogic extends MultiblockRecipeLogic {
        public int getParallelLimit() {
            return (int) (getMaxVoltage() / Math.max(getRecipeEUt(), 32));
        }

        public MechanicalMultiblockRecipeLogic(RecipeMapMechanicalMultiblockController tileEntity) {
            super(tileEntity, true);
        }

        @Override
        protected long getEnergyStored() {
            long energyStored = 0;
            List<IGTMechCapability> mechCapability = ((RecipeMapMechanicalMultiblockController) this.metaTileEntity).mechCapability;
            for (IGTMechCapability capability : mechCapability) {
                energyStored += capability.getPower(null);
            }
            return energyStored;
        }

        @Override
        protected long getEnergyCapacity() {
            long energyStored = 0;
            List<IGTMechCapability> mechCapability = ((RecipeMapMechanicalMultiblockController) this.metaTileEntity).mechCapability;
            for (IGTMechCapability capability : mechCapability) {
                energyStored += capability.getMaxPower();
            }
            return energyStored;
        }

        @Override
        protected boolean drawEnergy(int recipeEUt, boolean simulate) {
            return super.drawEnergy(recipeEUt, true);
        }

        @Override
        public long getMaxVoltage() {
            long energyStored = 0;
            List<IGTMechCapability> mechCapability = ((RecipeMapMechanicalMultiblockController) this.metaTileEntity).mechCapability;
            for (IGTMechCapability capability : mechCapability) {
                energyStored = Math.max(capability.getMaxPower(), energyStored);
            }
            return energyStored;
        }
    }
}
