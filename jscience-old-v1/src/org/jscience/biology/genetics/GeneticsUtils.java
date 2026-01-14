/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.biology.genetics;

import org.jscience.mathematics.MathUtils;
import org.jscience.mathematics.algebraic.matrices.DoubleVector;


/**
 * A class representing some useful methods for population genetics.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class GeneticsUtils extends Object {
    /**
     * The Ewens's sampling formula, see
     * http://en.wikipedia.org/wiki/Ewens%27s_sampling_formula omega must be
     * positive although this is unchecked Elements of the vector must be non
     * negative numbers although this is unchecked
     *
     * @param omega DOCUMENT ME!
     * @param vector DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //due to the fact that this formula relies heavily on factorial, may be we should use exact numbers
    public final static double getEwenSampling(int omega, DoubleVector vector) {
        int i;
        int n;
        double omegaSum;
        double omegaProduct;

        omegaSum = 1;

        for (i = omega; i < (omega + vector.getDimension()); i++) {
            omegaSum = omegaSum * i;
        }

        omegaProduct = 1;

        for (i = 0; i < vector.getDimension(); i++) {
            omegaProduct = omegaProduct * (Math.pow(omega,
                    vector.getPrimitiveElement(i)) / (Math.pow(i + 1,
                    vector.getPrimitiveElement(i)) * MathUtils.factorial(vector.getPrimitiveElement(
                        i))));
        }

        return (MathUtils.factorial(vector.getDimension()) * omegaProduct) / omegaSum;
    }

    //http://en.wikipedia.org/wiki/Effective_population_size
    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     * @param N DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final static double getAlleleFrequencyVariance(double p, double N) {
        return (p * (1 - p)) / (2 * N);
    }

    //effective population size Ne
    /**
     * DOCUMENT ME!
     *
     * @param ft DOCUMENT ME!
     * @param ft1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final static double getInbreedingEffictiveSize(double ft, double ft1) {
        return (1 - ft) / (2 * (ft1 - ft));
    }

    /**
     * DOCUMENT ME!
     *
     * @param ft1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final static double getInbreedingEffictiveSize(double ft1) {
        return 1 / (2 * ft1);
    }

    //Suppose there are t non-overlapping generations, then effective population size is given by the harmonic mean of the population sizes
    /**
     * DOCUMENT ME!
     *
     * @param populationSizes DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final static double getEffectivePopulationSize(
        double[] populationSizes) {
        double sum;

        sum = 0;

        for (int i = 0; i < populationSizes.length; i++) {
            sum = sum + (1 / populationSizes[i]);
        }

        return populationSizes.length / sum;
    }

    //Dioeciousness
    /**
     * DOCUMENT ME!
     *
     * @param Nm DOCUMENT ME!
     * @param Nf DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final static double getEffectivePopulationSize(double Nm, double Nf) {
        return (4 * (Nm * Nf)) / (Nm + Nf);
    }

    //Unequal contributions to the next generation
    /**
     * DOCUMENT ME!
     *
     * @param genotypeKey DOCUMENT ME!
     * @param allelesfrequencies DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final static double getGenotypeFrequency(double[] genotypeKey,
        double[] allelesfrequencies) {
        double ploidy;
        double numalleles;
        double[] coefficients;
        double value;

        ploidy = genotypeKey.length;
        numalleles = allelesfrequencies.length;
        coefficients = new double[allelesfrequencies.length];

        for (int i = 0; i < allelesfrequencies.length; i++) {
            for (int j = 0; j < genotypeKey.length; j++) {
                if ((genotypeKey[j] < 0) || (genotypeKey[j] >= numalleles)) {
                    throw new IllegalArgumentException(
                        "The genotype key elements must be between 0 and allelesfrequencies.length.");
                }

                if (genotypeKey[j] == i) {
                    coefficients[i] += 1;
                }
            }
        }

        value = 1;

        for (int i = 0; i < allelesfrequencies.length; i++) {
            value *= Math.pow(allelesfrequencies[i], coefficients[i]);
        }

        return MathUtils.polynomialExpansion(ploidy, coefficients) * value;
    }

    //Testing deviation from the HWP is generally performed using Pearson's chi-squared test, using the observed genotype frequencies obtained from the data and the expected genotype frequencies obtained using the HWP.
    //see http://en.wikipedia.org/wiki/F-statistics and org.jscience.mathematics.statistics
    //and http://en.wikipedia.org/wiki/Wahlund_effect
    //coefficient of relatedness between siblings
    //see http://en.wikipedia.org/wiki/Eusociality
}
