/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.chemistry.loaders.animl;

import java.util.ArrayList;
import java.util.List;

public class AnIMLSampleSet {
    private List<AnIMLSample> samples = new ArrayList<>();
    public List<AnIMLSample> getSamples() { return samples; }
    public void addSample(AnIMLSample sample) { samples.add(sample); }
}

