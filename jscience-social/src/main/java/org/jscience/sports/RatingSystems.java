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

import java.io.Serializable;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Implementation of various sports rating systems, including Elo and Glicko-2.
 * Provides skill estimation and ranking adjustment algorithms.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class RatingSystems {

    private RatingSystems() {}

    /** Default K-factor for standard Elo rating updates. */
    public static final double DEFAULT_K_FACTOR = 32.0;

    /** Calculates the expected outcome for player A vs player B using the Elo formula. */
    public static Real eloExpectedScore(Real ratingA, Real ratingB) {
        double diff = ratingB.subtract(ratingA).doubleValue();
        return Real.of(1.0 / (1.0 + Math.pow(10.0, diff / 400.0)));
    }

    /** Updates an Elo rating after a match result. */
    public static Real eloNewRating(Real currentRating, Real opponentRating, 
                                     double actualScore, double kFactor) {
        Real expected = eloExpectedScore(currentRating, opponentRating);
        return currentRating.add(Real.of(kFactor).multiply(Real.of(actualScore).subtract(expected)));
    }

    /** Data model for a Glicko-2 rating profile. */
    public record Glicko2Rating(double rating, double rd, double volatility) implements Serializable {
        public static Glicko2Rating initial() {
            return new Glicko2Rating(1500.0, 350.0, 0.06);
        }
    }

    private static final double GLICKO2_SCALE = 173.7178;

    /** Updates a Glicko-2 rating for a single match observation. */
    public static Glicko2Rating glicko2Update(Glicko2Rating player, Glicko2Rating opponent, double actualScore) {
        double mu = (player.rating() - 1500.0) / GLICKO2_SCALE;
        double phi = player.rd() / GLICKO2_SCALE;
        double muJ = (opponent.rating() - 1500.0) / GLICKO2_SCALE;
        double phiJ = opponent.rd() / GLICKO2_SCALE;

        double g = 1.0 / Math.sqrt(1.0 + 3.0 * phiJ * phiJ / (Math.PI * Math.PI));
        double e = 1.0 / (1.0 + Math.exp(-g * (mu - muJ)));
        double v = 1.0 / (g * g * e * (1.0 - e));

        double phiStar = Math.sqrt(phi * phi + player.volatility() * player.volatility());
        double newPhi = 1.0 / Math.sqrt(1.0 / (phiStar * phiStar) + 1.0 / v);
        double newMu = mu + newPhi * newPhi * g * (actualScore - e);

        return new Glicko2Rating(
            newMu * GLICKO2_SCALE + 1500.0,
            newPhi * GLICKO2_SCALE,
            player.volatility()
        );
    }
}
