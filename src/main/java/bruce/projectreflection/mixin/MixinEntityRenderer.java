package bruce.projectreflection.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.stats.StatisticsManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {
    @Shadow
    @Final
    private Minecraft mc;
    @Shadow
    private boolean debugView;

    @Inject(method = "updateFogColor", at = @At("HEAD"), cancellable = true)
    private void inject_updateFogColor(CallbackInfo ci) {
        if (this.mc.getRenderViewEntity() == null) {
            //ci.cancel();
            if (this.mc.player == null) {
                this.mc.player = this.mc.playerController.createPlayer(null, new StatisticsManager(), new RecipeBookClient());
                this.mc.playerController.flipPlayer(this.mc.player);
            }
            this.mc.setRenderViewEntity(new EntityItem(this.mc.world));
        }
    }

    @Inject(method = "getFOVModifier", at = @At("HEAD"), cancellable = true)
    private void inject_getFOVModifier(CallbackInfoReturnable<Float> cir) {

    }
}
