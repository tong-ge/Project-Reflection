package bruce.projectreflection.recipes.chemical;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.stack.MaterialStack;

import java.util.*;

public class Reaction {
    public static final Set<Reaction> REGISTRY = new HashSet<>();
    private static final double K = 1e-6;
    public List<MaterialStack> leftSide;
    public List<MaterialStack> rightSide;
    public double energy;//associated with total EU
    public double criticalTemperature;

    public Reaction(List<MaterialStack> leftSide, List<MaterialStack> rightSide, double energy, double criticalTemperature) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.energy = energy;
        this.criticalTemperature = criticalTemperature;
    }

    public Reaction(MaterialStack[] leftSide, MaterialStack[] rightSide, double energy, double criticalTemperature) {
        this(Arrays.asList(leftSide), Arrays.asList(rightSide), energy, criticalTemperature);
    }

    @Override
    public int hashCode() {
        return getReactionName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Reaction))
            return false;
        return this.getReactionName().equals(((Reaction) obj).getReactionName());
    }

    private static long gcd(long a, long b) {
        long c;
        while (b != 0) {
            c = a % b;
            a = b;
            b = c;
        }
        return a;
    }

    private String toStringNoEnergy() {
        Reaction normalized = normalize();
        StringBuilder sb = new StringBuilder();
        for (MaterialStack stack : normalized.leftSide) {
            sb.append(stack.material.getName() + "*" + stack.amount + "\n");
        }
        sb.append("->");
        for (MaterialStack stack : normalized.rightSide) {
            sb.append(stack.material.getName() + "*" + stack.amount + "\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return toStringNoEnergy() + String.format("dH=%sEU/mol@%sK", energy, getCriticalTemperature());
    }

    private Reaction normalize() {
        long normalizeFactor = leftSide.get(0).amount;
        for (MaterialStack stack : leftSide) {
            normalizeFactor = gcd(normalizeFactor, stack.amount);
        }
        for (MaterialStack stack : rightSide) {
            normalizeFactor = gcd(normalizeFactor, stack.amount);
        }
        List<MaterialStack> newLeftSide = new ArrayList<>();
        List<MaterialStack> newRightSide = new ArrayList<>();

        for (MaterialStack stack : leftSide) {
            newLeftSide.add(new MaterialStack(stack.material, stack.amount / normalizeFactor));
        }
        for (MaterialStack stack : rightSide) {
            newRightSide.add(new MaterialStack(stack.material, stack.amount / normalizeFactor));
        }
        return new Reaction(newLeftSide, newRightSide, energy / normalizeFactor, getCriticalTemperature());
    }

    private void registerInternal() {
        REGISTRY.removeIf(reaction -> reaction.getReactionName().equals(this.getReactionName())
                && Math.abs(reaction.energy) > Math.abs(this.energy));
        REGISTRY.add(this);
    }
    public void register() {
        Reaction normalized = this.normalize();

        if (normalized.canHappen()) {
            //ProjectReflection.logger.info("Registered recipe:{}", normalized);
            normalized.registerInternal();
        }
        Reaction reverseReaction = normalized.getReverseReaction();
        if (reverseReaction.canHappen()) {
            //ProjectReflection.logger.info("Registered recipe:{}", reverseReaction);
            reverseReaction.registerInternal();
        }
    }

    public double getDh() {
        return energy;
    }

    public Reaction getReverseReaction() {
        return new Reaction(rightSide, leftSide, -energy, criticalTemperature);
    }

    public boolean canHappen() {
        return energy < 0 || criticalTemperature > 0;
    }

    public boolean canAlwaysHappen() {

        return energy < 0 && criticalTemperature < 0;
    }

    public boolean canHappenAt(double temperature) {
        return getGibbsFreeEnergy(temperature) < 0;
    }

    public double getCriticalTemperature() {
        return this.criticalTemperature;
    }

    public double getGibbsFreeEnergy(double temperature) {

        return energy * (temperature - criticalTemperature);
    }

    public String getReactionEquation() {
        Reaction normalized = normalize();
        List<String> leftSide = new ArrayList<>();
        for (MaterialStack stack : normalized.leftSide) {
            leftSide.add(stack.amount + getChemicalFormulaOrName(stack.material));
        }
        List<String> rightSide = new ArrayList<>();
        for (MaterialStack stack : normalized.rightSide) {
            rightSide.add(stack.amount + getChemicalFormulaOrName(stack.material));
        }
        return String.join("+", leftSide) + "=" + String.join("+", rightSide);
    }

    public String getReactionName() {
        Reaction normalized = normalize();
        List<String> leftSide = new ArrayList<>();
        for (MaterialStack stack : normalized.leftSide) {
            leftSide.add(stack.amount + stack.material.getName());
        }
        List<String> rightSide = new ArrayList<>();
        for (MaterialStack stack : normalized.rightSide) {
            rightSide.add(stack.amount + stack.material.getName());
        }
        return String.join("+", leftSide) + "=" + String.join("+", rightSide);
    }

    public static String getChemicalFormulaOrName(Material material) {
        String formula = material.getChemicalFormula();
        return formula.isEmpty() ? material.getName() : formula;
    }
}
