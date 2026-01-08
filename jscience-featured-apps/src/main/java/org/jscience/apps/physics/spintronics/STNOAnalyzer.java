/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.analysis.transform.FFT;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Spin-Torque Nano-Oscillator (STNO) spectrum analyzer.
 * <p>
 * Records magnetization dynamics and computes real-time RF spectrum
 * using JScience's distributed/GPU-accelerated FFT module.
 * </p>
 * 
 * <h3>Physics Background</h3>
 * <p>
 * When a spin valve is driven by a DC current above a critical threshold,
 * the magnetization enters a stable precession regime instead of switching.
 * This creates an oscillating resistance (and voltage) at microwave frequencies
 * (1-20 GHz).
 * </p>
 * 
 * @see <a href="https://doi.org/10.1038/nature02050">Kiselev et al., Nature
 *      425, 380 (2003)</a>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class STNOAnalyzer {

    private final int bufferSize;
    private final Real[] mxBuffer;
    private int writeIndex = 0;
    private boolean bufferFilled = false;
    private final double sampleRate; // Samples per second (Hz)

    /**
     * Creates an STNO analyzer with a specified buffer size (must be power of 2).
     * 
     * @param bufferSize Number of samples to collect before FFT (e.g., 1024, 2048)
     * @param dt         Time step of the simulation in seconds
     */
    public STNOAnalyzer(int bufferSize, double dt) {
        // Ensure power of 2
        if ((bufferSize & (bufferSize - 1)) != 0) {
            throw new IllegalArgumentException("Buffer size must be a power of 2");
        }
        this.bufferSize = bufferSize;
        this.mxBuffer = new Real[bufferSize];
        this.sampleRate = 1.0 / dt;

        // Initialize buffer
        for (int i = 0; i < bufferSize; i++) {
            mxBuffer[i] = Real.ZERO;
        }
    }

    /**
     * Records a new sample of the x-component of magnetization.
     * 
     * @param mx The current value of M_x (or any oscillating quantity like
     *           resistance)
     */
    public void recordSample(Real mx) {
        mxBuffer[writeIndex] = mx;
        writeIndex = (writeIndex + 1) % bufferSize;
        if (writeIndex == 0) {
            bufferFilled = true;
        }
    }

    /**
     * Checks if enough samples have been collected for a valid FFT.
     */
    public boolean isReady() {
        return bufferFilled;
    }

    /**
     * Computes the power spectral density (PSD) of the recorded signal.
     * Uses JScience's FFT module (GPU/distributed if available).
     * 
     * @return Array of power values. Index i corresponds to frequency i *
     *         (sampleRate / bufferSize).
     */
    public Real[] computePowerSpectrum() {
        // Create a copy reordered from the circular buffer
        Real[] data = new Real[bufferSize];
        for (int i = 0; i < bufferSize; i++) {
            int srcIndex = (writeIndex + i) % bufferSize;
            data[i] = mxBuffer[srcIndex];
        }

        // Use JScience FFT
        return FFT.powerSpectrum(data);
    }

    /**
     * Gets the frequency axis corresponding to the PSD output.
     * 
     * @return Array of frequencies in Hz (or GHz if scaled).
     */
    public double[] getFrequencyAxis() {
        double[] freq = new double[bufferSize / 2]; // Only positive frequencies
        double resolution = sampleRate / bufferSize;
        for (int i = 0; i < freq.length; i++) {
            freq[i] = i * resolution;
        }
        return freq;
    }

    /**
     * Gets the peak oscillation frequency from the latest spectrum.
     * 
     * @return Frequency in Hz of the dominant peak.
     */
    public double getPeakFrequency() {
        Real[] psd = computePowerSpectrum();
        double[] freq = getFrequencyAxis();

        int peakIndex = 1; // Skip DC component (index 0)
        Real peakValue = psd[1];

        for (int i = 2; i < freq.length; i++) {
            if (psd[i].compareTo(peakValue) > 0) {
                peakValue = psd[i];
                peakIndex = i;
            }
        }

        return freq[peakIndex];
    }

    /**
     * Resets the analyzer for a new measurement.
     */
    public void reset() {
        writeIndex = 0;
        bufferFilled = false;
        for (int i = 0; i < bufferSize; i++) {
            mxBuffer[i] = Real.ZERO;
        }
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public double getSampleRate() {
        return sampleRate;
    }
}
