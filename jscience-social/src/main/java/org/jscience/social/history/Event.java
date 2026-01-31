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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.jscience.social.history.time.TimeCoordinate;
import org.jscience.core.util.Temporal;
import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.UUIDIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Represents a significant historical or scientific event.
 * Base class focusing on the temporal aspect of an event.
 * Implements ComprehensiveIdentification to support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Event implements ComprehensiveIdentification, Temporal<TimeCoordinate>, Comparable<Event> {

    private static final long serialVersionUID = 2L;

    @Id
    protected final Identification id;

    @Attribute
    protected final Map<String, Object> traits = new HashMap<>();



    @Relation(type = Relation.Type.ONE_TO_ONE)
    protected final TimeCoordinate when;

    @Attribute
    protected final EventCategory category;

    public Event(String name, String description, TimeCoordinate when, EventCategory category) {
        this.id = new UUIDIdentification(UUID.randomUUID().toString());
        setName(Objects.requireNonNull(name, "Name cannot be null"));
        setComments(description);
        this.when = Objects.requireNonNull(when, "Time coordinate cannot be null");
        this.category = category != null ? category : EventCategory.OTHER;
    }

    public Event(String name, TimeCoordinate when) {
        this(name, null, when, EventCategory.OTHER);
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public String getDescription() {
        return getComments();
    }

    @Override
    public TimeCoordinate getWhen() {
        return when;
    }

    public EventCategory getCategory() {
        return category;
    }

    public boolean happensBefore(Event other) {
        return this.when.compareTo(other.when) < 0;
    }

    public boolean happensAfter(Event other) {
        return this.when.compareTo(other.when) > 0;
    }

    @Override
    public int compareTo(Event o) {
        return this.when.compareTo(o.when);
    }

    @Override
    public String toString() {
        return String.format("%s (%s): %s", getName(), when, category);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event other)) return false;
        return Objects.equals(getId(), other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

