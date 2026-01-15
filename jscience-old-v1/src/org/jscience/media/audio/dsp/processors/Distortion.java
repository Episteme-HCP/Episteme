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
public class Distortion extends AbstractAudio {
    // Private class data
    /** DOCUMENT ME! */
    private int threshold = 32767;

    /** DOCUMENT ME! */
    private double gain = 1.0;

/**
     * Creates a new Distortion object.
     */
    public Distortion() {
        super("Distortion", AbstractAudio.PROCESSOR);
    }

    /**
     * DOCUMENT ME!
     *
     * @param threshold DOCUMENT ME!
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    /**
     * DOCUMENT ME!
     *
     * @param gain DOCUMENT ME!
     */
    public void setGain(double gain) {
        this.gain = gain;
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
        int len = previous.getSamples(buffer, length);

        if (getByPass()) {
            return len;
        }

        for (int i = 0; i < len; i++) {
            int sample = buffer[i];

            if (sample > threshold) {
                sample = threshold;
            } else if (sample < -threshold) {
                sample = -threshold;
            }

            buffer[i] = (short) (sample * gain);
        }

        return len;
    }
}
