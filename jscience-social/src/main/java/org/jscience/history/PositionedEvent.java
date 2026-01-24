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

import org.jscience.geography.Place;
import org.jscience.history.temporal.TemporalCoordinate;
import org.jscience.util.Positioned;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * An event that also has a geographical position.
 * Focuses on the association with a {@link Place}.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public class PositionedEvent extends Event implements Positioned<Place> {

    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Place position;

    public PositionedEvent(String name, String description, TemporalCoordinate when, Place position, Category category) {
        super(name, description, when, category);
        this.position = position;
    }

    public PositionedEvent(String name, TemporalCoordinate when, Place position) {
        this(name, null, when, position, Category.OTHER);
    }

    /**
     * Returns the place where this event occurred.
     * 
     * @return the position as a Place
     */
    public Place getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) at %s: %s", name, when, position, category);
    }
}
