/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.technical.backend.nativ;

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
    private static final java.util.Map<String, SymbolLookup> LOADED_LIBS = new java.util.concurrent.ConcurrentHashMap<>();

    /**
     * Finds the "libs" directory by searching upwards and checking module paths.
     */
    public static List<java.nio.file.Path> findLibsDirectories() {
        List<java.nio.file.Path> paths = new ArrayList<>();
        java.nio.file.Path current = java.nio.file.Paths.get(System.getProperty("user.dir"));
        
        // 1. Search upwards for "libs"
        java.nio.file.Path temp = current;
        while (temp != null) {
            java.nio.file.Path libs = temp.resolve("libs");
            if (java.nio.file.Files.exists(libs) && java.nio.file.Files.isDirectory(libs)) {
                paths.add(libs.toAbsolutePath());
            }
            temp = temp.getParent();
        }

        // 2. Check common module libs locations
        String[] modules = {"episteme-native", "episteme-jni", "episteme-core", "episteme-natural"};
        temp = current;
        while (temp != null) {
            for (String mod : modules) {
                java.nio.file.Path modLibs = temp.resolve(mod).resolve("libs");
                if (java.nio.file.Files.exists(modLibs) && java.nio.file.Files.isDirectory(modLibs)) {
                    paths.add(modLibs.toAbsolutePath());
                }
            }
            // If we find pom.xml, we are likely at root, stop or keep going? 
            // Keep going just in case of nested structures.
            temp = temp.getParent();
        }
        
        return paths;
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
        if (LOADED_LIBS.containsKey(libName)) {
            return Optional.of(LOADED_LIBS.get(libName));
        }
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
            } else if (libName.contains("bullet")) {
                variants.add("bullet_capi");
                variants.add("libbullet_capi");
            }
        }

        // Common Linux/Universal variants
        if (!isWin) {
            if (libName.equalsIgnoreCase("opencl")) {
                variants.add("OpenCL");
                variants.add("OpenCL.so.1");
                variants.add("OpenCL.so.2");
                variants.add("nvidia-opencl");
                variants.add("intel-opencl");
            } else if (libName.equals("cuda")) {
                variants.add("cuda");
                variants.add("cuda.so.1");
                variants.add("nvcuda");
            } else if (libName.equals("cublas")) {
                variants.add("cublas");
                variants.add("cublas.so.12");
                variants.add("cublas.so.11");
            } else if (libName.equals("cudart")) {
                variants.add("cudart");
                variants.add("cudart.so.12");
                variants.add("cudart.so.11");
            } else if (libName.contains("bullet")) {
                variants.add("bullet_capi");
                variants.add("bullet");
            }
        }

        List<java.nio.file.Path> discoveredPaths = findLibsDirectories();
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
            
            // 1. Try loading directly via System.loadLibrary (or equivalent)
            try {
                if (!FAILED_VARIANTS.contains(variant)) {
                    String msg = "Probing native library variant: " + variant;
                    logger.debug(msg);
                    System.out.println("[NativeLoader] " + msg);
                }
                
                // CRITICAL FIX: Only use libraryLookup(String) if it's a simple name (no dots/suffixes)
                if (!variant.contains(".") && !variant.contains("/") && !variant.contains("\\")) {
                    SymbolLookup lookup = SymbolLookup.libraryLookup(variant, arena);
                    String msg = "Successfully loaded library variant via system lookup: " + variant;
                    logger.info(msg);
                    System.out.println("[NativeLoader] " + msg);
                    LOADED_LIBS.put(libName, lookup);
                    return Optional.of(lookup);
                }
            } catch (Throwable t) {
                FAILURE_CAUSES.put(variant, t.toString());
            }

            // 2. Try custom search paths
            List<String> searchPaths = new ArrayList<>();
            for (java.nio.file.Path p : discoveredPaths) {
                searchPaths.add(p.toString());
            }
            
            String envCudaPath = System.getenv("CUDA_PATH");
            if (envCudaPath != null) {
                searchPaths.add(envCudaPath + java.io.File.separator + "bin");
                searchPaths.add(envCudaPath + java.io.File.separator + "lib64");
            }

            if (!isWin) {
                searchPaths.add("/usr/lib/x86_64-linux-gnu");
                searchPaths.add("/usr/lib/nvidia");
                searchPaths.add("/usr/local/cuda/lib64");
                searchPaths.add("/usr/local/lib");
            }
            
            searchPaths.add(System.getProperty("user.dir"));
            
            for (String path : searchPaths) {
                if (path == null || path.isEmpty()) continue; 
                Optional<SymbolLookup> found = tryLoadFromDirectory(java.nio.file.Paths.get(path), currentMapped, arena);
                if (found.isPresent()) {
                    LOADED_LIBS.put(libName, found.get());
                    return found;
                }
            }
        }
        
        if (FAILED_LIBS.add(libName)) {
            logger.warn("Could not find or load native library {} (tried variants: {})", libName, variants);
            logger.warn("Failure summary: {}", getFailureSummary(libName, variants));
        }
        return Optional.empty();
    }

    private static String getFailureSummary(String libName, List<String> variants) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n  - Library: ").append(libName);
        sb.append("\n  - Java Path: ").append(System.getProperty("java.library.path"));
        sb.append("\n  - LD_LIBRARY_PATH: ").append(System.getenv("LD_LIBRARY_PATH"));
        sb.append("\n  - Probed Variants Errors:");
        for (String v : variants) {
            sb.append("\n    * ").append(v).append(": ").append(FAILURE_CAUSES.getOrDefault(v, "Not found on disk"));
        }
        return sb.toString();
    }

    private static Optional<SymbolLookup> tryLoadFromDirectory(java.nio.file.Path basePath, String mappedName, Arena arena) {
        try {
            if (!java.nio.file.Files.exists(basePath)) return Optional.empty();
            final java.nio.file.Path fullPath = basePath.resolve(mappedName).toAbsolutePath();
            if (java.nio.file.Files.exists(fullPath)) {
                try {
                    SymbolLookup lookup = SymbolLookup.libraryLookup(fullPath, arena);
                    String msg = "Successfully loaded library from path: " + fullPath;
                    logger.info(msg);
                    System.out.println("[NativeLoader] " + msg);
                    return Optional.of(lookup);
                } catch (Throwable t) {
                    FAILURE_CAUSES.put(fullPath.toString(), t.toString());
                    String msg = "Dependency resolution failed for " + fullPath + ": " + t.getMessage();
                    logger.debug(msg);
                    System.err.println("[NativeLoader] " + msg);
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
            if (segment.isPresent()) return segment;
            
            // Try common suffixes/prefixes
            String[] variants = {"_" + name, name + "_", "__" + name};
            for (String v : variants) {
                segment = lookup.find(v);
                if (segment.isPresent()) return segment;
            }
        }
        return Optional.empty();
    }

    public static Optional<SymbolLookup> getSystemLookup() {
        return Optional.of(SymbolLookup.loaderLookup());
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
        FAILURE_CAUSES.clear();
        logger.info("Failure cache cleared.");
    }

    /**
     * Shuts down the loader, clearing all caches and references.
     */
    public static void shutdown() {
        LOADED_LIBS.clear();
        clearCache();
        logger.info("NativeLibraryLoader shut down.");
    }
}
