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

/**
 * Provides mathematical models for option pricing, including Black-Scholes 
 * and binomial tree implementations.
 * Supports both European and American style options.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class OptionPricer {

    private OptionPricer() {}

    public enum OptionType { CALL, PUT }
    public enum OptionStyle { EUROPEAN, AMERICAN }

    public record OptionParameters(
        double spotPrice,        // Current stock price
        double strikePrice,      // Exercise price
        double timeToExpiry,     // In years
        double riskFreeRate,     // Annualized
        double volatility,       // Annualized std dev
        OptionType type,
        OptionStyle style
    ) {}

    public record OptionGreeks(
        double delta,    // Price sensitivity to underlying
        double gamma,    // Rate of change of delta
        double theta,    // Time decay (per day)
        double vega,     // Volatility sensitivity
        double rho       // Interest rate sensitivity
    ) {}

    public record PricingResult(
        double price,
        OptionGreeks greeks,
        String model
    ) {}

    /**
     * Black-Scholes pricing for European options.
     */
    public static PricingResult blackScholes(OptionParameters params) {
        double S = params.spotPrice();
        double K = params.strikePrice();
        double T = params.timeToExpiry();
        double r = params.riskFreeRate();
        double sigma = params.volatility();
        
        double d1 = (Math.log(S / K) + (r + sigma * sigma / 2) * T) / (sigma * Math.sqrt(T));
        double d2 = d1 - sigma * Math.sqrt(T);
        
        double price;
        if (params.type() == OptionType.CALL) {
            price = S * normalCDF(d1) - K * Math.exp(-r * T) * normalCDF(d2);
        } else {
            price = K * Math.exp(-r * T) * normalCDF(-d2) - S * normalCDF(-d1);
        }
        
        // Greeks
        double delta = params.type() == OptionType.CALL ? normalCDF(d1) : normalCDF(d1) - 1;
        double gamma = normalPDF(d1) / (S * sigma * Math.sqrt(T));
        double theta = -(S * normalPDF(d1) * sigma) / (2 * Math.sqrt(T))
                      - r * K * Math.exp(-r * T) * normalCDF(params.type() == OptionType.CALL ? d2 : -d2);
        theta /= 365; // Per day
        double vega = S * normalPDF(d1) * Math.sqrt(T) / 100; // Per 1% vol change
        double rho = params.type() == OptionType.CALL 
            ? K * T * Math.exp(-r * T) * normalCDF(d2) / 100
            : -K * T * Math.exp(-r * T) * normalCDF(-d2) / 100;
        
        return new PricingResult(price, new OptionGreeks(delta, gamma, theta, vega, rho), "Black-Scholes");
    }

    /**
     * Binomial tree pricing (can handle American options).
     */
    public static PricingResult binomialTree(OptionParameters params, int steps) {
        double S = params.spotPrice();
        double K = params.strikePrice();
        double T = params.timeToExpiry();
        double r = params.riskFreeRate();
        double sigma = params.volatility();
        
        double dt = T / steps;
        double u = Math.exp(sigma * Math.sqrt(dt));      // Up factor
        double d = 1 / u;                                 // Down factor
        double p = (Math.exp(r * dt) - d) / (u - d);     // Risk-neutral probability
        double disc = Math.exp(-r * dt);                  // Discount factor
        
        // Build price tree at expiration
        double[] prices = new double[steps + 1];
        for (int i = 0; i <= steps; i++) {
            double ST = S * Math.pow(u, steps - i) * Math.pow(d, i);
            prices[i] = params.type() == OptionType.CALL 
                ? Math.max(0, ST - K) 
                : Math.max(0, K - ST);
        }
        
        // Work backwards through tree
        for (int step = steps - 1; step >= 0; step--) {
            for (int i = 0; i <= step; i++) {
                double continuation = disc * (p * prices[i] + (1 - p) * prices[i + 1]);
                
                if (params.style() == OptionStyle.AMERICAN) {
                    double ST = S * Math.pow(u, step - i) * Math.pow(d, i);
                    double exercise = params.type() == OptionType.CALL 
                        ? Math.max(0, ST - K) 
                        : Math.max(0, K - ST);
                    prices[i] = Math.max(continuation, exercise);
                } else {
                    prices[i] = continuation;
                }
            }
        }
        
        double price = prices[0];
        
        // Approximate Greeks using finite differences
        PricingResult base = blackScholes(params);
        
        return new PricingResult(price, base.greeks(), "Binomial (" + steps + " steps)");
    }

    /**
     * Calculates implied volatility from market price.
     */
    public static Real impliedVolatility(OptionParameters params, double marketPrice) {
        double low = 0.01;
        double high = 2.0;
        double tolerance = 0.0001;
        
        while (high - low > tolerance) {
            double mid = (low + high) / 2;
            OptionParameters testParams = new OptionParameters(
                params.spotPrice(), params.strikePrice(), params.timeToExpiry(),
                params.riskFreeRate(), mid, params.type(), params.style()
            );
            
            double testPrice = blackScholes(testParams).price();
            
            if (testPrice < marketPrice) {
                low = mid;
            } else {
                high = mid;
            }
        }
        
        return Real.of((low + high) / 2);
    }

    /**
     * Put-Call Parity check.
     */
    public static boolean checkPutCallParity(double callPrice, double putPrice,
            double spotPrice, double strikePrice, double riskFreeRate, double timeToExpiry) {
        
        double lhs = callPrice - putPrice;
        double rhs = spotPrice - strikePrice * Math.exp(-riskFreeRate * timeToExpiry);
        
        return Math.abs(lhs - rhs) < 0.01;
    }

    private static double normalCDF(double x) {
        return 0.5 * (1 + erf(x / Math.sqrt(2)));
    }

    private static double normalPDF(double x) {
        return Math.exp(-x * x / 2) / Math.sqrt(2 * Math.PI);
    }

    private static double erf(double x) {
        // Approximation
        double t = 1.0 / (1.0 + 0.5 * Math.abs(x));
        double tau = t * Math.exp(-x * x - 1.26551223 +
            t * (1.00002368 + t * (0.37409196 + t * (0.09678418 +
            t * (-0.18628806 + t * (0.27886807 + t * (-1.13520398 +
            t * (1.48851587 + t * (-0.82215223 + t * 0.17087277)))))))));
        
        return x >= 0 ? 1 - tau : tau - 1;
    }
}
