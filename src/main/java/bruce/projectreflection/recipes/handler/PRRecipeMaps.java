package bruce.projectreflection.recipes.handler;

import bruce.projectreflection.recipes.builder.AuraCollectorRecipeBuilder;
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
    public static RecipeMap<SimpleRecipeBuilder> INDUSTRIAL_MOB_FARM = new RecipeMap<>("industrial_mob_farm", 1, 12, 0, 12, new SimpleRecipeBuilder(), false);
}
