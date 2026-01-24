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

import org.jscience.history.temporal.TemporalCoordinate;
import org.jscience.history.temporal.FuzzyTemporalInterval;
import org.jscience.mathematics.numbers.real.Real;
import java.io.Serializable;
import java.util.*;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Simulates counterfactual historical scenarios ("What if?").
 * Analyzes historical contingency and potential alternative timelines based on causal chains.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class CounterfactualSimulator {

    private CounterfactualSimulator() {
        // Prevent instantiation
    }

    /**
     * Internal representation of a historical event with its causal dependencies.
     * 
     * @param id           unique ID
     * @param name         event name
     * @param date         fuzzy date
     * @param category     event category
     * @param importance   importance weight (0.0 to 1.0)
     * @param consequences list of dependent event IDs
     */
    @Persistent
    public record ContingencyEvent(
        @Attribute String id,
        @Attribute String name,
        @Relation(type = Relation.Type.ONE_TO_ONE) TemporalCoordinate date,
        @Attribute String category,
        @Attribute double importance,
        @Attribute List<String> consequences
    ) implements Serializable {
        private static final long serialVersionUID = 1L;

        public ContingencyEvent {
            Objects.requireNonNull(id, "ID cannot be null");
            Objects.requireNonNull(name, "Name cannot be null");
            Objects.requireNonNull(date, "Date cannot be null");
            consequences = consequences != null ? List.copyOf(consequences) : List.of();
        }
    }

    /**
     * Defines a specific counterfactual scenario starting from a divergence point.
     * 
     * @param name                scenario name
     * @param description         scenario summary
     * @param divergencePoint     the original event being changed
     * @param alternativeOutcome  the new outcome for the divergence point
     * @param affectedEvents       explicitly list of known affected events (optional)
     * @param changedConsequences mapping of event IDs to their new alternative outcomes
     */
    @Persistent
    public record CounterfactualScenario(
        @Attribute String name,
        @Attribute String description,
        @Relation(type = Relation.Type.MANY_TO_ONE) ContingencyEvent divergencePoint,
        @Attribute String alternativeOutcome,
        @Attribute List<String> affectedEvents,
        @Attribute Map<String, String> changedConsequences
    ) implements Serializable {
        private static final long serialVersionUID = 1L;

        public CounterfactualScenario {
            Objects.requireNonNull(name, "Scenario name cannot be null");
            Objects.requireNonNull(divergencePoint, "Divergence point cannot be null");
            affectedEvents = affectedEvents != null ? List.copyOf(affectedEvents) : List.of();
            changedConsequences = changedConsequences != null ? Map.copyOf(changedConsequences) : Map.of();
        }
    }

    /**
     * The analytical results of a counterfactual simulation.
     * 
     * @param scenario            the scenario tested
     * @param butterfliedEvents   list of events that changed
     * @param unchangedEvents     list of events that remained the same
     * @param historicalDivergence measure of divergence (0.0 to 1.0)
     * @param narrativeSummary    generated text analysis
     */
    @Persistent
    public record SimulationResult(
        @Relation(type = Relation.Type.MANY_TO_ONE) CounterfactualScenario scenario,
        @Attribute List<String> butterfliedEvents,
        @Attribute List<String> unchangedEvents,
        @Attribute double historicalDivergence,
        @Attribute String narrativeSummary
    ) implements Serializable {
        private static final long serialVersionUID = 1L;

        public SimulationResult {
            Objects.requireNonNull(scenario, "Scenario cannot be null");
            butterfliedEvents = butterfliedEvents != null ? List.copyOf(butterfliedEvents) : List.of();
            unchangedEvents = unchangedEvents != null ? List.copyOf(unchangedEvents) : List.of();
        }
    }

    private static final List<ContingencyEvent> EVENT_DATABASE = Collections.synchronizedList(new ArrayList<>());

    /**
     * Registers a contingency event in the simulation database.
     * 
     * @param event the event to add
     * @throws NullPointerException if event is null
     */
    public static void addEvent(ContingencyEvent event) {
        EVENT_DATABASE.add(Objects.requireNonNull(event, "Event cannot be null"));
    }

    /**
     * Simulates the cascade effects of a counterfactual scenario.
     * 
     * @param scenario the scenario to simulate
     * @return simulation results
     * @throws NullPointerException if scenario is null
     */
    public static SimulationResult simulate(CounterfactualScenario scenario) {
        Objects.requireNonNull(scenario, "Scenario cannot be null");
        Set<String> butterflied = new HashSet<>();
        Set<String> unchanged = new HashSet<>();
        
        // Find all events that depend on the divergence point
        cascadeEffects(scenario.divergencePoint().id(), butterflied);
        
        // Find unchanged events (before divergence or unrelated)
        synchronized (EVENT_DATABASE) {
            for (ContingencyEvent e : EVENT_DATABASE) {
                if (!butterflied.contains(e.id()) && !e.id().equals(scenario.divergencePoint().id())) {
                    // Check if before divergence
                    if (e.date().compareTo(scenario.divergencePoint().date()) < 0) {
                        unchanged.add(e.id());
                    } else if (!isConnected(e.id(), scenario.divergencePoint().id())) {
                        unchanged.add(e.id());
                    }
                }
            }
        }
        
        // Calculate divergence factor
        double divergence = (double) butterflied.size() / 
            Math.max(1, butterflied.size() + unchanged.size());
        
        // Generate narrative
        String narrative = generateNarrative(scenario, butterflied);
        
        return new SimulationResult(
            scenario,
            new ArrayList<>(butterflied),
            new ArrayList<>(unchanged),
            divergence,
            narrative
        );
    }

    /**
     * Calculates the historical contingency score of a specific event.
     * 
     * @param eventId unique ID of the event
     * @return contingency score as a {@link Real} number
     * @throws NullPointerException if eventId is null
     */
    public static Real calculateContingency(String eventId) {
        Objects.requireNonNull(eventId, "Event ID cannot be null");
        ContingencyEvent event = findEvent(eventId);
        if (event == null) return Real.ZERO;
        
        Set<String> dependents = new HashSet<>();
        cascadeEffects(eventId, dependents);
        
        double contingency = event.importance() * dependents.size() / 
            Math.max(1, EVENT_DATABASE.size());
        
        return Real.of(contingency);
    }

    /**
     * Suggests plausible counterfactual outcomes for a given event category.
     * 
     * @param eventId unique ID of the event
     * @return unmodifiable list of counterfactual suggestions
     * @throws NullPointerException if eventId is null
     */
    public static List<String> suggestCounterfactuals(String eventId) {
        Objects.requireNonNull(eventId, "Event ID cannot be null");
        ContingencyEvent event = findEvent(eventId);
        if (event == null) return Collections.emptyList();
        
        List<String> suggestions = new ArrayList<>();
        String category = event.category().toLowerCase().trim();
        
        if (category.contains("battle") || category.contains("war")) {
            suggestions.addAll(Arrays.asList(
                "What if the opposing side won?",
                "What if the battle never occurred (diplomacy succeeded)?",
                "What if the key commander died before the battle?"
            ));
        } else if (category.contains("invention") || category.contains("discovery")) {
            suggestions.addAll(Arrays.asList(
                "What if the invention was delayed by 50 years?",
                "What if a different inventor made the discovery?",
                "What if the invention was suppressed?"
            ));
        } else if (category.contains("assassination") || category.contains("death")) {
            suggestions.addAll(Arrays.asList(
                "What if the target survived?",
                "What if the assassination succeeded earlier?"
            ));
        } else if (category.contains("treaty") || category.contains("agreement")) {
            suggestions.addAll(Arrays.asList(
                "What if negotiations failed?",
                "What if the terms were more/less favorable?"
            ));
        }
        
        suggestions.add("What if this event never happened?");
        suggestions.add("What if this event happened 10 years earlier/later?");
        
        return Collections.unmodifiableList(suggestions);
    }

    /**
     * Populates the database with sample historical dataset for simulation testing.
     */
    public static void loadSampleData() {
        addEvent(new ContingencyEvent("roman_republic", "Founding of Roman Republic",
            FuzzyTemporalInterval.circa(-509), "Political", 0.9, Arrays.asList("punic_wars", "roman_empire")));
        
        addEvent(new ContingencyEvent("punic_wars", "Punic Wars",
            FuzzyTemporalInterval.circa(-264), "War", 0.85, Collections.singletonList("roman_empire")));
        
        addEvent(new ContingencyEvent("roman_empire", "Roman Empire established",
            FuzzyTemporalInterval.circa(-27), "Political", 0.95, Arrays.asList("christianity_rise", "fall_of_rome")));
        
        addEvent(new ContingencyEvent("christianity_rise", "Rise of Christianity",
            FuzzyTemporalInterval.circa(30), "Religious", 0.95, Arrays.asList("constantine", "medieval_church")));
        
        addEvent(new ContingencyEvent("fall_of_rome", "Fall of Western Roman Empire",
            FuzzyTemporalInterval.of(476, 9, 4), "Political", 0.9, Collections.singletonList("medieval_period")));
        
        addEvent(new ContingencyEvent("printing_press", "Gutenberg Printing Press",
            FuzzyTemporalInterval.circa(1440), "Technology", 0.9, Arrays.asList("reformation", "scientific_rev")));
        
        addEvent(new ContingencyEvent("reformation", "Protestant Reformation",
            FuzzyTemporalInterval.of(1517, 10, 31), "Religious", 0.85, Collections.singletonList("30_years_war")));
    }

    private static void cascadeEffects(String eventId, Set<String> affected) {
        ContingencyEvent event = findEvent(eventId);
        if (event == null) return;
        
        for (String consequenceId : event.consequences()) {
            if (affected.add(consequenceId)) {
                cascadeEffects(consequenceId, affected);
            }
        }
    }

    private static boolean isConnected(String eventId, String targetId) {
        Set<String> affected = new HashSet<>();
        cascadeEffects(targetId, affected);
        return affected.contains(eventId);
    }

    private static ContingencyEvent findEvent(String id) {
        synchronized (EVENT_DATABASE) {
            return EVENT_DATABASE.stream()
                .filter(e -> e.id().equals(id))
                .findFirst()
                .orElse(null);
        }
    }

    private static String generateNarrative(CounterfactualScenario scenario, Set<String> butterflied) {
        StringBuilder sb = new StringBuilder();
        sb.append("COUNTERFACTUAL ANALYSIS: ").append(scenario.name()).append("\n\n");
        sb.append("Divergence point: ").append(scenario.divergencePoint().name()).append("\n");
        sb.append("Alternative: ").append(scenario.alternativeOutcome()).append("\n\n");
        sb.append("Affected historical events: ").append(butterflied.size()).append("\n");
        
        for (String eventId : butterflied) {
            ContingencyEvent e = findEvent(eventId);
            if (e != null) {
                sb.append("  - ").append(e.name());
                if (scenario.changedConsequences().containsKey(eventId)) {
                    sb.append(" -> ").append(scenario.changedConsequences().get(eventId));
                }
                sb.append("\n");
            }
        }
        
        return sb.toString();
    }
}
