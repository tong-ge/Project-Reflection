package bruce.projectreflection.recipes;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.ProjectReflection;
import com.google.common.collect.ImmutableList;
import gregtech.api.GregTechAPI;
import gregtech.api.recipes.ModHandler;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.info.MaterialFlags;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.material.registry.MaterialRegistry;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.items.MetaItems;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMufflerHatch;
import knightminer.ceramics.Ceramics;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class RecipeRemover {
    private static final List<ItemStack> removedRecipes = new ArrayList<>();
    private static final List<RecipeMap> recipeMapList = new ArrayList<>();

    private static void init() {
        recipeMapList.add(RecipeMaps.MIXER_RECIPES);
        recipeMapList.add(RecipeMaps.CHEMICAL_RECIPES);
        recipeMapList.add(RecipeMaps.LARGE_CHEMICAL_RECIPES);
        recipeMapList.add(RecipeMaps.CIRCUIT_ASSEMBLER_RECIPES);
        recipeMapList.add(RecipeMaps.ASSEMBLY_LINE_RECIPES);
        removedRecipes.add(OreDictUnifier.get(OrePrefix.dust, Materials.EnderPearl, 1));
        removedRecipes.add(OreDictUnifier.get(OrePrefix.dust, Materials.Blaze, 1));
        //同级升级的电路板
        removedRecipes.add(MetaItems.INTEGRATED_CIRCUIT_MV.getStackForm());
        removedRecipes.add(MetaItems.PROCESSOR_ASSEMBLY_HV.getStackForm());
        removedRecipes.add(MetaItems.NANO_COMPUTER_IV.getStackForm());
        removedRecipes.add(MetaItems.QUANTUM_COMPUTER_LUV.getStackForm());
        removedRecipes.add(MetaItems.CRYSTAL_COMPUTER_ZPM.getStackForm());
        removedRecipes.add(MetaItems.WETWARE_SUPER_COMPUTER_UV.getStackForm());
        //切割降级的电路板
        //MV->LV->ULV
        removedRecipes.add(MetaItems.INTEGRATED_CIRCUIT_LV.getStackForm());
        removedRecipes.add(MetaItems.NAND_CHIP_ULV.getStackForm());
        //HV->MV->LV
        removedRecipes.add(MetaItems.PROCESSOR_MV.getStackForm());
        removedRecipes.add(MetaItems.MICROPROCESSOR_LV.getStackForm());
        //IV->EV->HV
        removedRecipes.add(MetaItems.NANO_PROCESSOR_ASSEMBLY_EV.getStackForm());
        removedRecipes.add(MetaItems.NANO_PROCESSOR_HV.getStackForm());
        //LuV->IV->EV
        removedRecipes.add(MetaItems.QUANTUM_ASSEMBLY_IV.getStackForm());
        removedRecipes.add(MetaItems.QUANTUM_PROCESSOR_EV.getStackForm());
        //ZPM->LuV->IV
        removedRecipes.add(MetaItems.CRYSTAL_ASSEMBLY_LUV.getStackForm());
        removedRecipes.add(MetaItems.CRYSTAL_PROCESSOR_IV.getStackForm());
        //UV->ZPM->LuV
        removedRecipes.add(MetaItems.WETWARE_PROCESSOR_ASSEMBLY_ZPM.getStackForm());
        removedRecipes.add(MetaItems.WETWARE_PROCESSOR_LUV.getStackForm());
        if (MetaTileEntities.MUFFLER_HATCH != null)
            for (MetaTileEntityMufflerHatch mte : MetaTileEntities.MUFFLER_HATCH) {
                if (mte != null) {
                    ItemStack stack = mte.getStackForm();
                    if (stack != null)
                        ModHandler.removeRecipeByOutput(stack);
                }
            }
        MaterialRegistry registry = GregTechAPI.materialManager.getRegistry(PRConstants.modid);

        for(int i=0;i<OreDictionary.WILDCARD_VALUE;i++)
        {
            Material material=registry.getObjectById(i);
            if(material != null && material.hasFlag(MaterialFlags.GENERATE_PLATE) && material.hasProperty(PropertyKey.WIRE))
            {
                ModHandler.removeRecipeByOutput(OreDictUnifier.get(OrePrefix.wireGtSingle, material));
            }
        }
    }

    public static void doRemove() {
        init();
        for (RecipeMap recipeMap : recipeMapList) {
            ImmutableList<Recipe> recipes = ImmutableList.copyOf(recipeMap.getRecipeList());
            for (Recipe recipe : recipes) {
                for (ItemStack stack : recipe.getOutputs()) {
                    for (ItemStack wanted : removedRecipes) {
                        if (OreDictionary.itemMatches(wanted, stack, false)) {
                            ProjectReflection.logger.info("Removing {}", recipe);
                            recipeMap.removeRecipe(recipe);
                        }
                    }
                }
            }
        }
    }
}
