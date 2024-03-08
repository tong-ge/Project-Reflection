package bruce.projectreflection.recipes.routines;

import bruce.projectreflection.materials.FirstTierMaterials;
import bruce.projectreflection.materials.SecondTierMaterials;
import bruce.projectreflection.materials.ThirdTierMaterials;
import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import gregtech.api.GTValues;
import gregtech.api.recipes.RecipeMaps;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.config.ConfigBlocks;

public class MagicRoutine {

    public static void init() {
        //aura
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
        //mana
        RecipeMaps.CHEMICAL_RECIPES.recipeBuilder()
                .circuitMeta(1)
                .fluidInputs(FirstTierMaterials.AURA.getFluid(4000), FirstTierMaterials.FLUIDIED_MANA.getFluid(1000))
                .fluidOutputs(SecondTierMaterials.BOOSTED_AURA.getFluid(5000))
                .EUt(GTValues.VA[GTValues.LV])
                .duration(20)
                .buildAndRegister();
        PRRecipeMaps.MAGICAL_GENERATOR.recipeBuilder()
                .fluidInputs(SecondTierMaterials.BOOSTED_AURA.getFluid(1))
                .EUt((int) GTValues.V[GTValues.LV])
                .duration(150)
                .buildAndRegister();
        PRRecipeMaps.MAGICAL_GENERATOR.recipeBuilder()
                .fluidInputs(FirstTierMaterials.FLUIDIED_MANA.getFluid(64))
                .EUt((int) GTValues.V[GTValues.LV])
                .duration(5)
                .buildAndRegister();
        //nether wart
        RecipeMaps.CHEMICAL_RECIPES.recipeBuilder()
                .inputs(new ItemStack(Items.NETHER_WART, 16))
                .fluidInputs(SecondTierMaterials.BOOSTED_AURA.getFluid(5000), new FluidStack(ConfigBlocks.FluidDeath.instance, 1000))
                .fluidOutputs(ThirdTierMaterials.REFINED_MAGICAL_FUEL.getFluid(6000))
                .EUt(GTValues.VA[GTValues.HV])
                .duration(2)
                .buildAndRegister();
        PRRecipeMaps.MAGICAL_GENERATOR.recipeBuilder()
                .fluidInputs(ThirdTierMaterials.REFINED_MAGICAL_FUEL.getFluid(1))
                .EUt((int) GTValues.V[GTValues.LV])
                .duration(225)
                .buildAndRegister();
    }
}
