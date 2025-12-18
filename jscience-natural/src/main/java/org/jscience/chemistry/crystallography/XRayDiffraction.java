package org.jscience.chemistry.crystallography;

/**
 * X-Ray Diffraction calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class XRayDiffraction {

    /**
     * Bragg's Law.
     * n * lambda = 2 * d * sin(theta)
     * 
     * @param lambda Wavelength of X-ray
     * @param d      Interplanar spacing
     * @param n      Order of diffraction (integer)
     * @return Scattering angle theta (radians)
     */
    public static double calculateBraggAngle(double lambda, double d, int n) {
        // sin(theta) = (n * lambda) / (2 * d)
        double sinTheta = (n * lambda) / (2 * d);
        if (sinTheta > 1 || sinTheta < -1)
            return Double.NaN;
        return Math.asin(sinTheta);
    }
}
