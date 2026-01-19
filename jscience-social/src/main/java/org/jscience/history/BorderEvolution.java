package org.jscience.history;

import org.jscience.history.time.UncertainDate;
import java.util.*;

/**
 * Models and visualizes the evolution of political borders over time.
 */
public final class BorderEvolution {

    private BorderEvolution() {}

    public record Territory(
        String name,
        String sovereignEntity,
        List<double[]> boundaryPoints,  // latitude, longitude pairs
        UncertainDate startDate,
        UncertainDate endDate,
        String notes
    ) {}

    public record BorderChange(
        UncertainDate date,
        String description,
        Territory beforeTerritory,
        Territory afterTerritory,
        ChangeType type
    ) {}

    public enum ChangeType {
        CONQUEST, CESSION, PURCHASE, UNION, DISSOLUTION, 
        TREATY, INDEPENDENCE, PARTITION, ANNEXATION
    }

    private static final List<Territory> TERRITORY_DATABASE = new ArrayList<>();
    private static final List<BorderChange> CHANGE_DATABASE = new ArrayList<>();

    /**
     * Adds a territory to the database.
     */
    public static void addTerritory(Territory territory) {
        TERRITORY_DATABASE.add(territory);
    }

    /**
     * Adds a border change event.
     */
    public static void addBorderChange(BorderChange change) {
        CHANGE_DATABASE.add(change);
    }

    /**
     * Gets all territories existing at a specific date.
     */
    public static List<Territory> getTerritoriesAtDate(UncertainDate date) {
        return TERRITORY_DATABASE.stream()
            .filter(t -> isActiveAt(t, date))
            .toList();
    }

    /**
     * Gets the sovereign entity controlling a location at a date.
     */
    public static Optional<String> getSovereignAt(double latitude, double longitude, 
            UncertainDate date) {
        
        for (Territory t : getTerritoriesAtDate(date)) {
            if (isPointInTerritory(latitude, longitude, t)) {
                return Optional.of(t.sovereignEntity());
            }
        }
        return Optional.empty();
    }

    /**
     * Gets all border changes affecting an entity.
     */
    public static List<BorderChange> getChangesForEntity(String entityName) {
        return CHANGE_DATABASE.stream()
            .filter(c -> 
                (c.beforeTerritory() != null && 
                 c.beforeTerritory().sovereignEntity().contains(entityName)) ||
                (c.afterTerritory() != null && 
                 c.afterTerritory().sovereignEntity().contains(entityName)))
            .sorted((a, b) -> a.date().compareTo(b.date()))
            .toList();
    }

    /**
     * Calculates territorial extent (simplified area) of an entity at a date.
     */
    public static double calculateTerritorialExtent(String entityName, UncertainDate date) {
        return getTerritoriesAtDate(date).stream()
            .filter(t -> t.sovereignEntity().contains(entityName))
            .mapToDouble(BorderEvolution::approximateArea)
            .sum();
    }

    /**
     * Generates a timeline of major border changes.
     */
    public static String generateTimeline(int startYear, int endYear) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Border Evolution Timeline ===\n\n");
        
        List<BorderChange> relevant = CHANGE_DATABASE.stream()
            .filter(c -> {
                int year = getYear(c.date());
                return year >= startYear && year <= endYear;
            })
            .sorted((a, b) -> a.date().compareTo(b.date()))
            .toList();
        
        for (BorderChange change : relevant) {
            sb.append(String.format("%s: %s (%s)\n",
                change.date().toString(),
                change.description(),
                change.type().name().replace("_", " ")));
        }
        
        return sb.toString();
    }

    /**
     * Loads sample historical border data.
     */
    public static void loadSampleData() {
        // Treaty of Verdun (843) - Division of Carolingian Empire
        addBorderChange(new BorderChange(
            UncertainDate.certain(843, 8, 1),
            "Treaty of Verdun - Carolingian Empire divided",
            new Territory("Carolingian Empire", "Carolingian Dynasty",
                List.of(new double[]{48.8, 2.3}), // Simplified
                UncertainDate.circa(800), UncertainDate.certain(843, 8, 1), ""),
            new Territory("West Francia", "Charles the Bald",
                List.of(new double[]{48.8, 2.3}),
                UncertainDate.certain(843, 8, 1), UncertainDate.circa(987), "Became France"),
            ChangeType.PARTITION
        ));
        
        // Norman Conquest (1066)
        addBorderChange(new BorderChange(
            UncertainDate.certain(1066, 10, 14),
            "Norman Conquest of England",
            new Territory("Anglo-Saxon England", "House of Wessex",
                List.of(new double[]{51.5, -0.1}),
                UncertainDate.circa(927), UncertainDate.certain(1066, 10, 14), ""),
            new Territory("Norman England", "House of Normandy",
                List.of(new double[]{51.5, -0.1}),
                UncertainDate.certain(1066, 12, 25), UncertainDate.circa(1154), ""),
            ChangeType.CONQUEST
        ));
        
        // Treaty of Westphalia (1648)
        addBorderChange(new BorderChange(
            UncertainDate.certain(1648, 10, 24),
            "Peace of Westphalia - End of Thirty Years' War",
            null,
            new Territory("Swiss Confederation", "Swiss Cantons",
                List.of(new double[]{46.9, 7.4}),
                UncertainDate.certain(1648, 10, 24), null, "Independence recognized"),
            ChangeType.INDEPENDENCE
        ));
    }

    private static boolean isActiveAt(Territory t, UncertainDate date) {
        if (t.startDate() != null && date.compareTo(t.startDate()) < 0) return false;
        if (t.endDate() != null && date.compareTo(t.endDate()) > 0) return false;
        return true;
    }

    private static boolean isPointInTerritory(double lat, double lon, Territory t) {
        // Simplified: check if point is near any boundary point
        for (double[] point : t.boundaryPoints()) {
            double dist = Math.sqrt(Math.pow(lat - point[0], 2) + Math.pow(lon - point[1], 2));
            if (dist < 5.0) return true; // Within 5 degrees (very rough)
        }
        return false;
    }

    private static double approximateArea(Territory t) {
        // Very simplified area calculation
        if (t.boundaryPoints().size() < 3) return 1000; // Default
        
        // Use bounding box as rough area estimate
        double minLat = Double.MAX_VALUE, maxLat = Double.MIN_VALUE;
        double minLon = Double.MAX_VALUE, maxLon = Double.MIN_VALUE;
        
        for (double[] point : t.boundaryPoints()) {
            minLat = Math.min(minLat, point[0]);
            maxLat = Math.max(maxLat, point[0]);
            minLon = Math.min(minLon, point[1]);
            maxLon = Math.max(maxLon, point[1]);
        }
        
        return (maxLat - minLat) * (maxLon - minLon) * 111 * 111; // Rough km²
    }

    private static int getYear(UncertainDate date) {
        var earliest = date.getEarliestPossible();
        return earliest != null ? earliest.getYear() : 0;
    }
}
