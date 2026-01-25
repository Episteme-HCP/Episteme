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

package org.jscience.history;

import java.util.Objects;
import java.util.UUID;
import org.jscience.history.temporal.TemporalCoordinate;
import org.jscience.util.Temporal;
import org.jscience.util.identity.AbstractIdentifiedEntity;
import org.jscience.util.identity.UUIDIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a significant historical or scientific event.
 * Base class focusing on the temporal aspect of an event.
 * Extends AbstractIdentifiedEntity to support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Event extends AbstractIdentifiedEntity implements Temporal<TemporalCoordinate>, Comparable<Event> {

    private static final long serialVersionUID = 2L;

    public enum Category {
        POLITICAL, MILITARY, CULTURAL, SCIENTIFIC, ECONOMIC, RELIGIOUS, NATURAL, OTHER
    }

    @Relation(type = Relation.Type.ONE_TO_ONE)
    protected final TemporalCoordinate when;

    @Attribute
    protected final Category category;

    public Event(String name, String description, TemporalCoordinate when, Category category) {
        super(new UUIDIdentification(UUID.randomUUID().toString()));
        setName(Objects.requireNonNull(name, "Name cannot be null"));
        setComments(description);
        this.when = Objects.requireNonNull(when, "Time coordinate cannot be null");
        this.category = category != null ? category : Category.OTHER;
    }

    public Event(String name, TemporalCoordinate when) {
        this(name, null, when, Category.OTHER);
    }

    public String getDescription() {
        return getComments();
    }

    @Override
    public TemporalCoordinate getWhen() {
        return when;
    }

    public Category getCategory() {
        return category;
    }

    public boolean happensBefore(Event other) {
        return this.when.toInstant().isBefore(other.when.toInstant());
    }

    public boolean happensAfter(Event other) {
        return this.when.toInstant().isAfter(other.when.toInstant());
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
