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

package org.episteme.social.sociology;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.episteme.core.util.UniversalDataModel;

/**
 * Data model for demographic population structures.
 * Supports representation via individual age-SociologicalGroup cohorts or aggregation from social groups.
 * Provides consolidated segments for population pyramid visualizations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class DemographicData implements UniversalDataModel, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String getModelType() {
        return "DEMOGRAPHIC_STRUCTURE";
    }

    /**
     * Represents a single age-cohort segment within a demographic structure.
     */
    public record AgeGroup(int minAge, int maxAge, long maleCount, long femaleCount) implements Serializable {
        private static final long serialVersionUID = 1L;

        /** Returns the total count for this age SociologicalGroup. */
        public long total() {
            return maleCount + femaleCount;
        }
    }

    private final List<AgeGroup> manualGroups = new ArrayList<>();
    private final List<SociologicalGroup> individuals = new ArrayList<>();
    private String populationName;

    /**
     * Manually adds an age cohort to the model.
     *
     * @param min    minimum age in years
     * @param max    maximum age in years
     * @param male   number of males
     * @param female number of females
     */
    public void addGroup(int min, int max, long male, long female) {
        manualGroups.add(new AgeGroup(min, max, male, female));
    }

    /**
     * Adds a social SociologicalGroup to the model for automated aggregation.
     * @param SociologicalGroup the SociologicalGroup to add
     */
    public void addGroup(SociologicalGroup SociologicalGroup) {
        individuals.add(Objects.requireNonNull(SociologicalGroup, "SociologicalGroup cannot be null"));
    }

    /**
     * Returns the name of the population being modeled.
     * @return the name
     */
    public String getPopulationName() {
        return populationName;
    }

    /**
     * Sets the name of the population.
     * @param name the name to set
     */
    public void setPopulationName(String name) {
        this.populationName = name;
    }

    /**
     * Consolidates all data into a list of AgeGroup segments for visualization.
     *
     * @param bucketSize the cohort size in years (e.g., 5 for 5-year chunks)
     * @return a sorted list of age cohorts
     */
    public List<AgeGroup> getConsolidatedGroups(int bucketSize) {
        if (!individuals.isEmpty()) {
            return computeFromIndividuals(bucketSize);
        }

        List<AgeGroup> sorted = new ArrayList<>(manualGroups);
        sorted.sort(Comparator.comparingInt(AgeGroup::minAge));
        return sorted;
    }

    private List<AgeGroup> computeFromIndividuals(int bucketSize) {
        Map<Integer, Long> males = new HashMap<>();
        Map<Integer, Long> females = new HashMap<>();

        for (SociologicalGroup g : individuals) {
            for (Person p : g.getMembers()) {
                int bucket = (p.getAge() / bucketSize) * bucketSize;
                if (p.getGender() == Gender.MALE) {
                    males.put(bucket, males.getOrDefault(bucket, 0L) + 1);
                } else if (p.getGender() == Gender.FEMALE) {
                    females.put(bucket, females.getOrDefault(bucket, 0L) + 1);
                }
            }
        }

        int maxAge = 100; // Default reasonable upper bound
        List<AgeGroup> result = new ArrayList<>();
        for (int age = 0; age <= maxAge; age += bucketSize) {
            result.add(new AgeGroup(age, age + bucketSize - 1,
                    males.getOrDefault(age, 0L), females.getOrDefault(age, 0L)));
        }
        return result;
    }

    /**
     * Returns the total population count across all specified segments and groups.
     * @return total count
     */
    public long getTotalPopulation() {
        long total = manualGroups.stream().mapToLong(AgeGroup::total).sum();
        total += individuals.stream().mapToLong(SociologicalGroup::size).sum();
        return total;
    }

    @Override
    public Map<String, org.episteme.core.measure.Quantity<?>> getQuantities() {
        Map<String, org.episteme.core.measure.Quantity<?>> quantities = new HashMap<>();
        quantities.put("total_population", org.episteme.core.measure.Quantities.create(getTotalPopulation(), org.episteme.core.measure.Units.ONE));
        return quantities;
    }
}


