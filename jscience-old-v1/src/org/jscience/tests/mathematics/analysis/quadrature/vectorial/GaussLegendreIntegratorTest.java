package org.jscience.tests.mathematics.analysis.quadrature.vectorial;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jscience.tests.mathematics.analysis.functions.FunctionException;
import org.jscience.tests.mathematics.analysis.functions.vectorial.ComputableFunction;

import java.util.Random;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class GaussLegendreIntegratorTest extends TestCase {
/**
     * Creates a new GaussLegendreIntegratorTest object.
     *
     * @param name DOCUMENT ME!
     */
    public GaussLegendreIntegratorTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws FunctionException DOCUMENT ME!
     */
    public void testExactIntegration() throws FunctionException {
        Random random = new Random(86343623467878363L);
        int order = 0;

        while (true) {
            GaussLegendreIntegrator integrator = new GaussLegendreIntegrator(order,
                    7.0);
            int availableOrder = integrator.getEvaluationsPerStep();

            if (availableOrder < order) {
                // we have tested all available orders
                return;
            }

            // an order n Gauss-Legendre integrator integrates
            // 2n-1 degree polynoms exactly
            for (int degree = 0; degree <= ((2 * availableOrder) - 1);
                    ++degree) {
                for (int i = 0; i < 10; ++i) {
                    Polynom p = new Polynom(degree, random, 100.0);
                    double[] s0 = integrator.integrate(p, -5.0, 15.0);
                    double[] s1 = p.exactIntegration(-5.0, 15.0);

                    for (int j = 0; j < p.getDimension(); ++j) {
                        assertTrue(Math.abs(s0[j] - s1[j]) < (1.0e-12 * (1.0 +
                            Math.abs(s0[j]))));
                    }
                }
            }

            ++order;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(GaussLegendreIntegratorTest.class);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class Polynom implements ComputableFunction {
        /** DOCUMENT ME! */
        private double[] coeffs0;

        /** DOCUMENT ME! */
        private double[] coeffs1;

/**
         * Creates a new Polynom object.
         *
         * @param degree DOCUMENT ME!
         * @param random DOCUMENT ME!
         * @param max    DOCUMENT ME!
         */
        public Polynom(int degree, Random random, double max) {
            coeffs0 = new double[degree + 1];
            coeffs1 = new double[degree + 1];

            for (int i = 0; i <= degree; ++i) {
                coeffs0[i] = 2.0 * max * (random.nextDouble() - 0.5);
                coeffs1[i] = 2.0 * max * (random.nextDouble() - 0.5);
            }
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
         * @param t DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws FunctionException DOCUMENT ME!
         */
        public double[] valueAt(double t) throws FunctionException {
            double[] y = new double[2];
            y[0] = coeffs0[coeffs0.length - 1];

            for (int i = coeffs0.length - 2; i >= 0; --i) {
                y[0] = (y[0] * t) + coeffs0[i];
            }

            y[1] = coeffs1[coeffs1.length - 1];

            for (int i = coeffs1.length - 2; i >= 0; --i) {
                y[1] = (y[1] * t) + coeffs1[i];
            }

            return y;
        }

        /**
         * DOCUMENT ME!
         *
         * @param a DOCUMENT ME!
         * @param b DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws FunctionException DOCUMENT ME!
         */
        public double[] exactIntegration(double a, double b)
            throws FunctionException {
            double[] res = new double[2];
            double yb = coeffs0[coeffs0.length - 1] / coeffs0.length;
            double ya = yb;

            for (int i = coeffs0.length - 2; i >= 0; --i) {
                yb = (yb * b) + (coeffs0[i] / (i + 1));
                ya = (ya * a) + (coeffs0[i] / (i + 1));
            }

            res[0] = (yb * b) - (ya * a);
            yb = coeffs1[coeffs1.length - 1] / coeffs1.length;
            ya = yb;

            for (int i = coeffs1.length - 2; i >= 0; --i) {
                yb = (yb * b) + (coeffs1[i] / (i + 1));
                ya = (ya * a) + (coeffs1[i] / (i + 1));
            }

            res[1] = (yb * b) - (ya * a);

            return res;
        }
    }
}
