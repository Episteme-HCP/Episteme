package org.jscience.biology.ecology;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Models trophic food webs and energy flow in ecosystems.
 */
public final class EcosystemFoodWeb {

    private EcosystemFoodWeb() {}

    public enum TrophicLevel {
        PRODUCER(1),
        PRIMARY_CONSUMER(2),
        SECONDARY_CONSUMER(3),
        TERTIARY_CONSUMER(4),
        APEX_PREDATOR(5),
        DECOMPOSER(0);

        private final int level;
        TrophicLevel(int level) { this.level = level; }
        public int getLevel() { return level; }
    }

    public record Species(
        String name,
        TrophicLevel trophicLevel,
        double biomassKgPerHa,
        double metabolicRateKjPerDay,
        boolean isKeystone
    ) {}

    public record TrophicLink(
        Species prey,
        Species predator,
        double energyTransferEfficiency  // 0-1, typically 0.1
    ) {}

    public record FoodWeb(
        String ecosystemName,
        List<Species> species,
        List<TrophicLink> links
    ) {}

    public record FoodWebMetrics(
        int speciesRichness,
        int linkDensity,
        double connectance,
        double meanTrophicLevel,
        List<String> keystoneSpecies
    ) {}

    /**
     * Calculates basic food web metrics.
     */
    public static FoodWebMetrics analyzeWeb(FoodWeb web) {
        int S = web.species().size();
        int L = web.links().size();
        
        // Connectance = L / S²
        double connectance = (double) L / (S * S);
        
        // Mean trophic level
        double meanTL = web.species().stream()
            .mapToInt(s -> s.trophicLevel().getLevel())
            .average()
            .orElse(0);
        
        // Keystone species
        List<String> keystones = web.species().stream()
            .filter(Species::isKeystone)
            .map(Species::name)
            .toList();
        
        return new FoodWebMetrics(S, L, connectance, meanTL, keystones);
    }

    /**
     * Calculates energy flow through trophic levels.
     */
    public static Map<TrophicLevel, Real> calculateEnergyFlow(FoodWeb web, 
            double primaryProductionKjPerHaPerDay) {
        
        Map<TrophicLevel, Real> energy = new EnumMap<>(TrophicLevel.class);
        energy.put(TrophicLevel.PRODUCER, Real.of(primaryProductionKjPerHaPerDay));
        
        // Calculate energy at each level (10% rule)
        double currentEnergy = primaryProductionKjPerHaPerDay;
        for (TrophicLevel level : List.of(
                TrophicLevel.PRIMARY_CONSUMER,
                TrophicLevel.SECONDARY_CONSUMER,
                TrophicLevel.TERTIARY_CONSUMER,
                TrophicLevel.APEX_PREDATOR)) {
            
            // Get average transfer efficiency for this level
            double avgEfficiency = web.links().stream()
                .filter(l -> l.predator().trophicLevel() == level)
                .mapToDouble(l -> l.energyTransferEfficiency()) // Replaced method reference with lambda
                .average()
                .orElse(0.1);
            
            currentEnergy *= avgEfficiency;
            energy.put(level, Real.of(currentEnergy));
        }
        
        return energy;
    }

    /**
     * Calculates biomass pyramid.
     */
    public static Map<TrophicLevel, Real> calculateBiomassPyramid(FoodWeb web) {
        Map<TrophicLevel, Real> pyramid = new EnumMap<>(TrophicLevel.class);
        
        for (TrophicLevel level : TrophicLevel.values()) {
            double totalBiomass = web.species().stream()
                .filter(s -> s.trophicLevel() == level)
                .mapToDouble(s -> s.biomassKgPerHa()) // Replaced method reference with lambda
                .sum();
            pyramid.put(level, Real.of(totalBiomass));
        }
        
        return pyramid;
    }

    /**
     * Simulates cascade effect of removing a species.
     */
    public static Map<Species, Real> simulateCascade(FoodWeb web, String removedSpecies) {
        Map<Species, Real> impact = new HashMap<>();
        
        Species removed = web.species().stream()
            .filter(s -> s.name().equalsIgnoreCase(removedSpecies))
            .findFirst()
            .orElse(null);
        
        if (removed == null) return impact;
        
        // Find species that prey on removed species (will decrease)
        List<Species> predators = web.links().stream()
            .filter(l -> l.prey().name().equals(removed.name()))
            .map(TrophicLink::predator)
            .toList();
        
        for (Species predator : predators) {
            // Calculate fraction of diet from removed species
            long totalPreyLinks = web.links().stream()
                .filter(l -> l.predator().name().equals(predator.name()))
                .count();
            double dietFraction = 1.0 / Math.max(1, totalPreyLinks);
            
            // Impact proportional to diet dependency
            impact.put(predator, Real.of(-dietFraction * 0.3));
        }
        
        // Find species that removed species preyed on (may increase)
        List<Species> prey = web.links().stream()
            .filter(l -> l.predator().name().equals(removed.name()))
            .map(TrophicLink::prey)
            .toList();
        
        for (Species p : prey) {
            impact.put(p, Real.of(0.2)); // Population may increase
        }
        
        // Keystone species have amplified effects
        if (removed.isKeystone()) {
            for (Species s : impact.keySet()) {
                Real current = impact.get(s);
                impact.put(s, current.multiply(Real.of(2)));
            }
        }
        
        return impact;
    }

    /**
     * Identifies potential keystone species based on structural importance.
     */
    public static List<Species> identifyKeystones(FoodWeb web) {
        Map<Species, Integer> connectivity = new HashMap<>();
        
        // Count links per species
        for (TrophicLink link : web.links()) {
            connectivity.merge(link.prey(), 1, (a, b) -> a + b);
            connectivity.merge(link.predator(), 1, (a, b) -> a + b);
        }
        
        // Species with highest connectivity
        double avgConnectivity = connectivity.values().stream()
            .mapToInt(Integer::intValue)
            .average()
            .orElse(0);
        
        return connectivity.entrySet().stream()
            .filter(e -> e.getValue() > avgConnectivity * 1.5)
            .map(Map.Entry::getKey)
            .toList();
    }

    /**
     * Calculates food chain length.
     */
    public static int maxFoodChainLength(FoodWeb web) {
        int maxLength = 0;
        
        for (Species producer : web.species()) {
            if (producer.trophicLevel() == TrophicLevel.PRODUCER) {
                int length = findMaxPathLength(web, producer, new HashSet<>());
                maxLength = Math.max(maxLength, length);
            }
        }
        
        return maxLength;
    }

    private static int findMaxPathLength(FoodWeb web, Species current, Set<String> visited) {
        if (visited.contains(current.name())) return 0;
        visited.add(current.name());
        
        int maxChild = 0;
        for (TrophicLink link : web.links()) {
            if (link.prey().name().equals(current.name())) {
                int childLength = findMaxPathLength(web, link.predator(), new HashSet<>(visited));
                maxChild = Math.max(maxChild, childLength);
            }
        }
        
        return 1 + maxChild;
    }
}
