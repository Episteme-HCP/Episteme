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

package org.jscience.psychology;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models human memory retention, recall probability, and forgetting curves.
 * Includes implementations of Ebbinghaus's forgetting curve and spacing effect algorithms.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class MemoryModels {

    private MemoryModels() {}

    /**
     * Calculates retention probability using the Ebbinghaus Forgetting Curve.
     * Formula: R = exp(-t/S)
     * 
     * @param t Time since learning (days)
     * @param s Stability of memory (strength), where higher values mean slower forgetting
     * @return Retention probability (0.0 to 1.0)
     */
    public static Real ebbinghausRetention(double t, double s) {
        if (s <= 0) return Real.ZERO;
        return Real.of(Math.exp(-t / s));
    }

    /**
     * Calculates the next optimal review interval using the SuperMemo 2 (SM-2) algorithm.
     * I(n) = I(n-1) * EF
     * 
     * @param iteration    The repetition number (1, 2, 3...)
     * @param prevInterval Previous interval in days
     * @param easeFactor   The Ease Factor (EF), initially usually 2.5
     * @param quality      User grade of recall quality (0-5, where >=3 is success)
     * @return The next interval in days
     */
    public static double sm2NextInterval(int iteration, double prevInterval, double easeFactor, int quality) {
        if (iteration == 1) return 1.0;
        if (iteration == 2) return 6.0;
        
        if (quality < 3) return 1.0; // Reset index if recall failed
        return prevInterval * easeFactor;
    }

    /**
     * Adjusts the Ease Factor (EF) based on recall quality.
     * Implementation of SM-2 EF adjustment.
     * NewEF = EF + (0.1 - (5-q)*(0.08 + (5-q)*0.02))
     * The new EF cannot drop below 1.3.
     *
     * @param oldEf   The previous Ease Factor
     * @param quality User grade of recall quality (0-5)
     * @return The new Ease Factor
     */
    public static double sm2NewEaseFactor(double oldEf, int quality) {
        double newEf = oldEf + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02));
        return Math.max(1.3, newEf);
    }

    /**
     * Models the Serial Position Effect (Primacy and Recency).
     * Calculates recall probability based on item position in a list.
     * Approximates the U-shaped curve where first and last items are recalled best.
     * 
     * @param position   Zero-indexed position of the item
     * @param totalItems Total number of items in the list
     * @return Estimated probability of recall (0.0 to 1.0)
     */
    public static double recallProbability(int position, int totalItems) {
        if (totalItems <= 1) return 1.0;
        
        // Normalized position x from 0 to 1
        double x = (double) position / (totalItems - 1);
        
        // Simple U-shaped quadratic approximation: 0.8 * (4*(x-0.5)^2) + 0.2
        // Min (middle) is ~0.2, Max (ends) is 1.0
        return 0.8 * (Math.pow(x - 0.5, 2) * 4) + 0.2;
    }
}
