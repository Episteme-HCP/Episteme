package org.episteme.benchmarks;

import org.episteme.nativ.mathematics.tensors.backends.NativeCPUTensorBackend;
import org.episteme.nativ.io.backends.NativeArrowBackend;
import org.episteme.nativ.mathematics.linearalgebra.tensors.backends.ND4JCUDATensorBackend;

public class TestFFMMain {
    public static void main(String[] args) {
        System.out.println("--- TESTING OTHER BACKENDS START ---");
        try {
            System.out.println("\n--- Vision Backends ---");
            org.episteme.nativ.media.vision.backends.NativeCPUCBindingVisionBackend cBindingVision = new org.episteme.nativ.media.vision.backends.NativeCPUCBindingVisionBackend();
            System.out.println("NativeCPUCBindingVisionBackend available: " + cBindingVision.isAvailable());

            org.episteme.nativ.media.vision.backends.NativeJavaCVVisionBackend javaCvVision = new org.episteme.nativ.media.vision.backends.NativeJavaCVVisionBackend();
            System.out.println("NativeJavaCVVisionBackend available: " + javaCvVision.isAvailable());

            org.episteme.nativ.media.vision.backends.NativeCPUVisionBackend cpuVision = new org.episteme.nativ.media.vision.backends.NativeCPUVisionBackend();
            System.out.println("NativeCPUVisionBackend (OpenCV) available: " + cpuVision.isAvailable());
            
            org.episteme.nativ.media.vision.backends.NativeCUDAVisionBackend cudaVision = new org.episteme.nativ.media.vision.backends.NativeCUDAVisionBackend();
            System.out.println("NativeCUDAVisionBackend available: " + cudaVision.isAvailable());
            
            org.episteme.nativ.media.vision.backends.NativeOpenCLVisionBackend oclVision = new org.episteme.nativ.media.vision.backends.NativeOpenCLVisionBackend();
            System.out.println("NativeOpenCLVisionBackend available: " + oclVision.isAvailable());
            
        } catch (Throwable t) {
            t.printStackTrace(System.out);
        }
        System.out.println("--- TESTING END ---");
    }
}
