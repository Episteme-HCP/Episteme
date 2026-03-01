package org.episteme.benchmarks;

import org.junit.jupiter.api.Test;
import org.episteme.nativ.mathematics.tensors.backends.NativeCPUTensorBackend;
import org.episteme.nativ.io.backends.NativeArrowBackend;
import org.episteme.nativ.mathematics.linearalgebra.tensors.backends.ND4JCUDATensorBackend;

public class TestFFM {
    @Test
    public void testFFM() {
        System.out.println("--- TESTING OTHER BACKENDS START ---");
        try {
            NativeCPUTensorBackend tensorBackend = new NativeCPUTensorBackend();
            System.out.println("NativeCPUTensorBackend available: " + tensorBackend.isAvailable());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        
        try {
            System.out.println("Preloading Bytedeco Arrow...");
            org.bytedeco.javacpp.Loader.load(org.bytedeco.arrow.global.arrow.class);
            NativeArrowBackend arrowBackend = new NativeArrowBackend();
            System.out.println("NativeArrowBackend available: " + arrowBackend.isAvailable());
        } catch (Throwable t) {
            t.printStackTrace(System.out);
        }

        try {
            ND4JCUDATensorBackend nd4jCuda = new ND4JCUDATensorBackend();
            System.out.println("ND4JCUDATensorBackend available: " + nd4jCuda.isAvailable());
        } catch (Throwable t) {
            t.printStackTrace(System.out);
        }
        System.out.println("--- TESTING OTHER BACKENDS END ---");
    }
}
