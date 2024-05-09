package bruce.projectreflection.recipes.chemical;

import bruce.projectreflection.ProjectReflection;
import gregtech.api.unification.stack.MaterialStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Reaction {
    public static final List<Reaction> REGISTRY = new ArrayList<>();
    private static final double K = 1e-5;
    public List<MaterialStack> leftSide;
    public List<MaterialStack> rightSide;
    public double energy;//associated with total EU
    public int difficulty;//associated with voltage

    public Reaction(List<MaterialStack> leftSide, List<MaterialStack> rightSide, double energy, int difficulty) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.energy = energy;
        this.difficulty = difficulty;
    }

    public Reaction(MaterialStack[] leftSide, MaterialStack[] rightSide, double energy, int difficulty) {
        this(Arrays.asList(leftSide), Arrays.asList(rightSide), energy, difficulty);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Reaction))
            return false;
        return this.toString().equals(obj.toString());
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

    @Override
    public String toString() {
        Reaction normalized = normalize();
        StringBuilder sb = new StringBuilder();
        for (MaterialStack stack : normalized.leftSide) {
            sb.append(stack.material.getName() + "*" + stack.amount + "\n");
        }
        sb.append("->");
        for (MaterialStack stack : normalized.rightSide) {
            sb.append(stack.material.getName() + "*" + stack.amount + "\n");
        }
        sb.append(String.format("dH=%sEU/mol,dS=%sEU/(mol.K),%sEU/t", energy, getDs(), difficulty));
        return sb.toString();
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
        return new Reaction(newLeftSide, newRightSide, energy / normalizeFactor, difficulty);
    }

    public void register() {
        Reaction normalized = this.normalize();

        if (normalized.canHappen()) {
            ProjectReflection.logger.info("Registered recipe:{}", normalized);
            REGISTRY.add(normalized);
        }
        Reaction reverseReaction = normalized.getReverseReaction();
        if (reverseReaction.canHappen()) {
            ProjectReflection.logger.info("Registered recipe:{}", reverseReaction);
            REGISTRY.add(reverseReaction);
        }
    }

    public double getDh() {
        return energy;
    }

    public double getDs() {
        long leftAmount = 0;
        long rightAmount = 0;
        for (MaterialStack stack : leftSide) {
            leftAmount += stack.amount;
        }
        for (MaterialStack stack : rightSide) {
            rightAmount += stack.amount;
        }
        return K * (rightAmount - leftAmount);
    }

    public Reaction getReverseReaction() {
        return new Reaction(rightSide, leftSide, -energy, difficulty);
    }

    public boolean canHappen() {
        return !(getDh() > 0 && getDs() < 0);
    }

    public boolean canAlwaysHappen() {
        return getDh() < 0 && getDs() > 0;
    }

    public boolean canHappenAt(double temperature) {
        return (getDh() - temperature * getDs()) < 0;
    }
}
