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
            // 1. Try System.loadLibrary equivalent via libraryLookup
            // Note: libraryLookup requires a specific file path or name usually handled by OS loader.
            // For simplicity in FFM, we often use SymbolLookup.libraryLookup(name, arena)
            return Optional.of(SymbolLookup.libraryLookup(libName, arena));
        } catch (IllegalArgumentException | UnsatisfiedLinkError e) {
            // Library not found in java.library.path
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
