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

package org.jscience.mathematics.tensor;

import org.junit.jupiter.api.Test;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.mathematics.linearalgebra.tensors.DenseTensor;
import org.jscience.mathematics.linearalgebra.tensors.TensorFactory;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests for tensor implementations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TensorTest {

    @Test
    void denseTensor_creation() {
        Real[] data = {
                Real.of(1), Real.of(2), Real.of(3),
                Real.of(4), Real.of(5), Real.of(6)
        };

        Tensor<Real> tensor = new DenseTensor<>(data, 2, 3);

        assertThat(tensor.rank()).isEqualTo(2);
        assertThat(tensor.shape()).containsExactly(2, 3);
        assertThat(tensor.size()).isEqualTo(6);
    }

    @Test
    void denseTensor_elementAccess() {
        Real[] data = {
                Real.of(1), Real.of(2),
                Real.of(3), Real.of(4)
        };

        Tensor<Real> tensor = new DenseTensor<>(data, 2, 2);

        assertThat(tensor.get(0, 0).doubleValue()).isEqualTo(1.0);
        assertThat(tensor.get(0, 1).doubleValue()).isEqualTo(2.0);
        assertThat(tensor.get(1, 0).doubleValue()).isEqualTo(3.0);
        assertThat(tensor.get(1, 1).doubleValue()).isEqualTo(4.0);
    }

    @Test
    void denseTensor_elementWiseAddition() {
        Real[] data1 = { Real.of(1), Real.of(2), Real.of(3), Real.of(4) };
        Real[] data2 = { Real.of(5), Real.of(6), Real.of(7), Real.of(8) };

        Tensor<Real> t1 = new DenseTensor<>(data1, 2, 2);
        Tensor<Real> t2 = new DenseTensor<>(data2, 2, 2);

        Tensor<Real> result = t1.add(t2);

        assertThat(result.get(0, 0).doubleValue()).isEqualTo(6.0);
        assertThat(result.get(0, 1).doubleValue()).isEqualTo(8.0);
        assertThat(result.get(1, 0).doubleValue()).isEqualTo(10.0);
        assertThat(result.get(1, 1).doubleValue()).isEqualTo(12.0);
    }

    @Test
    void denseTensor_transpose() {
        Real[] data = {
                Real.of(1), Real.of(2), Real.of(3),
                Real.of(4), Real.of(5), Real.of(6)
        };

        Tensor<Real> tensor = new DenseTensor<>(data, 2, 3);
        Tensor<Real> transposed = tensor.transpose();

        assertThat(transposed.shape()).containsExactly(3, 2);
        assertThat(transposed.get(0, 0).doubleValue()).isEqualTo(1.0);
        assertThat(transposed.get(0, 1).doubleValue()).isEqualTo(4.0);
        assertThat(transposed.get(1, 0).doubleValue()).isEqualTo(2.0);
    }

    @Test
    void tensorFactory_zeros() {
        Tensor<Real> tensor = TensorFactory.zeros(Real.class, 2, 3);

        assertThat(tensor.rank()).isEqualTo(2);
        assertThat(tensor.size()).isEqualTo(6);
        assertThat(tensor.get(0, 0).doubleValue()).isEqualTo(0.0);
        assertThat(tensor.get(1, 2).doubleValue()).isEqualTo(0.0);
    }

    @Test
    void tensorFactory_ones() {
        Tensor<Real> tensor = TensorFactory.ones(Real.class, 2, 2);

        assertThat(tensor.get(0, 0).doubleValue()).isEqualTo(1.0);
        assertThat(tensor.get(1, 1).doubleValue()).isEqualTo(1.0);
    }

    @Test
    void denseTensor_broadcast() {
        Real[] data = { Real.of(1), Real.of(2) }; // Shape [1, 2]
        Tensor<Real> tensor = new DenseTensor<>(data, 1, 2);

        // Broadcast to [2, 2]
        Tensor<Real> broadcasted = tensor.broadcast(2, 2);

        assertThat(broadcasted.shape()).containsExactly(2, 2);
        assertThat(broadcasted.get(0, 0).doubleValue()).isEqualTo(1.0);
        assertThat(broadcasted.get(1, 0).doubleValue()).isEqualTo(1.0); // Repeated row
        assertThat(broadcasted.get(0, 1).doubleValue()).isEqualTo(2.0);
        assertThat(broadcasted.get(1, 1).doubleValue()).isEqualTo(2.0); // Repeated row
    }

    @Test
    void denseTensor_slice() {
        Real[] data = {
                Real.of(1), Real.of(2), Real.of(3),
                Real.of(4), Real.of(5), Real.of(6)
        }; // Shape [2, 3]
        Tensor<Real> tensor = new DenseTensor<>(data, 2, 3);

        // Slice: row 1, cols 0-1 (size 1, 2)
        // starts: [1, 0], sizes: [1, 2] -> gets {4, 5}
        Tensor<Real> sliced = tensor.slice(new int[] { 1, 0 }, new int[] { 1, 2 });

        assertThat(sliced.shape()).containsExactly(1, 2);
        assertThat(sliced.get(0, 0).doubleValue()).isEqualTo(4.0);
        assertThat(sliced.get(0, 1).doubleValue()).isEqualTo(5.0);
    }

    @Test
    void sparseTensor_usage() {
        // Need explicit access to SparseTensor or factory
        // Assuming SparseTensor constructor is public
        // Shape [2, 2], zero = 0.0
        org.jscience.mathematics.linearalgebra.tensors.SparseTensor<Real> sparse = new org.jscience.mathematics.linearalgebra.tensors.SparseTensor<>(
                new int[] { 2, 2 }, Real.ZERO);

        sparse.set(Real.of(5.0), 0, 0);
        sparse.set(Real.of(3.0), 1, 1);

        assertThat(sparse.get(0, 0).doubleValue()).isEqualTo(5.0);
        assertThat(sparse.get(0, 1).doubleValue()).isEqualTo(0.0); // default

        // Broadcast: [2, 2] -> [2, 2, 2] (add dim on left?) No, must match rank or add
        // left.
        // Let's broadcast [2, 2] -> [1, 2, 2]??
        // No, broadcast usually does:
        // [1, 2] -> [2, 2]
        // Let's try slice first

        Tensor<Real> sliced = sparse.slice(new int[] { 0, 0 }, new int[] { 1, 1 });
        assertThat(sliced.shape()).containsExactly(1, 1);
        assertThat(sliced.get(0, 0).doubleValue()).isEqualTo(5.0);
    }
}

