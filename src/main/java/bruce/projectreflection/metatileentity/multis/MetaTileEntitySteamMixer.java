package bruce.projectreflection.metatileentity.multis;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.RecipeMapSteamMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.recipes.RecipeMap;
import gregtech.client.renderer.ICubeRenderer;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class MetaTileEntitySteamMixer extends RecipeMapSteamMultiblockController {
    public MetaTileEntitySteamMixer(ResourceLocation metaTileEntityId, RecipeMap<?> recipeMap, double conversionRate) {
        super(metaTileEntityId, recipeMap, conversionRate);
    }

    @Override
    protected @NotNull BlockPattern createStructurePattern() {
        return null;
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return null;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return null;
    }
}
