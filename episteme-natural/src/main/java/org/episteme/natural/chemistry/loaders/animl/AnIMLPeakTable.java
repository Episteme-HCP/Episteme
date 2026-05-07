/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.chemistry.loaders.animl;


import java.util.ArrayList;
import java.util.List;

public class AnIMLPeakTable {
    private List<AnIMLPeak> peaks = new ArrayList<>();
    public List<AnIMLPeak> getPeaks() { return peaks; }
    public void addPeak(AnIMLPeak peak) { peaks.add(peak); }
}

