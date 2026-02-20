/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

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

package org.jscience.benchmarks.benchmark.benchmarks;

import org.jscience.benchmarks.benchmark.RunnableBenchmark;

import org.openjdk.jmh.annotations.*;
import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.matrices.TiledMatrix;
import org.jscience.core.mathematics.linearalgebra.algorithms.DistributedSUMMAAlgorithm;
import org.jscience.core.mathematics.linearalgebra.algorithms.DistributedCannonAlgorithm;
import org.jscience.core.mathematics.linearalgebra.algorithms.DistributedFoxAlgorithm;
import org.jscience.core.mathematics.linearalgebra.algorithms.Distributed25DAlgorithm;
import org.jscience.core.mathematics.linearalgebra.algorithms.DistributedCARMAAlgorithm;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;
import org.jscience.core.technical.backend.distributed.DistributedContext;
import org.jscience.core.distributed.LocalDistributedContext;
import org.jscience.core.ComputeContext;
import org.jscience.core.technical.monitoring.DistributedMonitor;

import java.util.concurrent.TimeUnit;
import java.util.Random;

/**
 * Benchmark for distributed matrix operations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@State(Scope.Thread)
// Refactored to subpackage
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@AutoService(RunnableBenchmark.class)
public class DistributedMatrixBenchmark implements RunnableBenchmark {

    @Override
    public String getId() {
        return "matrix-distributed-" + size + "-p" + parallelism;
    }

    @Override
    public String getName() {
        return "Distributed Matrix Mult (" + size + "x" + size + ", p=" + parallelism + ")";
    }

    @Override
    public String getDescription() {
        return "Benchmarks distributed matrix multiplication algorithms (SUMMA, Cannon, CARMA, etc.) using a LocalDistributedContext.";
    }

    @Override
    public String getDomain() {
        return "Linear Algebra";
    }

    @Param({ "256", "512", "1024" })
    public int size = 256;

    @Param({ "64", "128" })
    public int tileSize = 64;

    @Param({ "1", "4" })
    public int parallelism = 4;

    private Matrix<Real> A;
    private Matrix<Real> B;
    private TiledMatrix tiledA;
    private TiledMatrix tiledB;

    @Override
    public void setup() {
        doSetup();
    }

    @Override
    public void run() {
        standardMultiply();
    }

    @Override
    public void teardown() {
        A = null;
        B = null;
        tiledA = null;
        tiledB = null;
    }

    @Setup(Level.Trial)
    public void doSetup() {
        // Configure distributed context
        DistributedContext ctx = new LocalDistributedContext(parallelism);
        ComputeContext.current().setDistributedContext(ctx);

        // Generate random matrices
        double[][] dataA = generateRandomData(size);
        double[][] dataB = generateRandomData(size);

        A = org.jscience.core.mathematics.linearalgebra.matrices.MatrixFactory.create(toReal(dataA), Reals.getInstance());
        B = org.jscience.core.mathematics.linearalgebra.matrices.MatrixFactory.create(toReal(dataB), Reals.getInstance());

        // Create tiled versions
        tiledA = new TiledMatrix(A, tileSize, tileSize);
        tiledB = new TiledMatrix(B, tileSize, tileSize);
    }

    @Benchmark
    public Matrix<Real> standardMultiply() {
        long start = System.nanoTime();
        Matrix<Real> res = A.multiply(B);
        record("standard", System.nanoTime() - start);
        return res;
    }

    @Benchmark
    public TiledMatrix summaMultiply() {
        long start = System.nanoTime();
        TiledMatrix res = DistributedSUMMAAlgorithm.multiply(tiledA, tiledB);
        record("summa", System.nanoTime() - start);
        return res;
    }

    @Benchmark
    public TiledMatrix cannonMultiply() {
        int gridSize = (int) Math.sqrt(parallelism);
        if (gridSize * gridSize == parallelism) {
            long start = System.nanoTime();
            TiledMatrix res = DistributedCannonAlgorithm.multiply(tiledA, tiledB);
            record("cannon", System.nanoTime() - start);
            return res;
        }
        return null;
    }

    @Benchmark
    public TiledMatrix foxMultiply() {
        int gridSize = (int) Math.sqrt(parallelism);
        if (gridSize * gridSize == parallelism) {
            long start = System.nanoTime();
            TiledMatrix res = DistributedFoxAlgorithm.multiply(tiledA, tiledB);
            record("fox", System.nanoTime() - start);
            return res;
        }
        return null;
    }

    @Benchmark
    public TiledMatrix twoAndHalfDMultiply() {
        int c = (parallelism >= 8) ? 2 : 1;
        if (parallelism % c == 0) {
            int pLayer = parallelism / c;
            int pSqrt = (int) Math.sqrt(pLayer);
            if (pSqrt * pSqrt == pLayer) {
                long start = System.nanoTime();
                TiledMatrix res = Distributed25DAlgorithm.multiply(tiledA, tiledB, c);
                record("2.5d", System.nanoTime() - start);
                return res;
            }
        }
        return null;
    }

    @Benchmark
    public TiledMatrix carmaMultiply() {
        long start = System.nanoTime();
        TiledMatrix res = DistributedCARMAAlgorithm.multiply(tiledA, tiledB);
        record("carma", System.nanoTime() - start);
        return res;
    }

    private void record(String algo, long durationNs) {
        DistributedMonitor.getInstance()
                .recordDistributedTask(algo, "local-pseudo-node", durationNs);
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
