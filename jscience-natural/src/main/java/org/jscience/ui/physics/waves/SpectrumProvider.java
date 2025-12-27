package org.jscience.ui.physics.waves;

/**
 * Interface for Spectrum Generation (FFT or Mock).
 * Allows benchmarking Object vs Primitive implementations.
 */
public interface SpectrumProvider {

    /**
     * Updates the spectrum based on time and sensitivity.
     */
    void update(double time, double sensitivity);

    /**
     * Returns the magnitude array.
     */
    double[] getSpectrum();

    String getName();

    /**
     * Sets the simulated audio source pattern.
     * 
     * @param pattern "Sine", "Noise", "Harmonics", "Voice"
     */
    void setSourcePattern(String pattern);
}
