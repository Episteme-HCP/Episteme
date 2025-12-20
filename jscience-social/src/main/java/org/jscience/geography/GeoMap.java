package org.jscience.geography;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a 2D map of a geographical area.
 */
public class GeoMap {

    private String name;
    private double width; // in meters
    private double height; // in meters
    private Coordinate topLeftCoordinate;

    private final Set<Place> places;
    private final Set<GeoPath> paths;
    private String description;

    public GeoMap(String name, Coordinate topLeftCoordinate, double width, double height) {
        this.name = name;
        this.topLeftCoordinate = topLeftCoordinate;
        this.width = width;
        this.height = height;
        this.places = new HashSet<>();
        this.paths = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public Coordinate getTopLeftCoordinate() {
        return topLeftCoordinate;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
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

    /**
     * Estimates bottom right coordinate based on width/height and simple
     * projection.
     * Assumes small area (flat earth approximation sufficient for simple bounding
     * box).
     */
    public Coordinate getBottomRightCoordinate() {
        // Simple approximation: 1 degree lat ~= 111km
        // 1 degree lon ~= 111km * cos(lat)

        double latOffset = -(height / 111000.0); // Going down (South)
        double lonOffset = width / (111000.0 * Math.cos(Math.toRadians(topLeftCoordinate.getLatitude()))); // Going
                                                                                                           // right
                                                                                                           // (East)

        return new Coordinate(
                topLeftCoordinate.getLatitude() + latOffset,
                topLeftCoordinate.getLongitude() + lonOffset);
    }
}
