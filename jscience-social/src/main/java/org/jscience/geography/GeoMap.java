/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a 2D map of a geographical area.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GeoMap {

    private String name;
    private Real width;
    private Real height;
    private Coordinate topLeftCoordinate;

    private final Set<Place> places;
    private final Set<GeoPath> paths;
    private String description;

    public GeoMap(String name, Coordinate topLeftCoordinate, Real width, Real height) {
        this.name = name;
        this.topLeftCoordinate = topLeftCoordinate;
        this.width = width;
        this.height = height;
        this.places = new HashSet<>();
        this.paths = new HashSet<>();
    }

    public GeoMap(String name, Coordinate topLeftCoordinate, double width, double height) {
        this(name, topLeftCoordinate, Real.of(width), Real.of(height));
    }

    public String getName() {
        return name;
    }

    public Coordinate getTopLeftCoordinate() {
        return topLeftCoordinate;
    }

    public Real getWidth() {
        return width;
    }

    public Real getHeight() {
        return height;
    }

    public Set<Place> getPlaces() {
        return Collections.unmodifiableSet(places);
    }

    public void addPlace(Place place) {
        places.add(place);
    }

    public void removePlace(Place place) {
        places.remove(place);
    }

    public Set<GeoPath> getPaths() {
        return Collections.unmodifiableSet(paths);
    }

    public void addPath(GeoPath path) {
        paths.add(path);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Coordinate getBottomRightCoordinate() {
        Real latOffset = height.negate().divide(Real.of(111000));
        Real cosLat = topLeftCoordinate.getLatitude().toRadians().cos();
        Real lonOffset = width.divide(Real.of(111000).multiply(cosLat));

        return new Coordinate(
                topLeftCoordinate.getLatitude().add(latOffset),
                topLeftCoordinate.getLongitude().add(lonOffset));
    }
}
