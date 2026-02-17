/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.util;

import java.lang.foreign.Arena;
import java.lang.foreign.SymbolLookup;
import java.util.Optional;

/**
 * Utility for loading native libraries using the Foreign Function & Memory API.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class NativeLibraryLoader {

    /**
     * Attempts to load a library by name using the default system lookup and library path.
     * 
     * @param libName the library name (e.g., "jscience_vision" without extension or prefix)
     * @param arena the arena to associate with the library loading (if applicable scope management needed)
     * @return an Optional containing the SymbolLookup if found, or empty.
     */
    public static Optional<SymbolLookup> loadLibrary(String libName, Arena arena) {
        try {
            // 0. Priority Check: C:\JScience-Native (User Configuration)
            String os = System.getProperty("os.name", "").toLowerCase();
            boolean isWin = os.contains("win");
            String libFileName = libName + (isWin ? ".dll" : ".so");
            
            // Search Paths
            String[] searchPaths = {
                "C:\\JScience-Native",
                System.getProperty("user.dir") + "/libs/" + libName, // e.g. libs/OpenBLAS
                System.getProperty("user.dir") + "/libs",
                System.getProperty("user.dir")
            };

            for (String path : searchPaths) {
                java.nio.file.Path p = java.nio.file.Path.of(path, libFileName);
                 if (java.nio.file.Files.exists(p)) {
                    return Optional.of(SymbolLookup.libraryLookup(p, arena));
                }
                // Try with "lib" prefix?
                java.nio.file.Path pLib = java.nio.file.Path.of(path, (isWin ? "" : "lib") + libName + (isWin ? ".dll" : ".so"));
                if (java.nio.file.Files.exists(pLib)) {
                     return Optional.of(SymbolLookup.libraryLookup(pLib, arena));
                }
            }

            // 1. Try System.loadLibrary equivalent via libraryLookup
            return Optional.of(SymbolLookup.libraryLookup(libName, arena));
        } catch (IllegalArgumentException | UnsatisfiedLinkError e) {
            return Optional.empty();
        }
    }

    /**
     * Looks up a symbol in the given lookup or system loader.
     */
    public static Optional<SymbolLookup> getSystemLookup() {
         return Optional.of(SymbolLookup.loaderLookup());
    }
}
