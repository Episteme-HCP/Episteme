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

package org.jscience.sociology;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Represents a social network graph where persons are nodes and relationships are edges.
 * Provides basic graph algorithms for social network analysis, such as degrees of separation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public class SocialNetwork implements Serializable {

    private static final long serialVersionUID = 1L;

    // Adjacency list storing the network graph.
    private final Map<Person, Set<Person>> adjList = new HashMap<>();

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
        if (p1 == null || p2 == null) return;
        
        addPerson(p1);
        addPerson(p2);
        
        adjList.get(p1).add(p2);
        adjList.get(p2).add(p1);
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

            // Using get() safely as current is guaranteed to be in adjList
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
}
