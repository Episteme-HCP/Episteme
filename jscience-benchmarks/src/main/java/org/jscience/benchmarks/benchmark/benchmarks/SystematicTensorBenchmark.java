/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.benchmarks.benchmark.benchmarks;

import org.jscience.benchmarks.benchmark.RunnableBenchmark;
import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.linearalgebra.tensors.providers.TensorProvider;
import org.jscience.core.mathematics.linearalgebra.Tensor;
import org.jscience.core.mathematics.numbers.real.Real;
import java.util.Random;

/**
 * A benchmark that systematically tests all available TensorProviders.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
public class SystematicTensorBenchmark implements SystematicBenchmark<TensorProvider> {

    private static final int DIM = 100;
    private static final int[] SHAPE = {DIM, DIM, DIM};
    private TensorProvider currentProvider;
    private Tensor<Real> tensorA;
    private Tensor<Real> tensorB;

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "tensor-systematic"; }
    @Override public String getNameBase() { return "Tensor Hadamard Product"; }
    @Override public Class<TensorProvider> getProviderClass() { return TensorProvider.class; }

    @Override
    public String getDescription() {
        return "Tensor Hadamard Product (100x100x100)";
    }

    @Override
    public String getDomain() {
        return "Deep Learning (Tensor)";
    }

    @Override
    public void setup() {
        // Setup requires the provider to create the tensors
        if (currentProvider != null) {
            Random r = new Random(42);
            int size = DIM * DIM * DIM;
            Real[] dataA = new Real[size];
            Real[] dataB = new Real[size];
            
            for(int i=0; i<size; i++) {
                dataA[i] = Real.of(r.nextDouble());
                dataB[i] = Real.of(r.nextDouble());
            }
            
            tensorA = currentProvider.create(dataA, SHAPE);
            tensorB = currentProvider.create(dataB, SHAPE);
        }
    }

    @Override
    public void setProvider(TensorProvider provider) {
        this.currentProvider = provider;
    }


    @Override
    public void run() {
        if (tensorA != null && tensorB != null) {
            tensorA.multiply(tensorB);
        }
    }

    @Override
    public void teardown() {
        tensorA = null;
        tensorB = null;
    }

    @Override
    public int getSuggestedIterations() {
        return 50;
    }
}
