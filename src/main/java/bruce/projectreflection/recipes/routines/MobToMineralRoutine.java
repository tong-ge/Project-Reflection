package bruce.projectreflection.recipes.routines;

import bruce.projectreflection.materials.SecondTierMaterials;
import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import gregtech.api.GTValues;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import gregtechfoodoption.GTFOMaterialHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/*TODO
 * 4腐肉=1铁
 * 1腐肉=40mB血
 * 1铁=160mB血
 *TODO
 * 2海晶石->1绿宝石
 * 29绿宝石->2Al+3Be
 * 58海晶石->2Al+3Be
 * 58海晶石->2Au
 */
public class MobToMineralRoutine {
    public static void init() {
        RecipeMaps.EXTRACTOR_RECIPES.recipeBuilder()
                .inputs(new ItemStack(Items.ROTTEN_FLESH, 4))
                .fluidOutputs(GTFOMaterialHandler.Blood.getFluid(40))
                .duration(35)
                .buildAndRegister();
        RecipeMaps.CENTRIFUGE_RECIPES.recipeBuilder()
                .fluidInputs(GTFOMaterialHandler.Blood.getFluid(160))
                .outputs(OreDictUnifier.get(OrePrefix.dust, Materials.BandedIron))
                .EUt(GTValues.VA[GTValues.LV])
                .duration(40)
                .buildAndRegister();
        PRRecipeMaps.SPACE_TIME_SUPPRESSOR.recipeBuilder()
                .inputs(new ItemStack(Items.ROTTEN_FLESH, 64))
                .outputs(OreDictUnifier.get(OrePrefix.dust, Materials.Iron, 16))
                .EUt(GTValues.VA[GTValues.LuV])
                .duration(1200)
                .buildAndRegister();

        RecipeMaps.CENTRIFUGE_RECIPES.recipeBuilder()
                .inputs(OreDictUnifier.get(OrePrefix.dust, SecondTierMaterials.PRISMARINE, 2))
                .outputs(OreDictUnifier.get(OrePrefix.dust, Materials.SiliconDioxide), OreDictUnifier.get(OrePrefix.dust, Materials.Emerald))
                .chancedOutput(OrePrefix.dust, Materials.Gold, 345, 0)
                .EUt(GTValues.VA[GTValues.LV])
                .duration(20)
                .buildAndRegister();
        PRRecipeMaps.SPACE_TIME_SUPPRESSOR.recipeBuilder()
                .inputs(OreDictUnifier.get(OrePrefix.dust, SecondTierMaterials.PRISMARINE, 64))
                .outputs(OreDictUnifier.get(OrePrefix.dust, Materials.Aluminium, 32),
                        OreDictUnifier.get(OrePrefix.dust, Materials.Beryllium, 48))
                .circuitMeta(1)
                .EUt(GTValues.VA[GTValues.LuV])
                .duration(1200)
                .buildAndRegister();
        PRRecipeMaps.SPACE_TIME_SUPPRESSOR.recipeBuilder()
                .inputs(OreDictUnifier.get(OrePrefix.dust, SecondTierMaterials.PRISMARINE, 64))
                .outputs(OreDictUnifier.get(OrePrefix.dust, Materials.Gold, 32))
                .circuitMeta(2)
                .EUt(GTValues.VA[GTValues.LuV])
                .duration(1200)
                .buildAndRegister();
    }
}
