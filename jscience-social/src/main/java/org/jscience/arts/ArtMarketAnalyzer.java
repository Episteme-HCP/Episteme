package org.jscience.arts;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.history.time.UncertainDate;
import java.util.*;

/**
 * Models and analyzes art market auction prices.
 */
public final class ArtMarketAnalyzer {

    private ArtMarketAnalyzer() {}

    public record AuctionRecord(
        String artworkTitle,
        String artist,
        int creationYear,
        String medium,
        String auctionHouse,
        UncertainDate saleDate,
        Real hammerPrice,
        Real estimateLow,
        Real estimateHigh,
        String currency
    ) {}

    public record ArtistMarketProfile(
        String artistName,
        Real averagePrice,
        Real medianPrice,
        Real priceVolatility,
        int totalSales,
        Real auctionAppearanceFrequency,
        int peakYear
    ) {}

    public record PricePrediction(
        Real estimatedPrice,
        Real confidenceLow,
        Real confidenceHigh,
        List<String> priceFactors
    ) {}

    private static final List<AuctionRecord> AUCTION_DATABASE = new ArrayList<>();

    /**
     * Records an auction sale.
     */
    public static void addAuctionRecord(AuctionRecord record) {
        AUCTION_DATABASE.add(record);
    }

    /**
     * Generates an artist's market profile.
     */
    public static ArtistMarketProfile analyzeArtist(String artistName) {
        List<AuctionRecord> artistSales = AUCTION_DATABASE.stream()
            .filter(r -> r.artist().equalsIgnoreCase(artistName))
            .toList();
        
        if (artistSales.isEmpty()) {
            return new ArtistMarketProfile(artistName, Real.ZERO, Real.ZERO, 
                Real.ZERO, 0, Real.ZERO, 0);
        }
        
        List<Double> prices = artistSales.stream()
            .map(r -> r.hammerPrice().doubleValue())
            .sorted()
            .toList();
        
        double avg = prices.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double median = prices.get(prices.size() / 2);
        
        // Calculate volatility (standard deviation / mean)
        double variance = prices.stream()
            .mapToDouble(p -> Math.pow(p - avg, 2))
            .average().orElse(0);
        double volatility = Math.sqrt(variance) / avg;
        
        // Find peak sales year
        Map<Integer, Integer> salesByYear = new HashMap<>();
        for (AuctionRecord r : artistSales) {
            int year = getYear(r.saleDate());
            salesByYear.merge(year, 1, Integer::sum);
        }
        int peakYear = salesByYear.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(0);
        
        return new ArtistMarketProfile(
            artistName,
            Real.of(avg),
            Real.of(median),
            Real.of(volatility),
            artistSales.size(),
            Real.of(artistSales.size() / 10.0), // Per year approximation
            peakYear
        );
    }

    /**
     * Predicts auction price for a work.
     */
    public static PricePrediction predictPrice(String artist, int creationYear,
            String medium, double sizeSqCm) {
        
        List<String> factors = new ArrayList<>();
        ArtistMarketProfile profile = analyzeArtist(artist);
        
        double basePrice = profile.averagePrice().doubleValue();
        double multiplier = 1.0;
        
        // Period adjustment
        if (creationYear < 1900) {
            multiplier *= 1.2;
            factors.add("Pre-1900 work (+20%)");
        }
        
        // Medium adjustment
        if (medium.toLowerCase().contains("oil")) {
            multiplier *= 1.3;
            factors.add("Oil painting premium (+30%)");
        } else if (medium.toLowerCase().contains("watercolor")) {
            multiplier *= 0.7;
            factors.add("Watercolor discount (-30%)");
        } else if (medium.toLowerCase().contains("print")) {
            multiplier *= 0.4;
            factors.add("Print/edition discount (-60%)");
        }
        
        // Size adjustment (larger = more expensive, diminishing returns)
        double sizeMultiplier = Math.log10(sizeSqCm / 1000.0 + 1) * 0.5 + 0.5;
        multiplier *= sizeMultiplier;
        factors.add(String.format("Size factor (%.0f%%)%n", (sizeMultiplier - 1) * 100));
        
        // Market trend (simplified)
        if (profile.priceVolatility().doubleValue() > 0.5) {
            factors.add("High volatility artist - wide range");
        }
        
        double predicted = basePrice * multiplier;
        double volatilityFactor = profile.priceVolatility().doubleValue();
        double low = predicted * (1 - volatilityFactor);
        double high = predicted * (1 + volatilityFactor);
        
        return new PricePrediction(
            Real.of(predicted),
            Real.of(low),
            Real.of(high),
            factors
        );
    }

    /**
     * Calculates market indices for art categories.
     */
    public static Map<String, Real> calculateMarketIndices(int baseYear, int currentYear) {
        Map<String, Real> indices = new HashMap<>();
        
        // Calculate index by comparing average prices
        Map<Integer, Double> pricesByYear = new HashMap<>();
        for (AuctionRecord r : AUCTION_DATABASE) {
            int year = getYear(r.saleDate());
            pricesByYear.merge(year, r.hammerPrice().doubleValue(), 
                (a, b) -> (a + b) / 2);
        }
        
        double basePrice = pricesByYear.getOrDefault(baseYear, 100.0);
        double currentPrice = pricesByYear.getOrDefault(currentYear, 100.0);
        
        indices.put("Overall Art Index", Real.of(currentPrice / basePrice * 100));
        
        return indices;
    }

    /**
     * Finds comparable sales.
     */
    public static List<AuctionRecord> findComparables(String artist, int year, 
            String medium, int maxResults) {
        
        return AUCTION_DATABASE.stream()
            .filter(r -> r.artist().equalsIgnoreCase(artist))
            .filter(r -> Math.abs(r.creationYear() - year) <= 10)
            .filter(r -> r.medium().toLowerCase().contains(medium.toLowerCase()))
            .sorted((a, b) -> b.hammerPrice().compareTo(a.hammerPrice()))
            .limit(maxResults)
            .toList();
    }

    private static int getYear(UncertainDate date) {
        var earliest = date != null ? date.getEarliestPossible() : null;
        return earliest != null ? earliest.getYear() : 2000;
    }
}
