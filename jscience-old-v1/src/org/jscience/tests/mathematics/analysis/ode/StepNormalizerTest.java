package org.jscience.tests.mathematics.analysis.ode;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jscience.tests.mathematics.analysis.estimation.EstimationException;
import org.jscience.tests.mathematics.analysis.fitting.PolynomialFitter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class StepNormalizerTest extends TestCase {
    /** DOCUMENT ME! */
    TestProblem3 pb;

    /** DOCUMENT ME! */
    FirstOrderIntegrator integ;

    /** DOCUMENT ME! */
    boolean lastSeen;

/**
     * Creates a new StepNormalizerTest object.
     *
     * @param name DOCUMENT ME!
     */
    public StepNormalizerTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws DerivativeException DOCUMENT ME!
     * @throws IntegratorException DOCUMENT ME!
     */
    public void testBoundaries()
        throws DerivativeException, IntegratorException {
        double range = pb.getFinalTime() - pb.getInitialTime();
        setLastSeen(false);
        integ.setStepHandler(new StepNormalizer(range / 10.0,
                new FixedStepHandler() {
                private boolean firstCall = true;

                public void handleStep(double t, double[] y, boolean isLast) {
                    if (firstCall) {
                        checkValue(t, pb.getInitialTime());
                        firstCall = false;
                    }

                    if (isLast) {
                        setLastSeen(true);
                        checkValue(t, pb.getFinalTime());
                    }
                }
            }));
        integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
            pb.getFinalTime(), new double[pb.getDimension()]);
        assertTrue(lastSeen);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws DerivativeException DOCUMENT ME!
     * @throws IntegratorException DOCUMENT ME!
     */
    public void testBeforeEnd() throws DerivativeException, IntegratorException {
        final double range = pb.getFinalTime() - pb.getInitialTime();
        setLastSeen(false);
        integ.setStepHandler(new StepNormalizer(range / 10.5,
                new FixedStepHandler() {
                public void handleStep(double t, double[] y, boolean isLast) {
                    if (isLast) {
                        setLastSeen(true);
                        checkValue(t, pb.getFinalTime() - (range / 21.0));
                    }
                }
            }));
        integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
            pb.getFinalTime(), new double[pb.getDimension()]);
        assertTrue(lastSeen);
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     * @param reference DOCUMENT ME!
     */
    public void checkValue(double value, double reference) {
        assertTrue(Math.abs(value - reference) < 1.0e-10);
    }

    /**
     * DOCUMENT ME!
     *
     * @param lastSeen DOCUMENT ME!
     */
    public void setLastSeen(boolean lastSeen) {
        this.lastSeen = lastSeen;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(StepNormalizerTest.class);
    }

    /**
     * DOCUMENT ME!
     */
    public void setUp() {
        pb = new TestProblem3(0.9);

        double minStep = 0;
        double maxStep = pb.getFinalTime() - pb.getInitialTime();
        integ = new DormandPrince54Integrator(minStep, maxStep, 10.e-8, 1.0e-8);
        lastSeen = false;
    }

    /**
     * DOCUMENT ME!
     */
    public void tearDown() {
        pb = null;
        integ = null;
    }
}
