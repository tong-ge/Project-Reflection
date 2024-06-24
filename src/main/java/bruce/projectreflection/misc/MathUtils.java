package bruce.projectreflection.misc;

import java.util.Random;

public final class MathUtils {
    public static double randbetween(Random rand, double min, double max) {
        return min + rand.nextDouble() * (max - min);
    }
}
