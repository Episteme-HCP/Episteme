/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
public class ThreeEighthesStepInterpolatorTest extends TestCase {
/**
     * Creates a new ThreeEighthesStepInterpolatorTest object.
     *
     * @param name DOCUMENT ME!
     */
    public ThreeEighthesStepInterpolatorTest(String name) {
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
        TestProblem3 pb = new TestProblem3(0.9);
        double step = (pb.getFinalTime() - pb.getInitialTime()) * 0.0003;
        ThreeEighthesIntegrator integ = new ThreeEighthesIntegrator(step);
        integ.setStepHandler(new ContinuousOutputModel());
        integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
            pb.getFinalTime(), new double[pb.getDimension()]);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(integ.getStepHandler());

        assertTrue(bos.size() > 700000);
        assertTrue(bos.size() < 701000);

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

        assertTrue(maxError > 0.005);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(ThreeEighthesStepInterpolatorTest.class);
    }
}
