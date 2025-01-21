package bruce.projectreflection.materials.properties;

import gregtech.api.unification.material.properties.IMaterialProperty;
import gregtech.api.unification.material.properties.MaterialProperties;
import gregtech.api.unification.material.properties.PropertyKey;

public class SpringProperty implements IMaterialProperty {
    public static PropertyKey<SpringProperty> KEY = new PropertyKey<>("spring", SpringProperty.class);

    @Override
    public void verifyProperty(MaterialProperties materialProperties) {
        materialProperties.ensureSet(PropertyKey.INGOT, true);
    }
}
