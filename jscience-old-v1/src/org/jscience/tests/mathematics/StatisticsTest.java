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

package org.jscience.tests.mathematics;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jscience.mathematics.statistics.*;


/**
 * Testcase for statistical distributions.
 *
 * @author Mark Hale
 */
public class StatisticsTest extends junit.framework.TestCase {
    /** DOCUMENT ME! */
    private static final ProbabilityDistribution[] distrs = new ProbabilityDistribution[] {
            new NormalDistribution(), new CauchyDistribution(),
            new BetaDistribution(5, 13), new FDistribution(5, 13),
            new TDistribution(11), new GeometricDistribution(0.3),
            new GammaDistribution(1.5), new ExponentialDistribution(),
            new LognormalDistribution(), new WeibullDistribution(1.5)
        };

    /** DOCUMENT ME! */
    private static int distrsIndex;

    /** DOCUMENT ME! */
    private final ProbabilityDistribution distribution;

/**
     * Creates a new StatisticsTest object.
     *
     * @param name DOCUMENT ME!
     */
    public StatisticsTest(String name) {
        super(name);
        distribution = distrs[distrsIndex];
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(StatisticsTest.class.toString());

        for (distrsIndex = 0; distrsIndex < distrs.length; distrsIndex++)
            suite.addTest(new TestSuite(StatisticsTest.class,
                    distrs[distrsIndex].toString()));

        return suite;
    }

    /**
     * DOCUMENT ME!
     */
    protected void setUp() {
        org.jscience.GlobalSettings.ZERO_TOL = 1.0e-8;
    }

    /**
     * Test inverse of cumulative distribution function.
     */
    public void testInverse() {
        double expected = Math.random();
        double actual = distribution.inverse(distribution.cumulative(expected));
        assertEquals(expected, actual, org.jscience.GlobalSettings.ZERO_TOL);
    }
}
