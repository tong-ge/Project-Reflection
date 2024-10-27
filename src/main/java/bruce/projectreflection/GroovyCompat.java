package bruce.projectreflection;

import com.cleanroommc.groovyscript.api.GroovyPlugin;
import com.cleanroommc.groovyscript.compat.mods.GroovyContainer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class GroovyCompat implements GroovyPlugin {
    @Override
    public @NotNull String getModId() {
        return PRConstants.modid;
    }

    @Override
    public @NotNull String getContainerName() {
        return "prlabs";
    }

    @Override
    public void onCompatLoaded(GroovyContainer<?> groovyContainer) {

    }
}
