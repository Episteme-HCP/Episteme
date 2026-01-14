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


// This version of CompExpWithUI.java illustrates implementation
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class CompExpHard extends AbstractAudio {
    /** DOCUMENT ME! */
    private static final double MAXTHRESHOLDDB = 0;

    /** DOCUMENT ME! */
    private static final double MINTHRESHOLDDB = -60;

    /** DOCUMENT ME! */
    public static final double THRESHOLDDEF = -16;

    /** DOCUMENT ME! */
    private static final double MAXBTRATIO = 1.0;

    /** DOCUMENT ME! */
    public static final double MINBTRATIO = 25.0;

    /** DOCUMENT ME! */
    public static final double BTRATIODEF = 1.0;

    /** DOCUMENT ME! */
    public static final double MAXATRATIO = +11.0;

    /** DOCUMENT ME! */
    public static final double MINATRATIO = -11.0;

    /** DOCUMENT ME! */
    public static final double ATRATIODEF = 0.0;

    /** DOCUMENT ME! */
    private static final double MAXATTACKMS = 500;

    /** DOCUMENT ME! */
    private static final double MINATTACKMS = 0;

    /** DOCUMENT ME! */
    public static final double ATTACKMSDEF = 50;

    /** DOCUMENT ME! */
    private static final double MAXRELEASEMS = 2000;

    /** DOCUMENT ME! */
    private static final double MINRELEASEMS = 0;

    /** DOCUMENT ME! */
    public static final double RELEASEMSDEF = 100;

    /** DOCUMENT ME! */
    public static final double MAXGAININDB = +12.0;

    /** DOCUMENT ME! */
    private static final double MINGAININDB = -12.0;

    /** DOCUMENT ME! */
    public static final double GAINDBDEF = 0.0;

    // Private class data
    /** DOCUMENT ME! */
    private boolean initializationComplete;

    /** DOCUMENT ME! */
    private int sampleRate = 0;

    /** DOCUMENT ME! */
    private int channels = 1;

    /** DOCUMENT ME! */
    private double thresholdValue = 32767.0;

    /** DOCUMENT ME! */
    private double btRatio = 1.0;

    /** DOCUMENT ME! */
    private double atRatio = 1.0;

    /** DOCUMENT ME! */
    private double attackInMs = 0;

    /** DOCUMENT ME! */
    private double releaseInMs = 0;

    /** DOCUMENT ME! */
    private double attackCount = 0;

    /** DOCUMENT ME! */
    private double releaseCount = 0;

    /** DOCUMENT ME! */
    private double gain = 1.0;

    /** DOCUMENT ME! */
    private boolean limiting = false;

    /** DOCUMENT ME! */
    private boolean gating = false;

    /** DOCUMENT ME! */
    private int calcAttackCount = 0;

    /** DOCUMENT ME! */
    private int calcReleaseCount = 0;

    /** DOCUMENT ME! */
    private boolean attackExpired = false;

    // Class constructor
    /**
     * Creates a new CompExpHard object.
     */
    public CompExpHard() {
        super("Compressor/Expander/Limiter/Noise Gate Processor",
            AbstractAudio.PROCESSOR);

        // Initialization will take place after sample rate is known
        initializationComplete = false;
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
        // Get samples from previous stage
        int len = previous.getSamples(buffer, length);

        // If bypass is enabled, short circuit processing
        if (getByPass() || !initializationComplete) {
            return len;
        }

        // We have samples to process
        for (int i = 0; i < len; i++) {
            // Get a sample
            double sample = (double) buffer[i];

            if (Math.abs(sample) >= thresholdValue) {
                // Sample value exceeds threshold
                releaseCount++;
                releaseCount %= (calcReleaseCount + 1);

                if (attackExpired) {
                    // Attack satisfied, process sample
                    if (!limiting) {
                        sample *= atRatio;
                    } else {
                        sample = (sample < 0) ? (-thresholdValue) : thresholdValue;
                    }
                } else {
                    attackCount--;

                    if (attackCount <= 0) {
                        attackExpired = true;
                        releaseCount = calcReleaseCount;
                    }
                }
            } else {
                // Sample value did not exceed threshold
                if (attackExpired) {
                    if (!limiting) {
                        sample *= atRatio;
                    }

                    releaseCount--;

                    if (releaseCount <= 0) {
                        attackExpired = false;
                        attackCount = calcAttackCount;
                    }
                } else {
                    attackCount++;
                    attackCount %= (calcAttackCount + 1);
                }

                // Now process below threshold noise gating
                sample *= btRatio;
            }

            // Apply gain
            sample *= gain;

            // Range check results
            if (sample > 32767.0) {
                sample = 32767.0;
            } else if (sample < -32768.0) {
                sample = -32768.0;
            }

            // Store sample back into buffer
            buffer[i] = (short) sample;
        }

        // Return count of sample processed
        return len;
    }

    // These methods called when UI controls are manipulated
    /**
     * DOCUMENT ME!
     *
     * @param thresholdInDB DOCUMENT ME!
     */
    public void setThreshold(double thresholdInDB) {
        // thresholdValue is the sample value which is thresholdInDB
        // below the maximum value of 32767.0
        thresholdValue = Math.pow(10, thresholdInDB / 20.0) * 32767.0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ratio DOCUMENT ME!
     */
    public void setBelowThresholdRatio(double ratio) {
        // Check for noise gating function
        gating = (ratio >= this.MINBTRATIO);

        // A noise gate clamps output to zero
        if (gating) {
            btRatio = 0.0;
        } else {
            btRatio = 1.0 / ratio;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param dBRatio DOCUMENT ME!
     */
    public void setAboveThresholdRatio(double dBRatio) {
        limiting = (dBRatio <= this.MINATRATIO);

        atRatio = Math.pow(10, dBRatio / 20);
    }

    /**
     * DOCUMENT ME!
     *
     * @param attackInMs DOCUMENT ME!
     */
    public void setAttack(double attackInMs) {
        this.attackInMs = attackInMs;
        calcAttackCount = (int) ((channels * attackInMs * sampleRate) / 1000);
        attackCount = calcAttackCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param releaseInMs DOCUMENT ME!
     */
    public void setRelease(double releaseInMs) {
        this.releaseInMs = releaseInMs;
        calcReleaseCount = (int) ((channels * releaseInMs * sampleRate) / 1000);
        releaseCount = calcReleaseCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param gainInDb DOCUMENT ME!
     */
    public void setGain(double gainInDb) {
        this.gain = Math.pow(10, gainInDb / 20);
    }

    // Perform calculations that require a known sample rate
    /**
     * DOCUMENT ME!
     */
    private void doInitialization() {
        calcAttackCount = (int) ((channels * attackInMs * sampleRate) / 1000);
        attackCount = calcAttackCount;

        calcReleaseCount = (int) ((channels * releaseInMs * sampleRate) / 1000);
        releaseCount = calcReleaseCount;

        // Indicate initialization is complete
        initializationComplete = true;
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
        channels = preferred;
    }
}
