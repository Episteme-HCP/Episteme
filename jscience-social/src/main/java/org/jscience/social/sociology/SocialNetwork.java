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

package org.jscience.social.sociology;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.Objects;
import org.jscience.core.mathematics.discrete.Graph;
import org.jscience.core.mathematics.discrete.WeightedGraph;
import org.jscience.core.mathematics.discrete.WeightedEdge;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.SimpleIdentification;
import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Represents a social network graph where persons are nodes and relationships are edges.
 * Provides basic graph algorithms for social network analysis, such as degrees of separation.
 * Modernized to implement ComprehensiveIdentification and support edge weights.
 * * @version 1.2
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class SocialNetwork implements ComprehensiveIdentification, WeightedGraph<Person, Real> {

    private static final long serialVersionUID = 2L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    // Adjacency list storing the network graph.
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Map<Person, Set<Person>> adjList = new HashMap<>();

    // Edge weights (relationship strength)
    @Attribute
    private final Map<String, Real> weights = new HashMap<>();

    public SocialNetwork(String name) {
        this.id = new SimpleIdentification("SocialNetwork:" + UUID.randomUUID());
        setName(Objects.requireNonNull(name, "Name cannot be null"));
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    @Override
    public Set<Person> vertices() {
        return getPersons();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<Graph.Edge<Person>> edges() {
        Set<Graph.Edge<Person>> edges = new HashSet<>();
        for (WeightedEdge<Person, Real> we : getWeightedEdges()) {
            edges.add((Graph.Edge<Person>) we);
        }
        return Collections.unmodifiableSet(edges);
    }

    @Override
    public boolean addVertex(Person vertex) {
        if (vertex == null) return false;
        if (adjList.containsKey(vertex)) return false;
        addPerson(vertex);
        return true;
    }

    @Override
    public boolean addEdge(Person source, Person target, Real weight) {
        if (source == null || target == null) return false;
        addConnection(source, target, weight);
        return true;
    }

    @Override
    public Set<Person> neighbors(Person vertex) {
        return getNeighbors(vertex);
    }

    @Override
    public int degree(Person vertex) {
        return adjList.containsKey(vertex) ? adjList.get(vertex).size() : 0;
    }

    @Override
    public boolean isDirected() {
        return false; // Social networks in this implementation are bidirectional graphs
    }

    @Override
    public int vertexCount() {
        return adjList.size();
    }

    @Override
    public Real getWeight(Person p1, Person p2) {
        return weights.get(getEdgeId(p1, p2));
    }

    @Override
    public Set<WeightedEdge<Person, Real>> getWeightedEdges() {
        Set<WeightedEdge<Person, Real>> allEdges = new HashSet<>();
        for (Map.Entry<Person, Set<Person>> entry : adjList.entrySet()) {
            Person p1 = entry.getKey();
            for (Person p2 : entry.getValue()) {
                // To avoid duplicates in undirected graph, we only add if id1 < id2
                if (p1.getId().toString().compareTo(p2.getId().toString()) < 0) {
                    allEdges.add(new SocialEdge(p1, p2, getWeight(p1, p2)));
                }
            }
        }
        return Collections.unmodifiableSet(allEdges);
    }

    @Override
    public Set<WeightedEdge<Person, Real>> getWeightedEdgesFrom(Person vertex) {
        if (!adjList.containsKey(vertex)) return Collections.emptySet();
        Set<WeightedEdge<Person, Real>> fromEdges = new HashSet<>();
        for (Person neighbor : adjList.get(vertex)) {
            fromEdges.add(new SocialEdge(vertex, neighbor, getWeight(vertex, neighbor)));
        }
        return Collections.unmodifiableSet(fromEdges);
    }

    @Override
    public Real getDefaultWeight() {
        return Real.ONE;
    }

    /**
     * Adds a person to the network as a node.
     * @param p the person to add
     */
    public void addPerson(Person p) {
        if (p != null) {
            adjList.putIfAbsent(p, new HashSet<>());
        }
    }

    /**
     * Adds a bidirectional connection (relationship) between two people.
     * Automatically adds the persons to the network if they are not already present.
     *
     * @param p1 first person
     * @param p2 second person
     */
    public void addConnection(Person p1, Person p2) {
        addConnection(p1, p2, Real.ONE);
    }

    /**
     * Adds a bidirectional weighted connection.
     * @param p1 first person
     * @param p2 second person
     * @param weight relationship strength
     */
    public void addConnection(Person p1, Person p2, Real weight) {
        if (p1 == null || p2 == null) return;
        
        addPerson(p1);
        addPerson(p2);
        
        adjList.get(p1).add(p2);
        adjList.get(p2).add(p1);
        
        String edgeId = getEdgeId(p1, p2);
        weights.put(edgeId, weight);
    }
    
    public void addConnection(Person p1, Person p2, double weight) {
        addConnection(p1, p2, Real.of(weight));
    }

    private String getEdgeId(Person p1, Person p2) {
        String id1 = p1.getId().toString();
        String id2 = p2.getId().toString();
        return id1.compareTo(id2) < 0 ? id1 + "_" + id2 : id2 + "_" + id1;
    }


    /**
     * Checks if two people are directly connected.
     *
     * @param p1 first person
     * @param p2 second person
     * @return true if a direct connection exists
     */
    public boolean areConnected(Person p1, Person p2) {
        if (p1 == null || p2 == null || !adjList.containsKey(p1)) {
            return false;
        }
        return adjList.get(p1).contains(p2);
    }

    /**
     * Finds the shortest path (degrees of separation) between two people using Breadth-First Search (BFS).
     *
     * @param start the starting person
     * @param end   the target person
     * @return the number of hops (degrees), or 0 if same person, or -1 if no path exists
     */
    public int getDegreesOfSeparation(Person start, Person end) {
        if (start == null || end == null) return -1;
        if (start.equals(end)) return 0;
        
        if (!adjList.containsKey(start) || !adjList.containsKey(end)) {
            return -1;
        }

        Queue<Person> queue = new LinkedList<>();
        Map<Person, Integer> distances = new HashMap<>();

        queue.add(start);
        distances.put(start, 0);

        while (!queue.isEmpty()) {
            Person current = queue.poll();
            int dist = distances.get(current);

            Set<Person> neighbors = adjList.get(current);
            if (neighbors == null) continue;

            for (Person neighbor : neighbors) {
                if (!distances.containsKey(neighbor)) {
                    distances.put(neighbor, dist + 1);
                    queue.add(neighbor);
                    if (neighbor.equals(end)) {
                        return dist + 1;
                    }
                }
            }
        }
        return -1; // No path found
    }

    /**
     * Calculates the degree centrality of a person.
     * @param p the person
     * @return normalized degree centrality (0.0 to 1.0)
     */
    public Real getDegreeCentrality(Person p) {
        if (p == null || adjList.isEmpty() || !adjList.containsKey(p)) return Real.ZERO;
        int n = adjList.size();
        if (n <= 1) return Real.ZERO;
        return Real.of(adjList.get(p).size()).divide(Real.of(n - 1));
    }

    /**
     * Calculates the closeness centrality of a person.
     * @param p the person
     * @return closeness centrality
     */
    public Real getClosenessCentrality(Person p) {
        if (p == null || !adjList.containsKey(p)) return Real.ZERO;
        
        int n = adjList.size();
        if (n <= 1) return Real.ZERO;

        int totalDistance = 0;
        int reachableNodes = 0;

        for (Person other : adjList.keySet()) {
            if (other.equals(p)) continue;
            int dist = getDegreesOfSeparation(p, other);
            if (dist != -1) {
                totalDistance += dist;
                reachableNodes++;
            }
        }

        if (totalDistance == 0) return Real.ZERO;
        
        // Classic formula: (N-1) / Sum(dist)
        return Real.of(reachableNodes).divide(Real.of(totalDistance));
    }

    /**
     * Calculates the local clustering coefficient of a person.
     * @param p the person
     * @return clustering coefficient (0.0 to 1.0)
     */
    public Real getClusteringCoefficient(Person p) {
        if (p == null || !adjList.containsKey(p)) return Real.ZERO;
        
        Set<Person> neighbors = adjList.get(p);
        int k = neighbors.size();
        if (k < 2) return Real.ZERO;

        int linksBetweenNeighbors = 0;
        List<Person> neighborList = new ArrayList<>(neighbors);
        
        for (int i = 0; i < neighborList.size(); i++) {
            for (int j = i + 1; j < neighborList.size(); j++) {
                if (areConnected(neighborList.get(i), neighborList.get(j))) {
                    linksBetweenNeighbors++;
                }
            }
        }

        return Real.of(2).multiply(Real.of(linksBetweenNeighbors)).divide(Real.of(k).multiply(Real.of(k - 1)));
    }

    public Set<Person> getNeighbors(Person p) {
        return adjList.containsKey(p) ? Collections.unmodifiableSet(adjList.get(p)) : Collections.emptySet();
    }

    public Set<Person> getPersons() {
        return Collections.unmodifiableSet(adjList.keySet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SocialNetwork that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getName() + " (" + adjList.size() + " nodes)";
    }

    /**
     * Internal implementation of a weighted edge for the social network.
     */
    private static class SocialEdge implements WeightedEdge<Person, Real>, Graph.Edge<Person>, Serializable {
        private static final long serialVersionUID = 1L;
        private final Person source;
        private final Person target;
        private final Real weight;

        public SocialEdge(Person source, Person target, Real weight) {
            this.source = source;
            this.target = target;
            this.weight = weight;
        }

        @Override public Person getSource() { return source; }
        @Override public Person getTarget() { return target; }
        @Override public Real getWeight() { return weight; }
        @Override public Person source() { return source; }
        @Override public Person target() { return target; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SocialEdge that)) return false;
            return connects(that.source, that.target);
        }

        @Override
        public int hashCode() {
            int h1 = Objects.hashCode(source);
            int h2 = Objects.hashCode(target);
            return h1 + h2; // Symmetric for undirected interpretation
        }
    }
}

