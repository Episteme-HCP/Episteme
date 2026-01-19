package org.jscience.arts;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Extracts and analyzes color palettes from artworks.
 */
public final class ColorPaletteAnalyzer {

    private ColorPaletteAnalyzer() {}

    public record ColorLAB(double l, double a, double b) {}

    /**
     * Calculates Delta E (CIE76) difference between two colors.
     */
    public static Real deltaE(ColorLAB c1, ColorLAB c2) {
        double diff = Math.sqrt(Math.pow(c1.l() - c2.l(), 2) + 
                                Math.pow(c1.a() - c2.a(), 2) + 
                                Math.pow(c1.b() - c2.b(), 2));
        return Real.of(diff);
    }

    /**
     * Finds complementary color (simplified in LAB space).
     */
    public static ColorLAB getComplementary(ColorLAB color) {
        return new ColorLAB(color.l(), -color.a(), -color.b());
    }

    /**
     * Calculates the dominance of a color in a set based on proximity.
     */
    public static double calculateDominance(ColorLAB target, List<ColorLAB> palette, double threshold) {
        long count = palette.stream()
            .filter(c -> deltaE(target, c).doubleValue() < threshold)
            .count();
        return (double) count / palette.size();
    }
}
