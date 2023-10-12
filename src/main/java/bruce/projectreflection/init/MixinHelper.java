package bruce.projectreflection.init;

import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Arrays;
import java.util.List;
@SuppressWarnings("unused")
public class MixinHelper implements ILateMixinLoader {
    @Override
    public List<String> getMixinConfigs() {
        return Arrays.asList("mixins.projectreflection.json");
    }
}
