package bruce.projectreflection.metatileentity.multis.multiblockpart;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import net.minecraft.util.ResourceLocation;

public class MetaTileEntitySteamMechanicsHatch extends MetaTileEntityMysticalMechanicsHatch {
    public MetaTileEntitySteamMechanicsHatch(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, 1, false);
    }

    public ICubeRenderer getBaseTexture() {
        MultiblockControllerBase controller = this.getController();
        if (controller == null) {
            return Textures.STEAM_CASING_BRONZE;
        } else {
            return controller.getBaseTexture(this);
        }
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MetaTileEntitySteamMechanicsHatch(metaTileEntityId);
    }
}
