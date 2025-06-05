package bruce.projectreflection.recipes.handler;

import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.PrimitiveRecipeBuilder;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;

public class PRRecipeMaps {
    public static final RecipeMap<SimpleRecipeBuilder> DIGESTER_RECIPES = new RecipeMap<>("biogas_digester",
            2, 1, 1, 1, new SimpleRecipeBuilder(), false);
    public static final RecipeMap<PrimitiveRecipeBuilder> SOLID_BOILER_FUELS = new RecipeMap<>("solid_boiler_fuel", 1, 1, 0, 0, new PrimitiveRecipeBuilder(), false).allowEmptyOutput();
    public static final RecipeMap<PrimitiveRecipeBuilder> FLUID_BOILER_FUELS = new RecipeMap<>("fluid_boiler_fuel", 0, 0, 1, 0, new PrimitiveRecipeBuilder(), false).allowEmptyOutput();
}
