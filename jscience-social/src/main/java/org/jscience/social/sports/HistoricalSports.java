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

package org.jscience.social.sports;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.jscience.social.history.time.FuzzyTimePoint;
import org.jscience.social.history.time.TimeCoordinate;

/**
 * Models ancestral sports and provides a catalog of historical physical activities.
 * Used for anthropological and historical sports research.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class HistoricalSports {

    private HistoricalSports() {}

    /** Data model for a historical sport. */
    public record HistoricalSport(
        String name,
        String modernDescendant,
        TimeCoordinate originPeriod,
        String region,
        List<String> rules,
        Map<String, String> equipment,
        String performanceMetric
    ) implements Serializable {}

    /** Reference catalog of historical sports. */
    public static final List<HistoricalSport> CATALOG = List.of(
        new HistoricalSport(
            "Soule", "Rugby/Football", 
            FuzzyTimePoint.circa(1147), "France/England",
            List.of("Two teams", "Ball must reach opponent's goal", "Few restrictions on contact"),
            Map.of("Ball", "Leather stuffed with hay", "Field", "Village to village"),
            "Goals scored"
        ),
        new HistoricalSport(
            "Cuju", "Football",
            FuzzyTimePoint.circa(-206), "China",
            List.of("Ball through goal opening", "No hands", "Teams of 12-16"),
            Map.of("Ball", "Leather with feathers", "Goal", "Silk net on bamboo poles"),
            "Goals scored"
        ),
        new HistoricalSport(
            "Pankration", "MMA",
            FuzzyTimePoint.circa(-648), "Greece",
            List.of("No biting", "No eye gouging", "Submission or knockout wins"),
            Map.of("Arena", "Sand pit", "Protection", "None"),
            "Victories in tournament"
        ),
        new HistoricalSport(
            "Ullamaliztli", "Basketball",
            FuzzyTimePoint.circa(-1400), "Mesoamerica",
            List.of("Ball through stone ring", "Cannot use hands/feet", "Hip/elbow only"),
            Map.of("Ball", "Solid rubber 4kg", "Ring", "Stone at 9m height"),
            "Ring passes"
        )
    );

    /**
     * Finds historical ancestors of a modern sport name.
     * 
     * @param modernSport query string
     * @return list of matching historical ancestors
     */
    public static List<HistoricalSport> findAncestors(String modernSport) {
        if (modernSport == null) return List.of();
        return CATALOG.stream()
            .filter(s -> s.modernDescendant().toLowerCase().contains(modernSport.toLowerCase()))
            .toList();
    }

    /**
     * Simulates a match result using period-appropriate scoring models.
     */
    public static Map<String, Integer> simulateMatch(HistoricalSport sport, String team1, String team2) {
        Random random = new Random();
        Map<String, Integer> scores = new HashMap<>();
        
        int maxScore = "Ullamaliztli".equals(sport.name()) ? 3 : 10;
        scores.put(team1, random.nextInt(maxScore + 1));
        scores.put(team2, random.nextInt(maxScore + 1));
        
        return scores;
    }
}

