/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.biology.ecology;

import java.util.*;
import java.util.function.Predicate;
import org.jscience.biology.Individual;
import org.jscience.biology.taxonomy.Species;

/**
 * Represents a population of individuals of the same species.
 * <p>
 * Provides population-level analysis: demographics, statistics, growth
 * modeling.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Population {

    private final String name;
    private final Species species;
    private final List<Individual> members = new ArrayList<>();
    private final String location;

    public Population(String name, Species species, String location) {
        this.name = name;
        this.species = species;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Species getSpecies() {
        return species;
    }

    public String getLocation() {
        return location;
    }

    public void addMember(Individual individual) {
        if (!individual.getSpecies().equals(species)) {
            throw new IllegalArgumentException("Individual must be of species " + species);
        }
        members.add(individual);
    }

    public void removeMember(Individual individual) {
        members.remove(individual);
    }

    public List<Individual> getMembers() {
        return Collections.unmodifiableList(members);
    }

    public int size() {
        return members.size();
    }

    // ========== Demographics ==========

    public long countAlive() {
        return members.stream().filter(Individual::isAlive).count();
    }

    public long countByStage(Individual.LifeStage stage) {
        return members.stream()
                .filter(i -> i.getLifeStage() == stage)
                .count();
    }

    public long countBySex(Individual.Sex sex) {
        return members.stream()
                .filter(i -> i.getSex() == sex)
                .count();
    }

    public double getSexRatio() {
        long males = countBySex(Individual.Sex.MALE);
        long females = countBySex(Individual.Sex.FEMALE);
        return females > 0 ? (double) males / females : 0;
    }

    // ========== Statistics ==========

    public double getAverageAge() {
        return members.stream()
                .filter(Individual::isAlive)
                .mapToInt(Individual::getAge)
                .average()
                .orElse(0);
    }

    public int getOldestAge() {
        return members.stream()
                .mapToInt(Individual::getAge)
                .max()
                .orElse(0);
    }

    public int getYoungestAge() {
        return members.stream()
                .filter(Individual::isAlive)
                .mapToInt(Individual::getAge)
                .min()
                .orElse(0);
    }

    public Map<Individual.LifeStage, Long> getAgeDistribution() {
        Map<Individual.LifeStage, Long> dist = new EnumMap<>(Individual.LifeStage.class);
        for (Individual.LifeStage stage : Individual.LifeStage.values()) {
            dist.put(stage, countByStage(stage));
        }
        return dist;
    }

    // ========== Genealogy ==========

    /**
     * Returns all individuals with no parents in this population.
     */
    public List<Individual> getFounders() {
        return members.stream()
                .filter(i -> i.getMother() == null && i.getFather() == null)
                .toList();
    }

    /**
     * Returns average number of offspring per adult.
     */
    public double getAverageFecundity() {
        return members.stream()
                .filter(i -> i.getLifeStage() == Individual.LifeStage.ADULT)
                .mapToInt(i -> i.getOffspring().size())
                .average()
                .orElse(0);
    }

    // ========== Filtering ==========

    public List<Individual> filter(Predicate<Individual> predicate) {
        return members.stream().filter(predicate).toList();
    }

    @Override
    public String toString() {
        return String.format("Population '%s' of %s at %s: %d members (%d alive)",
                name, species.getScientificName(), location, size(), countAlive());
    }
}
