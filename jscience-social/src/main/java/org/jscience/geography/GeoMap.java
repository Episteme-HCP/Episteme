/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

/**
 * Represents a 2D map of a geographical area.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GeoMap {

    private String name;
    private double widthDegrees;
    private double heightDegrees;
    private Coordinate topLeftCoordinate;

    private final Set<Place> places;
    private final Set<GeoPath> paths;
    private String description;

    /**
     * Creates a map with the specified top-left coordinate and dimensions.
     * 
     * @param name              map name
     * @param topLeftCoordinate top-left corner coordinate
     * @param widthDegrees      width in degrees
     * @param heightDegrees     height in degrees
     */
    public GeoMap(String name, Coordinate topLeftCoordinate, double widthDegrees, double heightDegrees) {
        this.name = name;
        this.topLeftCoordinate = topLeftCoordinate;
        this.widthDegrees = widthDegrees;
        this.heightDegrees = heightDegrees;
        this.places = new HashSet<>();
        this.paths = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public Coordinate getTopLeftCoordinate() {
        return topLeftCoordinate;
    }

    public double getWidthDegrees() {
        return widthDegrees;
    }

    public double getHeightDegrees() {
        return heightDegrees;
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

    /**
     * Calculates the bottom-right coordinate of this map.
     */
    public Coordinate getBottomRightCoordinate() {
        double topLat = topLeftCoordinate.getLatitudeDegrees();
        double topLon = topLeftCoordinate.getLongitudeDegrees();

        // Height goes down (south), width goes right (east)
        double bottomLat = topLat - heightDegrees;
        double rightLon = topLon + widthDegrees;

        return new Coordinate(bottomLat, rightLon);
    }
}
