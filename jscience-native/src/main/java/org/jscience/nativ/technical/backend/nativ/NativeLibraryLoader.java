/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.technical.backend.nativ;

import java.lang.foreign.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Robust native library loader using Project Panama.
 * <p>
 * Handles cross-platform library discovery and dynamic loading.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class NativeLibraryLoader {

    private static final Linker LINKER = Linker.nativeLinker();

    /**
     * Loads a native library by name.
     * 
     * @param libName Name of the library (e.g., "cuda", "opencl", "fftw3")
     * @return SymbolLookup for the loaded library
     * @throws RuntimeException if the library cannot be found or loaded
     */
    public static SymbolLookup loadLibrary(String libName) {
        
        // Handle common Windows DLL variations (e.g., libfftw3-3.dll vs fftw3.dll)
        List<String> variants = new ArrayList<>();
        variants.add(libName);
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            variants.add("lib" + libName);
            variants.add("lib" + libName + "-3");
            variants.add(libName + "-3");
        }

        for (String variant : variants) {
            String currentMapped = System.mapLibraryName(variant);
            try {
                // Try system lookup first
                return SymbolLookup.libraryLookup(variant, Arena.global());
            } catch (Exception e) {
                // Try common paths
                String[] searchPaths = {
                    "/usr/local/lib/",
                    "/usr/lib/",
                    "C:\\Windows\\System32\\",
                    System.getenv("NATIVE_ROOT") != null ? System.getenv("NATIVE_ROOT") : "C:\\JScience-Native",
                    "C:\\JScience-Native\\FFTW3",
                    "C:\\Program Files\\NVIDIA GPU Computing Toolkit\\CUDA\\v12.0\\bin\\",
                    System.getProperty("user.dir"),
                    System.getProperty("user.dir") + java.io.File.separator + "libs",
                    System.getProperty("user.dir") + java.io.File.separator + "libs" + java.io.File.separator + libName, // e.g. libs/FFTW3
                    System.getProperty("user.dir") + java.io.File.separator + "libs" + java.io.File.separator + "native",
                    System.getProperty("user.dir") + java.io.File.separator + "native"
                };
                
                for (String path : searchPaths) {
                    try {
                        java.nio.file.Path fullPath = java.nio.file.Paths.get(path, currentMapped);
                        if (java.nio.file.Files.exists(fullPath)) {
                            return SymbolLookup.libraryLookup(fullPath, Arena.global());
                        }
                    } catch (Exception ex) {
                        // continue
                    }
                }
            }
        }
        
        throw new RuntimeException("Could not load native library: " + libName + " (tried variants: " + variants + ")");
    }

    public static Linker getLinker() {
        return LINKER;
    }
}
