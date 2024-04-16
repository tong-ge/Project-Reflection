package bruce.projectreflection.api;

import bruce.projectreflection.PRConfig;
import mysticalmechanics.api.IMechCapability;

public interface IGTMechCapability extends IMechCapability {
    long getMaxEU();

    default long getMaxSpeed() {
        return Math.round(getMaxEU() * PRConfig.EU_TO_R);
    }

    default long getEffectiveEUt() {
        return (long) Math.floor(getPower(null) / PRConfig.EU_TO_R);
    }

    default void setEU(double EUt) {
        setPower(EUt * PRConfig.EU_TO_R, null);
    }
}
