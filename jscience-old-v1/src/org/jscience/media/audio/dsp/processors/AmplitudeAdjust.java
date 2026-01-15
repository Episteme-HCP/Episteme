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
public class AmplitudeAdjust extends AbstractAudio {
    // Private class data
    /** DOCUMENT ME! */
    private double adjValue = 1.0;

/**
     * Creates a new AmplitudeAdjust object.
     */
    public AmplitudeAdjust() {
        super("AmplitudeAdjust", AbstractAudio.PROCESSOR);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAmplitudeAdj() {
        return adjValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @param adjValue DOCUMENT ME!
     */
    public void setAmplitudeAdj(double adjValue) {
        this.adjValue = adjValue;
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

        for (int i = 0; i < len; i++)
            buffer[i] = (short) (buffer[i] * adjValue);

        return len;
    }
}
