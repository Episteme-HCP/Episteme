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
package org.jscience.media.audio.dsp.processors;

import org.jscience.media.audio.dsp.AbstractAudio;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Phaser extends AbstractAudio {
    // Private class data
    /** DOCUMENT ME! */
    private double thisOut1;

    // Private class data
    /** DOCUMENT ME! */
    private double thisOut2;

    // Private class data
    /** DOCUMENT ME! */
    private double thisOut3;

    // Private class data
    /** DOCUMENT ME! */
    private double thisOut4;

    /** DOCUMENT ME! */
    private double prevIn1;

    /** DOCUMENT ME! */
    private double prevIn2;

    /** DOCUMENT ME! */
    private double prevIn3;

    /** DOCUMENT ME! */
    private double prevIn4;

    /** DOCUMENT ME! */
    private double leftThisOut1;

    /** DOCUMENT ME! */
    private double leftThisOut2;

    /** DOCUMENT ME! */
    private double leftThisOut3;

    /** DOCUMENT ME! */
    private double leftThisOut4;

    /** DOCUMENT ME! */
    private double rightThisOut1;

    /** DOCUMENT ME! */
    private double rightThisOut2;

    /** DOCUMENT ME! */
    private double rightThisOut3;

    /** DOCUMENT ME! */
    private double rightThisOut4;

    /** DOCUMENT ME! */
    private double leftPrevIn1;

    /** DOCUMENT ME! */
    private double leftPrevIn2;

    /** DOCUMENT ME! */
    private double leftPrevIn3;

    /** DOCUMENT ME! */
    private double leftPrevIn4;

    /** DOCUMENT ME! */
    private double rightPrevIn1;

    /** DOCUMENT ME! */
    private double rightPrevIn2;

    /** DOCUMENT ME! */
    private double rightPrevIn3;

    /** DOCUMENT ME! */
    private double rightPrevIn4;

    /** DOCUMENT ME! */
    private double sweepRate;

    /** DOCUMENT ME! */
    private double sweepRange;

    /** DOCUMENT ME! */
    private double baseFreq;

    /** DOCUMENT ME! */
    private double wp;

    /** DOCUMENT ME! */
    private double minWp;

    /** DOCUMENT ME! */
    private double maxWp;

    /** DOCUMENT ME! */
    private double step;

    /** DOCUMENT ME! */
    private double currentStep;

    /** DOCUMENT ME! */
    private double sweepValue = 0;

    /** DOCUMENT ME! */
    private boolean invertPhase;

    /** DOCUMENT ME! */
    private int dryLevel;

    /** DOCUMENT ME! */
    private int wetLevel;

    /** DOCUMENT ME! */
    private int feedbackLevel;

    /** DOCUMENT ME! */
    private int sampleRate = 0;

    /** DOCUMENT ME! */
    private int numberOfChannels = 0;

    /** DOCUMENT ME! */
    private boolean initializationComplete;

/**
     * Creates a new Phaser object.
     */
    public Phaser() {
        super("Phaser", AbstractAudio.PROCESSOR);

        initializationComplete = false;
        invertPhase = false;
    }

    // Process the samples that pass thru this effect
    /**
     * DOCUMENT ME!
     *
     * @param buffer DOCUMENT ME!
     * @param length DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSamples(short[] buffer, int length) {
        int len = previous.getSamples(buffer, length);

        if (getByPass() || !initializationComplete) {
            return len;
        }

        // Not in bypass mode, process the samples
        if (numberOfChannels == 1) {
            return processMonoSamples(buffer, len);
        } else {
            return processStereoSamples(buffer, len);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param buffer DOCUMENT ME!
     * @param len DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected int processMonoSamples(short[] buffer, int len) {
        // Do the processing
        for (int i = 0; i < len; i++) {
            // Calculate A in difference equation
            double A = (1.0 - wp) / (1.0 + wp);

            int inSample = (int) buffer[i];

            double in = inSample +
                (((invertPhase ? (-1) : 1) * feedbackLevel * thisOut4) / 100.0);

            // Do the first allpass filter
            thisOut1 = (A * (in + thisOut1)) - prevIn1;
            prevIn1 = in;

            // Do the second allpass filter
            thisOut2 = (A * (thisOut1 + thisOut2)) - prevIn2;
            prevIn2 = thisOut1;

            // Do the third allpass filter
            thisOut3 = (A * (thisOut2 + thisOut3)) - prevIn3;
            prevIn3 = thisOut2;

            // Do the forth allpass filter
            thisOut4 = (A * (thisOut3 + thisOut4)) - prevIn4;
            prevIn4 = thisOut3;

            double outSample = ((thisOut4 * wetLevel) / 100.0) +
                ((inSample * dryLevel) / 100.0);

            // Clip output to legal levels
            if (outSample > 32767.0) {
                outSample = 32767;
            } else if (outSample < -32768.0) {
                outSample = -32768;
            }

            buffer[i] = (short) outSample;

            // Update sweep
            wp *= currentStep; // Apply step value

            if (wp > maxWp) { // Exceed max Wp ?
                currentStep = 1.0 / step;
            } else if (wp < minWp) { // Exceed min Wp ?
                currentStep = step;
            }
        }

        return len;
    }

    /**
     * DOCUMENT ME!
     *
     * @param buffer DOCUMENT ME!
     * @param len DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected int processStereoSamples(short[] buffer, int len) {
        // Do the processing
        for (int i = 0; i < (len / 2); i++) {
            // Calculate A in difference equation
            double A = (1.0 - wp) / (1.0 + wp);

            int leftInSample = (int) buffer[2 * i];
            int rightInSample = (int) buffer[(2 * i) + 1];

            double leftIn = leftInSample +
                (((invertPhase ? (-1) : 1) * feedbackLevel * leftThisOut4) / 100.0);

            double rightIn = rightInSample +
                (((invertPhase ? (-1) : 1) * feedbackLevel * rightThisOut4) / 100.0);

            // Do the first allpass filter - left channel
            leftThisOut1 = (A * (leftIn + leftThisOut1)) - leftPrevIn1;
            leftPrevIn1 = leftIn;

            // Do the first allpass filter - right channel
            rightThisOut1 = (A * (rightIn + rightThisOut1)) - rightPrevIn1;
            rightPrevIn1 = rightIn;

            // Do the second allpass filter - left channel
            leftThisOut2 = (A * (leftThisOut1 + leftThisOut2)) - leftPrevIn2;
            leftPrevIn2 = leftThisOut1;

            // Do the second allpass filter - right channel
            rightThisOut2 = (A * (rightThisOut1 + rightThisOut2)) -
                rightPrevIn2;
            rightPrevIn2 = rightThisOut1;

            // Do the third allpass filter - left channel
            leftThisOut3 = (A * (leftThisOut2 + leftThisOut3)) - leftPrevIn3;
            leftPrevIn3 = leftThisOut2;

            // Do the third allpass filter - right channel
            rightThisOut3 = (A * (rightThisOut2 + rightThisOut3)) -
                rightPrevIn3;
            rightPrevIn3 = rightThisOut2;

            // Do the forth allpass filter - left channel
            leftThisOut4 = (A * (leftThisOut3 + leftThisOut4)) - leftPrevIn4;
            leftPrevIn4 = leftThisOut3;

            // Do the forth allpass filter - right channel
            rightThisOut4 = (A * (rightThisOut3 + rightThisOut4)) -
                rightPrevIn4;
            rightPrevIn4 = rightThisOut3;

            double leftOutSample = ((leftThisOut4 * wetLevel) / 100.0) +
                ((leftInSample * dryLevel) / 100.0);

            double rightOutSample = ((rightThisOut4 * wetLevel) / 100.0) +
                ((rightInSample * dryLevel) / 100.0);

            // Clip output to legal levels
            if (leftOutSample > 32767.0) {
                leftOutSample = 32767;
            } else if (leftOutSample < -32768.0) {
                leftOutSample = -32768;
            }

            if (rightOutSample > 32767.0) {
                rightOutSample = 32767;
            } else if (rightOutSample < -32768.0) {
                rightOutSample = -32768;
            }

            buffer[2 * i] = (short) leftOutSample;
            buffer[(2 * i) + 1] = (short) rightOutSample;

            // Update sweep
            wp *= currentStep; // Apply step value

            if (wp > maxWp) { // Exceed max Wp ?
                currentStep = 1.0 / step;
            } else if (wp < minWp) { // Exceed min Wp ?
                currentStep = step;
            }
        }

        return len;
    }

    /**
     * DOCUMENT ME!
     *
     * @param sweepRate DOCUMENT ME!
     */
    public void setSweepRate(double sweepRate) {
        this.sweepRate = sweepRate;

        // Redo initialization
        doInitialization();
    }

    /**
     * DOCUMENT ME!
     *
     * @param sweepRange DOCUMENT ME!
     */
    public void setSweepRange(double sweepRange) {
        this.sweepRange = sweepRange;

        // Redo initialization
        doInitialization();
    }

    // Mode is either sin or triangle
    /**
     * DOCUMENT ME!
     *
     * @param baseFreq DOCUMENT ME!
     */
    public void setBaseFreq(double baseFreq) {
        this.baseFreq = baseFreq;

        // Redo initialization
        doInitialization();
    }

    /**
     * DOCUMENT ME!
     *
     * @param dryLevel DOCUMENT ME!
     */
    public void setDryLevel(int dryLevel) {
        this.dryLevel = dryLevel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param wetLevel DOCUMENT ME!
     */
    public void setWetLevel(int wetLevel) {
        this.wetLevel = wetLevel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param invertPhase DOCUMENT ME!
     */
    public void setFeedbackPhase(boolean invertPhase) {
        this.invertPhase = invertPhase;
    }

    /**
     * DOCUMENT ME!
     *
     * @param feedbackLevel DOCUMENT ME!
     */
    public void setFeedbackLevel(int feedbackLevel) {
        this.feedbackLevel = feedbackLevel;
    }

    /**
     * DOCUMENT ME!
     */
    public void doInitialization() {
        // Cannot initialize until sample rate is known
        if (sampleRate != 0) {
            wp = minWp = (2.0 * Math.PI * baseFreq) / sampleRate;

            // Convert octave range to freq range
            double freqRange = Math.pow(2.0, sweepRange);

            maxWp = minWp * freqRange;

            currentStep = step = Math.pow(freqRange,
                        sweepRate / (sampleRate / 2.0));

            // Indicate initialization is complete
            initializationComplete = true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param min DOCUMENT ME!
     * @param max DOCUMENT ME!
     * @param preferred DOCUMENT ME!
     */
    public void minMaxSamplingRate(int min, int max, int preferred) {
        super.minMaxSamplingRate(min, max, preferred);
        sampleRate = preferred;
        doInitialization();
    }

    // Negotiate the number of channels
    /**
     * DOCUMENT ME!
     *
     * @param min DOCUMENT ME!
     * @param max DOCUMENT ME!
     * @param preferred DOCUMENT ME!
     */
    public void minMaxChannels(int min, int max, int preferred) {
        super.minMaxChannels(min, max, preferred);
        numberOfChannels = preferred;
    }
}
