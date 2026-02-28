/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.arts;

import org.episteme.core.mathematics.numbers.real.Real;

/**
 * Advanced color science utility class for color space transformations, 
 * perceptual difference calculations (Delta E), and color harmony generation.
 * This class provides high-precision implementations of CIE colorimetry standards.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class ColorScience {

    private ColorScience() {}

    // sRGB to XYZ conversion matrix (D65 illuminant)
    private static final double[][] RGB_TO_XYZ = {
        {0.4124564, 0.3575761, 0.1804375},
        {0.2126729, 0.7151522, 0.0721750},
        {0.0193339, 0.1191920, 0.9503041}
    };

    /**
     * Converts sRGB (0-255) to CIE Lab color space.
     * 
     * @param r red component (0-255)
     * @param g green component (0-255)
     * @param b blue component (0-255)
     * @return array containing {L, a, b} components
     */
    public static double[] rgbToLab(int r, int g, int b) {
        double[] xyz = rgbToXyz(r, g, b);
        return xyzToLab(xyz[0], xyz[1], xyz[2]);
    }

    /**
     * Converts sRGB to CIE XYZ.
     * 
     * @param r red component (0-255)
     * @param g green component (0-255)
     * @param b blue component (0-255)
     * @return array containing {X, Y, Z} components
     */
    public static double[] rgbToXyz(int r, int g, int b) {
        double[] rgb = new double[3];
        rgb[0] = pivotRgb(r / 255.0);
        rgb[1] = pivotRgb(g / 255.0);
        rgb[2] = pivotRgb(b / 255.0);

        double x = RGB_TO_XYZ[0][0] * rgb[0] + RGB_TO_XYZ[0][1] * rgb[1] + RGB_TO_XYZ[0][2] * rgb[2];
        double y = RGB_TO_XYZ[1][0] * rgb[0] + RGB_TO_XYZ[1][1] * rgb[1] + RGB_TO_XYZ[1][2] * rgb[2];
        double z = RGB_TO_XYZ[2][0] * rgb[0] + RGB_TO_XYZ[2][1] * rgb[1] + RGB_TO_XYZ[2][2] * rgb[2];

        return new double[]{x * 100, y * 100, z * 100};
    }

    /**
     * Converts CIE XYZ to CIE Lab (D65 illuminant).
     * 
     * @param x X component
     * @param y Y component
     * @param z Z component
     * @return array containing {L, a, b} components
     */
    public static double[] xyzToLab(double x, double y, double z) {
        // D65 reference white
        double refX = 95.047, refY = 100.0, refZ = 108.883;

        double fx = pivotXyz(x / refX);
        double fy = pivotXyz(y / refY);
        double fz = pivotXyz(z / refZ);

        double L = 116 * fy - 16;
        double a = 500 * (fx - fy);
        double b = 200 * (fy - fz);

        return new double[]{L, a, b};
    }

    /**
     * Calculates Delta E (CIE76) - Euclidean distance in CIELAB space.
     * 
     * @param lab1 first color in Lab space
     * @param lab2 second color in Lab space
     * @return the color difference as a Real value
     */
    public static Real deltaE76(double[] lab1, double[] lab2) {
        double dL = lab1[0] - lab2[0];
        double da = lab1[1] - lab2[1];
        double db = lab1[2] - lab2[2];
        return Real.of(Math.sqrt(dL * dL + da * da + db * db));
    }

    /**
     * Calculates Delta E (CIE2000) - modern standard for perceptual uniformity.
     * This formula is significantly more complex than CIE76 but much closer 
     * to human perception.
     * 
     * @param lab1 first color in Lab space
     * @param lab2 second color in Lab space
     * @return the perceived color difference as a Real value
     */
    public static Real deltaE2000(double[] lab1, double[] lab2) {
        double L1 = lab1[0], a1 = lab1[1], b1 = lab1[2];
        double L2 = lab2[0], a2 = lab2[1], b2 = lab2[2];

        double C1 = Math.sqrt(a1 * a1 + b1 * b1);
        double C2 = Math.sqrt(a2 * a2 + b2 * b2);
        double Cmean = (C1 + C2) / 2.0;

        double G = 0.5 * (1 - Math.sqrt(Math.pow(Cmean, 7) / (Math.pow(Cmean, 7) + Math.pow(25, 7))));
        double a1p = a1 * (1 + G);
        double a2p = a2 * (1 + G);

        double C1p = Math.sqrt(a1p * a1p + b1 * b1);
        double C2p = Math.sqrt(a2p * a2p + b2 * b2);
        double h1p = Math.toDegrees(Math.atan2(b1, a1p));
        double h2p = Math.toDegrees(Math.atan2(b2, a2p));
        if (h1p < 0) h1p += 360;
        if (h2p < 0) h2p += 360;

        double dLp = L2 - L1;
        double dCp = C2p - C1p;
        double dhp = h2p - h1p;
        if (Math.abs(dhp) > 180) dhp -= 360 * Math.signum(dhp);
        double dHp = 2 * Math.sqrt(C1p * C2p) * Math.sin(Math.toRadians(dhp / 2));

        double Lpmean = (L1 + L2) / 2;
        double Cpmean = (C1p + C2p) / 2;
        double hpmean = (h1p + h2p) / 2;
        if (Math.abs(h1p - h2p) > 180) hpmean += 180;

        double T = 1 - 0.17 * Math.cos(Math.toRadians(hpmean - 30))
                     + 0.24 * Math.cos(Math.toRadians(2 * hpmean))
                     + 0.32 * Math.cos(Math.toRadians(3 * hpmean + 6))
                     - 0.20 * Math.cos(Math.toRadians(4 * hpmean - 63));

        double SL = 1 + (0.015 * Math.pow(Lpmean - 50, 2)) / Math.sqrt(20 + Math.pow(Lpmean - 50, 2));
        double SC = 1 + 0.045 * Cpmean;
        double SH = 1 + 0.015 * Cpmean * T;

        double RT = -2 * Math.sqrt(Math.pow(Cpmean, 7) / (Math.pow(Cpmean, 7) + Math.pow(25, 7)))
                    * Math.sin(Math.toRadians(60 * Math.exp(-Math.pow((hpmean - 275) / 25, 2))));

        double dE = Math.sqrt(Math.pow(dLp / SL, 2) + Math.pow(dCp / SC, 2) + Math.pow(dHp / SH, 2)
                              + RT * (dCp / SC) * (dHp / SH));
        return Real.of(dE);
    }

    /**
     * Generates the complementary color (opposite on the color wheel).
     * 
     * @param r red component
     * @param g green component
     * @param b blue component
     * @return array containing {R, G, B} of the complementary color
     */
    public static int[] complementary(int r, int g, int b) {
        return new int[]{255 - r, 255 - g, 255 - b};
    }

    /**
     * Generates triadic colors (three colors spaced 120Â° apart on the color wheel).
     * 
     * @param r red component
     * @param g green component
     * @param b blue component
     * @return array of two RGB arrays representing the other two colors in the triad
     */
    public static int[][] triadic(int r, int g, int b) {
        double[] hsv = rgbToHsv(r, g, b);
        return new int[][]{
            hsvToRgb((hsv[0] + 120) % 360, hsv[1], hsv[2]),
            hsvToRgb((hsv[0] + 240) % 360, hsv[1], hsv[2])
        };
    }

    private static double[] rgbToHsv(int r, int g, int b) {
        double rf = r / 255.0, gf = g / 255.0, bf = b / 255.0;
        double max = Math.max(rf, Math.max(gf, bf));
        double min = Math.min(rf, Math.min(gf, bf));
        double d = max - min;
        double h = 0, s = (max == 0) ? 0 : d / max, v = max;
        if (d != 0) {
            if (max == rf) h = 60 * ((gf - bf) / d % 6);
            else if (max == gf) h = 60 * ((bf - rf) / d + 2);
            else h = 60 * ((rf - gf) / d + 4);
        }
        if (h < 0) h += 360;
        return new double[]{h, s, v};
    }

    private static int[] hsvToRgb(double h, double s, double v) {
        double c = v * s;
        double x = c * (1 - Math.abs((h / 60) % 2 - 1));
        double m = v - c;
        double rf, gf, bf;
        if (h < 60) { rf = c; gf = x; bf = 0; }
        else if (h < 120) { rf = x; gf = c; bf = 0; }
        else if (h < 180) { rf = 0; gf = c; bf = x; }
        else if (h < 240) { rf = 0; gf = x; bf = c; }
        else if (h < 300) { rf = x; gf = 0; bf = c; }
        else { rf = c; gf = 0; bf = x; }
        return new int[]{(int)((rf + m) * 255), (int)((gf + m) * 255), (int)((bf + m) * 255)};
    }

    private static double pivotRgb(double n) {
        return (n > 0.04045) ? Math.pow((n + 0.055) / 1.055, 2.4) : n / 12.92;
    }

    private static double pivotXyz(double n) {
        return (n > 0.008856) ? Math.cbrt(n) : (7.787 * n) + (16.0 / 116.0);
    }
}

