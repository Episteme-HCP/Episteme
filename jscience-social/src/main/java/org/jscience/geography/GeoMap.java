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

package org.jscience.geography;

import org.jscience.earth.coordinates.GeodeticCoordinate;
import org.jscience.util.Named;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a logical map of a geographical area, containing places and paths.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class GeoMap implements Named, Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute
    private String name;

    @Attribute
    private GeodeticCoordinate topLeft;

    @Attribute
    private GeodeticCoordinate bottomRight;

    @Attribute
    private String description;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

// ...

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Image> layers = new ArrayList<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Place> places = new HashSet<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<GeoPath> paths = new HashSet<>();

    public GeoMap(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public GeoMap(String name, GeodeticCoordinate topLeft, GeodeticCoordinate bottomRight) {
        this(name);
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeodeticCoordinate getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(GeodeticCoordinate topLeft) {
        this.topLeft = topLeft;
    }

    public GeodeticCoordinate getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(GeodeticCoordinate bottomRight) {
        this.bottomRight = bottomRight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Place> getPlaces() {
        return Collections.unmodifiableSet(places);
    }

    public void addPlace(Place place) {
        places.add(Objects.requireNonNull(place));
    }

    public void removePlace(Place place) {
        places.remove(place);
    }

    public Set<GeoPath> getPaths() {
        return Collections.unmodifiableSet(paths);
    }

    public void addPath(GeoPath path) {
        paths.add(Objects.requireNonNull(path));
    }

    public void removePath(GeoPath path) {
        paths.remove(path);
    }

    @Override
    public String toString() {
        return String.format("Map: %s (%d places, %d paths)", name, places.size(), paths.size());
    }
}
