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
public class HighamHall54IntegratorTest extends TestCase {
/**
     * Creates a new HighamHall54IntegratorTest object.
     *
     * @param name DOCUMENT ME!
     */
    public HighamHall54IntegratorTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     */
    public void testDimensionCheck() {
        try {
            TestProblem1 pb = new TestProblem1();
            HighamHall54Integrator integrator = new HighamHall54Integrator(0.0,
                    1.0, 1.0e-10, 1.0e-10);
            integrator.integrate(pb, 0.0, new double[pb.getDimension() + 10],
                1.0, new double[pb.getDimension() + 10]);
            fail("an exception should have been thrown");
        } catch (DerivativeException de) {
            fail("wrong exception caught");
        } catch (IntegratorException ie) {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws DerivativeException DOCUMENT ME!
     * @throws IntegratorException DOCUMENT ME!
     */
    public void testMinStep() throws DerivativeException, IntegratorException {
        try {
            TestProblem1 pb = new TestProblem1();
            double minStep = 0.1 * (pb.getFinalTime() - pb.getInitialTime());
            double maxStep = pb.getFinalTime() - pb.getInitialTime();
            double scalAbsoluteTolerance = 1.0e-15;
            double scalRelativeTolerance = 1.0e-15;

            FirstOrderIntegrator integ = new HighamHall54Integrator(minStep,
                    maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
            TestProblemHandler handler = new TestProblemHandler(pb);
            integ.setStepHandler(handler);
            integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
                pb.getFinalTime(), new double[pb.getDimension()]);
            fail("an exception should have been thrown");
        } catch (DerivativeException de) {
            fail("wrong exception caught");
        } catch (IntegratorException ie) {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws DerivativeException DOCUMENT ME!
     * @throws IntegratorException DOCUMENT ME!
     */
    public void testIncreasingTolerance()
        throws DerivativeException, IntegratorException {
        int previousCalls = Integer.MAX_VALUE;

        for (int i = -12; i < -2; ++i) {
            TestProblem1 pb = new TestProblem1();
            double minStep = 0;
            double maxStep = pb.getFinalTime() - pb.getInitialTime();
            double scalAbsoluteTolerance = Math.pow(10.0, i);
            double scalRelativeTolerance = 0.01 * scalAbsoluteTolerance;

            FirstOrderIntegrator integ = new HighamHall54Integrator(minStep,
                    maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
            TestProblemHandler handler = new TestProblemHandler(pb);
            integ.setStepHandler(handler);
            integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
                pb.getFinalTime(), new double[pb.getDimension()]);

            // the 1.3 factor is only valid for this test
            // and has been obtained from trial and error
            // there is no general relation between local and global errors
            assertTrue(handler.getMaximalError() < (1.3 * scalAbsoluteTolerance));

            int calls = pb.getCalls();
            assertTrue(calls <= previousCalls);
            previousCalls = calls;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws DerivativeException DOCUMENT ME!
     * @throws IntegratorException DOCUMENT ME!
     */
    public void testSwitchingFunctions()
        throws DerivativeException, IntegratorException {
        TestProblem4 pb = new TestProblem4();
        double minStep = 0;
        double maxStep = pb.getFinalTime() - pb.getInitialTime();
        double scalAbsoluteTolerance = 1.0e-8;
        double scalRelativeTolerance = 0.01 * scalAbsoluteTolerance;

        FirstOrderIntegrator integ = new HighamHall54Integrator(minStep,
                maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        TestProblemHandler handler = new TestProblemHandler(pb);
        integ.setStepHandler(handler);

        SwitchingFunction[] functions = pb.getSwitchingFunctions();

        if (functions != null) {
            for (int l = 0; l < functions.length; ++l) {
                integ.addSwitchingFunction(functions[l],
                    Double.POSITIVE_INFINITY, 1.0e-8 * maxStep);
            }
        }

        integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
            pb.getFinalTime(), new double[pb.getDimension()]);

        assertTrue(handler.getMaximalError() < 1.0e-7);
        assertEquals(12.0, handler.getLastTime(), 1.0e-8 * maxStep);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws DerivativeException DOCUMENT ME!
     * @throws IntegratorException DOCUMENT ME!
     */
    public void testKepler() throws DerivativeException, IntegratorException {
        final TestProblem3 pb = new TestProblem3(0.9);
        double minStep = 0;
        double maxStep = pb.getFinalTime() - pb.getInitialTime();
        double scalAbsoluteTolerance = 1.0e-8;
        double scalRelativeTolerance = scalAbsoluteTolerance;

        FirstOrderIntegrator integ = new HighamHall54Integrator(minStep,
                maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        integ.setStepHandler(new StepHandler() {
                private int nbSteps = 0;
                private double maxError = 0;

                public boolean requiresDenseOutput() {
                    return false;
                }

                public void reset() {
                    nbSteps = 0;
                    maxError = 0;
                }

                public void handleStep(StepInterpolator interpolator,
                    boolean isLast) {
                    ++nbSteps;

                    double[] interpolatedY = interpolator.getInterpolatedState();
                    double[] theoreticalY = pb.computeTheoreticalState(interpolator.getCurrentTime());
                    double dx = interpolatedY[0] - theoreticalY[0];
                    double dy = interpolatedY[1] - theoreticalY[1];
                    double error = (dx * dx) + (dy * dy);

                    if (error > maxError) {
                        maxError = error;
                    }

                    if (isLast) {
                        assertTrue(maxError < 1.54e-10);
                        assertTrue(nbSteps < 520);
                    }
                }
            });
        integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
            pb.getFinalTime(), new double[pb.getDimension()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(HighamHall54IntegratorTest.class);
    }
}
