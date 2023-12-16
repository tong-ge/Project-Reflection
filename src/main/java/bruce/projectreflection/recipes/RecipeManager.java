package bruce.projectreflection.recipes;

import bruce.projectreflection.materials.FirstTierMaterials;
import bruce.projectreflection.materials.SecondTierMaterials;
import bruce.projectreflection.materials.ThirdTierMaterials;
import bruce.projectreflection.recipes.handler.OreProcessingHandler;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import scala.Int;

public class RecipeManager {
    public static void preInit() {
        OreProcessingHandler.init();
    }
    public static void init() {
        RecipeRemover.doRemove();
        MachineRecipes.register();
        RecipeMaps.MASS_FABRICATOR_RECIPES.recipeBuilder()
                .circuitMeta(1)
                .EUt(256)
                .duration(3200)
                .fluidOutputs(Materials.UUMatter.getFluid(1))
                .buildAndRegister();
        RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder()
                .inputs(
                        OreDictUnifier.get(OrePrefix.plate, Materials.WroughtIron),
                        OreDictUnifier.get(OrePrefix.plate, Materials.Bronze),
                        OreDictUnifier.get(OrePrefix.plate, Materials.Tin)
                )
                .circuitMeta(3)
                .outputs(OreDictUnifier.get(OrePrefix.ingot, FirstTierMaterials.ALLOY_ADVANCED))
                .EUt(30)
                .duration(600)
                .buildAndRegister();
        RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder()
                .inputs(
                        OreDictUnifier.get(OrePrefix.plate, Materials.Iridium, 4),
                        OreDictUnifier.get(OrePrefix.plate, FirstTierMaterials.ALLOY_ADVANCED, 4),
                        OreDictUnifier.get(OrePrefix.gemExquisite, Materials.Diamond)
                )
                .circuitMeta(2)
                .outputs(OreDictUnifier.get(OrePrefix.ingot, SecondTierMaterials.ALLOY_IRIDIUM))
                .EUt(7680)
                .duration(1200)
                .buildAndRegister();
        RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder()
                .inputs(
                        OreDictUnifier.get(OrePrefix.plate, SecondTierMaterials.ALLOY_IRIDIUM, 1),
                        OreDictUnifier.get(OrePrefix.plate, Materials.NiobiumTitanium, 2)
                )
                .circuitMeta(2)
                .outputs(OreDictUnifier.get(OrePrefix.ingot, ThirdTierMaterials.SUPER_IRIDIUM_ALLOY, 1))
                .EUt(1966080)
                .duration(2400)
                .buildAndRegister();

        RecipeMaps.BENDER_RECIPES.recipeBuilder()
                .inputs(OreDictUnifier.get(OrePrefix.ingot, FirstTierMaterials.ALLOY_ADVANCED))
                .outputs(OreDictUnifier.get(OrePrefix.plate, FirstTierMaterials.ALLOY_ADVANCED))
                .circuitMeta(1)
                .EUt(2)
                .duration(300)
                .buildAndRegister();

        RecipeMaps.IMPLOSION_RECIPES.recipeBuilder()
                .inputs(OreDictUnifier.get(OrePrefix.ingot, SecondTierMaterials.ALLOY_IRIDIUM))
                .outputs(OreDictUnifier.get(OrePrefix.plate, SecondTierMaterials.ALLOY_IRIDIUM))
                .explosivesAmount(4)
                .EUt(30)
                .duration(20)
                .buildAndRegister();
        RecipeMaps.IMPLOSION_RECIPES.recipeBuilder()
                .inputs(OreDictUnifier.get(OrePrefix.ingot, ThirdTierMaterials.SUPER_IRIDIUM_ALLOY))
                .outputs(OreDictUnifier.get(OrePrefix.plate, ThirdTierMaterials.SUPER_IRIDIUM_ALLOY))
                .circuitMeta(1)
                .explosivesAmount(4)
                .EUt(30)
                .duration(20)
                .buildAndRegister();
        RecipeMaps.IMPLOSION_RECIPES.recipeBuilder()
                .inputs(OreDictUnifier.get(OrePrefix.ingot, ThirdTierMaterials.SUPER_IRIDIUM_ALLOY, 9))
                .outputs(OreDictUnifier.get(OrePrefix.plateDense, ThirdTierMaterials.SUPER_IRIDIUM_ALLOY))
                .circuitMeta(2)
                .explosivesAmount(36)
                .EUt(120)
                .duration(90)
                .buildAndRegister();
        RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder()
                .inputs(new ItemStack(Items.GLASS_BOTTLE, 3),
                        new ItemStack(Items.NETHER_WART),
                        new ItemStack(Items.RABBIT_FOOT)
                )
                .outputs(new ItemStack(Items.EXPERIENCE_BOTTLE, 3))
                .EUt(512)
                .duration(Integer.MAX_VALUE)
                .buildAndRegister();
    }
}
