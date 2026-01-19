package org.jscience.history;

import org.jscience.history.time.UncertainDate;
import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Simulates counterfactual historical scenarios ("What if?").
 */
public final class CounterfactualSimulator {

    private CounterfactualSimulator() {}

    public record HistoricalEvent(
        String id,
        String name,
        UncertainDate date,
        String category,
        double importance,  // 0-1
        List<String> consequences  // IDs of events that depend on this
    ) {}

    public record CounterfactualScenario(
        String name,
        String description,
        HistoricalEvent divergencePoint,
        String alternativeOutcome,
        List<String> affectedEvents,
        Map<String, String> changedConsequences
    ) {}

    public record SimulationResult(
        CounterfactualScenario scenario,
        List<String> butterfliedEvents,
        List<String> unchangedEvents,
        double historicalDivergence,  // 0-1
        String narrativeSummary
    ) {}

    private static final List<HistoricalEvent> EVENT_DATABASE = new ArrayList<>();

    /**
     * Adds an event to the database.
     */
    public static void addEvent(HistoricalEvent event) {
        EVENT_DATABASE.add(event);
    }

    /**
     * Simulates the cascade effects of a counterfactual.
     */
    public static SimulationResult simulate(CounterfactualScenario scenario) {
        Set<String> butterflied = new HashSet<>();
        Set<String> unchanged = new HashSet<>();
        
        // Find all events that depend on the divergence point
        cascadeEffects(scenario.divergencePoint().id(), butterflied);
        
        // Find unchanged events (before divergence or unrelated)
        for (HistoricalEvent e : EVENT_DATABASE) {
            if (!butterflied.contains(e.id())) {
                // Check if before divergence
                if (e.date().compareTo(scenario.divergencePoint().date()) < 0) {
                    unchanged.add(e.id());
                } else if (!isConnected(e.id(), scenario.divergencePoint().id())) {
                    unchanged.add(e.id());
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
     * Analyzes the historical contingency of an event.
     */
    public static Real calculateContingency(String eventId) {
        HistoricalEvent event = findEvent(eventId);
        if (event == null) return Real.ZERO;
        
        // Count how many events depend on this one
        Set<String> dependents = new HashSet<>();
        cascadeEffects(eventId, dependents);
        
        // Contingency = importance * number of dependents / total events
        double contingency = event.importance() * dependents.size() / 
            Math.max(1, EVENT_DATABASE.size());
        
        return Real.of(contingency);
    }

    /**
     * Suggests plausible counterfactuals for an event.
     */
    public static List<String> suggestCounterfactuals(String eventId) {
        HistoricalEvent event = findEvent(eventId);
        if (event == null) return Collections.emptyList();
        
        List<String> suggestions = new ArrayList<>();
        
        String category = event.category().toLowerCase();
        
        if (category.contains("battle") || category.contains("war")) {
            suggestions.add("What if the opposing side won?");
            suggestions.add("What if the battle never occurred (diplomacy succeeded)?");
            suggestions.add("What if key commander died before the battle?");
        }
        
        if (category.contains("invention") || category.contains("discovery")) {
            suggestions.add("What if the invention was delayed by 50 years?");
            suggestions.add("What if a different inventor made the discovery?");
            suggestions.add("What if the invention was suppressed?");
        }
        
        if (category.contains("assassination") || category.contains("death")) {
            suggestions.add("What if the target survived?");
            suggestions.add("What if the assassination succeeded earlier?");
        }
        
        if (category.contains("treaty") || category.contains("agreement")) {
            suggestions.add("What if negotiations failed?");
            suggestions.add("What if terms were more/less favorable?");
        }
        
        suggestions.add("What if this event never happened?");
        suggestions.add("What if this event happened 10 years earlier/later?");
        
        return suggestions;
    }

    /**
     * Loads sample historical events.
     */
    public static void loadSampleData() {
        addEvent(new HistoricalEvent("roman_republic", "Founding of Roman Republic",
            UncertainDate.circa(-509), "Political", 0.9, List.of("punic_wars", "roman_empire")));
        
        addEvent(new HistoricalEvent("punic_wars", "Punic Wars",
            UncertainDate.circa(-264), "War", 0.85, List.of("roman_empire")));
        
        addEvent(new HistoricalEvent("roman_empire", "Roman Empire established",
            UncertainDate.circa(-27), "Political", 0.95, List.of("christianity_rise", "fall_of_rome")));
        
        addEvent(new HistoricalEvent("christianity_rise", "Rise of Christianity",
            UncertainDate.circa(30), "Religious", 0.95, List.of("constantine", "medieval_church")));
        
        addEvent(new HistoricalEvent("fall_of_rome", "Fall of Western Roman Empire",
            UncertainDate.certain(476, 9, 4), "Political", 0.9, List.of("medieval_period")));
        
        addEvent(new HistoricalEvent("printing_press", "Gutenberg Printing Press",
            UncertainDate.circa(1440), "Technology", 0.9, List.of("reformation", "scientific_rev")));
        
        addEvent(new HistoricalEvent("reformation", "Protestant Reformation",
            UncertainDate.certain(1517, 10, 31), "Religious", 0.85, List.of("30_years_war")));
    }

    private static void cascadeEffects(String eventId, Set<String> affected) {
        HistoricalEvent event = findEvent(eventId);
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

    private static HistoricalEvent findEvent(String id) {
        return EVENT_DATABASE.stream()
            .filter(e -> e.id().equals(id))
            .findFirst()
            .orElse(null);
    }

    private static String generateNarrative(CounterfactualScenario scenario, Set<String> butterflied) {
        StringBuilder sb = new StringBuilder();
        sb.append("COUNTERFACTUAL ANALYSIS: ").append(scenario.name()).append("\n\n");
        sb.append("Divergence point: ").append(scenario.divergencePoint().name()).append("\n");
        sb.append("Alternative: ").append(scenario.alternativeOutcome()).append("\n\n");
        sb.append("Affected historical events: ").append(butterflied.size()).append("\n");
        
        for (String eventId : butterflied) {
            HistoricalEvent e = findEvent(eventId);
            if (e != null) {
                sb.append("  - ").append(e.name());
                if (scenario.changedConsequences().containsKey(eventId)) {
                    sb.append(" → ").append(scenario.changedConsequences().get(eventId));
                }
                sb.append("\n");
            }
        }
        
        return sb.toString();
    }
}
