package org.jscience.tests.mathematics.analysis.fitting;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jscience.tests.mathematics.analysis.estimation.EstimationException;
import org.jscience.tests.mathematics.analysis.estimation.WeightedMeasurement;

import java.util.Random;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class HarmonicFitterTest extends TestCase {
/**
     * Creates a new HarmonicFitterTest object.
     *
     * @param name DOCUMENT ME!
     */
    public HarmonicFitterTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws EstimationException DOCUMENT ME!
     */
    public void testNoError() throws EstimationException {
        HarmonicFunction f = new HarmonicFunction(0.2, 3.4, 4.1);

        HarmonicFitter fitter = new HarmonicFitter(20, 1.0e-7, 1.0e-10, 1.0e-10);

        for (double x = 0.0; x < 1.3; x += 0.01) {
            fitter.addWeightedPair(1.0, x, f.valueAt(x));
        }

        double[] coeffs = fitter.fit();

        HarmonicFunction fitted = new HarmonicFunction(coeffs[0], coeffs[1],
                coeffs[2]);
        assertTrue(Math.abs(coeffs[0] - f.getA()) < 1.0e-12);
        assertTrue(Math.abs(coeffs[1] - f.getOmega()) < 1.0e-12);
        assertTrue(Math.abs(coeffs[2] - center(f.getPhi(), coeffs[2])) < 1.0e-12);

        for (double x = -1.0; x < 1.0; x += 0.01) {
            assertTrue(Math.abs(f.valueAt(x) - fitted.valueAt(x)) < 1.0e-12);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws EstimationException DOCUMENT ME!
     */
    public void test1PercentError() throws EstimationException {
        Random randomizer = new Random(64925784252L);
        HarmonicFunction f = new HarmonicFunction(0.2, 3.4, 4.1);

        HarmonicFitter fitter = new HarmonicFitter(20, 1.0e-7, 1.0e-10, 1.0e-10);

        for (double x = 0.0; x < 10.0; x += 0.1) {
            fitter.addWeightedPair(1.0, x,
                f.valueAt(x) + (0.01 * randomizer.nextGaussian()));
        }

        double[] coeffs = fitter.fit();

        HarmonicFunction fitted = new HarmonicFunction(coeffs[0], coeffs[1],
                coeffs[2]);
        assertTrue(Math.abs(coeffs[0] - f.getA()) < 1.0e-3);
        assertTrue(Math.abs(coeffs[1] - f.getOmega()) < 3.5e-3);
        assertTrue(Math.abs(coeffs[2] - center(f.getPhi(), coeffs[2])) < 2.0e-2);

        WeightedMeasurement[] measurements = fitter.getMeasurements();

        for (int i = 0; i < measurements.length; ++i) {
            WeightedMeasurement m = measurements[i];
            assertTrue(Math.abs(measurements[i].getMeasuredValue() -
                    m.getTheoreticalValue()) < 0.04);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws EstimationException DOCUMENT ME!
     */
    public void testUnsorted() throws EstimationException {
        Random randomizer = new Random(64925784252L);
        HarmonicFunction f = new HarmonicFunction(0.2, 3.4, 4.1);

        HarmonicFitter fitter = new HarmonicFitter(100, 1.0e-7, 1.0e-10, 1.0e-10);

        // build a regularly spaced array of measurements
        int size = 100;
        double[] xTab = new double[size];
        double[] yTab = new double[size];

        for (int i = 0; i < size; ++i) {
            xTab[i] = 0.1 * i;
            yTab[i] = f.valueAt(xTab[i]) + (0.01 * randomizer.nextGaussian());
        }

        // shake it
        for (int i = 0; i < size; ++i) {
            int i1 = randomizer.nextInt(size);
            int i2 = randomizer.nextInt(size);
            double xTmp = xTab[i1];
            double yTmp = yTab[i1];
            xTab[i1] = xTab[i2];
            yTab[i1] = yTab[i2];
            xTab[i2] = xTmp;
            yTab[i2] = yTmp;
        }

        // pass it to the fitter
        for (int i = 0; i < size; ++i) {
            fitter.addWeightedPair(1.0, xTab[i], yTab[i]);
        }

        double[] coeffs = fitter.fit();

        HarmonicFunction fitted = new HarmonicFunction(coeffs[0], coeffs[1],
                coeffs[2]);
        assertTrue(Math.abs(coeffs[0] - f.getA()) < 1.0e-3);
        assertTrue(Math.abs(coeffs[1] - f.getOmega()) < 3.5e-3);
        assertTrue(Math.abs(coeffs[2] - center(f.getPhi(), coeffs[2])) < 2.0e-2);

        WeightedMeasurement[] measurements = fitter.getMeasurements();

        for (int i = 0; i < measurements.length; ++i) {
            WeightedMeasurement m = measurements[i];
            assertTrue(Math.abs(m.getMeasuredValue() - m.getTheoreticalValue()) < 0.04);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(HarmonicFitterTest.class);
    }

    /**
     * Center an angle with respect to another one.
     *
     * @param a DOCUMENT ME!
     * @param ref DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static double center(double a, double ref) {
        double twoPi = Math.PI + Math.PI;

        return a - (twoPi * Math.floor(((a + Math.PI) - ref) / twoPi));
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class HarmonicFunction {
        /** DOCUMENT ME! */
        private double a;

        /** DOCUMENT ME! */
        private double omega;

        /** DOCUMENT ME! */
        private double phi;

/**
         * Creates a new HarmonicFunction object.
         *
         * @param a     DOCUMENT ME!
         * @param omega DOCUMENT ME!
         * @param phi   DOCUMENT ME!
         */
        public HarmonicFunction(double a, double omega, double phi) {
            this.a = a;
            this.omega = omega;
            this.phi = phi;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double valueAt(double x) {
            return a * Math.cos((omega * x) + phi);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double getA() {
            return a;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double getOmega() {
            return omega;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double getPhi() {
            return phi;
        }
    }
}
