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

import org.jscience.mathematics.analysis.Integration;
import org.jscience.mathematics.analysis.RealFunction;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Analytical tool for modeling supply and demand curves and calculating 
 * competitive equilibrium. It provides mathematical utilities for market 
 * efficiency analysis, including consumer/producer surplus and deadweight loss.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class SupplyDemandModel {

    private SupplyDemandModel() {}

    /**
     * Resolves the market equilibrium price where the quantity supplied 
     * exactly equals the quantity demanded.
     * 
     * @param supply the supply function S(p)
     * @param demand the demand function D(p)
     * @param priceLow lower bound for numerical search
     * @param priceHigh upper bound for numerical search
     * @param tolerance precision limit for the solver
     * @return the calculated equilibrium price
     */
    public static Real findEquilibriumPrice(RealFunction supply, RealFunction demand, 
                                            Real priceLow, Real priceHigh, Real tolerance) {
        Real low = priceLow;
        Real high = priceHigh;
        
        for (int i = 0; i < 100; i++) {
            Real mid = low.add(high).divide(Real.of(2.0));
            Real supplyAtMid = supply.apply(mid);
            Real demandAtMid = demand.apply(mid);
            Real diff = supplyAtMid.subtract(demandAtMid);
            
            if (diff.abs().compareTo(tolerance) < 0) {
                return mid;
            }
            
            Real diffLow = supply.apply(low).subtract(demand.apply(low));
            if (diffLow.multiply(diff).compareTo(Real.ZERO) > 0) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return low.add(high).divide(Real.of(2.0));
    }

    /**
     * Calculates the Consumer Surplus (CS), representing the cumulative 
     * difference between what consumers are willing to pay and the market price.
     * Formula: integral[0 to Q*] (D(q) - P*) dq
     * 
     * @param demand the demand function D(q) mapping quantity to price
     * @param equilibriumPrice (P*) the market clearing price
     * @param equilibriumQuantity (Q*) the market clearing quantity
     * @return cumulative consumer surplus value
     */
    public static Real consumerSurplus(RealFunction demand, Real equilibriumPrice, Real equilibriumQuantity) {
        RealFunction surplusFunction = q -> demand.apply(q).subtract(equilibriumPrice);
        return Integration.integrate(surplusFunction, Real.ZERO, equilibriumQuantity);
    }

    /**
     * Calculates the Producer Surplus (PS), representing the cumulative 
     * difference between the market price and the price producers are willing 
     * to accept.
     * Formula: integral[0 to Q*] (P* - S(q)) dq
     * 
     * @param supply the supply function S(q) mapping quantity to price
     * @param equilibriumPrice (P*) the market clearing price
     * @param equilibriumQuantity (Q*) the market clearing quantity
     * @return cumulative producer surplus value
     */
    public static Real producerSurplus(RealFunction supply, Real equilibriumPrice, Real equilibriumQuantity) {
        RealFunction surplusFunction = q -> equilibriumPrice.subtract(supply.apply(q));
        return Integration.integrate(surplusFunction, Real.ZERO, equilibriumQuantity);
    }

    /**
     * Calculates the Total Social Welfare as the sum of consumer and 
     * producer surpluses.
     * 
     * @param supply supply function S(q)
     * @param demand demand function D(q)
     * @param equilibriumPrice market price
     * @param equilibriumQuantity market quantity
     * @return total economic welfare
     */
    public static Real totalWelfare(RealFunction supply, RealFunction demand, 
                                    Real equilibriumPrice, Real equilibriumQuantity) {
        return consumerSurplus(demand, equilibriumPrice, equilibriumQuantity)
               .add(producerSurplus(supply, equilibriumPrice, equilibriumQuantity));
    }

    /**
     * Calculates the Deadweight Loss (DWL) resulting from market 
     * inefficiencies or external interventions (e.g., price caps).
     * 
     * @param supply supply function S(q)
     * @param demand demand function D(q)
     * @param equilibriumQuantity (Q*) the efficient quantity
     * @param actualQuantity the actual traded quantity
     * @return value of lost economic efficiency (DWL)
     */
    public static Real deadweightLoss(RealFunction supply, RealFunction demand,
                                      Real equilibriumQuantity, Real actualQuantity) {
        if (actualQuantity.compareTo(equilibriumQuantity) >= 0) {
            return Real.ZERO;
        }
        RealFunction lossFunction = q -> demand.apply(q).subtract(supply.apply(q));
        return Integration.integrate(lossFunction, actualQuantity, equilibriumQuantity);
    }

    /**
     * Estimates the Price Elasticity of Demand (PED) at a specific price point.
     * Formula: E = (dQ/dP) * (P/Q)
     * 
     * @param demand demand function D(p)
     * @param price target price point
     * @param deltaP increment for numerical derivative approximation
     * @return point elasticity value
     */
    public static Real priceElasticity(RealFunction demand, Real price, Real deltaP) {
        Real q1 = demand.apply(price);
        Real q2 = demand.apply(price.add(deltaP));
        Real dQ = q2.subtract(q1);
        
        if (q1.isZero() || deltaP.isZero()) return Real.ZERO;
        return dQ.divide(deltaP).multiply(price).divide(q1);
    }
}
