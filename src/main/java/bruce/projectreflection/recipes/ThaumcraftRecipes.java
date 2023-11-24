package bruce.projectreflection.recipes;

import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.items.ItemsTC;

public final class ThaumcraftRecipes {
    public static void register() {
        CrucibleRecipe thaumiumRecipe = ThaumcraftApi.getCrucibleRecipe(new ItemStack(ItemsTC.ingots));
        if (thaumiumRecipe != null) {
            thaumiumRecipe.setCatalyst(Ingredient.fromStacks(OreDictUnifier.get(OrePrefix.ingot, Materials.Steel)));
        }
    }
}
