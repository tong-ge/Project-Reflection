package bruce.projectreflection.metatileentity;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.recipes.recipemap.RecipeMapEnervation;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.ColourMultiplier;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.GTValues;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.SimpleGeneratorMetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.utils.PipelineUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;

public class MteEnervationGenerator extends SimpleGeneratorMetaTileEntity {
    public static final MteEnervationGenerator[] SAMPLES = new MteEnervationGenerator[9];

    static {
        for (int i = 0; i < SAMPLES.length; i++) {
            SAMPLES[i] = new MteEnervationGenerator(new ResourceLocation(PRConstants.modid,
                    "enervation_generator." + GTValues.VN[i].toLowerCase()), i);
        }
    }

    private MteEnervationGenerator(ResourceLocation metaTileEntityId, int tier) {
        super(metaTileEntityId, RecipeMapEnervation.ENERVATION, Textures.MULTIBLOCK_WORKABLE_OVERLAY, tier,
                GTUtility.genericGeneratorTankSizeFunction, true);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MteEnervationGenerator(metaTileEntityId, getTier());
    }

    public boolean isValidFrontFacing(EnumFacing facing) {
        return true;
    }

    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        IVertexOperation[] colouredPipeline = ArrayUtils.add(pipeline, new ColourMultiplier(GTUtility.convertRGBtoOpaqueRGBA_CL(this.getPaintingColorForRendering())));
        this.getBaseRenderer().render(renderState, translation, colouredPipeline);
        this.renderOverlays(renderState, translation, pipeline);
        Textures.ENERGY_OUT.renderSided(this.getFrontFacing(), renderState, translation, PipelineUtil.color(pipeline, GTValues.VC[this.getTier()]));
    }

    protected void renderOverlays(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        this.renderer.renderOrientedState(renderState, translation, pipeline, this.getFrontFacing().getOpposite(), this.workable.isActive(), this.workable.isWorkingEnabled());
    }
}
