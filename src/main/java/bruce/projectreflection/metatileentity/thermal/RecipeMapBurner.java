package bruce.projectreflection.metatileentity.thermal;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.capability.PRCapabilities;
import bruce.projectreflection.capability.energy.IHeatReceiver;
import gregtech.api.capability.impl.AbstractRecipeLogic;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.NotifiableFluidTank;
import gregtech.api.capability.impl.NotifiableItemStackHandler;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.LabelWidget;
import gregtech.api.items.itemhandlers.GTItemStackHandler;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public class RecipeMapBurner extends MetaTileEntity {
    public static final RecipeMapBurner SEMIFLUID_BURNER = new RecipeMapBurner(new ResourceLocation(PRConstants.modid, "semifluid_burner"),
            RecipeMaps.SEMI_FLUID_GENERATOR_FUELS, 32);
    protected final float efficiency;
    private final RecipeMap<?> recipeMap;
    private final AbstractRecipeLogic recipeLogic;

    public RecipeMapBurner(ResourceLocation metaTileEntityId, RecipeMap<?> recipeMap, int maxHUt) {
        super(metaTileEntityId);
        this.efficiency = maxHUt / 32.0f;
        this.recipeMap = recipeMap;
        this.recipeLogic = new RecipeLogicBurner(this, recipeMap);
        this.initializeInventory();
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new RecipeMapBurner(this.metaTileEntityId, recipeMap, 32);
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        int yOffset = 0;
        if (recipeMap.getMaxInputs() >= 6 || recipeMap.getMaxFluidInputs() >= 6 ||
                recipeMap.getMaxOutputs() >= 6 || recipeMap.getMaxFluidOutputs() >= 6)
            yOffset = 9;

        ModularUI.Builder builder;
        builder = recipeMap.createUITemplate(recipeLogic::getProgressPercent,
                importItems, exportItems, importFluids, exportFluids, yOffset);

        builder.widget(new LabelWidget(6, 6, getMetaFullName()))
                .bindPlayerInventory(entityPlayer.inventory, GuiTextures.SLOT, yOffset);

        return builder.build(getHolder(), entityPlayer);
    }

    protected void provideHeat(int amount) {

        BlockPos receiver = this.getPos().up();
        TileEntity receiverTE = this.getWorld().getTileEntity(receiver);
        if (receiverTE != null && receiverTE.hasCapability(PRCapabilities.CAPABILITY_HEAT_RECEIVER, EnumFacing.DOWN)) {
            IHeatReceiver heatReceiver = receiverTE.getCapability(PRCapabilities.CAPABILITY_HEAT_RECEIVER, EnumFacing.DOWN);
            heatReceiver.receiveHeat((int) (amount * efficiency));
        }
    }

    @Override
    protected IItemHandlerModifiable createImportItemHandler() {
        if (recipeLogic == null) return new GTItemStackHandler(this, 0);
        return new NotifiableItemStackHandler(this, recipeLogic.getRecipeMap().getMaxInputs(), this, false);
    }

    @Override
    protected IItemHandlerModifiable createExportItemHandler() {
        if (recipeLogic == null) return new GTItemStackHandler(this, 0);
        return new NotifiableItemStackHandler(this, recipeLogic.getRecipeMap().getMaxOutputs(), this, true);
    }

    @Override
    protected FluidTankList createImportFluidHandler() {
        if (recipeLogic == null) return new FluidTankList(false);
        NotifiableFluidTank[] fluidImports = new NotifiableFluidTank[recipeLogic.getRecipeMap().getMaxFluidInputs()];
        for (int i = 0; i < fluidImports.length; i++) {
            NotifiableFluidTank filteredFluidHandler = new NotifiableFluidTank(
                    16000, this, false);
            fluidImports[i] = filteredFluidHandler;
        }
        return new FluidTankList(false, fluidImports);
    }

    @Override
    protected FluidTankList createExportFluidHandler() {
        if (recipeLogic == null) return new FluidTankList(false);
        FluidTank[] fluidExports = new FluidTank[recipeLogic.getRecipeMap().getMaxFluidOutputs()];
        for (int i = 0; i < fluidExports.length; i++) {
            fluidExports[i] = new NotifiableFluidTank(16000, this, true);
        }
        return new FluidTankList(false, fluidExports);
    }

    private static class RecipeLogicBurner extends AbstractRecipeLogic {
        private final RecipeMapBurner tileEntity;

        public RecipeLogicBurner(RecipeMapBurner tileEntity, RecipeMap<?> recipeMap) {
            super(tileEntity, recipeMap);
            this.tileEntity = tileEntity;
        }

        @Override
        protected long getEnergyInputPerSecond() {
            return 0;
        }

        @Override
        protected long getEnergyStored() {
            return 0;
        }

        @Override
        protected long getEnergyCapacity() {
            return 0;
        }

        @Override
        protected boolean drawEnergy(int recipeEUt, boolean simulate) {
            if (!simulate) {
                tileEntity.provideHeat(recipeEUt);
            }
            return true;
        }

        @Override
        public long getMaxVoltage() {
            return 32;
        }

        @Override
        public boolean consumesEnergy() {
            return false;
        }

        @Override
        protected boolean hasEnoughPower(int @NotNull [] resultOverclock) {
            return true;
        }

        @Override
        public boolean isAllowOverclocking() {
            return false;
        }
    }
}
