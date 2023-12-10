package bruce.projectreflection.recipes;

import bruce.projectreflection.ProjectReflection;
import com.google.common.collect.ImmutableList;
import gregtech.api.recipes.ModHandler;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
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
        removedRecipes.add(OreDictUnifier.get(OrePrefix.dust, Materials.EnderPearl, 1));
        removedRecipes.add(OreDictUnifier.get(OrePrefix.dust, Materials.Blaze, 1));
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
