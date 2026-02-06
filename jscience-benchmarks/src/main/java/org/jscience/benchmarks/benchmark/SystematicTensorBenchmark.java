package org.jscience.benchmarks.benchmark;

import org.jscience.core.mathematics.linearalgebra.Tensor;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.TensorProvider;

import java.util.Random;

/**
 * A benchmark that systematically tests all available TensorProviders.
 */
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
