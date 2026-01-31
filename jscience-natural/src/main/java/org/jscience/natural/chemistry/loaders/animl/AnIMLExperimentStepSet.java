/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.chemistry.loaders.animl;

import java.util.ArrayList;
import java.util.List;

public class AnIMLExperimentStepSet {
    private List<AnIMLExperimentStep> steps = new ArrayList<>();
    public List<AnIMLExperimentStep> getSteps() { return steps; }
    public void addStep(AnIMLExperimentStep step) { steps.add(step); }
}

