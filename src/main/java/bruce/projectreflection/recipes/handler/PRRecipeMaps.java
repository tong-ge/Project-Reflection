package bruce.projectreflection.recipes.handler;

import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;

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
}
