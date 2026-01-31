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

package org.jscience.social.sports;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Predicts the statistical outcome and probability of winning for sports events.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class MatchPredictor {

    private MatchPredictor() {}

    /**
     * Calculates the probability of a home team win using an exponential logistic regression model.
     * 
     * @param homeRating    skill rating for the home team
     * @param awayRating    skill rating for the visiting team
     * @param homeAdvantage extra weight to account for home field advantage
     * @return probability of win (0.0 to 1.0)
     */
    public static Real winProbability(double homeRating, double awayRating, double homeAdvantage) {
        double diff = (homeRating + homeAdvantage) - awayRating;
        // Logistic sigmoid function
        double prob = 1.0 / (1.0 + Math.exp(-diff / 100.0));
        return Real.of(prob);
    }
}

