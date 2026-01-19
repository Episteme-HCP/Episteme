package org.jscience.history;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Maps and models historical trade routes.
 */
public final class TradeRouteMapper {

    private TradeRouteMapper() {}

    public record TradingPost(
        String name,
        double latitude,
        double longitude,
        String region,
        int foundedYear,
        List<String> primaryGoods
    ) {}

    public record TradeRoute(
        String name,
        List<TradingPost> waypoints,
        int activeFromYear,
        int activeToYear,
        String primaryCommodity,
        String transportMode  // land, sea, river, mixed
    ) {}

    public record RouteSegment(
        TradingPost start,
        TradingPost end,
        double distanceKm,
        int travelDays,
        double dangerLevel  // 0-1
    ) {}

    private static final List<TradeRoute> ROUTES = new ArrayList<>();
    private static final List<TradingPost> POSTS = new ArrayList<>();

    /**
     * Initializes historical trade routes.
     */
    public static void loadHistoricalData() {
        // Silk Road waypoints
        TradingPost xian = new TradingPost("Chang'an (Xi'an)", 34.27, 108.95, "China", -200, 
            List.of("silk", "porcelain", "tea"));
        TradingPost dunhuang = new TradingPost("Dunhuang", 40.14, 94.66, "China", -100,
            List.of("silk", "jade"));
        TradingPost samarkand = new TradingPost("Samarkand", 39.65, 66.96, "Central Asia", -700,
            List.of("paper", "textiles"));
        TradingPost ctesiphon = new TradingPost("Ctesiphon", 33.10, 44.58, "Persia", -600,
            List.of("spices", "glassware"));
        TradingPost antioch = new TradingPost("Antioch", 36.20, 36.16, "Levant", -300,
            List.of("purple dye", "glassware"));
        TradingPost constantinople = new TradingPost("Constantinople", 41.01, 28.98, "Byzantium", 330,
            List.of("gold", "silk", "spices"));
        
        POSTS.addAll(List.of(xian, dunhuang, samarkand, ctesiphon, antioch, constantinople));
        
        ROUTES.add(new TradeRoute("Silk Road", 
            List.of(xian, dunhuang, samarkand, ctesiphon, antioch, constantinople),
            -130, 1453, "silk", "land"));
        
        // Spice Route
        TradingPost malacca = new TradingPost("Malacca", 2.19, 102.25, "Southeast Asia", 1400,
            List.of("spices", "tin"));
        TradingPost calicut = new TradingPost("Calicut", 11.25, 75.77, "India", 100,
            List.of("pepper", "textiles"));
        TradingPost aden = new TradingPost("Aden", 12.79, 45.03, "Arabia", -200,
            List.of("frankincense", "myrrh"));
        TradingPost alexandria = new TradingPost("Alexandria", 31.20, 29.92, "Egypt", -332,
            List.of("grain", "papyrus"));
        TradingPost venice = new TradingPost("Venice", 45.44, 12.32, "Italy", 421,
            List.of("glass", "textiles"));
        
        POSTS.addAll(List.of(malacca, calicut, aden, alexandria, venice));
        
        ROUTES.add(new TradeRoute("Spice Route",
            List.of(malacca, calicut, aden, alexandria, venice),
            100, 1500, "spices", "sea"));
        
        // Hanseatic League
        TradingPost lubeck = new TradingPost("Lübeck", 53.87, 10.69, "Germany", 1143,
            List.of("salt", "beer"));
        TradingPost bergen = new TradingPost("Bergen", 60.39, 5.32, "Norway", 1070,
            List.of("fish", "timber"));
        TradingPost novgorod = new TradingPost("Novgorod", 58.52, 31.28, "Russia", 859,
            List.of("furs", "wax"));
        TradingPost bruges = new TradingPost("Bruges", 51.21, 3.22, "Flanders", 850,
            List.of("cloth", "wool"));
        
        POSTS.addAll(List.of(lubeck, bergen, novgorod, bruges));
        
        ROUTES.add(new TradeRoute("Hanseatic Route",
            List.of(bergen, lubeck, novgorod, bruges),
            1200, 1500, "fish", "sea"));
    }

    /**
     * Gets all routes active in a given year.
     */
    public static List<TradeRoute> getActiveRoutes(int year) {
        return ROUTES.stream()
            .filter(r -> year >= r.activeFromYear() && year <= r.activeToYear())
            .toList();
    }

    /**
     * Calculates total route distance.
     */
    public static Real calculateRouteDistance(TradeRoute route) {
        double total = 0;
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
     * Estimates travel time based on historical conditions.
     */
    public static int estimateTravelDays(TradeRoute route) {
        double distance = calculateRouteDistance(route).doubleValue();
        
        // Average daily travel distance by mode
        double dailyKm = switch (route.transportMode()) {
            case "land" -> 30;  // Caravan
            case "sea" -> 100;  // Sailing ship
            case "river" -> 50;
            default -> 40;
        };
        
        return (int) Math.ceil(distance / dailyKm);
    }

    /**
     * Finds trading posts that dealt in a specific commodity.
     */
    public static List<TradingPost> findByGoods(String commodity) {
        return POSTS.stream()
            .filter(p -> p.primaryGoods().stream()
                .anyMatch(g -> g.toLowerCase().contains(commodity.toLowerCase())))
            .toList();
    }

    /**
     * Calculates network centrality (importance in trade network).
     */
    public static Map<String, Real> calculateCentrality() {
        Map<String, Integer> connections = new HashMap<>();
        
        for (TradeRoute route : ROUTES) {
            for (TradingPost post : route.waypoints()) {
                connections.merge(post.name(), 1, Integer::sum);
            }
        }
        
        int maxConnections = connections.values().stream()
            .mapToInt(Integer::intValue)
            .max().orElse(1);
        
        Map<String, Real> centrality = new HashMap<>();
        connections.forEach((name, count) -> 
            centrality.put(name, Real.of((double) count / maxConnections)));
        
        return centrality;
    }

    private static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Earth radius km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon/2) * Math.sin(dLon/2);
        
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    }
}
