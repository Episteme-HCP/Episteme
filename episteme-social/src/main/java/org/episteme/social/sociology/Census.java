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

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a systematic collection of demographic data for a population of individuals.
 * Provides analytical methods for population statistics like average age and gender distribution.
 * * @version 1.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Census implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Person> population;

    /**
     * Creates a new Census for the given population.
     *
     * @param population the list of people to analyze
     * @throws NullPointerException if population is null
     */
    public Census(List<Person> population) {
        this.population = new ArrayList<>(Objects.requireNonNull(population, "Population cannot be null"));
    }

    /**
     * Returns the total number of individuals in the census.
     * @return population size
     */
    public int getTotalPopulation() {
        return population.size();
    }

    /**
     * Calculates the average age of the population.
     * @return average age in years, or 0.0 if population is empty
     */
    public double getAverageAge() {
        if (population.isEmpty()) {
            return 0.0;
        }
        double totalAge = 0;
        for (Person p : population) {
            totalAge += p.getAge();
        }
        return totalAge / population.size();
    }

    /**
     * Returns the distribution of genders within the population.
     * @return a map from gender to the count of individuals
     */
    public Map<Gender, Long> getGenderDistribution() {
        Map<Gender, Long> dist = new java.util.HashMap<>();
        for (Person p : population) {
            Gender gender = p.getGender();
            dist.put(gender, dist.getOrDefault(gender, 0L) + 1);
        }
        return dist;
    }
}

