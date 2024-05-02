package bruce.projectreflection.recipes.routines;

import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import gregtech.api.GTValues;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.builders.FuelRecipeBuilder;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FuelCellRoutine {
    public static void init() {
        Collection<Recipe> electrolyzerRecipes = RecipeMaps.ELECTROLYZER_RECIPES.getRecipeList();
        for (Recipe recipe : electrolyzerRecipes) {
            int EUt = (int) GTValues.V[GTValues.LV];
            int duration = recipe.getDuration() * recipe.getEUt() / EUt;
            List<GTRecipeInput> input = recipe.getInputs();
            List<GTRecipeInput> fluidInput = recipe.getFluidInputs();
            List<ItemStack> output = recipe.getOutputs();
            List<FluidStack> fluidOutput = recipe.getFluidOutputs();

            if (!fluidOutput.isEmpty()) {
                ItemStack[] fuelCellOutput = input.stream().filter(input2 -> !input2.isNonConsumable()).map(input2 -> input2.getInputStacks()[0]).collect(Collectors.toList()).toArray(new ItemStack[0]);
                FluidStack[] fuelCellFluidOutput = fluidInput.stream().map(input2 -> input2.getInputFluidStack()).collect(Collectors.toList()).toArray(new FluidStack[0]);

                FuelRecipeBuilder builder = PRRecipeMaps.FUEL_CELL.recipeBuilder();
                if (!output.isEmpty())
                    builder.inputs(output.toArray(new ItemStack[0]));
                if (!fluidOutput.isEmpty())
                    builder.fluidInputs(fluidOutput.toArray(new FluidStack[0]));
                if (fuelCellOutput.length != 0)
                    builder.outputs(fuelCellOutput);
                if (fuelCellFluidOutput.length != 0)
                    builder.fluidOutputs(fuelCellFluidOutput);
                builder.EUt(EUt)
                        .duration(duration)
                        //.circuitMeta((int)(Math.log(recipe.getEUt())/Math.log(2.0)))
                        .buildAndRegister();
            }
        }
    }
}
