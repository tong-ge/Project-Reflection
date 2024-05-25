package bruce.projectreflection.metatileentity.multis;

import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.NotifiableFluidTank;
import gregtech.api.capability.impl.NotifiableItemStackHandler;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.*;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.RecipeMapPrimitiveMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import knightminer.ceramics.Ceramics;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static bruce.projectreflection.metatileentity.multis.MetaTileEntityCeramicOven.renderer;

public class MetaTileEntityStewer extends RecipeMapPrimitiveMultiblockController {
    protected NotifiableItemStackHandler inventory;
    protected FluidTankList fluidInventory1;

    public MetaTileEntityStewer(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, PRRecipeMaps.STEWER);
    }

    @Override
    public boolean hasMaintenanceMechanics() {
        return false;
    }

    @Override
    protected @NotNull BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("&&&&&", "&YYY&", "&YYY&")
                .aisleRepeatable(3, 14, "&XXX&", "Y###Y", "Y###Y")
                .aisle("&&&&&", "&YYY&", "&Y@Y&")
                .where('X', states(Ceramics.clayHard.getDefaultState()))
                .where('Y', states(Ceramics.clayHard.getDefaultState()).or(this.autoAbilities()))
                .where('#', air())
                .where('&', any())
                .where('@', selfPredicate())
                .build();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return renderer;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected @NotNull ICubeRenderer getFrontOverlay() {
        return Textures.MULTIBLOCK_WORKABLE_OVERLAY;
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        getFrontOverlay().renderOrientedState(renderState, translation, pipeline, getFrontFacing(),
                recipeMapWorkable.isActive(), recipeMapWorkable.isWorkingEnabled());
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityStewer(this.metaTileEntityId);
    }

    private List<FluidTank> makeFluidTanks(int length, boolean isExport) {
        List<FluidTank> fluidTankList = new ArrayList(length);
        for (int i = 0; i < length; ++i) {
            fluidTankList.add(new NotifiableFluidTank(32000, this, isExport));
        }
        return fluidTankList;
    }

    @Override
    protected void initializeAbilities() {
        //super.initializeAbilities();
        this.inventory = new NotifiableItemStackHandler(this,
                getRecipeMap().getMaxInputs() + getRecipeMap().getMaxOutputs(),
                this, false);
        this.fluidInventory1 = new FluidTankList(true,
                this.makeFluidTanks(getRecipeMap().getMaxFluidInputs() + getRecipeMap().getMaxOutputs(),
                        false));
        this.itemInventory = inventory;
        this.fluidInventory = fluidInventory1;
        this.importItems = inventory;
        this.importFluids = fluidInventory1;
        this.exportItems = inventory;
        this.exportFluids = fluidInventory1;
    }

    @Override
    protected ModularUI.Builder createUITemplate(EntityPlayer entityPlayer) {
        ModularUI.Builder builder = ModularUI.builder(GuiTextures.BACKGROUND, 176, 166)
                .shouldColor(false)
                .widget(new LabelWidget(5, 5, getMetaFullName()));
        for (int i = 0; i < inventory.getSlots(); i++) {
            builder.widget(new SlotWidget(inventory, i, 16 + (i % 2) * 18, 13 + (i / 2) * 18, true, true)
                    .setBackgroundTexture(GuiTextures.SLOT));
        }

        builder.widget(new RecipeProgressWidget(recipeMapWorkable::getProgressPercent, 76, 16, 20, 54,
                GuiTextures.PROGRESS_BAR_HAMMER, ProgressWidget.MoveType.VERTICAL, this.getRecipeMap()));
        for (int i = 0; i < fluidInventory1.getTanks(); i++) {
            builder.widget(new TankWidget(fluidInventory1.getTankAt(i), 118 + (i % 2) * 18, 13 + (i / 2) * 18, 18, 18)
                    .setBackgroundTexture(GuiTextures.FLUID_SLOT)
                    .setContainerClicking(true, true)
                    .setAlwaysShowFull(true));
        }
        return builder.bindPlayerInventory(entityPlayer.inventory, 84);
    }
}
