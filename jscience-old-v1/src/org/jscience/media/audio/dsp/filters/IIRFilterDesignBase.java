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


// Base class for all IIR filters
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public abstract class IIRFilterDesignBase {
    // Private class data
    /** DOCUMENT ME! */
    protected int frequency;

    /** DOCUMENT ME! */
    protected int sampleRate;

    /** DOCUMENT ME! */
    protected double parameter;

    /** DOCUMENT ME! */
    protected double alpha;

    /** DOCUMENT ME! */
    protected double beta;

    /** DOCUMENT ME! */
    protected double gamma;

/**
     * Creates a new IIRFilterDesignBase object.
     *
     * @param frequency  DOCUMENT ME!
     * @param sampleRate DOCUMENT ME!
     * @param parameter  DOCUMENT ME!
     */
    public IIRFilterDesignBase(int frequency, int sampleRate, double parameter) {
        // Save incoming
        this.frequency = frequency;
        this.sampleRate = sampleRate;

        // Damping factor for highpass and lowpass, q for bandpass
        this.parameter = parameter;
    }

    // Given a frequency of interest, calculate radians/sample
    /**
     * DOCUMENT ME!
     *
     * @param freq DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected double calcRadiansPerSample(double freq) {
        return (2.0 * Math.PI * freq) / sampleRate;
    }

    // Return the radians per sample at the frequency of interest
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected double getThetaZero() {
        return calcRadiansPerSample(frequency);
    }

    // Do the design of the filter. Filter response controlled by
    /**
     * DOCUMENT ME!
     */
    public abstract void doFilterDesign();

    // Print all three IIR coefficients
    /**
     * DOCUMENT ME!
     */
    public void printCoefficients() {
        System.out.println("Filter Specifications:");
        System.out.println("\tSample Rate: " + sampleRate + ", Frequency: " +
            frequency + ", d/q: " + parameter);

        System.out.println("\tAlpha: " + alpha);
        System.out.println("\tBeta: " + beta);
        System.out.println("\tGamma: " + gamma);
    }

    // Return alpha coefficient
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAlpha() {
        return alpha;
    }

    // Return beta coefficient
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getBeta() {
        return beta;
    }

    // Return gamma coefficient
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getGamma() {
        return gamma;
    }
}
