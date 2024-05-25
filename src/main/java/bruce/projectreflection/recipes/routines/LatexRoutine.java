package bruce.projectreflection.recipes.routines;

import bruce.projectreflection.materials.FirstTierMaterials;
import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.common.items.MetaItems;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class LatexRoutine {
    public static void init() {
        PRRecipeMaps.STEWER.recipeBuilder()
                .inputs(MetaItems.STICKY_RESIN.getStackForm(20),
                        OreDictUnifier.get(OrePrefix.dust, Materials.Salt, 10))
                .fluidInputs(new FluidStack(FluidRegistry.WATER, 4000))
                .fluidOutputs(FirstTierMaterials.LATEX.getFluid(2000))
                .duration(1200)
                .buildAndRegister();
        PRRecipeMaps.STEWER.recipeBuilder()
                .inputs(OreDictUnifier.get(OrePrefix.dust, Materials.SodaAsh))
                .fluidInputs(FirstTierMaterials.LATEX.getFluid(200))
                .outputs(OreDictUnifier.get(OrePrefix.dust, Materials.RawRubber))
                .fluidOutputs(Materials.Glue.getFluid(200))
                .duration(200)
                .buildAndRegister();
    }
}
