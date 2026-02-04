/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.benchmarks.benchmark;

import org.openjdk.jmh.annotations.*;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.matrices.TiledMatrix;
import org.jscience.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.core.mathematics.linearalgebra.algorithms.DistributedSUMMAAlgorithm;
import org.jscience.core.mathematics.linearalgebra.algorithms.DistributedCannonAlgorithm;
import org.jscience.core.mathematics.linearalgebra.algorithms.DistributedFoxAlgorithm;
import org.jscience.core.mathematics.linearalgebra.algorithms.Distributed25DAlgorithm;
import org.jscience.core.mathematics.linearalgebra.algorithms.DistributedCARMAAlgorithm;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;
import org.jscience.core.distributed.DistributedContext;
import org.jscience.core.distributed.LocalDistributedContext;
import org.jscience.core.ComputeContext;

import java.util.concurrent.TimeUnit;
import java.util.Random;

/**
 * Benchmark for distributed matrix multiplication algorithms.
 * <p>
 * Compares: Standard, SUMMA, Cannon, Fox algorithms
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class DistributedMatrixBenchmark {

    @Param({ "256", "512", "1024" })
    public int size;

    @Param({ "64", "128" })
    public int tileSize;

    @Param({ "1", "4" })
    public int parallelism;

    private Matrix<Real> A;
    private Matrix<Real> B;
    private TiledMatrix tiledA;
    private TiledMatrix tiledB;

    @Setup(Level.Trial)
    public void setup() {
        // Configure distributed context
        DistributedContext ctx = new LocalDistributedContext(parallelism);
        ComputeContext.current().setDistributedContext(ctx);

        // Generate random matrices
        double[][] dataA = generateRandomData(size);
        double[][] dataB = generateRandomData(size);

        A = new DenseMatrix<>(toReal(dataA), Reals.getInstance());
        B = new DenseMatrix<>(toReal(dataB), Reals.getInstance());

        // Create tiled versions
        tiledA = new TiledMatrix(A, tileSize, tileSize);
        tiledB = new TiledMatrix(B, tileSize, tileSize);
    }

    @Benchmark
    public Matrix<Real> standardMultiply() {
        return A.multiply(B);
    }

    @Benchmark
    public TiledMatrix summaMultiply() {
        return DistributedSUMMAAlgorithm.multiply(tiledA, tiledB);
    }

    @Benchmark
    public TiledMatrix cannonMultiply() {
        // Only run if grid is square
        int gridSize = (int) Math.sqrt(parallelism);
        if (gridSize * gridSize == parallelism) {
            return DistributedCannonAlgorithm.multiply(tiledA, tiledB);
        }
        return null; // Skip if not square grid
    }

    @Benchmark
    public TiledMatrix foxMultiply() {
        // Only run if grid is square
        int gridSize = (int) Math.sqrt(parallelism);
        if (gridSize * gridSize == parallelism) {
            return DistributedFoxAlgorithm.multiply(tiledA, tiledB);
        }
        return null; // Skip if not square grid
    }

    @Benchmark
    public TiledMatrix twoAndHalfDMultiply() {
        // Use 2 layers if parallelism allows
        int c = (parallelism >= 8) ? 2 : 1;
        if (parallelism % c == 0) {
            int pLayer = parallelism / c;
            int pSqrt = (int) Math.sqrt(pLayer);
            if (pSqrt * pSqrt == pLayer) {
                return Distributed25DAlgorithm.multiply(tiledA, tiledB, c);
            }
        }
        return null;
    }

    @Benchmark
    public TiledMatrix carmaMultiply() {
        return DistributedCARMAAlgorithm.multiply(tiledA, tiledB);
    }

    private Real[][] toReal(double[][] data) {
        int n = data.length;
        Real[][] reals = new Real[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                reals[i][j] = Real.of(data[i][j]);
            }
        }
        return reals;
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
}


