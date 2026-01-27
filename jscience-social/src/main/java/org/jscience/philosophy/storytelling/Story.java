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

package org.jscience.philosophy.storytelling;

import org.jscience.history.Event;
import org.jscience.mathematics.discrete.Graph;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.SimpleIdentification;
import org.jscience.util.identity.ComprehensiveIdentification;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A class representing a continuous and logical flow of events as a graph G = (V, E).
 * This structure stores narrative scripts where events are vertices and causal/timed
 * links are edges.
 * Modernized to implement ComprehensiveIdentification and support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Story implements Graph<Event>, ComprehensiveIdentification {

    private static final long serialVersionUID = 3L;

    private final Identification id;
    private final Map<String, Object> traits = new HashMap<>();

    private final Set<Event> events;
    private final Set<Edge<Event>> relations;

    /**
     * Creates a new Story graph with a generated ID.
     */
    public Story() {
        this(new SimpleIdentification("Story:" + UUID.randomUUID()));
    }

    /**
     * Creates a new Story graph with a specific ID.
     */
    public Story(Identification id) {
        this.id = Objects.requireNonNull(id);
        this.events = new HashSet<>();
        this.relations = new HashSet<>();
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
    public Set<Event> vertices() {
        return Collections.unmodifiableSet(events);
    }

    @Override
    public Set<Edge<Event>> edges() {
        return Collections.unmodifiableSet(relations);
    }

    @Override
    public int vertexCount() {
        return events.size();
    }

    @Override
    public boolean addVertex(Event event) {
        if (event == null) throw new IllegalArgumentException("Cannot add a null event.");
        return events.add(event);
    }

    @Override
    public boolean addEdge(Event source, Event target) {
        if (source == null || target == null) throw new IllegalArgumentException("Events cannot be null.");
        addVertex(source);
        addVertex(target);
        return relations.add(new StoryEdge(source, target));
    }

    @Override
    public Set<Event> neighbors(Event event) {
        return relations.stream()
                .filter(e -> e.source().equals(event))
                .map(Edge::target)
                .collect(Collectors.toSet());
    }

    @Override
    public int degree(Event event) {
        return (int) relations.stream()
                .filter(e -> e.source().equals(event) || e.target().equals(event))
                .count();
    }

    @Override
    public boolean isDirected() {
        return true; // Narrative flow is generally directed
    }

    /**
     * Adds an event to the story.
     * @param event the event to add
     */
    public void addEvent(Event event) {
        addVertex(event);
    }

    /**
     * Simple inner class to represent causal or temporal edges in the story.
     */
    private static class StoryEdge implements Edge<Event>, Serializable {
        private static final long serialVersionUID = 1L;
        private final Event source;
        private final Event target;

        StoryEdge(Event source, Event target) {
            this.source = source;
            this.target = target;
        }

        @Override public Event source() { return source; }
        @Override public Event target() { return target; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof StoryEdge)) return false;
            StoryEdge storyEdge = (StoryEdge) o;
            return Objects.equals(source, storyEdge.source) && Objects.equals(target, storyEdge.target);
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, target);
        }
    }

    /**
     * Infers new events based on causal links starting from a specific event.
     * Logic adapted to the Graph structure.
     *
     * @param question the starting event
     * @return set of reachable events
     */
    public Set<Event> solve(Event question) {
        if (question == null || !events.contains(question)) {
            return Collections.emptySet();
        }

        Set<Event> discovered = new HashSet<>();
        bfs(question, (event, depth) -> discovered.add(event));
        
        return discovered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Story story)) return false;
        return Objects.equals(id, story.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getName() != null ? getName() : id.toString();
    }
}
