package org.jscience.history;

import org.jscience.history.time.UncertainDate;
import java.util.*;

/**
 * Correlates historical events to detect patterns and causal relationships.
 */
public final class HistoricalEventCorrelator {

    private HistoricalEventCorrelator() {}

    public record HistoricalEvent(
        String name,
        String category,
        UncertainDate date,
        String location,
        Map<String, Object> attributes
    ) {}

    public record Correlation(
        HistoricalEvent cause,
        HistoricalEvent effect,
        long lagDays,
        double confidence,
        String hypothesis
    ) {}

    // Known causal patterns
    private static final List<CausalPattern> PATTERNS = List.of(
        new CausalPattern("Volcanic Eruption", "Famine", 365, 730, 0.7,
            "Volcanic winter reduces crop yields"),
        new CausalPattern("Drought", "Famine", 180, 365, 0.85,
            "Crop failure from water shortage"),
        new CausalPattern("Famine", "Epidemic", 90, 365, 0.6,
            "Malnutrition weakens immune systems"),
        new CausalPattern("War", "Refugee Migration", 30, 180, 0.9,
            "Population displacement from conflict"),
        new CausalPattern("Monetary Debasement", "Inflation", 180, 730, 0.75,
            "Currency value reduction"),
        new CausalPattern("Plague", "Labor Shortage", 365, 1095, 0.8,
            "Population decline affects workforce"),
        new CausalPattern("Succession Crisis", "Civil War", 30, 365, 0.6,
            "Power vacuum leads to conflict")
    );

    private record CausalPattern(String causeCategory, String effectCategory,
            int minLagDays, int maxLagDays, double baseProbability, String mechanism) {}

    /**
     * Finds potential correlations between events.
     */
    public static List<Correlation> findCorrelations(List<HistoricalEvent> events) {
        List<Correlation> correlations = new ArrayList<>();
        
        for (HistoricalEvent cause : events) {
            for (HistoricalEvent effect : events) {
                if (cause == effect) continue;
                
                for (CausalPattern pattern : PATTERNS) {
                    if (cause.category().equalsIgnoreCase(pattern.causeCategory()) &&
                        effect.category().equalsIgnoreCase(pattern.effectCategory())) {
                        
                        OptionalLong lag = calculateLag(cause.date(), effect.date());
                        if (lag.isPresent()) {
                            long lagDays = lag.getAsLong();
                            if (lagDays >= pattern.minLagDays() && lagDays <= pattern.maxLagDays()) {
                                
                                // Adjust confidence based on location match
                                double confidence = pattern.baseProbability();
                                if (cause.location() != null && effect.location() != null &&
                                    cause.location().equalsIgnoreCase(effect.location())) {
                                    confidence *= 1.2;
                                }
                                confidence = Math.min(1.0, confidence);
                                
                                correlations.add(new Correlation(
                                    cause, effect, lagDays, confidence, pattern.mechanism()
                                ));
                            }
                        }
                    }
                }
            }
        }
        
        correlations.sort((a, b) -> Double.compare(b.confidence(), a.confidence()));
        return correlations;
    }

    /**
     * Detects clusters of events (multiple events in short timeframe).
     */
    public static List<List<HistoricalEvent>> findEventClusters(
            List<HistoricalEvent> events, int maxGapDays) {
        
        List<HistoricalEvent> sorted = new ArrayList<>(events);
        sorted.sort((a, b) -> {
            if (a.date() == null) return 1;
            if (b.date() == null) return -1;
            return a.date().compareTo(b.date());
        });
        
        List<List<HistoricalEvent>> clusters = new ArrayList<>();
        List<HistoricalEvent> currentCluster = new ArrayList<>();
        
        for (int i = 0; i < sorted.size(); i++) {
            if (currentCluster.isEmpty()) {
                currentCluster.add(sorted.get(i));
            } else {
                OptionalLong gap = calculateLag(
                    currentCluster.get(currentCluster.size() - 1).date(),
                    sorted.get(i).date()
                );
                
                if (gap.isPresent() && gap.getAsLong() <= maxGapDays) {
                    currentCluster.add(sorted.get(i));
                } else {
                    if (currentCluster.size() >= 2) {
                        clusters.add(new ArrayList<>(currentCluster));
                    }
                    currentCluster.clear();
                    currentCluster.add(sorted.get(i));
                }
            }
        }
        
        if (currentCluster.size() >= 2) {
            clusters.add(currentCluster);
        }
        
        return clusters;
    }

    /**
     * Calculates frequency of event categories over time.
     */
    public static Map<String, Integer> eventFrequencyByCategory(List<HistoricalEvent> events) {
        Map<String, Integer> frequency = new HashMap<>();
        for (HistoricalEvent e : events) {
            frequency.merge(e.category(), 1, Integer::sum);
        }
        return frequency;
    }

    private static OptionalLong calculateLag(UncertainDate earlier, UncertainDate later) {
        if (earlier == null || later == null) return OptionalLong.empty();
        
        var e = earlier.getLatestPossible();
        var l = later.getEarliestPossible();
        
        if (e == null || l == null) return OptionalLong.empty();
        
        long days = java.time.temporal.ChronoUnit.DAYS.between(e, l);
        return days >= 0 ? OptionalLong.of(days) : OptionalLong.empty();
    }
}
