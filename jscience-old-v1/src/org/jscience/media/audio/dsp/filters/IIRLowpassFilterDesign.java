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


// The IIR lowpass filter designed here has unity gain
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class IIRLowpassFilterDesign extends IIRFilterDesignBase {
/**
     * Creates a new IIRLowpassFilterDesign object.
     *
     * @param cutoffFrequency DOCUMENT ME!
     * @param sampleRate      DOCUMENT ME!
     * @param dampingFactor   DOCUMENT ME!
     */
    public IIRLowpassFilterDesign(int cutoffFrequency, int sampleRate,
        double dampingFactor) {
        super(cutoffFrequency, sampleRate, dampingFactor);
    }

    // Do the design of the filter. Filter response controlled by
    /**
     * DOCUMENT ME!
     */
    public void doFilterDesign() {
        // Get radians per sample at cutoff frequency
        double thetaZero = getThetaZero();

        double theSin = parameter / (2.0 * Math.sin(thetaZero));

        // Beta relates gain to cutoff freq
        beta = 0.5 * ((1.0 - theSin) / (1.0 + theSin));

        // Final filter coefficient
        gamma = (0.5 + beta) * Math.cos(thetaZero);

        // For unity gain
        alpha = ((0.5 + beta) - gamma) / 4.0;
    }

    // Entry point for testing
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("\nIIR Lowpass Filter Design Program");
            System.out.println("\nUsage:");
            System.out.println("\tIIRLowpassFilterDesign " +
                "cutoffFrequency sampleRate dampingFactor");

            System.exit(1);
        }

        // Parse the command line arguments
        int cutoffFrequency = new Integer(args[0]).intValue();
        int sampleRate = new Integer(args[1]).intValue();
        double dampingFactor = new Double(args[2]).doubleValue();

        // Instantiate highpass filter designer
        IIRLowpassFilterDesign d = new IIRLowpassFilterDesign(cutoffFrequency,
                sampleRate, dampingFactor);

        // Do the design
        d.doFilterDesign();

        // Print out the coefficients
        d.printCoefficients();
    }
}
