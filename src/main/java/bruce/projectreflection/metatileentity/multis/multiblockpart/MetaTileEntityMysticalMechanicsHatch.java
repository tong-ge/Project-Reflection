package bruce.projectreflection.metatileentity.multis.multiblockpart;

import bruce.projectreflection.PRAbility;
import bruce.projectreflection.ProjectReflection;
import bruce.projectreflection.api.IGTMechCapability;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.GTValues;
import gregtech.api.gui.ModularUI;
import gregtech.api.metatileentity.IDataInfoProvider;
import gregtech.api.metatileentity.ITieredMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.utils.PipelineUtil;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiblockNotifiablePart;
import mysticalmechanics.api.DefaultMechCapability;
import mysticalmechanics.api.IMechCapability;
import mysticalmechanics.api.MysticalMechanicsAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MetaTileEntityMysticalMechanicsHatch extends MetaTileEntityMultiblockNotifiablePart implements IMultiblockAbilityPart<IGTMechCapability>, IDataInfoProvider {
    private final IGTMechCapability mechContainer;

    private static class MechContainer extends DefaultMechCapability implements IGTMechCapability {
        private final MetaTileEntityMysticalMechanicsHatch tileEntity;
        private final int tier;

        public MechContainer(MetaTileEntityMysticalMechanicsHatch tileEntity, int tier) {
            this.tileEntity = tileEntity;
            this.tier = tier;
        }

        private boolean isExportHatch() {
            return tileEntity.isExportHatch;
        }

        private World getWorld() {
            return tileEntity.getWorld();
        }

        private EnumFacing getFront() {
            return tileEntity.getFrontFacing();
        }

        private BlockPos getPos() {
            return tileEntity.getPos();
        }

        @Override
        public boolean isInput(EnumFacing from) {
            return !isExportHatch() && from == tileEntity.getFrontFacing();
        }

        @Override
        public boolean isOutput(EnumFacing from) {
            return isExportHatch() && from == tileEntity.getFrontFacing();
        }

        @Override
        public void onPowerChange() {
            super.onPowerChange();
            if (isExportHatch()) {
                for (EnumFacing f : EnumFacing.values()) {
                    TileEntity t = getWorld().getTileEntity(getPos().offset(f));
                    if (t != null && f == getFront()) {
                        if (t.hasCapability(MysticalMechanicsAPI.MECH_CAPABILITY, f.getOpposite())) {
                            t.getCapability(MysticalMechanicsAPI.MECH_CAPABILITY, f.getOpposite())
                                    .setPower(this.getPower(f.getOpposite()), f.getOpposite());
                            t.markDirty();
                        }
                    }
                }
            }
            if (this.getPower(getFront()) > this.getMaxPower()) {
                tileEntity.doExplosion((float) this.getPower(getFront()));
            }
        }

        /*
                @Override
                public double getPower(EnumFacing from) {
                    if(this.tileEntity.getController().isActive())
                    return super.getPower(from);
                    else return 0;
                }
        */
        @Override
        public long getMaxPower() {
            return GTValues.V[this.tier];
        }
    }

    public MetaTileEntityMysticalMechanicsHatch(ResourceLocation metaTileEntityId, int tier, boolean isExportHatch) {
        super(metaTileEntityId, tier, isExportHatch);
        mechContainer = new MechContainer(this, tier);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MetaTileEntityMysticalMechanicsHatch(metaTileEntityId, getTier(), isExportHatch);
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        return null;
    }

    @Override
    public MultiblockAbility<IGTMechCapability> getAbility() {
        return this.isExportHatch ? PRAbility.OUTPUT_MECH : PRAbility.INPUT_MECH;
    }

    @Override
    public void registerAbilities(List<IGTMechCapability> list) {
        list.add(mechContainer);
    }

    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        if (this.shouldRenderOverlay()) {
            Textures.ENERGY_IN.renderSided(this.getFrontFacing(), renderState, translation, PipelineUtil.color(pipeline, GTValues.VC[this.getTier()]));
        }

    }

    @Override
    public @NotNull List<ITextComponent> getDataInfo() {
        return Collections.singletonList(new TextComponentTranslation("Current kinetic:%s", mechContainer.getPower(this.getFrontFacing())));
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing side) {
        if (capability == MysticalMechanicsAPI.MECH_CAPABILITY && side == this.getFrontFacing()) {
            return (T) this.mechContainer;
        }
        return super.getCapability(capability, side);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        data = super.writeToNBT(data);
        NBTTagCompound mechData = new NBTTagCompound();
        this.mechContainer.writeToNBT(mechData);
        data.setTag("mystical_mechanics", mechData);
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        NBTTagCompound mechData = data.getCompoundTag("mystical_mechanics");
        this.mechContainer.readFromNBT(mechData);
    }

    @Override
    public void removeFromMultiBlock(MultiblockControllerBase controllerBase) {
        super.removeFromMultiBlock(controllerBase);
        mechContainer.setPower(0, this.getFrontFacing());
    }
}
