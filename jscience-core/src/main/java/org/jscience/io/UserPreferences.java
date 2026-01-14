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
}
