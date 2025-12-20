package org.jscience.geography;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a path or track consisting of a sequence of coordinates.
 */
public class GeoPath {

    private final List<Coordinate> coordinates;

    public GeoPath(List<Coordinate> coordinates) {
        if (coordinates == null || coordinates.isEmpty()) {
            throw new IllegalArgumentException("Path must have at least one coordinate");
        }
        this.coordinates = new ArrayList<>(coordinates);
    }

    public List<Coordinate> getCoordinates() {
        return Collections.unmodifiableList(coordinates);
    }

    public Coordinate getStart() {
        return coordinates.get(0);
    }

    public Coordinate getEnd() {
        return coordinates.get(coordinates.size() - 1);
    }

    public boolean isClosed() {
        if (coordinates.size() < 2)
            return false;
        Coordinate start = getStart();
        Coordinate end = getEnd();
        // Simple distinct object check or value check. Coordinate doesn't have equals
        // yet?
        // We should assume referential or value equality.
        // Let's implement primitive check.
        return Math.abs(start.getLatitude() - end.getLatitude()) < 1e-9 &&
                Math.abs(start.getLongitude() - end.getLongitude()) < 1e-9;
    }

    public double getLength() {
        double length = 0;
        for (int i = 0; i < coordinates.size() - 1; i++) {
            length += coordinates.get(i).distanceTo(coordinates.get(i + 1));
        }
        return length;
    }

    public GeoPath reverse() {
        List<Coordinate> reversed = new ArrayList<>(coordinates);
        Collections.reverse(reversed);
        return new GeoPath(reversed);
    }
}
