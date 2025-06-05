package bruce.projectreflection.mixin;

import gregtech.api.capability.IElectricItem;
import gregtech.api.capability.impl.ElectricItem;
import gregtech.api.items.metaitem.ElectricStats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ElectricStats.class, remap = false)
public class MixinElectricStats {
    @Redirect(method = "getSubItems", at = @At(value = "INVOKE", target = "Lgregtech/api/capability/IElectricItem;charge(JIZZ)J", remap = false))
    private long redirect_charge(IElectricItem item, long charge, int tier, boolean b, boolean b1) {
        ((ElectricItem) item).setCharge(charge);
        return charge;
    }
}
