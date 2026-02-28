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

package org.episteme.social.history;

import org.episteme.core.mathematics.numbers.real.Real;
import java.io.Serializable;
import java.util.*;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;

/**
 * Maps and models historical trade routes, connecting distant trading posts.
 * Provides tools for distance calculation, travel time estimation, and network centrality analysis.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class TradeRouteMapper {

    private TradeRouteMapper() {
        // Prevent instantiation
    }

    /**
     * Represents a historical trading hub with its geographical location and primary goods.
     * 
     * @param name        trading post name
     * @param latitude    geographical latitude
     * @param longitude   geographical longitude
     * @param region      geographical region name
     * @param foundedYear year the trading post was established (CE, negative for BCE)
     * @param primaryGoods list of primary commodities traded
     */
    @Persistent
    public record TradingPost(
        @Attribute String name,
        @Attribute double latitude,
        @Attribute double longitude,
        @Attribute String region,
        @Attribute int foundedYear,
        @Attribute List<String> primaryGoods
    ) implements Serializable {
        private static final long serialVersionUID = 1L;

        public TradingPost {
            Objects.requireNonNull(name, "Trading post name cannot be null");
            primaryGoods = primaryGoods != null ? List.copyOf(primaryGoods) : List.of();
        }
    }

    /**
     * Represents an established trade route consisting of multiple waypoints.
     * 
     * @param name             route name
     * @param waypoints        ordered list of trading posts along the route
     * @param activeFromYear   start of active period (CE, negative for BCE)
     * @param activeToYear     end of active period (CE)
     * @param primaryCommodity the main traded good on this route
     * @param transportMode    mode of transport (land, sea, river, mixed)
     */
    @Persistent
    public record TradeRoute(
        @Attribute String name,
        @Attribute List<TradingPost> waypoints,
        @Attribute int activeFromYear,
        @Attribute int activeToYear,
        @Attribute String primaryCommodity,
        @Attribute String transportMode
    ) implements Serializable {
        private static final long serialVersionUID = 1L;

        public TradeRoute {
            Objects.requireNonNull(name, "Trade route name cannot be null");
            waypoints = waypoints != null ? List.copyOf(waypoints) : List.of();
        }
    }

    /**
     * Specific segment between two consecutive trading posts in a route.
     * 
     * @param start       segment origin
     * @param end         segment destination
     * @param distanceKm  distance in kilometers
     * @param travelDays  estimated travel time in days
     * @param dangerLevel risk level (0.0 = safe, 1.0 = perilous)
     */
    @Persistent
    public record RouteSegment(
        @Attribute TradingPost start,
        @Attribute TradingPost end,
        @Attribute double distanceKm,
        @Attribute int travelDays,
        @Attribute double dangerLevel
    ) implements Serializable {
        private static final long serialVersionUID = 1L;

        public RouteSegment {
            Objects.requireNonNull(start, "Start post cannot be null");
            Objects.requireNonNull(end, "End post cannot be null");
        }
    }

    private static final List<TradeRoute> ROUTES = Collections.synchronizedList(new ArrayList<>());
    private static final List<TradingPost> POSTS = Collections.synchronizedList(new ArrayList<>());

    /**
     * Initializes the database with major historical trade routes.
     */
    public static void loadHistoricalData() {
        // Silk Road waypoints
        TradingPost xian = new TradingPost("Chang'an (Xi'an)", 34.27, 108.95, "China", -200, 
            Arrays.asList("silk", "porcelain", "tea"));
        TradingPost dunhuang = new TradingPost("Dunhuang", 40.14, 94.66, "China", -100,
            Arrays.asList("silk", "jade"));
        TradingPost samarkand = new TradingPost("Samarkand", 39.65, 66.96, "Central Asia", -700,
            Arrays.asList("paper", "textiles"));
        TradingPost ctesiphon = new TradingPost("Ctesiphon", 33.10, 44.58, "Persia", -600,
            Arrays.asList("spices", "glassware"));
        TradingPost antioch = new TradingPost("Antioch", 36.20, 36.16, "Levant", -300,
            Arrays.asList("purple dye", "glassware"));
        TradingPost constantinople = new TradingPost("Constantinople", 41.01, 28.98, "Byzantium", 330,
            Arrays.asList("gold", "silk", "spices"));
        
        synchronized (POSTS) {
            POSTS.addAll(Arrays.asList(xian, dunhuang, samarkand, ctesiphon, antioch, constantinople));
        }
        
        synchronized (ROUTES) {
            ROUTES.add(new TradeRoute("Silk Road", 
                Arrays.asList(xian, dunhuang, samarkand, ctesiphon, antioch, constantinople),
                -130, 1453, "silk", "land"));
        }
        
        // Spice Route
        TradingPost malacca = new TradingPost("Malacca", 2.19, 102.25, "Southeast Asia", 1400,
            Arrays.asList("spices", "tin"));
        TradingPost calicut = new TradingPost("Calicut", 11.25, 75.77, "India", 100,
            Arrays.asList("pepper", "textiles"));
        TradingPost aden = new TradingPost("Aden", 12.79, 45.03, "Arabia", -200,
            Arrays.asList("frankincense", "myrrh"));
        TradingPost alexandria = new TradingPost("Alexandria", 31.20, 29.92, "Egypt", -332,
            Arrays.asList("grain", "papyrus"));
        TradingPost venice = new TradingPost("Venice", 45.44, 12.32, "Italy", 421,
            Arrays.asList("glass", "textiles"));
        
        synchronized (POSTS) {
            POSTS.addAll(Arrays.asList(malacca, calicut, aden, alexandria, venice));
        }
        
        synchronized (ROUTES) {
            ROUTES.add(new TradeRoute("Spice Route",
                Arrays.asList(malacca, calicut, aden, alexandria, venice),
                100, 1500, "spices", "sea"));
        }
        
        // Hanseatic League
        TradingPost lubeck = new TradingPost("LÃ¼beck", 53.87, 10.69, "Germany", 1143,
            Arrays.asList("salt", "beer"));
        TradingPost bergen = new TradingPost("Bergen", 60.39, 5.32, "Norway", 1070,
            Arrays.asList("fish", "timber"));
        TradingPost novgorod = new TradingPost("Novgorod", 58.52, 31.28, "Russia", 859,
            Arrays.asList("furs", "wax"));
        TradingPost bruges = new TradingPost("Bruges", 51.21, 3.22, "Flanders", 850,
            Arrays.asList("cloth", "wool"));
        
        synchronized (POSTS) {
            POSTS.addAll(Arrays.asList(lubeck, bergen, novgorod, bruges));
        }
        
        synchronized (ROUTES) {
            ROUTES.add(new TradeRoute("Hanseatic Route",
                Arrays.asList(bergen, lubeck, novgorod, bruges),
                1200, 1500, "fish", "sea"));
        }
    }

    /**
     * Retrieves all recorded routes that were active in a specific year.
     * 
     * @param year historical year
     * @return unmodifiable list of active trade routes
     */
    public static List<TradeRoute> getActiveRoutes(int year) {
        synchronized (ROUTES) {
            return ROUTES.stream()
                .filter(r -> year >= r.activeFromYear() && year <= r.activeToYear())
                .toList();
        }
    }

    /**
     * Calculates the total distance of a trade route in kilometers using haversine formula.
     * 
     * @param route the trade route
     * @return total distance as a {@link Real} number
     * @throws NullPointerException if route is null
     */
    public static Real calculateRouteDistance(TradeRoute route) {
        Objects.requireNonNull(route, "Trade route cannot be null");
        double total = 0.0;
        List<TradingPost> waypoints = route.waypoints();
        
        for (int i = 0; i < waypoints.size() - 1; i++) {
            total += haversineDistance(
                waypoints.get(i).latitude(), waypoints.get(i).longitude(),
                waypoints.get(i+1).latitude(), waypoints.get(i+1).longitude()
            );
        }
        
        return Real.of(total);
    }

    /**
     * Estimates the number of travel days for a route based on transport mode.
     * 
     * @param route the trade route
     * @return estimated duration in days
     * @throws NullPointerException if route is null
     */
    public static int estimateTravelDays(TradeRoute route) {
        Objects.requireNonNull(route, "Trade route cannot be null");
        double distance = calculateRouteDistance(route).doubleValue();
        
        String mode = route.transportMode();
        double dailyKm = switch (mode != null ? mode.toLowerCase().trim() : "mixed") {
            case "land" -> 30.0;  // Typical caravan speed
            case "sea" -> 100.0;  // Average sailing speed
            case "river" -> 50.0;
            default -> 40.0;
        };
        
        return (int) Math.ceil(distance / dailyKm);
    }

    /**
     * Searches for trading posts associated with a specific commodity.
     * 
     * @param commodity commodity name
     * @return unmodifiable list of trading posts dealing in that commodity
     * @throws NullPointerException if commodity is null
     */
    public static List<TradingPost> findByGoods(String commodity) {
        Objects.requireNonNull(commodity, "Commodity name cannot be null");
        String lower = commodity.toLowerCase().trim();
        synchronized (POSTS) {
            return POSTS.stream()
                .filter(p -> p.primaryGoods().stream()
                    .anyMatch(g -> g.toLowerCase().contains(lower)))
                .toList();
        }
    }

    /**
     * Calculates the network centrality of trading posts based on route connectivity.
     * 
     * @return unmodifiable map of trading post names to centrality score (0.0 to 1.0)
     */
    public static Map<String, Real> calculateCentrality() {
        Map<String, Integer> correlations = new HashMap<>();
        
        synchronized (ROUTES) {
            for (TradeRoute route : ROUTES) {
                for (TradingPost post : route.waypoints()) {
                    correlations.merge(post.name(), 1, (a, b) -> a + b);
                }
            }
        }
        
        int maxConn = correlations.values().stream()
            .mapToInt(Integer::intValue)
            .max().orElse(1);
        
        Map<String, Real> centrality = new HashMap<>();
        correlations.forEach((name, count) -> 
            centrality.put(name, Real.of((double) count / maxConn)));
        
        return Collections.unmodifiableMap(centrality);
    }

    private static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double r = 6371.0; // Earth radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2.0) * Math.sin(dLon / 2.0);
        
        return r * 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
    }
}

