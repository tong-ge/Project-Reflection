package bruce.projectreflection.recipes.routines;

import bruce.projectreflection.materials.FirstTierMaterials;
import gregtech.api.recipes.GTRecipeHandler;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.ingredients.IntCircuitIngredient;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.common.items.MetaItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public final class PhenolicResinRoutine {
    public static void init() {
        GTRecipeHandler.removeRecipesByInputs(RecipeMaps.ASSEMBLER_RECIPES, new ItemStack[]{
                        OreDictUnifier.get(OrePrefix.dust, Materials.Wood),
                        IntCircuitIngredient.getIntegratedCircuit(1)
                },
                new FluidStack[]{
                        Materials.Glue.getFluid(50)
                });
        RecipeMaps.CHEMICAL_RECIPES.recipeBuilder()
                .fluidInputs(Materials.Creosote.getFluid(1000), Materials.Water.getFluid(1000))
                .inputs(OreDictUnifier.get(OrePrefix.dust, Materials.SodiumHydroxide))
                .fluidOutputs(Materials.Phenol.getFluid(250))
                .EUt(30)
                .duration(320)
                .buildAndRegister();

        RecipeMaps.CHEMICAL_RECIPES.recipeBuilder()
                .fluidInputs(Materials.Methanol.getFluid(2000),
                        Materials.Oxygen.getFluid(1000))
                .notConsumable(OreDictUnifier.get(OrePrefix.dust, Materials.Silver))
                .fluidOutputs(FirstTierMaterials.FORMALDEHYDE.getFluid(2000), Materials.Water.getFluid(2000))
                .EUt(30)
                .duration(800)
                .buildAndRegister();
        RecipeMaps.CHEMICAL_RECIPES.recipeBuilder()
                .fluidInputs(FirstTierMaterials.FORMALDEHYDE.getFluid(1000), Materials.Phenol.getFluid(1000))
                .inputs(OreDictUnifier.get(OrePrefix.dust, Materials.SodiumHydroxide))
                .notConsumable(MetaItems.SHAPE_MOLD_PLATE.getStackForm())
                .fluidOutputs(Materials.Water.getFluid(1000))
                .outputs(OreDictUnifier.get(OrePrefix.plate, FirstTierMaterials.PHENOLIC_RESIN, 8))
                .EUt(30)
                .duration(800)
                .buildAndRegister();
        RecipeMaps.CHEMICAL_RECIPES.recipeBuilder()
                .inputs(OreDictUnifier.get(OrePrefix.plate, FirstTierMaterials.PHENOLIC_RESIN),
                        OreDictUnifier.get(OrePrefix.foil, Materials.Silver, 2))
                .fluidInputs(Materials.SulfuricAcid.getFluid(125))
                .outputs(MetaItems.PHENOLIC_BOARD.getStackForm())
                .EUt(30)
                .duration(320)
                .buildAndRegister();
    }
}
