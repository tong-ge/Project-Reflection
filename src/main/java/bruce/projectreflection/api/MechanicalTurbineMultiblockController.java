package bruce.projectreflection.api;

import bruce.projectreflection.PRAbility;
import gregtech.api.capability.IRotorHolder;
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

    public IRotorHolder getRotorHolder() {
        List<IRotorHolder> abilities = this.getAbilities(PRAbility.LL_ROTOR_HOLDER);
        return abilities.isEmpty() ? null : abilities.get(0);
    }

    public boolean isRotorFaceFree() {
        IRotorHolder rotorHolder = this.getRotorHolder();
        if (rotorHolder == null) {
            return false;
        } else {
            return this.isStructureFormed() && rotorHolder.isFrontFaceFree();
        }
    }

    public boolean isStructureObstructed() {
        return super.isStructureObstructed() || !this.isRotorFaceFree();
    }
    @Nonnull
    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXXX", "XXXX", "XXXX")
                .aisle("XXXX", "H##Y", "XXXX")
                .aisle("XXXX", "XSXX", "XXXX")
                .where('S', this.selfPredicate())
                .where('X', states(getMetalCasing()).setMinGlobalLimited(14)
                        .or(this.autoAbilities(false, true, false, false, true, true, true)))
                .where('#', states(getTurbineCasing()))
                .where('Y', abilities(PRAbility.OUTPUT_MECH))
                .where('H', abilities(PRAbility.LL_ROTOR_HOLDER))
                .build();
    }

    protected abstract IBlockState[] getMetalCasing();

    protected abstract IBlockState[] getTurbineCasing();

}
