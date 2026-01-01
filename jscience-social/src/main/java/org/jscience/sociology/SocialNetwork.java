/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

import java.util.*;

/**
 * Represents a social network graph.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SocialNetwork {

    private final Map<Person, Set<Person>> adjList = new HashMap<>();

    public void addPerson(Person p) {
        adjList.putIfAbsent(p, new HashSet<>());
    }

    public void addConnection(Person p1, Person p2) {
        addPerson(p1);
        addPerson(p2);
        adjList.get(p1).add(p2);
        adjList.get(p2).add(p1);
    }

    public boolean areConnected(Person p1, Person p2) {
        if (!adjList.containsKey(p1))
            return false;
        return adjList.get(p1).contains(p2);
    }

    /**
     * Finds the shortest path (degrees of separation) between two people.
     * Uses BFS. Returns -1 if no path.
     */
    public int getDegreesOfSeparation(Person start, Person end) {
        if (start.equals(end))
            return 0;
        if (!adjList.containsKey(start) || !adjList.containsKey(end))
            return -1;

        Queue<Person> queue = new LinkedList<>();
        Map<Person, Integer> distances = new HashMap<>();

        queue.add(start);
        distances.put(start, 0);

        while (!queue.isEmpty()) {
            Person current = queue.poll();
            int dist = distances.get(current);

            for (Person neighbor : adjList.get(current)) {
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


