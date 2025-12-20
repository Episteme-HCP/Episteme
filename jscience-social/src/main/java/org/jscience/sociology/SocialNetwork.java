package org.jscience.sociology;

import java.util.*;

/**
 * Represents a social network graph.
 * Nodes are Persons, edges are relationships.
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
