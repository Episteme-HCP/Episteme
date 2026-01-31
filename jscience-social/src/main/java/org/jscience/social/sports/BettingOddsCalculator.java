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
import java.util.List;
import java.util.Optional;
import org.jscience.core.mathematics.numbers.real.Real;

/**
 * A utility for calculating betting odds, probabilities, and expected values for sports matches.
 * Uses underlying rating systems (like Elo) and applies contextual factors.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class BettingOddsCalculator {

    private BettingOddsCalculator() {}

    /**
     * Contextual factors that influence the outcome of a match.
     * @param isHomeGame         True if Team A is playing at home
     * @param restDaysDifference Days rested (A - B). Positive favors A.
     * @param injuryImpact       0.0 to 1.0 representing squad depletion (for A)
     * @param formDifference     -1.0 to 1.0 representing recent form (A - B)
     * @param headToHeadAdvantage -1.0 to 1.0 historical advantage
     */
    public record MatchContext(
        boolean isHomeGame,
        double restDaysDifference,
        double injuryImpact,
        double formDifference,
        double headToHeadAdvantage
    ) implements Serializable {}

    /**
     * The result of an odds calculation, including probabilities and decimal odds.
     */
    public record OddsResult(
        double winProbabilityA,
        double drawProbability,
        double winProbabilityB,
        double decimalOddsA,
        double decimalOddsB,
        double decimalOddsDraw
    ) implements Serializable {}

    /**
     * Calculates match probabilities and odds based on Elo ratings and context.
     * 
     * @param ratingA    Elo rating of Team A
     * @param ratingB    Elo rating of Team B
     * @param context    Contextual factors
     * @param drawMargin The "width" of the draw band in probability space (sport specific)
     * @return OddsResult containing implied probabilities and bookmaker-style odds
     */
    public static OddsResult calculateFromElo(Real ratingA, Real ratingB, 
            MatchContext context, double drawMargin) {
        
        // Base expected score from Elo logic: 1 / (1 + 10^((Rb-Ra)/400))
        double expectedA = RatingSystems.eloExpectedScore(ratingA, ratingB).doubleValue();
        
        // Apply contextual adjustments
        if (context != null) {
            if (context.isHomeGame()) {
                expectedA += 0.05; // Standard home advantage approximation
            }
            expectedA += context.restDaysDifference() * 0.01;
            expectedA -= context.injuryImpact() * 0.1;
            expectedA += context.formDifference() * 0.05;
            expectedA += context.headToHeadAdvantage() * 0.03;
        }
        
        // Clamp probability to realistic bounds [5% - 95%]
        expectedA = Math.max(0.05, Math.min(0.95, expectedA));
        double expectedB = 1.0 - expectedA;
        
        // Calculate draw probability
        // Draw is most likely when teams are evenly matched (expectedA ~ 0.5)
        // Probability decays as mismatch increases.
        double drawProb = Math.max(0, drawMargin * (1.0 - Math.abs(expectedA - 0.5) * 2.0));
        
        // Renormalize Win/Loss probs to account for Draw slice
        double adjustedA = expectedA * (1.0 - drawProb);
        double adjustedB = expectedB * (1.0 - drawProb);
        
        // Convert to decimal odds with margin
        double bookmakerMargin = 1.05; // 5% overround
        double oddsA = bookmakerMargin / adjustedA;
        double oddsB = bookmakerMargin / adjustedB;
        double oddsDraw = (drawProb > 0.001) ? bookmakerMargin / drawProb : 0.0;
        
        return new OddsResult(adjustedA, drawProb, adjustedB, oddsA, oddsB, oddsDraw);
    }

    /**
     * Converts Decimal odds to Fractional odds.
     * e.g., 2.50 -> 1.5/1 (returned as 1.5)
     * @param decimalOdds Decimal odds
     * @return Fractional numerator (denominator assumed 1)
     */
    public static double decimalToFractional(double decimalOdds) {
        return decimalOdds - 1.0;
    }

    /**
     * Converts Decimal odds to US Moneyline (Positive).
     * Used for underdogs (odds >= 2.00). Returns amount won on 100 stake.
     * @param decimalOdds Decimal odds (> 2.0)
     * @return Moneyline value
     */
    public static int decimalToAmericanPositive(double decimalOdds) {
        return (int) Math.round((decimalOdds - 1.0) * 100);
    }

    /**
     * Converts Decimal odds to US Moneyline (Negative).
     * Used for favorites (odds < 2.00). Returns stake needed to win 100.
     * @param decimalOdds Decimal odds (< 2.0)
     * @return Moneyline value (negative integer)
     */
    public static int decimalToAmericanNegative(double decimalOdds) {
        if (decimalOdds <= 1.0) return 0;
        return (int) Math.round(-100 / (decimalOdds - 1.0));
    }

    /**
     * Converts Decimal odds to a formatted American format string.
     * @param decimalOdds Decimal odds
     * @return String like "+150" or "-200"
     */
    public static String decimalToAmerican(double decimalOdds) {
        if (decimalOdds >= 2.0) {
            return "+" + decimalToAmericanPositive(decimalOdds);
        } else {
            return String.valueOf(decimalToAmericanNegative(decimalOdds));
        }
    }

    /**
     * Calculates Implied Probability from Decimal odds.
     * P = 1 / Odds
     * @param decimalOdds Decimal odds
     * @return Probability (0-1)
     */
    public static double impliedProbability(double decimalOdds) {
        if (decimalOdds <= 0) return 0.0;
        return 1.0 / decimalOdds;
    }

    /**
     * Calculates the Expected Value (EV) of a specific bet.
     * EV = (Prob * Profit) - (LossProb * Stake)
     * 
     * @param stake           Amount wagered
     * @param decimalOdds     Odds offered
     * @param trueProbability The 'actual' probability of winning
     * @return Expected Value
     */
    public static Real expectedValue(double stake, double decimalOdds, double trueProbability) {
        double potentialWinnings = stake * (decimalOdds - 1.0);
        double ev = (trueProbability * potentialWinnings) - ((1.0 - trueProbability) * stake);
        return Real.of(ev);
    }

    /**
     * Calculates the Kelly Criterion fraction for optimal bet sizing.
     * f* = (bp - q) / b
     * where b = net odds (odds - 1), p = prob of winning, q = prob of losing.
     * 
     * @param trueProbability Estimated probability of winning
     * @param decimalOdds     Odds offered
     * @return Fraction of bankroll to bet (0.0 if EV is negative)
     */
    public static Real kellyBetFraction(double trueProbability, double decimalOdds) {
        double b = decimalOdds - 1.0; // Net odds
        if (b <= 0) return Real.ZERO;
        
        double p = trueProbability;
        double q = 1.0 - p;
        
        double kelly = (b * p - q) / b;
        return Real.of(Math.max(0.0, kelly));
    }

    /**
     * Checks for an arbitrage opportunity between two bookmakers (Surebet).
     * Occurs if the sum of implied probabilities is less than 1.
     * 
     * @param oddsA_bookie1 Odds for Outcome A at Bookie 1
     * @param oddsB_bookie2 Odds for Outcome B at Bookie 2
     * @return Optional containing the percentage profit (ROI) if arbitrage exists
     */
    public static Optional<Real> findArbitrage(double oddsA_bookie1, double oddsB_bookie2) {
        double impliedTotal = (1.0 / oddsA_bookie1) + (1.0 / oddsB_bookie2);
        
        if (impliedTotal < 1.0) {
            double profitPercent = (1.0 - impliedTotal) / impliedTotal * 100;
            return Optional.of(Real.of(profitPercent));
        }
        
        return Optional.empty();
    }

    /**
     * Calculates combined odds for a Parlay (Accumulator) bet.
     * Multiplies distinct event odds together.
     * 
     * @param individualOdds List of decimal odds for each selection
     * @return Combined decimal odds
     */
    public static double parlayOdds(List<Double> individualOdds) {
        return individualOdds.stream()
            .reduce(1.0, (a, b) -> a * b);
    }
}

