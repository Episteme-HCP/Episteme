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

package org.jscience.social.arts;

import java.io.Serializable;
import java.util.List;
import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Analytical tool for extracting and evaluating color palettes within artworks. 
 * It employs the CIELAB color space to calculate perceptual differences 
 * (Delta E) and analyze color harmony and dominance.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class ColorPaletteAnalyzer {

    private ColorPaletteAnalyzer() {}

    /**
     * Represents a color in the CIE 1976 (L*, a*, b*) color space. 
     * L* represents lightness, a* represents greenâ€“red, and b* represents blueâ€“yellow.
     */
    public record ColorLAB(double l, double a, double b) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Calculates the Delta E distance between two colors using the CIE76 formula. 
     * This metric quantifies the perceptual difference between colors as 
     * experienced by the human eye.
     * 
     * @param c1 the first color
     * @param c2 the second color
     * @return the Euclidean distance (perceptual difference) as a Real
     */
    public static Real deltaE(ColorLAB c1, ColorLAB c2) {
        if (c1 == null || c2 == null) return Real.ZERO;
        double diff = Math.sqrt(Math.pow(c1.l() - c2.l(), 2) + 
                                Math.pow(c1.a() - c2.a(), 2) + 
                                Math.pow(c1.b() - c2.b(), 2));
        return Real.of(diff);
    }

    /**
     * Computes the complementary color in the CIELAB space by inverting the 
     * chromatic a* and b* components while maintaining constant lightness.
     * 
     * @param color the source color
     * @return the complementary color coordinate
     */
    public static ColorLAB getComplementary(ColorLAB color) {
        if (color == null) return new ColorLAB(0, 0, 0);
        return new ColorLAB(color.l(), -color.a(), -color.b());
    }

    /**
     * Calculates the dominance fraction of a target color within a palette 
     * based on a perceptual similarity threshold.
     * 
     * @param target the reference color to measure dominance for
     * @param palette the list of colors representing the artwork's palette
     * @param threshold the Delta E limit below which colors are considered 'neighboring'
     * @return fraction (0.0 to 1.0) of the palette dominated by the target color
     */
    public static double calculateDominance(ColorLAB target, List<ColorLAB> palette, double threshold) {
        if (target == null || palette == null || palette.isEmpty()) return 0.0;
        
        long count = palette.stream()
            .filter(c -> deltaE(target, c).doubleValue() < threshold)
            .count();
        return (double) count / palette.size();
    }
}

