package bruce.projectreflection.metatileentity.steam;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.PRLabs;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.GTValues;
import gregtech.api.capability.impl.CommonFluidFilters;
import gregtech.api.capability.impl.FilteredFluidHandler;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.TankWidget;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.util.GTTransferUtils;
import gregtech.client.renderer.texture.Textures;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class SteamDiffuser extends MetaTileEntity {
    public static final SteamDiffuser SAMPLE = new SteamDiffuser(new ResourceLocation(PRConstants.modid, "steam_diffuser"));
    private FluidTank fluidTank;
    private int cooldown;

    public SteamDiffuser(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId);
        cooldown = 20;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new SteamDiffuser(metaTileEntityId);
    }

    @Override
    protected void initializeInventory() {
        super.initializeInventory();
        this.fluidTank = new FilteredFluidHandler(8000).setFilter(CommonFluidFilters.STEAM);
        this.fluidInventory = fluidTank;
        this.importFluids = new FluidTankList(false, fluidTank);
        this.exportFluids = new FluidTankList(false, fluidTank);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        data = super.writeToNBT(data);
        data.setTag("FluidTank", fluidTank.writeToNBT(new NBTTagCompound()));
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        fluidTank.readFromNBT(data.getCompoundTag("FluidTank"));
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        return ModularUI.builder(GuiTextures.BACKGROUND, 176, 166)
                .widget(new TankWidget(fluidTank, 78, 39, 18, 18)
                        .setBackgroundTexture(GuiTextures.FLUID_SLOT)
                        .setContainerClicking(true, true)
                )
                .bindPlayerInventory(entityPlayer.inventory)
                .build(getHolder(), entityPlayer);
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        Textures.VOLTAGE_CASINGS[GTValues.ULV].render(renderState, translation, pipeline);
    }

    private boolean absorbNearbyFluids() {
        for (EnumFacing facing : EnumFacing.VALUES) {
            BlockPos blockPos = this.getPos().offset(facing);
            //PRLabs.logger.info("Try absorbing fluid at {}",blockPos);
            IBlockState blockState = getWorld().getBlockState(blockPos);
            if (blockState.getBlock() instanceof BlockLiquid || blockState.getBlock() instanceof IFluidBlock) {
                IFluidHandler fluidHandler = FluidUtil.getFluidHandler(this.getWorld(), blockPos, facing.getOpposite());
                if (fluidHandler != null) {
                    int transferred = GTTransferUtils.transferFluids(fluidHandler, fluidTank);
                    //PRLabs.logger.info("{} mb transferred",transferred);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void update() {
        super.update();
        if (cooldown <= 0) {
            this.pushFluidsIntoNearbyHandlers(EnumFacing.values());
            if (this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount() >= 1000) {
                if (!absorbNearbyFluids())
                    cooldown = 20;
            }
        } else cooldown--;
    }
}
