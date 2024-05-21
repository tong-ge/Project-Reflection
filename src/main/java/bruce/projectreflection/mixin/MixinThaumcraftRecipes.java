package bruce.projectreflection.mixin;

import keqing.pollution.loaders.recipes.ThaumcraftRecipes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ThaumcraftRecipes.class, remap = false)
public class MixinThaumcraftRecipes {
    @Inject(method = "solar", at = @At("HEAD"), cancellable = true)
    private static void pre_solar(CallbackInfo ci) {
        ci.cancel();
    }
}
