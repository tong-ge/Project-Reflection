package bruce.projectreflection.recipes;

import bruce.projectreflection.recipes.handler.OreProcessingHandler;
import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import bruce.projectreflection.recipes.routines.*;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;

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
        RecipeMaps.MASS_FABRICATOR_RECIPES.recipeBuilder()
                .circuitMeta(1)
                .EUt(256)
                .duration(3200)
                .fluidOutputs(Materials.UUMatter.getFluid(1))
                .buildAndRegister();
        MobToMineralRoutine.init();
        FuelCellRoutine.init();
    }
}
