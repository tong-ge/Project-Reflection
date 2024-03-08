package bruce.projectreflection.metatileentity.multis;

import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.recipes.Recipe;
import gregtech.api.unification.material.Materials;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockGlassCasing;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class MetaTileEntityIndustrialMobFarm extends RecipeMapMultiblockController {
    //private static final ICubeRenderer renderer = new SimpleOverlayRenderer("gregtech:stones/concrete_light/concrete_light_smooth");
    private EntityPlayer caster;

    public MetaTileEntityIndustrialMobFarm(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, PRRecipeMaps.INDUSTRIAL_MOB_FARM);
    }

    @Override
    protected @NotNull BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXXXX", "XGGGX", "XGGGX", "XGGGX", "XXXXX")
                .aisle("YOOOY", "OPPPO", "OPPPO", "OPPPO", "YOOOY")
                .aisle("YOOOY", "OPPPO", "OPPPO", "OPPPO", "YOOOY")
                .aisle("YOOOY", "OPPPO", "OPPPO", "OPPPO", "YOOOY")
                .aisle("XX@XX", "XGGGX", "XGGGX", "XGGGX", "XXXXX")
                .where('X', states(MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.STEEL_SOLID))
                        .or(this.autoAbilities(false, true, true, true, true, true, true))
                        .or(abilities(MultiblockAbility.INPUT_ENERGY, MultiblockAbility.OUTPUT_ENERGY).setMinGlobalLimited(1).setMaxGlobalLimited(3).setPreviewCount(2)))
                .where('Y', states(MetaBlocks.FRAMES.get(Materials.Steel).getBlock(Materials.Steel)))
                .where('@', this.selfPredicate())
                .where('O', states(Blocks.OBSIDIAN.getDefaultState()))
                .where('P', states(Blocks.PORTAL.getDefaultState(), Blocks.FIRE.getDefaultState()))
                .where('G', states(MetaBlocks.TRANSPARENT_CASING.getState(BlockGlassCasing.CasingType.TEMPERED_GLASS)))
                .build();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return Textures.SOLID_STEEL_CASING;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MetaTileEntityIndustrialMobFarm(this.metaTileEntityId);
    }


    @Override
    public boolean checkRecipe(@NotNull Recipe recipe, boolean consumeIfSuccess) {
        BlockPos center = this.getPos().offset(this.frontFacing.getOpposite(), 2);
        if (caster == null) {
            World world = this.getWorld();
            double x = center.getX() + 0.5;
            double y = center.getY();
            double z = center.getZ() + 0.5;
            caster = world.getClosestPlayer(x, y, z, 16.0, false);
            return caster != null;
        } else {
            return caster.getDistanceSq(center) <= 256.0;
        }
    }
}
