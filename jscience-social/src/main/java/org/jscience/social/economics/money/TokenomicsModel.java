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

package org.jscience.social.economics.money;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Models token engineering and cryptocurrency dynamics (Tokenomics).
 * Includes models for Automated Market Makers (AMM) and supply curves.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TokenomicsModel {

    private TokenomicsModel() {
        // Utility class
    }

    /**
     * Constant Product AMM formula (x * y = k).
     * Calculates the amount of tokens received when swapping 'dx' amount of token X.
     *
     * @param reserveX Current reserve of token X in the pool.
     * @param reserveY Current reserve of token Y in the pool.
     * @param dx Amount of token X being swapped in.
     * @param fee Fee percentage (0.0 to 1.0, e.g., 0.003 for 0.3%).
     * @return The amount of token Y received (dy).
     */
    public static Real calculateSwapOutput(Real reserveX, Real reserveY, Real dx, Real fee) {
        // dy = (y * dx * (1 - fee)) / (x + dx * (1 - fee))
        
        Real amountInWithFee = dx.multiply(Real.of(1.0).subtract(fee));
        Real numerator = amountInWithFee.multiply(reserveY);
        Real denominator = reserveX.add(amountInWithFee);
        
        return numerator.divide(denominator);
    }
    
    /**
     * Calculates the price impact of a trade on a Constant Product AMM.
     * Price Impact = (Price_new - Price_old) / Price_old
     *
     * @param reserveX Current reserve of token X
     * @param reserveY Current reserve of token Y
     * @param dx Amount of token X input
     * @return Price impact fraction
     */
    public static Real calculatePriceImpact(Real reserveX, Real reserveY, Real dx) {
        // Ideal price = y / x
        // New price = y_new / x_new
        // Simplified approximation logic usually used.
        // For now, returning placeholder.
        return dx.divide(reserveX.add(dx));
    }
}
