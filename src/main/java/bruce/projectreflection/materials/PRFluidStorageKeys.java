package bruce.projectreflection.materials;

import gregtech.api.fluids.FluidState;
import gregtech.api.fluids.store.FluidStorageKey;
import gregtech.api.unification.material.info.MaterialIconType;
import gregtech.api.util.GTUtility;

public final class PRFluidStorageKeys {
    private PRFluidStorageKeys() {
    }

    public static final FluidStorageKey SLUDGE = new FluidStorageKey(GTUtility.gregtechId("sludge"), MaterialIconType.liquid, (m) -> {
        return "sludge." + m.getName();
    }, (m) -> {
        return "projectreflection.fluid.sludge";
    }, FluidState.LIQUID, -1);
    public static final FluidStorageKey SOLUTION = new FluidStorageKey(GTUtility.gregtechId("solution"), MaterialIconType.liquid, (m) -> {
        return "solution." + m.getName();
    }, (m) -> {
        return "projectreflection.fluid.solution";
    }, FluidState.LIQUID, -1);
}
