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
public class Chorus extends AbstractAudio {
    // Private class data
    /** DOCUMENT ME! */
    private boolean initializationComplete = false;

    /** DOCUMENT ME! */
    private int delayInMs;

    /** DOCUMENT ME! */
    private double halfDepth = 1;

    /** DOCUMENT ME! */
    private double halfDepthInSamples;

    /** DOCUMENT ME! */
    private double rateInHz;

    /** DOCUMENT ME! */
    private boolean isSinLFO;

    /** DOCUMENT ME! */
    private boolean invertPhase;

    /** DOCUMENT ME! */
    private double step;

    /** DOCUMENT ME! */
    private double sweepValue = 0;

    /** DOCUMENT ME! */
    private int sampleNumber = 0;

    /** DOCUMENT ME! */
    private double radiansPerSample;

    /** DOCUMENT ME! */
    private int depthLevel;

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
    private int delayBufferSize;

    /** DOCUMENT ME! */
    private short[] localBuffer = null;

    /** DOCUMENT ME! */
    private int[] delayBuffer = null;

    /** DOCUMENT ME! */
    private int[] leftDelayBuffer = null;

    /** DOCUMENT ME! */
    private int[] rightDelayBuffer = null;

    /** DOCUMENT ME! */
    private int readIndex;

    /** DOCUMENT ME! */
    private int writeIndex;

/**
     * Creates a new Chorus object.
     */
    public Chorus() {
        super("Chorus", AbstractAudio.PROCESSOR);

        initializationComplete = false;
        isSinLFO = false;
        invertPhase = false;

        // Allocate local sample buffer
        localBuffer = new short[AbstractAudio.SAMPLEBUFFERSIZE];
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
        if (getByPass() || !initializationComplete) {
            return previous.getSamples(buffer, length);
        }

        // Read number of samples requested from previous stage
        int len = previous.getSamples(localBuffer, length);

        if (len == -1) {
            return -1;
        }

        if (numberOfChannels == 1) {
            return processMonoSamples(localBuffer, buffer, len);
        } else {
            return processStereoSamples(localBuffer, buffer, len);
        }
    }

    // Process mono samples
    /**
     * DOCUMENT ME!
     *
     * @param localBuffer DOCUMENT ME!
     * @param buffer DOCUMENT ME!
     * @param len DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected int processMonoSamples(short[] localBuffer, short[] buffer,
        int len) {
        // Do the processing
        for (int i = 0; i < len; i++) {
            // Fetch the input samples from the local buffer
            int inputSample = (int) localBuffer[i];

            // Calculate sample offsets for fetching two samples
            double sampleOffset1 = sweepValue - halfDepthInSamples;
            double sampleOffset2 = sampleOffset1 - 1;

            // Calculate delta for linear interpolation
            double delta = Math.abs((int) sampleOffset1 - sampleOffset1);

            int actualIndex1 = readIndex + (int) sampleOffset1;
            int actualIndex2 = readIndex++ + (int) sampleOffset2;
            boolean underflow1 = (actualIndex1 < 0);
            boolean underflow2 = (actualIndex2 < 0);

            // Adjust indices for possible under/over flow
            if (underflow1) {
                actualIndex1 += delayBufferSize;
            } else {
                actualIndex1 %= delayBufferSize;
            }

            if (underflow2) {
                actualIndex2 += delayBufferSize;
            } else {
                actualIndex2 %= delayBufferSize;
            }

            // Fetch two samples and interpolate
            int delaySample1 = (int) delayBuffer[actualIndex1];
            int delaySample2 = (int) delayBuffer[actualIndex2];
            int delaySample = (int) ((delaySample2 * delta) +
                (delaySample1 * (1.0 - delta)));

            // Sum wet and dry portions of the output
            int outputSample = ((inputSample * dryLevel) / 100) +
                ((delaySample * wetLevel) / 100);

            // Clamp output to legal range
            if (outputSample > 32767) {
                outputSample = 32767;
            } else if (outputSample < -32768) {
                outputSample = -32768;
            }

            // Store output sample
            buffer[i] = (short) outputSample;

            // Calculate sample for storage in delay buffer
            inputSample += ((delaySample * feedbackLevel * (invertPhase ? (-1)
                                                                        : (+1))) / 100);

            // Store sample
            delayBuffer[writeIndex++] = inputSample;

            // Update indices
            readIndex %= delayBufferSize;
            writeIndex %= delayBufferSize;

            // Calculate new sweep value
            if (isSinLFO) {
                // LFO is sinusoidal
                sampleNumber %= sampleRate;
                sweepValue = halfDepthInSamples * Math.sin(radiansPerSample * sampleNumber++);
            } else {
                // LFO is triangular
                sweepValue += step;

                // Keep sweep in range
                if ((sweepValue >= halfDepthInSamples) ||
                        (sweepValue <= -halfDepthInSamples)) {
                    // Change direction of sweep
                    step *= -1;
                }
            }
        }

        return len;
    }

    // Process stereo samples
    /**
     * DOCUMENT ME!
     *
     * @param localBuffer DOCUMENT ME!
     * @param buffer DOCUMENT ME!
     * @param len DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected int processStereoSamples(short[] localBuffer, short[] buffer,
        int len) {
        // Do the processing
        for (int i = 0; i < (len / 2); i++) {
            // Fetch the input samples from the local buffer
            int leftInputSample = (int) localBuffer[2 * i];
            int rightInputSample = (int) localBuffer[(2 * i) + 1];

            // Calculate sample offsets for fetching two samples
            double sampleOffset1 = sweepValue - halfDepthInSamples;
            double sampleOffset2 = sampleOffset1 - 1;

            // Calculate delta for linear interpolation
            double delta = Math.abs((int) sampleOffset1 - sampleOffset1);

            int actualIndex1 = readIndex + (int) sampleOffset1;
            int actualIndex2 = readIndex++ + (int) sampleOffset2;
            boolean underflow1 = (actualIndex1 < 0);
            boolean underflow2 = (actualIndex2 < 0);

            // Adjust indices for possible under/over flow
            if (underflow1) {
                actualIndex1 += delayBufferSize;
            } else {
                actualIndex1 %= delayBufferSize;
            }

            if (underflow2) {
                actualIndex2 += delayBufferSize;
            } else {
                actualIndex2 %= delayBufferSize;
            }

            // Fetch two samples and interpolate
            int leftDelaySample1 = (int) leftDelayBuffer[actualIndex1];
            int leftDelaySample2 = (int) leftDelayBuffer[actualIndex2];
            int leftDelaySample = (int) ((leftDelaySample2 * delta) +
                (leftDelaySample1 * (1.0 - delta)));

            int rightDelaySample1 = (int) rightDelayBuffer[actualIndex1];
            int rightDelaySample2 = (int) rightDelayBuffer[actualIndex2];
            int rightDelaySample = (int) ((rightDelaySample2 * delta) +
                (rightDelaySample1 * (1.0 - delta)));

            // Sum wet and dry portions of the output
            int leftOutputSample = ((leftInputSample * dryLevel) / 100) +
                ((leftDelaySample * wetLevel) / 100);

            int rightOutputSample = ((rightInputSample * dryLevel) / 100) +
                ((rightDelaySample * wetLevel) / 100);

            // Clamp output to legal range
            if (leftOutputSample > 32767) {
                leftOutputSample = 32767;
            } else if (leftOutputSample < -32768) {
                leftOutputSample = -32768;
            }

            if (rightOutputSample > 32767) {
                rightOutputSample = 32767;
            } else if (rightOutputSample < -32768) {
                rightOutputSample = -32768;
            }

            // Store in output samples
            buffer[2 * i] = (short) leftOutputSample;
            buffer[(2 * i) + 1] = (short) rightOutputSample;

            // Calculate samples for storage in delay buffer
            leftInputSample += ((leftDelaySample * feedbackLevel * (invertPhase
            ? (-1) : (+1))) / 100);
            rightInputSample += ((rightDelaySample * feedbackLevel * (invertPhase
            ? (-1) : (+1))) / 100);

            // Store samples
            leftDelayBuffer[writeIndex] = leftInputSample;
            rightDelayBuffer[writeIndex++] = rightInputSample;

            // Update indices
            readIndex %= delayBufferSize;
            writeIndex %= delayBufferSize;

            // Calculate new sweep value
            if (isSinLFO) {
                // LFO is sinusoidal
                sampleNumber %= sampleRate;
                sweepValue = halfDepthInSamples * Math.sin(radiansPerSample * sampleNumber++);
            } else {
                // LFO is triangular
                sweepValue += step;

                // Keep sweep in range
                if ((sweepValue >= halfDepthInSamples) ||
                        (sweepValue <= -halfDepthInSamples)) {
                    // Change direction of sweep
                    step *= -1;
                }
            }
        }

        return len;
    }

    // Set a new delay value from UI
    /**
     * DOCUMENT ME!
     *
     * @param delayInMs DOCUMENT ME!
     */
    public void setDelayInMs(int delayInMs) {
        this.delayInMs = delayInMs;

        initializationComplete = false;

        doInitialization();
    }

    // Set a new LFO rate from UI
    /**
     * DOCUMENT ME!
     *
     * @param rateInHz DOCUMENT ME!
     */
    public void setRateInHz(double rateInHz) {
        this.rateInHz = rateInHz;

        // Calculate step size
        calculateStepSize();
    }

    // Set a new LFO mode from the UI
    /**
     * DOCUMENT ME!
     *
     * @param isSinLFO DOCUMENT ME!
     */
    public void setLFOMode(boolean isSinLFO) {
        this.isSinLFO = isSinLFO;

        // Calculate step size
        calculateStepSize();
    }

    // Set a new depth value from UI
    /**
     * DOCUMENT ME!
     *
     * @param depthInMs DOCUMENT ME!
     */
    public void setDepthLevel(double depthInMs) {
        halfDepth = depthInMs / 2.0;

        // Calculate step size
        calculateStepSize();
    }

    // Calculate new sweep value from LFO rate and depth
    /**
     * DOCUMENT ME!
     */
    private void calculateStepSize() {
        // Calculate half depth in samples
        halfDepthInSamples = (halfDepth * sampleRate) / 1000;

        sweepValue = 0.0;

        // Calculations for triangle wave
        double periodInSamples = (1.0 / rateInHz) * sampleRate;
        double quarterPeriod = periodInSamples / 4.0;

        step = halfDepthInSamples / quarterPeriod;

        // Calculations for sin wave
        sampleNumber = 0;
        radiansPerSample = (2 * Math.PI * rateInHz) / sampleRate;
    }

    // Set a new dry level value from UI
    /**
     * DOCUMENT ME!
     *
     * @param dryLevel DOCUMENT ME!
     */
    public void setDryLevel(int dryLevel) {
        this.dryLevel = dryLevel;
    }

    // Set a new wet level value from UI
    /**
     * DOCUMENT ME!
     *
     * @param wetLevel DOCUMENT ME!
     */
    public void setWetLevel(int wetLevel) {
        this.wetLevel = wetLevel;
    }

    // Set feedback phase from UI
    /**
     * DOCUMENT ME!
     *
     * @param invertPhase DOCUMENT ME!
     */
    public void setFeedbackPhase(boolean invertPhase) {
        this.invertPhase = invertPhase;
    }

    // Set a new feedback level value from UI
    /**
     * DOCUMENT ME!
     *
     * @param feedbackLevel DOCUMENT ME!
     */
    public void setFeedbackLevel(int feedbackLevel) {
        this.feedbackLevel = feedbackLevel;
    }

    // Calculate buffer sizes from delay values
    /**
     * DOCUMENT ME!
     */
    public void doInitialization() {
        // See if we have the necessary data to initialize delay
        if ((sampleRate != 0) && (numberOfChannels != 0) &&
                (!initializationComplete)) {
            // Calculate number of samples required for delay
            int delayOffset = (delayInMs * sampleRate) / 1000;

            if (numberOfChannels == 1) {
                // We're doing a mono signal
                // Calculate buffer size required
                delayBufferSize = AbstractAudio.SAMPLEBUFFERSIZE + delayOffset;

                // Allocate new delay buffer
                delayBuffer = new int[delayBufferSize];

                // Initialize indices
                // Index where dry sample is written
                writeIndex = 0;

                // Index where wet sample is read
                readIndex = AbstractAudio.SAMPLEBUFFERSIZE;
            } else {
                // We're doing a stereo signal
                // Calculate buffer size required
                int halfBufferSize = AbstractAudio.SAMPLEBUFFERSIZE / 2;
                delayBufferSize = halfBufferSize + delayOffset;

                // Allocate new delay buffers
                leftDelayBuffer = new int[delayBufferSize];
                rightDelayBuffer = new int[delayBufferSize];

                // Initialize indices
                // Index where dry sample is written
                writeIndex = 0;

                // Index where wet sample is read
                readIndex = halfBufferSize;
            }

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
        calculateStepSize();
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
