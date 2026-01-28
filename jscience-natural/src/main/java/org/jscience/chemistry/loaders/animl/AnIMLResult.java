/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.chemistry.loaders.animl;

import java.util.ArrayList;
import java.util.List;

public class AnIMLResult {
    private String name;
    private List<AnIMLSeries> seriesSet = new ArrayList<>();
    private List<AnIMLParameter> parameters = new ArrayList<>();
    private AnIMLPeakTable peakTable;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<AnIMLSeries> getSeriesSet() { return seriesSet; }
    public void addSeries(AnIMLSeries series) { seriesSet.add(series); }

    public List<AnIMLParameter> getParameters() { return parameters; }
    public void addParameter(AnIMLParameter parameter) { parameters.add(parameter); }

    public AnIMLPeakTable getPeakTable() { return peakTable; }
    public void setPeakTable(AnIMLPeakTable table) { this.peakTable = table; }
}
