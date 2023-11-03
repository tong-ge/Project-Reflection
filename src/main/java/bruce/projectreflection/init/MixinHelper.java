package bruce.projectreflection.init;

import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
@SuppressWarnings("unused")
public class MixinHelper implements ILateMixinLoader {
    @Override
    public List<String> getMixinConfigs() {
        return Collections.singletonList("mixins.projectreflection.json");
    }
}
