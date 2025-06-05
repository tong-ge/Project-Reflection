package bruce.projectreflection.mixin.early;

import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MathHelper.class)
public class MixinMathUtils {
    @Inject(method = "sqrt(F)F", at = @At("HEAD"), cancellable = true)
    private static void sqrt(float value, CallbackInfoReturnable<Float> cir) {
        //return (float)Math.sqrt((double)value);
        int x_bits = Float.floatToRawIntBits(value);
        float result = Float.intBitsToFloat((x_bits >> 1) + 0x1fbb4f2e);
        result = (result + value / result) * 0.5f;
        cir.setReturnValue(result);
    }
}
