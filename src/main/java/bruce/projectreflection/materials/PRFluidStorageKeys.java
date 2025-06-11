package bruce.projectreflection.materials;

import gregtech.api.fluids.FluidState;
import gregtech.api.fluids.store.FluidStorageKey;
import gregtech.api.unification.material.info.MaterialIconType;
import gregtech.api.util.GTUtility;

public final class PRFluidStorageKeys {
    private PRFluidStorageKeys() {
    }

    public static final FluidStorageKey SLUDGE = new FluidStorageKey(GTUtility.gregtechId("sludge"), MaterialIconType.liquid,
            (m) -> "sludge." + m.getName(), (m) -> "projectreflection.fluid.sludge", FluidState.LIQUID, -1);
    public static final FluidStorageKey SOLUTION = new FluidStorageKey(GTUtility.gregtechId("solution"), MaterialIconType.liquid,
            (m) -> "solution." + m.getName(), (m) -> "projectreflection.fluid.solution", FluidState.LIQUID, -1);
}
