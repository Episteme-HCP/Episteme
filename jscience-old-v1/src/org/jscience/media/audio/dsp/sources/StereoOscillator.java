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
package org.jscience.media.audio.dsp.sources;

import org.jscience.media.audio.dsp.NegotiationListener;


// NOTE: 16 bit PCM data has a min value of -32768 (8000H)
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class StereoOscillator extends Oscillator {
    // Private class data
    /** DOCUMENT ME! */
    private int frequencyL;

    /** DOCUMENT ME! */
    private int frequencyR;

    /** DOCUMENT ME! */
    private int posL;

    /** DOCUMENT ME! */
    private int posR;

    /** DOCUMENT ME! */
    private boolean toggle;

    /** DOCUMENT ME! */
    private double leftAmplitudeAdj;

    /** DOCUMENT ME! */
    private double rightAmplitudeAdj;

/**
     * Creates a new StereoOscillator object.
     *
     * @param type        DOCUMENT ME!
     * @param frequencyL  DOCUMENT ME!
     * @param frequencyR  DOCUMENT ME!
     * @param sampleRate  DOCUMENT ME!
     * @param negComplete DOCUMENT ME!
     */
    public StereoOscillator(int type, int frequencyL, int frequencyR,
        int sampleRate, NegotiationListener negComplete) {
        super(type, 0, sampleRate, 2, negComplete);

        // Save incoming
        this.frequencyL = frequencyL;
        this.frequencyR = frequencyR;

        toggle = false;
        posL = posR = 0;

        leftAmplitudeAdj = 1.0;
        rightAmplitudeAdj = 1.0;
    }

    // Constructor with reasonable defaults
    /**
     * Creates a new StereoOscillator object.
     *
     * @param negComplete DOCUMENT ME!
     */
    public StereoOscillator(NegotiationListener negComplete) {
        this(SINEWAVE, 440, 880, 22050, negComplete);
    }

    /**
     * DOCUMENT ME!
     *
     * @param buffer DOCUMENT ME!
     * @param length DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSamples(short[] buffer, int length) {
        int sample = 0;
        int count = length;

        while (count-- != 0) {
            if (!toggle) {
                toggle = true;

                buffer[sample++] = (short) (leftAmplitudeAdj * waveTable[posL]);

                posL += frequencyL;

                if (posL >= sampleRate) {
                    posL -= sampleRate;
                }
            } else {
                toggle = false;

                buffer[sample++] = (short) (rightAmplitudeAdj * waveTable[posR]);

                posR += frequencyR;

                if (posR >= sampleRate) {
                    posR -= sampleRate;
                }
            }
        }

        return length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLeftFrequency() {
        return frequencyL;
    }

    /**
     * DOCUMENT ME!
     *
     * @param frequency DOCUMENT ME!
     */
    public void setLeftFrequency(int frequency) {
        this.frequencyL = frequency;

        // Reset waveTable index
        posL = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRightFrequency() {
        return frequencyR;
    }

    /**
     * DOCUMENT ME!
     *
     * @param frequency DOCUMENT ME!
     */
    public void setRightFrequency(int frequency) {
        this.frequencyR = frequency;

        // Reset waveTable index
        posR = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getLeftAmplitudeAdj() {
        return leftAmplitudeAdj;
    }

    /**
     * DOCUMENT ME!
     *
     * @param amplitudeAdj DOCUMENT ME!
     */
    public void setLeftAmplitudeAdj(double amplitudeAdj) {
        this.leftAmplitudeAdj = amplitudeAdj;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getRightAmplitudeAdj() {
        return rightAmplitudeAdj;
    }

    /**
     * DOCUMENT ME!
     *
     * @param amplitudeAdj DOCUMENT ME!
     */
    public void setRightAmplitudeAdj(double amplitudeAdj) {
        this.rightAmplitudeAdj = amplitudeAdj;
    }
}
