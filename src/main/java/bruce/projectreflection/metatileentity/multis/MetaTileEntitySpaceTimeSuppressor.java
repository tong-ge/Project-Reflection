package bruce.projectreflection.metatileentity.multis;

import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import gregicality.multiblocks.api.render.GCYMTextures;
import gregicality.multiblocks.common.block.GCYMMetaBlocks;
import gregicality.multiblocks.common.block.blocks.BlockLargeMultiblockCasing;
import gregtech.api.GTValues;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockGlassCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class MetaTileEntitySpaceTimeSuppressor extends RecipeMapMultiblockController {
    //private static final ICubeRenderer renderer = new SimpleOverlayRenderer("gregtech:stones/concrete_light/concrete_light_smooth");
    public MetaTileEntitySpaceTimeSuppressor(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, PRRecipeMaps.SPACE_TIME_SUPPRESSOR);
    }

    @Override
    public boolean hasMufflerMechanics() {
        return true;
    }

    @Override
    protected @NotNull BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("######III######", "######OOO######", "###############")
                .aisle("####CCIIICC####", "####GGKKKGG####", "######ICI######")
                .aisle("###CCCIIICCC###", "###EKKOSOKKE###", "####CC###CC####")
                .aisle("##CCCC###CCCC##", "##EKEG###GEKE##", "###C#######C###")
                .aisle("#CCC#######CCC#", "#GKE#######EKG#", "##C#########C##")
                .aisle("#CCC#######CCC#", "#GKG#######GKG#", "##C#########C##")
                .aisle("III#########III", "OKO#########OKO", "#I###########I#")
                .aisle("III#########III", "OKO#########OKO", "#C###########C#")
                .aisle("III#########III", "OKO#########OKO", "#I###########I#")
                .aisle("#CCC#######CCC#", "#GKG#######GKG#", "##C#########C##")
                .aisle("#CCC#######CCC#", "#GKE#######EKG#", "##C#########C##")
                .aisle("##CCCC###CCCC##", "##EKEG###GEKE##", "###C#######C###")
                .aisle("###CCCIIICCC###", "###EKKOOOKKE###", "####CC###CC####")
                .aisle("####CCIIICC####", "####GGKKKGG####", "######ICI######")
                .aisle("######III######", "######OOO######", "###############")
                .where('S', selfPredicate())
                .where('G', states(getCasingState(), getGlassState()))
                .where('E', states(getCasingState(), getGlassState()).or(abilities(MultiblockAbility.INPUT_ENERGY)))
                .where('C', states(getCasingState()))
                .where('K', heatingCoils())
                .where('O', states(getCasingState(), getGlassState()).or(
                        this.autoAbilities(false, false, false, true,
                                false, true, true)))
                .where('I', states(getCasingState()).or(this.autoAbilities(false, true, true, false,
                        true, false, false)))
                .where('#', any())
                .build();
    }

    private IBlockState getGlassState() {
        return MetaBlocks.TRANSPARENT_CASING.getState(BlockGlassCasing.CasingType.LAMINATED_GLASS);
    }

    private IBlockState getCasingState() {
        return GCYMMetaBlocks.LARGE_MULTIBLOCK_CASING.getState(BlockLargeMultiblockCasing.CasingType.HIGH_TEMPERATURE_CASING);
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return GCYMTextures.BLAST_CASING;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MetaTileEntitySpaceTimeSuppressor(this.metaTileEntityId);
    }
}
