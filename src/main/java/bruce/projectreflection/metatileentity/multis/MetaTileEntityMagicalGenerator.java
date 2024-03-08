package bruce.projectreflection.metatileentity.multis;

import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import gregtech.api.GTValues;
import gregtech.api.capability.impl.MultiblockFuelRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.FuelMultiblockController;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockGlassCasing;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class MetaTileEntityMagicalGenerator extends FuelMultiblockController {
    public MetaTileEntityMagicalGenerator(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, PRRecipeMaps.MAGICAL_GENERATOR, GTValues.EV);
        this.recipeMapWorkable = new MagicalGeneratorRecipeLogic(this);
    }

    @Override
    protected @NotNull BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("CCC", "COC", "CCC")
                .aisleRepeatable(3, 10, "CCC", "GAG", "CMC")
                .aisle("CCC", "C@C", "CCC")
                .where('@', this.selfPredicate())
                .where('C', this.autoAbilities(false, true, false, false, true, true, false).or(states(MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.TITANIUM_STABLE))))
                .where('O', abilities(MultiblockAbility.OUTPUT_ENERGY))
                .where('M', abilities(MultiblockAbility.MUFFLER_HATCH))
                .where('G', states(MetaBlocks.TRANSPARENT_CASING.getState(BlockGlassCasing.CasingType.TEMPERED_GLASS)))
                .where('A', air())
                .build();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return Textures.STABLE_TITANIUM_CASING;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityMagicalGenerator(this.metaTileEntityId);
    }

    private static class MagicalGeneratorRecipeLogic extends MultiblockFuelRecipeLogic {
        public MagicalGeneratorRecipeLogic(RecipeMapMultiblockController tileEntity) {
            super(tileEntity);
            this.setAllowOverclocking(false);
        }

        @Override
        public int getParallelLimit() {
            return 1 << ((FuelMultiblockController) metaTileEntity).getAbilities(MultiblockAbility.MUFFLER_HATCH).size();
        }
    }
}
