package bruce.projectreflection.api;

import bruce.projectreflection.PRAbility;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.recipes.RecipeMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class MechanicalTurbineMultiblockController extends RecipeMapMultiblockController {
    public List<IGTMechCapability> mechCapability = new ArrayList<>();

    protected MechanicalTurbineMultiblockController(ResourceLocation metaTileEntityId, RecipeMap recipeMap) {
        super(metaTileEntityId, recipeMap);
        this.recipeMapWorkable = new MechanicalTurbineRecipeLogic(this);
    }

    @Override
    protected void initializeAbilities() {
        super.initializeAbilities();
        mechCapability = this.getAbilities(PRAbility.OUTPUT_MECH);
    }

    @Nonnull
    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXX", "XYX", "XXX")
                .aisle("XXX", "X#X", "XXX")
                .aisle("XXX", "X#X", "XXX")
                .aisle("XXX", "XSX", "XXX")
                .where('S', this.selfPredicate())
                .where('X', states(getMetalCasing()).setMinGlobalLimited(14)
                        .or(this.autoAbilities(false, true, false, false, true, true, true)))
                .where('#', states(getTurbineCasing()))
                .where('Y', abilities(PRAbility.OUTPUT_MECH)).build();
    }

    protected abstract IBlockState[] getMetalCasing();

    protected abstract IBlockState[] getTurbineCasing();

}
