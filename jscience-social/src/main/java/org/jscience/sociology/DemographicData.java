package org.jscience.sociology;

import org.jscience.data.UniversalDataModel;
import java.util.*;

/**
 * Data model for demographic population structures.
 * Can be populated via individual AgeGroup segments or derived from a List of Personen/Groups.
 */
public final class DemographicData implements UniversalDataModel {

    @Override
    public String getModelType() { return "DEMOGRAPHIC_STRUCTURE"; }

    public record AgeGroup(int minAge, int maxAge, long maleCount, long femaleCount) {
        public long total() { return maleCount + femaleCount; }
    }

    private final List<AgeGroup> manualGroups = new ArrayList<>();
    private final List<Group> individuals = new ArrayList<>();
    private String populationName;

    public void addGroup(int min, int max, long male, long female) {
        manualGroups.add(new AgeGroup(min, max, male, female));
    }

    public void addGroup(Group group) {
        individuals.add(group);
    }

    /**
     * Consolidates all data into a list of AgeGroup segments for visualization.
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
        
        for (Group g : individuals) {
            for (Person p : g.getMembers()) {
                int bucket = (p.getAge() / bucketSize) * bucketSize;
                if (p.getGender() == Person.Gender.MALE) {
                    males.put(bucket, males.getOrDefault(bucket, 0L) + 1);
                } else if (p.getGender() == Person.Gender.FEMALE) {
                    females.put(bucket, females.getOrDefault(bucket, 0L) + 1);
                }
            }
        }
        
        int maxAge = 100; // Default limit
        List<AgeGroup> result = new ArrayList<>();
        for (int age = 0; age <= maxAge; age += bucketSize) {
            result.add(new AgeGroup(age, age + bucketSize - 1, 
                       males.getOrDefault(age, 0L), females.getOrDefault(age, 0L)));
        }
        return result;
    }

    public void setPopulationName(String name) { this.populationName = name; }
    public String getPopulationName() { return populationName; }

    public long getTotalPopulation() {
        long total = manualGroups.stream().mapToLong(AgeGroup::total).sum();
        total += individuals.stream().mapToLong(Group::size).sum();
        return total;
    }
}
