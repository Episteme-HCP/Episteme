package org.jscience.benchmark;

import org.jscience.mathematics.linearalgebra.tensors.DenseTensor;
import org.jscience.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.mathematics.numbers.real.Real;

import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

/**
 * JMH Benchmarks for tensor operations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class TensorBenchmark {

    @Param({ "100", "500", "1000" })
    private int size;

    private Tensor<Real> tensorA;
    private Tensor<Real> tensorB;

    @Setup
    public void setup() {
        Real[] dataA = new Real[size];
        Real[] dataB = new Real[size];
        for (int i = 0; i < size; i++) {
            dataA[i] = Real.of(Math.random());
            dataB[i] = Real.of(Math.random());
        }
        tensorA = new DenseTensor<>(dataA, size);
        tensorB = new DenseTensor<>(dataB, size);
    }

    @Benchmark
    public Tensor<Real> tensorAdd() {
        return tensorA.add(tensorB);
    }

    @Benchmark
    public Tensor<Real> tensorMultiply() {
        return tensorA.multiply(tensorB);
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
}
