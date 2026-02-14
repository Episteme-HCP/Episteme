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

import org.jscience.core.mathematics.linearalgebra.Tensor;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.TensorProvider;
import com.google.auto.service.AutoService;

import java.util.Random;

/**
 * A benchmark that systematically tests all available TensorProviders.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
public class SystematicTensorBenchmark implements SystematicBenchmark<TensorProvider> {

    private int[] shape = {100, 100};
    private Real[] data;
    private TensorProvider currentProvider;
    private Tensor<Real> tensorA;
    private Tensor<Real> tensorB;

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "tensor-systematic"; }
    @Override public String getNameBase() { return "Systematic Tensor Operations"; }
    @Override public String getDescription() { return "Systematically benchmarks tensor creation and basic arithmetic operations across different providers."; }
    @Override public String getDomain() { return "Linear Algebra"; }
    @Override public Class<TensorProvider> getProviderClass() { return TensorProvider.class; }

    @Override
    public void setup() {
        int size = 1;
        for (int s : shape) size *= s;
        data = new Real[size];
        Random rand = new Random(42);
        for (int i = 0; i < size; i++) {
            data[i] = Real.of(rand.nextDouble());
        }
        
        if (currentProvider != null) {
            tensorA = currentProvider.create(data, shape);
            tensorB = currentProvider.create(data, shape);
        }
    }

    @Override
    public void setProvider(TensorProvider provider) {
        this.currentProvider = provider;
    }

    @Override
    public void run() {
        if (currentProvider != null) {
            // Factor creation
            currentProvider.zeros(Real.class, shape);
            currentProvider.ones(Real.class, shape);
            
            // Basic operations (polymorphic through the Tensor interface)
            if (tensorA != null && tensorB != null) {
                tensorA.add(tensorB);
                tensorA.multiply(tensorB);
            }
        }
    }

    @Override
    public void teardown() {
        data = null;
        tensorA = null;
        tensorB = null;
    }
}
