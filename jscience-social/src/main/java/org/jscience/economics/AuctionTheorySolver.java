package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Solvers for different auction formats.
 */
public final class AuctionTheorySolver {

    private AuctionTheorySolver() {}

    public record Bid(String bidder, double value) {}

    /**
     * Vickrey Auction (Second-price sealed-bid).
     * Winner is highest bidder, pays second highest price.
     */
    public static Bid solveVickrey(List<Bid> bids) {
        if (bids.size() < 2) return null;
        List<Bid> sorted = new ArrayList<>(bids);
        sorted.sort((b1, b2) -> Double.compare(b2.value(), b1.value()));
        
        return new Bid(sorted.get(0).bidder(), sorted.get(1).value());
    }

    /**
     * Dutch Auction simulator (Price drops until someone buys).
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
