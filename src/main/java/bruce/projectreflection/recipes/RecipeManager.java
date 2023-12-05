package bruce.projectreflection.recipes;

import bruce.projectreflection.recipes.handler.OreProcessingHandler;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.material.Materials;

public class RecipeManager {
    public static void init() {
        RecipeRemover.doRemove();
        MachineRecipes.register();
        OreProcessingHandler.init();
        RecipeMaps.MASS_FABRICATOR_RECIPES.recipeBuilder()
                .circuitMeta(1)
                .EUt(256)
                .duration(3200)
                .fluidOutputs(Materials.UUMatter.getFluid(1))
                .buildAndRegister();
    }
}
