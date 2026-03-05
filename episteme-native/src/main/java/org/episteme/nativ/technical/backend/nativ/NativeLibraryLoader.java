/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.technical.backend.nativ;

import java.lang.foreign.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
public class NativeLibraryLoader {

    private static final Logger logger = LoggerFactory.getLogger(NativeLibraryLoader.class);
    private static final Linker LINKER = Linker.nativeLinker();
    private static final java.util.Set<String> FAILED_LIBS = java.util.concurrent.ConcurrentHashMap.newKeySet();
    private static final java.util.Set<String> FAILED_VARIANTS = java.util.concurrent.ConcurrentHashMap.newKeySet();
    private static final java.util.Map<String, String> FAILURE_CAUSES = new java.util.concurrent.ConcurrentHashMap<>();

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

        // Common Linux/Universal variants
        if (!isWin) {
            if (libName.equalsIgnoreCase("opencl")) {
                variants.add("OpenCL");
                variants.add("OpenCL.so.1");
                variants.add("OpenCL.so.2");
                variants.add("OpenCL.so.1.2.0");
            } else if (libName.equals("cuda")) {
                variants.add("cuda");
                variants.add("cuda.so.1");
            } else if (libName.equals("cublas")) {
                variants.add("cublas");
                variants.add("cublas.so.12");
                variants.add("cublas.so.11");
                variants.add("cublas.so.10");
            } else if (libName.equals("cusolver")) {
                variants.add("cusolver");
                variants.add("cusolver.so.11");
                variants.add("cusolver.so.10");
            } else if (libName.equals("cudart")) {
                variants.add("cudart");
                variants.add("cudart.so.12");
                variants.add("cudart.so.11");
            }
        }

        java.nio.file.Path discoveredLibs = findLibsDirectory();
        if (discoveredLibs != null) {
            preloadRuntimes(discoveredLibs, arena);
        }

        for (String variant : variants) {
            String currentMapped = System.mapLibraryName(variant);
            
            // 1. Try loading directly via System.loadLibrary (or equivalent)
            try {
                logger.info("Probing native library variant: " + variant);
                // Force pre-loading of runtime dependencies before loading the main library
                preloadRuntimes(discoveredLibs, arena);
                SymbolLookup lookup = SymbolLookup.libraryLookup(variant, arena);
                logger.info("Successfully loaded library variant: " + variant);
                return Optional.of(lookup);
            } catch (Exception e) {
                FAILURE_CAUSES.put(variant, e.toString());
                if (FAILED_VARIANTS.add(variant)) {
                    logger.warn("Native load failed for " + variant + ": " + e.getMessage());
                }
            }

            // 2. Try custom search paths
            String cudaPath = System.getenv("CUDA_PATH");
            List<String> searchPaths = new ArrayList<>();
            searchPaths.add("/usr/local/lib/");
            searchPaths.add("/usr/lib/");
            searchPaths.add("C:\\Windows\\System32\\");
            
            if (System.getenv("NATIVE_ROOT") != null) searchPaths.add(System.getenv("NATIVE_ROOT"));
            if (cudaPath != null) {
                searchPaths.add(cudaPath + java.io.File.separator + "bin");
                searchPaths.add(cudaPath + java.io.File.separator + "lib64");
                searchPaths.add(cudaPath + java.io.File.separator + "lib");
            }
            
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
                            logger.info("Successfully loaded {} from subdirectory: {}", variant, sub.getName());
                            return found;
                        } else {
                            if (FAILED_VARIANTS.add(variant + "@" + sub.getName())) {
                                logger.trace("Library '{}' could not be loaded from subdirectory: {}", variant, sub.getName());
                            }
                        }
                    }
                }
            }
        }
        
        if (FAILED_LIBS.add(libName)) {
            logger.warn("Could not find or load native library {} (tried variants: {})", libName, variants);
        }
        return Optional.empty();
    }

    private static Optional<SymbolLookup> tryLoadFromDirectory(java.nio.file.Path basePath, String mappedName, Arena arena) {
        java.nio.file.Path logPath = basePath;
        try {
            final java.nio.file.Path fullPath = basePath.resolve(mappedName).toAbsolutePath();
            logPath = fullPath;
            if (java.nio.file.Files.exists(fullPath)) {
                try {
                    // Before loading the target library, try loading ALL other libraries in the same directory 
                    // as they might be runtime dependencies (like libstdc++-6.dll, .so, etc)
                    try (java.util.stream.Stream<java.nio.file.Path> siblingFiles = java.nio.file.Files.list(basePath)) {
                        siblingFiles.filter(p -> {
                            String name = p.toString().toLowerCase();
                            return (name.endsWith(".dll") || name.endsWith(".so") || name.endsWith(".dylib")) && !p.equals(fullPath);
                        })
                                    .forEach(p -> {
                                        try { 
                                            SymbolLookup.libraryLookup(p, arena); 
                                        } catch (Throwable ignored) {} 
                                    });
                    }
                    return Optional.of(SymbolLookup.libraryLookup(fullPath, arena));
                } catch (Throwable t) {
                    if (FAILED_VARIANTS.add(fullPath.toString())) {
                        logger.debug("Failed to load native library at {}: {}", fullPath, t.getMessage());
                    }
                }
            }
            
            String[] subs = {"lib", "bin"};
            for (String sub : subs) {
                java.nio.file.Path subPath = basePath.resolve(sub).resolve(mappedName).toAbsolutePath();
                if (java.nio.file.Files.exists(subPath)) {
                    return Optional.of(SymbolLookup.libraryLookup(subPath, arena));
                }
            }
        } catch (Throwable t) {
            if (FAILED_VARIANTS.add(mappedName + "@" + logPath)) {
                logger.trace("Failed to load {} from {}: {}", mappedName, logPath, t.getMessage());
            }
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

    /**
     * Returns the last recorded failure cause for a library or variant.
     */
    public static String getFailureCause(String libName) {
        return FAILURE_CAUSES.getOrDefault(libName, "No recorded error");
    }

    public static List<String> getAllFailureCauses() {
        List<String> results = new ArrayList<>();
        FAILURE_CAUSES.forEach((k, v) -> results.add(k + ": " + v));
        return results;
    }

    public static void clearCache() {
        FAILED_LIBS.clear();
        FAILED_VARIANTS.clear();
        logger.info("Failure cache cleared.");
    }
}
