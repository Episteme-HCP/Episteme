/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.benchmarks.benchmark;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.ejml.simple.SimpleMatrix;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import org.jblas.DoubleMatrix;
import java.util.Random;

/**
 * Manual benchmark runner comparing matrix libraries.
 * Compares: Apache Commons Math, EJML, Colt, JBlas
 */
public class ManualMatrixBenchmark {

    private static final int SIZE = 500;
    private static final int WARMUP = 3;
    private static final int ITERATIONS = 10;

    // JScience matrices
    private RealDoubleMatrix A;
    private RealDoubleMatrix B;

    // Commons Math
    private RealMatrix cmA;
    private RealMatrix cmB;

    // EJML
    private SimpleMatrix ejmlA;
    private SimpleMatrix ejmlB;

    // Colt
    private DoubleMatrix2D coltA;
    private DoubleMatrix2D coltB;
    private Algebra coltAlgebra = new Algebra();

    // JBlas
    private DoubleMatrix jblasA;
    private DoubleMatrix jblasB;

    public void setup() {
        double[][] dataA = generateRandomData(SIZE);
        double[][] dataB = generateRandomData(SIZE);

        // JScience
        A = RealDoubleMatrix.of(dataA);
        B = RealDoubleMatrix.of(dataB);

        // Commons Math
        cmA = new Array2DRowRealMatrix(dataA);
        cmB = new Array2DRowRealMatrix(dataB);

        // EJML
        ejmlA = new SimpleMatrix(dataA);
        ejmlB = new SimpleMatrix(dataB);

        // Colt
        coltA = new DenseDoubleMatrix2D(dataA);
        coltB = new DenseDoubleMatrix2D(dataB);

        // JBlas
        jblasA = new DoubleMatrix(dataA);
        jblasB = new DoubleMatrix(dataB);
    }

    private double[][] generateRandomData(int n) {
        Random r = new Random(42);
        double[][] data = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                data[i][j] = r.nextDouble();
            }
        }
        return data;
    }

    public void runBenchmark(String name, Runnable benchmark) {
        // Warmup
        System.out.print("  " + name + ": Warming up... ");
        System.out.flush();
        for (int i = 0; i < WARMUP; i++) {
            benchmark.run();
        }
        System.gc();
        
        // Measurement
        System.out.print("Measuring... ");
        System.out.flush();
        long start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            benchmark.run();
        }
        long end = System.nanoTime();
        
        double totalMs = (end - start) / 1_000_000.0;
        double avgMs = totalMs / ITERATIONS;
        System.out.printf("Done. Total: %.2f ms, Avg: %.2f ms/op%n", totalMs, avgMs);
    }

    public void runAll() {
        System.out.println("==========================================================");
        System.out.println("  Matrix Benchmark - " + SIZE + "x" + SIZE + " matrices");
        System.out.println("==========================================================");
        System.out.println();

        setup();

        runBenchmark("JScience Core    ", () -> A.multiply(B));
        runBenchmark("Apache Commons   ", () -> cmA.multiply(cmB));
        runBenchmark("EJML             ", () -> ejmlA.mult(ejmlB));
        runBenchmark("Colt             ", () -> coltAlgebra.mult(coltA, coltB));
        runBenchmark("JBlas            ", () -> jblasA.mmul(jblasB));

        System.out.println();
        System.out.println("==========================================================");
        System.out.println("  Benchmark Complete!");
        System.out.println("==========================================================");
    }

    public static void main(String[] args) {
        new ManualMatrixBenchmark().runAll();
    }
}
