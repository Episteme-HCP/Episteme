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

package org.jscience.social.economics;

import org.jscience.core.mathematics.numbers.real.Real;
import java.util.ArrayList;
import java.util.List;

/**
 * Solvers for different auction formats used in Game Theory.
 * Provides analytical solutions and simulations for standard auction types.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 */
public final class AuctionTheorySolver {

    private AuctionTheorySolver() {}

    /**
     * Represents a bid in an auction.
     * @param bidder the identifier of the bidder
     * @param value the monetary value of the bid
     */
    public record Bid(String bidder, double value) {}

    /**
     * Solves a Vickrey Auction (Second-price sealed-bid).
     * The winner is the highest bidder, but pays the price of the second highest bid.
     *
     * @param bids the list of all bids placed
     * @return the winning bid structure (winner identity, payment amount), or null if insufficient bids
     */
    public static Bid solveVickrey(List<Bid> bids) {
        if (bids.size() < 2) return null;
        List<Bid> sorted = new ArrayList<>(bids);
        sorted.sort((b1, b2) -> Double.compare(b2.value(), b1.value()));
        
        return new Bid(sorted.get(0).bidder(), sorted.get(1).value());
    }

    /**
     * Simulates a Dutch Auction (descending price).
     * The price starts high and drops until a bidder accepts the current price.
     *
     * @param startPrice the starting high price
     * @param reservePrice the minimum price the seller will accept
     * @param dropRate the amount the price drops per step
     * @param bidderValuations a list of valuations for each bidder
     * @return the final sale price
     */
    public static Real simulateDutch(double startPrice, double reservePrice, double dropRate, 
                                     List<Double> bidderValuations) {
        double currentPrice = startPrice;
        while (currentPrice > reservePrice) {
            for (double val : bidderValuations) {
                if (val >= currentPrice) return Real.of(currentPrice);
            }
            currentPrice -= dropRate;
        }
        return Real.of(reservePrice);
    }
}

