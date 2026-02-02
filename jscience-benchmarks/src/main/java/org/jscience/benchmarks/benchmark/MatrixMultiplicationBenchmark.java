package org.jscience.benchmarks.benchmark;

import org.openjdk.jmh.annotations.*;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.matrices.MatrixFactory;
import org.jscience.core.mathematics.linearalgebra.matrices.SIMDDoubleMatrix;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;
import java.util.concurrent.TimeUnit;
import java.util.Random;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class MatrixMultiplicationBenchmark {

    @Param({"128", "256", "512"})
    public int size;

    private Matrix<Real> matrixA;
    private Matrix<Real> matrixB;
    private Matrix<Real> simdA;
    private Matrix<Real> simdB;

    @Setup(Level.Trial)
    public void doSetup() {
        Real[][] dataA = generateRandomData(size);
        Real[][] dataB = generateRandomData(size);
        
        // Standard Generic/Dense Matrix
        matrixA = MatrixFactory.create(dataA, Reals.getInstance());
        matrixB = MatrixFactory.create(dataB, Reals.getInstance());
        
        // Explicit SIMD Matrix (bypass factory auto-selection if needed/verified)
        // Convert to flat double array
        double[] flatA = flatten(dataA);
        double[] flatB = flatten(dataB);
        
        simdA = new SIMDDoubleMatrix(size, size, flatA);
        simdB = new SIMDDoubleMatrix(size, size, flatB);
    }
    
    private Real[][] generateRandomData(int n) {
        Random rand = new Random(42);
        Real[][] d = new Real[n][n];
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                d[i][j] = Real.of(rand.nextDouble());
            }
        }
        return d;
    }
    
    private double[] flatten(Real[][] data) {
         int n = data.length;
         double[] res = new double[n*n];
         for(int i=0; i<n; i++)
             for(int j=0; j<n; j++)
                 res[i*n+j] = data[i][j].doubleValue();
         return res;
    }

    @Benchmark
    public Matrix<Real> standardMultiplication() {
        return matrixA.multiply(matrixB);
    }

    @Benchmark
    public Matrix<Real> simdMultiplication() {
        return simdA.multiply(simdB);
    }
}
