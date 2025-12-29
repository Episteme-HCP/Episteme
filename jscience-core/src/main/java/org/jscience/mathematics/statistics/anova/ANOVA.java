/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.mathematics.statistics.anova;

import org.jscience.mathematics.numbers.real.Real;
import java.util.List;

/**
 * Analysis of Variance (ANOVA) - Tests for significant differences between
 * group means.
 * <p>
 * One-way ANOVA: tests if means of 3+ groups are equal.
 * F-statistic = (between-group variance) / (within-group variance)
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ANOVA {

    /**
     * Result of ANOVA test.
     */
    public static class ANOVAResult {
        public final Real fStatistic;
        public final int dfBetween; // degrees of freedom between groups
        public final int dfWithin; // degrees of freedom within groups
        public final Real ssBetween; // sum of squares between
        public final Real ssWithin; // sum of squares within
        public final Real msBetween; // mean square between
        public final Real msWithin; // mean square within

        public ANOVAResult(Real fStatistic, int dfBetween, int dfWithin,
                Real ssBetween, Real ssWithin, Real msBetween, Real msWithin) {
            this.fStatistic = fStatistic;
            this.dfBetween = dfBetween;
            this.dfWithin = dfWithin;
            this.ssBetween = ssBetween;
            this.ssWithin = ssWithin;
            this.msBetween = msBetween;
            this.msWithin = msWithin;
        }

        @Override
        public String toString() {
            return String.format("F(%d, %d) = %s, SS_between = %s, SS_within = %s",
                    dfBetween, dfWithin, fStatistic, ssBetween, ssWithin);
        }
    }

    /**
     * One-way ANOVA.
     * 
     * @param groups list of groups, each group is list of observations
     * @return ANOVA result with F-statistic and components
     */
    public static ANOVAResult oneWay(List<List<Real>> groups) {
        if (groups.size() < 2) {
            throw new IllegalArgumentException("Need at least 2 groups");
        }

        int k = groups.size(); // number of groups
        int n = 0; // total observations

        // Compute group means and sizes
        Real[] groupMeans = new Real[k];
        int[] groupSizes = new int[k];

        for (int i = 0; i < k; i++) {
            List<Real> group = groups.get(i);
            groupSizes[i] = group.size();
            n += groupSizes[i];

            Real sum = Real.ZERO;
            for (Real val : group) {
                sum = sum.add(val);
            }
            groupMeans[i] = sum.divide(Real.of(groupSizes[i]));
        }

        // Grand mean
        Real grandSum = Real.ZERO;
        for (int i = 0; i < k; i++) {
            grandSum = grandSum.add(groupMeans[i].multiply(Real.of(groupSizes[i])));
        }
        Real grandMean = grandSum.divide(Real.of(n));

        // Sum of squares between groups (SSB)
        Real ssBetween = Real.ZERO;
        for (int i = 0; i < k; i++) {
            Real diff = groupMeans[i].subtract(grandMean);
            ssBetween = ssBetween.add(diff.multiply(diff).multiply(Real.of(groupSizes[i])));
        }

        // Sum of squares within groups (SSW)
        Real ssWithin = Real.ZERO;
        for (int i = 0; i < k; i++) {
            for (Real val : groups.get(i)) {
                Real diff = val.subtract(groupMeans[i]);
                ssWithin = ssWithin.add(diff.multiply(diff));
            }
        }

        // Degrees of freedom
        int dfBetween = k - 1;
        int dfWithin = n - k;

        // Mean squares
        Real msBetween = ssBetween.divide(Real.of(dfBetween));
        Real msWithin = ssWithin.divide(Real.of(dfWithin));

        // F-statistic
        Real fStatistic = msBetween.divide(msWithin);

        return new ANOVAResult(fStatistic, dfBetween, dfWithin,
                ssBetween, ssWithin, msBetween, msWithin);
    }

    /**
     * Two-way ANOVA (simplified - balanced design).
     * <p>
     * Tests effects of two factors and their interaction.
     * </p>
     * 
     * @param data [factor1_levels][factor2_levels][replicates]
     * @return array of results: [factor1, factor2, interaction]
     */
    public static ANOVAResult[] twoWay(Real[][][] data) {
        int a = data.length; // levels of factor A
        int b = data[0].length; // levels of factor B
        int n = data[0][0].length; // replicates per cell

        // Cell means
        Real[][] cellMeans = new Real[a][b];
        for (int i = 0; i < a; i++) {
            for (int j = 0; j < b; j++) {
                Real sum = Real.ZERO;
                for (int k = 0; k < n; k++) {
                    sum = sum.add(data[i][j][k]);
                }
                cellMeans[i][j] = sum.divide(Real.of(n));
            }
        }

        // Factor A means
        Real[] factorAMeans = new Real[a];
        for (int i = 0; i < a; i++) {
            Real sum = Real.ZERO;
            for (int j = 0; j < b; j++) {
                sum = sum.add(cellMeans[i][j]);
            }
            factorAMeans[i] = sum.divide(Real.of(b));
        }

        // Factor B means
        Real[] factorBMeans = new Real[b];
        for (int j = 0; j < b; j++) {
            Real sum = Real.ZERO;
            for (int i = 0; i < a; i++) {
                sum = sum.add(cellMeans[i][j]);
            }
            factorBMeans[j] = sum.divide(Real.of(a));
        }

        // Grand mean
        Real grandSum = Real.ZERO;
        for (int i = 0; i < a; i++) {
            grandSum = grandSum.add(factorAMeans[i]);
        }
        Real grandMean = grandSum.divide(Real.of(a));

        // SS Factor A
        Real ssA = Real.ZERO;
        for (int i = 0; i < a; i++) {
            Real diff = factorAMeans[i].subtract(grandMean);
            ssA = ssA.add(diff.multiply(diff).multiply(Real.of(b * n)));
        }

        // SS Factor B
        Real ssB = Real.ZERO;
        for (int j = 0; j < b; j++) {
            Real diff = factorBMeans[j].subtract(grandMean);
            ssB = ssB.add(diff.multiply(diff).multiply(Real.of(a * n)));
        }

        // SS Interaction
        Real ssAB = Real.ZERO;
        for (int i = 0; i < a; i++) {
            for (int j = 0; j < b; j++) {
                Real expected = factorAMeans[i].add(factorBMeans[j]).subtract(grandMean);
                Real diff = cellMeans[i][j].subtract(expected);
                ssAB = ssAB.add(diff.multiply(diff).multiply(Real.of(n)));
            }
        }

        // SS Within (error)
        Real ssWithin = Real.ZERO;
        for (int i = 0; i < a; i++) {
            for (int j = 0; j < b; j++) {
                for (int k = 0; k < n; k++) {
                    Real diff = data[i][j][k].subtract(cellMeans[i][j]);
                    ssWithin = ssWithin.add(diff.multiply(diff));
                }
            }
        }

        // Degrees of freedom
        int dfA = a - 1;
        int dfB = b - 1;
        int dfAB = (a - 1) * (b - 1);
        int dfWithin = a * b * (n - 1);

        // Mean squares
        Real msA = ssA.divide(Real.of(dfA));
        Real msB = ssB.divide(Real.of(dfB));
        Real msAB = ssAB.divide(Real.of(dfAB));
        Real msWithin = ssWithin.divide(Real.of(dfWithin));

        // F-statistics
        Real fA = msA.divide(msWithin);
        Real fB = msB.divide(msWithin);
        Real fAB = msAB.divide(msWithin);

        return new ANOVAResult[] {
                new ANOVAResult(fA, dfA, dfWithin, ssA, ssWithin, msA, msWithin),
                new ANOVAResult(fB, dfB, dfWithin, ssB, ssWithin, msB, msWithin),
                new ANOVAResult(fAB, dfAB, dfWithin, ssAB, ssWithin, msAB, msWithin)
        };
    }
}
