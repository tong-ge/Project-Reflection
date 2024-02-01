package bruce.projectreflection.api;

import bruce.projectreflection.PRAbility;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.ItemHandlerList;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.recipes.RecipeMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class RecipeMapMechanicalMultiblockController extends RecipeMapMultiblockController {
    protected RecipeMapMechanicalMultiblockController(ResourceLocation metaTileEntityId, RecipeMap<?> recipeMap) {
        super(metaTileEntityId, recipeMap);
        this.recipeMapWorkable = new MechanicalMultiblockRecipeLogic(this);
    }

    public List<IGTMechCapability> mechCapability = new ArrayList<>();

    public TraceabilityPredicate autoAbilities(boolean checkEnergyIn, boolean checkMaintenance, boolean checkItemIn, boolean checkItemOut, boolean checkFluidIn, boolean checkFluidOut, boolean checkMuffler) {
        TraceabilityPredicate predicate = super.autoAbilities(checkMaintenance, checkMuffler);
        if (checkEnergyIn) {
            predicate = predicate.or(abilities(PRAbility.INPUT_MECH).setMinGlobalLimited(1).setMaxGlobalLimited(2).setPreviewCount(1));
        }

        if (checkItemIn && this.recipeMap.getMaxInputs() > 0) {
            predicate = predicate.or(abilities(MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.STEAM_IMPORT_ITEMS).setPreviewCount(1));
        }

        if (checkItemOut && this.recipeMap.getMaxOutputs() > 0) {
            predicate = predicate.or(abilities(MultiblockAbility.EXPORT_ITEMS, MultiblockAbility.STEAM_EXPORT_ITEMS).setPreviewCount(1));
        }

        if (checkFluidIn && this.recipeMap.getMaxFluidInputs() > 0) {
            predicate = predicate.or(abilities(MultiblockAbility.IMPORT_FLUIDS).setPreviewCount(1));
        }

        if (checkFluidOut && this.recipeMap.getMaxFluidOutputs() > 0) {
            predicate = predicate.or(abilities(MultiblockAbility.EXPORT_FLUIDS).setPreviewCount(1));
        }

        return predicate;
    }

    @Override
    protected void initializeAbilities() {
        super.initializeAbilities();
        mechCapability = this.getAbilities(PRAbility.INPUT_MECH);
        List<IItemHandler> importItems = new ArrayList<>(this.getAbilities(MultiblockAbility.IMPORT_ITEMS));
        importItems.addAll(this.getAbilities(MultiblockAbility.STEAM_IMPORT_ITEMS));
        List<IItemHandler> exportItems = new ArrayList<>(this.getAbilities(MultiblockAbility.EXPORT_ITEMS));
        exportItems.addAll(this.getAbilities(MultiblockAbility.STEAM_EXPORT_ITEMS));
        this.inputInventory = new ItemHandlerList(importItems);
        this.outputInventory = new ItemHandlerList(exportItems);
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
                energyStored += capability.getEffectiveEUt();
            }
            return energyStored;
        }

        @Override
        protected long getEnergyCapacity() {
            long energyStored = 0;
            List<IGTMechCapability> mechCapability = ((RecipeMapMechanicalMultiblockController) this.metaTileEntity).mechCapability;
            for (IGTMechCapability capability : mechCapability) {
                energyStored += capability.getMaxEU();
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
                energyStored = Math.max(capability.getMaxEU(), energyStored);
            }
            return energyStored;
        }
    }
}
