package bruce.projectreflection.mixin;

import bruce.projectreflection.PRConfig;
import gregtech.loaders.recipe.MetaTileEntityLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MetaTileEntityLoader.class, remap = false)
public class MixinMTELoader {
    @Inject(method = "registerMachineRecipe(Z[Lgregtech/api/metatileentity/MetaTileEntity;[Ljava/lang/Object;)V", at = @At("HEAD"), cancellable = true)
    private static void pre_registerMachineRecipe(CallbackInfo ci) {
        if (PRConfig.disruptRecipes)
            ci.cancel();
    }
}
