package org.jscience.apps.apps.util;

import org.jscience.nativ.technical.backend.nativ.NativeLibraryLoader;
import java.lang.foreign.Arena;
import java.util.Optional;

public class LoaderVerification {
    public static void main(String[] args) {
        System.out.println("=== JScience Native Loader Verification ===");
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        
        String[] libs = {"openblas", "fftw3", "hdf5", "bulletc", "ode", "sndfile", "arrow", "quest", "vlc", "cuda"};
        
        for (String lib : libs) {
            System.out.print("Checking " + lib + "... ");
            try {
                if (lib.equals("arrow")) {
                   System.out.print("(pre-loading parquet) ");
                   NativeLibraryLoader.loadLibrary("parquet", Arena.ofAuto());
                }
                if (NativeLibraryLoader.loadLibrary(lib, Arena.ofAuto()).isPresent()) {
                    System.out.println("OK");
                } else {
                    System.out.println("MISSING");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
        System.out.println("==========================================");
    }
}