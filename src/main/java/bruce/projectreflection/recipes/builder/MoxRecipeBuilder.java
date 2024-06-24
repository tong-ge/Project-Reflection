package bruce.projectreflection.recipes.builder;

import bruce.projectreflection.recipes.properties.MoxProperty;
import gregtech.api.recipes.RecipeBuilder;

public class MoxRecipeBuilder extends RecipeBuilder<MoxRecipeBuilder> {

    public MoxRecipeBuilder uranium(float baseHeat) {
        applyProperty(MoxProperty.INSTANCE, new MoxProperty.UraniumModel(baseHeat));
        return this;
    }

    public MoxRecipeBuilder mox(float baseHeat) {
        applyProperty(MoxProperty.INSTANCE, new MoxProperty.StandardMox(baseHeat));
        return this;
    }
}
