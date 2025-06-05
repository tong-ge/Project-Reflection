package bruce.projectreflection;

import bruce.projectreflection.init.PRMetaTileEntityHandler;
import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import org.jetbrains.annotations.NotNull;

@JEIPlugin
public class PrJeiPlugin implements IModPlugin {
    @Override
    public void register(@NotNull IModRegistry registry) {
        registry.addRecipeCatalyst(PRMetaTileEntityHandler.SOLID_FUEL_BOILER.getStackForm(), "gregtech:" + PRRecipeMaps.SOLID_BOILER_FUELS.unlocalizedName);
        registry.addRecipeCatalyst(PRMetaTileEntityHandler.SOLID_FUEL_BOILER_HP.getStackForm(), "gregtech:" + PRRecipeMaps.SOLID_BOILER_FUELS.unlocalizedName);
        registry.addRecipeCatalyst(PRMetaTileEntityHandler.FLUID_FUEL_BOILER.getStackForm(), "gregtech:" + PRRecipeMaps.FLUID_BOILER_FUELS.unlocalizedName);
        registry.addRecipeCatalyst(PRMetaTileEntityHandler.FLUID_FUEL_BOILER_HP.getStackForm(), "gregtech:" + PRRecipeMaps.FLUID_BOILER_FUELS.unlocalizedName);
    }
}
