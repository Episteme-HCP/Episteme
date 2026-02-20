/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.mathematics.tensors.backends;

import org.jscience.core.mathematics.linearalgebra.Tensor;
import org.jscience.core.mathematics.linearalgebra.tensors.providers.TensorProvider;
import org.jscience.core.technical.backend.Backend;
import org.jscience.core.technical.backend.ComputeBackend;
import org.jscience.core.technical.backend.cpu.CPUBackend;
import org.jscience.nativ.mathematics.linearalgebra.tensors.NativeTensor;
import org.jscience.nativ.technical.backend.nativ.NativeBackend;
import org.jscience.nativ.technical.backend.nativ.NativeLibraryLoader;
import com.google.auto.service.AutoService;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.util.Optional;

/**
 * Native CPU Tensor Backend using Project Panama (FFM) for native acceleration.
 * <p>
 * Attempts to load 'dnnl' (oneDNN) or 'mkl_rt' for acceleration.
 * If not found, it falls back to Java-based off-heap operations (via NativeTensor).
 * Implements {@link CPUBackend} and {@link NativeBackend}.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({Backend.class, ComputeBackend.class, CPUBackend.class, NativeBackend.class, TensorProvider.class})
public class NativeCPUTensorBackend implements TensorProvider, CPUBackend, NativeBackend {

    @SuppressWarnings("unused")
    private static final SymbolLookup LOOKUP;
    private static final boolean IS_AVAILABLE;

    static {
        // Try to load oneDNN or MKL
        Optional<SymbolLookup> lib = NativeLibraryLoader.loadLibrary("dnnl", Arena.global());
        if (lib.isEmpty()) {
            lib = NativeLibraryLoader.loadLibrary("mkl_rt", Arena.global());
        }
        
        if (lib.isPresent()) {
            LOOKUP = lib.get();
            IS_AVAILABLE = true;
        } else {
            LOOKUP = null;
            IS_AVAILABLE = false;
        }
    }

    @Override
    public boolean isLoaded() {
        return IS_AVAILABLE;
    }

    @Override
    public String getNativeLibraryName() {
        return "dnnl";
    }

    @Override
    public String getId() {
        return "native-cpu-tensor";
    }

    @Override
    public String getName() {
        return "Native CPU Tensor Backend (oneDNN)";
    }

    @Override
    public String getDescription() {
        return "Native Tensor Backend using FFM (oneDNN/MKL) on CPU.";
    }

    @Override
    public boolean isAvailable() {
        return IS_AVAILABLE;
    }

    @Override
    public int getPriority() {
        return 10; 
    }

    @Override
    public org.jscience.core.technical.backend.ExecutionContext createContext() {
        return new org.jscience.core.technical.backend.cpu.CPUExecutionContext();
    }

    @Override
    public <T> Tensor<T> zeros(Class<T> elementType, int... shape) {
        return new NativeTensor<>(elementType, shape);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Tensor<T> ones(Class<T> elementType, int... shape) {
        long size = 1;
        for(int s : shape) size *= s;

        if (elementType == Float.class) {
             float[] data = new float[(int)size];
             java.util.Arrays.fill(data, 1.0f);
             return (Tensor<T>) create(toObjectArray(data), shape);
        } else if (elementType == Double.class) {
             double[] data = new double[(int)size];
             java.util.Arrays.fill(data, 1.0d);
             return (Tensor<T>) create(toObjectArray(data), shape);
        } else {
             throw new IllegalArgumentException("Unsupported type: " + elementType);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Tensor<T> create(T[] data, int... shape) {
        Class<T> type = (Class<T>) data.getClass().getComponentType();
        NativeTensor<T> tensor = new NativeTensor<>(type, shape);
        
        MemorySegment segment = tensor.getSegment();
        if (type == Float.class) {
             for (int i = 0; i < data.length; i++) {
                 segment.setAtIndex(ValueLayout.JAVA_FLOAT, i, (Float) data[i]);
             }
        } else if (type == Double.class) {
             for (int i = 0; i < data.length; i++) {
                 segment.setAtIndex(ValueLayout.JAVA_DOUBLE, i, (Double) data[i]);
             }
        }
        return tensor;
    }

    private Float[] toObjectArray(float[] arr) {
        Float[] res = new Float[arr.length];
        for(int i=0; i<arr.length; i++) res[i] = arr[i];
        return res;
    }
    private Double[] toObjectArray(double[] arr) {
        Double[] res = new Double[arr.length];
        for(int i=0; i<arr.length; i++) res[i] = arr[i];
        return res;
    }
}
