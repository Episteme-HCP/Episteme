package org.jscience.tests.mathematics.analysis.ode;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jscience.tests.mathematics.analysis.estimation.EstimationException;
import org.jscience.tests.mathematics.analysis.fitting.PolynomialFitter;

import java.io.*;

import java.util.Random;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class MidpointStepInterpolatorTest extends TestCase {
/**
     * Creates a new MidpointStepInterpolatorTest object.
     *
     * @param name DOCUMENT ME!
     */
    public MidpointStepInterpolatorTest(String name) {
        super(name);
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
        MidpointIntegrator integ = new MidpointIntegrator(step);
        integ.setStepHandler(new ContinuousOutputModel());
        integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
            pb.getFinalTime(), new double[pb.getDimension()]);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(integ.getStepHandler());

        assertTrue(bos.size() > 98000);
        assertTrue(bos.size() < 99000);

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

        assertTrue(maxError < 1.0e-6);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(MidpointStepInterpolatorTest.class);
    }
}
