package bruce.projectreflection.recipes;

import bruce.projectreflection.materials.ThermalMaterials;
import bruce.projectreflection.recipes.handler.OreProcessingHandler;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;
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

public class PRRecipeMaps {
    /*public static RecipeMap<FuelRecipeBuilder> HYDRAULIC_GENERATOR=new RecipeMap<>("hydraulic_generator",
            0,0,1,0,new FuelRecipeBuilder(),false)
            .allowEmptyOutput();*/
    public static RecipeMap<SimpleRecipeBuilder> PETROTHEUM_MACERATOR = new RecipeMap<>(
            "petrotheum_macerator",
            1, 3, 1, 0,
            new SimpleRecipeBuilder(), false);
    public static RecipeMap<SimpleRecipeBuilder> AEROTHEUM_CENTRIFUGE = new RecipeMap<>(
            "aerotheum_centrifuge",
            1, 6, 1, 0,
            new SimpleRecipeBuilder(), false);
    public static void init() {
        OreProcessingHandler.init();
    }
}
