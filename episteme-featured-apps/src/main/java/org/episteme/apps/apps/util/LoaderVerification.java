package org.episteme.apps.apps.util;

import org.episteme.jni.jni.NativeJNILoader;


public class LoaderVerification {
    public static void main(String[] args) {
        System.out.println("=== Episteme Native Loader Verification ===");
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        System.out.println("NATIVE_ROOT: " + System.getenv("NATIVE_ROOT"));
        System.out.println("java.library.path: " + System.getProperty("java.library.path"));
        
        String[] libs = {"openblas", "fftw3", "hdf5", "libbulletc", "ode", "libsndfile", "arrow", "QuEST", "libvlc", "cuda", "episteme-jni"};
        
        for (String lib : libs) {
            System.out.print("Checking " + lib + "... ");
            try {
                NativeJNILoader.loadLibrary(lib);
                System.out.println("OK");
            } catch (Throwable t) {
                System.out.println("ERROR: " + t.getMessage());
            }
        }
                System.out.println("ERROR: " + t.getMessage());
            }
        }
        System.out.println("==========================================");

        System.out.println("=== Algorithm Provider Discovery ===");
        java.util.ServiceLoader<org.episteme.core.technical.algorithm.AlgorithmProvider> pLoader = 
            java.util.ServiceLoader.load(org.episteme.core.technical.algorithm.AlgorithmProvider.class);
        int pCount = 0;
        for (org.episteme.core.technical.algorithm.AlgorithmProvider p : pLoader) {
            pCount++;
            System.out.println(String.format("[%d] %s: %s (Available: %b)", 
                pCount, p.getAlgorithmType(), p.getName(), p.isAvailable()));
        }
        System.out.println("Total providers discovered: " + pCount);
        System.out.println("==========================================");
    }
}
