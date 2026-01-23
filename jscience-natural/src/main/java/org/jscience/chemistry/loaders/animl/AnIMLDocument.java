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
 * Represents an AnIML document containing analytical chemistry data.
 * <p>
 * An AnIML document consists of samples, experiment steps, and associated
 * series data representing spectroscopic or chromatographic measurements.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class AnIMLDocument {

    private final List<AnIMLSample> samples = new ArrayList<>();
    private final List<AnIMLExperimentStep> experimentSteps = new ArrayList<>();
    private String version;

    /**
     * Creates an empty AnIML document.
     */
    public AnIMLDocument() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Returns an unmodifiable list of samples.
     */
    public List<AnIMLSample> getSamples() {
        return Collections.unmodifiableList(samples);
    }

    /**
     * Adds a sample to this document.
     */
    public void addSample(AnIMLSample sample) {
        if (sample != null) {
            samples.add(sample);
        }
    }

    /**
     * Returns an unmodifiable list of experiment steps.
     */
    public List<AnIMLExperimentStep> getExperimentSteps() {
        return Collections.unmodifiableList(experimentSteps);
    }

    /**
     * Adds an experiment step to this document.
     */
    public void addExperimentStep(AnIMLExperimentStep step) {
        if (step != null) {
            experimentSteps.add(step);
        }
    }

    /**
     * Returns the total number of data series across all experiment steps.
     */
    public int getTotalSeriesCount() {
        int count = 0;
        for (AnIMLExperimentStep step : experimentSteps) {
            count += step.getSeriesData().size();
        }
        return count;
    }

    @Override
    public String toString() {
        return "AnIMLDocument{" +
                "samples=" + samples.size() +
                ", experimentSteps=" + experimentSteps.size() +
                ", totalSeries=" + getTotalSeriesCount() +
                '}';
    }
}
