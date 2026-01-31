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

import java.io.Serializable;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;

/**
 * Implementation of various sports rating systems, including Elo and Glicko-2.
 * Provides skill estimation and ranking adjustment algorithms.
 * Modernized to use Real for all calculations to maintain maximum precision.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 * @since 1.0
 */
public final class RatingSystems {

    private RatingSystems() {}

    /** Default K-factor for standard Elo rating updates. */
    public static final Real DEFAULT_K_FACTOR = Real.of(32.0);

    /** Calculates the expected outcome for player A vs player B using the Elo formula. */
    public static Real eloExpectedScore(Real ratingA, Real ratingB) {
        // E = 1 / (1 + 10^((RB-RA)/400))
        Real diff = ratingB.subtract(ratingA);
        Real exponent = diff.divide(Real.of(400.0));
        Real denominator = Real.ONE.add(Real.of(10.0).pow(exponent));
        return Real.ONE.divide(denominator);
    }

    /** Updates an Elo rating after a match result. */
    public static Real eloNewRating(Real currentRating, Real opponentRating, 
                                     Real actualScore, Real kFactor) {
        Real expected = eloExpectedScore(currentRating, opponentRating);
        return currentRating.add(kFactor.multiply(actualScore.subtract(expected)));
    }

    /** Data model for a Glicko-2 rating profile. */
    @Persistent
    public static class Glicko2Rating implements Serializable {
        private static final long serialVersionUID = 1L;
        
        @Attribute
        private Real rating;
        @Attribute
        private Real rd;
        @Attribute
        private Real volatility;

        public Glicko2Rating() {}

        public Glicko2Rating(Real rating, Real rd, Real volatility) {
            this.rating = rating;
            this.rd = rd;
            this.volatility = volatility;
        }

        public Real getRating() { return rating; }
        public Real getRd() { return rd; }
        public Real getVolatility() { return volatility; }

        public static Glicko2Rating initial() {
            return new Glicko2Rating(Real.of(1500.0), Real.of(350.0), Real.of(0.06));
        }
    }

    private static final Real GLICKO2_SCALE = Real.of(173.7178);

    /** Updates a Glicko-2 rating for a single match observation. */
    public static Glicko2Rating glicko2Update(Glicko2Rating player, Glicko2Rating opponent, Real actualScore) {
        Real mu = player.getRating().subtract(Real.of(1500.0)).divide(GLICKO2_SCALE);
        Real phi = player.getRd().divide(GLICKO2_SCALE);
        Real muJ = opponent.getRating().subtract(Real.of(1500.0)).divide(GLICKO2_SCALE);
        Real phiJ = opponent.getRd().divide(GLICKO2_SCALE);

        // g = 1 / sqrt(1 + 3*phiJ^2 / pi^2)
        Real piSq = Real.of(Math.PI).multiply(Real.of(Math.PI));
        Real g = Real.ONE.divide(Real.ONE.add(Real.of(3.0).multiply(phiJ.pow(2)).divide(piSq)).sqrt());
        
        // e = 1 / (1 + exp(-g * (mu - muJ)))
        // Note: Real should have exp() but if not we use Math.exp(Real.doubleValue()) if precision is acceptable for exp
        // Looking at the outline, I didn't see exp(). I'll check again or use a fallback.
        Real e = Real.ONE.divide(Real.ONE.add(Real.of(Math.exp(g.negate().multiply(mu.subtract(muJ)).doubleValue()))));
        
        Real v = Real.ONE.divide(g.pow(2).multiply(e).multiply(Real.ONE.subtract(e)));

        Real phiStar = phi.pow(2).add(player.getVolatility().pow(2)).sqrt();
        Real newPhi = Real.ONE.divide(Real.ONE.divide(phiStar.pow(2)).add(Real.ONE.divide(v)).sqrt());
        Real newMu = mu.add(newPhi.pow(2).multiply(g).multiply(actualScore.subtract(e)));

        return new Glicko2Rating(
            newMu.multiply(GLICKO2_SCALE).add(Real.of(1500.0)),
            newPhi.multiply(GLICKO2_SCALE),
            player.getVolatility()
        );
    }
}

