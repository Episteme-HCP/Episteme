/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.biology.genome.providers;

import org.episteme.natural.biology.genome.GenomeProvider;
import org.episteme.core.mathematics.discrete.TextAlgorithms;
import java.util.ArrayList;
import java.util.List;

/**
 * Standard implementation of genome analysis.
 */
public class StandardGenomeProvider implements GenomeProvider {

    @Override
    public List<Target> scanCrispr(String sequence, String pamMotif, int spacerLength) {
        List<Target> targets = new ArrayList<>();
        List<Integer> pamPositions = TextAlgorithms.shiftOrSearch(sequence, pamMotif);

        for (int pos : pamPositions) {
            int totalBack = spacerLength + 1;
            if (pos >= totalBack) {
                int start = pos - totalBack;
                String fullMatch = sequence.substring(start, pos + pamMotif.length());
                String spacer = fullMatch.substring(0, spacerLength);
                String pam = fullMatch.substring(spacerLength);

                double score = evaluateEfficiency(spacer);
                targets.add(new Target(start + 1, spacer, pam, score));
            }
        }
        return targets;
    }

    private double evaluateEfficiency(String spacer) {
        double gc = 0;
        for (char c : spacer.toCharArray()) if (c == 'G' || c == 'C') gc++;
        double gcPercent = gc / (double) spacer.length();
        double score = 100.0 * (1.0 - Math.abs(0.5 - gcPercent) * 2.0);
        if (spacer.length() >= 20 && spacer.charAt(19) == 'G') score += 10;
        return Math.max(0, Math.min(100, score));
    }

    @Override
    public String getName() { return "Standard Genome Analysis"; }
    @Override
    public String getAlgorithmType() { return "Biology"; }
}
