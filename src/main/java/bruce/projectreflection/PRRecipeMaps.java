package bruce.projectreflection;

import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.FuelRecipeBuilder;

public class PRRecipeMaps {
    public static RecipeMap<FuelRecipeBuilder> HYDRAULIC_GENERATOR=new RecipeMap<>("hydraulic_generator",
            0,0,1,0,new FuelRecipeBuilder(),false)
            .allowEmptyOutput();
}
