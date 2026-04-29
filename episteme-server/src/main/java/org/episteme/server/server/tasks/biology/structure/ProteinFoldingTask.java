/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.server.server.tasks.biology.structure;

import org.episteme.core.distributed.DistributedTask;
import org.episteme.core.distributed.TaskRegistry;
import org.episteme.core.distributed.TaskState;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.natural.biology.structure.ProteinFoldingProvider;
import org.episteme.natural.biology.structure.providers.StandardProteinFoldingProvider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.auto.service.AutoService;

/**
 * Protein Folding Simulation Task.
 */
@AutoService(DistributedTask.class)
public class ProteinFoldingTask implements DistributedTask<ProteinFoldingTask, ProteinFoldingTask> {

    public enum ResidueType { HYDROPHOBIC('H'), POLAR('P'); final char code; ResidueType(char c) { this.code = c; } }
    
    public static class Monomer implements Serializable {
        public int x, y, z;
        public ResidueType type;
        public Monomer(int x, int y, int z, ResidueType type) { this.x = x; this.y = y; this.z = z; this.type = type; }
        public int x() { return x; }
        public int y() { return y; }
        public int z() { return z; }
        public ResidueType type() { return type; }
    }
    
    private final List<ResidueType> sequence;
    private final boolean[] isHydrophobic;
    private final int iterations;
    private final double temperature;
    
    private TaskState<int[][]> state;
    private double currentEnergy;
    private TaskRegistry.PrecisionMode mode = TaskRegistry.PrecisionMode.DOUBLE;
    private long seed;

    public ProteinFoldingTask(String hpSequence, int iterations, double temperature) {
        this.sequence = new ArrayList<>();
        this.isHydrophobic = new boolean[hpSequence.length()];
        for (int i = 0; i < hpSequence.length(); i++) {
            char c = Character.toUpperCase(hpSequence.charAt(i));
            ResidueType type = (c == 'P' ? ResidueType.POLAR : ResidueType.HYDROPHOBIC);
            sequence.add(type);
            isHydrophobic[i] = (type == ResidueType.HYDROPHOBIC);
        }
        this.iterations = iterations;
        this.temperature = temperature;
        this.seed = System.nanoTime();
        
        int[][] initial = new int[isHydrophobic.length][3];
        for (int i = 0; i < initial.length; i++) { initial[i][0] = i; initial[i][1] = 0; initial[i][2] = 0; }
        
        this.state = new TaskState<>(initial,
            r -> flattenToDouble(r), d -> unflattenFromDouble(d),
            r -> flattenToFloat(r), f -> unflattenFromFloat(f)
        );
    }

    private double[] flattenToDouble(int[][] r) {
        double[] d = new double[r.length * 3];
        for(int i=0; i<r.length; i++) { d[i*3] = r[i][0]; d[i*3+1] = r[i][1]; d[i*3+2] = r[i][2]; }
        return d;
    }

    private int[][] unflattenFromDouble(double[] d) {
        int[][] r = new int[d.length/3][3];
        for(int i=0; i<r.length; i++) { r[i][0] = (int)d[i*3]; r[i][1] = (int)d[i*3+1]; r[i][2] = (int)d[i*3+2]; }
        return r;
    }

    private float[] flattenToFloat(int[][] r) {
        float[] f = new float[r.length * 3];
        for(int i=0; i<r.length; i++) { f[i*3] = r[i][0]; f[i*3+1] = r[i][1]; f[i*3+2] = r[i][2]; }
        return f;
    }

    private int[][] unflattenFromFloat(float[] f) {
        int[][] r = new int[f.length/3][3];
        for(int i=0; i<r.length; i++) { r[i][0] = (int)f[i*3]; r[i][1] = (int)f[i*3+1]; r[i][2] = (int)f[i*3+2]; }
        return r;
    }

    public ProteinFoldingTask() { this("", 0, 0); }

    public void setMode(TaskRegistry.PrecisionMode mode) {
        this.mode = mode;
        if(state != null) state.syncTo(mode);
    }

    @Override
    public Class<ProteinFoldingTask> getInputType() { return ProteinFoldingTask.class; }
    @Override
    public Class<ProteinFoldingTask> getOutputType() { return ProteinFoldingTask.class; }

    @Override
    public ProteinFoldingTask execute(ProteinFoldingTask input) {
        if (input != null && input.isHydrophobic.length > 0) {
            input.run();
            return input;
        }
        return null;
    }

    @Override
    public String getTaskType() { return "PROTEIN_FOLDING"; }

    public void run() {
        ProteinFoldingProvider provider = new StandardProteinFoldingProvider();
        int[][] pos = state.getReal();
        provider.simulate(pos, isHydrophobic, iterations, temperature, seed);
        
        switch (mode) {
            case REAL -> currentEnergy = provider.calculateEnergy(pos, isHydrophobic).doubleValue();
            case FLOAT -> currentEnergy = provider.calculateEnergyFloat(pos, isHydrophobic);
            default -> currentEnergy = provider.calculateEnergyDouble(pos, isHydrophobic);
        }
        seed = System.nanoTime();
    }

    public int[][] getFold() { return state.getReal(); }
    
    public List<Monomer> getResult() {
        int[][] pos = state.getReal();
        List<Monomer> res = new ArrayList<>();
        for(int i=0; i<pos.length; i++) res.add(new Monomer(pos[i][0], pos[i][1], pos[i][2], sequence.get(i)));
        return res;
    }

    public double getEnergy() { return currentEnergy; }
}
