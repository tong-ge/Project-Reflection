package bruce.projectreflection.blocks;

import bruce.projectreflection.PRConstants;
import gregtech.api.unification.material.Materials;
import gregtech.api.util.GTUtility;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;

public class BlockSteam extends BlockFluidFinite {
    public static final BlockSteam INSTANCE = new BlockSteam(Materials.Steam.getFluid(),
            new MaterialLiquid(GTUtility.getMapColor(Materials.Steam.getMaterialRGB())));

    static {
        INSTANCE.setRegistryName(PRConstants.modid, "fluid.steam");
    }

    private BlockSteam(Fluid fluid, Material material) {
        super(fluid, material);
        this.setDensity(fluid.getDensity());
        this.setTemperature(fluid.getTemperature());
        this.setMaxScaledLight(fluid.getLuminosity());
        this.setTickRate(Math.max(20, fluid.getViscosity() / 200));
    }
}
