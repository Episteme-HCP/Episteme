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

package org.jscience.social.history;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.jscience.social.history.time.TimeCoordinate;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.SimpleIdentification;
import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * A sequence or collection of events organized in time.
 * Modernized to implement ComprehensiveIdentification for dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Timeline<E extends Event> implements ComprehensiveIdentification {

    private static final long serialVersionUID = 3L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    protected final List<E> events = new ArrayList<>();

    public Timeline(String name) {
        this.id = new SimpleIdentification("Timeline:" + UUID.randomUUID());
        setName(Objects.requireNonNull(name, "Name cannot be null"));
    }

    public Timeline() {
        this("Untitled Timeline");
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public Timeline<E> addEvent(E event) {
        events.add(Objects.requireNonNull(event, "Event cannot be null"));
        return this;
    }

    public boolean removeEvent(E event) {
        return events.remove(event);
    }

    public List<E> getEvents() {
        return events.stream()
                .sorted(Comparator.comparing(Event::getWhen))
                .toList();
    }

    public List<E> getEventsBetween(TimeCoordinate start, TimeCoordinate end) {
        return events.stream()
                .filter(e -> e.getWhen().compareTo(start) >= 0 &&
                        e.getWhen().compareTo(end) <= 0)
                .sorted(Comparator.comparing(Event::getWhen))
                .toList();
    }

    public Optional<E> getEarliestEvent() {
        return events.stream().min(Comparator.comparing(Event::getWhen));
    }

    public Optional<E> getLatestEvent() {
        return events.stream().max(Comparator.comparing(Event::getWhen));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Timeline<?> timeline)) return false;
        return Objects.equals(id, timeline.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s with %d events", getName(), events.size());
    }
}

