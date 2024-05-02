package bruce.projectreflection.recipes.handler;

import bruce.projectreflection.ProjectReflection;
import bruce.projectreflection.recipes.builder.AuraCollectorRecipeBuilder;
import bruce.projectreflection.recipes.routines.FuelCellRoutine;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.FuelRecipeBuilder;
import gregtech.api.recipes.builders.PrimitiveRecipeBuilder;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;

public class PRRecipeMaps {

    public static RecipeMap<SimpleRecipeBuilder> PETROTHEUM_MACERATOR = new RecipeMap<>(
            "petrotheum_macerator",
            1, 3, 1, 0,
            new SimpleRecipeBuilder(), false);
    public static RecipeMap<PrimitiveRecipeBuilder> CERAMIC_OVEN = new RecipeMap<>(
            "ceramic_oven",
            3, 2, 0, 0, new PrimitiveRecipeBuilder(), false);
    public static RecipeMap<AuraCollectorRecipeBuilder> AURA_COLLECTOR = new RecipeMap<>("aura_collector",
            1, 0, 0, 1, new AuraCollectorRecipeBuilder(), false);
    public static RecipeMap<FuelRecipeBuilder> MAGICAL_GENERATOR = new RecipeMap<>("magical_generator",
            0, 0, 1, 1, new FuelRecipeBuilder(), false).allowEmptyOutput();
    public static RecipeMap<SimpleRecipeBuilder> SPACE_TIME_SUPPRESSOR = new RecipeMap<>("space_time_suppressor",
            12, 12, 9, 9, new SimpleRecipeBuilder(), false)
            .setSound(ProjectReflection.UPDATE_SUPPRESSOR);

    public static RecipeMap<FuelRecipeBuilder> FUEL_CELL = new RecipeMap<>("fuel_cell", 6,
            2, 6, 1, new FuelRecipeBuilder(), false);
}
