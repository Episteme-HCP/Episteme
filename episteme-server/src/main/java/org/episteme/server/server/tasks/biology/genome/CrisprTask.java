/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.server.server.tasks.biology.genome;

import org.episteme.core.distributed.DistributedTask;
import org.episteme.natural.biology.genome.GenomeProvider;
import org.episteme.natural.biology.genome.providers.StandardGenomeProvider;
import com.google.auto.service.AutoService;
import java.util.ArrayList;
import java.util.List;

/**
 * CRISPR-Cas9 Target Scanning Task.
 */
@AutoService(DistributedTask.class)
public class CrisprTask implements DistributedTask<CrisprTask, ArrayList<GenomeProvider.Target>> {

    private final String sequence;
    private final String pamMotif;
    private final int spacerLength;

    public CrisprTask(String sequence) {
        this(sequence, "GG", 20);
    }

    public CrisprTask(String sequence, String pamMotif, int spacerLength) {
        this.sequence = sequence.toUpperCase().replaceAll("[^ATCG]", "");
        this.pamMotif = pamMotif.toUpperCase();
        this.spacerLength = spacerLength;
    }

    public CrisprTask() { this("", "GG", 20); }

    @Override
    public Class<CrisprTask> getInputType() { return CrisprTask.class; }
    
    @Override
    @SuppressWarnings("unchecked")
    public Class<ArrayList<GenomeProvider.Target>> getOutputType() { 
        return (Class<ArrayList<GenomeProvider.Target>>) (Class<?>) ArrayList.class; 
    }

    @Override
    public ArrayList<GenomeProvider.Target> execute(CrisprTask input) {
        if (input != null && !input.sequence.isEmpty()) {
            List<GenomeProvider.Target> result = input.scan();
            return new ArrayList<>(result);
        }
        return new ArrayList<>();
    }

    @Override
    public String getTaskType() { return "CRISPR_SCAN"; }

    public List<GenomeProvider.Target> scan() {
        GenomeProvider provider = new StandardGenomeProvider();
        return provider.scanCrispr(sequence, pamMotif, spacerLength);
    }
}
