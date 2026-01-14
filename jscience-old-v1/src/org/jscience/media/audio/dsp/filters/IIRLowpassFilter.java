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

// This code is repackaged after the code from Craig A. Lindley, from Digital Audio with Java
// Site ftp://ftp.prenhall.com/pub/ptr/professional_computer_science.w-022/digital_audio/
// Email
package org.jscience.media.audio.dsp.filters;


// Optimized IIR lowpass filter used as shelving EQ
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class IIRLowpassFilter extends IIRFilterBase {
    // IIRLowpassFilter class constructor
    /**
     * Creates a new IIRLowpassFilter object.
     *
     * @param alpha DOCUMENT ME!
     * @param beta DOCUMENT ME!
     * @param gamma DOCUMENT ME!
     */
    public IIRLowpassFilter(double alpha, double beta, double gamma) {
        super(alpha, beta, gamma);
    }

    // Filter coefficients can also be extracted by passing in
    /**
     * Creates a new IIRLowpassFilter object.
     *
     * @param fd DOCUMENT ME!
     */
    public IIRLowpassFilter(IIRLowpassFilterDesign fd) {
        super(fd);
    }

    // Run the filter algorithm
    /**
     * DOCUMENT ME!
     *
     * @param inBuffer DOCUMENT ME!
     * @param outBuffer DOCUMENT ME!
     * @param length DOCUMENT ME!
     */
    public void doFilter(short[] inBuffer, double[] outBuffer, int length) {
        for (int index = 0; index < length; index++) {
            // Fetch sample
            inArray[iIndex] = (double) inBuffer[index];

            // Do indices maintainance
            jIndex = iIndex - 2;

            if (jIndex < 0) {
                jIndex += HISTORYSIZE;
            }

            kIndex = iIndex - 1;

            if (kIndex < 0) {
                kIndex += HISTORYSIZE;
            }

            // Run the lowpass difference equation
            double out = outArray[iIndex] = 2.0 * (((alpha * (inArray[iIndex] +
                    (2 * inArray[kIndex]) + inArray[jIndex])) +
                    (gamma * outArray[kIndex])) - (beta * outArray[jIndex]));

            outBuffer[index] += (amplitudeAdj * out);

            iIndex = (iIndex + 1) % HISTORYSIZE;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param inBuffer DOCUMENT ME!
     * @param outBuffer DOCUMENT ME!
     * @param length DOCUMENT ME!
     */
    public void doFilterNoSum(short[] inBuffer, double[] outBuffer, int length) {
        for (int index = 0; index < length; index++) {
            // Fetch sample
            inArray[iIndex] = (double) inBuffer[index];

            // Do indices maintainance
            jIndex = iIndex - 2;

            if (jIndex < 0) {
                jIndex += HISTORYSIZE;
            }

            kIndex = iIndex - 1;

            if (kIndex < 0) {
                kIndex += HISTORYSIZE;
            }

            // Run the lowpass difference equation
            double out = outArray[iIndex] = 2.0 * (((alpha * (inArray[iIndex] +
                    (2 * inArray[kIndex]) + inArray[jIndex])) +
                    (gamma * outArray[kIndex])) - (beta * outArray[jIndex]));

            outBuffer[index] = out;

            iIndex = (iIndex + 1) % HISTORYSIZE;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param inBuffer DOCUMENT ME!
     * @param outBuffer DOCUMENT ME!
     * @param length DOCUMENT ME!
     */
    public void doFilterNoSum(double[] inBuffer, double[] outBuffer, int length) {
        for (int index = 0; index < length; index++) {
            // Fetch sample
            inArray[iIndex] = inBuffer[index];

            // Do indices maintainance
            jIndex = iIndex - 2;

            if (jIndex < 0) {
                jIndex += HISTORYSIZE;
            }

            kIndex = iIndex - 1;

            if (kIndex < 0) {
                kIndex += HISTORYSIZE;
            }

            // Run the lowpass difference equation
            double out = outArray[iIndex] = 2.0 * (((alpha * (inArray[iIndex] +
                    (2 * inArray[kIndex]) + inArray[jIndex])) +
                    (gamma * outArray[kIndex])) - (beta * outArray[jIndex]));

            outBuffer[index] = out;

            iIndex = (iIndex + 1) % HISTORYSIZE;
        }
    }
}
