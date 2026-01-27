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

package org.jscience.sports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.util.UniversalDataModel;

/**
 * Universal data model for performance tracking and human evolution trajectories.
 * Used for storing time-series data related to athletic performance or biological evolution.
 */
public final class TrajectoryDataSet implements UniversalDataModel, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String getModelType() { return "EVOLUTION_TRAJECTORY"; }

    /** Individual data point on a timeline. */
    public record DataPoint(double time, Real value) implements Serializable {}

    /** A labeled series of related data points. */
    public record Series(String id, String label, ColorType color, List<DataPoint> points) implements Serializable {}
    
    /** Color categories for visualization.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum ColorType { VITAL, STRESS, RECOVERY }

    private final Map<String, Series> seriesMap = new LinkedHashMap<>();

    /**
     * Appends a new data point to a specific series.
     */
    public void addPoint(String seriesId, String label, ColorType color, double time, Real value) {
        Series s = seriesMap.computeIfAbsent(seriesId, k -> new Series(seriesId, label, color, new ArrayList<>()));
        s.points().add(new DataPoint(time, value));
    }

    /** Returns all registered data series. */
    public Collection<Series> getSeries() { return seriesMap.values(); }
}
