/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.medicine.epidemiology;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Epidemiological statistics calculations.
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EpidemiologyStats {

    private EpidemiologyStats() {
    }

    /** Incidence rate = new cases / population at risk */
    public static Real incidenceRate(int newCases, int populationAtRisk) {
        return Real.of(newCases).divide(Real.of(populationAtRisk));
    }

    /** Prevalence = total cases / total population */
    public static Real prevalence(int totalCases, int totalPopulation) {
        return Real.of(totalCases).divide(Real.of(totalPopulation));
    }

    /** Odds ratio from 2x2 contingency table. OR = (a*d) / (b*c) */
    public static Real oddsRatio(int a, int b, int c, int d) {
        return Real.of(a * d).divide(Real.of(b * c));
    }

    /** Relative risk. RR = (a/(a+b)) / (c/(c+d)) */
    public static Real relativeRisk(int a, int b, int c, int d) {
        Real riskExposed = Real.of(a).divide(Real.of(a + b));
        Real riskUnexposed = Real.of(c).divide(Real.of(c + d));
        return riskExposed.divide(riskUnexposed);
    }

    /** Sensitivity = TP / (TP + FN) */
    public static Real sensitivity(int truePositive, int falseNegative) {
        return Real.of(truePositive).divide(Real.of(truePositive + falseNegative));
    }

    /** Specificity = TN / (TN + FP) */
    public static Real specificity(int trueNegative, int falsePositive) {
        return Real.of(trueNegative).divide(Real.of(trueNegative + falsePositive));
    }

    /** Positive Predictive Value = TP / (TP + FP) */
    public static Real positivePredictiveValue(int truePositive, int falsePositive) {
        return Real.of(truePositive).divide(Real.of(truePositive + falsePositive));
    }

    /** Negative Predictive Value = TN / (TN + FN) */
    public static Real negativePredictiveValue(int trueNegative, int falseNegative) {
        return Real.of(trueNegative).divide(Real.of(trueNegative + falseNegative));
    }

    /** Number Needed to Treat = 1 / ARR */
    public static Real numberNeededToTreat(Real controlEventRate, Real treatmentEventRate) {
        Real arr = controlEventRate.subtract(treatmentEventRate);
        return Real.ONE.divide(arr);
    }
}
