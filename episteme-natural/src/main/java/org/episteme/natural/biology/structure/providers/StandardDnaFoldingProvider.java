/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.biology.structure.providers;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.natural.biology.structure.DnaFoldingProvider;
import java.util.List;
import java.util.Random;

/**
 * Standard implementation of DNA/RNA folding using a Metropolis-Hastings energy model.
 */
public class StandardDnaFoldingProvider implements DnaFoldingProvider {

    private static final double IDEAL_DIST = 3.4;
    private static final double K_BOND = 10.0;
    private static final double PAIR_ENERGY = -10.0;
    private static final double CLASH_ENERGY = 1000.0;

    @Override
    public Real calculateEnergy(List<Real[]> points, String sequence) {
        Real energy = Real.of(0);
        Real idealDistR = Real.of(IDEAL_DIST);
        Real kR = Real.of(K_BOND);

        for (int i = 0; i < points.size() - 1; i++) {
            Real dist = distance(points.get(i), points.get(i+1));
            energy = energy.add(kR.multiply(dist.subtract(idealDistR).pow(2)));
        }

        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 2; j < points.size(); j++) {
                Real dist = distance(points.get(i), points.get(j));
                double d = dist.doubleValue();
                if (d < 1.0) energy = energy.add(Real.of(CLASH_ENERGY));
                else if (d < 6.0 && isPair(sequence.charAt(i), sequence.charAt(j)))
                    energy = energy.subtract(Real.of(10.0).divide(dist));
            }
        }
        return energy;
    }

    @Override
    public double calculateEnergy(double[][] points, String sequence) {
        double energy = 0;
        for (int i = 0; i < points.length - 1; i++) {
            double dist = distance(points[i], points[i+1]);
            energy += K_BOND * Math.pow(dist - IDEAL_DIST, 2);
        }
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 2; j < points.length; j++) {
                double dist = distance(points[i], points[j]);
                if (dist < 1.0) energy += CLASH_ENERGY;
                else if (dist < 6.0 && isPair(sequence.charAt(i), sequence.charAt(j)))
                    energy += PAIR_ENERGY / dist;
            }
        }
        return energy;
    }

    @Override
    public float calculateEnergy(float[][] points, String sequence) {
        float energy = 0f;
        for (int i = 0; i < points.length - 1; i++) {
            float dist = distance(points[i], points[i+1]);
            energy += (float)(K_BOND * Math.pow(dist - IDEAL_DIST, 2));
        }
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 2; j < points.length; j++) {
                float dist = distance(points[i], points[j]);
                if (dist < 1.0f) energy += (float)CLASH_ENERGY;
                else if (dist < 6.0f && isPair(sequence.charAt(i), sequence.charAt(j)))
                    energy += (float)(PAIR_ENERGY / dist);
            }
        }
        return energy;
    }

    @Override
    public void step(List<Real[]> points, String sequence, double temperature, long seed) {
        Random rand = new Random(seed);
        int idx = rand.nextInt(sequence.length());
        Real[] originalPos = points.get(idx).clone();
        
        Real currentEnergy = calculateEnergy(points, sequence);
        
        Real dx = Real.of((rand.nextDouble() - 0.5) * 2.0);
        Real dy = Real.of((rand.nextDouble() - 0.5) * 2.0);
        Real dz = Real.of((rand.nextDouble() - 0.5) * 2.0);
        
        points.set(idx, new Real[]{originalPos[0].add(dx), originalPos[1].add(dy), originalPos[2].add(dz)});
        
        Real newEnergy = calculateEnergy(points, sequence);
        
        if (!(newEnergy.doubleValue() < currentEnergy.doubleValue() 
            || Math.exp(-(newEnergy.subtract(currentEnergy).doubleValue()) / temperature) > rand.nextDouble())) {
            points.set(idx, originalPos);
        }
    }

    @Override
    public void step(double[][] points, String sequence, double temperature, long seed) {
        Random rand = new Random(seed);
        int idx = rand.nextInt(sequence.length());
        double[] originalPos = points[idx].clone();
        
        double currentEnergy = calculateEnergy(points, sequence);
        
        points[idx][0] += (rand.nextDouble() - 0.5) * 2.0;
        points[idx][1] += (rand.nextDouble() - 0.5) * 2.0;
        points[idx][2] += (rand.nextDouble() - 0.5) * 2.0;
        
        double newEnergy = calculateEnergy(points, sequence);
        
        if (!(newEnergy < currentEnergy || Math.exp(-(newEnergy - currentEnergy) / temperature) > rand.nextDouble())) {
            points[idx] = originalPos;
        }
    }

    @Override
    public void step(float[][] points, String sequence, float temperature, long seed) {
        Random rand = new Random(seed);
        int idx = rand.nextInt(sequence.length());
        float[] originalPos = points[idx].clone();
        
        float currentEnergy = calculateEnergy(points, sequence);
        
        points[idx][0] += (float)(rand.nextDouble() - 0.5) * 2.0;
        points[idx][1] += (float)(rand.nextDouble() - 0.5) * 2.0;
        points[idx][2] += (float)(rand.nextDouble() - 0.5) * 2.0;
        
        float newEnergy = calculateEnergy(points, sequence);
        
        if (!(newEnergy < currentEnergy || Math.exp(-(newEnergy - currentEnergy) / temperature) > rand.nextDouble())) {
            points[idx] = originalPos;
        }
    }

    @Override
    public double calculateEnergy(double[] flat, String sequence) {
        double energy = 0;
        int n = sequence.length();
        for (int i = 0; i < n - 1; i++) {
            double dx = flat[i*3] - flat[(i+1)*3];
            double dy = flat[i*3+1] - flat[(i+1)*3+1];
            double dz = flat[i*3+2] - flat[(i+1)*3+2];
            double dist = Math.sqrt(dx*dx + dy*dy + dz*dz);
            energy += K_BOND * Math.pow(dist - IDEAL_DIST, 2);
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 2; j < n; j++) {
                double dx = flat[i*3] - flat[j*3];
                double dy = flat[i*3+1] - flat[j*3+1];
                double dz = flat[i*3+2] - flat[j*3+2];
                double dist = Math.sqrt(dx*dx + dy*dy + dz*dz);
                if (dist < 1.0) energy += CLASH_ENERGY;
                else if (dist < 6.0 && isPair(sequence.charAt(i), sequence.charAt(j)))
                    energy += PAIR_ENERGY / dist;
            }
        }
        return energy;
    }

    @Override
    public float calculateEnergy(float[] flat, String sequence) {
        float energy = 0;
        int n = sequence.length();
        for (int i = 0; i < n - 1; i++) {
            float dx = flat[i*3] - flat[(i+1)*3];
            float dy = flat[i*3+1] - flat[(i+1)*3+1];
            float dz = flat[i*3+2] - flat[(i+1)*3+2];
            float dist = (float)Math.sqrt(dx*dx + dy*dy + dz*dz);
            energy += (float)(K_BOND * Math.pow(dist - IDEAL_DIST, 2));
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 2; j < n; j++) {
                float dx = flat[i*3] - flat[j*3];
                float dy = flat[i*3+1] - flat[j*3+1];
                float dz = flat[i*3+2] - flat[j*3+2];
                float dist = (float)Math.sqrt(dx*dx + dy*dy + dz*dz);
                if (dist < 1.0f) energy += (float)CLASH_ENERGY;
                else if (dist < 6.0f && isPair(sequence.charAt(i), sequence.charAt(j)))
                    energy += (float)(PAIR_ENERGY / dist);
            }
        }
        return energy;
    }

    @Override
    public void step(double[] flat, String sequence, double temperature, long seed) {
        Random rand = new Random(seed);
        int idx = rand.nextInt(sequence.length());
        double ox = flat[idx*3], oy = flat[idx*3+1], oz = flat[idx*3+2];
        double currentEnergy = calculateEnergy(flat, sequence);
        flat[idx*3] += (rand.nextDouble() - 0.5) * 2.0;
        flat[idx*3+1] += (rand.nextDouble() - 0.5) * 2.0;
        flat[idx*3+2] += (rand.nextDouble() - 0.5) * 2.0;
        double newEnergy = calculateEnergy(flat, sequence);
        if (!(newEnergy < currentEnergy || Math.exp(-(newEnergy - currentEnergy) / temperature) > rand.nextDouble())) {
            flat[idx*3] = ox; flat[idx*3+1] = oy; flat[idx*3+2] = oz;
        }
    }

    @Override
    public void step(float[] flat, String sequence, float temperature, long seed) {
        Random rand = new Random(seed);
        int idx = rand.nextInt(sequence.length());
        float ox = flat[idx*3], oy = flat[idx*3+1], oz = flat[idx*3+2];
        float currentEnergy = calculateEnergy(flat, sequence);
        flat[idx*3] += (float)(rand.nextDouble() - 0.5) * 2.0;
        flat[idx*3+1] += (float)(rand.nextDouble() - 0.5) * 2.0;
        flat[idx*3+2] += (float)(rand.nextDouble() - 0.5) * 2.0;
        float newEnergy = calculateEnergy(flat, sequence);
        if (!(newEnergy < currentEnergy || Math.exp(-(newEnergy - currentEnergy) / temperature) > rand.nextDouble())) {
            flat[idx*3] = ox; flat[idx*3+1] = oy; flat[idx*3+2] = oz;
        }
    }

    private Real distance(Real[] p1, Real[] p2) {
        return p1[0].subtract(p2[0]).pow(2)
                .add(p1[1].subtract(p2[1]).pow(2))
                .add(p1[2].subtract(p2[2]).pow(2)).sqrt();
    }

    private double distance(double[] p1, double[] p2) {
        double dx = p1[0] - p2[0];
        double dy = p1[1] - p2[1];
        double dz = p1[2] - p2[2];
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }

    private float distance(float[] p1, float[] p2) {
        float dx = p1[0] - p2[0];
        float dy = p1[1] - p2[1];
        float dz = p1[2] - p2[2];
        return (float)Math.sqrt(dx*dx + dy*dy + dz*dz);
    }

    private boolean isPair(char b1, char b2) {
        return (b1 == 'A' && b2 == 'T') || (b1 == 'T' && b2 == 'A') || (b1 == 'C' && b2 == 'G')
                || (b1 == 'G' && b2 == 'C');
    }

    @Override
    public String getName() { return "Standard Metropolis-Hastings Folding"; }
    @Override
    public String getAlgorithmType() { return "Biology"; }
}
