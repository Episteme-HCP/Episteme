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

package org.jscience.chemistry.loaders.animl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an experiment step in AnIML analytical data.
 * <p>
 * An experiment step contains the results of a single analytical measurement,
 * including technique information and series data.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class AnIMLExperimentStep {

    private String id;
    private String name;
    private String techniqueName;
    private String techniqueUri;
    private final List<AnIMLSeriesData> seriesData = new ArrayList<>();

    public AnIMLExperimentStep() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTechniqueName() {
        return techniqueName;
    }

    public void setTechniqueName(String techniqueName) {
        this.techniqueName = techniqueName;
    }

    private AnIMLResult result;
    private AnIMLMethod method;

    public String getExperimentStepId() {
        return id;
    }

    public void setExperimentStepId(String id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTechniqueUri() {
        return techniqueUri;
    }

    public void setTechniqueUri(String techniqueUri) {
        this.techniqueUri = techniqueUri;
    }

    public AnIMLResult getResult() {
        return result;
    }

    public void setResult(AnIMLResult result) {
        this.result = result;
    }

    public AnIMLMethod getMethod() {
        return method;
    }

    public void setMethod(AnIMLMethod method) {
        this.method = method;
    }

    public List<AnIMLSeriesData> getSeriesData() {
        // Return results series if available, otherwise internal list
        if (result != null && !result.getSeriesSet().isEmpty()) {
            List<AnIMLSeriesData> data = new ArrayList<>();
            for (AnIMLSeries s : result.getSeriesSet()) {
                AnIMLSeriesData sd = new AnIMLSeriesData();
                sd.setName(s.getName());
                sd.setValues(s.getData());
                sd.setUnitLabel(s.getUnit());
                sd.setDependency(s.getSeriesType());
                data.add(sd);
            }
            return data;
        }
        return Collections.unmodifiableList(seriesData);
    }

    public void addSeriesData(AnIMLSeriesData data) {
        if (data != null) {
            seriesData.add(data);
        }
    }

    /**
     * Returns the first series marked as independent (e.g., wavelength, time).
     */
    public AnIMLSeriesData getIndependentSeries() {
        for (AnIMLSeriesData series : seriesData) {
            if ("independent".equals(series.getDependency())) {
                return series;
            }
        }
        return null;
    }

    /**
     * Returns all dependent series (e.g., absorbance, intensity).
     */
    public List<AnIMLSeriesData> getDependentSeries() {
        List<AnIMLSeriesData> dependent = new ArrayList<>();
        for (AnIMLSeriesData series : seriesData) {
            if ("dependent".equals(series.getDependency())) {
                dependent.add(series);
            }
        }
        return dependent;
    }

    @Override
    public String toString() {
        return "AnIMLExperimentStep{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", technique='" + techniqueName + '\'' +
                ", series=" + seriesData.size() +
                '}';
    }
}
