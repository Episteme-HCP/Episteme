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
public class AllpassNetwork {
    // Private class data
    /** DOCUMENT ME! */
    private int sampleRate;

    /** DOCUMENT ME! */
    private int numberOfChannels;

    /** DOCUMENT ME! */
    private double delayInMs;

    /** DOCUMENT ME! */
    private double sustainTimeInMs;

    /** DOCUMENT ME! */
    private double gain1;

    /** DOCUMENT ME! */
    private double gain2;

    /** DOCUMENT ME! */
    private double gain3;

    /** DOCUMENT ME! */
    private int sustainSampleCount;

    /** DOCUMENT ME! */
    private double[] delayBuffer;

    /** DOCUMENT ME! */
    private int delayBufferSize;

    /** DOCUMENT ME! */
    private int writeIndex;

    /** DOCUMENT ME! */
    private int readIndex;

/**
     * Creates a new AllpassNetwork object.
     *
     * @param sampleRate       DOCUMENT ME!
     * @param numberOfChannels DOCUMENT ME!
     * @param delayInMs        DOCUMENT ME!
     */
    public AllpassNetwork(int sampleRate, int numberOfChannels, double delayInMs) {
        // Save incoming
        this.sampleRate = sampleRate;
        this.numberOfChannels = numberOfChannels;

        // Default gain of allpass network
        double networkGain = 0.7;
        gain1 = -networkGain;
        gain2 = 1.0 - (networkGain * networkGain);
        gain3 = networkGain;

        // Default sustain
        sustainTimeInMs = 65.0;

        // Initialize delay parameters
        setDelayInMs(delayInMs);
    }

    /**
     * DOCUMENT ME!
     *
     * @param delayInMs DOCUMENT ME!
     */
    public void setDelayInMs(double delayInMs) {
        this.delayInMs = delayInMs;

        // Do calculation to determine delay buffer size
        int delayOffset = (int) ((delayInMs + 0.5) * sampleRate * numberOfChannels) / 1000;

        delayBufferSize = AbstractAudio.SAMPLEBUFFERSIZE + delayOffset;

        // Allocate new delay buffer
        delayBuffer = new double[delayBufferSize];

        // Initialize indices
        // Index where dry sample is written
        writeIndex = 0;

        // Index where wet sample is read
        readIndex = AbstractAudio.SAMPLEBUFFERSIZE;

        // Calculate gain for filter
        calcGain();
    }

    /**
     * DOCUMENT ME!
     */
    private void calcGain() {
        // Calculate gain for this filter such that a recirculating
        // sample will reduce in level 60db in the specified
        // sustain time.
        double gain = Math.pow(0.001, delayInMs / sustainTimeInMs);

        // Now update the network gain
        gain1 = -gain;
        gain2 = 1.0 - (gain * gain);
        gain3 = gain;
    }

    // Sustain time is the time it takes the signal to drop
    /**
     * DOCUMENT ME!
     *
     * @param sustainTimeInMs DOCUMENT ME!
     */
    public void setSustainTimeInMs(double sustainTimeInMs) {
        this.sustainTimeInMs = sustainTimeInMs;

        // Number of samples needed for sustain duration
        sustainSampleCount = (int) ((sustainTimeInMs * sampleRate * numberOfChannels) / 1000);

        // Calculate gain for this filter
        calcGain();
    }

    // Do the data processing
    /**
     * DOCUMENT ME!
     *
     * @param inBuf DOCUMENT ME!
     * @param outBuf DOCUMENT ME!
     * @param length DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int doFilter(double[] inBuf, double[] outBuf, int length) {
        // See if at end of data
        if (length != -1) {
            // Sustain is not in effect because there are input samples
            for (int i = 0; i < length; i++) {
                double inSample = inBuf[i];
                double outSample = inSample * gain1;
                double delaySample = delayBuffer[readIndex++];
                outSample += (delaySample * gain2);

                // Output the new sample
                outBuf[i] = outSample;

                // Apply gain and feedback to sample
                inSample += (delaySample * gain3);

                // Store sample in delay buffer
                delayBuffer[writeIndex++] = inSample;

                // Update buffer indices
                readIndex %= delayBufferSize;
                writeIndex %= delayBufferSize;
            }

            return length;
        } else {
            // No more input samples are available therefore sustain is in
            // mode is in effect.
            int samplesToMove = Math.min(outBuf.length, sustainSampleCount);

            if (samplesToMove <= 0) {
                return -1;
            }

            // No more input samples are available therefore sustain is in
            // mode is in effect.
            for (int i = 0; i < samplesToMove; i++) {
                double delaySample = delayBuffer[readIndex++];
                double outSample = delaySample * gain2;

                // Output is from delay buffer
                outBuf[i] = outSample;

                // Apply gain and feedback to sample
                double inSample = delaySample * gain3;

                // Store sample in delay buffer
                delayBuffer[writeIndex++] = inSample;

                // Update buffer indices
                readIndex %= delayBufferSize;
                writeIndex %= delayBufferSize;
                sustainSampleCount--;
            }

            return samplesToMove;
        }
    }
}
