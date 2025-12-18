/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.linearalgebra.tensors;

import org.jscience.mathematics.linearalgebra.tensors.backends.CPUDenseTensorProvider;
import org.jscience.mathematics.linearalgebra.tensors.backends.CPUSIMDVectorTensorProvider;
import org.jscience.mathematics.linearalgebra.tensors.backends.TensorProvider;
import org.jscience.mathematics.numbers.real.Real;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Random;

/**
 * Basic benchmark to compare Native vs Vector API.
 * <p>
 * Not a full JMH benchmark, but a simple runner for verification.
 * </p>
 */
@Tag("benchmark")
public class TensorBenchmark {

    private static final int SIZE = 1_000_000;
    private static final int WARMUP_ITERATIONS = 5;
    private static final int ITERATIONS = 10;

    @Test
    void benchmarkTensorAddition() {
        System.out.println("Benchmarking Tensor Addition (Size: " + SIZE + ")");

        // Setup Provider 1: Native
        TensorProvider nativeProvider = new CPUDenseTensorProvider();
        Tensor<Real> t1Native = nativeProvider.create(randomData(SIZE), SIZE);
        Tensor<Real> t2Native = nativeProvider.create(randomData(SIZE), SIZE);

        // Setup Provider 2: Vector
        TensorProvider vectorProvider = new CPUSIMDVectorTensorProvider();
        if (!vectorProvider.isAvailable()) {
            System.out.println("Vector API not available. Skipping Vector benchmark.");
        }
        Tensor<Real> t1Vector = vectorProvider.create(randomData(SIZE), SIZE);
        Tensor<Real> t2Vector = vectorProvider.create(randomData(SIZE), SIZE);

        // Warmup
        System.out.println("Warming up...");
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            t1Native.add(t2Native);
            if (vectorProvider.isAvailable())
                t1Vector.add(t2Vector);
        }

        // Native Run
        long start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            t1Native.add(t2Native);
        }
        long durationNative = (System.nanoTime() - start) / ITERATIONS;
        System.out.println("Native Average: " + (durationNative / 1_000_000.0) + " ms");

        // Vector Run
        if (vectorProvider.isAvailable()) {
            start = System.nanoTime();
            for (int i = 0; i < ITERATIONS; i++) {
                t1Vector.add(t2Vector);
            }
            long durationVector = (System.nanoTime() - start) / ITERATIONS;
            System.out.println("Vector Average: " + (durationVector / 1_000_000.0) + " ms");

            System.out.println("Speedup: " + String.format("%.2fx", (double) durationNative / durationVector));
        }
    }

    private Real[] randomData(int size) {
        Real[] data = new Real[size];
        Random r = new Random(42);
        for (int i = 0; i < size; i++) {
            data[i] = Real.of(r.nextDouble());
        }
        return data;
    }
}
