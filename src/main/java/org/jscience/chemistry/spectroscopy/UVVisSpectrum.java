package org.jscience.chemistry.spectroscopy;

/**
 * UV-Vis Spectroscopy models.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class UVVisSpectrum {

    /**
     * Beer-Lambert Law.
     * A = epsilon * c * l
     * 
     * @param epsilon       Molar attenuation coefficient (L/(mol·cm))
     * @param concentration Molar concentration (mol/L)
     * @param pathLength    Path length (cm)
     * @return Absorbance (unitless)
     */
    public static double calculateAbsorbance(double epsilon, double concentration, double pathLength) {
        return epsilon * concentration * pathLength;
    }

    /**
     * Calculates Transmittance from Absorbance.
     * T = 10^(-A)
     * 
     * @param A Absorbance
     * @return Transmittance (0.0 to 1.0)
     */
    public static double calculateTransmittance(double A) {
        return Math.pow(10, -A);
    }

    /**
     * Converts Wavelength (nm) to Energy (eV).
     * E = hc / lambda
     */
    public static double wavelengthToEnergy(double nm) {
        // h = 4.135667696e-15 eV·s
        // c = 2.998e8 m/s
        // lambda = nm * 1e-9
        return 1239.8 / nm; // Approximation: 1240 / nm
    }
}
