/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.mathematics.linearalgebra.tensors.providers;

import org.jscience.core.mathematics.linearalgebra.Tensor;
import org.jscience.nativ.mathematics.linearalgebra.tensors.NativeTensor;
import org.jscience.nativ.mathematics.tensors.backends.NativeCPUTensorBackend;
import org.junit.jupiter.api.Test;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

import static org.junit.jupiter.api.Assertions.*;

class NativeCPUTensorBackendTest {

    @Test
    void testProviderMetadata() {
        NativeCPUTensorBackend provider = new NativeCPUTensorBackend();
        assertEquals("native-cpu-tensor", provider.getId());
        assertEquals("cpu", provider.getType());
        assertNotNull(provider.getDescription());
    }

    @Test
    void testIsAvailableDefaultsToFalseOrTrueDependingOnEnvironment() {
        // We can't strictly assert true or false without knowing if DNNL is installed.
        // But we can assert it returns a boolean safely.
        NativeCPUTensorBackend provider = new NativeCPUTensorBackend();
        provider.isAvailable();
        // Just ensure no exception
    }

    @Test
    void testCreateTensorFloat() {
        NativeCPUTensorBackend provider = new NativeCPUTensorBackend();
        Float[] data = {1.0f, 2.0f, 3.0f, 4.0f};
        Tensor<Float> tensor = provider.create(data, 2, 2);
        
        assertNotNull(tensor);
        assertTrue(tensor instanceof NativeTensor);
        assertEquals(2, tensor.rank());
        assertArrayEquals(new int[]{2, 2}, tensor.shape());
        assertEquals(4, tensor.size());
        
        assertEquals(1.0f, tensor.get(0, 0));
        assertEquals(4.0f, tensor.get(1, 1));
    }

    @Test
    void testCreateTensorDouble() {
        NativeCPUTensorBackend provider = new NativeCPUTensorBackend();
        Double[] data = {1.0, 2.0, 3.0, 4.0};
        Tensor<Double> tensor = provider.create(data, 2, 2);
        
        assertNotNull(tensor);
        assertEquals(1.0, tensor.get(0, 0));
        assertEquals(4.0, tensor.get(1, 1));
    }

    @Test
    void testAddTensor() {
        NativeCPUTensorBackend provider = new NativeCPUTensorBackend();
        Float[] d1 = {1.0f, 2.0f};
        Float[] d2 = {10.0f, 20.0f};
        
        Tensor<Float> t1 = provider.create(d1, 2);
        Tensor<Float> t2 = provider.create(d2, 2);
        
        Tensor<Float> result = t1.add(t2);
        
        assertEquals(11.0f, result.get(0));
        assertEquals(22.0f, result.get(1));
    }

    @Test
    void testSumTensor() {
        NativeCPUTensorBackend provider = new NativeCPUTensorBackend();
        Float[] d1 = {1.0f, 2.0f, 3.0f, 4.0f};
        Tensor<Float> t1 = provider.create(d1, 4);
        
        Float sum = t1.sum();
        assertEquals(10.0f, sum, 0.0001f);
    }
    
    @Test
    void testMemoryLayout() {
        // Verify off-heap storage works
        NativeCPUTensorBackend provider = new NativeCPUTensorBackend();
        Float[] data = {123.456f};
        NativeTensor<Float> tensor = (NativeTensor<Float>) provider.create(data, 1);
        
        MemorySegment segment = tensor.getSegment();
        float val = segment.getAtIndex(ValueLayout.JAVA_FLOAT, 0);
        assertEquals(123.456f, val, 0.0001f);
    }
}
