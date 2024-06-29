package bruce.projectreflection.init;

import bruce.projectreflection.network.PRNetwork;
import gregtech.common.items.MetaItems;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        PRNetwork.registerPackets();
        PRMetaTileEntityHandler.registerAllMetaTileEntities();
    }
}
