package bruce.projectreflection.metatileentity;

import gregtech.api.gui.ModularUI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.TieredMetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.common.world.aura.AuraHandler;

public class MetaTileEntityAuraGenerator extends TieredMetaTileEntity {
    static final float euPerVis=250.0f;
    public MetaTileEntityAuraGenerator(ResourceLocation metaTileEntityId, int tier) {
        super(metaTileEntityId, tier);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MetaTileEntityAuraGenerator(metaTileEntityId,getTier());
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        return null;
    }

    @Override
    public void update() {
        super.update();
        if(!getWorld().isRemote)
        {
            long euRest=energyContainer.getEnergyCapacity()-energyContainer.getEnergyStored();
            long euDesired=Math.min(euRest,energyContainer.getOutputVoltage()*energyContainer.getOutputAmperage());
            float desiredVis=(float) euDesired/euPerVis;
            float drainedVis= AuraHandler.drainVis(getWorld(),getPos(),desiredVis,false);
            long euGeneration=(long)Math.floor(drainedVis*euPerVis);
            energyContainer.changeEnergy(euGeneration);
            if(euGeneration >0)
            {
                AuraHelper.polluteAura(getWorld(),getPos(),Math.min(drainedVis,1.0f),false);
            }
        }
    }

    @Override
    protected boolean isEnergyEmitter() {
        return true;
    }
}
