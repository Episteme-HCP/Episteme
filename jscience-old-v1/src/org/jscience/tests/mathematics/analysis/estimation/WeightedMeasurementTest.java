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


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class WeightedMeasurementTest extends TestCase {
    /** DOCUMENT ME! */
    private EstimatedParameter p1;

    /** DOCUMENT ME! */
    private EstimatedParameter p2;

/**
     * Creates a new WeightedMeasurementTest object.
     *
     * @param name DOCUMENT ME!
     */
    public WeightedMeasurementTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     */
    public void testConstruction() {
        WeightedMeasurement m = new MyMeasurement(3.0, theoretical() + 0.1);
        checkValue(m.getWeight(), 3.0);
        checkValue(m.getMeasuredValue(), theoretical() + 0.1);
    }

    /**
     * DOCUMENT ME!
     */
    public void testIgnored() {
        WeightedMeasurement m = new MyMeasurement(3.0, theoretical() + 0.1);
        assertTrue(!m.isIgnored());
        m.setIgnored(true);
        assertTrue(m.isIgnored());
        m.setIgnored(false);
        assertTrue(!m.isIgnored());
    }

    /**
     * DOCUMENT ME!
     */
    public void testTheory() {
        WeightedMeasurement m = new MyMeasurement(3.0, theoretical() + 0.1);
        checkValue(m.getTheoreticalValue(), theoretical());
        checkValue(m.getResidual(), 0.1);

        double oldP1 = p1.getEstimate();
        p1.setEstimate(oldP1 + (m.getResidual() / m.getPartial(p1)));
        checkValue(m.getResidual(), 0.0);
        p1.setEstimate(oldP1);
        checkValue(m.getResidual(), 0.1);

        double oldP2 = p2.getEstimate();
        p2.setEstimate(oldP2 + (m.getResidual() / m.getPartial(p2)));
        checkValue(m.getResidual(), 0.0);
        p2.setEstimate(oldP2);
        checkValue(m.getResidual(), 0.1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(WeightedMeasurementTest.class);
    }

    /**
     * DOCUMENT ME!
     */
    public void setUp() {
        p1 = new EstimatedParameter("p1", 1.0);
        p2 = new EstimatedParameter("p2", 2.0);
    }

    /**
     * DOCUMENT ME!
     */
    public void tearDown() {
        p1 = null;
        p2 = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     * @param expected DOCUMENT ME!
     */
    private void checkValue(double value, double expected) {
        assertTrue(Math.abs(value - expected) < 1.0e-10);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double theoretical() {
        return (3 * p1.getEstimate()) - p2.getEstimate();
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double partial(EstimatedParameter p) {
        if (p == p1) {
            return 3.0;
        } else if (p == p2) {
            return -1.0;
        } else {
            return 0.0;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class MyMeasurement extends WeightedMeasurement {
/**
         * Creates a new MyMeasurement object.
         *
         * @param weight        DOCUMENT ME!
         * @param measuredValue DOCUMENT ME!
         */
        public MyMeasurement(double weight, double measuredValue) {
            super(weight, measuredValue);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double getTheoreticalValue() {
            return theoretical();
        }

        /**
         * DOCUMENT ME!
         *
         * @param p DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double getPartial(EstimatedParameter p) {
            return partial(p);
        }
    }
}
