/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.technical.backend.nativ;

import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

/**
 * Platform-independent utility for discovering native library directories.
 * <p>
 * This class is strictly JDK 21 compatible and does NOT use any preview APIs (like FFM),
 * allowing it to serve as a shared utility for all modules without versioning conflicts.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class NativeDiscovery {

    /**
     * Finds the "libs" directory by searching upwards and checking module paths.
     * 
     * @return A list of paths where native libraries might be located.
     */
    public static List<Path> findLibsDirectories() {
        List<Path> paths = new ArrayList<>();
        Path current = Paths.get(System.getProperty("user.dir"));
        
        // 1. Search upwards for "libs"
        Path temp = current;
        while (temp != null) {
            Path libs = temp.resolve("libs");
            if (Files.exists(libs) && Files.isDirectory(libs)) {
                paths.add(libs.toAbsolutePath());
            }
            temp = temp.getParent();
        }

        // 2. Check common module libs locations
        String[] modules = {"episteme-native", "episteme-jni", "episteme-core", "episteme-natural"};
        temp = current;
        while (temp != null) {
            for (String mod : modules) {
                Path modLibs = temp.resolve(mod).resolve("libs");
                if (Files.exists(modLibs) && Files.isDirectory(modLibs)) {
                    paths.add(modLibs.toAbsolutePath());
                }
            }
            temp = temp.getParent();
        }
        
        return paths;
    }

    /**
     * Resolves the environment-specific library path list, including standard system paths.
     * 
     * @return A list of search paths.
     */
    public static List<String> getSearchPaths() {
        List<String> searchPaths = new ArrayList<>();
        List<Path> discovered = findLibsDirectories();
        for (Path p : discovered) {
            searchPaths.add(p.toString());
        }
        
        String os = System.getProperty("os.name", "").toLowerCase();
        boolean isWin = os.contains("win");

        String envCudaPath = System.getenv("CUDA_PATH");
        if (envCudaPath != null) {
            searchPaths.add(envCudaPath + java.io.File.separator + "bin");
            searchPaths.add(envCudaPath + java.io.File.separator + "bin" + java.io.File.separator + "x64");
            searchPaths.add(envCudaPath + java.io.File.separator + "lib64");
        }

        if (!isWin) {
            searchPaths.add("/usr/lib/x86_64-linux-gnu");
            searchPaths.add("/usr/lib/nvidia");
            searchPaths.add("/usr/local/cuda/lib64");
            searchPaths.add("/usr/local/lib");
        }
        
        searchPaths.add(System.getProperty("user.dir"));
        return searchPaths;
    }
}
