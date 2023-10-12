package bruce.projectreflection.metatileentity;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.ProjectReflection;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.IRotorHolder;
import gregtech.api.capability.impl.MultiblockFuelRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockWithDisplayBase;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.unification.material.Materials;
import gregtech.api.util.TextFormattingUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class FissionWorkableHandler extends MultiblockFuelRecipeLogic {
    private MetaTileEntityFission fission;

    private static final FluidStack WATER_STACK= Materials.Water.getFluid(1000);
    public FissionWorkableHandler(MetaTileEntityFission tileEntity) {
        super(tileEntity);
        fission=tileEntity;
    }

    @Override
    protected long boostProduction(long production) {
        return Math.round((double)production*(1+fission.getTemperaturePercentage()*1.5));
    }

    @Override
    public void setParallelRecipesPerformed(int amount) {
        super.setParallelRecipesPerformed(amount);
        int heatMultiplier=1;
        if(amount>1)
        {
            heatMultiplier = amount;
        }
        fission.heatMultiplier=heatMultiplier;
    }

    @Override
    protected void updateRecipeProgress() {

        if (this.canRecipeProgress) {
            ProjectReflection.logger.info("Generating:{} EU/t",this.recipeEUt);
            this.drawEnergy(this.recipeEUt, false);

            int heatMultiplier= fission.heatMultiplier;
            if(fission.getTemperaturePercentage()>0.5)
            {
                heatMultiplier*=2;
            }
            double mean=Math.abs(0.08d*(double)(this.recipeEUt* heatMultiplier));
            double difference= mean/4.0d;
            double heat=Math.max(mean+ PRConstants.rand.nextGaussian()*difference,0);
            ProjectReflection.logger.info("update Recipe Progress at {}, {} heat added,(mean={},difference={},multiplier={})",
                    System.currentTimeMillis(),heat,mean,difference,heatMultiplier);
            fission.temperature+=heat;
            if (++this.progressTime > this.maxProgressTime) {
                this.completeRecipe();
            }
        }
        IMultipleTankHandler inputTank=fission.getInputFluidInventory();
        while(fission.temperature>4000)
        {
            if(!WATER_STACK.isFluidStackIdentical(inputTank.drain(WATER_STACK,false)))
            {
                break;
            }
            inputTank.drain(WATER_STACK,true);
            fission.temperature-=500;
        }
        fission.checkExplode();
    }
    @Override
    public FluidStack getInputFluidStack() {
        try {
            return super.getInputFluidStack();
        }
        catch (IndexOutOfBoundsException e)
        {
            return null;
        }
    }
    public String getRecipeFluidInputInfo() {
        String item="L";
        Recipe recipe;
        if (this.previousRecipe == null) {
            recipe = this.findRecipe(2147483647L, this.getInputInventory(), this.getInputTank());
            if (recipe == null) {
                return null;
            }
        } else {
            recipe = this.previousRecipe;
        }
        int neededAmount;
        int ocAmount = (int) (this.getMaxVoltage() / (long) recipe.getEUt());
        if(!recipe.getFluidInputs().isEmpty()) {
            FluidStack requiredFluidInput = ((GTRecipeInput) recipe.getFluidInputs().get(0)).getInputFluidStack();
            neededAmount = ocAmount * requiredFluidInput.amount;
        }
        else{
            ItemStack requiredItemInput=(recipe.getInputs().get(0)).getInputStacks()[0];
            neededAmount=requiredItemInput.getCount();
            item=" "+requiredItemInput.getDisplayName();
        }
        return TextFormatting.RED + TextFormattingUtil.formatNumbers((long)neededAmount) + item;
    }

    @Override
    public boolean isAllowOverclocking() {
        return false;
    }
}
