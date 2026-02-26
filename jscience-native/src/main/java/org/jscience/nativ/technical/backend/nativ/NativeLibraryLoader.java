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
            
            // CUDA/BLAS specific Windows naming
            if (libName.equals("cuda")) {
                variants.add("nvcuda");
            } else if (libName.equals("cublas")) {
                variants.add("cublas64_13");
                variants.add("cublas64_12");
                variants.add("cublas64_013"); // Some versions use 013
                variants.add("cublas64_11");
                variants.add("cublas64_10");
            } else if (libName.equals("cusolver")) {
                variants.add("cusolver64_13");
                variants.add("cusolver64_12");
                variants.add("cusolver64_11");
            } else if (libName.equals("cudart")) {
                variants.add("cudart64_13");
                variants.add("cudart64_130");
                variants.add("cudart64_120");
                variants.add("cudart64_110");
            } else if (libName.equals("sndfile")) {
                variants.add("libsndfile-1");
            }
        }

        for (String variant : variants) {
            String currentMapped = System.mapLibraryName(variant);
            System.out.println("[DEBUG] NativeLibraryLoader: Attempting to load " + variant + " (mapped: " + currentMapped + ")");
            
            // 1. Try system lookup first
            try {
                SymbolLookup lookup = SymbolLookup.libraryLookup(variant, arena);
                System.out.println("[INFO] NativeLibraryLoader: Successfully loaded " + variant + " from system paths.");
                return Optional.of(lookup);
            } catch (Exception e) {
                // System.out.println("[DEBUG] NativeLibraryLoader: System load failed for " + variant + ": " + e.getMessage());
            }

            // 2. Try custom search paths
            String cudaPath = System.getenv("CUDA_PATH");
            String[] searchPaths = {
                "/usr/local/lib/",
                "/usr/lib/",
                "C:\\Windows\\System32\\",
                System.getenv("NATIVE_ROOT"),
                cudaPath != null ? cudaPath + java.io.File.separator + "bin" : null,
                "C:\\Program Files\\NVIDIA GPU Computing Toolkit\\CUDA\\v12.0\\bin\\",
                "C:\\Program Files\\NVIDIA GPU Computing Toolkit\\CUDA\\v11.8\\bin\\",
                "C:\\Program Files\\NVIDIA GPU Computing Toolkit\\CUDA\\v12.1\\bin\\",
                System.getProperty("user.dir"),
                System.getProperty("user.dir") + java.io.File.separator + "libs",
                System.getProperty("user.dir") + java.io.File.separator + "launchers" + java.io.File.separator + "libs" + java.io.File.separator + "libs",
                System.getProperty("user.dir") + java.io.File.separator + "native"
            };

            for (String path : searchPaths) {
                if (path == null) continue;
                Optional<SymbolLookup> found = tryLoadFromDirectory(java.nio.file.Paths.get(path), currentMapped, arena);
                if (found.isPresent()) {
                    System.out.println("[INFO] NativeLibraryLoader: Successfully loaded " + variant + " from " + path);
                    return found;
                }
            }
            
            // 3. Scan subdirectories of "libs" if specific one wasn't there
            java.io.File libsDir = new java.io.File(System.getProperty("user.dir") + java.io.File.separator + "libs");
            if (libsDir.exists() && libsDir.isDirectory()) {
                java.io.File[] subDirs = libsDir.listFiles(java.io.File::isDirectory);
                if (subDirs != null) {
                    for (java.io.File sub : subDirs) {
                        Optional<SymbolLookup> found = tryLoadFromDirectory(sub.toPath(), currentMapped, arena);
                        if (found.isPresent()) {
                            System.out.println("[INFO] NativeLibraryLoader: Successfully loaded " + variant + " from subdirectory: " + sub.getName());
                            return found;
                        }
                    }
                }
            }
        }
        
        System.err.println("[WARNING] Could not find or load native library " + libName + " (tried variants: " + variants + ")");
        return Optional.empty();
    }

    private static Optional<SymbolLookup> tryLoadFromDirectory(java.nio.file.Path basePath, String mappedName, Arena arena) {
        java.nio.file.Path fullPath = null;
        try {
            fullPath = basePath.resolve(mappedName).toAbsolutePath();
            if (java.nio.file.Files.exists(fullPath)) {
                System.out.println("[DEBUG] NativeLibraryLoader: Attempting libraryLookup on: " + fullPath);
                return Optional.of(SymbolLookup.libraryLookup(fullPath, arena));
            }
            
            // Try common subdirectories (lib, bin)
            String[] subs = {"lib", "bin"};
            for (String sub : subs) {
                java.nio.file.Path subPath = basePath.resolve(sub).resolve(mappedName).toAbsolutePath();
                if (java.nio.file.Files.exists(subPath)) {
                    fullPath = subPath; // for logging in catch
                    System.out.println("[DEBUG] NativeLibraryLoader: Found candidate at " + subPath);
                    return Optional.of(SymbolLookup.libraryLookup(subPath, arena));
                }
            }
        } catch (Throwable t) {
            System.err.println("[ERROR] NativeLibraryLoader: Failed to load " + mappedName + " from " + (fullPath != null ? fullPath : basePath) + ": " + t.getMessage());
            if (t.getCause() != null) {
                System.err.println("  Cause: " + t.getCause().getMessage());
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
