package org.jscience.mathematics.dynamical;

/**
 * Represents the Logistic map: x_{n+1} = r * x_n * (1 - x_n).
 * A classic example of simple non-linear dynamics exhibiting chaos.
 */
public class LogisticMap implements DiscreteMap {

    private final double r;

    /**
     * Creates a Logistic map with parameter r.
     *
     * @param r the growth rate parameter (typically [0, 4])
     */
    public LogisticMap(double r) {
        this.r = r;
    }

    @Override
    public double[] map(double[] state) {
        if (state.length != 1) {
            throw new IllegalArgumentException("Logistic map is 1-dimensional");
        }
        double x = state[0];
        return new double[] { r * x * (1.0 - x) };
    }

    @Override
    public int dimensions() {
        return 1;
    }

    @Override
    public double hausdorffDimension() {
        // For r=4, dimension is 1. For other chaotic values, it's < 1.
        // This is a simplification.
        return 1.0;
    }
}
