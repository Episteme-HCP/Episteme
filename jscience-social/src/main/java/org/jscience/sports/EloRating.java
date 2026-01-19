package org.jscience.sports;

/**
 * Implementation of the Elo rating system and its variants.
 * 
 * Reference: Elo, Arpad (1978). The Rating of Chessplayers, Past and Present.
 */
public final class EloRating {

    private EloRating() {}

    /**
     * Calculates the expected score of player A against player B.
     * 
     * @param ratingA Rating of player A.
     * @param ratingB Rating of player B.
     * @return Expected score (0.0 to 1.0).
     */
    public static double calculateExpectedScore(double ratingA, double ratingB) {
        return 1.0 / (1.0 + Math.pow(10, (ratingB - ratingA) / 400.0));
    }

    /**
     * Calculates the new rating for a player.
     * 
     * @param currentRating Current Elo rating.
     * @param actualScore Actual result (1.0 for win, 0.5 for draw, 0.0 for loss).
     * @param expectedScore Expected score calculated via calculateExpectedScore.
     * @param kFactor The K-factor (determines sensitivity to recent results).
     * @return The updated rating.
     */
    public static double updateRating(double currentRating, double actualScore, double expectedScore, double kFactor) {
        return currentRating + kFactor * (actualScore - expectedScore);
    }
}
