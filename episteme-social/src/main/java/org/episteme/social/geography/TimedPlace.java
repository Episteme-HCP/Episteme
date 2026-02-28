/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.geography;

import org.episteme.natural.earth.Place;
import org.episteme.natural.earth.PlaceType;
import org.episteme.natural.earth.coordinates.EarthCoordinate;
import org.episteme.social.history.time.TimeCoordinate;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.mathematics.geometry.boundaries.Boundary;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * A place that supports boundaries that change over time.
 * Extends the basic {@link Place} to add support for {@link TimedBoundary}.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class TimedPlace extends Place {

    private static final long serialVersionUID = 1L;

    @Attribute
    private final List<TimedBoundary> timedBoundaries = new ArrayList<>();

    public TimedPlace(String name) {
        super(name);
    }

    public TimedPlace(String name, PlaceType type) {
        super(name, type);
    }

    /**
     * Returns the list of timed boundaries for this place.
     */
    public List<TimedBoundary> getTimedBoundaries() {
        return Collections.unmodifiableList(timedBoundaries);
    }

    /**
     * Adds a timed boundary to this place.
     */
    public void addTimedBoundary(TimedBoundary boundary) {
        if (boundary != null) {
            timedBoundaries.add(boundary);
        }
    }

    /**
     * Returns the effective boundaries at a specific time.
     * Combines static boundaries and relevant snapshots from timed boundaries.
     */
    public List<Boundary<EarthCoordinate>> getBoundariesAt(TimeCoordinate time) {
        List<Boundary<EarthCoordinate>> result = new ArrayList<>(getBoundaries());
        for (TimedBoundary tb : timedBoundaries) {
            Boundary<?> b = tb.getAt(time);
            if (b != null) {
                // We assume these are EarthCoordinate boundaries
                @SuppressWarnings("unchecked")
                Boundary<EarthCoordinate> eb = (Boundary<EarthCoordinate>) b;
                result.add(eb);
            }
        }
        return result;
    }
}
