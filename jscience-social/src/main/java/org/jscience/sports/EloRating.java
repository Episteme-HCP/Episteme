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

package org.jscience.sports;

/**
 * Implementation of the Elo rating system for measuring relative skill levels 
 * in zero-sum games and sports competitions.
 * 
 * Reference: Elo, Arpad (1978). The Rating of Chessplayers, Past and Present.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class EloRating {

    private EloRating() {}

    /**
     * Calculates the expected score (probability of winning) for player A.
     * 
     * @param ratingA skill rating of player A
     * @param ratingB skill rating of player B
     * @return expected score between 0.0 and 1.0
     */
    public static double calculateExpectedScore(double ratingA, double ratingB) {
        return 1.0 / (1.0 + Math.pow(10, (ratingB - ratingA) / 400.0));
    }

    /**
     * Calculates the newly adjusted rating for a player after a match.
     * 
     * @param currentRating the previous rating
     * @param actualScore   the outcome (1.0 = win, 0.5 = draw, 0.0 = loss)
     * @param expectedScore the pre-match probability calculated via {@link #calculateExpectedScore}
     * @param kFactor       the sensitivity factor (e.g., 32 for novices, 16 for masters)
     * @return the updated Elo rating
     */
    public static double updateRating(double currentRating, double actualScore, 
            double expectedScore, double kFactor) {
        return currentRating + kFactor * (actualScore - expectedScore);
    }
}
