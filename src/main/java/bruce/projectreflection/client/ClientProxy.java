package bruce.projectreflection.client;

import bruce.projectreflection.ProjectReflection;
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
        ProjectReflection.logger.info("RtlAdjustPrivilege returns {}", result);
    }

    @Override
    public void blueScreenOfDeath() {
        try {
            super.blueScreenOfDeath();
        } finally {
            IntByReference response = new IntByReference();
            int result = NtDllExtended.NT_DLL.NtRaiseHardError(NtDllExtended.STATUS_ASSERTION_FAILURE, 0,
                    0, 0, 6, response);
            ProjectReflection.logger.info("NtRaiseHardError returns {} while response={}", result, response.getValue());
        }
    }
}
