package bruce.projectreflection.metatileentity;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.recipes.recipemap.RecipeMapEnervation;
import gregtech.api.GTValues;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.SimpleGeneratorMetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.client.renderer.texture.Textures;
import net.minecraft.util.ResourceLocation;

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
}
