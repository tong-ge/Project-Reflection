package bruce.projectreflection.client;

import bruce.projectreflection.PRLabs;
import bruce.projectreflection.client.win32.NtDllExtended;
import bruce.projectreflection.init.CommonProxy;
import com.sun.jna.ptr.IntByReference;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        int result = NtDllExtended.NT_DLL.RtlAdjustPrivilege(NtDllExtended.SeShutdownPrivilege, true,
                false, new IntByReference());
        PRLabs.logger.info("RtlAdjustPrivilege returns {}", result);
    }
}
