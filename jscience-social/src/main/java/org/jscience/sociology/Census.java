package org.jscience.sociology;

import java.util.*;

/**
 * Aggregates demographic statistics for a population.
 */
public class Census {

    private final List<Person> population;

    public Census(List<Person> population) {
        this.population = new ArrayList<>(population);
    }

    public int getTotalPopulation() {
        return population.size();
    }

    public double getAverageAge() {
        if (population.isEmpty())
            return 0.0;
        double totalAge = 0;
        for (Person p : population) {
            totalAge += p.getAge();
        }
        return totalAge / population.size();
    }

    public Map<Person.Gender, Long> getGenderDistribution() {
        Map<Person.Gender, Long> dist = new EnumMap<>(Person.Gender.class);
        for (Person p : population) {
            dist.put(p.getGender(), dist.getOrDefault(p.getGender(), 0L) + 1);
        }
        return dist;
    }
}
