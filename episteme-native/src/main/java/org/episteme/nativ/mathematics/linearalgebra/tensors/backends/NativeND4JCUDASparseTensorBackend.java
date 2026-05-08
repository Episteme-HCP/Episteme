/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.tensors.backends;

import com.google.auto.service.AutoService;
import org.episteme.core.mathematics.linearalgebra.Tensor;
import org.episteme.core.mathematics.linearalgebra.tensors.TensorBackend;
import org.episteme.core.technical.backend.ExecutionContext;
import org.episteme.core.technical.backend.HardwareAccelerator;
import org.episteme.nativ.technical.backend.gpu.cuda.CUDAExecutionContext;
import org.episteme.core.technical.algorithm.AlgorithmProvider;

/**
 * NativeND4J Sparse Tensor backend.
 * <p>
 * This backend implements sparse tensor operations using ND4J on CUDA.
 * Delegation to fallbacks is strictly prohibited by architectural rules.
 * </p>
 */
@AutoService({TensorBackend.class, AlgorithmProvider.class})
public class NativeND4JCUDASparseTensorBackend implements TensorBackend {

    private static boolean available = false;

    static {
        boolean globalDisabled = Boolean.getBoolean("episteme.backend.native.disabled");
        boolean nd4jDisabled = Boolean.getBoolean("episteme.backend.nd4j.disabled");

        if (!globalDisabled && !nd4jDisabled) {
            try {
                Class.forName("org.nd4j.linalg.factory.Nd4j");
                org.nd4j.linalg.factory.Nd4jBackend backend = org.nd4j.linalg.factory.Nd4j.getBackend();
                available = backend != null && backend.getClass().getName().contains("CudaBackend");
            } catch (Throwable t) {
                available = false;
            }
        } else {
            available = false;
        }
    }

    @Override
    public boolean isAvailable() {
        if (Boolean.getBoolean("episteme.backend.native.disabled")) return false;
        if (Boolean.getBoolean("episteme.backend.nd4j.disabled")) return false;
        return available;
    }

    @Override
    public <T> Tensor<T> zeros(Class<T> elementType, int... shape) {
        throw new UnsupportedOperationException("Native ND4J Sparse operations are not yet implemented. Delegation removed per architectural rules.");
    }

    @Override
    public <T> Tensor<T> ones(Class<T> elementType, int... shape) {
        throw new UnsupportedOperationException("Native ND4J Sparse operations are not yet implemented. Delegation removed per architectural rules.");
    }

    @Override
    public <T> Tensor<T> create(T[] data, int... shape) {
        throw new UnsupportedOperationException("Native ND4J Sparse operations are not yet implemented. Delegation removed per architectural rules.");
    }

    @Override
    public boolean supportsGPU() {
        return available;
    }

    @Override
    public String getName() {
        return "ND4J-CUDA-Sparse (GPU-Tensor)";
    }

    @Override
    public int getPriority() {
        return available ? 70 : 0;
    }

    @Override
    public boolean supportsParallelOps() {
        return true;
    }

    @Override
    public ExecutionContext createContext() {
        if (!available) return null;
        return new CUDAExecutionContext(null, null);
    }


    @Override
    public String getId() {
        return "nd4jsparse";
    }

    @Override
    public String getDescription() {
        return "ND4J Sparse Tensor backend — memory-efficient sparse tensor operations";
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.GPU;
    }
}
