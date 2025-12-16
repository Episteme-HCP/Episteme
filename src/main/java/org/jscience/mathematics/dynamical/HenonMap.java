package org.jscience.mathematics.dynamical;

/**
 * Represents the Hénon map:
 * x_{n+1} = 1 - a * x_n^2 + y_n
 * y_{n+1} = b * x_n
 * 
 * A discrete-time dynamical system that exhibits a strange attractor.
 */
public class HenonMap implements DiscreteMap {

    private final double a;
    private final double b;

    /**
     * Creates a Hénon map with standard parameters a=1.4, b=0.3.
     */
    public HenonMap() {
        this(1.4, 0.3);
    }

    /**
     * Creates a Hénon map with specified parameters.
     *
     * @param a parameter a
     * @param b parameter b
     */
    public HenonMap(double a, double b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public double[] map(double[] state) {
        if (state.length != 2) {
            throw new IllegalArgumentException("Hénon map is 2-dimensional");
        }
        double x = state[0];
        double y = state[1];

        double xNext = 1.0 - a * x * x + y;
        double yNext = b * x;

        return new double[] { xNext, yNext };
    }

    @Override
    public int dimensions() {
        return 2;
    }

    @Override
    public double hausdorffDimension() {
        // Approximation for standard parameters a=1.4, b=0.3
        return 1.26;
    }
}
