// This code is repackaged after the code from Craig A. Lindley, from Digital Audio with Java
// Site ftp://ftp.prenhall.com/pub/ptr/professional_computer_science.w-022/digital_audio/
// Email
package org.jscience.media.audio.dsp.processors;

import org.jscience.media.audio.dsp.AbstractAudio;
import org.jscience.media.audio.dsp.filters.*;


/*
This parametric equalizer processor features three filter sections.
A high pass shelving filter with adjustable cutoff frequency, a bandpass
peaking filter with adjustable center frequency and quality factor (Q)
and a low pass shelving filter with adjustable cutoff frequency. All
filters are second order. This gives the highpass and lowpass filters
a slope of 12 db/octave rolloff. The range of boost and cut is
+/- 12 db.

All filter sections are IIR filters. The frequency ranges of the highpass
and bandpass filters are limited by the Nyquist frequency. The limit
is one half the sampling rate. The dampling factor controls how much
peaking the highpass and lowpass filters have. A factor of 1.0 exhibits
very little if any peaking.
 */
public class ParametricEQ extends AbstractAudio {
    /** DOCUMENT ME! */
    private static final double DAMPINGFACTOR = 1.0;

    /** DOCUMENT ME! */
    private static final int HIGHPASSFREQMIN = 5000;

    /** DOCUMENT ME! */
    private static final int HIGHPASSFREQMAX = 16000;

    /** DOCUMENT ME! */
    public static final int HIGHPASSFREQDEF = 5000;

    /** DOCUMENT ME! */
    private static final int BANDPASSFREQMIN = 1500;

    /** DOCUMENT ME! */
    private static final int BANDPASSFREQMAX = 6000;

    /** DOCUMENT ME! */
    public static final int BANDPASSFREQDEF = 3000;

    /** DOCUMENT ME! */
    private static final double BANDPASSQMIN = 1.1;

    /** DOCUMENT ME! */
    private static final double BANDPASSQMAX = 16.0;

    /** DOCUMENT ME! */
    public static final double BANDPASSQDEF = 8.0;

    /** DOCUMENT ME! */
    private static final int LOWPASSFREQMIN = 40;

    /** DOCUMENT ME! */
    private static final int LOWPASSFREQMAX = 1500;

    /** DOCUMENT ME! */
    public static final int LOWPASSFREQDEF = 200;

    // Private class data
    /** DOCUMENT ME! */
    private double[] dBuffer = new double[1];

    /** DOCUMENT ME! */
    public int sampleRate;

    /** DOCUMENT ME! */
    private int currentBPFreq;

    /** DOCUMENT ME! */
    private double currentBPQ;

    /** DOCUMENT ME! */
    private boolean initializationComplete;

    /** DOCUMENT ME! */
    private double gainFactor;

    // Individual filter object instances
    /** DOCUMENT ME! */
    private IIRLowpassFilterDesign lpfd;

    /** DOCUMENT ME! */
    private IIRLowpassFilter lowPassShelf = null;

    /** DOCUMENT ME! */
    private IIRBandpassFilterDesign bpfd;

    /** DOCUMENT ME! */
    private IIRBandpassFilter bandPassPeak = null;

    /** DOCUMENT ME! */
    private IIRHighpassFilterDesign hpfd;

    /** DOCUMENT ME! */
    private IIRHighpassFilter highPassShelf = null;

/**
     * Creates a new ParametricEQ object.
     */
    public ParametricEQ() {
        super("Parametric Equalizer", AbstractAudio.PROCESSOR);

        // Initialization will take place after sample rate is known
        initializationComplete = false;

        // Default values for bandpass
        currentBPFreq = this.BANDPASSFREQDEF;
        currentBPQ = this.BANDPASSQDEF;
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
        // If bypass is enabled, short circuit filtering
        if (getByPass() || !initializationComplete) {
            return previous.getSamples(buffer, length);
        }

        // Ask for a buffer of samples
        int len = previous.getSamples(buffer, length);

        if (len == -1) {
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
        lowPassShelf.doFilter(buffer, dBuffer, len);
        bandPassPeak.doFilter(buffer, dBuffer, len);
        highPassShelf.doFilter(buffer, dBuffer, len);

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
    public void lowPassShelfGain(double gain) {
        if (lowPassShelf != null) {
            lowPassShelf.setAmplitudeAdj(gain);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param gain DOCUMENT ME!
     */
    public void bandPassPeakGain(double gain) {
        if (bandPassPeak != null) {
            bandPassPeak.setAmplitudeAdj(gain);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param gain DOCUMENT ME!
     */
    public void highPassShelfGain(double gain) {
        if (highPassShelf != null) {
            highPassShelf.setAmplitudeAdj(gain);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param freq DOCUMENT ME!
     */
    public void lowPassShelfFreq(int freq) {
        if (lowPassShelf != null) {
            // Recalculate and install the filter with new freq
            lpfd = new IIRLowpassFilterDesign(freq, sampleRate, DAMPINGFACTOR);
            lpfd.doFilterDesign();
            lowPassShelf.updateFilterCoefficients(lpfd);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param freq DOCUMENT ME!
     */
    public void bandPassPeakFreq(int freq) {
        currentBPFreq = freq;

        if ((bandPassPeak != null) && (freq < (sampleRate / 2))) {
            // Recalculate and install the filter with new freq
            bpfd = new IIRBandpassFilterDesign(currentBPFreq, sampleRate,
                    currentBPQ);
            bpfd.doFilterDesign();
            bandPassPeak.updateFilterCoefficients(bpfd);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param freq DOCUMENT ME!
     */
    public void highPassShelfFreq(int freq) {
        if ((highPassShelf != null) && (freq < (sampleRate / 2))) {
            hpfd = new IIRHighpassFilterDesign(freq, sampleRate, DAMPINGFACTOR);
            hpfd.doFilterDesign();
            highPassShelf.updateFilterCoefficients(hpfd);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param q DOCUMENT ME!
     */
    public void bandPassPeakQ(double q) {
        currentBPQ = q;

        if (bandPassPeak != null) {
            // Recalculate and install the filter with new freq
            bpfd = new IIRBandpassFilterDesign(currentBPFreq, sampleRate,
                    currentBPQ);
            bpfd.doFilterDesign();
            bandPassPeak.updateFilterCoefficients(bpfd);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void doInitialization() {
        // Total the number of filter gain elements in chain
        int gainElements = 1;

        // Design the filters now that the sampling rate is known.
        // Design the low pass filter
        lpfd = new IIRLowpassFilterDesign(this.LOWPASSFREQDEF, sampleRate,
                DAMPINGFACTOR);
        lpfd.doFilterDesign();

        // Implement the filter design
        lowPassShelf = new IIRLowpassFilter(lpfd);
        gainElements++;

        // Design the band filter
        bpfd = new IIRBandpassFilterDesign(currentBPFreq, sampleRate, currentBPQ);
        bpfd.doFilterDesign();

        // Implement the filter design
        bandPassPeak = new IIRBandpassFilter(bpfd);
        gainElements++;

        // Design the high pass filter
        hpfd = new IIRHighpassFilterDesign(this.HIGHPASSFREQDEF, sampleRate,
                DAMPINGFACTOR);
        hpfd.doFilterDesign();

        // Implement the filter design
        highPassShelf = new IIRHighpassFilter(hpfd);
        gainElements++;

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
        doInitialization();
    }
}
