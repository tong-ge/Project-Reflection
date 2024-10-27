package bruce.projectreflection.mixin;

import bruce.projectreflection.PRConfig;
import bruce.projectreflection.PRConstants;
import bruce.projectreflection.PRLabs;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.security.SecureRandom;
import java.util.Random;

@Mixin(World.class)
public abstract class MixinWorld {
    /**
     * @author tong-ge
     * @reason Fix RANDAR
     */
    @Overwrite
    public Random setRandomSeed(int seedX, int seedY, int seedZ) {
        if (PRConfig.debug && PRConfig.verboseDebug) {
            PRLabs.logger.info("Setting structure random");
        }
        long j2 = (long) seedX * 341873128712L + (long) seedY * 132897987541L + this.getWorldInfo().getSeed() + (long) seedZ;
        PRConstants.structureRandom.setSeed(j2);
        return PRConstants.structureRandom;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void postInit(CallbackInfo ci) {
        if (PRConfig.debug) {
            PRLabs.logger.info("Setting world random");
        }
        rand = new SecureRandom();
    }

    @Shadow
    protected abstract WorldInfo getWorldInfo();

    @Shadow
    @Final
    @Mutable
    public Random rand;
}
