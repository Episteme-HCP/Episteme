package org.jscience.tests.mathematics.analysis.fitting;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jscience.tests.mathematics.analysis.estimation.EstimatedParameter;
import org.jscience.tests.mathematics.analysis.estimation.WeightedMeasurement;

import java.util.Random;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class AbstractCurveFitterTest extends TestCase {
    /** DOCUMENT ME! */
    private DummyFitter fitter;

/**
     * Creates a new AbstractCurveFitterTest object.
     *
     * @param name DOCUMENT ME!
     */
    public AbstractCurveFitterTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     */
    public void testAlreadySorted() {
        for (double x = 0.0; x < 100.0; x += 1.0) {
            fitter.addWeightedPair(1.0, x, 0.0);
        }

        checkSorted();
    }

    /**
     * DOCUMENT ME!
     */
    public void testReversed() {
        for (double x = 0.0; x < 100.0; x += 1.0) {
            fitter.addWeightedPair(1.0, 100.0 - x, 0.0);
        }

        checkSorted();
    }

    /**
     * DOCUMENT ME!
     */
    public void testRandom() {
        Random randomizer = new Random(86757343594L);

        for (int i = 0; i < 100; ++i) {
            fitter.addWeightedPair(1.0, 10.0 * randomizer.nextDouble(), 0.0);
        }

        checkSorted();
    }

    /**
     * DOCUMENT ME!
     */
    public void checkSorted() {
        fitter.doSort();

        WeightedMeasurement[] measurements = fitter.getMeasurements();

        for (int i = 1; i < measurements.length; ++i) {
            AbstractCurveFitter.FitMeasurement m1 = (AbstractCurveFitter.FitMeasurement) measurements[i -
                1];
            AbstractCurveFitter.FitMeasurement m2 = (AbstractCurveFitter.FitMeasurement) measurements[i];
            assertTrue(m1.x <= m2.x);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(AbstractCurveFitterTest.class);
    }

    /**
     * DOCUMENT ME!
     */
    public void setUp() {
        fitter = new DummyFitter();
    }

    /**
     * DOCUMENT ME!
     */
    public void tearOff() {
        fitter = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class DummyFitter extends AbstractCurveFitter {
/**
         * Creates a new DummyFitter object.
         */
        public DummyFitter() {
            super(10, 10, 0.0, 0.0, 0.0);
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double valueAt(double x) {
            return 0.0;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         * @param p DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double partial(double x, EstimatedParameter p) {
            return 0.0;
        }

        /**
         * DOCUMENT ME!
         */
        public void doSort() {
            sortMeasurements();
        }
    }
}
