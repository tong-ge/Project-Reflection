package bruce.projectreflection.api;

import gregtech.api.GregTechAPI;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.stack.MaterialStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MaterialContainer implements Iterable<MaterialStack> {
    private final Map<Material, Long> stackList = new HashMap<>();

    public MaterialContainer() {
    }

    public NBTTagList writeToNBT() {
        //data = super.writeToNBT(data);
        NBTTagList stackListNbt = new NBTTagList();
        for (Map.Entry<Material, Long> entry : stackList.entrySet()) {
            NBTTagCompound stackNbt = new NBTTagCompound();
            stackNbt.setString("Material", entry.getKey().getName());
            stackNbt.setLong("Amount", entry.getValue());
            stackListNbt.appendTag(stackNbt);
        }
        return stackListNbt;
    }

    public void readFromNBT(NBTTagList data) {
        //super.readFromNBT(data);
        stackList.clear();
        //NBTTagList stackListNbt = data.getTagList("Stacks", Constants.NBT.TAG_COMPOUND);
        for (NBTBase nbt : data) {
            if (nbt instanceof NBTTagCompound) {
                String materialName = ((NBTTagCompound) nbt).getString("Material");
                long amount = ((NBTTagCompound) nbt).getLong("Amount");
                Material material = GregTechAPI.materialManager.getMaterial(materialName);
                if (material != null) {
                    stackList.put(material, amount);
                }
            }
        }
    }

    public void insertMaterial(MaterialStack materialStack) {
        if (stackList.containsKey(materialStack.material)) {
            long old = stackList.get(materialStack.material);
            long old2 = stackList.replace(materialStack.material, old + materialStack.amount);
            assert old2 == old;
        } else stackList.put(materialStack.material, materialStack.amount);
    }

    public long extractMaterial(MaterialStack materialStack, boolean simulate) {
        if (stackList.containsKey(materialStack.material)) {
            long contained = stackList.get(materialStack.material);
            long extracted = Math.min(contained, materialStack.amount);
            if (!simulate) {
                stackList.replace(materialStack.material, contained - extracted);
            }
            return extracted;
        }
        return 0;
    }

    @NotNull
    @Override
    public Iterator<MaterialStack> iterator() {
        List<MaterialStack> stackList1 = new ArrayList<>();
        for (Map.Entry<Material, Long> entry : stackList.entrySet()) {
            stackList1.add(new MaterialStack(entry.getKey(), entry.getValue()));
        }
        return stackList1.iterator();
    }
}