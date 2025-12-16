package org.jscience.mathematics.linearalgebra.tensors.backends;

import org.jscience.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.mathematics.structures.rings.Field;
import org.jscience.technical.backend.ExecutionContext;

public class ND4JSparseTensorProvider implements TensorProvider {

    @Override
    public <T> Tensor<T> zeros(Class<T> elementType, int... shape) {
        throw new UnsupportedOperationException("ND4J Sparse support not yet implemented");
    }

    @Override
    public <T> Tensor<T> ones(Class<T> elementType, int... shape) {
        throw new UnsupportedOperationException("ND4J Sparse support not yet implemented");
    }

    @Override
    public <T> Tensor<T> create(T[] data, int... shape) {
        throw new UnsupportedOperationException("ND4J Sparse support not yet implemented");
    }

    @Override
    public boolean supportsGPU() {
        return true;
    }

    @Override
    public String getName() {
        return "ND4J Sparse";
    }

    @Override
    public boolean supportsParallelOps() {
        return true;
    }

    @Override
    public ExecutionContext createContext() {
        return null;
    }

    @Override
    public boolean isAvailable() {
        return false; // Not available yet
    }
}
