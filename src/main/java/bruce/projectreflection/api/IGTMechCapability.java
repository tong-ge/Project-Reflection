package bruce.projectreflection.api;

import bruce.projectreflection.PRConstants;
import mysticalmechanics.api.IMechCapability;

public interface IGTMechCapability extends IMechCapability {
    long getMaxEU();

    default long getMaxSpeed() {
        return getMaxEU() * PRConstants.EU_TO_R;
    }

    default long getEffectiveEUt() {
        return (long) Math.floor(getPower(null) / PRConstants.EU_TO_R);
    }

    default void setEU(double EUt) {
        setPower(EUt * PRConstants.EU_TO_R, null);
    }
}
