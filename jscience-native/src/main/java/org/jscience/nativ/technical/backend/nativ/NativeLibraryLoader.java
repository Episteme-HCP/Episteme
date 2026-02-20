/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.technical.backend.nativ;

import java.lang.foreign.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Robust native library loader using Project Panama (Foreign Function & Memory API).
 * <p>
 * Handles cross-platform library discovery and dynamic loading with variant support.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class NativeLibraryLoader {

    private static final Linker LINKER = Linker.nativeLinker();

    /**
     * Loads a native library by name using a global arena.
     * 
     * @param libName Name of the library (e.g., "cuda", "opencl", "fftw3")
     * @return SymbolLookup for the loaded library
     * @throws RuntimeException if the library cannot be found or loaded
     */
    public static SymbolLookup loadLibrary(String libName) {
        return loadLibrary(libName, Arena.global()).orElseThrow(() -> 
            new RuntimeException("Could not load native library: " + libName));
    }

    /**
     * Attempts to load a library by name with variant support and multiple search paths.
     * 
     * @param libName the library name
     * @param arena the arena to associate with the library loading
     * @return an Optional containing the SymbolLookup if found, or empty.
     */
    public static Optional<SymbolLookup> loadLibrary(String libName, Arena arena) {
        
        List<String> variants = new ArrayList<>();
        variants.add(libName);
        String os = System.getProperty("os.name", "").toLowerCase();
        boolean isWin = os.contains("win");
        
        if (isWin) {
            variants.add("lib" + libName);
            variants.add("lib" + libName + "-3");
            variants.add(libName + "-3");
        }

        for (String variant : variants) {
            String currentMapped = System.mapLibraryName(variant);
            try {
                // 1. Try system lookup first (standard java.library.path)
                return Optional.of(SymbolLookup.libraryLookup(variant, arena));
            } catch (Exception e) {
                // 2. Try common and custom search paths
                String[] searchPaths = {
                    "/usr/local/lib/",
                    "/usr/lib/",
                    "C:\\Windows\\System32\\",
                    System.getenv("NATIVE_ROOT"),
                    "C:\\Program Files\\NVIDIA GPU Computing Toolkit\\CUDA\\v12.0\\bin\\",
                    System.getProperty("user.dir"),
                    System.getProperty("user.dir") + java.io.File.separator + "libs",
                    System.getProperty("user.dir") + java.io.File.separator + "libs" + java.io.File.separator + libName,
                    System.getProperty("user.dir") + java.io.File.separator + "libs" + java.io.File.separator + "native",
                    System.getProperty("user.dir") + java.io.File.separator + "native"
                };
                
                for (String path : searchPaths) {
                    if (path == null) continue;
                    try {
                        java.nio.file.Path fullPath = java.nio.file.Paths.get(path, currentMapped);
                        if (java.nio.file.Files.exists(fullPath)) {
                            return Optional.of(SymbolLookup.libraryLookup(fullPath, arena));
                        }
                    } catch (Exception ex) {
                        // continue
                    }
                }
            }
        }
        
        return Optional.empty();
    }

    /**
     * Looks up a symbol in the given lookup or system loader.
     */
    public static Optional<SymbolLookup> getSystemLookup() {
         return Optional.of(SymbolLookup.loaderLookup());
    }

    public static Linker getLinker() {
        return LINKER;
    }
}
