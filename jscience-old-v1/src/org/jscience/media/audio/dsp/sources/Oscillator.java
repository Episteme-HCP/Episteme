// This code is repackaged after the code from Craig A. Lindley, from Digital Audio with Java
// Site ftp://ftp.prenhall.com/pub/ptr/professional_computer_science.w-022/digital_audio/
// Email
package org.jscience.media.audio.dsp.sources;

import org.jscience.media.audio.dsp.AbstractAudio;
import org.jscience.media.audio.dsp.NegotiationListener;

import java.util.Random;


// NOTE: 16 bit PCM data has a min value of -32768 (8000H)
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class Oscillator extends AbstractAudio {
    /** DOCUMENT ME! */
    public static final int NOTYPE = 0;

    /** DOCUMENT ME! */
    public static final int NOISE = 1;

    /** DOCUMENT ME! */
    public static final int SINEWAVE = 2;

    /** DOCUMENT ME! */
    public static final int TRIANGLEWAVE = 3;

    /** DOCUMENT ME! */
    public static final int SQUAREWAVE = 4;

    // Class data
    /** DOCUMENT ME! */
    protected int type;

    /** DOCUMENT ME! */
    protected int frequency;

    /** DOCUMENT ME! */
    protected int sampleRate;

    /** DOCUMENT ME! */
    protected int numberOfChannels;

    /** DOCUMENT ME! */
    protected NegotiationListener negComplete;

    /** DOCUMENT ME! */
    protected int pos;

    /** DOCUMENT ME! */
    protected short[] waveTable;

    /** DOCUMENT ME! */
    protected double amplitudeAdj;

/**
     * Creates a new Oscillator object.
     *
     * @param type             DOCUMENT ME!
     * @param frequency        DOCUMENT ME!
     * @param sampleRate       DOCUMENT ME!
     * @param numberOfChannels DOCUMENT ME!
     * @param negComplete      DOCUMENT ME!
     */
    public Oscillator(int type, int frequency, int sampleRate,
        int numberOfChannels, NegotiationListener negComplete) {
        super("Oscillator", AbstractAudio.SOURCE);

        // Save incoming
        this.type = type;
        this.frequency = frequency;
        this.sampleRate = sampleRate;
        this.numberOfChannels = numberOfChannels;
        this.negComplete = negComplete;

        // Set amplitude adjustment
        amplitudeAdj = 1.0;

        // Table of samples for oscillator waveform
        waveTable = null;

        // Generate wave table
        buildWaveTable();
    }

    // Constructor with reasonable defaults
    /**
     * Creates a new Oscillator object.
     *
     * @param negComplete DOCUMENT ME!
     */
    public Oscillator(NegotiationListener negComplete) {
        this(SINEWAVE, 1000, 22050, 1, negComplete);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getOscType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     */
    public void setOscType(int type) {
        this.type = type;

        buildWaveTable();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * DOCUMENT ME!
     *
     * @param frequency DOCUMENT ME!
     */
    public void setFrequency(int frequency) {
        this.frequency = frequency;

        // Reset waveTable index
        pos = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSampleRate() {
        return sampleRate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param sampleRate DOCUMENT ME!
     */
    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;

        buildWaveTable();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumberOfChannels() {
        return numberOfChannels;
    }

    /**
     * DOCUMENT ME!
     *
     * @param numberOfChannels DOCUMENT ME!
     */
    public void setNumberOfChannels(int numberOfChannels) {
        this.numberOfChannels = numberOfChannels;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAmplitudeAdj() {
        return amplitudeAdj;
    }

    /**
     * DOCUMENT ME!
     *
     * @param amplitudeAdj DOCUMENT ME!
     */
    public void setAmplitudeAdj(double amplitudeAdj) {
        this.amplitudeAdj = amplitudeAdj;
    }

    // Generate a wavetable for the waveform
    /**
     * DOCUMENT ME!
     */
    protected void buildWaveTable() {
        if (type == NOTYPE) {
            return;
        }

        // Initialize waveTable index as wave table is changing
        pos = 0;

        // Allocate a table for 1 cycle of waveform
        waveTable = new short[sampleRate];

        switch (type) {
        case NOISE:

            // Create a random number generator for returning gaussian
            // distributed numbers. The result is white noise.
            Random random = new Random();

            for (int sample = 0; sample < sampleRate; sample++)
                waveTable[sample] = (short) ((65535.0 * random.nextGaussian()) -
                    32768);

            break;

        case SINEWAVE:

            double scale = (2.0 * Math.PI) / sampleRate;

            for (int sample = 0; sample < sampleRate; sample++)
                waveTable[sample] = (short) (32767.0 * Math.sin(sample * scale));

            break;

        case TRIANGLEWAVE:

            double sign = 1.0;
            double value = 0.0;

            int oneQuarterWave = sampleRate / 4;
            int threeQuarterWave = (3 * sampleRate) / 4;

            scale = 32767.0 / oneQuarterWave;

            for (int sample = 0; sample < sampleRate; sample++) {
                if ((sample > oneQuarterWave) && (sample <= threeQuarterWave)) {
                    sign = -1.0;
                } else {
                    sign = 1.0;
                }

                value += (sign * scale);
                waveTable[sample] = (short) value;
            }

            break;

        case SQUAREWAVE:

            for (int sample = 0; sample < sampleRate; sample++) {
                if (sample < (sampleRate / 2)) {
                    waveTable[sample] = 32767;
                } else {
                    waveTable[sample] = -32768;
                }
            }

            break;
        }
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
        int sample = 0;
        int count = length;

        while (count-- != 0) {
            buffer[sample++] = (short) (amplitudeAdj * waveTable[pos]);

            pos += frequency;

            if (pos >= sampleRate) {
                pos -= sampleRate;
            }
        }

        return length;
    }

    // We know this is first device in chain so no propagation
    /**
     * DOCUMENT ME!
     *
     * @param min DOCUMENT ME!
     * @param max DOCUMENT ME!
     * @param preferred DOCUMENT ME!
     */
    public void minMaxSamplingRate(int min, int max, int preferred) {
        // Use the sample rate passed in
        max = sampleRate;
        min = sampleRate;
        preferred = sampleRate;

        // Determine if there is a listener interested in whether
        // negotations have been completed or not.
        if (negComplete != null) {
            negComplete.negotiationCompletedEvent();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param min DOCUMENT ME!
     * @param max DOCUMENT ME!
     * @param preferred DOCUMENT ME!
     */
    public void minMaxChannels(int min, int max, int preferred) {
        min = numberOfChannels;
        max = numberOfChannels;
        preferred = numberOfChannels;
    }
}
