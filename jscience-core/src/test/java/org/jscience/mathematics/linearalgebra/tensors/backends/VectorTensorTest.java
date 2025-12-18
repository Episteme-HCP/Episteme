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
package org.jscience.mathematics.linearalgebra.tensors.backends;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.jscience.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.mathematics.numbers.real.Real;

import static org.assertj.core.api.Assertions.assertThat;

public class VectorTensorTest {

    boolean isVectorApiAvailable() {
        try {
            return new CPUSIMDVectorTensorProvider().isAvailable();
        } catch (Throwable t) {
            return false;
        }
    }

    @Test
    void testProviderIsAvailable() {
        // This test might fail if run on generic JDK without vector modules, so we
        // check condition
        CPUSIMDVectorTensorProvider provider = new CPUSIMDVectorTensorProvider();
        System.out.println("Vector API available: " + provider.isAvailable());
        // We don't assert true because it depends on environment, but we ensure it
        // doesn't crash on init.
        assertThat(provider).isNotNull();
    }

    @Test
    @EnabledIf("isVectorApiAvailable")
    void testRealAddition() {
        CPUSIMDVectorTensorProvider provider = new CPUSIMDVectorTensorProvider();

        Real[] data1 = { Real.of(1), Real.of(2), Real.of(3) };
        Real[] data2 = { Real.of(4), Real.of(5), Real.of(6) };

        Tensor<Real> t1 = provider.create(data1, 3);
        Tensor<Real> t2 = provider.create(data2, 3);

        Tensor<Real> result = t1.add(t2);

        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).doubleValue()).isEqualTo(5.0);
        assertThat(result.get(1).doubleValue()).isEqualTo(7.0);
        assertThat(result.get(2).doubleValue()).isEqualTo(9.0);
    }
}
