/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.arts;

import java.io.Serializable;
import java.util.Objects;

/**
 * Provides analytical tools for color measurement and comparison (Colorimetry).
 * Implements standard color space transformations and difference calculations 
 * used in art conservation and pigment analysis.
 * 
 * <p>Reference: CIE (1976). Colorimetry.</p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class ColorMetrology {

    private ColorMetrology() {}

    /**
     * Represents a color in the CIELAB (CIE 1976 L*a*b*) color space.
     * 
     * @param l lightness (0 to 100)
     * @param a green–red color component
     * @param b blue–yellow color component
     */
    public record LabColor(double l, double a, double b) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Calculates the Delta E (CIE76) difference between two colors.
     * This represents the perceived difference between colors based 
     * on Euclidean distance in CIELAB space.
     * 
     * @param c1 first color
     * @param c2 second color
     * @return the Delta E value (typically < 1.0 is imperceptible)
     */
    public static double deltaE76(LabColor c1, LabColor c2) {
        Objects.requireNonNull(c1, "Color 1 cannot be null");
        Objects.requireNonNull(c2, "Color 2 cannot be null");
        return Math.sqrt(Math.pow(c2.l() - c1.l(), 2) + 
                         Math.pow(c2.a() - c1.a(), 2) + 
                         Math.pow(c2.b() - c1.b(), 2));
    }

    /**
     * Converts RGB color values (0-255) to CIELAB space.
     * Assumes sRGB color space and D65 standard illuminant.
     * 
     * @param r red component (0-255)
     * @param g green component (0-255)
     * @param b blue component (0-255)
     * @return equivalent LabColor
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

        // XYZ to Lab (D65)
        x /= 95.047;
        y /= 100.0;
        z /= 108.883;

        x = (x > 0.008856) ? Math.pow(x, 1/3.0) : (7.787 * x) + (16/116.0);
        y = (y > 0.008856) ? Math.pow(y, 1/3.0) : (7.787 * y) + (16/116.0);
        z = (z > 0.008856) ? Math.pow(z, 1/3.0) : (7.787 * z) + (16/116.0);

        return new LabColor((116.0 * y) - 16.0, 500.0 * (x - y), 200.0 * (y - z));
    }
}
