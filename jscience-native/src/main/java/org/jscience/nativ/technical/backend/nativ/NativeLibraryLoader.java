/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.technical.backend.nativ;

import java.lang.foreign.*;
import java.nio.file.Paths;

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
        String mappedName = System.mapLibraryName(libName);
        
        // Try system lookup first
        SymbolLookup.loaderLookup();
        
        try {
            return SymbolLookup.libraryLookup(libName, Arena.global());
        } catch (IllegalArgumentException e) {
            // Try common paths
            String[] searchPaths = {
                "/usr/local/lib/",
                "/usr/lib/",
                "C:\\Windows\\System32\\",
                "C:\\Program Files\\NVIDIA GPU Computing Toolkit\\CUDA\\v12.0\\bin\\"
            };
            
            for (String path : searchPaths) {
                try {
                    return SymbolLookup.libraryLookup(Paths.get(path, mappedName), Arena.global());
                } catch (Exception ex) {
                    // continue
                }
            }
        }
        
        throw new RuntimeException("Could not load native library: " + libName);
    }

    public static Linker getLinker() {
        return LINKER;
    }
}
