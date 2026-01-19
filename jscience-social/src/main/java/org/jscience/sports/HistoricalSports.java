package org.jscience.sports;

import org.jscience.history.time.UncertainDate;
import java.util.*;

/**
 * Models historical sports and their ancestors.
 */
public final class HistoricalSports {

    private HistoricalSports() {}

    public record HistoricalSport(
        String name,
        String modernDescendant,
        UncertainDate originPeriod,
        String region,
        List<String> rules,
        Map<String, String> equipment,
        String performanceMetric
    ) {}

    public static final List<HistoricalSport> CATALOG = List.of(
        new HistoricalSport(
            "Soule", "Rugby/Football", 
            UncertainDate.circa(1147), "France/England",
            List.of("Two teams", "Ball must reach opponent's goal", "Few restrictions on contact"),
            Map.of("Ball", "Leather stuffed with hay", "Field", "Village to village"),
            "Goals scored"
        ),
        new HistoricalSport(
            "Cuju", "Football",
            UncertainDate.circa(-206), "China",
            List.of("Ball through goal opening", "No hands", "Teams of 12-16"),
            Map.of("Ball", "Leather with feathers", "Goal", "Silk net on bamboo poles"),
            "Goals scored"
        ),
        new HistoricalSport(
            "Pankration", "MMA",
            UncertainDate.circa(-648), "Greece",
            List.of("No biting", "No eye gouging", "Submission or knockout wins"),
            Map.of("Arena", "Sand pit", "Protection", "None"),
            "Victories in tournament"
        ),
        new HistoricalSport(
            "Ullamaliztli", "Basketball (conceptually)",
            UncertainDate.circa(-1400), "Mesoamerica",
            List.of("Ball through stone ring", "Cannot use hands/feet", "Hip/elbow only"),
            Map.of("Ball", "Solid rubber 4kg", "Ring", "Stone at 9m height"),
            "Ring passes"
        ),
        new HistoricalSport(
            "Jousting", "Equestrian sports",
            UncertainDate.circa(1066), "Europe",
            List.of("Unhorse opponent", "Three passes", "Points for hits"),
            Map.of("Lance", "Wooden 3m", "Armor", "Full plate"),
            "Points / Unhorses"
        ),
        new HistoricalSport(
            "Episkyros", "Rugby",
            UncertainDate.circa(-800), "Greece",
            List.of("Two teams", "Move ball past opponent's line", "Physical contact allowed"),
            Map.of("Ball", "Leather with sand/wool"),
            "Line crosses"
        )
    );

    /**
     * Finds historical ancestors of a modern sport.
     */
    public static List<HistoricalSport> findAncestors(String modernSport) {
        return CATALOG.stream()
            .filter(s -> s.modernDescendant().toLowerCase().contains(modernSport.toLowerCase()))
            .toList();
    }

    /**
     * Simulates a historical match result using period-appropriate randomness.
     */
    public static Map<String, Integer> simulateMatch(HistoricalSport sport, String team1, String team2) {
        Random random = new Random();
        Map<String, Integer> scores = new HashMap<>();
        
        // Historical sports typically had lower scores
        int maxScore = sport.name().equals("Ullamaliztli") ? 3 : 10;
        scores.put(team1, random.nextInt(maxScore + 1));
        scores.put(team2, random.nextInt(maxScore + 1));
        
        return scores;
    }
}
