package org.jscience.tests.mathematics.analysis.functions.vectorial;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jscience.tests.mathematics.analysis.functions.ExhaustedSampleException;
import org.jscience.tests.mathematics.analysis.functions.FunctionException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class BasicSampledFunctionIteratorTest extends TestCase {
/**
     * Creates a new BasicSampledFunctionIteratorTest object.
     *
     * @param name DOCUMENT ME!
     */
    public BasicSampledFunctionIteratorTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws ExhaustedSampleException DOCUMENT ME!
     * @throws FunctionException DOCUMENT ME!
     */
    public void testIteration()
        throws ExhaustedSampleException, FunctionException {
        BasicSampledFunctionIterator iter = new BasicSampledFunctionIterator(new Function(
                    0.0, 0.1, 10));

        for (int i = 0; i < 10; ++i) {
            assertTrue(iter.hasNext());

            VectorialValuedPair pair = iter.nextSamplePoint();
            assertTrue(Math.abs(pair.getX() - (0.1 * i)) < 1.0e-10);
            assertTrue(Math.abs(pair.getY()[0] + (0.1 * i)) < 1.0e-10);
            assertTrue(Math.abs(pair.getY()[1] + (0.2 * i)) < 1.0e-10);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws ExhaustedSampleException DOCUMENT ME!
     * @throws FunctionException DOCUMENT ME!
     */
    public void testExhausted()
        throws ExhaustedSampleException, FunctionException {
        BasicSampledFunctionIterator iter = new BasicSampledFunctionIterator(new Function(
                    0.0, 0.1, 10));

        for (int i = 0; i < 10; ++i) {
            assertTrue(iter.hasNext());

            VectorialValuedPair pair = iter.nextSamplePoint();
        }

        assertTrue(!iter.hasNext());

        boolean exceptionOccurred = false;

        try {
            iter.nextSamplePoint();
        } catch (ExhaustedSampleException e) {
            exceptionOccurred = true;
        }

        assertTrue(exceptionOccurred);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws ExhaustedSampleException DOCUMENT ME!
     * @throws FunctionException DOCUMENT ME!
     */
    public void testUnderlyingException()
        throws ExhaustedSampleException, FunctionException {
        BasicSampledFunctionIterator iter = new BasicSampledFunctionIterator(new SampledFunction() {
                    private boolean fireException = false;

                    public int size() {
                        return 2;
                    }

                    public int getDimension() {
                        return 2;
                    }

                    public VectorialValuedPair samplePointAt(int i)
                        throws FunctionException {
                        if (fireException) {
                            throw new FunctionException("boom");
                        }

                        fireException = true;

                        return new VectorialValuedPair(0.0, null);
                    }
                });

        boolean exceptionOccurred = false;

        try {
            iter.nextSamplePoint();
        } catch (FunctionException e) {
            exceptionOccurred = true;
        }

        assertTrue(!exceptionOccurred);

        exceptionOccurred = false;

        try {
            iter.nextSamplePoint();
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
        return new TestSuite(BasicSampledFunctionIteratorTest.class);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class Function implements SampledFunction {
        /** DOCUMENT ME! */
        private double begin;

        /** DOCUMENT ME! */
        private double step;

        /** DOCUMENT ME! */
        private int n;

        /** DOCUMENT ME! */
        private double[] values;

/**
         * Creates a new Function object.
         *
         * @param begin DOCUMENT ME!
         * @param step  DOCUMENT ME!
         * @param n     DOCUMENT ME!
         */
        public Function(double begin, double step, int n) {
            this.begin = begin;
            this.step = step;
            this.n = n;
            values = new double[2];
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int size() {
            return n;
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
         * @param i DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws FunctionException DOCUMENT ME!
         */
        public VectorialValuedPair samplePointAt(int i)
            throws FunctionException {
            if ((i < 0) || (i >= n)) {
                throw new FunctionException("outside of range");
            }

            double x = begin + (i * step);
            values[0] = -x;
            values[1] = 2.0 * values[0];

            return new VectorialValuedPair(x, values);
        }
    }
}
