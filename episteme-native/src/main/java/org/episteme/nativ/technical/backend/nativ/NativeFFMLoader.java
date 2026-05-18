/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.technical.backend.nativ;

import java.lang.foreign.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.episteme.core.technical.backend.nativ.NativeDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class NativeFFMLoader {

    private static final Logger logger = LoggerFactory.getLogger(NativeFFMLoader.class);
    private static final Linker LINKER;
    private static final java.util.Set<String> FAILED_LIBS = java.util.concurrent.ConcurrentHashMap.newKeySet();
    private static final java.util.Map<String, String> FAILURE_CAUSES = new java.util.concurrent.ConcurrentHashMap<>();
    private static final java.util.Map<String, SymbolLookup> LOADED_LIBS = new java.util.concurrent.ConcurrentHashMap<>();

    static {
        Linker temp = null;
        try {
            temp = Linker.nativeLinker();
        } catch (Throwable t) {
            System.err.println("[NativeFFMLoader] CRITICAL: Linker.nativeLinker() failed: " + t.getMessage());
            t.printStackTrace();
        }
        LINKER = temp;
    }

    private static final java.util.Set<String> PRELOADED_RUNTIMES = new java.util.HashSet<>();

    /**
     * Pre-loads common runtime dependencies found in the libs directory.
     */
    private static void preloadRuntimes(java.nio.file.Path libsDir, Arena arena) {
        if (libsDir == null || !java.nio.file.Files.exists(libsDir)) return;
        String[] runtimes = {"libwinpthread-1", "libgcc_s_seh-1", "libstdc++-6", "zlib1", "msvcp140", "vcruntime140", "libgfortran-5", "libquadmath-0"};
        for (String rt : runtimes) {
            if (PRELOADED_RUNTIMES.contains(rt)) continue;
            try {
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
        if (Boolean.getBoolean("episteme.native.disable")) {
             logger.warn("[NativeFFMLoader] Native library loading is GLOBALLY disabled via 'episteme.native.disable' property.");
             return Optional.empty();
        }

        if (LOADED_LIBS.containsKey(libName)) {
            return Optional.of(LOADED_LIBS.get(libName));
        }
        if (FAILED_LIBS.contains(libName)) {
            return Optional.empty();
        }
        
        if (Boolean.getBoolean("episteme.native.skip." + libName)) {
             logger.info("[NativeFFMLoader] Skipping library {} as requested by 'episteme.native.skip.{}' property.", libName, libName);
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
            
            if (libName.equals("cuda")) {
                variants.add("nvcuda");
            } else if (libName.startsWith("cu")) {
                // Try major versions 13, 12, 11
                variants.add(libName + "64_13");
                variants.add(libName + "64_12");
                variants.add(libName + "64_11");
                if (libName.equals("cudart")) {
                    variants.add("cudart64_130");
                    variants.add("cudart64_120");
                    variants.add("cudart64_110");
                }
            } else if (libName.equals("opencl")) {
                variants.add("OpenCL");
            } else if (libName.equals("vlc")) {
                variants.add("libvlc"); 
                variants.add("libvlccore"); 
            } else if (libName.equals("openblas")) {
                variants.add("libopenblas");
            }
        }

        // Common Linux/Universal variants
        if (!isWin) {
            if (libName.equalsIgnoreCase("opencl")) {
                variants.add("OpenCL");
                variants.add("OpenCL.so.1");
            } else if (libName.equals("cuda")) {
                variants.add("cuda");
                variants.add("cuda.so.1");
            } else if (libName.equals("cublas")) {
                variants.add("cublas");
                variants.add("libcublas.so");
                variants.add("libcublas.so.12");
                variants.add("libcublas.so.11");
            } else if (libName.equals("cusolver")) {
                variants.add("cusolver");
                variants.add("libcusolver.so");
                variants.add("libcusolver.so.12");
                variants.add("libcusolver.so.11");
            } else if (libName.equals("cudart")) {
                variants.add("cudart");
                variants.add("libcudart.so");
                variants.add("libcudart.so.12");
                variants.add("libcudart.so.11");
            } else if (libName.equals("openblas")) {
                variants.add("openblas");
                variants.add("libopenblas.so.3");
                variants.add("libopenblas.so.0");
            }
        }

        List<java.nio.file.Path> discoveredPaths = NativeDiscovery.findLibsDirectories();
        for (java.nio.file.Path p : discoveredPaths) {
            preloadRuntimes(p, arena);
        }

        for (String variant : variants) {
            String currentMapped;
            if (!isWin && (variant.contains(".so") || variant.contains(".dylib"))) {
                currentMapped = variant.startsWith("lib") || variant.equals("OpenCL") ? variant : "lib" + variant;
            } else {
                currentMapped = System.mapLibraryName(variant);
            }
            
            System.err.println("[NativeFFMLoader] Attempting bare lookup for variant: " + variant);
            try {
                if (!variant.contains("/") && !variant.contains("\\")) {
                    SymbolLookup lookup = SymbolLookup.libraryLookup(variant, arena);
                    LOADED_LIBS.put(libName, lookup);
                    System.err.println("[NativeFFMLoader] Successfully loaded " + variant + " from system path");
                    return Optional.of(lookup);
                }
            } catch (Throwable t) {
                System.err.println("[NativeFFMLoader] System lookup failed for " + variant + ": " + t.getMessage());
                FAILURE_CAUSES.put(variant, t.toString());
            }

            for (String path : NativeDiscovery.getSearchPaths()) {
                if (path == null || path.isEmpty()) continue; 
                Optional<SymbolLookup> found = tryLoadFromDirectory(java.nio.file.Paths.get(path), currentMapped, arena);
                if (found.isPresent()) {
                    LOADED_LIBS.put(libName, found.get());
                    return found;
                }
            }
        }
        
        FAILED_LIBS.add(libName);
        return Optional.empty();
    }

    private static Optional<SymbolLookup> tryLoadFromDirectory(java.nio.file.Path basePath, String mappedName, Arena arena) {
        try {
            if (!java.nio.file.Files.exists(basePath)) return Optional.empty();
            final java.nio.file.Path fullPath = basePath.resolve(mappedName).toAbsolutePath();
            if (java.nio.file.Files.exists(fullPath)) {
                logger.debug("[NativeFFMLoader] Found file: {}, attempting to load...", fullPath);
                try {
                    if (Boolean.getBoolean("episteme.native.skip." + mappedName)) {
                         logger.warn("[NativeFFMLoader] Skipping library {} as requested by system property", mappedName);
                         return Optional.empty();
                    }
                    logger.info("[NativeFFMLoader] Attempting to load library: {}", fullPath);
                    SymbolLookup lookup = SymbolLookup.libraryLookup(fullPath, arena);
                    logger.info("[NativeFFMLoader] Successfully loaded library: {}", fullPath);
                    return Optional.of(lookup);
                } catch (Throwable t) {
                    logger.error("[NativeFFMLoader] Failed to load library from {}: {}", fullPath, t.getMessage());
                    FAILURE_CAUSES.put(fullPath.toString(), t.toString());
                }
            }
            
            // Try subdirectory "lib" or "bin"
            for (String sub : new String[]{"lib", "bin"}) {
                java.nio.file.Path subPath = basePath.resolve(sub).resolve(mappedName).toAbsolutePath();
                if (java.nio.file.Files.exists(subPath)) {
                    try {
                        return Optional.of(SymbolLookup.libraryLookup(subPath, arena));
                    } catch (Throwable t) {
                        FAILURE_CAUSES.put(subPath.toString(), t.toString());
                    }
                }
            }
        } catch (Throwable ignored) {}
        return Optional.empty();
    }

    public static Linker getLinker() {
        return LINKER;
    }

    public static Optional<MemorySegment> findSymbol(SymbolLookup lookup, String... names) {
        if (lookup == null) return Optional.empty();
        for (String name : names) {
            Optional<MemorySegment> segment = lookup.find(name);
            if (segment.isPresent()) {
                System.err.println("[NativeFFMLoader] Found symbol: " + name);
                return segment;
            }
            
            String[] variants = {"_" + name, name + "_", "__" + name};
            for (String v : variants) {
                segment = lookup.find(v);
                if (segment.isPresent()) {
                    System.err.println("[NativeFFMLoader] Found symbol (variant): " + v);
                    return segment;
                }
            }
        }
        return Optional.empty();
    }

    public static Optional<SymbolLookup> getSystemLookup() {
        return Optional.of(SymbolLookup.loaderLookup());
    }

    public static String getFailureCause(String libName) {
        return FAILURE_CAUSES.getOrDefault(libName, "No recorded error");
    }

    public static void clearCache() {
        FAILED_LIBS.clear();
        FAILURE_CAUSES.clear();
        LOADED_LIBS.clear();
        logger.info("Native library cache cleared.");
    }

    public static void shutdown() {
        clearCache();
        logger.info("NativeFFMLoader shut down.");
    }
}
