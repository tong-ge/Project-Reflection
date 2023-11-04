package bruce.projectreflection.recipes.handler;

import bruce.projectreflection.materials.ThermalMaterials;
import bruce.projectreflection.recipes.PRRecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.properties.OreProperty;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.MaterialStack;
import gregtech.api.util.GTUtility;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class OreProcessingHandler {
    public static void init() {
        OrePrefix.ore.addProcessingHandler(PropertyKey.ORE, OreProcessingHandler::processOre);
        OrePrefix.oreNetherrack.addProcessingHandler(PropertyKey.ORE, OreProcessingHandler::processOre);
        OrePrefix.oreEndstone.addProcessingHandler(PropertyKey.ORE, OreProcessingHandler::processOre);
        OrePrefix.crushedCentrifuged.addProcessingHandler(PropertyKey.ORE, OreProcessingHandler::processCrushedCentrifuged);
    }

    public static void processOre(OrePrefix orePrefix, Material material, OreProperty property) {
        Material byproductMaterial = property.getOreByProduct(0, material);
        ItemStack byproductStack = OreDictUnifier.get(OrePrefix.gem, byproductMaterial);
        if (byproductStack.isEmpty()) byproductStack = OreDictUnifier.get(OrePrefix.dust, byproductMaterial);
        double amountOfCrushedOre = property.getOreMultiplier();
        int oreTypeMultiplier = orePrefix == OrePrefix.oreNetherrack || orePrefix == OrePrefix.oreEndstone ? 2 : 1;
        ItemStack crushedStack = OreDictUnifier.get(OrePrefix.crushed, material);
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
                    .fluidInputs(ThermalMaterials.PETROTHEUM.getFluid(144))
                    .outputs(itemStackList.toArray(new ItemStack[0]))
                    .duration(50)
                    .EUt(30)
                    .buildAndRegister();
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
}