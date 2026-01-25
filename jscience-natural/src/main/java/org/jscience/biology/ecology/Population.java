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

package org.jscience.biology.ecology;

import java.util.*;
import java.util.function.Predicate;

import org.jscience.biology.Individual;
import org.jscience.biology.taxonomy.Species;
import org.jscience.geography.Place;
import org.jscience.util.Positioned;
import org.jscience.util.identity.AbstractIdentifiedEntity;
import org.jscience.util.identity.UUIDIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a population of individuals of the same species.
 * <p>
 * Provides population-level analysis: demographics, statistics, growth
 * modeling.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Population extends AbstractIdentifiedEntity implements Positioned<Place> {

    private static final long serialVersionUID = 1L;

    @Attribute
    private final Species species;
    
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Individual> members = new ArrayList<>();
    
    @Attribute
    private Place territory;

    public Population(String name, Species species, Place territory) {
        super(new UUIDIdentification(UUID.randomUUID().toString()));
        setName(name);
        this.species = Objects.requireNonNull(species, "Species cannot be null");
        this.territory = territory;
    }

    public Population(Species species) {
        this("Unnamed Population", species, null);
    }

    public Species getSpecies() {
        return species;
    }

    public Place getTerritory() {
        return territory;
    }

    public void setTerritory(Place territory) {
        this.territory = territory;
    }

    @Override
    public Place getPosition() {
        return territory;
    }

    public String getLocation() {
        return territory != null ? territory.getName() : "Unknown";
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

    public Set<Individual> getIndividuals() {
        return new HashSet<>(members);
    }

    public void setIndividuals(Set<Individual> individuals) {
        members.clear();
        if (individuals != null) {
            members.addAll(individuals);
        }
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
                .filter(i -> i.getParents().isEmpty())
                .toList();
    }

    /**
     * Returns average number of offspring per adult.
     */
    public double getAverageFecundity() {
        return members.stream()
                .filter(i -> i.getLifeStage() == Individual.LifeStage.ADULT)
                .mapToInt(i -> i.getChildren().size())
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
                getName(), species.getScientificName(), getLocation(), size(), countAlive());
    }
}
