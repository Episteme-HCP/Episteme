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
        }
        LINKER = temp;
    }

    public static List<java.nio.file.Path> findLibsDirectories() {
        List<java.nio.file.Path> paths = new ArrayList<>();
        java.nio.file.Path current = java.nio.file.Paths.get(System.getProperty("user.dir"));
        
        java.nio.file.Path temp = current;
        while (temp != null) {
            java.nio.file.Path libs = temp.resolve("libs");
            if (java.nio.file.Files.exists(libs) && java.nio.file.Files.isDirectory(libs)) {
                paths.add(libs.toAbsolutePath());
            }
            temp = temp.getParent();
        }

        String[] modules = {"episteme-native", "episteme-jni", "episteme-core", "episteme-natural"};
        temp = current;
        while (temp != null) {
            for (String mod : modules) {
                java.nio.file.Path modLibs = temp.resolve(mod).resolve("libs");
                if (java.nio.file.Files.exists(modLibs) && java.nio.file.Files.isDirectory(modLibs)) {
                    paths.add(modLibs.toAbsolutePath());
                }
            }
            temp = temp.getParent();
        }
        
        return paths;
    }

    private static final java.util.Set<String> PRELOADED_RUNTIMES = new java.util.HashSet<>();

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

    public static SymbolLookup loadLibrary(String libName) {
        return loadLibrary(libName, Arena.global()).orElseThrow(() -> 
            new RuntimeException("Could not load native library: " + libName));
    }

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
            if (libName.equals("cuda")) variants.add("nvcuda");
            else if (libName.startsWith("cu")) {
                variants.add(libName + "64_13");
                variants.add(libName + "64_12");
                variants.add(libName + "64_11");
            } else if (libName.equals("vlc")) {
                variants.add("libvlc"); 
                variants.add("libvlccore"); 
            } else if (libName.equals("openblas")) {
                variants.add("libopenblas");
            }
        }

        List<java.nio.file.Path> discoveredPaths = findLibsDirectories();
        for (java.nio.file.Path p : discoveredPaths) {
            preloadRuntimes(p, arena);
        }

        for (String variant : variants) {
            String currentMapped;
            if (!isWin && (variant.contains(".so") || variant.contains(".dylib"))) {
                currentMapped = variant.startsWith("lib") ? variant : "lib" + variant;
            } else {
                currentMapped = System.mapLibraryName(variant);
            }
            
            try {
                if (!variant.contains(".") && !variant.contains("/") && !variant.contains("\\")) {
                    SymbolLookup lookup = SymbolLookup.libraryLookup(variant, arena);
                    LOADED_LIBS.put(libName, lookup);
                    return Optional.of(lookup);
                }
            } catch (Throwable t) {
                FAILURE_CAUSES.put(variant, t.toString());
            }

            List<String> searchPaths = new ArrayList<>();
            for (java.nio.file.Path p : discoveredPaths) searchPaths.add(p.toString());
            
            if (!isWin) {
                searchPaths.add("/usr/lib/x86_64-linux-gnu");
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

        FAILED_LIBS.add(libName);
        return Optional.empty();
    }
        
    public static void clearCache() {
        LOADED_LIBS.clear();
        FAILED_LIBS.clear();
        FAILURE_CAUSES.clear();
        logger.info("Native library cache cleared.");
    }

    public static String getFailureCause(String libName) {
        return FAILURE_CAUSES.getOrDefault(libName, "No failure recorded for " + libName);
    }

    public static List<String> getAllFailureCauses() {
        List<String> causes = new ArrayList<>();
        FAILURE_CAUSES.forEach((lib, cause) -> causes.add(lib + ": " + cause));
        if (FAILED_LIBS.isEmpty() && FAILURE_CAUSES.isEmpty()) {
            return causes;
        }
        for (String lib : FAILED_LIBS) {
            if (!FAILURE_CAUSES.containsKey(lib)) {
                causes.add(lib + ": Generic failure (see logs)");
            }
        }
        return causes;
    }

    public static void shutdown() {
        clearCache();
    }

    private static Optional<SymbolLookup> tryLoadFromDirectory(java.nio.file.Path basePath, String mappedName, Arena arena) {
        try {
            if (!java.nio.file.Files.exists(basePath)) return Optional.empty();
            final java.nio.file.Path fullPath = basePath.resolve(mappedName).toAbsolutePath();
            if (java.nio.file.Files.exists(fullPath)) {
                try {
                    return Optional.of(SymbolLookup.libraryLookup(fullPath, arena));
                } catch (Throwable t) {
                    FAILURE_CAUSES.put(fullPath.toString(), t.toString());
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
        }
        return Optional.empty();
    }

    public static Optional<SymbolLookup> getSystemLookup() {
        return Optional.of(SymbolLookup.loaderLookup());
    }
}
