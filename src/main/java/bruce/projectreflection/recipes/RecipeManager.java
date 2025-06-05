package bruce.projectreflection.recipes;

import bruce.projectreflection.items.PRMetaItems;
import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import gregtech.api.unification.material.Materials;
import gregtech.common.items.MetaItems;
import net.minecraft.init.Items;

public class RecipeManager {
    public static void preInit() {

    }
    public static void init() {
        MachineRecipes.register();
        PRRecipeMaps.DIGESTER_RECIPES.recipeBuilder()
                .input(Items.ROTTEN_FLESH, 3)
                .fluidInputs(Materials.Water.getFluid(200))
                .circuitMeta(1)
                .fluidOutputs(Materials.Biomass.getFluid(200))
                .duration(105)
                .EUt(2)
                .buildAndRegister();
        PRRecipeMaps.DIGESTER_RECIPES.recipeBuilder()
                .input(Items.ROTTEN_FLESH, 3)
                .fluidInputs(Materials.Water.getFluid(200))
                .circuitMeta(2)
                .fluidOutputs(Materials.FermentedBiomass.getFluid(200))
                .duration(90)
                .EUt(9)
                .buildAndRegister();
        PRRecipeMaps.DIGESTER_RECIPES.recipeBuilder()
                .input(Items.ROTTEN_FLESH, 3)
                .fluidInputs(Materials.Water.getFluid(200))
                .circuitMeta(3)
                .fluidOutputs(Materials.Methane.getFluid(120))
                .chancedOutput(MetaItems.FERTILIZER, 2000, 0)
                .duration(72)
                .EUt(30)
                .buildAndRegister();

        PRRecipeMaps.SOLID_BOILER_FUELS.recipeBuilder()
                .input("dustBlaze")
                .duration(600)
                .buildAndRegister();
        PRRecipeMaps.SOLID_BOILER_FUELS.recipeBuilder()
                .input("stickBlaze")
                .duration(2400)
                .buildAndRegister();
    }
}
