package bruce.projectreflection.init;

import bruce.projectreflection.network.MessageBSOD;
import bruce.projectreflection.network.PRNetwork;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        PRNetwork.registerPackets();
        PRMetaTileEntityHandler.registerAllMetaTileEntities();
    }

    public void blueScreenOfDeath() {
        //TODO server-side packet logic
        PRNetwork.NETWORK_WRAPPER.sendToAll(new MessageBSOD());
    }
}
