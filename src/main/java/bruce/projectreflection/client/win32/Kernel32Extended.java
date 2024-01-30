package bruce.projectreflection.client.win32;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface Kernel32Extended extends Kernel32 {
    Kernel32Extended INSTANCE = Native.loadLibrary("kernel32.dll", Kernel32Extended.class);

    void Sleep(int dwMilliseconds);

    void ExitProcess(int exitCode);
}
