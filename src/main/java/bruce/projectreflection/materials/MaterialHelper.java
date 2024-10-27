package bruce.projectreflection.materials;

import bruce.projectreflection.PRConfig;
import bruce.projectreflection.PRConstants;
import bruce.projectreflection.PRLabs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meteor.extrabotany.common.block.fluid.ModFluid;
import gregtech.api.GregTechAPI;
import gregtech.api.fluids.FluidBuilder;
import gregtech.api.fluids.FluidState;
import gregtech.api.fluids.store.FluidStorageKeys;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.registry.MaterialRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;
import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;

public class MaterialHelper {
    private static HashMap<String, Integer> idMap;

    private static int id = 1;
    private static final File idCache = new File(Loader.instance().getConfigDir(), "projectreflection_id_map.json");

    private static int getNextAvailableId(String modid) {
        MaterialRegistry registry = GregTechAPI.materialManager.getRegistry(modid);
        while (id < 32000) {
            Material material = registry.getObjectById(id);
            if (material == null) {
                return id;
            }
            id++;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    private static int retrieveIdForName(ResourceLocation name) {
        Integer id = idMap.get(name.toString());
        if (id == null) {
            id = getNextAvailableId(name.getNamespace());
            idMap.put(name.toString(), id);
        }
        return id;
    }

    public static Material.Builder dynamicBuilder(String modid, String name, boolean autoFluid, @Nullable FluidBuilder fallback, boolean gaseous) {
        return dynamicBuilder(new ResourceLocation(modid, name), autoFluid, fallback, gaseous);
    }

    public static Material.Builder dynamicBuilder(ResourceLocation name, boolean autoFluid, @Nullable FluidBuilder fallback, boolean gaseous) {
        Material.Builder builder = new Material.Builder(retrieveIdForName(name), name);
        Fluid fluid = FluidRegistry.getFluid(name.getPath());
        if (autoFluid) {
            if (fluid != null) {
                System.out.println("Registering material for existing fluid:" + name);
                builder.fluid(fluid,
                        fluid.isGaseous() ? FluidStorageKeys.GAS : FluidStorageKeys.LIQUID,
                        fluid.isGaseous() ? FluidState.GAS : FluidState.LIQUID);
            } else if (fallback != null) {
                System.out.println("Registering material with new fluid:" + name);
                builder.fluid(gaseous ? FluidStorageKeys.GAS : FluidStorageKeys.LIQUID, fallback);
            }
        }
        return builder;
    }

    public static Material.Builder dynamicBuilder(String name, boolean autoFluid, int temperature, boolean gaseous) {
        return dynamicBuilder(PRConstants.modid, name, autoFluid,
                new FluidBuilder().temperature(temperature).state(gaseous ? FluidState.GAS : FluidState.LIQUID),
                gaseous);
    }

    public static Material.Builder dynamicBuilder(String name, boolean autoFluid, boolean gaseous) {
        return dynamicBuilder(PRConstants.modid, name, autoFluid,
                new FluidBuilder().state(gaseous ? FluidState.GAS : FluidState.LIQUID),
                gaseous);
    }
    public static Material.Builder dynamicBuilder(String name) {
        return dynamicBuilder(name, false, false);
    }

    public static void init() {
        if (PRConfig.debug) {
            System.out.println("Registering materials");
        }
        ModFluid.init();
    }


    public static void orePrefix() {
        try (Writer writer = new FileWriter(idCache)) {
            Gson gson = new Gson();
            gson.toJson(idMap, writer);
            if (PRConfig.debug) {
                System.out.printf("数据已保存到文件：%s\n", idCache);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initIDMap() {
        if (!idCache.exists()) {
            idMap = new HashMap<>();
        } else {
            try (Reader reader = new FileReader(idCache)) {
                Gson gson = PRConstants.gson;
                Type type = new TypeToken<HashMap<String, Integer>>() {
                }.getType();
                idMap = gson.fromJson(reader, type);  // 从文件中读取 JSON 并转换为 HashMap
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
