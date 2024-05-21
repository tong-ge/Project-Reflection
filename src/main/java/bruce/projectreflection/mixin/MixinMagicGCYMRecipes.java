package bruce.projectreflection.mixin;

import keqing.pollution.loaders.recipes.MagicGCYMRecipes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MagicGCYMRecipes.class, remap = false)
public class MixinMagicGCYMRecipes {
    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private static void pre_init(CallbackInfo ci) {
        ci.cancel();
    }
}
