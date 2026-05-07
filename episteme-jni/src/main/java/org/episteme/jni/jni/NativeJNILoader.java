/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.jni.jni;

import org.episteme.core.technical.backend.nativ.NativeDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.List;

/**
 * Robust JNI library loader (FFM-free, JDK 21 compatible).
 * <p>
 * Uses standard System.load instead of Panama SymbolLookup to avoid preview API conflicts.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class NativeJNILoader {

    private static final Logger logger = LoggerFactory.getLogger(NativeJNILoader.class);
    private static final java.util.Set<String> LOADED_LIBS = java.util.concurrent.ConcurrentHashMap.newKeySet();

    /**
     * Loads a native library using standard JNI mechanisms.
     * 
     * @param libName The library name (e.g. "episteme-jni").
     */
    public static void loadLibrary(String libName) {
        if (LOADED_LIBS.contains(libName)) return;

        // 1. Try standard system load
        try {
            System.loadLibrary(libName);
            LOADED_LIBS.add(libName);
            logger.info("Successfully loaded library via System.loadLibrary: {}", libName);
            return;
        } catch (UnsatisfiedLinkError e) {
            logger.debug("System.loadLibrary failed for {}, trying custom paths...", libName);
        }

        // 2. Try custom discovery paths
        String mappedName = System.mapLibraryName(libName);
        List<String> searchPaths = NativeDiscovery.getSearchPaths();
        
        for (String pathStr : searchPaths) {
            Path path = Paths.get(pathStr).resolve(mappedName).toAbsolutePath();
            if (Files.exists(path)) {
                try {
                    System.load(path.toString());
                    LOADED_LIBS.add(libName);
                    logger.info("Successfully loaded library from path: {}", path);
                    return;
                } catch (UnsatisfiedLinkError e) {
                    logger.warn("Failed to load library from {}: {}", path, e.getMessage());
                }
            }
            
            // Try subdirectories
            for (String sub : new String[]{"lib", "bin"}) {
                Path subPath = Paths.get(pathStr).resolve(sub).resolve(mappedName).toAbsolutePath();
                if (Files.exists(subPath)) {
                    try {
                        System.load(subPath.toString());
                        LOADED_LIBS.add(libName);
                        logger.info("Successfully loaded library from sub-path: {}", subPath);
                        return;
                    } catch (UnsatisfiedLinkError e) {
                         logger.debug("Failed to load library from {}: {}", subPath, e.getMessage());
                    }
                }
            }
        }

        throw new RuntimeException("Could not load native library: " + libName + ". Check your NATIVE_LIBS_SETUP.md and library paths.");
    }
}
