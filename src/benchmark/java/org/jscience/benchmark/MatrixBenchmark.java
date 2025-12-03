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
import org.jscience.mathematics.linear.Matrix;
import org.jscience.mathematics.number.Real;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class MatrixBenchmark {

    @Param({ "100", "500", "1000" })
    private int size;

    private Matrix<Real> m1;
    private Matrix<Real> m2;

    @Setup
    public void setup() {
        Real[][] data1 = new Real[size][size];
        Real[][] data2 = new Real[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                data1[i][j] = Real.of(Math.random());
                data2[i][j] = Real.of(Math.random());
            }
        }

        m1 = Matrix.dense(data1);
        m2 = Matrix.dense(data2);
    }

    @Benchmark
    public void multiplication() {
        m1.multiply(m2);
    }

    @Benchmark
    public void transpose() {
        m1.transpose();
    }

    // SVD is expensive, maybe run only for smaller sizes or separate benchmark
    // @Benchmark
    // public void svd() {
    // // Assuming SVD implementation exists on Matrix
    // // m1.svd();
    // }
}

