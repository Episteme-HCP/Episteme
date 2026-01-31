/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.chemistry.spectroscopy;

import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.SimpleIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Base class for all spectra (IR, NMR, Mass, UV-Vis, etc.).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Spectrum implements ComprehensiveIdentification {
    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Attribute
    private double[] xData;

    @Attribute
    private double[] yData;

    @Attribute
    private String xUnit;

    @Attribute
    private String yUnit;

    @Attribute
    private String xLabel;

    @Attribute
    private String yLabel;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<SpectralPeak> peaks = new ArrayList<>();

    public Spectrum(String name) {
        this.id = new SimpleIdentification("SPEC:" + UUID.randomUUID());
        setName(name);
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public void setXData(double[] data) { this.xData = data; }
    public void setYData(double[] data) { this.yData = data; }
    public void setXUnit(String unit) { this.xUnit = unit; }
    public void setYUnit(String unit) { this.yUnit = unit; }
    public void setXLabel(String label) { this.xLabel = label; }
    public void setYLabel(String label) { this.yLabel = label; }

    public double[] getXData() { return xData; }
    public double[] getYData() { return yData; }

    public void addPeak(SpectralPeak peak) {
        peaks.add(peak);
    }

    public List<SpectralPeak> getPeaks() {
        return peaks;
    }

    @Override
    public String toString() {
        return String.format("Spectrum[%s, %d points, %d peaks]", 
            getName(), xData != null ? xData.length : 0, peaks.size());
    }
}

