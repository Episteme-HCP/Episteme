// This code is repackaged after the code from Craig A. Lindley, from Digital Audio with Java
// Site ftp://ftp.prenhall.com/pub/ptr/professional_computer_science.w-022/digital_audio/
// Email
package org.jscience.media.audio.dsp.monitors;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class TriggerFlag {
    // Private class data
    /** DOCUMENT ME! */
    private boolean value;

/**
     * Creates a new TriggerFlag object.
     */
    public TriggerFlag() {
        value = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean trigger() {
        return value;
    }

    /**
     * DOCUMENT ME!
     */
    public void triggered() {
        value = true;
    }

    /**
     * DOCUMENT ME!
     */
    public void resetTrigger() {
        value = false;
    }
}
