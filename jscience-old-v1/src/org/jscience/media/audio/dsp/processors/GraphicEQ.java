// This code is repackaged after the code from Craig A. Lindley, from Digital Audio with Java
// Site ftp://ftp.prenhall.com/pub/ptr/professional_computer_science.w-022/digital_audio/
// Email
package org.jscience.media.audio.dsp.processors;

import org.jscience.media.audio.dsp.AbstractAudio;
import org.jscience.media.audio.dsp.filters.IIRBandpassFilter;
import org.jscience.media.audio.dsp.filters.IIRBandpassFilterDesign;


/*
This graphic equalizer processor uses the optimized bandpass
filters implemented in the IIRBandpassFilter and IIRBandpassFilterDesign
classes in the filters package. A quality factor (Q) of 1.4 was
chosen for the filters to minimize the ripple in the passband with
full boost or cut. Range of boost and cut is +12db .. -12db.

The frequency of the bandpass filters were choosen to be:
50Hz, 100Hz, 200Hz, 400Hz, 800Hz, 1.6KHz, 3.2KHz, 6.4KHz, 12.8KHz

At a 11025 sample rate or lower, the highest two filters are disabled due to
Nyquist criteria. At a sample rate of 22050, only the highest filter is
disabled. At a 44100 sample rate, all filters are enabled.
 */
public class GraphicEQ extends AbstractAudio {
    /** DOCUMENT ME! */
    private static final double Q = 1.4;

    // Private class data
    /** DOCUMENT ME! */
    private double[] dBuffer = new double[1];

    /** DOCUMENT ME! */
    private int sampleRate;

    /** DOCUMENT ME! */
    private boolean initializationComplete;

    /** DOCUMENT ME! */
    private double gainFactor;

    // Individual filter object instances
    /** DOCUMENT ME! */
    private IIRBandpassFilterDesign fd50Hz;

    /** DOCUMENT ME! */
    private IIRBandpassFilter f50Hz = null;

    /** DOCUMENT ME! */
    private IIRBandpassFilterDesign fd100Hz;

    /** DOCUMENT ME! */
    private IIRBandpassFilter f100Hz = null;

    /** DOCUMENT ME! */
    private IIRBandpassFilterDesign fd200Hz;

    /** DOCUMENT ME! */
    private IIRBandpassFilter f200Hz = null;

    /** DOCUMENT ME! */
    private IIRBandpassFilterDesign fd400Hz;

    /** DOCUMENT ME! */
    private IIRBandpassFilter f400Hz = null;

    /** DOCUMENT ME! */
    private IIRBandpassFilterDesign fd800Hz;

    /** DOCUMENT ME! */
    private IIRBandpassFilter f800Hz = null;

    /** DOCUMENT ME! */
    private IIRBandpassFilterDesign fd1600Hz;

    /** DOCUMENT ME! */
    private IIRBandpassFilter f1600Hz = null;

    /** DOCUMENT ME! */
    private IIRBandpassFilterDesign fd3200Hz;

    /** DOCUMENT ME! */
    private IIRBandpassFilter f3200Hz = null;

    /** DOCUMENT ME! */
    private IIRBandpassFilterDesign fd6400Hz;

    /** DOCUMENT ME! */
    private IIRBandpassFilter f6400Hz = null;

    /** DOCUMENT ME! */
    private IIRBandpassFilterDesign fd12800Hz;

    /** DOCUMENT ME! */
    private IIRBandpassFilter f12800Hz = null;

/**
     * Creates a new GraphicEQ object.
     */
    public GraphicEQ() {
        super("Graphic Equalizer", AbstractAudio.PROCESSOR);

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
        // Ask for a buffer of samples
        int len = previous.getSamples(buffer, length);

        if (len == -1) {
            return len;
        }

        // If bypass is enabled, short circuit filtering
        if (getByPass() || !initializationComplete) {
            return len;
        }

        // Realloc buffer as required
        if (dBuffer.length != len) {
            dBuffer = new double[len];
        }

        // Move short samples into summation buffer for processing
        // Prescale the data according to number of filter elements
        for (int i = 0; i < len; i++)
            dBuffer[i] = (double) buffer[i] * gainFactor;

        // Apply the filters
        f50Hz.doFilter(buffer, dBuffer, len);
        f100Hz.doFilter(buffer, dBuffer, len);
        f200Hz.doFilter(buffer, dBuffer, len);
        f400Hz.doFilter(buffer, dBuffer, len);
        f800Hz.doFilter(buffer, dBuffer, len);
        f1600Hz.doFilter(buffer, dBuffer, len);
        f3200Hz.doFilter(buffer, dBuffer, len);

        if (sampleRate > 12800) {
            f6400Hz.doFilter(buffer, dBuffer, len);
        }

        if (sampleRate > 25600) {
            f12800Hz.doFilter(buffer, dBuffer, len);
        }

        // Convert the double samples back into short samples after
        // range constraining them.
        for (int i = 0; i < len; i++) {
            double dSample = dBuffer[i];

            if (dSample > 32767.0) {
                dSample = 32767.0;
            } else if (dSample < -32768.0) {
                dSample = -32768.0;
            }

            // Convert sample and store
            buffer[i] = (short) dSample;
        }

        return len;
    }

    // These methods called when UI controls are manipulated
    /**
     * DOCUMENT ME!
     *
     * @param gain DOCUMENT ME!
     */
    public void f50HzGain(double gain) {
        if (f50Hz != null) {
            f50Hz.setAmplitudeAdj(gain);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param gain DOCUMENT ME!
     */
    public void f100HzGain(double gain) {
        if (f100Hz != null) {
            f100Hz.setAmplitudeAdj(gain);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param gain DOCUMENT ME!
     */
    public void f200HzGain(double gain) {
        if (f200Hz != null) {
            f200Hz.setAmplitudeAdj(gain);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param gain DOCUMENT ME!
     */
    public void f400HzGain(double gain) {
        if (f400Hz != null) {
            f400Hz.setAmplitudeAdj(gain);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param gain DOCUMENT ME!
     */
    public void f800HzGain(double gain) {
        if (f800Hz != null) {
            f800Hz.setAmplitudeAdj(gain);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param gain DOCUMENT ME!
     */
    public void f1600HzGain(double gain) {
        if (f1600Hz != null) {
            f1600Hz.setAmplitudeAdj(gain);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param gain DOCUMENT ME!
     */
    public void f3200HzGain(double gain) {
        if (f3200Hz != null) {
            f3200Hz.setAmplitudeAdj(gain);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param gain DOCUMENT ME!
     */
    public void f6400HzGain(double gain) {
        if (f6400Hz != null) {
            f6400Hz.setAmplitudeAdj(gain);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param gain DOCUMENT ME!
     */
    public void f12800HzGain(double gain) {
        if (f12800Hz != null) {
            f12800Hz.setAmplitudeAdj(gain);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void doInitialization() {
        // Total the number of filter gain elements in chain
        int gainElements = 1;

        // Design the filters now that the sampling rate is known.
        // Design the filter
        fd50Hz = new IIRBandpassFilterDesign(50, sampleRate, Q);
        fd50Hz.doFilterDesign();

        // Implement the filter design
        f50Hz = new IIRBandpassFilter(fd50Hz);
        gainElements++;

        // Design the filter
        fd100Hz = new IIRBandpassFilterDesign(100, sampleRate, Q);
        fd100Hz.doFilterDesign();

        // Implement the filter design
        f100Hz = new IIRBandpassFilter(fd100Hz);
        gainElements++;

        // Design the filter
        fd200Hz = new IIRBandpassFilterDesign(200, sampleRate, Q);
        fd200Hz.doFilterDesign();

        // Implement the filter design
        f200Hz = new IIRBandpassFilter(fd200Hz);
        gainElements++;

        // Design the filter
        fd400Hz = new IIRBandpassFilterDesign(400, sampleRate, Q);
        fd400Hz.doFilterDesign();

        // Implement the filter design
        f400Hz = new IIRBandpassFilter(fd400Hz);
        gainElements++;

        // Design the filter
        fd800Hz = new IIRBandpassFilterDesign(800, sampleRate, Q);
        fd800Hz.doFilterDesign();

        // Implement the filter design
        f800Hz = new IIRBandpassFilter(fd800Hz);
        gainElements++;

        // Design the filter
        fd1600Hz = new IIRBandpassFilterDesign(1600, sampleRate, Q);
        fd1600Hz.doFilterDesign();

        // Implement the filter design
        f1600Hz = new IIRBandpassFilter(fd1600Hz);
        gainElements++;

        // Design the filter
        fd3200Hz = new IIRBandpassFilterDesign(3200, sampleRate, Q);
        fd3200Hz.doFilterDesign();

        // Implement the filter design
        f3200Hz = new IIRBandpassFilter(fd3200Hz);
        gainElements++;

        // Conditionally design and implement the higher freq filters
        if (sampleRate > 12800) {
            // Design the filter
            fd6400Hz = new IIRBandpassFilterDesign(6400, sampleRate, Q);
            fd6400Hz.doFilterDesign();

            // Implement the filter design
            f6400Hz = new IIRBandpassFilter(fd6400Hz);
            gainElements++;
        }

        if (sampleRate > 25600) {
            // Design the filter
            fd12800Hz = new IIRBandpassFilterDesign(12800, sampleRate, Q);
            fd12800Hz.doFilterDesign();

            // Implement the filter design
            f12800Hz = new IIRBandpassFilter(fd12800Hz);
            gainElements++;
        }

        gainFactor = 1.0 / gainElements;

        // All filters designed, indicate initialization is complete
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

        // Cannot do initialization until sample rate is known
        doInitialization();
    }
}
