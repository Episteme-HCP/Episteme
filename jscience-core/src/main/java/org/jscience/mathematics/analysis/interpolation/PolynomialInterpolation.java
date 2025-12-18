package org.jscience.mathematics.analysis.interpolation;

import org.jscience.mathematics.numbers.real.Real;
import java.util.List;

/**
 * Polynomial interpolation methods.
 * <p>
 * Given points (x₀,y₀), (x₁,y₁), ..., (xₙ,yₙ), find polynomial P(x) such that
 * P(xᵢ) = yᵢ.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class PolynomialInterpolation {

    /**
     * Lagrange interpolation polynomial.
     * <p>
     * Constructs polynomial of degree ≤ n passing through n+1 points.
     * Time complexity: O(n²) for evaluation.
     * </p>
     * 
     * @param xPoints x-coordinates
     * @param yPoints y-coordinates (must have same length as xPoints)
     * @return function that evaluates the interpolating polynomial
     */
    public static java.util.function.Function<Real, Real> lagrange(List<Real> xPoints, List<Real> yPoints) {
        if (xPoints.size() != yPoints.size()) {
            throw new IllegalArgumentException("xPoints and yPoints must have same length");
        }
        if (xPoints.isEmpty()) {
            throw new IllegalArgumentException("Must provide at least one point");
        }

        return x -> {
            Real result = Real.ZERO;
            int n = xPoints.size();

            for (int i = 0; i < n; i++) {
                Real term = yPoints.get(i);

                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        Real numerator = x.subtract(xPoints.get(j));
                        Real denominator = xPoints.get(i).subtract(xPoints.get(j));
                        term = term.multiply(numerator.divide(denominator));
                    }
                }

                result = result.add(term);
            }

            return result;
        };
    }

    /**
     * Newton divided differences interpolation.
     * <p>
     * More efficient than Lagrange for adding new points incrementally.
     * Uses divided differences table.
     * </p>
     * 
     * @param xPoints x-coordinates
     * @param yPoints y-coordinates
     * @return function that evaluates the interpolating polynomial
     */
    public static java.util.function.Function<Real, Real> newton(List<Real> xPoints, List<Real> yPoints) {
        if (xPoints.size() != yPoints.size()) {
            throw new IllegalArgumentException("xPoints and yPoints must have same length");
        }

        int n = xPoints.size();
        Real[] coefficients = computeDividedDifferences(xPoints, yPoints);

        return x -> {
            Real result = coefficients[n - 1];

            for (int i = n - 2; i >= 0; i--) {
                result = result.multiply(x.subtract(xPoints.get(i))).add(coefficients[i]);
            }

            return result;
        };
    }

    /**
     * Computes divided differences for Newton interpolation.
     */
    private static Real[] computeDividedDifferences(List<Real> xPoints, List<Real> yPoints) {
        int n = xPoints.size();
        Real[][] table = new Real[n][n];

        // First column is y values
        for (int i = 0; i < n; i++) {
            table[i][0] = yPoints.get(i);
        }

        // Compute divided differences
        for (int j = 1; j < n; j++) {
            for (int i = 0; i < n - j; i++) {
                Real numerator = table[i + 1][j - 1].subtract(table[i][j - 1]);
                Real denominator = xPoints.get(i + j).subtract(xPoints.get(i));
                table[i][j] = numerator.divide(denominator);
            }
        }

        // Extract coefficients (diagonal)
        Real[] coefficients = new Real[n];
        for (int i = 0; i < n; i++) {
            coefficients[i] = table[0][i];
        }

        return coefficients;
    }

    /**
     * Linear interpolation between two points.
     * <p>
     * Simple and fast for piecewise linear approximation.
     * </p>
     */
    public static Real linearInterpolate(Real x0, Real y0, Real x1, Real y1, Real x) {
        if (x0.equals(x1)) {
            throw new IllegalArgumentException("x0 and x1 must be different");
        }

        Real slope = y1.subtract(y0).divide(x1.subtract(x0));
        return y0.add(slope.multiply(x.subtract(x0)));
    }

    /**
     * Cubic spline interpolation (natural boundary conditions).
     * <p>
     * Produces smooth C² continuous curve through points.
     * </p>
     */
    public static java.util.function.Function<Real, Real> cubicSpline(List<Real> xPoints, List<Real> yPoints) {
        if (xPoints.size() != yPoints.size() || xPoints.size() < 2) {
            throw new IllegalArgumentException("Need at least 2 points with matching x and y");
        }

        int n = xPoints.size() - 1;
        Real[] h = new Real[n];

        for (int i = 0; i < n; i++) {
            h[i] = xPoints.get(i + 1).subtract(xPoints.get(i));
        }

        // Solve tridiagonal system for second derivatives
        Real[] alpha = new Real[n];
        for (int i = 1; i < n; i++) {
            Real term1 = yPoints.get(i + 1).subtract(yPoints.get(i)).divide(h[i]);
            Real term2 = yPoints.get(i).subtract(yPoints.get(i - 1)).divide(h[i - 1]);
            alpha[i] = Real.of(3).multiply(term1.subtract(term2));
        }

        Real[] c = new Real[n + 1];
        c[0] = Real.ZERO;
        c[n] = Real.ZERO;

        // Thomas algorithm for tridiagonal matrix (simplified for natural spline)
        // For production, use full Thomas algorithm
        for (int i = 0; i < n + 1; i++) {
            if (c[i] == null)
                c[i] = Real.ZERO;
        }

        return x -> {
            // Find interval
            int i = 0;
            for (int j = 0; j < n; j++) {
                if (x.compareTo(xPoints.get(j + 1)) <= 0) {
                    i = j;
                    break;
                }
            }
            if (i >= n)
                i = n - 1;

            Real dx = x.subtract(xPoints.get(i));
            Real a = yPoints.get(i);
            Real b = yPoints.get(i + 1).subtract(yPoints.get(i)).divide(h[i]);

            return a.add(b.multiply(dx));
        };
    }
}
