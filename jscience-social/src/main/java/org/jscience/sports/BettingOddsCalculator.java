package org.jscience.sports;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Calculates betting odds based on rating systems and contextual factors.
 */
public final class BettingOddsCalculator {

    private BettingOddsCalculator() {}

    public record MatchContext(
        boolean isHomeGame,
        double restDaysDifference,  // Positive = team A more rested
        double injuryImpact,        // 0-1, 0 = no injuries
        double formDifference,      // Recent form -1 to 1
        double headToHeadAdvantage  // Historical H2H -1 to 1
    ) {}

    public record OddsResult(
        double winProbabilityA,
        double drawProbability,
        double winProbabilityB,
        double decimalOddsA,
        double decimalOddsB,
        double decimalOddsDraw
    ) {}

    /**
     * Calculates match probabilities from Elo ratings.
     */
    public static OddsResult calculateFromElo(Real ratingA, Real ratingB, 
            MatchContext context, double drawMargin) {
        
        // Base expected score from Elo
        double expectedA = RatingSystems.eloExpectedScore(ratingA, ratingB).doubleValue();
        
        // Apply contextual adjustments
        if (context != null) {
            if (context.isHomeGame()) {
                expectedA += 0.05; // Home advantage
            }
            expectedA += context.restDaysDifference() * 0.01;
            expectedA -= context.injuryImpact() * 0.1;
            expectedA += context.formDifference() * 0.05;
            expectedA += context.headToHeadAdvantage() * 0.03;
        }
        
        // Clamp to valid range
        expectedA = Math.max(0.05, Math.min(0.95, expectedA));
        double expectedB = 1 - expectedA;
        
        // Calculate draw probability (depends on sport)
        double drawProb = Math.max(0, drawMargin * (1 - Math.abs(expectedA - 0.5) * 2));
        
        // Adjust win probabilities for draw
        double adjustedA = expectedA * (1 - drawProb);
        double adjustedB = expectedB * (1 - drawProb);
        
        // Convert to decimal odds (including margin for bookmaker)
        double margin = 1.05; // 5% bookmaker margin
        double oddsA = margin / adjustedA;
        double oddsB = margin / adjustedB;
        double oddsDraw = drawProb > 0 ? margin / drawProb : 100.0;
        
        return new OddsResult(adjustedA, drawProb, adjustedB, oddsA, oddsDraw, oddsB);
    }

    /**
     * Converts between odds formats.
     */
    public static double decimalToFractional(double decimalOdds) {
        return decimalOdds - 1;
    }

    public static int decimalToAmericanPositive(double decimalOdds) {
        return (int) Math.round((decimalOdds - 1) * 100);
    }

    public static int decimalToAmericanNegative(double decimalOdds) {
        return (int) Math.round(-100 / (decimalOdds - 1));
    }

    public static String decimalToAmerican(double decimalOdds) {
        if (decimalOdds >= 2.0) {
            return "+" + decimalToAmericanPositive(decimalOdds);
        } else {
            return String.valueOf(decimalToAmericanNegative(decimalOdds));
        }
    }

    public static double impliedProbability(double decimalOdds) {
        return 1.0 / decimalOdds;
    }

    /**
     * Calculates expected value of a bet.
     */
    public static Real expectedValue(double stake, double decimalOdds, double trueProbability) {
        double winnings = stake * (decimalOdds - 1);
        double ev = (trueProbability * winnings) - ((1 - trueProbability) * stake);
        return Real.of(ev);
    }

    /**
     * Kelly Criterion for optimal bet sizing.
     */
    public static Real kellyBetFraction(double trueProbability, double decimalOdds) {
        double b = decimalOdds - 1; // Net odds (profit per unit wagered)
        double p = trueProbability;
        double q = 1 - p;
        
        double kelly = (b * p - q) / b;
        return Real.of(Math.max(0, kelly)); // Never bet negative
    }

    /**
     * Calculates arbitrage opportunity if any.
     */
    public static Optional<Real> findArbitrage(double oddsA_bookie1, double oddsB_bookie2) {
        double impliedTotal = (1.0 / oddsA_bookie1) + (1.0 / oddsB_bookie2);
        
        if (impliedTotal < 1.0) {
            double profit = (1.0 - impliedTotal) * 100;
            return Optional.of(Real.of(profit)); // Percentage profit
        }
        
        return Optional.empty();
    }

    /**
     * Calculates parlay/accumulator odds.
     */
    public static double parlayOdds(List<Double> individualOdds) {
        return individualOdds.stream()
            .reduce(1.0, (a, b) -> a * b);
    }
}
