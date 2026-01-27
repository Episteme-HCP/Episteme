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
        Real spotPrice,        // Current stock price
        Real strikePrice,      // Exercise price
        Real timeToExpiry,     // In years
        Real riskFreeRate,     // Annualized
        Real volatility,       // Annualized std dev
        OptionType type,
        OptionStyle style
    ) {}

    public record OptionGreeks(
        Real delta,    // Price sensitivity to underlying
        Real gamma,    // Rate of change of delta
        Real theta,    // Time decay (per day)
        Real vega,     // Volatility sensitivity
        Real rho       // Interest rate sensitivity
    ) {}

    public record PricingResult(
        Real price,
        OptionGreeks greeks,
        String model
    ) {}

    /**
     * Black-Scholes pricing for European options.
     */
    public static PricingResult blackScholes(OptionParameters params) {
        Real S = params.spotPrice();
        Real K = params.strikePrice();
        Real T = params.timeToExpiry();
        Real r = params.riskFreeRate();
        Real sigma = params.volatility();
        
        // d1 = (ln(S/K) + (r + sigma^2 / 2) * T) / (sigma * sqrt(T))
        Real sqrtT = T.sqrt();
        Real d1 = S.divide(K).log().add(r.add(sigma.multiply(sigma).divide(Real.TWO)).multiply(T))
                    .divide(sigma.multiply(sqrtT));
        Real d2 = d1.subtract(sigma.multiply(sqrtT));
        
        Real price;
        Real ert = r.multiply(T).negate().exp();
        if (params.type() == OptionType.CALL) {
            price = S.multiply(normalCDF(d1)).subtract(K.multiply(ert).multiply(normalCDF(d2)));
        } else {
            price = K.multiply(ert).multiply(normalCDF(d2.negate())).subtract(S.multiply(normalCDF(d1.negate())));
        }
        
        // Greeks
        Real delta = params.type() == OptionType.CALL ? normalCDF(d1) : normalCDF(d1).subtract(Real.ONE);
        Real gamma = normalPDF(d1).divide(S.multiply(sigma).multiply(sqrtT));
        
        Real ncdfD2 = normalCDF(params.type() == OptionType.CALL ? d2 : d2.negate());
        Real theta = S.multiply(normalPDF(d1)).multiply(sigma).divide(Real.TWO.multiply(sqrtT)).negate()
                       .subtract(r.multiply(K).multiply(ert).multiply(ncdfD2));
        theta = theta.divide(Real.of(365)); // Per day
        
        Real vega = S.multiply(normalPDF(d1)).multiply(sqrtT).divide(Real.of(100)); // Per 1% vol change
        Real rho = params.type() == OptionType.CALL 
            ? K.multiply(T).multiply(ert).multiply(normalCDF(d2)).divide(Real.of(100))
            : K.multiply(T).multiply(ert).multiply(normalOf(d2.negate())).divide(Real.of(100)).negate();
        
        return new PricingResult(price, new OptionGreeks(delta, gamma, theta, vega, rho), "Black-Scholes");
    }

    private static Real normalOf(Real x) {
        return normalCDF(x);
    }

    /**
     * Binomial tree pricing (can handle American options).
     */
    public static PricingResult binomialTree(OptionParameters params, int steps) {
        Real S = params.spotPrice();
        Real K = params.strikePrice();
        Real T = params.timeToExpiry();
        Real r = params.riskFreeRate();
        Real sigma = params.volatility();
        
        Real dt = T.divide(Real.of(steps));
        Real u = sigma.multiply(dt.sqrt()).exp();      // Up factor
        Real d = u.inverse();                          // Down factor
        
        Real rdtExp = r.multiply(dt).exp();
        Real p = rdtExp.subtract(d).divide(u.subtract(d));     // Risk-neutral probability
        Real pInv = Real.ONE.subtract(p);
        Real disc = rdtExp.inverse();                         // Discount factor
        
        // Build price tree at expiration
        Real[] prices = new Real[steps + 1];
        for (int i = 0; i <= steps; i++) {
            Real ST = S.multiply(u.pow(steps - i)).multiply(d.pow(i));
            Real exercise = params.type() == OptionType.CALL 
                ? ST.subtract(K).max(Real.ZERO) 
                : K.subtract(ST).max(Real.ZERO);
            prices[i] = exercise;
        }
        
        // Work backwards through tree
        for (int step = steps - 1; step >= 0; step--) {
            for (int i = 0; i <= step; i++) {
                Real continuation = disc.multiply(p.multiply(prices[i]).add(pInv.multiply(prices[i + 1])));
                
                if (params.style() == OptionStyle.AMERICAN) {
                    Real ST = S.multiply(u.pow(step - i)).multiply(d.pow(i));
                    Real exercise = params.type() == OptionType.CALL 
                        ? ST.subtract(K).max(Real.ZERO) 
                        : K.subtract(ST).max(Real.ZERO);
                    prices[i] = continuation.max(exercise);
                } else {
                    prices[i] = continuation;
                }
            }
        }
        
        Real price = prices[0];
        
        // Approximate Greeks using finite differences or just return Black-Scholes greeks for simplicity
        PricingResult base = blackScholes(params);
        
        return new PricingResult(price, base.greeks(), "Binomial (" + steps + " steps)");
    }

    /**
     * Calculates implied volatility from market price.
     */
    public static Real impliedVolatility(OptionParameters params, Real marketPrice) {
        Real low = Real.of(0.01);
        Real high = Real.of(2.0);
        Real tolerance = Real.of(0.0001);
        
        while (high.subtract(low).compareTo(tolerance) > 0) {
            Real mid = low.add(high).divide(Real.TWO);
            OptionParameters testParams = new OptionParameters(
                params.spotPrice(), params.strikePrice(), params.timeToExpiry(),
                params.riskFreeRate(), mid, params.type(), params.style()
            );
            
            Real testPrice = blackScholes(testParams).price();
            
            if (testPrice.compareTo(marketPrice) < 0) {
                low = mid;
            } else {
                high = mid;
            }
        }
        
        return low.add(high).divide(Real.TWO);
    }

    /**
     * Put-Call Parity check.
     */
    public static boolean checkPutCallParity(Real callPrice, Real putPrice,
            Real spotPrice, Real strikePrice, Real riskFreeRate, Real timeToExpiry) {
        
        Real lhs = callPrice.subtract(putPrice);
        Real rhs = spotPrice.subtract(strikePrice.multiply(riskFreeRate.multiply(timeToExpiry).negate().exp()));
        
        return lhs.subtract(rhs).abs().compareTo(Real.of(0.01)) < 0;
    }

    private static Real normalCDF(Real x) {
        return Real.of(0.5).multiply(Real.ONE.add(erf(x.divide(Real.TWO.sqrt()))));
    }

    private static Real normalPDF(Real x) {
        return x.multiply(x).divide(Real.TWO).negate().exp()
                    .divide(Real.TWO.multiply(Real.PI).sqrt());
    }

    private static Real erf(Real x) {
        // Approximation
        Real t = Real.ONE.divide(Real.ONE.add(Real.of(0.5).multiply(x.abs())));
        
        Real poly = Real.of(1.00002368).add(t.multiply(Real.of(0.37409196).add(t.multiply(Real.of(0.09678418).add(
            t.multiply(Real.of(-0.18628806).add(t.multiply(Real.of(0.27886807).add(t.multiply(Real.of(-1.13520398).add(
            t.multiply(Real.of(1.48851587).add(t.multiply(Real.of(-0.82215223).add(t.multiply(Real.of(0.17087277)))))))))))))))));
        
        Real tau = t.multiply(x.multiply(x).negate().subtract(Real.of(1.26551223)).add(t.multiply(poly)).exp());
        
        return x.compareTo(Real.ZERO) >= 0 ? Real.ONE.subtract(tau) : tau.subtract(Real.ONE);
    }
}
