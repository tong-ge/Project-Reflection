package bruce.projectreflection.network;

import bruce.projectreflection.PRConstants;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PRNetwork {
    public static final SimpleNetworkWrapper NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(PRConstants.modid);

    public static void registerPackets() {
        NETWORK_WRAPPER.registerMessage(MessageClientSpecialAttack.Handler.class, MessageClientSpecialAttack.class, 0, Side.CLIENT);
    }
}
