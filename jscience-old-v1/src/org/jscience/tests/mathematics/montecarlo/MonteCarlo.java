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

import org.jscience.mathematics.io.*;
import org.jscience.mathematics.statistics.NormalDistribution;

import java.io.File;
import java.io.FileReader;

/**
 * Monte Carlo calculation of Helium ground state energy.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class MonteCarlo implements Mapping {
    private int N;
    private double energy[];
    private AbstractDoubleVector r1;
    private AbstractDoubleVector r2;

    /**
     * Instantiate class.
     */
    public static void main(String arg[]) {
        if (arg.length == 1) {
            int n = Integer.valueOf(arg[0]).intValue();
            new MonteCarlo(n);
        } else {
            System.out.println("Need to specify command line arguments:");
            System.out.println("<number of iterations>");
        }
    }

    /**
     * Constructor.
     *
     * @param n number of iterations
     */
    public MonteCarlo(int n) {
        N = n;
        r1 = new Double3Vector(Math.random(), Math.random(), Math.random());
        r2 = new Double3Vector(-Math.random(), -Math.random(), -Math.random());
        energy = new double[N];
        compute();
        saveResults();
    }

    /**
     * Compute the ground state energy.
     */
    private void compute() {
        AbstractDoubleVector tmpr1, tmpr2;
        double prob, tmpprob;

// Stabilising
        for (int i = 0; i < 1000; i++) {
            tmpr1 = r1.mapComponents(this);
            tmpr2 = r2.mapComponents(this);
            tmpprob = trialWF(tmpr1, tmpr2);
            tmpprob *= tmpprob;
            prob = trialWF(r1, r2);
            prob *= prob;
            if (tmpprob / prob > Math.random()) {
                r1 = tmpr1;
                r2 = tmpr2;
            }
        }

        System.out.println("Integrating...");
        for (int i = 0; i < N; i++) {
            tmpr1 = r1.mapComponents(this);
            tmpr2 = r2.mapComponents(this);
            tmpprob = trialWF(tmpr1, tmpr2);
            tmpprob *= tmpprob;
            prob = trialWF(r1, r2);
            prob *= prob;
            if (tmpprob / prob > Math.random()) {
                r1 = tmpr1;
                r2 = tmpr2;
            }
            energy[i] = localEnergy(r1, r2);
        }
    }

    /**
     * Trial wavefunction.
     *
     * @param r1 position vector of electron 1
     * @param r2 position vector of electron 2
     */
    private double trialWF(AbstractDoubleVector r1, AbstractDoubleVector r2) {
        double modR1 = r1.norm();
        double modR2 = r2.norm();
        double modR12 = r1.subtract(r2).norm();
        return Math.exp(-2 * modR1) * Math.exp(-2 * modR2) * Math.exp(modR12 / 2);
    }

    /**
     * Local energy calculation.
     *
     * @param r1 position vector of electron 1
     * @param r2 position vector of electron 2
     */
    private double localEnergy(AbstractDoubleVector r1, AbstractDoubleVector r2) {
        AbstractDoubleVector r12 = r2.subtract(r1);
        double termR112 = r1.scalarProduct(r12) / (r1.norm() * r12.norm());
        double termR212 = r2.scalarProduct(r12) / (r2.norm() * r12.norm());
        return -17 / 4 - termR112 + termR212;
    }

    /**
     * Update electron co-ordinates.
     */
    public double map(double x) {
        return x + (Math.random() - 0.5);
    }

    /**
     * Not used, dummy implementation for Mapping interface.
     */
    public Complex map(Complex z) {
        return null;
    }

    /**
     * Log results to disk.
     */
    private void saveResults() {
        File file = new File("results.dat");
        char buf[] = null;
        NormalDistribution norm = new NormalDistribution(energy);
        double data[][] = new double[1][3];
        double mean = norm.getMean();
        double var = norm.getVariance();
        System.out.println("Energy: " + mean + "         Var: " + var);

        data[0][0] = N;
        data[0][1] = mean;
        data[0][2] = var;
// Read in existing data
        try {
            FileReader in = new FileReader(file);
            buf = new char[(int) file.length()];
            in.read(buf);
            in.close();
        } catch (Exception e) {
            System.out.println("No previous data - new file.");
        }
// Save all to file
        try {
            TextWriter out = new TextWriter(file, ',');
            if (buf != null)
                out.write(buf);
            out.write(data);
            out.close();
        } catch (Exception e) {
            System.out.println("Failed to save.");
        }
    }
}

