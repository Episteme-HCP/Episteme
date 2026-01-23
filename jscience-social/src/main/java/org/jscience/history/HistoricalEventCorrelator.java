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

import org.jscience.history.time.UncertainDate;
import java.io.Serializable;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Analyzes historical events to detect patterns, causal relationships, and clusters.
 * Provides statistical correlations based on known historical causality patterns.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class HistoricalEventCorrelator {

    private HistoricalEventCorrelator() {
        // Prevent instantiation
    }

    /**
     * Local event representation for correlation analysis.
     * 
     * @param name       event name
     * @param category   event category
     * @param date       event date (uncertain)
     * @param location   geographical location name
     * @param attributes additional metadata
     */
    public record CorrelatableEvent(
        String name,
        String category,
        UncertainDate date,
        String location,
        Map<String, Object> attributes
    ) implements Serializable {
        private static final long serialVersionUID = 1L;

        public CorrelatableEvent {
            Objects.requireNonNull(name, "Name cannot be null");
            Objects.requireNonNull(category, "Category cannot be null");
            // Attributes map should be immutable for safety if possible
            attributes = attributes != null ? Map.copyOf(attributes) : Map.of();
        }
    }

    /**
     * Represents a detected correlation between two historical events.
     * 
     * @param cause      the initiating event
     * @param effect     the resulting event
     * @param lagDays    estimated days between cause and effect
     * @param confidence statistical confidence score (0.0 to 1.0)
     * @param hypothesis description of the causal mechanism
     */
    public record Correlation(
        CorrelatableEvent cause,
        CorrelatableEvent effect,
        long lagDays,
        double confidence,
        String hypothesis
    ) implements Serializable {
        private static final long serialVersionUID = 1L;

        public Correlation {
            Objects.requireNonNull(cause, "Cause cannot be null");
            Objects.requireNonNull(effect, "Effect cannot be null");
            Objects.requireNonNull(hypothesis, "Hypothesis cannot be null");
        }
    }

    // Known causal patterns used for detection
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
            int minLagDays, int maxLagDays, double baseProbability, String mechanism) implements Serializable {
        private static final long serialVersionUID = 1L;
    }

    /**
     * Finds potential correlations between a list of events.
     * 
     * @param events list of historical events to analyze
     * @return sorted list of correlations by confidence
     * @throws NullPointerException if events list is null
     */
    public static List<Correlation> findCorrelations(List<CorrelatableEvent> events) {
        Objects.requireNonNull(events, "Events list cannot be null");
        List<Correlation> correlations = new ArrayList<>();
        
        for (CorrelatableEvent cause : events) {
            for (CorrelatableEvent effect : events) {
                if (cause.equals(effect)) continue;
                
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
                                    cause.location().trim().equalsIgnoreCase(effect.location().trim())) {
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
        return Collections.unmodifiableList(correlations);
    }

    /**
     * Detects clusters of events (multiple events occurring in a short timeframe).
     * 
     * @param events events to group
     * @param maxGapDays maximum days between consecutive events in a cluster
     * @return list of event clusters
     * @throws NullPointerException if events list is null
     */
    public static List<List<CorrelatableEvent>> findEventClusters(
            List<CorrelatableEvent> events, int maxGapDays) {
        Objects.requireNonNull(events, "Events list cannot be null");
        
        List<CorrelatableEvent> sorted = new ArrayList<>(events);
        sorted.sort((a, b) -> {
            if (a.date() == null && b.date() == null) return 0;
            if (a.date() == null) return 1;
            if (b.date() == null) return -1;
            return a.date().compareTo(b.date());
        });
        
        List<List<CorrelatableEvent>> clusters = new ArrayList<>();
        List<CorrelatableEvent> currentCluster = new ArrayList<>();
        
        for (CorrelatableEvent event : sorted) {
            if (currentCluster.isEmpty()) {
                currentCluster.add(event);
            } else {
                OptionalLong gap = calculateLag(
                    currentCluster.get(currentCluster.size() - 1).date(),
                    event.date()
                );
                
                if (gap.isPresent() && gap.getAsLong() <= maxGapDays) {
                    currentCluster.add(event);
                } else {
                    if (currentCluster.size() >= 2) {
                        clusters.add(List.copyOf(currentCluster));
                    }
                    currentCluster.clear();
                    currentCluster.add(event);
                }
            }
        }
        
        if (currentCluster.size() >= 2) {
            clusters.add(List.copyOf(currentCluster));
        }
        
        return Collections.unmodifiableList(clusters);
    }

    /**
     * Calculates the frequency of event categories.
     * 
     * @param events events to count
     * @return map of category to frequency
     * @throws NullPointerException if events list is null
     */
    public static Map<String, Integer> eventFrequencyByCategory(List<CorrelatableEvent> events) {
        Objects.requireNonNull(events, "Events list cannot be null");
        Map<String, Integer> frequency = new HashMap<>();
        for (CorrelatableEvent e : events) {
            frequency.merge(e.category(), 1, Integer::sum);
        }
        return Collections.unmodifiableMap(frequency);
    }

    private static OptionalLong calculateLag(UncertainDate earlier, UncertainDate later) {
        if (earlier == null || later == null) return OptionalLong.empty();
        
        var latestEarlier = earlier.getLatestOfMidPoints(); // Use midpoint for more stable correlation? 
        // Or keep getLatestPossible to be conservative if effect must follow cause.
        // Original code used getLatestPossible for earlier and getEarliestPossible for later.
        
        var e = earlier.getLatestPossible();
        var l = later.getEarliestPossible();
        
        if (e == null || l == null) return OptionalLong.empty();
        
        try {
            long days = ChronoUnit.DAYS.between(e, l);
            return days >= 0 ? OptionalLong.of(days) : OptionalLong.empty();
        } catch (Exception ex) {
            return OptionalLong.empty();
        }
    }
}
