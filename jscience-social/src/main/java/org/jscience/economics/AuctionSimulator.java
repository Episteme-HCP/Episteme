package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Auction simulation for various auction types.
 */
public final class AuctionSimulator {

    private AuctionSimulator() {}

    public record Bidder(String id, Real maxWillingness, double bidIncrement) {}

    public record AuctionResult(
        String winnerId,
        Real winningBid,
        Real sellerRevenue,
        int rounds,
        List<Bid> bidHistory
    ) {}

    public record Bid(String bidderId, Real amount, int round) {}

    /**
     * English (ascending) auction simulation.
     */
    public static AuctionResult englishAuction(List<Bidder> bidders, Real startingBid, 
            Real minimumIncrement) {
        
        List<Bid> history = new ArrayList<>();
        Real currentBid = startingBid;
        String currentWinner = null;
        int round = 0;
        
        List<Bidder> active = new ArrayList<>(bidders);
        
        while (active.size() > 1) {
            round++;
            boolean bidPlaced = false;
            
            for (Bidder bidder : new ArrayList<>(active)) {
                Real nextBid = currentBid.add(minimumIncrement);
                
                if (bidder.maxWillingness().compareTo(nextBid) >= 0) {
                    // This bidder can still compete
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
            
            if (!bidPlaced || round > 100) break; // Safety limit
        }
        
        return new AuctionResult(currentWinner, currentBid, currentBid, round, history);
    }

    /**
     * Dutch (descending) auction simulation.
     */
    public static AuctionResult dutchAuction(List<Bidder> bidders, Real startingBid, 
            Real decrement, Real reservePrice) {
        
        List<Bid> history = new ArrayList<>();
        Real currentPrice = startingBid;
        int round = 0;
        
        while (currentPrice.compareTo(reservePrice) >= 0) {
            round++;
            
            for (Bidder bidder : bidders) {
                // Bidder accepts if price is at or below their willingness
                // With some probability based on how good the deal is
                double ratio = currentPrice.doubleValue() / bidder.maxWillingness().doubleValue();
                
                if (ratio <= 1.0 && Math.random() < (1.0 - ratio) * 2) {
                    history.add(new Bid(bidder.id(), currentPrice, round));
                    return new AuctionResult(bidder.id(), currentPrice, currentPrice, round, history);
                }
            }
            
            currentPrice = currentPrice.subtract(decrement);
        }
        
        return new AuctionResult(null, Real.ZERO, Real.ZERO, round, history);
    }

    /**
     * Sealed-bid first-price auction simulation.
     */
    public static AuctionResult firstPriceSealedBid(List<Bidder> bidders) {
        List<Bid> bids = new ArrayList<>();
        
        for (Bidder bidder : bidders) {
            // Bid randomly between 70% and 95% of max willingness
            double fraction = 0.7 + Math.random() * 0.25;
            Real bid = bidder.maxWillingness().multiply(Real.of(fraction));
            bids.add(new Bid(bidder.id(), bid, 1));
        }
        
        bids.sort((a, b) -> b.amount().compareTo(a.amount()));
        
        if (bids.isEmpty()) {
            return new AuctionResult(null, Real.ZERO, Real.ZERO, 1, bids);
        }
        
        Bid winner = bids.get(0);
        return new AuctionResult(winner.bidderId(), winner.amount(), winner.amount(), 1, bids);
    }

    /**
     * Vickrey (second-price sealed-bid) auction simulation.
     * Winner pays second-highest bid.
     */
    public static AuctionResult vickreyAuction(List<Bidder> bidders) {
        List<Bid> bids = new ArrayList<>();
        
        for (Bidder bidder : bidders) {
            // In Vickrey, optimal strategy is to bid true value
            bids.add(new Bid(bidder.id(), bidder.maxWillingness(), 1));
        }
        
        bids.sort((a, b) -> b.amount().compareTo(a.amount()));
        
        if (bids.size() < 2) {
            return new AuctionResult(
                bids.isEmpty() ? null : bids.get(0).bidderId(),
                bids.isEmpty() ? Real.ZERO : bids.get(0).amount(),
                bids.isEmpty() ? Real.ZERO : bids.get(0).amount(),
                1, bids
            );
        }
        
        Bid winner = bids.get(0);
        Real secondPrice = bids.get(1).amount();
        
        return new AuctionResult(winner.bidderId(), winner.amount(), secondPrice, 1, bids);
    }

    /**
     * Calculates expected revenue for different auction types.
     */
    public static Map<String, Real> compareAuctionFormats(List<Bidder> bidders, int simulations) {
        Map<String, Double> totals = new HashMap<>();
        totals.put("English", 0.0);
        totals.put("Dutch", 0.0);
        totals.put("FirstPrice", 0.0);
        totals.put("Vickrey", 0.0);
        
        Real start = bidders.stream()
            .map(Bidder::maxWillingness)
            .max(Real::compareTo)
            .orElse(Real.of(100))
            .multiply(Real.of(0.5));
        
        for (int i = 0; i < simulations; i++) {
            totals.merge("English", 
                englishAuction(bidders, start, Real.of(1)).sellerRevenue().doubleValue(), 
                (a, b) -> a + b);
            totals.merge("Dutch",
                dutchAuction(bidders, start.multiply(Real.of(2)), Real.of(1), start.multiply(Real.of(0.5)))
                    .sellerRevenue().doubleValue(),
                (a, b) -> a + b);
            totals.merge("FirstPrice",
                firstPriceSealedBid(bidders).sellerRevenue().doubleValue(),
                (a, b) -> a + b);
            totals.merge("Vickrey",
                vickreyAuction(bidders).sellerRevenue().doubleValue(),
                (a, b) -> a + b);
        }
        
        Map<String, Real> averages = new HashMap<>();
        totals.forEach((k, v) -> averages.put(k, Real.of(v / simulations)));
        return averages;
    }
}
