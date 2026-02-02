/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.social;

import org.jscience.core.mathematics.ml.BayesianBeliefNetwork;
import org.jscience.core.mathematics.discrete.Graph;
import org.jscience.core.mathematics.discrete.generators.SmallWorldGenerator;
import org.jscience.core.mathematics.discrete.algorithms.CommunityDetection;
import org.jscience.core.mathematics.optimization.GeneticOptimizer;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.numbers.integers.Natural;
import org.jscience.social.sociology.Person;
import org.jscience.social.sociology.AxelrodModel;
import org.jscience.natural.biology.BiologicalSex;
import org.jscience.core.mathematics.discrete.UndirectedGraph;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class Phase3IntegrationTest {

    @Test
    public void testBayesianInference() {
        BayesianBeliefNetwork bbn = new BayesianBeliefNetwork();
        bbn.addVariable("Rain", "True", "False");
        bbn.addVariable("Sprinkler", "On", "Off");
        bbn.addVariable("GrassWet", "Yes", "No");

        bbn.addDependency("Rain", "GrassWet");
        bbn.addDependency("Sprinkler", "GrassWet");

        // P(Rain=True) = 0.2
        bbn.setCPT("Rain", 0.2, 0.8);
        
        // P(Sprinkler=On) = 0.4
        bbn.setCPT("Sprinkler", 0.4, 0.6);

        // P(GrassWet=Yes | Rain, Sprinkler)
        // R=T, S=On -> 0.99
        // R=T, S=Off -> 0.80
        // R=F, S=On -> 0.90
        // R=F, S=Off -> 0.01
        bbn.setCPT("GrassWet", 
            0.99, 0.01, // R=T, S=On 
            0.80, 0.20, // R=T, S=Off
            0.90, 0.10, // R=F, S=On
            0.01, 0.99  // R=F, S=Off
        );

        Map<String, String> evidence = new HashMap<>();
        evidence.put("Rain", "True");
        evidence.put("Sprinkler", "Off");

        Real prob = bbn.query("GrassWet", "Yes", evidence);
        // Using Variable Elimination now, result should be 0.80
        assertEquals(0.80, prob.doubleValue(), 1e-6);
    }

    @Test
    public void testAxelrodOnSmallWorld() {
        // 1. Generate Small World Graph
        int n = 50;
        Graph<Natural> naturalGraph = SmallWorldGenerator.generate(n, 4, 0.1);
        
        // 2. Map Naturals to Persons
        UndirectedGraph<Person> socialGraph = new UndirectedGraph<>();
        Map<Natural, Person> mapping = new HashMap<>();
        for (int i = 0; i < n; i++) {
            Natural v = Natural.of(i);
            Person p = new Person("Person " + i, BiologicalSex.MALE);
            mapping.put(v, p);
            socialGraph.addVertex(p);
        }
        
        for (Graph.Edge<Natural> edge : naturalGraph.edges()) {
            socialGraph.addEdge(mapping.get(edge.source()), mapping.get(edge.target()));
        }

        // 3. Run Axelrod
        AxelrodModel model = new AxelrodModel(socialGraph, 5, 10);
        for (int i = 0; i < 500; i++) {
            model.step();
        }

        // 4. Verification: check that some cultural exchange happened
        assertNotNull(model.getCulture(mapping.get(Natural.of(0))));
    }

    @Test
    public void testGeneticOptimization() {
        // Find minimum of f(x) = x^2
        java.util.function.Function<Real[], Real> fitness = x -> x[0].multiply(x[0]);
        
        Real[] result = GeneticOptimizer.optimize(fitness, 1, 20, 50, 0.1);
        
        // Should be close to 0
        assertTrue(result[0].abs().doubleValue() < 1.0);
    }

    @Test
    public void testCommunityDetection() {
        UndirectedGraph<Integer> graph = new UndirectedGraph<>();
        // Two cliques connected by one edge
        for (int i = 0; i < 5; i++) {
            for (int j = i + 1; j < 5; j++) {
                graph.addEdge(i, j);
            }
        }
        for (int i = 5; i < 10; i++) {
            for (int j = i + 1; j < 10; j++) {
                graph.addEdge(i, j);
            }
        }
        graph.addEdge(4, 5);

        Map<Integer, Integer> communities = CommunityDetection.detectCommunities(graph, 10);
        
        // Nodes 0-4 should share a label, nodes 5-9 should share a label
        assertEquals(communities.get(0), communities.get(1));
        assertEquals(communities.get(5), communities.get(6));
    }
}
