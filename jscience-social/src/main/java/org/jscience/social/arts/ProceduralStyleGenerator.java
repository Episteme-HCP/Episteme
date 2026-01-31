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

import org.jscience.core.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Procedural generation of artistic styles based on historical parameters.
 */
public final class ProceduralStyleGenerator {

    private ProceduralStyleGenerator() {}

    /**
     * Historical art periods with their characteristic parameters.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum Period {
        MEDIEVAL(false, 0.0, true, 0.3),
        RENAISSANCE(true, 1.0, false, 0.7),
        BAROQUE(true, 0.8, false, 0.9),
        IMPRESSIONIST(false, 0.5, false, 0.6),
        MODERN(false, 0.3, false, 0.4);

        private final boolean linearPerspective;
        private final double perspectiveStrength;
        private final boolean hierarchicalScale;
        private final double contrastLevel;

        Period(boolean linearPerspective, double perspectiveStrength, 
               boolean hierarchicalScale, double contrastLevel) {
            this.linearPerspective = linearPerspective;
            this.perspectiveStrength = perspectiveStrength;
            this.hierarchicalScale = hierarchicalScale;
            this.contrastLevel = contrastLevel;
        }

        public boolean usesLinearPerspective() { return linearPerspective; }
        public double getPerspectiveStrength() { return perspectiveStrength; }
        public boolean usesHierarchicalScale() { return hierarchicalScale; }
        public double getContrastLevel() { return contrastLevel; }
    }

    /**
     * Generates composition parameters for a given period.
     */
    public static Map<String, Object> generateCompositionRules(Period period) {
        Map<String, Object> rules = new HashMap<>();
        
        rules.put("perspective.linear", period.usesLinearPerspective());
        rules.put("perspective.strength", period.getPerspectiveStrength());
        rules.put("scale.hierarchical", period.usesHierarchicalScale());
        rules.put("contrast", period.getContrastLevel());
        
        // Vanishing point rules
        if (period.usesLinearPerspective()) {
            rules.put("vanishing.points", period == Period.RENAISSANCE ? 1 : 2);
            rules.put("horizon.position", 0.5); // Middle of canvas
        } else {
            rules.put("vanishing.points", 0);
            rules.put("scale.importance_based", period.usesHierarchicalScale());
        }
        
        // Color palette rules
        rules.put("palette.saturation", period == Period.IMPRESSIONIST ? 0.9 : 0.6);
        rules.put("palette.earth_tones", period == Period.MEDIEVAL || period == Period.BAROQUE);
        
        return rules;
    }

    /**
     * Calculates the size of an element based on importance (Medieval/Byzantine style).
     */
    public static Real hierarchicalScale(Real baseSize, int importanceRank, int totalElements) {
        double factor = 1.0 + (totalElements - importanceRank) * 0.2;
        return baseSize.multiply(Real.of(factor));
    }

    /**
     * Calculates perspective scaling factor for distance from viewer.
     */
    public static Real perspectiveScale(Real distance, Real focalLength, Period period) {
        if (!period.usesLinearPerspective()) {
            return Real.of(1.0); // No perspective scaling
        }
        double d = distance.doubleValue();
        double f = focalLength.doubleValue();
        double scale = f / (f + d * period.getPerspectiveStrength());
        return Real.of(scale);
    }

    /**
     * Generates a period-appropriate color palette.
     */
    public static List<int[]> generatePalette(Period period, int colorCount) {
        List<int[]> palette = new ArrayList<>();
        Random rand = new Random(period.hashCode());
        
        for (int i = 0; i < colorCount; i++) {
            int[] rgb;
            switch (period) {
                case MEDIEVAL:
                    rgb = generateEarthTone(rand);
                    break;
                case RENAISSANCE:
                    rgb = generateBalancedColor(rand);
                    break;
                case BAROQUE:
                    rgb = generateDramaticColor(rand, period.getContrastLevel());
                    break;
                case IMPRESSIONIST:
                    rgb = generateVibrantColor(rand);
                    break;
                default:
                    rgb = new int[]{rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)};
            }
            palette.add(rgb);
        }
        return palette;
    }

    private static int[] generateEarthTone(Random rand) {
        int base = 100 + rand.nextInt(80);
        return new int[]{base + rand.nextInt(40), base - 20 + rand.nextInt(40), base - 40};
    }

    private static int[] generateBalancedColor(Random rand) {
        int r = 80 + rand.nextInt(120);
        int g = 80 + rand.nextInt(120);
        int b = 80 + rand.nextInt(120);
        return new int[]{r, g, b};
    }

    private static int[] generateDramaticColor(Random rand, double contrast) {
        boolean dark = rand.nextBoolean();
        int base = dark ? 30 : 200;
        int variation = (int)(50 * (1 - contrast));
        return new int[]{
            base + rand.nextInt(variation),
            base + rand.nextInt(variation),
            base + rand.nextInt(variation)
        };
    }

    private static int[] generateVibrantColor(Random rand) {
        return new int[]{
            150 + rand.nextInt(105),
            100 + rand.nextInt(155),
            50 + rand.nextInt(200)
        };
    }
}

