package bruce.projectreflection.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumicperiphery.ThaumicPeriphery;
import thaumicperiphery.compat.MysticalMechanicsCompat;

@Mixin(value = MysticalMechanicsCompat.class, remap = false)
public abstract class MixinMysticalMechanicsCompat {
    @Inject(method = "initRecipes", at = @At("HEAD"))
    private static void preInitRecipes(CallbackInfo ci) {
        ThaumicPeriphery.embersLoaded = false;
    }
}
