package bruce.projectreflection.recipes.handler;

import bruce.projectreflection.materials.PRFluidStorageKeys;
import gregtech.api.GTValues;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.properties.BlastProperty;
import gregtech.api.unification.material.properties.OreProperty;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.common.items.MetaItems;
import net.minecraft.item.ItemStack;

public class OreProcessingHandler {
    public static void init() {
        OrePrefix.crushed.addProcessingHandler(PropertyKey.ORE, OreProcessingHandler::processCrushed);
        OrePrefix.crushedPurified.addProcessingHandler(PropertyKey.ORE, OreProcessingHandler::processCrushedPurified);
    }

    private static void processCrushed(OrePrefix orePrefix, Material material, OreProperty property) {
        if (!material.hasFluid() || material.getFluid(PRFluidStorageKeys.SLUDGE) == null) {
            return;
        }
        Material byproductMaterial = property.getOreByProduct(0, material);

        RecipeMaps.MIXER_RECIPES.recipeBuilder()
                .inputs(OreDictUnifier.get(orePrefix, material))
                .fluidInputs(Materials.Water.getFluid(1000))
                .output(OrePrefix.dust, Materials.Stone)
                .chancedOutput(OrePrefix.dust, byproductMaterial, 5000, 5)
                .fluidOutputs(material.getFluid(PRFluidStorageKeys.SLUDGE, 216))
                .EUt(GTValues.VA[GTValues.LV])
                .duration(600)
                .buildAndRegister();
        RecipeMaps.MIXER_RECIPES.recipeBuilder()
                .inputs(OreDictUnifier.get(orePrefix, material))
                .fluidInputs(Materials.DistilledWater.getFluid(1000))
                .output(OrePrefix.dust, Materials.Stone)
                .chancedOutput(OrePrefix.dust, byproductMaterial, 5000, 5)
                .fluidOutputs(material.getFluid(PRFluidStorageKeys.SLUDGE, 216))
                .EUt(GTValues.VA[GTValues.LV])
                .duration(400)
                .buildAndRegister();
    }

    private static void processCrushedPurified(OrePrefix orePrefix, Material material, OreProperty property) {
        if (!material.hasFluid() || material.getFluid(PRFluidStorageKeys.SLUDGE) == null) {
            return;
        }
        RecipeMaps.MIXER_RECIPES.recipeBuilder()
                .inputs(OreDictUnifier.get(orePrefix, material))
                .fluidInputs(Materials.Water.getFluid(1000))
                .fluidOutputs(material.getFluid(PRFluidStorageKeys.SLUDGE, 216))
                .EUt(GTValues.VA[GTValues.LV])
                .duration(400)
                .buildAndRegister();
        RecipeMaps.MIXER_RECIPES.recipeBuilder()
                .inputs(OreDictUnifier.get(orePrefix, material))
                .fluidInputs(Materials.DistilledWater.getFluid(1000))
                .fluidOutputs(material.getFluid(PRFluidStorageKeys.SLUDGE, 216))
                .EUt(GTValues.VA[GTValues.LV])
                .duration(300)
                .buildAndRegister();
        Material byproductMaterial = property.getOreByProduct(1, material);
        RecipeMaps.CENTRIFUGE_RECIPES.recipeBuilder()
                .fluidInputs(material.getFluid(PRFluidStorageKeys.SLUDGE, 144),
                        Materials.Water.getFluid(1000))
                .notConsumable(MetaItems.FLUID_FILTER.getStackForm())
                .fluidOutputs(material.getFluid(PRFluidStorageKeys.SOLUTION, 216))
                .chancedOutput(OrePrefix.dust, byproductMaterial, 5000, 5)
                .EUt(GTValues.VA[GTValues.LV])
                .duration(200)
                .buildAndRegister();
        RecipeMaps.CENTRIFUGE_RECIPES.recipeBuilder()
                .fluidInputs(material.getFluid(PRFluidStorageKeys.SLUDGE, 144),
                        Materials.DistilledWater.getFluid(1000))
                .notConsumable(MetaItems.FLUID_FILTER.getStackForm())
                .fluidOutputs(material.getFluid(PRFluidStorageKeys.SOLUTION, 216))
                .chancedOutput(OrePrefix.dust, byproductMaterial, 5000, 5)
                .EUt(GTValues.VA[GTValues.LV])
                .duration(150)
                .buildAndRegister();
        PRRecipeMaps.DEHYDRATOR_RECIPES.recipeBuilder()
                .fluidInputs(material.getFluid(PRFluidStorageKeys.SOLUTION, 144))
                .output(OrePrefix.dust, material)
                .chancedOutput(OrePrefix.dust, material, 5000, 5)
                .chancedOutput(OrePrefix.dust, property.getOreByProduct(2, material), 2100, 1280)
                .EUt(GTValues.VA[GTValues.LV])
                .duration(200)
                .buildAndRegister();

    }
}
