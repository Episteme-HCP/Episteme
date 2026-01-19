package org.jscience.arts;

import java.util.Objects;

/**
 * Science of color measurement and comparison.
 * 
 * References:
 * - CIE (1976). Colorimetry.
 */
public final class ColorMetrology {

    private ColorMetrology() {}

    /**
     * Represents a color in the CIELAB color space.
     */
    public record LabColor(double l, double a, double b) {}

    /**
     * Calculates the Delta E (CIE76) distance between two colors.
     * This is the Euclidean distance in Lab space.
     * 
     * @param c1 First color.
     * @param c2 Second color.
     * @return The color difference value.
     */
    public static double deltaE76(LabColor c1, LabColor c2) {
        Objects.requireNonNull(c1);
        Objects.requireNonNull(c2);
        return Math.sqrt(Math.pow(c2.l() - c1.l(), 2) + 
                         Math.pow(c2.a() - c1.a(), 2) + 
                         Math.pow(c2.b() - c1.b(), 2));
    }

    /**
     * Converts RGB (0-255) to CIELAB space.
     * Simplified conversion (assuming D65 illuminant).
     */
    public static LabColor rgbToLab(int r, int g, int b) {
        // Normalization
        double rNorm = r / 255.0;
        double gNorm = g / 255.0;
        double bNorm = b / 255.0;

        // Gamma correction (sRGB)
        rNorm = (rNorm > 0.04045) ? Math.pow((rNorm + 0.055) / 1.055, 2.4) : rNorm / 12.92;
        gNorm = (gNorm > 0.04045) ? Math.pow((gNorm + 0.055) / 1.055, 2.4) : gNorm / 12.92;
        bNorm = (bNorm > 0.04045) ? Math.pow((bNorm + 0.055) / 1.055, 2.4) : bNorm / 12.92;

        rNorm *= 100.0;
        gNorm *= 100.0;
        bNorm *= 100.0;

        // XYZ conversion
        double x = rNorm * 0.4124 + gNorm * 0.3576 + bNorm * 0.1805;
        double y = rNorm * 0.2126 + gNorm * 0.7152 + bNorm * 0.0722;
        double z = rNorm * 0.0193 + gNorm * 0.1192 + bNorm * 0.9505;

        // XYZ to Lab
        x /= 95.047;
        y /= 100.0;
        z /= 108.883;

        x = (x > 0.008856) ? Math.pow(x, 1/3.0) : (7.787 * x) + (16/116.0);
        y = (y > 0.008856) ? Math.pow(y, 1/3.0) : (7.787 * y) + (16/116.0);
        z = (z > 0.008856) ? Math.pow(z, 1/3.0) : (7.787 * z) + (16/116.0);

        return new LabColor((116.0 * y) - 16.0, 500.0 * (x - y), 200.0 * (y - z));
    }
}
