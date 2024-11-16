package bruce.projectreflection.misc;

import bruce.projectreflection.PRConfig;
import bruce.projectreflection.PRConstants;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gregtech.api.util.GTControlledRegistry;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * @author tong-ge
 * Unified dynamic registry handler
 */
public class DynamicRegistryHandler<K, V> {
    private int currentId;
    private final int endId;
    private HashBiMap<K, Integer> idMap;
    private final GTControlledRegistry<K, V> registry;
    private final File idCache;

    public DynamicRegistryHandler(GTControlledRegistry<K, V> registry, int startId, int endId, File idCache) {
        this.registry = registry;
        this.currentId = startId;
        this.endId = endId;
        this.idCache = idCache;
        if (idCache.exists()) {
            try (Reader reader = new FileReader(idCache)) {
                Gson gson = PRConstants.gson;
                Type type = new TypeToken<HashMap<K, Integer>>() {
                }.getType();
                idMap = HashBiMap.create(gson.fromJson(reader, type));  // 从文件中读取 JSON 并转换为 HashMap
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (idMap == null) idMap = HashBiMap.create();
    }

    private int getNextAvailableId() {
        while (currentId < endId) {
            V material = registry.getObjectById(currentId);
            if (material == null) {
                return currentId;
            }
            currentId++;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int retrieveIdForName(K name) {
        Integer id = idMap.get(name);
        if (id == null) {
            id = getNextAvailableId();
            idMap.put(name, id);
        }
        return id;
    }

    public void writeRegistryToFile() {
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
}
