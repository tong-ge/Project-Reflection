package bruce.projectreflection.init;

import bruce.projectreflection.materials.PROrePrefixes;
import bruce.projectreflection.network.MessageClientSpecialAttack;
import bruce.projectreflection.network.PRNetwork;
import gregtech.common.items.MetaItems;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        PRNetwork.registerPackets();
        PRMetaTileEntityHandler.registerAllMetaTileEntities();
        MetaItems.addOrePrefix(PROrePrefixes.cluster,
                PROrePrefixes.crystal);

    }

    public void blueScreenOfDeath() {
        //TODO server-side packet logic
        PRNetwork.NETWORK_WRAPPER.sendToAll(new MessageClientSpecialAttack());
    }
}
