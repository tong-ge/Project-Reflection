package bruce.projectreflection.recipes.handler;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.ProjectReflection;
import bruce.projectreflection.materials.FirstTierMaterials;
import bruce.projectreflection.materials.PROrePrefixes;
import gregtech.api.recipes.ModHandler;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.chance.output.ChancedOutputLogic;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.properties.OreProperty;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.MaterialStack;
import gregtech.api.unification.stack.UnificationEntry;
import gregtech.api.util.GTUtility;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.items.ItemsTC;

import java.util.ArrayList;
import java.util.List;

public class OreProcessingHandler {
    public static void init() {
        ProjectReflection.logger.info("Registering ore processors");
        OrePrefix.ore.addProcessingHandler(PropertyKey.ORE, OreProcessingHandler::processOre);
        OrePrefix.oreNetherrack.addProcessingHandler(PropertyKey.ORE, OreProcessingHandler::processOre);
        OrePrefix.oreEndstone.addProcessingHandler(PropertyKey.ORE, OreProcessingHandler::processOre);
        PROrePrefixes.oreDark.addProcessingHandler(PropertyKey.ORE, OreProcessingHandler::processOre);
        PROrePrefixes.oreAbyssal.addProcessingHandler(PropertyKey.ORE, OreProcessingHandler::processOre);
        PROrePrefixes.oreDread.addProcessingHandler(PropertyKey.ORE, OreProcessingHandler::processOre);
        PROrePrefixes.orePurifiedDread.addProcessingHandler(PropertyKey.ORE, OreProcessingHandler::processOre);
        PROrePrefixes.oreOmothol.addProcessingHandler(PropertyKey.ORE, OreProcessingHandler::processOre);
        OrePrefix.crushedCentrifuged.addProcessingHandler(PropertyKey.ORE, OreProcessingHandler::processCrushedCentrifuged);
        PROrePrefixes.cluster.addProcessingHandler(PropertyKey.ORE, OreProcessingHandler::processCluster);
    }

    public static void processOre(OrePrefix orePrefix, Material material, OreProperty property) {
        Material byproductMaterial = property.getOreByProduct(0, material);
        ItemStack byproductStack = OreDictUnifier.get(OrePrefix.gem, byproductMaterial);
        if (byproductStack.isEmpty()) byproductStack = OreDictUnifier.get(OrePrefix.dust, byproductMaterial);
        double amountOfCrushedOre = property.getOreMultiplier();
        int oreTypeMultiplier = orePrefix == OrePrefix.oreNetherrack || orePrefix == OrePrefix.oreEndstone ? 2 : 1;
        ItemStack crushedStack = OreDictUnifier.get(OrePrefix.crushed, material);
        Material smeltingMaterial = property.getDirectSmeltResult() == null ? material :
                property.getDirectSmeltResult();
        if (smeltingMaterial.hasProperty(PropertyKey.GEM)) {
            for (ItemStack inputStack : OreDictUnifier.getAll(new UnificationEntry(orePrefix, material))) {
                if (ModHandler.removeFurnaceSmelting(inputStack)) {
                    PRRecipeMaps.CERAMIC_OVEN.recipeBuilder()
                            .inputs(inputStack, OreDictUnifier.get(OrePrefix.gem, Materials.Coke))
                            .outputs(OreDictUnifier.get(OrePrefix.gem, smeltingMaterial, property.getOreMultiplier() * oreTypeMultiplier))
                            .duration(4800)
                            .buildAndRegister();
                    PRRecipeMaps.CERAMIC_OVEN.recipeBuilder()
                            .inputs(inputStack, OreDictUnifier.get(OrePrefix.gem, Materials.Charcoal, 2))
                            .outputs(OreDictUnifier.get(OrePrefix.gem, smeltingMaterial, property.getOreMultiplier() * oreTypeMultiplier))
                            .duration(4800)
                            .buildAndRegister();
                }
            }
        }
        List<ItemStack> itemStackList = new ArrayList<>();
        if (!crushedStack.isEmpty()) {
            itemStackList.add(GTUtility.copy((int) Math.floor(amountOfCrushedOre * 3.0 * oreTypeMultiplier), crushedStack));
        }
        if (!byproductStack.isEmpty()) {
            itemStackList.add(byproductStack);
        }
        for (MaterialStack secondaryMaterial : orePrefix.secondaryMaterials) {
            if (secondaryMaterial.material.hasProperty(PropertyKey.DUST)) {
                ItemStack dustStack = OreDictUnifier.getGem(secondaryMaterial);
                itemStackList.add(dustStack);
            }
        }
        if (!itemStackList.isEmpty()) {
            PRRecipeMaps.PETROTHEUM_MACERATOR.recipeBuilder()
                    .input(orePrefix, material)
                    .fluidInputs(FirstTierMaterials.PETROTHEUM.getFluid(144))
                    .outputs(itemStackList.toArray(new ItemStack[0]))
                    .duration(50)
                    .EUt(30)
                    .buildAndRegister();
        }

        for (ItemStack inputStack : OreDictUnifier.getAll(new UnificationEntry(orePrefix, material))) {
            ItemStack clusterStack = OreDictUnifier.get(PROrePrefixes.cluster, material, oreTypeMultiplier);
            if (clusterStack == null || clusterStack.isEmpty())
                continue;
            ProjectReflection.logger.info("Registering thaumcraft recipe for {}", material);
            ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(PRConstants.modid, new UnificationEntry(orePrefix, material).toString()),
                    new CrucibleRecipe("METALPURIFICATION", clusterStack,
                            inputStack, new AspectList().add(Aspect.METAL, 5).add(Aspect.ORDER, 5)));
        }
    }

    public static void processCrushedCentrifuged(OrePrefix centrifugedPrefix, Material material, OreProperty property) {
        ItemStack dustStack = OreDictUnifier.get(OrePrefix.dust, material);
        ItemStack byproductStack = OreDictUnifier.get(OrePrefix.dust, property.getOreByProduct(2, material), 1);
        PRRecipeMaps.PETROTHEUM_MACERATOR.recipeBuilder()
                .input(centrifugedPrefix, material, 2)
                .outputs(GTUtility.copy(3, dustStack), byproductStack)
                .duration(10)
                .EUt(30)
                .buildAndRegister();

    }

    public static void processCluster(OrePrefix orePrefix, Material material, OreProperty property) {
        Material byproductMaterial = property.getOreByProduct(0, material);
        ItemStack byproductStack = OreDictUnifier.get(OrePrefix.gem, byproductMaterial);
        if (byproductStack.isEmpty()) byproductStack = OreDictUnifier.get(OrePrefix.dust, byproductMaterial);
        ItemStack crystalStack = OreDictUnifier.get(PROrePrefixes.crystal, material);
        ItemStack clusterStack = OreDictUnifier.get(orePrefix, material);
        if (clusterStack == null || clusterStack.isEmpty())
            return;
        ModHandler.removeFurnaceSmelting(clusterStack);
        RecipeMaps.CHEMICAL_RECIPES.recipeBuilder()
                .inputs(clusterStack)
                .fluidInputs(Materials.SulfuricAcid.getFluid(500), Materials.Oxygen.getFluid(1000))
                .outputs(crystalStack)
                .chancedOutput(byproductStack, (int) ((double) ChancedOutputLogic.getMaxChancedValue() * 0.3), (int) ((double) ChancedOutputLogic.getMaxChancedValue() * 0.01))
                .EUt(30)
                .duration(400)
                .buildAndRegister();
    }
}
