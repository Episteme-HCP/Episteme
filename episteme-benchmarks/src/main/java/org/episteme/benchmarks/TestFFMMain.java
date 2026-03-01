package org.episteme.benchmarks;

import org.episteme.nativ.mathematics.tensors.backends.NativeCPUTensorBackend;
import org.episteme.nativ.io.backends.NativeArrowBackend;
import org.episteme.nativ.mathematics.linearalgebra.tensors.backends.ND4JCUDATensorBackend;

public class TestFFMMain {
    public static void main(String[] args) {
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
            java.lang.foreign.SymbolLookup lookup = java.lang.foreign.SymbolLookup.loaderLookup();
            System.out.println("Loader Lookup found ArrowArrayImport: " + lookup.find("ArrowArrayImport").isPresent());
            NativeArrowBackend arrowBackend = new NativeArrowBackend();
            System.out.println("NativeArrowBackend available: " + arrowBackend.isAvailable());
        } catch (Throwable t) {
            t.printStackTrace();
        }

        try {
            ND4JCUDATensorBackend nd4jCuda = new ND4JCUDATensorBackend();
            System.out.println("ND4JCUDATensorBackend available: " + nd4jCuda.isAvailable());
            org.episteme.nativ.mathematics.linearalgebra.tensors.backends.ND4JNativeTensorBackend nd4jNative = new org.episteme.nativ.mathematics.linearalgebra.tensors.backends.ND4JNativeTensorBackend();
            System.out.println("ND4JNativeTensorBackend available: " + nd4jNative.isAvailable());
            org.episteme.nativ.mathematics.linearalgebra.tensors.backends.ND4JSparseTensorBackend nd4jSparse = new org.episteme.nativ.mathematics.linearalgebra.tensors.backends.ND4JSparseTensorBackend();
            System.out.println("ND4JSparseTensorBackend available: " + nd4jSparse.isAvailable());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.out.println("--- TESTING OTHER BACKENDS END ---");
    }
}
