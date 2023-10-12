package bruce.projectreflection;

import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.FuelRecipeBuilder;

public class PRRecipeMaps {
    public static final RecipeMap<FuelRecipeBuilder> MODULAR_FISSION =new RecipeMap<>("modular_fission",
            1,1,1,2,new FuelRecipeBuilder(),false);
}