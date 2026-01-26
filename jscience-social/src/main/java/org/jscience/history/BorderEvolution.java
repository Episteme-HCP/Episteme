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

package org.jscience.history;

import org.jscience.history.time.TimeCoordinate;
import org.jscience.history.time.FuzzyTimePoint;
import java.time.ZoneOffset;
import java.io.Serializable;
import java.util.*;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Models and visualizes the evolution of political borders over historical time.
 * Provides tools for spatial-temporal queries regarding sovereignty and territorial changes.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class BorderEvolution {

    private BorderEvolution() {
        // Prevent instantiation
    }

    /**
     * Represents a defined geographical territory under specific sovereignty.
     * 
     * @param name            territory name
     * @param sovereignEntity sovereign entity name
     * @param boundaryPoints  list of coordinates (latitude, longitude)
     * @param startDate       start of sovereignty (fuzzy)
     * @param endDate         end of sovereignty (fuzzy)
     * @param notes           additional metadata
     */
    @Persistent
    public record Territory(
        @Attribute String name,
        @Attribute String sovereignEntity,
        @Attribute List<double[]> boundaryPoints,
        @Relation(type = Relation.Type.ONE_TO_ONE) TimeCoordinate startDate,
        @Relation(type = Relation.Type.ONE_TO_ONE) TimeCoordinate endDate,
        @Attribute String notes
    ) implements Serializable {
        private static final long serialVersionUID = 1L;

        public Territory {
            Objects.requireNonNull(name, "Territory name cannot be null");
            Objects.requireNonNull(sovereignEntity, "Sovereign entity cannot be null");
            boundaryPoints = boundaryPoints != null ? List.copyOf(boundaryPoints) : List.of();
        }
    }

    /**
     * Represents a specific event that changed territorial boundaries.
     * 
     * @param date            date of the change (fuzzy)
     * @param description     textual description
     * @param beforeTerritory state before the change
     * @param afterTerritory  state after the change
     * @param type           category of border modification
     */
    @Persistent
    public record BorderChange(
        @Relation(type = Relation.Type.ONE_TO_ONE) TimeCoordinate date,
        @Attribute String description,
        @Relation(type = Relation.Type.MANY_TO_ONE) Territory beforeTerritory,
        @Relation(type = Relation.Type.MANY_TO_ONE) Territory afterTerritory,
        @Attribute ChangeType type
    ) implements Serializable {
        private static final long serialVersionUID = 1L;

        public BorderChange {
            Objects.requireNonNull(date, "Change date cannot be null");
            Objects.requireNonNull(description, "Description cannot be null");
            Objects.requireNonNull(type, "Change type cannot be null");
        }
    }

    /**
     * Types of events that cause border modifications.
     */
    public enum ChangeType {
        CONQUEST, CESSION, PURCHASE, UNION, DISSOLUTION, 
        TREATY, INDEPENDENCE, PARTITION, ANNEXATION
    }

    private static final List<Territory> TERRITORY_DATABASE = Collections.synchronizedList(new ArrayList<>());
    private static final List<BorderChange> CHANGE_DATABASE = Collections.synchronizedList(new ArrayList<>());

    /**
     * Registers a territory in the global historical database.
     * 
     * @param territory the territory to add
     * @throws NullPointerException if territory is null
     */
    public static void addTerritory(Territory territory) {
        TERRITORY_DATABASE.add(Objects.requireNonNull(territory, "Territory cannot be null"));
    }

    /**
     * Registers a border change in the global historical database.
     * 
     * @param change the border change event to add
     * @throws NullPointerException if change is null
     */
    public static void addBorderChange(BorderChange change) {
        CHANGE_DATABASE.add(Objects.requireNonNull(change, "BorderChange cannot be null"));
    }

    /**
     * Retrieves all territories that were active at a specific historical date.
     * 
     * @param date the date to query
     * @return unmodifiable list of active territories
     * @throws NullPointerException if date is null
     */
    public static List<Territory> getTerritoriesAtDate(TimeCoordinate date) {
        Objects.requireNonNull(date, "Date cannot be null");
        synchronized (TERRITORY_DATABASE) {
            return TERRITORY_DATABASE.stream()
                .filter(t -> isActiveAt(t, date))
                .toList();
        }
    }

    /**
     * Determines which sovereign entity controlled a specific location at a given date.
     * 
     * @param latitude  geographical latitude
     * @param longitude geographical longitude
     * @param date      historical date
     * @return name of the sovereign entity, if found as an {@link Optional}
     * @throws NullPointerException if date is null
     */
    public static Optional<String> getSovereignAt(double latitude, double longitude, 
            TimeCoordinate date) {
        Objects.requireNonNull(date, "Date cannot be null");
        for (Territory t : getTerritoriesAtDate(date)) {
            if (isPointInTerritory(latitude, longitude, t)) {
                return Optional.of(t.sovereignEntity());
            }
        }
        return Optional.empty();
    }

    /**
     * Retrieves all recorded border changes affecting a specific political entity.
     * 
     * @param entityName the name of the entity
     * @return unmodifiable sorted list of border changes
     * @throws NullPointerException if entityName is null
     */
    public static List<BorderChange> getChangesForEntity(String entityName) {
        Objects.requireNonNull(entityName, "Entity name cannot be null");
        String finalName = entityName.trim();
        synchronized (CHANGE_DATABASE) {
            return CHANGE_DATABASE.stream()
                .filter(c -> 
                    (c.beforeTerritory() != null && 
                     c.beforeTerritory().sovereignEntity().contains(finalName)) ||
                    (c.afterTerritory() != null && 
                     c.afterTerritory().sovereignEntity().contains(finalName)))
                .sorted(Comparator.comparing(BorderChange::date))
                .toList();
        }
    }

    /**
     * Calculates the total territorial extent (approx. area in km²) of an entity at a given date.
     * 
     * @param entityName the name of the entity
     * @param date       historical date
     * @return approximate area in km²
     * @throws NullPointerException if entityName or date is null
     */
    public static double calculateTerritorialExtent(String entityName, TimeCoordinate date) {
        Objects.requireNonNull(entityName, "Entity name cannot be null");
        Objects.requireNonNull(date, "Date cannot be null");
        String finalName = entityName.trim();
        return getTerritoriesAtDate(date).stream()
            .filter(t -> t.sovereignEntity().contains(finalName))
            .mapToDouble(BorderEvolution::approximateArea)
            .sum();
    }

    /**
     * Generates a human-readable timeline of border changes within a year range.
     * 
     * @param startYear starting year
     * @param endYear   ending year
     * @return formatted timeline string
     */
    public static String generateTimeline(int startYear, int endYear) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Border Evolution Timeline [").append(startYear).append(" to ").append(endYear).append("] ===\n\n");
        
        List<BorderChange> relevant;
        synchronized (CHANGE_DATABASE) {
            relevant = CHANGE_DATABASE.stream()
                .filter(c -> {
                    int year = getYear(c.date());
                    return year >= startYear && year <= endYear;
                })
                .sorted(Comparator.comparing(BorderChange::date))
                .toList();
        }
        
        for (BorderChange change : relevant) {
            sb.append(String.format("%s: %s (%s)\n",
                change.date().toString(),
                change.description(),
                change.type().name().replace("_", " ").toLowerCase()));
        }
        
        return sb.toString();
    }

    /**
     * Populates the database with sample historical territorial data.
     */
    public static void loadSampleData() {
        // Treaty of Verdun (843) - Division of Carolingian Empire
        addBorderChange(new BorderChange(
            FuzzyTimePoint.of(843, 8, 1),
            "Treaty of Verdun - Carolingian Empire divided",
            new Territory("Carolingian Empire", "Carolingian Dynasty",
                List.of(new double[]{48.8, 2.3}),
                FuzzyTimePoint.circa(800), FuzzyTimePoint.of(843, 8, 1), ""),
            new Territory("West Francia", "Charles the Bald",
                List.of(new double[]{48.8, 2.3}),
                FuzzyTimePoint.of(843, 8, 1), FuzzyTimePoint.circa(987), "Became France"),
            ChangeType.PARTITION
        ));
        
        // Norman Conquest (1066)
        addBorderChange(new BorderChange(
            FuzzyTimePoint.of(1066, 10, 14),
            "Norman Conquest of England",
            new Territory("Anglo-Saxon England", "House of Wessex",
                List.of(new double[]{51.5, -0.1}),
                FuzzyTimePoint.circa(927), FuzzyTimePoint.of(1066, 10, 14), ""),
            new Territory("Norman England", "House of Normandy",
                List.of(new double[]{51.5, -0.1}),
                FuzzyTimePoint.of(1066, 12, 25), FuzzyTimePoint.circa(1154), ""),
            ChangeType.CONQUEST
        ));
        
        // Treaty of Westphalia (1648)
        addBorderChange(new BorderChange(
            FuzzyTimePoint.of(1648, 10, 24),
            "Peace of Westphalia - End of Thirty Years' War",
            null,
            new Territory("Swiss Confederation", "Swiss Cantons",
                List.of(new double[]{46.9, 7.4}),
                FuzzyTimePoint.of(1648, 10, 24), null, "Independence recognized"),
            ChangeType.INDEPENDENCE
        ));
    }

    private static boolean isActiveAt(Territory t, TimeCoordinate date) {
        if (t.startDate() != null && date.compareTo(t.startDate()) < 0) return false;
        if (t.endDate() != null && date.compareTo(t.endDate()) > 0) return false;
        return true;
    }

    private static boolean isPointInTerritory(double lat, double lon, Territory t) {
        for (double[] point : t.boundaryPoints()) {
            double dist = Math.sqrt(Math.pow(lat - point[0], 2) + Math.pow(lon - point[1], 2));
            if (dist < 5.0) return true; // Loose proximity check
        }
        return false;
    }

    private static double approximateArea(Territory t) {
        if (t.boundaryPoints().isEmpty()) return 1000.0;
        
        double minLat = Double.MAX_VALUE, maxLat = -Double.MAX_VALUE;
        double minLon = Double.MAX_VALUE, maxLon = -Double.MAX_VALUE;
        
        for (double[] point : t.boundaryPoints()) {
            minLat = Math.min(minLat, point[0]);
            maxLat = Math.max(maxLat, point[0]);
            minLon = Math.min(minLon, point[1]);
            maxLon = Math.max(maxLon, point[1]);
        }
        
        if (minLat == Double.MAX_VALUE) return 1000.0;
        
        return (maxLat - minLat) * (maxLon - minLon) * 111.0 * 111.0;
    }

    private static int getYear(TimeCoordinate date) {
        return date.toInstant().atZone(ZoneOffset.UTC).getYear();
    }
}
