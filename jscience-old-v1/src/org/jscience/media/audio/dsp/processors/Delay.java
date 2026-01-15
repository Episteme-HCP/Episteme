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
public class Delay extends AbstractAudio {
    // Private class data
    /** DOCUMENT ME! */
    private boolean initializationComplete = false;

    /** DOCUMENT ME! */
    private int delayInMs;

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
    private int delayBufferSize = 0;

    /** DOCUMENT ME! */
    private short[] localBuffer = null;

    /** DOCUMENT ME! */
    private short[] delayBuffer = null;

    /** DOCUMENT ME! */
    private int readIndex;

    /** DOCUMENT ME! */
    private int writeIndex;

/**
     * Creates a new Delay object.
     */
    public Delay() {
        super("Delay", AbstractAudio.PROCESSOR);

        initializationComplete = false;

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

        // Do the processing
        for (int i = 0; i < len; i++) {
            int inputSample = (int) localBuffer[i];
            int delaySample = (int) delayBuffer[readIndex++];
            int outputSample = ((inputSample * dryLevel) / 100) +
                ((delaySample * wetLevel) / 100);

            // Clamp output to legal range
            if (outputSample > 32767) {
                outputSample = 32767;
            } else if (outputSample < -32768) {
                outputSample = -32768;
            }

            // Store in output sample
            buffer[i] = (short) outputSample;

            // Calculate feedback
            inputSample += ((delaySample * feedbackLevel) / 100);

            // Clamp output to legal range
            if (inputSample > 32767) {
                inputSample = 32767;
            } else if (inputSample < -32768) {
                inputSample = -32768;
            }

            delayBuffer[writeIndex++] = (short) inputSample;

            // Update indices
            readIndex %= delayBufferSize;
            writeIndex %= delayBufferSize;
        }

        return len;
    }

    /**
     * DOCUMENT ME!
     *
     * @param delayInMs DOCUMENT ME!
     */
    public void setDelayInMs(int delayInMs) {
        initializationComplete = false;
        this.delayInMs = delayInMs;

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
     * @param feedbackLevel DOCUMENT ME!
     */
    public void setFeedbackLevel(int feedbackLevel) {
        this.feedbackLevel = feedbackLevel;
    }

    /**
     * DOCUMENT ME!
     */
    public void doInitialization() {
        // See if we have the necessary data to initialize delay
        if ((sampleRate != 0) && (numberOfChannels != 0) &&
                (!initializationComplete)) {
            // Allocate delay buffer
            int delayOffset = (delayInMs * sampleRate * numberOfChannels) / 1000;

            delayBufferSize = AbstractAudio.SAMPLEBUFFERSIZE + delayOffset;

            // Allocate new delay buffer
            delayBuffer = new short[delayBufferSize];

            // Initialize indices
            // Index where dry sample is written
            writeIndex = 0;

            // Index where wet sample is read
            readIndex = AbstractAudio.SAMPLEBUFFERSIZE;

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
