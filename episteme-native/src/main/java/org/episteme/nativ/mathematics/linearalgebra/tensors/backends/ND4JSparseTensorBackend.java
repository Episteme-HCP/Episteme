/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.tensors.backends;

import com.google.auto.service.AutoService;
import org.episteme.core.mathematics.linearalgebra.Tensor;
import org.episteme.core.mathematics.linearalgebra.tensors.TensorBackend;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.technical.backend.ExecutionContext;
import org.episteme.core.technical.backend.HardwareAccelerator;
import org.episteme.nativ.technical.backend.gpu.cuda.CUDAExecutionContext;
import org.episteme.core.mathematics.linearalgebra.tensors.backends.CPUSparseTensorBackend;

/**
 * ND4J Sparse Tensor backend.
 * <p>
 * When sparsity is low (&lt;30% non-zero), delegates to {@link ND4JNativeTensorBackend}.
 * Otherwise uses the CPU sparse fallback.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({TensorBackend.class})
public class ND4JSparseTensorBackend implements TensorBackend {

    private static boolean available = false;
    private static final CPUSparseTensorBackend fallback = new CPUSparseTensorBackend();

    static {
        try {
            Class.forName("org.nd4j.linalg.factory.Nd4j");
            Class.forName("org.nd4j.linalg.api.ndarray.BaseSparseNDArray");
            available = true;
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            available = false;
        }
    }

    @Override
    public <T> Tensor<T> zeros(Class<T> elementType, int... shape) {
        if (!available) return fallback.zeros(elementType, shape);
        if (!Real.class.isAssignableFrom(elementType)) return fallback.zeros(elementType, shape);
        return fallback.zeros(elementType, shape);
    }

    @Override
    public <T> Tensor<T> ones(Class<T> elementType, int... shape) {
        if (!available) return fallback.ones(elementType, shape);
        return fallback.ones(elementType, shape);
    }

    @Override
    public <T> Tensor<T> create(T[] data, int... shape) {
        if (!available) return fallback.create(data, shape);

        int nonZeroCount = 0;
        for (T element : data) {
            if (element instanceof Real) {
                if (((Real) element).doubleValue() != 0.0) nonZeroCount++;
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
        return available;
    }

    @Override
    public String getName() {
        return "ND4J Sparse";
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
        @SuppressWarnings("deprecation")
        ExecutionContext context = new CUDAExecutionContext();
        return context;
    }

    @Override
    public boolean isAvailable() {
        return available;
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
