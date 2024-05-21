package bruce.projectreflection.mixin;

import bruce.projectreflection.misc.BrightnessHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class MixinWorld {
    @Inject(remap = false, method = "getSunBrightnessBody", at = @At("RETURN"), cancellable = true)
    private void patch_getSunBrightnessBody(float partialTicks, CallbackInfoReturnable<Float> cir) {
        float brightness = cir.getReturnValueF();
        float wiggle = (float) BrightnessHelper.wiggle(5, 0.1);
        float rawBrightness = (brightness - 0.2f) * 1.25f;
        cir.setReturnValue(rawBrightness * (1.0f - wiggle) + wiggle);
    }
}
