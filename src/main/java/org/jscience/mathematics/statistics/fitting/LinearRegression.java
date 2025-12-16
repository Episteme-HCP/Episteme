package org.jscience.mathematics.statistics.fitting;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Linear regression: y = a + b*x (least squares).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class LinearRegression {

    private final double slope; // b
    private final double intercept; // a
    private final double rSquared; // R²
    private final double standardError;
    private final int n;

    private LinearRegression(double slope, double intercept, double rSquared,
            double standardError, int n) {
        this.slope = slope;
        this.intercept = intercept;
        this.rSquared = rSquared;
        this.standardError = standardError;
        this.n = n;
    }

    /**
     * Fits a linear model to the data.
     * 
     * @param x Independent variable values
     * @param y Dependent variable values
     * @return LinearRegression model
     */
    public static LinearRegression fit(double[] x, double[] y) {
        if (x.length != y.length || x.length < 2) {
            throw new IllegalArgumentException("Need at least 2 points with matching x and y");
        }

        int n = x.length;

        // Compute means
        double sumX = 0, sumY = 0;
        for (int i = 0; i < n; i++) {
            sumX += x[i];
            sumY += y[i];
        }
        double meanX = sumX / n;
        double meanY = sumY / n;

        // Compute slope and intercept
        double ssXX = 0, ssXY = 0, ssYY = 0;
        for (int i = 0; i < n; i++) {
            double dx = x[i] - meanX;
            double dy = y[i] - meanY;
            ssXX += dx * dx;
            ssXY += dx * dy;
            ssYY += dy * dy;
        }

        double slope = ssXY / ssXX;
        double intercept = meanY - slope * meanX;

        // R-squared
        double ssRes = 0;
        for (int i = 0; i < n; i++) {
            double predicted = intercept + slope * x[i];
            double residual = y[i] - predicted;
            ssRes += residual * residual;
        }
        double rSquared = 1.0 - (ssRes / ssYY);

        // Standard error of estimate
        double standardError = Math.sqrt(ssRes / (n - 2));

        return new LinearRegression(slope, intercept, rSquared, standardError, n);
    }

    /**
     * Fits linear model using Real types.
     */
    public static LinearRegression fit(Real[] x, Real[] y) {
        double[] xd = new double[x.length];
        double[] yd = new double[y.length];
        for (int i = 0; i < x.length; i++) {
            xd[i] = x[i].doubleValue();
            yd[i] = y[i].doubleValue();
        }
        return fit(xd, yd);
    }

    /**
     * Predicts y for given x.
     */
    public double predict(double x) {
        return intercept + slope * x;
    }

    public Real predict(Real x) {
        return Real.of(predict(x.doubleValue()));
    }

    // --- Accessors ---
    public double getSlope() {
        return slope;
    }

    public double getIntercept() {
        return intercept;
    }

    public double getRSquared() {
        return rSquared;
    }

    public double getStandardError() {
        return standardError;
    }

    public int getSampleSize() {
        return n;
    }

    @Override
    public String toString() {
        return String.format("y = %.6f + %.6f*x (R² = %.4f)", intercept, slope, rSquared);
    }
}
