package bruce.projectreflection.metatileentity.multi;

import bruce.projectreflection.PRAbility;
import bruce.projectreflection.capability.energy.IExtendedEnergyStorage;
import gregtech.api.GTValues;
import gregtech.api.capability.impl.AbstractRecipeLogic;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.recipes.RecipeMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.Collections;
import java.util.List;

public abstract class RecipeMapRfMultiblockController extends RecipeMapMultiblockController {
    protected List<IExtendedEnergyStorage> rfEnergyContainer;

    public RecipeMapRfMultiblockController(ResourceLocation metaTileEntityId, RecipeMap<?> recipeMap, int tier, boolean hasPerfectOC) {
        super(metaTileEntityId, recipeMap);
        this.recipeMapWorkable = new RfRecipeLogic(this, tier, hasPerfectOC);
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        this.rfEnergyContainer = Collections.emptyList();
    }

    @Override
    protected void initializeAbilities() {
        super.initializeAbilities();
        this.rfEnergyContainer = this.getAbilities(PRAbility.INPUT_RF);
    }

    @Override
    public TraceabilityPredicate autoAbilities(boolean checkEnergyIn, boolean checkMaintenance, boolean checkItemIn, boolean checkItemOut, boolean checkFluidIn, boolean checkFluidOut, boolean checkMuffler) {
        TraceabilityPredicate predicate = super.autoAbilities(false, checkMaintenance, checkItemIn, checkItemOut, checkFluidIn, checkFluidOut, checkMuffler);
        return checkEnergyIn ? predicate.or(abilities(PRAbility.INPUT_RF).setMinGlobalLimited(1).setMaxGlobalLimited(1).setPreviewCount(1)) : predicate;
    }

    protected static class RfRecipeLogic extends MultiblockRecipeLogic {
        protected final int tier;
        private static final double CONVERSION_RATE = 0.25;

        public RfRecipeLogic(RecipeMapRfMultiblockController tileEntity, int tier, boolean hasPerfectOC) {
            super(tileEntity, hasPerfectOC);
            this.tier = tier;
        }

        @Override
        public long getMaximumOverclockVoltage() {
            return this.getMaxVoltage();
        }

        @Override
        protected long getEnergyInputPerSecond() {
            return this.getEnergyStored();
        }

        protected long getRfEnergyStored() {
            RecipeMapRfMultiblockController controller = (RecipeMapRfMultiblockController) this.metaTileEntity;
            long sum = 0;
            for (IEnergyStorage storage : controller.rfEnergyContainer) {
                sum += storage.getEnergyStored();
            }
            return sum;
        }

        @Override
        protected long getEnergyStored() {

            return (long) Math.ceil(this.getRfEnergyStored() * CONVERSION_RATE);
        }

        @Override
        protected long getEnergyCapacity() {
            RecipeMapRfMultiblockController controller = (RecipeMapRfMultiblockController) this.metaTileEntity;
            long sum = 0;
            for (IEnergyStorage storage : controller.rfEnergyContainer) {
                sum += storage.getMaxEnergyStored();
            }
            return (long) Math.floor(sum * CONVERSION_RATE);
        }

        @Override
        protected boolean drawEnergy(int recipeEUt, boolean simulate) {
            RecipeMapRfMultiblockController controller = (RecipeMapRfMultiblockController) this.metaTileEntity;
            long resultDraw = (long) Math.ceil((double) recipeEUt / CONVERSION_RATE);
            if (resultDraw >= 0 && this.getRfEnergyStored() >= resultDraw) {
                for (IExtendedEnergyStorage storage : controller.rfEnergyContainer) {
                    resultDraw -= storage.extractEnergyInternal(resultDraw, simulate);
                }
                return resultDraw <= 0;
            }
            return false;
        }

        @Override
        public long getMaxVoltage() {
            return GTValues.V[this.tier];
        }

        @Override
        protected long getMaxParallelVoltage() {
            return this.getMaxVoltage();
        }
    }
}
