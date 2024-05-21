package bruce.projectreflection.recipes.routines;

import bruce.projectreflection.ProjectReflection;
import bruce.projectreflection.recipes.chemical.Reaction;
import gregtech.api.GTValues;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.ingredients.GTRecipeInput;
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

public class StewStoolStoveRoutine {
    private static void registerRecipesFrom(RecipeMap recipeMap) {
        Collection<Recipe> recipes = recipeMap.getRecipeList();
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

    public static void init() {
        /*
        registerRecipesFrom(RecipeMaps.DISTILLATION_RECIPES);
        registerRecipesFrom(RecipeMaps.ELECTROLYZER_RECIPES);
        registerRecipesFrom(RecipeMaps.MIXER_RECIPES);
        registerRecipesFrom(RecipeMaps.CRACKING_RECIPES);
        //registerRecipesFrom(RecipeMaps.ORE_WASHER_RECIPES);
        registerRecipesFrom(RecipeMaps.CHEMICAL_BATH_RECIPES);
        registerRecipesFrom(RecipeMaps.LARGE_CHEMICAL_RECIPES);
        for(Reaction reaction:Reaction.REGISTRY)
        {
            ProjectReflection.logger.info("{}@{} dH={}",reaction.getReactionName(),reaction.getCriticalTemperature(),reaction.getDh());
        }

         */
        new Reaction(
                new MaterialStack[]{
                        new MaterialStack(Materials.Methane, 1),
                        new MaterialStack(Materials.Oxygen, 4)},
                new MaterialStack[]{
                        new MaterialStack(Materials.CarbonDioxide, 1),
                        new MaterialStack(Materials.Water, 2)
                }, -4.4e-3, 0).register();
        new Reaction(
                new MaterialStack[]{
                        new MaterialStack(Materials.Methane, 1000)},
                new MaterialStack[]{
                        new MaterialStack(Materials.Carbon, 144),
                        new MaterialStack(Materials.Hydrogen, 4)
                }, 0.6, 1300).register();
        new Reaction(
                new MaterialStack[]{
                        new MaterialStack(Materials.Methane, 1000)},
                new MaterialStack[]{
                        new MaterialStack(Materials.Carbon, 144),
                        new MaterialStack(Materials.Hydrogen, 4)
                }, 0.6, 1300).register();
    }
}
