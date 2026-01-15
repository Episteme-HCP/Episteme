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
public class DormandPrince54IntegratorTest extends TestCase {
/**
     * Creates a new DormandPrince54IntegratorTest object.
     *
     * @param name DOCUMENT ME!
     */
    public DormandPrince54IntegratorTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     */
    public void testDimensionCheck() {
        try {
            TestProblem1 pb = new TestProblem1();
            DormandPrince54Integrator integrator = new DormandPrince54Integrator(0.0,
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

            FirstOrderIntegrator integ = new DormandPrince54Integrator(minStep,
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
    public void testSmallLastStep()
        throws DerivativeException, IntegratorException {
        TestProblemAbstract pb = new TestProblem5();
        double minStep = 1.25;
        double maxStep = Math.abs(pb.getFinalTime() - pb.getInitialTime());
        double scalAbsoluteTolerance = 6.0e-4;
        double scalRelativeTolerance = 6.0e-4;

        AdaptiveStepsizeIntegrator integ = new DormandPrince54Integrator(minStep,
                maxStep, scalAbsoluteTolerance, scalRelativeTolerance);

        DP54SmallLastHandler handler = new DP54SmallLastHandler(minStep);
        integ.setStepHandler(handler);
        integ.setInitialStepSize(1.7);
        integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
            pb.getFinalTime(), new double[pb.getDimension()]);
        assertTrue(handler.wasLastSeen());
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

            FirstOrderIntegrator integ = new DormandPrince54Integrator(minStep,
                    maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
            TestProblemHandler handler = new TestProblemHandler(pb);
            integ.setStepHandler(handler);
            integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
                pb.getFinalTime(), new double[pb.getDimension()]);

            // the 0.7 factor is only valid for this test
            // and has been obtained from trial and error
            // there is no general relation between local and global errors
            assertTrue(handler.getMaximalError() < (0.7 * scalAbsoluteTolerance));

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

        FirstOrderIntegrator integ = new DormandPrince54Integrator(minStep,
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

        assertTrue(handler.getMaximalError() < 5.0e-6);
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

        FirstOrderIntegrator integ = new DormandPrince54Integrator(minStep,
                maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
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

                    for (int a = 1; a < 10; ++a) {
                        double prev = interpolator.getPreviousTime();
                        double curr = interpolator.getCurrentTime();
                        double interp = (((10 - a) * prev) + (a * curr)) / 10;
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
                        assertTrue(maxError < 7.0e-10);
                        assertTrue(nbSteps < 400);
                    }
                }
            });
        integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
            pb.getFinalTime(), new double[pb.getDimension()]);

        assertTrue(pb.getCalls() < 2800);
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
        double scalAbsoluteTolerance = 1.0e-8;
        double scalRelativeTolerance = scalAbsoluteTolerance;

        FirstOrderIntegrator integ = new DormandPrince54Integrator(minStep,
                maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
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
                        assertTrue(minStep < (1.0 / 450.0));
                        assertTrue(maxStep > (1.0 / 4.2));
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
        return new TestSuite(DormandPrince54IntegratorTest.class);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class DP54SmallLastHandler implements StepHandler {
        /** DOCUMENT ME! */
        private boolean lastSeen;

        /** DOCUMENT ME! */
        private double minStep;

/**
         * Creates a new DP54SmallLastHandler object.
         *
         * @param minStep DOCUMENT ME!
         */
        public DP54SmallLastHandler(double minStep) {
            lastSeen = false;
            this.minStep = minStep;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean requiresDenseOutput() {
            return false;
        }

        /**
         * DOCUMENT ME!
         */
        public void reset() {
        }

        /**
         * DOCUMENT ME!
         *
         * @param interpolator DOCUMENT ME!
         * @param isLast DOCUMENT ME!
         */
        public void handleStep(StepInterpolator interpolator, boolean isLast) {
            if (isLast) {
                lastSeen = true;

                double h = interpolator.getCurrentTime() -
                    interpolator.getPreviousTime();
                assertTrue(Math.abs(h) < minStep);
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean wasLastSeen() {
            return lastSeen;
        }
    }
}
