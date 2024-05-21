package bruce.projectreflection.misc;

import bruce.projectreflection.PRConstants;

public class BrightnessHelper {
    //private static final double omega=10.0*Math.PI;
    private static final double DEG = Math.PI / 180.0;

    public static double wiggle(double freq, double depth) {
        double t = System.nanoTime() / 1000000000.0;
        double omega = 5.0 * Math.PI * freq;
        double phaseNoise = DEG * PRConstants.rand.nextDouble();
        return depth + Math.sin(omega * t + phaseNoise) * depth;
    }
}
