package bruce.projectreflection.mixin;

import keqing.pollution.loaders.recipes.MagicChemicalRecipes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MagicChemicalRecipes.class, remap = false)
public class MixinMagicChemicalRecipes {
    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private static void pre_init(CallbackInfo ci) {
        ci.cancel();
    }
}
