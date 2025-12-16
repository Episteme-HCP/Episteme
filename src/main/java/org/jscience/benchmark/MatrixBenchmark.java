package org.jscience.benchmark;

import org.openjdk.jmh.annotations.*;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.sets.Reals;
import org.jscience.mathematics.numbers.real.Real;
import java.util.concurrent.TimeUnit;
import java.util.Random;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class MatrixBenchmark {

    @Param({ "128", "256", "512" })
    public int size;

    private Matrix<Real> A;
    private Matrix<Real> B;

    @Setup(Level.Trial)
    public void setup() {
        double[][] dataA = generateRandomData(size);
        double[][] dataB = generateRandomData(size);

        A = new org.jscience.mathematics.linearalgebra.matrices.DenseMatrix<>(toReal(dataA), Reals.getInstance());
        B = new org.jscience.mathematics.linearalgebra.matrices.DenseMatrix<>(toReal(dataB), Reals.getInstance());
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

    @Benchmark
    public Matrix<Real> multiply() {
        return A.multiply(B);
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
