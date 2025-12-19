package org.jscience.computing.ai.fuzzy;

/**
 * Basic fuzzy logic engine.
 */
public class FuzzyLogic {

    private FuzzyLogic() {
    }

    /**
     * Triangular membership function.
     * 
     * @param x Input value
     * @param a Left foot
     * @param b Peak
     * @param c Right foot
     * @return Membership degree [0, 1]
     */
    public static double triangular(double x, double a, double b, double c) {
        if (x <= a || x >= c)
            return 0;
        if (x == b)
            return 1;
        if (x < b)
            return (x - a) / (b - a);
        return (c - x) / (c - b);
    }

    /**
     * Trapezoidal membership function.
     * 
     * @param x Input value
     * @param a Left foot
     * @param b Left shoulder
     * @param c Right shoulder
     * @param d Right foot
     */
    public static double trapezoidal(double x, double a, double b, double c, double d) {
        if (x <= a || x >= d)
            return 0;
        if (x >= b && x <= c)
            return 1;
        if (x < b)
            return (x - a) / (b - a);
        return (d - x) / (d - c);
    }

    /**
     * Gaussian membership function.
     * μ(x) = exp(-(x-c)²/(2σ²))
     */
    public static double gaussian(double x, double center, double sigma) {
        return Math.exp(-Math.pow(x - center, 2) / (2 * sigma * sigma));
    }

    /**
     * Fuzzy AND (T-norm: minimum).
     */
    public static double and(double a, double b) {
        return Math.min(a, b);
    }

    /**
     * Fuzzy OR (S-norm: maximum).
     */
    public static double or(double a, double b) {
        return Math.max(a, b);
    }

    /**
     * Fuzzy NOT (complement).
     */
    public static double not(double a) {
        return 1.0 - a;
    }

    /**
     * Product T-norm.
     */
    public static double productAnd(double a, double b) {
        return a * b;
    }

    /**
     * Probabilistic OR (S-norm).
     */
    public static double probabilisticOr(double a, double b) {
        return a + b - a * b;
    }

    /**
     * Centroid defuzzification (for discrete output).
     * 
     * @param values      Output values
     * @param memberships Corresponding membership degrees
     */
    public static double centroidDefuzzification(double[] values, double[] memberships) {
        double numerator = 0;
        double denominator = 0;
        for (int i = 0; i < values.length; i++) {
            numerator += values[i] * memberships[i];
            denominator += memberships[i];
        }
        return denominator == 0 ? 0 : numerator / denominator;
    }
}
