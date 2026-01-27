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

package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Portfolio optimization using Modern Portfolio Theory (Markowitz).
 */
public final class PortfolioOptimizer {

    private PortfolioOptimizer() {}

    public record Asset(
        String symbol,
        String name,
        Real expectedReturn,  // Annualized
        Real volatility       // Annualized std dev
    ) {}

    public record Portfolio(
        Map<String, Real> weights,
        Real expectedReturn,
        Real volatility,
        Real sharpeRatio
    ) {}

    public record EfficientFrontierPoint(
        Real targetReturn,
        Real minimumVolatility,
        Map<String, Real> weights
    ) {}

    /**
     * Calculates portfolio return.
     */
    public static Real portfolioReturn(List<Asset> assets, Real[] weights) {
        Real ret = Real.ZERO;
        for (int i = 0; i < assets.size(); i++) {
            ret = ret.add(assets.get(i).expectedReturn().multiply(weights[i]));
        }
        return ret;
    }

    /**
     * Calculates portfolio volatility.
     */
    public static Real portfolioVolatility(List<Asset> assets, Real[] weights, 
            Real[][] correlationMatrix) {
        
        int n = assets.size();
        Real variance = Real.ZERO;
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // var = sum(wi * wj * si * sj * rij)
                variance = variance.add(weights[i].multiply(weights[j])
                           .multiply(assets.get(i).volatility())
                           .multiply(assets.get(j).volatility())
                           .multiply(correlationMatrix[i][j]));
            }
        }
        
        return variance.sqrt();
    }

    /**
     * Calculates Sharpe Ratio.
     */
    public static Real sharpeRatio(Real portfolioReturn, Real volatility, Real riskFreeRate) {
        if (volatility.isZero() || volatility.isNegative()) return Real.ZERO;
        return portfolioReturn.subtract(riskFreeRate).divide(volatility);
    }

    /**
     * Optimizes portfolio for maximum Sharpe ratio.
     */
    public static Portfolio optimizeMaxSharpe(List<Asset> assets, Real[][] correlations,
            Real riskFreeRate) {
        
        int n = assets.size();
        Real[] bestWeights = new Real[n];
        Real bestSharpe = Real.NEGATIVE_INFINITY;
        
        // Simple grid search
        int steps = 10;
        Real[] weights = new Real[n];
        
        searchRecursive(assets, correlations, riskFreeRate, weights, 0, Real.ONE, 
            bestWeights, new Real[]{bestSharpe}, steps);
        
        Real ret = portfolioReturn(assets, bestWeights);
        Real vol = portfolioVolatility(assets, bestWeights, correlations);
        Real sharpe = sharpeRatio(ret, vol, riskFreeRate);
        
        Map<String, Real> weightMap = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            weightMap.put(assets.get(i).symbol(), bestWeights[i]);
        }
        
        return new Portfolio(weightMap, ret, vol, sharpe);
    }

    /**
     * Optimizes portfolio for minimum volatility given target return.
     */
    public static Portfolio optimizeMinVolatility(List<Asset> assets, Real[][] correlations,
            Real targetReturn) {
        
        int n = assets.size();
        Real[] bestWeights = new Real[n];
        Real bestVol = Real.POSITIVE_INFINITY;
        
        Real[] weights = new Real[n];
        
        // Grid search for minimum volatility at target return
        searchMinVol(assets, correlations, targetReturn, weights, 0, Real.ONE, 
            bestWeights, new Real[]{bestVol});
        
        Real ret = portfolioReturn(assets, bestWeights);
        Real vol = portfolioVolatility(assets, bestWeights, correlations);
        
        Map<String, Real> weightMap = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            weightMap.put(assets.get(i).symbol(), bestWeights[i]);
        }
        
        return new Portfolio(weightMap, ret, vol, sharpeRatio(ret, vol, Real.of(0.02)));
    }

    /**
     * Generates efficient frontier points.
     */
    public static List<EfficientFrontierPoint> generateEfficientFrontier(
            List<Asset> assets, Real[][] correlations, int numPoints) {
        
        List<EfficientFrontierPoint> frontier = new ArrayList<>();
        
        // Find return range
        Real minReturn = Real.of(assets.stream().mapToDouble(a -> a.expectedReturn().doubleValue()).min().orElse(0));
        Real maxReturn = Real.of(assets.stream().mapToDouble(a -> a.expectedReturn().doubleValue()).max().orElse(0));
        
        Real step = maxReturn.subtract(minReturn).divide(Real.of(numPoints - 1));
        
        for (int i = 0; i < numPoints; i++) {
            Real targetReturn = minReturn.add(Real.of(i).multiply(step));
            Portfolio optimal = optimizeMinVolatility(assets, correlations, targetReturn);
            
            frontier.add(new EfficientFrontierPoint(
                targetReturn,
                optimal.volatility(),
                optimal.weights()
            ));
        }
        
        return frontier;
    }

    /**
     * Creates correlation matrix (simplified: assumes given correlations).
     */
    public static Real[][] identityCorrelation(int n) {
        Real[][] corr = new Real[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                corr[i][j] = (i == j) ? Real.ONE : Real.of(0.3); // Default correlation
            }
        }
        return corr;
    }

    private static void searchRecursive(List<Asset> assets, Real[][] correlations,
            Real riskFreeRate, Real[] weights, int index, Real remaining,
            Real[] bestWeights, Real[] bestSharpe, int steps) {
        
        int n = assets.size();
        
        if (index == n - 1) {
            weights[index] = remaining;
            Real ret = portfolioReturn(assets, weights);
            Real vol = portfolioVolatility(assets, weights, correlations);
            Real sharpe = sharpeRatio(ret, vol, riskFreeRate);
            
            if (sharpe.compareTo(bestSharpe[0]) > 0) {
                bestSharpe[0] = sharpe;
                System.arraycopy(weights, 0, bestWeights, 0, n);
            }
            return;
        }
        
        for (int s = 0; s <= steps; s++) {
            weights[index] = remaining.multiply(Real.of(s)).divide(Real.of(steps));
            searchRecursive(assets, correlations, riskFreeRate, weights, index + 1,
                remaining.subtract(weights[index]), bestWeights, bestSharpe, steps);
        }
    }

    private static void searchMinVol(List<Asset> assets, Real[][] correlations,
            Real targetReturn, Real[] weights, int index, Real remaining,
            Real[] bestWeights, Real[] bestVol) {
        
        int n = assets.size();
        
        if (index == n - 1) {
            weights[index] = remaining;
            Real ret = portfolioReturn(assets, weights);
            
            // Only consider if close to target return
            if (ret.subtract(targetReturn).abs().compareTo(Real.of(0.01)) < 0) {
                Real vol = portfolioVolatility(assets, weights, correlations);
                if (vol.compareTo(bestVol[0]) < 0) {
                    bestVol[0] = vol;
                    System.arraycopy(weights, 0, bestWeights, 0, n);
                }
            }
            return;
        }
        
        for (int s = 0; s <= 10; s++) {
            weights[index] = remaining.multiply(Real.of(s)).divide(Real.of(10));
            searchMinVol(assets, correlations, targetReturn, weights, index + 1,
                remaining.subtract(weights[index]), bestWeights, bestVol);
        }
    }
}
