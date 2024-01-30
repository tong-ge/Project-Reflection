package bruce.projectreflection.recipes;

import bruce.projectreflection.recipes.handler.OreProcessingHandler;
import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import bruce.projectreflection.recipes.routines.CircuitRoutine;
import bruce.projectreflection.recipes.routines.PhenolicResinRoutine;
import bruce.projectreflection.recipes.routines.MagicRoutine;
import bruce.projectreflection.recipes.routines.SuperIridiumAlloyRoutine;
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
        //MetaTileEntityAuraCollector.initRecipeMap();
        RecipeMaps.MASS_FABRICATOR_RECIPES.recipeBuilder()
                .circuitMeta(1)
                .EUt(256)
                .duration(3200)
                .fluidOutputs(Materials.UUMatter.getFluid(1))
                .buildAndRegister();
        PRRecipeMaps.INDUSTRIAL_MOB_FARM.recipeBuilder()
                .circuitMeta(1)
                .EUt(30)
                .duration(1200)
                .outputs(OreDictUnifier.get(OrePrefix.ingot, Materials.Iron, 2),
                        OreDictUnifier.get(OrePrefix.ingot, Materials.Tin, 2),
                        OreDictUnifier.get(OrePrefix.ingot, Materials.Nickel, 2),
                        OreDictUnifier.get(OrePrefix.ingot, Materials.Copper, 2),
                        OreDictUnifier.get(OrePrefix.gem, Materials.Coal, 3)
                )
                .buildAndRegister();
        PRRecipeMaps.INDUSTRIAL_MOB_FARM.recipeBuilder()
                .circuitMeta(2)
                .EUt(120)
                .duration(1200)
                .outputs(OreDictUnifier.get(OrePrefix.dust, Materials.Sulfur, 3),
                        OreDictUnifier.get(OrePrefix.ingot, Materials.Silver, 2),
                        OreDictUnifier.get(OrePrefix.ingot, Materials.Gold, 2),
                        OreDictUnifier.get(OrePrefix.dust, Materials.Aluminium, 2),
                        OreDictUnifier.get(OrePrefix.ingot, Materials.Lead, 2),
                        OreDictUnifier.get(OrePrefix.dust, Materials.Redstone, 3),
                        OreDictUnifier.get(OrePrefix.dust, Materials.Glowstone, 3)
                )
                .buildAndRegister();
        PRRecipeMaps.INDUSTRIAL_MOB_FARM.recipeBuilder()
                .circuitMeta(3)
                .EUt(480)
                .duration(1200)
                .outputs(
                        OreDictUnifier.get(OrePrefix.gem, Materials.Diamond, 2),
                        OreDictUnifier.get(OrePrefix.gem, Materials.Emerald, 2)
                )
                .buildAndRegister();
    }
}
