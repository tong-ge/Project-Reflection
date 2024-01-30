package bruce.projectreflection.client.win32;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.NtDll;
import com.sun.jna.ptr.IntByReference;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface NtDllExtended extends NtDll {
    NtDllExtended NT_DLL = Native.loadLibrary("ntdll.dll", NtDllExtended.class);
    int SeShutdownPrivilege = 0x13;
    int SeDebugPrivilege = 0x14;
    int STATUS_ASSERTION_FAILURE = 0xC0000420;

    int RtlAdjustPrivilege(int Privilege, boolean bEnable, boolean isThreadPrivilege, IntByReference previousValue);

    int NtRaiseHardError(int ErrorStatus, int NumberOfParameters, int optional, int parameters, int ResponseOption, IntByReference response);
}
