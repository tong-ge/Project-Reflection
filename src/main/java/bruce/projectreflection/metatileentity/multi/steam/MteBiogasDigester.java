package bruce.projectreflection.metatileentity.multi.steam;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.ItemHandlerList;
import gregtech.api.items.itemhandlers.GTItemStackHandler;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapSteamMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.PatternMatchContext;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.IFluidTank;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MteBiogasDigester extends RecipeMapSteamMultiblockController {
    public static MteBiogasDigester SAMPLE = new MteBiogasDigester(new ResourceLocation(PRConstants.modid, "biogas_digester"));
    private FluidTankList inputFluidTank;
    private FluidTankList outputFluidTank;

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        this.initExtraAbilities();
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        resetExtraAbilities();
    }

    private void initExtraAbilities() {
        this.steamFluidTank = new FluidTankList(true, this.getAbilities(MultiblockAbility.STEAM));
        this.inputFluidTank = new FluidTankList(true, this.getAbilities(MultiblockAbility.IMPORT_FLUIDS));
        this.outputFluidTank = new FluidTankList(true, this.getAbilities(MultiblockAbility.EXPORT_FLUIDS));
    }

    private void resetExtraAbilities() {
        this.steamFluidTank = new FluidTankList(true);
        this.inputFluidTank = new FluidTankList(true);
        this.outputFluidTank = new FluidTankList(true);
    }

    private MteBiogasDigester(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, PRRecipeMaps.DIGESTER_RECIPES, CONVERSION_RATE);
        this.resetExtraAbilities();
    }

    public FluidTankList getImportFluids() {
        return this.inputFluidTank;
    }

    public FluidTankList getExportFluids() {
        return this.outputFluidTank;
    }

    @Override
    protected @NotNull BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("SSSSS", "SBBBS", "SBBBS", "SBBBS")
                .aisle("SCCCS", "BXXXB", "BAAAB", "BAAAB")
                .aisle("SCCCS", "BXXXB", "BAAAB", "BAAAB")
                .aisle("SCCCS", "BXXXB", "BAAAB", "BAAAB")
                .aisle("SS@SS", "SBBBS", "SBBBS", "SBBBS")
                .where('@', selfPredicate())
                .where('S', states(MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.BRONZE_BRICKS))
                        .or(this.autoAbilities())
                        .or(abilities(MultiblockAbility.IMPORT_FLUIDS, MultiblockAbility.EXPORT_FLUIDS)))
                .where('C', states(MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.BRONZE_PIPE)))
                .where('X', states(Objects.requireNonNull(Blocks.DIRT).getDefaultState()))
                .where('B', states(Objects.requireNonNull(Blocks.BRICK_BLOCK).getDefaultState()))
                .where('A', air())
                .build();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return Textures.BRONZE_PLATED_BRICKS;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MteBiogasDigester(this.metaTileEntityId);
    }
}
