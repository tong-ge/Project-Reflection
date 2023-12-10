package bruce.projectreflection.metatileentity.multis;

import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.LabelWidget;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.RecipeMapPrimitiveMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.renderer.texture.cube.SimpleOverlayRenderer;
import knightminer.ceramics.Ceramics;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MetaTileEntityCeramicOven extends RecipeMapPrimitiveMultiblockController {
    private static final ICubeRenderer renderer = new SimpleOverlayRenderer("ceramics:porcelain_bricks");

    public MetaTileEntityCeramicOven(ResourceLocation location) {
        super(location, PRRecipeMaps.CERAMIC_OVEN);
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXXXX", "XXXXX", "XXXXX", "XXXXX")
                .aisleRepeatable(3, 14, "XXXXX", "Y###Y", "Y###Y", "XXXXX")
                .aisle("XXXXX", "XX@XX", "XXXXX", "XXXXX")
                .where('X', states(Ceramics.clayHard.getDefaultState()))
                .where('Y', states(Ceramics.clayHard.getDefaultState()).or(this.autoAbilities()))
                .where('#', air())
                .where('@', selfPredicate())
                .build();

    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return renderer;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityCeramicOven(this.metaTileEntityId);
    }

    @Override
    public boolean hasMaintenanceMechanics() {
        return false;
    }

    protected ModularUI.Builder createUITemplate(EntityPlayer player) {

        return this.recipeMapWorkable.getRecipeMap()
                .createUITemplate(recipeMapWorkable::getProgressPercent, importItems, exportItems, importFluids, exportFluids,
                        0)
                .widget(new LabelWidget(5, 5, getMetaFullName()))
                .bindPlayerInventory(player.inventory, GuiTextures.SLOT, 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected ICubeRenderer getFrontOverlay() {
        return Textures.FURNACE_OVERLAY;
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        getFrontOverlay().renderOrientedState(renderState, translation, pipeline, getFrontFacing(),
                recipeMapWorkable.isActive(), recipeMapWorkable.isWorkingEnabled());
    }
}
