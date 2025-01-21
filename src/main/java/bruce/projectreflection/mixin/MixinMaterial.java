package bruce.projectreflection.mixin;

import gregtech.api.unification.material.Material;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Material.class, remap = false)
public abstract class MixinMaterial {
    @Shadow
    public abstract Fluid getFluid();

    @Inject(method = "getFluid(I)Lnet/minecraftforge/fluids/FluidStack;", at = @At("HEAD"), cancellable = true)
    private void pre_getFluid(CallbackInfoReturnable<FluidStack> ci) {
        if (this.getFluid() == null) {
            ci.setReturnValue(null);
        }
    }
}
