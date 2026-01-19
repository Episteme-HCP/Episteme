package org.jscience.health;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.data.UniversalDataModel;
import java.util.*;

/**
 * Universal data model for performance tracking and human evolution trajectories.
 */
public final class TrajectoryDataSet implements UniversalDataModel {

    @Override
    public String getModelType() { return "EVOLUTION_TRAJECTORY"; }

    public record DataPoint(double time, Real value) {}
    public record Series(String id, String label, ColorType color, List<DataPoint> points) {}
    
    public enum ColorType { VITAL, STRESS, RECOVERY }

    private final Map<String, Series> seriesMap = new LinkedHashMap<>();

    public void addPoint(String seriesId, String label, ColorType color, double time, Real value) {
        Series s = seriesMap.computeIfAbsent(seriesId, k -> new Series(seriesId, label, color, new ArrayList<>()));
        s.points().add(new DataPoint(time, value));
    }

    public Collection<Series> getSeries() { return seriesMap.values(); }
}
