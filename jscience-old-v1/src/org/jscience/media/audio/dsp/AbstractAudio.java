// This code is repackaged after the code from Craig A. Lindley, from Digital Audio with Java
// Site ftp://ftp.prenhall.com/pub/ptr/professional_computer_science.w-022/digital_audio/
// Email
package org.jscience.media.audio.dsp;

import org.jscience.util.Named;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public abstract class AbstractAudio implements Named {
    // Size of buffer used throughout the code
    /** DOCUMENT ME! */
    public static final int SAMPLEBUFFERSIZE = 7500;

    // AbstractAudio device types
    /** DOCUMENT ME! */
    public static final int ALLTYPES = -1;

    /** DOCUMENT ME! */
    public static final int NOTYPE = 0;

    /** DOCUMENT ME! */
    public static final int SOURCE = 1;

    /** DOCUMENT ME! */
    public static final int PROCESSOR = 2;

    /** DOCUMENT ME! */
    public static final int MONITOR = 3;

    /** DOCUMENT ME! */
    public static final int SINK = 4;

    // Class data
    /** DOCUMENT ME! */
    public AbstractAudio previous;

    /** DOCUMENT ME! */
    public AbstractAudio next;

    /**
     * DOCUMENT ME!
     */
    private String name;

    /**
     * DOCUMENT ME!
     */
    private int type;

    /**
     * DOCUMENT ME!
     */
    private int samplingRate;

    /**
     * DOCUMENT ME!
     */
    private int numberOfChannels;

    /**
     * DOCUMENT ME!
     */
    private boolean samplingRateFrozen;

    /**
     * DOCUMENT ME!
     */
    private boolean numberOfChannelsFrozen;

    /**
     * DOCUMENT ME!
     */
    private boolean byPass;

/**
     * AbstractAudio class constructor
     * <p/>
     * NOTE: name and type are for informational purposes only and
     * serve to identify a specific device.
     *
     * @param String name is the name given to this device
     * @param int    type is one of the device types listed above
     */
    public AbstractAudio(String name, int type) {
        // Save incoming
        this.name = name;
        this.type = type;

        // Do various initialization
        previous = null;
        next = null;

        samplingRate = 0;
        numberOfChannels = 0;

        samplingRateFrozen = false;
        numberOfChannelsFrozen = false;

        byPass = false;
    }

    /**
     * Static method for displaying a type string given the device type
     *
     * @param type type is the type of this AbstractAudio device
     *
     * @return DOCUMENT ME!
     */
    public static String typeString(int type) {
        switch (type) {
        case NOTYPE:
            return "No Type";

        case SOURCE:
            return "Source";

        case PROCESSOR:
            return "Processor";

        case MONITOR:
            return "Monitor";

        case SINK:
            return "Sink";
        }

        return "Unknown type";
    }

    /**
     * Convert AbstractAudio parameters to a string for display
     *
     * @return String containing description of this device
     */
    public String toString() {
        String retString = "<AbstractAudio: " + name;
        retString += (" Type: " + typeString(type));
        retString += (" Rate: " + samplingRate);
        retString += (" Channels: " + numberOfChannels);
        retString += (" Bypass: " + byPass + ">\n");

        return retString;
    }

    /**
     * Function to determine if one AbstractAudio device is the same as
     * another. Equality is assumed if name and type match. This method is
     * used in the LinkedListVector class to find specific devices on the
     * list.
     *
     * @param a DOCUMENT ME!
     *
     * @return boolean true if the name and type match, false otherwise.
     */
    public boolean equals(AbstractAudio a) {
        return (name.equals(a.getName()) && (type == a.type));
    }

    /**
     * Return the name of this AbstractAudio device
     *
     * @return String containing device's assigned name
     */
    public String getName() {
        return name;
    }

    /**
     * This method must be iplemented by all devices that extend the
     * AbstractAudio class. This is the method by which audio samples are
     * moved between device stages. Call the getSamples() method on the device
     * previous to this device in the signal path causes samples to be pull
     * from it.
     *
     * @param buffer [] buffer is a buffer that this stage of processing should
     *        fill with data for subsequent return to calling code.
     * @param length length is the number of samples that are requested
     *
     * @return int indicating the number of samples available or -1 if the end
     *         of input or file has been reached.
     */
    public abstract int getSamples(short[] buffer, int length);

    /**
     * Return the current by pass state of this device.
     *
     * @return boolean true if this stage of processing is bypassed and false
     *         otherwise.
     */
    public boolean getByPass() {
        return byPass;
    }

    /**
     * Used to set the bypass state of this device
     *
     * @param byPass byPass if true stage will be bypassed.
     */
    public void setByPass(boolean byPass) {
        this.byPass = byPass;
    }

    /**
     * Retrieve the sample rate of the signal path. If not already set,
     * this call with instigate negotiation.
     *
     * @return int containing the agreed upon sample rate
     */
    public int getSamplingRate() {
        if (!samplingRateFrozen) { // Not frozen?
            negotiateSamplingRate(); // Go figure it out
        }

        return samplingRate; // Return sampling rate
    }

    /**
     * Sets the sample rate for this device if possible
     *
     * @param s s is the sample rate to set
     */
    protected void setSamplingRate(int s) {
        if (samplingRateFrozen) {
            System.out.println("Can't change sampling rate");
            System.exit(1);
        }

        samplingRate = s;
    }

    /**
     * Called to instigate sample rate negotiation in the signal chain
     * of AbstractAudio device. First it causes propagation towards the sink
     * in the signal chain. Next, the default sample rate values are set and
     * the negotiation is begun by a call to minMaxSamplingRate. Upon return
     * from negotiation, some checking is done to see that the negotiated rate
     * is acceptable and then setSamplingRateRecursive is called to force the
     * agreed upon sample rate into each of the AbstractAudio devices in the
     * signal chain.
     */
    protected void negotiateSamplingRate() {
        if (next != null) { // Are we right most?
            next.negotiateSamplingRate(); // No, propagate to the right
        } else { // Yes, we are

            // Set reasonable defaults
            int min = 11025;
            int max = 44100;
            int preferred = 11025;

            // Get the real values
            minMaxSamplingRate(min, max, preferred);

            if (min > max) { // Check for bogus values
                System.out.println("Couldn't negotiate sampling rate");
                System.exit(1);
            }

            setSamplingRateRecursive(preferred); // Set them everywhere
        }
    }

    /**
     * Called to find the min, preferred and max values for sample rate
     * the devices in the signal path find acceptable.
     *
     * @param min min is the wrapped minimum value of sampling rate this signal
     *        path can tolerate.
     * @param max max is the wrapped maximum value of sampling rate this signal
     *        path can tolerate.
     * @param preferred preferred is the wrapped sampling rate value this
     *        signal path prefers.
     */
    public void minMaxSamplingRate(int min, int max, int preferred) {
        // Propagate call until first device in chain is located
        if (previous != null) {
            previous.minMaxSamplingRate(min, max, preferred);
        }

        // Get current values
        int thismin = min;
        int thismax = max;
        int thispreferred = preferred;

        // Does this stage have a sampling rate preference?
        if (samplingRate != 0) {
            thispreferred = samplingRate;
            preferred = samplingRate;
        }

        // Is the preferred value below the lower bound?
        if (thispreferred < thismin) {
            preferred = thismin;
        }

        // Is the preferred value above the upper bound?
        if (thispreferred > thismax) {
            preferred = thismax;
        }
    }

    /**
     * Causes all the device stages to have their sampling rate set to
     * the specified value.
     *
     * @param sr sr is the sampling rate that was negotiated and needs
     *        therefore to be set into each stage.
     */
    public void setSamplingRateRecursive(int sr) {
        if (previous != null) { // Are we left most?
            previous.setSamplingRateRecursive(sr);
        }

        setSamplingRate(sr); // Set it
        samplingRateFrozen = true; // Yes, we've negotiated
    }

    /**
     * Retrieve the number of channel of the signal path. If not
     * already set, this call with instigate negotiation.
     *
     * @return int containing the agreed upon number of channels
     */
    public int getNumberOfChannels() {
        if (!numberOfChannelsFrozen) {
            negotiateNumberOfChannels();
        }

        return numberOfChannels;
    }

    /**
     * Sets the number of channels for this device if possible
     *
     * @param channels channels is the number of channels to set
     */
    protected void setNumberOfChannels(int channels) {
        if (numberOfChannelsFrozen) {
            System.out.println("Can't change number of channels");
            System.exit(1);
        }

        numberOfChannels = channels;
    }

    /**
     * Called to instigate number of channel negotiation in the signal
     * chain of AbstractAudio device. First it causes propagation towards the
     * sink in the signal chain. Next, the default number of channels value is
     * set and the negotiation is begun by a call to minMaxChannels. Upon
     * return from negotiation, some checking is done to see that the
     * negotiated channels is acceptable and then setChannelsRecursive is
     * called to force the agreed upon number of channels into each of the
     * AbstractAudio devices in the signal chain.
     */
    protected void negotiateNumberOfChannels() {
        if (next != null) { // Are we right most device?
            next.negotiateNumberOfChannels(); // Move towards the right
        } else {
            // Set default values
            int min = 1;
            int max = 2;
            int preferred = 1;

            // Negotiate for the real values
            minMaxChannels(min, max, preferred);

            if (min > max) {
                System.out.println("Couldn't negotiate channels");
                System.exit(1);
            }

            setChannelsRecursive(preferred);
        }
    }

    /**
     * Called to find the min, preferred and max values for the number
     * of channels the devices in the signal path find acceptable.
     *
     * @param min min is the wrapped minimum number of channels this signal
     *        path can tolerate.
     * @param max max is the wrapped maximum number of channels this signal
     *        path can tolerate.
     * @param preferred preferred is the wrapped number of channels this signal
     *        path prefers.
     */
    public void minMaxChannels(int min, int max, int preferred) {
        // Propagate call towards the source
        if (previous != null) {
            previous.minMaxChannels(min, max, preferred);
        }

        // If this stage has a channel preference, set it
        if (numberOfChannels != 0) {
            preferred = numberOfChannels;
        }

        // Get the parameters from previous stages
        int currentMin = min;
        int currentMax = max;
        int currentPreferred = preferred;

        // Force preferred value to be within min, max range
        if (currentPreferred < currentMin) {
            preferred = currentMin;
        }

        if (currentPreferred > currentMax) {
            preferred = currentMax;
        }
    }

    /**
     * Called to force the specified number of channels to be used in
     * the device chain. This causes a propagation towards the source in the
     * signal chain and then on return sets the number of channels variable in
     * each stage of the chain.
     *
     * @param ch ch is the number of channels to set either 1 for mono or 2 for
     *        stereo.
     */
    public void setChannelsRecursive(int ch) {
        // Propagate call towards source
        if (previous != null) {
            previous.setChannelsRecursive(ch);
        }

        // Set negotiated channels and indicate negotiation complete
        numberOfChannels = ch;
        numberOfChannelsFrozen = true;
    }

    /**
     * Override this method if reset functionality is required for your
     * AbstractAudio device derivative. If not overridden nothing will happen
     * in this device when a reset operation occurs.
     */
    protected void reset() {
    }

    /**
     * Propagate reset call to all processing stages
     */
    protected void propagateReset() {
        if (previous != null) {
            previous.propagateReset();
        }

        // Call reset on this stage of processing
        reset();
    }

    /**
     * Called to perform a reset operation on all participating device
     * stages.
     */
    public void doReset() {
        // Propagate reset towards sink then towards source
        if (next != null) {
            next.doReset();
        } else {
            propagateReset();
        }
    }

    /**
     * Hex string display method
     *
     * @param i i is the value to convert to a hex string
     */
    public void hexo(int i) {
        System.out.println(Long.toHexString(i));
    }

    /**
     * Labeled hex string display method
     *
     * @param s s is the label to prepend to the hex string
     * @param i i is the value to convert to a hex string
     */
    public void hexo(String s, int i) {
        System.out.println(s + Long.toHexString(i));
    }

    /**
     * Hex string display method
     *
     * @param i i is the value to convert to a hex string
     */
    public static void hexo(long i) {
        System.out.println(Long.toHexString(i));
    }

    /**
     * Labeled hex string display method
     *
     * @param s s is the label to prepend to the hex string
     * @param i i is the value to convert to a hex string
     */
    public static void hexo(String s, long i) {
        System.out.println(s + Long.toHexString(i));
    }

    /**
     * Shortcut method for system.out.println
     *
     * @param s s is the string to write to standard out
     */
    public static void o(String s) {
        System.out.println(s);
    }
}
