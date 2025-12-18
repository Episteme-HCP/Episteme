package org.jscience.mathematics.analysis;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Provides numerical and symbolic differentiation capabilities.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class Differentiation {

    private Differentiation() {
        // Utility class
    }

    /**
     * Returns the derivative of the given function.
     * <p>
     * If the function implements its own {@link RealFunction#derivative()} method,
     * it is used. Otherwise, a numerical differentiation wrapper is returned.
     * </p>
     * 
     * @param f the function to differentiate
     * @return the derivative function f'
     */
    public static RealFunction of(RealFunction f) {
        return f.derivative();
    }

    /**
     * Computes the numerical derivative of a function at a specific point using
     * Ridders' method of polynomial extrapolation for high accuracy.
     * 
     * @param f the function
     * @param x the point at which to evaluate the derivative
     * @return the approximate value of f'(x)
     */
    public static Real derivativeAt(RealFunction f, Real x) {
        // Ridders' method implementation
        // Initial step size
        double h = 0.01;
        double xVal = x.doubleValue();

        // Maximum number of iterations
        int n = 10;
        double[][] d = new double[n][n];

        // Initial estimates using central difference
        for (int i = 0; i < n; i++) {
            double fPlus = f.evaluate(Real.of(xVal + h)).doubleValue();
            double fMinus = f.evaluate(Real.of(xVal - h)).doubleValue();
            d[i][0] = (fPlus - fMinus) / (2 * h);
            h /= 2.0;
        }

        // Extrapolation
        for (int j = 1; j < n; j++) {
            for (int i = 0; i < n - j; i++) {
                // Richardson extrapolation formula
                double factor = Math.pow(4, j);
                d[i][j] = (d[i + 1][j - 1] * factor - d[i][j - 1]) / (factor - 1);
            }
        }

        // The best estimate is usually the one with the smallest error estimate,
        // but for simplicity here we return the top of the triangle which corresponds
        // to the smallest step size extrapolated.
        // A more robust implementation would track error.

        // For now, return the last extrapolated value which should be most accurate
        // assuming well-behaved function.
        return Real.of(d[0][n - 1]);
    }
}
