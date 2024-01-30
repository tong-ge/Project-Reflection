package bruce.projectreflection.recipes.routines;

import bruce.projectreflection.materials.FirstTierMaterials;
import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import net.minecraft.item.ItemStack;
import thaumcraft.api.blocks.BlocksTC;

public class MagicRoutine {

    public static void init() {
        PRRecipeMaps.AURA_COLLECTOR.recipeBuilder()
                .circuitMeta(1)
                .fluidOutputs(FirstTierMaterials.AURA.getFluid(1))
                .aura(12.0f, false)
                .EUt(20)
                .duration(10)
                .buildAndRegister();
        PRRecipeMaps.AURA_COLLECTOR.recipeBuilder()
                .notConsumable(new ItemStack(BlocksTC.arcaneWorkbenchCharger))
                .fluidOutputs(FirstTierMaterials.AURA.getFluid(1))
                .aura(12.0f, true)
                .EUt(20)
                .duration(10)
                .buildAndRegister();
        PRRecipeMaps.MAGICAL_GENERATOR.recipeBuilder()
                .fluidInputs(FirstTierMaterials.AURA.getFluid(1))
                .EUt(32)
                .duration(100)
                .buildAndRegister();
    }
}
