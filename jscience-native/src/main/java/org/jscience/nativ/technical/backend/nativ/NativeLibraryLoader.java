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
    private static final java.util.Set<String> FAILED_LIBS = java.util.concurrent.ConcurrentHashMap.newKeySet();

    /**
     * Finds the "libs" directory by searching upwards from user.dir.
     */
    public static java.nio.file.Path findLibsDirectory() {
        java.nio.file.Path current = java.nio.file.Paths.get(System.getProperty("user.dir"));
        while (current != null) {
            java.nio.file.Path libs = current.resolve("libs");
            if (java.nio.file.Files.exists(libs) && java.nio.file.Files.isDirectory(libs)) {
                return libs.toAbsolutePath();
            }
            current = current.getParent();
        }
        return null;
    }

    private static final java.util.Set<String> PRELOADED_RUNTIMES = new java.util.HashSet<>();

    /**
     * Pre-loads common runtime dependencies found in the libs directory.
     */
    private static void preloadRuntimes(java.nio.file.Path libsDir, Arena arena) {
        if (libsDir == null || !java.nio.file.Files.exists(libsDir)) return;
        String[] runtimes = {"libwinpthread-1", "libgcc_s_seh-1", "libstdc++-6", "zlib1", "msvcp140", "vcruntime140"};
        for (String rt : runtimes) {
            if (PRELOADED_RUNTIMES.contains(rt)) continue;
            try {
                // Try to load from the libs root directly
                String mapped = System.mapLibraryName(rt);
                java.nio.file.Path rtPath = libsDir.resolve(mapped).toAbsolutePath();
                if (java.nio.file.Files.exists(rtPath)) {
                    SymbolLookup.libraryLookup(rtPath, arena);
                    PRELOADED_RUNTIMES.add(rt);
                }
            } catch (Throwable ignored) {}
        }
    }

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
        if (FAILED_LIBS.contains(libName)) {
            return Optional.empty();
        }

        List<String> variants = new ArrayList<>();
        variants.add(libName);
        String os = System.getProperty("os.name", "").toLowerCase();
        boolean isWin = os.contains("win");
        
        if (isWin) {
            variants.add("lib" + libName);
            variants.add("lib" + libName + "-3");
            variants.add(libName + "-3");
            
            // Specialized Windows naming
            if (libName.equals("cuda")) {
                variants.add("nvcuda");
            } else if (libName.equals("cublas")) {
                variants.add("cublas64_13");
                variants.add("cublas64_12");
                variants.add("cublas64_013");
                variants.add("cublas64_11");
                variants.add("cublas64_10"); 
            } else if (libName.equals("cusolver")) {
                variants.add("cusolver64_13");
                variants.add("cusolver64_12");
                variants.add("cusolver64_11"); 
            } else if (libName.equals("cudart")) {
                variants.add("cudart64_130");
                variants.add("cudart64_120");
                variants.add("cudart64_13"); 
                variants.add("cudart64_110"); 
            } else if (libName.equals("sndfile")) {
                variants.add("libsndfile-1");
            } else if (libName.equals("vlc")) {
                variants.add("libvlc"); 
                variants.add("libvlccore"); 
            } else if (libName.equals("dnnl")) {
                variants.add("libdnnl");
                variants.add("mkldnn");
            } else if (libName.equals("arrow")) {
                variants.add("parquet");
                variants.add("arrow_dataset");
                variants.add("arrow_acero");
            }
        }

        java.nio.file.Path discoveredLibs = findLibsDirectory();
        if (discoveredLibs != null) {
            preloadRuntimes(discoveredLibs, arena);
        }

        for (String variant : variants) {
            String currentMapped = System.mapLibraryName(variant);
            
            try {
                SymbolLookup lookup = SymbolLookup.libraryLookup(variant, arena);
                return Optional.of(lookup);
            } catch (Exception e) {
                // System.out.println("[DEBUG] NativeLibraryLoader: System load failed for " + variant + ": " + e.getMessage());
            }

            // 2. Try custom search paths
            String cudaPath = System.getenv("CUDA_PATH");
            List<String> searchPaths = new ArrayList<>();
            searchPaths.add("/usr/local/lib/");
            searchPaths.add("/usr/lib/");
            searchPaths.add("C:\\Windows\\System32\\");
            
            if (System.getenv("NATIVE_ROOT") != null) searchPaths.add(System.getenv("NATIVE_ROOT"));
            if (cudaPath != null) searchPaths.add(cudaPath + java.io.File.separator + "bin");
            
            if (discoveredLibs != null) searchPaths.add(discoveredLibs.toString());

            searchPaths.add(System.getProperty("user.dir"));
            searchPaths.add(System.getProperty("user.dir") + java.io.File.separator + "libs");
            
            for (String path : searchPaths) {
                if (path == null || path.isEmpty()) continue; 
                Optional<SymbolLookup> found = tryLoadFromDirectory(java.nio.file.Paths.get(path), currentMapped, arena);
                if (found.isPresent()) {
                    return found;
                }
            }
            
            if (discoveredLibs != null && java.nio.file.Files.isDirectory(discoveredLibs)) {
                java.io.File[] subDirs = discoveredLibs.toFile().listFiles(java.io.File::isDirectory);
                if (subDirs != null) {
                    for (java.io.File sub : subDirs) {
                        Optional<SymbolLookup> found = tryLoadFromDirectory(sub.toPath(), currentMapped, arena);
                        if (found.isPresent()) {
                            System.out.println("[INFO] NativeLibraryLoader: Successfully loaded " + variant + " from subdirectory: " + sub.getName());
                            System.out.flush();
                            return found;
                        }
                    }
                }
            }
        }
        
        System.err.println("[WARNING] Could not find or load native library " + libName + " (tried variants: " + variants + ")");
        System.err.flush();
        FAILED_LIBS.add(libName);
        return Optional.empty();
    }

    private static Optional<SymbolLookup> tryLoadFromDirectory(java.nio.file.Path basePath, String mappedName, Arena arena) {
        java.nio.file.Path fullPath = null;
        try {
            fullPath = basePath.resolve(mappedName).toAbsolutePath();
            if (java.nio.file.Files.exists(fullPath)) {
                return Optional.of(SymbolLookup.libraryLookup(fullPath, arena));
            }
            
            String[] subs = {"lib", "bin"};
            for (String sub : subs) {
                java.nio.file.Path subPath = basePath.resolve(sub).resolve(mappedName).toAbsolutePath();
                if (java.nio.file.Files.exists(subPath)) {
                    fullPath = subPath; 
                    return Optional.of(SymbolLookup.libraryLookup(subPath, arena));
                }
            }
        } catch (Throwable t) {
            System.err.println("[ERROR] NativeLibraryLoader: Failed to load " + mappedName + " from " + (fullPath != null ? fullPath : basePath) + ": " + t.getMessage());
            if (t.getCause() != null) {
                System.err.println("  Cause: " + t.getCause().getMessage());
            }
            System.err.flush();
        }
        return Optional.empty();
    }

    public static Optional<SymbolLookup> getSystemLookup() {
         return Optional.of(SymbolLookup.loaderLookup());
    }

    public static Linker getLinker() {
        return LINKER;
    }

    public static Optional<MemorySegment> findSymbol(SymbolLookup lookup, String... names) {
        if (lookup == null) return Optional.empty();
        for (String name : names) {
            Optional<MemorySegment> segment = lookup.find(name);
            if (segment.isPresent()) return segment;

            segment = lookup.find("_" + name);
            if (segment.isPresent()) return segment;
        }
        return Optional.empty();
    }

    public static void clearCache() {
        FAILED_LIBS.clear();
        System.out.println("[INFO] NativeLibraryLoader: Failure cache cleared.");
        System.out.flush();
    }
}
