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
package org.jscience.benchmark;

import org.openjdk.jmh.annotations.*;
import org.jscience.mathematics.tensor.Tensor;
import org.jscience.mathematics.tensor.TensorFactory;
import org.jscience.mathematics.number.Real;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class TensorBenchmark {

    @Param({ "10", "50", "100" })
    private int size;

    private Tensor<Real> t1;
    private Tensor<Real> t2;
    private Tensor<Real> tensor3d;

    @Setup
    public void setup() {
        // 2D tensors (matrices)
        t1 = TensorFactory.ones(Real.class, size, size);
        t2 = TensorFactory.ones(Real.class, size, size);

        // 3D tensor
        tensor3d = TensorFactory.zeros(Real.class, size, size, size);

        // Fill with random values
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                t1.set(Real.of(Math.random()), i, j);
                t2.set(Real.of(Math.random()), i, j);

                for (int k = 0; k < size; k++) {
                    tensor3d.set(Real.of(Math.random()), i, j, k);
                }
            }
        }
    }

    @Benchmark
    public void elementWiseAddition() {
        t1.add(t2);
    }

    @Benchmark
    public void elementWiseMultiplication() {
        t1.multiply(t2);
    }

    @Benchmark
    public void scalarMultiplication() {
        t1.scale(Real.of(2.5));
    }

    @Benchmark
    public void transpose2D() {
        t1.transpose();
    }

    @Benchmark
    public void reshape() {
        int totalSize = size * size;
        if (totalSize >= 4) {
            // Reshape to different dimensions with same total size
            int newRows = 2;
            int newCols = totalSize / 2;
            t1.reshape(newRows, newCols);
        }
    }

    @Benchmark
    public void sumAll() {
        t1.sum();
    }

    @Benchmark
    public void tensor3dAddition() {
        Tensor<Real> t3d_2 = TensorFactory.ones(Real.class, size, size, size);
        tensor3d.add(t3d_2);
    }

    @Benchmark
    public void copyTensor() {
        t1.copy();
    }
}

