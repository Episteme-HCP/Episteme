package org.jscience.apps.apps.util;

import org.jscience.nativ.technical.backend.nativ.NativeLibraryLoader;
import java.lang.foreign.Arena;
import java.util.Optional;

public class LoaderVerification {
    public static void main(String[] args) {
        System.out.println("=== JScience Native Loader Verification ===");
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        
        String[] libs = {"openblas", "fftw3", "hdf5", "bullet_capi", "ode", "libsndfile", "arrow", "QuEST", "libvlc", "cuda"};
        
        for (String lib : libs) {
            System.out.print("Checking " + lib + "... ");
            try {
                if (lib.equals("arrow")) {
                   System.out.print("(pre-loading parquet) ");
                   try { NativeLibraryLoader.loadLibrary("parquet", Arena.ofAuto()); } catch (Exception e) {}
                }
                if (NativeLibraryLoader.loadLibrary(lib, Arena.ofAuto()).isPresent()) {
                    System.out.println("OK");
                } else {
                    System.out.println("MISSING (Check dependencies in NATIVE_LIBS_SETUP.md)");
                }
            } catch (Throwable t) {
                System.out.println("ERROR: " + t.getMessage());
            }
        }
        System.out.println("==========================================");
    }
}