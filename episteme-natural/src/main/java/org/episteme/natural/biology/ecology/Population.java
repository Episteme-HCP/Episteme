/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.biology.ecology;

import java.util.*;
import java.util.function.Predicate;

import org.episteme.natural.biology.Individual;
import org.episteme.natural.biology.BiologicalSex;
import org.episteme.natural.biology.LifeStage;
import org.episteme.natural.biology.SocialCollective;
import org.episteme.natural.biology.taxonomy.Species;
import org.episteme.natural.earth.Place;
import org.episteme.core.util.Positioned;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.identity.UUIDIdentification;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.natural.engineering.eventdriven.EventDrivenEngine;
import org.episteme.natural.engineering.eventdriven.Event;

/**
 * Represents a population of individuals of the same species.
 * Provides population-level analysis: demographics, statistics, growth modeling.
 */
@Persistent
public class Population<T extends Individual> extends SocialCollective<T> implements Positioned<Place> {

    private static final long serialVersionUID = 2L;

    @Attribute
    private final Species species;
    
    @Attribute
    private Place territory;

    public Population(String name, Species species, Place territory) {
        this(new UUIDIdentification(UUID.randomUUID().toString()), name, species, territory);
    }

    public Population(Identification id, String name, Species species, Place territory) {
        this(id, name, species, territory, null);
    }

    public Population(Identification id, String name, Species species, Place territory, EventDrivenEngine engine) {
        super(id, engine);
        setName(name); // setName is inherited or expected from ComprehensiveIdentification
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

    @Override
    public void addMember(T individual) {
        if (!individual.getSpecies().equals(species)) {
            throw new IllegalArgumentException("Individual must be of species " + species);
        }
        super.addMember(individual);
    }

    public Set<T> getIndividuals() {
        return new HashSet<>(members);
    }

    public void setIndividuals(Set<T> individuals) {
        members.clear();
        if (individuals != null) {
            members.addAll(individuals);
        }
    }

    public int size() {
        return members.size();
    }

    public long countAlive() {
        return members.stream().filter(Individual::isAlive).count();
    }

    public long countByStage(LifeStage stage) {
        return members.stream()
                .filter(i -> i.getLifeStage() != null && i.getLifeStage().equals(stage))
                .count();
    }

    public long countBySex(BiologicalSex sex) {
        return members.stream()
                .filter(i -> i.getSex() != null && i.getSex().equals(sex))
                .count();
    }

    public double getSexRatio() {
        long males = countBySex(BiologicalSex.MALE);
        long females = countBySex(BiologicalSex.FEMALE);
        return females > 0 ? (double) males / females : 0;
    }

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

    public Map<LifeStage, Long> getAgeDistribution() {
        Map<LifeStage, Long> dist = new HashMap<>();
        for (LifeStage stage : LifeStage.values()) {
            dist.put(stage, countByStage(stage));
        }
        return dist;
    }

    public List<T> getFounders() {
        return members.stream()
                .filter(i -> i.getParents().isEmpty())
                .toList();
    }

    public double getAverageFecundity() {
        return members.stream()
                .filter(i -> i.getLifeStage() != null && i.getLifeStage().equals(LifeStage.ADULT))
                .mapToInt(i -> i.getChildren().size())
                .average()
                .orElse(0);
    }

    public List<T> filter(Predicate<T> predicate) {
        return members.stream().filter(predicate).toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Population<?> population)) return false;
        return Objects.equals(id, population.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public void processEvent(Event event) {
        // Default implementation: handle population events
    }
    
    @Override
    public String toString() {
        return String.format("Population '%s' of %s at %s: %d members (%d alive)",
                getName(), species.getScientificName(), getLocation(), size(), countAlive());
    }
}
