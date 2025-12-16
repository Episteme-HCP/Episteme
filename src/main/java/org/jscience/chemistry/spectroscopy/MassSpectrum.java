package org.jscience.chemistry.spectroscopy;

/**
 * Mass Spectrometry models.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class MassSpectrum {

    /**
     * Calculates Mass-to-Charge ratio (m/z).
     * 
     * @param mass   Mass in Daltons (u)
     * @param charge Charge (integer, e.g. +1, +2)
     * @return m/z ratio
     */
    public static double calculateMZ(double mass, int charge) {
        if (charge == 0)
            throw new IllegalArgumentException("Charge cannot be zero");
        // Usually mass of electron is subtracted for positive ions formed by electron
        // loss
        // M+ = (M_neutral - m_e) / 1
        return mass / (double) Math.abs(charge);
    }

    /**
     * Radius of curvature in magnetic field.
     * r = (1/B) * sqrt(2Vm/q)
     * or for velocity selector:
     * qvB = mv^2/r -> r = mv/qB
     */
    public static double cycloidalRadius(double massKg, double velocity, double chargeC, double magneticFieldTesla) {
        return (massKg * velocity) / (chargeC * magneticFieldTesla);
    }
}
