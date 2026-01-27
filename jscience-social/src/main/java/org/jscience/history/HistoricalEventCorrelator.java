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

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalLong;
import org.jscience.history.time.TimeCoordinate;

/**
 * Analyzes historical events to detect patterns, causal relationships, and clusters.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class HistoricalEventCorrelator {

    private HistoricalEventCorrelator() {}

    public record CorrelatableEvent(
        String name,
        String category,
        TimeCoordinate date,
        String location,
        Map<String, Object> attributes
    ) implements Serializable {
        private static final long serialVersionUID = 2L;

        public CorrelatableEvent {
            Objects.requireNonNull(name, "Name cannot be null");
            Objects.requireNonNull(category, "Category cannot be null");
            attributes = attributes != null ? Map.copyOf(attributes) : Map.of();
        }
    }

    public record Correlation(
        CorrelatableEvent cause,
        CorrelatableEvent effect,
        long lagDays,
        double confidence,
        String hypothesis
    ) implements Serializable {
        private static final long serialVersionUID = 2L;

        public Correlation {
            Objects.requireNonNull(cause, "Cause cannot be null");
            Objects.requireNonNull(effect, "Effect cannot be null");
            Objects.requireNonNull(hypothesis, "Hypothesis cannot be null");
        }
    }

    private static final List<CausalPattern> PATTERNS = List.of(
        new CausalPattern("Volcanic Eruption", "Famine", 365, 730, 0.7, "Volcanic winter reduces crop yields"),
        new CausalPattern("Drought", "Famine", 180, 365, 0.85, "Crop failure from water shortage"),
        new CausalPattern("Famine", "Epidemic", 90, 365, 0.6, "Malnutrition weakens immune systems"),
        new CausalPattern("War", "Refugee Migration", 30, 180, 0.9, "Population displacement from conflict"),
        new CausalPattern("Monetary Debasement", "Inflation", 180, 730, 0.75, "Currency value reduction"),
        new CausalPattern("Plague", "Labor Shortage", 365, 1095, 0.8, "Population decline affects workforce"),
        new CausalPattern("Succession Crisis", "Civil War", 30, 365, 0.6, "Power vacuum leads to conflict")
    );

    private record CausalPattern(String causeCategory, String effectCategory,
            int minLagDays, int maxLagDays, double baseProbability, String mechanism) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

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
                                double confidence = pattern.baseProbability();
                                if (cause.location() != null && effect.location() != null &&
                                    cause.location().trim().equalsIgnoreCase(effect.location().trim())) {
                                    confidence *= 1.2;
                                }
                                confidence = Math.min(1.0, confidence);
                                correlations.add(new Correlation(cause, effect, lagDays, confidence, pattern.mechanism()));
                            }
                        }
                    }
                }
            }
        }
        correlations.sort((a, b) -> Double.compare(b.confidence(), a.confidence()));
        return Collections.unmodifiableList(correlations);
    }

    public static List<List<CorrelatableEvent>> findEventClusters(List<CorrelatableEvent> events, int maxGapDays) {
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
                OptionalLong gap = calculateLag(currentCluster.get(currentCluster.size() - 1).date(), event.date());
                if (gap.isPresent() && gap.getAsLong() <= maxGapDays) {
                    currentCluster.add(event);
                } else {
                    if (currentCluster.size() >= 2) clusters.add(List.copyOf(currentCluster));
                    currentCluster.clear();
                    currentCluster.add(event);
                }
            }
        }
        if (currentCluster.size() >= 2) clusters.add(List.copyOf(currentCluster));
        return Collections.unmodifiableList(clusters);
    }

    public static Map<String, Integer> eventFrequencyByCategory(List<CorrelatableEvent> events) {
        Objects.requireNonNull(events, "Events list cannot be null");
        Map<String, Integer> frequency = new HashMap<>();
        for (CorrelatableEvent e : events) frequency.merge(e.category(), 1, (a, b) -> a + b);
        return Collections.unmodifiableMap(frequency);
    }

    private static OptionalLong calculateLag(TimeCoordinate earlier, TimeCoordinate later) {
        if (earlier == null || later == null) return OptionalLong.empty();
        Instant e = earlier.toInstant();
        Instant l = later.toInstant();
        try {
            long days = java.time.Duration.between(e, l).toDays();
            return days >= 0 ? OptionalLong.of(days) : OptionalLong.empty();
        } catch (Exception ex) {
            return OptionalLong.empty();
        }
    }
}
