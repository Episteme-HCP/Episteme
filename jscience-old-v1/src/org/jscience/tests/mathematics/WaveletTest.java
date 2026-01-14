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

package org.jscience.tests.mathematics;

import org.jscience.mathematics.WaveletMath;


/**
 * Testcase for wavelet methods.
 *
 * @author Mark Hale
 */
public class WaveletTest extends junit.framework.TestCase {
/**
     * Creates a new WaveletTest object.
     *
     * @param name DOCUMENT ME!
     */
    public WaveletTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        junit.textui.TestRunner.run(WaveletTest.class);
    }

    /**
     * DOCUMENT ME!
     */
    protected void setUp() {
        org.jscience.GlobalSettings.ZERO_TOL = 1.0e-6;
    }

    /**
     * This is a test that verifies perfect reconstruction for a signal
     * null near the boundaries (to avoid the effects of the zero-padding we
     * used).
     */
    public void testReconstruction() {
        double[] data = { 0, 0, 0, 1, 0, 0, 0, 0 };
        double[] filter = {
                0.48296291314, 0.8365163037, 0.224143868, -0.12940952255126
            };
        double[] lowpass = WaveletMath.downsample(filter, data);
        double[] filterh = {
                -0.12940952255126, -0.224143868, 0.8365163037, -0.48296291314
            };
        double[] highpass = WaveletMath.downsample(filterh, data);
        double[] reconstlow = WaveletMath.upsample(filter, lowpass);
        double[] reconsthigh = WaveletMath.upsample(filterh, highpass);
        double[] reconst = new double[data.length];

        for (int i = 0; i < data.length; i++) {
            reconst[i] = reconstlow[i] + reconsthigh[i];
            assertEquals(data[i], reconst[i],
                org.jscience.GlobalSettings.ZERO_TOL);
        }
    }

    /**
     * This is a test to verify interpolation from a Lagrange filter.
     */
    public void testLagrange() {
        double[] data = { 0, 0, 1, 0, 0 };
        double[] filter = { -0.0625, 0, 0.5625, 0, 1, 0, 0.5625, 0, -0.0625 };
        double[] interpol = WaveletMath.upsample(filter, data);

        for (int i = 0; i < data.length; i++)
            assertEquals(filter[i], interpol[i],
                org.jscience.GlobalSettings.ZERO_TOL);
    }
}
