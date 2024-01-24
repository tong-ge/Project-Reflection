package bruce.projectreflection.metatileentity.multis.multiblockpart;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.GTValues;
import gregtech.api.capability.IMufflerHatch;
import gregtech.api.gui.ModularUI;
import gregtech.api.metatileentity.ITieredMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.metatileentity.multiblock.MultiblockWithDisplayBase;
import gregtech.api.util.GTUtility;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.utils.TooltipHelper;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiblockPart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aura.AuraHelper;

import java.util.List;

public class MetaTileEntityFluxMuffler extends MetaTileEntityMultiblockPart implements
        IMultiblockAbilityPart<IMufflerHatch>, ITieredMetaTileEntity, IMufflerHatch {

    private boolean frontFaceFree;
    private final float pollutionMultiplier;

    public MetaTileEntityFluxMuffler(ResourceLocation metaTileEntityId, int tier) {
        super(metaTileEntityId, tier);
        this.pollutionMultiplier = ((100F - 12.5F * ((float) tier - 1F)) / 100F);
        this.frontFaceFree = false;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityFluxMuffler(metaTileEntityId, getTier());
    }

    @Override
    public void update() {
        super.update();
        MultiblockControllerBase controller = getController();
        if (!getWorld().isRemote) {
            if (getOffsetTimer() % 10 == 0)
                this.frontFaceFree = checkFrontFaceFree();
        } else if (controller instanceof MultiblockWithDisplayBase && controller.isActive()) {
            pollutionParticles();
        }
    }

    public void recoverItemsTable(List<ItemStack> recoveryItems) {
        int stacksize = 0;
        for (ItemStack stack : recoveryItems) {
            stacksize += stack.getCount();
        }
        float pollution = ((float) stacksize * pollutionMultiplier);
        AuraHelper.polluteAura(getWorld(), getPos(), pollution, false);
    }


    /**
     * @return true if front face is free and contains only air blocks in 1x1 area
     */
    public boolean isFrontFaceFree() {
        return frontFaceFree;
    }

    private boolean checkFrontFaceFree() {
        BlockPos frontPos = getPos().offset(getFrontFacing());
        IBlockState blockState = getWorld().getBlockState(frontPos);
        MultiblockWithDisplayBase controller = (MultiblockWithDisplayBase) getController();

        // break a snow layer if it exists, and if this machine is running
        if (controller != null && controller.isActive()) {
            if (GTUtility.tryBreakSnow(getWorld(), frontPos, blockState, true)) {
                return true;
            }
            return blockState.getBlock().isAir(blockState, getWorld(), frontPos);
        }
        return blockState.getBlock().isAir(blockState, getWorld(), frontPos) || GTUtility.isBlockSnow(blockState);
    }

    @SideOnly(Side.CLIENT)
    public void pollutionParticles() {
        BlockPos pos = this.getPos();
        EnumFacing facing = this.getFrontFacing();
        float xPos = facing.getXOffset() * 0.76F + pos.getX() + 0.25F;
        float yPos = facing.getYOffset() * 0.76F + pos.getY() + 0.25F;
        float zPos = facing.getZOffset() * 0.76F + pos.getZ() + 0.25F;

        float ySpd = facing.getYOffset() * 0.1F + 0.2F + 0.1F * GTValues.RNG.nextFloat();
        float xSpd;
        float zSpd;

        if (facing.getYOffset() == -1) {
            float temp = GTValues.RNG.nextFloat() * 2 * (float) Math.PI;
            xSpd = (float) Math.sin(temp) * 0.1F;
            zSpd = (float) Math.cos(temp) * 0.1F;
        } else {
            xSpd = facing.getXOffset() * (0.1F + 0.2F * GTValues.RNG.nextFloat());
            zSpd = facing.getZOffset() * (0.1F + 0.2F * GTValues.RNG.nextFloat());
        }
        if (getController() instanceof MultiblockWithDisplayBase)
            ((MultiblockWithDisplayBase) getController()).runMufflerEffect(
                    xPos + GTValues.RNG.nextFloat() * 0.5F,
                    yPos + GTValues.RNG.nextFloat() * 0.5F,
                    zPos + GTValues.RNG.nextFloat() * 0.5F,
                    xSpd, ySpd, zSpd);
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        if (shouldRenderOverlay())
            Textures.MUFFLER_OVERLAY.renderSided(getFrontFacing(), renderState, translation, pipeline);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, @NotNull List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(I18n.format("projectreflection.machine.muffler_hatch.tooltip1"));
        tooltip.add(I18n.format("projectreflection.muffler.pollution_tooltip", 100.0 * pollutionMultiplier));
        tooltip.add(I18n.format("gregtech.universal.enabled"));
        tooltip.add(TooltipHelper.BLINKING_RED + I18n.format("gregtech.machine.muffler_hatch.tooltip2"));
    }

    @Override
    public void addToolUsages(ItemStack stack, @Nullable World world, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.format("gregtech.tool_action.screwdriver.access_covers"));
        tooltip.add(I18n.format("gregtech.tool_action.wrench.set_facing"));
        super.addToolUsages(stack, world, tooltip, advanced);
    }

    @Override
    public MultiblockAbility<IMufflerHatch> getAbility() {
        return MultiblockAbility.MUFFLER_HATCH;
    }

    @Override
    public void registerAbilities(List<IMufflerHatch> abilityList) {
        abilityList.add(this);
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        return null;
    }
}
