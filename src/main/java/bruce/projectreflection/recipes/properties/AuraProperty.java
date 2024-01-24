package bruce.projectreflection.recipes.properties;

import com.sun.jna.Structure;
import gregtech.api.metatileentity.multiblock.CleanroomType;
import gregtech.api.recipes.recipeproperties.ComputationProperty;
import gregtech.api.recipes.recipeproperties.RecipeProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import java.util.ArrayList;
import java.util.List;

public class AuraProperty extends RecipeProperty<AuraProperty.AuraPropertyStruct> {
    public static final String KEY = "aura_information";
    public static final AuraProperty INSTANCE = new AuraProperty();

    protected AuraProperty() {
        super(KEY, AuraPropertyStruct.class);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int x, int y, int color, Object value) {
        AuraPropertyStruct type = castValue(value);
        if (type == null) return;

        minecraft.fontRenderer.drawString(I18n.format("projectreflection.recipe.aura", type.auraConsumption, type.isExtended), x, y, color);
    }

    public static class AuraPropertyStruct extends Structure {
        public AuraPropertyStruct(float auraConsumption, boolean isExtended) {
            this.auraConsumption = auraConsumption;
            this.isExtended = isExtended;
        }

        public float auraConsumption;
        public boolean isExtended;

        @Override
        protected List<String> getFieldOrder() {
            return new ArrayList<>();
        }
    }
}
