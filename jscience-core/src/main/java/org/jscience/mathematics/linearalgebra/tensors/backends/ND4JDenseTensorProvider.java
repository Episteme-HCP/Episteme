/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.linearalgebra.tensors.backends;

import org.jscience.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.mathematics.linearalgebra.tensors.DenseTensor;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.technical.backend.ExecutionContext;
import org.jscience.technical.backend.cuda.CUDAExecutionContext;

/**
 * ND4J-backed TensorProvider implementation.
 * <p>
 * This provider uses ND4J (N-Dimensional Arrays for Java) for GPU-accelerated
 * tensor operations. When ND4J is not available on the classpath, it falls back
 * to the native implementation.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ND4JDenseTensorProvider implements TensorProvider {

    private static boolean isAvailable = false;
    private static final CPUDenseTensorProvider fallback = new CPUDenseTensorProvider();

    static {
        try {
            // Try to load ND4J classes
            Class.forName("org.nd4j.linalg.factory.Nd4j");
            Class.forName("org.nd4j.linalg.api.ndarray.INDArray");
            isAvailable = true;
        } catch (ClassNotFoundException e) {
            isAvailable = false;
        } catch (NoClassDefFoundError e) {
            isAvailable = false;
        } catch (UnsatisfiedLinkError e) {
            isAvailable = false;
        }
    }

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public ExecutionContext createContext() {
        if (!isAvailable) {
            throw new IllegalStateException("ND4J backend not available");
        }
        // ND4J manages its own context usually, but if we need a return
        return new CUDAExecutionContext();
    }

    @Override
    public boolean supportsParallelOps() {
        return true;
    }

    /**
     * Returns true if ND4J is available on the classpath.
     * 
     * @return true if ND4J can be used
     */
    public static boolean isND4JAvailable() {
        return isAvailable;
    }

    @Override
    public String getName() {
        return "ND4J";
    }

    @Override
    public int getPriority() {
        return isAvailable ? 80 : 0;
    }

    @Override
    public boolean supportsGPU() {
        if (!isAvailable) {
            return false;
        }
        try {
            // Check if CUDA backend is available
            Class.forName("org.nd4j.jita.allocator.impl.AtomicAllocator");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Tensor<T> zeros(Class<T> elementType, int... shape) {
        if (!isAvailable) {
            return fallback.zeros(elementType, shape);
        }
        
        if (!Real.class.isAssignableFrom(elementType)) {
            return fallback.zeros(elementType, shape);
        }
        
        try {
            // Create ND4J zeros array
            long[] longShape = new long[shape.length];
            for (int i = 0; i < shape.length; i++) {
                longShape[i] = shape[i];
            }
            
            org.nd4j.linalg.api.ndarray.INDArray ndArray = 
                org.nd4j.linalg.factory.Nd4j.zeros(longShape);
            
            // Convert to JScience Tensor
            return (Tensor<T>) fromINDArray(ndArray);
        } catch (Exception e) {
            return fallback.zeros(elementType, shape);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Tensor<T> ones(Class<T> elementType, int... shape) {
        if (!isAvailable) {
            return fallback.ones(elementType, shape);
        }
        
        if (!Real.class.isAssignableFrom(elementType)) {
            return fallback.ones(elementType, shape);
        }
        
        try {
            // Create ND4J ones array
            long[] longShape = new long[shape.length];
            for (int i = 0; i < shape.length; i++) {
                longShape[i] = shape[i];
            }
            
            org.nd4j.linalg.api.ndarray.INDArray ndArray = 
                org.nd4j.linalg.factory.Nd4j.ones(longShape);
            
            // Convert to JScience Tensor
            return (Tensor<T>) fromINDArray(ndArray);
        } catch (Exception e) {
            return fallback.ones(elementType, shape);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Tensor<T> create(T[] data, int... shape) {
        if (!isAvailable) {
            return fallback.create(data, shape);
        }
        
        if (data.length == 0 || !(data[0] instanceof Real)) {
            return fallback.create(data, shape);
        }
        
        try {
            // Convert data to double array
            double[] doubleData = new double[data.length];
            for (int i = 0; i < data.length; i++) {
                doubleData[i] = ((Real) data[i]).doubleValue();
            }
            
            // Create ND4J array with shape
            long[] longShape = new long[shape.length];
            for (int i = 0; i < shape.length; i++) {
                longShape[i] = shape[i];
            }
            
            org.nd4j.linalg.api.ndarray.INDArray ndArray = 
                org.nd4j.linalg.factory.Nd4j.create(doubleData, longShape);
            
            // Convert to JScience Tensor
            return (Tensor<T>) fromINDArray(ndArray);
        } catch (Exception e) {
            return fallback.create(data, shape);
        }
    }

    /**
     * Converts a JScience Tensor to an ND4J INDArray (when ND4J is available).
     * 
     * @param tensor the tensor to convert
     * @return the ND4J INDArray representation
     */
    public org.nd4j.linalg.api.ndarray.INDArray toINDArray(Tensor<Real> tensor) {
        if (!isAvailable) {
            throw new UnsupportedOperationException("ND4J is not available");
        }
        
        int[] shape = tensor.shape();
        long[] longShape = new long[shape.length];
        for (int i = 0; i < shape.length; i++) {
            longShape[i] = shape[i];
        }
        
        // Calculate total size
        int totalSize = 1;
        for (int dim : shape) {
            totalSize *= dim;
        }
        
        // Extract data as flat array
        double[] data = new double[totalSize];
        int[] indices = new int[shape.length];
        
        for (int i = 0; i < totalSize; i++) {
            // Calculate indices for this position
            int remaining = i;
            for (int d = shape.length - 1; d >= 0; d--) {
                indices[d] = remaining % shape[d];
                remaining /= shape[d];
            }
            data[i] = tensor.get(indices).doubleValue();
        }
        
        return org.nd4j.linalg.factory.Nd4j.create(data, longShape);
    }

    /**
     * Creates a JScience Tensor from an ND4J INDArray.
     * 
     * @param indArray the ND4J array to convert
     * @return the JScience Tensor representation
     */
    public Tensor<Real> fromINDArray(org.nd4j.linalg.api.ndarray.INDArray indArray) {
        if (!isAvailable) {
            throw new UnsupportedOperationException("ND4J is not available");
        }
        
        long[] longShape = indArray.shape();
        int[] shape = new int[longShape.length];
        for (int i = 0; i < longShape.length; i++) {
            shape[i] = (int) longShape[i];
        }
        
        // Calculate total size
        int totalSize = 1;
        for (int dim : shape) {
            totalSize *= dim;
        }
        
        // Extract data
        Real[] data = new Real[totalSize];
        double[] ndData = indArray.data().asDouble();
        
        for (int i = 0; i < totalSize; i++) {
            data[i] = Real.of(ndData[i]);
        }
        
        return new DenseTensor<>(data, shape);
    }

    @Override
    public String getId() {
        return "nd4jdense";
    }

    @Override
    public String getDescription() {
        return "ND4J Dense Tensor Provider - GPU-accelerated N-dimensional arrays";
    }
}
