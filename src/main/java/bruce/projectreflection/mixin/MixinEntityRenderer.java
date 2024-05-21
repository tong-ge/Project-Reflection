package bruce.projectreflection.mixin;

import bruce.projectreflection.misc.BrightnessHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {
    @Shadow
    private boolean lightmapUpdateNeeded;
    @Shadow
    @Final
    private Minecraft mc;
    @Shadow
    @Final
    private int[] lightmapColors;
    @Shadow
    @Final
    private DynamicTexture lightmapTexture;
    @Shadow
    private float torchFlickerX;

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void updateLightmap(float partialTicks) {
        if (this.lightmapUpdateNeeded) {
            this.mc.profiler.startSection("lightTex");
            World world = this.mc.world;

            if (world != null) {
                float f = world.getSunBrightness(1.0F);
                float wiggle = (float) BrightnessHelper.wiggle(5, 0.25);
                float f1 = f * (1.0f - wiggle) + wiggle;

                for (int i = 0; i < 256; ++i) {
                    float f2 = world.provider.getLightBrightnessTable()[i / 16] * f1;
                    float f3 = world.provider.getLightBrightnessTable()[i % 16] * (this.torchFlickerX * 0.1F + 1.5F);

                    if (world.getLastLightningBolt() > 0) {
                        f2 = world.provider.getLightBrightnessTable()[i / 16];
                    }

                    float f4 = f2 * (f * 0.65F + 0.35F);
                    float f5 = f2 * (f * 0.65F + 0.35F);
                    float f6 = f3 * ((f3 * 0.6F + 0.4F) * 0.6F + 0.4F);
                    float f7 = f3 * (f3 * f3 * 0.6F + 0.4F);
                    float f8 = f4 + f3;
                    float f9 = f5 + f6;
                    float f10 = f2 + f7;
                    float wiggle2 = (float) BrightnessHelper.wiggle(5, 0.03);

                    f8 = f8 * (1.0f - wiggle2) + wiggle2;
                    f9 = f9 * (1.0f - wiggle2) + wiggle2;
                    f10 = f10 * (1.0f - wiggle2) + wiggle2;
                    //boss color modifier

                    /*
                    if (world.provider.getDimensionType().getId() == 1)
                    {
                        f8 = 0.22F + f3 * 0.75F;
                        f9 = 0.28F + f6 * 0.75F;
                        f10 = 0.25F + f7 * 0.75F;
                    }


                     */
                    float[] colors = {f8, f9, f10};
                    world.provider.getLightmapColors(partialTicks, f, f2, f3, colors);
                    f8 = colors[0];
                    f9 = colors[1];
                    f10 = colors[2];

                    // Forge: fix MC-58177
                    f8 = MathHelper.clamp(f8, 0f, 1f);
                    f9 = MathHelper.clamp(f9, 0f, 1f);
                    f10 = MathHelper.clamp(f10, 0f, 1f);

                    if (this.mc.player.isPotionActive(MobEffects.NIGHT_VISION)) {
                        float f15 = this.getNightVisionBrightness(this.mc.player, partialTicks);
                        float f12 = 1.0F / f8;

                        if (f12 > 1.0F / f9) {
                            f12 = 1.0F / f9;
                        }

                        if (f12 > 1.0F / f10) {
                            f12 = 1.0F / f10;
                        }

                        f8 = f8 * (1.0F - f15) + f8 * f12 * f15;
                        f9 = f9 * (1.0F - f15) + f9 * f12 * f15;
                        f10 = f10 * (1.0F - f15) + f10 * f12 * f15;
                    }

                    if (f8 > 1.0F) {
                        f8 = 1.0F;
                    }

                    if (f9 > 1.0F) {
                        f9 = 1.0F;
                    }

                    if (f10 > 1.0F) {
                        f10 = 1.0F;
                    }

                    float f16 = this.mc.gameSettings.gammaSetting;
                    float f17 = 1.0F - f8;
                    float f13 = 1.0F - f9;
                    float f14 = 1.0F - f10;
                    f17 = 1.0F - f17 * f17 * f17 * f17;
                    f13 = 1.0F - f13 * f13 * f13 * f13;
                    f14 = 1.0F - f14 * f14 * f14 * f14;
                    f8 = f8 * (1.0F - f16) + f17 * f16;
                    f9 = f9 * (1.0F - f16) + f13 * f16;
                    f10 = f10 * (1.0F - f16) + f14 * f16;

                    f8 = f8 * (1.0f - wiggle2) + wiggle2;
                    f9 = f9 * (1.0f - wiggle2) + wiggle2;
                    f10 = f10 * (1.0f - wiggle2) + wiggle2;

                    if (f8 > 1.0F) {
                        f8 = 1.0F;
                    }

                    if (f9 > 1.0F) {
                        f9 = 1.0F;
                    }

                    if (f10 > 1.0F) {
                        f10 = 1.0F;
                    }

                    if (f8 < 0.0F) {
                        f8 = 0.0F;
                    }

                    if (f9 < 0.0F) {
                        f9 = 0.0F;
                    }

                    if (f10 < 0.0F) {
                        f10 = 0.0F;
                    }

                    int k = (int) (f8 * 255.0F);
                    int l = (int) (f9 * 255.0F);
                    int i1 = (int) (f10 * 255.0F);
                    this.lightmapColors[i] = -16777216 | k << 16 | l << 8 | i1;
                }

                this.lightmapTexture.updateDynamicTexture();
                this.lightmapUpdateNeeded = false;
                this.mc.profiler.endSection();
            }
        }
    }

    @Shadow

    protected abstract float getNightVisionBrightness(EntityLivingBase player, float partialTicks);
}
