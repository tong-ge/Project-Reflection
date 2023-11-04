package bruce.projectreflection.mixin;

import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.builders.CircuitAssemblerRecipeBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nonnull;

@Mixin(value = CircuitAssemblerRecipeBuilder.class, remap = false)
public abstract class MixinCircuitRecipeBuilder extends RecipeBuilder<CircuitAssemblerRecipeBuilder> {
    @Shadow
    public abstract CircuitAssemblerRecipeBuilder solderMultiplier(int multiplier);

    @Override
    public boolean applyProperty(@Nonnull String key, Object value) {
        if (key.equals("solderMultiplier")) {
            this.solderMultiplier(((Number) value).intValue());
            return true;
        }
        return super.applyProperty(key, value);
    }
}
