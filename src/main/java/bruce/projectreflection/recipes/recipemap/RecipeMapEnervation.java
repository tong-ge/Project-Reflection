package bruce.projectreflection.recipes.recipemap;

import bruce.projectreflection.PRLabs;
import gregtech.api.capability.GregtechCapabilities;
import gregtech.api.capability.IElectricItem;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.FuelRecipeBuilder;
import gregtech.api.util.GTUtility;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RecipeMapEnervation extends RecipeMap<FuelRecipeBuilder> {
    public static RecipeMap<FuelRecipeBuilder> ENERVATION = new RecipeMapEnervation();

    private RecipeMapEnervation() {
        super("enervation", 1, 1, 1, 0, new FuelRecipeBuilder(), false);
        this.allowEmptyOutput();
    }

    @Override
    public @Nullable Recipe findRecipe(long voltage, List<ItemStack> inputs, List<FluidStack> fluidInputs, boolean exactVoltage) {
        Recipe normal = super.findRecipe(voltage, inputs, fluidInputs, exactVoltage);
        if (normal == null && !inputs.isEmpty()) {
            for (ItemStack input : inputs) {
                if (input.hasCapability(GregtechCapabilities.CAPABILITY_ELECTRIC_ITEM, null)) {
                    ItemStack copy = input.copy();
                    ItemStack output = input.copy();
                    IElectricItem electricItem = output.getCapability(GregtechCapabilities.CAPABILITY_ELECTRIC_ITEM, null);
                    if (electricItem != null) {
                        int time = Math.max(1, (int) (electricItem.getCharge() / voltage));
                        long totalEU = electricItem.discharge(voltage * time, GTUtility.getTierByVoltage(voltage), true, true, false);
                        PRLabs.logger.info("{} EU discharged", totalEU);
                        if (totalEU > 0) {
                            copy.setCount(1);
                            output.setCount(1);
                            return recipeBuilder().inputs(copy)
                                    .outputs(output)
                                    .EUt((int) Math.min(totalEU, voltage))
                                    .duration(Math.max(1, (int) (totalEU / voltage)))
                                    .build()
                                    .getResult();
                        }
                    }
                }
            }
        }
        return normal;
    }
}
