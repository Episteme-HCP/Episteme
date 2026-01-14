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

package org.jscience.util;

import org.junit.jupiter.api.Test;
import org.jscience.mathematics.statistics.anova.ANOVA;
import org.jscience.mathematics.statistics.distributions.*;
import org.jscience.mathematics.statistics.fitting.*;
import org.jscience.mathematics.statistics.timeseries.*;
// import org.jscience.computing.ai.neuralnetwork.*; // In jscience-natural
import org.jscience.mathematics.ml.*;
// import org.jscience.mathematics.algebra.groups.*;

public class CorePojoTests {

    @Test
    public void testStatisticsPojos() {
        PojoTester.testPojo(ANOVA.class);

        // Distributions
        PojoTester.testPojo(NormalDistribution.class);
        PojoTester.testPojo(BinomialDistribution.class);
        PojoTester.testPojo(ChiSquareDistribution.class);
        PojoTester.testPojo(ExponentialDistribution.class);
        PojoTester.testPojo(GammaDistribution.class);
        PojoTester.testPojo(GeometricDistribution.class);
        PojoTester.testPojo(LogNormalDistribution.class);
        PojoTester.testPojo(PoissonDistribution.class);
        PojoTester.testPojo(StudentTDistribution.class);
        PojoTester.testPojo(UniformDistribution.class);
        PojoTester.testPojo(WeibullDistribution.class);
        PojoTester.testPojo(BetaDistribution.class);
        PojoTester.testPojo(CauchyDistribution.class);

        // Fitting
        PojoTester.testPojo(LinearRegression.class);
        PojoTester.testPojo(PolynomialRegression.class);

        // TimeSeries
        PojoTester.testPojo(TimeSeries.class);
        PojoTester.testPojo(KalmanFilter.class);
        // Ensure inner classes are tested if static
        PojoTester.testPojo(TimeSeries.SeasonalDecomposition.class);
        PojoTester.testPojo(TimeSeries.MAModel.class);
        PojoTester.testPojo(TimeSeries.ARModel.class);
    }

    @Test
    public void testMachineLearningPojos() {
        PojoTester.testPojo(KMeans.class);
        PojoTester.testPojo(LogisticRegression.class);
        PojoTester.testPojo(PCA.class);
        // PojoTester.testPojo(NeuralNetwork.class); // In jscience-natural

    }

    @Test
    public void testComplexNumbers() {
        PojoTester.testPojo(org.jscience.mathematics.numbers.complex.Complex.class);
        PojoTester.testPojo(org.jscience.mathematics.numbers.complex.Quaternion.class);
        PojoTester.testPojo(org.jscience.mathematics.numbers.complex.Octonion.class);
    }

    @Test
    public void testAlgebraGroups() {
        PojoTester.testPojo(org.jscience.mathematics.algebra.groups.CyclicGroup.class);
        PojoTester.testPojo(org.jscience.mathematics.algebra.groups.DihedralGroup.class);
        PojoTester.testPojo(org.jscience.mathematics.algebra.groups.FreeGroup.class);
        PojoTester.testPojo(org.jscience.mathematics.algebra.groups.LieGroup.class);
        PojoTester.testPojo(org.jscience.mathematics.algebra.groups.QuaternionGroup.class);
        PojoTester.testPojo(org.jscience.mathematics.algebra.groups.SO3_1Group.class);
        PojoTester.testPojo(org.jscience.mathematics.algebra.groups.SU2Group.class);
        PojoTester.testPojo(org.jscience.mathematics.algebra.groups.SU3Group.class);
        PojoTester.testPojo(org.jscience.mathematics.algebra.groups.SymmetricGroup.class);
        PojoTester.testPojo(org.jscience.mathematics.algebra.groups.U1Group.class);
    }
}
