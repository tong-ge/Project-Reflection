package bruce.projectreflection.api;

import bruce.projectreflection.ProjectReflection;
import bruce.projectreflection.recipes.chemical.Reaction;
import gregtech.api.GregTechAPI;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.stack.MaterialStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.client.model.obj.OBJModel;
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
        } else {
            stackList.put(materialStack.material, materialStack.amount);
        }
        //this.invalidate();
    }

    public long extractMaterial(MaterialStack materialStack, boolean simulate) {
        if (contains(materialStack.material)) {
            long contained = stackList.get(materialStack.material);
            long extracted = Math.min(contained, materialStack.amount);
            if (!simulate) {
                stackList.replace(materialStack.material, contained - extracted);
                //this.invalidate();
            }
            return extracted;
        }
        return 0;
    }

    public boolean contains(Material material) {
        return stackList.containsKey(material);
    }

    public boolean contains(MaterialStack materialStack) {
        return extractMaterial(materialStack, true) == materialStack.amount;
    }

    private boolean canReact(Reaction reaction, double temperature) {
        for (MaterialStack stack : reaction.leftSide) {
            if (!contains(stack)) {
                return false;
            }
        }
        return reaction.canHappenAt(temperature);
    }

    public Reaction[] findAllReactions(double temperature) {
        List<Reaction> reactions = new ArrayList<>();
        for (Reaction reaction : Reaction.REGISTRY) {
            if (canReact(reaction, temperature)) {
                reactions.add(reaction);
            }
        }
        return reactions.toArray(new Reaction[0]);
    }

    public double react(Reaction reaction) {
        for (MaterialStack reactant : reaction.leftSide) {
            if (this.contains(reactant)) {
                long extracted = this.extractMaterial(reactant, false);
                ProjectReflection.logger.info("extracted:{}*{}", extracted, Reaction.getChemicalFormulaOrName(reactant.material));
            } else {
                return 0;
            }
        }
        for (MaterialStack product : reaction.rightSide) {
            this.insertMaterial(product);
            ProjectReflection.logger.info("inserted:{}*{}", product.amount, Reaction.getChemicalFormulaOrName(product.material));
        }
        return reaction.getDh();
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

    public void invalidate() {
        Set<Material> materialSet = new HashSet<>(this.stackList.keySet());

        for (Material material : materialSet) {
            if (stackList.get(material) <= 0) {
                stackList.remove(material);
            }
        }
    }
}