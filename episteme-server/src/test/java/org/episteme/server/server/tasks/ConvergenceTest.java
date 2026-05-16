/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.server.server.tasks;

import org.episteme.core.distributed.TaskRegistry;
import org.episteme.server.server.tasks.mathematics.mandelbrot.MandelbrotTask;
import org.episteme.server.server.tasks.mathematics.montecarlo.MonteCarloPiTask;
import org.episteme.server.server.tasks.physics.wave.WaveSimTask;
import org.episteme.server.server.tasks.physics.nbody.NBodyTask;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConvergenceTest {

    @Test
    public void testMonteCarloConvergence() {
        long samples = 1000000;
        MonteCarloPiTask taskDouble = new MonteCarloPiTask(samples);
        taskDouble.setMode(TaskRegistry.PrecisionMode.DOUBLE);
        long resultDouble = taskDouble.execute(samples);

        MonteCarloPiTask taskFloat = new MonteCarloPiTask(samples);
        taskFloat.setMode(TaskRegistry.PrecisionMode.FLOAT);
        long resultFloat = taskFloat.execute(samples);

        MonteCarloPiTask taskReal = new MonteCarloPiTask(samples);
        taskReal.setMode(TaskRegistry.PrecisionMode.REAL);
        long resultReal = taskReal.execute(samples);

        System.out.println("MC Pi Double: " + (4.0 * resultDouble / samples));
        System.out.println("MC Pi Float:  " + (4.0 * resultFloat / samples));
        System.out.println("MC Pi Real:   " + (4.0 * resultReal / samples));

        // Statistical check (within 1% for 1M samples usually)
        double piDouble = 4.0 * resultDouble / samples;
        double piFloat = 4.0 * resultFloat / samples;
        double piReal = 4.0 * resultReal / samples;

        assertTrue(Math.abs(piDouble - Math.PI) < 0.01);
        assertTrue(Math.abs(piFloat - Math.PI) < 0.01);
        assertTrue(Math.abs(piReal - Math.PI) < 0.01);
    }

    @Test
    public void testMandelbrotConvergence() {
        int w = 100, h = 100;
        MandelbrotTask taskDouble = new MandelbrotTask(w, h, -2.0, 0.5, -1.25, 1.25);
        taskDouble.setMode(TaskRegistry.PrecisionMode.DOUBLE);
        taskDouble.compute();

        MandelbrotTask taskFloat = new MandelbrotTask(w, h, -2.0, 0.5, -1.25, 1.25);
        taskFloat.setMode(TaskRegistry.PrecisionMode.FLOAT);
        taskFloat.compute();

        MandelbrotTask taskReal = new MandelbrotTask(w, h, -2.0, 0.5, -1.25, 1.25);
        taskReal.setMode(TaskRegistry.PrecisionMode.REAL);
        taskReal.compute();

        int[][] resD = taskDouble.getResult();
        int[][] resF = taskFloat.getResult();
        int[][] resR = taskReal.getResult();

        int diffF = 0, diffR = 0;
        for(int i=0; i<w; i++) {
            for(int j=0; j<h; j++) {
                if(resD[i][j] != resF[i][j]) diffF++;
                if(resD[i][j] != resR[i][j]) diffR++;
            }
        }
        System.out.println("Mandelbrot diff Float: " + diffF);
        System.out.println("Mandelbrot diff Real:  " + diffR);

        // At this resolution, they should be very similar
        assertTrue(diffF < 50); // Less than 0.5% diff
        assertTrue(diffR < 10); // Real should be almost identical to Double
    }

    @Test
    public void testWaveSimConvergence() {
        int size = 50;
        WaveSimTask taskDouble = new WaveSimTask(size, size);
        taskDouble.setMode(TaskRegistry.PrecisionMode.DOUBLE);
        
        WaveSimTask taskFloat = new WaveSimTask(size, size);
        taskFloat.setMode(TaskRegistry.PrecisionMode.FLOAT);

        // Perturb center
        double[][] u = new double[size][size];
        u[size/2][size/2] = 1.0;
        taskDouble.updateState(u, new double[size][size]);
        taskFloat.updateState(u, new double[size][size]);

        for(int i=0; i<10; i++) {
            taskDouble.step();
            taskFloat.step();
        }

        double[][] resD = taskDouble.getU();
        double[][] resF = taskFloat.getU();

        double maxDiff = 0;
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                maxDiff = Math.max(maxDiff, Math.abs(resD[i][j] - resF[i][j]));
            }
        }
        System.out.println("WaveSim Max Diff (Double vs Float): " + maxDiff);
        assertTrue(maxDiff < 1e-5);
    }

    @Test
    public void testNBodyConvergence() {
        List<NBodyTask.Body> bodies = new ArrayList<>();
        bodies.add(new NBodyTask.Body(0, 0, 0, 0, 0, 0, 1000)); // Sun
        bodies.add(new NBodyTask.Body(10, 0, 0, 0, 10, 0, 1));  // Planet

        NBodyTask taskDouble = new NBodyTask(bodies);
        taskDouble.setMode(TaskRegistry.PrecisionMode.DOUBLE);

        NBodyTask taskFloat = new NBodyTask(bodies);
        taskFloat.setMode(TaskRegistry.PrecisionMode.FLOAT);

        for(int i=0; i<100; i++) {
            taskDouble.step();
            taskFloat.step();
        }

        List<NBodyTask.Body> resD = taskDouble.getBodies();
        List<NBodyTask.Body> resF = taskFloat.getBodies();

        double distD = Math.sqrt(Math.pow(resD.get(1).x, 2) + Math.pow(resD.get(1).y, 2));
        double distF = Math.sqrt(Math.pow(resF.get(1).x, 2) + Math.pow(resF.get(1).y, 2));

        System.out.println("NBody Dist Double: " + distD);
        System.out.println("NBody Dist Float:  " + distF);
        System.out.println("NBody Dist Diff:   " + Math.abs(distD - distF));

        assertTrue(Math.abs(distD - distF) < 1e-3);
    }
}
