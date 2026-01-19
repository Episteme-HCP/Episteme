package org.jscience.sports;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Elo and Glicko-2 rating system implementations.
 */
public final class RatingSystems {

    private RatingSystems() {}

    // --- ELO RATING ---

    /** Standard K-factor for Elo calculations. */
    public static final double DEFAULT_K_FACTOR = 32.0;

    /**
     * Calculates the expected score for player A against player B.
     * E_A = 1 / (1 + 10^((R_B - R_A) / 400))
     */
    public static Real eloExpectedScore(Real ratingA, Real ratingB) {
        double diff = ratingB.subtract(ratingA).doubleValue();
        double expected = 1.0 / (1.0 + Math.pow(10.0, diff / 400.0));
        return Real.of(expected);
    }

    /**
     * Calculates the new Elo rating after a match.
     * 
     * @param currentRating Current rating of the player.
     * @param opponentRating Rating of the opponent.
     * @param actualScore 1.0 for win, 0.5 for draw, 0.0 for loss.
     * @param kFactor The K-factor (sensitivity).
     * @return New rating.
     */
    public static Real eloNewRating(Real currentRating, Real opponentRating, 
                                     double actualScore, double kFactor) {
        Real expected = eloExpectedScore(currentRating, opponentRating);
        Real change = Real.of(kFactor).multiply(Real.of(actualScore).subtract(expected));
        return currentRating.add(change);
    }

    /**
     * Convenience method with default K-factor.
     */
    public static Real eloNewRating(Real currentRating, Real opponentRating, double actualScore) {
        return eloNewRating(currentRating, opponentRating, actualScore, DEFAULT_K_FACTOR);
    }

    // --- GLICKO-2 RATING ---

    /** Glicko-2 system constant (τ). */
    public static final double GLICKO2_TAU = 0.5;
    private static final double GLICKO2_SCALE = 173.7178;

    /**
     * Represents a Glicko-2 rating with rating, deviation, and volatility.
     */
    public record Glicko2Rating(double rating, double rd, double volatility) {
        public static Glicko2Rating initial() {
            return new Glicko2Rating(1500.0, 350.0, 0.06);
        }
    }

    /**
     * Converts Glicko rating to Glicko-2 scale.
     */
    public static double toGlicko2Scale(double rating) {
        return (rating - 1500.0) / GLICKO2_SCALE;
    }

    /**
     * Converts Glicko-2 scale back to Glicko rating.
     */
    public static double fromGlicko2Scale(double mu) {
        return mu * GLICKO2_SCALE + 1500.0;
    }

    /**
     * Calculates g(φ) = 1 / sqrt(1 + 3φ²/π²)
     */
    public static double glicko2G(double phi) {
        return 1.0 / Math.sqrt(1.0 + 3.0 * phi * phi / (Math.PI * Math.PI));
    }

    /**
     * Calculates E(μ, μj, φj) = 1 / (1 + exp(-g(φj)(μ - μj)))
     */
    public static double glicko2E(double mu, double muJ, double phiJ) {
        double g = glicko2G(phiJ);
        return 1.0 / (1.0 + Math.exp(-g * (mu - muJ)));
    }

    /**
     * Updates a Glicko-2 rating after a match.
     * Simplified single-opponent version.
     */
    public static Glicko2Rating glicko2Update(Glicko2Rating player, Glicko2Rating opponent, double actualScore) {
        double mu = toGlicko2Scale(player.rating());
        double phi = player.rd() / GLICKO2_SCALE;
        double muJ = toGlicko2Scale(opponent.rating());
        double phiJ = opponent.rd() / GLICKO2_SCALE;

        double g = glicko2G(phiJ);
        double e = glicko2E(mu, muJ, phiJ);
        double v = 1.0 / (g * g * e * (1 - e));

        
        // Simplified volatility update (iterative method skipped for brevity)
        double newSigma = player.volatility();
        
        double phiStar = Math.sqrt(phi * phi + newSigma * newSigma);
        double newPhi = 1.0 / Math.sqrt(1.0 / (phiStar * phiStar) + 1.0 / v);
        double newMu = mu + newPhi * newPhi * g * (actualScore - e);

        return new Glicko2Rating(
            fromGlicko2Scale(newMu),
            newPhi * GLICKO2_SCALE,
            newSigma
        );
    }
}
