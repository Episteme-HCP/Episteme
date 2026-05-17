/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.biology.genome;

import org.episteme.core.technical.algorithm.AlgorithmProvider;
import java.util.List;
import java.io.Serializable;

/**
 * Provider for genomic analysis algorithms.
 */
public interface GenomeProvider extends AlgorithmProvider {

    record Target(int position, String spacer, String pam, double score) implements Serializable {}

    /**
     * Scans for CRISPR targets.
     */
    List<Target> scanCrispr(String sequence, String pamMotif, int spacerLength);
}
