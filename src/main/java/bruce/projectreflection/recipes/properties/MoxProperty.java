package bruce.projectreflection.recipes.properties;

import gregtech.api.recipes.recipeproperties.RecipeProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class MoxProperty extends RecipeProperty<MoxProperty.MoxFunction> {
    public static final String KEY = "mox_function";
    public static final MoxProperty INSTANCE = new MoxProperty();

    protected MoxProperty() {
        super(KEY, MoxFunction.class);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int x, int y, int color, Object value) {
        MoxFunction type = castValue(value);
        if (type == null) return;

        minecraft.fontRenderer.drawString(I18n.format("projectreflection.recipe.mox.desc", type.getDescription()), x, y, color);
    }

    public interface MoxFunction {
        float getHeat(float temperature, float maxTemperature);

        float boostProduction(float basePower, float temperature, float maxTemperature);

        String getDescription();
    }

    public static class UraniumModel implements MoxFunction {
        protected final float baseHeat;

        public UraniumModel(float baseHeat) {
            this.baseHeat = baseHeat;
        }

        @Override
        public float getHeat(float temperature, float maxTemperature) {
            return baseHeat;
        }

        @Override
        public float boostProduction(float basePower, float temperature, float maxTemperature) {
            return basePower;
        }


        @Override
        public String getDescription() {
            return "Uranium Model";
        }
    }

    public static final class StandardMox extends UraniumModel {
        private final float powerMultiplier;
        private final float heatMultiplier;
        private final float heatThreshold;

        public StandardMox(float baseHeat) {
            this(baseHeat, 4f, 2f, 0.5f);
        }

        public StandardMox(float baseHeat, float powerMultiplier, float heatMultiplier, float heatThreshold) {
            super(baseHeat);
            this.powerMultiplier = powerMultiplier;
            this.heatMultiplier = heatMultiplier;
            this.heatThreshold = heatThreshold;
        }

        @Override
        public float getHeat(float temperature, float maxTemperature) {
            return temperature >= maxTemperature * heatThreshold ? baseHeat * heatMultiplier : baseHeat;
        }

        @Override
        public float boostProduction(float basePower, float temperature, float maxTemperature) {
            float heatPercentage = temperature / maxTemperature;
            return basePower * (1 + powerMultiplier * heatPercentage);
        }


        @Override
        public String getDescription() {
            return String.format("Power=base*(1+%f*T),Heat=%f(%f),Threshold=%.02f%%", powerMultiplier, baseHeat, baseHeat * heatMultiplier, heatThreshold * 100f);
        }
    }
}
