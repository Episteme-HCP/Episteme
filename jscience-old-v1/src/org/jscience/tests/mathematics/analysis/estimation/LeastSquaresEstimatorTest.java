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

package org.jscience.tests.mathematics.analysis.estimation;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Random;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class LeastSquaresEstimatorTest extends TestCase
    implements EstimationProblem {
    /** DOCUMENT ME! */
    private EstimatedParameter[] perfectPars;

    /** DOCUMENT ME! */
    private EstimatedParameter[] randomizedPars;

    /** DOCUMENT ME! */
    private EstimatedParameter[] unboundPars;

    /** DOCUMENT ME! */
    private WeightedMeasurement[] measurements;

/**
     * Creates a new LeastSquaresEstimatorTest object.
     *
     * @param name DOCUMENT ME!
     */
    public LeastSquaresEstimatorTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws EstimationException DOCUMENT ME!
     */
    public void testNoMeasurementError() throws EstimationException {
        initRandomizedGrid(2.3);
        initProblem(0.0);

        LeastSquaresEstimator estimator = new LeastSquaresEstimator(100,
                1.0e-7, 1.0e-10, 1.0e-10);
        estimator.estimate(this);
        checkGrid(0.01);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws EstimationException DOCUMENT ME!
     */
    public void testSmallMeasurementError() throws EstimationException {
        initRandomizedGrid(2.3);
        initProblem(0.02);

        LeastSquaresEstimator estimator = new LeastSquaresEstimator(100,
                1.0e-7, 1.0e-10, 1.0e-10);
        estimator.estimate(this);
        checkGrid(0.1);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws EstimationException DOCUMENT ME!
     */
    public void testNoError() throws EstimationException {
        initRandomizedGrid(0.0);
        initProblem(0.0);

        LeastSquaresEstimator estimator = new LeastSquaresEstimator(100,
                1.0e-7, 1.0e-10, 1.0e-10);
        estimator.estimate(this);
        checkGrid(1.0e-10);
    }

    /**
     * DOCUMENT ME!
     */
    public void testUnsolvableProblem() {
        initRandomizedGrid(2.3);
        initProblem(0.0);

        // reduce the number of measurements below the limit threshold
        int unknowns = unboundPars.length;
        WeightedMeasurement[] reducedSet = new WeightedMeasurement[unknowns -
            1];

        for (int i = 0; i < reducedSet.length; ++i) {
            reducedSet[i] = measurements[i];
        }

        measurements = reducedSet;

        boolean gotIt = false;

        try {
            LeastSquaresEstimator estimator = new LeastSquaresEstimator(100,
                    1.0e-7, 1.0e-10, 1.0e-10);
            estimator.estimate(this);
        } catch (EstimationException e) {
            gotIt = true;
        }

        assertTrue(gotIt);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(LeastSquaresEstimatorTest.class);
    }

    /**
     * DOCUMENT ME!
     */
    public void setUp() {
        initPerfectGrid(5);
    }

    /**
     * DOCUMENT ME!
     */
    public void tearDown() {
        perfectPars = null;
        randomizedPars = null;
        unboundPars = null;
        measurements = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param gridSize DOCUMENT ME!
     */
    private void initPerfectGrid(int gridSize) {
        perfectPars = new EstimatedParameter[gridSize * gridSize * 2];

        int k = 0;

        for (int i = 0; i < gridSize; ++i) {
            for (int j = 0; j < gridSize; ++j) {
                String name = new Integer(k).toString();
                perfectPars[2 * k] = new EstimatedParameter("x" + name, i);
                perfectPars[(2 * k) + 1] = new EstimatedParameter("y" + name, j);
                ++k;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param initialGuessError DOCUMENT ME!
     */
    private void initRandomizedGrid(double initialGuessError) {
        Random randomizer = new Random(2353995334L);
        randomizedPars = new EstimatedParameter[perfectPars.length];

        // add an error to every point coordinate
        for (int k = 0; k < randomizedPars.length; ++k) {
            String name = perfectPars[k].getName();
            double value = perfectPars[k].getEstimate();
            double error = randomizer.nextGaussian() * initialGuessError;
            randomizedPars[k] = new EstimatedParameter(name, value + error);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param measurementError DOCUMENT ME!
     */
    private void initProblem(double measurementError) {
        int pointsNumber = randomizedPars.length / 2;
        int measurementsNumber = (pointsNumber * (pointsNumber - 1)) / 2;
        measurements = new WeightedMeasurement[measurementsNumber];

        Random randomizer = new Random(5785631926L);

        // for the test, we consider that the perfect grid is the reality
        // and that the randomized grid is the first (wrong) estimate.
        int i = 0;

        for (int l = 0; l < (pointsNumber - 1); ++l) {
            for (int m = l + 1; m < pointsNumber; ++m) {
                // perfect measurements on the real data
                double dx = perfectPars[2 * l].getEstimate() -
                    perfectPars[2 * m].getEstimate();
                double dy = perfectPars[(2 * l) + 1].getEstimate() -
                    perfectPars[(2 * m) + 1].getEstimate();
                double d = Math.sqrt((dx * dx) + (dy * dy));

                // adding a noise to the measurements
                d += (randomizer.nextGaussian() * measurementError);

                // add the measurement to the current problem
                measurements[i++] = new Distance(1.0, d, randomizedPars[2 * l],
                        randomizedPars[(2 * l) + 1], randomizedPars[2 * m],
                        randomizedPars[(2 * m) + 1]);
            }
        }

        // fix three values in the randomized grid and bind them (there
        // are two abscissas and one ordinate, so if there were no error
        // at all, the estimated grid should be correctly centered on the
        // perfect grid)
        int oddNumber = (2 * (randomizedPars.length / 4)) - 1;

        for (int k = 0; k < ((2 * oddNumber) + 1); k += oddNumber) {
            randomizedPars[k].setEstimate(perfectPars[k].getEstimate());
            randomizedPars[k].setBound(true);
        }

        // store the unbound parameters in a specific table
        unboundPars = new EstimatedParameter[randomizedPars.length - 3];

        for (int src = 0, dst = 0; src < randomizedPars.length; ++src) {
            if (!randomizedPars[src].isBound()) {
                unboundPars[dst++] = randomizedPars[src];
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param threshold DOCUMENT ME!
     */
    private void checkGrid(double threshold) {
        double rms = 0;

        for (int i = 0; i < perfectPars.length; ++i) {
            rms += (perfectPars[i].getEstimate() -
            randomizedPars[i].getEstimate());
        }

        rms = Math.sqrt(rms / perfectPars.length);

        assertTrue(rms <= threshold);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public WeightedMeasurement[] getMeasurements() {
        return measurements;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public EstimatedParameter[] getUnboundParameters() {
        return unboundPars;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public EstimatedParameter[] getAllParameters() {
        return randomizedPars;
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class Distance extends WeightedMeasurement {
        /** DOCUMENT ME! */
        private EstimatedParameter x1;

        /** DOCUMENT ME! */
        private EstimatedParameter y1;

        /** DOCUMENT ME! */
        private EstimatedParameter x2;

        /** DOCUMENT ME! */
        private EstimatedParameter y2;

/**
         * Creates a new Distance object.
         *
         * @param weight        DOCUMENT ME!
         * @param measuredValue DOCUMENT ME!
         * @param x1            DOCUMENT ME!
         * @param y1            DOCUMENT ME!
         * @param x2            DOCUMENT ME!
         * @param y2            DOCUMENT ME!
         */
        public Distance(double weight, double measuredValue,
            EstimatedParameter x1, EstimatedParameter y1,
            EstimatedParameter x2, EstimatedParameter y2) {
            super(weight, measuredValue);
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double getTheoreticalValue() {
            double dx = x2.getEstimate() - x1.getEstimate();
            double dy = y2.getEstimate() - y1.getEstimate();

            return Math.sqrt((dx * dx) + (dy * dy));
        }

        /**
         * DOCUMENT ME!
         *
         * @param p DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double getPartial(EstimatedParameter p) {
            // first quick answer for most parameters
            if ((p != x1) && (p != y1) && (p != x2) && (p != y2)) {
                return 0.0;
            }

            // compute the value now as we know we depend on the specified parameter
            double distance = getTheoreticalValue();

            if (p == x1) {
                return (x1.getEstimate() - x2.getEstimate()) / distance;
            } else if (p == x2) {
                return (x2.getEstimate() - x1.getEstimate()) / distance;
            } else if (p == y1) {
                return (y1.getEstimate() - y2.getEstimate()) / distance;
            } else {
                return (y2.getEstimate() - y1.getEstimate()) / distance;
            }
        }
    }
}
