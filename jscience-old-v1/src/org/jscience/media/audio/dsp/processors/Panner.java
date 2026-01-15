// This code is repackaged after the code from Craig A. Lindley, from Digital Audio with Java
// Site ftp://ftp.prenhall.com/pub/ptr/professional_computer_science.w-022/digital_audio/
// Email
package org.jscience.media.audio.dsp.processors;

import org.jscience.media.audio.dsp.AbstractAudio;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class Panner extends AbstractAudio {
    // Private class data
    /** DOCUMENT ME! */
    private boolean monoSource;

    /** DOCUMENT ME! */
    private int preferredChannels;

    /** DOCUMENT ME! */
    private double leftPanFactor = 0.5;

    /** DOCUMENT ME! */
    private double rightPanFactor = 0.5;

    /** DOCUMENT ME! */
    private boolean mixMode = false;

/**
     * Creates a new Panner object.
     */
    public Panner() {
        super("Panner", AbstractAudio.PROCESSOR);

        // Assume a mono source
        monoSource = true;
    }

    // Convert pot value into an attenuation factor for left and right
    /**
     * DOCUMENT ME!
     *
     * @param panValue DOCUMENT ME!
     */
    public void setPanValue(int panValue) {
        this.leftPanFactor = (100.0 - panValue) / 100.0;
        this.rightPanFactor = ((double) panValue) / 100.0;
    }

    // If set, stereo signals are mixed before panning is applied. If
    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void setMixMode(boolean state) {
        this.mixMode = state;
    }

    // Apply the panner effect to the samples passing through this
    /**
     * DOCUMENT ME!
     *
     * @param buffer DOCUMENT ME!
     * @param length DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSamples(short[] buffer, int length) {
        // If the previous stage constitutes a mono (single channel)
        // source, then halve the number of samples requested. This
        // allows the use of a single buffer for processing.
        int halfLength = length / 2;

        // Request samples from previous stage
        int len = previous.getSamples(buffer, monoSource ? halfLength : length);

        // Was EOF indication returned?
        if (len == -1) {
            return -1;
        }

        // If bypass in effect and we have a stereo source, don't do
        // anything as samples are already in the buffer. If we have
        // a mono source, copy mono samples to both the left and right
        // channels.
        if (getByPass()) {
            if (monoSource) {
                // We have a mono source to process. Work from back to front
                // of buffer to prevent over writing unprocessed data.
                int sourceIndex = halfLength - 1;
                int destIndex = length - 2;

                for (int i = 0; i < halfLength; i++) {
                    short s = buffer[sourceIndex--]; // Read mono sample
                    buffer[destIndex] = s; // Write left channel
                    buffer[destIndex + 1] = s; // Write right channel
                    destIndex -= 2;
                }
            }

            return length;
        }

        // Bypass not in effect, do some panning
        // What is done depends upon source and mode
        if (monoSource) {
            // We have a mono source to process. Work from back to front
            // of buffer to prevent over writing unprocessed data.
            int sourceIndex = halfLength - 1;
            int destIndex = length - 2;

            for (int i = 0; i < halfLength; i++) {
                short s = buffer[sourceIndex--];
                buffer[destIndex] = (short) (s * leftPanFactor);
                buffer[destIndex + 1] = (short) (s * rightPanFactor);
                destIndex -= 2;
            }
        } else {
            // We have a stereo source to process. Check the mode.
            if (mixMode) {
                // Mix left and right before panning
                for (int i = 0; i < length; i += 2) {
                    double s = (buffer[i] + buffer[i + 1]) / 2.0;
                    buffer[i] = (short) (s * leftPanFactor);
                    buffer[i + 1] = (short) (s * rightPanFactor);
                }
            } else {
                // Leave stereo separation intact
                for (int i = 0; i < length; i += 2) {
                    buffer[i] = (short) (buffer[i] * leftPanFactor);
                    buffer[i + 1] = (short) (buffer[i + 1] * rightPanFactor);
                }
            }
        }

        return length;
    }

    /**
     * DOCUMENT ME!
     */
    public void stopUI() {
        setByPass(true);
    }

    // Override AbstractAudio methods as required to influence the
    /**
     * DOCUMENT ME!
     *
     * @param min DOCUMENT ME!
     * @param max DOCUMENT ME!
     * @param preferred DOCUMENT ME!
     */
    public void minMaxChannels(int min, int max, int preferred) {
        // Propagate call towards the source
        if (previous != null) {
            previous.minMaxChannels(min, max, preferred);
        }

        // Save the preferred value from previous stages
        preferredChannels = preferred;

        // Set flag to indicate source mode
        monoSource = (preferredChannels == 1);

        // Set up for stereo as the output of the panner is
        // always stereo.
        min = 2;
        max = 2;
        preferred = 2;
    }

    // Override this method so that all stages before the panner
    /**
     * DOCUMENT ME!
     *
     * @param ch DOCUMENT ME!
     */
    public void setChannelsRecursive(int ch) {
        ch = preferredChannels;

        super.setChannelsRecursive(ch);
    }
}
