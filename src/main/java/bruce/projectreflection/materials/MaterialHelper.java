package bruce.projectreflection.materials;

import bruce.projectreflection.PRConfig;
import bruce.projectreflection.PRConstants;
import bruce.projectreflection.PRLabs;
import bruce.projectreflection.materials.properties.PropertyArmor;
import bruce.projectreflection.misc.DynamicRegistryHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meteor.extrabotany.common.block.fluid.ModFluid;
import gregtech.api.GregTechAPI;
import gregtech.api.fluids.FluidBuilder;
import gregtech.api.fluids.FluidState;
import gregtech.api.fluids.store.FluidStorageKeys;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
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
    //private static HashMap<String, Integer> idMap;
    private static DynamicRegistryHandler<String, Material> registryHandler;

    //private static int id = 22500;//FREE RANGE
    //private static final File idCache = new File(Loader.instance().getConfigDir(), "projectreflection_id_map.json");
/*
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
*/
    private static int retrieveIdForName(ResourceLocation name) {
        /*
        Integer id = idMap.get(name.toString());
        if (id == null) {
            id = getNextAvailableId(name.getNamespace());
            idMap.put(name.toString(), id);
        }
        return id;
         */
        if (!PRConstants.modid.equals(name.getNamespace())) {
            throw new IllegalArgumentException("only supports projectreflection materials");
        }
        return registryHandler.retrieveIdForName(name.toString());
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
        initIDMap();
    }


    public static void orePrefix() {
        /*
        try (Writer writer = new FileWriter(idCache)) {
            Gson gson = new Gson();
            gson.toJson(idMap, writer);
            if (PRConfig.debug) {
                System.out.printf("数据已保存到文件：%s\n", idCache);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
         */
        registryHandler.writeRegistryToFile();

        Materials.Neutronium.setProperty(PropertyArmor.KEY, new PropertyArmor(1024, 65535, 5));
        Materials.Iron.setProperty(PropertyArmor.KEY, new PropertyArmor(new double[]{2.0, 5.0, 6.0, 2.0}, new int[]{195, 225, 240, 165}).setEnchantability(9));
        Materials.Gold.setProperty(PropertyArmor.KEY, new PropertyArmor(new double[]{1.0, 3.0, 5.0, 2.0}, new int[]{91, 105, 112, 77}).setEnchantability(25));
        //Materials.HSSE.setProperty(PropertyArmor.KEY,new PropertyArmor(20,3072,3).setEnchantability(20));
    }

    public static void initIDMap() {
        /*
        if (idCache.exists()) {
            try (Reader reader = new FileReader(idCache)) {
                Gson gson = PRConstants.gson;
                Type type = new TypeToken<HashMap<String, Integer>>() {
                }.getType();
                idMap = gson.fromJson(reader, type);  // 从文件中读取 JSON 并转换为 HashMap
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (idMap == null) idMap = new HashMap<>();
         */
        registryHandler = new DynamicRegistryHandler<>(GregTechAPI.materialManager.getRegistry(PRConstants.modid),
                22050, 32000,
                new File(Loader.instance().getConfigDir(), "projectreflection_material_id_map.json"));
    }
}
