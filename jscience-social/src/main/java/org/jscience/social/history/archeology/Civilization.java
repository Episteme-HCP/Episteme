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

package org.jscience.social.history.archeology;

import java.util.Objects;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.jscience.natural.earth.Place;
import org.jscience.social.history.time.TimeCoordinate;
import org.jscience.social.sociology.Culture;
import org.jscience.core.util.Positioned;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.SimpleIdentification;
import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Represents a civilization, modeled as a culture distributed through space and time.
 * Covers both living and historical societies, tracking their geographical extent and duration.
 * Modernized to implement ComprehensiveIdentification and support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Civilization implements Positioned<Place>, ComprehensiveIdentification {

    private static final long serialVersionUID = 3L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Culture culture;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Place place;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private final TimeCoordinate startDate;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private TimeCoordinate endDate;

    public Civilization(Culture culture, Place place, TimeCoordinate startDate) {
        this.id = new SimpleIdentification("Civilization:" + UUID.randomUUID());
        this.culture = Objects.requireNonNull(culture, "Culture cannot be null");
        setName(culture.getName());
        this.place = Objects.requireNonNull(place, "Place cannot be null");
        this.startDate = Objects.requireNonNull(startDate, "Start date cannot be null");
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public Culture getCulture() {
        return culture;
    }

    @Override
    public Place getPosition() {
        return place;
    }

    public TimeCoordinate getStartDate() {
        return startDate;
    }

    public TimeCoordinate getEndDate() {
        return endDate;
    }

    public void setEndDate(TimeCoordinate endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return getComments();
    }

    public void setDescription(String description) {
        setComments(description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Civilization that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s in %s (%s - %s)", culture, place, startDate, 
            endDate != null ? endDate : "present");
    }
}

