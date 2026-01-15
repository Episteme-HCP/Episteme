package org.jscience.tests.mathematics.analysis.ode;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.*;

import java.util.Random;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class EulerStepInterpolatorTest extends TestCase {
/**
     * Creates a new EulerStepInterpolatorTest object.
     *
     * @param name DOCUMENT ME!
     */
    public EulerStepInterpolatorTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     */
    public void testNoReset() {
        double[] y = { 0.0, 1.0, -2.0 };
        double[][] yDot = {
                { 1.0, 2.0, -2.0 }
            };
        EulerStepInterpolator interpolator = new EulerStepInterpolator();
        interpolator.reinitialize(new DummyEquations(), y, yDot, true);
        interpolator.storeTime(0);
        interpolator.shift();
        interpolator.storeTime(1);

        double[] result = interpolator.getInterpolatedState();

        for (int i = 0; i < result.length; ++i) {
            assertTrue(Math.abs(result[i] - y[i]) < 1.0e-10);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws DerivativeException DOCUMENT ME!
     */
    public void testInterpolationAtBounds() throws DerivativeException {
        double t0 = 0;
        double[] y0 = { 0.0, 1.0, -2.0 };

        double[] y = new double[y0.length];
        System.arraycopy(y0, 0, y, 0, y0.length);

        double[][] yDot = { new double[y0.length] };
        EulerStepInterpolator interpolator = new EulerStepInterpolator();
        interpolator.reinitialize(new DummyEquations(), y, yDot, true);
        interpolator.storeTime(t0);

        double dt = 1.0;
        y[0] = 1.0;
        y[1] = 3.0;
        y[2] = -4.0;
        yDot[0][0] = (y[0] - y0[0]) / dt;
        yDot[0][1] = (y[1] - y0[1]) / dt;
        yDot[0][2] = (y[2] - y0[2]) / dt;
        interpolator.shift();
        interpolator.storeTime(t0 + dt);

        interpolator.setInterpolatedTime(interpolator.getPreviousTime());

        double[] result = interpolator.getInterpolatedState();

        for (int i = 0; i < result.length; ++i) {
            assertTrue(Math.abs(result[i] - y0[i]) < 1.0e-10);
        }

        interpolator.setInterpolatedTime(interpolator.getCurrentTime());
        result = interpolator.getInterpolatedState();

        for (int i = 0; i < result.length; ++i) {
            assertTrue(Math.abs(result[i] - y[i]) < 1.0e-10);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws DerivativeException DOCUMENT ME!
     */
    public void testInterpolationInside() throws DerivativeException {
        double[] y = { 1.0, 3.0, -4.0 };
        double[][] yDot = {
                { 1.0, 2.0, -2.0 }
            };
        EulerStepInterpolator interpolator = new EulerStepInterpolator();
        interpolator.reinitialize(new DummyEquations(), y, yDot, true);
        interpolator.storeTime(0);
        interpolator.shift();
        interpolator.storeTime(1);

        interpolator.setInterpolatedTime(0.1);

        double[] result = interpolator.getInterpolatedState();
        assertTrue(Math.abs(result[0] - 0.1) < 1.0e-10);
        assertTrue(Math.abs(result[1] - 1.2) < 1.0e-10);
        assertTrue(Math.abs(result[2] + 2.2) < 1.0e-10);

        interpolator.setInterpolatedTime(0.5);
        result = interpolator.getInterpolatedState();
        assertTrue(Math.abs(result[0] - 0.5) < 1.0e-10);
        assertTrue(Math.abs(result[1] - 2.0) < 1.0e-10);
        assertTrue(Math.abs(result[2] + 3.0) < 1.0e-10);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws DerivativeException DOCUMENT ME!
     * @throws IntegratorException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws ClassNotFoundException DOCUMENT ME!
     */
    public void testSerialization()
        throws DerivativeException, IntegratorException, IOException,
            ClassNotFoundException {
        TestProblem1 pb = new TestProblem1();
        double step = (pb.getFinalTime() - pb.getInitialTime()) * 0.001;
        EulerIntegrator integ = new EulerIntegrator(step);
        integ.setStepHandler(new ContinuousOutputModel());
        integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
            pb.getFinalTime(), new double[pb.getDimension()]);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(integ.getStepHandler());

        assertTrue(bos.size() > 82000);
        assertTrue(bos.size() < 83000);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        ContinuousOutputModel cm = (ContinuousOutputModel) ois.readObject();

        Random random = new Random(347588535632L);
        double maxError = 0.0;

        for (int i = 0; i < 1000; ++i) {
            double r = random.nextDouble();
            double time = (r * pb.getInitialTime()) +
                ((1.0 - r) * pb.getFinalTime());
            cm.setInterpolatedTime(time);

            double[] interpolatedY = cm.getInterpolatedState();
            double[] theoreticalY = pb.computeTheoreticalState(time);
            double dx = interpolatedY[0] - theoreticalY[0];
            double dy = interpolatedY[1] - theoreticalY[1];
            double error = (dx * dx) + (dy * dy);

            if (error > maxError) {
                maxError = error;
            }
        }

        assertTrue(maxError < 0.001);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(EulerStepInterpolatorTest.class);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class DummyEquations implements FirstOrderDifferentialEquations {
        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getDimension() {
            return 0;
        }

        /**
         * DOCUMENT ME!
         *
         * @param t DOCUMENT ME!
         * @param y DOCUMENT ME!
         * @param yDot DOCUMENT ME!
         */
        public void computeDerivatives(double t, double[] y, double[] yDot) {
        }
    }
}
