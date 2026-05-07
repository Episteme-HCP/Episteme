/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.arts;

import java.io.Serializable;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.episteme.social.history.time.TimeCoordinate;
import org.episteme.core.mathematics.numbers.real.Real;

/**
 * Models and analyzes art market activity, specifically focusing on auction prices,
 * artist performance metrics, and price prediction.
 * It uses historical auction data to generate artist profiles and calculate 
 * market indices.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class ArtMarketAnalyzer {

    private ArtMarketAnalyzer() {}

    /**
     * Represents a single auction sale record.
     */
    public record AuctionRecord(
        String artworkTitle,
        String artist,
        int creationYear,
        String medium,
        String auctionHouse,
        TimeCoordinate saleDate,
        Real hammerPrice,
        Real estimateLow,
        Real estimateHigh,
        String currency
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Statistical profile of an artist's performance in the auction market.
     */
    public record ArtistMarketProfile(
        String artistName,
        Real averagePrice,
        Real medianPrice,
        Real priceVolatility,
        int totalSales,
        Real auctionAppearanceFrequency,
        int peakYear
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Result of a price prediction analysis for a specific artwork.
     */
    public record PricePrediction(
        Real estimatedPrice,
        Real confidenceLow,
        Real confidenceHigh,
        List<String> priceFactors
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    private static final List<AuctionRecord> AUCTION_DATABASE = new ArrayList<>();

    /**
     * Records a new auction sale in the internal database.
     * 
     * @param record the auction record to add
     */
    public static void addAuctionRecord(AuctionRecord record) {
        AUCTION_DATABASE.add(record);
    }

    /**
     * Generates a market performance profile for a specific artist based on 
     * recorded sales.
     * 
     * @param artistName the name of the artist to analyze
     * @return an ArtistMarketProfile containing statistical metrics
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
        double volatility = avg != 0 ? Math.sqrt(variance) / avg : 0;
        
        // Find peak sales year
        Map<Integer, Integer> salesByYear = new HashMap<>();
        for (AuctionRecord r : artistSales) {
            int year = getYear(r.saleDate());
            salesByYear.merge(year, 1, (a, b) -> a + b);
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
     * Predicts the potential auction price for an artwork based on artist performance, 
     * medium, age, and physical size.
     * 
     * @param artist the name of the artist
     * @param creationYear the year the work was created
     * @param medium the materials used (e.g., "oil", "watercolor")
     * @param sizeSqCm the surface area in square centimeters
     * @return a PricePrediction containing estimated value and confidence range
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
     * Calculates market indices by comparing average prices over time.
     * 
     * @param baseYear the starting year for the index (value 100)
     * @param currentYear the year to evaluate
     * @return a map of market indices (e.g., "Overall Art Index")
     */
    public static Map<String, Real> calculateMarketIndices(int baseYear, int currentYear) {
        Map<String, Real> indices = new HashMap<>();
        
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
     * Finds previous auction sales that are comparable to a given work.
     * 
     * @param artist the name of the artist
     * @param year the creation year (within a 10-year range)
     * @param medium the medium used
     * @param maxResults maximum number of records to return
     * @return a list of comparable AuctionRecords sorted by price
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

    private static int getYear(TimeCoordinate date) {
        return date != null ? date.toInstant().atZone(ZoneOffset.UTC).getYear() : 2000;
    }
}

