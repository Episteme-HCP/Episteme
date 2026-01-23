package org.jscience.mathematics.geometry;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.util.UniversalDataModel;
import java.util.*;

/**
 * Universal data model for spatial visualization.
 * Tracks locations (with values) and flows between them.
 */
public class SpatialDataSet implements UniversalDataModel {

    @Override
    public String getModelType() { return "SPATIAL_GEOMETRY"; }

    public record Location(String id, String label, Real x, Real y, Map<String, Real> values) {}
    public record Flow(String fromId, String toId, Real intensity) {}

    private final List<Location> locations = new ArrayList<>();
    private final List<Flow> flows = new ArrayList<>();
    private Real minX, maxX, minY, maxY;

    public void addLocation(String id, String label, Real x, Real y) {
        locations.add(new Location(id, label, x, y, new HashMap<>()));
        updateBounds(x, y);
    }

    public void addValue(String locId, String key, Real value) {
        locations.stream().filter(l -> l.id().equals(locId)).findFirst()
                .ifPresent(l -> l.values().put(key, value));
    }

    public void addFlow(String from, String to, Real intensity) {
        flows.add(new Flow(from, to, intensity));
    }

    private void updateBounds(Real x, Real y) {
        if (minX == null || x.compareTo(minX) < 0) minX = x;
        if (maxX == null || x.compareTo(maxX) > 0) maxX = x;
        if (minY == null || y.compareTo(minY) < 0) minY = y;
        if (maxY == null || y.compareTo(maxY) > 0) maxY = y;
    }

    public List<Location> getLocations() { return Collections.unmodifiableList(locations); }
    public List<Flow> getFlows() { return Collections.unmodifiableList(flows); }
    public Real getMinX() { return minX; }
    public Real getMaxX() { return maxX; }
    public Real getMinY() { return minY; }
    public Real getMaxY() { return maxY; }
}
