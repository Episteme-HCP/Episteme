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

package org.jscience.social.history;

import org.jscience.core.mathematics.numbers.real.Real;
import java.util.Objects;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;

/**
 * Analyzes historical coins, their physical characteristics, and economic implications.
 * Includes tools for calculating intrinsic value and modeling economic laws like Gresham's Law.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class NumismaticAnalyzer {

    private NumismaticAnalyzer() {
        // Prevent instantiation
    }

    /**
     * Data record representing a historical coin and its metal composition.
     */
    @Persistent
    public record Coin(
        @Attribute String id,
        @Attribute double weightGrams,
        @Attribute double purityRatio // 0.0 - 1.0
    ) implements java.io.Serializable {
        private static final long serialVersionUID = 1L;

        public Coin {
            Objects.requireNonNull(id, "ID cannot be null");
        }
    }

    /**
     * Calculates the intrinsic metal value of a coin.
     * 
     * @param coin the coin to analyze
     * @param metalPricePerGram current market price of the precious metal
     * @return intrinsic value as a Real number
     * @throws NullPointerException if coin is null
     */
    public static Real calculateIntrinsicValue(Coin coin, double metalPricePerGram) {
        Objects.requireNonNull(coin, "Coin cannot be null");
        return Real.of(coin.weightGrams() * coin.purityRatio() * metalPricePerGram);
    }

    /**
     * Models Gresham's Law: "Bad money drives out good."
     * Estimates the probability of "good" money being hoarded when "bad" money is in circulation.
     * 
     * @param intrinsic1 intrinsic value of currency 1
     * @param face1 nominal face value of currency 1
     * @param intrinsic2 intrinsic value of currency 2
     * @param face2 nominal face value of currency 2
     * @return probability (0.0 to 1.0) of currency 1 being hoarded
     */
    public static double hoardingProbability(double intrinsic1, double face1, 
                                             double intrinsic2, double face2) {
        if (face1 == 0 || face2 == 0) return 0.0;
        
        double ratio1 = intrinsic1 / face1;
        double ratio2 = intrinsic2 / face2;
        
        if (ratio1 > ratio2) {
            return Math.min(1.0, (ratio1 - ratio2) * 5.0);
        }
        return 0.0;
    }
}

