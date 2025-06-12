package bruce.projectreflection.capability;

import bruce.projectreflection.capability.energy.IHeatReceiver;
import gregtech.api.capability.SimpleCapabilityManager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class PRCapabilities {
    @CapabilityInject(IHeatReceiver.class)
    public static Capability<IHeatReceiver> CAPABILITY_HEAT_RECEIVER = null;

    public static void init() {
        SimpleCapabilityManager.registerCapabilityWithNoDefault(IHeatReceiver.class);
    }
}
