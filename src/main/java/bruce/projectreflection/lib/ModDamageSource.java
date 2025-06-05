package bruce.projectreflection.lib;

import net.minecraft.util.DamageSource;

public interface ModDamageSource {
    DamageSource mahouTsukai = new DamageSource("mahoutsukai").setDamageBypassesArmor().setDamageIsAbsolute();
}
