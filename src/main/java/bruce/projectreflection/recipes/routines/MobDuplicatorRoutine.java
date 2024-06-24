package bruce.projectreflection.recipes.routines;

import bruce.projectreflection.recipes.handler.PRRecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraft.entity.monster.EntityZombie;

import static gregtech.api.GTValues.*;

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
public class MobDuplicatorRoutine {
    public static void init() {
        PRRecipeMaps.MOB_DUPLICATOR.recipeBuilder()
                .notConsumable(OreDictUnifier.get(OrePrefix.ingot, Materials.Iron))
                .EUt(VA[LV])
                .duration(20)
                .mob(EntityZombie.class)
                .buildAndRegister();

    }
}
