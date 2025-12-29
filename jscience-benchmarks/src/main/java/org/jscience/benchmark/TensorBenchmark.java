/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
