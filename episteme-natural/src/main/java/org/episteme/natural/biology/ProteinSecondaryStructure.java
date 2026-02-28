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

package org.episteme.natural.biology;

import org.episteme.core.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Predicts protein secondary structure (alpha helices, beta sheets, coils).
 */
public final class ProteinSecondaryStructure {

    private ProteinSecondaryStructure() {}

    public enum StructureType {
        HELIX('H'),
        SHEET('E'),
        COIL('C');

        private final char symbol;
        StructureType(char symbol) { this.symbol = symbol; }
        public char getSymbol() { return symbol; }
    }

    public record StructurePrediction(
        String sequence,
        String prediction,      // H/E/C for each position
        double[] helixProbabilities,
        double[] sheetProbabilities,
        double[] coilProbabilities
    ) {}

    // Chou-Fasman propensity values (simplified)
    private static final Map<Character, double[]> PROPENSITIES = Map.ofEntries(
        // Format: [P_helix, P_sheet, P_coil]
        Map.entry('A', new double[]{1.42, 0.83, 0.66}),  // Alanine - strong helix former
        Map.entry('R', new double[]{0.98, 0.93, 1.03}),  // Arginine
        Map.entry('N', new double[]{0.67, 0.89, 1.33}),  // Asparagine - helix breaker
        Map.entry('D', new double[]{1.01, 0.54, 1.46}),  // Aspartic acid
        Map.entry('C', new double[]{0.70, 1.19, 1.19}),  // Cysteine
        Map.entry('E', new double[]{1.51, 0.37, 1.16}),  // Glutamic acid - strong helix
        Map.entry('Q', new double[]{1.11, 1.10, 0.98}),  // Glutamine
        Map.entry('G', new double[]{0.57, 0.75, 1.56}),  // Glycine - helix breaker
        Map.entry('H', new double[]{1.00, 0.87, 0.95}),  // Histidine
        Map.entry('I', new double[]{1.08, 1.60, 0.47}),  // Isoleucine - sheet former
        Map.entry('L', new double[]{1.21, 1.30, 0.59}),  // Leucine
        Map.entry('K', new double[]{1.16, 0.74, 1.07}),  // Lysine
        Map.entry('M', new double[]{1.45, 1.05, 0.60}),  // Methionine
        Map.entry('F', new double[]{1.13, 1.38, 0.60}),  // Phenylalanine
        Map.entry('P', new double[]{0.57, 0.55, 1.52}),  // Proline - helix breaker
        Map.entry('S', new double[]{0.77, 0.75, 1.32}),  // Serine
        Map.entry('T', new double[]{0.83, 1.19, 0.96}),  // Threonine
        Map.entry('W', new double[]{1.08, 1.37, 0.96}),  // Tryptophan
        Map.entry('Y', new double[]{0.69, 1.47, 0.76}),  // Tyrosine
        Map.entry('V', new double[]{1.06, 1.70, 0.50})   // Valine - strong sheet
    );

    /**
     * Predicts secondary structure using Chou-Fasman method.
     */
    public static StructurePrediction predict(String sequence) {
        int n = sequence.length();
        double[] helixProb = new double[n];
        double[] sheetProb = new double[n];
        double[] coilProb = new double[n];
        
        // Window-based prediction
        int window = 6;
        
        for (int i = 0; i < n; i++) {
            double hSum = 0, eSum = 0, cSum = 0;
            
            
            for (int j = Math.max(0, i - window/2); j < Math.min(n, i + window/2); j++) {
                char aa = sequence.charAt(j);
                double[] prop = PROPENSITIES.getOrDefault(Character.toUpperCase(aa), 
                    new double[]{1.0, 1.0, 1.0});
                hSum += prop[0];
                eSum += prop[1];
                cSum += prop[2];
            }
            
            double total = hSum + eSum + cSum;
            helixProb[i] = hSum / total;
            sheetProb[i] = eSum / total;
            coilProb[i] = cSum / total;
        }
        
        // Assign structure based on highest probability
        StringBuilder prediction = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (helixProb[i] >= sheetProb[i] && helixProb[i] >= coilProb[i]) {
                prediction.append('H');
            } else if (sheetProb[i] >= helixProb[i] && sheetProb[i] >= coilProb[i]) {
                prediction.append('E');
            } else {
                prediction.append('C');
            }
        }
        
        // Post-process: enforce minimum segment lengths
        String refined = refineSegments(prediction.toString());
        
        return new StructurePrediction(sequence, refined, helixProb, sheetProb, coilProb);
    }

    /**
     * Calculates secondary structure composition.
     */
    public static Map<StructureType, Real> calculateComposition(String prediction) {
        int helix = 0, sheet = 0, coil = 0;
        for (char c : prediction.toCharArray()) {
            switch (c) {
                case 'H' -> helix++;
                case 'E' -> sheet++;
                case 'C' -> coil++;
            }
        }
        
        int total = prediction.length();
        return Map.of(
            StructureType.HELIX, Real.of((double) helix / total),
            StructureType.SHEET, Real.of((double) sheet / total),
            StructureType.COIL, Real.of((double) coil / total)
        );
    }

    /**
     * Finds secondary structure elements (segments).
     */
    public static List<Map.Entry<StructureType, int[]>> findElements(String prediction) {
        List<Map.Entry<StructureType, int[]>> elements = new ArrayList<>();
        
        if (prediction.isEmpty()) return elements;
        
        char current = prediction.charAt(0);
        int start = 0;
        
        for (int i = 1; i <= prediction.length(); i++) {
            char c = i < prediction.length() ? prediction.charAt(i) : '\0';
            if (c != current) {
                StructureType type = switch (current) {
                    case 'H' -> StructureType.HELIX;
                    case 'E' -> StructureType.SHEET;
                    default -> StructureType.COIL;
                };
                elements.add(Map.entry(type, new int[]{start, i - 1}));
                start = i;
                current = c;
            }
        }
        
        return elements;
    }

    private static String refineSegments(String raw) {
        char[] refined = raw.toCharArray();
        int minHelixLen = 4;
        int minSheetLen = 3;
        
        // Remove isolated structures
        char[] result = refined.clone();
        for (int i = 0; i < result.length; i++) {
            if (result[i] == 'H') {
                int len = countRun(result, i, 'H');
                if (len < minHelixLen) {
                    Arrays.fill(result, i, i + len, 'C');
                }
                i += len - 1;
            } else if (result[i] == 'E') {
                int len = countRun(result, i, 'E');
                if (len < minSheetLen) {
                    Arrays.fill(result, i, i + len, 'C');
                }
                i += len - 1;
            }
        }
        
        return new String(result);
    }

    private static int countRun(char[] arr, int start, char c) {
        int count = 0;
        for (int i = start; i < arr.length && arr[i] == c; i++) {
            count++;
        }
        return count;
    }
}

