package bruce.projectreflection.metatileentity.multi.part;

import bruce.projectreflection.PRAbility;
import bruce.projectreflection.capability.energy.IExtendedEnergyStorage;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.GTValues;
import gregtech.api.gui.ModularUI;
import gregtech.api.metatileentity.IDataInfoProvider;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiblockNotifiablePart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * RF仓室
 *
 * @author tong-ge
 */
public class MteFluxHatch extends MetaTileEntityMultiblockNotifiablePart implements IMultiblockAbilityPart<IExtendedEnergyStorage>, IDataInfoProvider {
    public MteFluxHatch(ResourceLocation metaTileEntityId, int tier, boolean isExportHatch) {
        super(metaTileEntityId, tier, isExportHatch);
        energyStorage = new EnergyStorageHandler(this, (int) GTValues.V[tier] * 512L, isExportHatch ? 0 : (int) GTValues.V[tier] * 8, isExportHatch ? (int) GTValues.V[tier] * 8 : 0);
    }

    private final IExtendedEnergyStorage energyStorage;

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MteFluxHatch(this.metaTileEntityId, this.getTier(), this.isExportHatch);
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        return null;
    }

    @Override
    public MultiblockAbility<IExtendedEnergyStorage> getAbility() {
        return isExportHatch ? PRAbility.OUTPUT_RF : PRAbility.INPUT_RF;
    }

    @Override
    public void registerAbilities(List<IExtendedEnergyStorage> list) {
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

    @Override
    public @NotNull List<ITextComponent> getDataInfo() {
        return Collections.singletonList(new TextComponentString(String.format("%d / %d RF", this.energyStorage.getEnergyStored(), this.energyStorage.getMaxEnergyStored())));
    }
}
