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
public class Reverb extends AbstractAudio {
    // Private class data
    /** DOCUMENT ME! */
    private boolean initializationComplete;

    /** DOCUMENT ME! */
    private boolean endOfData;

    /** DOCUMENT ME! */
    private double sustainTimeInMs;

    /** DOCUMENT ME! */
    private int sampleRate;

    /** DOCUMENT ME! */
    private int numberOfChannels;

    /** DOCUMENT ME! */
    private SchroederReverb reverb;

/**
     * Creates a new Reverb object.
     */
    public Reverb() {
        super("Reverb", AbstractAudio.PROCESSOR);

        // Initialization will take place after sample rate is known
        initializationComplete = false;
        endOfData = false;
    }

    // Prepare for running reverb again
    /**
     * DOCUMENT ME!
     */
    public void reset() {
        o("ReverbWithUI reset");

        endOfData = false;

        // Calling this function will reset all comb and allpass filters
        // in preparation for running again.
        reverb.setSustainInMs(sustainTimeInMs);
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
        // If bypass is enabled, short circuit processing
        if (getByPass() || !initializationComplete) {
            return previous.getSamples(buffer, length);
        }

        // Must use endOfData to stop reading input otherwise
        // if source is file it will continually be reread.
        if (!endOfData) {
            // Ask for a buffer of samples
            length = previous.getSamples(buffer, length);

            if (length == -1) {
                endOfData = true;
            }

            // Do the reverb on the samples
            return reverb.doReverb(buffer, length);
        } else {
            // Propagate the sustain samples
            return reverb.doReverb(buffer, -1);
        }
    }

    // These methods called when UI controls are manipulated
    /**
     * DOCUMENT ME!
     *
     * @param delay DOCUMENT ME!
     */
    public void comb1Delay(double delay) {
        if (reverb != null) {
            reverb.setComb1Delay(delay);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param delay DOCUMENT ME!
     */
    public void comb2Delay(double delay) {
        if (reverb != null) {
            reverb.setComb2Delay(delay);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param delay DOCUMENT ME!
     */
    public void comb3Delay(double delay) {
        if (reverb != null) {
            reverb.setComb3Delay(delay);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param delay DOCUMENT ME!
     */
    public void comb4Delay(double delay) {
        if (reverb != null) {
            reverb.setComb4Delay(delay);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param delay DOCUMENT ME!
     */
    public void allpass1Delay(double delay) {
        if (reverb != null) {
            reverb.setAllpass1Delay(delay);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param delay DOCUMENT ME!
     */
    public void allpass2Delay(double delay) {
        if (reverb != null) {
            reverb.setAllpass2Delay(delay);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param sustainTimeInMs DOCUMENT ME!
     */
    public void setSustainTime(double sustainTimeInMs) {
        // Remember what sustain time was requested
        this.sustainTimeInMs = sustainTimeInMs;

        if (reverb != null) {
            reverb.setSustainInMs(sustainTimeInMs);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param mix DOCUMENT ME!
     */
    public void setDryWetMix(double mix) {
        if (reverb != null) {
            reverb.setDryWetMix(mix);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void doInitialization() {
        reverb = new SchroederReverb(sampleRate, numberOfChannels);

        // Set the saved sustain time
        reverb.setSustainInMs(sustainTimeInMs);

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
        numberOfChannels = preferred;
    }
}
