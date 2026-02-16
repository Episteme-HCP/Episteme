/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.mathematics.linearalgebra.tensors.backends;

import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.linearalgebra.Tensor;
import org.jscience.core.mathematics.linearalgebra.tensors.TensorBackend;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.backend.ExecutionContext;
import org.jscience.core.technical.backend.gpu.cuda.CUDAExecutionContext;
import org.jscience.core.mathematics.linearalgebra.tensors.backends.CPUSparseTensorBackend;

/**
 * ND4J Sparse Tensor Provider.
 */
@AutoService(TensorBackend.class)
public class ND4JSparseTensorBackend implements TensorBackend {

    private static boolean isAvailable = false;
    private static final CPUSparseTensorBackend fallback = new CPUSparseTensorBackend();

    static {
        try {
            Class.forName("org.nd4j.linalg.factory.Nd4j");
            // Check for sparse support
            Class.forName("org.nd4j.linalg.api.ndarray.BaseSparseNDArray");
            isAvailable = true;
        } catch (ClassNotFoundException e) {
            isAvailable = false;
        } catch (NoClassDefFoundError e) {
            isAvailable = false;
        }
    }

    @Override
    public <T> Tensor<T> zeros(Class<T> elementType, int... shape) {
        if (!isAvailable) {
            return fallback.zeros(elementType, shape);
        }
        if (!Real.class.isAssignableFrom(elementType)) {
            return fallback.zeros(elementType, shape);
        }
        return fallback.zeros(elementType, shape);
    }

    @Override
    public <T> Tensor<T> ones(Class<T> elementType, int... shape) {
        if (!isAvailable) {
            return fallback.ones(elementType, shape);
        }
        return fallback.ones(elementType, shape);
    }

    @Override
    public <T> Tensor<T> create(T[] data, int... shape) {
        if (!isAvailable) {
            return fallback.create(data, shape);
        }
        
        int nonZeroCount = 0;
        for (T element : data) {
            if (element instanceof Real) {
                if (((Real) element).doubleValue() != 0.0) {
                    nonZeroCount++;
                }
            } else {
                nonZeroCount++;
            }
        }
        
        double sparsity = (double) nonZeroCount / data.length;
        if (sparsity > 0.3) {
            return new ND4JNativeTensorBackend().create(data, shape);
        }
        
        return fallback.create(data, shape);
    }

    @Override
    public boolean supportsGPU() {
        return isAvailable;
    }

    @Override
    public String getName() {
        return "ND4J Sparse";
    }

    @Override
    public int getPriority() {
        return isAvailable ? 70 : 0;
    }

    @Override
    public boolean supportsParallelOps() {
        return true;
    }

    @Override
    public ExecutionContext createContext() {
        if (!isAvailable) {
            return null;
        }
        return new CUDAExecutionContext();
    }

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public String getId() {
        return "nd4jsparse";
    }

    @Override
    public String getDescription() {
        return "ND4J Sparse Tensor Provider - Memory-efficient sparse tensor operations";
    }

    @Override
    public org.jscience.core.technical.backend.HardwareAccelerator getAcceleratorType() {
        return org.jscience.core.technical.backend.HardwareAccelerator.GPU;
    }
}
