package bruce.projectreflection.recipes.routines;

import bruce.projectreflection.recipes.chemical.Reaction;
import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import gregtech.api.GTValues;
import gregtech.api.fluids.store.FluidStorageKeys;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.builders.FuelRecipeBuilder;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.recipes.ingredients.IntCircuitIngredient;
import gregtech.api.unification.FluidUnifier;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.stack.MaterialStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ExothermicRoutine {
    public static void init() {

        Collection<Recipe> recipes = RecipeMaps.LARGE_CHEMICAL_RECIPES.getRecipeList();
        for (Recipe recipe : recipes) {
            int EUt = recipe.getEUt();
            int duration = recipe.getDuration();
            List<GTRecipeInput> input = recipe.getInputs();
            List<GTRecipeInput> fluidInput = recipe.getFluidInputs();
            List<ItemStack> output = recipe.getAllItemOutputs();
            List<FluidStack> fluidOutput = recipe.getAllFluidOutputs();
            List<MaterialStack> leftSide = new ArrayList<>();
            List<MaterialStack> rightSide = new ArrayList<>();
            try {
                for (GTRecipeInput input1 : input) {
                    if (input1.isNonConsumable())
                        continue;
                    ItemStack stack = input1.getInputStacks()[0];
                    if (stack != null) {
                        MaterialStack stack1 = OreDictUnifier.getMaterial(stack);
                        if (stack1 == null)
                            throw new IllegalArgumentException();
                        if (stack1.amount < 0)
                            throw new IllegalArgumentException();
                        leftSide.add(new MaterialStack(stack1.material, stack1.amount * stack.getCount()));
                    }
                }
                for (GTRecipeInput input1 : fluidInput) {
                    if (input1.isNonConsumable())
                        continue;
                    FluidStack stack = input1.getInputFluidStack();
                    if (stack != null) {
                        Material material = FluidUnifier.getMaterialFromFluid(stack.getFluid());
                        if (material == null)
                            throw new IllegalArgumentException();
                        leftSide.add(new MaterialStack(material, stack.amount * (GTValues.M / 144)));
                    }
                }
                for (ItemStack stack : output) {
                    MaterialStack stack1 = OreDictUnifier.getMaterial(stack);
                    if (stack1 == null)
                        throw new IllegalArgumentException();
                    rightSide.add(new MaterialStack(stack1.material, stack1.amount * stack.getCount()));
                }
                for (FluidStack stack : fluidOutput) {
                    if (stack != null) {
                        Material material = FluidUnifier.getMaterialFromFluid(stack.getFluid());
                        if (material == null)
                            throw new IllegalArgumentException();
                        rightSide.add(new MaterialStack(material, stack.amount * (GTValues.M / 144)));
                    }
                }
            } catch (IllegalArgumentException e) {

            }
            if (!leftSide.isEmpty() && !rightSide.isEmpty()) {
                new Reaction(leftSide, rightSide, EUt * duration, EUt).register();
            }
        }
    }
}
