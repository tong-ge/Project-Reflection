package bruce.projectreflection.metatileentity.multis.mysticalmechanics.generators;

import bruce.projectreflection.api.MechanicalTurbineMultiblockController;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.recipes.RecipeMaps;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.BlockTurbineCasing;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public class MetaTileEntityMechanicalCombustionEngine extends MechanicalTurbineMultiblockController {
    public MetaTileEntityMechanicalCombustionEngine(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, RecipeMaps.COMBUSTION_GENERATOR_FUELS);
    }

    @Override
    protected IBlockState[] getMetalCasing() {
        return new IBlockState[]{MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.STEEL_SOLID)};
    }

    @Override
    protected IBlockState[] getTurbineCasing() {
        return new IBlockState[]{MetaBlocks.TURBINE_CASING.getState(BlockTurbineCasing.TurbineCasingType.STEEL_GEARBOX)};
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return Textures.SOLID_STEEL_CASING;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MetaTileEntityMechanicalCombustionEngine(this.metaTileEntityId);
    }
}
