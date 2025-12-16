package org.jscience.chemistry.crystallography;

/**
 * Miller Indices (hkl) for crystal planes.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class MillerIndices {

    public final int h, k, l;

    public MillerIndices(int h, int k, int l) {
        this.h = h;
        this.k = k;
        this.l = l;
    }

    /**
     * Calculates interplanar spacing (d-spacing) for a Cubic system.
     * d = a / sqrt(h^2 + k^2 + l^2)
     * 
     * @param a Lattice constant (length)
     * @return d-spacing
     */
    public double calculateInterplanarSpacingCubic(double a) {
        return a / Math.sqrt(h * h + k * k + l * l);
    }

    @Override
    public String toString() {
        return String.format("(%d %d %d)", h, k, l);
    }
}
