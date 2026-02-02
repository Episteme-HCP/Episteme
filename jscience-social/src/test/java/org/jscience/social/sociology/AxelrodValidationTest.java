package org.jscience.social.sociology;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.jscience.core.mathematics.discrete.Graph;
import org.jscience.core.mathematics.discrete.UndirectedGraph;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

/**
 * Validation tests for Axelrod's Cultural Dissemination Model.
 * Verifies scientific properties like local convergence and cultural fragmentation.
 */
public class AxelrodValidationTest {

    @Test
    public void testConvergenceSmallNetwork() {
        // Create a small complete graph of 10 people
        Graph<Person> network = new UndirectedGraph<>();
        Person[] people = new Person[10];
        for (int i = 0; i < 10; i++) {
            people[i] = new Person("P" + i, org.jscience.natural.biology.BiologicalSex.MALE);
            network.addVertex(people[i]);
        }
        
        // Connect everyone (Complete Graph)
        for (int i = 0; i < 10; i++) {
            for (int j = i + 1; j < 10; j++) {
                network.addEdge(people[i], people[j]);
                network.addEdge(people[j], people[i]);
            }
        }
        
        // Initialize Model: 5 features, 2 traits per feature
        // Low number of traits promotes convergence (mono-culture)
        AxelrodModel model = new AxelrodModel(network, 5, 2);
        
        int initialDistinctCultures = countDistinctCultures(model, people);
        // Run simulation
        for (int i = 0; i < 10000; i++) {
            model.step();
        }
        
        int finalDistinctCultures = countDistinctCultures(model, people);
        
        System.out.println("Initial Cultures: " + initialDistinctCultures);
        System.out.println("Final Cultures: " + finalDistinctCultures);
        
        // In a connected graph with few traits, we expect significant convergence
        assertTrue(finalDistinctCultures <= initialDistinctCultures, 
            String.format("Cultures should not diverge (started with %d, ended with %d)", 
                initialDistinctCultures, finalDistinctCultures));
        assertTrue(finalDistinctCultures < 10, "Should have some convergence in a complete graph");
    }
    
    @Test
    public void testFragmentationHighDiversity() {
        // High number of traits -> Likely fragmentation
        Graph<Person> network = new UndirectedGraph<>();
        Person[] people = new Person[20];
        for (int i = 0; i < 20; i++) {
            people[i] = new Person("P" + i, org.jscience.natural.biology.BiologicalSex.MALE);
            network.addVertex(people[i]);
        }
        // Linear chain topology (harder to converge locally)
        for (int i = 0; i < 19; i++) {
            network.addEdge(people[i], people[i+1]);
            network.addEdge(people[i+1], people[i]);
        }
        
        // 5 features, 50 traits -> Extremely unlikely they share common traits initially
        AxelrodModel model = new AxelrodModel(network, 5, 50);
        
        int initialDistinctCultures = countDistinctCultures(model, people);
        for (int i = 0; i < 10000; i++) {
            model.step();
        }
        int finalDistinctCultures = countDistinctCultures(model, people);
        
        System.out.println("Initial Cultures: " + initialDistinctCultures);
        System.out.println("Final Cultures: " + finalDistinctCultures);
        
        // Should remain fragmented
        assertTrue(finalDistinctCultures > 1, "Should not fully converge with high initial diversity");
        assertTrue(finalDistinctCultures <= initialDistinctCultures, 
            String.format("Cultures should not increase (started with %d, ended with %d)", 
                initialDistinctCultures, finalDistinctCultures));
    }

    private int countDistinctCultures(AxelrodModel model, Person[] people) {
        Set<String> cultures = new HashSet<>();
        for (Person p : people) {
            int[] c = model.getCulture(p);
            cultures.add(Arrays.toString(c));
        }
        return cultures.size();
    }
}
