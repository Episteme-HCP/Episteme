/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.sociology;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.vectors.DenseVector;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.mathematics.numbers.real.Real;

/**
 * Provides mathematical models for age-structured populations and demographic transitions.
 * Includes implementations for Leslie Matrix projections and standard demographic ratios.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class Demography implements Serializable {

    private static final long serialVersionUID = 1L;

    private Demography() {
        // Prevent instantiation
    }

    /**
     * Data structure representing the age and gender distribution of a population.
     * Male and female counts should follow standard cohort groupings (e.g., 5-year intervals).
     */
    public record PopulationPyramid(
            int[] maleCounts,
            int[] femaleCounts
    ) implements Serializable {
        private static final long serialVersionUID = 1L;
    }

    /**
     * Performs a Leslie Matrix projection to estimate the next generation's population.
     * P(t+1) = L * P(t).
     *
     * @param population current population vector by age SociologicalGroup (Real)
     * @param fertility  fertility rates per age SociologicalGroup (Real)
     * @param survival   survival probabilities per age SociologicalGroup (Real)
     * @return predicted population vector for the next time step
     */
    public static Vector<Real> projectLeslie(Vector<Real> population, Vector<Real> fertility, Vector<Real> survival) {
        int n = population.dimension();
        List<Real> next = new ArrayList<>(n);
        for (int k = 0; k < n; k++) next.add(Real.ZERO);

        // New births (first row of Leslie matrix)
        Real term0 = Real.ZERO;
        for (int i = 0; i < n; i++) {
            Real pop = population.get(i);
            Real fert = (i < fertility.dimension()) ? fertility.get(i) : Real.ZERO;
            term0 = term0.add(pop.multiply(fert));
        }
        next.set(0, term0);

        // Aging (sub-diagonal)
        for (int i = 0; i < n - 1; i++) {
            Real pop = population.get(i);
            Real surv = (i < survival.dimension()) ? survival.get(i) : Real.ZERO;
            next.set(i + 1, pop.multiply(surv));
        }

        return DenseVector.of(next, Reals.getInstance());
    }

    /**
     * Calculates the Child-Woman Ratio (CWR).
     * CWR = (Children 0-4 / Women 15-49) * 1000.
     *
     * @param children0to4 number of children aged 0-4
     * @param women15to49  number of women aged 15-49
     * @return the CWR as a Real number
     */
    public static Real childWomanRatio(int children0to4, int women15to49) {
        if (women15to49 == 0) {
            return Real.ZERO;
        }
        return Real.of(1000).multiply(Real.of(children0to4)).divide(Real.of(women15to49));
    }

    /**
     * Calculates the Net Reproduction Rate (NRR).
     * NRR = Sigma (Fertility_i * femaleSurvival_i).
     *
     * @param ageFertility   fertility rates per age cohort
     * @param femaleSurvival female survival probabilities per age cohort
     * @return the NRR as a Real number
     */
    public static Real netReproductionRate(Vector<Real> ageFertility, Vector<Real> femaleSurvival) {
        Real nrr = Real.ZERO;
        int len = Math.min(ageFertility.dimension(), femaleSurvival.dimension());
        for (int i = 0; i < len; i++) {
            nrr = nrr.add(ageFertility.get(i).multiply(femaleSurvival.get(i)));
        }
        return nrr;
    }

    /**
     * Estimates the demographic transition stage based on birth and death rates.
     *
     * @param birthRate birth rate per 1000 individuals
     * @param deathRate death rate per 1000 individuals
     * @return the transition stage (1 to 5)
     */
    public static int estimateTransitionStage(Real birthRate, Real deathRate) {
        if (birthRate.compareTo(Real.of(35)) > 0 && deathRate.compareTo(Real.of(30)) > 0) {
            return 1; // High stationary
        }
        if (birthRate.compareTo(Real.of(30)) > 0 && deathRate.compareTo(Real.of(20)) < 0) {
            return 2; // Early expanding
        }
        if (birthRate.compareTo(Real.of(30)) < 0 && deathRate.compareTo(Real.of(15)) < 0 && birthRate.compareTo(deathRate) > 0) {
            return 3; // Late expanding
        }
        if (birthRate.compareTo(Real.of(15)) < 0 && deathRate.compareTo(Real.of(15)) < 0) {
            return 4; // Low stationary
        }
        if (birthRate.compareTo(deathRate) < 0) {
            return 5; // Declining
        }
        return 3;
    }
}

