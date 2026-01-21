/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2014 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.analysis.RealFunction;
import org.jscience.mathematics.analysis.Integration;

/**
 * Supply and Demand curve modeling with economic equilibrium calculations.
 * Provides tools for finding equilibrium prices, calculating surpluses, 
 * and analyzing market efficiency.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.2
 */
public final class SupplyDemandModel {

    private SupplyDemandModel() {}

    /**
     * Finds the equilibrium price where supply equals demand.
     * Uses bisection method to find the root of supply(p) - demand(p) = 0.
     * 
     * @param supply the supply function s(p)
     * @param demand the demand function d(p)
     * @param priceLow lower bound for price search
     * @param priceHigh upper bound for price search
     * @param tolerance convergence tolerance
     * @return the equilibrium price
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
            
            // If supply > demand, price is too high (reduce)
            // If demand > supply, price is too low (increase)
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
     * Calculates Consumer Surplus: integral from 0 to Q* of D(q) - P*.
     * 
     * @param demand the demand function D(q)
     * @param equilibriumPrice the market price P*
     * @param equilibriumQuantity the quantity traded Q*
     * @return the consumer surplus value
     */
    public static Real consumerSurplus(RealFunction demand, Real equilibriumPrice, Real equilibriumQuantity) {
        RealFunction surplusFunction = q -> demand.apply(q).subtract(equilibriumPrice);
        return Integration.integrate(surplusFunction, Real.ZERO, equilibriumQuantity);
    }

    /**
     * Calculates Producer Surplus: integral from 0 to Q* of P* - S(q).
     * 
     * @param supply the supply function S(q)
     * @param equilibriumPrice the market price P*
     * @param equilibriumQuantity the quantity traded Q*
     * @return the producer surplus value
     */
    public static Real producerSurplus(RealFunction supply, Real equilibriumPrice, Real equilibriumQuantity) {
        RealFunction surplusFunction = q -> equilibriumPrice.subtract(supply.apply(q));
        return Integration.integrate(surplusFunction, Real.ZERO, equilibriumQuantity);
    }

    /**
     * Calculates Total Welfare (Consumer + Producer Surplus).
     * 
     * @param supply the supply function
     * @param demand the demand function
     * @param equilibriumPrice the market price
     * @param equilibriumQuantity the quantity traded
     * @return the total economic welfare
     */
    public static Real totalWelfare(RealFunction supply, RealFunction demand, 
                                    Real equilibriumPrice, Real equilibriumQuantity) {
        return consumerSurplus(demand, equilibriumPrice, equilibriumQuantity)
               .add(producerSurplus(supply, equilibriumPrice, equilibriumQuantity));
    }

    /**
     * Calculates Deadweight Loss from a price floor or ceiling.
     * 
     * @param supply the supply function
     * @param demand the demand function
     * @param equilibriumQuantity the efficient quantity Q*
     * @param actualQuantity the actual quantity traded
     * @return the deadweight loss value
     */
    public static Real deadweightLoss(RealFunction supply, RealFunction demand,
                                      Real equilibriumQuantity, Real actualQuantity) {
        if (actualQuantity.compareTo(equilibriumQuantity) >= 0) {
            return Real.ZERO; // No deadweight loss if quantity is at or above equilibrium
        }
        // Integrate the difference between demand and supply from actualQ to equilibriumQ
        RealFunction lossFunction = q -> demand.apply(q).subtract(supply.apply(q));
        return Integration.integrate(lossFunction, actualQuantity, equilibriumQuantity);
    }

    /**
     * Price Elasticity of Demand at a given price point.
     * E = (dQ/dP) * (P/Q)
     * 
     * @param demand demand function
     * @param price price point
     * @param deltaP small change in price for approximation
     * @return price elasticity of demand
     */
    public static Real priceElasticity(RealFunction demand, Real price, Real deltaP) {
        Real q1 = demand.apply(price);
        Real q2 = demand.apply(price.add(deltaP));
        Real dQ = q2.subtract(q1);
        Real dP = deltaP;
        
        if (q1.compareTo(Real.ZERO) == 0) return Real.of(Double.NaN);
        return dQ.divide(dP).multiply(price).divide(q1);
    }
}
