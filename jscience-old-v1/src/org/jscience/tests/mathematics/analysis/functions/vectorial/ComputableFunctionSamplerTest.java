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

package org.jscience.tests.mathematics.analysis.functions.vectorial;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jscience.tests.mathematics.analysis.functions.FunctionException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ComputableFunctionSamplerTest extends TestCase {
/**
     * Creates a new ComputableFunctionSamplerTest object.
     *
     * @param name DOCUMENT ME!
     */
    public ComputableFunctionSamplerTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws FunctionException DOCUMENT ME!
     */
    public void testBeginStepNumber() throws FunctionException {
        ComputableFunctionSampler sampler = new ComputableFunctionSampler(new Function(
                    0.0, 1.0), 0.0, 0.099, 11);

        assertTrue(sampler.size() == 11);
        assertTrue(sampler.getDimension() == 2);
        assertTrue(Math.abs(sampler.samplePointAt(0).getX() - 0.000) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(0).getY()[0] + 0.000) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(0).getY()[1] + 0.000) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(5).getX() - 0.495) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(5).getY()[0] + 0.495) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(5).getY()[1] + 0.990) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(10).getX() - 0.990) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(10).getY()[0] + 0.990) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(10).getY()[1] + 1.980) < 1.0e-10);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws FunctionException DOCUMENT ME!
     */
    public void testRangeNumber() throws FunctionException {
        double[] range = new double[2];
        range[0] = 0.0;
        range[1] = 1.0;

        ComputableFunctionSampler sampler = new ComputableFunctionSampler(new Function(
                    0.0, 1.0), range, 11);

        assertTrue(sampler.size() == 11);
        assertTrue(sampler.getDimension() == 2);
        assertTrue(Math.abs(sampler.samplePointAt(0).getX() - 0.0) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(0).getY()[0] + 0.0) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(0).getY()[1] + 0.0) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(5).getX() - 0.5) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(5).getY()[0] + 0.5) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(5).getY()[1] + 1.0) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(10).getX() - 1.0) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(10).getY()[0] + 1.0) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(10).getY()[1] + 2.0) < 1.0e-10);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws FunctionException DOCUMENT ME!
     */
    public void testRangeStepNoAdjust() throws FunctionException {
        double[] range = new double[2];
        range[0] = 0.0;
        range[1] = 1.0;

        ComputableFunctionSampler sampler = new ComputableFunctionSampler(new Function(
                    0.0, 1.0), range, 0.083, false);

        assertTrue(sampler.size() == 12);
        assertTrue(sampler.getDimension() == 2);
        assertTrue(Math.abs(sampler.samplePointAt(0).getX() - 0.000) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(0).getY()[0] + 0.000) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(0).getY()[1] + 0.000) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(5).getX() - 0.415) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(5).getY()[0] + 0.415) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(5).getY()[1] + 0.830) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(11).getX() - 0.913) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(11).getY()[0] + 0.913) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(11).getY()[1] + 1.826) < 1.0e-10);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws FunctionException DOCUMENT ME!
     */
    public void testRangeStepAdjust() throws FunctionException {
        double[] range = new double[2];
        range[0] = 0.0;
        range[1] = 1.0;

        ComputableFunctionSampler sampler = new ComputableFunctionSampler(new Function(
                    0.0, 1.0), range, 0.083, true);

        assertTrue(sampler.size() == 13);
        assertTrue(sampler.getDimension() == 2);
        assertTrue(Math.abs(sampler.samplePointAt(0).getX() - 0.0) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(0).getY()[0] + 0.0) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(0).getY()[1] + 0.0) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(6).getX() - 0.5) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(6).getY()[0] + 0.5) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(6).getY()[1] + 1.0) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(12).getX() - 1.0) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(12).getY()[0] + 1.0) < 1.0e-10);
        assertTrue(Math.abs(sampler.samplePointAt(12).getY()[1] + 2.0) < 1.0e-10);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws FunctionException DOCUMENT ME!
     */
    public void testOutOfRange() throws FunctionException {
        ComputableFunctionSampler sampler = new ComputableFunctionSampler(new Function(
                    0.0, 1.0), 0.0, 1.0, 10);

        boolean exceptionOccurred = false;

        try {
            sampler.samplePointAt(-1);
        } catch (ArrayIndexOutOfBoundsException e) {
            exceptionOccurred = true;
        }

        assertTrue(exceptionOccurred);

        exceptionOccurred = false;

        try {
            sampler.samplePointAt(10);
        } catch (ArrayIndexOutOfBoundsException e) {
            exceptionOccurred = true;
        }

        assertTrue(exceptionOccurred);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws FunctionException DOCUMENT ME!
     */
    public void testUnderlyingException() {
        ComputableFunctionSampler sampler = new ComputableFunctionSampler(new ComputableFunction() {
                    public int getDimension() {
                        return 2;
                    }

                    public double[] valueAt(double x) throws FunctionException {
                        if (x < 0.5) {
                            double[] res = new double[2];
                            res[0] = -x;
                            res[1] = -2.0 * x;

                            return res;
                        } else {
                            throw new FunctionException(
                                "upper half range exception");
                        }
                    }
                }, 0.0, 0.1, 11);

        boolean exceptionOccurred = false;

        try {
            sampler.samplePointAt(2);
        } catch (FunctionException e) {
            exceptionOccurred = true;
        }

        assertTrue(!exceptionOccurred);

        exceptionOccurred = false;

        try {
            sampler.samplePointAt(8);
        } catch (FunctionException e) {
            exceptionOccurred = true;
        }

        assertTrue(exceptionOccurred);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(ComputableFunctionSamplerTest.class);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class Function implements ComputableFunction {
        /** DOCUMENT ME! */
        private double min;

        /** DOCUMENT ME! */
        private double max;

        /** DOCUMENT ME! */
        private double[] values;

/**
         * Creates a new Function object.
         *
         * @param min DOCUMENT ME!
         * @param max DOCUMENT ME!
         */
        public Function(double min, double max) {
            this.min = min;
            this.max = max;
            values = new double[2];
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getDimension() {
            return 2;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws FunctionException DOCUMENT ME!
         */
        public double[] valueAt(double x) throws FunctionException {
            if ((x < min) || (x > max)) {
                throw new FunctionException("outside of range");
            }

            values[0] = -x;
            values[1] = -2.0 * x;

            return values;
        }
    }
}
