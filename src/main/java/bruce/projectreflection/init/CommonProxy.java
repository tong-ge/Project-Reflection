package bruce.projectreflection.init;

import bruce.projectreflection.materials.PROrePrefixes;
import bruce.projectreflection.network.MessageClientSpecialAttack;
import bruce.projectreflection.network.PRNetwork;
import gregtech.common.items.MetaItems;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import teamroots.embers.ConfigManager;
import teamroots.embers.RegistryManager;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        PRNetwork.registerPackets();
        PRMetaTileEntityHandler.registerAllMetaTileEntities();
        MetaItems.addOrePrefix(PROrePrefixes.cluster,
                PROrePrefixes.crystal);
        /*
        RegistryManager.stamp_gear.toString();
        Integer.valueOf(ConfigManager.stampGearAmount);

         */
    }

    public void blueScreenOfDeath() {
        //TODO server-side packet logic
        PRNetwork.NETWORK_WRAPPER.sendToAll(new MessageClientSpecialAttack());
    }
}
