package bruce.projectreflection.metatileentity.multi.part;

import bruce.projectreflection.PRAbility;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.GTValues;
import gregtech.api.gui.ModularUI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiblockNotifiablePart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.List;

/**
 * RF仓室
 *
 * @author tong-ge
 */
public class MteFluxHatch extends MetaTileEntityMultiblockNotifiablePart implements IMultiblockAbilityPart<IEnergyStorage> {
    public MteFluxHatch(ResourceLocation metaTileEntityId, int tier, boolean isExportHatch) {
        super(metaTileEntityId, tier, isExportHatch);
        energyStorage = new EnergyStorageHandler(this, (int) GTValues.V[tier] * 512, isExportHatch ? 0 : (int) GTValues.V[tier] * 8, isExportHatch ? (int) GTValues.V[tier] * 8 : 0);
    }

    private final IEnergyStorage energyStorage;

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MteFluxHatch(this.metaTileEntityId, this.getTier(), this.isExportHatch);
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        return null;
    }

    @Override
    public MultiblockAbility<IEnergyStorage> getAbility() {
        return isExportHatch ? PRAbility.OUTPUT_RF : PRAbility.INPUT_RF;
    }

    @Override
    public void registerAbilities(List<IEnergyStorage> list) {
        list.add(energyStorage);
    }

    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        if (this.shouldRenderOverlay()) {
            if (this.isExportHatch) {
                Textures.CONVERTER_FE_OUT.renderSided(this.getFrontFacing(), renderState, translation, pipeline);
            } else {
                Textures.CONVERTER_FE_IN.renderSided(this.getFrontFacing(), renderState, translation, pipeline);
            }
        }

    }

}
