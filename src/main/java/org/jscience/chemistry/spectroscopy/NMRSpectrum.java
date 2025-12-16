package org.jscience.chemistry.spectroscopy;

/**
 * Nuclear Magnetic Resonance (NMR) spectroscopy models.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class NMRSpectrum {

    /**
     * Calculates Larmor frequency.
     * v = (gamma * B0) / (2 * pi)
     * 
     * @param gyromagneticRatio gamma (rad s^-1 T^-1)
     * @param magneticField     B0 (Tesla)
     * @return Frequency (Hz)
     */
    public static double calculateLarmorFrequency(double gyromagneticRatio, double magneticField) {
        return (gyromagneticRatio * magneticField) / (2 * Math.PI);
    }

    /**
     * Calculates chemical shift (delta).
     * delta = (v - v_ref) / v_spectrometer * 10^6
     * 
     * @param frequencyObserved     v (Hz)
     * @param frequencyReference    v_ref (Hz, e.g., TMS)
     * @param spectrometerFrequency v_spectrometer (Hz)
     * @return Chemical shift (ppm)
     */
    public static double calculateChemicalShift(double frequencyObserved, double frequencyReference,
            double spectrometerFrequency) {
        return ((frequencyObserved - frequencyReference) / spectrometerFrequency) * 1e6;
    }

    // Common Gyromagnetic Ratios (rad/s/T)
    public static final double GAMMA_1H = 267.513e6;
    public static final double GAMMA_13C = 67.262e6;
    public static final double GAMMA_15N = -27.116e6;
    public static final double GAMMA_31P = 108.291e6;
}
