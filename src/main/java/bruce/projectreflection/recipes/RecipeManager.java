package bruce.projectreflection.recipes;

import bruce.projectreflection.recipes.handler.OreProcessingHandler;
import bruce.projectreflection.recipes.routines.CircuitRoutine;
import bruce.projectreflection.recipes.routines.PhenolicResinRoutine;
import bruce.projectreflection.recipes.routines.MagicRoutine;
import bruce.projectreflection.recipes.routines.SuperIridiumAlloyRoutine;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.material.Materials;

public class RecipeManager {
    public static void preInit() {
        OreProcessingHandler.init();
    }
    public static void init() {
        RecipeRemover.doRemove();
        MachineRecipes.register();
        PhenolicResinRoutine.init();
        SuperIridiumAlloyRoutine.init();
        CircuitRoutine.init();
        MagicRoutine.init();
        //MetaTileEntityAuraCollector.initRecipeMap();
        RecipeMaps.MASS_FABRICATOR_RECIPES.recipeBuilder()
                .circuitMeta(1)
                .EUt(256)
                .duration(3200)
                .fluidOutputs(Materials.UUMatter.getFluid(1))
                .buildAndRegister();
    }
}
