/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.io;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;

/**
 * User preferences manager with file-based persistence.
 * Stores user preferences in the user's home directory.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class UserPreferences {

    private static final String PREFS_DIR = ".jscience";
    private static final String PREFS_FILE = "preferences.properties";
    
    // Backend preference keys
    public static final String KEY_MAP_BACKEND = "backend.map";
    public static final String KEY_NETWORK_BACKEND = "backend.network";
    public static final String KEY_PLOTTING_BACKEND = "backend.plotting";
    public static final String KEY_MOLECULAR_BACKEND = "backend.molecular";
    public static final String KEY_QUANTUM_BACKEND = "backend.quantum";
    public static final String KEY_MATH_BACKEND = "backend.math";
    
    // UI preference keys
    public static final String KEY_LANGUAGE = "ui.language";
    public static final String KEY_THEME = "ui.theme";
    
    // Compute preference keys
    public static final String KEY_COMPUTE_BACKEND = "compute.backend";
    public static final String KEY_COMPUTE_THREADS = "compute.threads";
    public static final String KEY_COMPUTE_GPU_ENABLED = "compute.gpu.enabled";
    public static final String KEY_COMPUTE_OPENCL_DEVICE = "compute.opencl.device";
    public static final String KEY_COMPUTE_CUDA_DEVICE = "compute.cuda.device";
    
    // Default values
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String DEFAULT_THEME = "dark";
    public static final String DEFAULT_COMPUTE_BACKEND = "cpu";
    
    private static UserPreferences instance;
    private final Properties properties;
    private final Path prefsPath;

    private UserPreferences() {
        properties = new Properties();
        Path userHome = Paths.get(System.getProperty("user.home"));
        Path prefsDir = userHome.resolve(PREFS_DIR);
        prefsPath = prefsDir.resolve(PREFS_FILE);
        
        // Create directory if needed
        try {
            Files.createDirectories(prefsDir);
        } catch (IOException e) {
            System.err.println("Could not create preferences directory: " + e.getMessage());
        }
        
        load();
    }

    public static synchronized UserPreferences getInstance() {
        if (instance == null) {
            instance = new UserPreferences();
        }
        return instance;
    }

    /**
     * Gets a preference value.
     */
    public String get(String key) {
        return properties.getProperty(key);
    }

    /**
     * Gets a preference value with default.
     */
    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Sets a preference value and saves immediately.
     */
    public void set(String key, String value) {
        properties.setProperty(key, value);
        save();
    }

    /**
     * Gets the preferred backend for a given type.
     */
    public String getPreferredBackend(String backendType) {
        return get("backend." + backendType);
    }

    /**
     * Sets the preferred backend for a given type.
     */
    public void setPreferredBackend(String backendType, String backendId) {
        set("backend." + backendType, backendId);
    }

    /**
     * Loads preferences from file.
     */
    private void load() {
        if (Files.exists(prefsPath)) {
            try (InputStream is = Files.newInputStream(prefsPath)) {
                properties.load(is);
            } catch (IOException e) {
                System.err.println("Could not load preferences: " + e.getMessage());
            }
        }
    }

    /**
     * Saves preferences to file.
     */
    public void save() {
        try (OutputStream os = Files.newOutputStream(prefsPath)) {
            properties.store(os, "JScience User Preferences");
        } catch (IOException e) {
            System.err.println("Could not save preferences: " + e.getMessage());
        }
    }

    /**
     * Returns the path to the preferences file.
     */
    public Path getPreferencesPath() {
        return prefsPath;
    }

    // === Convenience methods for common preferences ===

    /** Gets the UI language (default: en). */
    public String getLanguage() {
        return get(KEY_LANGUAGE, DEFAULT_LANGUAGE);
    }

    /** Sets the UI language. */
    public void setLanguage(String language) {
        set(KEY_LANGUAGE, language);
    }

    /** Gets the UI theme (default: dark). */
    public String getTheme() {
        return get(KEY_THEME, DEFAULT_THEME);
    }

    /** Sets the UI theme. */
    public void setTheme(String theme) {
        set(KEY_THEME, theme);
    }

    /** Gets the compute backend (cpu, opencl, cuda). */
    public String getComputeBackend() {
        return get(KEY_COMPUTE_BACKEND, DEFAULT_COMPUTE_BACKEND);
    }

    /** Sets the compute backend. */
    public void setComputeBackend(String backend) {
        set(KEY_COMPUTE_BACKEND, backend);
    }

    /** Gets the number of compute threads. */
    public int getComputeThreads() {
        String val = get(KEY_COMPUTE_THREADS);
        if (val == null) return Runtime.getRuntime().availableProcessors();
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return Runtime.getRuntime().availableProcessors();
        }
    }

    /** Sets the number of compute threads. */
    public void setComputeThreads(int threads) {
        set(KEY_COMPUTE_THREADS, String.valueOf(threads));
    }

    /** Gets whether GPU compute is enabled. */
    public boolean isGpuEnabled() {
        return "true".equalsIgnoreCase(get(KEY_COMPUTE_GPU_ENABLED, "false"));
    }

    /** Sets whether GPU compute is enabled. */
    public void setGpuEnabled(boolean enabled) {
        set(KEY_COMPUTE_GPU_ENABLED, String.valueOf(enabled));
    }
}
