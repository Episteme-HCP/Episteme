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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Provides mathematical simulations for various auction types (English, Dutch, Sealed-bid).
 * Analyzes bidder behavior and expected seller revenue.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class AuctionSimulator {

    private AuctionSimulator() {}

    /** Model for an individual auction participant. */
    public record Bidder(String id, Real maxWillingness, double bidIncrement) implements Serializable {}

    /** Result summary for a completed auction simulation. */
    public record AuctionResult(
        String winnerId,
        Real winningBid,
        Real sellerRevenue,
        int rounds,
        List<Bid> bidHistory
    ) implements Serializable {}

    /** Individual bid entry in an auction history. */
    public record Bid(String bidderId, Real amount, int round) implements Serializable {}

    /**
     * English (ascending) auction simulation.
     * 
     * @param bidders          the participating bidders
     * @param startingBid      initial price
     * @param minimumIncrement minimum allowed raise
     * @return the simulation result
     */
    public static AuctionResult englishAuction(List<Bidder> bidders, Real startingBid, 
            Real minimumIncrement) {
        
        List<Bid> history = new ArrayList<>();
        Real currentBid = Objects.requireNonNull(startingBid, "Starting bid cannot be null");
        String currentWinner = null;
        int round = 0;
        
        List<Bidder> active = new ArrayList<>(Objects.requireNonNull(bidders, "Bidders list cannot be null"));
        
        while (active.size() > 1) {
            round++;
            boolean bidPlaced = false;
            
            for (Bidder bidder : new ArrayList<>(active)) {
                Real nextBid = currentBid.add(minimumIncrement);
                
                if (bidder.maxWillingness().compareTo(nextBid) >= 0) {
                    if (currentWinner == null || !currentWinner.equals(bidder.id())) {
                        currentBid = nextBid;
                        currentWinner = bidder.id();
                        history.add(new Bid(bidder.id(), currentBid, round));
                        bidPlaced = true;
                    }
                } else {
                    active.remove(bidder);
                }
            }
            if (!bidPlaced || round > 1000) break;
        }
        
        return new AuctionResult(currentWinner, currentBid, currentBid, round, history);
    }

    /** First-price sealed-bid auction simulation. */
    public static AuctionResult firstPriceSealedBid(List<Bidder> bidders) {
        if (bidders == null || bidders.isEmpty()) {
            return new AuctionResult(null, Real.ZERO, Real.ZERO, 1, List.of());
        }
        List<Bid> bids = new ArrayList<>();
        for (Bidder bidder : bidders) {
            // Strategic shading: bid 70-95% of true value
            double fraction = 0.7 + Math.random() * 0.25;
            Real amount = bidder.maxWillingness().multiply(Real.of(fraction));
            bids.add(new Bid(bidder.id(), amount, 1));
        }
        bids.sort((a, b) -> b.amount().compareTo(a.amount()));
        Bid winner = bids.get(0);
        return new AuctionResult(winner.bidderId(), winner.amount(), winner.amount(), 1, bids);
    }

    /** Vickrey (second-price sealed-bid) auction simulation. */
    public static AuctionResult vickreyAuction(List<Bidder> bidders) {
        if (bidders == null || bidders.isEmpty()) {
            return new AuctionResult(null, Real.ZERO, Real.ZERO, 1, List.of());
        }
        List<Bid> bids = new ArrayList<>();
        for (Bidder bidder : bidders) {
            // Dominant strategy: bid true value
            bids.add(new Bid(bidder.id(), bidder.maxWillingness(), 1));
        }
        bids.sort((a, b) -> b.amount().compareTo(a.amount()));
        
        if (bids.size() < 2) {
            Bid winner = bids.get(0);
            return new AuctionResult(winner.bidderId(), winner.amount(), winner.amount(), 1, bids);
        }
        
        Bid winner = bids.get(0);
        Real secondPrice = bids.get(1).amount();
        return new AuctionResult(winner.bidderId(), winner.amount(), secondPrice, 1, bids);
    }

    /**
     * Statistical comparison of revenue across multiple simulations of different formats.
     */
    public static Map<String, Real> compareAuctionFormats(List<Bidder> bidders, int simulations) {
        Map<String, Double> totals = new HashMap<>();
        String[] types = {"English", "FirstPrice", "Vickrey"};
        for (String t : types) totals.put(t, 0.0);
        
        Real start = bidders.stream().map(Bidder::maxWillingness).max(Real::compareTo).orElse(Real.ZERO).multiply(Real.of(0.1));
        
        for (int i = 0; i < simulations; i++) {
            totals.merge("English", englishAuction(bidders, start, Real.of(1)).sellerRevenue().doubleValue(), Double::sum);
            totals.merge("FirstPrice", firstPriceSealedBid(bidders).sellerRevenue().doubleValue(), Double::sum);
            totals.merge("Vickrey", vickreyAuction(bidders).sellerRevenue().doubleValue(), Double::sum);
        }
        
        Map<String, Real> averages = new HashMap<>();
        totals.forEach((k, v) -> averages.put(k, Real.of(v / simulations)));
        return averages;
    }
}
