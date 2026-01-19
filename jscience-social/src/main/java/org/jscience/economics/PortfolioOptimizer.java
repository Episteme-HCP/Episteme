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
        double expectedReturn,  // Annual %
        double volatility       // Standard deviation
    ) {}

    public record Portfolio(
        Map<String, Double> weights,
        double expectedReturn,
        double volatility,
        double sharpeRatio
    ) {}

    public record EfficientFrontierPoint(
        double targetReturn,
        double minimumVolatility,
        Map<String, Double> weights
    ) {}

    /**
     * Calculates portfolio return.
     */
    public static Real portfolioReturn(List<Asset> assets, double[] weights) {
        double ret = 0;
        for (int i = 0; i < assets.size(); i++) {
            ret += assets.get(i).expectedReturn() * weights[i];
        }
        return Real.of(ret);
    }

    /**
     * Calculates portfolio volatility.
     */
    public static Real portfolioVolatility(List<Asset> assets, double[] weights, 
            double[][] correlationMatrix) {
        
        int n = assets.size();
        double variance = 0;
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                variance += weights[i] * weights[j] * 
                           assets.get(i).volatility() * assets.get(j).volatility() *
                           correlationMatrix[i][j];
            }
        }
        
        return Real.of(Math.sqrt(variance));
    }

    /**
     * Calculates Sharpe Ratio.
     */
    public static Real sharpeRatio(double portfolioReturn, double volatility, double riskFreeRate) {
        if (volatility <= 0) return Real.ZERO;
        return Real.of((portfolioReturn - riskFreeRate) / volatility);
    }

    /**
     * Optimizes portfolio for maximum Sharpe ratio.
     */
    public static Portfolio optimizeMaxSharpe(List<Asset> assets, double[][] correlations,
            double riskFreeRate) {
        
        int n = assets.size();
        double[] bestWeights = new double[n];
        double bestSharpe = Double.NEGATIVE_INFINITY;
        
        // Simple grid search (production would use quadratic programming)
        int steps = 10;
        double[] weights = new double[n];
        
        searchRecursive(assets, correlations, riskFreeRate, weights, 0, 1.0, 
            bestWeights, new double[]{bestSharpe}, steps);
        
        double ret = portfolioReturn(assets, bestWeights).doubleValue();
        double vol = portfolioVolatility(assets, bestWeights, correlations).doubleValue();
        double sharpe = sharpeRatio(ret, vol, riskFreeRate).doubleValue();
        
        Map<String, Double> weightMap = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            weightMap.put(assets.get(i).symbol(), bestWeights[i]);
        }
        
        return new Portfolio(weightMap, ret, vol, sharpe);
    }

    /**
     * Optimizes portfolio for minimum volatility given target return.
     */
    public static Portfolio optimizeMinVolatility(List<Asset> assets, double[][] correlations,
            double targetReturn) {
        
        int n = assets.size();
        double[] bestWeights = new double[n];
        double bestVol = Double.MAX_VALUE;
        
        double[] weights = new double[n];
        
        // Grid search for minimum volatility at target return
        searchMinVol(assets, correlations, targetReturn, weights, 0, 1.0, 
            bestWeights, new double[]{bestVol});
        
        double ret = portfolioReturn(assets, bestWeights).doubleValue();
        double vol = portfolioVolatility(assets, bestWeights, correlations).doubleValue();
        
        Map<String, Double> weightMap = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            weightMap.put(assets.get(i).symbol(), bestWeights[i]);
        }
        
        return new Portfolio(weightMap, ret, vol, sharpeRatio(ret, vol, 0.02).doubleValue());
    }

    /**
     * Generates efficient frontier points.
     */
    public static List<EfficientFrontierPoint> generateEfficientFrontier(
            List<Asset> assets, double[][] correlations, int numPoints) {
        
        List<EfficientFrontierPoint> frontier = new ArrayList<>();
        
        // Find return range
        double minReturn = assets.stream().mapToDouble(Asset::expectedReturn).min().orElse(0);
        double maxReturn = assets.stream().mapToDouble(Asset::expectedReturn).max().orElse(0);
        
        double step = (maxReturn - minReturn) / (numPoints - 1);
        
        for (int i = 0; i < numPoints; i++) {
            double targetReturn = minReturn + i * step;
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
    public static double[][] identityCorrelation(int n) {
        double[][] corr = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                corr[i][j] = (i == j) ? 1.0 : 0.3; // Default correlation
            }
        }
        return corr;
    }

    private static void searchRecursive(List<Asset> assets, double[][] correlations,
            double riskFreeRate, double[] weights, int index, double remaining,
            double[] bestWeights, double[] bestSharpe, int steps) {
        
        int n = assets.size();
        
        if (index == n - 1) {
            weights[index] = remaining;
            double ret = portfolioReturn(assets, weights).doubleValue();
            double vol = portfolioVolatility(assets, weights, correlations).doubleValue();
            double sharpe = sharpeRatio(ret, vol, riskFreeRate).doubleValue();
            
            if (sharpe > bestSharpe[0]) {
                bestSharpe[0] = sharpe;
                System.arraycopy(weights, 0, bestWeights, 0, n);
            }
            return;
        }
        
        for (int s = 0; s <= steps; s++) {
            weights[index] = remaining * s / steps;
            searchRecursive(assets, correlations, riskFreeRate, weights, index + 1,
                remaining - weights[index], bestWeights, bestSharpe, steps);
        }
    }

    private static void searchMinVol(List<Asset> assets, double[][] correlations,
            double targetReturn, double[] weights, int index, double remaining,
            double[] bestWeights, double[] bestVol) {
        
        int n = assets.size();
        
        if (index == n - 1) {
            weights[index] = remaining;
            double ret = portfolioReturn(assets, weights).doubleValue();
            
            // Only consider if close to target return
            if (Math.abs(ret - targetReturn) < 0.01) {
                double vol = portfolioVolatility(assets, weights, correlations).doubleValue();
                if (vol < bestVol[0]) {
                    bestVol[0] = vol;
                    System.arraycopy(weights, 0, bestWeights, 0, n);
                }
            }
            return;
        }
        
        for (int s = 0; s <= 10; s++) {
            weights[index] = remaining * s / 10;
            searchMinVol(assets, correlations, targetReturn, weights, index + 1,
                remaining - weights[index], bestWeights, bestVol);
        }
    }
}
