package bruce.projectreflection.recipes.handler;

import bruce.projectreflection.materials.PRFluidStorageKeys;
import gregtech.api.GTValues;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.properties.BlastProperty;
import gregtech.api.unification.material.properties.OreProperty;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.material.properties.WireProperties;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.common.items.MetaItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Objects;
import java.util.Optional;

public class OreProcessingHandler {
    public static void init() {
        OrePrefix.crushed.addProcessingHandler(PropertyKey.ORE, OreProcessingHandler::processCrushed);
        OrePrefix.crushedPurified.addProcessingHandler(PropertyKey.ORE, OreProcessingHandler::processCrushedPurified);
        OrePrefix.ingot.addProcessingHandler(PropertyKey.WIRE, OreProcessingHandler::processWire);
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
//        PRRecipeMaps.DEHYDRATOR_RECIPES.recipeBuilder()
//                .fluidInputs(material.getFluid(PRFluidStorageKeys.SOLUTION, 144))
//                .output(OrePrefix.dust, material)
//                .chancedOutput(OrePrefix.dust, material, 5000, 5)
//                .chancedOutput(OrePrefix.dust, property.getOreByProduct(2, material), 2100, 1280)
//                .EUt(GTValues.VA[GTValues.LV])
//                .duration(200)
//                .buildAndRegister();
    }

    private static void processWire(OrePrefix orePrefix, Material material, WireProperties wireProperties) {
//        if (wireProperties.isSuperconductor() || wireProperties.getLossPerBlock() == 0) {
//            RecipeMaps.MIXER_RECIPES.getRecipeList().stream()
//                    .filter(recipe -> recipe.getAllItemOutputs().stream()
//                            .anyMatch(itemStack -> Optional.ofNullable(OreDictUnifier.getMaterial(itemStack))
//                                    .map(materialStack -> materialStack.material == material)
//                                    .orElse(false))
//                    )
//                    .forEach(recipe -> {
//                        int totalEnergy = recipe.getDuration() * recipe.getEUt();
//                        int EUt = recipe.getEUt();
//                        int stackSize = recipe.getAllItemOutputs().stream().filter(itemStack -> Optional.ofNullable(OreDictUnifier.getMaterial(itemStack))
//                                        .map(materialStack -> materialStack.material == material)
//                                        .orElse(false))
//                                .map(ItemStack::getCount).reduce(0, Integer::sum);
//                        int blastTemp = 900;
//                        BlastProperty blastProperty = material.getProperty(PropertyKey.BLAST);
//                        if (blastProperty != null) {
//                            blastTemp = blastProperty.getBlastTemperature();
//                            int blastDuration = blastProperty.getDurationOverride();
//                            if (blastDuration <= 0) {
//                                blastDuration = Math.max(1, (int) (material.getMass() * (long) blastTemp / 50L));
//                            }
//
//                            int blastEUt = blastProperty.getEUtOverride();
//                            if (blastEUt <= 0) {
//                                blastEUt = GTValues.VA[2];
//                            }
//                            totalEnergy += Math.max(0, blastEUt * blastDuration * stackSize);
//                            EUt = Math.max(EUt, blastEUt);
//                        }
//                        ItemStack output = OreDictUnifier.get(OrePrefix.ingotHot, material, stackSize);
//                        if (output.isEmpty())
//                            output = OreDictUnifier.get(OrePrefix.ingot, material, stackSize);
//                        PRRecipeMaps.SUPERCONDUCTOR_SMELTER.recipeBuilder()
//                                .inputs(recipe.getInputs().toArray(new GTRecipeInput[0]))
//                                .fluidInputs(recipe.getFluidInputs().stream().map(GTRecipeInput::getInputFluidStack).toArray(FluidStack[]::new))
//                                .outputs(output)
//                                .blastFurnaceTemp(blastTemp)
//                                .EUt(EUt)
//                                .duration(totalEnergy / EUt)
//                                .buildAndRegister();
//                        RecipeMaps.MIXER_RECIPES.removeRecipe(recipe);
//                    });
//        }
    }
}
