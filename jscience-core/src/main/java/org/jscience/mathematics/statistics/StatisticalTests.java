package org.jscience.mathematics.statistics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.List;

/**
 * Advanced statistical methods and tests.
 * <p>
 * Hypothesis testing, correlation, regression.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class StatisticalTests {

    /**
     * Pearson correlation coefficient: measures linear relationship [-1, 1].
     */
    public static Real pearsonCorrelation(List<Real> x, List<Real> y) {
        if (x.size() != y.size() || x.isEmpty()) {
            throw new IllegalArgumentException("Lists must have same non-zero length");
        }

        int n = x.size();
        Real meanX = mean(x);
        Real meanY = mean(y);

        Real numerator = Real.ZERO;
        Real sumSqX = Real.ZERO;
        Real sumSqY = Real.ZERO;

        for (int i = 0; i < n; i++) {
            Real dx = x.get(i).subtract(meanX);
            Real dy = y.get(i).subtract(meanY);
            numerator = numerator.add(dx.multiply(dy));
            sumSqX = sumSqX.add(dx.multiply(dx));
            sumSqY = sumSqY.add(dy.multiply(dy));
        }

        Real denominator = sumSqX.multiply(sumSqY).sqrt();
        return numerator.divide(denominator);
    }

    /**
     * Linear regression: y = a + bx. Returns [intercept, slope].
     */
    public static Real[] linearRegression(List<Real> x, List<Real> y) {
        if (x.size() != y.size() || x.isEmpty()) {
            throw new IllegalArgumentException("Lists must have same non-zero length");
        }

        int n = x.size();
        Real meanX = mean(x);
        Real meanY = mean(y);

        Real numerator = Real.ZERO;
        Real denominator = Real.ZERO;

        for (int i = 0; i < n; i++) {
            Real dx = x.get(i).subtract(meanX);
            numerator = numerator.add(dx.multiply(y.get(i).subtract(meanY)));
            denominator = denominator.add(dx.multiply(dx));
        }

        Real slope = numerator.divide(denominator);
        Real intercept = meanY.subtract(slope.multiply(meanX));

        return new Real[] { intercept, slope };
    }

    /**
     * Coefficient of determination R²: proportion of variance explained.
     */
    public static Real rSquared(List<Real> x, List<Real> y) {
        Real[] regression = linearRegression(x, y);
        Real intercept = regression[0];
        Real slope = regression[1];

        Real meanY = mean(y);
        Real ssTotal = Real.ZERO;
        Real ssResidual = Real.ZERO;

        for (int i = 0; i < y.size(); i++) {
            Real predicted = intercept.add(slope.multiply(x.get(i)));
            Real residual = y.get(i).subtract(predicted);
            Real totalDev = y.get(i).subtract(meanY);

            ssResidual = ssResidual.add(residual.multiply(residual));
            ssTotal = ssTotal.add(totalDev.multiply(totalDev));
        }

        return Real.ONE.subtract(ssResidual.divide(ssTotal));
    }

    /**
     * Student's t-test for independent samples.
     * Returns t-statistic.
     */
    public static Real tTest(List<Real> sample1, List<Real> sample2) {
        Real mean1 = mean(sample1);
        Real mean2 = mean(sample2);
        Real var1 = variance(sample1);
        Real var2 = variance(sample2);

        int n1 = sample1.size();
        int n2 = sample2.size();

        Real pooledVariance = var1.divide(Real.of(n1)).add(var2.divide(Real.of(n2)));
        Real t = mean1.subtract(mean2).divide(pooledVariance.sqrt());

        return t;
    }

    /**
     * Chi-squared test for goodness of fit.
     * Returns chi-squared statistic.
     */
    public static Real chiSquaredTest(List<Real> observed, List<Real> expected) {
        if (observed.size() != expected.size()) {
            throw new IllegalArgumentException("Lists must have same length");
        }

        Real chiSquared = Real.ZERO;

        for (int i = 0; i < observed.size(); i++) {
            Real diff = observed.get(i).subtract(expected.get(i));
            chiSquared = chiSquared.add(diff.multiply(diff).divide(expected.get(i)));
        }

        return chiSquared;
    }

    /**
     * Covariance between two datasets.
     */
    public static Real covariance(List<Real> x, List<Real> y) {
        if (x.size() != y.size() || x.isEmpty()) {
            throw new IllegalArgumentException("Lists must have same non-zero length");
        }

        Real meanX = mean(x);
        Real meanY = mean(y);
        Real sum = Real.ZERO;

        for (int i = 0; i < x.size(); i++) {
            sum = sum.add(x.get(i).subtract(meanX).multiply(y.get(i).subtract(meanY)));
        }

        return sum.divide(Real.of(x.size() - 1));
    }

    // Helper methods
    private static Real mean(List<Real> data) {
        Real sum = Real.ZERO;
        for (Real value : data) {
            sum = sum.add(value);
        }
        return sum.divide(Real.of(data.size()));
    }

    private static Real variance(List<Real> data) {
        Real m = mean(data);
        Real sum = Real.ZERO;

        for (Real value : data) {
            Real diff = value.subtract(m);
            sum = sum.add(diff.multiply(diff));
        }

        return sum.divide(Real.of(data.size() - 1));
    }

    /**
     * Standard deviation.
     */
    public static Real standardDeviation(List<Real> data) {
        return variance(data).sqrt();
    }

    /**
     * One-Way Analysis of Variance (ANOVA).
     * Returns the F-statistic.
     * 
     * @param groups list of sample groups
     * @return F-statistic
     */
    public static Real oneWayAnova(List<List<Real>> groups) {
        if (groups.size() < 2) {
            throw new IllegalArgumentException("ANOVA requires at least two groups");
        }

        int k = groups.size();
        int nTotal = 0;
        Real grandSum = Real.ZERO;

        // Calculate Grand Mean
        for (List<Real> group : groups) {
            nTotal += group.size();
            for (Real val : group) {
                grandSum = grandSum.add(val);
            }
        }
        Real grandMean = grandSum.divide(Real.of(nTotal));

        Real ssb = Real.ZERO;
        Real ssw = Real.ZERO;

        for (List<Real> group : groups) {
            int ni = group.size();
            Real groupSum = Real.ZERO;
            for (Real val : group) {
                groupSum = groupSum.add(val);
            }
            Real groupMean = groupSum.divide(Real.of(ni));

            // Sum of Squares Between: Σ ni * (mean_i - grandMean)^2
            Real diffBetween = groupMean.subtract(grandMean);
            ssb = ssb.add(Real.of(ni).multiply(diffBetween.multiply(diffBetween)));

            // Sum of Squares Within: Σ Σ (val_ij - mean_i)^2
            for (Real val : group) {
                Real diffWithin = val.subtract(groupMean);
                ssw = ssw.add(diffWithin.multiply(diffWithin));
            }
        }

        int dfBetween = k - 1;
        int dfWithin = nTotal - k;

        if (dfWithin <= 0) {
            throw new IllegalArgumentException("Not enough degrees of freedom for Within usage");
        }

        Real msb = ssb.divide(Real.of(dfBetween));
        Real msw = ssw.divide(Real.of(dfWithin));

        return msb.divide(msw);
    }
}
