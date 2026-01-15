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
public class GraggBulirschStoerIntegratorTest extends TestCase {
/**
     * Creates a new GraggBulirschStoerIntegratorTest object.
     *
     * @param name DOCUMENT ME!
     */
    public GraggBulirschStoerIntegratorTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     */
    public void testDimensionCheck() {
        try {
            TestProblem1 pb = new TestProblem1();
            GraggBulirschStoerIntegrator integrator = new GraggBulirschStoerIntegrator(0.0,
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
     */
    public void testNullIntervalCheck() {
        try {
            TestProblem1 pb = new TestProblem1();
            GraggBulirschStoerIntegrator integrator = new GraggBulirschStoerIntegrator(0.0,
                    1.0, 1.0e-10, 1.0e-10);
            integrator.integrate(pb, 0.0, new double[pb.getDimension()], 0.0,
                new double[pb.getDimension()]);
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
            double absTolerance = 1.0e-20;
            double relTolerance = 1.0e-20;

            FirstOrderIntegrator integ = new GraggBulirschStoerIntegrator(minStep,
                    maxStep, absTolerance, relTolerance);
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

        for (int i = -12; i < -4; ++i) {
            TestProblem1 pb = new TestProblem1();
            double minStep = 0;
            double maxStep = pb.getFinalTime() - pb.getInitialTime();
            double absTolerance = Math.pow(10.0, i);
            double relTolerance = absTolerance;

            FirstOrderIntegrator integ = new GraggBulirschStoerIntegrator(minStep,
                    maxStep, absTolerance, relTolerance);
            TestProblemHandler handler = new TestProblemHandler(pb);
            integ.setStepHandler(handler);
            integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
                pb.getFinalTime(), new double[pb.getDimension()]);

            // the coefficients are only valid for this test
            // and have been obtained from trial and error
            // there is no general relation between local and global errors
            double ratio = handler.getMaximalError() / absTolerance;
            assertTrue(ratio < 2.4);
            assertTrue(ratio > 0.02);

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
        double scalAbsoluteTolerance = 1.0e-10;
        double scalRelativeTolerance = 0.01 * scalAbsoluteTolerance;

        FirstOrderIntegrator integ = new GraggBulirschStoerIntegrator(minStep,
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

        assertTrue(handler.getMaximalError() < 5.0e-8);
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
        double absTolerance = 1.0e-6;
        double relTolerance = 1.0e-6;

        FirstOrderIntegrator integ = new GraggBulirschStoerIntegrator(minStep,
                maxStep, absTolerance, relTolerance);
        integ.setStepHandler(new StepHandler() {
                private int nbSteps = 0;
                private double maxError = 0;

                public boolean requiresDenseOutput() {
                    return true;
                }

                public void reset() {
                    nbSteps = 0;
                    maxError = 0;
                }

                public void handleStep(StepInterpolator interpolator,
                    boolean isLast) throws DerivativeException {
                    ++nbSteps;

                    for (int a = 1; a < 100; ++a) {
                        double prev = interpolator.getPreviousTime();
                        double curr = interpolator.getCurrentTime();
                        double interp = (((100 - a) * prev) + (a * curr)) / 100;
                        interpolator.setInterpolatedTime(interp);

                        double[] interpolatedY = interpolator.getInterpolatedState();
                        double[] theoreticalY = pb.computeTheoreticalState(interpolator.getInterpolatedTime());
                        double dx = interpolatedY[0] - theoreticalY[0];
                        double dy = interpolatedY[1] - theoreticalY[1];
                        double error = (dx * dx) + (dy * dy);

                        if (error > maxError) {
                            maxError = error;
                        }
                    }

                    if (isLast) {
                        assertTrue(maxError < 5.6e-7);
                        assertTrue(nbSteps < 70);
                    }
                }
            });
        integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
            pb.getFinalTime(), new double[pb.getDimension()]);

        assertTrue(pb.getCalls() < 2600);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws DerivativeException DOCUMENT ME!
     * @throws IntegratorException DOCUMENT ME!
     */
    public void testVariableSteps()
        throws DerivativeException, IntegratorException {
        final TestProblem3 pb = new TestProblem3(0.9);
        double minStep = 0;
        double maxStep = pb.getFinalTime() - pb.getInitialTime();
        double absTolerance = 1.0e-8;
        double relTolerance = 1.0e-8;
        FirstOrderIntegrator integ = new GraggBulirschStoerIntegrator(minStep,
                maxStep, absTolerance, relTolerance);
        integ.setStepHandler(new StepHandler() {
                private boolean firstTime = true;
                private double minStep = 0;
                private double maxStep = 0;

                public boolean requiresDenseOutput() {
                    return false;
                }

                public void reset() {
                    firstTime = true;
                    minStep = 0;
                    maxStep = 0;
                }

                public void handleStep(StepInterpolator interpolator,
                    boolean isLast) {
                    double step = Math.abs(interpolator.getCurrentTime() -
                            interpolator.getPreviousTime());

                    if (firstTime) {
                        minStep = Math.abs(step);
                        maxStep = minStep;
                        firstTime = false;
                    } else {
                        if (step < minStep) {
                            minStep = step;
                        }

                        if (step > maxStep) {
                            maxStep = step;
                        }
                    }

                    if (isLast) {
                        assertTrue(minStep < 5.0e-4);
                        assertTrue(maxStep > 1.5);
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
        return new TestSuite(GraggBulirschStoerIntegratorTest.class);
    }
}
