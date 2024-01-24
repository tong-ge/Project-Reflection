package bruce.projectreflection.recipes.builder;

import bruce.projectreflection.recipes.properties.AuraProperty;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;

public class AuraCollectorRecipeBuilder extends RecipeBuilder<AuraCollectorRecipeBuilder> {
    public AuraCollectorRecipeBuilder() {
    }

    public AuraCollectorRecipeBuilder(Recipe recipe, RecipeMap<AuraCollectorRecipeBuilder> recipeMap) {
        super(recipe, recipeMap);
    }

    public AuraCollectorRecipeBuilder(RecipeBuilder<AuraCollectorRecipeBuilder> recipeBuilder) {
        super(recipeBuilder);
    }

    @Override
    public AuraCollectorRecipeBuilder copy() {
        return new AuraCollectorRecipeBuilder(this);
    }

    public AuraCollectorRecipeBuilder aura(float amount, boolean extended) {
        applyProperty(AuraProperty.INSTANCE, new AuraProperty.AuraPropertyStruct(amount, extended));
        return this;
    }
}
